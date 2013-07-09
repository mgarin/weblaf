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

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * User: mgarin Date: 23.01.13 Time: 15:32
 */

public class GroupLayout implements LayoutManager, SwingConstants
{
    public static final String PREFERRED = "PREFERRED";
    public static final String FILL = "FILL";

    private int orientation;
    private int gap;

    private Map<Component, String> constraints = new HashMap<Component, String> ();

    public GroupLayout ()
    {
        this ( HORIZONTAL );
    }

    public GroupLayout ( int orientation )
    {
        this ( orientation, 0 );
    }

    public GroupLayout ( int orientation, int gap )
    {
        super ();
        setOrientation ( orientation );
        setGap ( gap );
    }

    public int getOrientation ()
    {
        return orientation;
    }

    public void setOrientation ( int orientation )
    {
        this.orientation = orientation;
    }

    public int getGap ()
    {
        return gap;
    }

    public void setGap ( int gap )
    {
        this.gap = gap;
    }

    public void addLayoutComponent ( String name, Component comp )
    {
        constraints.put ( comp, name );
    }

    public void removeLayoutComponent ( Component comp )
    {
        constraints.remove ( comp );
    }

    public void layoutContainer ( Container parent )
    {
        // Gathering component sizes
        int fillCount = 0;
        int preferred = 0;
        for ( Component component : parent.getComponents () )
        {
            boolean fill = isFill ( component );
            if ( fill )
            {
                fillCount++;
            }
            if ( orientation == HORIZONTAL )
            {
                preferred += fill ? 0 : component.getPreferredSize ().width;
            }
            else
            {
                preferred += fill ? 0 : component.getPreferredSize ().height;
            }
        }
        if ( parent.getComponentCount () > 0 )
        {
            preferred += gap * ( parent.getComponentCount () - 1 );
        }

        // Calculating required sizes
        boolean ltr = parent.getComponentOrientation ().isLeftToRight ();
        Insets insets = parent.getInsets ();
        Dimension size = parent.getSize ();
        int width = size.width - insets.left - insets.right;
        int height = size.height - insets.top - insets.bottom;
        int fillSize = orientation == HORIZONTAL ? ( fillCount > 0 && width > preferred ? ( width - preferred ) / fillCount : 0 ) :
                ( fillCount > 0 && height > preferred ? ( height - preferred ) / fillCount : 0 );
        int x = ltr || orientation == VERTICAL ? insets.left : size.width - insets.right;
        int y = insets.top;

        // Placing components
        for ( Component component : parent.getComponents () )
        {
            Dimension cps = component.getPreferredSize ();
            boolean fill = isFill ( component );
            if ( orientation == HORIZONTAL )
            {
                int w = fill ? fillSize : cps.width;
                component.setBounds ( x - ( ltr ? 0 : w ), y, w, height );
                x += ( ltr ? 1 : -1 ) * ( w + gap );
            }
            else
            {
                int h = fill ? fillSize : cps.height;
                component.setBounds ( x, y, width, h );
                y += h + gap;
            }
        }
    }

    public Dimension preferredLayoutSize ( Container parent )
    {
        return calculateSize ( parent, false );
    }

    public Dimension minimumLayoutSize ( Container parent )
    {
        return calculateSize ( parent, true );
    }

    private Dimension calculateSize ( Container parent, boolean minimum )
    {
        Insets insets = parent.getInsets ();
        Dimension ps = new Dimension ();
        for ( Component component : parent.getComponents () )
        {
            Dimension cps = minimum ? component.getMinimumSize () : component.getPreferredSize ();
            boolean ignoreSize = minimum && isFill ( component );
            if ( orientation == HORIZONTAL )
            {
                ps.width += ( ignoreSize ? 1 : cps.width );
                ps.height = Math.max ( ps.height, cps.height );
            }
            else
            {
                ps.width = Math.max ( ps.width, cps.width );
                ps.height += ( ignoreSize ? 1 : cps.height );
            }
        }
        if ( parent.getComponentCount () > 0 )
        {
            if ( orientation == HORIZONTAL )
            {
                ps.width += gap * ( parent.getComponentCount () - 1 );
            }
            else
            {
                ps.height += gap * ( parent.getComponentCount () - 1 );
            }
        }
        ps.width += insets.left + insets.right;
        ps.height += insets.top + insets.bottom;
        return ps;
    }

    private boolean isFill ( Component component )
    {
        if ( constraints.containsKey ( component ) )
        {
            String constraint = constraints.get ( component );
            return constraint != null && constraint.equals ( FILL );
        }
        else
        {
            return false;
        }
    }
}
