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
import com.alee.laf.tree.WebTreeNode;
import com.alee.utils.CollectionUtils;
import com.alee.utils.CoreSwingUtils;
import com.alee.utils.compare.Filter;

import javax.swing.event.EventListenerList;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import java.util.*;

/**
 * {@link WebTreeModel} extension that is based on data from {@link AsyncTreeDataProvider}.
 * It allows tree to load its data asynchronously but only supports sorting and filtering of loaded nodes.
 *
 * @param <N> {@link AsyncUniqueNode} type
 * @author Mikle Garin
 * @see WebAsyncTree
 * @see AsyncTreeDataProvider
 */
public class AsyncTreeModel<N extends AsyncUniqueNode> extends WebTreeModel<N> implements FilterableNodes<N>, SortableNodes<N>
{
    /**
     * todo 1. Add {@link AsyncTreeDataUpdater} support
     */

    /**
     * {@link AsyncTreeDataProvider} implementation.
     * It is used to provide all {@link AsyncUniqueNode}s for the tree.
     */
    protected final AsyncTreeDataProvider<N> dataProvider;

    /**
     * Event listeners.
     */
    protected final EventListenerList listeners;

    /**
     * Nodes cached states (parent identifier -&gt; children cached state).
     * If child nodes for some parent node are cached then this map contains "true" value under that parent node identifier as a key.
     */
    protected transient Map<String, Boolean> nodeCached;

    /**
     * Cache for children nodes returned by data provider (parent identifier -&gt; list of raw child nodes).
     * This map contains raw children which weren't affected by sorting and filtering operations.
     * If children needs to be re-sorted or re-filtered they are simply taken from the cache and re-organized once again.
     */
    protected transient Map<String, List<N>> rawNodeChildrenCache;

    /**
     * Direct nodes cache (node identifier -&gt; node).
     * Used for quick node search within the tree.
     */
    protected transient Map<String, N> nodeById;

    /**
     * Asynchronous tree that uses this model.
     */
    protected transient WebAsyncTree<N> tree;

    /**
     * Root node cache.
     * Cached when root is requested for the first time.
     */
    protected transient N rootNode;

    /**
     * {@link Filter} for {@link AsyncUniqueNode}s.
     */
    protected transient Filter<N> filter;

    /**
     * {@link Comparator} for {@link AsyncUniqueNode}s.
     */
    protected transient Comparator<N> comparator;

    /**
     * Constructs new {@link AsyncTreeModel} with custom {@link AsyncTreeDataProvider}.
     *
     * @param dataProvider {@link AsyncTreeDataProvider}
     */
    public AsyncTreeModel ( final AsyncTreeDataProvider<N> dataProvider )
    {
        super ( null );
        this.dataProvider = dataProvider;
        this.listeners = new EventListenerList ();
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

    /**
     * Installs this {@link AsyncTreeModel} into the specified {@link WebAsyncTree}.
     *
     * @param tree {@link WebAsyncTree}
     */
    public void install ( final WebAsyncTree<N> tree )
    {
        WebLookAndFeel.checkEventDispatchThread ();
        this.nodeCached = new HashMap<String, Boolean> ( 50 );
        this.rawNodeChildrenCache = new HashMap<String, List<N>> ( 10 );
        this.nodeById = new HashMap<String, N> ( 50 );
        this.tree = tree;
        this.rootNode = null;
        addAsyncTreeModelListener ( tree );
    }

    /**
     * Uninstalls this {@link AsyncTreeModel} from the specified {@link WebAsyncTree}.
     *
     * @param tree {@link WebAsyncTree}
     */
    public void uninstall ( final WebAsyncTree<N> tree )
    {
        WebLookAndFeel.checkEventDispatchThread ();
        removeAsyncTreeModelListener ( tree );
        for ( final Map.Entry<String, N> entry : nodeById.entrySet () )
        {
            final N node = entry.getValue ();
            node.detachLoadIconObserver ( tree );
        }
        this.rootNode = null;
        this.tree = null;
        this.nodeById = null;
        this.rawNodeChildrenCache = null;
        this.nodeCached = null;
    }

    /**
     * Returns whether or not this {@link AsyncTreeModel} is installed into some {@link WebAsyncTree}.
     *
     * @return {@code true} if this {@link AsyncTreeModel} is installed into some {@link WebAsyncTree}, {@code false} otherwise
     */
    public boolean isInstalled ()
    {
        return tree != null;
    }

    /**
     * Checks whether or not this {@link AsyncTreeModel} is installed into some {@link WebAsyncTree}.
     * If it is not installed - {@link IllegalStateException} is thrown to emphasize problem.
     */
    protected void checkInstalled ()
    {
        if ( !isInstalled () )
        {
            throw new IllegalStateException ( "This operation cannot be performed before model is installed into WebAsyncTree" );
        }
    }

    @Override
    public N getRoot ()
    {
        // Event Dispatch Thread check
        WebLookAndFeel.checkEventDispatchThread ();

        // Ensure model is installed
        checkInstalled ();

        // Creating root node if needed
        if ( rootNode == null )
        {
            // Retrieving and caching root node
            rootNode = getDataProvider ().getRoot ();

            // Caching root node by identifier
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

        // Ensure model is installed
        checkInstalled ();

        // Redirecting check to provider
        return getDataProvider ().isLeaf ( ( N ) node );
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

        // Ensure model is installed
        checkInstalled ();

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

        // Ensure model is installed
        checkInstalled ();

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

        // Ensure model is installed
        checkInstalled ();

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
            if ( tree.isAsyncLoading () )
            {
                // Executing children load in a separate thread to avoid locking EDT
                // This queue will also take care of amount of threads to execute async trees requests
                AsyncTreeQueue.getInstance ( tree ).execute ( new Runnable ()
                {
                    @Override
                    public void run ()
                    {
                        // Loading children
                        getDataProvider ().loadChildren ( parent, new NodesLoadCallback<N> ()
                        {
                            @Override
                            public void completed ( final List<N> children )
                            {
                                CoreSwingUtils.invokeLater ( new Runnable ()
                                {
                                    @Override
                                    public void run ()
                                    {
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
                // todo There is no guarantee that provider won't load children asynchronously
                // todo Practically speaking - it's better to get rid of sync loading option in this tree and remove this part
                getDataProvider ().loadChildren ( parent, new NodesLoadCallback<N> ()
                {
                    @Override
                    public void completed ( final List<N> children )
                    {
                        loadChildrenCompleted ( parent, children );
                    }

                    @Override
                    public void failed ( final Throwable cause )
                    {
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
        // Operation might have finished after model was removed from the tree
        if ( isInstalled () )
        {
            // Event Dispatch Thread check
            WebLookAndFeel.checkEventDispatchThread ();

            // Caching raw children
            rawNodeChildrenCache.put ( parent.getId (), children );
            cacheNodesById ( children );

            // Adding image observers
            // We need to do this separately here since some nodes might not be inserted right away
            // Instead they will appear on the next global update and will "escape" observer addition in that case
            for ( final N child : children )
            {
                child.attachLoadIconObserver ( tree );
            }

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
    }

    /**
     * Cancells children loading upon failure.
     *
     * @param parent parent node
     * @param cause  failure cause
     */
    protected void loadChildrenFailed ( final N parent, final Throwable cause )
    {
        // Operation might have finished after model was removed from the tree
        if ( isInstalled () )
        {
            // Event Dispatch Thread check
            WebLookAndFeel.checkEventDispatchThread ();

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
    }

    @Override
    public void valueForPathChanged ( final TreePath path, final Object newValue )
    {
        // Ensure model is installed
        checkInstalled ();

        // Perform default operations
        super.valueForPathChanged ( path, newValue );

        // Updating filtering and sorting for parent of this node unless it is root node
        final N node = tree.getNodeForPath ( path );
        if ( node != null )
        {
            final WebTreeNode parent = node.getParent ();
            if ( parent != null )
            {
                filterAndSort ( ( N ) parent, false );
            }
        }
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

        // Ensure model is installed
        checkInstalled ();

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

        // Ensure model is installed
        checkInstalled ();

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

        // Ensure model is installed
        checkInstalled ();

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

        // Ensure model is installed
        checkInstalled ();

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

        // Ensure model is installed
        checkInstalled ();

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

        // Ensure model is installed
        checkInstalled ();

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

        // Ensure model is installed
        checkInstalled ();

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

        // Ensure model is installed
        checkInstalled ();

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

    @Override
    public Filter<N> getFilter ()
    {
        return filter;
    }

    @Override
    public void setFilter ( final Filter<N> filter )
    {
        this.filter = filter;
        filter ();
    }

    @Override
    public void clearFilter ()
    {
        setFilter ( null );
    }

    @Override
    public void filter ()
    {
        filterAndSort ( true );
    }

    @Override
    public void filter ( final N node )
    {
        filterAndSort ( node, false );
    }

    @Override
    public void filter ( final N node, final boolean recursively )
    {
        filterAndSort ( node, recursively );
    }

    @Override
    public Comparator<N> getComparator ()
    {
        return comparator;
    }

    @Override
    public void setComparator ( final Comparator<N> comparator )
    {
        this.comparator = comparator;
        sort ();
    }

    @Override
    public void clearComparator ()
    {
        setComparator ( null );
    }

    @Override
    public void sort ()
    {
        filterAndSort ( true );
    }

    @Override
    public void sort ( final N node )
    {
        filterAndSort ( node, false );
    }

    @Override
    public void sort ( final N node, final boolean recursively )
    {
        filterAndSort ( node, recursively );
    }

    /**
     * Updates sorting and filtering for the root node children.
     *
     * @param recursively whether should update the whole children structure recursively or not
     */
    public void filterAndSort ( final boolean recursively )
    {
        filterAndSort ( null, recursively );
    }

    /**
     * Updates sorting and filtering for the specified node children.
     *
     * @param parent      node which children sorting and filtering should be updated
     * @param recursively whether should update the whole children structure recursively or not
     */
    public void filterAndSort ( final N parent, final boolean recursively )
    {
        // Operation might have finished after model was removed from the tree
        if ( isInstalled () )
        {
            // Event Dispatch Thread check
            WebLookAndFeel.checkEventDispatchThread ();

            // Determining actual parent
            final N actualParent = parent != null ? parent : getRoot ();

            // Redirecting call to internal implementation
            filterAndSort ( actualParent, recursively, true );
        }
    }

    /**
     * Updates sorting and filtering for the specified node children.
     *
     * @param parent         node which children sorting and filtering should be updated
     * @param recursively    whether should update the whole children structure recursively or not
     * @param performUpdates whether tree updates should be triggered within this method
     */
    protected void filterAndSort ( final N parent, final boolean recursively, final boolean performUpdates )
    {
        // Process this action only if node children are already loaded and cached
        if ( parent.isLoaded () && rawNodeChildrenCache.containsKey ( parent.getId () ) )
        {
            // Children are already loaded, simply updating their sorting and filtering
            filterAndSortRecursively ( parent, recursively, performUpdates );
        }
        else if ( parent.isLoading () )
        {
            // Children are being loaded, wait until the operation finishes
            addAsyncTreeModelListener ( new AsyncTreeModelAdapter ()
            {
                @Override
                public void loadCompleted ( final AsyncUniqueNode completedFor, final List children )
                {
                    // Performing delayed filtering and sorting
                    if ( parent.getId ().equals ( completedFor.getId () ) )
                    {
                        removeAsyncTreeModelListener ( this );
                        filterAndSortRecursively ( parent, recursively, performUpdates );
                    }
                }

                @Override
                public void loadFailed ( final AsyncUniqueNode failedFor, final Throwable cause )
                {
                    // Cancelling any further operations
                    if ( parent.getId ().equals ( failedFor.getId () ) )
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
     * @param parent         node which children sorting and filtering should be updated
     * @param recursively    whether should update the whole children structure recursively or not
     * @param performUpdates whether tree updates should be triggered within this method
     */
    protected void filterAndSortRecursively ( final N parent, final boolean recursively, final boolean performUpdates )
    {
        // Saving tree state to restore it right after children update
        // todo This doesn't work if some of the children updates are delayed using listener
        final TreeState treeState = tree.getTreeState ( parent );

        // Updating node children sorting and filtering
        // Process this action only if node children are already loaded and cached
        final List<N> cachedChildren = rawNodeChildrenCache.get ( parent.getId () );
        if ( cachedChildren != null )
        {
            // Removing old children
            parent.removeAllChildren ();

            // Filtering and sorting raw children
            final List<N> children = filterAndSort ( parent, cachedChildren );

            // Inserting new children
            for ( final N child : children )
            {
                parent.add ( child );
            }
        }

        // Updating children's children
        if ( recursively )
        {
            for ( int i = 0; i < parent.getChildCount (); i++ )
            {
                filterAndSort ( ( N ) parent.getChildAt ( i ), true, false );
            }
        }

        // Performing tree updates
        if ( performUpdates )
        {
            // Forcing tree structure update for the node
            nodeStructureChanged ( parent );

            // Restoring tree state including all selections and expansions
            tree.setTreeState ( treeState, parent );
        }
    }

    /**
     * Returns list of filtered and sorted raw children.
     *
     * @param parent   parent node
     * @param children children to filter and sort
     * @return list of filtered and sorted children
     */
    protected List<N> filterAndSort ( final N parent, final List<N> children )
    {
        final List<N> result;
        if ( CollectionUtils.notEmpty ( children ) )
        {
            // Data provider
            final AsyncTreeDataProvider<N> dataProvider = getDataProvider ();

            // Filtering children
            final Filter<N> dataProviderFilter = dataProvider.getChildrenFilter ( parent, children );
            final Filter<N> treeFilter = tree.getFilter ();
            final Filter<N> modelFilter = getFilter ();
            result = CollectionUtils.filter ( children, dataProviderFilter, treeFilter, modelFilter );

            // Sorting children
            final Comparator<N> dataProviderComparator = dataProvider.getChildrenComparator ( parent, result );
            final Comparator<N> treeComparator = tree.getComparator ();
            final Comparator<N> modelComparator = getComparator ();
            CollectionUtils.sort ( result, dataProviderComparator, treeComparator, modelComparator );
        }
        else
        {
            // Simply return an empty list if there is no children
            result = new ArrayList<N> ( 0 );
        }
        return result;
    }

    /**
     * Looks for the node with the specified identifier in the tree model and returns it or null if it was not found.
     *
     * @param nodeId node identifier
     * @return node with the specified identifier or null if it was not found
     */
    public N findNode ( final String nodeId )
    {
        // Ensure model is installed
        checkInstalled ();

        // Get node from cache
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
        // Ensure model is installed
        checkInstalled ();

        // Check children load state
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
     * Caches node by its identifier.
     *
     * @param node node to cache
     */
    protected void cacheNodeById ( final N node )
    {
        nodeById.put ( node.getId (), node );
    }

    /**
     * Caches nodes by their identifiers.
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
     * Adds new asynchronous tree model listener.
     *
     * @param listener asynchronous tree model listener to add
     */
    public void addAsyncTreeModelListener ( final AsyncTreeModelListener listener )
    {
        // Event Dispatch Thread check
        WebLookAndFeel.checkEventDispatchThread ();

        // Adding new listener
        listeners.add ( AsyncTreeModelListener.class, listener );
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
        listeners.remove ( AsyncTreeModelListener.class, listener );
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
        for ( final AsyncTreeModelListener listener : listeners.getListeners ( AsyncTreeModelListener.class ) )
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
        for ( final AsyncTreeModelListener listener : listeners.getListeners ( AsyncTreeModelListener.class ) )
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
        for ( final AsyncTreeModelListener listener : listeners.getListeners ( AsyncTreeModelListener.class ) )
        {
            listener.loadFailed ( parent, cause );
        }
    }
}