package com.alee.managers.style.skin.web;

import com.alee.extended.painter.AbstractPainter;
import com.alee.global.StyleConstants;
import com.alee.laf.WebLookAndFeel;
import com.alee.laf.tree.*;
import com.alee.managers.tooltip.ToolTipProvider;
import com.alee.utils.*;
import com.alee.utils.ninepatch.NinePatchIcon;

import javax.swing.*;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
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
 * @author Alexandr Zernov
 */

public class WebTreePainter<E extends JTree, U extends WebTreeUI> extends AbstractPainter<E, U> implements TreePainter<E, U>
{
    /**
     * Default drop line gradient fractions.
     */
    protected static final float[] fractions = { 0, 0.25f, 0.75f, 1f };

    /**
     * Style settings.
     */
    protected int selectionRound = WebTreeStyle.selectionRound;
    protected int selectionShadeWidth = WebTreeStyle.selectionShadeWidth;
    protected int selectorRound = WebTreeStyle.selectorRound;
    protected int dropCellShadeWidth = WebTreeStyle.dropCellShadeWidth;
    protected BasicStroke selectorStroke = WebTreeStyle.selectorStroke;
    protected Color linesColor = WebTreeStyle.linesColor;
    protected Color selectorColor = WebTreeStyle.selectorColor;
    protected Color selectorBorderColor = WebTreeStyle.selectorBorderColor;
    protected Color selectionBorderColor = WebTreeStyle.selectionBorderColor;
    protected Color selectionBackgroundColor = WebTreeStyle.selectionBackgroundColor;
    protected boolean paintLines = WebTreeStyle.paintLines;
    protected boolean webColoredSelection = WebTreeStyle.webColoredSelection;
    protected boolean selectorEnabled = WebTreeStyle.selectorEnabled;

    /**
     * Listeners.
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
     * Painting variables.
     */
    protected boolean lineTypeDashed = false;
    protected int totalChildIndent;
    protected int depthOffset;
    protected TreeModel treeModel;
    protected AbstractLayoutCache treeState;
    protected Hashtable<TreePath, Boolean> drawingCache;
    protected CellRendererPane rendererPane;
    protected TreeCellRenderer currentCellRenderer;
    protected int editingRow = -1;
    protected int lastSelectionRow = -1;

    /**
     * {@inheritDoc}
     */
    @Override
    public void install ( final E c, final U ui )
    {
        super.install ( c, ui );

        leftToRight = component.getComponentOrientation ().isLeftToRight ();

        // Orientation change listener
        orientationChangeListener = new PropertyChangeListener ()
        {
            @Override
            public void propertyChange ( final PropertyChangeEvent evt )
            {
                leftToRight = component.getComponentOrientation ().isLeftToRight ();
            }
        };
        component.addPropertyChangeListener ( WebLookAndFeel.ORIENTATION_PROPERTY, orientationChangeListener );
        SwingUtils.setOrientation ( component );

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
                    component.repaint ( getNodeDropLocationBounds ( oldLocation.getPath () ) );
                }

                // Repainting current drop location
                final JTree.DropLocation newLocation = ( JTree.DropLocation ) evt.getNewValue ();
                if ( newLocation != null )
                {
                    component.repaint ( getNodeDropLocationBounds ( newLocation.getPath () ) );
                }
            }
        };
        component.addPropertyChangeListener ( WebLookAndFeel.DROP_LOCATION, dropLocationChangeListener );

        // Selection listener
        treeSelectionListener = new TreeSelectionListener ()
        {
            @Override
            public void valueChanged ( final TreeSelectionEvent e )
            {
                // Optimized selection repaint
                repaintSelection ();

                // Tree expansion on selection
                if ( ui.isAutoExpandSelectedNode () && component.getSelectionCount () > 0 )
                {
                    component.expandPath ( component.getSelectionPath () );
                }
            }
        };
        component.addTreeSelectionListener ( treeSelectionListener );

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
        component.addTreeExpansionListener ( treeExpansionListener );

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
                    if ( ui.getRowForPoint ( e.getPoint (), false ) == -1 )
                    {
                        if ( isSelectorAvailable () )
                        {
                            // Avoiding selection start when pressed on tree expand handle
                            final TreePath path = ui.getClosestPathForLocation ( component, e.getX (), e.getY () );
                            if ( path == null || !isLocationInExpandControl ( path, e.getX (), e.getY () ) &&
                                    !ui.isLocationInCheckBoxControl ( path, e.getX (), e.getY () ) )
                            {
                                // Avoid starting multiselection if row is selected and drag is possible
                                final int rowForPath = ui.getRowForPath ( component, path );
                                if ( isDragAvailable () && rowForPath != -1 &&
                                        ui.getRowBounds ( rowForPath ).contains ( e.getX (), e.getY () ) &&
                                        component.isRowSelected ( rowForPath ) )
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
                            final TreePath path = ui.getClosestPathForLocation ( component, e.getX (), e.getY () );
                            if ( path != null && !isLocationInExpandControl ( path, e.getX (), e.getY () ) &&
                                    !ui.isLocationInCheckBoxControl ( path, e.getX (), e.getY () ) )
                            {
                                // Single row selection
                                if ( component.getSelectionModel ().getSelectionMode () == TreeSelectionModel.SINGLE_TREE_SELECTION )
                                {
                                    component.setSelectionRow ( ui.getRowForPoint ( e.getPoint (), true ) );
                                }

                                // Marking row to be dragged
                                final int rowForPath = ui.getRowForPath ( component, path );
                                if ( isDragAvailable () && ui.getRowBounds ( rowForPath ).contains ( e.getX (), e.getY () ) &&
                                        component.isRowSelected ( rowForPath ) )
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
                    final TransferHandler transferHandler = component.getTransferHandler ();
                    transferHandler.exportAsDrag ( component, e, transferHandler.getSourceActions ( component ) );
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

                    if ( !component.getVisibleRect ().contains ( e.getPoint () ) )
                    {
                        component.scrollRectToVisible ( new Rectangle ( e.getPoint (), new Dimension ( 0, 0 ) ) );
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
                final java.util.List<Integer> newSelection = new ArrayList<Integer> ();
                if ( SwingUtils.isShift ( e ) )
                {
                    for ( int row = 0; row < component.getRowCount (); row++ )
                    {
                        if ( ui.getRowBounds ( row ).intersects ( selection ) && !initialSelection.contains ( row ) )
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
                    final java.util.List<Integer> excludedRows = new ArrayList<Integer> ();
                    for ( int row = 0; row < component.getRowCount (); row++ )
                    {
                        if ( ui.getRowBounds ( row ).intersects ( selection ) )
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
                    for ( int row = 0; row < component.getRowCount (); row++ )
                    {
                        if ( ui.getRowBounds ( row ).intersects ( selection ) )
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
                        component.setSelectionRows ( CollectionUtils.toArray ( newSelection ) );
                    }
                    else
                    {
                        component.clearSelection ();
                    }
                }
            }

            private java.util.List<Integer> getSelectionRowsList ()
            {
                final java.util.List<Integer> selection = new ArrayList<Integer> ();
                final int[] selectionRows = component.getSelectionRows ();
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
                component.repaint ( component.getVisibleRect () );
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
                if ( component.isEnabled () )
                {
                    final int index = ui.getRowForPoint ( e.getPoint () );
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
                if ( ui.isHighlightRolloverNode () )
                {
                    updateRow ( oldIndex );
                    updateRow ( newIndex );
                }

                // Updating custom WebLaF tooltip display state
                final ToolTipProvider provider = getToolTipProvider ();
                if ( provider != null )
                {
                    provider.rolloverCellChanged ( component, oldIndex, 0, newIndex, 0 );
                }
            }

            private void updateRow ( final int row )
            {
                if ( row != -1 )
                {
                    final Rectangle rowBounds = ui.getFullRowBounds ( row );
                    if ( rowBounds != null )
                    {
                        component.repaint ( rowBounds );
                    }
                }
            }
        };
        component.addMouseListener ( mouseAdapter );
        component.addMouseMotionListener ( mouseAdapter );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void uninstall ( final E c, final U ui )
    {
        // Removing listeners
        component.removePropertyChangeListener ( WebLookAndFeel.ORIENTATION_PROPERTY, orientationChangeListener );
        component.removePropertyChangeListener ( WebLookAndFeel.DROP_LOCATION, dropLocationChangeListener );
        component.removeTreeSelectionListener ( treeSelectionListener );
        component.removeTreeExpansionListener ( treeExpansionListener );
        component.removeMouseListener ( mouseAdapter );
        component.removeMouseMotionListener ( mouseAdapter );

        super.uninstall ( c, ui );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void paint ( final Graphics2D g2d, final Rectangle bounds, final E c, final U ui )
    {
        // Initial variables validation
        if ( component != c )
        {
            throw new InternalError ( "incorrect component" );
        }


        // Prepare to paint
        treeState = ui.getTreeState ();
        if ( treeState == null )
        {
            return;
        }
        treeModel = component.getModel ();
        totalChildIndent = ui.getLeftChildIndent () + ui.getRightChildIndent ();
        rendererPane = ui.getCellRendererPane ();
        lastSelectionRow = component.getLeadSelectionRow ();
        final TreePath editingPath = component.getEditingPath ();
        editingRow = editingPath != null ? component.getRowForPath ( editingPath ) : -1;
        updateDepthOffset ();

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

        treeModel = null;
        treeState = null;
        drawingCache = null;
        rendererPane = null;
    }

    @Override
    public void prepareToPaint ( final Hashtable<TreePath, Boolean> drawingCache, final TreeCellRenderer currentCellRenderer )
    {
        this.drawingCache = drawingCache;
        this.currentCellRenderer = currentCellRenderer;
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
        if ( component.getSelectionCount () > 0 )
        {
            for ( final Rectangle rect : getSelectionRects () )
            {
                component.repaint ( rect );
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
        if ( ui.getSelectionStyle () == TreeSelectionStyle.none )
        {
            return Collections.emptyList ();
        }

        // Checking that selection exists
        final int[] rows = component.getSelectionRows ();
        if ( rows == null || rows.length == 0 )
        {
            return Collections.emptyList ();
        }

        // Sorting selected rows
        Arrays.sort ( rows );

        // Calculating selection rects
        final List<Rectangle> selections = new ArrayList<Rectangle> ( component.getSelectionCount () );
        final Insets insets = component.getInsets ();
        Rectangle maxRect = null;
        int lastRow = -1;
        for ( final int row : rows )
        {
            if ( ui.getSelectionStyle () == TreeSelectionStyle.single )
            {
                // Required bounds
                selections.add ( component.getRowBounds ( row ) );
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
                    final Rectangle b = component.getRowBounds ( row );
                    if ( isFullLineSelection () )
                    {
                        b.x = insets.left;
                        b.width = component.getWidth () - insets.left - insets.right;
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

    protected int getHorizontalLegBuffer ()
    {
        return -2;
    }

    /**
     * Paints special WebLaF tree nodes selection.
     * It is rendered separately from nodes allowing you to simplify your tree cell renderer component.
     *
     * @param g2d graphics context
     */
    protected void paintSelection ( final Graphics2D g2d )
    {
        if ( component.getSelectionCount () > 0 )
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
                LafUtils.drawCustomWebBorder ( g2d, component,
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
        if ( component.isEnabled () && ui.isHighlightRolloverNode () && ui.getSelectionStyle () != TreeSelectionStyle.none &&
                rolloverRow != -1 && !component.isRowSelected ( rolloverRow ) )
        {
            final Rectangle rect = isFullLineSelection () ? ui.getFullRowBounds ( rolloverRow ) : component.getRowBounds ( rolloverRow );
            if ( rect != null )
            {
                // Bounds fix
                rect.x += selectionShadeWidth;
                rect.y += selectionShadeWidth;
                rect.width -= selectionShadeWidth * 2 + ( selectionBorderColor != null ? 1 : 0 );
                rect.height -= selectionShadeWidth * 2 + ( selectionBorderColor != null ? 1 : 0 );

                // Painting transparent node selection
                final Composite old = GraphicsUtils.setupAlphaComposite ( g2d, 0.35f );
                LafUtils.drawCustomWebBorder ( g2d, component,
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
        final Insets insets = component.getInsets ();
        final TreePath initialPath = ui.getClosestPathForLocation ( component, 0, paintBounds.y );
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
                        hasBeenExpanded = component.hasBeenExpanded ( path );
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
     * Returns true if <code>mouseX</code> and <code>mouseY</code> fall
     * in the area of row that is used to expand/collapse the node and
     * the node at <code>row</code> does not represent a leaf.
     */
    @SuppressWarnings ("UnusedParameters")
    protected boolean isLocationInExpandControl ( final TreePath path, final int mouseX, final int mouseY )
    {
        if ( path != null && !component.getModel ().isLeaf ( path.getLastPathComponent () ) )
        {
            final int boxWidth;
            final Insets i = component.getInsets ();

            if ( ui.getExpandedIcon () != null )
            {
                boxWidth = ui.getExpandedIcon ().getIconWidth ();
            }
            else
            {
                boxWidth = 8;
            }

            int boxLeftX = getRowX ( component.getRowForPath ( path ), path.getPathCount () - 1 );

            if ( leftToRight )
            {
                boxLeftX = boxLeftX + i.left - ui.getRightChildIndent () + 1;
            }
            else
            {
                boxLeftX = component.getWidth () - boxLeftX - i.right + ui.getRightChildIndent () - 1;
            }

            boxLeftX = findCenteredX ( boxLeftX, boxWidth );

            return ( mouseX >= boxLeftX && mouseX < ( boxLeftX + boxWidth ) );
        }
        return false;
    }

    /**
     * Paints the expand (toggle) part of a row. The receiver should NOT modify <code>clipBounds</code>, or <code>insets</code>.
     */
    @SuppressWarnings ("UnusedParameters")
    protected void paintExpandControl ( final Graphics g, final Rectangle clipBounds, final Insets insets, final Rectangle bounds,
                                        final TreePath path, final int row, final boolean isExpanded, final boolean hasBeenExpanded,
                                        final boolean isLeaf )
    {
        final Object value = path.getLastPathComponent ();

        // Draw icons if not a leaf and either hasn't been loaded,
        // or the model child count is > 0.
        if ( !isLeaf && ( !hasBeenExpanded || treeModel.getChildCount ( value ) > 0 ) )
        {
            final int middleXOfKnob;
            if ( leftToRight )
            {
                middleXOfKnob = bounds.x - ui.getRightChildIndent () + 1;
            }
            else
            {
                middleXOfKnob = bounds.x + bounds.width + ui.getRightChildIndent () - 1;
            }
            final int middleYOfKnob = bounds.y + ( bounds.height / 2 );

            if ( isExpanded )
            {
                final Icon expandedIcon = ui.getExpandedIcon ();
                if ( expandedIcon != null )
                {
                    drawCentered ( component, g, expandedIcon, middleXOfKnob, middleYOfKnob );
                }
            }
            else
            {
                final Icon collapsedIcon = ui.getCollapsedIcon ();
                if ( collapsedIcon != null )
                {
                    drawCentered ( component, g, collapsedIcon, middleXOfKnob, middleYOfKnob );
                }
            }
        }
    }

    /**
     * Paints the renderer part of a row. The receiver should NOT modify <code>clipBounds</code>, or <code>insets</code>.
     */
    protected void paintRow ( final Graphics g, final Rectangle clipBounds, final Insets insets, final Rectangle bounds,
                              final TreePath path, final int row, final boolean isExpanded, final boolean hasBeenExpanded,
                              final boolean isLeaf )
    {
        // Don't paint the renderer if editing this row.
        if ( /*editingComponent != null &&*/ editingRow == row )
        {
            return;
        }

        final int leadIndex;

        if ( component.hasFocus () )
        {
            leadIndex = lastSelectionRow;
        }
        else
        {
            leadIndex = -1;
        }

        final Component rowComponent = currentCellRenderer
                .getTreeCellRendererComponent ( component, path.getLastPathComponent (), component.isRowSelected ( row ), isExpanded,
                        isLeaf, row, ( leadIndex == row ) );

        rendererPane.paintComponent ( g, rowComponent, component, bounds.x, bounds.y, bounds.width, bounds.height, true );
    }

    /**
     * Returns true if the expand (toggle) control should be drawn for the specified row.
     */
    @SuppressWarnings ("UnusedParameters")
    protected boolean shouldPaintExpandControl ( final TreePath path, final int row, final boolean isExpanded,
                                                 final boolean hasBeenExpanded, final boolean isLeaf )
    {
        if ( isLeaf )
        {
            return false;
        }

        final int depth = path.getPathCount () - 1;
        return !( ( depth == 0 || ( depth == 1 && !isRootVisible () ) ) && !getShowsRootHandles () );
    }

    protected boolean isRootVisible ()
    {
        return component != null && component.isRootVisible ();
    }

    protected boolean getShowsRootHandles ()
    {
        return component != null && component.getShowsRootHandles ();
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
    @SuppressWarnings ("UnusedParameters")
    protected void paintHorizontalPartOfLeg ( final Graphics g, final Rectangle clipBounds, final Insets insets, final Rectangle bounds,
                                              final TreePath path, final int row, final boolean isExpanded, final boolean hasBeenExpanded,
                                              final boolean isLeaf )
    {
        if ( !paintLines )
        {
            return;
        }

        // Don't paint the legs for the root node if the
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

        if ( leftToRight )
        {
            final int leftX = bounds.x - ui.getRightChildIndent ();
            final int nodeX = bounds.x - getHorizontalLegBuffer ();

            if ( lineY >= clipTop && lineY < clipBottom && nodeX >= clipLeft && leftX < clipRight && leftX < nodeX )
            {

                g.setColor ( linesColor );
                paintHorizontalLine ( g, component, lineY, leftX, nodeX - 1 );
            }
        }
        else
        {
            final int nodeX = bounds.x + bounds.width + getHorizontalLegBuffer ();
            final int rightX = bounds.x + bounds.width + ui.getRightChildIndent ();

            if ( lineY >= clipTop && lineY < clipBottom && rightX >= clipLeft && nodeX < clipRight && nodeX < rightX )
            {

                g.setColor ( linesColor );
                paintHorizontalLine ( g, component, lineY, nodeX, rightX - 1 );
            }
        }
    }

    /**
     * Paints the vertical part of the leg.
     *
     * @param g          graphics
     * @param clipBounds clip bounds
     * @param insets     tree insets
     * @param path       tree path
     */
    protected void paintVerticalPartOfLeg ( final Graphics g, final Rectangle clipBounds, final Insets insets, final TreePath path )
    {
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
        if ( leftToRight )
        {
            lineX = lineX - ui.getRightChildIndent () + insets.left;
        }
        else
        {
            lineX = component.getWidth () - lineX - insets.right + ui.getRightChildIndent () - 1;
        }
        final int clipLeft = clipBounds.x;
        final int clipRight = clipBounds.x + ( clipBounds.width - 1 );

        if ( lineX >= clipLeft && lineX <= clipRight )
        {
            final int clipTop = clipBounds.y;
            final int clipBottom = clipBounds.y + clipBounds.height;
            Rectangle parentBounds = getPathBounds ( component, path );
            final Rectangle lastChildBounds = getPathBounds ( component, getLastChildPath ( path ) );

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
                if ( treeModel != null )
                {
                    final Object root = treeModel.getRoot ();

                    if ( treeModel.getChildCount ( root ) > 0 )
                    {
                        parentBounds = getPathBounds ( component, path.
                                pathByAddingChild ( treeModel.getChild ( root, 0 ) ) );
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
                g.setColor ( linesColor );
                paintVerticalLine ( g, component, lineX, top, bottom );
            }
        }
    }

    /**
     * Paints a vertical line.
     */
    protected void paintVerticalLine ( final Graphics g, final JComponent c, final int x, final int top, final int bottom )
    {
        if ( lineTypeDashed )
        {
            drawDashedVerticalLine ( g, x, top, bottom );
        }
        else
        {
            g.drawLine ( x, top, x, bottom );
        }
    }

    /**
     * Returns a path to the last child of <code>parent</code>.
     */
    protected TreePath getLastChildPath ( final TreePath parent )
    {
        if ( treeModel != null )
        {
            final int childCount = treeModel.getChildCount ( parent.getLastPathComponent () );

            if ( childCount > 0 )
            {
                return parent.pathByAddingChild ( treeModel.getChild ( parent.getLastPathComponent (), childCount - 1 ) );
            }
        }
        return null;
    }

    /**
     * Paints a horizontal line.
     */
    protected void paintHorizontalLine ( final Graphics g, final JComponent c, final int y, final int left, final int right )
    {
        if ( lineTypeDashed )
        {
            drawDashedHorizontalLine ( g, y, left, right );
        }
        else
        {
            g.drawLine ( left, y, right, y );
        }
    }

    // This method is slow -- revisit when Java2D is ready.
    // assumes x1 <= x2
    protected void drawDashedHorizontalLine ( final Graphics g, final int y, int x1, final int x2 )
    {
        // Drawing only even coordinates helps join line segments so they
        // appear as one line.  This can be defeated by translating the
        // Graphics by an odd amount.
        x1 += ( x1 % 2 );

        for ( int x = x1; x <= x2; x += 2 )
        {
            g.drawLine ( x, y, x, y );
        }
    }

    // This method is slow -- revisit when Java2D is ready.
    // assumes y1 <= y2
    protected void drawDashedVerticalLine ( final Graphics g, final int x, int y1, final int y2 )
    {
        // Drawing only even coordinates helps join line segments so they
        // appear as one line.  This can be defeated by translating the
        // Graphics by an odd amount.
        y1 += ( y1 % 2 );

        for ( int y = y1; y <= y2; y += 2 )
        {
            g.drawLine ( x, y, x, y );
        }
    }

    /**
     * Returns the location, along the x-axis, to render a particular row
     * at. The return value does not include any Insets specified on the JTree.
     * This does not check for the validity of the row or depth, it is assumed
     * to be correct and will not throw an Exception if the row or depth
     * doesn't match that of the tree.
     *
     * @param row   Row to return x location for
     * @param depth Depth of the row
     * @return amount to indent the given row.
     */
    @SuppressWarnings ("UnusedParameters")
    protected int getRowX ( final int row, final int depth )
    {
        return totalChildIndent * ( depth + depthOffset );
    }

    /**
     * Updates how much each depth should be offset by.
     */
    protected void updateDepthOffset ()
    {
        if ( isRootVisible () )
        {
            if ( getShowsRootHandles () )
            {
                depthOffset = 1;
            }
            else
            {
                depthOffset = 0;
            }
        }
        else if ( !getShowsRootHandles () )
        {
            depthOffset = -1;
        }
        else
        {
            depthOffset = 0;
        }
    }

    /**
     * The vertical element of legs between nodes starts at the bottom of the
     * parent node by default.  This method makes the leg start below that.
     */
    protected int getVerticalLegBuffer ()
    {
        return 0;
    }

    /**
     * Paints drop location if it is available.
     *
     * @param g2d graphics context
     */
    protected void paintDropLocation ( final Graphics2D g2d )
    {
        final JTree.DropLocation dropLocation = component.getDropLocation ();
        if ( dropLocation != null )
        {
            final TreePath dropPath = dropLocation.getPath ();
            if ( isDropLine ( dropLocation ) )
            {
                // Painting drop line (between nodes)
                final Color background = component.getBackground ();
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
        final Rectangle bounds = component.getPathBounds ( dropPath );
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
            final Rectangle fsb = sb.intersection ( SwingUtils.size ( component ) );
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
     * Returns whether selector is available for current tree or not.
     *
     * @return true if selector is available for current tree, false otherwise
     */
    protected boolean isSelectorAvailable ()
    {
        return selectorEnabled && component != null && component.isEnabled () &&
                component.getSelectionModel ().getSelectionMode () != TreeSelectionModel.SINGLE_TREE_SELECTION;
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
        final Insets insets = component.getInsets ();

        if ( component.getRowCount () == 0 )
        {
            rect = new Rectangle ( insets.left, insets.top, component.getWidth () - insets.left - insets.right, 0 );
        }
        else
        {
            final Object root = treeModel.getRoot ();

            if ( path.getLastPathComponent () == root && index >= treeModel.getChildCount ( root ) )
            {

                rect = component.getRowBounds ( component.getRowCount () - 1 );
                rect.y = rect.y + rect.height;
                final Rectangle xRect;

                if ( !component.isRootVisible () )
                {
                    xRect = component.getRowBounds ( 0 );
                }
                else if ( treeModel.getChildCount ( root ) == 0 )
                {
                    xRect = component.getRowBounds ( 0 );
                    xRect.x += totalChildIndent;
                    xRect.width -= totalChildIndent + totalChildIndent;
                }
                else
                {
                    final TreePath lastChildPath =
                            path.pathByAddingChild ( treeModel.getChild ( root, treeModel.getChildCount ( root ) - 1 ) );
                    xRect = component.getPathBounds ( lastChildPath );
                }

                rect.x = xRect.x;
                rect.width = xRect.width;
            }
            else
            {
                rect = component.getPathBounds ( path.pathByAddingChild ( treeModel.getChild ( path.getLastPathComponent (), index ) ) );
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
     * Returns the Rectangle enclosing the label portion that the
     * last item in path will be drawn into.  Will return null if
     * any component in path is currently valid.
     */
    protected Rectangle getPathBounds ( final JTree tree, final TreePath path )
    {
        if ( tree != null && treeState != null )
        {
            return getPathBounds ( path, tree.getInsets (), new Rectangle () );
        }
        return null;
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
                bounds.x = component.getWidth () - ( bounds.x + bounds.width ) - insets.right;
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
     * Returns whether tree selection style points that the whole line is a single cell or not.
     *
     * @return true if tree selection style points that the whole line is a single cell, false otherwise
     */
    protected boolean isFullLineSelection ()
    {
        return ui.getSelectionStyle () == TreeSelectionStyle.line;
    }

    /**
     * Returns custom WebLaF tooltip provider.
     *
     * @return custom WebLaF tooltip provider
     */
    protected ToolTipProvider<? extends WebTree> getToolTipProvider ()
    {
        return component != null && component instanceof WebTree ? ( ( WebTree ) component ).getToolTipProvider () : null;
    }

    /**
     * Returns whether tree nodes drag available or not.
     *
     * @return true if tree nodes drag available, false otherwise
     */
    protected boolean isDragAvailable ()
    {
        return component != null && component.isEnabled () && component.getDragEnabled () && component.getTransferHandler () != null &&
                component.getTransferHandler ().getSourceActions ( component ) > 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Dimension getPreferredSize ()
    {
        final Dimension preferredSize = new Dimension ();
        treeState = ui.getTreeState ();
        if ( treeState != null )
        {
            final Insets i = component.getInsets ();

            if ( component.isLargeModel () )
            {
                final Rectangle visRect = component.getVisibleRect ();

                if ( visRect.x == 0 && visRect.y == 0 &&
                        visRect.width == 0 && visRect.height == 0 &&
                        component.getVisibleRowCount () > 0 )
                {
                    // The tree doesn't have a valid bounds yet. Calculate
                    // based on visible row count.
                    visRect.width = 1;
                    visRect.height = component.getRowHeight () * component.getVisibleRowCount ();
                }
                else
                {
                    visRect.x -= i.left;
                    visRect.y -= i.top;
                }
                // we should consider a non-visible area above
                Component parent = SwingUtilities.getUnwrappedParent ( component );
                if ( parent instanceof JViewport )
                {
                    parent = parent.getParent ();
                    if ( parent instanceof JScrollPane )
                    {
                        final JScrollPane pane = ( JScrollPane ) parent;
                        final JScrollBar bar = pane.getHorizontalScrollBar ();
                        if ( ( bar != null ) && bar.isVisible () )
                        {
                            final int height = bar.getHeight ();
                            visRect.y -= height;
                            visRect.height += height;
                        }
                    }
                }
                preferredSize.width = treeState.getPreferredWidth ( visRect );
            }
            else
            {
                preferredSize.width = treeState.getPreferredWidth ( null );
            }
            preferredSize.height = treeState.getPreferredHeight ();
            preferredSize.width += i.left + i.right;
            preferredSize.height += i.top + i.bottom;
        }

        treeState = null;

        return preferredSize;
    }
}