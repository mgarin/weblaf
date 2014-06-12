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
 * User: mgarin Date: 30.12.11 Time: 18:16
 */

public class HorizontalOverflowLayout extends AbstractLayoutManager
{
    protected int overflow;

    public HorizontalOverflowLayout ( int overflow )
    {
        this.overflow = overflow;
    }

    public int getOverflow ()
    {
        return overflow;
    }

    public void setOverflow ( int overflow )
    {
        this.overflow = overflow;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Dimension preferredLayoutSize ( Container parent )
    {
        return getLayoutSize ( parent, false );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Dimension minimumLayoutSize ( Container parent )
    {
        return getLayoutSize ( parent, true );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void layoutContainer ( Container parent )
    {
        // Required size
        Dimension required = preferredLayoutSize ( parent );

        // Available size (limiting width to required)
        Dimension available = new Dimension ( required.width, parent.getSize ().height );

        boolean min = required.width < available.width;
        Insets insets = parent.getInsets ();
        int x = insets.left;
        final int y = insets.top;
        final int h = Math.max ( available.height, required.height ) - insets.top - insets.bottom;
        final int xsWidth = available.width - required.width;

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

                c.setBounds ( x, y, w, h );
                x += ( w - overflow );
            }
        }
    }

    protected Dimension getLayoutSize ( Container parent, boolean min )
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
                size.width -= overflow;
            }
        }
        Insets border = parent.getInsets ();
        size.width += border.left + border.right;
        size.height += border.top + border.bottom;
        return size;
    }
}