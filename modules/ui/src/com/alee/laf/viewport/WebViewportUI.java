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

package com.alee.laf.viewport;

import com.alee.extended.painter.Painter;
import com.alee.extended.painter.PainterSupport;
import com.alee.managers.style.StyleManager;
import com.alee.utils.SwingUtils;
import com.alee.utils.laf.Styleable;
import com.alee.utils.swing.DataRunnable;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicViewportUI;
import java.awt.*;

/**
 * Custom UI for JViewport component.
 *
 * @author Mikle Garin
 */

public class WebViewportUI extends BasicViewportUI implements Styleable
{
    /**
     * Component painter.
     */
    protected ViewportPainter painter;

    /**
     * Runtime variables.
     */
    protected String styleId = null;
    protected JViewport viewport = null;

    /**
     * Returns an instance of the WebViewportUI for the specified component.
     * This tricky method is used by UIManager to create component UIs when needed.
     *
     * @param c component that will use UI instance
     * @return instance of the WebViewportUI
     */
    @SuppressWarnings ("UnusedParameters")
    public static ComponentUI createUI ( final JComponent c )
    {
        return new WebViewportUI ();
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

        // Saving separator to local variable
        viewport = ( JViewport ) c;

        // Applying skin
        StyleManager.applySkin ( viewport );
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
        StyleManager.removeSkin ( viewport );

        // Cleaning up reference
        viewport = null;

        // Uninstalling UI
        super.uninstallUI ( c );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getStyleId ()
    {
        return styleId;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setStyleId ( final String id )
    {
        this.styleId = id;
        StyleManager.applySkin ( viewport );
    }

    /**
     * Returns viewport painter.
     *
     * @return viewport painter
     */
    public Painter getPainter ()
    {
        return PainterSupport.getAdaptedPainter ( painter );
    }

    /**
     * Sets viewport painter.
     * Pass null to remove viewport painter.
     *
     * @param painter new viewport painter
     */
    public void setPainter ( final Painter painter )
    {
        PainterSupport.setPainter ( viewport, new DataRunnable<ViewportPainter> ()
        {
            @Override
            public void run ( final ViewportPainter newPainter )
            {
                WebViewportUI.this.painter = newPainter;
            }
        }, this.painter, painter, ViewportPainter.class, AdaptiveViewportPainter.class );
    }

    /**
     * Paints viewport.
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