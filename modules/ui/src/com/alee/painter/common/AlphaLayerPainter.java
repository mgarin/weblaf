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

package com.alee.painter.common;

import com.alee.painter.AbstractPainter;
import com.alee.utils.LafUtils;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import java.awt.*;

/**
 * Alpha layer painter.
 * This painter fills component background with an alpha-like texture.
 *
 * @param <E> component type
 * @author Mikle Garin
 * @see com.alee.painter.AbstractPainter
 * @see com.alee.painter.Painter
 */

public class AlphaLayerPainter<E extends JComponent, U extends ComponentUI> extends AbstractPainter<E, U>
{
    /**
     * Square size.
     */
    protected int squareSize;

    /**
     * Light square color.
     */
    protected Color lightSquareColor;

    /**
     * Dark square color.
     */
    protected Color darkSquareColor;

    /**
     * Returns square size.
     *
     * @return square size
     */
    public int getSquareSize ()
    {
        return squareSize;
    }

    /**
     * Sets square size.
     *
     * @param squareSize new square size
     */
    public void setSquareSize ( final int squareSize )
    {
        this.squareSize = squareSize;
        repaint ();
    }

    /**
     * Returns light square color.
     *
     * @return light square color
     */
    public Color getLightSquareColor ()
    {
        return lightSquareColor;
    }

    /**
     * Sets light square color.
     *
     * @param lightSquareColor new light square color
     */
    public void setLightSquareColor ( final Color lightSquareColor )
    {
        this.lightSquareColor = lightSquareColor;
        repaint ();
    }

    /**
     * Returns dark square color.
     *
     * @return dark square color
     */
    public Color getDarkSquareColor ()
    {
        return darkSquareColor;
    }

    /**
     * Sets dark square color.
     *
     * @param darkSquareColor new dark square color
     */
    public void setDarkSquareColor ( final Color darkSquareColor )
    {
        this.darkSquareColor = darkSquareColor;
        repaint ();
    }

    @Override
    public Boolean isOpaque ()
    {
        return lightSquareColor.getAlpha () == 255 && darkSquareColor.getAlpha () == 255;
    }

    @Override
    public void paint ( final Graphics2D g2d, final Rectangle bounds, final E c, final U ui )
    {
        // todo Optimize paint by using generated texture image
        LafUtils.drawAlphaLayer ( g2d, bounds.x, bounds.y, bounds.width, bounds.height, squareSize, lightSquareColor, darkSquareColor );
    }
}