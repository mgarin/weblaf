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

import com.alee.api.annotations.NotNull;
import com.alee.laf.WebLookAndFeel;
import com.alee.laf.tree.*;
import com.alee.utils.CollectionUtils;
import com.alee.utils.compare.Filter;

import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import java.util.*;

/**
 * {@link WebTreeModel} extension that is based on data from {@link ExTreeDataProvider}.
 * All data is always instantly loaded based on the provided {@link ExTreeDataProvider} which allows sorting and filtering for all nodes.
 *
 * @param <N> {@link AsyncUniqueNode} type
 * @author Mikle Garin
 * @see WebExTree
 * @see ExTreeDataProvider
 */
public class ExTreeModel<N extends UniqueNode> extends WebTreeModel<N> implements FilterableNodes<N>, SortableNodes<N>
{
    /**
     * {@link ExTreeDataProvider} used by this model
     */
    protected final ExTreeDataProvider<N> dataProvider;

    /**
     * Cache for children nodes returned by data provider (parent ID -&gt; list of raw child nodes).
     * This map contains raw children which weren't affected by sorting and filtering operations.
     * If children needs to be re-sorted or re-filtered they are simply taken from the cache and re-organized once again.
     */
    protected transient Map<String, List<N>> rawNodeChildrenCache;

    /**
     * Nodes cache.
     * Used for quick node search within the tree.
     */
    protected transient Map<String, N> nodeById;

    /**
     * Nodes parent cache.
     * Used for node parent retrieval within the tree.
     */
    protected transient Map<String, String> parentById;

    /**
     * {@link WebTree} that uses this model
     */
    protected transient WebTree<N> tree;

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
     * Constructs default ex tree model using custom data provider.
     *
     * @param dataProvider {@link ExTreeDataProvider} this model should be based on
     */
    public ExTreeModel ( final ExTreeDataProvider<N> dataProvider )
    {
        super ( null );
        this.dataProvider = dataProvider;
    }

    /**
     * Returns {@link ExTreeDataProvider} used by this model.
     *
     * @return {@link ExTreeDataProvider} used by this model
     */
    public ExTreeDataProvider<N> getDataProvider ()
    {
        return dataProvider;
    }

    /**
     * Installs this {@link ExTreeModel} into the specified {@link WebTree}.
     *
     * @param tree {@link WebTree}
     */
    public void install ( final WebTree<N> tree )
    {
        WebLookAndFeel.checkEventDispatchThread ();
        this.rawNodeChildrenCache = new HashMap<String, List<N>> ( 10 );
        this.nodeById = new HashMap<String, N> ( 50 );
        this.parentById = new HashMap<String, String> ( 50 );
        this.tree = tree;
        this.rootNode = loadRootNode ();
        loadTreeData ( getRootNode () );
    }

    /**
     * Uninstalls this {@link ExTreeModel} from the specified {@link WebTree}.
     *
     * @param tree {@link WebTree}
     */
    public void uninstall ( final WebTree<N> tree )
    {
        WebLookAndFeel.checkEventDispatchThread ();
        this.rootNode = null;
        this.tree = null;
        this.parentById = null;
        this.nodeById = null;
        this.rawNodeChildrenCache = null;
    }

    /**
     * Returns whether or not this {@link ExTreeModel} is installed into some {@link WebTree}.
     *
     * @return {@code true} if this {@link ExTreeModel} is installed into some {@link WebTree}, {@code false} otherwise
     */
    public boolean isInstalled ()
    {
        return tree != null;
    }

    /**
     * Checks whether or not this {@link ExTreeModel} is installed into some {@link WebTree}.
     * If it is not installed - {@link IllegalStateException} is thrown to emphasize problem.
     */
    protected void checkInstalled ()
    {
        if ( !isInstalled () )
        {
            throw new IllegalStateException ( "This operation cannot be performed before model is installed into WebAsyncTree" );
        }
    }

    /**
     * Returns root node provided by {@link ExTreeDataProvider}.
     *
     * @return root node provided by {@link ExTreeDataProvider}
     */
    protected N loadRootNode ()
    {
        // Event Dispatch Thread check
        WebLookAndFeel.checkEventDispatchThread ();

        // Ensure model is installed
        checkInstalled ();

        // Retrieving root node
        final N rootNode = getDataProvider ().getRoot ();

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
     * This method is mostly used to ensure that at any given time tree has all of its nodes.
     * That heavily simplifies work with the tree in case you need to access random nodes in the tree directly.
     * In case this is not your goal it is probably better to use {@link AsyncTreeModel}.
     *
     * @param parent node to load children for
     */
    protected void loadTreeData ( final N parent )
    {
        // Loading children
        final List<N> children = getDataProvider ().getChildren ( parent );

        // Caching nodes
        setRawChildren ( parent, children );
        cacheNodesById ( children );
        cacheParentId ( children, parent.getId () );

        // Inserting loaded nodes if any of them are displayed
        final List<N> displayedChildren = filterAndSort ( parent, children );
        if ( displayedChildren != null && displayedChildren.size () > 0 )
        {
            super.insertNodesInto ( displayedChildren, parent, 0 );
        }

        // Forcing child nodes to load their structures
        for ( final N child : children )
        {
            loadTreeData ( child );
        }
    }

    @Override
    public N getRoot ()
    {
        // Event Dispatch Thread check
        WebLookAndFeel.checkEventDispatchThread ();

        // Ensure model is installed
        checkInstalled ();

        // Returning root node
        return rootNode;
    }

    @Override
    public N getChild ( final Object parent, final int index )
    {
        // Event Dispatch Thread check
        WebLookAndFeel.checkEventDispatchThread ();

        // Ensure model is installed
        checkInstalled ();

        // Looking for child node
        return ( N ) super.getChild ( parent, index );
    }

    @Override
    public void reload ( final TreeNode node )
    {
        // Event Dispatch Thread check
        WebLookAndFeel.checkEventDispatchThread ();

        // Ensure model is installed
        checkInstalled ();

        final N reloadedNode = ( N ) node;

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
     * Sets child nodes for the specified node.
     * This method might be used to manually change tree node children without causing any structure corruptions.
     *
     * @param parent   node to process
     * @param children new node children
     */
    public void setChildNodes ( final N parent, final List<N> children )
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
    public void addChildNode ( final N parent, final N child )
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
    public void addChildNodes ( final N parent, final List<N> children )
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
    public void insertNodeInto ( @NotNull final MutableTreeNode child, @NotNull final MutableTreeNode parent, final int index )
    {
        // Event Dispatch Thread check
        WebLookAndFeel.checkEventDispatchThread ();

        // Ensure model is installed
        checkInstalled ();

        final N childNode = ( N ) child;
        final N parentNode = ( N ) parent;

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
    public void insertNodesInto ( @NotNull final List<N> children, @NotNull final N parent, final int index )
    {
        // Event Dispatch Thread check
        WebLookAndFeel.checkEventDispatchThread ();

        // Ensure model is installed
        checkInstalled ();

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
        for ( final N child : children )
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
    public void insertNodesInto ( @NotNull final N[] children, @NotNull final N parent, final int index )
    {
        // Event Dispatch Thread check
        WebLookAndFeel.checkEventDispatchThread ();

        // Ensure model is installed
        checkInstalled ();

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
        for ( final N child : children )
        {
            loadTreeData ( child );
        }

        // Updating parent node sorting and filtering
        filterAndSort ( parent, false );
    }

    @Override
    public void removeNodeFromParent ( @NotNull final MutableTreeNode node )
    {
        // Event Dispatch Thread check
        WebLookAndFeel.checkEventDispatchThread ();

        // Ensure model is installed
        checkInstalled ();

        final N child = ( N ) node;
        final N parent = findParent ( child.getId () );

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
    public void removeNodesFromParent ( @NotNull final N parent )
    {
        // Event Dispatch Thread check
        WebLookAndFeel.checkEventDispatchThread ();

        // Ensure model is installed
        checkInstalled ();

        // Clearing node children caches
        clearRawChildren ( parent, false );

        // Removing node children
        super.removeNodesFromParent ( parent );
    }

    @Override
    public void removeNodesFromParent ( @NotNull final N[] nodes )
    {
        // Event Dispatch Thread check
        WebLookAndFeel.checkEventDispatchThread ();

        // Ensure model is installed
        checkInstalled ();

        // Redirecting to another method
        removeNodesFromParent ( CollectionUtils.toList ( nodes ) );
    }

    @Override
    public void removeNodesFromParent ( @NotNull final List<N> nodes )
    {
        // Event Dispatch Thread check
        WebLookAndFeel.checkEventDispatchThread ();

        // Ensure model is installed
        checkInstalled ();

        // Removing node caches
        final List<N> visible = new ArrayList<N> ( nodes.size () );
        for ( final N child : nodes )
        {
            final N parent = findParent ( child.getId () );

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
     * Updates filtering and sorting for the specified {@link UniqueNode} children.
     *
     * @param parent      {@link UniqueNode} for which children filtering and sorting should be updated
     * @param recursively whether should update filtering and sorting for all {@link UniqueNode} children recursively
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

            // Saving tree state to restore it right after children update
            final TreeState treeState = tree.getTreeState ( actualParent );

            // Updating root node children
            if ( recursively )
            {
                filterAndSortRecursively ( actualParent );
            }
            else
            {
                filterAndSort ( actualParent );
            }

            // Informing tree about possible major structure changes
            nodeStructureChanged ( actualParent );

            // Restoring tree state including all selections and expansions
            tree.setTreeState ( treeState, actualParent );
        }
    }

    /**
     * Updates filtering and sorting for the specified {@link UniqueNode} children recursively.
     *
     * @param parent {@link UniqueNode} for which children filtering and sorting should be updated
     */
    protected void filterAndSortRecursively ( final N parent )
    {
        // Filtering and sorting children of the specified parent first
        filterAndSort ( parent );

        // Now performing the same for each of the remaining children (not raw anymore to avoid unnecessary operations)
        for ( int i = 0; i < parent.getChildCount (); i++ )
        {
            filterAndSortRecursively ( ( N ) parent.getChildAt ( i ) );
        }
    }

    /**
     * Updates filtering and sorting for the specified {@link UniqueNode} children.
     *
     * @param parent {@link UniqueNode} for which children filtering and sorting should be updated
     */
    protected void filterAndSort ( final N parent )
    {
        // Removing old children
        parent.removeAllChildren ();

        // Filtering and sorting raw children
        final List<N> children = getRawChildren ( parent );
        final List<N> realChildren = filterAndSort ( parent, children );

        // Adding new children
        for ( final N child : realChildren )
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
    protected List<N> filterAndSort ( final N parent, final List<N> children )
    {
        final List<N> result;
        if ( CollectionUtils.notEmpty ( children ) )
        {
            // Data provider
            final ExTreeDataProvider<N> dataProvider = getDataProvider ();

            // Filtering children
            final Filter<N> dataProviderFilter = dataProvider.getChildrenFilter ( parent, children );
            final Filter<N> treeFilter = tree instanceof FilterableNodes ? ( ( FilterableNodes<N> ) tree ).getFilter () : null;
            final Filter<N> modelFilter = getFilter ();
            result = CollectionUtils.filter ( children, dataProviderFilter, treeFilter, modelFilter );

            // Sorting children
            final Comparator<N> dataProviderComparator = dataProvider.getChildrenComparator ( parent, result );
            final Comparator<N> treeComparator = tree instanceof SortableNodes ? ( ( SortableNodes<N> ) tree ).getComparator () : null;
            final Comparator<N> modelComparator = getComparator ();
            CollectionUtils.sort ( result, dataProviderComparator, treeComparator, modelComparator );
        }
        else
        {
            // Simply return an empty array if there is no children
            result = new ArrayList<N> ( 0 );
        }
        return result;
    }

    /**
     * Returns {@link UniqueNode} with the specified identifier if it is in the model, {@code null} if it is not.
     *
     * @param nodeId {@link UniqueNode} identifier
     * @return {@link UniqueNode} with the specified identifier if it is in the model, {@code null} if it is not
     */
    public N findNode ( final String nodeId )
    {
        // Ensure model is installed
        checkInstalled ();

        // Get node from cache
        return nodeById.get ( nodeId );
    }

    /**
     * Returns raw parent for the specified {@link UniqueNode}.
     *
     * @param node {@link UniqueNode} to find raw parent for
     * @return raw parent for the specified {@link UniqueNode}
     */
    public N getRawParent ( final N node )
    {
        // Ensure model is installed
        checkInstalled ();

        // Find actual parent
        final N parent = ( N ) node.getParent ();
        return parent != null ? parent : findParent ( node.getId () );
    }

    /**
     * Returns raw children for the specified {@link UniqueNode}.
     *
     * @param parent {@link UniqueNode} to return raw children for
     * @return raw children for the specified {@link UniqueNode}
     */
    public List<N> getRawChildren ( final N parent )
    {
        // Ensure model is installed
        checkInstalled ();

        // Get actual children from cache
        final List<N> children = rawNodeChildrenCache.get ( parent.getId () );
        if ( children == null )
        {
            throw new RuntimeException ( "Raw children are not available for node: " + parent );
        }
        return children;
    }

    /**
     * Returns child {@link UniqueNode} at the specified index in parent {@link UniqueNode}.
     *
     * @param parent parent {@link UniqueNode}
     * @param index  child {@link UniqueNode} index
     * @return child {@link UniqueNode} at the specified index in parent {@link UniqueNode}
     */
    public N getRawChildAt ( final N parent, final int index )
    {
        // Ensure model is installed
        checkInstalled ();

        // Get actual child at specified index from cache
        final List<N> children = rawNodeChildrenCache.get ( parent.getId () );
        if ( children == null )
        {
            throw new RuntimeException ( "Raw children are not available for node: " + parent );
        }
        return children.get ( index );
    }

    /**
     * Sets raw children for the {@link UniqueNode} with the specified identifier
     *
     * @param parent {@link UniqueNode} identifier to set raw children for
     * @param nodes  {@link List} of {@link UniqueNode}s to set as children
     */
    protected void setRawChildren ( final N parent, final List<N> nodes )
    {
        rawNodeChildrenCache.put ( parent.getId (), nodes );
    }

    /**
     * Returns raw children count for the specified {@link UniqueNode}.
     *
     * @param parent {@link UniqueNode} to return raw children count for
     * @return raw children count for the specified {@link UniqueNode}
     */
    public int getRawChildrenCount ( final N parent )
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
    protected void addRawChild ( final N parent, final N node, final int index )
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
    protected void addRawChildren ( final N parent, final List<N> nodes, final int index )
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
    protected void addRawChildren ( final N parent, final N[] nodes, final int index )
    {
        final List<N> cachedChildren = getRawChildren ( parent );
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
    protected void removeRawChild ( final N parent, final N node )
    {
        getRawChildren ( parent ).remove ( node );
    }

    /**
     * Clears node and all of its child nodes children cached states.
     *
     * @param node      node to clear cache for
     * @param clearNode whether should clear node cache or not
     */
    protected void clearRawChildren ( final N node, final boolean clearNode )
    {
        // Clears node cache
        if ( clearNode )
        {
            nodeById.remove ( node.getId () );
            parentById.remove ( node.getId () );
        }

        // Clears node raw children cache
        final List<N> children = rawNodeChildrenCache.remove ( node.getId () );
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
    protected void clearRawChildren ( final List<N> nodes, final boolean clearNodes )
    {
        for ( final N node : nodes )
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
    protected void clearRawChildren ( final N[] nodes, final boolean clearNodes )
    {
        for ( final N node : nodes )
        {
            clearRawChildren ( node, clearNodes );
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
            cacheNodeById ( node );
        }
    }

    /**
     * Caches nodes by their IDs.
     *
     * @param nodes array of nodes to cache
     */
    protected void cacheNodesById ( final N[] nodes )
    {
        for ( final N node : nodes )
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
    public N findParent ( final String nodeId )
    {
        // Ensure model is installed
        checkInstalled ();

        // Get parent from cache
        final String parentId = parentById.get ( nodeId );
        return findNode ( parentId );
    }

    /**
     * Caches {@link UniqueNode} parent identifier.
     *
     * @param node     {@link UniqueNode}
     * @param parentId {@link UniqueNode} parent identifier
     */
    protected void cacheParentId ( final N node, final String parentId )
    {
        parentById.put ( node.getId (), parentId );
    }

    /**
     * Caches parent identifier for {@link List} of {@link UniqueNode}.
     *
     * @param nodes    {@link List} of {@link UniqueNode}s
     * @param parentId {@link UniqueNode} parent identifier
     */
    protected void cacheParentId ( final List<N> nodes, final String parentId )
    {
        for ( final N node : nodes )
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
    protected void cacheParentId ( final N[] nodes, final String parentId )
    {
        for ( final N node : nodes )
        {
            cacheParentId ( node, parentId );
        }
    }
}