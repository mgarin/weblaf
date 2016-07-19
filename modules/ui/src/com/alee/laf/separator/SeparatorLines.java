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

package com.alee.laf.separator;

import com.alee.utils.GraphicsUtils;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import java.awt.*;
import java.io.Serializable;
import java.util.List;

/**
 * Multiply separator lines data.
 *
 * @author Mikle Garin
 * @see com.alee.laf.separator.AbstractSeparatorPainter
 */

@XStreamAlias ("SeparatorLines")
public class SeparatorLines implements Serializable
{
    /**
     * Separator lines data.
     * Must always be provided to properly render separator.
     */
    @XStreamImplicit ( itemFieldName = "line" )
    protected List<SeparatorLine> lines;

    /**
     * Returns separator lines count.
     *
     * @return separator lines count
     */
    protected int getLinesCount ()
    {
        return lines != null ? lines.size () : 0;
    }

    /**
     * Returns whether or not this descriptor represents empty content.
     *
     * @return true if this descriptor represents empty content, false otherwise
     */
    public boolean isEmpty ()
    {
        return getLinesCount () == 0;
    }

    /**
     * Paints existing lines onto the provided graphics context.
     *
     * @param g2d         graphics context
     * @param bounds      painting bounds
     * @param orientation separator orientation
     * @param ltr         component orientation
     */
    public void paint ( final Graphics2D g2d, final Rectangle bounds, final int orientation, final boolean ltr )
    {
        // General settings
        final boolean vertical = orientation == WebSeparator.VERTICAL;

        // Painting each separator line
        for ( int i = 0; i < lines.size (); i++ )
        {
            // Current line
            final SeparatorLine line = lines.get ( i );

            // Calculating line coordinates
            final int x1;
            final int y1;
            final int x2;
            final int y2;
            if ( vertical )
            {
                x1 = x2 = ltr ? bounds.x + i : bounds.x + bounds.width - i - 1;
                y1 = bounds.y;
                y2 = bounds.y + bounds.height - 1;
            }
            else
            {
                x1 = bounds.x;
                x2 = bounds.x + bounds.width - 1;
                y1 = y2 = bounds.y + i;
            }

            // Painting line
            final Stroke stroke = GraphicsUtils.setupStroke ( g2d, line.getStroke (), line.getStroke () != null );
            final Paint op = GraphicsUtils.setupPaint ( g2d, line.getPaint ( x1, y1, x2, y2 ) );
            g2d.drawLine ( x1, y1, x2, y2 );
            GraphicsUtils.restorePaint ( g2d, op );
            GraphicsUtils.restoreStroke ( g2d, stroke, line.getStroke () != null );
        }
    }

    /**
     * Returns preferred size for the described content.
     *
     * @param orientation separator orientation
     * @return preferred size for the described content
     */
    public Dimension getPreferredSize ( final int orientation )
    {
        final int lines = getLinesCount ();
        final boolean ver = orientation == WebSeparator.VERTICAL;
        return new Dimension ( ver ? lines : 0, ver ? 0 : lines );
    }
}