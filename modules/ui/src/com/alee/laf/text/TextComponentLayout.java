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

import com.alee.extended.layout.AbstractLayoutManager;
import com.alee.utils.collection.ValuesTable;
import com.alee.utils.laf.PaddingSupport;

import javax.swing.plaf.TextUI;
import javax.swing.text.JTextComponent;
import java.awt.*;

/**
 * User: mgarin Date: 14.12.12 Time: 16:56
 */

public class TextComponentLayout extends AbstractLayoutManager
{
    // Positions component at the leading side of the field
    public static final String LEADING = "LEADING";
    // Positions component at the trailing side of the field
    public static final String TRAILING = "TRAILING";

    // Empty insets
    private static final Insets emptyInsets = new Insets ( 0, 0, 0, 0 );

    // todo Make weak references
    // Saved layout constraints
    private final ValuesTable<Component, String> constraints = new ValuesTable<Component, String> ();

    // Text component
    private final JTextComponent textComponent;

    public TextComponentLayout ( final JTextComponent textComponent )
    {
        super ();
        this.textComponent = textComponent;
    }

    /**
     * Standard LayoutManager methods
     */

    /**
     * {@inheritDoc}
     */
    @Override
    public void addComponent ( final Component component, final Object constraints )
    {
        final String value = ( String ) constraints;
        if ( value == null || !value.equals ( LEADING ) && !value.equals ( TRAILING ) )
        {
            throw new IllegalArgumentException ( "Cannot add to layout: constraint must be 'LEADING' or 'TRAILING' string" );
        }
        this.constraints.put ( component, value );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeComponent ( final Component component )
    {
        this.constraints.remove ( component );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Dimension preferredLayoutSize ( final Container parent )
    {
        final Insets b = getInsets ( parent );
        final Dimension l = constraints.containsValue ( LEADING ) ? constraints.getKey ( LEADING ).getPreferredSize () : new Dimension ();
        final Dimension t = constraints.containsValue ( TRAILING ) ? constraints.getKey ( TRAILING ).getPreferredSize () : new Dimension ();
        return new Dimension ( b.left + l.width + t.width + b.right, b.top + Math.max ( l.height, t.height ) + b.bottom );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void layoutContainer ( final Container parent )
    {
        final boolean ltr = parent.getComponentOrientation ().isLeftToRight ();
        final Insets b = getInsets ( parent );
        if ( constraints.containsValue ( LEADING ) )
        {
            final Component leading = constraints.getKey ( LEADING );
            final int w = leading.getPreferredSize ().width;
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
            final Component trailing = constraints.getKey ( TRAILING );
            final int w = trailing.getPreferredSize ().width;
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

    private Insets getInsets ( final Container parent )
    {
        final Insets b = parent.getInsets ();
        final Insets fm = getFieldMargin ();
        final boolean ltr = parent.getComponentOrientation ().isLeftToRight ();
        return new Insets ( b.top - fm.top, b.left - ( ltr ? fm.left : fm.right ), b.bottom - fm.bottom,
                b.right - ( ltr ? fm.right : fm.left ) );
    }

    private Insets getFieldMargin ()
    {
        final TextUI ui = textComponent.getUI ();
        if ( ui instanceof PaddingSupport )
        {
            return ( ( PaddingSupport ) ui ).getPadding ();
        }
        else
        {
            return emptyInsets;
        }
    }
}