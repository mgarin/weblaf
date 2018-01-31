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
import com.alee.laf.tree.UniqueNode;
import com.alee.laf.tree.WebTree;
import com.alee.laf.tree.WebTreeModel;
import com.alee.utils.CollectionUtils;
import com.alee.utils.compare.Filter;

import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;
import java.util.*;

/**
 * {@link WebTreeModel} extension that is based on data from {@link ExTreeDataProvider}.
 * All data is always instantly loaded based on the provided {@link ExTreeDataProvider} which allows sorting and filtering for all nodes.
 *
 * @param <E> node type
 * @author Mikle Garin
 * @see WebExTree
 * @see ExTreeDataProvider
 */

public class ExTreeModel<E extends UniqueNode> extends WebTreeModel<E>
{
    /**
     * {@link WebTree} that uses this model
     */
    protected final WebTree<E> tree;

    /**
     * {@link ExTreeDataProvider} used by this model
     */
    protected final ExTreeDataProvider<E> dataProvider;

    /**
     * Cache for children nodes returned by data provider (parent ID -&gt; list of raw child nodes).
     * This map contains raw children which weren't affected by sorting and filtering operations.
     * If children needs to be re-sorted or re-filtered they are simply taken from the cache and re-organized once again.
     */
    protected final Map<String, List<E>> rawNodeChildrenCache;

    /**
     * Nodes cache.
     * Used for quick node search within the tree.
     */
    protected final Map<String, E> nodeById;

    /**
     * Nodes parent cache.
     * Used for node parent retrieval within the tree.
     */
    protected final Map<String, String> parentById;

    /**
     * Root node cache.
     * Cached when root is requested for the first time.
     */
    protected final E rootNode;

    /**
     * Constructs default ex tree model using custom data provider.
     *
     * @param tree         {@link WebTree} that uses this model
     * @param dataProvider {@link ExTreeDataProvider} this model should be based on
     */
    public ExTreeModel ( final WebTree<E> tree, final ExTreeDataProvider<E> dataProvider )
    {
        super ( null );
        this.tree = tree;
        this.dataProvider = dataProvider;
        this.rawNodeChildrenCache = new HashMap<String, List<E>> ( 10 );
        this.nodeById = new HashMap<String, E> ( 50 );
        this.parentById = new HashMap<String, String> ( 50 );
        this.rootNode = loadRootNode ();
        loadTreeData ( getRootNode () );
    }

    /**
     * Returns {@link ExTreeDataProvider} used by this model.
     *
     * @return {@link ExTreeDataProvider} used by this model
     */
    public ExTreeDataProvider<E> getDataProvider ()
    {
        return dataProvider;
    }

    /**
     * Returns root node provided by {@link ExTreeDataProvider}.
     *
     * @return root node provided by {@link ExTreeDataProvider}
     */
    protected E loadRootNode ()
    {
        // Event Dispatch Thread check
        WebLookAndFeel.checkEventDispatchThread ();

        // Retrieving root node
        final ExTreeDataProvider<E> dataProvider = getDataProvider ();
        final E rootNode = dataProvider.getRoot ();

        // Caching root node
        cacheNodeById ( rootNode );
        cacheParentId ( rootNode, null );

        return rootNode;
    }

    /**
     * Forces model to cache the whole structure so any node can be accessed right away.
     * Note that this might take some time in case tree structure is large as it will be fully loaded.
     * Though this doesn't force any repaints or other visual updates, so the speed depends only on ExTreeDataProvider.
     *
     * This method is mostly used to ensure that at any given time {@link WebExTree} has all of its nodes.
     * That heavily simplifies work with the tree in case you need to access random nodes in the tree directly.
     * In case this is not your goal it is probably better to use {@link WebAsyncTree}.
     *
     * @param parent node to load children for
     */
    protected void loadTreeData ( final E parent )
    {
        // Event Dispatch Thread check
        WebLookAndFeel.checkEventDispatchThread ();

        // Loading children
        final ExTreeDataProvider<E> dataProvider = getDataProvider ();
        final List<E> children = dataProvider.getChildren ( parent );

        // Caching nodes
        setRawChildren ( parent, children );
        cacheNodesById ( children );
        cacheParentId ( children, parent.getId () );

        // Inserting loaded nodes if any of them are displayed
        final List<E> displayedChildren = filterAndSort ( parent, children );
        if ( displayedChildren != null && displayedChildren.size () > 0 )
        {
            super.insertNodesInto ( displayedChildren, parent, 0 );
        }

        // Forcing child nodes to load their structures
        for ( final E child : children )
        {
            loadTreeData ( child );
        }
    }

    @Override
    public E getRoot ()
    {
        // Event Dispatch Thread check
        WebLookAndFeel.checkEventDispatchThread ();

        // Returning root node
        return rootNode;
    }

    @Override
    public E getChild ( final Object parent, final int index )
    {
        // Event Dispatch Thread check
        WebLookAndFeel.checkEventDispatchThread ();

        // Looking for child node
        return ( E ) super.getChild ( parent, index );
    }

    @Override
    public void reload ( final TreeNode node )
    {
        // Event Dispatch Thread check
        WebLookAndFeel.checkEventDispatchThread ();

        final E reloadedNode = ( E ) node;

        // Cancels tree editing
        tree.cancelEditing ();

        // Cleaning up nodes cache
        clearRawChildren ( reloadedNode, false );

        // Removing all old children if such exist
        // We don't need to inform about child nodes removal here due to later structural update call
        reloadedNode.removeAllChildren ();

        // Forcing structure reload
        loadTreeData ( reloadedNode );

        // Forcing children reload
        super.reload ( reloadedNode );
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
        removeNodesFromParent ( parent );
        addChildNodes ( parent, children );
    }

    /**
     * Adds child node for the specified node.
     * This method might be used to manually change tree node children without causing any structure corruptions.
     *
     * @param parent node to process
     * @param child  new node child
     */
    public void addChildNode ( final E parent, final E child )
    {
        insertNodeInto ( child, parent, getRawChildrenCount ( parent ) );
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
        insertNodesInto ( children, parent, getRawChildrenCount ( parent ) );
    }

    /**
     * Inserts new child node into parent node at the specified index.
     * This method might be used to manually change tree node children without causing any structure corruptions.
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

        final E childNode = ( E ) child;
        final E parentNode = ( E ) parent;

        // Caching node
        addRawChild ( parentNode, childNode, index );
        cacheNodeById ( childNode );
        cacheParentId ( childNode, parentNode.getId () );

        // Clearing nodes children caches
        // That might be required in case nodes were moved inside of the tree
        clearRawChildren ( childNode, false );

        // Inserting node
        super.insertNodeInto ( childNode, parentNode, Math.min ( index, parentNode.getChildCount () ) );

        // Loading data for newly added node
        loadTreeData ( childNode );

        // Updating parent node sorting and filtering
        filterAndSort ( parentNode, false );
    }

    /**
     * Inserts a list of child nodes into parent node.
     * This method might be used to manually change tree node children without causing any structure corruptions.
     *
     * @param children list of new child nodes
     * @param parent   parent node
     * @param index    insert index
     */
    @Override
    public void insertNodesInto ( final List<E> children, final E parent, final int index )
    {
        // Event Dispatch Thread check
        WebLookAndFeel.checkEventDispatchThread ();

        // Caching nodes
        addRawChildren ( parent, children, index );
        cacheNodesById ( children );
        cacheParentId ( children, parent.getId () );

        // Clearing nodes children caches
        // That might be required in case nodes were moved inside of the tree
        clearRawChildren ( children, false );

        // Performing actual nodes insertion
        super.insertNodesInto ( children, parent, Math.min ( index, parent.getChildCount () ) );

        // Loading data for newly added nodes
        for ( final E child : children )
        {
            loadTreeData ( child );
        }

        // Updating parent node sorting and filtering
        filterAndSort ( parent, false );
    }

    /**
     * Inserts an array of child nodes into parent node.
     * This method might be used to manually change tree node children without causing any structure corruptions.
     *
     * @param children array of new child nodes
     * @param parent   parent node
     * @param index    insert index
     */
    @Override
    public void insertNodesInto ( final E[] children, final E parent, final int index )
    {
        // Event Dispatch Thread check
        WebLookAndFeel.checkEventDispatchThread ();

        // Caching nodes
        addRawChildren ( parent, children, index );
        cacheNodesById ( children );
        cacheParentId ( children, parent.getId () );

        // Clearing nodes children caches
        // That might be required in case nodes were moved inside of the tree
        clearRawChildren ( children, false );

        // Inserting nodes
        super.insertNodesInto ( children, parent, Math.min ( index, parent.getChildCount () ) );

        // Loading data for newly added nodes
        for ( final E child : children )
        {
            loadTreeData ( child );
        }

        // Updating parent node sorting and filtering
        filterAndSort ( parent, false );
    }

    @Override
    public void removeNodeFromParent ( final MutableTreeNode node )
    {
        // Event Dispatch Thread check
        WebLookAndFeel.checkEventDispatchThread ();

        final E child = ( E ) node;
        final E parent = findParent ( child.getId () );

        // Clearing nodes children caches
        removeRawChild ( parent, child );
        clearRawChildren ( child, true );

        // Removing node children so they won't mess up anything when we place node back into tree
        // This will basically strip node from unnecessary children which will be reloaded upon node addition into other ExTreeModel
        child.removeAllChildren ();

        // Removing actual node if it is needed, node might not be present in the tree due to filtering
        if ( child.getParent () == parent )
        {
            // Removing node from parent
            super.removeNodeFromParent ( node );
        }
    }

    @Override
    public void removeNodesFromParent ( final E parent )
    {
        // Clearing node children caches
        clearRawChildren ( parent, false );

        // Removing node children
        super.removeNodesFromParent ( parent );
    }

    @Override
    public void removeNodesFromParent ( final E[] nodes )
    {
        // Redirecting to another method
        removeNodesFromParent ( CollectionUtils.toList ( nodes ) );
    }

    @Override
    public void removeNodesFromParent ( final List<E> nodes )
    {
        // Event Dispatch Thread check
        WebLookAndFeel.checkEventDispatchThread ();

        // Removing node caches
        final List<E> visible = new ArrayList<E> ( nodes.size () );
        for ( final E child : nodes )
        {
            final E parent = findParent ( child.getId () );

            // Clearing nodes children caches
            removeRawChild ( parent, child );
            clearRawChildren ( child, true );

            // Removing node children so they won't mess up anything when we place node back into tree
            // This will basically strip node from unnecessary children which will be reloaded upon node addition into other ExTreeModel
            child.removeAllChildren ();

            // Saving nodes visible in the tree structure
            if ( child.getParent () == parent )
            {
                visible.add ( child );
            }
        }

        // Removing actual nodes if it is needed, nodes might not be present in the tree due to filtering
        super.removeNodesFromParent ( visible );
    }

    /**
     * Updates filtering and sorting for the specified {@link UniqueNode} children.
     *
     * @param parent      {@link UniqueNode} for which children filtering and sorting should be updated
     * @param recursively whether should update filtering and sorting for all {@link UniqueNode} children recursively
     */
    public void filterAndSort ( final E parent, final boolean recursively )
    {
        // Event Dispatch Thread check
        WebLookAndFeel.checkEventDispatchThread ();

        // Saving tree state to restore it right after children update
        final TreeState treeState = tree.getTreeState ( parent );

        // Updating root node children
        if ( recursively )
        {
            filterAndSortRecursively ( parent );
        }
        else
        {
            filterAndSort ( parent );
        }

        // Informing tree about possible major structure changes
        nodeStructureChanged ( parent );

        // Restoring tree state including all selections and expansions
        tree.setTreeState ( treeState, parent );
    }

    /**
     * Updates filtering and sorting for the specified {@link UniqueNode} children recursively.
     *
     * @param parent {@link UniqueNode} for which children filtering and sorting should be updated
     */
    protected void filterAndSortRecursively ( final E parent )
    {
        // Filtering and sorting children of the specified parent first
        filterAndSort ( parent );

        // Now performing the same for each of the remaining children (not raw anymore to avoid unnecessary operations)
        for ( int i = 0; i < parent.getChildCount (); i++ )
        {
            filterAndSortRecursively ( ( E ) parent.getChildAt ( i ) );
        }
    }

    /**
     * Updates filtering and sorting for the specified {@link UniqueNode} children.
     *
     * @param parent {@link UniqueNode} for which children filtering and sorting should be updated
     */
    protected void filterAndSort ( final E parent )
    {
        // Removing old children
        parent.removeAllChildren ();

        // Filtering and sorting raw children
        final List<E> children = getRawChildren ( parent );
        final List<E> realChildren = filterAndSort ( parent, children );

        // Adding new children
        for ( final E child : realChildren )
        {
            parent.add ( child );
        }
    }

    /**
     * Returns {@link List} of filtered and sorted {@link UniqueNode}s.
     *
     * @param parent   {@link UniqueNode} for which children filtering and sorting should be updated
     * @param children {@link List} of {@link UniqueNode}s to filter and sort
     * @return {@link List} of filtered and sorted {@link UniqueNode}s
     */
    protected List<E> filterAndSort ( final E parent, final List<E> children )
    {
        final List<E> result;
        if ( CollectionUtils.notEmpty ( children ) )
        {
            // Data provider
            final ExTreeDataProvider<E> dataProvider = getDataProvider ();

            // Filtering children
            final Filter<E> filter = dataProvider.getChildrenFilter ( parent, children );
            result = filter != null ? CollectionUtils.filter ( children, filter ) : CollectionUtils.copy ( children );

            // Sorting children
            final Comparator<E> comparator = dataProvider.getChildrenComparator ( parent, result );
            if ( comparator != null )
            {
                Collections.sort ( result, comparator );
            }
        }
        else
        {
            // Simply return an empty array if there is no children
            result = new ArrayList<E> ( 0 );
        }
        return result;
    }

    /**
     * Returns {@link UniqueNode} with the specified identifier if it is in the model, {@code null} if it is not.
     *
     * @param nodeId {@link UniqueNode} identifier
     * @return {@link UniqueNode} with the specified identifier if it is in the model, {@code null} if it is not
     */
    public E findNode ( final String nodeId )
    {
        return nodeById.get ( nodeId );
    }

    /**
     * Returns raw children for the specified {@link UniqueNode}.
     *
     * @param parent {@link UniqueNode} to return raw children for
     * @return raw children for the specified {@link UniqueNode}
     */
    public List<E> getRawChildren ( final E parent )
    {
        final List<E> children = rawNodeChildrenCache.get ( parent.getId () );
        if ( children == null )
        {
            throw new RuntimeException ( "Raw children are not available for node: " + parent );
        }
        return children;
    }

    /**
     * Sets raw children for the {@link UniqueNode} with the specified identifier
     *
     * @param parent {@link UniqueNode} identifier to set raw children for
     * @param nodes  {@link List} of {@link UniqueNode}s to set as children
     */
    protected void setRawChildren ( final E parent, final List<E> nodes )
    {
        rawNodeChildrenCache.put ( parent.getId (), nodes );
    }

    /**
     * Returns raw children count for the specified {@link UniqueNode}.
     *
     * @param parent {@link UniqueNode} to return raw children count for
     * @return raw children count for the specified {@link UniqueNode}
     */
    public int getRawChildrenCount ( final E parent )
    {
        return getRawChildren ( parent ).size ();
    }

    /**
     * Adds raw child to the specified {@link UniqueNode}.
     *
     * @param parent {@link UniqueNode} to add child to
     * @param node   {@link UniqueNode} child
     * @param index  index to add child at
     */
    protected void addRawChild ( final E parent, final E node, final int index )
    {
        getRawChildren ( parent ).add ( index, node );
    }

    /**
     * Adds raw childred to the specified {@link UniqueNode}.
     *
     * @param parent {@link UniqueNode} to add children to
     * @param nodes  {@link List} of {@link UniqueNode} children
     * @param index  index to add children at
     */
    protected void addRawChildren ( final E parent, final List<E> nodes, final int index )
    {
        getRawChildren ( parent ).addAll ( index, nodes );
    }

    /**
     * Adds raw childred to the specified {@link UniqueNode}.
     *
     * @param parent {@link UniqueNode} to add children to
     * @param nodes  {@link List} of {@link UniqueNode} children
     * @param index  index to add children at
     */
    protected void addRawChildren ( final E parent, final E[] nodes, final int index )
    {
        final List<E> cachedChildren = getRawChildren ( parent );
        for ( int i = nodes.length - 1; i >= 0; i-- )
        {
            cachedChildren.add ( index, nodes[ i ] );
        }
    }

    /**
     * Removes raw child from the specified {@link UniqueNode}.
     *
     * @param parent {@link UniqueNode} to remove child from
     * @param node   {@link UniqueNode} child to remove
     */
    protected void removeRawChild ( final E parent, final E node )
    {
        getRawChildren ( parent ).remove ( node );
    }

    /**
     * Clears node and all of its child nodes children cached states.
     *
     * @param node      node to clear cache for
     * @param clearNode whether should clear node cache or not
     */
    protected void clearRawChildren ( final E node, final boolean clearNode )
    {
        // Clears node cache
        if ( clearNode )
        {
            nodeById.remove ( node.getId () );
            parentById.remove ( node.getId () );
        }

        // Clears node raw children cache
        final List<E> children = rawNodeChildrenCache.remove ( node.getId () );
        if ( CollectionUtils.notEmpty ( children ) )
        {
            clearRawChildren ( children, true );
        }
    }

    /**
     * Clears nodes children cached states.
     *
     * @param nodes      nodes to clear cache for
     * @param clearNodes whether should clear nodes cache or not
     */
    protected void clearRawChildren ( final List<E> nodes, final boolean clearNodes )
    {
        for ( final E node : nodes )
        {
            clearRawChildren ( node, clearNodes );
        }
    }

    /**
     * Clears nodes children cached states.
     *
     * @param nodes      nodes to clear cache for
     * @param clearNodes whether should clear nodes cache or not
     */
    protected void clearRawChildren ( final E[] nodes, final boolean clearNodes )
    {
        for ( final E node : nodes )
        {
            clearRawChildren ( node, clearNodes );
        }
    }

    /**
     * Caches node by its IDs.
     *
     * @param node node to cache
     */
    protected void cacheNodeById ( final E node )
    {
        nodeById.put ( node.getId (), node );
    }

    /**
     * Caches nodes by their IDs.
     *
     * @param nodes list of nodes to cache
     */
    protected void cacheNodesById ( final List<E> nodes )
    {
        for ( final E node : nodes )
        {
            cacheNodeById ( node );
        }
    }

    /**
     * Caches nodes by their IDs.
     *
     * @param nodes array of nodes to cache
     */
    protected void cacheNodesById ( final E[] nodes )
    {
        for ( final E node : nodes )
        {
            cacheNodeById ( node );
        }
    }

    /**
     * Returns parent of the {@link UniqueNode} with the specified identifier, {@code null} if it cannot be found.
     *
     * @param nodeId {@link UniqueNode} identifier
     * @return parent of the {@link UniqueNode} with the specified identifier, {@code null} if it cannot be found
     */
    public E findParent ( final String nodeId )
    {
        final String parentId = parentById.get ( nodeId );
        return findNode ( parentId );
    }

    /**
     * Caches {@link UniqueNode} parent identifier.
     *
     * @param node     {@link UniqueNode}
     * @param parentId {@link UniqueNode} parent identifier
     */
    protected void cacheParentId ( final E node, final String parentId )
    {
        parentById.put ( node.getId (), parentId );
    }

    /**
     * Caches parent identifier for {@link List} of {@link UniqueNode}.
     *
     * @param nodes    {@link List} of {@link UniqueNode}s
     * @param parentId {@link UniqueNode} parent identifier
     */
    protected void cacheParentId ( final List<E> nodes, final String parentId )
    {
        for ( final E node : nodes )
        {
            cacheParentId ( node, parentId );
        }
    }

    /**
     * Caches parent identifier for array of {@link UniqueNode}.
     *
     * @param nodes    array of {@link UniqueNode}s
     * @param parentId {@link UniqueNode} parent identifier
     */
    protected void cacheParentId ( final E[] nodes, final String parentId )
    {
        for ( final E node : nodes )
        {
            cacheParentId ( node, parentId );
        }
    }
}