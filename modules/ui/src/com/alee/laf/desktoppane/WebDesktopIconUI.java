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
import com.alee.utils.swing.DataRunnable;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicDesktopIconUI;
import java.awt.*;

/**
 * @author Mikle Garin
 * @author Alexandr Zernov
 */

public class WebDesktopIconUI extends BasicDesktopIconUI implements Styleable, ShapeProvider, MarginSupport, PaddingSupport
{
    /**
     * Component painter.
     */
    @DefaultPainter ( DesktopIconPainter.class )
    protected IDesktopIconPainter painter;

    /**
     * Runtime variables.
     */
    protected Insets margin = null;
    protected Insets padding = null;

    /**
     * Returns an instance of the WebDesktopIconUI for the specified component.
     * This tricky method is used by UIManager to create component UIs when needed.
     *
     * @param c component that will use UI instance
     * @return instance of the WebDesktopIconUI
     */
    @SuppressWarnings ("UnusedParameters")
    public static ComponentUI createUI ( final JComponent c )
    {
        return new WebDesktopIconUI ();
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

        // Applying skin
        StyleManager.installSkin ( desktopIcon );
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
    public StyleId getStyleId ()
    {
        return StyleManager.getStyleId ( desktopIcon );
    }

    @Override
    public StyleId setStyleId ( final StyleId id )
    {
        return StyleManager.setStyleId ( desktopIcon, id );
    }

    @Override
    public Shape provideShape ()
    {
        return PainterSupport.getShape ( desktopIcon, painter );
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
     * Returns desktop icon painter.
     *
     * @return desktop icon painter
     */
    public Painter getPainter ()
    {
        return PainterSupport.getAdaptedPainter ( painter );
    }

    /**
     * Sets desktop icon painter.
     * Pass null to remove desktop icon painter.
     *
     * @param painter new desktop icon painter
     */
    public void setPainter ( final Painter painter )
    {
        PainterSupport.setPainter ( desktopIcon, new DataRunnable<IDesktopIconPainter> ()
        {
            @Override
            public void run ( final IDesktopIconPainter newPainter )
            {
                WebDesktopIconUI.this.painter = newPainter;
            }
        }, this.painter, painter, IDesktopIconPainter.class, AdaptiveDesktopIconPainter.class );
    }

    @Override
    protected void installComponents ()
    {
        desktopIcon.setLayout ( new BorderLayout () );
        iconPane = new WebInternalFrameTitlePane ( desktopIcon, frame );
        desktopIcon.add ( iconPane, BorderLayout.CENTER );
    }

    @Override
    public void paint ( final Graphics g, final JComponent c )
    {
        if ( painter != null )
        {
            painter.paint ( ( Graphics2D ) g, Bounds.component.of ( c ), c, this );
        }
    }

    @Override
    public Dimension getPreferredSize ( final JComponent c )
    {
        return PainterSupport.getPreferredSize ( c, super.getPreferredSize ( c ), painter );
    }
}