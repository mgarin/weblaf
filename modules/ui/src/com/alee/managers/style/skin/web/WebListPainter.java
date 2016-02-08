package com.alee.managers.style.skin.web;

import com.alee.laf.list.IListItemPainter;
import com.alee.laf.list.IListPainter;
import com.alee.laf.list.ListSelectionStyle;
import com.alee.laf.list.WebListUI;
import com.alee.managers.style.skin.web.data.decoration.IDecoration;
import com.alee.painter.PainterSupport;
import com.alee.utils.GeometryUtils;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author Alexandr Zernov
 */

public class WebListPainter<E extends JList, U extends WebListUI, D extends IDecoration<E, D>> extends AbstractDecorationPainter<E, U, D>
        implements IListPainter<E, U>
{
    /**
     * Hover list item decoration painter.
     */
    protected IListItemPainter hoverPainter;

    /**
     * Selected list items decoration painter.
     */
    protected IListItemPainter selectionPainter;

    /**
     * Listeners.
     */
    protected ListSelectionListener listSelectionListener;

    /**
     * Painting variables.
     */
    protected int layoutOrientation;
    protected CellRendererPane rendererPane;
    protected int[] cellHeights = null;
    protected int cellHeight = -1;
    protected int cellWidth = -1;
    protected int listHeight = -1;
    protected int listWidth = -1;
    protected int columnCount;
    protected int preferredHeight;
    protected int rowsPerColumn;
    protected boolean updateLayoutStateNeeded = true;

    @Override
    public void install ( final E c, final U ui )
    {
        super.install ( c, ui );

        // Properly installing section painters
        this.hoverPainter = PainterSupport.installSectionPainter ( this, hoverPainter, null, c, ui );
        this.selectionPainter = PainterSupport.installSectionPainter ( this, selectionPainter, null, c, ui );

        // Selection listener
        listSelectionListener = new ListSelectionListener ()
        {
            @Override
            public void valueChanged ( final ListSelectionEvent e )
            {
                // Optimized selection repaint
                repaintSelection ();
            }
        };
        component.addListSelectionListener ( listSelectionListener );
    }

    @Override
    public void uninstall ( final E c, final U ui )
    {
        // Removing listeners
        component.removeListSelectionListener ( listSelectionListener );
        listSelectionListener = null;

        // Clearing painting variables
        cellHeights = null;

        // Properly uninstalling section painters
        this.selectionPainter = PainterSupport.uninstallSectionPainter ( selectionPainter, c, ui );
        this.hoverPainter = PainterSupport.uninstallSectionPainter ( hoverPainter, c, ui );

        super.uninstall ( c, ui );
    }

    @Override
    public void prepareToPaint ( final boolean updateLayoutStateNeeded )
    {
        this.updateLayoutStateNeeded |= updateLayoutStateNeeded;
    }

    @Override
    public void paint ( final Graphics2D g2d, final Rectangle bounds, final E c, final U ui )
    {
        super.paint ( g2d, bounds, c, ui );

        // Painting hover cell background
        paintHoverCellBackground ( g2d );

        // Painting selected cells background
        paintSelectedCellsBackground ( g2d );

        // Painting list
        paintList ( g2d );

        // Painting drop location
        paintDropLocation ( g2d );

        // Updating layout state
        checkLayoutState ( ui );
    }

    /**
     * Paints existing list items.
     *
     * @param g2d graphics context
     */
    protected void paintList ( final Graphics2D g2d )
    {
        // Saving initial clip
        final Shape clip = g2d.getClip ();

        // Retrieving paint settings
        layoutOrientation = component.getLayoutOrientation ();
        rendererPane = ui.getCellRendererPane ();

        switch ( component.getLayoutOrientation () )
        {
            case JList.VERTICAL_WRAP:
                if ( component.getHeight () != listHeight )
                {
                    updateLayoutStateNeeded = true;
                    redrawList ();
                    updateLayoutState ();
                }
                break;
            case JList.HORIZONTAL_WRAP:
                if ( component.getWidth () != listWidth )
                {
                    updateLayoutStateNeeded = true;
                    redrawList ();
                    updateLayoutState ();
                }
                break;
            default:
                break;
        }

        if ( updateLayoutStateNeeded )
        {
            updateLayoutState ();
        }

        final ListCellRenderer renderer = component.getCellRenderer ();
        final ListModel dataModel = component.getModel ();
        final int size = dataModel.getSize ();
        if ( renderer != null && size > 0 )
        {
            // Determine how many columns we need to paint
            final Rectangle paintBounds = g2d.getClipBounds ();
            final int startColumn;
            final int endColumn;
            if ( ltr )
            {
                startColumn = convertLocationToColumn ( paintBounds.x, paintBounds.y );
                endColumn = convertLocationToColumn ( paintBounds.x + paintBounds.width, paintBounds.y );
            }
            else
            {
                startColumn = convertLocationToColumn ( paintBounds.x + paintBounds.width, paintBounds.y );
                endColumn = convertLocationToColumn ( paintBounds.x, paintBounds.y );
            }
            final int maxY = paintBounds.y + paintBounds.height;
            final int leadIndex = adjustIndex ( component.getLeadSelectionIndex (), component );
            final int rowIncrement = ( layoutOrientation == JList.HORIZONTAL_WRAP ) ? columnCount : 1;

            final ListSelectionModel selModel = component.getSelectionModel ();
            for ( int colCounter = startColumn; colCounter <= endColumn; colCounter++ )
            {
                // And then how many rows in this column
                int row = convertLocationToRowInColumn ( paintBounds.y, colCounter );
                final int rowCount = getRowCount ( colCounter );
                int index = getModelIndex ( colCounter, row );
                final Rectangle rowBounds = ui.getCellBounds ( component, index, index );
                if ( rowBounds == null )
                {
                    // Not valid, bail!
                    return;
                }
                while ( row < rowCount && rowBounds.y < maxY &&
                        index < size )
                {
                    rowBounds.height = getHeight ( colCounter, row );
                    g2d.setClip ( rowBounds.x, rowBounds.y, rowBounds.width, rowBounds.height );
                    g2d.clipRect ( paintBounds.x, paintBounds.y, paintBounds.width, paintBounds.height );
                    paintCell ( g2d, index, rowBounds, renderer, dataModel, selModel, leadIndex );
                    rowBounds.y += rowBounds.height;
                    index += rowIncrement;
                    row++;
                }
            }
        }

        // Empty out the renderer pane, allowing renderers to be gc'ed.
        rendererPane.removeAll ();
        rendererPane = null;

        // Restoring initial clip
        g2d.setClip ( clip );
    }

    /**
     * Requests full list update.
     */
    protected void redrawList ()
    {
        component.revalidate ();
        component.repaint ();
    }

    /**
     * Recompute the value of cellHeight or cellHeights based
     * and cellWidth, based on the current font and the current
     * values of fixedCellWidth, fixedCellHeight, and prototypeCellValue.
     */
    protected void updateLayoutState ()
    {
        /* If both JList fixedCellWidth and fixedCellHeight have been
         * set, then initialize cellWidth and cellHeight, and set
         * cellHeights to null.
         */

        final int fixedCellHeight = component.getFixedCellHeight ();
        final int fixedCellWidth = component.getFixedCellWidth ();

        cellWidth = ( fixedCellWidth != -1 ) ? fixedCellWidth : -1;

        if ( fixedCellHeight != -1 )
        {
            cellHeight = fixedCellHeight;
            cellHeights = null;
        }
        else
        {
            cellHeight = -1;
            cellHeights = new int[ component.getModel ().getSize () ];
        }

        /* If either of  JList fixedCellWidth and fixedCellHeight haven't
         * been set, then initialize cellWidth and cellHeights by
         * scanning through the entire model.  Note: if the renderer is
         * null, we just set cellWidth and cellHeights[*] to zero,
         * if they're not set already.
         */

        if ( ( fixedCellWidth == -1 ) || ( fixedCellHeight == -1 ) )
        {

            final ListModel dataModel = component.getModel ();
            final int dataModelSize = dataModel.getSize ();
            final ListCellRenderer renderer = component.getCellRenderer ();

            if ( renderer != null )
            {
                for ( int index = 0; index < dataModelSize; index++ )
                {
                    final Object value = dataModel.getElementAt ( index );
                    final Component c = renderer.getListCellRendererComponent ( component, value, index, false, false );
                    rendererPane.add ( c );
                    final Dimension cellSize = c.getPreferredSize ();
                    if ( fixedCellWidth == -1 )
                    {
                        cellWidth = Math.max ( cellSize.width, cellWidth );
                    }
                    if ( fixedCellHeight == -1 )
                    {
                        cellHeights[ index ] = cellSize.height;
                    }
                }
            }
            else
            {
                if ( cellWidth == -1 )
                {
                    cellWidth = 0;
                }
                if ( cellHeights == null )
                {
                    cellHeights = new int[ dataModelSize ];
                }
                for ( int index = 0; index < dataModelSize; index++ )
                {
                    cellHeights[ index ] = 0;
                }
            }
        }

        columnCount = 1;
        if ( layoutOrientation != JList.VERTICAL )
        {
            updateHorizontalLayoutState ( fixedCellWidth, fixedCellHeight );
        }
    }

    /**
     * Invoked when the list is layed out horizontally to determine how many columns to create. This updates the {@code rowsPerColumn},
     * {@code columnCount}, {@code preferredHeight} and potentially {@code cellHeight} instance variables.
     *
     * @param fixedCellWidth  fixed list item width
     * @param fixedCellHeight fixed list item height
     */
    @SuppressWarnings ( "UnusedParameters" )
    protected void updateHorizontalLayoutState ( final int fixedCellWidth, final int fixedCellHeight )
    {
        final int visRows = component.getVisibleRowCount ();
        final int dataModelSize = component.getModel ().getSize ();
        final Insets insets = component.getInsets ();

        listHeight = component.getHeight ();
        listWidth = component.getWidth ();

        if ( dataModelSize == 0 )
        {
            rowsPerColumn = columnCount = 0;
            preferredHeight = insets.top + insets.bottom;
            return;
        }

        final int height;

        if ( fixedCellHeight != -1 )
        {
            height = fixedCellHeight;
        }
        else
        {
            // Determine the max of the renderer heights.
            int maxHeight = 0;
            if ( cellHeights.length > 0 )
            {
                maxHeight = cellHeights[ cellHeights.length - 1 ];
                for ( int counter = cellHeights.length - 2; counter >= 0; counter-- )
                {
                    maxHeight = Math.max ( maxHeight, cellHeights[ counter ] );
                }
            }
            height = cellHeight = maxHeight;
            cellHeights = null;
        }
        // The number of rows is either determined by the visible row
        // count, or by the height of the list.
        rowsPerColumn = dataModelSize;
        if ( visRows > 0 )
        {
            rowsPerColumn = visRows;
            columnCount = Math.max ( 1, dataModelSize / rowsPerColumn );
            if ( dataModelSize > 0 && dataModelSize > rowsPerColumn &&
                    dataModelSize % rowsPerColumn != 0 )
            {
                columnCount++;
            }
            if ( layoutOrientation == JList.HORIZONTAL_WRAP )
            {
                // Because HORIZONTAL_WRAP flows differently, the
                // rowsPerColumn needs to be adjusted.
                rowsPerColumn = dataModelSize / columnCount;
                if ( dataModelSize % columnCount > 0 )
                {
                    rowsPerColumn++;
                }
            }
        }
        else if ( layoutOrientation == JList.VERTICAL_WRAP && height != 0 )
        {
            rowsPerColumn = Math.max ( 1, ( listHeight - insets.top -
                    insets.bottom ) / height );
            columnCount = Math.max ( 1, dataModelSize / rowsPerColumn );
            if ( dataModelSize > 0 && dataModelSize > rowsPerColumn &&
                    dataModelSize % rowsPerColumn != 0 )
            {
                columnCount++;
            }
        }
        else if ( layoutOrientation == JList.HORIZONTAL_WRAP && cellWidth > 0 &&
                listWidth > 0 )
        {
            columnCount = Math.max ( 1, ( listWidth - insets.left -
                    insets.right ) / cellWidth );
            rowsPerColumn = dataModelSize / columnCount;
            if ( dataModelSize % columnCount > 0 )
            {
                rowsPerColumn++;
            }
        }
        preferredHeight = rowsPerColumn * cellHeight + insets.top +
                insets.bottom;
    }

    /**
     * Returns the height of the cell at the passed in location.
     *
     * @param column list column
     * @param row    list row
     * @return height of the cell at the passed in location
     */
    protected int getHeight ( final int column, final int row )
    {
        if ( column < 0 || column > columnCount || row < 0 )
        {
            return -1;
        }
        if ( layoutOrientation != JList.VERTICAL )
        {
            return cellHeight;
        }
        if ( row >= component.getModel ().getSize () )
        {
            return -1;
        }
        return ( cellHeights == null ) ? cellHeight : row < cellHeights.length ? cellHeights[ row ] : -1;
    }

    /**
     * Returns the model index for the specified display location.
     * If {@code column} x {@code row} is beyond the length of the model, this will return the model size - 1.
     *
     * @param column list column
     * @param row    list row
     * @return model index for the specified display location
     */
    protected int getModelIndex ( final int column, final int row )
    {
        switch ( layoutOrientation )
        {
            case JList.VERTICAL_WRAP:
                return Math.min ( component.getModel ().getSize () - 1, rowsPerColumn * column + Math.min ( row, rowsPerColumn - 1 ) );
            case JList.HORIZONTAL_WRAP:
                return Math.min ( component.getModel ().getSize () - 1, row * columnCount + column );
            default:
                return row;
        }
    }

    /**
     * Returns the number of rows in the given column.
     *
     * @param column column index
     * @return number of rows in the given column
     */
    protected int getRowCount ( final int column )
    {
        if ( column < 0 || column >= columnCount )
        {
            return -1;
        }
        if ( layoutOrientation == JList.VERTICAL || ( column == 0 && columnCount == 1 ) )
        {
            return component.getModel ().getSize ();
        }
        if ( column >= columnCount )
        {
            return -1;
        }
        if ( layoutOrientation == JList.VERTICAL_WRAP )
        {
            if ( column < ( columnCount - 1 ) )
            {
                return rowsPerColumn;
            }
            return component.getModel ().getSize () - ( columnCount - 1 ) * rowsPerColumn;
        }
        // JList.HORIZONTAL_WRAP
        final int diff = columnCount - ( columnCount * rowsPerColumn - component.getModel ().getSize () );

        if ( column >= diff )
        {
            return Math.max ( 0, rowsPerColumn - 1 );
        }
        return rowsPerColumn;
    }

    /**
     * Returns the closest row that starts at the specified y-location in the passed in column.
     *
     * @param y      Y location
     * @param column column index
     * @return closest row that starts at the specified y-location in the passed in column
     */
    protected int convertLocationToRowInColumn ( final int y, final int column )
    {
        int x = 0;

        if ( layoutOrientation != JList.VERTICAL )
        {
            if ( ltr )
            {
                x = column * cellWidth;
            }
            else
            {
                x = component.getWidth () - ( column + 1 ) * cellWidth - component.getInsets ().right;
            }
        }
        return convertLocationToRow ( x, y, true );
    }

    /**
     * Returns the row at the location specified by X and Y coordinates.
     * If {@code closest} is {@code true} and the location doesn't exactly match a particular location, closest row will be returned.
     *
     * @param x       X location
     * @param y0      Y location
     * @param closest whether or not should try finding closest row if exact location doesn't match any
     * @return row at the location specified by X and Y coordinates
     */
    @SuppressWarnings ( "UnusedParameters" )
    protected int convertLocationToRow ( final int x, final int y0, final boolean closest )
    {
        final int size = component.getModel ().getSize ();
        if ( size <= 0 )
        {
            return -1;
        }

        final Insets insets = component.getInsets ();
        if ( cellHeights == null )
        {
            int row = ( cellHeight == 0 ) ? 0 : ( ( y0 - insets.top ) / cellHeight );
            if ( closest )
            {
                if ( row < 0 )
                {
                    row = 0;
                }
                else if ( row >= size )
                {
                    row = size - 1;
                }
            }
            return row;
        }
        else if ( size > cellHeights.length )
        {
            return -1;
        }
        else
        {
            int y = insets.top;
            int row = 0;
            if ( closest && y0 < y )
            {
                return 0;
            }

            int i;
            for ( i = 0; i < size; i++ )
            {
                if ( ( y0 >= y ) && ( y0 < y + cellHeights[ i ] ) )
                {
                    return row;
                }
                y += cellHeights[ i ];
                row += 1;
            }
            return i - 1;
        }
    }

    /**
     * Returns the column at the location specified by X and Y coordinates.
     *
     * @param x X location
     * @param y Y location
     * @return column at the location specified by X and Y coordinates
     */
    @SuppressWarnings ( "UnusedParameters" )
    protected int convertLocationToColumn ( final int x, final int y )
    {
        if ( cellWidth > 0 )
        {
            if ( layoutOrientation == JList.VERTICAL )
            {
                return 0;
            }
            final Insets insets = component.getInsets ();
            final int col;
            if ( ltr )
            {
                col = ( x - insets.left ) / cellWidth;
            }
            else
            {
                col = ( component.getWidth () - x - insets.right - 1 ) / cellWidth;
            }
            if ( col < 0 )
            {
                return 0;
            }
            else if ( col >= columnCount )
            {
                return columnCount - 1;
            }
            return col;
        }
        return 0;
    }

    /**
     * Returns corrected item index.
     *
     * @param index list item index
     * @param list  painted list
     * @return corrected item index
     */
    protected int adjustIndex ( final int index, final E list )
    {
        return index < list.getModel ().getSize () ? index : -1;
    }

    /**
     * Paints list drop location.
     *
     * @param g2d graphics context
     */
    @SuppressWarnings ( "UnusedParameters" )
    protected void paintDropLocation ( final Graphics2D g2d )
    {
        final JList.DropLocation loc = component.getDropLocation ();
        if ( loc == null || !loc.isInsert () )
        {
            //noinspection UnnecessaryReturnStatement
            return;
        }

        // todo check needs. Maybe move to the style
        //        final Color c = DefaultLookup.getColor ( list, this, "List.dropLineColor", null );
        //        if ( c != null )
        //        {
        //            g.setColor ( c );
        //            final Rectangle rect = getDropLineRect ( loc );
        //            g.fillRect ( rect.x, rect.y, rect.width, rect.height );
        //        }
    }

    //    private Rectangle getDropLineRect ( final JList.DropLocation loc )
    //    {
    //        final int size = list.getModel ().getSize ();
    //
    //        if ( size == 0 )
    //        {
    //            final Insets insets = list.getInsets ();
    //            if ( layoutOrientation == JList.HORIZONTAL_WRAP )
    //            {
    //                if ( ltr )
    //                {
    //                    return new Rectangle ( insets.left, insets.top, DROP_LINE_THICKNESS, 20 );
    //                }
    //                else
    //                {
    //                    return new Rectangle ( list.getWidth () - DROP_LINE_THICKNESS - insets.right, insets.top, DROP_LINE_THICKNESS, 20 );
    //                }
    //            }
    //            else
    //            {
    //                return new Rectangle ( insets.left, insets.top, list.getWidth () - insets.left - insets.right, DROP_LINE_THICKNESS );
    //            }
    //        }
    //
    //        Rectangle rect = null;
    //        int index = loc.getIndex ();
    //        boolean decr = false;
    //
    //        if ( layoutOrientation == JList.HORIZONTAL_WRAP )
    //        {
    //            if ( index == size )
    //            {
    //                decr = true;
    //            }
    //            else if ( index != 0 && convertModelToRow ( index ) != convertModelToRow ( index - 1 ) )
    //            {
    //                final Rectangle prev = getCellBounds ( list, index - 1 );
    //                final Rectangle me = getCellBounds ( list, index );
    //                final Point p = loc.getDropPoint ();
    //
    //                if ( ltr )
    //                {
    //                    decr = Point2D.distance ( prev.x + prev.width, prev.y + ( int ) ( prev.height / 2.0 ), p.x, p.y ) <
    //                            Point2D.distance ( me.x, me.y + ( int ) ( me.height / 2.0 ), p.x, p.y );
    //                }
    //                else
    //                {
    //                    decr = Point2D.distance ( prev.x, prev.y + ( int ) ( prev.height / 2.0 ), p.x, p.y ) <
    //                            Point2D.distance ( me.x + me.width, me.y + ( int ) ( prev.height / 2.0 ), p.x, p.y );
    //                }
    //            }
    //
    //            if ( decr )
    //            {
    //                index--;
    //                rect = getCellBounds ( list, index );
    //                if ( ltr )
    //                {
    //                    rect.x += rect.width;
    //                }
    //                else
    //                {
    //                    rect.x -= DROP_LINE_THICKNESS;
    //                }
    //            }
    //            else
    //            {
    //                rect = getCellBounds ( list, index );
    //                if ( !ltr )
    //                {
    //                    rect.x += rect.width - DROP_LINE_THICKNESS;
    //                }
    //            }
    //
    //            if ( rect.x >= list.getWidth () )
    //            {
    //                rect.x = list.getWidth () - DROP_LINE_THICKNESS;
    //            }
    //            else if ( rect.x < 0 )
    //            {
    //                rect.x = 0;
    //            }
    //
    //            rect.width = DROP_LINE_THICKNESS;
    //        }
    //        else if ( layoutOrientation == JList.VERTICAL_WRAP )
    //        {
    //            if ( index == size )
    //            {
    //                index--;
    //                rect = getCellBounds ( list, index );
    //                rect.y += rect.height;
    //            }
    //            else if ( index != 0 && convertModelToColumn ( index ) != convertModelToColumn ( index - 1 ) )
    //            {
    //
    //                final Rectangle prev = getCellBounds ( list, index - 1 );
    //                final Rectangle me = getCellBounds ( list, index );
    //                final Point p = loc.getDropPoint ();
    //                if ( Point2D.distance ( prev.x + ( int ) ( prev.width / 2.0 ), prev.y + prev.height, p.x, p.y ) <
    //                        Point2D.distance ( me.x + ( int ) ( me.width / 2.0 ), me.y, p.x, p.y ) )
    //                {
    //                    index--;
    //                    rect = getCellBounds ( list, index );
    //                    rect.y += rect.height;
    //                }
    //                else
    //                {
    //                    rect = getCellBounds ( list, index );
    //                }
    //            }
    //            else
    //            {
    //                rect = getCellBounds ( list, index );
    //            }
    //
    //            if ( rect.y >= list.getHeight () )
    //            {
    //                rect.y = list.getHeight () - DROP_LINE_THICKNESS;
    //            }
    //
    //            rect.height = DROP_LINE_THICKNESS;
    //        }
    //        else
    //        {
    //            if ( index == size )
    //            {
    //                index--;
    //                rect = getCellBounds ( list, index );
    //                rect.y += rect.height;
    //            }
    //            else
    //            {
    //                rect = getCellBounds ( list, index );
    //            }
    //
    //            if ( rect.y >= list.getHeight () )
    //            {
    //                rect.y = list.getHeight () - DROP_LINE_THICKNESS;
    //            }
    //
    //            rect.height = DROP_LINE_THICKNESS;
    //        }
    //
    //        return rect;
    //    }

    //    /**
    //     * Returns the row that the model index <code>index</code> will be displayed in..
    //     */
    //    protected int convertModelToRow ( final int index )
    //    {
    //        final int size = list.getModel ().getSize ();
    //
    //        if ( ( index < 0 ) || ( index >= size ) )
    //        {
    //            return -1;
    //        }
    //
    //        if ( layoutOrientation != JList.VERTICAL && columnCount > 1 &&
    //                rowsPerColumn > 0 )
    //        {
    //            if ( layoutOrientation == JList.VERTICAL_WRAP )
    //            {
    //                return index % rowsPerColumn;
    //            }
    //            return index / columnCount;
    //        }
    //        return index;
    //    }

    //    /**
    //     * Returns the column that the model index <code>index</code> will be displayed in.
    //     */
    //    protected int convertModelToColumn ( final int index )
    //    {
    //        final int size = list.getModel ().getSize ();
    //
    //        if ( ( index < 0 ) || ( index >= size ) )
    //        {
    //            return -1;
    //        }
    //
    //        if ( layoutOrientation != JList.VERTICAL && rowsPerColumn > 0 &&
    //                columnCount > 1 )
    //        {
    //            if ( layoutOrientation == JList.VERTICAL_WRAP )
    //            {
    //                return index / rowsPerColumn;
    //            }
    //            return index % columnCount;
    //        }
    //        return 0;
    //    }
    //
    //    /**
    //     * Gets the bounds of the specified model index, returning the resulting
    //     * bounds, or null if <code>index</code> is not valid.
    //     */
    //    protected Rectangle getCellBounds ( final JList list, final int index )
    //    {
    //        return ui.getCellBounds ( list, index, index );
    //    }

    /**
     * Paint one List cell: compute the relevant state, get the "rubber stamp" cell renderer component, and then use the CellRendererPane
     * to paint it. Subclasses may want to override this method rather than paint().
     *
     * @param g2d          graphics context
     * @param index        cell index
     * @param rowBounds    cell bounds
     * @param cellRenderer cell renderer
     * @param dataModel    list model
     * @param selModel     list selection model
     * @param leadIndex    lead cell index
     * @see #paint
     */
    protected void paintCell ( final Graphics2D g2d, final int index, final Rectangle rowBounds, final ListCellRenderer cellRenderer,
                               final ListModel dataModel, final ListSelectionModel selModel, final int leadIndex )
    {
        final Object value = dataModel.getElementAt ( index );
        final boolean isSelected = selModel.isSelectedIndex ( index );
        final boolean cellHasFocus = component.hasFocus () && ( index == leadIndex );
        final Component renderer = cellRenderer.getListCellRendererComponent ( component, value, index, isSelected, cellHasFocus );
        rendererPane.paintComponent ( g2d, renderer, component, rowBounds.x, rowBounds.y, rowBounds.width, rowBounds.height, true );
    }

    @Override
    public boolean isHoverDecorationSupported ()
    {
        return hoverPainter != null && component != null && component.isEnabled ();
    }

    /**
     * Paints hover cell highlight.
     *
     * @param g2d graphics context
     */
    protected void paintHoverCellBackground ( final Graphics2D g2d )
    {
        if ( isHoverDecorationSupported () )
        {
            // Checking hover cell availability
            final int hoverIndex = ui.getHoverIndex ();
            if ( hoverIndex != -1 && !component.isSelectedIndex ( hoverIndex ) )
            {
                // Checking hover cell bounds
                final Rectangle r = ui.getCellBounds ( component, hoverIndex, hoverIndex );
                if ( r != null )
                {
                    // Painting hover cell background
                    hoverPainter.paint ( g2d, r, component, ui );
                }
            }
        }
    }

    /**
     * Paints special WebLaF list cells selection.
     * It is rendered separately from cells allowing you to simplify your list cell renderer component.
     *
     * @param g2d graphics context
     */
    protected void paintSelectedCellsBackground ( final Graphics2D g2d )
    {
        if ( selectionPainter != null && component.getSelectedIndex () != -1 && ui.getSelectionStyle () != ListSelectionStyle.none )
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
     * Returns list of list selections bounds.
     * This method takes selection style into account.
     *
     * @return list of list selections bounds
     */
    protected List<Rectangle> getSelectionRects ()
    {
        // Return empty selection rects when custom selection painting is disabled
        if ( ui.getSelectionStyle () == ListSelectionStyle.none )
        {
            return Collections.emptyList ();
        }

        // Checking that selection exists
        final int[] indices = component.getSelectedIndices ();
        if ( indices == null || indices.length == 0 )
        {
            return Collections.emptyList ();
        }

        // Sorting selected rows
        Arrays.sort ( indices );

        // Calculating selection rects
        final List<Rectangle> selections = new ArrayList<Rectangle> ( indices.length );
        Rectangle maxRect = null;
        int lastRow = -1;
        for ( final int index : indices )
        {
            if ( ui.getSelectionStyle () == ListSelectionStyle.single )
            {
                // Required bounds
                selections.add ( component.getCellBounds ( index, index ) );
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
                    final Rectangle b = component.getCellBounds ( index, index );

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
     * Repaints all rectangles containing list selections.
     * This method is optimized to repaint only those area which are actually have selection in them.
     */
    protected void repaintSelection ()
    {
        if ( component.getSelectedIndex () != -1 )
        {
            for ( final Rectangle rect : getSelectionRects () )
            {
                component.repaint ( rect );
            }
        }
    }

    /**
     * Requests layout state update if needed.
     *
     * @param ui painted component UI
     */
    protected void checkLayoutState ( final U ui )
    {
        if ( updateLayoutStateNeeded )
        {
            ui.requestLayoutStateUpdate ();
            updateLayoutStateNeeded = false;
        }
    }
}