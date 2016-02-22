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
 * Custom layout used by {@link com.alee.laf.toolbar.WebToolBar} component by default.
 *
 * @author Mikle Garin
 */

public class ToolbarLayout extends AbstractLayoutManager implements SwingConstants
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
     * Saved layout constraints.
     */
    protected Map<Component, String> constraints = new HashMap<Component, String> ();

    /**
     * Spacing between components.
     */
    protected int spacing = 2;

    /**
     * Spacing between left and right (top and bottom) layout parts.
     */
    protected int partsSpacing = 20;

    /**
     * Layout orientation.
     */
    protected int orientation = HORIZONTAL;

    /**
     * Layout margin.
     */
    protected Insets margin = null;

    public ToolbarLayout ()
    {
        super ();
    }

    public ToolbarLayout ( final int spacing )
    {
        super ();
        this.spacing = spacing;
    }

    public ToolbarLayout ( final int spacing, final int orientation )
    {
        super ();
        this.spacing = spacing;
        this.orientation = orientation;
    }

    public ToolbarLayout ( final int spacing, final int partsSpacing, final int orientation )
    {
        super ();
        this.spacing = spacing;
        this.partsSpacing = partsSpacing;
        this.orientation = orientation;
    }

    /**
     * Layout constraints
     */

    public Map<Component, String> getConstraints ()
    {
        return constraints;
    }

    public void setConstraints ( final Map<Component, String> constraints )
    {
        this.constraints = constraints;
    }

    /**
     * Layout cells spacing
     */

    public int getSpacing ()
    {
        return spacing;
    }

    public void setSpacing ( final int spacing )
    {
        this.spacing = spacing;
    }

    /**
     * Start-end parts spacing This one does not affect layout if there are any components in FILL part
     */

    public int getPartsSpacing ()
    {
        return partsSpacing;
    }

    public void setPartsSpacing ( final int partsSpacing )
    {
        this.partsSpacing = partsSpacing;
    }

    /**
     * Layout orientation
     */

    public int getOrientation ()
    {
        return orientation;
    }

    public void setOrientation ( final int orientation )
    {
        this.orientation = orientation;
    }

    /**
     * Layout sides margin In case this value is null component border is taken into account instead
     */

    public Insets getMargin ()
    {
        return margin;
    }

    public void setMargin ( final Insets margin )
    {
        this.margin = margin;
    }

    /**
     * Standard LayoutManager methods
     */

    @Override
    public void addComponent ( final Component component, final Object constraints )
    {
        final String value = ( String ) constraints;
        if ( value != null && !value.trim ().equals ( "" ) && !value.equals ( START ) &&
                !value.equals ( MIDDLE ) && !value.equals ( FILL ) && !value.equals ( END ) )
        {
            throw new IllegalArgumentException (
                    "Cannot add to layout: constraint must be null or an empty/'START'/'MIDDLE'/'FILL'/'END' string" );
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
        final Insets insets = getActualInsets ( parent );
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
        final Insets insets = getActualInsets ( parent );
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

    protected boolean hasElement ( final String element )
    {
        return constraints.containsValue ( element );
    }

    protected Insets getActualInsets ( final Container container )
    {
        if ( margin != null )
        {
            final Insets insets = container.getInsets ();
            insets.top += margin.top;
            insets.left += margin.left;
            insets.bottom += margin.bottom;
            insets.right += margin.right;
            return insets;
        }
        else
        {
            return container.getInsets ();
        }
    }
}