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
import com.alee.utils.GraphicsUtils;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
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
 * @see com.alee.painter.AbstractPainter
 * @see com.alee.painter.Painter
 */

public class BorderPainter<E extends JComponent, U extends ComponentUI> extends AbstractPainter<E, U>
{
    /**
     * Border round.
     */
    protected int round;

    /**
     * Border color.
     */
    protected Color color;

    /**
     * Border stroke.
     */
    protected Stroke stroke;

    /**
     * Constructs default border painter.
     */
    public BorderPainter ()
    {
        super ();
    }

    /**
     * Constructs border painter with a specified color.
     *
     * @param color border color
     */
    public BorderPainter ( final Color color )
    {
        super ();
        this.color = color;
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
    public void setRound ( final int round )
    {
        this.round = round;
        repaint ();
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
    public void setColor ( final Color color )
    {
        this.color = color;
        repaint ();
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
     *
     * @param stroke new border stroke
     */
    public void setStroke ( final Stroke stroke )
    {
        this.stroke = stroke;
        repaint ();
    }

    /**
     * Returns stroke width.
     *
     * @return stroke width
     */
    public int getStrokeWidth ()
    {
        final Stroke stroke = getStroke ();
        return stroke != null && stroke instanceof BasicStroke ? Math.round ( ( ( BasicStroke ) stroke ).getLineWidth () ) : 0;
    }

    /**
     * Returns margin required for visual data provided by this painter.
     * This margin is usually added to component's margin when the final component border is calculated.
     *
     * @return margin required for visual data provided by this painter
     */
    @Override
    public Insets getBorders ()
    {
        final int width = getStrokeWidth ();
        return i ( width, width, width, width );
    }

    /**
     * Paints visual data onto the component graphics.
     * Provided graphics and component are taken directly from component UI paint method.
     * Provided bounds are usually fake (zero location, component size) but in some cases it might be specified by componentUI.
     *
     * @param g2d    component graphics
     * @param bounds bounds for painter visual data
     * @param c      component to process
     * @param ui     component UI
     */
    @Override
    public void paint ( final Graphics2D g2d, final Rectangle bounds, final E c, final U ui )
    {
        if ( stroke != null && color != null )
        {
            final Object aa = GraphicsUtils.setupAntialias ( g2d );
            final Stroke os = GraphicsUtils.setupStroke ( g2d, stroke, stroke != null );

            g2d.setPaint ( color );
            g2d.draw ( getBorderShape ( bounds ) );

            GraphicsUtils.restoreStroke ( g2d, os, stroke != null );
            GraphicsUtils.restoreAntialias ( g2d, aa );
        }
    }

    /**
     * Returns border shape for the specified limiting bounds.
     *
     * @param bounds limiting bounds
     * @return border shape for the specified limiting bounds
     */
    protected RectangularShape getBorderShape ( final Rectangle bounds )
    {
        final int width = getStrokeWidth ();
        final double shear = width == 1 ? 0 : ( double ) width / 2;
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

    /**
     * Returns preferred size required for proper painting of visual data provided by this painter.
     * This should not take into account any sizes not related to this painter settings (for example text size on button).
     *
     * @return preferred size required for proper painting of visual data provided by this painter
     */
    @Override
    public Dimension getPreferredSize ()
    {
        final int width = getStrokeWidth ();
        return new Dimension ( Math.max ( width * 2, round * 2 ), Math.max ( width * 2, round * 2 ) );
    }
}