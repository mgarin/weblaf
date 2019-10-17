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

package com.alee.utils.collection;

import com.alee.api.jdk.Supplier;

import java.util.Collection;

/**
 * @param <T> value type
 * @param <C> collection type
 * @param <B> builder type
 * @author Mikle Garin
 */
public abstract class CollectionBuilder<T, C extends Collection<T>, B extends CollectionBuilder<T, C, B>>
{
    /**
     * {@link Collection} instance.
     */
    protected final C collection;

    /**
     * Constructs new {@link CollectionBuilder}.
     *
     * @param collection {@link Collection} instance
     */
    public CollectionBuilder ( final C collection )
    {
        this.collection = collection;
    }

    /**
     * Adds specified element into {@link #collection}.
     *
     * @param element element to add
     * @return this {@link CollectionBuilder}
     */
    public B add ( final T element )
    {
        this.collection.add ( element );
        return ( B ) this;
    }

    /**
     * Adds specified element into {@link #collection} if condition is met.
     *
     * @param condition condition to be met for element to be added
     * @param element   element to add
     * @return this {@link CollectionBuilder}
     */
    public B add ( final boolean condition, final T element )
    {
        if ( condition )
        {
            this.collection.add ( element );
        }
        return ( B ) this;
    }

    /**
     * Adds specified element into {@link #collection} if condition is met.
     *
     * @param condition {@link Supplier} of condition to be met for element to be added
     * @param element   element to add
     * @return this {@link CollectionBuilder}
     */
    public B add ( final Supplier<Boolean> condition, final T element )
    {
        if ( condition.get () )
        {
            this.collection.add ( element );
        }
        return ( B ) this;
    }

    /**
     * Adds all {@link Collection} elements into {@link #collection}.
     *
     * @param collection {@link Collection} containing elements to add
     * @return this {@link CollectionBuilder}
     */
    public B addAll ( final Collection<? extends T> collection )
    {
        this.collection.addAll ( collection );
        return ( B ) this;
    }

    /**
     * Adds all {@link Collection} elements into {@link #collection} if condition is met.
     *
     * @param condition  condition to be met for elements to be added
     * @param collection {@link Collection} containing elements to add
     * @return this {@link CollectionBuilder}
     */
    public B addAll ( final boolean condition, final Collection<? extends T> collection )
    {
        if ( condition )
        {
            this.collection.addAll ( collection );
        }
        return ( B ) this;
    }

    /**
     * Adds all {@link Collection} elements into {@link #collection} if condition is met.
     *
     * @param condition  {@link Supplier} of condition to be met for element to be added
     * @param collection {@link Collection} containing elements to add
     * @return this {@link CollectionBuilder}
     */
    public B addAll ( final Supplier<Boolean> condition, final Collection<? extends T> collection )
    {
        if ( condition.get () )
        {
            this.collection.addAll ( collection );
        }
        return ( B ) this;
    }

    /**
     * Returns built {@link Collection}.
     *
     * @return built {@link Collection}
     */
    public C build ()
    {
        return collection;
    }
}