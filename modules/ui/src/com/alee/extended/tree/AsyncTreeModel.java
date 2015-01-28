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

import javax.swing.*;
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
     * Whether to load childs asynchronously or not.
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
     * Nodes cached states (parent ID -> childs cached state).
     * If child nodes for some parent node are cached then this map contains "true" value under that parent node ID as a key.
     */
    protected final Map<String, Boolean> nodeCached = new HashMap<String, Boolean> ();

    /**
     * Cache for childs nodes returned by data provider (parent ID -> list of raw child nodes).
     * This map contains raw childs which weren't affected by sorting and filtering operations.
     * If childs needs to be re-sorted or re-filtered they are simply taken from the cache and re-organized once again.
     */
    protected final Map<String, List<E>> rawNodeChildsCache = new HashMap<String, List<E>> ();

    /**
     * Direct nodes cache (node ID -> node).
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
     * Returns whether childs are loaded asynchronously or not.
     *
     * @return true if childs are loaded asynchronously, false otherwise
     */
    public boolean isAsyncLoading ()
    {
        return asyncLoading;
    }

    /**
     * Sets whether to load childs asynchronously or not.
     *
     * @param asyncLoading whether to load childs asynchronously or not
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
            registerObserver ( rootNode );
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
     * Returns childs count for specified node.
     *
     * @param parent parent node
     * @return childs count
     */
    @Override
    public int getChildCount ( final Object parent )
    {
        final E node = ( E ) parent;
        if ( isLeaf ( node ) )
        {
            return 0;
        }
        else if ( areChildsLoaded ( node ) )
        {
            return super.getChildCount ( parent );
        }
        else
        {
            return loadChildsCount ( node );
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
        if ( areChildsLoaded ( node ) )
        {
            return ( E ) super.getChild ( parent, index );
        }
        else
        {
            return null;
        }
    }

    /**
     * Returns whether childs for the specified node are already loaded or not.
     *
     * @param node node to process
     * @return true if childs for the specified node are already loaded, false otherwise
     */
    public boolean areChildsLoaded ( final E node )
    {
        synchronized ( cacheLock )
        {
            final Boolean cached = nodeCached.get ( node.getId () );
            return cached != null && cached;
        }
    }

    /**
     * Reloads node childs.
     *
     * @param node node
     */
    @Override
    public void reload ( final TreeNode node )
    {
        // Cancels tree editing
        tree.cancelEditing ();

        // Cleaning up nodes cache
        clearNodeChildsCache ( ( E ) node, false );

        // Forcing childs reload
        super.reload ( node );
    }

    /**
     * Clears node and all of its child nodes childs cached states.
     *
     * @param node      node to clear cache for
     * @param clearNode whether should clear node cache or not
     */
    protected void clearNodeChildsCache ( final E node, final boolean clearNode )
    {
        synchronized ( cacheLock )
        {
            // Clears node cache
            if ( clearNode )
            {
                nodeById.remove ( node.getId () );
            }

            // Clears node childs cached state
            nodeCached.remove ( node.getId () );

            // Clears node raw childs cache
            final List<E> children = rawNodeChildsCache.remove ( node.getId () );

            // Clears chld nodes cache
            if ( children != null )
            {
                clearNodeChildsCache ( children, true );
            }
        }
    }

    /**
     * Clears nodes childs cached states.
     *
     * @param nodes      nodes to clear cache for
     * @param clearNodes whether should clear nodes cache or not
     */
    protected void clearNodeChildsCache ( final List<E> nodes, final boolean clearNodes )
    {
        synchronized ( cacheLock )
        {
            for ( final E node : nodes )
            {
                clearNodeChildsCache ( node, clearNodes );
            }
        }
    }

    /**
     * Clears nodes childs cached states.
     *
     * @param nodes      nodes to clear cache for
     * @param clearNodes whether should clear nodes cache or not
     */
    protected void clearNodeChildsCache ( final E[] nodes, final boolean clearNodes )
    {
        synchronized ( cacheLock )
        {
            for ( final E node : nodes )
            {
                clearNodeChildsCache ( node, clearNodes );
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
     * Loads (or reloads) node childs and returns zero or childs count if async mode is off.
     * This is base method that uses installed AsyncTreeDataProvider to retrieve tree node childs.
     *
     * @param parent node to load childs for
     * @return zero or childs count if async mode is off
     * @see AsyncTreeDataProvider#loadChilds(AsyncUniqueNode, ChildsListener)
     */
    protected int loadChildsCount ( final E parent )
    {
        // todo Use when moved to JDK8
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
        fireChildsLoadStarted ( parent );

        // todo This should actually be called on node reload?
        // Removing all old childs if such exist
        final int childCount = parent.getChildCount ();
        if ( childCount > 0 )
        {
            final int[] indices = new int[ childCount ];
            final Object[] childs = new Object[ childCount ];
            for ( int i = childCount - 1; i >= 0; i-- )
            {
                indices[ i ] = i;
                childs[ i ] = parent.getChildAt ( i );
                parent.remove ( i );
            }
            nodesWereRemoved ( parent, indices, childs );
        }

        // Loading node childs
        if ( asyncLoading )
        {
            // Executing childs load in a separate thread to avoid locking EDT
            // This queue will also take care of amount of threads to execute async trees requests
            AsyncTreeQueue.execute ( tree, new Runnable ()
            {
                @Override
                public void run ()
                {
                    // Loading childs
                    dataProvider.loadChilds ( parent, new ChildsListener<E> ()
                    {
                        @Override
                        public void childsLoadCompleted ( final List<E> childs )
                        {
                            // Caching raw childs
                            synchronized ( cacheLock )
                            {
                                rawNodeChildsCache.put ( parent.getId (), childs );
                                cacheNodesById ( childs );
                            }

                            // Filtering and sorting raw childs
                            final List<E> realChilds = filterAndSort ( parent, childs );

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
                                    if ( realChilds != null && realChilds.size () > 0 )
                                    {
                                        // Inserting loaded nodes
                                        insertNodesIntoImpl ( realChilds, parent, 0 );
                                    }

                                    // Releasing node busy state
                                    synchronized ( busyLock )
                                    {
                                        parent.setState ( AsyncNodeState.loaded );
                                        nodeChanged ( parent );
                                    }

                                    // Firing load completed event
                                    fireChildsLoadCompleted ( parent, realChilds );
                                }
                            } );
                        }

                        @Override
                        public void childsLoadFailed ( final Throwable cause )
                        {
                            // Caching childs
                            synchronized ( cacheLock )
                            {
                                rawNodeChildsCache.put ( parent.getId (), new ArrayList<E> ( 0 ) );
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
                                    fireChildsLoadFailed ( parent, cause );
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
            // Loading childs
            dataProvider.loadChilds ( parent, new ChildsListener<E> ()
            {
                @Override
                public void childsLoadCompleted ( final List<E> childs )
                {
                    // Caching raw childs
                    synchronized ( cacheLock )
                    {
                        rawNodeChildsCache.put ( parent.getId (), childs );
                        cacheNodesById ( childs );
                    }

                    // Filtering and sorting raw childs
                    final List<E> realChilds = filterAndSort ( parent, childs );

                    // Updating cache
                    synchronized ( cacheLock )
                    {
                        nodeCached.put ( parent.getId (), true );
                    }

                    // Checking if any nodes loaded
                    if ( realChilds != null && realChilds.size () > 0 )
                    {
                        // Inserting loaded nodes
                        insertNodesIntoImpl ( realChilds, parent, 0 );
                    }

                    // Releasing node busy state
                    synchronized ( busyLock )
                    {
                        parent.setState ( AsyncNodeState.loaded );
                        nodeChanged ( parent );
                    }

                    // Firing load completed event
                    fireChildsLoadCompleted ( parent, realChilds );
                }

                @Override
                public void childsLoadFailed ( final Throwable cause )
                {
                    // Caching childs
                    synchronized ( cacheLock )
                    {
                        rawNodeChildsCache.put ( parent.getId (), new ArrayList<E> ( 0 ) );
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
                    fireChildsLoadFailed ( parent, cause );
                }
            } );
            return parent.getChildCount ();
        }
    }

    /**
     * Sets child nodes for the specified node.
     * This method might be used to manually change tree node childs without causing any structure corruptions.
     *
     * @param parent node to process
     * @param childs new node childs
     */
    public void setChildNodes ( final E parent, final List<E> childs )
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

        // Caching raw childs
        synchronized ( cacheLock )
        {
            rawNodeChildsCache.put ( parent.getId (), childs );
            cacheNodesById ( childs );
        }

        // Filtering and sorting raw childs
        final List<E> realChilds = filterAndSort ( parent, childs );

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
                if ( realChilds != null && realChilds.size () > 0 )
                {
                    // Clearing raw nodes cache
                    // That might be required in case nodes were moved inside of the tree
                    clearNodeChildsCache ( childs, false );

                    // Inserting nodes
                    insertNodesIntoImpl ( realChilds, parent, 0 );
                }

                // Release node busy state
                synchronized ( busyLock )
                {
                    parent.setState ( AsyncNodeState.loaded );
                    nodeChanged ( parent );
                }

                // Firing load completed event
                fireChildsLoadCompleted ( parent, realChilds );
            }
        } );
    }

    /**
     * Adds child nodes for the specified node.
     * This method might be used to manually change tree node childs without causing any structure corruptions.
     *
     * @param parent node to process
     * @param childs new node childs
     */
    public void addChildNodes ( final E parent, final List<E> childs )
    {
        // Simply ignore if parent node is not yet loaded
        if ( !parent.isLoaded () )
        {
            return;
        }

        // Adding new raw childs
        synchronized ( cacheLock )
        {
            List<E> cachedChilds = rawNodeChildsCache.get ( parent.getId () );
            if ( cachedChilds == null )
            {
                cachedChilds = new ArrayList<E> ( childs.size () );
                rawNodeChildsCache.put ( parent.getId (), cachedChilds );
            }
            cachedChilds.addAll ( childs );
            cacheNodesById ( childs );
        }

        // Clearing nodes cache
        // That might be required in case nodes were moved inside of the tree
        clearNodeChildsCache ( childs, false );

        // Inserting nodes
        insertNodesIntoImpl ( childs, parent, parent.getChildCount () );

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

        // Removing raw childs
        synchronized ( cacheLock )
        {
            final List<E> childs = rawNodeChildsCache.get ( parentNode.getId () );
            if ( childs != null )
            {
                childs.remove ( childNode );
            }
        }

        // Clearing node cache
        clearNodeChildsCache ( childNode, true );

        // Removing node childs so they won't mess up anything when we place node back into tree
        childNode.removeAllChildren ();

        // Removing node from parent
        super.removeNodeFromParent ( node );

        // Updating parent node sorting and filtering
        updateSortingAndFiltering ( parentNode );
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

        // Inserting new raw childs
        synchronized ( cacheLock )
        {
            List<E> childs = rawNodeChildsCache.get ( parentNode.getId () );
            if ( childs == null )
            {
                childs = new ArrayList<E> ( 1 );
                rawNodeChildsCache.put ( parentNode.getId (), childs );
            }
            childs.add ( index, childNode );
            cacheNodeById ( childNode );
        }

        // Clearing node cache
        // That might be required in case nodes were moved inside of the tree
        clearNodeChildsCache ( childNode, false );

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

        // Inserting new raw childs
        synchronized ( cacheLock )
        {
            List<E> childs = rawNodeChildsCache.get ( parent.getId () );
            if ( childs == null )
            {
                childs = new ArrayList<E> ( 1 );
                rawNodeChildsCache.put ( parent.getId (), childs );
            }
            childs.addAll ( index, children );
            cacheNodesById ( children );
        }

        // Clearing nodes cache
        // That might be required in case nodes were moved inside of the tree
        clearNodeChildsCache ( children, false );

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

        // Inserting new raw childs
        synchronized ( cacheLock )
        {
            List<E> childs = rawNodeChildsCache.get ( parent.getId () );
            if ( childs == null )
            {
                childs = new ArrayList<E> ( 1 );
                rawNodeChildsCache.put ( parent.getId (), childs );
            }
            for ( int i = children.length - 1; i >= 0; i-- )
            {
                childs.add ( index, children[ i ] );
            }
            cacheNodesById ( Arrays.asList ( children ) );
        }

        // Clearing nodes cache
        // That might be required in case nodes were moved inside of the tree
        clearNodeChildsCache ( children, false );

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
        registerObserver ( child );
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
        registerObservers ( children );
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
        registerObservers ( children );
    }

    /**
     * Updates nodes sorting and filtering for all loaded nodes.
     */
    public void updateSortingAndFiltering ()
    {
        updateSortingAndFiltering ( getRoot (), true );
    }

    /**
     * Updates sorting and filtering for the specified node childs.
     *
     * @param parentNode node which childs sorting and filtering should be updated
     */
    public void updateSortingAndFiltering ( final E parentNode )
    {
        updateSortingAndFiltering ( parentNode, false );
    }

    /**
     * Updates sorting and filtering for the specified node childs.
     *
     * @param parentNode  node which childs sorting and filtering should be updated
     * @param recursively whether should update the whole childs structure recursively or not
     */
    public void updateSortingAndFiltering ( final E parentNode, final boolean recursively )
    {
        // Process only this is not a root node
        // We don't need to update root sorting as there is always one root in the tree
        if ( parentNode != null )
        {
            // Process this action only if node childs are already loaded and cached
            if ( parentNode.isLoaded () && rawNodeChildsCache.containsKey ( parentNode.getId () ) )
            {
                // Childs are already loaded, simply updating their sorting and filtering
                performSortingAndFiltering ( parentNode, recursively );
            }
            else if ( parentNode.isLoading () )
            {
                // Childs are being loaded, wait until the operation finishes
                addAsyncTreeModelListener ( new AsyncTreeModelAdapter ()
                {
                    @Override
                    public void childsLoadCompleted ( final AsyncUniqueNode parent, final List childs )
                    {
                        if ( parentNode.getId ().equals ( parent.getId () ) )
                        {
                            removeAsyncTreeModelListener ( this );
                            performSortingAndFiltering ( parentNode, recursively );
                        }
                    }

                    @Override
                    public void childsLoadFailed ( final AsyncUniqueNode parent, final Throwable cause )
                    {
                        if ( parentNode.getId ().equals ( parent.getId () ) )
                        {
                            removeAsyncTreeModelListener ( this );
                        }
                    }
                } );
            }
        }
    }

    /**
     * Updates node childs using current comparator and filter.
     * Updates the whole node childs structure if recursive update requested.
     *
     * @param parentNode  node which childs sorting and filtering should be updated
     * @param recursively whether should update the whole childs structure recursively or not
     */
    protected void performSortingAndFiltering ( final E parentNode, final boolean recursively )
    {
        // todo Restore tree state only for the updated node
        // Saving tree state to restore it right after childs update
        final TreeState treeState = tree.getTreeState ();

        // Updating root node childs
        if ( recursively )
        {
            performSortingAndFilteringRecursivelyImpl ( parentNode );
        }
        else
        {
            performSortingAndFilteringImpl ( parentNode );
        }
        nodeStructureChanged ( parentNode );

        // Restoring tree state including all selections and expansions
        tree.setTreeState ( treeState );
    }

    /**
     * Updates node childs using current comparator and filter.
     *
     * @param parentNode node to update
     */
    protected void performSortingAndFilteringRecursivelyImpl ( final E parentNode )
    {
        performSortingAndFilteringImpl ( parentNode );
        for ( int i = 0; i < parentNode.getChildCount (); i++ )
        {
            performSortingAndFilteringRecursivelyImpl ( ( E ) parentNode.getChildAt ( i ) );
        }
    }

    /**
     * Updates node childs recursively using current comparator and filter.
     *
     * @param parentNode node to update
     */
    protected void performSortingAndFilteringImpl ( final E parentNode )
    {
        // Retrieving raw childs
        final List<E> childs = rawNodeChildsCache.get ( parentNode.getId () );

        // Process this action only if node childs are already loaded and cached
        if ( childs != null )
        {
            // Removing old children
            parentNode.removeAllChildren ();

            // Filtering and sorting raw childs
            final List<E> realChilds = filterAndSort ( parentNode, childs );

            // Inserting new childs
            for ( final E child : realChilds )
            {
                parentNode.add ( child );
            }
        }
    }

    /**
     * Performs raw childs filtering and sorting before they can be passed into real tree and returns list of filtered and sorted childs.
     *
     * @param childs childs to filter and sort
     * @return list of filtered and sorted childs
     */
    protected List<E> filterAndSort ( final E parentNode, List<E> childs )
    {
        // Simply return an empty array if there is no childs
        if ( childs == null || childs.size () == 0 )
        {
            return new ArrayList<E> ( 0 );
        }

        // Filter and sort childs
        final Filter<E> filter = dataProvider.getChildsFilter ( parentNode );
        final Comparator<E> comparator = dataProvider.getChildsComparator ( parentNode );
        if ( filter != null )
        {
            final List<E> filtered = CollectionUtils.filter ( childs, filter );
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
                childs = CollectionUtils.copy ( childs );
                Collections.sort ( childs, comparator );
            }
            return childs;
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
     * Registers image observer for loader icons of the specified nodes.
     *
     * @param nodes nodes
     */
    protected void registerObservers ( final List<E> nodes )
    {
        for ( final E newChild : nodes )
        {
            registerObserver ( newChild );
        }
    }

    /**
     * Registers image observer for loader icons of the specified nodes.
     *
     * @param nodes nodes
     */
    protected void registerObservers ( final E[] nodes )
    {
        for ( final E newChild : nodes )
        {
            registerObserver ( newChild );
        }
    }

    /**
     * Registers image observer for loader icon of the specified node.
     *
     * @param node node
     */
    protected void registerObserver ( final E node )
    {
        final ImageIcon loaderIcon = node.getLoaderIcon ();
        if ( loaderIcon != null )
        {
            loaderIcon.setImageObserver ( new NodeImageObserver ( tree, node ) );
        }
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
     * Fires childs load start event.
     *
     * @param parent node which childs are being loaded
     */
    protected void fireChildsLoadStarted ( final E parent )
    {
        final List<AsyncTreeModelListener> listeners;
        synchronized ( modelListenersLock )
        {
            listeners = CollectionUtils.copy ( asyncTreeModelListeners );
        }
        for ( final AsyncTreeModelListener listener : listeners )
        {
            listener.childsLoadStarted ( parent );
        }
    }

    /**
     * Fires childs load complete event.
     *
     * @param parent node which childs were loaded
     * @param childs loaded child nodes
     */
    protected void fireChildsLoadCompleted ( final E parent, final List<E> childs )
    {
        final List<AsyncTreeModelListener> listeners;
        synchronized ( modelListenersLock )
        {
            listeners = CollectionUtils.copy ( asyncTreeModelListeners );
        }
        for ( final AsyncTreeModelListener listener : listeners )
        {
            listener.childsLoadCompleted ( parent, childs );
        }
    }

    /**
     * Fires childs load failed event.
     *
     * @param parent node which childs were loaded
     * @param cause  childs load failure cause
     */
    protected void fireChildsLoadFailed ( final E parent, final Throwable cause )
    {
        final List<AsyncTreeModelListener> listeners;
        synchronized ( modelListenersLock )
        {
            listeners = CollectionUtils.copy ( asyncTreeModelListeners );
        }
        for ( final AsyncTreeModelListener listener : listeners )
        {
            listener.childsLoadFailed ( parent, cause );
        }
    }
}