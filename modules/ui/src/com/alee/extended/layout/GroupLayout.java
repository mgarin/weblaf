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
 * Custom layout mostly used by custom GroupPanel container.
 * It allows simple grouping of components placed into one line - either horizontally or vertically.
 *
 * @author Mikle Garin
 */

public class GroupLayout extends AbstractLayoutManager implements SwingConstants
{
    public static final String PREFERRED = "PREFERRED";
    public static final String FILL = "FILL";

    protected int orientation;
    protected int gap;

    protected Map<Component, String> constraints = new HashMap<Component, String> ();

    public GroupLayout ()
    {
        this ( HORIZONTAL );
    }

    public GroupLayout ( final int orientation )
    {
        this ( orientation, 0 );
    }

    public GroupLayout ( final int orientation, final int gap )
    {
        super ();
        setOrientation ( orientation );
        setGap ( gap );
    }

    public int getOrientation ()
    {
        return orientation;
    }

    public void setOrientation ( final int orientation )
    {
        this.orientation = orientation;
    }

    public int getGap ()
    {
        return gap;
    }

    public void setGap ( final int gap )
    {
        this.gap = gap;
    }

    @Override
    public void addComponent ( final Component component, final Object constraints )
    {
        this.constraints.put ( component, ( String ) constraints );
    }

    @Override
    public void removeComponent ( final Component component )
    {
        this.constraints.remove ( component );
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

    @Override
    public void layoutContainer ( final Container container )
    {
        // Gathering component sizes
        int fillCount = 0;
        int preferred = 0;
        for ( final Component component : container.getComponents () )
        {
            final boolean fill = isFill ( component );
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
        if ( container.getComponentCount () > 0 )
        {
            preferred += gap * ( container.getComponentCount () - 1 );
        }

        // Calculating required sizes
        final boolean ltr = container.getComponentOrientation ().isLeftToRight ();
        final Insets insets = container.getInsets ();
        final Dimension size = container.getSize ();
        final int width = size.width - insets.left - insets.right;
        final int height = size.height - insets.top - insets.bottom;
        final int fillSize = orientation == HORIZONTAL ? fillCount > 0 && width > preferred ? ( width - preferred ) / fillCount : 0 :
                fillCount > 0 && height > preferred ? ( height - preferred ) / fillCount : 0;
        int x = ltr || orientation == VERTICAL ? insets.left : size.width - insets.right;
        int y = insets.top;

        // Placing components
        for ( final Component component : container.getComponents () )
        {
            final Dimension cps = component.getPreferredSize ();
            final boolean fill = isFill ( component );
            if ( orientation == HORIZONTAL )
            {
                final int w = fill ? fillSize : cps.width;
                component.setBounds ( x - ( ltr ? 0 : w ), y, w, height );
                x += ( ltr ? 1 : -1 ) * ( w + gap );
            }
            else
            {
                final int h = fill ? fillSize : cps.height;
                component.setBounds ( x, y, width, h );
                y += h + gap;
            }
        }
    }

    protected Dimension getLayoutSize ( final Container container, final boolean minimum )
    {
        final Insets insets = container.getInsets ();
        final Dimension ps = new Dimension ();
        for ( final Component component : container.getComponents () )
        {
            final Dimension cps = minimum ? component.getMinimumSize () : component.getPreferredSize ();
            final boolean ignoreSize = minimum && isFill ( component );
            if ( orientation == HORIZONTAL )
            {
                ps.width += ignoreSize ? 1 : cps.width;
                ps.height = Math.max ( ps.height, cps.height );
            }
            else
            {
                ps.width = Math.max ( ps.width, cps.width );
                ps.height += ignoreSize ? 1 : cps.height;
            }
        }
        if ( container.getComponentCount () > 0 )
        {
            if ( orientation == HORIZONTAL )
            {
                ps.width += gap * ( container.getComponentCount () - 1 );
            }
            else
            {
                ps.height += gap * ( container.getComponentCount () - 1 );
            }
        }
        ps.width += insets.left + insets.right;
        ps.height += insets.top + insets.bottom;
        return ps;
    }

    protected boolean isFill ( final Component component )
    {
        if ( constraints.containsKey ( component ) )
        {
            final String constraint = constraints.get ( component );
            return constraint != null && constraint.equals ( FILL );
        }
        else
        {
            return false;
        }
    }
}