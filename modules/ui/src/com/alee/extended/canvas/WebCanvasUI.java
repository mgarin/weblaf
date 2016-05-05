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

package com.alee.extended.canvas;

import com.alee.managers.style.*;
import com.alee.painter.DefaultPainter;
import com.alee.painter.Painter;
import com.alee.painter.PainterSupport;
import com.alee.utils.swing.DataRunnable;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import java.awt.*;

/**
 * Custom UI for WebCanvas component.
 *
 * @author Mikle Garin
 */

public class WebCanvasUI extends CanvasUI implements Styleable, ShapeProvider, MarginSupport, PaddingSupport
{
    /**
     * Component painter.
     */
    @DefaultPainter ( CanvasPainter.class )
    protected ICanvasPainter painter;

    /**
     * Runtime variables.
     */
    protected WebCanvas canvas;
    protected Insets margin = null;
    protected Insets padding = null;

    /**
     * Returns an instance of the WebCanvasUI for the specified component.
     * This tricky method is used by UIManager to create component UIs when needed.
     *
     * @param c component that will use UI instance
     * @return instance of the WebCanvasUI
     */
    @SuppressWarnings ( "UnusedParameters" )
    public static ComponentUI createUI ( final JComponent c )
    {
        return new WebCanvasUI ();
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

        // Saving canvas reference
        canvas = ( WebCanvas ) c;

        // Applying skin
        StyleManager.installSkin ( canvas );
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
        StyleManager.uninstallSkin ( canvas );

        // Removing canvas reference
        canvas = null;

        // Uninstalling UI
        super.uninstallUI ( c );
    }

    @Override
    public StyleId getStyleId ()
    {
        return StyleManager.getStyleId ( canvas );
    }

    @Override
    public StyleId setStyleId ( final StyleId id )
    {
        return StyleManager.setStyleId ( canvas, id );
    }

    @Override
    public Shape provideShape ()
    {
        return PainterSupport.getShape ( canvas, painter );
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
     * Returns canvas painter.
     *
     * @return canvas painter
     */
    public Painter getPainter ()
    {
        return PainterSupport.getAdaptedPainter ( painter );
    }

    /**
     * Sets canvas painter.
     * Pass null to remove canvas painter.
     *
     * @param painter new canvas painter
     */
    public void setPainter ( final Painter painter )
    {
        PainterSupport.setPainter ( canvas, new DataRunnable<ICanvasPainter> ()
        {
            @Override
            public void run ( final ICanvasPainter newPainter )
            {
                WebCanvasUI.this.painter = newPainter;
            }
        }, this.painter, painter, ICanvasPainter.class, AdaptiveCanvasPainter.class );
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
        return PainterSupport.getPreferredSize ( c, painter );
    }
}