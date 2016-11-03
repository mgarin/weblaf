package com.alee.laf.table;

import com.alee.painter.AbstractPainter;
import com.alee.utils.CompareUtils;
import com.alee.utils.SwingUtils;

import javax.swing.*;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import java.awt.*;

/**
 * Basic painter for {@link JTableHeader} component.
 * It is used as {@link WebTableHeaderUI} default painter.
 *
 * @param <E> component type
 * @param <U> component UI type
 * @author Alexandr Zernov
 * @author Mikle Garin
 */

public class TableHeaderPainter<E extends JTableHeader, U extends WebTableHeaderUI> extends AbstractPainter<E, U>
        implements ITableHeaderPainter<E, U>
{
    /**
     * Style settings.
     */
    protected Integer headerHeight;
    protected Color topLineColor;
    protected Color bottomLineColor;
    protected Color topBgColor;
    protected Color bottomBgColor;
    protected Color gridColor;
    protected Color borderColor;

    /**
     * Painting variables.
     */
    protected CellRendererPane rendererPane = null;

    @Override
    public void prepareToPaint ( final CellRendererPane rendererPane )
    {
        // Saving renderer pane reference
        this.rendererPane = rendererPane;
    }

    @Override
    public void paint ( final Graphics2D g2d, final Rectangle bounds, final E c, final U ui )
    {
        // Creating background paint
        final Paint bgPaint = getBackgroundPaint ( 0, 1, 0, component.getHeight () - 1 );

        // Table header background
        g2d.setPaint ( bgPaint );
        g2d.fillRect ( 0, 1, component.getWidth (), component.getHeight () - 1 );

        // Header top and bottom lines
        g2d.setPaint ( topLineColor );
        g2d.drawLine ( 0, 0, component.getWidth (), 0 );
        g2d.setPaint ( bottomLineColor );
        g2d.drawLine ( 0, component.getHeight () - 1, component.getWidth (), component.getHeight () - 1 );

        if ( component.getColumnModel ().getColumnCount () <= 0 )
        {
            return;
        }

        // Variables
        final Rectangle clip = g2d.getClipBounds ();
        final Point left = clip.getLocation ();
        final Point right = p ( clip.x + clip.width - 1, clip.y );
        final TableColumnModel cm = component.getColumnModel ();
        int cMin = component.columnAtPoint ( ltr ? left : right );
        int cMax = component.columnAtPoint ( ltr ? right : left );

        // This should never happen.
        if ( cMin == -1 )
        {
            cMin = 0;
        }

        // If the table does not have enough columns to fill the view we'll get -1.
        // Replace this with the index of the last column.
        if ( cMax == -1 )
        {
            cMax = cm.getColumnCount () - 1;
        }

        // Table titles
        final TableColumn draggedColumn = component.getDraggedColumn ();
        int columnWidth;
        final Rectangle cellRect = component.getHeaderRect ( ltr ? cMin : cMax );
        TableColumn aColumn;
        if ( ltr )
        {
            for ( int column = cMin; column <= cMax; column++ )
            {
                aColumn = cm.getColumn ( column );
                columnWidth = aColumn.getWidth ();
                cellRect.width = columnWidth;
                if ( aColumn != draggedColumn )
                {
                    paintCell ( g2d, c, cellRect, column, aColumn, draggedColumn, cm );
                }
                cellRect.x += columnWidth;
            }
        }
        else
        {
            for ( int column = cMax; column >= cMin; column-- )
            {
                aColumn = cm.getColumn ( column );
                columnWidth = aColumn.getWidth ();
                cellRect.width = columnWidth;
                if ( aColumn != draggedColumn )
                {
                    paintCell ( g2d, c, cellRect, column, aColumn, draggedColumn, cm );
                }
                cellRect.x += columnWidth;
            }
        }

        // Paint the dragged column if we are dragging.
        if ( draggedColumn != null )
        {
            // Calculating dragged cell rect
            final int draggedColumnIndex = getViewIndexForColumn ( draggedColumn );
            final Rectangle draggedCellRect = component.getHeaderRect ( draggedColumnIndex );
            draggedCellRect.x += component.getDraggedDistance ();

            // Background
            g2d.setPaint ( bgPaint );
            g2d.fillRect ( draggedCellRect.x - 1, draggedCellRect.y, draggedCellRect.width, draggedCellRect.height - 1 );

            // Header cell
            paintCell ( g2d, c, draggedCellRect, draggedColumnIndex, draggedColumn, draggedColumn, cm );
        }

        // Remove all components in the rendererPane
        rendererPane.removeAll ();

        // Clearing renderer pane reference
        rendererPane = null;
    }

    /**
     * Paints single table header cell (column).
     *
     * @param g2d           graphics context
     * @param c             table header
     * @param rect          cell bounds
     * @param columnIndex   column index
     * @param column        table column
     * @param draggedColumn currently dragged table column
     * @param columnModel   table column model
     */
    protected void paintCell ( final Graphics2D g2d, final E c, final Rectangle rect, final int columnIndex, final TableColumn column,
                               final TableColumn draggedColumn, final TableColumnModel columnModel )
    {
        // Table reference
        final JTable table = c.getTable ();

        // Complex check for the cases when trailing border should be painted
        // It can be painted for middle columns, dragged column or when table is smaller than viewport
        final JScrollPane scrollPane = SwingUtils.getScrollPane ( table );
        final boolean paintTrailingBorder = scrollPane != null && ( column == draggedColumn ||
                ( table.getAutoResizeMode () == JTable.AUTO_RESIZE_OFF && scrollPane.getViewport ().getWidth () > table.getWidth () ) ||
                ( ltr ? columnIndex != columnModel.getColumnCount () - 1 : columnIndex != 0 ) );

        // Left side border
        if ( ltr || paintTrailingBorder )
        {
            g2d.setColor ( borderColor );
            g2d.drawLine ( rect.x - 1, rect.y + 2, rect.x - 1, rect.y + rect.height - 4 );
        }

        // Painting dragged cell renderer
        final JComponent headerRenderer = ( JComponent ) getHeaderRenderer ( columnIndex );
        headerRenderer.setOpaque ( false );
        headerRenderer.setEnabled ( table.isEnabled () );
        rendererPane.paintComponent ( g2d, headerRenderer, component, rect.x, rect.y, rect.width, rect.height, true );

        // Right side border
        if ( !ltr || paintTrailingBorder )
        {
            g2d.setColor ( gridColor );
            g2d.drawLine ( rect.x + rect.width - 1, rect.y + 2, rect.x + rect.width - 1, rect.y + rect.height - 4 );
        }
    }

    /**
     * Returns table header background {@link Paint}.
     *
     * @param x1 first painting bounds X coordinate
     * @param y1 first painting bounds Y coordinate
     * @param x2 second painting bounds X coordinate
     * @param y2 second painting bounds Y coordinate
     * @return table header background {@link Paint}
     */
    protected Paint getBackgroundPaint ( final int x1, final int y1, final int x2, final int y2 )
    {
        if ( bottomBgColor == null || CompareUtils.equals ( topBgColor, bottomBgColor ) )
        {
            return topBgColor;
        }
        else
        {
            return new GradientPaint ( x1, y1, topBgColor, x2, y2, bottomBgColor );
        }
    }

    /**
     * Returns header cell renderer {@link Component} for the specified column index.
     *
     * @param index column index
     * @return header cell renderer {@link Component} for the specified column index
     */
    protected Component getHeaderRenderer ( final int index )
    {
        final TableColumn aColumn = component.getColumnModel ().getColumn ( index );
        TableCellRenderer renderer = aColumn.getHeaderRenderer ();
        if ( renderer == null )
        {
            renderer = component.getDefaultRenderer ();
        }
        final boolean hasFocus = !component.isPaintingForPrint () && component.hasFocus ();
        return renderer.getTableCellRendererComponent ( component.getTable (), aColumn.getHeaderValue (), false, hasFocus, -1, index );
    }

    /**
     * Returns actual view index for the specified column.
     *
     * @param column table column
     * @return actual view index for the specified column
     */
    protected int getViewIndexForColumn ( final TableColumn column )
    {
        final TableColumnModel cm = component.getColumnModel ();
        for ( int index = 0; index < cm.getColumnCount (); index++ )
        {
            if ( cm.getColumn ( index ) == column )
            {
                return index;
            }
        }
        return -1;
    }

    @Override
    public Dimension getPreferredSize ()
    {
        final Dimension ps = super.getPreferredSize ();
        if ( headerHeight != null )
        {
            ps.height = Math.max ( ps.height, headerHeight );
        }
        return ps;
    }
}