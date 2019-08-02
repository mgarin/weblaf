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
import javax.swing.plaf.basic.BasicDesktopIconUI;
import java.awt.*;

/**
 * Custom UI for {@link JInternalFrame.JDesktopIcon} component.
 *
 * @author Mikle Garin
 * @author Alexandr Zernov
 */
public class WebDesktopIconUI extends BasicDesktopIconUI implements ShapeSupport, MarginSupport, PaddingSupport
{
    /**
     * Component painter.
     */
    @DefaultPainter ( DesktopIconPainter.class )
    protected IDesktopIconPainter painter;

    /**
     * Returns an instance of the {@link WebDesktopIconUI} for the specified component.
     * This tricky method is used by {@link UIManager} to create component UIs when needed.
     *
     * @param c component that will use UI instance
     * @return instance of the {@link WebDesktopIconUI}
     */
    public static ComponentUI createUI ( final JComponent c )
    {
        return new WebDesktopIconUI ();
    }

    @Override
    public void installUI ( final JComponent c )
    {
        super.installUI ( c );

        // Applying skin
        StyleManager.installSkin ( desktopIcon );
    }

    @Override
    public void uninstallUI ( final JComponent c )
    {
        // Uninstalling applied skin
        StyleManager.uninstallSkin ( desktopIcon );

        super.uninstallUI ( c );
    }

    @Override
    protected void installDefaults ()
    {
        //
    }

    @Override
    protected void uninstallDefaults ()
    {
        //
    }

    @Override
    protected void installComponents ()
    {
        iconPane = new WebInternalFrameTitlePane ( desktopIcon, frame );
        desktopIcon.setLayout ( new BorderLayout () );
        desktopIcon.add ( iconPane, BorderLayout.CENTER );
    }

    @Override
    protected void uninstallComponents ()
    {
        desktopIcon.remove ( iconPane );
        desktopIcon.setLayout ( null );
        iconPane = null;
    }

    @Override
    protected void installListeners ()
    {
        // Installing default listeners
        super.installListeners ();

        // Instaling custom listeners
        if ( iconPane instanceof WebInternalFrameTitlePane )
        {
            ( ( WebInternalFrameTitlePane ) iconPane ).install ();
        }
    }

    @Override
    protected void uninstallListeners ()
    {
        // Uninstaling custom listeners
        if ( iconPane instanceof WebInternalFrameTitlePane )
        {
            ( ( WebInternalFrameTitlePane ) iconPane ).uninstall ();
        }

        // Uninstalling default listeners
        super.uninstallListeners ();
    }

    @Override
    public Shape getShape ()
    {
        return PainterSupport.getShape ( desktopIcon, painter );
    }

    @Override
    public boolean isShapeDetectionEnabled ()
    {
        return PainterSupport.isShapeDetectionEnabled ( desktopIcon, painter );
    }

    @Override
    public void setShapeDetectionEnabled ( final boolean enabled )
    {
        PainterSupport.setShapeDetectionEnabled ( desktopIcon, painter, enabled );
    }

    @Override
    public Insets getMargin ()
    {
        return PainterSupport.getMargin ( desktopIcon );
    }

    @Override
    public void setMargin ( final Insets margin )
    {
        PainterSupport.setMargin ( desktopIcon, margin );
    }

    @Override
    public Insets getPadding ()
    {
        return PainterSupport.getPadding ( desktopIcon );
    }

    @Override
    public void setPadding ( final Insets padding )
    {
        PainterSupport.setPadding ( desktopIcon, padding );
    }

    /**
     * Returns desktop icon painter.
     *
     * @return desktop icon painter
     */
    public Painter getPainter ()
    {
        return PainterSupport.getPainter ( painter );
    }

    /**
     * Sets desktop icon painter.
     * Pass null to remove desktop icon painter.
     *
     * @param painter new desktop icon painter
     */
    public void setPainter ( final Painter painter )
    {
        PainterSupport.setPainter ( desktopIcon, new Consumer<IDesktopIconPainter> ()
        {
            @Override
            public void accept ( final IDesktopIconPainter newPainter )
            {
                WebDesktopIconUI.this.painter = newPainter;
            }
        }, this.painter, painter, IDesktopIconPainter.class, AdaptiveDesktopIconPainter.class );
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
        return PainterSupport.getPreferredSize ( c, super.getPreferredSize ( c ), painter );
    }
}