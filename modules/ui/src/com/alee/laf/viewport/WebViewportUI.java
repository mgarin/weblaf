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

import com.alee.managers.style.*;
import com.alee.painter.DefaultPainter;
import com.alee.painter.Painter;
import com.alee.painter.PainterSupport;
import com.alee.utils.swing.DataRunnable;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicViewportUI;
import java.awt.*;

/**
 * Custom UI for JViewport component.
 * JViewport is an unique component that doesn't allow any borders to be set thus it doesn't support margin or padding.
 *
 * @author Mikle Garin
 * @author Alexandr Zernov
 */

public class WebViewportUI extends BasicViewportUI implements Styleable, ShapeProvider
{
    /**
     * Component painter.
     */
    @DefaultPainter ( ViewportPainter.class )
    protected IViewportPainter painter;

    /**
     * Runtime variables.
     */
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
        StyleManager.installSkin ( viewport );
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
        StyleManager.uninstallSkin ( viewport );

        // Cleaning up reference
        viewport = null;

        // Uninstalling UI
        super.uninstallUI ( c );
    }

    @Override
    public StyleId getStyleId ()
    {
        return StyleManager.getStyleId ( viewport );
    }

    @Override
    public StyleId setStyleId ( final StyleId id )
    {
        return StyleManager.setStyleId ( viewport, id );
    }

    @Override
    public Shape provideShape ()
    {
        return PainterSupport.getShape ( viewport, painter );
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
        PainterSupport.setPainter ( viewport, new DataRunnable<IViewportPainter> ()
        {
            @Override
            public void run ( final IViewportPainter newPainter )
            {
                WebViewportUI.this.painter = newPainter;
            }
        }, this.painter, painter, IViewportPainter.class, AdaptiveViewportPainter.class );
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
        // return PainterSupport.getPreferredSize ( c, painter );
        return null;
    }
}