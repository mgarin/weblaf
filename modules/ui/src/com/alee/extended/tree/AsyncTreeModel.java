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

import com.alee.laf.tree.TreeState;
import com.alee.laf.tree.WebTreeModel;
import com.alee.utils.CollectionUtils;
import com.alee.utils.MapUtils;
import com.alee.utils.SwingUtils;
import com.alee.utils.collection.DoubleMap;
import com.alee.utils.compare.Filter;

import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;
import java.util.*;

/**
 * Special model for asynchronous tree that provides asynchronous data loading.
 * This class also controls the loading animation in elements.
 *
 * @param <E> custom node type
 * @author Mikle Garin
 * @see com.alee.extended.tree.WebAsyncTree
 * @see com.alee.extended.tree.AsyncTreeDataProvider
 */

public class AsyncTreeModel<E extends AsyncUniqueNode> extends WebTreeModel<E>
{
    /**
     * todo 1. Add AsyncTreeDataUpdater support
     */

    /**
     * Lock object for asynchronous tree listeners.
     */
    protected final Object modelListenersLock = new Object ();

    /**
     * Asynchronous tree listeners.
     */
    protected final List<AsyncTreeModelListener> asyncTreeModelListeners = new ArrayList<AsyncTreeModelListener> ( 1 );

    /**
     * Asynchronous tree that uses this model.
     */
    protected final WebAsyncTree<E> tree;

    /**
     * Asynchronous tree data provider.
     */
    protected final AsyncTreeDataProvider<E> dataProvider;

    /**
     * Whether to load children asynchronously or not.
     */
    protected boolean asyncLoading = true;

    //    /**
    //     * Data updater for this asynchronous tree.
    //     */
    //    protected AsyncTreeDataUpdater<E> dataUpdater;

    /**
     * Root node cache.
     * Cached when root is requested for the first time.
     */
    protected E rootNode = null;

    /**
     * Lock object for cache changes.
     */
    protected final Object cacheLock = new Object ();

    /**
     * Nodes cached states (parent ID -&gt; children cached state).
     * If child nodes for some parent node are cached then this map contains "true" value under that parent node ID as a key.
     */
    protected final Map<String, Boolean> nodeCached = new HashMap<String, Boolean> ();

    /**
     * Cache for children nodes returned by data provider (parent ID -&gt; list of raw child nodes).
     * This map contains raw children which weren't affected by sorting and filtering operations.
     * If children needs to be re-sorted or re-filtered they are simply taken from the cache and re-organized once again.
     */
    protected final Map<String, List<E>> rawNodeChildrenCache = new HashMap<String, List<E>> ();

    /**
     * Direct nodes cache (node ID -&gt; node).
     * Used for quick node search within the tree.
     */
    protected final DoubleMap<String, E> nodeById = new DoubleMap<String, E> ();

    /**
     * Lock object for busy state changes.
     */
    protected final Object busyLock = new Object ();

    /**
     * Constructs default asynchronous tree model using custom data provider.
     *
     * @param tree         asynchronous tree
     * @param dataProvider data provider
     */
    public AsyncTreeModel ( final WebAsyncTree<E> tree, final AsyncTreeDataProvider<E> dataProvider )
    {
        super ( null );
        this.tree = tree;
        this.dataProvider = dataProvider;
    }

    //    /**
    //     * Returns data updater for this asynchronous tree.
    //     *
    //     * @return data updater
    //     */
    //    public AsyncTreeDataUpdater<E> getDataUpdater ()
    //    {
    //        return dataUpdater;
    //    }
    //
    //    /**
    //     * Changes data updater for this asynchronous tree.
    //     *
    //     * @param dataUpdater new data updater
    //     */
    //    public void setDataUpdater ( final AsyncTreeDataUpdater<E> dataUpdater )
    //    {
    //        this.dataUpdater = dataUpdater;
    //    }

    /**
     * Returns whether children are loaded asynchronously or not.
     *
     * @return true if children are loaded asynchronously, false otherwise
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
        this.asyncLoading = asyncLoading;
    }

    /**
     * Returns asynchronous tree data provider.
     *
     * @return data provider
     */
    public AsyncTreeDataProvider<E> getDataProvider ()
    {
        return dataProvider;
    }

    /**
     * Returns tree root node.
     *
     * @return root node
     */
    @Override
    public E getRoot ()
    {
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

    /**
     * Returns whether the specified node is leaf or not.
     *
     * @param node node
     * @return true if node is leaf, false otherwise
     */
    @Override
    public boolean isLeaf ( final Object node )
    {
        return dataProvider.isLeaf ( ( E ) node );
    }

    /**
     * Returns children count for specified node.
     *
     * @param parent parent node
     * @return children count
     */
    @Override
    public int getChildCount ( final Object parent )
    {
        final E node = ( E ) parent;
        if ( isLeaf ( node ) )
        {
            return 0;
        }
        else if ( areChildrenLoaded ( node ) )
        {
            return super.getChildCount ( parent );
        }
        else
        {
            return loadChildren ( node );
        }
    }

    /**
     * Returns child node for parent node at the specified index.
     *
     * @param parent parent node
     * @param index  child node index
     * @return child node
     */
    @Override
    public E getChild ( final Object parent, final int index )
    {
        final E node = ( E ) parent;
        if ( areChildrenLoaded ( node ) )
        {
            return ( E ) super.getChild ( parent, index );
        }
        else
        {
            return null;
        }
    }

    /**
     * Returns whether children for the specified node are already loaded or not.
     *
     * @param node node to process
     * @return true if children for the specified node are already loaded, false otherwise
     */
    public boolean areChildrenLoaded ( final E node )
    {
        synchronized ( cacheLock )
        {
            final Boolean cached = nodeCached.get ( node.getId () );
            return cached != null && cached;
        }
    }

    /**
     * Reloads node children.
     *
     * @param node node
     */
    @Override
    public void reload ( final TreeNode node )
    {
        // Cancels tree editing
        tree.cancelEditing ();

        // Cleaning up nodes cache
        clearNodeChildrenCache ( ( E ) node, false );

        // Forcing children reload
        super.reload ( node );
    }

    /**
     * Clears node and all of its child nodes children cached states.
     *
     * @param node      node to clear cache for
     * @param clearNode whether should clear node cache or not
     */
    protected void clearNodeChildrenCache ( final E node, final boolean clearNode )
    {
        synchronized ( cacheLock )
        {
            // Clears node cache
            if ( clearNode )
            {
                nodeById.remove ( node.getId () );
            }

            // Clears node children cached state
            nodeCached.remove ( node.getId () );

            // Clears node raw children cache
            final List<E> children = rawNodeChildrenCache.remove ( node.getId () );

            // Clears chld nodes cache
            if ( children != null )
            {
                clearNodeChildrenCache ( children, true );
            }
        }
    }

    /**
     * Clears nodes children cached states.
     *
     * @param nodes      nodes to clear cache for
     * @param clearNodes whether should clear nodes cache or not
     */
    protected void clearNodeChildrenCache ( final List<E> nodes, final boolean clearNodes )
    {
        synchronized ( cacheLock )
        {
            for ( final E node : nodes )
            {
                clearNodeChildrenCache ( node, clearNodes );
            }
        }
    }

    /**
     * Clears nodes children cached states.
     *
     * @param nodes      nodes to clear cache for
     * @param clearNodes whether should clear nodes cache or not
     */
    protected void clearNodeChildrenCache ( final E[] nodes, final boolean clearNodes )
    {
        synchronized ( cacheLock )
        {
            for ( final E node : nodes )
            {
                clearNodeChildrenCache ( node, clearNodes );
            }
        }
    }

    /**
     * Caches node by its IDs.
     *
     * @param node node to cache
     */
    protected void cacheNodeById ( final E node )
    {
        synchronized ( cacheLock )
        {
            nodeById.put ( node.getId (), node );
        }
    }

    /**
     * Caches nodes by their IDs.
     *
     * @param nodes list of nodes to cache
     */
    protected void cacheNodesById ( final List<E> nodes )
    {
        synchronized ( cacheLock )
        {
            for ( final E node : nodes )
            {
                nodeById.put ( node.getId (), node );
            }
        }
    }

    /**
     * Loads (or reloads) node children and returns zero or children count if async mode is off.
     * This is base method that uses installed AsyncTreeDataProvider to retrieve tree node children.
     *
     * @param parent node to load children for
     * @return zero or children count if async mode is off
     * @see AsyncTreeDataProvider#loadChildren(AsyncUniqueNode, ChildrenListener)
     */
    protected int loadChildren ( final E parent )
    {
        // todo Use when moved to JDK8?
        // final SecondaryLoop loop = Toolkit.getDefaultToolkit ().getSystemEventQueue ().createSecondaryLoop ();
        // loop.enter/exit

        // Checking if the node is busy already
        synchronized ( busyLock )
        {
            if ( parent.isLoading () )
            {
                return 0;
            }
            else
            {
                parent.setState ( AsyncNodeState.loading );
                nodeChanged ( parent );
            }
        }

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
        if ( asyncLoading )
        {
            // Executing children load in a separate thread to avoid locking EDT
            // This queue will also take care of amount of threads to execute async trees requests
            AsyncTreeQueue.execute ( tree, new Runnable ()
            {
                @Override
                public void run ()
                {
                    // Loading children
                    dataProvider.loadChildren ( parent, new ChildrenListener<E> ()
                    {
                        @Override
                        public void loadCompleted ( final List<E> children )
                        {
                            // Caching raw children
                            synchronized ( cacheLock )
                            {
                                rawNodeChildrenCache.put ( parent.getId (), children );
                                cacheNodesById ( children );
                            }

                            // Filtering and sorting raw children
                            final List<E> realChildren = filterAndSort ( parent, children );

                            // Updating cache
                            synchronized ( cacheLock )
                            {
                                nodeCached.put ( parent.getId (), true );
                            }

                            // Performing UI updates and event notification in EDT
                            SwingUtils.invokeLater ( new Runnable ()
                            {
                                @Override
                                public void run ()
                                {
                                    // Checking if any nodes loaded
                                    if ( realChildren != null && realChildren.size () > 0 )
                                    {
                                        // Inserting loaded nodes
                                        insertNodesIntoImpl ( realChildren, parent, 0 );
                                    }

                                    // Releasing node busy state
                                    synchronized ( busyLock )
                                    {
                                        parent.setState ( AsyncNodeState.loaded );
                                        nodeChanged ( parent );
                                    }

                                    // Firing load completed event
                                    fireChildrenLoadCompleted ( parent, realChildren );
                                }
                            } );
                        }

                        @Override
                        public void loadFailed ( final Throwable cause )
                        {
                            // Caching children
                            synchronized ( cacheLock )
                            {
                                rawNodeChildrenCache.put ( parent.getId (), new ArrayList<E> ( 0 ) );
                                nodeCached.put ( parent.getId (), true );
                            }

                            // Performing event notification in EDT
                            SwingUtils.invokeLater ( new Runnable ()
                            {
                                @Override
                                public void run ()
                                {
                                    // Releasing node busy state
                                    synchronized ( busyLock )
                                    {
                                        parent.setState ( AsyncNodeState.failed );
                                        parent.setFailureCause ( cause );
                                        nodeChanged ( parent );
                                    }

                                    // Firing load failed event
                                    fireChildrenLoadFailed ( parent, cause );
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
            dataProvider.loadChildren ( parent, new ChildrenListener<E> ()
            {
                @Override
                public void loadCompleted ( final List<E> children )
                {
                    // Caching raw children
                    synchronized ( cacheLock )
                    {
                        rawNodeChildrenCache.put ( parent.getId (), children );
                        cacheNodesById ( children );
                    }

                    // Filtering and sorting raw children
                    final List<E> realChildren = filterAndSort ( parent, children );

                    // Updating cache
                    synchronized ( cacheLock )
                    {
                        nodeCached.put ( parent.getId (), true );
                    }

                    // Checking if any nodes loaded
                    if ( realChildren != null && realChildren.size () > 0 )
                    {
                        // Inserting loaded nodes
                        insertNodesIntoImpl ( realChildren, parent, 0 );
                    }

                    // Releasing node busy state
                    synchronized ( busyLock )
                    {
                        parent.setState ( AsyncNodeState.loaded );
                        nodeChanged ( parent );
                    }

                    // Firing load completed event
                    fireChildrenLoadCompleted ( parent, realChildren );
                }

                @Override
                public void loadFailed ( final Throwable cause )
                {
                    // Caching children
                    synchronized ( cacheLock )
                    {
                        rawNodeChildrenCache.put ( parent.getId (), new ArrayList<E> ( 0 ) );
                        nodeCached.put ( parent.getId (), true );
                    }

                    // Releasing node busy state
                    synchronized ( busyLock )
                    {
                        parent.setState ( AsyncNodeState.failed );
                        parent.setFailureCause ( cause );
                        nodeChanged ( parent );
                    }

                    // Firing load failed event
                    fireChildrenLoadFailed ( parent, cause );
                }
            } );
            return parent.getChildCount ();
        }
    }

    /**
     * Sets child nodes for the specified node.
     * This method might be used to manually change tree node children without causing any structure corruptions.
     *
     * @param parent   node to process
     * @param children new node children
     */
    public void setChildNodes ( final E parent, final List<E> children )
    {
        // Check if the node is busy already
        synchronized ( busyLock )
        {
            if ( parent.isLoading () )
            {
                return;
            }
            else
            {
                parent.setState ( AsyncNodeState.loading );
                nodeChanged ( parent );
            }
        }

        // Caching raw children
        synchronized ( cacheLock )
        {
            rawNodeChildrenCache.put ( parent.getId (), children );
            cacheNodesById ( children );
        }

        // Filtering and sorting raw children
        final List<E> realChildren = filterAndSort ( parent, children );

        // Updating cache
        synchronized ( cacheLock )
        {
            nodeCached.put ( parent.getId (), true );
        }

        // Performing UI updates in EDT
        SwingUtils.invokeLater ( new Runnable ()
        {
            @Override
            public void run ()
            {
                // Checking if any nodes loaded
                if ( realChildren != null && realChildren.size () > 0 )
                {
                    // Clearing raw nodes cache
                    // That might be required in case nodes were moved inside of the tree
                    clearNodeChildrenCache ( children, false );

                    // Inserting nodes
                    insertNodesIntoImpl ( realChildren, parent, 0 );
                }

                // Release node busy state
                synchronized ( busyLock )
                {
                    parent.setState ( AsyncNodeState.loaded );
                    nodeChanged ( parent );
                }

                // Firing load completed event
                fireChildrenLoadCompleted ( parent, realChildren );
            }
        } );
    }

    /**
     * Adds child nodes for the specified node.
     * This method might be used to manually change tree node children without causing any structure corruptions.
     *
     * @param parent   node to process
     * @param children new node children
     */
    public void addChildNodes ( final E parent, final List<E> children )
    {
        // Simply ignore if parent node is not yet loaded
        if ( !parent.isLoaded () )
        {
            return;
        }

        // Adding new raw children
        synchronized ( cacheLock )
        {
            List<E> cachedChildren = rawNodeChildrenCache.get ( parent.getId () );
            if ( cachedChildren == null )
            {
                cachedChildren = new ArrayList<E> ( children.size () );
                rawNodeChildrenCache.put ( parent.getId (), cachedChildren );
            }
            cachedChildren.addAll ( children );
            cacheNodesById ( children );
        }

        // Clearing nodes cache
        // That might be required in case nodes were moved inside of the tree
        clearNodeChildrenCache ( children, false );

        // Inserting nodes
        insertNodesIntoImpl ( children, parent, parent.getChildCount () );

        // Updating parent node sorting and filtering
        updateSortingAndFiltering ( parent );
    }

    /**
     * Removes specified node from parent node.
     *
     * @param node node to remove
     */
    @Override
    public void removeNodeFromParent ( final MutableTreeNode node )
    {
        // Simply ignore null nodes
        if ( node == null )
        {
            return;
        }

        final E childNode = ( E ) node;
        final E parentNode = ( E ) childNode.getParent ();

        // Simply ignore if parent node is null or not yet loaded
        if ( parentNode == null || !parentNode.isLoaded () )
        {
            return;
        }

        // Removing raw children
        synchronized ( cacheLock )
        {
            final List<E> children = rawNodeChildrenCache.get ( parentNode.getId () );
            if ( children != null )
            {
                children.remove ( childNode );
            }
        }

        // Clearing node cache
        clearNodeChildrenCache ( childNode, true );

        // Removing node children so they won't mess up anything when we place node back into tree
        childNode.removeAllChildren ();

        // Removing node from parent
        super.removeNodeFromParent ( node );

        // Updating parent node sorting and filtering
        updateSortingAndFiltering ( parentNode );

        // Removing image observer
        childNode.detachLoadIconObserver ();
    }

    // todo Implement when those methods will be separate from single one
    //    public void removeNodesFromParent ( List<E> nodes )
    //    {
    //        super.removeNodesFromParent ( nodes );
    //    }
    //
    //    public void removeNodesFromParent ( E[] nodes )
    //    {
    //        super.removeNodesFromParent ( nodes );
    //    }

    /**
     * Inserts new child node into parent node at the specified index.
     *
     * @param newChild new child node
     * @param parent   parent node
     * @param index    insert index
     */
    @Override
    public void insertNodeInto ( final MutableTreeNode newChild, final MutableTreeNode parent, final int index )
    {
        final E childNode = ( E ) newChild;
        final E parentNode = ( E ) parent;

        // Simply ignore if parent node is not yet loaded
        if ( !parentNode.isLoaded () )
        {
            return;
        }

        // Inserting new raw children
        synchronized ( cacheLock )
        {
            List<E> cachedChildren = rawNodeChildrenCache.get ( parentNode.getId () );
            if ( cachedChildren == null )
            {
                cachedChildren = new ArrayList<E> ( 1 );
                rawNodeChildrenCache.put ( parentNode.getId (), cachedChildren );
            }
            cachedChildren.add ( index, childNode );
            cacheNodeById ( childNode );
        }

        // Clearing node cache
        // That might be required in case nodes were moved inside of the tree
        clearNodeChildrenCache ( childNode, false );

        // Inserting node
        insertNodeIntoImpl ( childNode, parentNode, index );

        // Updating parent node sorting and filtering
        updateSortingAndFiltering ( parentNode );
    }

    /**
     * Inserts a list of child nodes into parent node.
     *
     * @param children list of new child nodes
     * @param parent   parent node
     * @param index    insert index
     */
    @Override
    public void insertNodesInto ( final List<E> children, final E parent, final int index )
    {
        // Simply ignore if parent node is not yet loaded
        if ( !parent.isLoaded () )
        {
            return;
        }

        // Inserting new raw children
        synchronized ( cacheLock )
        {
            List<E> cachedChildren = rawNodeChildrenCache.get ( parent.getId () );
            if ( cachedChildren == null )
            {
                cachedChildren = new ArrayList<E> ( 1 );
                rawNodeChildrenCache.put ( parent.getId (), cachedChildren );
            }
            cachedChildren.addAll ( index, children );
            cacheNodesById ( children );
        }

        // Clearing nodes cache
        // That might be required in case nodes were moved inside of the tree
        clearNodeChildrenCache ( children, false );

        // Performing actual nodes insertion
        insertNodesIntoImpl ( children, parent, index );

        // Updating parent node sorting and filtering
        updateSortingAndFiltering ( parent );
    }

    /**
     * Inserts an array of child nodes into parent node.
     *
     * @param children array of new child nodes
     * @param parent   parent node
     * @param index    insert index
     */
    @Override
    public void insertNodesInto ( final E[] children, final E parent, final int index )
    {
        // Simply ignore if parent node is not yet loaded
        if ( !parent.isLoaded () )
        {
            return;
        }

        // Inserting new raw children
        synchronized ( cacheLock )
        {
            List<E> cachedChildren = rawNodeChildrenCache.get ( parent.getId () );
            if ( cachedChildren == null )
            {
                cachedChildren = new ArrayList<E> ( 1 );
                rawNodeChildrenCache.put ( parent.getId (), cachedChildren );
            }
            for ( int i = children.length - 1; i >= 0; i-- )
            {
                cachedChildren.add ( index, children[ i ] );
            }
            cacheNodesById ( Arrays.asList ( children ) );
        }

        // Clearing nodes cache
        // That might be required in case nodes were moved inside of the tree
        clearNodeChildrenCache ( children, false );

        // Inserting nodes
        insertNodesIntoImpl ( children, parent, index );

        // Updating parent node sorting and filtering
        updateSortingAndFiltering ( parent );
    }

    /**
     * Inserts a child node into parent node.
     *
     * @param child  new child node
     * @param parent parent node
     * @param index  insert index
     */
    protected void insertNodeIntoImpl ( final E child, final E parent, final int index )
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
    protected void insertNodesIntoImpl ( final List<E> children, final E parent, final int index )
    {
        super.insertNodesInto ( children, parent, index );

        // Adding image observers
        for ( final E child : children )
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
    protected void insertNodesIntoImpl ( final E[] children, final E parent, final int index )
    {
        super.insertNodesInto ( children, parent, index );

        // Adding image observers
        for ( final E child : children )
        {
            child.attachLoadIconObserver ( tree );
        }
    }

    /**
     * Updates nodes sorting and filtering for all loaded nodes.
     */
    public void updateSortingAndFiltering ()
    {
        updateSortingAndFiltering ( getRoot (), true );
    }

    /**
     * Updates sorting and filtering for the specified node children.
     *
     * @param parentNode node which children sorting and filtering should be updated
     */
    public void updateSortingAndFiltering ( final E parentNode )
    {
        updateSortingAndFiltering ( parentNode, false );
    }

    /**
     * Updates sorting and filtering for the specified node children.
     *
     * @param parentNode  node which children sorting and filtering should be updated
     * @param recursively whether should update the whole children structure recursively or not
     */
    public void updateSortingAndFiltering ( final E parentNode, final boolean recursively )
    {
        // Process only this is not a root node
        // We don't need to update root sorting as there is always one root in the tree
        if ( parentNode != null )
        {
            updateSortingAndFilteringImpl ( parentNode, recursively, true );
        }
    }

    /**
     * Updates sorting and filtering for the specified node children.
     *
     * @param parentNode     node which children sorting and filtering should be updated
     * @param recursively    whether should update the whole children structure recursively or not
     * @param performUpdates whether tree updates should be triggered within this method
     */
    protected void updateSortingAndFilteringImpl ( final E parentNode, final boolean recursively, final boolean performUpdates )
    {
        // Process this action only if node children are already loaded and cached
        if ( parentNode.isLoaded () && rawNodeChildrenCache.containsKey ( parentNode.getId () ) )
        {
            // Children are already loaded, simply updating their sorting and filtering
            performSortingAndFiltering ( parentNode, recursively, performUpdates );
        }
        else if ( parentNode.isLoading () )
        {
            // Children are being loaded, wait until the operation finishes
            addAsyncTreeModelListener ( new AsyncTreeModelAdapter ()
            {
                @Override
                public void loadCompleted ( final AsyncUniqueNode parent, final List children )
                {
                    if ( parentNode.getId ().equals ( parent.getId () ) )
                    {
                        removeAsyncTreeModelListener ( this );
                        performSortingAndFiltering ( parentNode, recursively, performUpdates );
                    }
                }

                @Override
                public void loadFailed ( final AsyncUniqueNode parent, final Throwable cause )
                {
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
    protected void performSortingAndFiltering ( final E parentNode, final boolean recursively, final boolean performUpdates )
    {
        // todo Restore tree state only for the updated node
        // todo This also won't work if some of the children updates are delayed using listener
        // Saving tree state to restore it right after children update
        final TreeState treeState = tree.getTreeState ();

        // Updating node children sorting and filtering
        // Process this action only if node children are already loaded and cached
        final List<E> cachedChildren = rawNodeChildrenCache.get ( parentNode.getId () );
        if ( cachedChildren != null )
        {
            // Removing old children
            parentNode.removeAllChildren ();

            // Filtering and sorting raw children
            final List<E> children = filterAndSort ( parentNode, cachedChildren );

            // Inserting new children
            for ( final E child : children )
            {
                parentNode.add ( child );
            }
        }

        // Updating children's children
        if ( recursively )
        {
            for ( int i = 0; i < parentNode.getChildCount (); i++ )
            {
                updateSortingAndFilteringImpl ( ( E ) parentNode.getChildAt ( i ), true, false );
            }
        }

        // Performing tree updates
        if ( performUpdates )
        {
            // Forcing tree structure update for the node
            nodeStructureChanged ( parentNode );

            // Restoring tree state including all selections and expansions
            tree.setTreeState ( treeState );
        }
    }

    /**
     * Returns list of filtered and sorted raw children.
     *
     * @param parentNode parent node
     * @param children   children to filter and sort
     * @return list of filtered and sorted children
     */
    protected List<E> filterAndSort ( final E parentNode, List<E> children )
    {
        // Simply return an empty array if there is no children
        if ( children == null || children.size () == 0 )
        {
            return new ArrayList<E> ( 0 );
        }

        // Filter and sort children
        final Filter<E> filter = dataProvider.getChildrenFilter ( parentNode );
        final Comparator<E> comparator = dataProvider.getChildrenComparator ( parentNode );
        if ( filter != null )
        {
            final List<E> filtered = CollectionUtils.filter ( children, filter );
            if ( comparator != null )
            {
                Collections.sort ( filtered, comparator );
            }
            return filtered;
        }
        else
        {
            if ( comparator != null )
            {
                children = CollectionUtils.copy ( children );
                Collections.sort ( children, comparator );
            }
            return children;
        }
    }

    /**
     * Looks for the node with the specified ID in the tree model and returns it or null if it was not found.
     *
     * @param nodeId node ID
     * @return node with the specified ID or null if it was not found
     */
    public E findNode ( final String nodeId )
    {
        return nodeById.get ( nodeId );
    }

    /**
     * Returns nodes cache map copy.
     *
     * @return nodes cache map copy
     */
    public DoubleMap<String, E> getNodesCache ()
    {
        return MapUtils.copyDoubleMap ( nodeById );
    }

    /**
     * Returns list of all available asynchronous tree model listeners.
     *
     * @return asynchronous tree model listeners list
     */
    public List<AsyncTreeModelListener> getAsyncTreeModelListeners ()
    {
        synchronized ( modelListenersLock )
        {
            return CollectionUtils.copy ( asyncTreeModelListeners );
        }
    }

    /**
     * Adds new asynchronous tree model listener.
     *
     * @param listener asynchronous tree model listener to add
     */
    public void addAsyncTreeModelListener ( final AsyncTreeModelListener listener )
    {
        synchronized ( modelListenersLock )
        {
            asyncTreeModelListeners.add ( listener );
        }
    }

    /**
     * Removes asynchronous tree model listener.
     *
     * @param listener asynchronous tree model listener to remove
     */
    public void removeAsyncTreeModelListener ( final AsyncTreeModelListener listener )
    {
        synchronized ( modelListenersLock )
        {
            asyncTreeModelListeners.remove ( listener );
        }
    }

    /**
     * Fires children load start event.
     *
     * @param parent node which children are being loaded
     */
    protected void fireChildrenLoadStarted ( final E parent )
    {
        final List<AsyncTreeModelListener> listeners;
        synchronized ( modelListenersLock )
        {
            listeners = CollectionUtils.copy ( asyncTreeModelListeners );
        }
        for ( final AsyncTreeModelListener listener : listeners )
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
    protected void fireChildrenLoadCompleted ( final E parent, final List<E> children )
    {
        final List<AsyncTreeModelListener> listeners;
        synchronized ( modelListenersLock )
        {
            listeners = CollectionUtils.copy ( asyncTreeModelListeners );
        }
        for ( final AsyncTreeModelListener listener : listeners )
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
    protected void fireChildrenLoadFailed ( final E parent, final Throwable cause )
    {
        final List<AsyncTreeModelListener> listeners;
        synchronized ( modelListenersLock )
        {
            listeners = CollectionUtils.copy ( asyncTreeModelListeners );
        }
        for ( final AsyncTreeModelListener listener : listeners )
        {
            listener.loadFailed ( parent, cause );
        }
    }
}