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
import com.alee.laf.panel.WebPanel;
import com.alee.laf.scroll.ScrollCornerProvider;
import com.alee.laf.table.editors.WebBooleanEditor;
import com.alee.laf.table.editors.WebDateEditor;
import com.alee.laf.table.editors.WebGenericEditor;
import com.alee.laf.table.editors.WebNumberEditor;
import com.alee.laf.table.renderers.*;
import com.alee.managers.style.*;
import com.alee.managers.style.Bounds;
import com.alee.painter.DefaultPainter;
import com.alee.painter.Painter;
import com.alee.painter.PainterSupport;
import com.alee.utils.swing.DataRunnable;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicTableUI;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Date;

/**
 * Custom UI for JTable component.
 *
 * @author Mikle Garin
 */

public class WebTableUI extends BasicTableUI implements Styleable, ShapeProvider, MarginSupport, PaddingSupport, ScrollCornerProvider
{
    /**
     * Component painter.
     */
    @DefaultPainter ( TablePainter.class )
    protected ITablePainter painter;

    /**
     * Listeners.
     */
    protected PropertyChangeListener propertyChangeListener;

    /**
     * Runtime variables.
     */
    protected Insets margin = null;
    protected Insets padding = null;

    /**
     * Returns an instance of the WebTreeUI for the specified component.
     * This tricky method is used by UIManager to create component UIs when needed.
     *
     * @param c component that will use UI instance
     * @return instance of the WebTreeUI
     */
    @SuppressWarnings ("UnusedParameters")
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

        // Table header change listener
        propertyChangeListener = new PropertyChangeListener ()
        {
            @Override
            public void propertyChange ( final PropertyChangeEvent evt )
            {
                // Header might be null so we should guard against it here
                final JTableHeader header = table.getTableHeader ();
                if ( header != null )
                {
                    // Pairing table header style with table as parent
                    StyleId.tableHeader.at ( table ).set ( header );
                }
            }
        };
        table.addPropertyChangeListener ( WebLookAndFeel.TABLE_HEADER_PROPERTY, propertyChangeListener );

        // Applying skin
        StyleManager.installSkin ( table );
    }

    /**
     * Uninstalls UI from the specified component.
     *
     * @param c component with this UI
     */
    @Override
    public void uninstallUI ( final JComponent c )
    {
        // Uninstalling applied skin
        StyleManager.uninstallSkin ( table );

        // Cleaning up listeners
        table.removePropertyChangeListener ( WebLookAndFeel.TABLE_HEADER_PROPERTY, propertyChangeListener );
        propertyChangeListener = null;

        super.uninstallUI ( c );
    }

    @Override
    public StyleId getStyleId ()
    {
        return StyleManager.getStyleId ( table );
    }

    @Override
    public StyleId setStyleId ( final StyleId id )
    {
        return StyleManager.setStyleId ( table, id );
    }

    @Override
    public Shape provideShape ()
    {
        return PainterSupport.getShape ( table, painter );
    }

    @Override
    public Insets getMargin ()
    {
        return margin;
    }

    @Override
    public void setMargin ( final Insets margin )
    {
        this.margin = margin;
        PainterSupport.updateBorder ( getPainter () );
    }

    @Override
    public Insets getPadding ()
    {
        return padding;
    }

    @Override
    public void setPadding ( final Insets padding )
    {
        this.padding = padding;
        PainterSupport.updateBorder ( getPainter () );
    }

    /**
     * Returns text field painter.
     *
     * @return text field painter
     */
    public Painter getPainter ()
    {
        return PainterSupport.getAdaptedPainter ( painter );
    }

    /**
     * Sets text field painter.
     * Pass null to remove text field painter.
     *
     * @param painter new text field painter
     */
    public void setPainter ( final Painter painter )
    {
        PainterSupport.setPainter ( table, new DataRunnable<ITablePainter> ()
        {
            @Override
            public void run ( final ITablePainter newPainter )
            {
                WebTableUI.this.painter = newPainter;
            }
        }, this.painter, painter, ITablePainter.class, AdaptiveTablePainter.class );
    }

    @Override
    public JComponent getCorner ( final String key )
    {
        return JScrollPane.UPPER_TRAILING_CORNER.equals ( key ) ? new WebPanel ( StyleId.tableCorner.at ( table ) ) : null;
    }

    @Override
    public void paint ( final Graphics g, final JComponent c )
    {
        if ( painter != null )
        {
            painter.prepareToPaint ( rendererPane );
            painter.paint ( ( Graphics2D ) g, Bounds.component.of ( c ), c, this );
        }
    }

    @Override
    public Dimension getPreferredSize ( final JComponent c )
    {
        return PainterSupport.getPreferredSize ( c, super.getPreferredSize ( c ), painter );
    }
}