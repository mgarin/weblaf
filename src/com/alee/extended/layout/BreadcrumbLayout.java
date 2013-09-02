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
 * User: mgarin Date: 01.03.12 Time: 18:50
 */

public class BreadcrumbLayout extends AbstractLayoutManager
{
    private int overlap;

    public BreadcrumbLayout ()
    {
        this ( 0 );
    }

    public BreadcrumbLayout ( int overlap )
    {
        super ();
        this.overlap = overlap;
    }

    public int getOverlap ()
    {
        return overlap;
    }

    public void setOverlap ( int overlap )
    {
        this.overlap = overlap;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Dimension preferredLayoutSize ( Container parent )
    {
        Insets insets = parent.getInsets ();
        Dimension maxSize = new Dimension ( insets.left + insets.right, insets.top + insets.bottom );
        for ( int i = 0; i < parent.getComponentCount (); i++ )
        {
            Dimension ps = parent.getComponent ( i ).getPreferredSize ();
            maxSize.width += ps.width - ( i < parent.getComponentCount () - 1 ? overlap : 0 );
            maxSize.height = Math.max ( maxSize.height, insets.top + ps.height + insets.bottom );
        }
        return maxSize;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void layoutContainer ( Container parent )
    {
        Insets insets = parent.getInsets ();
        if ( parent.getComponentOrientation ().isLeftToRight () )
        {
            int x = insets.left;
            for ( Component component : parent.getComponents () )
            {
                Dimension ps = component.getPreferredSize ();
                component.setBounds ( x, insets.top, ps.width, parent.getHeight () - insets.top - insets.bottom );
                x += ps.width - overlap;
            }
        }
        else
        {
            int x = parent.getWidth () - insets.right;
            for ( Component component : parent.getComponents () )
            {
                Dimension ps = component.getPreferredSize ();
                component.setBounds ( x - ps.width, insets.top, ps.width, parent.getHeight () - insets.top - insets.bottom );
                x += overlap - ps.width;
            }
        }
    }
}