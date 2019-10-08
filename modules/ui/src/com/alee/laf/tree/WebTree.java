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

import com.alee.api.annotations.NotNull;
import com.alee.api.annotations.Nullable;
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
import com.alee.utils.CollectionUtils;
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
import java.util.List;
import java.util.*;

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
     * Component properties.
     */
    @NotNull
    public static final String DROP_LOCATION = "dropLocation";

    /**
     * Client properties used for backward compatibility with Swing {@link JTree}.
     *
     * @see TreeToolTipProvider
     */
    public static final String TOOLTIP_PROVIDER_PROPERTY = "tooltipProvider";

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
    @Nullable
    protected transient TreeSelectionListener scrollToSelectionListener = null;

    /**
     * Special state provider that can be set to check whether or not specific nodes are editable.
     */
    @Nullable
    protected transient Predicate<N> editableStateProvider = null;

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
    public WebTree ( @NotNull final Object[] data )
    {
        this ( StyleId.auto, data );
    }

    /**
     * Constructs tree with model based on specified values.
     *
     * @param data tree data
     */
    public WebTree ( @NotNull final Vector<?> data )
    {
        this ( StyleId.auto, data );
    }

    /**
     * Constructs tree with model based on specified values.
     *
     * @param data tree data
     */
    public WebTree ( @NotNull final Hashtable<?, ?> data )
    {
        this ( StyleId.auto, data );
    }

    /**
     * Constructs tree with model based on specified root node.
     *
     * @param root tree root node
     */
    public WebTree ( @Nullable final N root )
    {
        this ( StyleId.auto, root );
    }

    /**
     * Constructs tree with model based on specified root node and which decides whether a node is a leaf node in the specified manner.
     *
     * @param root               tree root node
     * @param asksAllowsChildren false if any node can have children, true if each node is asked to see if it can have children
     */
    public WebTree ( @Nullable final N root, final boolean asksAllowsChildren )
    {
        this ( StyleId.auto, root, asksAllowsChildren );
    }

    /**
     * Constructs tree with specified model.
     *
     * @param newModel tree model
     */
    public WebTree ( @Nullable final TreeModel newModel )
    {
        this ( StyleId.auto, newModel );
    }

    /**
     * Constructs tree with default sample model.
     *
     * @param id style ID
     */
    public WebTree ( @NotNull final StyleId id )
    {
        this ( id, createDefaultTreeModel () );
    }

    /**
     * Constructs tree with model based on specified values.
     *
     * @param id   style ID
     * @param data tree data
     */
    public WebTree ( @NotNull final StyleId id, @NotNull final Object[] data )
    {
        this ( id, createTreeModel ( data ) );
    }

    /**
     * Constructs tree with model based on specified values.
     *
     * @param id   style ID
     * @param data tree data
     */
    public WebTree ( @NotNull final StyleId id, @NotNull final Vector<?> data )
    {
        this ( id, createTreeModel ( data ) );
    }

    /**
     * Constructs tree with model based on specified values.
     *
     * @param id   style ID
     * @param data tree data
     */
    public WebTree ( @NotNull final StyleId id, @NotNull final Hashtable<?, ?> data )
    {
        this ( id, createTreeModel ( data ) );
    }

    /**
     * Constructs tree with model based on specified root node.
     *
     * @param id   style ID
     * @param root tree root node
     */
    public WebTree ( @NotNull final StyleId id, @Nullable final N root )
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
    public WebTree ( @NotNull final StyleId id, @Nullable final N root, final boolean asksAllowsChildren )
    {
        this ( id, new WebTreeModel<N> ( root, asksAllowsChildren ) );
    }

    /**
     * Constructs tree with specified model.
     *
     * @param id       style ID
     * @param newModel tree model
     */
    public WebTree ( @NotNull final StyleId id, @Nullable final TreeModel newModel )
    {
        super ( newModel );
        setStyleId ( id );
    }

    @Override
    public void setCellEditor ( @Nullable final TreeCellEditor cellEditor )
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
    @Nullable
    @Override
    public String getToolTipText ( @Nullable final MouseEvent event )
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
                    if ( pathBounds != null )
                    {
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
    public void addCellEditorListener ( @NotNull final CellEditorListener listener )
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
    public void removeCellEditorListener ( @NotNull final CellEditorListener listener )
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
    @Nullable
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
    public void setEditableStateProvider ( @Nullable final Predicate<N> stateProvider )
    {
        this.editableStateProvider = stateProvider;
    }

    @Override
    public boolean isPathEditable ( @NotNull final TreePath path )
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
    public boolean isNodeEditable ( @NotNull final N node )
    {
        return editableStateProvider == null || editableStateProvider.test ( node );
    }

    /**
     * Returns {@link TreeToolTipProvider}.
     *
     * @return {@link TreeToolTipProvider}
     */
    @Nullable
    public TreeToolTipProvider<N> getToolTipProvider ()
    {
        return ( TreeToolTipProvider<N> ) getClientProperty ( TOOLTIP_PROVIDER_PROPERTY );
    }

    /**
     * Sets {@link TreeToolTipProvider}.
     *
     * @param provider {@link TreeToolTipProvider}
     */
    public void setToolTipProvider ( @Nullable final TreeToolTipProvider<N> provider )
    {
        putClientProperty ( TOOLTIP_PROVIDER_PROPERTY, provider );
    }

    /**
     * Expands the root path, assuming the current TreeModel has been set.
     */
    public void expandRoot ()
    {
        expandNode ( getRootNode () );
    }

    /**
     * Expands all tree nodes.
     * It is not recommended to expand large tree chunks this way since that might cause interface lags.
     */
    public void expandAll ()
    {
        expandAllImpl ( getRootNode (), null, Integer.MAX_VALUE );
    }

    /**
     * Expands all tree nodes.
     * {@link Filter} can be specified to expand only the accepted {@link MutableTreeNode}s.
     * It is not recommended to expand large tree chunks this way since that might cause interface lags.
     *
     * @param filter {@link Filter} to limit expanded {@link MutableTreeNode}s or {@code null}
     */
    public void expandAll ( @Nullable final Filter<N> filter )
    {
        expandAllImpl ( getRootNode (), filter, Integer.MAX_VALUE );
    }

    /**
     * Expands specified {@link MutableTreeNode} and all of it's child nodes.
     * It is not recommended to expand large tree chunks this way since that might cause interface lags.
     *
     * @param node {@link MutableTreeNode} to expand
     */
    public void expandAll ( @Nullable final N node )
    {
        expandAllImpl ( node, null, Integer.MAX_VALUE );
    }

    /**
     * Expands specified {@link MutableTreeNode} and all of it's child nodes.
     * {@link Filter} can be specified to expand only the accepted {@link MutableTreeNode}s.
     * It is not recommended to expand large tree chunks this way since that might cause interface lags.
     *
     * @param node   {@link MutableTreeNode} to expand
     * @param filter {@link Filter} to limit expanded {@link MutableTreeNode}s or {@code null}
     */
    public void expandAll ( @Nullable final N node, @Nullable final Filter<N> filter )
    {
        expandAllImpl ( node, filter, Integer.MAX_VALUE );
    }

    /**
     * Expands all tree nodes.
     * Specific depth value can be specified to limit expansion depth, for instance with value of {@code 1} only one level will be expanded.
     * It is not recommended to expand large tree chunks this way since that might cause interface lags.
     *
     * @param depth depth to expand until
     */
    public void expandAll ( final int depth )
    {
        expandAllImpl ( getRootNode (), null, depth );
    }

    /**
     * Expands all tree nodes.
     * {@link Filter} can be specified to expand only the accepted {@link MutableTreeNode}s.
     * Specific depth value can be specified to limit expansion depth, for instance with value of {@code 1} only one level will be expanded.
     * It is not recommended to expand large tree chunks this way since that might cause interface lags.
     *
     * @param filter {@link Filter} to limit expanded {@link MutableTreeNode}s or {@code null}
     * @param depth  depth to expand until
     */
    public void expandAll ( @Nullable final Filter<N> filter, final int depth )
    {
        expandAllImpl ( getRootNode (), filter, depth );
    }

    /**
     * Expands specified {@link MutableTreeNode} and all of it's child nodes.
     * Specific depth value can be specified to limit expansion depth, for instance with value of {@code 1} only one level will be expanded.
     * It is not recommended to expand large tree chunks this way since that might cause interface lags.
     *
     * @param node  {@link MutableTreeNode} to expand
     * @param depth depth to expand until
     */
    public void expandAll ( @Nullable final N node, final int depth )
    {
        expandAllImpl ( node, null, depth );
    }

    /**
     * Expands specified {@link MutableTreeNode} and all of it's child nodes.
     * {@link Filter} can be specified to expand only the accepted {@link MutableTreeNode}s.
     * Specific depth value can be specified to limit expansion depth, for instance with value of {@code 1} only one level will be expanded.
     * It is not recommended to expand large tree chunks this way since that might cause interface lags.
     *
     * @param node   {@link MutableTreeNode} to expand
     * @param filter {@link Filter} to limit expanded {@link MutableTreeNode}s or {@code null}
     * @param depth  depth to expand until
     */
    public void expandAll ( @Nullable final N node, @Nullable final Filter<N> filter, final int depth )
    {
        expandAllImpl ( node, filter, depth );
    }

    /**
     * Expands specified {@link MutableTreeNode} and all of it's child nodes.
     * {@link Filter} can be specified to expand only the accepted {@link MutableTreeNode}s.
     * Specific depth value can be specified to limit expansion depth, for instance with value of {@code 1} only one level will be expanded.
     *
     * @param node   {@link MutableTreeNode} to expand
     * @param filter {@link Filter} to limit expanded {@link MutableTreeNode}s or {@code null}
     * @param depth  depth to expand until
     */
    protected void expandAllImpl ( @Nullable final N node, @Nullable final Filter<N> filter, final int depth )
    {
        if ( depth > 0 && ( filter == null || filter.accept ( node ) ) && !getModel ().isLeaf ( node ) )
        {
            if ( !isExpanded ( node ) )
            {
                expandNode ( node );
            }
            if ( node != null )
            {
                for ( int i = 0; i < node.getChildCount (); i++ )
                {
                    expandAllImpl ( ( N ) node.getChildAt ( i ), filter, depth - 1 );
                }
            }
        }
    }

    /**
     * Collapses all tree nodes.
     */
    public void collapseAll ()
    {
        collapseAll ( getRootNode (), null );
    }

    /**
     * Collapses all tree nodes.
     * {@link Filter} can be specified to collapse only the accepted {@link MutableTreeNode}s.
     *
     * @param filter {@link Filter} to limit collapsed {@link MutableTreeNode}s or {@code null}
     */
    public void collapseAll ( @Nullable final Filter<N> filter )
    {
        collapseAll ( getRootNode (), filter );
    }

    /**
     * Collapses specified {@link MutableTreeNode} and all of it's child nodes.
     *
     * @param node {@link MutableTreeNode} to collapse
     */
    public void collapseAll ( @Nullable final N node )
    {
        collapseAll ( node, null );
    }

    /**
     * Collapses specified {@link MutableTreeNode} and all of it's child nodes.
     * {@link Filter} can be specified to collapse only the accepted {@link MutableTreeNode}s.
     *
     * @param node   {@link MutableTreeNode} to collapse
     * @param filter {@link Filter} to limit collapsed {@link MutableTreeNode}s or {@code null}
     */
    public void collapseAll ( @Nullable final N node, @Nullable final Filter<N> filter )
    {
        collapseAllImpl ( node, filter );
    }

    /**
     * Collapses specified {@link MutableTreeNode} and all of it's child nodes.
     * {@link Filter} can be specified to collapse only the accepted {@link MutableTreeNode}s.
     *
     * @param node   {@link MutableTreeNode} to collapse
     * @param filter {@link Filter} to limit collapsed {@link MutableTreeNode}s or {@code null}
     */
    protected void collapseAllImpl ( @Nullable final N node, @Nullable final Filter<N> filter )
    {
        if ( ( filter == null || filter.accept ( node ) ) && !getModel ().isLeaf ( node ) )
        {
            if ( !isCollapsed ( node ) )
            {
                collapseNode ( node );
            }
            if ( node != null )
            {
                for ( int i = 0; i < node.getChildCount (); i++ )
                {
                    collapseAllImpl ( ( N ) node.getChildAt ( i ), filter );
                }
            }
        }
    }

    /**
     * Expands the specified {@link MutableTreeNode}.
     *
     * @param node {@link MutableTreeNode} to expand
     */
    public void expandNode ( @Nullable final N node )
    {
        expandPath ( getPathForNode ( node ) );
    }

    /**
     * Returns whether {@link MutableTreeNode} is expanded or not.
     *
     * @param node {@link MutableTreeNode} to check
     * @return {@code true} if {@link MutableTreeNode} is expanded, {@code false} otherwise
     */
    public boolean isExpanded ( @Nullable final N node )
    {
        return isExpanded ( getPathForNode ( node ) );
    }

    /**
     * Collapses the specified {@link MutableTreeNode}.
     *
     * @param node {@link MutableTreeNode} to collapse
     */
    public void collapseNode ( @Nullable final N node )
    {
        collapsePath ( getPathForNode ( node ) );
    }

    /**
     * Returns whether {@link MutableTreeNode} is collapsed or not.
     *
     * @param node {@link MutableTreeNode} to check
     * @return {@code true} if {@link MutableTreeNode} is collapsed, {@code false} otherwise
     */
    public boolean isCollapsed ( @Nullable final N node )
    {
        return isCollapsed ( getPathForNode ( node ) );
    }

    /**
     * Returns selected {@link MutableTreeNode} bounds.
     *
     * @return selected {@link MutableTreeNode} bounds
     */
    @Nullable
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
    @Nullable
    public Rectangle getNodeBounds ( @Nullable final N node )
    {
        return getPathBounds ( getPathForNode ( node ) );
    }

    /**
     * Returns combined bounds for the {@link List} of {@link MutableTreeNode}s.
     *
     * @param nodes {@link List} of {@link MutableTreeNode}s to combine bounds for
     * @return combined bounds for the {@link List} of {@link MutableTreeNode}s
     */
    @Nullable
    public Rectangle getNodeBounds ( @Nullable final List<N> nodes )
    {
        Rectangle bounds = null;
        if ( CollectionUtils.notEmpty ( nodes ) )
        {
            for ( final N node : nodes )
            {
                bounds = GeometryUtils.getContainingRect ( bounds, getNodeBounds ( node ) );
            }
        }
        return bounds;
    }

    /**
     * Returns row for the specified {@link MutableTreeNode}.
     *
     * @param node {@link MutableTreeNode} to find row for
     * @return row for the specified {@link MutableTreeNode}
     */
    public int getRowForNode ( @Nullable final N node )
    {
        return getRowForPath ( getPathForNode ( node ) );
    }

    /**
     * Returns {@link MutableTreeNode} for the specified row.
     *
     * @param row row to look for {@link MutableTreeNode} at
     * @return {@link MutableTreeNode} for the specified row
     */
    @Nullable
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
    @Nullable
    public TreePath getPathForNode ( @Nullable final N node )
    {
        return TreeUtils.getTreePath ( node );
    }

    /**
     * Returns {@link MutableTreeNode} for specified {@link TreePath}.
     *
     * @param path {@link TreePath} to retrieve {@link MutableTreeNode} for
     * @return {@link MutableTreeNode} for specified {@link TreePath}
     */
    @Nullable
    public N getNodeForPath ( @Nullable final TreePath path )
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
    @Nullable
    public N getNodeForLocation ( @NotNull final Point location )
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
    @Nullable
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
    @Nullable
    public TreePath getPathForLocation ( @NotNull final Point location )
    {
        return getPathForLocation ( location.x, location.y );
    }

    /**
     * Returns {@link MutableTreeNode} closest to the specified location.
     *
     * @param location location to process
     * @return {@link MutableTreeNode} closest to the specified location
     */
    @Nullable
    public N getClosestNodeForLocation ( @NotNull final Point location )
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
    @Nullable
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
    @Nullable
    public TreePath getClosestPathForLocation ( @NotNull final Point location )
    {
        return getClosestPathForLocation ( location.x, location.y );
    }

    /**
     * Returns whether specified {@link MutableTreeNode} is selected or not.
     *
     * @param node {@link MutableTreeNode} to check
     * @return {@code true} if specified {@link MutableTreeNode} is selected, {@code false} otherwise
     */
    public boolean isSelected ( @Nullable final N node )
    {
        return isPathSelected ( getPathForNode ( node ) );
    }

    /**
     * Returns selected {@link MutableTreeNode}.
     *
     * @return selected {@link MutableTreeNode}
     */
    @Nullable
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
    @NotNull
    public List<N> getSelectedNodes ( @NotNull final NodesAcceptPolicy policy )
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
        policy.filter ( this, selectedNodes );
        return selectedNodes;
    }

    /**
     * Returns {@link List} of selected {@link MutableTreeNode}s which are currently within {@link #getVisibleRect()} of this tree.
     * This will include {@link MutableTreeNode}s that are fully visible and those that are partially visible as well.
     *
     * @return {@link List} of selected {@link MutableTreeNode}s which are currently within {@link #getVisibleRect()} of this tree
     */
    @NotNull
    public List<N> getVisibleSelectedNodes ()
    {
        final List<N> selectedNodes = getSelectedNodes ();
        final Rectangle vr = getVisibleRect ();
        final Iterator<N> iterator = selectedNodes.iterator ();
        while ( iterator.hasNext () )
        {
            final N node = iterator.next ();
            final Rectangle bounds = getNodeBounds ( node );
            if ( bounds == null || !vr.intersects ( bounds ) )
            {
                iterator.remove ();
            }
        }
        return selectedNodes;
    }

    /**
     * Returns user object extracted from the selected {@link MutableTreeNode}.
     *
     * @param <U> user object type
     * @return user object extracted from the selected {@link MutableTreeNode}
     */
    @Nullable
    public <U> U getSelectedUserObject ()
    {
        return getUserObject ( getSelectedNode () );
    }

    /**
     * Returns {@link List} of user objects extracted from all selected {@link MutableTreeNode}s.
     *
     * @param <U> user object type
     * @return {@link List} of user objects extracted from all selected {@link MutableTreeNode}s
     * @see NodesAcceptPolicy#all
     */
    @NotNull
    public <U> List<U> getSelectedUserObjects ()
    {
        return getSelectedUserObjects ( NodesAcceptPolicy.all );
    }

    /**
     * Returns {@link List} of user objects extracted from selected {@link MutableTreeNode}s filtered by {@link NodesAcceptPolicy}.
     *
     * @param policy {@link NodesAcceptPolicy} used for filtering {@link MutableTreeNode}s
     * @param <U>    user object type
     * @return {@link List} of user objects extracted from selected {@link MutableTreeNode}s filtered by {@link NodesAcceptPolicy}
     * @see NodesAcceptPolicy
     */
    @NotNull
    public <U> List<U> getSelectedUserObjects ( @NotNull final NodesAcceptPolicy policy )
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
    @Nullable
    protected <U> U getUserObject ( @Nullable final N node )
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
    public void selectNodeUnderPoint ( @NotNull final Point point )
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
    public void setSelectedNode ( @Nullable final N node )
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
    public void setSelectedNodes ( @NotNull final List<N> nodes )
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
    public void setSelectedNodes ( @NotNull final N[] nodes )
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
    @Nullable
    public N getFirstVisibleLeafNode ()
    {
        N firstVisibleLeafNode = null;
        for ( int i = 0; i < getRowCount (); i++ )
        {
            final N node = getNodeForRow ( i );
            if ( getModel ().isLeaf ( node ) )
            {
                firstVisibleLeafNode = node;
                break;
            }
        }
        return firstVisibleLeafNode;
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
    @Nullable
    public N getRootNode ()
    {
        final TreeModel model = getModel ();
        return model != null ? ( N ) model.getRoot () : null;
    }

    /**
     * Returns {@link List} of all {@link MutableTreeNode}s available in this tree.
     *
     * @return {@link List} of all {@link MutableTreeNode}s available in this tree
     */
    @NotNull
    public List<N> getAvailableNodes ()
    {
        return getAvailableNodes ( getRootNode (), NodesAcceptPolicy.all );
    }

    /**
     * Returns {@link List} of all {@link MutableTreeNode}s available in this tree.
     *
     * @param policy {@link NodesAcceptPolicy} used for filtering {@link MutableTreeNode}s
     * @return {@link List} of all {@link MutableTreeNode}s available in this tree
     */
    @NotNull
    public List<N> getAvailableNodes ( @NotNull final NodesAcceptPolicy policy )
    {
        return getAvailableNodes ( getRootNode (), policy );
    }

    /**
     * Returns {@link List} of all {@link MutableTreeNode}s available under the specified {@link MutableTreeNode} including that node.
     *
     * @param parent {@link MutableTreeNode} to collect nodes for
     * @return {@link List} of all {@link MutableTreeNode}s available under the specified {@link MutableTreeNode} including that node
     */
    @NotNull
    public List<N> getAvailableNodes ( @Nullable final N parent )
    {
        return getAvailableNodes ( parent, NodesAcceptPolicy.all );
    }

    /**
     * Returns {@link List} of all {@link MutableTreeNode}s available under the specified {@link MutableTreeNode} including that node.
     *
     * @param parent {@link MutableTreeNode} to collect nodes for
     * @param policy {@link NodesAcceptPolicy} used for filtering {@link MutableTreeNode}s
     * @return {@link List} of all {@link MutableTreeNode}s available under the specified {@link MutableTreeNode} including that node
     */
    @NotNull
    public List<N> getAvailableNodes ( @Nullable final N parent, @NotNull final NodesAcceptPolicy policy )
    {
        final List<N> nodes = new ArrayList<N> ();
        collectAllNodesImpl ( parent, nodes );
        policy.filter ( this, nodes );
        return nodes;
    }

    /**
     * Collects {@link List} of all {@link MutableTreeNode}s available under the specified {@link MutableTreeNode} including that node.
     *
     * @param parent {@link MutableTreeNode} to start collecting from
     * @param nodes  {@link List} into which all {@link MutableTreeNode}s should be collected
     */
    protected void collectAllNodesImpl ( @Nullable final N parent, @NotNull final List<N> nodes )
    {
        if ( parent != null )
        {
            nodes.add ( parent );
            for ( int i = 0; i < parent.getChildCount (); i++ )
            {
                collectAllNodesImpl ( ( N ) parent.getChildAt ( i ), nodes );
            }
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
    public void scrollToNode ( @Nullable final N node )
    {
        scrollToNode ( node, false );
    }

    /**
     * Scrolls tree view to specified {@link MutableTreeNode}.
     *
     * @param node     {@link MutableTreeNode} to scroll tree view to
     * @param centered whether or not should vertically center specified {@link MutableTreeNode} in view bounds
     */
    public void scrollToNode ( @Nullable final N node, final boolean centered )
    {
        if ( node != null )
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
    public void startEditingNode ( @Nullable final N node )
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
    public void updateNode ( @Nullable final N node )
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
    public void updateNodes ( @Nullable final N... nodes )
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
    public void updateNodes ( @Nullable final List<N> nodes )
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
    @NotNull
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
    @NotNull
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
    @NotNull
    public TreeState getTreeState ( @Nullable final N node )
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
    @NotNull
    public TreeState getTreeState ( @Nullable final N node, final boolean saveSelection )
    {
        return TreeUtils.getTreeState ( this, node, saveSelection );
    }

    /**
     * Restores tree expansion and selection states.
     * Tree nodes must be instances of UniqueNode class.
     *
     * @param treeState tree expansion and selection states
     */
    public void setTreeState ( @Nullable final TreeState treeState )
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
    public void setTreeState ( @Nullable final TreeState treeState, final boolean restoreSelection )
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
    public void setTreeState ( @Nullable final TreeState treeState, @Nullable final N node )
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
    public void setTreeState ( @Nullable final TreeState treeState, @Nullable final N node, final boolean restoreSelection )
    {
        TreeUtils.setTreeState ( this, treeState, node, restoreSelection );
    }

    /**
     * Returns tree selection style.
     *
     * @return tree selection style
     */
    @NotNull
    public TreeSelectionStyle getSelectionStyle ()
    {
        return getUI ().getSelectionStyle ();
    }

    /**
     * Sets tree selection style.
     *
     * @param style tree selection style
     */
    public void setSelectionStyle ( @NotNull final TreeSelectionStyle style )
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

    @NotNull
    @Override
    public StyleId getDefaultStyleId ()
    {
        return StyleId.tree;
    }

    @NotNull
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

    @NotNull
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

    @Nullable
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
    public void setMargin ( @Nullable final Insets margin )
    {
        MarginMethodsImpl.setMargin ( this, margin );
    }

    @Nullable
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
    public void setPadding ( @Nullable final Insets padding )
    {
        PaddingMethodsImpl.setPadding ( this, padding );
    }

    /**
     * Adds hover listener.
     *
     * @param listener hover listener to add
     */
    public void addHoverListener ( @NotNull final HoverListener<N> listener )
    {
        listenerList.add ( HoverListener.class, listener );
    }

    /**
     * Removes hover listener.
     *
     * @param listener hover listener to remove
     */
    public void removeHoverListener ( @NotNull final HoverListener<N> listener )
    {
        listenerList.remove ( HoverListener.class, listener );
    }

    /**
     * Returns hover listeners.
     *
     * @return hover listeners
     */
    @NotNull
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
    public void fireHoverChanged ( @Nullable final N previous, @Nullable final N current )
    {
        for ( final HoverListener listener : getHoverListeners () )
        {
            listener.hoverChanged ( previous, current );
        }
    }

    @Override
    public int getScrollableUnitIncrement ( @NotNull final Rectangle visibleRect, final int orientation, final int direction )
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
    public void repaint ( @Nullable final N node )
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
    public void repaint ( @Nullable final List<N> nodes )
    {
        if ( CollectionUtils.notEmpty ( nodes ) )
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
    public MouseAdapter onNodeDoubleClick ( @NotNull final TreeNodeEventRunnable<N> runnable )
    {
        return TreeEventMethodsImpl.onNodeDoubleClick ( this, runnable );
    }

    @Override
    public MouseAdapter onNodeDoubleClick ( @Nullable final Predicate<N> condition, @NotNull final TreeNodeEventRunnable<N> runnable )
    {
        return TreeEventMethodsImpl.onNodeDoubleClick ( this, condition, runnable );
    }

    @NotNull
    @Override
    public MouseAdapter onMousePress ( @NotNull final MouseEventRunnable runnable )
    {
        return EventMethodsImpl.onMousePress ( this, runnable );
    }

    @NotNull
    @Override
    public MouseAdapter onMousePress ( @Nullable final MouseButton mouseButton, @NotNull final MouseEventRunnable runnable )
    {
        return EventMethodsImpl.onMousePress ( this, mouseButton, runnable );
    }

    @NotNull
    @Override
    public MouseAdapter onMouseEnter ( @NotNull final MouseEventRunnable runnable )
    {
        return EventMethodsImpl.onMouseEnter ( this, runnable );
    }

    @NotNull
    @Override
    public MouseAdapter onMouseExit ( @NotNull final MouseEventRunnable runnable )
    {
        return EventMethodsImpl.onMouseExit ( this, runnable );
    }

    @NotNull
    @Override
    public MouseAdapter onMouseDrag ( @NotNull final MouseEventRunnable runnable )
    {
        return EventMethodsImpl.onMouseDrag ( this, runnable );
    }

    @NotNull
    @Override
    public MouseAdapter onMouseDrag ( @Nullable final MouseButton mouseButton, @NotNull final MouseEventRunnable runnable )
    {
        return EventMethodsImpl.onMouseDrag ( this, mouseButton, runnable );
    }

    @NotNull
    @Override
    public MouseAdapter onMouseClick ( @NotNull final MouseEventRunnable runnable )
    {
        return EventMethodsImpl.onMouseClick ( this, runnable );
    }

    @NotNull
    @Override
    public MouseAdapter onMouseClick ( @Nullable final MouseButton mouseButton, @NotNull final MouseEventRunnable runnable )
    {
        return EventMethodsImpl.onMouseClick ( this, mouseButton, runnable );
    }

    @NotNull
    @Override
    public MouseAdapter onDoubleClick ( @NotNull final MouseEventRunnable runnable )
    {
        return EventMethodsImpl.onDoubleClick ( this, runnable );
    }

    @NotNull
    @Override
    public MouseAdapter onMenuTrigger ( @NotNull final MouseEventRunnable runnable )
    {
        return EventMethodsImpl.onMenuTrigger ( this, runnable );
    }

    @NotNull
    @Override
    public KeyAdapter onKeyType ( @NotNull final KeyEventRunnable runnable )
    {
        return EventMethodsImpl.onKeyType ( this, runnable );
    }

    @NotNull
    @Override
    public KeyAdapter onKeyType ( @Nullable final HotkeyData hotkey, @NotNull final KeyEventRunnable runnable )
    {
        return EventMethodsImpl.onKeyType ( this, hotkey, runnable );
    }

    @NotNull
    @Override
    public KeyAdapter onKeyPress ( @NotNull final KeyEventRunnable runnable )
    {
        return EventMethodsImpl.onKeyPress ( this, runnable );
    }

    @NotNull
    @Override
    public KeyAdapter onKeyPress ( @Nullable final HotkeyData hotkey, @NotNull final KeyEventRunnable runnable )
    {
        return EventMethodsImpl.onKeyPress ( this, hotkey, runnable );
    }

    @NotNull
    @Override
    public KeyAdapter onKeyRelease ( @NotNull final KeyEventRunnable runnable )
    {
        return EventMethodsImpl.onKeyRelease ( this, runnable );
    }

    @NotNull
    @Override
    public KeyAdapter onKeyRelease ( @Nullable final HotkeyData hotkey, @NotNull final KeyEventRunnable runnable )
    {
        return EventMethodsImpl.onKeyRelease ( this, hotkey, runnable );
    }

    @NotNull
    @Override
    public FocusAdapter onFocusGain ( @NotNull final FocusEventRunnable runnable )
    {
        return EventMethodsImpl.onFocusGain ( this, runnable );
    }

    @NotNull
    @Override
    public FocusAdapter onFocusLoss ( @NotNull final FocusEventRunnable runnable )
    {
        return EventMethodsImpl.onFocusLoss ( this, runnable );
    }

    @NotNull
    @Override
    public MouseAdapter onDragStart ( final int shift, @NotNull final MouseEventRunnable runnable )
    {
        return EventMethodsImpl.onDragStart ( this, shift, runnable );
    }

    @NotNull
    @Override
    public MouseAdapter onDragStart ( final int shift, @Nullable final MouseButton mouseButton, @NotNull final MouseEventRunnable runnable )
    {
        return EventMethodsImpl.onDragStart ( this, shift, mouseButton, runnable );
    }

    @Override
    public void addLanguageListener ( @NotNull final LanguageListener listener )
    {
        UILanguageManager.addLanguageListener ( this, listener );
    }

    @Override
    public void removeLanguageListener ( @NotNull final LanguageListener listener )
    {
        UILanguageManager.removeLanguageListener ( this, listener );
    }

    @Override
    public void removeLanguageListeners ()
    {
        UILanguageManager.removeLanguageListeners ( this );
    }

    @Override
    public void addDictionaryListener ( @NotNull final DictionaryListener listener )
    {
        UILanguageManager.addDictionaryListener ( this, listener );
    }

    @Override
    public void removeDictionaryListener ( @NotNull final DictionaryListener listener )
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

    @NotNull
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

    @NotNull
    @Override
    public WebTree<N> setPreferredHeight ( final int preferredHeight )
    {
        return SizeMethodsImpl.setPreferredHeight ( this, preferredHeight );
    }

    @NotNull
    @Override
    public Dimension getPreferredSize ()
    {
        return SizeMethodsImpl.getPreferredSize ( this, super.getPreferredSize () );
    }

    @NotNull
    @Override
    public Dimension getOriginalPreferredSize ()
    {
        return SizeMethodsImpl.getOriginalPreferredSize ( this, super.getPreferredSize () );
    }

    @NotNull
    @Override
    public WebTree<N> setPreferredSize ( final int width, final int height )
    {
        return SizeMethodsImpl.setPreferredSize ( this, width, height );
    }

    @Override
    public int getMaximumWidth ()
    {
        return SizeMethodsImpl.getMaximumWidth ( this );
    }

    @NotNull
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

    @NotNull
    @Override
    public WebTree<N> setMaximumHeight ( final int maximumHeight )
    {
        return SizeMethodsImpl.setMaximumHeight ( this, maximumHeight );
    }

    @NotNull
    @Override
    public Dimension getMaximumSize ()
    {
        return SizeMethodsImpl.getMaximumSize ( this, super.getMaximumSize () );
    }

    @NotNull
    @Override
    public Dimension getOriginalMaximumSize ()
    {
        return SizeMethodsImpl.getOriginalMaximumSize ( this, super.getMaximumSize () );
    }

    @NotNull
    @Override
    public WebTree<N> setMaximumSize ( final int width, final int height )
    {
        return SizeMethodsImpl.setMaximumSize ( this, width, height );
    }

    @Override
    public int getMinimumWidth ()
    {
        return SizeMethodsImpl.getMinimumWidth ( this );
    }

    @NotNull
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

    @NotNull
    @Override
    public WebTree<N> setMinimumHeight ( final int minimumHeight )
    {
        return SizeMethodsImpl.setMinimumHeight ( this, minimumHeight );
    }

    @NotNull
    @Override
    public Dimension getMinimumSize ()
    {
        return SizeMethodsImpl.getMinimumSize ( this, super.getMinimumSize () );
    }

    @NotNull
    @Override
    public Dimension getOriginalMinimumSize ()
    {
        return SizeMethodsImpl.getOriginalMinimumSize ( this, super.getMinimumSize () );
    }

    @NotNull
    @Override
    public WebTree<N> setMinimumSize ( final int width, final int height )
    {
        return SizeMethodsImpl.setMinimumSize ( this, width, height );
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

    @NotNull
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
    @NotNull
    protected static TreeModel createTreeModel ( @NotNull final Object data )
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
    @NotNull
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