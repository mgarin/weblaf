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

package com.alee.laf.tree;

import com.alee.api.Predicate;
import com.alee.laf.WebLookAndFeel;
import com.alee.managers.hotkey.HotkeyData;
import com.alee.managers.log.Log;
import com.alee.managers.settings.DefaultValue;
import com.alee.managers.settings.SettingsManager;
import com.alee.managers.settings.SettingsMethods;
import com.alee.managers.settings.SettingsProcessor;
import com.alee.managers.style.ShapeProvider;
import com.alee.managers.style.StyleId;
import com.alee.managers.style.Styleable;
import com.alee.managers.tooltip.ToolTipProvider;
import com.alee.utils.EventUtils;
import com.alee.utils.GeometryUtils;
import com.alee.utils.ReflectUtils;
import com.alee.utils.SwingUtils;
import com.alee.utils.compare.Filter;
import com.alee.utils.swing.*;

import javax.swing.*;
import javax.swing.event.CellEditorListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.KeyAdapter;
import java.awt.event.MouseAdapter;
import java.util.*;
import java.util.List;

/**
 * This JTree extension class provides a direct access to WebTreeUI methods.
 * There is also a set of additional methods to simplify some operations with tree.
 * <p/>
 * This component should never be used with a non-Web UIs as it might cause an unexpected behavior.
 * You could still use that component even if WebLaF is not your application L&amp;F as this component will use Web-UI in any case.
 *
 * @param <E> tree nodes type
 * @author Mikle Garin
 */

public class WebTree<E extends DefaultMutableTreeNode> extends JTree
        implements Styleable, ShapeProvider, TreeEventMethods<E>, EventMethods, SettingsMethods, FontMethods<WebTree<E>>
{
    /**
     * Bound property name for tree data provider.
     * Data provider is not supported by WebTree, but it is a base for various extensions so property is located here.
     */
    public final static String TREE_DATA_PROVIDER_PROPERTY = "dataProvider";

    /**
     * Bound property name for tree filter.
     * Filtering is not supported by WebTree, but it is a base for various extensions so property is located here.
     */
    public final static String TREE_FILTER_PROPERTY = "filter";

    /**
     * Bound property name for tree comparator.
     * Sorting is not supported by WebTree, but it is a base for various extensions so property is located here.
     */
    public final static String TREE_COMPARATOR_PROPERTY = "comparator";

    /**
     * Single selection mode.
     * Only one node can be selected.
     */
    public static final int SINGLE_TREE_SELECTION = TreeSelectionModel.SINGLE_TREE_SELECTION;

    /**
     * Contiguous selection mode.
     * Any amount of nodes can be selected in a row.
     */
    public static final int CONTIGUOUS_TREE_SELECTION = TreeSelectionModel.CONTIGUOUS_TREE_SELECTION;

    /**
     * Discontiguous selection mode.
     * Any amount of nodes can be selected anywhere.
     */
    public static final int DISCONTIGUOUS_TREE_SELECTION = TreeSelectionModel.DISCONTIGUOUS_TREE_SELECTION;

    /**
     * Tree cell editor listeners.
     * These listeners act separately from the cell editor and will be moved to new tree cell editor automatically on set.
     */
    protected List<CellEditorListener> cellEditorListeners = new ArrayList<CellEditorListener> ( 1 );

    /**
     * Mouseover listeners.
     * These listeners inform about mouse enter and exit events for tree nodes.
     */
    protected List<MouseoverListener> mouseoverListeners = new ArrayList<MouseoverListener> ( 1 );

    /**
     * Listener that forces tree to scroll view to selection.
     * It is disabled by default and null in that case.
     */
    protected TreeSelectionListener scrollToSelectionListener = null;

    /**
     * Special state provider that can be set to check whether or not specific nodes are editable.
     */
    protected StateProvider<E> editableStateProvider = null;

    /**
     * Custom WebLaF tooltip provider.
     */
    protected ToolTipProvider<? extends WebTree> toolTipProvider = null;

    /**
     * Constructs tree with default sample model.
     */
    public WebTree ()
    {
        this ( getDefaultTreeModel () );
    }

    /**
     * Constructs tree with model based on specified values.
     *
     * @param value tree data
     */
    public WebTree ( final Object[] value )
    {
        this ( createTreeModel ( value ) );
        setRootVisible ( false );
        setShowsRootHandles ( true );
        expandRoot ();
    }

    /**
     * Constructs tree with model based on specified values.
     *
     * @param value tree data
     */
    public WebTree ( final Vector<?> value )
    {
        this ( createTreeModel ( value ) );
        setRootVisible ( false );
        setShowsRootHandles ( true );
        expandRoot ();
    }

    /**
     * Constructs tree with model based on specified values.
     *
     * @param value tree data
     */
    public WebTree ( final Hashtable<?, ?> value )
    {
        this ( createTreeModel ( value ) );
        setRootVisible ( false );
        setShowsRootHandles ( true );
        expandRoot ();
    }

    /**
     * Constructs tree with model based on specified root node.
     *
     * @param root tree root node
     */
    public WebTree ( final E root )
    {
        this ( new WebTreeModel<E> ( root ) );
    }

    /**
     * Constructs tree with model based on specified root node and which decides whether a node is a leaf node in the specified manner.
     *
     * @param root               tree root node
     * @param asksAllowsChildren false if any node can have children, true if each node is asked to see if it can have children
     */
    public WebTree ( final E root, final boolean asksAllowsChildren )
    {
        this ( new WebTreeModel<E> ( root, asksAllowsChildren ) );
    }

    /**
     * Constructs tree with specified model.
     *
     * @param newModel tree model
     */
    public WebTree ( final TreeModel newModel )
    {
        super ( newModel );
        init ();
    }

    /**
     * Constructs tree with default sample model.
     *
     * @param id style ID
     */
    public WebTree ( final StyleId id )
    {
        this ( id, getDefaultTreeModel () );
    }

    /**
     * Constructs tree with model based on specified values.
     *
     * @param id    style ID
     * @param value tree data
     */
    public WebTree ( final StyleId id, final Object[] value )
    {
        this ( id, createTreeModel ( value ) );
        setRootVisible ( false );
        setShowsRootHandles ( true );
        expandRoot ();
    }

    /**
     * Constructs tree with model based on specified values.
     *
     * @param id    style ID
     * @param value tree data
     */
    public WebTree ( final StyleId id, final Vector<?> value )
    {
        this ( id, createTreeModel ( value ) );
        setRootVisible ( false );
        setShowsRootHandles ( true );
        expandRoot ();
    }

    /**
     * Constructs tree with model based on specified values.
     *
     * @param id    style ID
     * @param value tree data
     */
    public WebTree ( final StyleId id, final Hashtable<?, ?> value )
    {
        this ( id, createTreeModel ( value ) );
        setRootVisible ( false );
        setShowsRootHandles ( true );
        expandRoot ();
    }

    /**
     * Constructs tree with model based on specified root node.
     *
     * @param id   style ID
     * @param root tree root node
     */
    public WebTree ( final StyleId id, final E root )
    {
        this ( id, new WebTreeModel<E> ( root ) );
    }

    /**
     * Constructs tree with model based on specified root node and which decides whether a node is a leaf node in the specified manner.
     *
     * @param id                 style ID
     * @param root               tree root node
     * @param asksAllowsChildren false if any node can have children, true if each node is asked to see if it can have children
     */
    public WebTree ( final StyleId id, final E root, final boolean asksAllowsChildren )
    {
        this ( id, new WebTreeModel<E> ( root, asksAllowsChildren ) );
    }

    /**
     * Constructs tree with specified model.
     *
     * @param id       style ID
     * @param newModel tree model
     */
    public WebTree ( final StyleId id, final TreeModel newModel )
    {
        super ( newModel );
        setStyleId ( id );
        init ();
    }

    /**
     * Initializes additional tree settings.
     */
    protected void init ()
    {
        // You can add your own initialize implementation here
    }

    @Override
    public void setCellEditor ( final TreeCellEditor cellEditor )
    {
        // Removing cell editor listeners from old cell editor
        for ( final CellEditorListener listener : cellEditorListeners )
        {
            this.cellEditor.removeCellEditorListener ( listener );
        }

        super.setCellEditor ( cellEditor );

        // Adding cell editor listeners to new cell editor
        for ( final CellEditorListener listener : cellEditorListeners )
        {
            this.cellEditor.addCellEditorListener ( listener );
        }
    }

    /**
     * Adds tree cell editor listener.
     * These listeners act separately from the cell editor and will be moved to new tree cell editor automatically on set.
     *
     * @param listener cell editor listener to add
     */
    public void addCellEditorListener ( final CellEditorListener listener )
    {
        cellEditorListeners.add ( listener );
        if ( cellEditor != null )
        {
            cellEditor.addCellEditorListener ( listener );
        }
    }

    /**
     * Removes tree cell editor listener.
     *
     * @param listener cell editor listener to remove
     */
    public void removeCellEditorListener ( final CellEditorListener listener )
    {
        cellEditorListeners.remove ( listener );
        if ( cellEditor != null )
        {
            cellEditor.removeCellEditorListener ( listener );
        }
    }

    /**
     * Returns special state provider that can be set to check whether or not specific nodes are editable.
     * By default this provider is not specified and simply ignored by the tree.
     *
     * @return special state provider that can be set to check whether or not specific nodes are editable
     */
    public StateProvider<E> getEditableStateProvider ()
    {
        return editableStateProvider;
    }

    /**
     * Sets special state provider that can be set to check whether or not specific nodes are editable.
     * You can provide null to disable this state check at all.
     *
     * @param stateProvider special state provider that can be set to check whether or not specific nodes are editable
     */
    public void setEditableStateProvider ( final StateProvider<E> stateProvider )
    {
        this.editableStateProvider = stateProvider;
    }

    @Override
    public boolean isPathEditable ( final TreePath path )
    {
        return super.isPathEditable ( path ) && isNodeEditable ( ( E ) path.getLastPathComponent () );
    }

    /**
     * Returns whether the specified tree node is editable or not.
     * This method is an improved version of default JTree method isPathEditable.
     *
     * @param node node to check editable state for
     * @return true if the specified tree node is editable, false otherwise
     * @see #isPathEditable(javax.swing.tree.TreePath)
     */
    public boolean isNodeEditable ( final E node )
    {
        return editableStateProvider == null || editableStateProvider.provide ( node );
    }

    /**
     * Returns custom WebLaF tooltip provider.
     *
     * @return custom WebLaF tooltip provider
     */
    public ToolTipProvider<? extends WebTree> getToolTipProvider ()
    {
        return toolTipProvider;
    }

    /**
     * Sets custom WebLaF tooltip provider.
     *
     * @param provider custom WebLaF tooltip provider
     */
    public void setToolTipProvider ( final ToolTipProvider<? extends WebTree> provider )
    {
        this.toolTipProvider = provider;
    }

    /**
     * Expands the root path, assuming the current TreeModel has been set.
     */
    public void expandRoot ()
    {
        expandNode ( getRootNode () );
    }

    /**
     * Expands all tree nodes in a single call.
     * It is not recommended to expand large trees this way since that might cause huge interface lags.
     */
    public void expandAll ()
    {
        int i = 0;
        while ( i < getRowCount () )
        {
            expandRow ( i );
            i++;
        }
    }

    /**
     * Expands all tree nodes accepted by filter in a single call.
     * It is not recommended to expand large trees this way since that might cause huge interface lags.
     *
     * @param shouldExpand expand filter
     */
    public void expandAll ( final Filter<E> shouldExpand )
    {
        if ( shouldExpand == null )
        {
            expandAll ();
        }
        else
        {
            final E rootNode = getRootNode ();
            expandAll ( rootNode, shouldExpand );
        }
    }

    /**
     * Expands all child nodes of the specified node.
     *
     * @param node node to expand
     */
    public void expandAll ( final E node )
    {
        expandAll ( node, null );
    }

    /**
     * Expands all child nodes accepted by filter in a single call.
     *
     * @param node         node to expand
     * @param shouldExpand expand filter
     */
    public void expandAll ( final E node, final Filter<E> shouldExpand )
    {
        if ( shouldExpand == null || shouldExpand.accept ( node ) )
        {
            expandNode ( node );
            for ( int i = 0; i < node.getChildCount (); i++ )
            {
                expandAll ( ( E ) node.getChildAt ( i ), shouldExpand );
            }
        }
    }

    /**
     * Expands all child nodes until the specified structure depth is reached.
     * Depth == 1 will force tree to expand all nodes under the root.
     *
     * @param depth max structure depth to be expanded
     */
    public void expandAll ( final int depth )
    {
        expandAllImpl ( getRootNode (), 0, depth );
    }

    /**
     * Expands all child nodes until the specified max structure depth is reached.
     *
     * @param node         current level parent node
     * @param currentDepth current depth level
     * @param maxDepth     max depth level
     */
    private void expandAllImpl ( final E node, final int currentDepth, final int maxDepth )
    {
        final int depth = currentDepth + 1;
        for ( int i = 0; i < node.getChildCount (); i++ )
        {
            final E child = ( E ) node.getChildAt ( i );
            expandNode ( child );
            if ( depth < maxDepth )
            {
                expandAllImpl ( child, depth, maxDepth );
            }
        }
    }

    /**
     * Expands the specified node.
     *
     * @param node node to expand
     */
    public void expandNode ( final E node )
    {
        expandPath ( getPathForNode ( node ) );
    }

    /**
     * Returns whether node is expanded or not.
     *
     * @param node node to check
     * @return true if node is expanded, false otherwise
     */
    public boolean isExpanded ( final E node )
    {
        return isExpanded ( getPathForNode ( node ) );
    }

    /**
     * Returns selected node bounds.
     *
     * @return selected node bounds
     */
    public Rectangle getSelectedNodeBounds ()
    {
        return getNodeBounds ( getSelectedNode () );
    }

    /**
     * Returns node bounds.
     *
     * @param node node to process
     * @return node bounds
     */
    public Rectangle getNodeBounds ( final E node )
    {
        return getPathBounds ( getPathForNode ( node ) );
    }

    /**
     * Returns nodes combined bounds.
     *
     * @param nodes nodes to process
     * @return nodes combined bounds
     */
    public Rectangle getNodeBounds ( final List<E> nodes )
    {
        if ( nodes == null || nodes.size () == 0 )
        {
            return null;
        }
        else
        {
            Rectangle combined = null;
            for ( final E node : nodes )
            {
                combined = GeometryUtils.getContainingRect ( combined, getNodeBounds ( node ) );
            }
            return combined;
        }
    }

    /**
     * Returns full row bounds including the space before the node and after the node.
     *
     * @param node node to retrieve full row bounds for
     * @return full row bounds including the space before the node and after the node
     */
    public Rectangle getFullRowBounds ( final E node )
    {
        return getFullRowBounds ( getRowForNode ( node ) );
    }

    /**
     * Returns full row bounds including the space before the node and after the node.
     *
     * @param row row to retrieve full bounds for
     * @return full row bounds including the space before the node and after the node
     */
    public Rectangle getFullRowBounds ( final int row )
    {
        return getWebUI ().getFullRowBounds ( row );
    }

    /**
     * Returns row of the specified node.
     *
     * @param node node to find row for
     * @return row of the specified node
     */
    public int getRowForNode ( final E node )
    {
        return getRowForPath ( getPathForNode ( node ) );
    }

    /**
     * Returns tree node for the specified row.
     *
     * @param row row to process
     * @return tree node for the specified row
     */
    public E getNodeForRow ( final int row )
    {
        return getNodeForPath ( getPathForRow ( row ) );
    }

    /**
     * Returns tree path for specified node.
     *
     * @param node node to process
     * @return tree path
     */
    public TreePath getPathForNode ( final E node )
    {
        return node != null ? new TreePath ( node.getPath () ) : null;
    }

    /**
     * Returns tree node for specified path.
     *
     * @param path path to process
     * @return tree node for specified path
     */
    public E getNodeForPath ( final TreePath path )
    {
        return path != null ? ( E ) path.getLastPathComponent () : null;
    }

    /**
     * Returns tree node for the specified location.
     *
     * @param location location to process
     * @return tree node for the specified location
     */
    public E getNodeForLocation ( final Point location )
    {
        return getNodeForLocation ( location.x, location.y );
    }

    /**
     * Returns tree node for the specified location.
     *
     * @param x location X coordinate
     * @param y location Y coordinate
     * @return tree node for the specified location
     */
    public E getNodeForLocation ( final int x, final int y )
    {
        return getNodeForPath ( getPathForLocation ( x, y ) );
    }

    /**
     * Returns the path for the node at the specified location.
     *
     * @param location location to process
     * @return the {@code TreePath} for the node at that location
     */
    public TreePath getPathForLocation ( final Point location )
    {
        return getPathForLocation ( location.x, location.y );
    }

    /**
     * Returns closest tree node for the specified location.
     *
     * @param location location to process
     * @return tree node for the specified location
     */
    public E getClosestNodeForLocation ( final Point location )
    {
        return getClosestNodeForLocation ( location.x, location.y );
    }

    /**
     * Returns closest tree node for the specified location.
     *
     * @param x location X coordinate
     * @param y location Y coordinate
     * @return tree node for the specified location
     */
    public E getClosestNodeForLocation ( final int x, final int y )
    {
        return getNodeForPath ( getClosestPathForLocation ( x, y ) );
    }

    /**
     * Returns the path to the node that is closest to the specified location.
     *
     * @param location location to process
     * @return the {@code TreePath} for the node closest to that location, {@code null} if nothing is viewable or there is no model
     */
    public TreePath getClosestPathForLocation ( final Point location )
    {
        return getClosestPathForLocation ( location.x, location.y );
    }

    /**
     * Returns whether specified node is selected or not.
     *
     * @param node node to check
     * @return true if specified node is selected, false otherwise
     */
    public boolean isSelected ( final E node )
    {
        return isPathSelected ( getPathForNode ( node ) );
    }

    /**
     * Returns selected node.
     *
     * @return selected node
     */
    public E getSelectedNode ()
    {
        return getNodeForPath ( getSelectionPath () );
    }

    /**
     * Returns selected nodes.
     *
     * @return selected nodes
     */
    public List<E> getSelectedNodes ()
    {
        final List<E> selectedNodes = new ArrayList<E> ();
        final TreePath[] selectionPaths = getSelectionPaths ();
        if ( selectionPaths != null )
        {
            for ( final TreePath path : selectionPaths )
            {
                selectedNodes.add ( getNodeForPath ( path ) );
            }
        }
        return selectedNodes;
    }

    /**
     * Returns only selected nodes which are currently visible in tree area.
     * This will include nodes which are fully and partially visible in tree area.
     *
     * @return selected nodes which are currently visible in tree area
     */
    public List<E> getVisibleSelectedNodes ()
    {
        final List<E> selectedNodes = getSelectedNodes ();
        final Rectangle vr = getVisibleRect ();
        final Iterator<E> iterator = selectedNodes.iterator ();
        while ( iterator.hasNext () )
        {
            final E node = iterator.next ();
            if ( !vr.intersects ( getNodeBounds ( node ) ) )
            {
                iterator.remove ();
            }
        }
        return selectedNodes;
    }

    /**
     * Selects node under the specified point.
     *
     * @param point point to look for node
     */
    public void selectNodeUnderPoint ( final Point point )
    {
        selectNodeUnderPoint ( point.x, point.y );
    }

    /**
     * Selects node under the specified point.
     *
     * @param x point X coordinate
     * @param y point Y coordinate
     */
    public void selectNodeUnderPoint ( final int x, final int y )
    {
        setSelectionPath ( getPathForLocation ( x, y ) );
    }

    /**
     * Sets selected node.
     *
     * @param node node to select
     */
    public void setSelectedNode ( final E node )
    {
        final TreePath path = getPathForNode ( node );
        if ( path != null )
        {
            setSelectionPath ( path );
        }
    }

    /**
     * Sets selected nodes.
     *
     * @param nodes nodes to select
     */
    public void setSelectedNodes ( final List<E> nodes )
    {
        final TreePath[] paths = new TreePath[ nodes.size () ];
        for ( int i = 0; i < nodes.size (); i++ )
        {
            paths[ i ] = getPathForNode ( nodes.get ( i ) );
        }
        setSelectionPaths ( paths );
    }

    /**
     * Sets selected nodes.
     *
     * @param nodes nodes to select
     */
    public void setSelectedNodes ( final E[] nodes )
    {
        final TreePath[] paths = new TreePath[ nodes.length ];
        for ( int i = 0; i < nodes.length; i++ )
        {
            paths[ i ] = getPathForNode ( nodes[ i ] );
        }
        setSelectionPaths ( paths );
    }

    /**
     * Returns first visible leaf node from the top of the tree.
     * This doesn't include nodes under collapsed paths but does include nodes which are not in visible rect.
     *
     * @return first visible leaf node from the top of the tree
     */
    public E getFirstVisibleLeafNode ()
    {
        for ( int i = 0; i < getRowCount (); i++ )
        {
            final E node = getNodeForRow ( i );
            if ( getModel ().isLeaf ( node ) )
            {
                return node;
            }
        }
        return null;
    }

    /**
     * Selects first visible leaf node from the top of the tree.
     */
    public void selectFirstVisibleLeafNode ()
    {
        final E node = getFirstVisibleLeafNode ();
        if ( node != null )
        {
            setSelectedNode ( node );
        }
    }

    /**
     * Selects row next to currently selected.
     * First row will be selected if none or last row was selected.
     */
    public void selectNextRow ()
    {
        selectNextRow ( true );
    }

    /**
     * Selects row next to currently selected.
     * First row will be selected if none was selected.
     * First row will be selected if last row was selected and cycling is allowed.
     *
     * @param cycle whether or not should allow cycled selection
     */
    public void selectNextRow ( final boolean cycle )
    {
        final int row = getLeadSelectionRow ();
        if ( row != -1 )
        {
            if ( row < getRowCount () - 1 )
            {
                setSelectionRow ( row + 1 );
            }
            else if ( cycle )
            {
                setSelectionRow ( 0 );
            }
        }
        else
        {
            setSelectionRow ( 0 );
        }
    }

    /**
     * Selects row next to currently selected.
     * Last row will be selected if none or first row was selected.
     */
    public void selectPreviousRow ()
    {
        selectPreviousRow ( true );
    }

    /**
     * Selects row previous to currently selected.
     * Last row will be selected if none or last was selected.
     * Last row will be selected if first row was selected and cycling is allowed.
     *
     * @param cycle whether or not should allow cycled selection
     */
    public void selectPreviousRow ( final boolean cycle )
    {
        final int row = getLeadSelectionRow ();
        if ( row != -1 )
        {
            if ( row > 0 )
            {
                setSelectionRow ( row - 1 );
            }
            else if ( cycle )
            {
                setSelectionRow ( getRowCount () - 1 );
            }
        }
        else
        {
            setSelectionRow ( getRowCount () - 1 );
        }
    }

    /**
     * Returns tree root node.
     *
     * @return tree root node
     */
    public E getRootNode ()
    {
        return ( E ) getModel ().getRoot ();
    }

    /**
     * Returns list of all nodes added into the tree.
     *
     * @return list of all nodes added into the tree
     */
    public List<E> getAllNodes ()
    {
        final List<E> nodes = new ArrayList<E> ();
        getAllNodesImpl ( nodes, getRootNode () );
        return nodes;
    }

    /**
     * Collects list of all nodes added into the tree.
     *
     * @param nodes list into which all nodes should be collected
     * @param node  node to start collecting from
     */
    private void getAllNodesImpl ( final List<E> nodes, final E node )
    {
        nodes.add ( node );
        for ( int i = 0; i < node.getChildCount (); i++ )
        {
            getAllNodesImpl ( nodes, ( E ) node.getChildAt ( i ) );
        }
    }

    /**
     * Sets tree selection mode.
     *
     * @param mode tree selection mode
     */
    public void setSelectionMode ( final int mode )
    {
        getSelectionModel ().setSelectionMode ( mode );
    }

    /**
     * Sets whether multiply nodes selection allowed or not.
     * This call simply changes selection mode according to provided value.
     *
     * @param allowed whether multiply nodes selection allowed or not
     */
    public void setMultiplySelectionAllowed ( final boolean allowed )
    {
        setSelectionMode ( allowed ? DISCONTIGUOUS_TREE_SELECTION : SINGLE_TREE_SELECTION );
    }

    /**
     * Returns whether tree automatically scrolls to selection or not.
     *
     * @return true if tree automatically scrolls to selection, false otherwise
     */
    public boolean isScrollToSelection ()
    {
        return scrollToSelectionListener != null;
    }

    /**
     * Sets whether tree should automatically scroll to selection or not.
     *
     * @param scroll whether tree should automatically scroll to selection or not
     */
    public void setScrollToSelection ( final boolean scroll )
    {
        if ( scroll )
        {
            if ( !isScrollToSelection () )
            {
                scrollToSelectionListener = new TreeSelectionListener ()
                {
                    @Override
                    public void valueChanged ( final TreeSelectionEvent e )
                    {
                        scrollToSelection ();
                    }
                };
                addTreeSelectionListener ( scrollToSelectionListener );
            }
        }
        else
        {
            if ( isScrollToSelection () )
            {
                removeTreeSelectionListener ( scrollToSelectionListener );
                scrollToSelectionListener = null;
            }
        }
    }

    /**
     * Scrolls tree view to the beginning of the tree.
     */
    public void scrollToStart ()
    {
        scrollRectToVisible ( new Rectangle ( 0, 0, 1, 1 ) );
    }

    /**
     * Scrolls tree view to selected nodes.
     */
    public void scrollToSelection ()
    {
        final Rectangle bounds = getPathBounds ( getSelectionPath () );
        if ( bounds != null )
        {
            // Ignore scroll action if node is already fully visible
            final Rectangle vr = getVisibleRect ();
            if ( !vr.contains ( bounds ) )
            {
                // Leave additional 1/2 of visible height on top of the node
                // Otherwise it is hard to look where this node is located
                bounds.y = bounds.y + bounds.height / 2 - vr.height / 2;

                // Put node into the middle of visible area
                if ( vr.width > bounds.width )
                {
                    bounds.x = bounds.x + bounds.width / 2 - vr.width / 2;
                }

                // Setup width and height we want to see
                bounds.width = vr.width;
                bounds.height = vr.height;

                scrollRectToVisible ( bounds );
            }
        }
    }

    /**
     * Scrolls tree view to specified node.
     *
     * @param node node to scroll to
     */
    public void scrollToNode ( final E node )
    {
        final Rectangle bounds = getNodeBounds ( node );
        if ( bounds != null )
        {
            scrollRectToVisible ( bounds );
        }
    }

    /**
     * Starts editing selected tree node.
     */
    public void startEditingSelectedNode ()
    {
        startEditingNode ( getSelectedNode () );
    }

    /**
     * Starts editing the specified node.
     *
     * @param node tree node to edit
     */
    public void startEditingNode ( final E node )
    {
        if ( node != null )
        {
            final TreePath path = getPathForNode ( node );
            if ( path != null )
            {
                if ( !isVisible ( path ) )
                {
                    expandPath ( path );
                }
                startEditingAtPath ( path );
            }
        }
    }

    /**
     * Updates all visible nodes.
     * This might be used to update node sizes if renderer has changed.
     */
    public void updateAllVisibleNodes ()
    {
        revalidate ();
        repaint ();
    }

    /**
     * Returns tree expansion and selection states.
     * Tree nodes must be instances of UniqueNode class.
     *
     * @return tree expansion and selection states
     */
    public TreeState getTreeState ()
    {
        return TreeUtils.getTreeState ( this );
    }

    /**
     * Returns tree expansion and selection states.
     * Tree nodes must be instances of UniqueNode class.
     *
     * @param saveSelection whether to save selection states or not
     * @return tree expansion and selection states
     */
    public TreeState getTreeState ( final boolean saveSelection )
    {
        return TreeUtils.getTreeState ( this, saveSelection );
    }

    /**
     * Restores tree expansion and selection states.
     * Tree nodes must be instances of UniqueNode class.
     *
     * @param treeState tree expansion and selection states
     */
    public void setTreeState ( final TreeState treeState )
    {
        TreeUtils.setTreeState ( this, treeState );
    }

    /**
     * Restores tree expansion and selection states.
     * Tree nodes must be instances of UniqueNode class.
     *
     * @param treeState        tree expansion and selection states
     * @param restoreSelection whether to restore selection states or not
     */
    public void setTreeState ( final TreeState treeState, final boolean restoreSelection )
    {
        TreeUtils.setTreeState ( this, treeState, restoreSelection );
    }

    /**
     * Returns whether tree should auto-expand nodes on selection or not.
     *
     * @return true if tree should auto-expand nodes on selection, false otherwise
     */
    public boolean isAutoExpandSelectedNode ()
    {
        return getWebUI ().isAutoExpandSelectedNode ();
    }

    /**
     * Sets whether tree should auto-expand nodes on selection or not.
     *
     * @param autoExpand whether tree should auto-expand nodes on selection or not
     */
    public void setAutoExpandSelectedNode ( final boolean autoExpand )
    {
        getWebUI ().setAutoExpandSelectedNode ( autoExpand );
    }

    /**
     * Returns whether tree should auto-expand single child nodes or not.
     * If set to true when any node is expanded and there is only one single child node in it - it will be automatically expanded.
     *
     * @return true if tree should auto-expand single child nodes, false otherwise
     */
    public boolean isAutoExpandSingleChildNode ()
    {
        return AutoExpandSingleChildNodeListener.isInstalled ( this );
    }

    /**
     * Sets whether tree should auto-expand single child nodes or not.
     * If set to true when any node is expanded and there is only one single child node in it - it will be automatically expanded.
     *
     * @param autoExpand whether tree should auto-expand single child nodes or not
     */
    public void setAutoExpandSingleChildNode ( final boolean autoExpand )
    {
        if ( autoExpand )
        {
            if ( !isAutoExpandSingleChildNode () )
            {
                AutoExpandSingleChildNodeListener.install ( this );
            }
        }
        else
        {
            if ( isAutoExpandSingleChildNode () )
            {
                AutoExpandSingleChildNodeListener.uninstall ( this );
            }
        }
    }

    /**
     * Returns current mousover row.
     *
     * @return current mousover row
     */
    public int getMouseoverRow ()
    {
        return getWebUI ().getMouseoverRow ();
    }

    /**
     * Returns whether or not nodes should be selected on mouseover.
     *
     * @return true if nodes should be selected on mouseover, false otherwise
     */
    public boolean isMouseoverSelection ()
    {
        return getWebUI ().isMouseoverSelection ();
    }

    /**
     * Sets whether or not nodes should be selected on mouseover.
     *
     * @param select whether or not nodes should be selected on mouseover
     */
    public void setMouseoverSelection ( final boolean select )
    {
        getWebUI ().setMouseoverSelection ( select );
    }

    /**
     * Returns whether or not mouseover nodes should be highlighted.
     *
     * @return true if mouseover nodes should be highlighted, false otherwise
     */
    public boolean isMouseoverHighlight ()
    {
        return getWebUI ().isMouseoverHighlight ();
    }

    /**
     * Sets whether or not mouseover nodes should be highlighted.
     *
     * @param highlight whether or not mouseover nodes should be highlighted
     */
    public void setMouseoverHighlight ( final boolean highlight )
    {
        getWebUI ().setMouseoverHighlight ( highlight );
    }

    /**
     * Returns tree selection style.
     *
     * @return tree selection style
     */
    public TreeSelectionStyle getSelectionStyle ()
    {
        return getWebUI ().getSelectionStyle ();
    }

    /**
     * Sets tree selection style.
     *
     * @param style tree selection style
     */
    public void setSelectionStyle ( final TreeSelectionStyle style )
    {
        getWebUI ().setSelectionStyle ( style );
    }

    @Override
    public StyleId getStyleId ()
    {
        return getWebUI ().getStyleId ();
    }

    @Override
    public StyleId setStyleId ( final StyleId id )
    {
        return getWebUI ().setStyleId ( id );
    }

    @Override
    public Shape provideShape ()
    {
        return getWebUI ().provideShape ();
    }

    /**
     * Adds mouseover listener.
     *
     * @param listener mouseover listener to add
     */
    public void addMouseoverListener ( final MouseoverListener<E> listener )
    {
        mouseoverListeners.add ( listener );
    }

    /**
     * Removes mouseover listener.
     *
     * @param listener mouseover listener to remove
     */
    public void removeMouseoverListener ( final MouseoverListener<E> listener )
    {
        mouseoverListeners.remove ( listener );
    }

    /**
     * Informs about mouseover node change.
     *
     * @param previous previous mouseover node
     * @param current  current mouseover node
     */
    public void fireMouseoverChanged ( final E previous, final E current )
    {
        for ( final MouseoverListener listener : mouseoverListeners )
        {
            listener.mouseoverChanged ( previous, current );
        }
    }

    /**
     * Returns Web-UI applied to this class.
     *
     * @return Web-UI applied to this class
     */
    public WebTreeUI getWebUI ()
    {
        return ( WebTreeUI ) getUI ();
    }

    /**
     * Installs a Web-UI into this component.
     */
    @Override
    public void updateUI ()
    {
        if ( getUI () == null || !( getUI () instanceof WebTreeUI ) )
        {
            try
            {
                setUI ( ( WebTreeUI ) ReflectUtils.createInstance ( WebLookAndFeel.treeUI ) );
            }
            catch ( final Throwable e )
            {
                Log.error ( this, e );
                setUI ( new WebTreeUI () );
            }
        }
        else
        {
            setUI ( getUI () );
        }
    }

    /**
     * Repaints specified tree row.
     *
     * @param row row index
     */
    public void repaint ( final int row )
    {
        repaint ( getWebUI ().getRowBounds ( row ) );
    }

    /**
     * Repaints all tree rows in specified range.
     *
     * @param from first row index
     * @param to   last row index
     */
    public void repaint ( final int from, final int to )
    {
        final WebTreeUI webUI = getWebUI ();
        final Rectangle fromBounds = webUI.getRowBounds ( from );
        final Rectangle toBounds = webUI.getRowBounds ( to );
        final Rectangle rect = GeometryUtils.getContainingRect ( fromBounds, toBounds );
        if ( rect != null )
        {
            repaint ( rect );
        }
    }

    /**
     * Repaints specified node.
     *
     * @param node node to repaint
     */
    public void repaint ( final E node )
    {
        if ( node != null )
        {
            final Rectangle bounds = getNodeBounds ( node );
            if ( bounds != null )
            {
                repaint ( bounds );
            }
        }
    }

    /**
     * Repaints specified node.
     *
     * @param nodes nodes to repaint
     */
    public void repaint ( final List<E> nodes )
    {
        if ( nodes != null && nodes.size () > 0 )
        {
            Rectangle summ = null;
            for ( final E node : nodes )
            {
                summ = GeometryUtils.getContainingRect ( summ, getNodeBounds ( node ) );
            }
            if ( summ != null )
            {
                repaint ( summ );
            }
        }
    }

    /**
     * Returns tree cell renderer pane.
     *
     * @return tree cell renderer pane
     */
    public CellRendererPane getCellRendererPane ()
    {
        return getWebUI ().getCellRendererPane ();
    }

    @Override
    public MouseAdapter onNodeDoubleClick ( final TreeNodeEventRunnable<E> runnable )
    {
        return TreeEventUtils.onNodeDoubleClick ( this, runnable );
    }

    @Override
    public MouseAdapter onNodeDoubleClick ( final Predicate<E> condition, final TreeNodeEventRunnable<E> runnable )
    {
        return TreeEventUtils.onNodeDoubleClick ( this, condition, runnable );
    }

    @Override
    public MouseAdapter onMousePress ( final MouseEventRunnable runnable )
    {
        return EventUtils.onMousePress ( this, runnable );
    }

    @Override
    public MouseAdapter onMousePress ( final MouseButton mouseButton, final MouseEventRunnable runnable )
    {
        return EventUtils.onMousePress ( this, mouseButton, runnable );
    }

    @Override
    public MouseAdapter onMouseEnter ( final MouseEventRunnable runnable )
    {
        return EventUtils.onMouseEnter ( this, runnable );
    }

    @Override
    public MouseAdapter onMouseExit ( final MouseEventRunnable runnable )
    {
        return EventUtils.onMouseExit ( this, runnable );
    }

    @Override
    public MouseAdapter onMouseDrag ( final MouseEventRunnable runnable )
    {
        return EventUtils.onMouseDrag ( this, runnable );
    }

    @Override
    public MouseAdapter onMouseDrag ( final MouseButton mouseButton, final MouseEventRunnable runnable )
    {
        return EventUtils.onMouseDrag ( this, mouseButton, runnable );
    }

    @Override
    public MouseAdapter onMouseClick ( final MouseEventRunnable runnable )
    {
        return EventUtils.onMouseClick ( this, runnable );
    }

    @Override
    public MouseAdapter onMouseClick ( final MouseButton mouseButton, final MouseEventRunnable runnable )
    {
        return EventUtils.onMouseClick ( this, mouseButton, runnable );
    }

    @Override
    public MouseAdapter onDoubleClick ( final MouseEventRunnable runnable )
    {
        return EventUtils.onDoubleClick ( this, runnable );
    }

    @Override
    public MouseAdapter onMenuTrigger ( final MouseEventRunnable runnable )
    {
        return EventUtils.onMenuTrigger ( this, runnable );
    }

    @Override
    public KeyAdapter onKeyType ( final KeyEventRunnable runnable )
    {
        return EventUtils.onKeyType ( this, runnable );
    }

    @Override
    public KeyAdapter onKeyType ( final HotkeyData hotkey, final KeyEventRunnable runnable )
    {
        return EventUtils.onKeyType ( this, hotkey, runnable );
    }

    @Override
    public KeyAdapter onKeyPress ( final KeyEventRunnable runnable )
    {
        return EventUtils.onKeyPress ( this, runnable );
    }

    @Override
    public KeyAdapter onKeyPress ( final HotkeyData hotkey, final KeyEventRunnable runnable )
    {
        return EventUtils.onKeyPress ( this, hotkey, runnable );
    }

    @Override
    public KeyAdapter onKeyRelease ( final KeyEventRunnable runnable )
    {
        return EventUtils.onKeyRelease ( this, runnable );
    }

    @Override
    public KeyAdapter onKeyRelease ( final HotkeyData hotkey, final KeyEventRunnable runnable )
    {
        return EventUtils.onKeyRelease ( this, hotkey, runnable );
    }

    @Override
    public FocusAdapter onFocusGain ( final FocusEventRunnable runnable )
    {
        return EventUtils.onFocusGain ( this, runnable );
    }

    @Override
    public FocusAdapter onFocusLoss ( final FocusEventRunnable runnable )
    {
        return EventUtils.onFocusLoss ( this, runnable );
    }

    @Override
    public void registerSettings ( final String key )
    {
        SettingsManager.registerComponent ( this, key );
    }

    @Override
    public <V extends DefaultValue> void registerSettings ( final String key, final Class<V> defaultValueClass )
    {
        SettingsManager.registerComponent ( this, key, defaultValueClass );
    }

    @Override
    public void registerSettings ( final String key, final Object defaultValue )
    {
        SettingsManager.registerComponent ( this, key, defaultValue );
    }

    @Override
    public void registerSettings ( final String group, final String key )
    {
        SettingsManager.registerComponent ( this, group, key );
    }

    @Override
    public <V extends DefaultValue> void registerSettings ( final String group, final String key, final Class<V> defaultValueClass )
    {
        SettingsManager.registerComponent ( this, group, key, defaultValueClass );
    }

    @Override
    public void registerSettings ( final String group, final String key, final Object defaultValue )
    {
        SettingsManager.registerComponent ( this, group, key, defaultValue );
    }

    @Override
    public void registerSettings ( final String key, final boolean loadInitialSettings, final boolean applySettingsChanges )
    {
        SettingsManager.registerComponent ( this, key, loadInitialSettings, applySettingsChanges );
    }

    @Override
    public <V extends DefaultValue> void registerSettings ( final String key, final Class<V> defaultValueClass,
                                                            final boolean loadInitialSettings, final boolean applySettingsChanges )
    {
        SettingsManager.registerComponent ( this, key, defaultValueClass, loadInitialSettings, applySettingsChanges );
    }

    @Override
    public void registerSettings ( final String key, final Object defaultValue, final boolean loadInitialSettings,
                                   final boolean applySettingsChanges )
    {
        SettingsManager.registerComponent ( this, key, defaultValue, loadInitialSettings, applySettingsChanges );
    }

    @Override
    public <V extends DefaultValue> void registerSettings ( final String group, final String key, final Class<V> defaultValueClass,
                                                            final boolean loadInitialSettings, final boolean applySettingsChanges )
    {
        SettingsManager.registerComponent ( this, group, key, defaultValueClass, loadInitialSettings, applySettingsChanges );
    }

    @Override
    public void registerSettings ( final String group, final String key, final Object defaultValue, final boolean loadInitialSettings,
                                   final boolean applySettingsChanges )
    {
        SettingsManager.registerComponent ( this, group, key, defaultValue, loadInitialSettings, applySettingsChanges );
    }

    @Override
    public void registerSettings ( final SettingsProcessor settingsProcessor )
    {
        SettingsManager.registerComponent ( this, settingsProcessor );
    }

    @Override
    public void unregisterSettings ()
    {
        SettingsManager.unregisterComponent ( this );
    }

    @Override
    public void loadSettings ()
    {
        SettingsManager.loadComponentSettings ( this );
    }

    @Override
    public void saveSettings ()
    {
        SettingsManager.saveComponentSettings ( this );
    }

    @Override
    public WebTree<E> setPlainFont ()
    {
        return SwingUtils.setPlainFont ( this );
    }

    @Override
    public WebTree<E> setPlainFont ( final boolean apply )
    {
        return SwingUtils.setPlainFont ( this, apply );
    }

    @Override
    public boolean isPlainFont ()
    {
        return SwingUtils.isPlainFont ( this );
    }

    @Override
    public WebTree<E> setBoldFont ()
    {
        return SwingUtils.setBoldFont ( this );
    }

    @Override
    public WebTree<E> setBoldFont ( final boolean apply )
    {
        return SwingUtils.setBoldFont ( this, apply );
    }

    @Override
    public boolean isBoldFont ()
    {
        return SwingUtils.isBoldFont ( this );
    }

    @Override
    public WebTree<E> setItalicFont ()
    {
        return SwingUtils.setItalicFont ( this );
    }

    @Override
    public WebTree<E> setItalicFont ( final boolean apply )
    {
        return SwingUtils.setItalicFont ( this, apply );
    }

    @Override
    public boolean isItalicFont ()
    {
        return SwingUtils.isItalicFont ( this );
    }

    @Override
    public WebTree<E> setFontStyle ( final boolean bold, final boolean italic )
    {
        return SwingUtils.setFontStyle ( this, bold, italic );
    }

    @Override
    public WebTree<E> setFontStyle ( final int style )
    {
        return SwingUtils.setFontStyle ( this, style );
    }

    @Override
    public WebTree<E> setFontSize ( final int fontSize )
    {
        return SwingUtils.setFontSize ( this, fontSize );
    }

    @Override
    public WebTree<E> changeFontSize ( final int change )
    {
        return SwingUtils.changeFontSize ( this, change );
    }

    @Override
    public int getFontSize ()
    {
        return SwingUtils.getFontSize ( this );
    }

    @Override
    public WebTree<E> setFontSizeAndStyle ( final int fontSize, final boolean bold, final boolean italic )
    {
        return SwingUtils.setFontSizeAndStyle ( this, fontSize, bold, italic );
    }

    @Override
    public WebTree<E> setFontSizeAndStyle ( final int fontSize, final int style )
    {
        return SwingUtils.setFontSizeAndStyle ( this, fontSize, style );
    }

    @Override
    public WebTree<E> setFontName ( final String fontName )
    {
        return SwingUtils.setFontName ( this, fontName );
    }

    @Override
    public String getFontName ()
    {
        return SwingUtils.getFontName ( this );
    }

    /**
     * Returns a TreeModel wrapping the specified object.
     * If the object is an array of Object, Hashtable or Vector then a new root node is created with each of the incoming objects as
     * children. Otherwise, a new root is created with the specified object as its value.
     *
     * @param value Object used as the foundation for the TreeModel
     * @return a TreeModel wrapping the specified object
     */
    protected static TreeModel createTreeModel ( final Object value )
    {
        final DefaultMutableTreeNode root;
        if ( value instanceof Object[] || value instanceof Hashtable || value instanceof Vector )
        {
            root = new DefaultMutableTreeNode ( "root" );
            DynamicUtilTreeNode.createChildren ( root, value );
        }
        else
        {
            root = new DynamicUtilTreeNode ( "root", value );
        }
        return new WebTreeModel<DefaultMutableTreeNode> ( root, false );
    }

    /**
     * Creates and returns a sample TreeModel.
     * Used primarily for beanbuilders to show something interesting.
     *
     * @return the default TreeModel
     */
    public static TreeModel getDefaultTreeModel ()
    {
        final UniqueNode root = new UniqueNode ( "JTree" );

        UniqueNode parent = new UniqueNode ( "colors" );
        parent.add ( new UniqueNode ( "blue" ) );
        parent.add ( new UniqueNode ( "violet" ) );
        parent.add ( new UniqueNode ( "red" ) );
        parent.add ( new UniqueNode ( "yellow" ) );
        root.add ( parent );

        parent = new UniqueNode ( "sports" );
        parent.add ( new UniqueNode ( "basketball" ) );
        parent.add ( new UniqueNode ( "soccer" ) );
        parent.add ( new UniqueNode ( "football" ) );
        parent.add ( new UniqueNode ( "hockey" ) );
        root.add ( parent );

        parent = new UniqueNode ( "food" );
        parent.add ( new UniqueNode ( "hot dogs" ) );
        parent.add ( new UniqueNode ( "pizza" ) );
        parent.add ( new UniqueNode ( "ravioli" ) );
        parent.add ( new UniqueNode ( "bananas" ) );
        root.add ( parent );

        return new WebTreeModel<UniqueNode> ( root );
    }
}