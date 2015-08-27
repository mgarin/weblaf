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

import com.alee.extended.painter.Painter;
import com.alee.extended.painter.PainterSupport;
import com.alee.managers.style.StyleId;
import com.alee.managers.style.StyleManager;
import com.alee.utils.SwingUtils;
import com.alee.utils.laf.ShapeProvider;
import com.alee.utils.laf.Styleable;
import com.alee.utils.swing.DataRunnable;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicDesktopIconUI;
import java.awt.*;

/**
 * @author Mikle Garin
 * @author Alexandr Zernov
 */

public class WebDesktopIconUI extends BasicDesktopIconUI implements Styleable, ShapeProvider
{
    /**
     * Component painter.
     */
    protected DesktopIconPainter painter;

    /**
     * Runtime variables.
     */
    protected StyleId styleId = null;

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
        StyleManager.applySkin ( desktopIcon );
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
        StyleManager.removeSkin ( desktopIcon );

        super.uninstallUI ( c );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public StyleId getStyleId ()
    {
        return StyleManager.getStyleId ( frame );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setStyleId ( final StyleId id )
    {
        StyleManager.setStyleId ( frame, id );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Shape provideShape ()
    {
        return PainterSupport.getShape ( frame, painter );
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
        PainterSupport.setPainter ( frame, new DataRunnable<DesktopIconPainter> ()
        {
            @Override
            public void run ( final DesktopIconPainter newPainter )
            {
                WebDesktopIconUI.this.painter = newPainter;
            }
        }, this.painter, painter, DesktopIconPainter.class, AdaptiveDesktopIconPainter.class );
    }

    @Override
    protected void installComponents ()
    {
        iconPane = new WebInternalFrameIconPane ( frame );
        desktopIcon.setLayout ( new BorderLayout () );
        desktopIcon.add ( iconPane, BorderLayout.CENTER );
    }

    /**
     * Paints desktop icon.
     *
     * @param g graphics
     * @param c component
     */
    @Override
    public void paint ( final Graphics g, final JComponent c )
    {
        if ( painter != null )
        {
            painter.paint ( ( Graphics2D ) g, SwingUtils.size ( c ), c, this );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Dimension getPreferredSize ( final JComponent c )
    {
        return PainterSupport.getPreferredSize ( c, super.getPreferredSize ( c ), painter );
    }
}