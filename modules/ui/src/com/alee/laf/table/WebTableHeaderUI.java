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

import com.alee.laf.table.renderers.WebTableHeaderCellRenderer;
import com.alee.managers.style.*;
import com.alee.painter.DefaultPainter;
import com.alee.painter.Painter;
import com.alee.painter.PainterSupport;
import com.alee.api.jdk.Consumer;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicTableHeaderUI;
import java.awt.*;

/**
 * Custom UI for {@link javax.swing.table.JTableHeader} component.
 *
 * @author Mikle Garin
 */
public class WebTableHeaderUI extends BasicTableHeaderUI implements ShapeSupport, MarginSupport, PaddingSupport
{
    /**
     * Component painter.
     */
    @DefaultPainter ( TableHeaderPainter.class )
    protected ITableHeaderPainter painter;

    /**
     * Returns an instance of the {@link WebTableHeaderUI} for the specified component.
     * This tricky method is used by {@link UIManager} to create component UIs when needed.
     *
     * @param c component that will use UI instance
     * @return instance of the {@link WebTableHeaderUI}
     */
    public static ComponentUI createUI ( final JComponent c )
    {
        return new WebTableHeaderUI ();
    }

    @Override
    public void installUI ( final JComponent c )
    {
        super.installUI ( c );

        // Applying skin
        StyleManager.installSkin ( header );

        // Default renderer
        header.setDefaultRenderer ( new WebTableHeaderCellRenderer.UIResource () );
    }

    @Override
    public void uninstallUI ( final JComponent c )
    {
        // Uninstalling applied skin
        StyleManager.uninstallSkin ( header );

        super.uninstallUI ( c );
    }

    @Override
    public Shape getShape ()
    {
        return PainterSupport.getShape ( header, painter );
    }

    @Override
    public boolean isShapeDetectionEnabled ()
    {
        return PainterSupport.isShapeDetectionEnabled ( header, painter );
    }

    @Override
    public void setShapeDetectionEnabled ( final boolean enabled )
    {
        PainterSupport.setShapeDetectionEnabled ( header, painter, enabled );
    }

    @Override
    public Insets getMargin ()
    {
        return PainterSupport.getMargin ( header );
    }

    @Override
    public void setMargin ( final Insets margin )
    {
        PainterSupport.setMargin ( header, margin );
    }

    @Override
    public Insets getPadding ()
    {
        return PainterSupport.getPadding ( header );
    }

    @Override
    public void setPadding ( final Insets padding )
    {
        PainterSupport.setPadding ( header, padding );
    }

    /**
     * Returns table header painter.
     *
     * @return table header painter
     */
    public Painter getPainter ()
    {
        return PainterSupport.getPainter ( painter );
    }

    /**
     * Sets table header painter.
     * Pass null to remove table header painter.
     *
     * @param painter new table header painter
     */
    public void setPainter ( final Painter painter )
    {
        PainterSupport.setPainter ( header, new Consumer<ITableHeaderPainter> ()
        {
            @Override
            public void accept ( final ITableHeaderPainter newPainter )
            {
                WebTableHeaderUI.this.painter = newPainter;
            }
        }, this.painter, painter, ITableHeaderPainter.class, AdaptiveTableHeaderPainter.class );
    }

    @Override
    public boolean contains ( final JComponent c, final int x, final int y )
    {
        return PainterSupport.contains ( c, this, painter, x, y );
    }

    @Override
    public void paint ( final Graphics g, final JComponent c )
    {
        if ( painter != null )
        {
            painter.prepareToPaint ( rendererPane );
            painter.paint ( ( Graphics2D ) g, c, this, new Bounds ( c ) );
        }
    }

    @Override
    public Dimension getPreferredSize ( final JComponent c )
    {
        return PainterSupport.getPreferredSize ( c, super.getPreferredSize ( c ), painter );
    }
}