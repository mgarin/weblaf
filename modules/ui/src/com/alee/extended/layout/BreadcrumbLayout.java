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

public class BreadcrumbLayout extends AbstractLayoutManager
{
    private int overlap;

    public BreadcrumbLayout ()
    {
        this ( 0 );
    }

    public BreadcrumbLayout ( final int overlap )
    {
        super ();
        this.overlap = overlap;
    }

    public int getOverlap ()
    {
        return overlap;
    }

    public void setOverlap ( final int overlap )
    {
        this.overlap = overlap;
    }

    @Override
    public Dimension preferredLayoutSize ( final Container parent )
    {
        final Insets insets = parent.getInsets ();
        final Dimension maxSize = new Dimension ( insets.left + insets.right, insets.top + insets.bottom );
        for ( int i = 0; i < parent.getComponentCount (); i++ )
        {
            final Dimension ps = parent.getComponent ( i ).getPreferredSize ();
            maxSize.width += ps.width - ( i < parent.getComponentCount () - 1 ? overlap : 0 );
            maxSize.height = Math.max ( maxSize.height, insets.top + ps.height + insets.bottom );
        }
        return maxSize;
    }

    @Override
    public void layoutContainer ( final Container parent )
    {
        final Insets insets = parent.getInsets ();
        if ( parent.getComponentOrientation ().isLeftToRight () )
        {
            int x = insets.left;
            for ( final Component component : parent.getComponents () )
            {
                final Dimension ps = component.getPreferredSize ();
                component.setBounds ( x, insets.top, ps.width, parent.getHeight () - insets.top - insets.bottom );
                x += ps.width - overlap;
            }
        }
        else
        {
            int x = parent.getWidth () - insets.right;
            for ( final Component component : parent.getComponents () )
            {
                final Dimension ps = component.getPreferredSize ();
                component.setBounds ( x - ps.width, insets.top, ps.width, parent.getHeight () - insets.top - insets.bottom );
                x += overlap - ps.width;
            }
        }
    }
}