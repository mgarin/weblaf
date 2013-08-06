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

import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import java.util.ArrayList;
import java.util.List;

/**
 * This class provides a custom tree with asynchronous childs loading.
 * All you need is to provide a custom AsyncTreeDataProvider for the new WebAsyncTree instance.
 *
 * @param <E> custom node type
 * @author Mikle Garin
 * @since 1.4
 */

public class WebAsyncTree<E extends AsyncUniqueNode> extends WebTree<E> implements AsyncTreeModelListener<E>
{
    /**
     * Asynchronous tree listeners.
     */
    private List<AsyncTreeListener> asyncTreeListeners = new ArrayList<AsyncTreeListener> ();

    /**
     * Asynchronous tree listeners lock object.
     */
    private final Object listenersLock = new Object ();

    /**
     * Whether to load childs asynchronously or not.
     */
    private boolean asyncLoading = true;

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
    public WebAsyncTree ( AsyncTreeDataProvider dataProvider )
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
    public WebAsyncTree ( AsyncTreeModel newModel )
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
    public void setAsyncLoading ( boolean asyncLoading )
    {
        this.asyncLoading = asyncLoading;
        if ( isAsyncModel () )
        {
            getAsyncModel ().setAsyncLoading ( asyncLoading );
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
    public void setDataProvider ( AsyncTreeDataProvider dataProvider )
    {
        if ( dataProvider != null )
        {
            setModel ( new AsyncTreeModel<E> ( this, dataProvider ) );
        }
    }

    /**
     * Sets the TreeModel that will provide the data.
     * This method also updates async tree model listener inside the model.
     *
     * @param newModel the TreeModel that is to provide the data
     */
    public void setModel ( TreeModel newModel )
    {
        // Disable asynchronous loading for the model installation time
        // This made to load initial data without delay and in EDT
        setAsyncLoading ( false );

        // Removing AsyncTreeModelListener from old model
        if ( getModel () instanceof AsyncTreeModel )
        {
            getAsyncModel ().removeAsyncTreeModelListener ( this );
        }

        // Adding AsyncTreeModelListener into new model
        if ( newModel instanceof AsyncTreeModel )
        {
            final AsyncTreeModel model = ( AsyncTreeModel ) newModel;
            model.setAsyncLoading ( asyncLoading );
            model.addAsyncTreeModelListener ( this );
        }

        super.setModel ( newModel );

        // Enabling asynchronous loading
        setAsyncLoading ( true );
    }

    /**
     * Returns asynchronous tree model.
     *
     * @return asynchronous tree model
     */
    public AsyncTreeModel getAsyncModel ()
    {
        return ( AsyncTreeModel ) getModel ();
    }

    /**
     * Returns whether asynchronous tree model is installed or not.
     *
     * @return true if asynchronous tree model is installed, false otherwise
     */
    public boolean isAsyncModel ()
    {
        TreeModel model = getModel ();
        return model != null && model instanceof AsyncTreeModel;
    }

    /**
     * Returns asynchronous tree root node.
     *
     * @return asynchronous tree root node
     */
    public E getRootNode ()
    {
        return ( E ) getAsyncModel ().getRoot ();
    }

    /**
     * Reloads selected node childs.
     * Unlike asynchronous methods this one works in EDT and forces to wait until the nodes load finishes.
     */
    public void reloadSelectedNodesSync ()
    {
        setAsyncLoading ( false );
        reloadSelectedNodes ();
        setAsyncLoading ( true );
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
            for ( TreePath path : paths )
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
    public void reloadNodeSync ( E node )
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
    public void reloadNodeSync ( E node, boolean select )
    {
        setAsyncLoading ( false );
        reloadNode ( node, select );
        setAsyncLoading ( true );
    }

    /**
     * Reloads specified node childs.
     *
     * @param node node to reload
     */
    public void reloadNode ( E node )
    {
        reloadNode ( node, false );
    }

    /**
     * Reloads specified node childs and selects it if requested.
     *
     * @param node   node to reload
     * @param select whether select the node or not
     */
    public void reloadNode ( E node, boolean select )
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
    public void reloadPathSync ( TreePath path )
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
    public void reloadPathSync ( TreePath path, boolean select )
    {
        setAsyncLoading ( false );
        reloadPath ( path, select );
        setAsyncLoading ( true );
    }

    /**
     * Reloads node childs at the specified path.
     *
     * @param path path of the node to reload
     */
    public void reloadPath ( TreePath path )
    {
        reloadPath ( path, false );
    }

    /**
     * Reloads node childs at the specified path and selects it if needed.
     *
     * @param path   path of the node to reload
     * @param select whether select the node or not
     */
    public void reloadPath ( TreePath path, boolean select )
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
    private void performReload ( E node, TreePath path, boolean select )
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
    public void addAsyncTreeListener ( AsyncTreeListener listener )
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
    public void removeAsyncTreeListener ( AsyncTreeListener listener )
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
    public void childsLoadStarted ( E parent )
    {
        fireChildsLoadStarted ( parent );
    }

    /**
     * Fires childs load start event.
     *
     * @param parent node which childs are being loaded
     */
    private void fireChildsLoadStarted ( E parent )
    {
        final List<AsyncTreeListener> listeners;
        synchronized ( listenersLock )
        {
            listeners = CollectionUtils.copy ( asyncTreeListeners );
        }
        for ( AsyncTreeListener listener : listeners )
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
    public void childsLoadCompleted ( E parent, List<E> childs )
    {
        fireChildsLoadCompleted ( parent, childs );
    }

    /**
     * Fires childs load complete event.
     *
     * @param parent node which childs were loaded
     * @param childs loaded child nodes
     */
    private void fireChildsLoadCompleted ( E parent, List<E> childs )
    {
        final List<AsyncTreeListener> listeners;
        synchronized ( listenersLock )
        {
            listeners = CollectionUtils.copy ( asyncTreeListeners );
        }
        for ( AsyncTreeListener listener : listeners )
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
    private void performPathExpand ( TreePath path )
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
    private void performSyncPathExpand ( TreePath path )
    {
        // Expand path
        expandPath ( path );

        // Expand sub-paths
        E node = getNodeForPath ( path );
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
    private void performAsyncPathExpand ( final TreePath path )
    {
        // Add path expand listener first to get notified when this path will be expanded
        addAsyncTreeListener ( new AsyncTreeAdapter<E> ()
        {
            public void childsLoadCompleted ( E parent, List<E> childs )
            {
                if ( parent == getNodeForPath ( path ) )
                {
                    for ( E child : childs )
                    {
                        performPathExpand ( new TreePath ( child.getPath () ) );
                    }
                }
            }
        } );

        // Force path to expand
        expandPath ( path );
    }
}