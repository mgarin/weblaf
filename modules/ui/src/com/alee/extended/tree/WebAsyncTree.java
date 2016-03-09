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

import com.alee.extended.tree.sample.SampleAsyncDataProvider;
import com.alee.extended.tree.sample.SampleTreeCellEditor;
import com.alee.laf.tree.WebTree;
import com.alee.laf.tree.WebTreeCellEditor;
import com.alee.managers.style.StyleId;
import com.alee.utils.CollectionUtils;
import com.alee.utils.compare.Filter;
import com.alee.utils.swing.CellEditorAdapter;

import javax.swing.event.ChangeEvent;
import javax.swing.tree.TreeCellEditor;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * This class provides a custom tree with asynchronous children loading.
 * All you need is to provide a custom {@link com.alee.extended.tree.AsyncTreeDataProvider} for the new tree instance.
 *
 * @param <E> tree nodes type
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-WebAsyncTree">How to use WebAsyncTree</a>
 * @see com.alee.extended.tree.AsyncTreeModel
 * @see com.alee.extended.tree.AsyncTreeDataProvider
 */

public class WebAsyncTree<E extends AsyncUniqueNode> extends WebTree<E> implements AsyncTreeModelListener<E>
{
    /**
     * todo 1. Allow adding async tree listener for specific node/nodeId listening
     */

    /**
     * General lock.
     */
    protected static final Object lock = new Object ();

    /**
     * Asynchronous tree listeners lock object.
     */
    protected final Object listenersLock = new Object ();

    /**
     * Asynchronous tree listeners.
     */
    protected List<AsyncTreeListener> asyncTreeListeners = new ArrayList<AsyncTreeListener> ( 1 );

    /**
     * Sync loading methods and option lock.
     */
    protected final Object syncLoadingLock = new Object ();

    /**
     * Whether to load children asynchronously or not.
     */
    protected boolean asyncLoading = true;

    /**
     * Tree nodes comparator.
     */
    protected Comparator<E> comparator;

    /**
     * Tree nodes filter.
     */
    protected Filter<E> filter;

    /**
     * Special cell editor listener.
     * It updates filtering and sorting after editing has finished.
     */
    protected CellEditorAdapter cellEditorAdapter;

    /**
     * Constructs sample asynchronous tree.
     */
    public WebAsyncTree ()
    {
        this ( StyleId.asynctree );
    }

    /**
     * Constructs sample asynchronous tree.
     *
     * @param id style ID
     */
    public WebAsyncTree ( final StyleId id )
    {
        super ( id );

        // Installing sample data provider
        setDataProvider ( new SampleAsyncDataProvider () );
        setAsyncLoading ( true );

        // Tree cell editor
        setCellEditor ( new SampleTreeCellEditor () );
    }

    /**
     * Costructs asynchronous tree using data from the custom data provider.
     *
     * @param dataProvider custom data provider
     */
    public WebAsyncTree ( final AsyncTreeDataProvider dataProvider )
    {
        this ( StyleId.asynctree, dataProvider );
    }

    /**
     * Costructs asynchronous tree using data from the custom data provider.
     *
     * @param id           style ID
     * @param dataProvider custom data provider
     */
    public WebAsyncTree ( final StyleId id, final AsyncTreeDataProvider dataProvider )
    {
        super ( id );

        // Installing data provider
        setDataProvider ( dataProvider );

        // Tree cell editor
        setCellEditor ( new WebTreeCellEditor () );
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
        synchronized ( syncLoadingLock != null ? syncLoadingLock : lock )
        {
            this.asyncLoading = asyncLoading;
            if ( isAsyncModel () )
            {
                getAsyncModel ().setAsyncLoading ( asyncLoading );
            }
        }
    }

    /**
     * Returns asynchronous tree data provider.
     *
     * @return data provider
     */
    public AsyncTreeDataProvider<E> getDataProvider ()
    {
        final TreeModel model = getModel ();
        return model != null && model instanceof AsyncTreeModel ? getAsyncModel ().getDataProvider () : null;
    }

    /**
     * Changes data provider for this asynchronous tree.
     *
     * @param dataProvider new data provider
     */
    public void setDataProvider ( final AsyncTreeDataProvider dataProvider )
    {
        if ( dataProvider != null )
        {
            final AsyncTreeDataProvider<E> oldDataProvider = getDataProvider ();
            setModel ( new AsyncTreeModel<E> ( this, dataProvider ) );
            firePropertyChange ( TREE_DATA_PROVIDER_PROPERTY, oldDataProvider, dataProvider );
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
        final Comparator<E> oldComparator = this.comparator;
        this.comparator = comparator;

        final AsyncTreeDataProvider dataProvider = getDataProvider ();
        if ( dataProvider instanceof AbstractAsyncTreeDataProvider )
        {
            ( ( AbstractAsyncTreeDataProvider ) dataProvider ).setChildrenComparator ( comparator );
            updateSortingAndFiltering ();
        }

        firePropertyChange ( TREE_COMPARATOR_PROPERTY, oldComparator, comparator );
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
        final Filter<E> oldFilter = this.filter;
        this.filter = filter;

        final AsyncTreeDataProvider dataProvider = getDataProvider ();
        if ( dataProvider instanceof AbstractAsyncTreeDataProvider )
        {
            ( ( AbstractAsyncTreeDataProvider ) dataProvider ).setChildrenFilter ( filter );
            updateSortingAndFiltering ();
        }

        firePropertyChange ( TREE_FILTER_PROPERTY, oldFilter, filter );
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
        getAsyncModel ().updateSortingAndFiltering ();
    }

    /**
     * Updates sorting and filtering for the specified node children.
     *
     * @param node node to update sorting and filter for
     */
    public void updateSortingAndFiltering ( final E node )
    {
        getAsyncModel ().updateSortingAndFiltering ( node );
    }

    /**
     * Sets the TreeModel that will provide the data.
     * This method also adds async tree model listener in the provided model.
     *
     * @param newModel the TreeModel that is to provide the data
     */
    @Override
    public void setModel ( final TreeModel newModel )
    {
        // Removing AsyncTreeModelListener from old model
        if ( getModel () instanceof AsyncTreeModel )
        {
            getAsyncModel ().removeAsyncTreeModelListener ( this );
        }

        // Adding AsyncTreeModelListener into new model
        if ( newModel instanceof AsyncTreeModel )
        {
            synchronized ( syncLoadingLock != null ? syncLoadingLock : lock )
            {
                final AsyncTreeModel model = ( AsyncTreeModel ) newModel;
                model.setAsyncLoading ( asyncLoading );
                model.addAsyncTreeModelListener ( this );
            }
        }

        super.setModel ( newModel );
    }

    /**
     * Sets the cell editor for this tree.
     * This method also adds cell editor listener in the provided model.
     *
     * @param cellEditor cell editor
     */
    @Override
    public void setCellEditor ( final TreeCellEditor cellEditor )
    {
        // Removing cell editor listener from old cell editor
        if ( this.cellEditor != null )
        {
            this.cellEditor.removeCellEditorListener ( cellEditorAdapter );
        }

        // Adding cell editor to the tree
        super.setCellEditor ( cellEditor );

        // Adding cell editor listener to new cell editor
        if ( cellEditor != null )
        {
            cellEditorAdapter = new CellEditorAdapter ()
            {
                @Override
                public void editingStopped ( final ChangeEvent e )
                {
                    // Updating tree sorting and filtering for parent of the edited node
                    final E node = ( E ) cellEditor.getCellEditorValue ();
                    updateSortingAndFiltering ( ( E ) node.getParent () );

                    //                    // Performing data update in a proper separate thread as it might take some time
                    //                    if ( dataUpdater != null )
                    //                    {
                    //                        AsyncTreeQueue.execute ( WebAsyncTree.this, new Runnable ()
                    //                        {
                    //                            @Override
                    //                            public void run ()
                    //                            {
                    //                                dataUpdater.nodeRenamed ( node, new Runnable ()
                    //                                {
                    //                                    @Override
                    //                                    public void run ()
                    //                                    {
                    //                                        //
                    //                                    }
                    //                                } );
                    //                            }
                    //                        } );
                    //                    }
                }
            };
            cellEditor.addCellEditorListener ( cellEditorAdapter );
        }
    }

    /**
     * Returns asynchronous tree model.
     *
     * @return asynchronous tree model
     */
    public AsyncTreeModel<E> getAsyncModel ()
    {
        return ( AsyncTreeModel<E> ) getModel ();
    }

    /**
     * Returns whether asynchronous tree model is installed or not.
     *
     * @return true if asynchronous tree model is installed, false otherwise
     */
    public boolean isAsyncModel ()
    {
        final TreeModel model = getModel ();
        return model != null && model instanceof AsyncTreeModel;
    }

    /**
     * Sets maximum threads amount for this asynchronous tree.
     * Separate threads are used for children loading, data updates and other actions which should be performed asynchronously.
     *
     * @param amount new maximum threads amount
     */
    public void setMaximumThreadsAmount ( final int amount )
    {
        AsyncTreeQueue.setMaximumThreadsAmount ( this, amount );
    }

    /**
     * Sets child nodes for the specified node.
     * This method might be used to manually change tree node children without causing any structure corruptions.
     * It will also cause node to change its state to loaded and it will not retrieve children from data provider unless reload is called.
     *
     * @param parent   node to process
     * @param children new node children
     */
    public void setChildNodes ( final E parent, final List<E> children )
    {
        getAsyncModel ().setChildNodes ( parent, children );
    }

    /**
     * Adds child node for the specified node.
     * This method might be used to manually change tree node children without causing any structure corruptions.
     * Be aware that added node will not be displayed if parent node is not yet loaded, this is a strict restriction for async tree.
     *
     * @param parent node to process
     * @param child  new node child
     */
    public void addChildNode ( final E parent, final E child )
    {
        getAsyncModel ().addChildNodes ( parent, Arrays.asList ( child ) );
    }

    /**
     * Adds child nodes for the specified node.
     * This method might be used to manually change tree node children without causing any structure corruptions.
     * Be aware that added nodes will not be displayed if parent node is not yet loaded, this is a strict restriction for async tree.
     *
     * @param parent   node to process
     * @param children new node children
     */
    public void addChildNodes ( final E parent, final List<E> children )
    {
        getAsyncModel ().addChildNodes ( parent, children );
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
    public void insertChildNodes ( final List<E> children, final E parent, final int index )
    {
        getAsyncModel ().insertNodesInto ( children, parent, index );
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
    public void insertChildNodes ( final E[] children, final E parent, final int index )
    {
        getAsyncModel ().insertNodesInto ( children, parent, index );
    }

    /**
     * Inserts child node into parent node.
     * This method might be used to manually change tree node children without causing any structure corruptions.
     *
     * @param child  new child node
     * @param parent parent node
     * @param index  insert index
     */
    public void insertChildNode ( final E child, final E parent, final int index )
    {
        getAsyncModel ().insertNodeInto ( child, parent, index );
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
            getAsyncModel ().removeNodeFromParent ( node );
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
        getAsyncModel ().removeNodesFromParent ( nodes );
    }

    /**
     * Removes nodes from tree structure.
     * This method will have effect only if nodes exist.
     *
     * @param nodes array of nodes to remove
     */
    public void removeNodes ( final E[] nodes )
    {
        getAsyncModel ().removeNodesFromParent ( nodes );
    }

    /**
     * Returns whether children for the specified node are already loaded or not.
     *
     * @param parent node to process
     * @return true if children for the specified node are already loaded, false otherwise
     */
    public boolean areChildrenLoaded ( final E parent )
    {
        return getAsyncModel ().areChildrenLoaded ( parent );
    }

    /**
     * Looks for the node with the specified ID in the tree model and returns it or null if it was not found.
     *
     * @param nodeId node ID
     * @return node with the specified ID or null if it was not found
     */
    public E findNode ( final String nodeId )
    {
        return getAsyncModel ().findNode ( nodeId );
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
        getAsyncModel ().updateNode ( node );

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
        getAsyncModel ().updateNodeStructure ( node );
    }

    /**
     * Reloads selected node children.
     * Unlike asynchronous methods this one works in EDT and forces to wait until the nodes load finishes.
     */
    public void reloadSelectedNodesSync ()
    {
        synchronized ( syncLoadingLock != null ? syncLoadingLock : lock )
        {
            final boolean async = isAsyncLoading ();
            setAsyncLoading ( false );
            reloadSelectedNodes ();
            setAsyncLoading ( async );
        }
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
                final E node = getNodeForPath ( path );
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
    public E reloadNodeUnderPoint ( final Point point )
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
    public E reloadNodeUnderPoint ( final int x, final int y )
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
    public E reloadNodeSync ( final String nodeId )
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
    public E reloadNodeSync ( final E node )
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
    public E reloadNodeSync ( final E node, final boolean select )
    {
        synchronized ( syncLoadingLock != null ? syncLoadingLock : lock )
        {
            final boolean async = isAsyncLoading ();
            setAsyncLoading ( false );
            reloadNode ( node, select );
            setAsyncLoading ( async );
            return node;
        }
    }

    /**
     * Reloads root node children.
     *
     * @return reloaded root node
     */
    public E reloadRootNode ()
    {
        return reloadNode ( getRootNode () );
    }

    /**
     * Reloads node with the specified ID.
     *
     * @param nodeId ID of the node to reload
     * @return reloaded node or null if none reloaded
     */
    public E reloadNode ( final String nodeId )
    {
        return reloadNode ( findNode ( nodeId ) );
    }

    /**
     * Reloads specified node children.
     *
     * @param node node to reload
     * @return reloaded node or null if none reloaded
     */
    public E reloadNode ( final E node )
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
    public E reloadNode ( final E node, final boolean select )
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
    public E reloadPathSync ( final TreePath path )
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
    public E reloadPathSync ( final TreePath path, final boolean select )
    {
        synchronized ( syncLoadingLock != null ? syncLoadingLock : lock )
        {
            final boolean async = isAsyncLoading ();
            setAsyncLoading ( false );
            final E node = reloadPath ( path, select );
            setAsyncLoading ( async );
            return node;
        }
    }

    /**
     * Reloads node children at the specified path.
     *
     * @param path path of the node to reload
     * @return reloaded node or null if none reloaded
     */
    public E reloadPath ( final TreePath path )
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
    public E reloadPath ( final TreePath path, final boolean select )
    {
        // Checking that path is not null
        if ( path != null )
        {
            // Checking if node is not null and not busy yet
            final E node = getNodeForPath ( path );
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
    protected void performReload ( final E node, final TreePath path, final boolean select )
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
            getAsyncModel ().reload ( node );
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
            final E initialNode = findNode ( ids.get ( initial ) );
            if ( initialNode != null )
            {
                for ( int i = 0; i <= initial; i++ )
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
    protected void expandPathImpl ( final E currentNode, final List<String> leftToExpand, final boolean expandLastNode,
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
                final E nextNode = findNode ( leftToExpand.get ( 0 ) );
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
                            final E nextNode = findNode ( leftToExpand.get ( 0 ) );
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
    protected void expandPathEndImpl ( final E lastFoundNode, final boolean expandLastNode, final boolean selectLastNode )
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
     * <p>
     * This method provides similar functionality to WebTree expandAll method and will actually expand all tree elements - even those which
     * are not yet loaded from data provider. Make sure you know what you are doing before calling this method.
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
        final E node = getNodeForPath ( path );
        for ( int i = 0; i < node.getChildCount (); i++ )
        {
            performFullPathExpand ( getPathForNode ( ( E ) node.getChildAt ( i ) ) );
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
        addAsyncTreeListener ( new AsyncTreeAdapter<E> ()
        {
            @Override
            public void loadCompleted ( final E parent, final List<E> children )
            {
                if ( parent == getNodeForPath ( path ) )
                {
                    for ( final E child : children )
                    {
                        performFullPathExpand ( child.getTreePath () );
                    }
                    removeAsyncTreeListener ( this );
                }
            }

            @Override
            public void loadFailed ( final E parent, final Throwable cause )
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
        synchronized ( listenersLock )
        {
            return CollectionUtils.copy ( asyncTreeListeners );
        }
    }

    /**
     * Adds new asynchronous tree listener.
     *
     * @param listener asynchronous tree listener to add
     */
    public void addAsyncTreeListener ( final AsyncTreeListener listener )
    {
        synchronized ( listenersLock )
        {
            asyncTreeListeners.add ( listener );
        }
    }

    /**
     * Removes asynchronous tree listener.
     *
     * @param listener asynchronous tree listener to remove
     */
    public void removeAsyncTreeListener ( final AsyncTreeListener listener )
    {
        synchronized ( listenersLock )
        {
            asyncTreeListeners.remove ( listener );
        }
    }

    /**
     * Invoked when children load operation starts.
     *
     * @param parent node which children are being loaded
     */
    @Override
    public void loadStarted ( final E parent )
    {
        fireChildrenLoadStarted ( parent );
    }

    /**
     * Fires children load start event.
     *
     * @param parent node which children are being loaded
     */
    protected void fireChildrenLoadStarted ( final E parent )
    {
        final List<AsyncTreeListener> listeners;
        synchronized ( listenersLock )
        {
            listeners = CollectionUtils.copy ( asyncTreeListeners );
        }
        for ( final AsyncTreeListener listener : listeners )
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
    public void loadCompleted ( final E parent, final List<E> children )
    {
        fireChildrenLoadCompleted ( parent, children );
    }

    /**
     * Fires children load complete event.
     *
     * @param parent   node which children were loaded
     * @param children loaded child nodes
     */
    protected void fireChildrenLoadCompleted ( final E parent, final List<E> children )
    {
        final List<AsyncTreeListener> listeners;
        synchronized ( listenersLock )
        {
            listeners = CollectionUtils.copy ( asyncTreeListeners );
        }
        for ( final AsyncTreeListener listener : listeners )
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
    public void loadFailed ( final E parent, final Throwable cause )
    {
        fireChildrenLoadFailed ( parent, cause );
    }

    /**
     * Fires children load complete event.
     *
     * @param parent node which children were loaded
     * @param cause  children load failure cause
     */
    protected void fireChildrenLoadFailed ( final E parent, final Throwable cause )
    {
        final List<AsyncTreeListener> listeners;
        synchronized ( listenersLock )
        {
            listeners = CollectionUtils.copy ( asyncTreeListeners );
        }
        for ( final AsyncTreeListener listener : listeners )
        {
            listener.loadFailed ( parent, cause );
        }
    }
}