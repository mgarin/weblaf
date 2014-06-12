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

import com.alee.laf.WebLookAndFeel;
import com.alee.utils.GraphicsUtils;
import com.alee.utils.LafUtils;
import com.alee.utils.SwingUtils;
import com.alee.utils.laf.ShapeProvider;
import com.alee.utils.swing.BorderMethods;

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

public class WebToolTipUI extends BasicToolTipUI implements ShapeProvider, BorderMethods
{
    /**
     * Tooltip instance.
     */
    private JComponent tooltip = null;

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

        this.tooltip = c;

        // Default settings
        SwingUtils.setOrientation ( tooltip );
        LookAndFeel.installProperty ( tooltip, WebLookAndFeel.OPAQUE_PROPERTY, Boolean.FALSE );
        tooltip.setBackground ( WebTooltipStyle.backgroundColor );
        tooltip.setForeground ( WebTooltipStyle.textColor );

        // Updating border
        updateBorder ();
    }

    /**
     * Uninstalls UI from the specified component.
     *
     * @param c component with this UI
     */
    @Override
    public void uninstallUI ( final JComponent c )
    {
        this.tooltip = null;

        super.uninstallUI ( c );
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
     * {@inheritDoc}
     */
    @Override
    public void updateBorder ()
    {
        // Preserve old borders
        if ( SwingUtils.isPreserveBorders ( tooltip ) )
        {
            return;
        }

        tooltip.setBorder ( LafUtils.createWebBorder ( WebTooltipStyle.contentMargin ) );
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
        super.paint ( g, c );
        SwingUtils.restoreTextAntialias ( g2d, taa );
    }
}