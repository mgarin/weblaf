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

import com.alee.utils.LafUtils;
import com.alee.utils.SwingUtils;
import com.alee.utils.laf.ShapeProvider;

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
 * @since 1.4
 */

public class WebToolTipUI extends BasicToolTipUI implements ShapeProvider
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
    public static ComponentUI createUI ( JComponent c )
    {
        return new WebToolTipUI ();
    }

    /**
     * Installs UI in the specified component.
     *
     * @param c component for this UI
     */
    public void installUI ( JComponent c )
    {
        super.installUI ( c );

        this.tooltip = c;

        // Default settings
        SwingUtils.setOrientation ( tooltip );
        tooltip.setOpaque ( false );
        tooltip.setBackground ( WebTooltipStyle.backgroundColor );
        tooltip.setForeground ( WebTooltipStyle.textColor );

        // Updating border
        updateBorder ( tooltip );
    }

    /**
     * Uninstalls UI from the specified component.
     *
     * @param c component with this UI
     */
    public void uninstallUI ( JComponent c )
    {
        this.tooltip = null;

        super.uninstallUI ( c );
    }

    /**
     * Returns component shape.
     *
     * @return component shape
     */
    public Shape provideShape ()
    {
        return new RoundRectangle2D.Double ( 0, 0, tooltip.getWidth (), tooltip.getHeight (), WebTooltipStyle.round * 2,
                WebTooltipStyle.round * 2 );
    }

    /**
     * Updates component border
     *
     * @param c component to process
     */
    private void updateBorder ( JComponent c )
    {
        c.setBorder ( LafUtils.createWebBorder ( WebTooltipStyle.contentMargin ) );
    }

    /**
     * Paints tooltip.
     *
     * @param g graphics
     * @param c component
     */
    public void paint ( Graphics g, JComponent c )
    {
        Graphics2D g2d = ( Graphics2D ) g;

        Object aa = LafUtils.setupAntialias ( g2d );
        Composite oc = LafUtils.setupAlphaComposite ( g2d, WebTooltipStyle.trasparency );

        g2d.setPaint ( c.getBackground () );
        g2d.fillRoundRect ( 0, 0, c.getWidth (), c.getHeight (), WebTooltipStyle.round * 2, WebTooltipStyle.round * 2 );

        LafUtils.restoreComposite ( g2d, oc );
        LafUtils.restoreAntialias ( g2d, aa );

        Map taa = SwingUtils.setupTextAntialias ( g2d, c );
        super.paint ( g, c );
        SwingUtils.restoreTextAntialias ( g2d, taa );
    }
}