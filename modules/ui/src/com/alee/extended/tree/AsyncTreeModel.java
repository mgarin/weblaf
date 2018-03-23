/*
 * This file is part of WebLookAndFeel library.
 *
 * WebLookAndFeel library is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * WebLookAndFeel library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with WebLookAndFeel library.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.alee.extended.tree;

import com.alee.laf.WebLookAndFeel;
import com.alee.laf.tree.TreeState;
import com.alee.laf.tree.WebTreeModel;
import com.alee.utils.CollectionUtils;
import com.alee.utils.CoreSwingUtils;
import com.alee.utils.compare.Filter;

import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;
import java.util.*;

/**
 * {@link WebTreeModel} extension that is based on data from {@link AsyncTreeDataProvider}.
 * It allows tree to load its data asynchronously but only supports sorting and filtering of loaded nodes.
 *
 * @param <N> node type
 * @author Mikle Garin
 * @see WebAsyncTree
 * @see AsyncTreeDataProvider
 */

public class AsyncTreeModel<N extends AsyncUniqueNode> extends WebTreeModel<N>
{
    /**
     * todo 1. Add {@link AsyncTreeDataUpdater} support
     */

    /**
     * Asynchronous tree that uses this model.
     */
    protected final WebAsyncTree<N> tree;

    /**
     * Asynchronous tree data provider.
     */
    protected final AsyncTreeDataProvider<N> dataProvider;

    /**
     * Whether to load children asynchronously or not.
     */
    protected boolean asyncLoading;

    /**
     * Root node cache.
     * Cached when root is requested for the first time.
     */
    protected N rootNode;

    /**
     * Asynchronous tree listeners.
     */
    protected transient final List<AsyncTreeModelListener> asyncTreeModelListeners = new ArrayList<AsyncTreeModelListener> ( 1 );

    /**
     * Nodes cached states (parent ID -&gt; children cached state).
     * If child nodes for some parent node are cached then this map contains "true" value under that parent node ID as a key.
     */
    protected transient final Map<String, Boolean> nodeCached = new HashMap<String, Boolean> ();

    /**
     * Cache for children nodes returned by data provider (parent ID -&gt; list of raw child nodes).
     * This map contains raw children which weren't affected by sorting and filtering operations.
     * If children needs to be re-sorted or re-filtered they are simply taken from the cache and re-organized once again.
     */
    protected transient final Map<String, List<N>> rawNodeChildrenCache = new HashMap<String, List<N>> ();

    /**
     * Direct nodes cache (node ID -&gt; node).
     * Used for quick node search within the tree.
     */
    protected transient final Map<String, N> nodeById = new HashMap<String, N> ();

    /**
     * Constructs default asynchronous tree model using custom data provider.
     *
     * @param tree         asynchronous tree
     * @param dataProvider data provider
     */
    public AsyncTreeModel ( final WebAsyncTree<N> tree, final AsyncTreeDataProvider<N> dataProvider )
    {
        super ( null );
        this.tree = tree;
        this.dataProvider = dataProvider;
        this.asyncLoading = true;
        this.rootNode = null;
    }

    /**
     * Returns whether children are loaded asynchronously or not.
     *
     * @return {@code true} if children are loaded asynchronously, {@code false} otherwise
     */
    public boolean isAsyncLoading ()
    {
        return asyncLoading;
    }

    /**
     * Sets whether to load children asynchronously or not.
     *
     * @param asyncLoading whether to load children asynchronously or not
     */
    public void setAsyncLoading ( final boolean asyncLoading )
    {
        // Event Dispatch Thread check
        WebLookAndFeel.checkEventDispatchThread ();

        // Updating parameter
        this.asyncLoading = asyncLoading;
    }

    /**
     * Returns asynchronous tree data provider.
     *
     * @return data provider
     */
    public AsyncTreeDataProvider<N> getDataProvider ()
    {
        return dataProvider;
    }

    @Override
    public N getRoot ()
    {
        // Event Dispatch Thread check
        WebLookAndFeel.checkEventDispatchThread ();

        // Creating root node if needed
        if ( rootNode == null )
        {
            // Retrieving and caching root node
            rootNode = dataProvider.getRoot ();

            // Caching root node by ID
            cacheNodeById ( rootNode );

            // Adding image observer
            rootNode.attachLoadIconObserver ( tree );
        }

        return rootNode;
    }

    @Override
    public boolean isLeaf ( final Object node )
    {
        // Event Dispatch Thread check
        WebLookAndFeel.checkEventDispatchThread ();

        // Redirecting check to provider
        return dataProvider.isLeaf ( ( N ) node );
    }

    /**
     * todo Generally speaking children loading shouldn't be invoked by this method as it might be a problem
     * todo There should be a separate UI for async tree where we would listen for nodes expansion and perform loading and updates
     */
    @Override
    public int getChildCount ( final Object parent )
    {
        // Event Dispatch Thread check
        WebLookAndFeel.checkEventDispatchThread ();

        // Counting child nodes
        final int count;
        final N node = ( N ) parent;
        if ( areChildrenLoaded ( node ) )
        {
            // Simply using nodes count
            count = super.getChildCount ( parent );
        }
        else if ( isLeaf ( node ) )
        {
            // Caching empty children and returning zero
            count = loadEmptyChildren ( node );
        }
        else
        {
            // Loading children and returning count right away if possible
            count = loadChildren ( node );
        }
        return count;
    }

    @Override
    public N getChild ( final Object parent, final int index )
    {
        // Event Dispatch Thread check
        WebLookAndFeel.checkEventDispatchThread ();

        final N node = ( N ) parent;
        if ( areChildrenLoaded ( node ) )
        {
            return ( N ) super.getChild ( parent, index );
        }
        else
        {
            return null;
        }
    }

    @Override
    public void reload ( final TreeNode node )
    {
        // Event Dispatch Thread check
        WebLookAndFeel.checkEventDispatchThread ();

        // Cancels tree editing
        tree.cancelEditing ();

        // Cleaning up nodes cache
        clearNodeChildrenCache ( ( N ) node, false );

        // Forcing children reload
        super.reload ( node );
    }

    /**
     * Loads empty node children.
     * It is called for any node that {@link #isLeaf(Object)} has returned {@code true}.
     * This is a small workaround to avoid {@link #loadChildren(AsyncUniqueNode)} call upon child nodes insert into empty parent node.
     *
     * @param parent node to load empty children for
     * @return {@code 0} children count
     */
    protected int loadEmptyChildren ( final N parent )
    {
        // Event Dispatch Thread check
        WebLookAndFeel.checkEventDispatchThread ();

        // Caching empty raw children
        rawNodeChildrenCache.put ( parent.getId (), new ArrayList<N> ( 0 ) );

        // Updatng cache
        nodeCached.put ( parent.getId (), true );

        // Updating parent node load state
        parent.setState ( AsyncNodeState.loaded );

        // Always return zero children count
        return 0;
    }

    /**
     * Loads (or reloads) node children and returns zero or children count if async mode is off.
     * This is base method that uses installed {@link AsyncTreeDataProvider} to retrieve tree node children.
     *
     * @param parent node to load children for
     * @return zero or children count if async mode is off
     * @see AsyncTreeDataProvider#loadChildren(AsyncUniqueNode, NodesLoadCallback)
     */
    protected int loadChildren ( final N parent )
    {
        // Event Dispatch Thread check
        WebLookAndFeel.checkEventDispatchThread ();

        // Ensure that we aren't already loading children
        if ( !parent.isLoading () )
        {
            // Updating parent node load state
            parent.setState ( AsyncNodeState.loading );
            nodeChanged ( parent );

            // Firing load started event
            fireChildrenLoadStarted ( parent );

            // todo This should actually be called on node reload?
            // Removing all old children if such exist
            final int childCount = parent.getChildCount ();
            if ( childCount > 0 )
            {
                final int[] indices = new int[ childCount ];
                final Object[] children = new Object[ childCount ];
                for ( int i = childCount - 1; i >= 0; i-- )
                {
                    indices[ i ] = i;
                    children[ i ] = parent.getChildAt ( i );
                    parent.remove ( i );
                }
                nodesWereRemoved ( parent, indices, children );
            }

            // Loading node children
            if ( isAsyncLoading () )
            {
                // Executing children load in a separate thread to avoid locking EDT
                // This queue will also take care of amount of threads to execute async trees requests
                AsyncTreeQueue.getInstance ( tree ).execute ( new Runnable ()
                {
                    @Override
                    public void run ()
                    {
                        // Loading children
                        dataProvider.loadChildren ( parent, new NodesLoadCallback<N> ()
                        {
                            @Override
                            public void completed ( final List<N> children )
                            {
                                CoreSwingUtils.invokeLater ( new Runnable ()
                                {
                                    @Override
                                    public void run ()
                                    {
                                        // Finishing children loading
                                        loadChildrenCompleted ( parent, children );
                                    }
                                } );
                            }

                            @Override
                            public void failed ( final Throwable cause )
                            {
                                CoreSwingUtils.invokeLater ( new Runnable ()
                                {
                                    @Override
                                    public void run ()
                                    {
                                        // Canceling children loading
                                        loadChildrenFailed ( parent, cause );
                                    }
                                } );
                            }
                        } );
                    }
                } );
                return 0;
            }
            else
            {
                // Loading children
                dataProvider.loadChildren ( parent, new NodesLoadCallback<N> ()
                {
                    @Override
                    public void completed ( final List<N> children )
                    {
                        // Event Dispatch Thread check
                        WebLookAndFeel.checkEventDispatchThread ();

                        // Finishing children loading
                        loadChildrenCompleted ( parent, children );
                    }

                    @Override
                    public void failed ( final Throwable cause )
                    {
                        // Event Dispatch Thread check
                        WebLookAndFeel.checkEventDispatchThread ();

                        // Canceling children loading
                        loadChildrenFailed ( parent, cause );
                    }
                } );
                return parent.getChildCount ();
            }
        }
        else
        {
            // Simply return zero if we are still loading children
            return 0;
        }
    }

    /**
     * Finishes children loading for the specified parent.
     *
     * @param parent   parent node
     * @param children loaded child nodes
     */
    protected void loadChildrenCompleted ( final N parent, final List<N> children )
    {
        // Caching raw children
        rawNodeChildrenCache.put ( parent.getId (), children );
        cacheNodesById ( children );

        // Filtering and sorting raw children
        final List<N> realChildren = filterAndSort ( parent, children );

        // Updating cache
        nodeCached.put ( parent.getId (), true );

        // Checking if any nodes loaded
        if ( realChildren != null && realChildren.size () > 0 )
        {
            // Inserting loaded nodes
            insertNodesIntoImpl ( realChildren, parent, 0 );
        }

        // Updating parent node load state
        parent.setState ( AsyncNodeState.loaded );
        nodeChanged ( parent );

        // Firing load completed event
        fireChildrenLoadCompleted ( parent, realChildren );
    }

    /**
     * Cancells children loading upon failure.
     *
     * @param parent parent node
     * @param cause  failure cause
     */
    protected void loadChildrenFailed ( final N parent, final Throwable cause )
    {
        // Caching children
        rawNodeChildrenCache.put ( parent.getId (), new ArrayList<N> ( 0 ) );
        nodeCached.put ( parent.getId (), true );

        // Updating parent node load state
        parent.setState ( AsyncNodeState.failed );
        parent.setFailureCause ( cause );
        nodeChanged ( parent );

        // Firing load failed event
        fireChildrenLoadFailed ( parent, cause );
    }

    /**
     * Replaces child nodes for the specified node.
     * This method might be used to manually change tree node children without causing any structure corruptions.
     *
     * @param parent   node to process
     * @param children new node children
     */
    public void setChildNodes ( final N parent, final List<N> children )
    {
        // Event Dispatch Thread check
        WebLookAndFeel.checkEventDispatchThread ();

        // Ensure that node is not busy right now
        // Although unlike other methods we don't care if node children are loaded or not since we are replacing them
        if ( !parent.isLoading () )
        {
            // Caching raw children
            rawNodeChildrenCache.put ( parent.getId (), children );
            cacheNodesById ( children );

            // Filtering and sorting raw children
            final List<N> realChildren = filterAndSort ( parent, children );

            // Updating cache
            nodeCached.put ( parent.getId (), true );

            // Checking if any nodes loaded
            if ( realChildren != null && realChildren.size () > 0 )
            {
                // Clearing raw nodes cache
                // That might be required in case nodes were moved inside of the tree
                clearNodeChildrenCache ( children, false );

                // Inserting nodes
                insertNodesIntoImpl ( realChildren, parent, 0 );
            }

            // Updating parent node load state
            parent.setState ( AsyncNodeState.loaded );
            nodeChanged ( parent );

            // Firing load completed event
            fireChildrenLoadCompleted ( parent, realChildren );
        }
    }

    /**
     * Adds child node for the specified node.
     * This method might be used to manually change tree node children without causing any structure corruptions.
     *
     * @param parent node to process
     * @param child  new child node
     */
    public void addChildNode ( final N parent, final N child )
    {
        addChildNodes ( parent, CollectionUtils.asList ( child ) );
    }

    /**
     * Adds child nodes for the specified node.
     * This method might be used to manually change tree node children without causing any structure corruptions.
     *
     * @param parent   node to process
     * @param children new node children
     */
    public void addChildNodes ( final N parent, final List<N> children )
    {
        // Event Dispatch Thread check
        WebLookAndFeel.checkEventDispatchThread ();

        // Ensure node children are loaded
        if ( parent.isLoaded () )
        {
            // Adding new raw children
            List<N> cachedChildren = rawNodeChildrenCache.get ( parent.getId () );
            if ( cachedChildren == null )
            {
                cachedChildren = new ArrayList<N> ( children.size () );
                rawNodeChildrenCache.put ( parent.getId (), cachedChildren );
            }
            cachedChildren.addAll ( children );
            cacheNodesById ( children );

            // Clearing nodes cache
            // That might be required in case nodes were moved inside of the tree
            clearNodeChildrenCache ( children, false );

            // Inserting nodes
            insertNodesIntoImpl ( children, parent, parent.getChildCount () );

            // Updating parent node sorting and filtering
            filterAndSort ( parent, false );
        }
    }

    /**
     * Inserts new child node into parent node at the specified index.
     *
     * @param child  new child node
     * @param parent parent node
     * @param index  insert index
     */
    @Override
    public void insertNodeInto ( final MutableTreeNode child, final MutableTreeNode parent, final int index )
    {
        // Event Dispatch Thread check
        WebLookAndFeel.checkEventDispatchThread ();

        final N parentNode = ( N ) parent;
        final N childNode = ( N ) child;

        // Ensure node children are loaded
        if ( parentNode.isLoaded () )
        {
            // Inserting new raw children
            List<N> cachedChildren = rawNodeChildrenCache.get ( parentNode.getId () );
            if ( cachedChildren == null )
            {
                cachedChildren = new ArrayList<N> ( 1 );
                rawNodeChildrenCache.put ( parentNode.getId (), cachedChildren );
            }
            cachedChildren.add ( index, childNode );
            cacheNodeById ( childNode );

            // Clearing node cache
            // That might be required in case nodes were moved inside of the tree
            clearNodeChildrenCache ( childNode, false );

            // Inserting node
            insertNodeIntoImpl ( childNode, parentNode, index );

            // Updating parent node sorting and filtering
            filterAndSort ( parentNode, false );
        }
    }

    /**
     * Inserts a child node into parent node.
     *
     * @param child  new child node
     * @param parent parent node
     * @param index  insert index
     */
    protected void insertNodeIntoImpl ( final N child, final N parent, final int index )
    {
        super.insertNodeInto ( child, parent, index );

        // Adding image observers
        child.attachLoadIconObserver ( tree );
    }

    /**
     * Inserts a list of child nodes into parent node.
     *
     * @param children list of new child nodes
     * @param parent   parent node
     * @param index    insert index
     */
    @Override
    public void insertNodesInto ( final List<N> children, final N parent, final int index )
    {
        // Event Dispatch Thread check
        WebLookAndFeel.checkEventDispatchThread ();

        // Ensure node children are loaded
        if ( parent.isLoaded () )
        {
            // Inserting new raw children
            List<N> cachedChildren = rawNodeChildrenCache.get ( parent.getId () );
            if ( cachedChildren == null )
            {
                cachedChildren = new ArrayList<N> ( 1 );
                rawNodeChildrenCache.put ( parent.getId (), cachedChildren );
            }
            cachedChildren.addAll ( index, children );
            cacheNodesById ( children );

            // Clearing nodes cache
            // That might be required in case nodes were moved inside of the tree
            clearNodeChildrenCache ( children, false );

            // Performing actual nodes insertion
            insertNodesIntoImpl ( children, parent, index );

            // Updating parent node sorting and filtering
            filterAndSort ( parent, false );
        }
    }

    /**
     * Inserts a list of child nodes into parent node.
     *
     * @param children list of new child nodes
     * @param parent   parent node
     * @param index    insert index
     */
    protected void insertNodesIntoImpl ( final List<N> children, final N parent, final int index )
    {
        super.insertNodesInto ( children, parent, index );

        // Adding image observers
        for ( final N child : children )
        {
            child.attachLoadIconObserver ( tree );
        }
    }

    /**
     * Inserts an array of child nodes into parent node.
     *
     * @param children array of new child nodes
     * @param parent   parent node
     * @param index    insert index
     */
    @Override
    public void insertNodesInto ( final N[] children, final N parent, final int index )
    {
        // Event Dispatch Thread check
        WebLookAndFeel.checkEventDispatchThread ();

        // Ensure node children are loaded
        if ( parent.isLoaded () )
        {
            // Inserting new raw children
            List<N> cachedChildren = rawNodeChildrenCache.get ( parent.getId () );
            if ( cachedChildren == null )
            {
                cachedChildren = new ArrayList<N> ( 1 );
                rawNodeChildrenCache.put ( parent.getId (), cachedChildren );
            }
            for ( int i = children.length - 1; i >= 0; i-- )
            {
                cachedChildren.add ( index, children[ i ] );
            }
            cacheNodesById ( CollectionUtils.asList ( children ) );

            // Clearing nodes cache
            // That might be required in case nodes were moved inside of the tree
            clearNodeChildrenCache ( children, false );

            // Inserting nodes
            insertNodesIntoImpl ( children, parent, index );

            // Updating parent node sorting and filtering
            filterAndSort ( parent, false );
        }
    }

    /**
     * Inserts an array of child nodes into parent node.
     *
     * @param children array of new child nodes
     * @param parent   parent node
     * @param index    insert index
     */
    protected void insertNodesIntoImpl ( final N[] children, final N parent, final int index )
    {
        super.insertNodesInto ( children, parent, index );

        // Adding image observers
        for ( final N child : children )
        {
            child.attachLoadIconObserver ( tree );
        }
    }

    @Override
    public void removeNodeFromParent ( final MutableTreeNode node )
    {
        // Event Dispatch Thread check
        WebLookAndFeel.checkEventDispatchThread ();

        // Simply ignore null nodes
        if ( node != null )
        {
            final N child = ( N ) node;
            final N parent = ( N ) child.getParent ();

            // Ensure parent exists and children are loaded
            if ( parent != null && parent.isLoaded () )
            {
                // Removing raw children
                final List<N> children = rawNodeChildrenCache.get ( parent.getId () );
                if ( children != null )
                {
                    children.remove ( child );
                }

                // Clearing node cache
                clearNodeChildrenCache ( child, true );

                // Removing node children so they won't mess up anything when we place node back into tree
                child.removeAllChildren ();

                // Removing node from parent
                super.removeNodeFromParent ( node );

                // Removing image observer
                child.detachLoadIconObserver ( tree );
            }
        }
    }

    @Override
    public void removeNodesFromParent ( final N parent )
    {
        // Event Dispatch Thread check
        WebLookAndFeel.checkEventDispatchThread ();

        // Ensure parent exists and children are loaded
        if ( parent != null && parent.isLoaded () )
        {
            // Removing raw children
            final List<N> removed = rawNodeChildrenCache.get ( parent.getId () );

            // Clearing node children caches
            clearNodeChildrenCache ( parent, false );

            // Removing node children
            super.removeNodesFromParent ( parent );

            // Removing image observers
            if ( CollectionUtils.notEmpty ( removed ) )
            {
                for ( final N node : removed )
                {
                    node.detachLoadIconObserver ( tree );
                }
            }
        }
    }

    @Override
    public void removeNodesFromParent ( final N[] nodes )
    {
        // Redirecting to another method
        removeNodesFromParent ( CollectionUtils.toList ( nodes ) );
    }

    @Override
    public void removeNodesFromParent ( final List<N> nodes )
    {
        // Event Dispatch Thread check
        WebLookAndFeel.checkEventDispatchThread ();

        // Removing raw children
        final List<N> removed = new ArrayList<N> ( nodes.size () );
        for ( final N child : nodes )
        {
            // Ensure parent exists and children are loaded
            final N parent = ( N ) child.getParent ();
            if ( parent != null && parent.isLoaded () )
            {
                // Updating children caches
                final List<N> children = rawNodeChildrenCache.get ( parent.getId () );
                if ( children != null )
                {
                    children.remove ( child );
                }

                // Clearing nodes children caches
                clearNodeChildrenCache ( child, true );

                // Removing node children so they won't mess up anything when we place node back into tree
                child.removeAllChildren ();

                // Saving removed node
                removed.add ( child );
            }
        }

        // Removing nodes from parent
        super.removeNodesFromParent ( nodes );

        // Removing image observers
        if ( CollectionUtils.notEmpty ( removed ) )
        {
            for ( final N node : removed )
            {
                node.detachLoadIconObserver ( tree );
            }
        }
    }

    /**
     * Updates sorting and filtering for the specified node children.
     *
     * @param parentNode  node which children sorting and filtering should be updated
     * @param recursively whether should update the whole children structure recursively or not
     */
    public void filterAndSort ( final N parentNode, final boolean recursively )
    {
        // Event Dispatch Thread check
        WebLookAndFeel.checkEventDispatchThread ();

        // Redirecting call to internal implementation
        filterAndSort ( parentNode, recursively, true );
    }

    /**
     * Updates sorting and filtering for the specified node children.
     *
     * @param parentNode     node which children sorting and filtering should be updated
     * @param recursively    whether should update the whole children structure recursively or not
     * @param performUpdates whether tree updates should be triggered within this method
     */
    protected void filterAndSort ( final N parentNode, final boolean recursively, final boolean performUpdates )
    {
        // Process this action only if node children are already loaded and cached
        if ( parentNode.isLoaded () && rawNodeChildrenCache.containsKey ( parentNode.getId () ) )
        {
            // Children are already loaded, simply updating their sorting and filtering
            filterAndSortRecursively ( parentNode, recursively, performUpdates );
        }
        else if ( parentNode.isLoading () )
        {
            // Children are being loaded, wait until the operation finishes
            addAsyncTreeModelListener ( new AsyncTreeModelAdapter ()
            {
                @Override
                public void loadCompleted ( final AsyncUniqueNode parent, final List children )
                {
                    // Event Dispatch Thread check
                    WebLookAndFeel.checkEventDispatchThread ();

                    // Performing delayed filtering and sorting
                    if ( parentNode.getId ().equals ( parent.getId () ) )
                    {
                        removeAsyncTreeModelListener ( this );
                        filterAndSortRecursively ( parentNode, recursively, performUpdates );
                    }
                }

                @Override
                public void loadFailed ( final AsyncUniqueNode parent, final Throwable cause )
                {
                    // Event Dispatch Thread check
                    WebLookAndFeel.checkEventDispatchThread ();

                    // Cancelling any further operations
                    if ( parentNode.getId ().equals ( parent.getId () ) )
                    {
                        removeAsyncTreeModelListener ( this );
                    }
                }
            } );
        }
    }

    /**
     * Updates node children using current comparator and filter.
     * Updates the whole node children structure if recursive update requested.
     *
     * @param parentNode     node which children sorting and filtering should be updated
     * @param recursively    whether should update the whole children structure recursively or not
     * @param performUpdates whether tree updates should be triggered within this method
     */
    protected void filterAndSortRecursively ( final N parentNode, final boolean recursively, final boolean performUpdates )
    {
        // Saving tree state to restore it right after children update
        // todo This doesn't work if some of the children updates are delayed using listener
        final TreeState treeState = tree.getTreeState ( parentNode );

        // Updating node children sorting and filtering
        // Process this action only if node children are already loaded and cached
        final List<N> cachedChildren = rawNodeChildrenCache.get ( parentNode.getId () );
        if ( cachedChildren != null )
        {
            // Removing old children
            parentNode.removeAllChildren ();

            // Filtering and sorting raw children
            final List<N> children = filterAndSort ( parentNode, cachedChildren );

            // Inserting new children
            for ( final N child : children )
            {
                parentNode.add ( child );
            }
        }

        // Updating children's children
        if ( recursively )
        {
            for ( int i = 0; i < parentNode.getChildCount (); i++ )
            {
                filterAndSort ( ( N ) parentNode.getChildAt ( i ), true, false );
            }
        }

        // Performing tree updates
        if ( performUpdates )
        {
            // Forcing tree structure update for the node
            nodeStructureChanged ( parentNode );

            // Restoring tree state including all selections and expansions
            tree.setTreeState ( treeState, parentNode );
        }
    }

    /**
     * Returns list of filtered and sorted raw children.
     *
     * @param parentNode parent node
     * @param children   children to filter and sort
     * @return list of filtered and sorted children
     */
    protected List<N> filterAndSort ( final N parentNode, final List<N> children )
    {
        // Simply return an empty array if there is no children
        if ( children == null || children.size () == 0 )
        {
            return new ArrayList<N> ( 0 );
        }

        // Filtering children
        final Filter<N> filter = dataProvider.getChildrenFilter ( parentNode, children );
        final List<N> result = filter != null ? CollectionUtils.filter ( children, filter ) : CollectionUtils.copy ( children );

        // Sorting children
        final Comparator<N> comparator = dataProvider.getChildrenComparator ( parentNode, result );
        if ( comparator != null )
        {
            Collections.sort ( result, comparator );
        }

        return result;
    }

    /**
     * Looks for the node with the specified ID in the tree model and returns it or null if it was not found.
     *
     * @param nodeId node ID
     * @return node with the specified ID or null if it was not found
     */
    public N findNode ( final String nodeId )
    {
        return nodeById.get ( nodeId );
    }

    /**
     * Returns whether children for the specified node are already loaded or not.
     *
     * @param node node to process
     * @return true if children for the specified node are already loaded, false otherwise
     */
    public boolean areChildrenLoaded ( final N node )
    {
        final Boolean cached = nodeCached.get ( node.getId () );
        return cached != null && cached;
    }

    /**
     * Clears node and all of its child nodes children cached states.
     *
     * @param node      node to clear cache for
     * @param clearNode whether should clear node cache or not
     */
    protected void clearNodeChildrenCache ( final N node, final boolean clearNode )
    {
        // Clears node cache
        if ( clearNode )
        {
            nodeById.remove ( node.getId () );
        }

        // Clears node children cached state
        nodeCached.remove ( node.getId () );

        // Clears node raw children cache
        final List<N> children = rawNodeChildrenCache.remove ( node.getId () );

        // Clears chld nodes cache
        if ( children != null )
        {
            clearNodeChildrenCache ( children, true );
        }
    }

    /**
     * Clears nodes children cached states.
     *
     * @param nodes      nodes to clear cache for
     * @param clearNodes whether should clear nodes cache or not
     */
    protected void clearNodeChildrenCache ( final List<N> nodes, final boolean clearNodes )
    {
        for ( final N node : nodes )
        {
            clearNodeChildrenCache ( node, clearNodes );
        }
    }

    /**
     * Clears nodes children cached states.
     *
     * @param nodes      nodes to clear cache for
     * @param clearNodes whether should clear nodes cache or not
     */
    protected void clearNodeChildrenCache ( final N[] nodes, final boolean clearNodes )
    {
        for ( final N node : nodes )
        {
            clearNodeChildrenCache ( node, clearNodes );
        }
    }

    /**
     * Caches node by its IDs.
     *
     * @param node node to cache
     */
    protected void cacheNodeById ( final N node )
    {
        nodeById.put ( node.getId (), node );
    }

    /**
     * Caches nodes by their IDs.
     *
     * @param nodes list of nodes to cache
     */
    protected void cacheNodesById ( final List<N> nodes )
    {
        for ( final N node : nodes )
        {
            nodeById.put ( node.getId (), node );
        }
    }

    /**
     * Returns list of all available asynchronous tree model listeners.
     *
     * @return asynchronous tree model listeners list
     */
    public List<AsyncTreeModelListener> getAsyncTreeModelListeners ()
    {
        return CollectionUtils.copy ( asyncTreeModelListeners );
    }

    /**
     * Adds new asynchronous tree model listener.
     *
     * @param listener asynchronous tree model listener to add
     */
    public void addAsyncTreeModelListener ( final AsyncTreeModelListener listener )
    {
        // Event Dispatch Thread check
        WebLookAndFeel.checkEventDispatchThread ();

        // Adding new listener
        asyncTreeModelListeners.add ( listener );
    }

    /**
     * Removes asynchronous tree model listener.
     *
     * @param listener asynchronous tree model listener to remove
     */
    public void removeAsyncTreeModelListener ( final AsyncTreeModelListener listener )
    {
        // Event Dispatch Thread check
        WebLookAndFeel.checkEventDispatchThread ();

        // Removing existing listener
        asyncTreeModelListeners.remove ( listener );
    }

    /**
     * Fires children load start event.
     *
     * @param parent node which children are being loaded
     */
    protected void fireChildrenLoadStarted ( final N parent )
    {
        // Event Dispatch Thread check
        WebLookAndFeel.checkEventDispatchThread ();

        // Firing listeners
        for ( final AsyncTreeModelListener listener : CollectionUtils.copy ( asyncTreeModelListeners ) )
        {
            listener.loadStarted ( parent );
        }
    }

    /**
     * Fires children load complete event.
     *
     * @param parent   node which children were loaded
     * @param children loaded child nodes
     */
    protected void fireChildrenLoadCompleted ( final N parent, final List<N> children )
    {
        // Event Dispatch Thread check
        WebLookAndFeel.checkEventDispatchThread ();

        // Firing listeners
        for ( final AsyncTreeModelListener listener : CollectionUtils.copy ( asyncTreeModelListeners ) )
        {
            listener.loadCompleted ( parent, children );
        }
    }

    /**
     * Fires children load failed event.
     *
     * @param parent node which children were loaded
     * @param cause  children load failure cause
     */
    protected void fireChildrenLoadFailed ( final N parent, final Throwable cause )
    {
        // Event Dispatch Thread check
        WebLookAndFeel.checkEventDispatchThread ();

        // Firing listeners
        for ( final AsyncTreeModelListener listener : CollectionUtils.copy ( asyncTreeModelListeners ) )
        {
            listener.loadFailed ( parent, cause );
        }
    }
}