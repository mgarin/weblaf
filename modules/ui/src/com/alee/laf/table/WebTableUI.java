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

import com.alee.laf.WebLookAndFeel;
import com.alee.laf.table.editors.WebBooleanEditor;
import com.alee.laf.table.editors.WebDateEditor;
import com.alee.laf.table.editors.WebGenericEditor;
import com.alee.laf.table.editors.WebNumberEditor;
import com.alee.laf.table.renderers.*;
import com.alee.managers.tooltip.ToolTipProvider;
import com.alee.utils.CompareUtils;
import com.alee.utils.SwingUtils;
import com.alee.utils.swing.AncestorAdapter;

import javax.swing.*;
import javax.swing.event.AncestorEvent;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicTableUI;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Date;

/**
 * Custom UI for JTable component.
 *
 * @author Mikle Garin
 */

public class WebTableUI extends BasicTableUI
{
    /**
     * Table listeners.
     */
    protected AncestorAdapter ancestorAdapter;
    protected MouseAdapter mouseAdapter;

    private Color scrollPaneBackgroundColor = WebTableStyle.scrollPaneBackgroundColor;

    /**
     * Runtime variables.
     */
    protected Point rolloverCell;

    public Color getScrollPaneBackgroundColor ()
    {
        return scrollPaneBackgroundColor;
    }

    public void setScrollPaneBackgroundColor ( final Color scrollPaneBackgroundColor )
    {
        this.scrollPaneBackgroundColor = scrollPaneBackgroundColor;
    }

    /**
     * Returns an instance of the WebTreeUI for the specified component.
     * This tricky method is used by UIManager to create component UIs when needed.
     *
     * @param c component that will use UI instance
     * @return instance of the WebTreeUI
     */
    @SuppressWarnings ( "UnusedParameters" )
    public static ComponentUI createUI ( final JComponent c )
    {
        return new WebTableUI ();
    }

    /**
     * Installs UI in the specified component.
     *
     * @param c component for this UI
     */
    @Override
    public void installUI ( final JComponent c )
    {
        super.installUI ( c );

        // Default settings
        SwingUtils.setOrientation ( table );
        LookAndFeel.installProperty ( table, WebLookAndFeel.OPAQUE_PROPERTY, Boolean.FALSE );
        table.setFillsViewportHeight ( false );
        table.setBackground ( WebTableStyle.background );
        table.setForeground ( WebTableStyle.foreground );
        table.setSelectionBackground ( WebTableStyle.selectionBackground );
        table.setSelectionForeground ( WebTableStyle.selectionForeground );
        table.setRowHeight ( WebTableStyle.rowHeight );
        table.setShowHorizontalLines ( WebTableStyle.showHorizontalLines );
        table.setShowVerticalLines ( WebTableStyle.showVerticalLines );
        table.setIntercellSpacing ( WebTableStyle.cellsSpacing );

        // todo Save and restore old renderers/editors on uninstall
        // Configuring default renderers
        table.setDefaultRenderer ( Object.class, new WebTableCellRenderer () );
        table.setDefaultRenderer ( Number.class, new WebNumberRenderer () );
        table.setDefaultRenderer ( Double.class, new WebDoubleRenderer () );
        table.setDefaultRenderer ( Float.class, new WebDoubleRenderer () );
        table.setDefaultRenderer ( Date.class, new WebDateRenderer () );
        table.setDefaultRenderer ( Icon.class, new WebIconRenderer () );
        table.setDefaultRenderer ( ImageIcon.class, new WebIconRenderer () );
        table.setDefaultRenderer ( Boolean.class, new WebBooleanRenderer () );
        // todo Additional renderers:
        // table.setDefaultRenderer ( Dimension.class,  );
        // table.setDefaultRenderer ( Point.class,  );
        // table.setDefaultRenderer ( File.class,  );
        // table.setDefaultRenderer ( Color.class,  );
        // table.setDefaultRenderer ( List.class,  );

        // Configuring default editors
        table.setDefaultEditor ( Object.class, new WebGenericEditor () );
        table.setDefaultEditor ( Number.class, new WebNumberEditor () );
        table.setDefaultEditor ( Boolean.class, new WebBooleanEditor () );
        table.setDefaultEditor ( Date.class, new WebDateEditor () );
        // todo Additional editors:
        // table.setDefaultEditor ( Dimension.class,  );
        // table.setDefaultEditor ( Point.class,  );
        // table.setDefaultEditor ( File.class,  );
        // table.setDefaultEditor ( Color.class,  );
        // table.setDefaultEditor ( List.class,  );

        // Configuring scrollpane corner
        configureEnclosingScrollPaneUI ( table );
        ancestorAdapter = new AncestorAdapter ()
        {
            @Override
            public void ancestorAdded ( final AncestorEvent event )
            {
                configureEnclosingScrollPaneUI ( table );
            }
        };
        table.addAncestorListener ( ancestorAdapter );

        // Rollover listener
        mouseAdapter = new MouseAdapter ()
        {
            @Override
            public void mouseMoved ( final MouseEvent e )
            {
                updateMouseover ( e );
            }

            @Override
            public void mouseDragged ( final MouseEvent e )
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
                final Point point = e.getPoint ();
                final Point cell = new Point ( table.columnAtPoint ( point ), table.rowAtPoint ( point ) );
                if ( cell.x != -1 && cell.y != -1 )
                {
                    if ( !CompareUtils.equals ( rolloverCell, cell ) )
                    {
                        updateRolloverCell ( rolloverCell, cell );
                    }
                }
                else
                {
                    clearMouseover ();
                }
            }

            private void clearMouseover ()
            {
                if ( rolloverCell != null )
                {
                    updateRolloverCell ( rolloverCell, null );
                }
            }

            private void updateRolloverCell ( final Point oldCell, final Point newCell )
            {
                // Updating rollover cell
                rolloverCell = newCell;

                // Updating custom WebLaF tooltip display state
                final ToolTipProvider provider = getToolTipProvider ();
                if ( provider != null )
                {
                    final int oldIndex = oldCell != null ? oldCell.y : -1;
                    final int oldColumn = oldCell != null ? oldCell.x : -1;
                    final int newIndex = newCell != null ? newCell.y : -1;
                    final int newColumn = newCell != null ? newCell.x : -1;
                    provider.rolloverCellChanged ( table, oldIndex, oldColumn, newIndex, newColumn );
                }
            }
        };
        table.addMouseListener ( mouseAdapter );
        table.addMouseMotionListener ( mouseAdapter );
    }

    /**
     * Uninstalls UI from the specified component.
     *
     * @param c component with this UI
     */
    @Override
    public void uninstallUI ( final JComponent c )
    {
        table.removeMouseListener ( mouseAdapter );
        table.removeMouseMotionListener ( mouseAdapter );
        table.removeAncestorListener ( ancestorAdapter );

        super.uninstallUI ( c );
    }

    /**
     * Returns custom WebLaF tooltip provider.
     *
     * @return custom WebLaF tooltip provider
     */
    protected ToolTipProvider<? extends WebTable> getToolTipProvider ()
    {
        return table != null && table instanceof WebTable ? ( ( WebTable ) table ).getToolTipProvider () : null;
    }

    /**
     * Configures table scroll pane with UI specific settings.
     *
     * @param table table to process
     */
    protected void configureEnclosingScrollPaneUI ( final JTable table )
    {
        // Retrieving table scroll pane if it has one
        final JScrollPane scrollPane = SwingUtils.getScrollPane ( table );
        if ( scrollPane != null )
        {
            // Make certain we are the viewPort's view and not, for
            // example, the rowHeaderView of the scrollPane -
            // an implementor of fixed columns might do this.
            final JViewport viewport = scrollPane.getViewport ();
            if ( viewport == null || viewport.getView () != table )
            {
                return;
            }

            scrollPane.getViewport().setBackground( scrollPaneBackgroundColor );

            // Adding both corners to the scroll pane for both orientation cases
            scrollPane.setCorner ( JScrollPane.UPPER_LEADING_CORNER, new WebTableCorner ( false ) );
            scrollPane.setCorner ( JScrollPane.UPPER_TRAILING_CORNER, new WebTableCorner ( true ) );
        }
    }
}