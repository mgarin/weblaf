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

import com.alee.api.annotations.NotNull;
import com.alee.api.annotations.Nullable;
import com.alee.api.data.Corner;
import com.alee.extended.canvas.WebCanvas;
import com.alee.laf.scroll.ScrollPaneCornerProvider;
import com.alee.laf.table.editors.WebBooleanEditor;
import com.alee.laf.table.editors.WebDateEditor;
import com.alee.laf.table.editors.WebGenericEditor;
import com.alee.laf.table.editors.WebNumberEditor;
import com.alee.laf.table.renderers.*;
import com.alee.managers.style.*;
import com.alee.painter.PainterSupport;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Date;

/**
 * Custom UI for {@link JTable} component.
 *
 * @author Mikle Garin
 */
public class WebTableUI extends WTableUI implements ScrollPaneCornerProvider
{
    /**
     * Listeners.
     */
    protected transient PropertyChangeListener propertyChangeListener;

    /**
     * Returns an instance of the {@link WebTableUI} for the specified component.
     * This tricky method is used by {@link UIManager} to create component UIs when needed.
     *
     * @param c component that will use UI instance
     * @return instance of the {@link WebTableUI}
     */
    @NotNull
    public static ComponentUI createUI ( @NotNull final JComponent c )
    {
        return new WebTableUI ();
    }

    @Override
    public void installUI ( @NotNull final JComponent c )
    {
        super.installUI ( c );

        // Configuring default renderers
        table.setDefaultRenderer ( Object.class,
                new WebTableCellRenderer.UIResource<Object, JTable, TableCellParameters<Object, JTable>> () );
        table.setDefaultRenderer ( Number.class,
                new WebTableNumberCellRenderer.UIResource<Number, JTable, TableCellParameters<Number, JTable>> () );
        table.setDefaultRenderer ( Double.class,
                new WebTableDoubleCellRenderer.UIResource<Double, JTable, TableCellParameters<Double, JTable>> () );
        table.setDefaultRenderer ( Float.class,
                new WebTableFloatCellRenderer.UIResource<Float, JTable, TableCellParameters<Float, JTable>> () );
        table.setDefaultRenderer ( Date.class,
                new WebTableDateCellRenderer.UIResource<Date, JTable, TableCellParameters<Date, JTable>> () );
        table.setDefaultRenderer ( Icon.class,
                new WebTableIconCellRenderer.UIResource<Icon, JTable, TableCellParameters<Icon, JTable>> () );
        table.setDefaultRenderer ( ImageIcon.class,
                new WebTableIconCellRenderer.UIResource<ImageIcon, JTable, TableCellParameters<ImageIcon, JTable>> () );
        table.setDefaultRenderer ( Boolean.class,
                new WebTableBooleanCellRenderer.UIResource<JTable, TableCellParameters<Boolean, JTable>> () );
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

        // Table header change listener
        updateTableHeaderStyleId ();
        propertyChangeListener = new PropertyChangeListener ()
        {
            @Override
            public void propertyChange ( @NotNull final PropertyChangeEvent evt )
            {
                updateTableHeaderStyleId ();
            }
        };
        table.addPropertyChangeListener ( WebTable.TABLE_HEADER_PROPERTY, propertyChangeListener );

        // Applying skin
        StyleManager.installSkin ( table );
    }

    @Override
    public void uninstallUI ( @NotNull final JComponent c )
    {
        // Uninstalling applied skin
        StyleManager.uninstallSkin ( table );

        // Cleaning up listeners
        table.removePropertyChangeListener ( WebTable.TABLE_HEADER_PROPERTY, propertyChangeListener );
        propertyChangeListener = null;

        super.uninstallUI ( c );
    }

    /**
     * Performs table header {@link StyleId} update.
     * This method helps to keep header {@link StyleId} in sync with table style.
     */
    protected void updateTableHeaderStyleId ()
    {
        // Header might be null so we should guard against it here
        final JTableHeader header = table.getTableHeader ();
        if ( header != null )
        {
            // Pairing table header style with table as parent
            StyleId.tableHeader.at ( table ).set ( header );
        }
    }

    @Nullable
    @Override
    public JComponent getCorner ( @NotNull final Corner type )
    {
        return type == Corner.upperTrailing ? new WebCanvas ( StyleId.tableCorner.at ( table ), type.name () ) : null;
    }

    @Override
    public boolean contains ( @NotNull final JComponent c, final int x, final int y )
    {
        return PainterSupport.contains ( c, this,  x, y );
    }

    @NotNull
    @Override
    public CellRendererPane getCellRendererPane ()
    {
        return rendererPane;
    }

    @Override
    public void paint ( @NotNull final Graphics g, @NotNull final JComponent c )
    {
        PainterSupport.paint ( g, c, this );
    }

    @Nullable
    @Override
    public Dimension getPreferredSize ( @NotNull final JComponent c )
    {
        return PainterSupport.getPreferredSize ( c, super.getPreferredSize ( c ) );
    }
}