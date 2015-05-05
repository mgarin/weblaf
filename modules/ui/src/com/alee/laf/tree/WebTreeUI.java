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

import com.alee.extended.tree.WebCheckBoxTree;
import com.alee.global.StyleConstants;
import com.alee.laf.WebLookAndFeel;
import com.alee.managers.tooltip.ToolTipProvider;
import com.alee.utils.*;
import com.alee.utils.ninepatch.NinePatchIcon;

import javax.swing.*;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicTreeUI;
import javax.swing.tree.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.*;
import java.util.List;

/**
 * Custom UI for JTree component.
 *
 * @author Mikle Garin
 */

public class WebTreeUI extends BasicTreeUI
{
    /**
     * Expand and collapse control icons.
     */
    public static ImageIcon EXPAND_ICON = new ImageIcon ( WebTreeUI.class.getResource ( "icons/expand.png" ) );
    public static ImageIcon COLLAPSE_ICON = new ImageIcon ( WebTreeUI.class.getResource ( "icons/collapse.png" ) );
    public static ImageIcon DISABLED_EXPAND_ICON = ImageUtils.createDisabledCopy ( EXPAND_ICON );
    public static ImageIcon DISABLED_COLLAPSE_ICON = ImageUtils.createDisabledCopy ( COLLAPSE_ICON );

    /**
     * Default node icons.
     */
    public static ImageIcon ROOT_ICON = new ImageIcon ( WebTreeUI.class.getResource ( "icons/root.png" ) );
    public static ImageIcon CLOSED_ICON = new ImageIcon ( WebTreeUI.class.getResource ( "icons/closed.png" ) );
    public static ImageIcon OPEN_ICON = new ImageIcon ( WebTreeUI.class.getResource ( "icons/open.png" ) );
    public static ImageIcon LEAF_ICON = new ImageIcon ( WebTreeUI.class.getResource ( "icons/leaf.png" ) );

    /**
     * Default drop line gradient fractions.
     */
    protected static final float[] fractions = { 0, 0.25f, 0.75f, 1f };

    /**
     * Style settings.
     */
    protected boolean autoExpandSelectedNode = WebTreeStyle.autoExpandSelectedPath;
    protected boolean highlightRolloverNode = WebTreeStyle.highlightRolloverNode;
    protected boolean paintLines = WebTreeStyle.paintLines;
    protected Color linesColor = WebTreeStyle.linesColor;
    protected TreeSelectionStyle selectionStyle = WebTreeStyle.selectionStyle;
    protected int selectionRound = WebTreeStyle.selectionRound;
    protected int selectionShadeWidth = WebTreeStyle.selectionShadeWidth;
    protected boolean selectorEnabled = WebTreeStyle.selectorEnabled;
    protected Color selectorColor = WebTreeStyle.selectorColor;
    protected Color selectorBorderColor = WebTreeStyle.selectorBorderColor;
    protected int selectorRound = WebTreeStyle.selectorRound;
    protected BasicStroke selectorStroke = WebTreeStyle.selectorStroke;
    protected boolean webColoredSelection = WebTreeStyle.webColoredSelection;
    protected Color selectionBorderColor = WebTreeStyle.selectionBorderColor;
    protected Color selectionBackgroundColor = WebTreeStyle.selectionBackgroundColor;
    protected int dropCellShadeWidth = WebTreeStyle.dropCellShadeWidth;

    /**
     * Tree listeners.
     */
    protected PropertyChangeListener orientationChangeListener;
    protected PropertyChangeListener dropLocationChangeListener;
    protected TreeSelectionListener treeSelectionListener;
    protected TreeExpansionListener treeExpansionListener;
    protected MouseAdapter mouseAdapter;

    /**
     * Runtime variables.
     */
    protected int rolloverRow = -1;
    protected List<Integer> initialSelection = new ArrayList<Integer> ();
    protected Point selectionStart = null;
    protected Point selectionEnd = null;
    protected boolean leftToRight = true;
    protected TreePath draggablePath = null;

    /**
     * Returns an instance of the WebTreeUI for the specified component.
     * This tricky method is used by UIManager to create component UIs when needed.
     *
     * @param c component that will use UI instance
     * @return instance of the WebTreeUI
     */
    @SuppressWarnings ( "UnusedParameters" )
    public static ComponentUI createUI ( final JComponent c )
    {
        return new WebTreeUI ();
    }

    /**
     * Installs UI in the specified component.
     *
     * @param c component for this UI
     */
    @Override
    public void installUI ( final JComponent c )
    {
        super.installUI ( c );

        this.leftToRight = tree.getComponentOrientation ().isLeftToRight ();

        // Overwrite indent in case WebLookAndFeel is not installed as L&F
        if ( !WebLookAndFeel.isInstalled () )
        {
            setRightChildIndent ( WebTreeStyle.nodeLineIndent );
            setLeftChildIndent ( WebTreeStyle.nodeLineIndent );
        }

        // Allow each cell to choose its own preferred height
        tree.setRowHeight ( -1 );

        // Modifying default drop mode
        // USE_SELECTION mode is not preferred since WebLaF provides a better visual drop representation
        tree.setDropMode ( DropMode.ON );

        // Use a moderate amount of visible rows by default
        // BasicTreeUI uses 20 rows by default which is too much
        tree.setVisibleRowCount ( 10 );

        // Forces tree to save changes when another tree node is selected instead of cancelling them
        tree.setInvokesStopCellEditing ( true );

        // Orientation change listener
        orientationChangeListener = new PropertyChangeListener ()
        {
            @Override
            public void propertyChange ( final PropertyChangeEvent evt )
            {
                leftToRight = tree.getComponentOrientation ().isLeftToRight ();
            }
        };
        tree.addPropertyChangeListener ( WebLookAndFeel.ORIENTATION_PROPERTY, orientationChangeListener );
        SwingUtils.setOrientation ( tree );

        // Drop location change listener
        dropLocationChangeListener = new PropertyChangeListener ()
        {
            @Override
            public void propertyChange ( final PropertyChangeEvent evt )
            {
                // Repainting previous drop location
                final JTree.DropLocation oldLocation = ( JTree.DropLocation ) evt.getOldValue ();
                if ( oldLocation != null )
                {
                    tree.repaint ( getNodeDropLocationBounds ( oldLocation.getPath () ) );
                }

                // Repainting current drop location
                final JTree.DropLocation newLocation = ( JTree.DropLocation ) evt.getNewValue ();
                if ( newLocation != null )
                {
                    tree.repaint ( getNodeDropLocationBounds ( newLocation.getPath () ) );
                }
            }
        };
        tree.addPropertyChangeListener ( WebLookAndFeel.DROP_LOCATION, dropLocationChangeListener );

        // Selection listener
        treeSelectionListener = new TreeSelectionListener ()
        {
            @Override
            public void valueChanged ( final TreeSelectionEvent e )
            {
                // Optimized selection repaint
                repaintSelection ();

                // Tree expansion on selection
                if ( autoExpandSelectedNode && tree.getSelectionCount () > 0 )
                {
                    tree.expandPath ( tree.getSelectionPath () );
                }
            }
        };
        tree.addTreeSelectionListener ( treeSelectionListener );

        // Expansion listener
        treeExpansionListener = new TreeExpansionListener ()
        {
            @Override
            public void treeExpanded ( final TreeExpansionEvent event )
            {
                repaintSelection ();
            }

            @Override
            public void treeCollapsed ( final TreeExpansionEvent event )
            {
                repaintSelection ();
            }
        };
        tree.addTreeExpansionListener ( treeExpansionListener );

        // Mouse events adapter
        mouseAdapter = new MouseAdapter ()
        {
            @Override
            public void mousePressed ( final MouseEvent e )
            {
                // Only left mouse button events
                if ( SwingUtilities.isLeftMouseButton ( e ) )
                {
                    // Check that mouse did not hit actual tree cell
                    if ( getRowForPoint ( e.getPoint (), false ) == -1 )
                    {
                        if ( isSelectorAvailable () )
                        {
                            // Avoiding selection start when pressed on tree expand handle
                            final TreePath path = getClosestPathForLocation ( tree, e.getX (), e.getY () );
                            if ( path == null || !isLocationInExpandControl ( path, e.getX (), e.getY () ) &&
                                    !isLocationInCheckBoxControl ( path, e.getX (), e.getY () ) )
                            {
                                // Avoid starting multiselection if row is selected and drag is possible
                                final int rowForPath = getRowForPath ( tree, path );
                                if ( isDragAvailable () && rowForPath != -1 &&
                                        getRowBounds ( rowForPath ).contains ( e.getX (), e.getY () ) && tree.isRowSelected ( rowForPath ) )
                                {
                                    // Marking row to be dragged
                                    draggablePath = path;
                                }
                                else
                                {
                                    // Selection
                                    selectionStart = e.getPoint ();
                                    selectionEnd = selectionStart;

                                    // Initial tree selection
                                    initialSelection = getSelectionRowsList ();

                                    // Updating selection
                                    validateSelection ( e );

                                    // Repainting selection on the tree
                                    repaintSelector ();
                                }
                            }
                        }
                        else if ( isFullLineSelection () )
                        {
                            // todo Start DnD on selected row
                            // Avoiding selection start when pressed on tree expand handle
                            final TreePath path = getClosestPathForLocation ( tree, e.getX (), e.getY () );
                            if ( path != null && !isLocationInExpandControl ( path, e.getX (), e.getY () ) &&
                                    !isLocationInCheckBoxControl ( path, e.getX (), e.getY () ) )
                            {
                                // Single row selection
                                if ( tree.getSelectionModel ().getSelectionMode () == TreeSelectionModel.SINGLE_TREE_SELECTION )
                                {
                                    tree.setSelectionRow ( getRowForPoint ( e.getPoint (), true ) );
                                }

                                // Marking row to be dragged
                                final int rowForPath = getRowForPath ( tree, path );
                                if ( isDragAvailable () && getRowBounds ( rowForPath ).contains ( e.getX (), e.getY () ) &&
                                        tree.isRowSelected ( rowForPath ) )
                                {
                                    draggablePath = path;
                                }
                            }
                        }
                    }
                    //                    else
                    //                    {
                    //                        // todo Start DnD on selected row
                    //                    }
                }
            }

            @Override
            public void mouseDragged ( final MouseEvent e )
            {
                if ( draggablePath != null )
                {
                    final TransferHandler transferHandler = tree.getTransferHandler ();
                    transferHandler.exportAsDrag ( tree, e, transferHandler.getSourceActions ( tree ) );
                    draggablePath = null;
                }
                if ( isSelectorAvailable () && selectionStart != null )
                {
                    // Selection
                    selectionEnd = e.getPoint ();

                    // Updating selection
                    validateSelection ( e );

                    // Repainting selection on the tree
                    repaintSelector ();

                    if ( !tree.getVisibleRect ().contains ( e.getPoint () ) )
                    {
                        tree.scrollRectToVisible ( new Rectangle ( e.getPoint (), new Dimension ( 0, 0 ) ) );
                    }
                }
            }

            @Override
            public void mouseReleased ( final MouseEvent e )
            {
                if ( draggablePath != null )
                {
                    draggablePath = null;
                }
                if ( isSelectorAvailable () && selectionStart != null )
                {
                    // Saving selection rect to repaint
                    // Rectangle fr = GeometryUtils.getContainingRect ( selectionStart, selectionEnd );

                    // Selection
                    selectionStart = null;
                    selectionEnd = null;

                    // Repainting selection on the tree
                    repaintSelector ( /*fr*/ );
                }
            }

            private void validateSelection ( final MouseEvent e )
            {
                // todo Possibly optimize selection? - modify it instead of overwriting each time

                // Selection rect
                final Rectangle selection = GeometryUtils.getContainingRect ( selectionStart, selectionEnd );

                // Compute new selection
                final List<Integer> newSelection = new ArrayList<Integer> ();
                if ( SwingUtils.isShift ( e ) )
                {
                    for ( int row = 0; row < tree.getRowCount (); row++ )
                    {
                        if ( getRowBounds ( row ).intersects ( selection ) && !initialSelection.contains ( row ) )
                        {
                            newSelection.add ( row );
                        }
                    }
                    for ( final int row : initialSelection )
                    {
                        newSelection.add ( row );
                    }
                }
                else if ( SwingUtils.isCtrl ( e ) )
                {
                    final List<Integer> excludedRows = new ArrayList<Integer> ();
                    for ( int row = 0; row < tree.getRowCount (); row++ )
                    {
                        if ( getRowBounds ( row ).intersects ( selection ) )
                        {
                            if ( initialSelection.contains ( row ) )
                            {
                                excludedRows.add ( row );
                            }
                            else
                            {
                                newSelection.add ( row );
                            }
                        }
                    }
                    for ( final int row : initialSelection )
                    {
                        if ( !excludedRows.contains ( row ) )
                        {
                            newSelection.add ( row );
                        }
                    }
                }
                else
                {
                    for ( int row = 0; row < tree.getRowCount (); row++ )
                    {
                        if ( getRowBounds ( row ).intersects ( selection ) )
                        {
                            newSelection.add ( row );
                        }
                    }
                }

                // Change selection if it is not the same as before
                if ( !CollectionUtils.areEqual ( getSelectionRowsList (), newSelection ) )
                {
                    if ( newSelection.size () > 0 )
                    {
                        tree.setSelectionRows ( CollectionUtils.toArray ( newSelection ) );
                    }
                    else
                    {
                        tree.clearSelection ();
                    }
                }
            }

            private List<Integer> getSelectionRowsList ()
            {
                final List<Integer> selection = new ArrayList<Integer> ();
                final int[] selectionRows = tree.getSelectionRows ();
                if ( selectionRows != null )
                {
                    for ( final int row : selectionRows )
                    {
                        selection.add ( row );
                    }
                }
                return selection;
            }

            private void repaintSelector ()
            {
                // Replaced with full repaint due to strange tree lines painting bug
                tree.repaint ( tree.getVisibleRect () );
            }

            @Override
            public void mouseEntered ( final MouseEvent e )
            {
                updateMouseover ( e );
            }

            @Override
            public void mouseMoved ( final MouseEvent e )
            {
                updateMouseover ( e );
            }

            @Override
            public void mouseExited ( final MouseEvent e )
            {
                clearMouseover ();
            }

            private void updateMouseover ( final MouseEvent e )
            {
                if ( tree.isEnabled () )
                {
                    final int index = getRowForPoint ( e.getPoint () );
                    if ( rolloverRow != index )
                    {
                        updateRolloverCell ( rolloverRow, index );
                    }
                }
                else
                {
                    clearMouseover ();
                }
            }

            private void clearMouseover ()
            {
                if ( rolloverRow != -1 )
                {
                    updateRolloverCell ( rolloverRow, -1 );
                }
            }

            private void updateRolloverCell ( final int oldIndex, final int newIndex )
            {
                // Updating rollover row
                rolloverRow = newIndex;

                // Updating rollover row display
                if ( highlightRolloverNode )
                {
                    updateRow ( oldIndex );
                    updateRow ( newIndex );
                }

                // Updating custom WebLaF tooltip display state
                final ToolTipProvider provider = getToolTipProvider ();
                if ( provider != null )
                {
                    provider.rolloverCellChanged ( tree, oldIndex, 0, newIndex, 0 );
                }
            }

            private void updateRow ( final int row )
            {
                if ( row != -1 )
                {
                    final Rectangle rowBounds = getFullRowBounds ( row );
                    if ( rowBounds != null )
                    {
                        tree.repaint ( rowBounds );
                    }
                }
            }
        };
        tree.addMouseListener ( mouseAdapter );
        tree.addMouseMotionListener ( mouseAdapter );
    }

    /**
     * Uninstalls UI from the specified component.
     *
     * @param c component with this UI
     */
    @Override
    public void uninstallUI ( final JComponent c )
    {
        tree.removePropertyChangeListener ( WebLookAndFeel.ORIENTATION_PROPERTY, orientationChangeListener );
        tree.removePropertyChangeListener ( WebLookAndFeel.DROP_LOCATION, dropLocationChangeListener );
        tree.removeTreeSelectionListener ( treeSelectionListener );
        tree.removeTreeExpansionListener ( treeExpansionListener );
        tree.removeMouseListener ( mouseAdapter );
        tree.removeMouseMotionListener ( mouseAdapter );

        super.uninstallUI ( c );
    }

    /**
     * Returns custom WebLaF tooltip provider.
     *
     * @return custom WebLaF tooltip provider
     */
    protected ToolTipProvider<? extends WebTree> getToolTipProvider ()
    {
        return tree != null && tree instanceof WebTree ? ( ( WebTree ) tree ).getToolTipProvider () : null;
    }

    /**
     * Returns whether tree nodes drag available or not.
     *
     * @return true if tree nodes drag available, false otherwise
     */
    protected boolean isDragAvailable ()
    {
        return tree != null && tree.isEnabled () && tree.getDragEnabled () && tree.getTransferHandler () != null &&
                tree.getTransferHandler ().getSourceActions ( tree ) > 0;
    }

    /**
     * Returns whether tree should expand nodes on selection or not.
     *
     * @return true if tree should expand nodes on selection, false otherwise
     */
    public boolean isAutoExpandSelectedNode ()
    {
        return autoExpandSelectedNode;
    }

    /**
     * Sets whether tree should expand nodes on selection or not.
     *
     * @param autoExpandSelectedNode whether tree should expand nodes on selection or not
     */
    public void setAutoExpandSelectedNode ( final boolean autoExpandSelectedNode )
    {
        this.autoExpandSelectedNode = autoExpandSelectedNode;
    }

    /**
     * Returns whether tree should highlight rollover node or not.
     *
     * @return true if tree should highlight rollover, false otherwise
     */
    public boolean isHighlightRolloverNode ()
    {
        return highlightRolloverNode;
    }

    /**
     * Sets whether tree should highlight rollover node or not.
     *
     * @param highlight whether tree should highlight rollover node or not
     */
    public void setHighlightRolloverNode ( final boolean highlight )
    {
        this.highlightRolloverNode = highlight;
    }

    /**
     * Returns whether tree should paint structure lines or not.
     *
     * @return true if tree should paint structure lines, false otherwise
     */
    public boolean isPaintLines ()
    {
        return paintLines;
    }

    /**
     * Sets whether tree should paint structure lines or not.
     *
     * @param paint whether tree should paint structure lines or not
     */
    public void setPaintLines ( final boolean paint )
    {
        this.paintLines = paint;
    }

    /**
     * Returns tree structure lines color.
     *
     * @return tree structure lines color
     */
    public Color getLinesColor ()
    {
        return linesColor;
    }

    /**
     * Sets tree structure lines color.
     *
     * @param color tree structure lines color
     */
    public void setLinesColor ( final Color color )
    {
        this.linesColor = color;
    }

    /**
     * Returns tree selection style.
     *
     * @return tree selection style
     */
    public TreeSelectionStyle getSelectionStyle ()
    {
        return selectionStyle;
    }

    /**
     * Sets tree selection style.
     *
     * @param style tree selection style
     */
    public void setSelectionStyle ( final TreeSelectionStyle style )
    {
        this.selectionStyle = style;
    }

    /**
     * Returns tree selection rounding.
     *
     * @return tree selection rounding
     */
    public int getSelectionRound ()
    {
        return selectionRound;
    }

    /**
     * Sets tree selection rounding.
     *
     * @param round tree selection rounding
     */
    public void setSelectionRound ( final int round )
    {
        this.selectionRound = round;
    }

    /**
     * Returns tree selection shade width.
     *
     * @return tree selection shade width
     */
    public int getSelectionShadeWidth ()
    {
        return selectionShadeWidth;
    }

    /**
     * Sets tree selection shade width.
     *
     * @param shadeWidth tree selection shade width
     */
    public void setSelectionShadeWidth ( final int shadeWidth )
    {
        if ( this.selectionShadeWidth != shadeWidth )
        {
            // Saving new selection shade width
            this.selectionShadeWidth = shadeWidth;

            // Properly updating the whole tree structure since this value might affect renderer's size
            TreeUtils.updateAllVisibleNodes ( tree );
        }
    }

    /**
     * Returns whether selector is enabled or not.
     *
     * @return true if selector is enabled, false otherwise
     */
    public boolean isSelectorEnabled ()
    {
        return selectorEnabled;
    }

    /**
     * Sets whether selector is enabled or not.
     *
     * @param enabled whether selector is enabled or not
     */
    public void setSelectorEnabled ( final boolean enabled )
    {
        this.selectorEnabled = enabled;
    }

    /**
     * Returns selector color.
     *
     * @return selector color
     */
    public Color getSelectorColor ()
    {
        return selectorColor;
    }

    /**
     * Sets selector color.
     *
     * @param color selector color
     */
    public void setSelectorColor ( final Color color )
    {
        this.selectorColor = color;
    }

    /**
     * Returns selector border color.
     *
     * @return selector border color
     */
    public Color getSelectorBorderColor ()
    {
        return selectorBorderColor;
    }

    /**
     * Sets selector border color.
     *
     * @param color selector border color
     */
    public void setSelectorBorderColor ( final Color color )
    {
        this.selectorBorderColor = color;
    }

    /**
     * Returns selector rounding.
     *
     * @return selector rounding
     */
    public int getSelectorRound ()
    {
        return selectorRound;
    }

    /**
     * Sets selector rounding.
     *
     * @param round selector rounding
     */
    public void setSelectorRound ( final int round )
    {
        this.selectorRound = round;
    }

    /**
     * Returns selector border stroke.
     *
     * @return selector border stroke
     */
    public BasicStroke getSelectorStroke ()
    {
        return selectorStroke;
    }

    /**
     * Sets selector border stroke.
     *
     * @param stroke selector border stroke
     */
    public void setSelectorStroke ( final BasicStroke stroke )
    {
        this.selectorStroke = stroke;
    }

    /**
     * Returns whether selection should be web-colored or not.
     * In case it is not web-colored selectionBackgroundColor value will be used as background color.
     *
     * @return true if selection should be web-colored, false otherwise
     */
    public boolean isWebColoredSelection ()
    {
        return webColoredSelection;
    }

    /**
     * Sets whether selection should be web-colored or not.
     * In case it is not web-colored selectionBackgroundColor value will be used as background color.
     *
     * @param webColored whether selection should be web-colored or not
     */
    public void setWebColoredSelection ( final boolean webColored )
    {
        this.webColoredSelection = webColored;
    }

    /**
     * Returns selection border color.
     *
     * @return selection border color
     */
    public Color getSelectionBorderColor ()
    {
        return selectionBorderColor;
    }

    /**
     * Sets selection border color.
     *
     * @param color selection border color
     */
    public void setSelectionBorderColor ( final Color color )
    {
        this.selectionBorderColor = color;
    }

    /**
     * Returns selection background color.
     * It is used only when webColoredSelection is set to false.
     *
     * @return selection background color
     */
    public Color getSelectionBackgroundColor ()
    {
        return selectionBackgroundColor;
    }

    /**
     * Sets selection background color.
     * It is used only when webColoredSelection is set to false.
     *
     * @param color selection background color
     */
    public void setSelectionBackgroundColor ( final Color color )
    {
        this.selectionBackgroundColor = color;
    }

    /**
     * Returns drop cell highlight shade width.
     *
     * @return drop cell highlight shade width
     */
    public int getDropCellShadeWidth ()
    {
        return dropCellShadeWidth;
    }

    /**
     * Sets drop cell highlight shade width.
     *
     * @param dropCellShadeWidth new drop cell highlight shade width
     */
    public void setDropCellShadeWidth ( final int dropCellShadeWidth )
    {
        this.dropCellShadeWidth = dropCellShadeWidth;
    }

    /**
     * Returns whether selector is available for current tree or not.
     *
     * @return true if selector is available for current tree, false otherwise
     */
    public boolean isSelectorAvailable ()
    {
        return isSelectorEnabled () && tree != null && tree.isEnabled () &&
                tree.getSelectionModel ().getSelectionMode () != TreeSelectionModel.SINGLE_TREE_SELECTION;
    }

    /**
     * Returns whether tree selection style points that the whole line is a single cell or not.
     *
     * @return true if tree selection style points that the whole line is a single cell, false otherwise
     */
    public boolean isFullLineSelection ()
    {
        return selectionStyle == TreeSelectionStyle.line;
    }

    /**
     * Returns row index for specified point on the tree.
     * This method takes selection style into account.
     *
     * @param point point on the tree
     * @return row index for specified point on the tree
     */
    public int getRowForPoint ( final Point point )
    {
        return getRowForPoint ( point, isFullLineSelection () );
    }

    /**
     * Returns row index for specified point on the tree.
     *
     * @param point        point on the tree
     * @param countFullRow whether take the whole row into account or just node renderer rect
     * @return row index for specified point on the tree
     */
    public int getRowForPoint ( final Point point, final boolean countFullRow )
    {
        if ( tree != null )
        {
            // todo Optimize
            for ( int row = 0; row < tree.getRowCount (); row++ )
            {
                final Rectangle bounds = getRowBounds ( row, countFullRow );
                if ( bounds.contains ( point ) )
                {
                    return row;
                }
            }
        }
        return -1;
    }

    /**
     * Returns row bounds by its index.
     * This method takes selection style into account.
     *
     * @param row row index
     * @return row bounds by its index
     */
    public Rectangle getRowBounds ( final int row )
    {
        return getRowBounds ( row, isFullLineSelection () );
    }

    /**
     * Returns row bounds by its index.
     *
     * @param row          row index
     * @param countFullRow whether take the whole row into account or just node renderer rect
     * @return row bounds by its index
     */
    public Rectangle getRowBounds ( final int row, final boolean countFullRow )
    {
        return countFullRow ? getFullRowBounds ( row ) : tree.getRowBounds ( row );
    }

    /**
     * Returns full row bounds by its index.
     *
     * @param row row index
     * @return full row bounds by its index
     */
    public Rectangle getFullRowBounds ( final int row )
    {
        final Rectangle b = tree.getRowBounds ( row );
        if ( b != null )
        {
            final Insets insets = tree.getInsets ();
            b.x = insets.left;
            b.width = tree.getWidth () - insets.left - insets.right;
        }
        return b;
    }

    /**
     * Returns default tree cell editor.
     *
     * @return default tree cell editor
     */
    @Override
    protected TreeCellEditor createDefaultCellEditor ()
    {
        return new WebTreeCellEditor ();
    }

    /**
     * Returns default tree cell renderer.
     *
     * @return default tree cell renderer
     */
    @Override
    protected TreeCellRenderer createDefaultCellRenderer ()
    {
        return new WebTreeCellRenderer ();
    }

    /**
     * Selects tree path for the specified event.
     *
     * @param path tree path to select
     * @param e    event to process
     */
    @Override
    protected void selectPathForEvent ( final TreePath path, final MouseEvent e )
    {
        if ( !isLocationInCheckBoxControl ( path, e.getX (), e.getY () ) )
        {
            super.selectPathForEvent ( path, e );
        }
    }

    /**
     * Returns whether location is in the checkbox tree checkbox control or not.
     *
     * @param path tree path
     * @param x    location X coordinate
     * @param y    location Y coordinate
     * @return true if location is in the checkbox tree checkbox control, false otherwise
     */
    protected boolean isLocationInCheckBoxControl ( final TreePath path, final int x, final int y )
    {
        if ( tree instanceof WebCheckBoxTree )
        {
            final WebCheckBoxTree checkBoxTree = ( WebCheckBoxTree ) tree;
            if ( checkBoxTree.isCheckingByUserEnabled () )
            {
                final DefaultMutableTreeNode node = ( DefaultMutableTreeNode ) path.getLastPathComponent ();
                if ( checkBoxTree.isCheckBoxVisible ( node ) && checkBoxTree.isCheckBoxEnabled ( node ) )
                {
                    final Rectangle checkBoxBounds = checkBoxTree.getCheckBoxBounds ( path );
                    return checkBoxBounds != null && checkBoxBounds.contains ( x, y );
                }
                else
                {
                    return false;
                }
            }
            else
            {
                return false;
            }
        }
        else
        {
            return false;
        }
    }

    /**
     * Returns tree structure lines color.
     *
     * @return tree structure lines color
     */
    @Override
    protected Color getHashColor ()
    {
        return linesColor;
    }

    /**
     * Returns tree structure expanded node icon.
     *
     * @return tree structure expanded node icon
     */
    @Override
    public Icon getExpandedIcon ()
    {
        return tree.isEnabled () ? COLLAPSE_ICON : DISABLED_COLLAPSE_ICON;
    }

    /**
     * Returns tree structure collapsed node icon.
     *
     * @return tree structure collapsed node icon
     */
    @Override
    public Icon getCollapsedIcon ()
    {
        return tree.isEnabled () ? EXPAND_ICON : DISABLED_EXPAND_ICON;
    }

    /**
     * Paints centered icon.
     *
     * @param c    component
     * @param g    graphics
     * @param icon icon
     * @param x    x coordinate
     * @param y    y coordinate
     */
    @Override
    protected void drawCentered ( final Component c, final Graphics g, final Icon icon, final int x, final int y )
    {                                                                   //x-icon.getIconWidth ()/2-2
        icon.paintIcon ( c, g, findCenteredX ( x, icon.getIconWidth () ), y - icon.getIconHeight () / 2 );
    }

    /**
     * Returns centered x coordinate for the icon.
     *
     * @param x         x coordinate
     * @param iconWidth icon width
     * @return centered x coordinate
     */
    protected int findCenteredX ( final int x, final int iconWidth )
    {
        return leftToRight ? ( x - 2 - ( int ) Math.ceil ( iconWidth / 2.0 ) ) : ( x - 1 - ( int ) Math.floor ( iconWidth / 2.0 ) );
    }

    /**
     * Repaints all rectangles containing tree selections.
     * This method is optimized to repaint only those area which are actually has selection in them.
     */
    protected void repaintSelection ()
    {
        if ( tree.getSelectionCount () > 0 )
        {
            for ( final Rectangle rect : getSelectionRects () )
            {
                tree.repaint ( rect );
            }
        }
    }

    /**
     * Returns list of tree selections bounds.
     * This method takes selection style into account.
     *
     * @return list of tree selections bounds
     */
    protected List<Rectangle> getSelectionRects ()
    {
        // Return empty selection rects when custom selection painting is disabled
        if ( selectionStyle == TreeSelectionStyle.none )
        {
            return Collections.emptyList ();
        }

        // Checking that selection exists
        final int[] rows = tree.getSelectionRows ();
        if ( rows == null || rows.length == 0 )
        {
            return Collections.emptyList ();
        }

        // Sorting selected rows
        Arrays.sort ( rows );

        // Calculating selection rects
        final List<Rectangle> selections = new ArrayList<Rectangle> ( tree.getSelectionCount () );
        final Insets insets = tree.getInsets ();
        Rectangle maxRect = null;
        int lastRow = -1;
        for ( final int row : rows )
        {
            if ( selectionStyle == TreeSelectionStyle.single )
            {
                // Required bounds
                selections.add ( tree.getRowBounds ( row ) );
            }
            else
            {
                if ( lastRow != -1 && lastRow + 1 != row )
                {
                    // Save determined group
                    selections.add ( maxRect );

                    // Reset counting
                    maxRect = null;
                    lastRow = -1;
                }
                if ( lastRow == -1 || lastRow + 1 == row )
                {
                    // Required bounds
                    final Rectangle b = tree.getRowBounds ( row );
                    if ( isFullLineSelection () )
                    {
                        b.x = insets.left;
                        b.width = tree.getWidth () - insets.left - insets.right;
                    }

                    // Increase rect
                    maxRect = lastRow == -1 ? b : GeometryUtils.getContainingRect ( maxRect, b );

                    // Remember last row
                    lastRow = row;
                }
            }
        }
        if ( maxRect != null )
        {
            selections.add ( maxRect );
        }
        return selections;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected int getHorizontalLegBuffer ()
    {
        return -2;
    }

    /**
     * Paints tree.
     *
     * @param g graphics
     * @param c component
     */
    @Override
    public void paint ( final Graphics g, final JComponent c )
    {
        // Initial variables validation
        if ( tree != c )
        {
            throw new InternalError ( "incorrect component" );
        }
        if ( treeState == null )
        {
            return;
        }

        final Graphics2D g2d = ( Graphics2D ) g;

        // Cells selection
        paintSelection ( g2d );

        // Rollover cell
        paintRolloverNodeHighlight ( g2d );

        // Painting tree
        paintTree ( g2d );

        // Drop cell
        paintDropLocation ( g2d );

        // Multiselector
        paintMultiselector ( g2d );
    }

    /**
     * Paints special WebLaF tree nodes selection.
     * It is rendered separately from nodes allowing you to simplify your tree cell renderer component.
     *
     * @param g2d graphics context
     */
    protected void paintSelection ( final Graphics2D g2d )
    {
        if ( tree.getSelectionCount () > 0 )
        {
            // Draw final selections
            final List<Rectangle> selections = getSelectionRects ();
            for ( final Rectangle rect : selections )
            {
                // Bounds fix
                rect.x += selectionShadeWidth;
                rect.y += selectionShadeWidth;
                rect.width -= selectionShadeWidth * 2 + ( selectionBorderColor != null ? 1 : 0 );
                rect.height -= selectionShadeWidth * 2 + ( selectionBorderColor != null ? 1 : 0 );

                // Painting selection
                LafUtils.drawCustomWebBorder ( g2d, tree,
                        new RoundRectangle2D.Double ( rect.x, rect.y, rect.width, rect.height, selectionRound * 2, selectionRound * 2 ),
                        StyleConstants.shadeColor, selectionShadeWidth, true, webColoredSelection, selectionBorderColor,
                        selectionBorderColor, selectionBackgroundColor );
            }
        }
    }

    /**
     * Paints rollover node highlight.
     *
     * @param g2d graphics context
     */
    protected void paintRolloverNodeHighlight ( final Graphics2D g2d )
    {
        if ( tree.isEnabled () && highlightRolloverNode && selectionStyle != TreeSelectionStyle.none && rolloverRow != -1 &&
                !tree.isRowSelected ( rolloverRow ) )
        {
            final Rectangle rect = isFullLineSelection () ? getFullRowBounds ( rolloverRow ) : tree.getRowBounds ( rolloverRow );
            if ( rect != null )
            {
                // Bounds fix
                rect.x += selectionShadeWidth;
                rect.y += selectionShadeWidth;
                rect.width -= selectionShadeWidth * 2 + ( selectionBorderColor != null ? 1 : 0 );
                rect.height -= selectionShadeWidth * 2 + ( selectionBorderColor != null ? 1 : 0 );

                // Painting transparent node selection
                final Composite old = GraphicsUtils.setupAlphaComposite ( g2d, 0.35f );
                LafUtils.drawCustomWebBorder ( g2d, tree,
                        new RoundRectangle2D.Double ( rect.x, rect.y, rect.width, rect.height, selectionRound * 2, selectionRound * 2 ),
                        StyleConstants.shadeColor, selectionShadeWidth, true, webColoredSelection, selectionBorderColor,
                        selectionBorderColor, selectionBackgroundColor );
                GraphicsUtils.restoreComposite ( g2d, old );
            }
        }
    }

    /**
     * Paints all base tree elements.
     * This is almost the same method as in BasicTreeUI but it doesn't paint drop line.
     *
     * @param g2d graphics context
     */
    protected void paintTree ( final Graphics2D g2d )
    {
        final Rectangle paintBounds = g2d.getClipBounds ();
        final Insets insets = tree.getInsets ();
        final TreePath initialPath = getClosestPathForLocation ( tree, 0, paintBounds.y );
        final Enumeration paintingEnumerator = treeState.getVisiblePathsFrom ( initialPath );
        final int endY = paintBounds.y + paintBounds.height;
        int row = treeState.getRowForPath ( initialPath );

        drawingCache.clear ();

        if ( initialPath != null && paintingEnumerator != null )
        {
            TreePath parentPath = initialPath;

            // Draw the lines, knobs, and rows

            // Find each parent and have them draw a line to their last child
            parentPath = parentPath.getParentPath ();
            while ( parentPath != null )
            {
                paintVerticalPartOfLeg ( g2d, paintBounds, insets, parentPath );
                drawingCache.put ( parentPath, Boolean.TRUE );
                parentPath = parentPath.getParentPath ();
            }


            // Information for the node being rendered.
            final Rectangle boundsBuffer = new Rectangle ();
            final boolean rootVisible = isRootVisible ();
            boolean isExpanded;
            boolean hasBeenExpanded;
            boolean isLeaf;
            Rectangle bounds;
            TreePath path;

            boolean done = false;
            while ( !done && paintingEnumerator.hasMoreElements () )
            {
                path = ( TreePath ) paintingEnumerator.nextElement ();
                if ( path != null )
                {
                    isLeaf = treeModel.isLeaf ( path.getLastPathComponent () );
                    if ( isLeaf )
                    {
                        isExpanded = hasBeenExpanded = false;
                    }
                    else
                    {
                        isExpanded = treeState.getExpandedState ( path );
                        hasBeenExpanded = tree.hasBeenExpanded ( path );
                    }
                    bounds = getPathBounds ( path, insets, boundsBuffer );
                    if ( bounds == null )
                    {
                        // This will only happen if the model changes out
                        // from under us (usually in another thread).
                        // Swing isn't multithreaded, but I'll put this
                        // check in anyway.
                        return;
                    }
                    // See if the vertical line to the parent has been drawn.
                    parentPath = path.getParentPath ();
                    if ( parentPath != null )
                    {
                        if ( drawingCache.get ( parentPath ) == null )
                        {
                            paintVerticalPartOfLeg ( g2d, paintBounds, insets, parentPath );
                            drawingCache.put ( parentPath, Boolean.TRUE );
                        }
                        paintHorizontalPartOfLeg ( g2d, paintBounds, insets, bounds, path, row, isExpanded, hasBeenExpanded, isLeaf );
                    }
                    else if ( rootVisible && row == 0 )
                    {
                        paintHorizontalPartOfLeg ( g2d, paintBounds, insets, bounds, path, row, isExpanded, hasBeenExpanded, isLeaf );
                    }
                    if ( shouldPaintExpandControl ( path, row, isExpanded, hasBeenExpanded, isLeaf ) )
                    {
                        paintExpandControl ( g2d, paintBounds, insets, bounds, path, row, isExpanded, hasBeenExpanded, isLeaf );
                    }
                    paintRow ( g2d, paintBounds, insets, bounds, path, row, isExpanded, hasBeenExpanded, isLeaf );
                    if ( ( bounds.y + bounds.height ) >= endY )
                    {
                        done = true;
                    }
                }
                else
                {
                    done = true;
                }
                row++;
            }
        }

        // Empty out the renderer pane, allowing renderers to be gc'ed.
        rendererPane.removeAll ();
    }

    /**
     * Paints the horizontal part of the leg.
     *
     * @param g               graphics
     * @param clipBounds      clip bounds
     * @param insets          tree insets
     * @param bounds          tree path bounds
     * @param path            tree path
     * @param row             row index
     * @param isExpanded      whether row is expanded or not
     * @param hasBeenExpanded whether row has been expanded once before or not
     * @param isLeaf          whether node is leaf or not
     */
    @Override
    protected void paintHorizontalPartOfLeg ( final Graphics g, final Rectangle clipBounds, final Insets insets, final Rectangle bounds,
                                              final TreePath path, final int row, final boolean isExpanded, final boolean hasBeenExpanded,
                                              final boolean isLeaf )
    {
        if ( !isPaintLines () )
        {
            return;
        }
        super.paintHorizontalPartOfLeg ( g, clipBounds, insets, bounds, path, row, isExpanded, hasBeenExpanded, isLeaf );
    }

    /**
     * Paints the vertical part of the leg.
     *
     * @param g          graphics
     * @param clipBounds clip bounds
     * @param insets     tree insets
     * @param path       tree path
     */
    @Override
    protected void paintVerticalPartOfLeg ( final Graphics g, final Rectangle clipBounds, final Insets insets, final TreePath path )
    {
        if ( !isPaintLines () )
        {
            return;
        }
        super.paintVerticalPartOfLeg ( g, clipBounds, insets, path );
    }

    /**
     * Paints drop location if it is available.
     *
     * @param g2d graphics context
     */
    protected void paintDropLocation ( final Graphics2D g2d )
    {
        final JTree.DropLocation dropLocation = tree.getDropLocation ();
        if ( dropLocation != null )
        {
            final TreePath dropPath = dropLocation.getPath ();
            if ( isDropLine ( dropLocation ) )
            {
                // Painting drop line (between nodes)
                final Color background = tree.getBackground ();
                final Color dropLineColor = UIManager.getColor ( "Tree.dropLineColor" );
                final Color[] colors = { background, dropLineColor, dropLineColor, background };
                final Rectangle rect = getDropLineRect ( dropLocation );
                g2d.setPaint ( new LinearGradientPaint ( rect.x, rect.y, rect.x + rect.width, rect.y, fractions, colors ) );
                g2d.fillRect ( rect.x, rect.y, rect.width, 1 );
            }
            else
            {
                // Painting drop bounds (onto node)
                final Rectangle bounds = getNodeDropLocationBounds ( dropPath );
                final NinePatchIcon dropShade = NinePatchUtils.getShadeIcon ( dropCellShadeWidth, selectionRound, 1f );
                dropShade.paintIcon ( g2d, bounds );
            }
        }
    }

    /**
     * Returns node drop location drawing bounds.
     *
     * @param dropPath node path
     * @return node drop location drawing bounds
     */
    protected Rectangle getNodeDropLocationBounds ( final TreePath dropPath )
    {
        final Rectangle bounds = tree.getPathBounds ( dropPath );
        bounds.x -= dropCellShadeWidth;
        bounds.y -= dropCellShadeWidth;
        bounds.width += dropCellShadeWidth * 2;
        bounds.height += dropCellShadeWidth * 2;
        return bounds;
    }

    /**
     * Paints custom WebLaF multiselector.
     *
     * @param g2d graphics context
     */
    protected void paintMultiselector ( final Graphics2D g2d )
    {
        if ( isSelectorAvailable () && selectionStart != null && selectionEnd != null )
        {
            final Object aa = GraphicsUtils.setupAntialias ( g2d );
            final Stroke os = GraphicsUtils.setupStroke ( g2d, selectorStroke );

            final Rectangle sb = GeometryUtils.getContainingRect ( selectionStart, selectionEnd );
            final Rectangle fsb = sb.intersection ( SwingUtils.size ( tree ) );
            fsb.width -= 1;
            fsb.height -= 1;

            g2d.setPaint ( selectorColor );
            g2d.fill ( getSelectionShape ( fsb, true ) );

            g2d.setPaint ( selectorBorderColor );
            g2d.draw ( getSelectionShape ( fsb, false ) );

            GraphicsUtils.restoreStroke ( g2d, os );
            GraphicsUtils.restoreAntialias ( g2d, aa );
        }
    }

    /**
     * Returns whether the specified drop location should be displayed as line or not.
     *
     * @param loc drop location
     * @return true if the specified drop location should be displayed as line, false otherwise
     */
    protected boolean isDropLine ( final JTree.DropLocation loc )
    {
        return loc != null && loc.getPath () != null && loc.getChildIndex () != -1;
    }

    /**
     * Returns drop line rectangle.
     *
     * @param loc drop location
     * @return drop line rectangle
     */
    protected Rectangle getDropLineRect ( final JTree.DropLocation loc )
    {
        final Rectangle rect;
        final TreePath path = loc.getPath ();
        final int index = loc.getChildIndex ();
        final Insets insets = tree.getInsets ();

        if ( tree.getRowCount () == 0 )
        {
            rect = new Rectangle ( insets.left, insets.top, tree.getWidth () - insets.left - insets.right, 0 );
        }
        else
        {
            final TreeModel model = getModel ();
            final Object root = model.getRoot ();

            if ( path.getLastPathComponent () == root && index >= model.getChildCount ( root ) )
            {

                rect = tree.getRowBounds ( tree.getRowCount () - 1 );
                rect.y = rect.y + rect.height;
                final Rectangle xRect;

                if ( !tree.isRootVisible () )
                {
                    xRect = tree.getRowBounds ( 0 );
                }
                else if ( model.getChildCount ( root ) == 0 )
                {
                    xRect = tree.getRowBounds ( 0 );
                    xRect.x += totalChildIndent;
                    xRect.width -= totalChildIndent + totalChildIndent;
                }
                else
                {
                    final TreePath lastChildPath = path.pathByAddingChild ( model.getChild ( root, model.getChildCount ( root ) - 1 ) );
                    xRect = tree.getPathBounds ( lastChildPath );
                }

                rect.x = xRect.x;
                rect.width = xRect.width;
            }
            else
            {
                rect = tree.getPathBounds ( path.pathByAddingChild ( model.getChild ( path.getLastPathComponent (), index ) ) );
            }
        }

        if ( rect.y != 0 )
        {
            rect.y--;
        }

        if ( !leftToRight )
        {
            rect.x = rect.x + rect.width - 80;
        }

        rect.width = 80;
        rect.height = 2;

        return rect;
    }

    /**
     * Returns path bounds used for painting.
     *
     * @param path   tree path
     * @param insets tree insets
     * @param bounds bounds buffer
     * @return path bounds
     */
    protected Rectangle getPathBounds ( final TreePath path, final Insets insets, Rectangle bounds )
    {
        bounds = treeState.getBounds ( path, bounds );
        if ( bounds != null )
        {
            if ( leftToRight )
            {
                bounds.x += insets.left;
            }
            else
            {
                bounds.x = tree.getWidth () - ( bounds.x + bounds.width ) - insets.right;
            }
            bounds.y += insets.top;
        }
        return bounds;
    }

    /**
     * Returns selection shape for specified selection bounds.
     *
     * @param sb   selection bounds
     * @param fill should fill the whole cell
     * @return selection shape for specified selection bounds
     */
    protected Shape getSelectionShape ( final Rectangle sb, final boolean fill )
    {
        final int shear = fill ? 1 : 0;
        if ( selectorRound > 0 )
        {
            return new RoundRectangle2D.Double ( sb.x + shear, sb.y + shear, sb.width - shear, sb.height - shear, selectorRound * 2,
                    selectorRound * 2 );
        }
        else
        {
            return new Rectangle2D.Double ( sb.x + shear, sb.y + shear, sb.width - shear, sb.height - shear );
        }
    }

    /**
     * Returns tree cell renderer pane.
     *
     * @return tree cell renderer pane
     */
    public CellRendererPane getCellRendererPane ()
    {
        return rendererPane;
    }
}