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

package com.alee.managers.style.skin.web;

import com.alee.utils.GraphicsUtils;

import javax.swing.*;
import java.awt.*;

/**
 * Custom painter for WebSyntaxPanel component.
 *
 * @author Mikle Garin
 * @see com.alee.managers.style.skin.web.WebPanelPainter
 * @see com.alee.extended.painter.AbstractPainter
 * @see com.alee.extended.painter.Painter
 */

public class WebSyntaxPanelPainter<E extends JPanel> extends WebPanelPainter<E>
{
    /**
     * Used colors.
     */
    public static Color boldBorder = new Color ( 204, 204, 204 );
    public static Color boldBackground = new Color ( 247, 247, 247 );
    public static Color thickBorder = new Color ( 221, 221, 221 );
    public static Color thickBackground = new Color ( 248, 248, 248 );

    /**
     * Syntax panel style.
     */
    protected SyntaxPanelStyle style = SyntaxPanelStyle.thick;

    /**
     * Returns syntax panel style.
     *
     * @return syntax panel style
     */
    public SyntaxPanelStyle getStyle ()
    {
        return style;
    }

    /**
     * Sets syntax panel style.
     *
     * @param style new syntax panel style
     */
    public void setStyle ( final SyntaxPanelStyle style )
    {
        this.style = style;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void paint ( final Graphics2D g2d, final Rectangle bounds, final E panel )
    {
        // Paint simple background if undecorated & opaque
        if ( panel.isOpaque () )
        {
            g2d.setPaint ( panel.getBackground () );
            g2d.fillRect ( bounds.x, bounds.y, bounds.width, bounds.height );
        }

        // Paint syntax panel styling
        final Object aa = GraphicsUtils.setupAntialias ( g2d );
        if ( style == SyntaxPanelStyle.bold )
        {
            // White spacer
            g2d.setPaint ( Color.WHITE );
            g2d.fillRoundRect ( bounds.x, bounds.y, bounds.width, bounds.height - 1, 6, 6 );

            // Background
            g2d.setPaint ( boldBackground );
            g2d.fillRoundRect ( bounds.x + 3, bounds.y + 3, bounds.width - 6, bounds.height - 7, 3, 3 );

            // Border
            g2d.setPaint ( boldBorder );
            g2d.drawRoundRect ( bounds.x, bounds.y, bounds.width - 1, bounds.height - 2, 6, 6 );
            g2d.drawRoundRect ( bounds.x, bounds.y, bounds.width - 1, bounds.height - 1, 6, 6 );
        }
        else if ( style == SyntaxPanelStyle.thick )
        {
            // Background
            g2d.setPaint ( thickBackground );
            g2d.fillRoundRect ( bounds.x, bounds.y, bounds.width, bounds.height, 6, 6 );

            // Border
            g2d.setPaint ( thickBorder );
            g2d.drawRoundRect ( bounds.x, bounds.y, bounds.width - 1, bounds.height - 2, 6, 6 );
        }
        GraphicsUtils.restoreAntialias ( g2d, aa );
    }
}