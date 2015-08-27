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
import com.alee.utils.laf.Styleable;
import com.alee.utils.swing.DataRunnable;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicDesktopPaneUI;
import java.awt.*;

/**
 * User: mgarin Date: 17.08.11 Time: 23:14
 */

public class WebDesktopPaneUI extends BasicDesktopPaneUI implements Styleable
{
    /**
     * Component painter.
     */
    protected DesktopPanePainter painter;

    /**
     * Runtime variables.
     */
    protected StyleId styleId = null;
    protected JDesktopPane desktopPane = null;

    @SuppressWarnings ("UnusedParameters")
    public static ComponentUI createUI ( final JComponent c )
    {
        return new WebDesktopPaneUI ();
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

        // Saving desktop pane to local variable
        desktopPane = ( JDesktopPane ) c;

        // Applying skin
        StyleManager.applySkin ( desktopPane );
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
        StyleManager.removeSkin ( desktopPane );

        // Cleaning up reference
        desktopPane = null;

        // Uninstalling UI
        super.uninstallUI ( c );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public StyleId getStyleId ()
    {
        return StyleManager.getStyleId ( desktopPane );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setStyleId ( final StyleId id )
    {
        StyleManager.setStyleId ( desktopPane, id );
    }

    /**
     * Returns desktop pane painter.
     *
     * @return desktop pane painter
     */
    public Painter getPainter ()
    {
        return PainterSupport.getAdaptedPainter ( painter );
    }

    /**
     * Sets desktop pane painter.
     * Pass null to remove desktop pane painter.
     *
     * @param painter new desktop pane painter
     */
    public void setPainter ( final Painter painter )
    {
        PainterSupport.setPainter ( desktopPane, new DataRunnable<DesktopPanePainter> ()
        {
            @Override
            public void run ( final DesktopPanePainter newPainter )
            {
                WebDesktopPaneUI.this.painter = newPainter;
            }
        }, this.painter, painter, DesktopPanePainter.class, AdaptiveDesktopPanePainter.class );
    }

    /**
     * Paints desktop pane.
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
}