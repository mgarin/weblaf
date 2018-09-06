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
import com.alee.laf.tree.WebTree;
import com.alee.managers.style.StyleId;
import com.alee.utils.CollectionUtils;
import com.alee.utils.compare.Filter;

import javax.swing.tree.TreeCellEditor;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.util.Comparator;
import java.util.List;

/**
 * {@link WebTree} extension class.
 * It uses {@link AsyncTreeDataProvider} as data source instead of {@link TreeModel}.
 * This tree structure is almost never fully available and always loaded on demand.
 *
 * This component should never be used with a non-Web UIs as it might cause an unexpected behavior.
 * You could still use that component even if WebLaF is not your application LaF as this component will use Web-UI in any case.
 *
 * @param <N> {@link AsyncUniqueNode} type
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-WebAsyncTree">How to use WebAsyncTree</a>
 * @see WebTree
 * @see com.alee.laf.tree.WebTreeUI
 * @see com.alee.laf.tree.TreePainter
 * @see AsyncTreeModel
 * @see AsyncTreeDataProvider
 */
public class WebAsyncTree<N extends AsyncUniqueNode> extends WebTree<N>
        implements FilterableNodes<N>, SortableNodes<N>, AsyncTreeModelListener<N>
{
    /**
     * todo 1. Allow adding async tree listener for specific node/nodeId listening
     */

    /**
     * Component properties.
     */
    public static final String ASYNC_LOADING_PROPERTY = "asyncLoading";

    /**
     * Whether to load children asynchronously or not.
     */
    protected boolean asyncLoading = true;

    /**
     * Tree nodes filter.
     */
    protected Filter<N> filter;

    /**
     * Tree nodes comparator.
     */
    protected Comparator<N> comparator;

    /**
     * Constructs new {@link WebAsyncTree} with sample data.
     */
    public WebAsyncTree ()
    {
        this ( StyleId.auto );
    }

    /**
     * Costructs new {@link WebAsyncTree} with the specified {@link AsyncTreeDataProvider} as data source.
     *
     * @param dataProvider {@link AsyncTreeDataProvider} implementation
     */
    public WebAsyncTree ( final AsyncTreeDataProvider dataProvider )
    {
        this ( StyleId.auto, dataProvider );
    }

    /**
     * Costructs new {@link WebAsyncTree} with the specified {@link AsyncTreeDataProvider} as data source.
     *
     * @param dataProvider {@link AsyncTreeDataProvider} implementation
     * @param renderer     {@link TreeCellRenderer} implementation, default implementation is used if {@code null} is provided
     */
    public WebAsyncTree ( final AsyncTreeDataProvider dataProvider, final TreeCellRenderer renderer )
    {
        this ( StyleId.auto, dataProvider, renderer );
    }

    /**
     * Costructs new {@link WebAsyncTree} with the specified {@link AsyncTreeDataProvider} as data source.
     *
     * @param dataProvider {@link AsyncTreeDataProvider} implementation
     * @param editor       {@link TreeCellEditor} implementation, default implementation is used if {@code null} is provided
     */
    public WebAsyncTree ( final AsyncTreeDataProvider dataProvider, final TreeCellEditor editor )
    {
        this ( StyleId.auto, dataProvider, editor );
    }

    /**
     * Costructs new {@link WebAsyncTree} with the specified {@link AsyncTreeDataProvider} as data source.
     *
     * @param dataProvider {@link AsyncTreeDataProvider} implementation
     * @param renderer     {@link TreeCellRenderer} implementation, default implementation is used if {@code null} is provided
     * @param editor       {@link TreeCellEditor} implementation, default implementation is used if {@code null} is provided
     */
    public WebAsyncTree ( final AsyncTreeDataProvider dataProvider, final TreeCellRenderer renderer, final TreeCellEditor editor )
    {
        this ( StyleId.auto, dataProvider, renderer, editor );
    }

    /**
     * Constructs new {@link WebAsyncTree} with sample data.
     *
     * @param id {@link StyleId}
     */
    public WebAsyncTree ( final StyleId id )
    {
        this ( id, null, null, null );
    }

    /**
     * Costructs new {@link WebAsyncTree} with the specified {@link AsyncTreeDataProvider} as data source.
     *
     * @param id           {@link StyleId}
     * @param dataProvider {@link AsyncTreeDataProvider} implementation
     */
    public WebAsyncTree ( final StyleId id, final AsyncTreeDataProvider dataProvider )
    {
        this ( id, dataProvider, null, null );
    }

    /**
     * Costructs new {@link WebAsyncTree} with the specified {@link AsyncTreeDataProvider} as data source.
     *
     * @param id           {@link StyleId}
     * @param dataProvider {@link AsyncTreeDataProvider} implementation
     * @param renderer     {@link TreeCellRenderer} implementation, default implementation is used if {@code null} is provided
     */
    public WebAsyncTree ( final StyleId id, final AsyncTreeDataProvider dataProvider,
                          final TreeCellRenderer renderer )
    {
        this ( id, dataProvider, renderer, null );
    }

    /**
     * Costructs new {@link WebAsyncTree} with the specified {@link AsyncTreeDataProvider} as data source.
     *
     * @param id           {@link StyleId}
     * @param dataProvider {@link AsyncTreeDataProvider} implementation
     * @param editor       {@link TreeCellEditor} implementation, default implementation is used if {@code null} is provided
     */
    public WebAsyncTree ( final StyleId id, final AsyncTreeDataProvider dataProvider,
                          final TreeCellEditor editor )
    {
        this ( id, dataProvider, null, editor );
    }

    /**
     * Costructs new {@link WebAsyncTree} with the specified {@link AsyncTreeDataProvider} as data source.
     *
     * @param id           {@link StyleId}
     * @param dataProvider {@link AsyncTreeDataProvider} implementation
     * @param renderer     {@link TreeCellRenderer} implementation, default implementation is used if {@code null} is provided
     * @param editor       {@link TreeCellEditor} implementation, default implementation is used if {@code null} is provided
     */
    public WebAsyncTree ( final StyleId id, final AsyncTreeDataProvider dataProvider,
                          final TreeCellRenderer renderer, final TreeCellEditor editor )
    {
        super ( id, new EmptyTreeModel () );
        if ( dataProvider != null )
        {
            setDataProvider ( dataProvider );
        }
        if ( renderer != null )
        {
            setCellRenderer ( renderer );
        }
        if ( editor != null )
        {
            setEditable ( true );
            setCellEditor ( editor );
        }
    }

    @Override
    public StyleId getDefaultStyleId ()
    {
        return StyleId.asynctree;
    }

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
        // Event Dispatch Thread check
        WebLookAndFeel.checkEventDispatchThread ();

        // Updating loading mode
        final boolean old = this.asyncLoading;
        this.asyncLoading = asyncLoading;

        // Notifying about property change
        firePropertyChange ( ASYNC_LOADING_PROPERTY, old, asyncLoading );
    }

    @Override
    public AsyncTreeModel<N> getModel ()
    {
        return ( AsyncTreeModel<N> ) super.getModel ();
    }

    @Override
    public void setModel ( final TreeModel newModel )
    {
        // Event Dispatch Thread check
        WebLookAndFeel.checkEventDispatchThread ();

        /**
         * Simply ignoring any models that are not {@link AsyncTreeModel}-based.
         * This is a workaround to avoid default model being set in {@link javax.swing.JTree}.
         * This way we can prevent any models from being forced on us and avoid unnecessary events and UI updates.
         */
        if ( newModel instanceof AsyncTreeModel )
        {
            final AsyncTreeModel<N> old = getModel ();
            if ( old != null )
            {
                old.uninstall ( this );
            }

            final AsyncTreeModel model = ( AsyncTreeModel ) newModel;
            model.install ( this );

            super.setModel ( newModel );
        }
        else if ( newModel == null )
        {
            throw new NullPointerException ( "AsyncTreeModel cannot be null" );
        }
    }

    /**
     * Returns {@link AsyncTreeDataProvider} used by this {@link WebAsyncTree}.
     *
     * @return {@link AsyncTreeDataProvider} used by this {@link WebAsyncTree}
     */
    public AsyncTreeDataProvider<N> getDataProvider ()
    {
        final AsyncTreeModel<N> model = getModel ();
        return model != null ? model.getDataProvider () : null;
    }

    /**
     * Sets {@link AsyncTreeDataProvider} used by this {@link WebAsyncTree}.
     *
     * @param dataProvider new {@link AsyncTreeDataProvider} for this {@link WebAsyncTree}
     */
    public void setDataProvider ( final AsyncTreeDataProvider dataProvider )
    {
        // Event Dispatch Thread check
        WebLookAndFeel.checkEventDispatchThread ();

        /**
         * Initializing new {@link AsyncTreeModel} based on specified {@link AsyncTreeDataProvider}.
         * This is necessary as the model will keep {@link AsyncTreeDataProvider} instead of {@link WebAsyncTree}.
         */
        if ( dataProvider != null )
        {
            final AsyncTreeDataProvider<N> oldDataProvider = getDataProvider ();
            setModel ( new AsyncTreeModel<N> ( dataProvider ) );
            firePropertyChange ( WebLookAndFeel.TREE_DATA_PROVIDER_PROPERTY, oldDataProvider, dataProvider );
        }
        else
        {
            throw new NullPointerException ( "AsyncTreeDataProvider cannot be null" );
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
        final Filter<N> oldFilter = this.filter;
        this.filter = filter;
        filter ();
        firePropertyChange ( WebLookAndFeel.TREE_FILTER_PROPERTY, oldFilter, filter );
    }

    @Override
    public void clearFilter ()
    {
        setFilter ( null );
    }

    @Override
    public void filter ()
    {
        getModel ().filter ();
    }

    @Override
    public void filter ( final N node )
    {
        getModel ().filter ( node );
    }

    @Override
    public void filter ( final N node, final boolean recursively )
    {
        getModel ().filter ( node, recursively );
    }

    @Override
    public Comparator<N> getComparator ()
    {
        return comparator;
    }

    @Override
    public void setComparator ( final Comparator<N> comparator )
    {
        final Comparator<N> oldComparator = this.comparator;
        this.comparator = comparator;
        sort ();
        firePropertyChange ( WebLookAndFeel.TREE_COMPARATOR_PROPERTY, oldComparator, comparator );
    }

    @Override
    public void clearComparator ()
    {
        setComparator ( null );
    }

    @Override
    public void sort ()
    {
        getModel ().sort ();
    }

    @Override
    public void sort ( final N node )
    {
        getModel ().sort ( node );
    }

    @Override
    public void sort ( final N node, final boolean recursively )
    {
        getModel ().sort ( node, recursively );
    }

    /**
     * Updates nodes sorting and filtering for all loaded nodes.
     */
    public void filterAndSort ()
    {
        getModel ().filterAndSort ( true );
    }

    /**
     * Updates sorting and filtering for the specified node children.
     *
     * @param node node to update sorting and filter for
     */
    public void filterAndSort ( final N node )
    {
        getModel ().filterAndSort ( node, false );
    }

    /**
     * Updates sorting and filtering for the specified node children.
     *
     * @param node        node to update sorting and filter for
     * @param recursively whether should update the whole children structure recursively or not
     */
    public void filterAndSort ( final N node, final boolean recursively )
    {
        getModel ().filterAndSort ( node, recursively );
    }

    /**
     * Sets maximum threads amount for this asynchronous tree.
     * Separate threads are used for children loading, data updates and other actions which should be performed asynchronously.
     *
     * @param amount new maximum threads amount
     */
    public void setMaximumThreadsAmount ( final int amount )
    {
        AsyncTreeQueue.getInstance ( this ).setMaximumThreadsAmount ( amount );
    }

    /**
     * Sets child nodes for the specified node.
     * This method might be used to manually change tree node children without causing any structure corruptions.
     * It will also cause node to change its state to loaded and it will not retrieve children from data provider unless reload is called.
     *
     * @param parent   node to process
     * @param children new node children
     */
    public void setChildNodes ( final N parent, final List<N> children )
    {
        getModel ().setChildNodes ( parent, children );
    }

    /**
     * Adds child node for the specified node.
     * This method might be used to manually change tree node children without causing any structure corruptions.
     * Be aware that added node will not be displayed if parent node is not yet loaded, this is a strict restriction for async tree.
     *
     * @param parent node to process
     * @param child  new node child
     */
    public void addChildNode ( final N parent, final N child )
    {
        getModel ().addChildNode ( parent, child );
    }

    /**
     * Adds child nodes for the specified node.
     * This method might be used to manually change tree node children without causing any structure corruptions.
     * Be aware that added nodes will not be displayed if parent node is not yet loaded, this is a strict restriction for async tree.
     *
     * @param parent   node to process
     * @param children new node children
     */
    public void addChildNodes ( final N parent, final List<N> children )
    {
        getModel ().addChildNodes ( parent, children );
    }

    /**
     * Inserts a list of child nodes into parent node.
     * This method might be used to manually change tree node children without causing any structure corruptions.
     * Be aware that added nodes will not be displayed if parent node is not yet loaded, this is a strict restriction for async tree.
     *
     * @param children list of new child nodes
     * @param parent   parent node
     * @param index    insert index
     */
    public void insertChildNodes ( final List<N> children, final N parent, final int index )
    {
        getModel ().insertNodesInto ( children, parent, index );
    }

    /**
     * Inserts an array of child nodes into parent node.
     * This method might be used to manually change tree node children without causing any structure corruptions.
     * Be aware that added nodes will not be displayed if parent node is not yet loaded, this is a strict restriction for async tree.
     *
     * @param children array of new child nodes
     * @param parent   parent node
     * @param index    insert index
     */
    public void insertChildNodes ( final N[] children, final N parent, final int index )
    {
        getModel ().insertNodesInto ( children, parent, index );
    }

    /**
     * Inserts child node into parent node.
     * This method might be used to manually change tree node children without causing any structure corruptions.
     *
     * @param child  new child node
     * @param parent parent node
     * @param index  insert index
     */
    public void insertChildNode ( final N child, final N parent, final int index )
    {
        getModel ().insertNodeInto ( child, parent, index );
    }

    /**
     * Removes node with the specified ID from tree structure.
     * This method will have effect only if node exists.
     *
     * @param nodeId ID of the node to remove
     */
    public void removeNode ( final String nodeId )
    {
        removeNode ( findNode ( nodeId ) );
    }

    /**
     * Removes node from tree structure.
     * This method will have effect only if node exists.
     *
     * @param node node to remove
     */
    public void removeNode ( final N node )
    {
        getModel ().removeNodeFromParent ( node );
    }

    /**
     * Removes nodes from tree structure.
     * This method will have effect only if nodes exist.
     *
     * @param nodes list of nodes to remove
     */
    public void removeNodes ( final List<N> nodes )
    {
        getModel ().removeNodesFromParent ( nodes );
    }

    /**
     * Removes nodes from tree structure.
     * This method will have effect only if nodes exist.
     *
     * @param nodes array of nodes to remove
     */
    public void removeNodes ( final N[] nodes )
    {
        getModel ().removeNodesFromParent ( nodes );
    }

    /**
     * Returns whether children for the specified node are already loaded or not.
     *
     * @param parent node to process
     * @return true if children for the specified node are already loaded, false otherwise
     */
    public boolean areChildrenLoaded ( final N parent )
    {
        return getModel ().areChildrenLoaded ( parent );
    }

    /**
     * Looks for the node with the specified ID in the tree model and returns it or null if it was not found.
     *
     * @param nodeId node ID
     * @return node with the specified ID or null if it was not found
     */
    public N findNode ( final String nodeId )
    {
        return getModel ().findNode ( nodeId );
    }

    /**
     * Forces tree node with the specified ID to be updated.
     *
     * @param nodeId ID of the tree node to be updated
     */
    public void updateNode ( final String nodeId )
    {
        updateNode ( findNode ( nodeId ) );
    }

    /**
     * Forces tree node structure with the specified ID to be updated.
     *
     * @param nodeId ID of the tree node to be updated
     */
    public void updateNodeStructure ( final String nodeId )
    {
        updateNodeStructure ( findNode ( nodeId ) );
    }

    /**
     * Forces tree node structure with the specified ID to be updated.
     *
     * @param node tree node to be updated
     */
    public void updateNodeStructure ( final N node )
    {
        getModel ().updateNodeStructure ( node );
    }

    /**
     * Reloads selected node children.
     * Unlike asynchronous methods this one works in EDT and forces to wait until the nodes load finishes.
     */
    public void reloadSelectedNodesSync ()
    {
        final boolean async = isAsyncLoading ();
        setAsyncLoading ( false );
        reloadSelectedNodes ();
        setAsyncLoading ( async );
    }

    /**
     * Reloads selected node children.
     */
    public void reloadSelectedNodes ()
    {
        // Checking that selection is not empty
        final TreePath[] paths = getSelectionPaths ();
        if ( paths != null )
        {
            // Reloading all selected nodes
            for ( final TreePath path : paths )
            {
                // Checking if node is not null and not busy yet
                final N node = getNodeForPath ( path );
                if ( node != null && !node.isLoading () )
                {
                    // Reloading node children
                    performReload ( node, path, false );
                }
            }
        }
    }

    /**
     * Reloads node under the specified point.
     *
     * @param point point to look for node
     * @return reloaded node or null if none reloaded
     */
    public N reloadNodeUnderPoint ( final Point point )
    {
        return reloadNodeUnderPoint ( point.x, point.y );
    }

    /**
     * Reloads node under the specified point.
     *
     * @param x point X coordinate
     * @param y point Y coordinate
     * @return reloaded node or null if none reloaded
     */
    public N reloadNodeUnderPoint ( final int x, final int y )
    {
        return reloadPath ( getPathForLocation ( x, y ) );
    }

    /**
     * Reloads node with the specified ID.
     * Unlike asynchronous methods this one works in EDT and forces to wait until the nodes load finishes.
     *
     * @param nodeId ID of the node to reload
     * @return reloaded node or null if none reloaded
     */
    public N reloadNodeSync ( final String nodeId )
    {
        return reloadNodeSync ( findNode ( nodeId ) );
    }

    /**
     * Reloads specified node children.
     * Unlike asynchronous methods this one works in EDT and forces to wait until the nodes load finishes.
     *
     * @param node node to reload
     * @return reloaded node or null if none reloaded
     */
    public N reloadNodeSync ( final N node )
    {
        return reloadNodeSync ( node, false );
    }

    /**
     * Reloads specified node children and selects it if requested.
     * Unlike asynchronous methods this one works in EDT and forces to wait until the nodes load finishes.
     *
     * @param node   node to reload
     * @param select whether select the node or not
     * @return reloaded node or null if none reloaded
     */
    public N reloadNodeSync ( final N node, final boolean select )
    {
        final boolean async = isAsyncLoading ();
        setAsyncLoading ( false );
        reloadNode ( node, select );
        setAsyncLoading ( async );
        return node;
    }

    /**
     * Reloads root node children.
     *
     * @return reloaded root node
     */
    public N reloadRootNode ()
    {
        return reloadNode ( getRootNode () );
    }

    /**
     * Reloads node with the specified ID.
     *
     * @param nodeId ID of the node to reload
     * @return reloaded node or null if none reloaded
     */
    public N reloadNode ( final String nodeId )
    {
        return reloadNode ( findNode ( nodeId ) );
    }

    /**
     * Reloads specified node children.
     *
     * @param node node to reload
     * @return reloaded node or null if none reloaded
     */
    public N reloadNode ( final N node )
    {
        return reloadNode ( node, false );
    }

    /**
     * Reloads specified node children and selects it if requested.
     *
     * @param node   node to reload
     * @param select whether select the node or not
     * @return reloaded node or null if none reloaded
     */
    public N reloadNode ( final N node, final boolean select )
    {
        // Checking that node is not null
        if ( node != null && !node.isLoading () )
        {
            // Reloading node children
            performReload ( node, getPathForNode ( node ), select );
            return node;
        }
        return null;
    }

    /**
     * Reloads node children at the specified path.
     * Unlike asynchronous methods this one works in EDT and forces to wait until the nodes load finishes.
     *
     * @param path path of the node to reload
     * @return reloaded node or null if none reloaded
     */
    public N reloadPathSync ( final TreePath path )
    {
        return reloadPathSync ( path, false );
    }

    /**
     * Reloads node children at the specified path and selects it if needed.
     * Unlike asynchronous methods this one works in EDT and forces to wait until the nodes load finishes.
     *
     * @param path   path of the node to reload
     * @param select whether select the node or not
     * @return reloaded node or null if none reloaded
     */
    public N reloadPathSync ( final TreePath path, final boolean select )
    {
        final boolean async = isAsyncLoading ();
        setAsyncLoading ( false );
        final N node = reloadPath ( path, select );
        setAsyncLoading ( async );
        return node;
    }

    /**
     * Reloads node children at the specified path.
     *
     * @param path path of the node to reload
     * @return reloaded node or null if none reloaded
     */
    public N reloadPath ( final TreePath path )
    {
        return reloadPath ( path, false );
    }

    /**
     * Reloads node children at the specified path and selects it if needed.
     *
     * @param path   path of the node to reload
     * @param select whether select the node or not
     * @return reloaded node or null if none reloaded
     */
    public N reloadPath ( final TreePath path, final boolean select )
    {
        // Checking that path is not null
        if ( path != null )
        {
            // Checking if node is not null and not busy yet
            final N node = getNodeForPath ( path );
            if ( node != null && !node.isLoading () )
            {
                // Reloading node children
                performReload ( node, path, select );
                return node;
            }
        }
        return null;
    }

    /**
     * Performs the actual reload call.
     *
     * @param node   node to reload
     * @param path   path to node
     * @param select whether select the node or not
     */
    protected void performReload ( final N node, final TreePath path, final boolean select )
    {
        // Select node under the mouse
        if ( select && !isPathSelected ( path ) )
        {
            setSelectionPath ( path );
        }

        // Expand the selected node since the collapsed node will ignore reload call
        // In case the node children were not loaded yet this call will cause it to load children
        if ( !isExpanded ( path ) )
        {
            expandPath ( path );
        }

        // Reload selected node children
        // This won't be called if node was not loaded yet since expand would call load before
        if ( node != null && !node.isLoading () )
        {
            getModel ().reload ( node );
        }
    }

    /**
     * Expands node with the specified ID.
     *
     * @param nodeId ID of the node to expand
     */
    public void expandNode ( final String nodeId )
    {
        expandNode ( findNode ( nodeId ) );
    }

    /**
     * Expands path using the specified node path IDs.
     * IDs are used to find real nodes within the expanded roots.
     * Be aware that operation might stop even before reaching the end of the path if something unexpected happened.
     *
     * @param pathNodeIds node path IDs
     */
    public void expandPath ( final List<String> pathNodeIds )
    {
        expandPath ( pathNodeIds, true, true, null );
    }

    /**
     * Expands path using the specified node path IDs.
     * IDs are used to find real nodes within the expanded roots.
     * Be aware that operation might stop even before reaching the end of the path if something unexpected happened.
     *
     * @param pathNodeIds node path IDs
     * @param listener    async path expansion listener
     */
    public void expandPath ( final List<String> pathNodeIds, final AsyncPathExpansionListener listener )
    {
        expandPath ( pathNodeIds, true, true, listener );
    }

    /**
     * Expands path using the specified node path IDs.
     * IDs are used to find real nodes within the expanded roots.
     * Be aware that operation might stop even before reaching the end of the path if something unexpected happened.
     *
     * @param pathNodeIds    node path IDs
     * @param expandLastNode whether should expand last found path node or not
     */
    public void expandPath ( final List<String> pathNodeIds, final boolean expandLastNode )
    {
        expandPath ( pathNodeIds, expandLastNode, true, null );
    }

    /**
     * Expands path using the specified node path IDs.
     * IDs are used to find real nodes within the expanded roots.
     * Be aware that operation might stop even before reaching the end of the path if something unexpected happened.
     *
     * @param pathNodeIds    node path IDs
     * @param expandLastNode whether should expand last found path node or not
     * @param listener       async path expansion listener
     */
    public void expandPath ( final List<String> pathNodeIds, final boolean expandLastNode, final AsyncPathExpansionListener listener )
    {
        expandPath ( pathNodeIds, expandLastNode, true, listener );
    }

    /**
     * Expands path using the specified node path IDs.
     * IDs are used to find real nodes within the expanded roots.
     * Be aware that operation might stop even before reaching the end of the path if something unexpected happened.
     *
     * @param pathNodeIds    node path IDs
     * @param expandLastNode whether should expand last found path node or not
     * @param selectLastNode whether should select last found path node or not
     */
    public void expandPath ( final List<String> pathNodeIds, final boolean expandLastNode, final boolean selectLastNode )
    {
        expandPath ( pathNodeIds, expandLastNode, selectLastNode, null );
    }

    /**
     * Expands path using the specified node path IDs.
     * IDs are used to find real nodes within the expanded roots.
     * Be aware that operation might stop even before reaching the end of the path if something unexpected happened.
     *
     * @param pathNodeIds    node path IDs
     * @param expandLastNode whether should expand last found path node or not
     * @param selectLastNode whether should select last found path node or not
     * @param listener       async path expansion listener
     */
    public void expandPath ( final List<String> pathNodeIds, final boolean expandLastNode, final boolean selectLastNode,
                             final AsyncPathExpansionListener listener )
    {
        final List<String> ids = CollectionUtils.copy ( pathNodeIds );
        for ( int initial = 0; initial < ids.size (); initial++ )
        {
            final N initialNode = findNode ( ids.get ( initial ) );
            if ( initialNode != null )
            {
                for ( int i = initial; i >= 0; i-- )
                {
                    ids.remove ( i );
                }
                if ( ids.size () > 0 )
                {
                    expandPathImpl ( initialNode, ids, expandLastNode, selectLastNode, listener );
                }
                return;
            }
        }

        // Informing listener that path failed to expand
        if ( listener != null )
        {
            listener.pathFailedToExpand ();
        }
    }

    /**
     * Performs a single path node expansion.
     * Be aware that operation might stop even before reaching the end of the path if something unexpected happened.
     *
     * @param currentNode    last reached node
     * @param leftToExpand   node path IDs left for expansion
     * @param expandLastNode whether should expand last found path node or not
     * @param selectLastNode whether should select last found path node or not
     * @param listener       async path expansion listener
     */
    protected void expandPathImpl ( final N currentNode, final List<String> leftToExpand, final boolean expandLastNode,
                                    final boolean selectLastNode, final AsyncPathExpansionListener listener )
    {
        // There is still more to load
        if ( leftToExpand.size () > 0 )
        {
            if ( currentNode.isLoaded () )
            {
                // Expanding already loaded node
                expandNode ( currentNode );

                // Informing listener that one of path nodes was just expanded
                if ( listener != null )
                {
                    listener.pathNodeExpanded ( currentNode );
                }

                // Retrieving next node
                final N nextNode = findNode ( leftToExpand.get ( 0 ) );
                leftToExpand.remove ( 0 );

                // If node exists continue expanding path
                if ( nextNode != null )
                {
                    expandPathImpl ( nextNode, leftToExpand, expandLastNode, selectLastNode, listener );
                }
                else
                {
                    expandPathEndImpl ( currentNode, expandLastNode, selectLastNode );

                    // Informing listener that path was expanded as much as it was possible
                    if ( listener != null )
                    {
                        listener.pathPartiallyExpanded ( currentNode );
                    }
                }
            }
            else
            {
                // Adding node expansion listener
                addAsyncTreeListener ( new AsyncTreeAdapter ()
                {
                    @Override
                    public void loadCompleted ( final AsyncUniqueNode parent, final List children )
                    {
                        if ( parent.getId ().equals ( currentNode.getId () ) )
                        {
                            removeAsyncTreeListener ( this );

                            // Informing listener that one of path nodes was just expanded
                            if ( listener != null )
                            {
                                listener.pathNodeExpanded ( currentNode );
                            }

                            // Retrieving next node
                            final N nextNode = findNode ( leftToExpand.get ( 0 ) );
                            leftToExpand.remove ( 0 );

                            // If node exists continue expanding path
                            if ( nextNode != null )
                            {
                                expandPathImpl ( nextNode, leftToExpand, expandLastNode, selectLastNode, listener );
                            }
                            else
                            {
                                expandPathEndImpl ( currentNode, expandLastNode, selectLastNode );

                                // Informing listener that path was expanded as much as it was possible
                                if ( listener != null )
                                {
                                    listener.pathPartiallyExpanded ( currentNode );
                                }
                            }
                        }
                    }

                    @Override
                    public void loadFailed ( final AsyncUniqueNode parent, final Throwable cause )
                    {
                        if ( parent.getId ().equals ( currentNode.getId () ) )
                        {
                            removeAsyncTreeListener ( this );
                            expandPathEndImpl ( currentNode, expandLastNode, selectLastNode );

                            // Informing listener that path was expanded as much as it was possible
                            if ( listener != null )
                            {
                                listener.pathPartiallyExpanded ( currentNode );
                            }
                        }
                    }
                } );
                expandNode ( currentNode );
            }
        }
        else
        {
            expandPathEndImpl ( currentNode, expandLastNode, selectLastNode );

            // todo Maybe wait till last node expands?
            // Informing listener that path was fully expanded
            if ( listener != null )
            {
                listener.pathExpanded ( currentNode );
            }
        }
    }

    /**
     * Finishes async tree path expansion.
     *
     * @param lastFoundNode  last found path node
     * @param expandLastNode whether should expand last found path node or not
     * @param selectLastNode whether should select last found path node or not
     */
    protected void expandPathEndImpl ( final N lastFoundNode, final boolean expandLastNode, final boolean selectLastNode )
    {
        if ( selectLastNode )
        {
            setSelectedNode ( lastFoundNode );
        }
        if ( expandLastNode )
        {
            expandNode ( lastFoundNode );
        }
    }

    /**
     * Expands all visible tree rows in a single call.
     * This method provides similar functionality to {@link WebTree#expandAll()} method and will actually expand all tree elements,
     * even those which are not yet loaded from data provider. Make sure you know what you are doing before calling this method.
     */
    @Override
    public final void expandAll ()
    {
        if ( isAsyncLoading () )
        {
            for ( int i = getRowCount () - 1; i >= 0; i-- )
            {
                final TreePath path = getPathForRow ( i );
                if ( !getModel ().isLeaf ( getNodeForPath ( path ) ) )
                {
                    performFullPathExpand ( path );
                }
            }
        }
        else
        {
            super.expandAll ();
        }
    }

    /**
     * Expands path right away (if node children were loaded atleast once before or not) or asynchronously.
     *
     * @param path path to expand
     */
    protected void performFullPathExpand ( final TreePath path )
    {
        if ( hasBeenExpanded ( path ) )
        {
            performFullSyncPathExpand ( path );
        }
        else
        {
            performFullAsyncPathExpand ( path );
        }
    }

    /**
     * Expands path right away.
     *
     * @param path path to expand
     */
    protected void performFullSyncPathExpand ( final TreePath path )
    {
        // Expand path
        expandPath ( path );

        // Expand sub-paths
        final N node = getNodeForPath ( path );
        for ( int i = 0; i < node.getChildCount (); i++ )
        {
            performFullPathExpand ( getPathForNode ( ( N ) node.getChildAt ( i ) ) );
        }
    }

    /**
     * Expands path asynchronously.
     *
     * @param path path to expand
     */
    protected void performFullAsyncPathExpand ( final TreePath path )
    {
        // Add path expand listener first to get notified when this path will be expanded
        addAsyncTreeListener ( new AsyncTreeAdapter<N> ()
        {
            @Override
            public void loadCompleted ( final N parent, final List<N> children )
            {
                if ( parent == getNodeForPath ( path ) )
                {
                    for ( final N child : children )
                    {
                        performFullPathExpand ( child.getTreePath () );
                    }
                    removeAsyncTreeListener ( this );
                }
            }

            @Override
            public void loadFailed ( final N parent, final Throwable cause )
            {
                if ( parent == getNodeForPath ( path ) )
                {
                    removeAsyncTreeListener ( this );
                }
            }
        } );

        // Force path to expand
        expandPath ( path );
    }

    /**
     * Returns all available asynchronous tree listeners list.
     *
     * @return asynchronous tree listeners list
     */
    public List<AsyncTreeListener> getAsyncTreeListeners ()
    {
        return CollectionUtils.asList ( listenerList.getListeners ( AsyncTreeListener.class ) );
    }

    /**
     * Adds new asynchronous tree listener.
     *
     * @param listener asynchronous tree listener to add
     */
    public void addAsyncTreeListener ( final AsyncTreeListener listener )
    {
        listenerList.add ( AsyncTreeListener.class, listener );
    }

    /**
     * Removes asynchronous tree listener.
     *
     * @param listener asynchronous tree listener to remove
     */
    public void removeAsyncTreeListener ( final AsyncTreeListener listener )
    {
        listenerList.remove ( AsyncTreeListener.class, listener );
    }

    /**
     * Invoked when children load operation starts.
     *
     * @param parent node which children are being loaded
     */
    @Override
    public void loadStarted ( final N parent )
    {
        fireChildrenLoadStarted ( parent );
    }

    /**
     * Fires children load start event.
     *
     * @param parent node which children are being loaded
     */
    protected void fireChildrenLoadStarted ( final N parent )
    {
        for ( final AsyncTreeListener listener : listenerList.getListeners ( AsyncTreeListener.class ) )
        {
            listener.loadStarted ( parent );
        }
    }

    /**
     * Invoked when children load operation finishes.
     *
     * @param parent   node which children were loaded
     * @param children loaded child nodes
     */
    @Override
    public void loadCompleted ( final N parent, final List<N> children )
    {
        fireChildrenLoadCompleted ( parent, children );
    }

    /**
     * Fires children load complete event.
     *
     * @param parent   node which children were loaded
     * @param children loaded child nodes
     */
    protected void fireChildrenLoadCompleted ( final N parent, final List<N> children )
    {
        for ( final AsyncTreeListener listener : listenerList.getListeners ( AsyncTreeListener.class ) )
        {
            listener.loadCompleted ( parent, children );
        }
    }

    /**
     * Invoked when children load operation fails.
     *
     * @param parent node which children were loaded
     * @param cause  children load failure cause
     */
    @Override
    public void loadFailed ( final N parent, final Throwable cause )
    {
        fireChildrenLoadFailed ( parent, cause );
    }

    /**
     * Fires children load complete event.
     *
     * @param parent node which children were loaded
     * @param cause  children load failure cause
     */
    protected void fireChildrenLoadFailed ( final N parent, final Throwable cause )
    {
        for ( final AsyncTreeListener listener : listenerList.getListeners ( AsyncTreeListener.class ) )
        {
            listener.loadFailed ( parent, cause );
        }
    }
}