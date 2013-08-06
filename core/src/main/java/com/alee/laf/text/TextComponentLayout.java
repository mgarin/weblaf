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

package com.alee.laf.text;

import com.alee.utils.collection.ValuesTable;

import javax.swing.plaf.TextUI;
import javax.swing.text.JTextComponent;
import java.awt.*;

/**
 * User: mgarin Date: 14.12.12 Time: 16:56
 */

public class TextComponentLayout implements LayoutManager
{
    // Positions component at the leading side of the field
    public static final String LEADING = "LEADING";
    // Positions component at the trailing side of the field
    public static final String TRAILING = "TRAILING";

    // Empty insets
    private static final Insets emptyInsets = new Insets ( 0, 0, 0, 0 );

    // Saved layout constraints
    private ValuesTable<Component, String> constraints = new ValuesTable<Component, String> ();

    // Text component
    private JTextComponent textComponent;

    public TextComponentLayout ( JTextComponent textComponent )
    {
        super ();
        this.textComponent = textComponent;
    }

    /**
     * Standard LayoutManager methods
     */

    public void addLayoutComponent ( String name, Component comp )
    {
        if ( name == null || !name.equals ( LEADING ) && !name.equals ( TRAILING ) )
        {
            throw new IllegalArgumentException ( "Cannot add to layout: constraint must be 'LEADING' or 'TRAILING' string" );
        }
        constraints.put ( comp, name );
    }

    public void removeLayoutComponent ( Component comp )
    {
        constraints.remove ( comp );
    }

    public Dimension preferredLayoutSize ( Container parent )
    {
        Insets b = getInsets ( parent );
        Dimension l = constraints.containsValue ( LEADING ) ? constraints.getKey ( LEADING ).getPreferredSize () : new Dimension ();
        Dimension t = constraints.containsValue ( TRAILING ) ? constraints.getKey ( TRAILING ).getPreferredSize () : new Dimension ();
        return new Dimension ( b.left + l.width + t.width + b.right, b.top + Math.max ( l.height, t.height ) + b.bottom );
    }

    public Dimension minimumLayoutSize ( Container parent )
    {
        return preferredLayoutSize ( parent );
    }

    public void layoutContainer ( Container parent )
    {
        boolean ltr = parent.getComponentOrientation ().isLeftToRight ();
        Insets b = getInsets ( parent );
        if ( constraints.containsValue ( LEADING ) )
        {
            Component leading = constraints.getKey ( LEADING );
            int w = leading.getPreferredSize ().width;
            if ( ltr )
            {
                leading.setBounds ( b.left - w, b.top, w, parent.getHeight () - b.top - b.bottom );
            }
            else
            {
                leading.setBounds ( parent.getWidth () - b.right, b.top, w, parent.getHeight () - b.top - b.bottom );
            }
        }
        if ( constraints.containsValue ( TRAILING ) )
        {
            Component trailing = constraints.getKey ( TRAILING );
            int w = trailing.getPreferredSize ().width;
            if ( ltr )
            {
                trailing.setBounds ( parent.getWidth () - b.right, b.top, w, parent.getHeight () - b.top - b.bottom );
            }
            else
            {
                trailing.setBounds ( b.left - w, b.top, w, parent.getHeight () - b.top - b.bottom );
            }
        }
    }

    /**
     * Actual border
     */

    private Insets getInsets ( Container parent )
    {
        Insets b = parent.getInsets ();
        Insets fm = getFieldMargin ();
        boolean ltr = parent.getComponentOrientation ().isLeftToRight ();
        return new Insets ( b.top - fm.top, b.left - ( ltr ? fm.left : fm.right ), b.bottom - fm.bottom,
                b.right - ( ltr ? fm.right : fm.left ) );
    }

    private Insets getFieldMargin ()
    {
        TextUI ui = textComponent.getUI ();
        if ( ui instanceof WebTextFieldUI )
        {
            return ( ( WebTextFieldUI ) ui ).getFieldMargin ();
        }
        else if ( ui instanceof WebPasswordFieldUI )
        {
            return ( ( WebPasswordFieldUI ) ui ).getFieldMargin ();
        }
        else
        {
            return emptyInsets;
        }
    }
}
