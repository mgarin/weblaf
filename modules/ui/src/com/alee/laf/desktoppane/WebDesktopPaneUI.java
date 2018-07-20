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

package com.alee.laf.desktoppane;

import com.alee.managers.style.*;
import com.alee.painter.DefaultPainter;
import com.alee.painter.Painter;
import com.alee.painter.PainterSupport;
import com.alee.api.jdk.Consumer;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicDesktopPaneUI;
import java.awt.*;

/**
 * Custom UI for {@link JDesktopPane} component.
 *
 * @author Mikle Garin
 */
public class WebDesktopPaneUI extends BasicDesktopPaneUI implements ShapeSupport, MarginSupport, PaddingSupport
{
    /**
     * Component painter.
     */
    @DefaultPainter ( DesktopPanePainter.class )
    protected IDesktopPanePainter painter;

    /**
     * Runtime variables.
     */
    protected transient JDesktopPane desktopPane = null;

    /**
     * Returns an instance of the {@link WebDesktopPaneUI} for the specified component.
     * This tricky method is used by {@link UIManager} to create component UIs when needed.
     *
     * @param c component that will use UI instance
     * @return instance of the {@link WebDesktopPaneUI}
     */
    public static ComponentUI createUI ( final JComponent c )
    {
        return new WebDesktopPaneUI ();
    }

    @Override
    public void installUI ( final JComponent c )
    {
        super.installUI ( c );

        // Saving desktop pane to local variable
        desktopPane = ( JDesktopPane ) c;

        // Applying skin
        StyleManager.installSkin ( desktopPane );
    }

    @Override
    public void uninstallUI ( final JComponent c )
    {
        // Uninstalling applied skin
        StyleManager.uninstallSkin ( desktopPane );

        // Cleaning up reference
        desktopPane = null;

        // Uninstalling UI
        super.uninstallUI ( c );
    }

    @Override
    public Shape getShape ()
    {
        return PainterSupport.getShape ( desktopPane, painter );
    }

    @Override
    public boolean isShapeDetectionEnabled ()
    {
        return PainterSupport.isShapeDetectionEnabled ( desktopPane, painter );
    }

    @Override
    public void setShapeDetectionEnabled ( final boolean enabled )
    {
        PainterSupport.setShapeDetectionEnabled ( desktopPane, painter, enabled );
    }

    @Override
    public Insets getMargin ()
    {
        return PainterSupport.getMargin ( desktopPane );
    }

    @Override
    public void setMargin ( final Insets margin )
    {
        PainterSupport.setMargin ( desktopPane, margin );
    }

    @Override
    public Insets getPadding ()
    {
        return PainterSupport.getPadding ( desktopPane );
    }

    @Override
    public void setPadding ( final Insets padding )
    {
        PainterSupport.setPadding ( desktopPane, padding );
    }

    /**
     * Returns desktop pane painter.
     *
     * @return desktop pane painter
     */
    public Painter getPainter ()
    {
        return PainterSupport.getPainter ( painter );
    }

    /**
     * Sets desktop pane painter.
     * Pass null to remove desktop pane painter.
     *
     * @param painter new desktop pane painter
     */
    public void setPainter ( final Painter painter )
    {
        PainterSupport.setPainter ( desktopPane, new Consumer<IDesktopPanePainter> ()
        {
            @Override
            public void accept ( final IDesktopPanePainter newPainter )
            {
                WebDesktopPaneUI.this.painter = newPainter;
            }
        }, this.painter, painter, IDesktopPanePainter.class, AdaptiveDesktopPanePainter.class );
    }

    @Override
    public boolean contains ( final JComponent c, final int x, final int y )
    {
        return PainterSupport.contains ( c, this, painter, x, y );
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
        // return PainterSupport.getPreferredSize ( c, painter );
        return null;
    }
}