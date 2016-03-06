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
 * @author Mikle Garin
 */

public class HorizontalFlowLayout extends AbstractLayoutManager
{
    /**
     * todo 1. Alignment
     */

    protected int gap;
    protected boolean fillLast;

    public HorizontalFlowLayout ()
    {
        this ( 2 );
    }

    public HorizontalFlowLayout ( final int gap )
    {
        this ( gap, false );
    }

    public HorizontalFlowLayout ( final int gap, final boolean fillLast )
    {
        this.gap = gap;
        this.fillLast = fillLast;
    }

    public int getHorizontalGap ()
    {
        return gap;
    }

    public void setHorizGap ( final int gap )
    {
        this.gap = gap;
    }

    public boolean isFillLast ()
    {
        return fillLast;
    }

    public void setFillLast ( final boolean fill )
    {
        this.fillLast = fill;
    }

    @Override
    public Dimension preferredLayoutSize ( final Container parent )
    {
        return getLayoutSize ( parent, false );
    }

    @Override
    public Dimension minimumLayoutSize ( final Container parent )
    {
        return getLayoutSize ( parent, true );
    }

    @Override
    public void layoutContainer ( final Container parent )
    {
        // Required size
        final Dimension required = preferredLayoutSize ( parent );

        // Available size (limiting width to required)
        final Dimension available = new Dimension ( required.width, parent.getSize ().height );

        // Additional variables
        final boolean ltr = parent.getComponentOrientation ().isLeftToRight ();
        final Insets insets = parent.getInsets ();
        final int ls = ltr ? insets.left : insets.right;
        final int rs = ltr ? insets.right : insets.left;
        final boolean min = required.width < available.width;
        int x = ls;
        final int y = insets.top;
        final int height = Math.max ( available.height, required.height ) - insets.top - insets.bottom;
        final int xsWidth = available.width - required.width;

        // Layouting components
        final int count = parent.getComponentCount ();
        for ( int i = 0; i < count; i++ )
        {
            final Component c = parent.getComponent ( i );
            if ( c.isVisible () )
            {
                int w = ( min ) ? c.getMinimumSize ().width : c.getPreferredSize ().width;
                if ( xsWidth > 0 )
                {
                    w += w * xsWidth / required.width;
                }

                final int width = fillLast && i == count - 1 &&
                        parent.getWidth () - x - rs > 0 ? parent.getWidth () - x - rs : w;
                if ( ltr )
                {
                    c.setBounds ( x, y, width, height );
                }
                else
                {
                    c.setBounds ( parent.getWidth () - x - width, y, width, height );
                }
                x += w + getHorizontalGap ();
            }
        }
    }

    protected Dimension getLayoutSize ( final Container parent, final boolean min )
    {
        final int count = parent.getComponentCount ();
        final Dimension size = new Dimension ( 0, 0 );
        for ( int i = 0; i < count; i++ )
        {
            final Component c = parent.getComponent ( i );
            final Dimension tmp = ( min ) ? c.getMinimumSize () : c.getPreferredSize ();
            size.height = Math.max ( tmp.height, size.height );
            size.width += tmp.width;

            if ( i != 0 )
            {
                size.width += getHorizontalGap ();
            }
        }
        final Insets border = parent.getInsets ();
        size.width += border.left + border.right;
        size.height += border.top + border.bottom;
        return size;
    }
}