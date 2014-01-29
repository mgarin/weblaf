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

package com.alee.extended.painter;

import com.alee.utils.LafUtils;

import javax.swing.*;
import java.awt.*;

/**
 * Alpha layer painter.
 * This painter fills component background with an alpha-like texture.
 *
 * @param <E> component type
 * @author Mikle Garin
 * @see AbstractPainter
 * @see Painter
 */

public class AlphaLayerPainter<E extends JComponent> extends AbstractPainter<E>
{
    /**
     * Square size.
     */
    protected int squareSize = AlphaLayerPainterStyle.squareSize;

    /**
     * Light square color.
     */
    protected Color lightSquareColor = AlphaLayerPainterStyle.lightSquareColor;

    /**
     * Dark square color.
     */
    protected Color darkSquareColor = AlphaLayerPainterStyle.darkSquareColor;

    /**
     * Constructs default alpha layer painter.
     */
    public AlphaLayerPainter ()
    {
        super ();
    }

    /**
     * Constructs alpha layer painter with a specified square size.
     */
    public AlphaLayerPainter ( final int squareSize )
    {
        super ();
        this.squareSize = squareSize;
    }

    /**
     * Constructs alpha layer painter with a specified square colors.
     */
    public AlphaLayerPainter ( final Color lightSquareColor, final Color darkSquareColor )
    {
        super ();
        this.lightSquareColor = lightSquareColor;
        this.darkSquareColor = darkSquareColor;
    }

    /**
     * Constructs alpha layer painter with a specified square size and colors.
     */
    public AlphaLayerPainter ( final int squareSize, final Color lightSquareColor, final Color darkSquareColor )
    {
        super ();
        this.squareSize = squareSize;
        this.lightSquareColor = lightSquareColor;
        this.darkSquareColor = darkSquareColor;
    }

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

    /**
     * Returns whether visual data provided by this painter is opaque or not.
     * Returned value might affect component opacity depending on painter support inside that component UI.
     *
     * @param c component to process
     * @return true if visual data provided by this painter is opaque, false otherwise
     */
    @Override
    public boolean isOpaque ( final E c )
    {
        return lightSquareColor.getAlpha () == 255 && darkSquareColor.getAlpha () == 255;
    }

    /**
     * Paints visual data onto the component graphics.
     * Provided graphics and component are taken directly from component UI paint method.
     * Provided bounds are usually fake (zero location, component size) but in some cases it might be specified by componentUI.
     *
     * @param g2d    component graphics
     * @param bounds bounds for painter visual data
     * @param c      component to process
     */
    @Override
    public void paint ( final Graphics2D g2d, final Rectangle bounds, final E c )
    {
        // todo Optimize paint by using generated texture image
        LafUtils.drawAlphaLayer ( g2d, bounds.x, bounds.y, bounds.width, bounds.height, squareSize, lightSquareColor, darkSquareColor );
    }
}