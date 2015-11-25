package com.alee.managers.style.skin.web;

import com.alee.global.StyleConstants;
import com.alee.laf.table.TableHeaderPainter;
import com.alee.laf.table.WebTableHeaderUI;
import com.alee.laf.table.WebTableStyle;
import com.alee.painter.AbstractPainter;
import com.alee.utils.CompareUtils;

import javax.swing.*;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import java.awt.*;

/**
 * @author Alexandr Zernov
 */

public class WebTableHeaderPainter<E extends JTableHeader, U extends WebTableHeaderUI> extends AbstractPainter<E, U>
        implements TableHeaderPainter<E, U>
{
    /**
     * Style settings.
     */
    protected int headerHeight = WebTableStyle.headerHeight;
    protected Color topLineColor = WebTableStyle.headerTopLineColor;
    protected Color bottomLineColor = WebTableStyle.headerBottomLineColor;
    protected Color topBgColor = WebTableStyle.headerTopBgColor;
    protected Color bottomBgColor = WebTableStyle.headerBottomBgColor;
    protected Color gridColor = WebTableStyle.gridColor;
    protected Color borderColor = StyleConstants.borderColor;

    /**
     * Painting variables.
     */
    protected CellRendererPane rendererPane = null;

    public int getHeaderHeight ()
    {
        return headerHeight;
    }

    public void setHeaderHeight ( final int headerHeight )
    {
        this.headerHeight = headerHeight;
    }

    public Color getTopLineColor ()
    {
        return topLineColor;
    }

    public void setTopLineColor ( final Color topLineColor )
    {
        this.topLineColor = topLineColor;
    }

    public Color getBottomLineColor ()
    {
        return bottomLineColor;
    }

    public void setBottomLineColor ( final Color bottomLineColor )
    {
        this.bottomLineColor = bottomLineColor;
    }

    public Color getTopBgColor ()
    {
        return topBgColor;
    }

    public void setTopBgColor ( final Color topBgColor )
    {
        this.topBgColor = topBgColor;
    }

    public Color getBottomBgColor ()
    {
        return bottomBgColor;
    }

    public void setBottomBgColor ( final Color bottomBgColor )
    {
        this.bottomBgColor = bottomBgColor;
    }

    public Color getGridColor ()
    {
        return gridColor;
    }

    public void setGridColor ( final Color gridColor )
    {
        this.gridColor = gridColor;
    }

    public Color getBorderColor ()
    {
        return borderColor;
    }

    public void setBorderColor ( final Color borderColor )
    {
        this.borderColor = borderColor;
    }

    @Override
    public void prepareToPaint ( final CellRendererPane rendererPane )
    {
        this.rendererPane = rendererPane;
    }

    @Override
    public void paint ( final Graphics2D g2d, final Rectangle bounds, final E c, final U ui )
    {
        // Table header background
        final Paint bgPaint = createBackgroundPaint ( 0, 1, 0, component.getHeight () - 1 );
        g2d.setPaint ( bgPaint );
        g2d.fillRect ( 0, 1, component.getWidth (), component.getHeight () - 1 );

        // Header top and bottom lines
        g2d.setColor ( topLineColor );
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
        final Point right = new Point ( clip.x + clip.width - 1, clip.y );
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
        // Left side border
        g.setColor ( borderColor );
        g.drawLine ( rect.x - 1, rect.y + 2, rect.x - 1, rect.y + rect.height - 4 );

        // Painting dragged cell renderer
        final JComponent headerRenderer = ( JComponent ) getHeaderRenderer ( columnIndex );
        headerRenderer.setOpaque ( false );
        headerRenderer.setEnabled ( component.getTable ().isEnabled () );
        rendererPane.paintComponent ( g, headerRenderer, component, rect.x, rect.y, rect.width, rect.height, true );

        // Right side border
        if ( column == draggedColumn || ( ltr ? columnIndex != columnModel.getColumnCount () - 1 : columnIndex != 0 ) )
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