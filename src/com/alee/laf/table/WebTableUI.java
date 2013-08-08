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
        table.setFillsViewportHeight ( false );
        table.setBackground ( WebTableStyle.background );
        table.setForeground ( WebTableStyle.foreground );
        table.setSelectionBackground ( WebTableStyle.selectionBackground );
        table.setSelectionForeground ( WebTableStyle.selectionForeground );
        table.setRowHeight ( WebTableStyle.rowHeight );
        table.setShowHorizontalLines ( WebTableStyle.showHorizontalLines );
        table.setShowVerticalLines ( WebTableStyle.showVerticalLines );
        table.setIntercellSpacing ( WebTableStyle.cellsSpacing );

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
}