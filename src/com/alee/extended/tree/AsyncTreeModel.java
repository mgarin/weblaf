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

import com.alee.laf.tree.WebTreeModel;
import com.alee.utils.CollectionUtils;
import com.alee.utils.SwingUtils;

import javax.swing.*;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;
import java.awt.*;
import java.awt.image.ImageObserver;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Special model for asynchronous tree that provides asynchronous data loading.
 * This class also controls the loading animation in elements.
 *
 * @param <E> custom node type
 * @author Mikle Garin
 * @since 1.4
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
     * If child nodes for some parent node are cached then this map contains <code>true</code> value under that parent node ID as a key.
     */
    protected Map<String, Boolean> nodeCached = new HashMap<String, Boolean> ();

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
     * @param node node
     * @return zero or childs count if async mode is off
     */
    protected int loadChilds ( final E node )
    {
        // Checking if the node is busy already
        synchronized ( busyLock )
        {
            if ( node.isBusy () )
            {
                return 0;
            }
            else
            {
                node.setBusy ( true );
                nodeChanged ( node );
            }
        }

        // Firing load started event
        fireChildsLoadStarted ( node );

        // Removing all old childs if such exist
        final int childCount = node.getChildCount ();
        if ( childCount > 0 )
        {
            final int[] indices = new int[ childCount ];
            final Object[] childs = new Object[ childCount ];
            for ( int i = childCount - 1; i >= 0; i-- )
            {
                indices[ i ] = i;
                childs[ i ] = node.getChildAt ( i );
                node.remove ( i );
            }
            nodesWereRemoved ( node, indices, childs );
        }

        // Loading node childs
        if ( asyncLoading )
        {
            // Executing childs load in a separate thread to avoid locking EDT
            // This queue will also take care of amount of threads to execute async trees requests
            AsyncTreeQueue.execute ( tree, new Runnable ()
            {
                public void run ()
                {
                    // Loading childs
                    final List<E> childs = dataProvider.getChilds ( node );

                    // Updating cache
                    synchronized ( cacheLock )
                    {
                        nodeCached.put ( node.getId (), true );
                    }

                    // Performing UI updates in EDT
                    SwingUtils.invokeLater ( new Runnable ()
                    {
                        public void run ()
                        {
                            // Checking if any nodes loaded
                            if ( childs != null && childs.size () > 0 )
                            {
                                // Inserting loaded nodes
                                insertNodesInto ( childs, node, 0 );
                            }

                            // Releasing node busy state
                            synchronized ( busyLock )
                            {
                                node.setBusy ( false );
                                nodeChanged ( node );
                            }

                            // Firing load completed event
                            fireChildsLoadCompleted ( node, childs );
                        }
                    } );
                }
            } );
            return 0;
        }
        else
        {
            // Loading childs
            final List<E> childs = dataProvider.getChilds ( node );

            // Updating cache
            synchronized ( cacheLock )
            {
                nodeCached.put ( node.getId (), true );
            }

            // Checking if any nodes loaded
            if ( childs != null && childs.size () > 0 )
            {
                // Inserting loaded nodes
                insertNodesInto ( childs, node, 0 );
            }

            // Releasing node busy state
            synchronized ( busyLock )
            {
                node.setBusy ( false );
                nodeChanged ( node );
            }

            // Firing load completed event
            fireChildsLoadCompleted ( node, childs );

            return childs.size ();
        }
    }

    /**
     * Sets child nodes for the specified node.
     * This method might be used to manually change tree node childs without causing any structure corruptions.
     *
     * @param node   node to process
     * @param childs new node childs
     */
    public void setChildNodes ( final E node, final List<E> childs )
    {
        // Check if the node is busy already
        synchronized ( busyLock )
        {
            if ( node.isBusy () )
            {
                return;
            }
            else
            {
                node.setBusy ( true );
                nodeChanged ( node );
            }
        }

        // Updating cache
        synchronized ( cacheLock )
        {
            nodeCached.put ( node.getId (), true );
        }

        // Performing UI updates in EDT
        SwingUtils.invokeLater ( new Runnable ()
        {
            public void run ()
            {
                // Checking if any nodes loaded
                if ( childs != null && childs.size () > 0 )
                {
                    // Inserting loaded nodes
                    insertNodesInto ( childs, node, 0 );
                }

                // Release node busy state
                synchronized ( busyLock )
                {
                    node.setBusy ( false );
                    nodeChanged ( node );
                }

                // Firing load completed event
                fireChildsLoadCompleted ( node, childs );
            }
        } );
    }

    /**
     * Inserts new child node into parent node at the specified index.
     *
     * @param newChild new child node
     * @param parent   parent node
     * @param index    insert index
     */
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
     * Custom image observer class for animated loader icons.
     *
     * @param <E> custom node type
     */
    protected class NodeImageObserver<E extends AsyncUniqueNode> implements ImageObserver
    {
        // Asynchronous tree
        private WebAsyncTree tree;

        // Observed node
        private E node;

        /**
         * Constructs default node observer.
         *
         * @param tree asynchronous tree
         * @param node observed node
         */
        NodeImageObserver ( WebAsyncTree tree, E node )
        {
            this.tree = tree;
            this.node = node;
        }

        /**
         * Updates loader icon view in tree cell.
         *
         * @param img   image being observed
         * @param flags bitwise inclusive OR of flags
         * @param x     x coordinate
         * @param y     y coordinate
         * @param w     width
         * @param h     height
         * @return false if the infoflags indicate that the image is completely loaded, true otherwise
         */
        public boolean imageUpdate ( Image img, int flags, int x, int y, int w, int h )
        {
            if ( node.isBusy () && ( flags & ( FRAMEBITS | ALLBITS ) ) != 0 )
            {
                final Rectangle rect = tree.getPathBounds ( node.getTreePath () );
                if ( rect != null )
                {
                    tree.repaint ( rect );
                }
            }
            return ( flags & ( ALLBITS | ABORT ) ) == 0;
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