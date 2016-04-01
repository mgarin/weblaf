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
import com.alee.laf.tree.UniqueNode;
import com.alee.laf.tree.WebTree;
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
 * @author Mikle Garin
 * @see com.alee.extended.tree.WebExTree
 * @see com.alee.extended.tree.ExTreeDataProvider
 */

public class ExTreeModel<E extends UniqueNode> extends WebTreeModel<E>
{
    /**
     * Ex tree that uses this model.
     */
    protected final WebTree<E> tree;

    /**
     * Ex tree data provider.
     */
    protected final ExTreeDataProvider<E> dataProvider;

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
     * Constructs default ex tree model using custom data provider.
     *
     * @param tree         asynchronous tree
     * @param dataProvider data provider
     */
    public ExTreeModel ( final WebTree<E> tree, final ExTreeDataProvider<E> dataProvider )
    {
        super ( null );
        this.tree = tree;
        this.dataProvider = dataProvider;
        loadTreeData ( getRootNode () );
    }

    /**
     * Returns ex tree data provider.
     *
     * @return data provider
     */
    public ExTreeDataProvider<E> getDataProvider ()
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
        return ( E ) super.getChild ( parent, index );
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
     * Forces model to cache the whole structure so any node can be accessed right away.
     * Note that this might take some time in case tree structure is large as it will be fully loaded.
     * Though this doesn't force any repaints or other visual updates, so the speed depends only on ExTreeDataProvider.
     * <p>
     * This method is mostly used to ensure that at any given time {@link com.alee.extended.tree.WebExTree} has all of its nodes.
     * That heavily simplifies work with the tree in case you need to access random nodes in the tree directly.
     * In case this is not your goal it is probably better to use {@link com.alee.extended.tree.WebAsyncTree}.
     *
     * @param node node to load data for
     */
    protected void loadTreeData ( final E node )
    {
        // Simply retrieving children count
        // This method uses cache so it won't force children reload when it is not needed
        getChildCount ( node );
    }

    /**
     * Reloads node children.
     *
     * @param node node
     */
    @Override
    public void reload ( final TreeNode node )
    {
        final E reloadedNode = ( E ) node;

        // Cancels tree editing
        tree.cancelEditing ();

        // Cleaning up nodes cache
        clearNodeChildrenCache ( reloadedNode, false );

        // Removing all old children if such exist
        // We don't need to inform about child nodes removal here due to later structural update call
        reloadedNode.removeAllChildren ();

        // Forcing children reload
        super.reload ( reloadedNode );

        // Forcing structure reload
        loadTreeData ( reloadedNode );
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
     */
    protected int loadChildren ( final E parent )
    {
        // Loading children
        final List<E> children = dataProvider.getChildren ( parent );

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

        return parent.getChildCount ();
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

        // Simply ignore if parent node is null
        if ( parentNode == null )
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

        // Inserting new raw children
        synchronized ( cacheLock )
        {
            List<E> children = rawNodeChildrenCache.get ( parentNode.getId () );
            if ( children == null )
            {
                children = new ArrayList<E> ( 1 );
                rawNodeChildrenCache.put ( parentNode.getId (), children );
            }
            children.add ( index, childNode );
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

        // Forcing child node to load its structure
        loadTreeData ( child );
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

        // Forcing child nodes to load their structures
        for ( final E child : children )
        {
            loadTreeData ( child );
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

        // Forcing child nodes to load their structures
        for ( final E child : children )
        {
            loadTreeData ( child );
        }
    }

    /**
     * Updates nodes sorting and filtering for all nodes.
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
            performSortingAndFiltering ( parentNode, recursively );
        }
    }

    /**
     * Updates node children using current comparator and filter.
     * Updates the whole node children structure if recursive update requested.
     *
     * @param parentNode  node which children sorting and filtering should be updated
     * @param recursively whether should update the whole children structure recursively or not
     */
    protected void performSortingAndFiltering ( final E parentNode, final boolean recursively )
    {
        // todo Restore tree state only for the updated node
        // Saving tree state to restore it right after children update
        final TreeState treeState = tree.getTreeState ();

        // Updating root node children
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
     * Updates node children using current comparator and filter.
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
     * Updates node children recursively using current comparator and filter.
     *
     * @param parentNode node to update
     */
    protected void performSortingAndFilteringImpl ( final E parentNode )
    {
        // Retrieving raw children
        final List<E> children = rawNodeChildrenCache.get ( parentNode.getId () );

        // Process this action only if node children are already loaded and cached
        if ( children != null )
        {
            // Removing old children
            parentNode.removeAllChildren ();

            // Filtering and sorting raw children
            final List<E> realChildren = filterAndSort ( parentNode, children );

            // Inserting new children
            for ( final E child : realChildren )
            {
                parentNode.add ( child );
            }
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
}