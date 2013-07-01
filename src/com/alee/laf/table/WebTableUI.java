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

import com.alee.laf.table.editors.WebBooleanEditor;
import com.alee.laf.table.editors.WebGenericEditor;
import com.alee.laf.table.editors.WebNumberEditor;
import com.alee.laf.table.renderers.*;
import com.alee.utils.SwingUtils;
import com.alee.utils.swing.AncestorAdapter;

import javax.swing.*;
import javax.swing.event.AncestorEvent;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicTableUI;
import java.awt.*;
import java.util.Date;

/**
 * User: mgarin Date: 07.07.11 Time: 17:56
 */

public class WebTableUI extends BasicTableUI
{
    private Color selectionBackground = WebTableStyle.selectionBackground;

    private AncestorAdapter ancestorAdapter;

    public static ComponentUI createUI ( JComponent c )
    {
        return new WebTableUI ();
    }

    public void installUI ( final JComponent c )
    {
        super.installUI ( c );

        // Default settings
        SwingUtils.setOrientation ( table );
        table.setOpaque ( false );
        table.setBackground ( WebTableStyle.background );
        table.setForeground ( WebTableStyle.foreground );
        table.setSelectionBackground ( WebTableStyle.selectionBackground );
        table.setSelectionForeground ( WebTableStyle.selectionForeground );
        table.setFillsViewportHeight ( false );
        table.setRowHeight ( WebTableStyle.rowHeight );
        table.setShowVerticalLines ( false );
        table.setIntercellSpacing ( new Dimension ( 0, 1 ) );

        // Configuring default renderers
        table.setDefaultRenderer ( Object.class, new WebTableCellRenderer () );
        table.setDefaultRenderer ( Number.class, new WebNumberRenderer () );
        table.setDefaultRenderer ( Double.class, new WebDoubleRenderer () );
        table.setDefaultRenderer ( Float.class, new WebDoubleRenderer () );
        table.setDefaultRenderer ( Date.class, new WebDateRenderer () );
        table.setDefaultRenderer ( Icon.class, new WebIconRenderer () );
        table.setDefaultRenderer ( ImageIcon.class, new WebIconRenderer () );
        table.setDefaultRenderer ( Boolean.class, new WebBooleanRenderer () );

        // Configuring default editors
        table.setDefaultEditor ( Object.class, new WebGenericEditor () );
        table.setDefaultEditor ( Number.class, new WebNumberEditor () );
        table.setDefaultEditor ( Boolean.class, new WebBooleanEditor () );
        // todo Additional editors:
        // table.setDefaultEditor ( Date.class,  );
        // table.setDefaultEditor ( Color.class,  );
        // table.setDefaultEditor ( List.class,  );

        // Configuring scrollpane corner
        configureEnclosingScrollPaneUI ( table );
        ancestorAdapter = new AncestorAdapter ()
        {
            public void ancestorAdded ( AncestorEvent event )
            {
                configureEnclosingScrollPaneUI ( table );
            }
        };
        table.addAncestorListener ( ancestorAdapter );
    }

    public void uninstallUI ( JComponent c )
    {
        table.removeAncestorListener ( ancestorAdapter );

        super.uninstallUI ( c );
    }

    protected void configureEnclosingScrollPaneUI ( JComponent c )
    {
        Container p = c.getParent ();
        if ( p instanceof JViewport )
        {
            Container gp = p.getParent ();
            if ( gp instanceof JScrollPane )
            {
                JScrollPane scrollPane = ( JScrollPane ) gp;

                // Make certain we are the viewPort's view and not, for
                // example, the rowHeaderView of the scrollPane -
                // an implementor of fixed columns might do this.
                JViewport viewport = scrollPane.getViewport ();
                if ( viewport == null || viewport.getView () != c )
                {
                    return;
                }

                // Adding both corners to the scroll pane for both orientation cases
                scrollPane.setCorner ( JScrollPane.UPPER_LEADING_CORNER, new WebTableCorner ( false ) );
                scrollPane.setCorner ( JScrollPane.UPPER_TRAILING_CORNER, new WebTableCorner ( true ) );
            }
        }
    }

    //    public void paint ( Graphics g, JComponent c )
    //    {
    //        if ( table.getRowCount () > 0 )
    //        {
    //            Rectangle vr = table.getVisibleRect ();
    //
    //            int row = table.rowAtPoint ( vr.getLocation () );
    //            Rectangle r = table.getCellRect ( row, 0, false );
    //            boolean contained = true;
    //
    //            while ( contained )
    //            {
    //                if ( evenLineColor != null || oddLineColor != null )
    //                {
    //                    boolean even = row % 2 == 0;
    //                    if ( even && evenLineColor != null )
    //                    {
    //                        g.setColor ( evenLineColor );
    //                        g.fillRect ( vr.x, r.y, vr.width, r.height );
    //                    }
    //                    if ( !even && oddLineColor != null )
    //                    {
    //                        g.setColor ( oddLineColor );
    //                        g.fillRect ( vr.x, r.y, vr.width, r.height );
    //                    }
    //                }
    //
    //                row++;
    //                r = table.getCellRect ( row, 0, false );
    //                contained = vr.intersects ( vr.x, r.y, vr.width, r.height );
    //            }
    //
    //            boolean rs = table.getRowSelectionAllowed ();
    //            boolean cs = table.getColumnSelectionAllowed ();
    //            if ( rs && cs )
    //            {
    //                List<SelectionInterval> rowSelectionIntervals = getRowSelectionIntervals ();
    //                List<SelectionInterval> colSelectionIntervals = getColumnSelectionIntervals ();
    //                for ( SelectionInterval rsi : rowSelectionIntervals )
    //                {
    //                    for ( SelectionInterval csi : colSelectionIntervals )
    //                    {
    //                        Rectangle start = table.getCellRect ( rsi.getStartIndex (), csi.getStartIndex (), false );
    //                        Rectangle end = table.getCellRect ( rsi.getEndIndex (), csi.getEndIndex (), false );
    //
    //                        g.setColor ( new Color ( 65, 118, 194 ) );
    //                        g.drawLine ( start.x, start.y, end.x + end.width, start.y );
    //                        g.setColor ( new Color ( 59, 125, 211 ) );
    //                        g.fillRect ( start.x, start.y + 1, end.x + end.width - start.x, end.y + end.height - start.y - 1 );
    //                    }
    //                }
    //            }
    //            else if ( rs )
    //            {
    //                List<SelectionInterval> rowSelectionIntervals = getRowSelectionIntervals ();
    //                for ( SelectionInterval rsi : rowSelectionIntervals )
    //                {
    //                    Rectangle start = table.getCellRect ( rsi.getStartIndex (), 0, false );
    //                    Rectangle end = table.getCellRect ( rsi.getEndIndex (), 0, false );
    //
    //                    g.setColor ( new Color ( 65, 118, 194 ) );
    //                    g.drawLine ( vr.x, start.y, vr.x + vr.width, start.y );
    //                    g.setColor ( new Color ( 59, 125, 211 ) );
    //                    g.fillRect ( vr.x, start.y + 1, vr.width, end.y + end.height - start.y - 1 );
    //                }
    //            }
    //            else if ( cs )
    //            {
    //                List<SelectionInterval> colSelectionIntervals = getColumnSelectionIntervals ();
    //                for ( SelectionInterval csi : colSelectionIntervals )
    //                {
    //                    Rectangle start = table.getCellRect ( 0, csi.getStartIndex (), false );
    //                    Rectangle end = table.getCellRect ( 0, csi.getEndIndex (), false );
    //
    //                    g.setColor ( new Color ( 65, 118, 194 ) );
    //                    g.drawLine ( start.x, vr.y, end.x + end.width, vr.y );
    //                    g.setColor ( new Color ( 59, 125, 211 ) );
    //                    g.fillRect ( start.x, vr.y + 1, end.x + end.width - start.x, vr.height - 1 );
    //                }
    //            }
    //            //            if ( table.isRowSelected ( row ) )
    //            //            {
    //            //                g.setColor ( new Color ( 65, 118, 194 ) );
    //            //                g.drawLine ( lr.x, lr.y, lr.width, lr.y );
    //            //                g.setColor ( new Color ( 59, 125, 211 ) );
    //            //                g.fillRect ( lr.x, lr.y + 1, lr.width, lr.height - 1 );
    //            //            }
    //
    //        }
    //
    //        super.paint ( g, c );
    //    }
    //
    //    private List<SelectionInterval> getRowSelectionIntervals ()
    //    {
    //        List<SelectionInterval> intervals = new ArrayList<SelectionInterval> ();
    //        SelectionInterval current = null;
    //        final int[] rows = table.getSelectedRows ();
    //        for ( int i = 0; i < rows.length; i++ )
    //        {
    //            if ( current == null )
    //            {
    //                current = new SelectionInterval ();
    //                current.setStartIndex ( rows[ i ] );
    //                current.setEndIndex ( rows[ i ] );
    //                intervals.add ( current );
    //            }
    //            else
    //            {
    //                current.setEndIndex ( rows[ i ] );
    //            }
    //            if ( i < rows.length - 1 && rows[ i + 1 ] > rows[ i ] + 1 )
    //            {
    //                current = null;
    //            }
    //        }
    //        return intervals;
    //    }
    //
    //    private List<SelectionInterval> getColumnSelectionIntervals ()
    //    {
    //        List<SelectionInterval> intervals = new ArrayList<SelectionInterval> ();
    //        SelectionInterval current = null;
    //        final int[] cols = table.getSelectedColumns ();
    //        for ( int i = 0; i < cols.length; i++ )
    //        {
    //            if ( current == null )
    //            {
    //                current = new SelectionInterval ();
    //                current.setStartIndex ( cols[ i ] );
    //                current.setEndIndex ( cols[ i ] );
    //                intervals.add ( current );
    //            }
    //            else
    //            {
    //                current.setEndIndex ( cols[ i ] );
    //            }
    //            if ( i < cols.length - 1 && cols[ i + 1 ] > cols[ i ] + 1 )
    //            {
    //                current = null;
    //            }
    //        }
    //        return intervals;
    //    }
    //
    //    private class SelectionInterval
    //    {
    //        int startIndex = -1;
    //        int endIndex = -1;
    //
    //        public SelectionInterval ()
    //        {
    //            super ();
    //        }
    //
    //        public SelectionInterval ( int startIndex, int endIndex )
    //        {
    //            super ();
    //        }
    //
    //        private int getStartIndex ()
    //        {
    //            return startIndex;
    //        }
    //
    //        private void setStartIndex ( int startIndex )
    //        {
    //            this.startIndex = startIndex;
    //        }
    //
    //        private int getEndIndex ()
    //        {
    //            return endIndex;
    //        }
    //
    //        private void setEndIndex ( int endIndex )
    //        {
    //            this.endIndex = endIndex;
    //        }
    //    }
}