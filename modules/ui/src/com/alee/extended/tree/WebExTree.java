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
import com.alee.extended.tree.sample.SampleTreeCellRenderer;
import com.alee.laf.tree.UniqueNode;
import com.alee.laf.tree.WebTree;
import com.alee.laf.tree.WebTreeCellEditor;
import com.alee.laf.tree.WebTreeCellRenderer;
import com.alee.utils.compare.Filter;

import javax.swing.tree.TreeModel;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * WebTree extension that provides simple
 *
 * @author Mikle Garin
 */

public class WebExTree<E extends UniqueNode> extends WebTree<E>
{
    /**
     * Tree nodes comparator.
     */
    protected Comparator<E> comparator;

    /**
     * Tree nodes filter.
     */
    protected Filter<E> filter;

    /**
     * Constructs sample ex tree.
     */
    public WebExTree ()
    {
        super ();

        // Installing sample data provider
        setDataProvider ( new SampleExDataProvider () );

        // Tree cell renderer & editor
        setCellRenderer ( new SampleTreeCellRenderer () );
        setCellEditor ( new SampleTreeCellEditor () );
    }

    /**
     * Costructs ex tree using data from the custom data provider.
     *
     * @param dataProvider custom data provider
     */
    public WebExTree ( final ExTreeDataProvider dataProvider )
    {
        super ();

        // Installing data provider
        setDataProvider ( dataProvider );

        // Tree cell renderer & editor
        setCellRenderer ( new WebTreeCellRenderer () );
        setCellEditor ( new WebTreeCellEditor () );
    }

    /**
     * Returns ex tree data provider.
     *
     * @return data provider
     */
    public ExTreeDataProvider<E> getDataProvider ()
    {
        return getExModel ().getDataProvider ();
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
            setModel ( new ExTreeModel<E> ( this, dataProvider ) );
        }
    }

    /**
     * Returns tree nodes comparator.
     *
     * @return tree nodes comparator
     */
    public Comparator<E> getComparator ()
    {
        return comparator;
    }

    /**
     * Sets tree nodes comparator.
     * Comparator replacement will automatically update all loaded nodes sorting.
     *
     * @param comparator tree nodes comparator
     */
    public void setComparator ( final Comparator<E> comparator )
    {
        this.comparator = comparator;

        final ExTreeDataProvider dataProvider = getDataProvider ();
        if ( dataProvider instanceof AbstractExTreeDataProvider )
        {
            ( ( AbstractExTreeDataProvider ) dataProvider ).setChildsComparator ( comparator );
            updateSortingAndFiltering ();
        }
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
    public Filter<E> getFilter ()
    {
        return filter;
    }

    /**
     * Sets tree nodes filter.
     * Comparator replacement will automatically re-filter all loaded nodes.
     *
     * @param filter tree nodes filter
     */
    public void setFilter ( final Filter<E> filter )
    {
        this.filter = filter;

        final ExTreeDataProvider dataProvider = getDataProvider ();
        if ( dataProvider instanceof AbstractExTreeDataProvider )
        {
            ( ( AbstractExTreeDataProvider ) dataProvider ).setChildsFilter ( filter );
            updateSortingAndFiltering ();
        }
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
    public void updateSortingAndFiltering ()
    {
        getExModel ().updateSortingAndFiltering ();
    }

    /**
     * Updates sorting and filtering for the specified node childs.
     */
    public void updateSortingAndFiltering ( final E node )
    {
        getExModel ().updateSortingAndFiltering ( node );
    }

    /**
     * Returns ex tree model.
     *
     * @return ex tree model
     */
    public ExTreeModel<E> getExModel ()
    {
        return ( ExTreeModel<E> ) getModel ();
    }

    /**
     * Returns whether ex tree model is installed or not.
     *
     * @return true if ex tree model is installed, false otherwise
     */
    public boolean isExModel ()
    {
        final TreeModel model = getModel ();
        return model != null && model instanceof ExTreeModel;
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
        getExModel ().setChildNodes ( parent, children );
    }

    /**
     * Adds child node for the specified node.
     * This method might be used to manually change tree node childs without causing any structure corruptions.
     *
     * @param parent node to process
     * @param child  new node child
     */
    public void addChildNode ( final E parent, final E child )
    {
        getExModel ().addChildNodes ( parent, Arrays.asList ( child ) );
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
        getExModel ().addChildNodes ( parent, children );
    }

    /**
     * Inserts a list of child nodes into parent node.
     * This method might be used to manually change tree node childs without causing any structure corruptions.
     *
     * @param children list of new child nodes
     * @param parent   parent node
     * @param index    insert index
     */
    public void insertChildNodes ( final List<E> children, final E parent, final int index )
    {
        getExModel ().insertNodesInto ( children, parent, index );
    }

    /**
     * Inserts an array of child nodes into parent node.
     * This method might be used to manually change tree node childs without causing any structure corruptions.
     *
     * @param children array of new child nodes
     * @param parent   parent node
     * @param index    insert index
     */
    public void insertChildNodes ( final E[] children, final E parent, final int index )
    {
        getExModel ().insertNodesInto ( children, parent, index );
    }

    /**
     * Removes node with the specified ID from tree structure.
     * This method will have effect only if node exists.
     *
     * @param nodeId ID of the node to remove
     * @return true if tree structure was changed by the operation, false otherwise
     */
    public boolean removeNode ( final String nodeId )
    {
        return removeNode ( findNode ( nodeId ) );
    }

    /**
     * Removes node from tree structure.
     * This method will have effect only if node exists.
     *
     * @param node node to remove
     * @return true if tree structure was changed by the operation, false otherwise
     */
    public boolean removeNode ( final E node )
    {
        final boolean exists = node != null && node.getParent () != null;
        if ( exists )
        {
            getExModel ().removeNodeFromParent ( node );
        }
        return exists;
    }

    /**
     * Removes nodes from tree structure.
     * This method will have effect only if nodes exist.
     *
     * @param nodes list of nodes to remove
     */
    public void removeNodes ( final List<E> nodes )
    {
        getExModel ().removeNodesFromParent ( nodes );
    }

    /**
     * Removes nodes from tree structure.
     * This method will have effect only if nodes exist.
     *
     * @param nodes array of nodes to remove
     */
    public void removeNodes ( final E[] nodes )
    {
        getExModel ().removeNodesFromParent ( nodes );
    }

    /**
     * Looks for the node with the specified ID in the tree model and returns it or null if it was not found.
     *
     * @param nodeId node ID
     * @return node with the specified ID or null if it was not found
     */
    public E findNode ( final String nodeId )
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
     * Forces tree node to be updated.
     *
     * @param node tree node to be updated
     */
    public void updateNode ( final E node )
    {
        getExModel ().updateNode ( node );

        // todo Should actually perform this here (but need to improve filter interface methods - add cache clear methods)
        // updateSortingAndFiltering ( ( E ) node.getParent () );
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
    public void updateNodeStructure ( final E node )
    {
        getExModel ().updateNodeStructure ( node );
    }
}