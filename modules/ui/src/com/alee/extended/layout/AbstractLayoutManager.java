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

import com.alee.api.annotations.NotNull;
import com.alee.api.annotations.Nullable;

import java.awt.*;

/**
 * Abstract {@link LayoutManager2} implementation that hides some less frequently used methods.
 * It also unifies different underlying component addition and removal methods.
 *
 * todo 1. Add generic for {@link Container} type and implement it correctly in all extending layouts
 *
 * @author Mikle Garin
 */
public abstract class AbstractLayoutManager implements LayoutManager2
{
    @Override
    public final void addLayoutComponent ( @NotNull final Component component, @Nullable final Object constraints )
    {
        addComponent ( component, constraints );
    }

    @Override
    public final void addLayoutComponent ( @Nullable final String constraints, @NotNull final Component component )
    {
        addComponent ( component, constraints );
    }

    @Override
    public final void removeLayoutComponent ( @NotNull final Component component )
    {
        removeComponent ( component );
    }

    /**
     * Called when component added into container with this layout.
     *
     * @param component   added component
     * @param constraints component constraints
     */
    public void addComponent ( @NotNull final Component component, @Nullable final Object constraints )
    {
        /**
         * Do nothing by default.
         */
    }

    /**
     * Called when component removed from container with this layout.
     *
     * @param component removed component
     */
    public void removeComponent ( @NotNull final Component component )
    {
        /**
         * Do nothing by default.
         */
    }

    /**
     * This method is called on layout that is being set by the styling system.
     * It can be used to migrate settings from the old layout into the new one.
     * Usually it comes down to constraints of already added components if there are any.
     * If layout doesn't use any constraints (which is often the case for UI layouts) then this method implementation might not be needed.
     *
     * @param container {@link Container} this layout will be set into
     * @param oldLayout {@link LayoutManager} that {@link Container} currently has
     */
    public void migrate ( @NotNull final Container container, @Nullable final LayoutManager oldLayout )
    {
        /**
         * Do nothing by default.
         * Override this to provide your own migration strategy.
         */
    }

    @Override
    public abstract void layoutContainer ( @NotNull Container parent );

    @NotNull
    @Override
    public abstract Dimension preferredLayoutSize ( @NotNull Container parent );

    @NotNull
    @Override
    public Dimension minimumLayoutSize ( @NotNull final Container container )
    {
        return preferredLayoutSize ( container );
    }

    @NotNull
    @Override
    public Dimension maximumLayoutSize ( @NotNull final Container container )
    {
        return new Dimension ( Integer.MAX_VALUE, Integer.MAX_VALUE );
    }

    @Override
    public float getLayoutAlignmentX ( @NotNull final Container container )
    {
        return 0.5f;
    }

    @Override
    public float getLayoutAlignmentY ( @NotNull final Container container )
    {
        return 0.5f;
    }

    @Override
    public void invalidateLayout ( @NotNull final Container container )
    {
        /**
         * Do nothing by default.
         */
    }
}