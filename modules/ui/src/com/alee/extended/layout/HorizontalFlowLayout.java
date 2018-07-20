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
 * @see com.alee.extended.layout.VerticalFlowLayout
 */
public class HorizontalFlowLayout extends AbstractLayoutManager
{
    /**
     * todo 1. Add horizontal alignment setting
     */

    protected int hgap;
    protected boolean fillLast;

    public HorizontalFlowLayout ()
    {
        this ( 2 );
    }

    public HorizontalFlowLayout ( final int hgap )
    {
        this ( hgap, false );
    }

    public HorizontalFlowLayout ( final int hgap, final boolean fillLast )
    {
        this.hgap = hgap;
        this.fillLast = fillLast;
    }

    public int getHorizontalGap ()
    {
        return hgap;
    }

    public void setHorizontalGap ( final int hgap )
    {
        this.hgap = hgap;
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
    public void layoutContainer ( final Container container )
    {
        // Required size
        final Dimension required = preferredLayoutSize ( container );

        // Available size (limiting width to required)
        final Dimension available = new Dimension ( required.width, container.getSize ().height );

        // Additional variables
        final boolean ltr = container.getComponentOrientation ().isLeftToRight ();
        final Insets insets = container.getInsets ();
        final int ls = ltr ? insets.left : insets.right;
        final int rs = ltr ? insets.right : insets.left;
        final boolean min = required.width < available.width;
        int x = ls;
        final int y = insets.top;
        final int height = Math.max ( available.height, required.height ) - insets.top - insets.bottom;
        final int xsWidth = available.width - required.width;

        // Layouting components
        final int count = container.getComponentCount ();
        for ( int i = 0; i < count; i++ )
        {
            final Component c = container.getComponent ( i );
            if ( c.isVisible () )
            {
                int w = min ? c.getMinimumSize ().width : c.getPreferredSize ().width;
                if ( xsWidth > 0 )
                {
                    w += w * xsWidth / required.width;
                }

                final int width = fillLast && i == count - 1 &&
                        container.getWidth () - x - rs > 0 ? container.getWidth () - x - rs : w;
                if ( ltr )
                {
                    c.setBounds ( x, y, width, height );
                }
                else
                {
                    c.setBounds ( container.getWidth () - x - width, y, width, height );
                }
                x += w + getHorizontalGap ();
            }
        }
    }

    @Override
    public Dimension preferredLayoutSize ( final Container container )
    {
        return getLayoutSize ( container, false );
    }

    @Override
    public Dimension minimumLayoutSize ( final Container container )
    {
        return getLayoutSize ( container, true );
    }

    protected Dimension getLayoutSize ( final Container container, final boolean minimum )
    {
        final Dimension size = new Dimension ( 0, 0 );

        for ( int i = 0; i < container.getComponentCount (); i++ )
        {
            final Component c = container.getComponent ( i );
            if ( c.isVisible () )
            {
                final Dimension tmp = minimum ? c.getMinimumSize () : c.getPreferredSize ();
                size.width += tmp.width;
                if ( i > 0 )
                {
                    size.width += getHorizontalGap ();
                }
                size.height = Math.max ( tmp.height, size.height );
            }
        }

        final Insets border = container.getInsets ();
        size.width += border.left + border.right;
        size.height += border.top + border.bottom;

        return size;
    }
}