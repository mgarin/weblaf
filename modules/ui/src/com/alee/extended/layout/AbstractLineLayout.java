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

import com.alee.utils.CompareUtils;
import com.alee.utils.TextUtils;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Custom {@link LayoutManager}
 *
 * @author Mikle Garin
 */

public abstract class AbstractLineLayout extends AbstractLayoutManager implements SwingConstants
{
    /**
     * Positions component at the leading side of the container.
     */
    public static final String START = "START";

    /**
     * Positions component in the middle between leading and trailing sides.
     */
    public static final String MIDDLE = "MIDDLE";

    /**
     * Forces component to fill all the space left between leading and trailing sides.
     */
    public static final String FILL = "FILL";

    /**
     * Positions component at the trailing side of the container.
     */
    public static final String END = "END";

    /**
     * Spacing between layout elements.
     */
    protected int spacing;

    /**
     * Spacing between {@link #START}, {@link #MIDDLE} and {@link #END} layout parts.
     */
    protected int partsSpacing;

    /**
     * Saved layout constraints.
     */
    protected transient Map<Component, String> constraints;

    /**
     * Constructs new {@link AbstractLineLayout}.
     */
    public AbstractLineLayout ()
    {
        this ( 2 );
    }

    /**
     * Constructs new {@link AbstractLineLayout}.
     *
     * @param spacing spacing between layout elements
     */
    public AbstractLineLayout ( final int spacing )
    {
        this ( spacing, 20 );
    }

    /**
     * Constructs new {@link AbstractLineLayout}.
     *
     * @param spacing      spacing between layout elements
     * @param partsSpacing spacing between {@link #START}, {@link #MIDDLE} and {@link #END} layout parts
     */
    public AbstractLineLayout ( final int spacing, final int partsSpacing )
    {
        super ();
        this.spacing = spacing;
        this.partsSpacing = partsSpacing;
        this.constraints = new HashMap<Component, String> ();
    }

    @Override
    public void addComponent ( final Component component, final Object constraints )
    {
        final String value = ( String ) constraints;
        if ( !TextUtils.isBlank ( value ) && !CompareUtils.equals ( value, START, MIDDLE, FILL, END ) )
        {
            throw new IllegalArgumentException ( "Layout only supports 'null', empty, 'START', 'MIDDLE', 'FILL' or 'END' constraints" );
        }
        this.constraints.put ( component, value == null || value.trim ().equals ( "" ) ? START : value );
    }

    @Override
    public void removeComponent ( final Component component )
    {
        constraints.remove ( component );
    }


    @Override
    public Dimension preferredLayoutSize ( final Container parent )
    {
        final int orientation = getOrientation ( parent );
        final Insets insets = parent.getInsets ();
        final Dimension ps = new Dimension ( insets.left + insets.right, insets.top + insets.bottom );
        final int componentCount = parent.getComponentCount ();
        for ( int i = 0; i < componentCount; i++ )
        {
            final Component component = parent.getComponent ( i );
            final Dimension cps = component.getPreferredSize ();
            if ( orientation == HORIZONTAL )
            {
                ps.width += cps.width + ( i < componentCount - 1 ? spacing : 0 );
                ps.height = Math.max ( ps.height, cps.height + insets.top + insets.bottom );
            }
            else
            {
                ps.width = Math.max ( ps.width, cps.width + insets.left + insets.right );
                ps.height += cps.height + ( i < componentCount - 1 ? spacing : 0 );
            }
        }

        // Additional spacing between start and end parts
        final boolean addPartsSpacing = hasElement ( START ) && hasElement ( END ) && !hasElement ( MIDDLE ) && !hasElement ( FILL );
        if ( orientation == HORIZONTAL )
        {
            // ps.height = insets.top + ps.height + insets.bottom;
            if ( addPartsSpacing )
            {
                ps.width += partsSpacing;
            }
        }
        else
        {
            // ps.width = insets.left + ps.width + insets.right;
            if ( addPartsSpacing )
            {
                ps.height += partsSpacing;
            }
        }
        return ps;
    }

    @Override
    public void layoutContainer ( final Container parent )
    {
        final int orientation = getOrientation ( parent );
        final Insets insets = parent.getInsets ();
        if ( orientation == HORIZONTAL )
        {
            if ( parent.getComponentOrientation ().isLeftToRight () )
            {
                // LTR component orientation

                // Filling start with components
                int startX = insets.left;
                for ( int i = 0; i < parent.getComponentCount (); i++ )
                {
                    final Component component = parent.getComponent ( i );
                    if ( constraints.get ( component ) == null ||
                            constraints.get ( component ).trim ().equals ( "" ) ||
                            constraints.get ( component ).equals ( START ) )
                    {
                        final Dimension ps = component.getPreferredSize ();
                        component.setBounds ( startX, insets.top, ps.width, parent.getHeight () - insets.top - insets.bottom );
                        startX += ps.width + spacing;
                    }
                }

                // Filling end with components
                int endX = parent.getWidth () - insets.right;
                if ( hasElement ( END ) )
                {
                    for ( int i = parent.getComponentCount () - 1; i >= 0; i-- )
                    {
                        final Component component = parent.getComponent ( i );
                        if ( constraints.get ( component ) != null && constraints.get ( component ).equals ( END ) )
                        {
                            final Dimension ps = component.getPreferredSize ();
                            endX -= ps.width;
                            component.setBounds ( endX, insets.top, ps.width, parent.getHeight () - insets.top - insets.bottom );
                            endX -= spacing;
                        }
                    }
                }

                if ( endX > startX && ( hasElement ( MIDDLE ) || hasElement ( FILL ) ) )
                {
                    for ( final Component component : parent.getComponents () )
                    {
                        if ( constraints.get ( component ) != null )
                        {
                            if ( constraints.get ( component ).equals ( MIDDLE ) )
                            {
                                final Dimension ps = component.getPreferredSize ();
                                component.setBounds ( Math.max ( startX, ( startX + endX ) / 2 - ps.width / 2 ), insets.top,
                                        Math.min ( ps.width, endX - startX ), parent.getHeight () - insets.top - insets.bottom );
                            }
                            else if ( constraints.get ( component ).equals ( FILL ) )
                            {
                                component.setBounds ( startX, insets.top, Math.max ( 0, endX - startX ),
                                        parent.getHeight () - insets.top - insets.bottom );
                            }
                        }
                    }
                }
            }
            else
            {
                // RTL component orientation

                // Filling start with components
                int startX = insets.left;
                if ( hasElement ( END ) )
                {
                    for ( int i = parent.getComponentCount () - 1; i >= 0; i-- )
                    {
                        final Component component = parent.getComponent ( i );
                        if ( constraints.get ( component ) != null && constraints.get ( component ).equals ( END ) )
                        {
                            final Dimension ps = component.getPreferredSize ();
                            component.setBounds ( startX, insets.top, ps.width, parent.getHeight () - insets.top - insets.bottom );
                            startX += ps.width + spacing;
                        }
                    }
                }

                // Filling end with components
                int endX = parent.getWidth () - insets.right;
                for ( int i = 0; i < parent.getComponentCount (); i++ )
                {
                    final Component component = parent.getComponent ( i );
                    if ( constraints.get ( component ) == null ||
                            constraints.get ( component ).trim ().equals ( "" ) ||
                            constraints.get ( component ).equals ( START ) )
                    {
                        final Dimension ps = component.getPreferredSize ();
                        endX -= ps.width;
                        component.setBounds ( endX, insets.top, ps.width, parent.getHeight () - insets.top - insets.bottom );
                        endX -= spacing;
                    }
                }

                if ( endX > startX && ( hasElement ( MIDDLE ) || hasElement ( FILL ) ) )
                {
                    for ( final Component component : parent.getComponents () )
                    {
                        if ( constraints.get ( component ) != null )
                        {
                            if ( constraints.get ( component ).equals ( MIDDLE ) )
                            {
                                final Dimension ps = component.getPreferredSize ();
                                component.setBounds ( Math.max ( startX, ( startX + endX ) / 2 - ps.width / 2 ), insets.top,
                                        Math.min ( ps.width, endX - startX ), parent.getHeight () - insets.top - insets.bottom );
                            }
                            else if ( constraints.get ( component ).equals ( FILL ) )
                            {
                                component.setBounds ( startX, insets.top, Math.max ( 0, endX - startX ),
                                        parent.getHeight () - insets.top - insets.bottom );
                            }
                        }
                    }
                }
            }
        }
        else
        {
            // Filling start with components
            int startY = insets.top;
            for ( int i = 0; i < parent.getComponentCount (); i++ )
            {
                final Component component = parent.getComponent ( i );
                if ( constraints.get ( component ) == null || constraints.get ( component ).equals ( START ) )
                {
                    final Dimension ps = component.getPreferredSize ();
                    component.setBounds ( insets.left, startY, parent.getWidth () - insets.left - insets.right, ps.height );
                    startY += ps.height + spacing;
                }
            }

            // Filling end with components
            int endY = parent.getHeight () - insets.bottom;
            if ( hasElement ( END ) )
            {
                for ( int i = parent.getComponentCount () - 1; i >= 0; i-- )
                {
                    final Component component = parent.getComponent ( i );
                    if ( constraints.get ( component ) != null && constraints.get ( component ).equals ( END ) )
                    {
                        final Dimension ps = component.getPreferredSize ();
                        endY -= ps.height;
                        component.setBounds ( insets.left, endY, parent.getWidth () - insets.left - insets.right, ps.height );
                        endY -= spacing;
                    }
                }
            }

            if ( endY > startY && ( hasElement ( MIDDLE ) || hasElement ( FILL ) ) )
            {
                for ( final Component component : parent.getComponents () )
                {
                    if ( constraints.get ( component ) != null )
                    {
                        if ( constraints.get ( component ).equals ( MIDDLE ) )
                        {
                            final Dimension ps = component.getPreferredSize ();
                            component.setBounds ( insets.left, Math.max ( startY, ( startY + endY ) / 2 - ps.height / 2 ),
                                    parent.getWidth () - insets.left - insets.right, Math.min ( ps.height, endY - startY ) );
                        }
                        else if ( constraints.get ( component ).equals ( FILL ) )
                        {
                            component.setBounds ( insets.left, startY, parent.getWidth () - insets.left - insets.right,
                                    Math.max ( 0, endY - startY ) );
                        }
                    }
                }
            }
        }
    }

    /**
     * Returns whether or not layout contains element under the specified constraints.
     *
     * @param consraints element constraints
     * @return {@code true} if layout contains element under the specified constraints, {@code false} otherwise
     */
    protected boolean hasElement ( final String consraints )
    {
        return constraints.containsValue ( consraints );
    }

    /**
     * Returns either {@link #HORIZONTAL} or {@link #VERTICAL} orientation for {@link Container}.
     *
     * @param container {@link Container} to retrieve orientation for
     * @return either {@link #HORIZONTAL} or {@link #VERTICAL} orientation for {@link Container}
     */
    public abstract int getOrientation ( Container container );
}