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
import com.alee.laf.StyleConstants;
import com.alee.laf.WebLookAndFeel;
import com.alee.utils.*;

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
import java.util.ArrayList;
import java.util.Arrays;
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

    /**
     * Tree listeners.
     */
    protected PropertyChangeListener propertyChangeListener;
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
    protected boolean ltr = true;

    /**
     * Returns an instance of the WebTreeUI for the specified component.
     * This tricky method is used by UIManager to create component UIs when needed.
     *
     * @param c component that will use UI instance
     * @return instance of the WebTreeUI
     */
    @SuppressWarnings ("UnusedParameters")
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

        this.ltr = tree.getComponentOrientation ().isLeftToRight ();

        // Default settings
        tree.setRowHeight ( -1 );
        tree.setVisibleRowCount ( 10 );

        // Orientation change listener
        propertyChangeListener = new PropertyChangeListener ()
        {
            @Override
            public void propertyChange ( final PropertyChangeEvent evt )
            {
                ltr = tree.getComponentOrientation ().isLeftToRight ();
            }
        };
        tree.addPropertyChangeListener ( WebLookAndFeel.COMPONENT_ORIENTATION_PROPERTY, propertyChangeListener );
        SwingUtils.setOrientation ( tree );

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
                    // Check that mouse did not hit actuall tree cell
                    if ( getRowForPoint ( e.getPoint (), false ) == -1 )
                    {
                        if ( isSelectorAvailable () )
                        {
                            // Avoiding selection start when pressed on tree expand handle
                            final TreePath path = getClosestPathForLocation ( tree, e.getX (), e.getY () );
                            if ( !isLocationInExpandControl ( path, e.getX (), e.getY () ) &&
                                    !isLocationInCheckBoxControl ( path, e.getX (), e.getY () ) )
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
                        else if ( isFullLineSelection () )
                        {
                            // Avoiding selection start when pressed on tree expand handle
                            final TreePath path = getClosestPathForLocation ( tree, e.getX (), e.getY () );
                            if ( !isLocationInExpandControl ( path, e.getX (), e.getY () ) &&
                                    !isLocationInCheckBoxControl ( path, e.getX (), e.getY () ) )
                            {
                                if ( tree.getSelectionModel ().getSelectionMode () == TreeSelectionModel.SINGLE_TREE_SELECTION )
                                {
                                    // Selection
                                    tree.setSelectionRow ( getRowForPoint ( e.getPoint (), true ) );
                                }
                            }
                        }
                    }
                }
            }

            @Override
            public void mouseDragged ( final MouseEvent e )
            {
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
                //                // Calculating selector pervious and current rects
                //                final Rectangle sb1 = GeometryUtils.getContainingRect ( selectionStart, selectionPrevEnd );
                //                final Rectangle sb2 = GeometryUtils.getContainingRect ( selectionStart, selectionEnd );
                //
                //                // Repainting final rect
                //                repaintSelector ( GeometryUtils.getContainingRect ( sb1, sb2 ) );

                // Replaced with full repaint due to strange tree lines painting bug
                tree.repaint ( tree.getVisibleRect () );
            }

            //            private void repaintSelector ( Rectangle fr )
            //            {
            //                //                // Expanding width and height to fully cover the selector
            //                //                fr.x -= 1;
            //                //                fr.y -= 1;
            //                //                fr.width += 2;
            //                //                fr.height += 2;
            //                //
            //                //                // Repainting selector area
            //                //                tree.repaint ( fr );
            //
            //                // Replaced with full repaint due to strange tree lines painting bug
            //                tree.repaint ();
            //            }

            @Override
            public void mouseEntered ( final MouseEvent e )
            {
                updateMouseover ( e );
            }

            @Override
            public void mouseExited ( final MouseEvent e )
            {
                clearMouseover ();
            }

            @Override
            public void mouseMoved ( final MouseEvent e )
            {
                updateMouseover ( e );
            }

            private void updateMouseover ( MouseEvent e )
            {
                if ( tree.isEnabled () && highlightRolloverNode )
                {
                    final int index = getRowForPoint ( e.getPoint () );
                    if ( rolloverRow != index )
                    {
                        final int oldRollover = rolloverRow;
                        rolloverRow = index;
                        updateRow ( index );
                        updateRow ( oldRollover );
                    }
                }
                else
                {
                    clearMouseover ();
                }
            }

            private void clearMouseover ()
            {
                final int oldRollover = rolloverRow;
                rolloverRow = -1;
                updateRow ( oldRollover );
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
        tree.removePropertyChangeListener ( WebLookAndFeel.COMPONENT_ORIENTATION_PROPERTY, propertyChangeListener );
        tree.removeTreeSelectionListener ( treeSelectionListener );
        tree.removeTreeExpansionListener ( treeExpansionListener );
        tree.removeMouseListener ( mouseAdapter );
        tree.removeMouseMotionListener ( mouseAdapter );

        super.uninstallUI ( c );
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
        this.selectionShadeWidth = shadeWidth;
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
        return selectionStyle.equals ( TreeSelectionStyle.line );
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
            b.x = 0;
            b.width = tree.getWidth ();
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
     * Paints horizontal part of tree structure lines.
     *
     * @param g               graphics
     * @param clipBounds      clip bounds
     * @param insets          insets
     * @param bounds          bounds
     * @param path            path
     * @param row             row index
     * @param isExpanded      whether node is expanded or not
     * @param hasBeenExpanded whether node was expanded atleast once or not
     * @param isLeaf          whether node is leaf or not
     */
    @Override
    protected void paintHorizontalPartOfLeg ( final Graphics g, final Rectangle clipBounds, final Insets insets, final Rectangle bounds,
                                              final TreePath path, final int row, final boolean isExpanded, final boolean hasBeenExpanded,
                                              final boolean isLeaf )
    {
        if ( !paintLines )
        {
            return;
        }

        // Don't paint the legs for the root'ish node if the
        final int depth = path.getPathCount () - 1;
        if ( ( depth == 0 || ( depth == 1 && !isRootVisible () ) ) && !getShowsRootHandles () )
        {
            return;
        }

        final int clipLeft = clipBounds.x;
        final int clipRight = clipBounds.x + clipBounds.width;
        final int clipTop = clipBounds.y;
        final int clipBottom = clipBounds.y + clipBounds.height;
        final int lineY = bounds.y + bounds.height / 2;

        if ( ltr )
        {
            final int leftX = bounds.x - getRightChildIndent ();
            final int nodeX = bounds.x - getHorizontalLegBuffer ();
            if ( lineY >= clipTop && lineY < clipBottom && nodeX >= clipLeft && leftX < clipRight && leftX < nodeX )
            {
                g.setColor ( getHashColor () );
                paintHorizontalLine ( g, tree, lineY, leftX, nodeX - 1 );
            }
        }
        else
        {
            final int nodeX = bounds.x + bounds.width + getHorizontalLegBuffer ();
            final int rightX = bounds.x + bounds.width + getRightChildIndent ();
            if ( lineY >= clipTop && lineY < clipBottom && rightX >= clipLeft && nodeX < clipRight && nodeX < rightX )
            {
                g.setColor ( getHashColor () );
                paintHorizontalLine ( g, tree, lineY, nodeX, rightX - 1 );
            }
        }
    }

    /**
     * Paints horizontal tree structure line.
     *
     * @param g     graphics
     * @param c     component
     * @param y     y coordinate
     * @param left  left side of the line
     * @param right right side of the line
     */
    @Override
    protected void paintHorizontalLine ( final Graphics g, final JComponent c, final int y, int left, int right )
    {
        // todo Causes incorrect line repaints
        left = ltr ? left + 4 : left - 4;
        right = ltr ? right + 4 : right - 4;
        left += ( left % 2 );
        for ( int x = left; x <= right; x += 2 )
        {
            g.drawLine ( x, y, x, y );
        }
    }

    /**
     * Paints vertical part of tree structure lines.
     *
     * @param g          graphics
     * @param clipBounds clip bounds
     * @param insets     insets
     * @param path       path
     */
    @Override
    protected void paintVerticalPartOfLeg ( final Graphics g, final Rectangle clipBounds, final Insets insets, final TreePath path )
    {
        if ( !paintLines )
        {
            return;
        }

        if ( !paintLines )
        {
            return;
        }

        final int depth = path.getPathCount () - 1;
        if ( depth == 0 && !getShowsRootHandles () && !isRootVisible () )
        {
            return;
        }
        int lineX = getRowX ( -1, depth + 1 );
        if ( ltr )
        {
            lineX = lineX - getRightChildIndent () + insets.left;
        }
        else
        {
            lineX = tree.getWidth () - lineX - insets.right + getRightChildIndent () - 1;
        }

        final int clipLeft = clipBounds.x;
        final int clipRight = clipBounds.x + ( clipBounds.width - 1 );

        if ( lineX >= clipLeft && lineX <= clipRight )
        {
            final int clipTop = clipBounds.y;
            final int clipBottom = clipBounds.y + clipBounds.height;
            final Rectangle lastChildBounds = getPathBounds ( tree, getLastChildPath ( path ) );
            Rectangle parentBounds = getPathBounds ( tree, path );

            // This shouldn't happen, but if the model is modified in another thread it is possible for this to happen.
            // Swing isn't multithreaded, but better to check this anyway.
            if ( lastChildBounds == null )
            {
                return;
            }

            int top;
            if ( parentBounds == null )
            {
                top = Math.max ( insets.top + getVerticalLegBuffer (), clipTop );
            }
            else
            {
                top = Math.max ( parentBounds.y + parentBounds.height + getVerticalLegBuffer (), clipTop );
            }

            if ( depth == 0 && !isRootVisible () )
            {
                final TreeModel model = getModel ();
                if ( model != null )
                {
                    final Object root = model.getRoot ();
                    if ( model.getChildCount ( root ) > 0 )
                    {
                        parentBounds = getPathBounds ( tree, path.pathByAddingChild ( model.getChild ( root, 0 ) ) );
                        if ( parentBounds != null )
                        {
                            top = Math.max ( insets.top + getVerticalLegBuffer (), parentBounds.y + parentBounds.height / 2 );
                        }
                    }
                }
            }

            final int bottom = Math.min ( lastChildBounds.y + ( lastChildBounds.height / 2 ), clipBottom );
            if ( top <= bottom )
            {
                g.setColor ( getHashColor () );
                paintVerticalLine ( g, tree, lineX, top, bottom );
            }
        }
    }

    /**
     * Paints vertical tree structure line.
     *
     * @param g      graphics
     * @param c      component
     * @param x      x coordinate
     * @param top    top side of the line
     * @param bottom bottom side of the line
     */
    @Override
    protected void paintVerticalLine ( final Graphics g, final JComponent c, int x, int top, final int bottom )
    {
        // todo Causes incorrect line repaints
        x = ltr ? x + 4 : x - 4;
        top += ( top % 2 );
        for ( int y = top; y <= bottom; y += 2 )
        {
            g.drawLine ( x, y, x, y );
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
    {
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
        return ltr ? x + 2 - ( int ) Math.ceil ( iconWidth / 2.0 ) : x - 2 - ( int ) Math.floor ( iconWidth / 2.0 ) - 3;
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
        final List<Rectangle> selections = new ArrayList<Rectangle> ();

        // Checking that selection exists
        final int[] rows = tree.getSelectionRows ();
        if ( rows == null )
        {
            return selections;
        }

        // Sorting selected rows
        Arrays.sort ( rows );

        // Calculating selection rects
        Rectangle maxRect = null;
        int lastRow = -1;
        for ( final int row : rows )
        {
            if ( selectionStyle.equals ( TreeSelectionStyle.single ) )
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
                        b.x = 0;
                        b.width = tree.getWidth ();

                        // todo Full line painting case
                        // b.x = 0 - selectionShadeWidth - selectionRound - 1;
                        // b.width = tree.getWidth () + selectionShadeWidth * 2 + selectionRound + 2;
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
     * Paints tree.
     *
     * @param g graphics
     * @param c component
     */
    @Override
    public void paint ( final Graphics g, final JComponent c )
    {
        // Cells selection
        if ( tree.getSelectionCount () > 0 )
        {
            // Draw final selections
            final List<Rectangle> selections = getSelectionRects ();
            for ( final Rectangle rect : selections )
            {
                LafUtils.drawCustomWebBorder ( ( Graphics2D ) g, tree,
                        new RoundRectangle2D.Double ( rect.x + selectionShadeWidth, rect.y + selectionShadeWidth,
                                rect.width - selectionShadeWidth * 2 - 1, rect.height - selectionShadeWidth * 2 - 1, selectionRound * 2,
                                selectionRound * 2 ), StyleConstants.shadeColor, selectionShadeWidth, true, true );
            }
        }

        // Rollover cell
        if ( tree.isEnabled () && highlightRolloverNode && rolloverRow != -1 && !tree.isRowSelected ( rolloverRow ) )
        {
            final Rectangle rect = isFullLineSelection () ? getFullRowBounds ( rolloverRow ) : tree.getRowBounds ( rolloverRow );
            if ( rect != null )
            {
                rect.x += selectionShadeWidth;
                rect.y += selectionShadeWidth;
                rect.width -= selectionShadeWidth * 2 + 1;
                rect.height -= selectionShadeWidth * 2 + 1;

                final Composite old = LafUtils.setupAlphaComposite ( ( Graphics2D ) g, 0.35f );
                LafUtils.drawCustomWebBorder ( ( Graphics2D ) g, tree,
                        new RoundRectangle2D.Double ( rect.x, rect.y, rect.width, rect.height, selectionRound * 2, selectionRound * 2 ),
                        StyleConstants.shadeColor, selectionShadeWidth, true, true );
                LafUtils.restoreComposite ( ( Graphics2D ) g, old );
            }
        }

        super.paint ( g, c );

        // Multiselector
        if ( isSelectorAvailable () && selectionStart != null && selectionEnd != null )
        {
            final Graphics2D g2d = ( Graphics2D ) g;
            final Object aa = LafUtils.setupAntialias ( g2d );
            final Stroke os = LafUtils.setupStroke ( g2d, selectorStroke );

            final Rectangle sb = GeometryUtils.getContainingRect ( selectionStart, selectionEnd );

            g2d.setPaint ( selectorColor );
            g2d.fill ( getSelectionShape ( sb, true ) );

            g2d.setPaint ( selectorBorderColor );
            g2d.draw ( getSelectionShape ( sb, false ) );

            LafUtils.restoreStroke ( g2d, os );
            LafUtils.restoreAntialias ( g2d, aa );
        }
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
}