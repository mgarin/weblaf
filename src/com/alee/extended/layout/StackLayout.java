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
 * User: mgarin Date: 26.10.11 Time: 13:38
 */

public class StackLayout implements LayoutManager
{
    // StackLayout constraints constants
    public static final String CONTENT = "CONTENT";
    public static final String HIDDEN = "HIDDEN";

    // Saved layout constraints
    private Map<Component, String> content = new HashMap<Component, String> ();

    public StackLayout ()
    {
        super ();
    }

    @Override
    public void addLayoutComponent ( String name, Component comp )
    {
        if ( name != null && !name.trim ().equals ( "" ) && !name.equals ( CONTENT ) &&
                !name.equals ( HIDDEN ) )
        {
            throw new IllegalArgumentException ( "Cannot add to layout: constraint must be null or an empty/'CONTENT'/'HIDDEN' string" );
        }

        content.put ( comp, name == null || name.trim ().equals ( "" ) ? CONTENT : name );
    }

    @Override
    public void removeLayoutComponent ( Component comp )
    {
        content.remove ( comp );
    }

    @Override
    public Dimension preferredLayoutSize ( Container parent )
    {
        Insets insets = parent.getInsets ();
        Dimension ps = new Dimension ( 0, 0 );
        for ( Component component : parent.getComponents () )
        {
            if ( content.get ( component ) == null || !content.get ( component ).equals ( HIDDEN ) )
            {
                Dimension cps = component.getPreferredSize ();
                ps.width = Math.max ( ps.width, cps.width );
                ps.height = Math.max ( ps.height, cps.height );
            }
        }
        return new Dimension ( insets.left + ps.width + insets.right, insets.top + ps.height + insets.bottom );
    }

    @Override
    public Dimension minimumLayoutSize ( Container parent )
    {
        return preferredLayoutSize ( parent );
    }

    @Override
    public void layoutContainer ( Container parent )
    {
        Insets insets = parent.getInsets ();
        for ( Component component : parent.getComponents () )
        {
            if ( content.get ( component ) == null || !content.get ( component ).equals ( HIDDEN ) )
            {
                component.setBounds ( insets.left, insets.top, parent.getWidth () - insets.left - insets.right,
                        parent.getHeight () - insets.top - insets.bottom );
            }
        }
    }
}
