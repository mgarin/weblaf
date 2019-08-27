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

import com.alee.api.annotations.NotNull;
import com.alee.managers.style.Bounds;
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
 * This painter might be used instead of {@link javax.swing.border.LineBorder} in any component that supports painters.
 *
 * @param <C> component type
 * @param <U> component UI type
 * @author Mikle Garin
 * @see AbstractPainter
 * @see com.alee.painter.Painter
 */
public class BorderPainter<C extends JComponent, U extends ComponentUI> extends AbstractPainter<C, U>
{
    /**
     * Border round.
     */
    protected Integer round;

    /**
     * Border stroke.
     */
    protected Stroke stroke;

    /**
     * Border color.
     */
    protected Color color;

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
        return round != null ? round : 0;
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
     * Returns stroke width.
     *
     * @return stroke width
     */
    protected int getStrokeWidth ()
    {
        final Stroke stroke = getStroke ();
        return stroke != null && stroke instanceof BasicStroke ? Math.round ( ( ( BasicStroke ) stroke ).getLineWidth () ) : 1;
    }

    /**
     * Sets border stroke.
     *
     * @param stroke new border stroke
     */
    public void setStroke ( final Stroke stroke )
    {
        this.stroke = stroke;
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
    }

    @NotNull
    @Override
    protected Insets getBorder ()
    {
        final int width = getStrokeWidth ();
        return new Insets ( width, width, width, width );
    }

    @Override
    public void paint ( @NotNull final Graphics2D g2d, @NotNull final C c, @NotNull final U ui, @NotNull final Bounds bounds )
    {
        final Object aa = GraphicsUtils.setupAntialias ( g2d );
        final Stroke os = GraphicsUtils.setupStroke ( g2d, stroke, stroke != null );

        g2d.setPaint ( color );
        g2d.draw ( getBorderShape ( bounds.get () ) );

        GraphicsUtils.restoreStroke ( g2d, os, stroke != null );
        GraphicsUtils.restoreAntialias ( g2d, aa );
    }

    /**
     * Returns border shape for the specified limiting bounds.
     *
     * @param bounds limiting bounds
     * @return border shape for the specified limiting bounds
     */
    protected RectangularShape getBorderShape ( final Rectangle bounds )
    {
        final RectangularShape shape;
        final int round = getRound ();
        final int width = getStrokeWidth ();
        final double shear = width == 1 ? 0 : ( double ) width / 2;
        if ( round > 0 )
        {
            shape = new RoundRectangle2D.Double (
                    bounds.x + shear,
                    bounds.y + shear,
                    bounds.width - shear * 2 - 1,
                    bounds.height - shear * 2 - 1, round * 2, round * 2
            );
        }
        else
        {
            shape = new Rectangle2D.Double (
                    bounds.x + shear,
                    bounds.y + shear,
                    bounds.width - shear * 2 - 1,
                    bounds.height - shear * 2 - 1
            );
        }
        return shape;
    }

    @NotNull
    @Override
    public Dimension getPreferredSize ()
    {
        final int width = getStrokeWidth ();
        return new Dimension ( Math.max ( width * 2, round * 2 ), Math.max ( width * 2, round * 2 ) );
    }
}