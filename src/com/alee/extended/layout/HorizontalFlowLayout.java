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
 * User: mgarin Date: 06.05.11 Time: 15:17
 */

public class HorizontalFlowLayout implements LayoutManager
{
    private int horizGap;
    private boolean fillLast;

    public HorizontalFlowLayout ()
    {
        this ( 2 );
    }

    public HorizontalFlowLayout ( int gap )
    {
        this ( gap, false );
    }

    public HorizontalFlowLayout ( int gap, boolean fillLast )
    {
        this.horizGap = gap;
        this.fillLast = fillLast;
    }

    public int getHorizontalGap ()
    {
        return horizGap;
    }

    @Override
    public void addLayoutComponent ( String name, Component comp )
    {
    }

    @Override
    public void removeLayoutComponent ( Component comp )
    {
    }

    @Override
    public Dimension preferredLayoutSize ( Container parent )
    {
        return getLayoutSize ( parent, false );
    }

    @Override
    public Dimension minimumLayoutSize ( Container parent )
    {
        return getLayoutSize ( parent, true );
    }

    private Dimension getLayoutSize ( Container parent, boolean min )
    {
        int count = parent.getComponentCount ();
        Dimension size = new Dimension ( 0, 0 );
        for ( int i = 0; i < count; i++ )
        {
            Component c = parent.getComponent ( i );
            Dimension tmp = ( min ) ? c.getMinimumSize () : c.getPreferredSize ();
            size.height = Math.max ( tmp.height, size.height );
            size.width += tmp.width;

            if ( i != 0 )
            {
                size.width += getHorizontalGap ();
            }
        }
        Insets border = parent.getInsets ();
        size.width += border.left + border.right;
        size.height += border.top + border.bottom;
        return size;
    }

    @Override
    public void layoutContainer ( Container parent )
    {
        // Required size
        Dimension required = preferredLayoutSize ( parent );

        // Available size (limiting width to required)
        Dimension available = new Dimension ( required.width, parent.getSize ().height );

        // Additional variables
        boolean ltr = parent.getComponentOrientation ().isLeftToRight ();
        Insets insets = parent.getInsets ();
        int ls = ltr ? insets.left : insets.right;
        int rs = ltr ? insets.right : insets.left;
        boolean min = required.width < available.width;
        int x = ls;
        final int y = insets.top;
        final int height = Math.max ( available.height, required.height ) - insets.top - insets.bottom;
        final int xsWidth = available.width - required.width;

        // Layouting components
        int count = parent.getComponentCount ();
        for ( int i = 0; i < count; i++ )
        {
            Component c = parent.getComponent ( i );
            if ( c.isVisible () )
            {
                int w = ( min ) ? c.getMinimumSize ().width : c.getPreferredSize ().width;
                if ( xsWidth > 0 )
                {
                    w += ( w * xsWidth / required.width );
                }

                int width = fillLast && i == count - 1 &&
                        parent.getWidth () - x - rs > 0 ? parent.getWidth () - x - rs : w;
                if ( ltr )
                {
                    c.setBounds ( x, y, width, height );
                }
                else
                {
                    c.setBounds ( parent.getWidth () - x - width, y, width, height );
                }
                x += ( w + getHorizontalGap () );
            }
        }
    }
}
