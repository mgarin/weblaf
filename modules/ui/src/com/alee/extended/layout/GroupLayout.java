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

import com.alee.api.annotations.NotNull;
import com.alee.api.annotations.Nullable;
import com.alee.api.data.Orientation;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Custom layout mostly used by custom GroupPanel container.
 * It allows simple grouping of components placed into one line - either horizontally or vertically.
 *
 * @author Mikle Garin
 */
public class GroupLayout extends AbstractLayoutManager
{
    /**
     * All {@link #PREFERRED} {@link Component}s use their preferred width for horizontal layout and preferred height for vertical layout.
     */
    public static final String PREFERRED = "PREFERRED";

    /**
     * All remaining space left after {@link Component}s placed under {@link #PREFERRED} constraints is shared equally between
     * {@link Component}s placed under {@link #FILL} constraints.
     */
    public static final String FILL = "FILL";

    /**
     * Layout orientation.
     */
    protected Orientation orientation;

    /**
     * Gap between {@link Component}s in this layout.
     */
    protected int gap;

    /**
     * Saved constraints.
     */
    protected final Map<Component, String> constraints;

    /**
     * Constructs new {@link GroupLayout}.
     */
    public GroupLayout ()
    {
        this ( Orientation.horizontal, 0 );
    }

    /**
     * Constructs new {@link GroupLayout}.
     *
     * @param orientation layout orientation
     */
    public GroupLayout ( final Orientation orientation )
    {
        this ( orientation, 0 );
    }

    /**
     * Constructs new {@link GroupLayout}.
     *
     * @param orientation layout orientation
     * @param gap         gap between {@link Component}s in this layout
     */
    public GroupLayout ( final Orientation orientation, final int gap )
    {
        this.orientation = orientation;
        this.gap = gap;
        this.constraints = new HashMap<Component, String> ();
    }

    /**
     * Returns layout orientation.
     *
     * @return layout orientation
     */
    public Orientation getOrientation ()
    {
        return orientation;
    }

    /**
     * Sets layout orientation.
     *
     * @param orientation layout orientation
     */
    public void setOrientation ( final Orientation orientation )
    {
        this.orientation = orientation;
    }

    /**
     * Returns gap between {@link Component}s in this layout.
     *
     * @return gap between {@link Component}s in this layout
     */
    public int getGap ()
    {
        return gap;
    }

    /**
     * Sets gap between {@link Component}s in this layout.
     *
     * @param gap gap between {@link Component}s in this layout
     */
    public void setGap ( final int gap )
    {
        this.gap = gap;
    }

    @Override
    public void addComponent ( @NotNull final Component component, @Nullable final Object constraints )
    {
        this.constraints.put ( component, ( String ) constraints );
    }

    @Override
    public void removeComponent ( @NotNull final Component component )
    {
        this.constraints.remove ( component );
    }

    @Override
    public void layoutContainer ( @NotNull final Container container )
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
            if ( orientation.isHorizontal () )
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
        final int fillSize = orientation.isHorizontal () ?
                fillCount > 0 && width > preferred ? ( width - preferred ) / fillCount : 0 :
                fillCount > 0 && height > preferred ? ( height - preferred ) / fillCount : 0;
        int x = ltr || orientation.isVertical () ? insets.left : size.width - insets.right;
        int y = insets.top;

        // Placing components
        for ( final Component component : container.getComponents () )
        {
            final Dimension cps = component.getPreferredSize ();
            final boolean fill = isFill ( component );
            if ( orientation.isHorizontal () )
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

    @NotNull
    @Override
    public Dimension preferredLayoutSize ( @NotNull final Container container )
    {
        return getLayoutSize ( container, false );
    }

    @NotNull
    @Override
    public Dimension minimumLayoutSize ( @NotNull final Container container )
    {
        return getLayoutSize ( container, true );
    }

    /**
     * Returns {@link Container}'s layout size of the requested type.
     *
     * @param container {@link Container}
     * @param minimum   whether or not should return minimum size
     * @return {@link Container}'s layout size of the requested type
     */
    protected Dimension getLayoutSize ( final Container container, final boolean minimum )
    {
        final Insets insets = container.getInsets ();
        final Dimension ps = new Dimension ();
        for ( final Component component : container.getComponents () )
        {
            final Dimension cps = minimum ? component.getMinimumSize () : component.getPreferredSize ();
            final boolean ignoreSize = minimum && isFill ( component );
            if ( orientation.isHorizontal () )
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
            if ( orientation.isHorizontal () )
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

    /**
     * Returns whether or not specified {@link Component} uses {@link #FILL} constraint.
     *
     * @param component {@link Component}
     * @return {@code true} if specified {@link Component} uses {@link #FILL} constraint, {@code false} otherwise
     */
    protected boolean isFill ( final Component component )
    {
        final boolean fill;
        if ( constraints.containsKey ( component ) )
        {
            final String constraint = constraints.get ( component );
            fill = constraint != null && constraint.equals ( FILL );
        }
        else
        {
            fill = false;
        }
        return fill;
    }
}