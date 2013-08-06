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
 * @since 1.4
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
    private boolean autoExpandSelectedNode = WebTreeStyle.autoExpandSelectedPath;
    private boolean highlightRolloverNode = WebTreeStyle.highlightRolloverNode;
    private boolean paintLines = WebTreeStyle.paintLines;
    private Color linesColor = WebTreeStyle.linesColor;
    private TreeSelectionStyle selectionStyle = WebTreeStyle.selectionStyle;
    private int selectionRound = WebTreeStyle.selectionRound;
    private int selectionShadeWidth = WebTreeStyle.selectionShadeWidth;
    private boolean selectorEnabled = WebTreeStyle.selectorEnabled;
    private Color selectorColor = WebTreeStyle.selectorColor;
    private Color selectorBorderColor = WebTreeStyle.selectorBorderColor;
    private int selectorRound = WebTreeStyle.selectorRound;
    private BasicStroke selectorStroke = WebTreeStyle.selectorStroke;

    /**
     * Tree listeners.
     */
    private PropertyChangeListener propertyChangeListener;
    private TreeSelectionListener treeSelectionListener;
    private TreeExpansionListener treeExpansionListener;
    private MouseAdapter mouseAdapter;

    /**
     * Runtime variables.
     */
    private int rolloverRow = -1;
    private List<Integer> initialSelection = new ArrayList<Integer> ();
    private List<Integer> lastSelection = null;
    private Point selectionStart = null;
    private Point selectionPrevEnd = null;
    private Point selectionEnd = null;
    private boolean ltr = true;

    /**
     * Returns an instance of the WebTreeUI for the specified component.
     * This tricky method is used by UIManager to create component UIs when needed.
     *
     * @param c component that will use UI instance
     * @return instance of the WebTreeUI
     */
    public static ComponentUI createUI ( JComponent c )
    {
        return new WebTreeUI ();
    }

    /**
     * Installs UI in the specified component.
     *
     * @param c component for this UI
     */
    public void installUI ( JComponent c )
    {
        super.installUI ( c );

        this.ltr = tree.getComponentOrientation ().isLeftToRight ();

        // Default settings
        SwingUtils.setOrientation ( tree );
        tree.setRowHeight ( -1 );
        tree.setVisibleRowCount ( 10 );

        // Orientation change listener
        propertyChangeListener = new PropertyChangeListener ()
        {
            public void propertyChange ( PropertyChangeEvent evt )
            {
                ltr = tree.getComponentOrientation ().isLeftToRight ();
            }
        };
        tree.addPropertyChangeListener ( WebLookAndFeel.COMPONENT_ORIENTATION_PROPERTY, propertyChangeListener );

        // Selection listener
        treeSelectionListener = new TreeSelectionListener ()
        {
            public void valueChanged ( TreeSelectionEvent e )
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
            public void treeExpanded ( TreeExpansionEvent event )
            {
                repaintSelection ();
            }

            public void treeCollapsed ( TreeExpansionEvent event )
            {
                repaintSelection ();
            }
        };
        tree.addTreeExpansionListener ( treeExpansionListener );

        // Mouse events adapter
        mouseAdapter = new MouseAdapter ()
        {
            public void mousePressed ( MouseEvent e )
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
                            TreePath path = getClosestPathForLocation ( tree, e.getX (), e.getY () );
                            if ( !isLocationInExpandControl ( path, e.getX (), e.getY () ) )
                            {
                                // Selection
                                selectionStart = e.getPoint ();
                                selectionPrevEnd = selectionStart;
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
                            TreePath path = getClosestPathForLocation ( tree, e.getX (), e.getY () );
                            if ( !isLocationInExpandControl ( path, e.getX (), e.getY () ) )
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

            public void mouseDragged ( MouseEvent e )
            {
                if ( isSelectorAvailable () && selectionStart != null )
                {
                    // Selection
                    selectionPrevEnd = selectionEnd;
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

            public void mouseReleased ( MouseEvent e )
            {
                if ( isSelectorAvailable () && selectionStart != null )
                {
                    // Saving selection rect to repaint
                    Rectangle fr = GeometryUtils.getContainingRect ( selectionStart, selectionEnd );

                    // Selection
                    selectionStart = null;
                    selectionPrevEnd = null;
                    selectionEnd = null;

                    // Repainting selection on the tree
                    repaintSelector ( fr );
                }
            }

            private void validateSelection ( MouseEvent e )
            {
                // todo Possibly optimize selection? - modify it instead of overwriting each time

                // Selection rect
                Rectangle selection = GeometryUtils.getContainingRect ( selectionStart, selectionEnd );

                // Compute new selection
                List<Integer> newSelection = new ArrayList<Integer> ();
                if ( SwingUtils.isShift ( e ) )
                {

                    for ( int row = 0; row < tree.getRowCount (); row++ )
                    {
                        if ( getRowBounds ( row ).intersects ( selection ) && !initialSelection.contains ( row ) )
                        {
                            newSelection.add ( row );
                        }
                    }
                    for ( int row : initialSelection )
                    {
                        newSelection.add ( row );
                    }
                }
                else if ( SwingUtils.isCtrl ( e ) )
                {
                    List<Integer> excludedRows = new ArrayList<Integer> ();
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
                    for ( int row : initialSelection )
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
                List<Integer> selection = new ArrayList<Integer> ();
                final int[] selectionRows = tree.getSelectionRows ();
                if ( selectionRows != null )
                {
                    for ( int row : selectionRows )
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
                tree.repaint ();
            }

            private void repaintSelector ( Rectangle fr )
            {
                //                // Expanding width and height to fully cover the selector
                //                fr.x -= 1;
                //                fr.y -= 1;
                //                fr.width += 2;
                //                fr.height += 2;
                //
                //                // Repainting selector area
                //                tree.repaint ( fr );

                // Replaced with full repaint due to strange tree lines painting bug
                tree.repaint ();
            }

            public void mouseEntered ( MouseEvent e )
            {
                updateMouseover ( e );
            }

            public void mouseExited ( MouseEvent e )
            {
                clearMouseover ();
            }

            public void mouseMoved ( MouseEvent e )
            {
                updateMouseover ( e );
            }

            private void updateMouseover ( MouseEvent e )
            {
                if ( tree.isEnabled () && highlightRolloverNode )
                {
                    int index = getRowForPoint ( e.getPoint () );
                    if ( rolloverRow != index )
                    {
                        int oldRollover = rolloverRow;
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
                int oldRollover = rolloverRow;
                rolloverRow = -1;
                updateRow ( oldRollover );
            }

            private void updateRow ( int row )
            {
                if ( row != -1 )
                {
                    Rectangle rowBounds = getFullRowBounds ( row );
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
    public void uninstallUI ( JComponent c )
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
    public void setAutoExpandSelectedNode ( boolean autoExpandSelectedNode )
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
    public void setHighlightRolloverNode ( boolean highlight )
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
    public void setPaintLines ( boolean paint )
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
    public void setLinesColor ( Color color )
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
    public void setSelectionStyle ( TreeSelectionStyle style )
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
    public void setSelectionRound ( int round )
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
    public void setSelectionShadeWidth ( int shadeWidth )
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
    public void setSelectorEnabled ( boolean enabled )
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
    public void setSelectorColor ( Color color )
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
    public void setSelectorBorderColor ( Color color )
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
    public void setSelectorRound ( int round )
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
    public void setSelectorStroke ( BasicStroke stroke )
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
    public int getRowForPoint ( Point point )
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
    public int getRowForPoint ( Point point, boolean countFullRow )
    {
        if ( tree != null )
        {
            for ( int row = 0; row < tree.getRowCount (); row++ )
            {
                Rectangle bounds = getRowBounds ( row, countFullRow );
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
    public Rectangle getRowBounds ( int row )
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
    public Rectangle getRowBounds ( int row, boolean countFullRow )
    {
        return countFullRow ? getFullRowBounds ( row ) : tree.getRowBounds ( row );
    }

    /**
     * Returns full row bounds by its index.
     *
     * @param row row index
     * @return full row bounds by its index
     */
    public Rectangle getFullRowBounds ( int row )
    {
        Rectangle b = tree.getRowBounds ( row );
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
    protected TreeCellEditor createDefaultCellEditor ()
    {
        return new WebTreeCellEditor ();
    }

    /**
     * Returns default tree cell renderer.
     *
     * @return default tree cell renderer
     */
    protected TreeCellRenderer createDefaultCellRenderer ()
    {
        return new WebTreeCellRenderer ();
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
    protected void paintHorizontalPartOfLeg ( Graphics g, Rectangle clipBounds, Insets insets, Rectangle bounds, TreePath path, int row,
                                              boolean isExpanded, boolean hasBeenExpanded, boolean isLeaf )
    {
        if ( !paintLines )
        {
            return;
        }

        // Don't paint the legs for the root'ish node if the
        int depth = path.getPathCount () - 1;
        if ( ( depth == 0 || ( depth == 1 && !isRootVisible () ) ) && !getShowsRootHandles () )
        {
            return;
        }

        int clipLeft = clipBounds.x;
        int clipRight = clipBounds.x + clipBounds.width;
        int clipTop = clipBounds.y;
        int clipBottom = clipBounds.y + clipBounds.height;
        int lineY = bounds.y + bounds.height / 2;

        if ( ltr )
        {
            int leftX = bounds.x - getRightChildIndent ();
            int nodeX = bounds.x - getHorizontalLegBuffer ();

            if ( lineY >= clipTop && lineY < clipBottom && nodeX >= clipLeft && leftX < clipRight &&
                    leftX < nodeX )
            {

                g.setColor ( getHashColor () );
                paintHorizontalLine ( g, tree, lineY, leftX, nodeX - 1 );
            }
        }
        else
        {
            int nodeX = bounds.x + bounds.width + getHorizontalLegBuffer ();
            int rightX = bounds.x + bounds.width + getRightChildIndent ();

            if ( lineY >= clipTop && lineY < clipBottom && rightX >= clipLeft &&
                    nodeX < clipRight && nodeX < rightX )
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
    protected void paintHorizontalLine ( Graphics g, JComponent c, int y, int left, int right )
    {
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
    protected void paintVerticalPartOfLeg ( Graphics g, Rectangle clipBounds, Insets insets, TreePath path )
    {
        if ( !paintLines )
        {
            return;
        }

        if ( !paintLines )
        {
            return;
        }

        int depth = path.getPathCount () - 1;
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
        int clipLeft = clipBounds.x;
        int clipRight = clipBounds.x + ( clipBounds.width - 1 );

        if ( lineX >= clipLeft && lineX <= clipRight )
        {
            int clipTop = clipBounds.y;
            int clipBottom = clipBounds.y + clipBounds.height;
            Rectangle parentBounds = getPathBounds ( tree, path );
            Rectangle lastChildBounds = getPathBounds ( tree, getLastChildPath ( path ) );

            if ( lastChildBounds == null )
            // This shouldn't happen, but if the model is modified
            // in another thread it is possible for this to happen.
            // Swing isn't multithreaded, but I'll add this check in
            // anyway.
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
                top = Math.max ( parentBounds.y + parentBounds.height +
                        getVerticalLegBuffer (), clipTop );
            }
            if ( depth == 0 && !isRootVisible () )
            {
                TreeModel model = getModel ();

                if ( model != null )
                {
                    Object root = model.getRoot ();

                    if ( model.getChildCount ( root ) > 0 )
                    {
                        parentBounds = getPathBounds ( tree, path.
                                pathByAddingChild ( model.getChild ( root, 0 ) ) );
                        if ( parentBounds != null )
                        {
                            top = Math.max ( insets.top + getVerticalLegBuffer (), parentBounds.y + parentBounds.height / 2 );
                        }
                    }
                }
            }

            int bottom = Math.min ( lastChildBounds.y + ( lastChildBounds.height / 2 ), clipBottom );

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
    protected void paintVerticalLine ( Graphics g, JComponent c, int x, int top, int bottom )
    {
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
    protected Color getHashColor ()
    {
        return linesColor;
    }

    /**
     * Returns tree structure expanded node icon.
     *
     * @return tree structure expanded node icon
     */
    public Icon getExpandedIcon ()
    {
        return tree.isEnabled () ? COLLAPSE_ICON : DISABLED_COLLAPSE_ICON;
    }

    /**
     * Returns tree structure collapsed node icon.
     *
     * @return tree structure collapsed node icon
     */
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
    protected void drawCentered ( Component c, Graphics g, Icon icon, int x, int y )
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
    private int findCenteredX ( int x, int iconWidth )
    {
        return ltr ? x + 2 - ( int ) Math.ceil ( iconWidth / 2.0 ) : x - 2 - ( int ) Math.floor ( iconWidth / 2.0 ) - 3;
    }

    /**
     * Repaints all rectangles containing tree selections.
     * This method is optimized to repaint only those area which are actually has selection in them.
     */
    private void repaintSelection ()
    {
        if ( tree.getSelectionCount () > 0 )
        {
            for ( Rectangle rect : getSelectionRects () )
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
    private List<Rectangle> getSelectionRects ()
    {
        List<Rectangle> selections = new ArrayList<Rectangle> ();

        // Checking that selection exists
        int[] rows = tree.getSelectionRows ();
        if ( rows == null )
        {
            return selections;
        }

        // Sorting selected rows
        Arrays.sort ( rows );

        // Calculating selection rects
        Rectangle maxRect = null;
        int lastRow = -1;
        for ( int row : rows )
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
                    Rectangle b = tree.getRowBounds ( row );
                    if ( isFullLineSelection () )
                    {
                        b.x = 0;
                        b.width = tree.getWidth ();
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
    public void paint ( Graphics g, JComponent c )
    {
        // Cells selection
        if ( tree.getSelectionCount () > 0 )
        {
            // Draw final selections
            List<Rectangle> selections = getSelectionRects ();
            for ( Rectangle rect : selections )
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
            Rectangle rect = isFullLineSelection () ? getFullRowBounds ( rolloverRow ) : tree.getRowBounds ( rolloverRow );
            if ( rect != null )
            {
                rect.x += selectionShadeWidth;
                rect.y += selectionShadeWidth;
                rect.width -= selectionShadeWidth * 2 + 1;
                rect.height -= selectionShadeWidth * 2 + 1;

                Composite old = LafUtils.setupAlphaComposite ( ( Graphics2D ) g, 0.35f );
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
            Graphics2D g2d = ( Graphics2D ) g;
            LafUtils.setupAntialias ( g2d );
            Object aa = LafUtils.setupAntialias ( g2d );
            Stroke os = LafUtils.setupStroke ( g2d, selectorStroke );

            Rectangle sb = GeometryUtils.getContainingRect ( selectionStart, selectionEnd );

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
    private Shape getSelectionShape ( Rectangle sb, boolean fill )
    {
        int shear = fill ? 1 : 0;
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