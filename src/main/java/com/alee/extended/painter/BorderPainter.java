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
import java.awt.geom.Rectangle2D;
import java.awt.geom.RectangularShape;
import java.awt.geom.RoundRectangle2D;

/**
 * Simple border painter.
 * This painter might be used instead of LineBorder in any component that supports painters.
 *
 * @param <E> component type
 * @author Mikle Garin
 * @see DefaultPainter
 * @see Painter
 * @since 1.4
 */

public class BorderPainter<E extends JComponent> extends DefaultPainter<E>
{
    /**
     * Border width.
     */
    protected int width = BorderPainterStyle.width;

    /**
     * Border round.
     */
    protected int round = BorderPainterStyle.round;

    /**
     * Border color.
     */
    protected Color color = BorderPainterStyle.color;

    /**
     * Border stroke.
     */
    protected Stroke stroke = null;

    /**
     * Constructs default border painter.
     */
    public BorderPainter ()
    {
        super ();
        updateStroke ();
    }

    /**
     * Constructs border painter with a specified width.
     *
     * @param width border width
     */
    public BorderPainter ( int width )
    {
        super ();
        setWidth ( width );
    }

    /**
     * Constructs border painter with a specified color.
     *
     * @param color border color
     */
    public BorderPainter ( Color color )
    {
        super ();
        setColor ( color );
        updateStroke ();
    }

    /**
     * Constructs border painter with a specified width and color.
     *
     * @param width border width
     * @param color border color
     */
    public BorderPainter ( int width, Color color )
    {
        super ();
        setWidth ( width );
        setColor ( color );
    }

    /**
     * Returns border width.
     *
     * @return border width
     */
    public int getWidth ()
    {
        return width;
    }

    /**
     * Sets border width.
     * This will also force stroke to update and overwrite old stroke value.
     *
     * @param width new border width
     */
    public void setWidth ( int width )
    {
        this.width = width;
        updateStroke ();
    }

    /**
     * Returns border round.
     *
     * @return border round
     */
    public int getRound ()
    {
        return round;
    }

    /**
     * Sets border round.
     * This will also force stroke to update and overwrite old stroke value.
     *
     * @param round new border round
     */
    public void setRound ( int round )
    {
        this.round = round;
        updateStroke ();
    }

    /**
     * Returns border color.
     *
     * @return border color
     */
    public Color getColor ()
    {
        return color;
    }

    /**
     * Sets border color.
     *
     * @param color new border color
     */
    public void setColor ( Color color )
    {
        this.color = color;
    }

    /**
     * Returns border stroke.
     *
     * @return border stroke
     */
    public Stroke getStroke ()
    {
        return stroke;
    }

    /**
     * Sets border stroke.
     * Be aware that this value might be overwritten when you modify other painter settings.
     *
     * @param stroke new border stroke
     * @see #setWidth(int)
     * @see #setRound(int)
     */
    public void setStroke ( Stroke stroke )
    {
        this.stroke = stroke;
    }

    /**
     * Updates border stroke depending on painter settings.
     */
    protected void updateStroke ()
    {
        stroke = new BasicStroke ( getWidth (), BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND );
    }

    /**
     * Returns preferred size required for proper painting of visual data provided by this painter.
     * This should not take into account any sizes not related to this painter settings (for example text size on button).
     *
     * @param c component to process
     * @return preferred size required for proper painting of visual data provided by this painter
     */
    public Dimension getPreferredSize ( E c )
    {
        return new Dimension ( Math.max ( width * 2, round * 2 ), Math.max ( width * 2, round * 2 ) );
    }

    /**
     * Returns margin required for visual data provided by this painter.
     * This margin is usually added to component's margin when the final component border is calculated.
     *
     * @param c component to process
     * @return margin required for visual data provided by this painter
     */
    public Insets getMargin ( E c )
    {
        Insets m = super.getMargin ( c );
        return new Insets ( m.top + width, m.left + width, m.bottom + width, m.right + width );
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
    public void paint ( Graphics2D g2d, Rectangle bounds, E c )
    {
        Object aa = LafUtils.setupAntialias ( g2d );
        Stroke os = LafUtils.setupStroke ( g2d, stroke, stroke != null );

        g2d.setPaint ( color );
        g2d.draw ( getBorderShape ( bounds ) );

        LafUtils.restoreStroke ( g2d, os, stroke != null );
        LafUtils.restoreAntialias ( g2d, aa );
    }

    /**
     * Returns border shape for the specified limiting bounds.
     *
     * @param bounds limiting bounds
     * @return border shape for the specified limiting bounds
     */
    protected RectangularShape getBorderShape ( Rectangle bounds )
    {
        double shear = width == 1 ? 0 : ( double ) width / 2;
        if ( round > 0 )
        {
            return new RoundRectangle2D.Double ( bounds.x + shear, bounds.y + shear, bounds.width - shear * 2 - 1,
                    bounds.height - shear * 2 - 1, round * 2, round * 2 );
        }
        else
        {
            return new Rectangle2D.Double ( bounds.x + shear, bounds.y + shear, bounds.width - shear * 2 - 1,
                    bounds.height - shear * 2 - 1 );
        }
    }
}