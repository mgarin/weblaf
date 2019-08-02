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

package com.alee.extended.layout;

import java.awt.*;

/**
 * Replacement for {@link FlowLayout} that doesn't add {@link #hgap} and {@link #vgap} as additional padding around container.
 *
 * @author Mikle Garin
 */
public class CompactFlowLayout extends FlowLayout
{
    /**
     * Constructs new {@link CompactFlowLayout}.
     */
    public CompactFlowLayout ()
    {
        super ();
    }

    /**
     * Constructs new {@link CompactFlowLayout}.
     *
     * @param align content alignment
     */
    public CompactFlowLayout ( final int align )
    {
        super ( align );
    }

    /**
     * Constructs new {@link CompactFlowLayout}.
     *
     * @param align content alignment
     * @param hgap  horizontal gap
     * @param vgap  vertical gap
     */
    public CompactFlowLayout ( final int align, final int hgap, final int vgap )
    {
        super ( align, hgap, vgap );
    }

    @Override
    public Dimension preferredLayoutSize ( final Container container )
    {
        synchronized ( container.getTreeLock () )
        {
            final Dimension ps = super.preferredLayoutSize ( container );
            ps.width -= getHgap () * 2;
            ps.height -= getVgap () * 2;
            return ps;
        }
    }

    @Override
    public Dimension minimumLayoutSize ( final Container container )
    {
        synchronized ( container.getTreeLock () )
        {
            final Dimension ms = super.minimumLayoutSize ( container );
            ms.width -= getHgap () * 2;
            ms.height -= getVgap () * 2;
            return ms;
        }
    }

    @Override
    public void layoutContainer ( final Container container )
    {
        synchronized ( container.getTreeLock () )
        {
            final Insets insets = container.getInsets ();
            final int maxwidth = container.getWidth () - ( insets.left + insets.right );
            final int nmembers = container.getComponentCount ();
            int x = 0;
            int y = insets.top;
            int rowh = 0, start = 0;

            final boolean ltr = container.getComponentOrientation ().isLeftToRight ();

            final boolean useBaseline = getAlignOnBaseline ();
            int[] ascent = null;
            int[] descent = null;

            if ( useBaseline )
            {
                ascent = new int[ nmembers ];
                descent = new int[ nmembers ];
            }

            for ( int i = 0; i < nmembers; i++ )
            {
                final Component m = container.getComponent ( i );
                if ( m.isVisible () )
                {
                    final Dimension d = m.getPreferredSize ();
                    m.setSize ( d.width, d.height );

                    if ( useBaseline )
                    {
                        final int baseline = m.getBaseline ( d.width, d.height );
                        if ( baseline >= 0 )
                        {
                            ascent[ i ] = baseline;
                            descent[ i ] = d.height - baseline;
                        }
                        else
                        {
                            ascent[ i ] = -1;
                        }
                    }
                    if ( x == 0 || x + d.width + ( x > 0 ? getHgap () : 0 ) <= maxwidth )
                    {
                        if ( x > 0 )
                        {
                            x += getHgap ();
                        }
                        x += d.width;
                        rowh = Math.max ( rowh, d.height );
                    }
                    else
                    {
                        rowh = moveComponents ( container, insets.left, y,
                                maxwidth - x, rowh,
                                start, i, ltr, useBaseline, ascent, descent );
                        x = d.width;
                        y += getVgap () + rowh;
                        rowh = d.height;
                        start = i;
                    }
                }
            }
            moveComponents ( container, insets.left, y,
                    maxwidth - x, rowh,
                    start, nmembers, ltr, useBaseline, ascent, descent );
        }
    }

    /**
     * Centers the elements in the specified row, if there is any slack.
     *
     * @param container   the component which needs to be moved
     * @param x           the x coordinate
     * @param y           the y coordinate
     * @param width       the width dimensions
     * @param height      the height dimensions
     * @param rowStart    the beginning of the row
     * @param rowEnd      the the ending of the row
     * @param ltr         component orientation
     * @param useBaseline whether or not to align on baseline
     * @param ascent      ascent for the components, only valid if useBaseline is true
     * @param descent     ascent for the components, only valid if useBaseline is true
     * @return actual row height
     */
    protected int moveComponents ( final Container container, int x, final int y, final int width, int height, final int rowStart,
                                   final int rowEnd, final boolean ltr, final boolean useBaseline, final int[] ascent, final int[] descent )
    {
        switch ( getAlignment () )
        {
            case LEFT:
                x += ltr ? 0 : width;
                break;
            case CENTER:
                x += width / 2;
                break;
            case RIGHT:
                x += ltr ? width : 0;
                break;
            case LEADING:
                break;
            case TRAILING:
                x += width;
                break;
        }
        int maxAscent = 0;
        int nonbaselineHeight = 0;
        int baselineOffset = 0;
        if ( useBaseline )
        {
            int maxDescent = 0;
            for ( int i = rowStart; i < rowEnd; i++ )
            {
                final Component m = container.getComponent ( i );
                if ( m.isVisible () )
                {
                    if ( ascent[ i ] >= 0 )
                    {
                        maxAscent = Math.max ( maxAscent, ascent[ i ] );
                        maxDescent = Math.max ( maxDescent, descent[ i ] );
                    }
                    else
                    {
                        nonbaselineHeight = Math.max ( m.getHeight (),
                                nonbaselineHeight );
                    }
                }
            }
            height = Math.max ( maxAscent + maxDescent, nonbaselineHeight );
            baselineOffset = ( height - maxAscent - maxDescent ) / 2;
        }
        for ( int i = rowStart; i < rowEnd; i++ )
        {
            final Component m = container.getComponent ( i );
            if ( m.isVisible () )
            {
                final int cy;
                if ( useBaseline && ascent[ i ] >= 0 )
                {
                    cy = y + baselineOffset + maxAscent - ascent[ i ];
                }
                else
                {
                    cy = y + ( height - m.getHeight () ) / 2;
                }
                if ( ltr )
                {
                    m.setLocation ( x, cy );
                }
                else
                {
                    m.setLocation ( container.getWidth () - x - m.getWidth (), cy );
                }
                x += m.getWidth () + getHgap ();
            }
        }
        return height;
    }
}