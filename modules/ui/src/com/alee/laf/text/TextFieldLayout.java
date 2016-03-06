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
import com.alee.utils.CompareUtils;

import java.awt.*;

/**
 * Special layout used within text field components to place additional leading and trailing components inside.
 *
 * @author Mikle Garin
 */

public class TextFieldLayout extends AbstractLayoutManager
{
    /**
     * Positions component at the leading side of the field.
     */
    public static final String LEADING = "LEADING";

    /**
     * Positions component at the trailing side of the field
     */
    public static final String TRAILING = "TRAILING";

    /**
     * Leading layout component.
     */
    private Component leading;

    /**
     * Trailing layout component.
     */
    private Component trailing;

    @Override
    public void addComponent ( final Component component, final Object constraints )
    {
        if ( CompareUtils.equals ( constraints, LEADING ) )
        {
            leading = component;
        }
        else if ( CompareUtils.equals ( constraints, TRAILING ) )
        {
            trailing = component;
        }
        else
        {
            final String msg = "Component cannot be added to layout: constraint must be either of '%s' or '%s' string value";
            throw new IllegalArgumentException ( String.format ( msg, LEADING, TRAILING ) );
        }
    }

    @Override
    public void removeComponent ( final Component component )
    {
        if ( leading == component )
        {
            leading = null;
        }
        else if ( trailing == component )
        {
            trailing = null;
        }
    }

    @Override
    public Dimension preferredLayoutSize ( final Container parent )
    {
        final Insets b = parent.getInsets ();
        final Dimension l = leading != null ? leading.getPreferredSize () : new Dimension ();
        final Dimension t = trailing != null ? trailing.getPreferredSize () : new Dimension ();
        return new Dimension ( b.left + l.width + t.width + b.right, b.top + Math.max ( l.height, t.height ) + b.bottom );
    }

    @Override
    public void layoutContainer ( final Container parent )
    {
        final Insets b = parent.getInsets ();
        final boolean ltr = parent.getComponentOrientation ().isLeftToRight ();
        if ( leading != null )
        {
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
        if ( trailing != null )
        {
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
}