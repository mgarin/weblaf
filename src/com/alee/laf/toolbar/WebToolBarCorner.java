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

package com.alee.laf.toolbar;

import com.alee.extended.painter.AbstractPainter;
import com.alee.extended.painter.Painter;
import com.alee.laf.panel.WebPanel;

import javax.swing.*;
import java.awt.*;

/**
 * This component serves as a visual connector between horizontal and vertical toolbars.
 *
 * @author Mikle Garin
 */

public class WebToolBarCorner extends WebPanel implements SwingConstants
{
    /**
     * Single painter class for all corners.
     */
    private static Painter<WebToolBarCorner> painter = new WebToolBarCornerPainter ();

    /**
     * Corner position which defines which toolbars this corner connects.
     * For example when in NORTH_WEST position it should connect horizontal toolbar at its left side with a vertical toolbar.
     */
    private int position = NORTH_WEST;

    /**
     * Constructs toolbar corner.
     */
    public WebToolBarCorner ()
    {
        super ( painter );
    }

    /**
     * Constructs toolbar corner.
     */
    public WebToolBarCorner ( int position )
    {
        super ( painter );
        setPosition ( position );
    }

    /**
     * Returns corner position which defines which toolbars this corner connects.
     *
     * @return corner position which defines which toolbars this corner connects
     */
    public int getPosition ()
    {
        return position;
    }

    /**
     * Sets corner position which defines which toolbars this corner connects.
     *
     * @param position corner position which defines which toolbars this corner connects
     */
    public void setPosition ( int position )
    {
        this.position = position;
    }

    /**
     * Painter class for toolbar corners.
     */
    private static class WebToolBarCornerPainter extends AbstractPainter<WebToolBarCorner>
    {
        /**
         * Constant fractions.
         */
        private static final float[] fractions = new float[]{ 0f, 0.5f };

        /**
         * Constant colors.
         */
        private static final Color[] colors = new Color[]{ WebToolBarStyle.bottomBgColor, WebToolBarStyle.topBgColor };

        /**
         * {@inheritDoc}
         */
        @Override
        public void paint ( final Graphics2D g2d, final Rectangle b, final WebToolBarCorner c )
        {
            final boolean ltr = c.getComponentOrientation ().isLeftToRight ();
            final boolean top = c.getPosition () == NORTH_WEST || c.getPosition () == NORTH_EAST;
            final boolean left = ltr ? ( c.getPosition () == NORTH_WEST || c.getPosition () == SOUTH_WEST ) :
                    ( c.getPosition () == NORTH_EAST || c.getPosition () == SOUTH_EAST );
            if ( top )
            {
                if ( left )
                {
                    final Rectangle gradientBounds = new Rectangle ( b.x, b.y, b.width * 2, b.height * 2 );
                    g2d.setPaint (
                            new RadialGradientPaint ( gradientBounds, fractions, colors, MultipleGradientPaint.CycleMethod.NO_CYCLE ) );
                    g2d.fill ( b );

                    g2d.setPaint ( c.isEnabled () ? WebToolBarStyle.borderColor : WebToolBarStyle.disabledBorderColor );
                    g2d.drawLine ( b.x + b.width - 1, b.y + b.height - 1, b.x + b.width - 1, b.y + b.height - 1 );
                }
                else
                {
                    final Rectangle gradientBounds = new Rectangle ( b.x - b.width, b.y, b.width * 2, b.height * 2 );
                    g2d.setPaint (
                            new RadialGradientPaint ( gradientBounds, fractions, colors, MultipleGradientPaint.CycleMethod.NO_CYCLE ) );
                    g2d.fill ( b );

                    g2d.setPaint ( c.isEnabled () ? WebToolBarStyle.borderColor : WebToolBarStyle.disabledBorderColor );
                    g2d.drawLine ( b.x, b.y + b.height - 1, b.x, b.y + b.height - 1 );
                }
            }
            else
            {
                if ( left )
                {
                    final Rectangle gradientBounds = new Rectangle ( b.x, b.y - b.height, b.width * 2, b.height * 2 );
                    g2d.setPaint (
                            new RadialGradientPaint ( gradientBounds, fractions, colors, MultipleGradientPaint.CycleMethod.NO_CYCLE ) );
                    g2d.fill ( b );

                    g2d.setPaint ( c.isEnabled () ? WebToolBarStyle.borderColor : WebToolBarStyle.disabledBorderColor );
                    g2d.drawLine ( b.x + b.width - 1, b.y, b.x + b.width - 1, b.y );
                }
                else
                {
                    final Rectangle gradientBounds = new Rectangle ( b.x - b.width, b.y - b.height, b.width * 2, b.height * 2 );
                    g2d.setPaint (
                            new RadialGradientPaint ( gradientBounds, fractions, colors, MultipleGradientPaint.CycleMethod.NO_CYCLE ) );
                    g2d.fill ( b );

                    g2d.setPaint ( c.isEnabled () ? WebToolBarStyle.borderColor : WebToolBarStyle.disabledBorderColor );
                    g2d.drawLine ( b.x, b.y, b.x, b.y );
                }
            }
        }
    }
}