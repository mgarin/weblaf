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

package com.alee.laf.table;

import com.alee.laf.StyleConstants;
import com.alee.laf.table.renderers.WebTableHeaderCellRenderer;
import com.alee.utils.SwingUtils;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicTableHeaderUI;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import java.awt.*;

/**
 * User: mgarin Date: 17.08.11 Time: 23:08
 */

public class WebTableHeaderUI extends BasicTableHeaderUI
{
    public static final Color topLineColor = new Color ( 232, 234, 235 );
    public static final Color topBgColor = new Color ( 226, 226, 226 );
    public static final Color bottomBgColor = new Color ( 201, 201, 201 );
    public static final Color bottomLineColor = new Color ( 104, 104, 104 );

    @SuppressWarnings ("UnusedParameters")
    public static ComponentUI createUI ( JComponent c )
    {
        return new WebTableHeaderUI ();
    }

    @Override
    public void installUI ( JComponent c )
    {
        super.installUI ( c );

        // Default settings
        SwingUtils.setOrientation ( header );
        header.setOpaque ( true );

        // Default renderer
        header.setDefaultRenderer ( new WebTableHeaderCellRenderer ()
        {
            @Override
            public Component getTableCellRendererComponent ( JTable table, Object value, boolean isSelected, boolean hasFocus, int row,
                                                             int column )
            {
                JLabel renderer = ( JLabel ) super.getTableCellRendererComponent ( table, value, isSelected, hasFocus, row, column );
                renderer.setHorizontalAlignment ( JLabel.CENTER );
                return renderer;
            }
        } );
    }

    @Override
    public void paint ( Graphics g, JComponent c )
    {
        Graphics2D g2d = ( Graphics2D ) g;

        // Table header background
        GradientPaint bgPaint = createBackgroundPaint ( 0, 1, 0, header.getHeight () - 1 );
        g2d.setPaint ( bgPaint );
        g2d.fillRect ( 0, 1, header.getWidth (), header.getHeight () - 1 );

        // Header top and bottom lines
        g2d.setColor ( topLineColor );
        g2d.drawLine ( 0, 0, header.getWidth (), 0 );
        g2d.setPaint ( bottomLineColor );
        g2d.drawLine ( 0, header.getHeight () - 1, header.getWidth (), header.getHeight () - 1 );

        if ( header.getColumnModel ().getColumnCount () <= 0 )
        {
            return;
        }

        // Variables
        boolean ltr = header.getComponentOrientation ().isLeftToRight ();
        Rectangle clip = g.getClipBounds ();
        Point left = clip.getLocation ();
        Point right = new Point ( clip.x + clip.width - 1, clip.y );
        TableColumnModel cm = header.getColumnModel ();
        int cMin = header.columnAtPoint ( ltr ? left : right );
        int cMax = header.columnAtPoint ( ltr ? right : left );

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
        TableColumn draggedColumn = header.getDraggedColumn ();
        int columnWidth;
        Rectangle cellRect = header.getHeaderRect ( ltr ? cMin : cMax );
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
                    paintCell ( g, cellRect, column, aColumn, draggedColumn, cm );
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
                    paintCell ( g, cellRect, column, aColumn, draggedColumn, cm );
                }
                cellRect.x += columnWidth;
            }
        }

        // Paint the dragged column if we are dragging.
        if ( draggedColumn != null )
        {
            // Calculating dragged cell rect
            int draggedColumnIndex = viewIndexForColumn ( draggedColumn );
            Rectangle draggedCellRect = header.getHeaderRect ( draggedColumnIndex );
            draggedCellRect.x += header.getDraggedDistance ();

            // Background
            g2d.setPaint ( bgPaint );
            g2d.fillRect ( draggedCellRect.x - 1, draggedCellRect.y, draggedCellRect.width, draggedCellRect.height - 1 );

            // Header cell
            paintCell ( g, draggedCellRect, draggedColumnIndex, draggedColumn, draggedColumn, cm );
        }
    }

    public static GradientPaint createBackgroundPaint ( int x1, int y1, int x2, int y2 )
    {
        return new GradientPaint ( x1, y1, topBgColor, x2, y2, bottomBgColor );
    }

    private void paintCell ( Graphics g, Rectangle rect, int columnIndex, TableColumn tc, TableColumn dc, TableColumnModel cm )
    {
        boolean ltr = header.getComponentOrientation ().isLeftToRight ();

        // Left side border
        g.setColor ( StyleConstants.borderColor );
        g.drawLine ( rect.x - 1, rect.y + 2, rect.x - 1, rect.y + rect.height - 4 );

        // Painting dragged cell renderer
        JComponent component = ( JComponent ) getHeaderRenderer ( columnIndex );
        component.setOpaque ( false );
        component.setEnabled ( header.getTable ().isEnabled () );
        rendererPane.paintComponent ( g, component, header, rect.x, rect.y, rect.width, rect.height, true );

        // Right side border
        if ( tc == dc || ( ltr ? columnIndex != cm.getColumnCount () - 1 : columnIndex != 0 ) )
        {
            g.setColor ( WebTableStyle.gridColor );
            g.drawLine ( rect.x + rect.width - 1, rect.y + 2, rect.x + rect.width - 1, rect.y + rect.height - 4 );
        }
    }

    private Component getHeaderRenderer ( int columnIndex )
    {
        TableColumn aColumn = header.getColumnModel ().getColumn ( columnIndex );
        TableCellRenderer renderer = aColumn.getHeaderRenderer ();
        if ( renderer == null )
        {
            renderer = header.getDefaultRenderer ();
        }

        boolean hasFocus = !header.isPaintingForPrint () && header.hasFocus ();
        return renderer.getTableCellRendererComponent ( header.getTable (), aColumn.getHeaderValue (), false, hasFocus, -1, columnIndex );
    }

    private int viewIndexForColumn ( TableColumn aColumn )
    {
        TableColumnModel cm = header.getColumnModel ();
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
    public Dimension getPreferredSize ( JComponent c )
    {
        Dimension ps = super.getPreferredSize ( c );
        ps.height = Math.max ( ps.height, WebTableStyle.headerHeight );
        return ps;
    }
}