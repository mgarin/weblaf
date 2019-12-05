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

package com.alee.extended.memorybar;

import com.alee.api.annotations.NotNull;
import com.alee.api.annotations.Nullable;
import com.alee.graphics.data.Line;
import com.alee.painter.decoration.DecorationException;
import com.alee.painter.decoration.IDecoration;
import com.alee.painter.decoration.background.AbstractBackground;
import com.alee.utils.GraphicsUtils;
import com.alee.utils.MathUtils;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.lang.management.MemoryUsage;

/**
 * Custom background implementation for {@link WebMemoryBar}.
 *
 * @param <C> component type
 * @param <D> decoration type
 * @param <I> background type
 * @author Mikle Garin
 * @see WebMemoryBar
 */
@XStreamAlias ( "MemoryBarBackground" )
public class MemoryBarBackground<C extends WebMemoryBar, D extends IDecoration<C, D>, I extends MemoryBarBackground<C, D, I>>
        extends AbstractBackground<C, D, I>
{
    /**
     * todo 1. Separate into shape/border/background implementations with future updates
     */

    /**
     * Progress shape round.
     */
    @Nullable
    @XStreamAsAttribute
    protected Integer round;

    /**
     * Used border {@link Color}.
     */
    @Nullable
    @XStreamAsAttribute
    protected Color usedBorderColor;

    /**
     * Used fill {@link Color}.
     */
    @Nullable
    @XStreamAsAttribute
    protected Color usedFillColor;

    /**
     * Allocated mark {@link Color}.
     */
    @Nullable
    @XStreamAsAttribute
    protected Color allocatedMarkColor;

    @NotNull
    @Override
    public String getId ()
    {
        return id != null ? id : "memory-background";
    }

    /**
     * Returns progress shape round.
     *
     * @param c {@link WebMemoryBar} that is being painted
     * @param d {@link IDecoration} state
     * @return progress shape round
     */
    protected int getRound ( @NotNull final C c, @NotNull final D d )
    {
        if ( round == null )
        {
            throw new DecorationException ( "Round must be specified" );
        }
        return round;
    }

    /**
     * Returns used border {@link Color}.
     *
     * @param c {@link WebMemoryBar} that is being painted
     * @param d {@link IDecoration} state
     * @return used border {@link Color}
     */
    @NotNull
    protected Color getUsedBorderColor ( @NotNull final C c, @NotNull final D d )
    {
        if ( usedBorderColor == null )
        {
            throw new DecorationException ( "Used border color must be specified" );
        }
        return usedBorderColor;
    }

    /**
     * Returns used fill {@link Color}.
     *
     * @param c {@link WebMemoryBar} that is being painted
     * @param d {@link IDecoration} state
     * @return used fill {@link Color}
     */
    @NotNull
    protected Color getUsedFillColor ( @NotNull final C c, @NotNull final D d )
    {
        if ( usedFillColor == null )
        {
            throw new DecorationException ( "Used fill color must be specified" );
        }
        return usedFillColor;
    }

    /**
     * Returns allocated mark {@link Color}.
     *
     * @param c {@link WebMemoryBar} that is being painted
     * @param d {@link IDecoration} state
     * @return allocated mark {@link Color}
     */
    @NotNull
    protected Color getAllocatedMarkColor ( @NotNull final C c, @NotNull final D d )
    {
        if ( allocatedMarkColor == null )
        {
            throw new DecorationException ( "Allocated mark color must be specified" );
        }
        return allocatedMarkColor;
    }

    @Override
    public void paint ( @NotNull final Graphics2D g2d, @NotNull final Rectangle bounds, @NotNull final C c, @NotNull final D d,
                        @NotNull final Shape shape )
    {
        final float opacity = getOpacity ( c, d );
        if ( opacity > 0 )
        {
            final Composite oc = GraphicsUtils.setupAlphaComposite ( g2d, opacity, opacity < 1f );

            final Rectangle b = shape.getBounds ();
            final Rectangle ib = new Rectangle ( b.x + 2, b.y + 2, b.width - 4, b.height - 4 );

            final MemoryUsage memoryUsage = c.getMemoryUsage ();
            final long max = c.isMaximumMemoryDisplayed () ? memoryUsage.getMax () : memoryUsage.getCommitted ();

            // Allocated memory line
            if ( c.isMaximumMemoryDisplayed () )
            {
                final Line line;
                final boolean ltr = c.getComponentOrientation ().isLeftToRight ();
                final int allocatedWidth = getBarLength ( c, d, ib, memoryUsage.getCommitted (), max, false );
                if ( c.getOrientation ().isHorizontal () )
                {
                    final int x = ltr ? ib.x + allocatedWidth : ib.x + ib.width - allocatedWidth - 1;
                    line = new Line ( x, ib.y, x, ib.y + ib.height - 1 );
                }
                else
                {
                    final int y = ltr ? ib.y + ib.height - allocatedWidth - 1 : ib.y + allocatedWidth;
                    line = new Line ( ib.x, y, ib.x + ib.width - 1, y );
                }
                g2d.setPaint ( getAllocatedMarkColor ( c, d ) );
                g2d.drawLine ( line.x1, line.y1, line.x2, line.y2 );
            }

            // Used memory background
            g2d.setPaint ( getUsedFillColor ( c, d ) );
            g2d.fill ( getBarShape ( c, d, ib, memoryUsage.getUsed (), max, true ) );

            // Used memory border
            g2d.setPaint ( getUsedBorderColor ( c, d ) );
            g2d.draw ( getBarShape ( c, d, ib, memoryUsage.getUsed (), max, false ) );

            GraphicsUtils.restoreComposite ( g2d, oc, opacity < 1f );
        }
    }

    /**
     * Returns bar shape.
     *
     * @param c      {@link WebMemoryBar} that is being painted
     * @param d      {@link IDecoration} state
     * @param bounds content bounds
     * @param value  bar value
     * @param max    max bar value
     * @param fill   whether or not fill shape is required
     * @return bar shape
     */
    @NotNull
    protected Shape getBarShape ( @NotNull final C c, @NotNull final D d, @NotNull final Rectangle bounds,
                                  final long value, final long max, final boolean fill )
    {
        final Shape barShape;
        final boolean ltr = c.getComponentOrientation ().isLeftToRight ();
        final int barLength = getBarLength ( c, d, bounds, value, max, fill );
        final int adjustment = fill ? 0 : 1;
        if ( c.getOrientation ().isHorizontal () )
        {
            barShape = new RoundRectangle2D.Double (
                    ltr ? bounds.x : bounds.x + bounds.width - adjustment - barLength,
                    bounds.y,
                    barLength,
                    bounds.height - adjustment,
                    getRound ( c, d ),
                    getRound ( c, d )
            );
        }
        else
        {
            barShape = new RoundRectangle2D.Double (
                    bounds.x,
                    ltr ? bounds.y + bounds.height - adjustment - barLength : bounds.y,
                    bounds.width - adjustment,
                    barLength,
                    getRound ( c, d ),
                    getRound ( c, d )
            );
        }
        return barShape;
    }

    /**
     * Returns bar length.
     *
     * @param c      {@link WebMemoryBar} that is being painted
     * @param d      {@link IDecoration} state
     * @param bounds content bounds
     * @param value  bar value
     * @param max    max bar value
     * @param fill   whether or not fill shape is required
     * @return bar length
     */
    protected int getBarLength ( @NotNull final C c, @NotNull final D d, @NotNull final Rectangle bounds,
                                 final long value, final long max, final boolean fill )
    {
        final boolean hor = c.getOrientation ().isHorizontal ();
        return MathUtils.roundToInt ( ( double ) ( ( hor ? bounds.width : bounds.height ) - ( fill ? 0 : 1 ) ) * value / max );
    }
}