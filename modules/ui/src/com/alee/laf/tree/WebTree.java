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

import com.alee.api.jdk.Objects;
import com.alee.api.jdk.Predicate;
import com.alee.laf.tree.behavior.TreeHoverSelectionBehavior;
import com.alee.laf.tree.behavior.TreeSelectionExpandBehavior;
import com.alee.laf.tree.behavior.TreeSingleChildExpandBehavior;
import com.alee.managers.hotkey.HotkeyData;
import com.alee.managers.language.DictionaryListener;
import com.alee.managers.language.LanguageEventMethods;
import com.alee.managers.language.LanguageListener;
import com.alee.managers.language.UILanguageManager;
import com.alee.managers.settings.Configuration;
import com.alee.managers.settings.SettingsMethods;
import com.alee.managers.settings.SettingsProcessor;
import com.alee.managers.settings.UISettingsManager;
import com.alee.managers.style.*;
import com.alee.painter.Paintable;
import com.alee.painter.Painter;
import com.alee.utils.GeometryUtils;
import com.alee.utils.compare.Filter;
import com.alee.utils.swing.HoverListener;
import com.alee.utils.swing.MouseButton;
import com.alee.utils.swing.extensions.*;

import javax.swing.*;
import javax.swing.event.CellEditorListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.KeyAdapter;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.*;
import java.util.List;

/**
 * {@link JTree} extension class.
 * It contains various useful methods to simplify core component usage.
 * <p>
 * This component should never be used with a non-Web UIs as it might cause an unexpected behavior.
 * You could still use that component even if WebLaF is not your application LaF as this component will use Web-UI in any case.
 *
 * @param <N> node type
 * @author Mikle Garin
 * @see JTree
 * @see WebTreeUI
 * @see TreePainter
 * @see WebTreeModel
 */
public class WebTree<N extends MutableTreeNode> extends JTree implements Styleable, Paintable, ShapeMethods, MarginMethods, PaddingMethods,
        TreeEventMethods<N>, EventMethods, LanguageEventMethods, SettingsMethods, FontMethods<WebTree<N>>, SizeMethods<WebTree<N>>
{
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
     * Listener that forces tree to scroll view to selection.
     * It is disabled by default and null in that case.
     */
    protected transient TreeSelectionListener scrollToSelectionListener = null;

    /**
     * Special state provider that can be set to check whether or not specific nodes are editable.
     */
    protected transient Predicate<N> editableStateProvider = null;

    /**
     * Custom WebLaF tooltip provider.
     */
    protected transient TreeToolTipProvider<N> toolTipProvider = null;

    /**
     * Constructs tree with default sample model.
     */
    public WebTree ()
    {
        this ( StyleId.auto );
    }

    /**
     * Constructs tree with model based on specified values.
     *
     * @param data tree data
     */
    public WebTree ( final Object[] data )
    {
        this ( StyleId.auto, data );
    }

    /**
     * Constructs tree with model based on specified values.
     *
     * @param data tree data
     */
    public WebTree ( final Vector<?> data )
    {
        this ( StyleId.auto, data );
    }

    /**
     * Constructs tree with model based on specified values.
     *
     * @param data tree data
     */
    public WebTree ( final Hashtable<?, ?> data )
    {
        this ( StyleId.auto, data );
    }

    /**
     * Constructs tree with model based on specified root node.
     *
     * @param root tree root node
     */
    public WebTree ( final N root )
    {
        this ( StyleId.auto, root );
    }

    /**
     * Constructs tree with model based on specified root node and which decides whether a node is a leaf node in the specified manner.
     *
     * @param root               tree root node
     * @param asksAllowsChildren false if any node can have children, true if each node is asked to see if it can have children
     */
    public WebTree ( final N root, final boolean asksAllowsChildren )
    {
        this ( StyleId.auto, root, asksAllowsChildren );
    }

    /**
     * Constructs tree with specified model.
     *
     * @param newModel tree model
     */
    public WebTree ( final TreeModel newModel )
    {
        this ( StyleId.auto, newModel );
    }

    /**
     * Constructs tree with default sample model.
     *
     * @param id style ID
     */
    public WebTree ( final StyleId id )
    {
        this ( id, createDefaultTreeModel () );
    }

    /**
     * Constructs tree with model based on specified values.
     *
     * @param id   style ID
     * @param data tree data
     */
    public WebTree ( final StyleId id, final Object[] data )
    {
        this ( id, createTreeModel ( data ) );
    }

    /**
     * Constructs tree with model based on specified values.
     *
     * @param id   style ID
     * @param data tree data
     */
    public WebTree ( final StyleId id, final Vector<?> data )
    {
        this ( id, createTreeModel ( data ) );
    }

    /**
     * Constructs tree with model based on specified values.
     *
     * @param id   style ID
     * @param data tree data
     */
    public WebTree ( final StyleId id, final Hashtable<?, ?> data )
    {
        this ( id, createTreeModel ( data ) );
    }

    /**
     * Constructs tree with model based on specified root node.
     *
     * @param id   style ID
     * @param root tree root node
     */
    public WebTree ( final StyleId id, final N root )
    {
        this ( id, new WebTreeModel<N> ( root ) );
    }

    /**
     * Constructs tree with model based on specified root node and which decides whether a node is a leaf node in the specified manner.
     *
     * @param id                 style ID
     * @param root               tree root node
     * @param asksAllowsChildren false if any node can have children, true if each node is asked to see if it can have children
     */
    public WebTree ( final StyleId id, final N root, final boolean asksAllowsChildren )
    {
        this ( id, new WebTreeModel<N> ( root, asksAllowsChildren ) );
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
    }

    @Override
    public void setCellEditor ( final TreeCellEditor cellEditor )
    {
        // Removing cell editor listeners from old cell editor
        if ( this.cellEditor != null )
        {
            for ( final CellEditorListener listener : listenerList.getListeners ( CellEditorListener.class ) )
            {
                this.cellEditor.removeCellEditorListener ( listener );
            }
        }

        // Updating cell editor
        super.setCellEditor ( cellEditor );

        // Adding cell editor listeners to new cell editor
        if ( cellEditor != null )
        {
            for ( final CellEditorListener listener : listenerList.getListeners ( CellEditorListener.class ) )
            {
                cellEditor.addCellEditorListener ( listener );
            }
        }
    }

    /**
     * Unlike {@link JTree#getToolTipText()} this implementation takes row selection style into account.
     * That means that tooltips for {@link TreeSelectionStyle#line} will be displayed at any point in the row, not just on the node.
     *
     * @param event {@link MouseEvent}
     * @return tooltip text
     */
    @Override
    public String getToolTipText ( final MouseEvent event )
    {
        String tip = null;
        if ( event != null )
        {
            final Point point = event.getPoint ();
            final WTreeUI ui = getUI ();
            final int row = ui.getExactRowForLocation ( point );
            final TreeCellRenderer cellRenderer = getCellRenderer ();
            if ( row != -1 && cellRenderer != null )
            {
                final TreePath path = getPathForRow ( row );
                final Object value = path.getLastPathComponent ();
                final boolean selected = isRowSelected ( row );
                final boolean expanded = isExpanded ( row );
                final boolean leaf = getModel ().isLeaf ( value );
                final Component renderer = cellRenderer.getTreeCellRendererComponent ( this, value, selected, expanded, leaf, row, true );
                if ( renderer instanceof JComponent )
                {
                    final Rectangle pathBounds = getPathBounds ( path );
                    final MouseEvent newEvent = new MouseEvent ( renderer, event.getID (),
                            event.getWhen (),
                            event.getModifiers (),
                            point.x - pathBounds.x,
                            point.y - pathBounds.y,
                            event.getXOnScreen (),
                            event.getYOnScreen (),
                            event.getClickCount (),
                            event.isPopupTrigger (),
                            MouseEvent.NOBUTTON );

                    final JComponent jComponent = ( JComponent ) renderer;
                    tip = jComponent.getToolTipText ( newEvent );
                }
            }
        }
        if ( tip == null )
        {
            tip = getToolTipText ();
        }
        return tip;
    }

    /**
     * Adds tree cell editor listener.
     * These listeners act separately from the cell editor and will be moved to new tree cell editor automatically on set.
     *
     * @param listener cell editor listener to add
     */
    public void addCellEditorListener ( final CellEditorListener listener )
    {
        // Saving listener
        listenerList.add ( CellEditorListener.class, listener );

        // Adding listener to the current cell editor
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
        // Removing listener
        listenerList.remove ( CellEditorListener.class, listener );

        // Removing listener from the current cell editor
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
    public Predicate<N> getEditableStateProvider ()
    {
        return editableStateProvider;
    }

    /**
     * Sets special state provider that can be set to check whether or not specific nodes are editable.
     * You can provide null to disable this state check at all.
     *
     * @param stateProvider special state provider that can be set to check whether or not specific nodes are editable
     */
    public void setEditableStateProvider ( final Predicate<N> stateProvider )
    {
        this.editableStateProvider = stateProvider;
    }

    @Override
    public boolean isPathEditable ( final TreePath path )
    {
        return super.isPathEditable ( path ) && isNodeEditable ( ( N ) path.getLastPathComponent () );
    }

    /**
     * Returns whether the specified tree node is editable or not.
     * This method is an improved version of default JTree method isPathEditable.
     *
     * @param node node to check editable state for
     * @return true if the specified tree node is editable, false otherwise
     * @see #isPathEditable(javax.swing.tree.TreePath)
     */
    public boolean isNodeEditable ( final N node )
    {
        return editableStateProvider == null || editableStateProvider.test ( node );
    }

    /**
     * Returns {@link TreeToolTipProvider}.
     *
     * @return {@link TreeToolTipProvider}
     */
    public TreeToolTipProvider<N> getToolTipProvider ()
    {
        return toolTipProvider;
    }

    /**
     * Sets {@link TreeToolTipProvider}.
     *
     * @param provider {@link TreeToolTipProvider}
     */
    public void setToolTipProvider ( final TreeToolTipProvider<N> provider )
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
    public void expandAll ( final Filter<N> shouldExpand )
    {
        if ( shouldExpand == null )
        {
            expandAll ();
        }
        else
        {
            final N rootNode = getRootNode ();
            expandAll ( rootNode, shouldExpand );
        }
    }

    /**
     * Expands all child nodes of the specified node.
     *
     * @param node node to expand
     */
    public void expandAll ( final N node )
    {
        expandAll ( node, null );
    }

    /**
     * Expands all child nodes accepted by filter in a single call.
     *
     * @param node         node to expand
     * @param shouldExpand expand filter
     */
    public void expandAll ( final N node, final Filter<N> shouldExpand )
    {
        if ( shouldExpand == null || shouldExpand.accept ( node ) )
        {
            expandNode ( node );
            for ( int i = 0; i < node.getChildCount (); i++ )
            {
                expandAll ( ( N ) node.getChildAt ( i ), shouldExpand );
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
    private void expandAllImpl ( final N node, final int currentDepth, final int maxDepth )
    {
        final int depth = currentDepth + 1;
        for ( int i = 0; i < node.getChildCount (); i++ )
        {
            final N child = ( N ) node.getChildAt ( i );
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
    public void expandNode ( final N node )
    {
        expandPath ( getPathForNode ( node ) );
    }

    /**
     * Returns whether node is expanded or not.
     *
     * @param node node to check
     * @return true if node is expanded, false otherwise
     */
    public boolean isExpanded ( final N node )
    {
        return isExpanded ( getPathForNode ( node ) );
    }

    /**
     * Returns selected {@link MutableTreeNode} bounds.
     *
     * @return selected {@link MutableTreeNode} bounds
     */
    public Rectangle getSelectedNodeBounds ()
    {
        return getNodeBounds ( getSelectedNode () );
    }

    /**
     * Returns {@link MutableTreeNode} bounds.
     *
     * @param node {@link MutableTreeNode} to retrieve bounds for
     * @return {@link MutableTreeNode} bounds
     */
    public Rectangle getNodeBounds ( final N node )
    {
        return getPathBounds ( getPathForNode ( node ) );
    }

    /**
     * Returns combined bounds for the {@link List} of {@link MutableTreeNode}s.
     *
     * @param nodes {@link List} of {@link MutableTreeNode}s to combine bounds for
     * @return combined bounds for the {@link List} of {@link MutableTreeNode}s
     */
    public Rectangle getNodeBounds ( final List<N> nodes )
    {
        if ( nodes == null || nodes.size () == 0 )
        {
            return null;
        }
        else
        {
            Rectangle combined = null;
            for ( final N node : nodes )
            {
                combined = GeometryUtils.getContainingRect ( combined, getNodeBounds ( node ) );
            }
            return combined;
        }
    }

    /**
     * Returns row for the specified {@link MutableTreeNode}.
     *
     * @param node {@link MutableTreeNode} to find row for
     * @return row for the specified {@link MutableTreeNode}
     */
    public int getRowForNode ( final N node )
    {
        return getRowForPath ( getPathForNode ( node ) );
    }

    /**
     * Returns {@link MutableTreeNode} for the specified row.
     *
     * @param row row to look for {@link MutableTreeNode} at
     * @return {@link MutableTreeNode} for the specified row
     */
    public N getNodeForRow ( final int row )
    {
        return getNodeForPath ( getPathForRow ( row ) );
    }

    /**
     * Returns {@link TreePath} for specified {@link MutableTreeNode}.
     *
     * @param node {@link MutableTreeNode} to retrieve {@link TreePath} for
     * @return {@link TreePath} for specified {@link MutableTreeNode}
     */
    public TreePath getPathForNode ( final N node )
    {
        return node != null ? new TreePath ( TreeUtils.getPath ( node ) ) : null;
    }

    /**
     * Returns {@link MutableTreeNode} for specified {@link TreePath}.
     *
     * @param path {@link TreePath} to retrieve {@link MutableTreeNode} for
     * @return {@link MutableTreeNode} for specified {@link TreePath}
     */
    public N getNodeForPath ( final TreePath path )
    {
        return path != null ? ( N ) path.getLastPathComponent () : null;
    }

    /**
     * Returns {@link MutableTreeNode} at the exact location.
     * Could return {@code null} if there is no {@link MutableTreeNode} at the specified location.
     * Could also return {@code null} if there is no {@link TreeModel} or nothing is viewable.
     *
     * @param location location to process
     * @return {@link MutableTreeNode} at the exact location
     */
    public N getNodeForLocation ( final Point location )
    {
        return getNodeForLocation ( location.x, location.y );
    }

    /**
     * Returns {@link MutableTreeNode} at the exact location.
     * Could return {@code null} if there is no {@link MutableTreeNode} at the specified location.
     * Could also return {@code null} if there is no {@link TreeModel} or nothing is viewable.
     *
     * @param x location X coordinate
     * @param y location Y coordinate
     * @return {@link MutableTreeNode} at the exact location
     */
    public N getNodeForLocation ( final int x, final int y )
    {
        return getNodeForPath ( getPathForLocation ( x, y ) );
    }

    /**
     * Returns {@link TreePath} for the {@link MutableTreeNode} at the exact location.
     * Could return {@code null} if there is no {@link MutableTreeNode} at the specified location.
     * Could also return {@code null} if there is no {@link TreeModel} or nothing is viewable.
     *
     * @param location location to process
     * @return {@link TreePath} for the {@link MutableTreeNode} at the exact location
     */
    public TreePath getPathForLocation ( final Point location )
    {
        return getPathForLocation ( location.x, location.y );
    }

    /**
     * Returns {@link MutableTreeNode} closest to the specified location.
     *
     * @param location location to process
     * @return {@link MutableTreeNode} closest to the specified location
     */
    public N getClosestNodeForLocation ( final Point location )
    {
        return getClosestNodeForLocation ( location.x, location.y );
    }

    /**
     * Returns {@link MutableTreeNode} closest to the specified location.
     *
     * @param x location X coordinate
     * @param y location Y coordinate
     * @return {@link MutableTreeNode} closest to the specified location
     */
    public N getClosestNodeForLocation ( final int x, final int y )
    {
        return getNodeForPath ( getClosestPathForLocation ( x, y ) );
    }

    /**
     * Returns {@link TreePath} for the {@link MutableTreeNode} closest to the specified location.
     * Could return {@code null} if there is no {@link TreeModel} or nothing is viewable.
     *
     * @param location location to process
     * @return {@link TreePath} for the {@link MutableTreeNode} closest to the specified location
     */
    public TreePath getClosestPathForLocation ( final Point location )
    {
        return getClosestPathForLocation ( location.x, location.y );
    }

    /**
     * Returns whether specified {@link MutableTreeNode} is selected or not.
     *
     * @param node {@link MutableTreeNode} to check
     * @return {@code true} if specified {@link MutableTreeNode} is selected, {@code false} otherwise
     */
    public boolean isSelected ( final N node )
    {
        return isPathSelected ( getPathForNode ( node ) );
    }

    /**
     * Returns selected {@link MutableTreeNode}.
     *
     * @return selected {@link MutableTreeNode}
     */
    public N getSelectedNode ()
    {
        return getNodeForPath ( getSelectionPath () );
    }

    /**
     * Returns {@link List} of all selected {@link MutableTreeNode}s.
     *
     * @return {@link List} of all selected {@link MutableTreeNode}s
     * @see NodesAcceptPolicy#all
     */
    public List<N> getSelectedNodes ()
    {
        return getSelectedNodes ( NodesAcceptPolicy.all );
    }

    /**
     * Returns {@link List} of selected {@link MutableTreeNode}s filtered by {@link NodesAcceptPolicy}.
     *
     * @param policy {@link NodesAcceptPolicy} used for filtering {@link MutableTreeNode}s
     * @return {@link List} of selected {@link MutableTreeNode}s filtered by {@link NodesAcceptPolicy}
     * @see NodesAcceptPolicy
     */
    public List<N> getSelectedNodes ( final NodesAcceptPolicy policy )
    {
        final TreePath[] selectionPaths = getSelectionPaths ();
        final List<N> selectedNodes;
        if ( selectionPaths != null )
        {
            selectedNodes = new ArrayList<N> ( selectionPaths.length );
            for ( final TreePath path : selectionPaths )
            {
                selectedNodes.add ( getNodeForPath ( path ) );
            }
        }
        else
        {
            selectedNodes = new ArrayList<N> ();
        }
        if ( policy != null )
        {
            policy.filter ( this, selectedNodes );
        }
        return selectedNodes;
    }

    /**
     * Returns {@link List} of selected {@link MutableTreeNode}s which are currently within {@link #getVisibleRect()} of this tree.
     * This will include {@link MutableTreeNode}s that are fully visible and those that are partially visible as well.
     *
     * @return {@link List} of selected {@link MutableTreeNode}s which are currently within {@link #getVisibleRect()} of this tree
     */
    public List<N> getVisibleSelectedNodes ()
    {
        final List<N> selectedNodes = getSelectedNodes ();
        final Rectangle vr = getVisibleRect ();
        final Iterator<N> iterator = selectedNodes.iterator ();
        while ( iterator.hasNext () )
        {
            final N node = iterator.next ();
            if ( !vr.intersects ( getNodeBounds ( node ) ) )
            {
                iterator.remove ();
            }
        }
        return selectedNodes;
    }

    /**
     * Returns user object extracted from the selected {@link MutableTreeNode}.
     *
     * @return user object extracted from the selected {@link MutableTreeNode}
     */
    public <U> U getSelectedUserObject ()
    {
        return getUserObject ( getSelectedNode () );
    }

    /**
     * Returns {@link List} of user objects extracted from all selected {@link MutableTreeNode}s.
     *
     * @return {@link List} of user objects extracted from all selected {@link MutableTreeNode}s
     * @see NodesAcceptPolicy#all
     */
    public <U> List<U> getSelectedUserObjects ()
    {
        return getSelectedUserObjects ( NodesAcceptPolicy.all );
    }

    /**
     * Returns {@link List} of user objects extracted from selected {@link MutableTreeNode}s filtered by {@link NodesAcceptPolicy}.
     *
     * @param policy {@link NodesAcceptPolicy} used for filtering {@link MutableTreeNode}s
     * @return {@link List} of user objects extracted from selected {@link MutableTreeNode}s filtered by {@link NodesAcceptPolicy}
     * @see NodesAcceptPolicy
     */
    public <U> List<U> getSelectedUserObjects ( final NodesAcceptPolicy policy )
    {
        final List<N> selectedNodes = getSelectedNodes ( policy );
        final List<U> selectedUserObjects = new ArrayList<U> ( selectedNodes.size () );
        for ( final N selectedNode : selectedNodes )
        {
            selectedUserObjects.add ( ( U ) getUserObject ( selectedNode ) );
        }
        return selectedUserObjects;
    }

    /**
     * Returns user object extracted from the specified {@link MutableTreeNode}.
     * Unfortunately {@code node.getUserObject()} method is not available in {@link TreeNode} or {@link MutableTreeNode} interfaces,
     * that is why we cannot rely on node having this method and have to manually check node type and extract user object.
     * Although this method can be overridden to provide more options for user object extraction from the {@link MutableTreeNode}.
     *
     * @param node {@link MutableTreeNode} to extract user object from
     * @param <U>  user object type
     * @return user object extracted from the specified {@link MutableTreeNode}
     */
    protected <U> U getUserObject ( final N node )
    {
        final U selectedUserObject;
        if ( node instanceof WebTreeNode )
        {
            selectedUserObject = ( U ) ( ( WebTreeNode ) node ).getUserObject ();
        }
        else if ( node instanceof DefaultMutableTreeNode )
        {
            selectedUserObject = ( U ) ( ( DefaultMutableTreeNode ) node ).getUserObject ();
        }
        else
        {
            selectedUserObject = null;
        }
        return selectedUserObject;
    }

    /**
     * Selects {@link MutableTreeNode} under the specified point.
     *
     * @param point point to look for {@link MutableTreeNode}
     */
    public void selectNodeUnderPoint ( final Point point )
    {
        selectNodeUnderPoint ( point.x, point.y );
    }

    /**
     * Selects {@link MutableTreeNode} under the specified point.
     *
     * @param x point X coordinate
     * @param y point Y coordinate
     */
    public void selectNodeUnderPoint ( final int x, final int y )
    {
        setSelectionPath ( getPathForLocation ( x, y ) );
    }

    /**
     * Sets specified {@link MutableTreeNode} as selected.
     * Any other selected {@link MutableTreeNode}s will be deselected.
     *
     * @param node {@link MutableTreeNode} to select
     */
    public void setSelectedNode ( final N node )
    {
        final TreePath path = getPathForNode ( node );
        if ( path != null )
        {
            setSelectionPath ( path );
        }
    }

    /**
     * Sets specified {@link List} of {@link MutableTreeNode}s as selected.
     * Any other selected {@link MutableTreeNode}s will be deselected.
     *
     * @param nodes {@link List} of {@link MutableTreeNode}s to select
     */
    public void setSelectedNodes ( final List<N> nodes )
    {
        final TreePath[] paths = new TreePath[ nodes.size () ];
        for ( int i = 0; i < nodes.size (); i++ )
        {
            paths[ i ] = getPathForNode ( nodes.get ( i ) );
        }
        setSelectionPaths ( paths );
    }

    /**
     * Sets specified {@link MutableTreeNode}s as selected.
     * Any other selected {@link MutableTreeNode}s will be deselected.
     *
     * @param nodes {@link MutableTreeNode}s to select
     */
    public void setSelectedNodes ( final N[] nodes )
    {
        final TreePath[] paths = new TreePath[ nodes.length ];
        for ( int i = 0; i < nodes.length; i++ )
        {
            paths[ i ] = getPathForNode ( nodes[ i ] );
        }
        setSelectionPaths ( paths );
    }

    /**
     * Returns first visible leaf {@link MutableTreeNode} from the top of the tree.
     * This doesn't include {@link MutableTreeNode}s under collapsed paths.
     * This does include {@link MutableTreeNode}s which are not in visible rect.
     *
     * @return first visible leaf {@link MutableTreeNode} from the top of the tree
     */
    public N getFirstVisibleLeafNode ()
    {
        for ( int i = 0; i < getRowCount (); i++ )
        {
            final N node = getNodeForRow ( i );
            if ( getModel ().isLeaf ( node ) )
            {
                return node;
            }
        }
        return null;
    }

    /**
     * Selects first visible leaf {@link MutableTreeNode} from the top of the tree.
     */
    public void selectFirstVisibleLeafNode ()
    {
        final N node = getFirstVisibleLeafNode ();
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
     * Returns root {@link MutableTreeNode}.
     *
     * @return root {@link MutableTreeNode}
     */
    public N getRootNode ()
    {
        return ( N ) getModel ().getRoot ();
    }

    /**
     * Returns {@link List} of all {@link MutableTreeNode}s available in this tree.
     *
     * @return {@link List} of all {@link MutableTreeNode}s available in this tree
     */
    public List<N> getAvailableNodes ()
    {
        return getAvailableNodes ( getRootNode () );
    }

    /**
     * Returns {@link List} of all {@link MutableTreeNode}s available under the specified {@link MutableTreeNode} including that node.
     *
     * @param parent {@link MutableTreeNode} to collect nodes for
     * @return {@link List} of all {@link MutableTreeNode}s available under the specified {@link MutableTreeNode} including that node
     */
    public List<N> getAvailableNodes ( final N parent )
    {
        final List<N> nodes = new ArrayList<N> ();
        getAllNodesImpl ( nodes, getRootNode () );
        return nodes;
    }

    /**
     * Collects {@link List} of all {@link MutableTreeNode}s available under the specified {@link MutableTreeNode} including that node.
     *
     * @param nodes  {@link List} into which all {@link MutableTreeNode}s should be collected
     * @param parent {@link MutableTreeNode} to start collecting from
     */
    private void getAllNodesImpl ( final List<N> nodes, final N parent )
    {
        nodes.add ( parent );
        for ( int i = 0; i < parent.getChildCount (); i++ )
        {
            getAllNodesImpl ( nodes, ( N ) parent.getChildAt ( i ) );
        }
    }

    /**
     * Returns tree selection mode.
     *
     * @return tree selection mode
     * @see TreeSelectionModel#SINGLE_TREE_SELECTION
     * @see TreeSelectionModel#CONTIGUOUS_TREE_SELECTION
     * @see TreeSelectionModel#DISCONTIGUOUS_TREE_SELECTION
     */
    public int getSelectionMode ()
    {
        return getSelectionModel ().getSelectionMode ();
    }

    /**
     * Sets tree selection mode.
     *
     * @param mode tree selection mode
     * @see TreeSelectionModel#SINGLE_TREE_SELECTION
     * @see TreeSelectionModel#CONTIGUOUS_TREE_SELECTION
     * @see TreeSelectionModel#DISCONTIGUOUS_TREE_SELECTION
     */
    public void setSelectionMode ( final int mode )
    {
        getSelectionModel ().setSelectionMode ( mode );
    }

    /**
     * Sets whether or not multiple nodes selection is allowed.
     *
     * @return {@code true} if multiple nodes selection is allowed, {@code false} otherwise
     * @see #getSelectionMode()
     */
    public boolean isMultipleSelectionAllowed ()
    {
        return Objects.equals ( getSelectionMode (), CONTIGUOUS_TREE_SELECTION, DISCONTIGUOUS_TREE_SELECTION );
    }

    /**
     * Sets whether or not multiple nodes selection is allowed.
     *
     * @param allowed whether or not multiple nodes selection is allowed
     * @see #setSelectionMode(int)
     */
    public void setMultipleSelectionAllowed ( final boolean allowed )
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
     * Sets whether or not tree should automatically scroll to selected {@link MutableTreeNode}s.
     *
     * @param scroll whether or not tree should automatically scroll to selected {@link MutableTreeNode}s
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
     * Scrolls tree view to specified {@link MutableTreeNode}.
     *
     * @param node {@link MutableTreeNode} to scroll tree view to
     */
    public void scrollToNode ( final N node )
    {
        scrollToNode ( node, false );
    }

    /**
     * Scrolls tree view to specified {@link MutableTreeNode}.
     *
     * @param node     {@link MutableTreeNode} to scroll tree view to
     * @param centered whether or not should vertically center specified {@link MutableTreeNode} in view bounds
     */
    public void scrollToNode ( final N node, final boolean centered )
    {
        final Rectangle nodeBounds = getNodeBounds ( node );
        if ( nodeBounds != null )
        {
            if ( node.getParent () != null )
            {
                final int indent = ( getUI ().getLeftChildIndent () + getUI ().getRightChildIndent () ) * 2;
                nodeBounds.x -= indent;
                nodeBounds.width += indent;
            }
            final Dimension visibleBounds = getVisibleRect ().getSize ();
            if ( nodeBounds.width > visibleBounds.width )
            {
                nodeBounds.width = visibleBounds.width;
            }
            if ( centered )
            {
                nodeBounds.y = nodeBounds.y + nodeBounds.height / 2 - visibleBounds.height / 2;
                nodeBounds.height = visibleBounds.height;
            }
            scrollRectToVisible ( nodeBounds );
        }
    }

    /**
     * Starts editing selected {@link MutableTreeNode}.
     */
    public void startEditingSelectedNode ()
    {
        startEditingNode ( getSelectedNode () );
    }

    /**
     * Starts editing specified {@link MutableTreeNode}.
     *
     * @param node {@link MutableTreeNode} to edit
     */
    public void startEditingNode ( final N node )
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
     * Forces specified {@link MutableTreeNode} view update.
     * This can be used to update {@link MutableTreeNode} renderer size/view if data it is based on has changed.
     *
     * @param node {@link MutableTreeNode} to update view for
     */
    public void updateNode ( final N node )
    {
        final TreeModel model = getModel ();
        if ( model instanceof WebTreeModel )
        {
            ( ( WebTreeModel ) getModel () ).updateNode ( node );
        }
    }

    /**
     * Forces specified {@link MutableTreeNode}s view update.
     * This can be used to update {@link MutableTreeNode}s renderer size/view if data it is based on has changed.
     *
     * @param nodes {@link MutableTreeNode}s to update view for
     */
    public void updateNodes ( final N... nodes )
    {
        final TreeModel model = getModel ();
        if ( model instanceof WebTreeModel )
        {
            ( ( WebTreeModel ) model ).updateNodes ( nodes );
        }
    }

    /**
     * Forces tree nodes to be updated.
     * This can be used to update nodes sizes/view if renderer has changed.
     *
     * @param nodes tree nodes to be updated
     */
    public void updateNodes ( final List<N> nodes )
    {
        final TreeModel model = getModel ();
        if ( model instanceof WebTreeModel )
        {
            ( ( WebTreeModel ) model ).updateNodes ( nodes );
        }
    }

    /**
     * Updates all nodes visible in the tree.
     * This includes nodes which are off screen (hidden behind the scroll).
     * This can be used to update nodes sizes/view if renderer has changed.
     */
    public void updateVisibleNodes ()
    {
        final int rows = getRowCount ();
        final List<N> nodes = new ArrayList<N> ( rows );
        for ( int i = 0; i < rows; i++ )
        {
            nodes.add ( getNodeForRow ( i ) );
        }
        updateNodes ( nodes );
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
     * Returns tree expansion and selection states.
     * Tree nodes must be instances of UniqueNode class.
     *
     * @param node node to save state for
     * @return tree expansion and selection states
     */
    public TreeState getTreeState ( final N node )
    {
        return TreeUtils.getTreeState ( this, node );
    }

    /**
     * Returns tree expansion and selection states.
     * Tree nodes must be instances of UniqueNode class.
     *
     * @param node          node to save state for
     * @param saveSelection whether to save selection states or not
     * @return tree expansion and selection states
     */
    public TreeState getTreeState ( final N node, final boolean saveSelection )
    {
        return TreeUtils.getTreeState ( this, node, saveSelection );
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
     * Restores tree expansion and selection states.
     * Tree nodes must be instances of UniqueNode class.
     *
     * @param treeState tree expansion and selection states
     * @param node      node to restore state for
     */
    public void setTreeState ( final TreeState treeState, final N node )
    {
        TreeUtils.setTreeState ( this, treeState, node );
    }

    /**
     * Restores tree expansion and selection states.
     * Tree nodes must be instances of UniqueNode class.
     *
     * @param treeState        tree expansion and selection states
     * @param node             node to restore state for
     * @param restoreSelection whether to restore selection states or not
     */
    public void setTreeState ( final TreeState treeState, final N node, final boolean restoreSelection )
    {
        TreeUtils.setTreeState ( this, treeState, node, restoreSelection );
    }

    /**
     * Returns tree selection style.
     *
     * @return tree selection style
     */
    public TreeSelectionStyle getSelectionStyle ()
    {
        return getUI ().getSelectionStyle ();
    }

    /**
     * Sets tree selection style.
     *
     * @param style tree selection style
     */
    public void setSelectionStyle ( final TreeSelectionStyle style )
    {
        getUI ().setSelectionStyle ( style );
    }

    /**
     * Returns whether tree should auto-expand nodes on selection or not.
     *
     * @return true if tree should auto-expand nodes on selection, false otherwise
     */
    public boolean isExpandSelected ()
    {
        return TreeSelectionExpandBehavior.isInstalled ( this );
    }

    /**
     * Sets whether tree should auto-expand nodes on selection or not.
     *
     * @param expand whether tree should auto-expand nodes on selection or not
     */
    public void setExpandSelected ( final boolean expand )
    {
        if ( expand )
        {
            if ( !isExpandSelected () )
            {
                TreeSelectionExpandBehavior.install ( this );
            }
        }
        else
        {
            if ( isExpandSelected () )
            {
                TreeSelectionExpandBehavior.uninstall ( this );
            }
        }
    }

    /**
     * Returns whether tree should auto-expand single child nodes or not.
     * If set to true when any node is expanded and there is only one single child node in it - it will be automatically expanded.
     *
     * @return true if tree should auto-expand single child nodes, false otherwise
     */
    public boolean isAutoExpandSingleChildNode ()
    {
        return TreeSingleChildExpandBehavior.isInstalled ( this );
    }

    /**
     * Sets whether tree should auto-expand single child nodes or not.
     * If set to true when any node is expanded and there is only one single child node in it - it will be automatically expanded.
     *
     * @param expand whether tree should auto-expand single child nodes or not
     */
    public void setAutoExpandSingleChildNode ( final boolean expand )
    {
        if ( expand )
        {
            if ( !isAutoExpandSingleChildNode () )
            {
                TreeSingleChildExpandBehavior.install ( this );
            }
        }
        else
        {
            if ( isAutoExpandSingleChildNode () )
            {
                TreeSingleChildExpandBehavior.uninstall ( this );
            }
        }
    }

    /**
     * Returns whether or not nodes should be selected on hover.
     *
     * @return true if nodes should be selected on hover, false otherwise
     */
    public boolean isSelectOnHover ()
    {
        return TreeHoverSelectionBehavior.isInstalled ( this );
    }

    /**
     * Sets whether or not nodes should be selected on hover.
     *
     * @param select whether or not nodes should be selected on hover
     */
    public void setSelectOnHover ( final boolean select )
    {
        if ( select )
        {
            if ( !isSelectOnHover () )
            {
                TreeHoverSelectionBehavior.install ( this );
            }
        }
        else
        {
            if ( isSelectOnHover () )
            {
                TreeHoverSelectionBehavior.uninstall ( this );
            }
        }
    }

    @Override
    public StyleId getDefaultStyleId ()
    {
        return StyleId.tree;
    }

    @Override
    public StyleId getStyleId ()
    {
        return StyleManager.getStyleId ( this );
    }

    @Override
    public StyleId setStyleId ( final StyleId id )
    {
        return StyleManager.setStyleId ( this, id );
    }

    @Override
    public StyleId resetStyleId ()
    {
        return StyleManager.resetStyleId ( this );
    }

    @Override
    public Skin getSkin ()
    {
        return StyleManager.getSkin ( this );
    }

    @Override
    public Skin setSkin ( final Skin skin )
    {
        return StyleManager.setSkin ( this, skin );
    }

    @Override
    public Skin setSkin ( final Skin skin, final boolean recursively )
    {
        return StyleManager.setSkin ( this, skin, recursively );
    }

    @Override
    public Skin resetSkin ()
    {
        return StyleManager.resetSkin ( this );
    }

    @Override
    public void addStyleListener ( final StyleListener listener )
    {
        StyleManager.addStyleListener ( this, listener );
    }

    @Override
    public void removeStyleListener ( final StyleListener listener )
    {
        StyleManager.removeStyleListener ( this, listener );
    }

    @Override
    public Painter getCustomPainter ()
    {
        return StyleManager.getCustomPainter ( this );
    }

    @Override
    public Painter setCustomPainter ( final Painter painter )
    {
        return StyleManager.setCustomPainter ( this, painter );
    }

    @Override
    public boolean resetCustomPainter ()
    {
        return StyleManager.resetCustomPainter ( this );
    }

    @Override
    public Shape getShape ()
    {
        return ShapeMethodsImpl.getShape ( this );
    }

    @Override
    public boolean isShapeDetectionEnabled ()
    {
        return ShapeMethodsImpl.isShapeDetectionEnabled ( this );
    }

    @Override
    public void setShapeDetectionEnabled ( final boolean enabled )
    {
        ShapeMethodsImpl.setShapeDetectionEnabled ( this, enabled );
    }

    @Override
    public Insets getMargin ()
    {
        return MarginMethodsImpl.getMargin ( this );
    }

    @Override
    public void setMargin ( final int margin )
    {
        MarginMethodsImpl.setMargin ( this, margin );
    }

    @Override
    public void setMargin ( final int top, final int left, final int bottom, final int right )
    {
        MarginMethodsImpl.setMargin ( this, top, left, bottom, right );
    }

    @Override
    public void setMargin ( final Insets margin )
    {
        MarginMethodsImpl.setMargin ( this, margin );
    }

    @Override
    public Insets getPadding ()
    {
        return PaddingMethodsImpl.getPadding ( this );
    }

    @Override
    public void setPadding ( final int padding )
    {
        PaddingMethodsImpl.setPadding ( this, padding );
    }

    @Override
    public void setPadding ( final int top, final int left, final int bottom, final int right )
    {
        PaddingMethodsImpl.setPadding ( this, top, left, bottom, right );
    }

    @Override
    public void setPadding ( final Insets padding )
    {
        PaddingMethodsImpl.setPadding ( this, padding );
    }

    /**
     * Adds hover listener.
     *
     * @param listener hover listener to add
     */
    public void addHoverListener ( final HoverListener<N> listener )
    {
        listenerList.add ( HoverListener.class, listener );
    }

    /**
     * Removes hover listener.
     *
     * @param listener hover listener to remove
     */
    public void removeHoverListener ( final HoverListener<N> listener )
    {
        listenerList.remove ( HoverListener.class, listener );
    }

    /**
     * Returns hover listeners.
     *
     * @return hover listeners
     */
    public HoverListener[] getHoverListeners ()
    {
        return listenerList.getListeners ( HoverListener.class );
    }

    /**
     * Informs about hover node change.
     *
     * @param previous previous hover node
     * @param current  current hover node
     */
    public void fireHoverChanged ( final N previous, final N current )
    {
        for ( final HoverListener listener : getHoverListeners () )
        {
            listener.hoverChanged ( previous, current );
        }
    }

    @Override
    public int getScrollableUnitIncrement ( final Rectangle visibleRect, final int orientation, final int direction )
    {
        int increment = super.getScrollableUnitIncrement ( visibleRect, orientation, direction );

        // Minor fix for Swing JTree scrollable issue
        // Without this we will always scroll to first row bounds, but will never get to actual zero Y on visible rect
        // This will ensure to add top insets to the increment in case we are scrolling the tree up and we got to first node
        if ( orientation == SwingConstants.VERTICAL && direction < 0 )
        {
            final Insets i = getInsets ();
            if ( visibleRect.y - increment == i.top )
            {
                increment += i.top;
            }
        }

        return increment;
    }

    /**
     * Repaints specified tree row.
     *
     * @param row row index
     */
    public void repaint ( final int row )
    {
        repaint ( getUI ().getRowBounds ( row ) );
    }

    /**
     * Repaints all tree rows in specified range.
     *
     * @param from first row index
     * @param to   last row index
     */
    public void repaint ( final int from, final int to )
    {
        final WTreeUI ui = getUI ();
        final Rectangle fromBounds = ui.getRowBounds ( from );
        final Rectangle toBounds = ui.getRowBounds ( to );
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
    public void repaint ( final N node )
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
    public void repaint ( final List<N> nodes )
    {
        if ( nodes != null && nodes.size () > 0 )
        {
            Rectangle summ = null;
            for ( final N node : nodes )
            {
                summ = GeometryUtils.getContainingRect ( summ, getNodeBounds ( node ) );
            }
            if ( summ != null )
            {
                repaint ( summ );
            }
        }
    }

    @Override
    public MouseAdapter onNodeDoubleClick ( final TreeNodeEventRunnable<N> runnable )
    {
        return TreeEventMethodsImpl.onNodeDoubleClick ( this, runnable );
    }

    @Override
    public MouseAdapter onNodeDoubleClick ( final Predicate<N> condition, final TreeNodeEventRunnable<N> runnable )
    {
        return TreeEventMethodsImpl.onNodeDoubleClick ( this, condition, runnable );
    }

    @Override
    public MouseAdapter onMousePress ( final MouseEventRunnable runnable )
    {
        return EventMethodsImpl.onMousePress ( this, runnable );
    }

    @Override
    public MouseAdapter onMousePress ( final MouseButton mouseButton, final MouseEventRunnable runnable )
    {
        return EventMethodsImpl.onMousePress ( this, mouseButton, runnable );
    }

    @Override
    public MouseAdapter onMouseEnter ( final MouseEventRunnable runnable )
    {
        return EventMethodsImpl.onMouseEnter ( this, runnable );
    }

    @Override
    public MouseAdapter onMouseExit ( final MouseEventRunnable runnable )
    {
        return EventMethodsImpl.onMouseExit ( this, runnable );
    }

    @Override
    public MouseAdapter onMouseDrag ( final MouseEventRunnable runnable )
    {
        return EventMethodsImpl.onMouseDrag ( this, runnable );
    }

    @Override
    public MouseAdapter onMouseDrag ( final MouseButton mouseButton, final MouseEventRunnable runnable )
    {
        return EventMethodsImpl.onMouseDrag ( this, mouseButton, runnable );
    }

    @Override
    public MouseAdapter onMouseClick ( final MouseEventRunnable runnable )
    {
        return EventMethodsImpl.onMouseClick ( this, runnable );
    }

    @Override
    public MouseAdapter onMouseClick ( final MouseButton mouseButton, final MouseEventRunnable runnable )
    {
        return EventMethodsImpl.onMouseClick ( this, mouseButton, runnable );
    }

    @Override
    public MouseAdapter onDoubleClick ( final MouseEventRunnable runnable )
    {
        return EventMethodsImpl.onDoubleClick ( this, runnable );
    }

    @Override
    public MouseAdapter onMenuTrigger ( final MouseEventRunnable runnable )
    {
        return EventMethodsImpl.onMenuTrigger ( this, runnable );
    }

    @Override
    public KeyAdapter onKeyType ( final KeyEventRunnable runnable )
    {
        return EventMethodsImpl.onKeyType ( this, runnable );
    }

    @Override
    public KeyAdapter onKeyType ( final HotkeyData hotkey, final KeyEventRunnable runnable )
    {
        return EventMethodsImpl.onKeyType ( this, hotkey, runnable );
    }

    @Override
    public KeyAdapter onKeyPress ( final KeyEventRunnable runnable )
    {
        return EventMethodsImpl.onKeyPress ( this, runnable );
    }

    @Override
    public KeyAdapter onKeyPress ( final HotkeyData hotkey, final KeyEventRunnable runnable )
    {
        return EventMethodsImpl.onKeyPress ( this, hotkey, runnable );
    }

    @Override
    public KeyAdapter onKeyRelease ( final KeyEventRunnable runnable )
    {
        return EventMethodsImpl.onKeyRelease ( this, runnable );
    }

    @Override
    public KeyAdapter onKeyRelease ( final HotkeyData hotkey, final KeyEventRunnable runnable )
    {
        return EventMethodsImpl.onKeyRelease ( this, hotkey, runnable );
    }

    @Override
    public FocusAdapter onFocusGain ( final FocusEventRunnable runnable )
    {
        return EventMethodsImpl.onFocusGain ( this, runnable );
    }

    @Override
    public FocusAdapter onFocusLoss ( final FocusEventRunnable runnable )
    {
        return EventMethodsImpl.onFocusLoss ( this, runnable );
    }

    @Override
    public MouseAdapter onDragStart ( final int shift, final MouseEventRunnable runnable )
    {
        return EventMethodsImpl.onDragStart ( this, shift, runnable );
    }

    @Override
    public MouseAdapter onDragStart ( final int shift, final MouseButton mouseButton, final MouseEventRunnable runnable )
    {
        return EventMethodsImpl.onDragStart ( this, shift, mouseButton, runnable );
    }

    @Override
    public void addLanguageListener ( final LanguageListener listener )
    {
        UILanguageManager.addLanguageListener ( this, listener );
    }

    @Override
    public void removeLanguageListener ( final LanguageListener listener )
    {
        UILanguageManager.removeLanguageListener ( this, listener );
    }

    @Override
    public void removeLanguageListeners ()
    {
        UILanguageManager.removeLanguageListeners ( this );
    }

    @Override
    public void addDictionaryListener ( final DictionaryListener listener )
    {
        UILanguageManager.addDictionaryListener ( this, listener );
    }

    @Override
    public void removeDictionaryListener ( final DictionaryListener listener )
    {
        UILanguageManager.removeDictionaryListener ( this, listener );
    }

    @Override
    public void removeDictionaryListeners ()
    {
        UILanguageManager.removeDictionaryListeners ( this );
    }

    @Override
    public void registerSettings ( final Configuration configuration )
    {
        UISettingsManager.registerComponent ( this, configuration );
    }

    @Override
    public void registerSettings ( final SettingsProcessor processor )
    {
        UISettingsManager.registerComponent ( this, processor );
    }

    @Override
    public void unregisterSettings ()
    {
        UISettingsManager.unregisterComponent ( this );
    }

    @Override
    public void loadSettings ()
    {
        UISettingsManager.loadSettings ( this );
    }

    @Override
    public void saveSettings ()
    {
        UISettingsManager.saveSettings ( this );
    }

    @Override
    public WebTree<N> setPlainFont ()
    {
        return FontMethodsImpl.setPlainFont ( this );
    }

    @Override
    public WebTree<N> setPlainFont ( final boolean apply )
    {
        return FontMethodsImpl.setPlainFont ( this, apply );
    }

    @Override
    public boolean isPlainFont ()
    {
        return FontMethodsImpl.isPlainFont ( this );
    }

    @Override
    public WebTree<N> setBoldFont ()
    {
        return FontMethodsImpl.setBoldFont ( this );
    }

    @Override
    public WebTree<N> setBoldFont ( final boolean apply )
    {
        return FontMethodsImpl.setBoldFont ( this, apply );
    }

    @Override
    public boolean isBoldFont ()
    {
        return FontMethodsImpl.isBoldFont ( this );
    }

    @Override
    public WebTree<N> setItalicFont ()
    {
        return FontMethodsImpl.setItalicFont ( this );
    }

    @Override
    public WebTree<N> setItalicFont ( final boolean apply )
    {
        return FontMethodsImpl.setItalicFont ( this, apply );
    }

    @Override
    public boolean isItalicFont ()
    {
        return FontMethodsImpl.isItalicFont ( this );
    }

    @Override
    public WebTree<N> setFontStyle ( final boolean bold, final boolean italic )
    {
        return FontMethodsImpl.setFontStyle ( this, bold, italic );
    }

    @Override
    public WebTree<N> setFontStyle ( final int style )
    {
        return FontMethodsImpl.setFontStyle ( this, style );
    }

    @Override
    public WebTree<N> setFontSize ( final int fontSize )
    {
        return FontMethodsImpl.setFontSize ( this, fontSize );
    }

    @Override
    public WebTree<N> changeFontSize ( final int change )
    {
        return FontMethodsImpl.changeFontSize ( this, change );
    }

    @Override
    public int getFontSize ()
    {
        return FontMethodsImpl.getFontSize ( this );
    }

    @Override
    public WebTree<N> setFontSizeAndStyle ( final int fontSize, final boolean bold, final boolean italic )
    {
        return FontMethodsImpl.setFontSizeAndStyle ( this, fontSize, bold, italic );
    }

    @Override
    public WebTree<N> setFontSizeAndStyle ( final int fontSize, final int style )
    {
        return FontMethodsImpl.setFontSizeAndStyle ( this, fontSize, style );
    }

    @Override
    public WebTree<N> setFontName ( final String fontName )
    {
        return FontMethodsImpl.setFontName ( this, fontName );
    }

    @Override
    public String getFontName ()
    {
        return FontMethodsImpl.getFontName ( this );
    }

    @Override
    public int getPreferredWidth ()
    {
        return SizeMethodsImpl.getPreferredWidth ( this );
    }

    @Override
    public WebTree<N> setPreferredWidth ( final int preferredWidth )
    {
        return SizeMethodsImpl.setPreferredWidth ( this, preferredWidth );
    }

    @Override
    public int getPreferredHeight ()
    {
        return SizeMethodsImpl.getPreferredHeight ( this );
    }

    @Override
    public WebTree<N> setPreferredHeight ( final int preferredHeight )
    {
        return SizeMethodsImpl.setPreferredHeight ( this, preferredHeight );
    }

    @Override
    public int getMinimumWidth ()
    {
        return SizeMethodsImpl.getMinimumWidth ( this );
    }

    @Override
    public WebTree<N> setMinimumWidth ( final int minimumWidth )
    {
        return SizeMethodsImpl.setMinimumWidth ( this, minimumWidth );
    }

    @Override
    public int getMinimumHeight ()
    {
        return SizeMethodsImpl.getMinimumHeight ( this );
    }

    @Override
    public WebTree<N> setMinimumHeight ( final int minimumHeight )
    {
        return SizeMethodsImpl.setMinimumHeight ( this, minimumHeight );
    }

    @Override
    public int getMaximumWidth ()
    {
        return SizeMethodsImpl.getMaximumWidth ( this );
    }

    @Override
    public WebTree<N> setMaximumWidth ( final int maximumWidth )
    {
        return SizeMethodsImpl.setMaximumWidth ( this, maximumWidth );
    }

    @Override
    public int getMaximumHeight ()
    {
        return SizeMethodsImpl.getMaximumHeight ( this );
    }

    @Override
    public WebTree<N> setMaximumHeight ( final int maximumHeight )
    {
        return SizeMethodsImpl.setMaximumHeight ( this, maximumHeight );
    }

    @Override
    public Dimension getPreferredSize ()
    {
        return SizeMethodsImpl.getPreferredSize ( this, super.getPreferredSize () );
    }

    @Override
    public Dimension getOriginalPreferredSize ()
    {
        return SizeMethodsImpl.getOriginalPreferredSize ( this, super.getPreferredSize () );
    }

    @Override
    public WebTree<N> setPreferredSize ( final int width, final int height )
    {
        return SizeMethodsImpl.setPreferredSize ( this, width, height );
    }

    /**
     * Returns the look and feel (LaF) object that renders this component.
     *
     * @return the {@link WTreeUI} object that renders this component
     */
    @Override
    public WTreeUI getUI ()
    {
        return ( WTreeUI ) super.getUI ();
    }

    /**
     * Sets the LaF object that renders this component.
     *
     * @param ui {@link WTreeUI}
     */
    public void setUI ( final WTreeUI ui )
    {
        super.setUI ( ui );
    }

    @Override
    public void updateUI ()
    {
        StyleManager.getDescriptor ( this ).updateUI ( this );
    }

    @Override
    public String getUIClassID ()
    {
        return StyleManager.getDescriptor ( this ).getUIClassId ();
    }

    /**
     * Returns a TreeModel wrapping the specified object.
     * If the object is an array of Object, Hashtable or Vector then a new root node is created with each of the incoming objects as
     * children. Otherwise, a new root is created with the specified object as its value.
     *
     * @param data data object used as the foundation for the TreeModel
     * @return a TreeModel wrapping the specified object
     */
    protected static TreeModel createTreeModel ( final Object data )
    {
        final DefaultMutableTreeNode root;
        if ( data instanceof Object[] || data instanceof Hashtable || data instanceof Vector )
        {
            root = new DefaultMutableTreeNode ( "root" );
            DynamicUtilTreeNode.createChildren ( root, data );
        }
        else
        {
            root = new DynamicUtilTreeNode ( "root", data );
        }
        return new WebTreeModel<DefaultMutableTreeNode> ( root, false );
    }

    /**
     * Creates and returns a sample TreeModel.
     * Used primarily for beanbuilders to show something interesting.
     *
     * @return the default TreeModel
     */
    public static TreeModel createDefaultTreeModel ()
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