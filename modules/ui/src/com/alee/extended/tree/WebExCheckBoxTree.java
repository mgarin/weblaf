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

import com.alee.extended.tree.sample.SampleExDataProvider;
import com.alee.extended.tree.sample.SampleTreeCellEditor;
import com.alee.laf.WebLookAndFeel;
import com.alee.laf.tree.UniqueNode;
import com.alee.laf.tree.WebTreeCellEditor;
import com.alee.laf.tree.WebTreeCellRenderer;
import com.alee.managers.style.StyleId;
import com.alee.utils.CollectionUtils;
import com.alee.utils.compare.Filter;

import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.util.Comparator;
import java.util.List;

/**
 * {@link WebCheckBoxTree} extension class.
 * It uses {@link ExTreeDataProvider} as data source instead of {@link TreeModel}.
 * This tree structure is always fully available and can be navigated through the nodes.
 *
 * This component should never be used with a non-Web UIs as it might cause an unexpected behavior.
 * You could still use that component even if WebLaF is not your application LaF as this component will use Web-UI in any case.
 *
 * @param <N> node type
 * @author Mikle Garin
 * @see WebCheckBoxTree
 * @see com.alee.laf.tree.WebTreeUI
 * @see com.alee.laf.tree.TreePainter
 * @see TreeCheckingModel
 * @see ExTreeModel
 * @see ExTreeDataProvider
 */

public class WebExCheckBoxTree<N extends UniqueNode> extends WebCheckBoxTree<N>
{
    /**
     * Tree nodes comparator.
     */
    protected Comparator<N> comparator;

    /**
     * Tree nodes filter.
     */
    protected Filter<N> filter;

    /**
     * Constructs sample ex checkbox tree.
     */
    public WebExCheckBoxTree ()
    {
        this ( StyleId.auto );
    }

    /**
     * Constructs sample ex checkbox tree.
     *
     * @param id style ID
     */
    public WebExCheckBoxTree ( final StyleId id )
    {
        super ( id );

        // Installing sample data provider
        setDataProvider ( new SampleExDataProvider () );

        // Tree cell renderer & editor
        setCellEditor ( new SampleTreeCellEditor () );
    }

    /**
     * Costructs ex checkbox tree using data from the custom data provider.
     *
     * @param dataProvider custom data provider
     */
    public WebExCheckBoxTree ( final ExTreeDataProvider dataProvider )
    {
        this ( StyleId.auto, dataProvider );
    }

    /**
     * Costructs ex checkbox tree using data from the custom data provider.
     *
     * @param id           style ID
     * @param dataProvider custom data provider
     */
    public WebExCheckBoxTree ( final StyleId id, final ExTreeDataProvider dataProvider )
    {
        super ( id );

        // Installing data provider
        setDataProvider ( dataProvider );

        // Tree cell renderer & editor
        setCellRenderer ( new WebTreeCellRenderer () );
        setCellEditor ( new WebTreeCellEditor () );
    }

    @Override
    public StyleId getDefaultStyleId ()
    {
        return StyleId.excheckboxtree;
    }

    /**
     * Returns ex tree data provider.
     *
     * @return data provider
     */
    public ExTreeDataProvider<N> getDataProvider ()
    {
        final TreeModel model = getModel ();
        return model != null && model instanceof ExTreeModel ? getExModel ().getDataProvider () : null;
    }

    /**
     * Changes data provider for this ex tree.
     *
     * @param dataProvider new data provider
     */
    public void setDataProvider ( final ExTreeDataProvider dataProvider )
    {
        if ( dataProvider != null )
        {
            final ExTreeDataProvider<N> oldDataProvider = getDataProvider ();

            // Updating model
            setModel ( new ExTreeModel<N> ( this, dataProvider ) );

            // Informing about data provider change
            firePropertyChange ( WebLookAndFeel.TREE_DATA_PROVIDER_PROPERTY, oldDataProvider, dataProvider );
        }
    }

    /**
     * Returns tree nodes comparator.
     *
     * @return tree nodes comparator
     */
    public Comparator<N> getComparator ()
    {
        return comparator;
    }

    /**
     * Sets tree nodes comparator.
     * Comparator replacement will automatically update all loaded nodes sorting.
     *
     * @param comparator tree nodes comparator
     */
    public void setComparator ( final Comparator<N> comparator )
    {
        final Comparator<N> oldComparator = this.comparator;
        this.comparator = comparator;

        final ExTreeDataProvider dataProvider = getDataProvider ();
        if ( dataProvider instanceof AbstractExTreeDataProvider )
        {
            ( ( AbstractExTreeDataProvider ) dataProvider ).setChildrenComparator ( comparator );
            filterAndSort ();
        }

        firePropertyChange ( WebLookAndFeel.TREE_COMPARATOR_PROPERTY, oldComparator, comparator );
    }

    /**
     * Removes any applied tree nodes comparator.
     */
    public void clearComparator ()
    {
        setComparator ( null );
    }

    /**
     * Returns tree nodes filter.
     *
     * @return tree nodes filter
     */
    public Filter<N> getFilter ()
    {
        return filter;
    }

    /**
     * Sets tree nodes filter.
     * Comparator replacement will automatically re-filter all loaded nodes.
     *
     * @param filter tree nodes filter
     */
    public void setFilter ( final Filter<N> filter )
    {
        final Filter<N> oldFilter = this.filter;
        this.filter = filter;

        final ExTreeDataProvider dataProvider = getDataProvider ();
        if ( dataProvider instanceof AbstractExTreeDataProvider )
        {
            ( ( AbstractExTreeDataProvider ) dataProvider ).setChildrenFilter ( filter );
            filterAndSort ();
        }

        firePropertyChange ( WebLookAndFeel.TREE_FILTER_PROPERTY, oldFilter, filter );
    }

    /**
     * Removes any applied tree nodes filter.
     */
    public void clearFilter ()
    {
        setFilter ( null );
    }

    /**
     * Updates nodes sorting and filtering for all loaded nodes.
     */
    public void filterAndSort ()
    {
        getExModel ().filterAndSort ( getRootNode (), true );
    }

    /**
     * Updates sorting and filtering for the specified node children.
     *
     * @param node node to update sorting and filtering for
     */
    public void filterAndSort ( final N node )
    {
        getExModel ().filterAndSort ( node, false );
    }

    /**
     * Returns ex tree model.
     *
     * @return ex tree model
     */
    public ExTreeModel<N> getExModel ()
    {
        return ( ExTreeModel<N> ) getModel ();
    }

    /**
     * Returns whether {@link ExTreeModel} is installed or not.
     *
     * @return {@code true} if {@link ExTreeModel} is installed, {@code false} otherwise
     */
    public boolean isExModel ()
    {
        final TreeModel model = getModel ();
        return model != null && model instanceof ExTreeModel;
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
        getExModel ().setChildNodes ( parent, children );
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
        getExModel ().addChildNode ( parent, child );
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
        getExModel ().addChildNodes ( parent, children );
    }

    /**
     * Inserts a list of child nodes into parent node.
     * This method might be used to manually change tree node children without causing any structure corruptions.
     *
     * @param children list of new child nodes
     * @param parent   parent node
     * @param index    insert index
     */
    public void insertChildNodes ( final List<N> children, final N parent, final int index )
    {
        getExModel ().insertNodesInto ( children, parent, index );
    }

    /**
     * Inserts an array of child nodes into parent node.
     * This method might be used to manually change tree node children without causing any structure corruptions.
     *
     * @param children array of new child nodes
     * @param parent   parent node
     * @param index    insert index
     */
    public void insertChildNodes ( final N[] children, final N parent, final int index )
    {
        getExModel ().insertNodesInto ( children, parent, index );
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
        getExModel ().insertNodeInto ( child, parent, index );
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
        getExModel ().removeNodeFromParent ( node );
    }

    /**
     * Removes nodes from tree structure.
     * This method will have effect only if nodes exist.
     *
     * @param nodes list of nodes to remove
     */
    public void removeNodes ( final List<N> nodes )
    {
        getExModel ().removeNodesFromParent ( nodes );
    }

    /**
     * Removes nodes from tree structure.
     * This method will have effect only if nodes exist.
     *
     * @param nodes array of nodes to remove
     */
    public void removeNodes ( final N[] nodes )
    {
        getExModel ().removeNodesFromParent ( nodes );
    }

    /**
     * Looks for the node with the specified ID in the tree model and returns it or null if it was not found.
     *
     * @param nodeId node ID
     * @return node with the specified ID or null if it was not found
     */
    public N findNode ( final String nodeId )
    {
        return getExModel ().findNode ( nodeId );
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
        getExModel ().updateNodeStructure ( node );
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
                if ( node != null )
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
        if ( node != null )
        {
            // Reloading node children
            performReload ( node, getPathForNode ( node ), select );
            return node;
        }
        return null;
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
            if ( node != null )
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
        if ( node != null )
        {
            getExModel ().reload ( node );
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
        expandPath ( pathNodeIds, true, true );
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
        expandPath ( pathNodeIds, expandLastNode, true );
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
        final List<String> ids = CollectionUtils.copy ( pathNodeIds );
        for ( int initial = 0; initial < ids.size (); initial++ )
        {
            final N initialNode = findNode ( ids.get ( initial ) );
            if ( initialNode != null )
            {
                for ( int i = 0; i <= initial; i++ )
                {
                    ids.remove ( i );
                }
                if ( ids.size () > 0 )
                {
                    expandPathImpl ( initialNode, ids, expandLastNode, selectLastNode );
                }
                return;
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
     */
    protected void expandPathImpl ( final N currentNode, final List<String> leftToExpand, final boolean expandLastNode,
                                    final boolean selectLastNode )
    {
        // There is still more to load
        if ( leftToExpand.size () > 0 )
        {
            // Expanding already loaded node
            expandNode ( currentNode );

            // Retrieving next node
            final N nextNode = findNode ( leftToExpand.get ( 0 ) );
            leftToExpand.remove ( 0 );

            // If node exists continue expanding path
            if ( nextNode != null )
            {
                expandPathImpl ( nextNode, leftToExpand, expandLastNode, selectLastNode );
            }
            else
            {
                expandPathEndImpl ( currentNode, expandLastNode, selectLastNode );
            }
        }
        else
        {
            expandPathEndImpl ( currentNode, expandLastNode, selectLastNode );
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
}