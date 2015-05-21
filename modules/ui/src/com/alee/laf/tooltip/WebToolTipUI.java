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

package com.alee.laf.tooltip;

import com.alee.extended.painter.Painter;
import com.alee.extended.painter.PainterSupport;
import com.alee.managers.style.StyleManager;
import com.alee.utils.GraphicsUtils;
import com.alee.utils.SwingUtils;
import com.alee.utils.laf.ShapeProvider;
import com.alee.utils.laf.Styleable;
import com.alee.utils.swing.DataRunnable;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicToolTipUI;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.util.Map;

/**
 * Custom UI for JTooltip component.
 *
 * @author Mikle Garin
 */

public class WebToolTipUI extends BasicToolTipUI implements Styleable, ShapeProvider
{
    /**
     * Component painter.
     */
    protected ToolTipPainter painter;

    /**
     * Runtime variables.
     */
    protected String styleId = null;
    protected JComponent tooltip = null;

    /**
     * Returns an instance of the WebToolTipUI for the specified component.
     * This tricky method is used by UIManager to create component UIs when needed.
     *
     * @param c component that will use UI instance
     * @return instance of the WebToolTipUI
     */
    @SuppressWarnings ( "UnusedParameters" )
    public static ComponentUI createUI ( final JComponent c )
    {
        return new WebToolTipUI ();
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

        // Saving tooltip to local variable
        tooltip = c;

        // Applying skin
        StyleManager.applySkin ( tooltip );
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
        StyleManager.removeSkin ( tooltip );

        // Cleaning up reference
        this.tooltip = null;

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
        StyleManager.applySkin ( tooltip );
    }

    /**
     * Returns component shape.
     *
     * @return component shape
     */
    @Override
    public Shape provideShape ()
    {
        return new RoundRectangle2D.Double ( 0, 0, tooltip.getWidth (), tooltip.getHeight (), WebTooltipStyle.round * 2,
                WebTooltipStyle.round * 2 );
    }

    /**
     * Returns tooltip painter.
     *
     * @return tooltip painter
     */
    public Painter getPainter ()
    {
        return PainterSupport.getAdaptedPainter ( painter );
    }

    /**
     * Sets tooltip painter.
     * Pass null to remove tooltip painter.
     *
     * @param painter new tooltip painter
     */
    public void setPainter ( final Painter painter )
    {
        PainterSupport.setPainter ( tooltip, new DataRunnable<ToolTipPainter> ()
        {
            @Override
            public void run ( final ToolTipPainter newPainter )
            {
                WebToolTipUI.this.painter = newPainter;
            }
        }, this.painter, painter, ToolTipPainter.class, AdaptiveToolTipPainter.class );
    }

    /**
     * Paints tooltip.
     *
     * @param g graphics
     * @param c component
     */
    @Override
    public void paint ( final Graphics g, final JComponent c )
    {
        final Graphics2D g2d = ( Graphics2D ) g;

        final Object aa = GraphicsUtils.setupAntialias ( g2d );
        final Composite oc = GraphicsUtils.setupAlphaComposite ( g2d, WebTooltipStyle.trasparency );

        g2d.setPaint ( c.getBackground () );
        g2d.fillRoundRect ( 0, 0, c.getWidth (), c.getHeight (), WebTooltipStyle.round * 2, WebTooltipStyle.round * 2 );

        GraphicsUtils.restoreComposite ( g2d, oc );
        GraphicsUtils.restoreAntialias ( g2d, aa );

        final Map taa = SwingUtils.setupTextAntialias ( g2d );
        // todo paint text from decorated painter
        super.paint ( g, c );
        SwingUtils.restoreTextAntialias ( g2d, taa );
    }
}