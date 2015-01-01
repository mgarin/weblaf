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

import com.alee.global.StyleConstants;
import com.alee.laf.WebLookAndFeel;
import com.alee.laf.table.renderers.WebTableHeaderCellRenderer;
import com.alee.utils.CompareUtils;
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
    public static final Color topLineColor = WebTableStyle.headerTopLineColor;
    public static final Color bottomLineColor = WebTableStyle.headerBottomLineColor;
    public static final Color topBgColor = WebTableStyle.headerTopBgColor;
    public static final Color bottomBgColor = WebTableStyle.headerBottomBgColor;

    @SuppressWarnings ( "UnusedParameters" )
    public static ComponentUI createUI ( final JComponent c )
    {
        return new WebTableHeaderUI ();
    }

    @Override
    public void installUI ( final JComponent c )
    {
        super.installUI ( c );

        // Default settings
        SwingUtils.setOrientation ( header );
        LookAndFeel.installProperty ( header, WebLookAndFeel.OPAQUE_PROPERTY, Boolean.TRUE );

        // Default renderer
        header.setDefaultRenderer ( new WebTableHeaderCellRenderer ()
        {
            @Override
            public Component getTableCellRendererComponent ( final JTable table, final Object value, final boolean isSelected,
                                                             final boolean hasFocus, final int row, final int column )
            {
                final JLabel renderer = ( JLabel ) super.getTableCellRendererComponent ( table, value, isSelected, hasFocus, row, column );
                renderer.setHorizontalAlignment ( JLabel.CENTER );
                return renderer;
            }
        } );
    }

    @Override
    public void paint ( final Graphics g, final JComponent c )
    {
        final Graphics2D g2d = ( Graphics2D ) g;

        // Table header background
        final Paint bgPaint = createBackgroundPaint ( 0, 1, 0, header.getHeight () - 1 );
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
        final boolean ltr = header.getComponentOrientation ().isLeftToRight ();
        final Rectangle clip = g.getClipBounds ();
        final Point left = clip.getLocation ();
        final Point right = new Point ( clip.x + clip.width - 1, clip.y );
        final TableColumnModel cm = header.getColumnModel ();
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
        final TableColumn draggedColumn = header.getDraggedColumn ();
        int columnWidth;
        final Rectangle cellRect = header.getHeaderRect ( ltr ? cMin : cMax );
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
            final int draggedColumnIndex = viewIndexForColumn ( draggedColumn );
            final Rectangle draggedCellRect = header.getHeaderRect ( draggedColumnIndex );
            draggedCellRect.x += header.getDraggedDistance ();

            // Background
            g2d.setPaint ( bgPaint );
            g2d.fillRect ( draggedCellRect.x - 1, draggedCellRect.y, draggedCellRect.width, draggedCellRect.height - 1 );

            // Header cell
            paintCell ( g, draggedCellRect, draggedColumnIndex, draggedColumn, draggedColumn, cm );
        }
    }

    public static Paint createBackgroundPaint ( final int x1, final int y1, final int x2, final int y2 )
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

    private void paintCell ( final Graphics g, final Rectangle rect, final int columnIndex, final TableColumn tc, final TableColumn dc,
                             final TableColumnModel cm )
    {
        final boolean ltr = header.getComponentOrientation ().isLeftToRight ();

        // Left side border
        g.setColor ( StyleConstants.borderColor );
        g.drawLine ( rect.x - 1, rect.y + 2, rect.x - 1, rect.y + rect.height - 4 );

        // Painting dragged cell renderer
        final JComponent component = ( JComponent ) getHeaderRenderer ( columnIndex );
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

    private Component getHeaderRenderer ( final int columnIndex )
    {
        final TableColumn aColumn = header.getColumnModel ().getColumn ( columnIndex );
        TableCellRenderer renderer = aColumn.getHeaderRenderer ();
        if ( renderer == null )
        {
            renderer = header.getDefaultRenderer ();
        }

        final boolean hasFocus = !header.isPaintingForPrint () && header.hasFocus ();
        return renderer.getTableCellRendererComponent ( header.getTable (), aColumn.getHeaderValue (), false, hasFocus, -1, columnIndex );
    }

    private int viewIndexForColumn ( final TableColumn aColumn )
    {
        final TableColumnModel cm = header.getColumnModel ();
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
    public Dimension getPreferredSize ( final JComponent c )
    {
        final Dimension ps = super.getPreferredSize ( c );
        ps.height = Math.max ( ps.height, WebTableStyle.headerHeight );
        return ps;
    }
}