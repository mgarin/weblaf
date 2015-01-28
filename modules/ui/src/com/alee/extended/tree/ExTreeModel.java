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
        return ( E ) super.getChild ( parent, index );
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
     * Forces model to cache the whole structure so any node can be accessed right away.
     * Note that this might take some time in case tree structure is large.
     * Though this doesn't force any repaints or other visual updates, so the speed depends only on ExTreeDataProvider.
     */
    protected void loadTreeData ( final E node )
    {
        final int childCount = getChildCount ( node );
        for ( int i = 0; i < childCount; i++ )
        {
            loadTreeData ( getChild ( node, i ) );
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
        final E reloadedNode = ( E ) node;

        // Cancels tree editing
        tree.cancelEditing ();

        // Cleaning up nodes cache
        clearNodeChildsCache ( reloadedNode, false );

        // Removing all old childs if such exist
        // We don't need to inform about child nodes removal here due to later structural update call
        reloadedNode.removeAllChildren ();

        // Forcing childs reload
        super.reload ( reloadedNode );

        // Forcing structure reload
        loadTreeData ( reloadedNode );
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
        // Loading childs
        final List<E> childs = dataProvider.getChilds ( parent );

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

        return parent.getChildCount ();
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

        // Simply ignore if parent node is null
        if ( parentNode == null )
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
    }

    /**
     * Updates nodes sorting and filtering for all nodes.
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
            performSortingAndFiltering ( parentNode, recursively );
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
}