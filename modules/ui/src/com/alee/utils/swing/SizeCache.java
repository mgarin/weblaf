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

package com.alee.utils.swing;

import com.alee.api.annotations.NotNull;
import com.alee.api.annotations.Nullable;

import java.awt.*;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * Utility that caches {@link Component} sizes for as long as this instance is used.
 * It can optimize performance for multiple requests to same {@link Component} sizes.
 *
 * @author Mikle Garin
 */
public final class SizeCache
{
    /**
     * Size caches.
     * Uses {@link WeakHashMap} to avoid hard {@link Component} references.
     * If {@link Component} is GCd at any point - it's {@link Sizes} will simply be forgotten without any extra actions.
     */
    private final Map<Component, Sizes> sizes;

    /**
     * Constructs new {@link SizeCache}.
     *
     * @param container {@link Container} for initial capacity calculation
     */
    public SizeCache ( @NotNull final Container container )
    {
        this ( container.getComponentCount () );
    }

    /**
     * Constructs new {@link SizeCache}.
     *
     * @param initialCapacity initial cache capacity
     */
    public SizeCache ( final int initialCapacity )
    {
        sizes = new WeakHashMap<Component, Sizes> ( initialCapacity );
    }

    /**
     * Returns {@link Component} preferred size.
     * Note that returned value might have been cached and might not contain actual current preferred size.
     *
     * @param component {@link Component} to retrieve preferred size for
     * @return {@link Component} preferred size
     */
    @NotNull
    public Dimension preferred ( @NotNull final Component component )
    {
        return getSizes ( component ).preferred ( component );
    }

    /**
     * Returns {@link Container}'s child {@link Component} preferred size.
     * Note that returned value might have been cached and might not contain actual current preferred size.
     *
     * @param container {@link Container} to retrieve {@link Component} from
     * @param index     {@link Component} index in {@link Container}
     * @return {@link Container}'s child {@link Component} preferred size
     */
    @NotNull
    public Dimension preferred ( @NotNull final Container container, final int index )
    {
        final Component component = container.getComponent ( index );
        return getSizes ( component ).preferred ( component );
    }

    /**
     * Returns {@link Component} minimum size.
     * Note that returned value might have been cached and might not contain actual current minimum size.
     *
     * @param component {@link Component} to retrieve minimum size for
     * @return {@link Component} minimum size
     */
    @NotNull
    public Dimension minimum ( @NotNull final Component component )
    {
        return getSizes ( component ).minimum ( component );
    }

    /**
     * Returns {@link Container}'s child {@link Component} minimum size.
     * Note that returned value might have been cached and might not contain actual current minimum size.
     *
     * @param container {@link Container} to retrieve {@link Component} from
     * @param index     {@link Component} index in {@link Container}
     * @return {@link Container}'s child {@link Component} minimum size
     */
    @NotNull
    public Dimension minimum ( @NotNull final Container container, final int index )
    {
        final Component component = container.getComponent ( index );
        return getSizes ( component ).minimum ( component );
    }

    /**
     * Returns {@link Component} maximum size.
     * Note that returned value might have been cached and might not contain actual current maximum size.
     *
     * @param component {@link Component} to retrieve maximum size for
     * @return {@link Component} maximum size
     */
    @NotNull
    public Dimension maximum ( @NotNull final Component component )
    {
        return getSizes ( component ).maximum ( component );
    }

    /**
     * Returns {@link Container}'s child {@link Component} maximum size.
     * Note that returned value might have been cached and might not contain actual current maximum size.
     *
     * @param container {@link Container} to retrieve {@link Component} from
     * @param index     {@link Component} index in {@link Container}
     * @return {@link Container}'s child {@link Component} maximum size
     */
    @NotNull
    public Dimension maximum ( @NotNull final Container container, final int index )
    {
        final Component component = container.getComponent ( index );
        return getSizes ( component ).maximum ( component );
    }

    /**
     * Clears size caches for the specified {@link Component}
     *
     * @param component {@link Component} to clear size caches for
     */
    public void clear ( @NotNull final Component component )
    {
        final Sizes sizes = this.sizes.get ( component );
        if ( sizes != null )
        {
            sizes.preferred = null;
            sizes.minimum = null;
            sizes.maximum = null;
        }
    }

    /**
     * Returns {@link Sizes} for the specified {@link Component}.
     *
     * @param component {@link Component} to retrieve {@link Sizes} for
     * @return {@link Sizes} for the specified {@link Component}
     */
    @NotNull
    private Sizes getSizes ( @NotNull final Component component )
    {
        Sizes sizes = this.sizes.get ( component );
        if ( sizes == null )
        {
            sizes = new Sizes ();
            this.sizes.put ( component, sizes );
        }
        return sizes;
    }

    /**
     * Simple object that contains {@link Component} sizes.
     * It is only used within {@link SizeCache} for convenience and shouldn't be accessed from outside.
     */
    private static final class Sizes
    {
        /**
         * {@link Component} preferred size.
         * Might be {@code null} if haven't been requested yet.
         */
        @Nullable
        private Dimension preferred;

        /**
         * {@link Component} minimum size.
         * Might be {@code null} if haven't been requested yet.
         */
        @Nullable
        private Dimension minimum;

        /**
         * {@link Component} maximum size.
         * Might be {@code null} if haven't been requested yet.
         */
        @Nullable
        private Dimension maximum;

        /**
         * Returns {@link Component} preferred size.
         * Note that returned value might have been cached and might not contain actual current preferred size.
         *
         * @param component {@link Component} to retrieve preferred size for
         * @return {@link Component} preferred size
         */
        @NotNull
        public Dimension preferred ( @NotNull final Component component )
        {
            if ( preferred == null )
            {
                preferred = component.getPreferredSize ();
            }
            return preferred;
        }

        /**
         * Returns {@link Component} minimum size.
         * Note that returned value might have been cached and might not contain actual current minimum size.
         *
         * @param component {@link Component} to retrieve minimum size for
         * @return {@link Component} minimum size
         */
        @NotNull
        public Dimension minimum ( @NotNull final Component component )
        {
            if ( minimum == null )
            {
                minimum = component.getMinimumSize ();
            }
            return minimum;
        }

        /**
         * Returns {@link Component} maximum size.
         * Note that returned value might have been cached and might not contain actual current maximum size.
         *
         * @param component {@link Component} to retrieve maximum size for
         * @return {@link Component} maximum size
         */
        @NotNull
        public Dimension maximum ( @NotNull final Component component )
        {
            if ( maximum == null )
            {
                maximum = component.getMaximumSize ();
            }
            return maximum;
        }
    }
}