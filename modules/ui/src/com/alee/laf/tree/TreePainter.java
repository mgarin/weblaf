package com.alee.laf.tree;

import com.alee.laf.WebLookAndFeel;
import com.alee.managers.style.Bounds;
import com.alee.painter.DefaultPainter;
import com.alee.painter.decoration.AbstractDecorationPainter;
import com.alee.painter.decoration.IDecoration;
import com.alee.painter.PainterSupport;
import com.alee.painter.SectionPainter;
import com.alee.utils.CollectionUtils;
import com.alee.utils.CompareUtils;
import com.alee.utils.GeometryUtils;
import com.alee.utils.SwingUtils;

import javax.swing.*;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.*;
import java.util.List;

/**
 * @author Alexandr Zernov
 */

public class TreePainter<E extends JTree, U extends WebTreeUI, D extends IDecoration<E, D>> extends AbstractDecorationPainter<E, U, D>
        implements ITreePainter<E, U>
{
    /**
     * Style settings.
     */
    protected boolean paintLines;
    protected boolean dashedLines;
    protected Color linesColor;

    /**
     * Tree rows background painter.
     * It can be used to provide background customization for specific tree rows.
     */
    @DefaultPainter ( AlternateTreeRowPainter.class )
    protected ITreeRowPainter rowPainter;

    /**
     * Hover node background painter.
     * It can be used to provide background for hover nodes.
     */
    @DefaultPainter ( TreeNodePainter.class )
    protected ITreeNodePainter hoverPainter;

    /**
     * Selected nodes background painter.
     * It can be used to provide background for selected nodes.
     * WebLaF uses this painter instead of cell renderer -based selection decoration.
     */
    @DefaultPainter ( TreeNodePainter.class )
    protected ITreeNodePainter selectionPainter;

    /**
     * Tree drop location painter.
     * Provides visual representation for drag-and-drop operation on tree nodes.
     */
    @DefaultPainter ( TreeDropLocationPainter.class )
    protected ITreeDropLocationPainter dropLocationPainter;

    /**
     * Tree nodes selector painter.
     * It can be provided to enable nodes multiselector.
     */
    @DefaultPainter ( TreeSelectorPainter.class )
    protected ITreeSelectorPainter selectorPainter;

    /**
     * Listeners.
     */
    protected TreeSelectionListener treeSelectionListener;
    protected TreeExpansionListener treeExpansionListener;
    protected MouseAdapter mouseAdapter;

    /**
     * Runtime variables.
     */
    protected List<Integer> initialSelection = new ArrayList<Integer> ();
    protected Point selectionStart = null;
    protected Point selectionEnd = null;
    protected TreePath draggablePath = null;

    /**
     * Painting variables.
     */
    protected int totalChildIndent;
    protected int depthOffset;
    protected TreeModel treeModel;
    protected AbstractLayoutCache treeState;
    protected Hashtable<TreePath, Boolean> paintingCache;
    protected CellRendererPane rendererPane;
    protected TreeCellRenderer currentCellRenderer;
    protected int editingRow = -1;
    protected int lastSelectionRow = -1;

    @Override
    public void install ( final E c, final U ui )
    {
        super.install ( c, ui );

        // Properly installing section painters
        this.rowPainter = PainterSupport.installSectionPainter ( this, rowPainter, null, c, ui );
        this.hoverPainter = PainterSupport.installSectionPainter ( this, hoverPainter, null, c, ui );
        this.selectionPainter = PainterSupport.installSectionPainter ( this, selectionPainter, null, c, ui );
        this.dropLocationPainter = PainterSupport.installSectionPainter ( this, dropLocationPainter, null, c, ui );
        this.selectorPainter = PainterSupport.installSectionPainter ( this, selectorPainter, null, c, ui );

        // Selection listener
        // Required for proper update of complex selection
        treeSelectionListener = new TreeSelectionListener ()
        {
            @Override
            public void valueChanged ( final TreeSelectionEvent e )
            {
                // Optimized selection repaint
                repaintSelection ();

                // Tree expansion on selection
                if ( ui.isExpandSelected () && component.getSelectionCount () > 0 )
                {
                    component.expandPath ( component.getSelectionPath () );
                }
            }
        };
        component.addTreeSelectionListener ( treeSelectionListener );

        // Expansion listener
        // Required for proper update of complex selection
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
                    if ( !SwingUtils.isCtrl ( e ) && ( !component.getDragEnabled () || component.getTransferHandler () == null ) ||
                            ui.getRowForPoint ( e.getPoint (), false ) == -1 )
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
                                    initialSelection = getSelectedRows ();

                                    // Updating selection
                                    validateSelection ( e );

                                    // Repainting selection on the tree
                                    repaintSelector ();
                                }
                            }
                        }
                        else if ( isFullLineSelection () )
                        {
                            // todo Start DnD on selected line here
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
                    // else
                    // {
                    //     // todo Start DnD on selected row here
                    //     // todo Also collapse node expansion on double-click if it is expanded and clicked on full line
                    // }
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

            /**
             * Performs selection validation and updates.
             * todo Modify selection instead of overwriting each time?
             *
             * @param e mouse event
             */
            private void validateSelection ( final MouseEvent e )
            {
                // Selection rect
                final Rectangle selection = GeometryUtils.getContainingRect ( selectionStart, selectionEnd );

                // Compute new selection
                final List<Integer> newSelection = new ArrayList<Integer> ();
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
                    final List<Integer> excludedRows = new ArrayList<Integer> ();
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
                if ( !CollectionUtils.equals ( getSelectedRows (), newSelection ) )
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

            /**
             * Returns selected rows list.
             *
             * @return selected rows list
             */
            private List<Integer> getSelectedRows ()
            {
                final List<Integer> selection = new ArrayList<Integer> ();
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

            /**
             * Repaints tree selector.
             * Replaced with full repaint due to strange tree lines painting bug.
             */
            private void repaintSelector ()
            {
                component.repaint ( component.getVisibleRect () );
            }
        };
        component.addMouseListener ( mouseAdapter );
        component.addMouseMotionListener ( mouseAdapter );
    }

    @Override
    public void uninstall ( final E c, final U ui )
    {
        // Removing listeners
        component.removeTreeSelectionListener ( treeSelectionListener );
        treeSelectionListener = null;
        component.removeTreeExpansionListener ( treeExpansionListener );
        treeExpansionListener = null;
        component.removeMouseListener ( mouseAdapter );
        component.removeMouseMotionListener ( mouseAdapter );
        mouseAdapter = null;

        // Properly uninstalling section painters
        this.selectorPainter = PainterSupport.uninstallSectionPainter ( selectorPainter, c, ui );
        this.dropLocationPainter = PainterSupport.uninstallSectionPainter ( dropLocationPainter, c, ui );
        this.selectionPainter = PainterSupport.uninstallSectionPainter ( selectionPainter, c, ui );
        this.hoverPainter = PainterSupport.uninstallSectionPainter ( hoverPainter, c, ui );
        this.rowPainter = PainterSupport.uninstallSectionPainter ( rowPainter, c, ui );

        super.uninstall ( c, ui );
    }

    @Override
    protected void propertyChange ( final String property, final Object oldValue, final Object newValue )
    {
        // Perform basic actions on property changes
        super.propertyChange ( property, oldValue, newValue );

        // Update visual drop location
        if ( CompareUtils.equals ( property, WebLookAndFeel.DROP_LOCATION ) && dropLocationPainter != null )
        {
            // Repainting previous drop location
            final JTree.DropLocation oldLocation = ( JTree.DropLocation ) oldValue;
            if ( oldLocation != null )
            {
                component.repaint ( dropLocationPainter.getDropViewBounds ( oldLocation ) );
            }

            // Repainting current drop location
            final JTree.DropLocation newLocation = ( JTree.DropLocation ) newValue;
            if ( newLocation != null )
            {
                component.repaint ( dropLocationPainter.getDropViewBounds ( newLocation ) );
            }
        }
    }

    @Override
    protected List<SectionPainter<E, U>> getSectionPainters ()
    {
        return asList ( rowPainter, selectionPainter, hoverPainter );
    }

    @Override
    protected void paintContent ( final Graphics2D g2d, final Rectangle bounds, final E c, final U ui )
    {
        // Checking tree state
        treeState = ui.getTreeState ();
        if ( treeState == null )
        {
            return;
        }

        // Preparing to paint tree
        treeModel = component.getModel ();
        totalChildIndent = ui.getLeftChildIndent () + ui.getRightChildIndent ();
        rendererPane = ui.getCellRendererPane ();
        lastSelectionRow = component.getLeadSelectionRow ();
        final TreePath editingPath = component.getEditingPath ();
        editingRow = editingPath != null ? component.getRowForPath ( editingPath ) : -1;
        updateDepthOffset ();

        // Painting tree background
        paintBackground ( g2d );

        // Painting hover node background
        paintHoverNodeBackground ( g2d );

        // Painting selected nodes background
        paintSelectedNodesBackground ( g2d );

        // Painting tree
        paintTree ( g2d );

        // Painting drop location
        paintDropLocation ( g2d, bounds, c, ui );

        // Multiselector
        paintMultiselector ( g2d );

        treeModel = null;
        treeState = null;
        paintingCache = null;
        rendererPane = null;
    }

    /**
     * Paints tree background.
     *
     * @param g2d graphics context
     */
    protected void paintBackground ( final Graphics2D g2d )
    {
        // Painting row background if one is available
        if ( rowPainter != null )
        {
            final Rectangle paintBounds = g2d.getClipBounds ();
            final TreePath initialPath = ui.getClosestPathForLocation ( component, 0, paintBounds.y );
            final Enumeration paintingEnumerator = treeState.getVisiblePathsFrom ( initialPath );

            if ( initialPath != null && paintingEnumerator != null )
            {
                final Insets insets = component.getInsets ();
                final int endY = paintBounds.y + paintBounds.height;
                final Rectangle boundsBuffer = new Rectangle ();

                Rectangle bounds;
                TreePath path;
                int row = treeState.getRowForPath ( initialPath );
                while ( paintingEnumerator.hasMoreElements () )
                {
                    path = ( TreePath ) paintingEnumerator.nextElement ();
                    if ( path != null )
                    {
                        bounds = getPathBounds ( path, insets, boundsBuffer );
                        if ( bounds == null )
                        {
                            // This will only happen if the model changes out
                            // from under us (usually in another thread).
                            // Swing isn't multi-threaded, but I'll put this
                            // check in anyway.
                            return;
                        }

                        // Preparing row painter to paint row background
                        rowPainter.prepareToPaint ( row );

                        // Calculating row bounds and painting its background
                        final Rectangle rowBounds = ui.getFullRowBounds ( row );
                        final Insets padding = ui.getPadding ();
                        if ( padding != null )
                        {
                            // Increasing background by the padding sizes at left and right sides
                            // This is required to properly display full row background, not node background
                            rowBounds.x -= padding.left;
                            rowBounds.width += padding.left + padding.right;
                        }
                        rowPainter.paint ( g2d, rowBounds, component, ui );

                        if ( ( bounds.y + bounds.height ) >= endY )
                        {
                            break;
                        }
                    }
                    else
                    {
                        break;
                    }
                    row++;
                }
            }
        }
    }

    @Override
    public void prepareToPaint ( final Hashtable<TreePath, Boolean> paintingCache, final TreeCellRenderer currentCellRenderer )
    {
        this.paintingCache = paintingCache;
        this.currentCellRenderer = currentCellRenderer;
    }

    /**
     * Paints centered icon.
     *
     * @param c    component
     * @param g2d  graphics
     * @param icon icon
     * @param x    X coordinate
     * @param y    Y coordinate
     */
    protected void paintCentered ( final Component c, final Graphics2D g2d, final Icon icon, final int x, final int y )
    {                                                                   //x-icon.getIconWidth ()/2-2
        icon.paintIcon ( c, g2d, findCenteredX ( x, icon.getIconWidth () ), y - icon.getIconHeight () / 2 );
    }

    /**
     * Returns centered x coordinate for the icon.
     *
     * @param x         X coordinate
     * @param iconWidth icon width
     * @return centered x coordinate
     */
    protected int findCenteredX ( final int x, final int iconWidth )
    {
        return ltr ? ( x - 2 - ( int ) Math.ceil ( iconWidth / 2.0 ) ) : ( x - 1 - ( int ) Math.floor ( iconWidth / 2.0 ) );
    }

    @Override
    public boolean isHoverDecorationSupported ()
    {
        return hoverPainter != null && component != null && component.isEnabled ();
    }

    /**
     * Paints hover node highlight.
     *
     * @param g2d graphics context
     */
    protected void paintHoverNodeBackground ( final Graphics2D g2d )
    {
        if ( isHoverDecorationSupported () )
        {
            // Checking hover row availability
            final int hoverRow = ui.getHoverRow ();
            if ( hoverRow != -1 && !component.isRowSelected ( hoverRow ) )
            {
                // Checking hover row bounds
                final Rectangle r = isFullLineSelection () ? ui.getFullRowBounds ( hoverRow ) : component.getRowBounds ( hoverRow );
                if ( r != null )
                {
                    // Painting hover node background
                    hoverPainter.paint ( g2d, r, component, ui );
                }
            }
        }
    }

    /**
     * Paints special WebLaF tree nodes selection.
     * It is rendered separately from nodes allowing you to simplify your tree cell renderer component.
     *
     * @param g2d graphics context
     */
    protected void paintSelectedNodesBackground ( final Graphics2D g2d )
    {
        if ( selectionPainter != null && component.getSelectionCount () > 0 && ui.getSelectionStyle () != TreeSelectionStyle.none )
        {
            // Painting selections
            final List<Rectangle> selections = getSelectionRects ();
            for ( final Rectangle rect : selections )
            {
                selectionPainter.paint ( g2d, rect, component, ui );
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
        final int[] indices = component.getSelectionRows ();
        if ( indices == null || indices.length == 0 )
        {
            return Collections.emptyList ();
        }

        // Sorting selected rows
        Arrays.sort ( indices );

        // Calculating selection rects
        final List<Rectangle> selections = new ArrayList<Rectangle> ( indices.length );
        final Insets insets = component.getInsets ();
        Rectangle maxRect = null;
        int lastRow = -1;
        for ( final int index : indices )
        {
            if ( ui.getSelectionStyle () == TreeSelectionStyle.single )
            {
                // Required bounds
                selections.add ( component.getRowBounds ( index ) );
            }
            else
            {
                if ( lastRow != -1 && lastRow + 1 != index )
                {
                    // Save determined group
                    selections.add ( maxRect );

                    // Reset counting
                    maxRect = null;
                    lastRow = -1;
                }
                if ( lastRow == -1 || lastRow + 1 == index )
                {
                    // Required bounds
                    final Rectangle b = component.getRowBounds ( index );

                    // Increasing bounds to cover whole line
                    if ( isFullLineSelection () )
                    {
                        b.x = insets.left;
                        b.width = component.getWidth () - insets.left - insets.right;
                    }

                    // Increase rect
                    maxRect = lastRow == -1 ? b : GeometryUtils.getContainingRect ( maxRect, b );

                    // Remember last row
                    lastRow = index;
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
     * Repaints all rectangles containing tree selections.
     * This method is optimized to repaint only those area which are actually have selection in them.
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

        paintingCache.clear ();

        if ( initialPath != null && paintingEnumerator != null )
        {
            TreePath parentPath = initialPath;

            // Paint the lines, knobs, and rows

            // Find each parent and have them paint a line to their last child
            parentPath = parentPath.getParentPath ();
            while ( parentPath != null )
            {
                paintVerticalPartOfLeg ( g2d, paintBounds, insets, parentPath );
                paintingCache.put ( parentPath, Boolean.TRUE );
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

            while ( paintingEnumerator.hasMoreElements () )
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
                        // This will only happen if the model changes out from under us (usually in another thread).
                        // Swing isn't multi-threaded, but I'll put this check in anyway.
                        return;
                    }

                    // See if the vertical line to the parent has been painted
                    parentPath = path.getParentPath ();
                    if ( parentPath != null )
                    {
                        if ( paintingCache.get ( parentPath ) == null )
                        {
                            paintVerticalPartOfLeg ( g2d, paintBounds, insets, parentPath );
                            paintingCache.put ( parentPath, Boolean.TRUE );
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
                        break;
                    }
                }
                else
                {
                    break;
                }
                row++;
            }
        }

        // Empty out the renderer pane, allowing renderers to be gc'ed.
        rendererPane.removeAll ();
    }

    /**
     * Returns whether or not {@code mouseX} and {@code mouseY} fall in the area of row that is used to expand/collapse the node and the
     * node at {@code row} does not represent a leaf.
     *
     * @param path   tree path
     * @param mouseX mouse X location
     * @param mouseY mouse Y location
     * @return true if {@code mouseX} and {@code mouseY} fall in the area of row that is used to expand/collapse the node and the node at
     * {@code row} does not represent a leaf, false otherwise
     */
    @SuppressWarnings ( "UnusedParameters" )
    protected boolean isLocationInExpandControl ( final TreePath path, final int mouseX, final int mouseY )
    {
        if ( path != null && !component.getModel ().isLeaf ( path.getLastPathComponent () ) )
        {
            final int boxWidth = ui.getExpandedIcon () != null ? ui.getExpandedIcon ().getIconWidth () : 8;
            final Insets i = component.getInsets ();

            int boxLeftX = getRowX ( component.getRowForPath ( path ), path.getPathCount () - 1 );

            boxLeftX = ltr ? boxLeftX + i.left - ui.getRightChildIndent () + 1 :
                    component.getWidth () - boxLeftX - i.right + ui.getRightChildIndent () - 1;

            boxLeftX = findCenteredX ( boxLeftX, boxWidth );

            return mouseX >= boxLeftX && mouseX < ( boxLeftX + boxWidth );
        }
        return false;
    }

    /**
     * Paints the expand (toggle) part of a row.
     * The receiver should NOT modify {@code clipBounds}, or {@code insets}.
     *
     * @param g2d             graphics context
     * @param clipBounds      clip bounds
     * @param insets          tree insets
     * @param bounds          tree path bounds
     * @param path            tree path
     * @param row             row index
     * @param isExpanded      whether row is expanded or not
     * @param hasBeenExpanded whether row has been expanded once before or not
     * @param isLeaf          whether node is leaf or not
     */
    @SuppressWarnings ( "UnusedParameters" )
    protected void paintExpandControl ( final Graphics2D g2d, final Rectangle clipBounds, final Insets insets, final Rectangle bounds,
                                        final TreePath path, final int row, final boolean isExpanded, final boolean hasBeenExpanded,
                                        final boolean isLeaf )
    {
        final Object value = path.getLastPathComponent ();

        // Paint icons if not a leaf and either hasn't been loaded,
        // or the model child count is > 0.
        if ( !isLeaf && ( !hasBeenExpanded || treeModel.getChildCount ( value ) > 0 ) )
        {
            final int middleXOfKnob;
            if ( ltr )
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
                    paintCentered ( component, g2d, expandedIcon, middleXOfKnob, middleYOfKnob );
                }
            }
            else
            {
                final Icon collapsedIcon = ui.getCollapsedIcon ();
                if ( collapsedIcon != null )
                {
                    paintCentered ( component, g2d, collapsedIcon, middleXOfKnob, middleYOfKnob );
                }
            }
        }
    }

    /**
     * Paints the renderer part of a row.
     * The receiver should NOT modify {@code clipBounds}, or {@code insets}.
     *
     * @param g2d             graphics context
     * @param clipBounds      clip bounds
     * @param insets          tree insets
     * @param bounds          tree path bounds
     * @param path            tree path
     * @param row             row index
     * @param isExpanded      whether row is expanded or not
     * @param hasBeenExpanded whether row has been expanded once before or not
     * @param isLeaf          whether node is leaf or not
     */
    @SuppressWarnings ( "UnusedParameters" )
    protected void paintRow ( final Graphics2D g2d, final Rectangle clipBounds, final Insets insets, final Rectangle bounds,
                              final TreePath path, final int row, final boolean isExpanded, final boolean hasBeenExpanded,
                              final boolean isLeaf )
    {
        // Don't paint the renderer if editing this row.
        if ( editingRow == row )
        {
            return;
        }

        // Retrieving row cell renderer
        final Object value = path.getLastPathComponent ();
        final boolean hasFocus = ( component.hasFocus () ? lastSelectionRow : -1 ) == row;
        final boolean selected = component.isRowSelected ( row );
        final Component rowComponent =
                currentCellRenderer.getTreeCellRendererComponent ( component, value, selected, isExpanded, isLeaf, row, hasFocus );

        // Painting cell renderer
        rendererPane.paintComponent ( g2d, rowComponent, component, bounds.x, bounds.y, bounds.width, bounds.height, true );
    }

    /**
     * Returns whether or not the expand (toggle) control should be painted for the specified row.
     *
     * @param path            tree path
     * @param row             row index
     * @param isExpanded      whether row is expanded or not
     * @param hasBeenExpanded whether row has been expanded once before or not
     * @param isLeaf          whether node is leaf or not
     * @return true if the expand (toggle) control should be painted for the specified row, false otherwise
     */
    @SuppressWarnings ( "UnusedParameters" )
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

    /**
     * Returns whether or not root is visible.
     *
     * @return true if root is visible, false otherwise
     */
    protected boolean isRootVisible ()
    {
        return component != null && component.isRootVisible ();
    }

    /**
     * Returns whether or not root handles should be displayed.
     *
     * @return true if root handles should be displayed, false otherwise
     */
    protected boolean getShowsRootHandles ()
    {
        return component != null && component.getShowsRootHandles ();
    }

    /**
     * Paints the horizontal part of the leg.
     *
     * @param g2d             graphics
     * @param clipBounds      clip bounds
     * @param insets          tree insets
     * @param bounds          tree path bounds
     * @param path            tree path
     * @param row             row index
     * @param isExpanded      whether row is expanded or not
     * @param hasBeenExpanded whether row has been expanded once before or not
     * @param isLeaf          whether node is leaf or not
     */
    @SuppressWarnings ( "UnusedParameters" )
    protected void paintHorizontalPartOfLeg ( final Graphics2D g2d, final Rectangle clipBounds, final Insets insets, final Rectangle bounds,
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

        if ( ltr )
        {
            final int leftX = bounds.x - ui.getRightChildIndent ();
            final int nodeX = bounds.x - getHorizontalLegIndent ();

            if ( lineY >= clipTop && lineY < clipBottom && nodeX >= clipLeft && leftX < clipRight && leftX < nodeX )
            {

                g2d.setPaint ( linesColor );
                paintHorizontalLine ( g2d, lineY, leftX, nodeX - 1 );
            }
        }
        else
        {
            final int nodeX = bounds.x + bounds.width + getHorizontalLegIndent ();
            final int rightX = bounds.x + bounds.width + ui.getRightChildIndent ();

            if ( lineY >= clipTop && lineY < clipBottom && rightX >= clipLeft && nodeX < clipRight && nodeX < rightX )
            {

                g2d.setPaint ( linesColor );
                paintHorizontalLine ( g2d, lineY, nodeX, rightX - 1 );
            }
        }
    }

    /**
     * Returns horizontal leg indent.
     *
     * @return horizontal leg indent
     */
    protected int getHorizontalLegIndent ()
    {
        return -2;
    }

    /**
     * Paints the vertical part of the leg.
     *
     * @param g2d        graphics
     * @param clipBounds clip bounds
     * @param insets     tree insets
     * @param path       tree path
     */
    protected void paintVerticalPartOfLeg ( final Graphics2D g2d, final Rectangle clipBounds, final Insets insets, final TreePath path )
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
        if ( ltr )
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
            Rectangle parentBounds = getPathBounds ( path );
            final Rectangle lastChildBounds = getPathBounds ( getLastChildPath ( path ) );

            if ( lastChildBounds == null )
            // This shouldn't happen, but if the model is modified
            // in another thread it is possible for this to happen.
            // Swing isn't multi-threaded, but I'll add this check in
            // anyway.
            {
                return;
            }

            int top;

            if ( parentBounds == null )
            {
                top = Math.max ( insets.top + getVerticalLegIndent (), clipTop );
            }
            else
            {
                top = Math.max ( parentBounds.y + parentBounds.height +
                        getVerticalLegIndent (), clipTop );
            }
            if ( depth == 0 && !isRootVisible () )
            {
                if ( treeModel != null )
                {
                    final Object root = treeModel.getRoot ();

                    if ( treeModel.getChildCount ( root ) > 0 )
                    {
                        parentBounds = getPathBounds ( path.pathByAddingChild ( treeModel.getChild ( root, 0 ) ) );
                        if ( parentBounds != null )
                        {
                            top = Math.max ( insets.top + getVerticalLegIndent (), parentBounds.y + parentBounds.height / 2 );
                        }
                    }
                }
            }

            final int bottom = Math.min ( lastChildBounds.y + ( lastChildBounds.height / 2 ), clipBottom );

            if ( top <= bottom )
            {
                g2d.setPaint ( linesColor );
                paintVerticalLine ( g2d, lineX, top, bottom );
            }
        }
    }

    /**
     * Returns a path to the last child of {@code parent}.
     *
     * @param parent parent tree path
     * @return path to the last child of {@code parent}
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
     * Paints a vertical line.
     *
     * @param g2d graphics context
     * @param x   X coordinate
     * @param y1  start Y coordinate
     * @param y2  end Y coordinate
     */
    protected void paintVerticalLine ( final Graphics2D g2d, final int x, final int y1, final int y2 )
    {
        if ( dashedLines )
        {
            paintDashedVerticalLine ( g2d, x, y1, y2 );
        }
        else
        {
            g2d.drawLine ( x, y1, x, y2 );
        }
    }

    /**
     * Paints dashed vertical line.
     * This method assumes that y1 &lt;= y2 always.
     * todo Change to proper stroke usage instead as this implementation is slow
     *
     * @param g2d graphics context
     * @param x   X coordinate
     * @param y1  start Y coordinate
     * @param y2  end Y coordinate
     */
    protected void paintDashedVerticalLine ( final Graphics2D g2d, final int x, int y1, final int y2 )
    {
        // Painting only even coordinates helps join line segments so they appear as one line
        // This can be defeated by translating the Graphics2D by an odd amount
        y1 += y1 % 2;

        // Painting dashed line
        for ( int y = y1; y <= y2; y += 2 )
        {
            g2d.drawLine ( x, y, x, y );
        }
    }

    /**
     * Paints a horizontal line.
     *
     * @param g2d graphics context
     * @param y   Y coordinate
     * @param x1  start X coordinate
     * @param x2  end X coordinate
     */
    protected void paintHorizontalLine ( final Graphics2D g2d, final int y, final int x1, final int x2 )
    {
        if ( dashedLines )
        {
            paintDashedHorizontalLine ( g2d, y, x1, x2 );
        }
        else
        {
            g2d.drawLine ( x1, y, x2, y );
        }
    }

    /**
     * Paints dashed horizontal line.
     * This method assumes that x1 &lt;= x2 always.
     * todo Change to proper stroke usage instead as this implementation is slow
     *
     * @param g2d graphics context
     * @param y   Y coordinate
     * @param x1  start X coordinate
     * @param x2  end X coordinate
     */
    protected void paintDashedHorizontalLine ( final Graphics2D g2d, final int y, int x1, final int x2 )
    {
        // Painting only even coordinates helps join line segments so they appear as one line
        // This can be defeated by translating the Graphics2D by an odd amount
        x1 += x1 % 2;

        // Painting dashed line
        for ( int x = x1; x <= x2; x += 2 )
        {
            g2d.drawLine ( x, y, x, y );
        }
    }

    /**
     * Returns the location, along the x-axis, to render a particular row at. The return value does not include any Insets specified on the
     * JTree. This does not check for the validity of the row or depth, it is assumed to be correct and will not throw an Exception if the
     * row or depth doesn't match that of the tree.
     *
     * @param row   Row to return x location for
     * @param depth Depth of the row
     * @return amount to indent the given row.
     */
    @SuppressWarnings ( "UnusedParameters" )
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
     * The vertical element of legs between nodes starts at the bottom of the parent node by default.
     * This method makes the leg start below that.
     *
     * @return vertical leg indent
     */
    protected int getVerticalLegIndent ()
    {
        return 0;
    }

    /**
     * Paints drop location if it is available.
     *
     * @param g2d    graphics context
     * @param bounds painting bounds
     * @param c      painted component
     * @param ui     painted component UI
     */
    protected void paintDropLocation ( final Graphics2D g2d, final Rectangle bounds, final E c, final U ui )
    {
        if ( isDropLocationAvailable () )
        {
            dropLocationPainter.prepareToPaint ( component.getDropLocation () );
            dropLocationPainter.paint ( g2d, bounds, c, ui );
        }
    }

    /**
     * Returns whether or not drop location is available.
     *
     * @return true if drop location is available, false otherwise
     */
    protected boolean isDropLocationAvailable ()
    {
        return dropLocationPainter != null && component.getDropLocation () != null;
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
            final Rectangle sb = GeometryUtils.getContainingRect ( selectionStart, selectionEnd );
            final Rectangle fsb = sb.intersection ( Bounds.component.of ( component ) );
            fsb.width -= 1;
            fsb.height -= 1;
            selectorPainter.paint ( g2d, fsb, component, ui );
        }
    }

    /**
     * Returns whether selector is available for current tree or not.
     *
     * @return true if selector is available for current tree, false otherwise
     */
    protected boolean isSelectorAvailable ()
    {
        return selectorPainter != null && component != null && component.isEnabled () &&
                component.getSelectionModel ().getSelectionMode () != TreeSelectionModel.SINGLE_TREE_SELECTION;
    }

    /**
     * Returns the Rectangle enclosing the label portion that the last item in path will be painted into.
     * Will return null if any component in path is currently valid.
     *
     * @param path tree path
     * @return Rectangle enclosing the label portion that the last item in path will be painted into
     */
    protected Rectangle getPathBounds ( final TreePath path )
    {
        if ( component != null && treeState != null )
        {
            return getPathBounds ( path, component.getInsets (), new Rectangle () );
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
            if ( ltr )
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
     * Returns whether tree selection style points that the whole line is a single cell or not.
     *
     * @return true if tree selection style points that the whole line is a single cell, false otherwise
     */
    protected boolean isFullLineSelection ()
    {
        return ui.getSelectionStyle () == TreeSelectionStyle.line;
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
}