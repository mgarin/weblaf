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
import com.alee.api.annotations.Nullable;
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
    public static final String DATA_PROVIDER_PROPERTY = "dataProvider";
    public static final String FILTER_PROPERTY = "filter";
    public static final String COMPARATOR_PROPERTY = "comparator";
    public static final String ASYNC_LOADING_PROPERTY = "asyncLoading";

    /**
     * Whether to load children asynchronously or not.
     */
    @Nullable
    protected Boolean asyncLoading;

    /**
     * Tree nodes filter.
     */
    @Nullable
    protected Filter<N> filter;

    /**
     * Tree nodes comparator.
     */
    @Nullable
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
    public WebAsyncTree ( @Nullable final AsyncTreeDataProvider dataProvider )
    {
        this ( StyleId.auto, dataProvider );
    }

    /**
     * Costructs new {@link WebAsyncTree} with the specified {@link AsyncTreeDataProvider} as data source.
     *
     * @param dataProvider {@link AsyncTreeDataProvider} implementation
     * @param renderer     {@link TreeCellRenderer} implementation, default implementation is used if {@code null} is provided
     */
    public WebAsyncTree ( @Nullable final AsyncTreeDataProvider dataProvider, @Nullable final TreeCellRenderer renderer )
    {
        this ( StyleId.auto, dataProvider, renderer );
    }

    /**
     * Costructs new {@link WebAsyncTree} with the specified {@link AsyncTreeDataProvider} as data source.
     *
     * @param dataProvider {@link AsyncTreeDataProvider} implementation
     * @param editor       {@link TreeCellEditor} implementation, default implementation is used if {@code null} is provided
     */
    public WebAsyncTree ( @Nullable final AsyncTreeDataProvider dataProvider, @Nullable final TreeCellEditor editor )
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
    public WebAsyncTree ( @Nullable final AsyncTreeDataProvider dataProvider, @Nullable final TreeCellRenderer renderer,
                          @Nullable final TreeCellEditor editor )
    {
        this ( StyleId.auto, dataProvider, renderer, editor );
    }

    /**
     * Constructs new {@link WebAsyncTree} with sample data.
     *
     * @param id {@link StyleId}
     */
    public WebAsyncTree ( @NotNull final StyleId id )
    {
        this ( id, null, null, null );
    }

    /**
     * Costructs new {@link WebAsyncTree} with the specified {@link AsyncTreeDataProvider} as data source.
     *
     * @param id           {@link StyleId}
     * @param dataProvider {@link AsyncTreeDataProvider} implementation
     */
    public WebAsyncTree ( @NotNull final StyleId id, @Nullable final AsyncTreeDataProvider dataProvider )
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
    public WebAsyncTree ( @NotNull final StyleId id, @Nullable final AsyncTreeDataProvider dataProvider,
                          @Nullable final TreeCellRenderer renderer )
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
    public WebAsyncTree ( @NotNull final StyleId id, @Nullable final AsyncTreeDataProvider dataProvider,
                          @Nullable final TreeCellEditor editor )
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
    public WebAsyncTree ( @NotNull final StyleId id, @Nullable final AsyncTreeDataProvider dataProvider,
                          @Nullable final TreeCellRenderer renderer, @Nullable final TreeCellEditor editor )
    {
        super ( id, dataProvider != null ? new AsyncTreeModel<N> ( dataProvider ) : null );
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

    @NotNull
    @Override
    public StyleId getDefaultStyleId ()
    {
        return StyleId.asynctree;
    }

    @Nullable
    @Override
    public AsyncTreeModel<N> getModel ()
    {
        return ( AsyncTreeModel<N> ) super.getModel ();
    }

    @Override
    public void setModel ( @Nullable final TreeModel newModel )
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
            final AsyncTreeDataProvider<N> oldDataProvider;
            if ( old != null )
            {
                oldDataProvider = old.getDataProvider ();
                old.uninstall ( this );
            }
            else
            {
                oldDataProvider = null;
            }

            final AsyncTreeModel model = ( AsyncTreeModel ) newModel;
            model.install ( this );

            super.setModel ( model );

            firePropertyChange ( DATA_PROVIDER_PROPERTY, oldDataProvider, model.getDataProvider () );
        }
        else if ( newModel != null )
        {
            throw new NullPointerException ( "Only AsyncTreeModel implementations can be used for WebAsyncTree" );
        }
    }

    /**
     * Returns {@link AsyncTreeDataProvider} used by this {@link WebAsyncTree}.
     *
     * @return {@link AsyncTreeDataProvider} used by this {@link WebAsyncTree}
     */
    @Nullable
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
    public void setDataProvider ( @NotNull final AsyncTreeDataProvider dataProvider )
    {
        // Event Dispatch Thread check
        WebLookAndFeel.checkEventDispatchThread ();

        /**
         * Initializing new {@link AsyncTreeModel} based on specified {@link AsyncTreeDataProvider}.
         * This is necessary as the model will keep {@link AsyncTreeDataProvider} instead of {@link WebAsyncTree}.
         */
        setModel ( new AsyncTreeModel<N> ( dataProvider ) );
    }

    /**
     * Returns whether children are loaded asynchronously or not.
     *
     * @return true if children are loaded asynchronously, false otherwise
     */
    public boolean isAsyncLoading ()
    {
        return asyncLoading == null || asyncLoading;
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

        // Ensure parameter changed
        if ( asyncLoading != isAsyncLoading () )
        {
            final boolean old = isAsyncLoading ();
            this.asyncLoading = asyncLoading;
            firePropertyChange ( ASYNC_LOADING_PROPERTY, old, asyncLoading );
        }
    }

    @Nullable
    @Override
    public Filter<N> getFilter ()
    {
        return filter;
    }

    @Override
    public void setFilter ( @Nullable final Filter<N> filter )
    {
        // Event Dispatch Thread check
        WebLookAndFeel.checkEventDispatchThread ();

        // Ensure parameter changed
        if ( filter != getFilter () )
        {
            final Filter<N> old = getFilter ();
            this.filter = filter;
            filter ();
            firePropertyChange ( FILTER_PROPERTY, old, filter );
        }
    }

    @Override
    public void clearFilter ()
    {
        setFilter ( null );
    }

    @Override
    public void filter ()
    {
        final AsyncTreeModel<N> model = getModel ();
        if ( model != null )
        {
            model.filter ();
        }
    }

    @Override
    public void filter ( @NotNull final N parent )
    {
        final AsyncTreeModel<N> model = getModel ();
        if ( model != null )
        {
            model.filter ( parent );
        }
    }

    @Override
    public void filter ( @NotNull final N parent, final boolean recursively )
    {
        final AsyncTreeModel<N> model = getModel ();
        if ( model != null )
        {
            model.filter ( parent, recursively );
        }
    }

    @Nullable
    @Override
    public Comparator<N> getComparator ()
    {
        return comparator;
    }

    @Override
    public void setComparator ( @Nullable final Comparator<N> comparator )
    {
        // Event Dispatch Thread check
        WebLookAndFeel.checkEventDispatchThread ();

        // Ensure parameter changed
        if ( comparator != getComparator () )
        {
            final Comparator<N> old = getComparator ();
            this.comparator = comparator;
            sort ();
            firePropertyChange ( COMPARATOR_PROPERTY, old, comparator );
        }
    }

    @Override
    public void clearComparator ()
    {
        setComparator ( null );
    }

    @Override
    public void sort ()
    {
        final AsyncTreeModel<N> model = getModel ();
        if ( model != null )
        {
            model.sort ();
        }
    }

    @Override
    public void sort ( @NotNull final N parent )
    {
        final AsyncTreeModel<N> model = getModel ();
        if ( model != null )
        {
            model.sort ( parent );
        }
    }

    @Override
    public void sort ( @NotNull final N parent, final boolean recursively )
    {
        final AsyncTreeModel<N> model = getModel ();
        if ( model != null )
        {
            model.sort ( parent, recursively );
        }
    }

    /**
     * Updates nodes sorting and filtering for all loaded nodes.
     */
    public void filterAndSort ()
    {
        final AsyncTreeModel<N> model = getModel ();
        if ( model != null )
        {
            model.filterAndSort ( true );
        }
    }

    /**
     * Updates sorting and filtering for the specified node children.
     *
     * @param parent node to update sorting and filter for
     */
    public void filterAndSort ( @NotNull final N parent )
    {
        final AsyncTreeModel<N> model = getModel ();
        if ( model != null )
        {
            model.filterAndSort ( parent, false );
        }
    }

    /**
     * Updates sorting and filtering for the specified node children.
     *
     * @param parent      node to update sorting and filter for
     * @param recursively whether should update the whole children structure recursively or not
     */
    public void filterAndSort ( @NotNull final N parent, final boolean recursively )
    {
        final AsyncTreeModel<N> model = getModel ();
        if ( model != null )
        {
            model.filterAndSort ( parent, recursively );
        }
    }

    /**
     * Sets child nodes for the specified node.
     * This method might be used to manually change tree node children without causing any structure corruptions.
     * It will also cause node to change its state to loaded and it will not retrieve children from data provider unless reload is called.
     *
     * @param parent   node to process
     * @param children new node children
     */
    public void setChildNodes ( @NotNull final N parent, @NotNull final List<N> children )
    {
        final AsyncTreeModel<N> model = getModel ();
        if ( model != null )
        {
            model.setChildNodes ( parent, children );
        }
    }

    /**
     * Adds child node for the specified node.
     * This method might be used to manually change tree node children without causing any structure corruptions.
     * Be aware that added node will not be displayed if parent node is not yet loaded, this is a strict restriction for async tree.
     *
     * @param parent node to process
     * @param child  new node child
     */
    public void addChildNode ( @NotNull final N parent, @NotNull final N child )
    {
        final AsyncTreeModel<N> model = getModel ();
        if ( model != null )
        {
            model.addChildNode ( parent, child );
        }
    }

    /**
     * Adds child nodes for the specified node.
     * This method might be used to manually change tree node children without causing any structure corruptions.
     * Be aware that added nodes will not be displayed if parent node is not yet loaded, this is a strict restriction for async tree.
     *
     * @param parent   node to process
     * @param children new node children
     */
    public void addChildNodes ( @NotNull final N parent, @NotNull final List<N> children )
    {
        final AsyncTreeModel<N> model = getModel ();
        if ( model != null )
        {
            model.addChildNodes ( parent, children );
        }
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
    public void insertChildNodes ( @NotNull final List<N> children, @NotNull final N parent, final int index )
    {
        final AsyncTreeModel<N> model = getModel ();
        if ( model != null )
        {
            model.insertNodesInto ( children, parent, index );
        }
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
    public void insertChildNodes ( @NotNull final N[] children, @NotNull final N parent, final int index )
    {
        final AsyncTreeModel<N> model = getModel ();
        if ( model != null )
        {
            model.insertNodesInto ( children, parent, index );
        }
    }

    /**
     * Inserts child node into parent node.
     * This method might be used to manually change tree node children without causing any structure corruptions.
     *
     * @param child  new child node
     * @param parent parent node
     * @param index  insert index
     */
    public void insertChildNode ( @NotNull final N child, @NotNull final N parent, final int index )
    {
        final AsyncTreeModel<N> model = getModel ();
        if ( model != null )
        {
            model.insertNodeInto ( child, parent, index );
        }
    }

    /**
     * Removes node with the specified ID from tree structure.
     * This method will have effect only if node exists.
     *
     * @param nodeId ID of the node to remove
     */
    public void removeNode ( @NotNull final String nodeId )
    {
        final N node = findNode ( nodeId );
        if ( node != null )
        {
            removeNode ( node );
        }
    }

    /**
     * Removes node from tree structure.
     * This method will have effect only if node exists.
     *
     * @param node node to remove
     */
    public void removeNode ( @NotNull final N node )
    {
        final AsyncTreeModel<N> model = getModel ();
        if ( model != null )
        {
            model.removeNodeFromParent ( node );
        }
    }

    /**
     * Removes nodes from tree structure.
     * This method will have effect only if nodes exist.
     *
     * @param nodes list of nodes to remove
     */
    public void removeNodes ( @NotNull final List<N> nodes )
    {
        final AsyncTreeModel<N> model = getModel ();
        if ( model != null )
        {
            model.removeNodesFromParent ( nodes );
        }
    }

    /**
     * Removes nodes from tree structure.
     * This method will have effect only if nodes exist.
     *
     * @param nodes array of nodes to remove
     */
    public void removeNodes ( @NotNull final N[] nodes )
    {
        final AsyncTreeModel<N> model = getModel ();
        if ( model != null )
        {
            model.removeNodesFromParent ( nodes );
        }
    }

    /**
     * Returns whether children for the specified node are already loaded or not.
     *
     * @param parent node to process
     * @return true if children for the specified node are already loaded, false otherwise
     */
    public boolean areChildrenLoaded ( @NotNull final N parent )
    {
        final AsyncTreeModel<N> model = getModel ();
        return model != null && model.areChildrenLoaded ( parent );
    }

    /**
     * Looks for the node with the specified ID in the tree model and returns it or null if it was not found.
     *
     * @param nodeId node ID
     * @return node with the specified ID or null if it was not found
     */
    @Nullable
    public N findNode ( @NotNull final String nodeId )
    {
        final AsyncTreeModel<N> model = getModel ();
        return model != null ? model.findNode ( nodeId ) : null;
    }

    /**
     * Forces tree node with the specified ID to be updated.
     *
     * @param nodeId ID of the tree node to be updated
     */
    public void updateNode ( @NotNull final String nodeId )
    {
        updateNode ( findNode ( nodeId ) );
    }

    /**
     * Forces tree node structure with the specified ID to be updated.
     *
     * @param nodeId ID of the tree node to be updated
     */
    public void updateNodeStructure ( @NotNull final String nodeId )
    {
        updateNodeStructure ( findNode ( nodeId ) );
    }

    /**
     * Forces tree node structure with the specified ID to be updated.
     *
     * @param node tree node to be updated
     */
    public void updateNodeStructure ( @Nullable final N node )
    {
        final AsyncTreeModel<N> model = getModel ();
        if ( model != null )
        {
            model.updateNodeStructure ( node );
        }
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
        final TreePath[] paths = getSelectionPaths ();
        if ( paths != null )
        {
            for ( final TreePath path : paths )
            {
                final N node = getNodeForPath ( path );
                if ( node != null && !node.isLoading () )
                {
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
    @Nullable
    public N reloadNodeUnderPoint ( @NotNull final Point point )
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
    @Nullable
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
    @Nullable
    public N reloadNodeSync ( @NotNull final String nodeId )
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
    @Nullable
    public N reloadNodeSync ( @Nullable final N node )
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
    @Nullable
    public N reloadNodeSync ( @Nullable final N node, final boolean select )
    {
        final boolean async = isAsyncLoading ();
        setAsyncLoading ( false );
        final N reloadedNode = reloadNode ( node, select );
        setAsyncLoading ( async );
        return reloadedNode;
    }

    /**
     * Reloads root node children.
     *
     * @return reloaded root node
     */
    @Nullable
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
    @Nullable
    public N reloadNode ( @NotNull final String nodeId )
    {
        return reloadNode ( findNode ( nodeId ) );
    }

    /**
     * Reloads specified node children.
     *
     * @param node node to reload
     * @return reloaded node or null if none reloaded
     */
    @Nullable
    public N reloadNode ( @Nullable final N node )
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
    @Nullable
    public N reloadNode ( @Nullable final N node, final boolean select )
    {
        N reloadedNode = null;
        if ( node != null && !node.isLoading () )
        {
            final TreePath path = getPathForNode ( node );
            if ( path != null )
            {
                performReload ( node, path, select );
                reloadedNode = node;
            }
        }
        return reloadedNode;
    }

    /**
     * Reloads node children at the specified path.
     * Unlike asynchronous methods this one works in EDT and forces to wait until the nodes load finishes.
     *
     * @param path path of the node to reload
     * @return reloaded node or null if none reloaded
     */
    @Nullable
    public N reloadPathSync ( @Nullable final TreePath path )
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
    @Nullable
    public N reloadPathSync ( @Nullable final TreePath path, final boolean select )
    {
        final boolean async = isAsyncLoading ();
        setAsyncLoading ( false );
        final N reloadedNode = reloadPath ( path, select );
        setAsyncLoading ( async );
        return reloadedNode;
    }

    /**
     * Reloads node children at the specified path.
     *
     * @param path path of the node to reload
     * @return reloaded node or null if none reloaded
     */
    @Nullable
    public N reloadPath ( @Nullable final TreePath path )
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
    @Nullable
    public N reloadPath ( @Nullable final TreePath path, final boolean select )
    {
        N reloadedNode = null;
        if ( path != null )
        {
            final N node = getNodeForPath ( path );
            if ( node != null && !node.isLoading () )
            {
                performReload ( node, path, select );
                reloadedNode = node;
            }
        }
        return reloadedNode;
    }

    /**
     * Performs the actual reload call.
     *
     * @param node   node to reload
     * @param path   path to node
     * @param select whether select the node or not
     */
    protected void performReload ( @NotNull final N node, @NotNull final TreePath path, final boolean select )
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
        if ( !node.isLoading () )
        {
            final AsyncTreeModel<N> model = getModel ();
            if ( model != null )
            {
                model.reload ( node );
            }
        }
    }

    /**
     * Expands node with the specified ID.
     *
     * @param nodeId ID of the node to expand
     */
    public void expandNode ( @NotNull final String nodeId )
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
    public void expandPath ( @NotNull final List<String> pathNodeIds )
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
    public void expandPath ( @NotNull final List<String> pathNodeIds, @Nullable final AsyncPathExpansionListener<N> listener )
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
    public void expandPath ( @NotNull final List<String> pathNodeIds, final boolean expandLastNode )
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
    public void expandPath ( @NotNull final List<String> pathNodeIds, final boolean expandLastNode,
                             @Nullable final AsyncPathExpansionListener<N> listener )
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
    public void expandPath ( @NotNull final List<String> pathNodeIds, final boolean expandLastNode, final boolean selectLastNode )
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
    public void expandPath ( @NotNull final List<String> pathNodeIds, final boolean expandLastNode, final boolean selectLastNode,
                             @Nullable final AsyncPathExpansionListener<N> listener )
    {
        boolean expanded = false;
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
                expanded = true;
                break;
            }
        }

        // Informing listener that path failed to expand
        if ( !expanded )
        {
            if ( listener != null )
            {
                listener.pathFailedToExpand ();
            }
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
    protected void expandPathImpl ( @NotNull final N currentNode, @NotNull final List<String> leftToExpand, final boolean expandLastNode,
                                    final boolean selectLastNode, @Nullable final AsyncPathExpansionListener<N> listener )
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
                    public void loadCompleted ( @NotNull final AsyncUniqueNode parent, @NotNull final List children )
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
                    public void loadFailed ( @NotNull final AsyncUniqueNode parent, @NotNull final Throwable cause )
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
    protected void expandPathEndImpl ( @Nullable final N lastFoundNode, final boolean expandLastNode, final boolean selectLastNode )
    {
        if ( selectLastNode )
        {
            setSelectedNode ( lastFoundNode );
            scrollToNode ( lastFoundNode );
        }
        if ( expandLastNode )
        {
            expandNode ( lastFoundNode );
        }
    }

    @Override
    protected void expandAllImpl ( @NotNull final N node, @Nullable final Filter<N> filter, final int depth )
    {
        final AsyncTreeModel<N> model = getModel ();
        if ( model != null )
        {
            if ( isAsyncLoading () )
            {
                if ( depth > 0 && ( filter == null || filter.accept ( node ) ) && !model.isLeaf ( node ) )
                {
                    if ( hasBeenExpanded ( getPathForNode ( node ) ) )
                    {
                        if ( !isExpanded ( node ) )
                        {
                            expandNode ( node );
                        }
                        for ( int i = 0; i < node.getChildCount (); i++ )
                        {
                            expandAllImpl ( ( N ) node.getChildAt ( i ), filter, depth - 1 );
                        }
                    }
                    else
                    {
                        if ( !isExpanded ( node ) )
                        {
                            addAsyncTreeListener ( new AsyncTreeAdapter<N> ()
                            {
                                @Override
                                public void loadCompleted ( @NotNull final N parent, @NotNull final List<N> children )
                                {
                                    if ( parent == node )
                                    {
                                        for ( final N child : children )
                                        {
                                            expandAllImpl ( child, filter, depth - 1 );
                                        }
                                        removeAsyncTreeListener ( this );
                                    }
                                }

                                @Override
                                public void loadFailed ( @NotNull final N parent, @NotNull final Throwable cause )
                                {
                                    if ( parent == node )
                                    {
                                        removeAsyncTreeListener ( this );
                                    }
                                }
                            } );
                            expandNode ( node );
                        }
                    }
                }
            }
            else
            {
                super.expandAllImpl ( node, filter, depth );
            }
        }
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
    public void loadStarted ( @NotNull final N parent )
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
    public void loadCompleted ( @NotNull final N parent, @NotNull final List<N> children )
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
    public void loadFailed ( @NotNull final N parent, @NotNull final Throwable cause )
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