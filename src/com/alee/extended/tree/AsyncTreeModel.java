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
import com.alee.utils.SwingUtils;
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
 */

public class AsyncTreeModel<E extends AsyncUniqueNode> extends WebTreeModel<E>
{
    /**
     * Cache key for root node.
     */
    protected static final String ROOT_CACHE = "root";

    /**
     * Asynchronous tree listeners.
     */
    protected List<AsyncTreeModelListener> asyncTreeModelListeners = new ArrayList<AsyncTreeModelListener> ();

    /**
     * Asynchronous tree that uses this model.
     */
    protected WebAsyncTree<E> tree;

    /**
     * Whether to load childs asynchronously or not.
     */
    private boolean asyncLoading = true;

    /**
     * Asynchronous tree data provider.
     */
    protected AsyncTreeDataProvider<E> dataProvider;

    /**
     * Root node cache.
     * Cached when root is requested for the first time.
     */
    protected E rootNode = null;

    /**
     * Nodes caching state.
     * If child nodes for some parent node are cached then this map contains "true" value under that parent node ID as a key.
     */
    protected Map<String, Boolean> nodeCached = new HashMap<String, Boolean> ();

    /**
     * Data provider childs cache.
     * This map contains raw childs without sorting and filtering returned by data provider.
     */
    protected Map<String, List<E>> rawNodeChildsCache = new HashMap<String, List<E>> ();

    /**
     * Lock object for cache changes.
     */
    protected final Object cacheLock = new Object ();

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
    public AsyncTreeModel ( WebAsyncTree<E> tree, AsyncTreeDataProvider<E> dataProvider )
    {
        super ( null );
        this.tree = tree;
        this.dataProvider = dataProvider;
    }

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
    public void setAsyncLoading ( boolean asyncLoading )
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
    public boolean isLeaf ( Object node )
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
    public int getChildCount ( Object parent )
    {
        final E node = ( E ) parent;
        if ( nodeCached.containsKey ( node.getId () ) )
        {
            return super.getChildCount ( parent );
        }
        else
        {
            return loadChilds ( node );
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
    public E getChild ( Object parent, int index )
    {
        final E node = ( E ) parent;
        if ( nodeCached.containsKey ( node.getId () ) )
        {
            return ( E ) super.getChild ( parent, index );
        }
        else
        {
            return null;
        }
    }

    /**
     * Reloads node childs.
     *
     * @param node node
     */
    @Override
    public void reload ( TreeNode node )
    {
        // Cleaning up node cache
        nodeCached.remove ( ( ( E ) node ).getId () );

        // Forcing childs reload
        super.reload ( node );
    }

    /**
     * Loads (or reloads) node childs and returns zero or childs count if async mode is off.
     *
     * @param parent node to load childs for
     * @return zero or childs count if async mode is off
     */
    protected int loadChilds ( final E parent )
    {
        // Checking if the node is busy already
        synchronized ( busyLock )
        {
            if ( parent.isBusy () )
            {
                return 0;
            }
            else
            {
                parent.setBusy ( true );
                nodeChanged ( parent );
            }
        }

        // Firing load started event
        fireChildsLoadStarted ( parent );

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
                    // Loading raw childs
                    final List<E> childs = dataProvider.getChilds ( parent );

                    // Caching raw childs
                    synchronized ( cacheLock )
                    {
                        rawNodeChildsCache.put ( parent.getId (), childs );
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
                                // Inserting loaded nodes
                                insertNodesInto ( realChilds, parent, 0 );
                            }

                            // Releasing node busy state
                            synchronized ( busyLock )
                            {
                                parent.setBusy ( false );
                                nodeChanged ( parent );
                            }

                            // Firing load completed event
                            fireChildsLoadCompleted ( parent, realChilds );
                        }
                    } );
                }
            } );
            return 0;
        }
        else
        {
            // Loading childs
            final List<E> childs = dataProvider.getChilds ( parent );

            // Checking if any nodes loaded
            if ( childs != null && childs.size () > 0 )
            {
                // Inserting loaded nodes
                insertNodesInto ( childs, parent, 0 );
            }

            // Updating cache
            synchronized ( cacheLock )
            {
                nodeCached.put ( parent.getId (), true );
            }

            // Releasing node busy state
            synchronized ( busyLock )
            {
                parent.setBusy ( false );
                nodeChanged ( parent );
            }

            // Firing load completed event
            fireChildsLoadCompleted ( parent, childs );

            return childs.size ();
        }
    }

    /**
     * Sets child nodes for the specified node.
     * This method might be used to manually change tree node childs without causing any structure corruptions.
     *
     * @param parent   node to process
     * @param children new node children
     */
    public void setChildNodes ( final E parent, final List<E> children )
    {
        // Check if the node is busy already
        synchronized ( busyLock )
        {
            if ( parent.isBusy () )
            {
                return;
            }
            else
            {
                parent.setBusy ( true );
                nodeChanged ( parent );
            }
        }

        // Caching raw childs
        synchronized ( cacheLock )
        {
            rawNodeChildsCache.put ( parent.getId (), children );
        }

        // Filtering and sorting raw childs
        final List<E> realChilds = filterAndSort ( parent, children );

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
                    // Inserting loaded nodes
                    insertNodesInto ( realChilds, parent, 0 );
                }

                // Release node busy state
                synchronized ( busyLock )
                {
                    parent.setBusy ( false );
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
     * @param parent   node to process
     * @param children new node children
     */
    public void addChildNodes ( final E parent, final List<E> children )
    {
        // Adding new raw childs
        synchronized ( cacheLock )
        {
            List<E> childs = rawNodeChildsCache.get ( parent.getId () );
            if ( childs == null )
            {
                childs = new ArrayList<E> ( children.size () );
                rawNodeChildsCache.put ( parent.getId (), childs );
                nodeCached.put ( parent.getId (), true );
            }
            childs.addAll ( children );
        }

        insertNodesInto ( children, parent, parent.getChildCount () );

        updateSortingAndFiltering ( parent );
    }

    /**
     * Removes specified node from parent node.
     *
     * @param node node to remove
     */
    @Override
    public void removeNodeFromParent ( MutableTreeNode node )
    {
        // Removing raw childs
        synchronized ( cacheLock )
        {
            E childNode = ( E ) node;
            E parentNode = ( E ) childNode.getParent ();
            List<E> childs = rawNodeChildsCache.get ( parentNode.getId () );
            if ( childs != null )
            {
                childs.remove ( childNode );
            }
        }

        super.removeNodeFromParent ( node );
    }

    /**
     * Inserts new child node into parent node at the specified index.
     *
     * @param newChild new child node
     * @param parent   parent node
     * @param index    insert index
     */
    @Override
    public void insertNodeInto ( MutableTreeNode newChild, MutableTreeNode parent, int index )
    {
        super.insertNodeInto ( newChild, parent, index );

        // Adding image observer
        registerObserver ( ( E ) newChild );
    }

    /**
     * Inserts a list of child nodes into parent node.
     *
     * @param children list of new child nodes
     * @param parent   parent node
     * @param index    insert index
     */
    @Override
    public void insertNodesInto ( List<E> children, E parent, int index )
    {
        super.insertNodesInto ( children, parent, index );

        // Adding image observers
        for ( E newChild : children )
        {
            registerObserver ( newChild );
        }
    }

    /**
     * Updates nodes sorting and filtering for all loaded nodes.
     */
    public void updateSortingAndFiltering ()
    {
        // Saving tree state to restore it right after childs update
        TreeState treeState = tree.getTreeState ();

        // Updating node childs
        final E node = getRoot ();
        updateSortingAndFilteringRecursivelyImpl ( node );
        nodeStructureChanged ( node );

        // Restoring tree state including all selections and expansions
        tree.setTreeState ( treeState );
    }

    /**
     * Updates sorting and filtering for the specified node childs.
     */
    public void updateSortingAndFiltering ( E node )
    {
        // Process this action only if node childs are already loaded and cached
        if ( rawNodeChildsCache.containsKey ( node.getId () ) )
        {
            // Saving tree state to restore it right after childs update
            TreeState treeState = tree.getTreeState ();

            // Updating node childs
            updateSortingAndFilteringImpl ( node );
            nodeStructureChanged ( node );

            // Restoring tree state including all selections and expansions
            tree.setTreeState ( treeState );
        }
    }

    /**
     * Updates node childs using current comparator and filter.
     *
     * @param node node to update
     */
    protected void updateSortingAndFilteringRecursivelyImpl ( E node )
    {
        updateSortingAndFilteringImpl ( node );
        for ( int i = 0; i < node.getChildCount (); i++ )
        {
            updateSortingAndFilteringRecursivelyImpl ( ( E ) node.getChildAt ( i ) );
        }
    }

    /**
     * Updates node childs recursively using current comparator and filter.
     *
     * @param node node to update
     */
    protected void updateSortingAndFilteringImpl ( E node )
    {
        // Retrieving raw childs
        final List<E> childs = rawNodeChildsCache.get ( node.getId () );

        // Process this action only if node childs are already loaded and cached
        if ( childs != null )
        {
            // Removing old children
            node.removeAllChildren ();

            // Filtering and sorting raw childs
            final List<E> realChilds = filterAndSort ( node, childs );

            // Inserting new childs
            for ( E child : realChilds )
            {
                node.add ( child );
            }
        }
    }

    /**
     * Performs raw childs filtering and sorting before they can be passed into real tree and returns list of filtered and sorted childs.
     *
     * @param childs childs to filter and sort
     * @return list of filtered and sorted childs
     */
    protected List<E> filterAndSort ( E node, List<E> childs )
    {
        // Simply return an empty array if there is no childs
        if ( childs.size () == 0 )
        {
            return new ArrayList<E> ( 0 );
        }

        // Filter and sort childs
        Filter<E> filter = dataProvider.getChildsFilter ( node );
        Comparator<E> comparator = dataProvider.getChildsComparator ( node );
        if ( filter != null )
        {
            List<E> filtered = CollectionUtils.filter ( childs, filter );
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
     * Registers image observer for specified node loader icon.
     *
     * @param node node
     */
    protected void registerObserver ( E node )
    {
        final ImageIcon loaderIcon = node.getLoaderIcon ();
        if ( loaderIcon != null )
        {
            loaderIcon.setImageObserver ( new NodeImageObserver ( tree, node ) );
        }
    }

    /**
     * Returns all available asynchronous tree model listeners list.
     *
     * @return asynchronous tree model listeners list
     */
    public List<AsyncTreeModelListener> getAsyncTreeModelListeners ()
    {
        return asyncTreeModelListeners;
    }

    /**
     * Adds new asynchronous tree model listener.
     *
     * @param listener asynchronous tree model listener to add
     */
    public void addAsyncTreeModelListener ( AsyncTreeModelListener listener )
    {
        asyncTreeModelListeners.add ( listener );
    }

    /**
     * Removes asynchronous tree model listener.
     *
     * @param listener asynchronous tree model listener to remove
     */
    public void removeAsyncTreeModelListener ( AsyncTreeModelListener listener )
    {
        asyncTreeModelListeners.add ( listener );
    }

    /**
     * Fires childs load start event.
     *
     * @param parent node which childs are being loaded
     */
    protected void fireChildsLoadStarted ( E parent )
    {
        for ( AsyncTreeModelListener listener : CollectionUtils.copy ( asyncTreeModelListeners ) )
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
    protected void fireChildsLoadCompleted ( E parent, List<E> childs )
    {
        for ( AsyncTreeModelListener listener : CollectionUtils.copy ( asyncTreeModelListeners ) )
        {
            listener.childsLoadCompleted ( parent, childs );
        }
    }
}