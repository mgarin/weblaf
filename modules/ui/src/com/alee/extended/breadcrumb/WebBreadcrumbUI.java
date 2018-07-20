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

package com.alee.extended.breadcrumb;

import com.alee.api.jdk.Consumer;
import com.alee.managers.style.*;
import com.alee.painter.DefaultPainter;
import com.alee.painter.Painter;
import com.alee.painter.PainterSupport;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import java.awt.*;

/**
 * Custom UI for {@link WebBreadcrumb} component.
 *
 * @param <C> component type
 * @author Mikle Garin
 */
public class WebBreadcrumbUI<C extends WebBreadcrumb> extends WBreadcrumbUI<C> implements ShapeSupport, MarginSupport, PaddingSupport
{
    /**
     * Component painter.
     */
    @DefaultPainter ( BreadcrumbPainter.class )
    protected IBreadcrumbPainter painter;

    /**
     * Returns an instance of the {@link WebBreadcrumbUI} for the specified component.
     * This tricky method is used by {@link UIManager} to create component UIs when needed.
     *
     * @param c component that will use UI instance
     * @return instance of the {@link WebBreadcrumbUI}
     */
    public static ComponentUI createUI ( final JComponent c )
    {
        return new WebBreadcrumbUI ();
    }

    @Override
    public void installUI ( final JComponent c )
    {
        // Installing UI
        super.installUI ( c );

        // Applying skin
        StyleManager.installSkin ( breadcrumb );
    }

    @Override
    public void uninstallUI ( final JComponent c )
    {
        // Uninstalling applied skin
        StyleManager.uninstallSkin ( breadcrumb );

        // Uninstalling UI
        super.uninstallUI ( c );
    }

    /**
     * Creates and returns default breadcrumb layout.
     *
     * @return default breadcrumb layout
     */
    protected BreadcrumbLayout createDefaultLayout ()
    {
        return new BreadcrumbLayout ();
    }

    @Override
    public Shape getShape ()
    {
        return PainterSupport.getShape ( breadcrumb, painter );
    }

    @Override
    public boolean isShapeDetectionEnabled ()
    {
        return PainterSupport.isShapeDetectionEnabled ( breadcrumb, painter );
    }

    @Override
    public void setShapeDetectionEnabled ( final boolean enabled )
    {
        PainterSupport.setShapeDetectionEnabled ( breadcrumb, painter, enabled );
    }

    @Override
    public Insets getMargin ()
    {
        return PainterSupport.getMargin ( breadcrumb );
    }

    @Override
    public void setMargin ( final Insets margin )
    {
        PainterSupport.setMargin ( breadcrumb, margin );
    }

    @Override
    public Insets getPadding ()
    {
        return PainterSupport.getPadding ( breadcrumb );
    }

    @Override
    public void setPadding ( final Insets padding )
    {
        PainterSupport.setPadding ( breadcrumb, padding );
    }

    /**
     * Returns breadcrumb painter.
     *
     * @return breadcrumb painter
     */
    public Painter getPainter ()
    {
        return PainterSupport.getPainter ( painter );
    }

    /**
     * Sets breadcrumb painter.
     * Pass null to remove breadcrumb painter.
     *
     * @param painter new breadcrumb painter
     */
    public void setPainter ( final Painter painter )
    {
        PainterSupport.setPainter ( breadcrumb, new Consumer<IBreadcrumbPainter> ()
        {
            @Override
            public void accept ( final IBreadcrumbPainter newPainter )
            {
                WebBreadcrumbUI.this.painter = newPainter;
            }
        }, this.painter, painter, IBreadcrumbPainter.class, AdaptiveBreadcrumbPainter.class );
    }

    @Override
    public int getBaseline ( final JComponent c, final int width, final int height )
    {
        return PainterSupport.getBaseline ( c, this, painter, width, height );
    }

    @Override
    public Component.BaselineResizeBehavior getBaselineResizeBehavior ( final JComponent c )
    {
        return PainterSupport.getBaselineResizeBehavior ( c, this, painter );
    }

    @Override
    public void paint ( final Graphics g, final JComponent c )
    {
        if ( painter != null )
        {
            painter.paint ( ( Graphics2D ) g, c, this, new Bounds ( c ) );
        }
    }

    @Override
    public Dimension getPreferredSize ( final JComponent c )
    {
        return PainterSupport.getPreferredSize ( c, painter );
    }
}