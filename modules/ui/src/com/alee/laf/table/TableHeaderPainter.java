package com.alee.laf.table;

import com.alee.laf.table.ITableHeaderPainter;
import com.alee.laf.table.WebTableHeaderUI;
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
 * @author Alexandr Zernov
 */

public class TableHeaderPainter<E extends JTableHeader, U extends WebTableHeaderUI> extends AbstractPainter<E, U>
        implements ITableHeaderPainter<E, U>
{
    /**
     * Style settings.
     */
    protected int headerHeight;
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
    protected JTable table = null;

    @Override
    public void prepareToPaint ( final CellRendererPane rendererPane )
    {
        this.rendererPane = rendererPane;
    }

    @Override
    public void paint ( final Graphics2D g2d, final Rectangle bounds, final E c, final U ui )
    {
        table = component.getTable ();

        // Table header background
        final Paint bgPaint = createBackgroundPaint ( 0, 1, 0, component.getHeight () - 1 );
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
                    paintCell ( g2d, cellRect, column, aColumn, draggedColumn, cm );
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
                    paintCell ( g2d, cellRect, column, aColumn, draggedColumn, cm );
                }
                cellRect.x += columnWidth;
            }
        }

        // Paint the dragged column if we are dragging.
        if ( draggedColumn != null )
        {
            // Calculating dragged cell rect
            final int draggedColumnIndex = viewIndexForColumn ( draggedColumn );
            final Rectangle draggedCellRect = component.getHeaderRect ( draggedColumnIndex );
            draggedCellRect.x += component.getDraggedDistance ();

            // Background
            g2d.setPaint ( bgPaint );
            g2d.fillRect ( draggedCellRect.x - 1, draggedCellRect.y, draggedCellRect.width, draggedCellRect.height - 1 );

            // Header cell
            paintCell ( g2d, draggedCellRect, draggedColumnIndex, draggedColumn, draggedColumn, cm );
        }

        rendererPane = null;
        table = null;
    }

    protected Paint createBackgroundPaint ( final int x1, final int y1, final int x2, final int y2 )
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

    protected void paintCell ( final Graphics g, final Rectangle rect, final int columnIndex, final TableColumn column,
                               final TableColumn draggedColumn, final TableColumnModel columnModel )
    {
        // Complex check for the cases when trailing border should be painted
        // It can be painted for middle columns, dragged column or when table is smaller than viewport
        final JScrollPane scrollPane = SwingUtils.getScrollPane ( table );
        final boolean paintTrailingBorder = scrollPane != null && ( column == draggedColumn ||
                ( table.getAutoResizeMode () == JTable.AUTO_RESIZE_OFF && scrollPane.getViewport ().getWidth () > table.getWidth () ) ||
                ( ltr ? columnIndex != columnModel.getColumnCount () - 1 : columnIndex != 0 ) );

        // Left side border
        if ( ltr || paintTrailingBorder )
        {
            g.setColor ( borderColor );
            g.drawLine ( rect.x - 1, rect.y + 2, rect.x - 1, rect.y + rect.height - 4 );
        }

        // Painting dragged cell renderer
        final JComponent headerRenderer = ( JComponent ) getHeaderRenderer ( columnIndex );
        headerRenderer.setOpaque ( false );
        headerRenderer.setEnabled ( table.isEnabled () );
        rendererPane.paintComponent ( g, headerRenderer, component, rect.x, rect.y, rect.width, rect.height, true );

        // Right side border
        if ( !ltr || paintTrailingBorder )
        {
            g.setColor ( gridColor );
            g.drawLine ( rect.x + rect.width - 1, rect.y + 2, rect.x + rect.width - 1, rect.y + rect.height - 4 );
        }
    }

    protected Component getHeaderRenderer ( final int columnIndex )
    {
        final TableColumn aColumn = component.getColumnModel ().getColumn ( columnIndex );
        TableCellRenderer renderer = aColumn.getHeaderRenderer ();
        if ( renderer == null )
        {
            renderer = component.getDefaultRenderer ();
        }

        final boolean hasFocus = !component.isPaintingForPrint () && component.hasFocus ();
        return renderer
                .getTableCellRendererComponent ( component.getTable (), aColumn.getHeaderValue (), false, hasFocus, -1, columnIndex );
    }

    protected int viewIndexForColumn ( final TableColumn aColumn )
    {
        final TableColumnModel cm = component.getColumnModel ();
        for ( int column = 0; column < cm.getColumnCount (); column++ )
        {
            if ( cm.getColumn ( column ) == aColumn )
            {
                return column;
            }
        }
        return -1;
    }

    @Override
    public Dimension getPreferredSize ()
    {
        final Dimension ps = super.getPreferredSize ();
        ps.height = Math.max ( ps.height, headerHeight );
        return ps;
    }
}