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

package com.alee.extended.lazy;

import com.alee.api.annotations.NotNull;
import com.alee.api.annotations.Nullable;
import com.alee.extended.layout.AbstractLayoutManager;

import java.awt.*;

/**
 * Custom {@link LayoutManager} for {@link LazyPanel}.
 * It removes any child {@link Component} previously added upon addition of new {@link Component}.
 *
 * @author Mikle Garin
 */
public class LazyPanelLayout extends AbstractLayoutManager
{
    @Override
    public void addComponent ( @NotNull final Component component, @Nullable final Object constraints )
    {
        final Container container = component.getParent ();
        if ( container != null && container.getComponentCount () > 1 )
        {
            for ( int i = container.getComponentCount () - 1; i >= 0; i-- )
            {
                final Component other = container.getComponent ( i );
                if ( other != component )
                {
                    container.remove ( i );
                }
            }
        }
    }

    @Override
    public void layoutContainer ( @NotNull final Container parent )
    {
        if ( parent.getComponentCount () > 0 )
        {
            final int w = parent.getWidth ();
            final int h = parent.getHeight ();
            final Insets insets = parent.getInsets ();
            final Component component = parent.getComponent ( 0 );
            component.setBounds ( insets.left, insets.top, w - insets.left - insets.right, h - insets.top - insets.bottom );
        }
    }

    @NotNull
    @Override
    public Dimension preferredLayoutSize ( @NotNull final Container parent )
    {
        final Dimension ps = new Dimension ( 0, 0 );

        final Insets insets = parent.getInsets ();
        ps.width += insets.left + insets.right;
        ps.height += insets.top + insets.bottom;

        if ( parent.getComponentCount () > 0 )
        {
            final Dimension cps = parent.getComponent ( 0 ).getPreferredSize ();
            ps.width += cps.width;
            ps.height += cps.height;
        }

        return ps;
    }
}