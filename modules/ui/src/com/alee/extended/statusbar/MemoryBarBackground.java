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

package com.alee.extended.statusbar;

import com.alee.painter.decoration.IDecoration;
import com.alee.painter.decoration.background.AbstractBackground;
import com.alee.utils.GraphicsUtils;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

import java.awt.*;
import java.awt.geom.RoundRectangle2D;

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
     * Progress shape round.
     */
    @XStreamAsAttribute
    protected Integer round;

    /**
     * Used border color.
     */
    @XStreamAsAttribute
    protected Color usedBorderColor;

    /**
     * Used fill color.
     */
    @XStreamAsAttribute
    protected Color usedFillColor;

    /**
     * Allocated mark color.
     */
    @XStreamAsAttribute
    protected Color allocatedMarkColor;

    @Override
    public String getId ()
    {
        return id != null ? id : "memory-background";
    }

    @Override
    public void paint ( final Graphics2D g2d, final Rectangle bounds, final C c, final D d, final Shape shape )
    {
        final float opacity = getOpacity ();
        if ( opacity > 0 )
        {
            final Composite oc = GraphicsUtils.setupAlphaComposite ( g2d, opacity, opacity < 1f );

            final Rectangle b = shape.getBounds ();
            final Rectangle ib = new Rectangle ( b.x + 2, b.y + 2, b.width - 4, b.height - 4 );
            final long max = c.isShowMaximumMemory () ? c.getMaxMemory () : c.getAllocatedMemory ();

            // Allocated memory line
            if ( c.isShowMaximumMemory () )
            {
                final int allocatedWidth = getProgressWidth ( ib, c.getAllocatedMemory (), max, false );
                g2d.setPaint ( allocatedMarkColor );
                g2d.drawLine ( ib.x + allocatedWidth, ib.y, ib.x + allocatedWidth, ib.y + ib.height - 1 );
            }

            // Used memory background
            g2d.setPaint ( usedFillColor );
            g2d.fill ( getProgressShape ( ib, c.getUsedMemory (), max, true ) );

            // Used memory border
            g2d.setPaint ( usedBorderColor );
            g2d.draw ( getProgressShape ( ib, c.getUsedMemory (), max, false ) );

            GraphicsUtils.restoreComposite ( g2d, oc, opacity < 1f );
        }
    }

    /**
     * Returns progress shape.
     *
     * @param bounds   content bounds
     * @param progress progress amount
     * @param max      max progress amount
     * @param fill     whether or not fill shape is required
     * @return progress shape
     */
    protected Shape getProgressShape ( final Rectangle bounds, final long progress, final long max, final boolean fill )
    {
        return new RoundRectangle2D.Double ( bounds.x, bounds.y, getProgressWidth ( bounds, progress, max, fill ),
                bounds.height - ( fill ? 0 : 1 ), round, round );
    }

    /**
     * Returns progress width.
     *
     * @param bounds   content bounds
     * @param progress progress amount
     * @param max      max progress amount
     * @param fill     whether or not fill shape is required
     * @return progress width
     */
    protected int getProgressWidth ( final Rectangle bounds, final long progress, final long max, final boolean fill )
    {
        return Math.round ( ( float ) ( bounds.width - ( fill ? 0 : 1 ) ) * progress / max );
    }
}