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

import com.alee.api.annotations.NotNull;
import com.alee.extended.layout.AbstractLayoutManager;

import java.awt.*;

/**
 * Special layout used within text field components to place additional leading and trailing components inside.
 *
 * @author Mikle Garin
 */
public class TextFieldLayout extends AbstractLayoutManager
{
    /**
     * Text field painter implementation.
     */
    private final IAbstractTextFieldPainter painter;

    /**
     * Constructs new text field layout.
     *
     * @param painter text field painter implementation
     */
    public TextFieldLayout ( final IAbstractTextFieldPainter painter )
    {
        super ();
        this.painter = painter;
    }

    @NotNull
    @Override
    public Dimension preferredLayoutSize ( @NotNull final Container container )
    {
        final Insets b = container.getInsets ();
        final Component leading = painter.getLeadingComponent ();
        final Component trailing = painter.getTrailingComponent ();
        final Dimension l = leading != null ? leading.getPreferredSize () : new Dimension ();
        final Dimension t = trailing != null ? trailing.getPreferredSize () : new Dimension ();
        return new Dimension ( b.left + l.width + t.width + b.right, b.top + Math.max ( l.height, t.height ) + b.bottom );
    }

    @Override
    public void layoutContainer ( @NotNull final Container container )
    {
        final Insets b = container.getInsets ();
        final boolean ltr = container.getComponentOrientation ().isLeftToRight ();
        final Component leading = painter.getLeadingComponent ();
        final Component trailing = painter.getTrailingComponent ();
        if ( leading != null )
        {
            final int w = leading.getPreferredSize ().width;
            if ( ltr )
            {
                leading.setBounds ( b.left - w, b.top, w, container.getHeight () - b.top - b.bottom );
            }
            else
            {
                leading.setBounds ( container.getWidth () - b.right, b.top, w, container.getHeight () - b.top - b.bottom );
            }
        }
        if ( trailing != null )
        {
            final int w = trailing.getPreferredSize ().width;
            if ( ltr )
            {
                trailing.setBounds ( container.getWidth () - b.right, b.top, w, container.getHeight () - b.top - b.bottom );
            }
            else
            {
                trailing.setBounds ( b.left - w, b.top, w, container.getHeight () - b.top - b.bottom );
            }
        }
    }
}