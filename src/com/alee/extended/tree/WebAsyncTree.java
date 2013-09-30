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

import com.alee.extended.tree.sample.SampleDataProvider;
import com.alee.extended.tree.sample.SampleTreeCellEditor;
import com.alee.extended.tree.sample.SampleTreeCellRenderer;
import com.alee.laf.tree.WebTree;
import com.alee.laf.tree.WebTreeCellEditor;
import com.alee.utils.CollectionUtils;
import com.alee.utils.compare.Filter;
import com.alee.utils.swing.CellEditorAdapter;

import javax.swing.event.ChangeEvent;
import javax.swing.tree.TreeCellEditor;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * This class provides a custom tree with asynchronous childs loading.
 * All you need is to provide a custom AsyncTreeDataProvider for the new WebAsyncTree instance.
 *
 * @param <E> tree nodes type
 * @author Mikle Garin
 */

public class WebAsyncTree<E extends AsyncUniqueNode> extends WebTree<E> implements AsyncTreeModelListener<E>
{
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
     * Whether to load childs asynchronously or not.
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
     * Constructs sample asynchronous tree.
     */
    public WebAsyncTree ()
    {
        super ();

        // Installing sample data provider
        setDataProvider ( new SampleDataProvider () );
        setAsyncLoading ( true );

        // Tree cell renderer & editor
        setCellRenderer ( new SampleTreeCellRenderer () );
        setCellEditor ( new SampleTreeCellEditor () );
    }

    /**
     * Costructs asynchronous tree using data from the custom data provider.
     *
     * @param dataProvider custom data provider
     */
    public WebAsyncTree ( final AsyncTreeDataProvider dataProvider )
    {
        super ();

        // Installing data provider
        setDataProvider ( dataProvider );

        // Tree cell renderer & editor
        setCellRenderer ( new WebAsyncTreeCellRenderer () );
        setCellEditor ( new WebTreeCellEditor () );
    }

    /**
     * Costructs asynchronous tree using the specified tree model.
     *
     * @param newModel custom tree model
     */
    public WebAsyncTree ( final AsyncTreeModel newModel )
    {
        super ( newModel );

        // Installing tree model
        setModel ( newModel );

        // Tree cell renderer & editor
        setCellRenderer ( new WebAsyncTreeCellRenderer () );
        setCellEditor ( new WebTreeCellEditor () );
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
        return getAsyncModel ().getDataProvider ();
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
            setModel ( new AsyncTreeModel<E> ( this, dataProvider ) );
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

        final AsyncTreeDataProvider dataProvider = getDataProvider ();
        if ( dataProvider instanceof AbstractTreeDataProvider )
        {
            ( ( AbstractTreeDataProvider ) dataProvider ).setChildsComparator ( comparator );
            updateSortingAndFiltering ();
        }
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

        final AsyncTreeDataProvider dataProvider = getDataProvider ();
        if ( dataProvider instanceof AbstractTreeDataProvider )
        {
            ( ( AbstractTreeDataProvider ) dataProvider ).setChildsFilter ( filter );
            updateSortingAndFiltering ();
        }
    }

    /**
     * Updates nodes sorting and filtering for all loaded nodes.
     */
    public void updateSortingAndFiltering ()
    {
        getAsyncModel ().updateSortingAndFiltering ();
    }

    /**
     * Updates sorting and filtering for the specified node childs.
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
        super.setCellEditor ( cellEditor );

        cellEditor.addCellEditorListener ( new CellEditorAdapter ()
        {
            @Override
            public void editingStopped ( final ChangeEvent e )
            {
                final E node = ( E ) cellEditor.getCellEditorValue ();
                updateSortingAndFiltering ( ( E ) node.getParent () );
            }
        } );
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
     *
     * @param amount new maximum threads amount
     */
    public void setMaximumThreadsAmount ( final int amount )
    {
        AsyncTreeQueue.setMaximumThreadsAmount ( this, amount );
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
        getAsyncModel ().setChildNodes ( parent, children );
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
        getAsyncModel ().addChildNodes ( parent, children );
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
        final boolean exists = node != null;
        if ( exists )
        {
            getAsyncModel ().removeNodeFromParent ( node );
        }
        return exists;
    }

    /**
     * Returns whether childs for the specified node are already loaded or not.
     *
     * @param parent node to process
     * @return true if childs for the specified node are already loaded, false otherwise
     */
    public boolean areChildsLoaded ( final E parent )
    {
        return getAsyncModel ().areChildsLoaded ( parent );
    }

    /**
     * Reloads selected node childs.
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
     * Reloads selected node childs.
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
                if ( node != null && !node.isBusy () )
                {
                    // Reloading node childs
                    performReload ( node, path, false );
                }
            }
        }
    }

    /**
     * Reloads specified node childs.
     * Unlike asynchronous methods this one works in EDT and forces to wait until the nodes load finishes.
     *
     * @param node node to reload
     */
    public void reloadNodeSync ( final E node )
    {
        reloadNodeSync ( node, false );
    }

    /**
     * Reloads specified node childs and selects it if requested.
     * Unlike asynchronous methods this one works in EDT and forces to wait until the nodes load finishes.
     *
     * @param node   node to reload
     * @param select whether select the node or not
     */
    public void reloadNodeSync ( final E node, final boolean select )
    {
        synchronized ( syncLoadingLock != null ? syncLoadingLock : lock )
        {
            final boolean async = isAsyncLoading ();
            setAsyncLoading ( false );
            reloadNode ( node, select );
            setAsyncLoading ( async );
        }
    }

    /**
     * Reloads specified node childs.
     *
     * @param node node to reload
     */
    public void reloadNode ( final E node )
    {
        reloadNode ( node, false );
    }

    /**
     * Reloads root node childs.
     */
    public void reloadRootNode ()
    {
        reloadNode ( getRootNode () );
    }

    /**
     * Reloads specified node childs and selects it if requested.
     *
     * @param node   node to reload
     * @param select whether select the node or not
     */
    public void reloadNode ( final E node, final boolean select )
    {
        // Checking that node is not null
        if ( node != null && !node.isBusy () )
        {
            // Reloading node childs
            performReload ( node, getPathForNode ( node ), select );
        }
    }

    /**
     * Reloads node childs at the specified path.
     * Unlike asynchronous methods this one works in EDT and forces to wait until the nodes load finishes.
     *
     * @param path path of the node to reload
     */
    public void reloadPathSync ( final TreePath path )
    {
        reloadPathSync ( path, false );
    }

    /**
     * Reloads node childs at the specified path and selects it if needed.
     * Unlike asynchronous methods this one works in EDT and forces to wait until the nodes load finishes.
     *
     * @param path   path of the node to reload
     * @param select whether select the node or not
     */
    public void reloadPathSync ( final TreePath path, final boolean select )
    {
        synchronized ( syncLoadingLock != null ? syncLoadingLock : lock )
        {
            final boolean async = isAsyncLoading ();
            setAsyncLoading ( false );
            reloadPath ( path, select );
            setAsyncLoading ( async );
        }
    }

    /**
     * Reloads node childs at the specified path.
     *
     * @param path path of the node to reload
     */
    public void reloadPath ( final TreePath path )
    {
        reloadPath ( path, false );
    }

    /**
     * Reloads node childs at the specified path and selects it if needed.
     *
     * @param path   path of the node to reload
     * @param select whether select the node or not
     */
    public void reloadPath ( final TreePath path, final boolean select )
    {
        // Checking that path is not null
        if ( path != null )
        {
            // Checking if node is not null and not busy yet
            final E node = getNodeForPath ( path );
            if ( node != null && !node.isBusy () )
            {
                // Reloading node childs
                performReload ( node, path, select );
            }
        }
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
        // In case the node childs were not loaded yet this call will cause it to load childs
        if ( !isExpanded ( path ) )
        {
            expandPath ( path );
        }

        // Reload selected node childs
        // This won't be called if node was not loaded yet since expand would call load before
        if ( node != null && !node.isBusy () )
        {
            getAsyncModel ().reload ( node );
        }
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
            asyncTreeListeners.add ( listener );
        }
    }

    /**
     * Invoked when childs load operation starts.
     *
     * @param parent node which childs are being loaded
     */
    @Override
    public void childsLoadStarted ( final E parent )
    {
        fireChildsLoadStarted ( parent );
    }

    /**
     * Fires childs load start event.
     *
     * @param parent node which childs are being loaded
     */
    protected void fireChildsLoadStarted ( final E parent )
    {
        final List<AsyncTreeListener> listeners;
        synchronized ( listenersLock )
        {
            listeners = CollectionUtils.copy ( asyncTreeListeners );
        }
        for ( final AsyncTreeListener listener : listeners )
        {
            listener.childsLoadStarted ( parent );
        }
    }

    /**
     * Invoked when childs load operation finishes.
     *
     * @param parent node which childs were loaded
     * @param childs loaded child nodes
     */
    @Override
    public void childsLoadCompleted ( final E parent, final List<E> childs )
    {
        fireChildsLoadCompleted ( parent, childs );
    }

    /**
     * Fires childs load complete event.
     *
     * @param parent node which childs were loaded
     * @param childs loaded child nodes
     */
    protected void fireChildsLoadCompleted ( final E parent, final List<E> childs )
    {
        final List<AsyncTreeListener> listeners;
        synchronized ( listenersLock )
        {
            listeners = CollectionUtils.copy ( asyncTreeListeners );
        }
        for ( final AsyncTreeListener listener : listeners )
        {
            listener.childsLoadCompleted ( parent, childs );
        }
    }

    /**
     * Expands all visible tree rows in a single call.
     * It is not recommended to expand large trees this way since that might cause huge interface lags.
     * <p/>
     * This method provides similar functionality to WebTree expandAll method and will actually expand all tree elements.
     * Even those which are not yet loaded from some custom data provider.
     * So make sure you know what you are doing before calling this method.
     */
    @Override
    public final void expandAll ()
    {
        for ( int i = getRowCount () - 1; i >= 0; i-- )
        {
            final TreePath path = getPathForRow ( i );
            if ( !getModel ().isLeaf ( getNodeForPath ( path ) ) )
            {
                performPathExpand ( path );
            }
        }
    }

    /**
     * Expands path right away (if node childs were loaded atleast once before or not) or asynchronously.
     *
     * @param path path to expand
     */
    protected void performPathExpand ( final TreePath path )
    {
        if ( hasBeenExpanded ( path ) )
        {
            performSyncPathExpand ( path );
        }
        else
        {
            performAsyncPathExpand ( path );
        }
    }

    /**
     * Expands path right away.
     *
     * @param path path to expand
     */
    protected void performSyncPathExpand ( final TreePath path )
    {
        // Expand path
        expandPath ( path );

        // Expand sub-paths
        final E node = getNodeForPath ( path );
        for ( int i = 0; i < node.getChildCount (); i++ )
        {
            performPathExpand ( getPathForNode ( ( E ) node.getChildAt ( i ) ) );
        }
    }

    /**
     * Expands path asynchronously.
     *
     * @param path path to expand
     */
    protected void performAsyncPathExpand ( final TreePath path )
    {
        // Add path expand listener first to get notified when this path will be expanded
        addAsyncTreeListener ( new AsyncTreeAdapter<E> ()
        {
            @Override
            public void childsLoadCompleted ( final E parent, final List<E> childs )
            {
                if ( parent == getNodeForPath ( path ) )
                {
                    for ( final E child : childs )
                    {
                        performPathExpand ( child.getTreePath () );
                    }
                }
            }
        } );

        // Force path to expand
        expandPath ( path );
    }
}