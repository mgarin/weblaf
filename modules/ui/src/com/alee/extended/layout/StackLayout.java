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
import java.util.HashMap;
import java.util.Map;

/**
 * This layout manager allows you to stack components atop of each other.
 * It also allows to hide some of components.
 *
 * @author Mikle Garin
 */

public class StackLayout extends AbstractLayoutManager
{
    /**
     * Visible component constraint.
     */
    public static final String CONTENT = "CONTENT";

    /**
     * Hidden component constraint.
     */
    public static final String HIDDEN = "HIDDEN";

    /**
     * Saved layout constraints.
     */
    protected Map<Component, String> constraints;

    /**
     * Constructs new StackLayout.
     */
    public StackLayout ()
    {
        super ();
        constraints = new HashMap<Component, String> ();
    }

    @Override
    public void addComponent ( final Component component, final Object constraints )
    {
        final String value = ( String ) constraints;
        if ( value != null && !value.trim ().equals ( "" ) && !value.equals ( CONTENT ) && !value.equals ( HIDDEN ) )
        {
            throw new IllegalArgumentException ( "Cannot add to layout: constraint must be null or an empty/'CONTENT'/'HIDDEN' string" );
        }
        this.constraints.put ( component, value == null || value.trim ().equals ( "" ) ? CONTENT : value );
    }

    @Override
    public void removeComponent ( final Component component )
    {
        this.constraints.remove ( component );
    }

    @Override
    public Dimension preferredLayoutSize ( final Container parent )
    {
        final Insets insets = parent.getInsets ();
        final Dimension ps = new Dimension ( 0, 0 );
        for ( final Component component : parent.getComponents () )
        {
            if ( constraints.get ( component ) == null || !constraints.get ( component ).equals ( HIDDEN ) )
            {
                final Dimension cps = component.getPreferredSize ();
                ps.width = Math.max ( ps.width, cps.width );
                ps.height = Math.max ( ps.height, cps.height );
            }
        }
        return new Dimension ( insets.left + ps.width + insets.right, insets.top + ps.height + insets.bottom );
    }

    @Override
    public void layoutContainer ( final Container parent )
    {
        final Insets insets = parent.getInsets ();
        for ( final Component component : parent.getComponents () )
        {
            if ( constraints.get ( component ) == null || !constraints.get ( component ).equals ( HIDDEN ) )
            {
                component.setBounds ( insets.left, insets.top, parent.getWidth () - insets.left - insets.right,
                        parent.getHeight () - insets.top - insets.bottom );
            }
        }
    }
}