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

package com.alee.laf.tabbedpane;

import com.alee.api.annotations.NotNull;
import com.alee.api.annotations.Nullable;
import com.alee.managers.tooltip.AbstractComponentArea;
import com.alee.managers.tooltip.ComponentArea;

import javax.swing.*;
import javax.swing.plaf.TabbedPaneUI;
import java.awt.*;

/**
 * {@link ComponentArea} implementation describing {@link JTabbedPane} tab area.
 *
 * @param <V> tab value type
 * @param <C> component type
 * @author Mikle Garin
 */
public class TabbedPaneTabArea<V, C extends JTabbedPane> extends AbstractComponentArea<V, C>
{
    /**
     * Tab index.
     */
    protected final int tab;

    /**
     * Constructs new {@link TabbedPaneTabArea}.
     *
     * @param tab tab index
     */
    public TabbedPaneTabArea ( final int tab )
    {
        this.tab = tab;
    }

    /**
     * Returns tab index.
     *
     * @return tab index
     */
    public int tab ()
    {
        return tab;
    }

    @Override
    public boolean isAvailable ( @NotNull final C component )
    {
        return 0 <= tab && tab < component.getTabCount ();
    }

    @Nullable
    @Override
    public Rectangle getBounds ( @NotNull final C component )
    {
        // Calculating tab bounds
        final Rectangle bounds = component.getBoundsAt ( tab );

        // Adjusting tooltip location
        if ( bounds != null )
        {
            final TabbedPaneUI ui = component.getUI ();
            if ( ui instanceof WTabbedPaneUI )
            {
                final Tab tab = ( ( WTabbedPaneUI ) ui ).getTab ( this.tab );
                if ( tab != null )
                {
                    adjustBounds ( component, tab, bounds );
                }
            }
        }

        return bounds;
    }

    @Nullable
    @Override
    public V getValue ( @NotNull final C component )
    {
        return ( V ) component.getComponentAt ( tab );
    }

    @Override
    protected void adjustBounds ( @NotNull final C component, @NotNull final Component content, @NotNull final Rectangle bounds )
    {
        if ( content instanceof Tab )
        {
            /**
             * Adjusting tab title content bounds.
             */
            final Tab tab = ( Tab ) content;
            if ( tab.getComponent () == null )
            {
                final int align = tab.getHorizontalAlignment ();
                adjustBounds ( component, tab, bounds, align );
            }
            else
            {
                final Component tabComponent = tab.getComponent ();
                final Rectangle tabComponentBounds = tabComponent.getBounds ();
                tabComponentBounds.x += bounds.x;
                tabComponentBounds.y += bounds.y;
                adjustBounds ( component, tabComponent, tabComponentBounds );
            }
        }
        else
        {
            /**
             * Adjusting other types.
             */
            super.adjustBounds ( component, content, bounds );
        }
    }

    @Override
    public boolean equals ( final Object other )
    {
        return other instanceof TabbedPaneTabArea &&
                this.tab == ( ( TabbedPaneTabArea ) other ).tab;
    }
}