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

import java.util.ArrayList;
import java.util.Collection;

/**
 * Simple aggregator for convenient {@link ArrayList} creation.
 *
 * @author Mikle Garin
 */
public class ArrayListAggregator<T>
{
    /**
     * Aggregated {@link ArrayList}.
     */
    private final ArrayList<T> list;

    /**
     * Constructs new {@link ArrayListAggregator} and instantiates {@link ArrayList}.
     */
    public ArrayListAggregator ()
    {
        list = new ArrayList<T> ();
    }

    /**
     * Adds specified element into {@link ArrayList}.
     *
     * @param element element to add
     * @return this {@link ArrayListAggregator}
     */
    public ArrayListAggregator<T> add ( final T element )
    {
        list.add ( element );
        return this;
    }

    /**
     * Adds specified element into {@link ArrayList} if condition is met.
     *
     * @param condition condition to be met for element to be added
     * @param element   element to add
     * @return this {@link ArrayListAggregator}
     */
    public ArrayListAggregator<T> add ( final boolean condition, final T element )
    {
        if ( condition )
        {
            list.add ( element );
        }
        return this;
    }

    /**
     * Adds specified element into {@link ArrayList} if condition is met.
     *
     * @param condition {@link Supplier} of condition to be met for element to be added
     * @param element   element to add
     * @return this {@link ArrayListAggregator}
     */
    public ArrayListAggregator<T> add ( final Supplier<Boolean> condition, final T element )
    {
        if ( condition.get () )
        {
            list.add ( element );
        }
        return this;
    }

    /**
     * Adds all {@link Collection} elements into {@link ArrayList}.
     *
     * @param collection {@link Collection} containing elements to add
     * @return this {@link ArrayListAggregator}
     */
    public ArrayListAggregator<T> addAll ( final Collection<? extends T> collection )
    {
        list.addAll ( collection );
        return this;
    }

    /**
     * Adds all {@link Collection} elements into {@link ArrayList} if condition is met.
     *
     * @param condition  condition to be met for elements to be added
     * @param collection {@link Collection} containing elements to add
     * @return this {@link ArrayListAggregator}
     */
    public ArrayListAggregator<T> addAll ( final boolean condition, final Collection<? extends T> collection )
    {
        if ( condition )
        {
            list.addAll ( collection );
        }
        return this;
    }

    /**
     * Adds all {@link Collection} elements into {@link ArrayList} if condition is met.
     *
     * @param condition  {@link Supplier} of condition to be met for element to be added
     * @param collection {@link Collection} containing elements to add
     * @return this {@link ArrayListAggregator}
     */
    public ArrayListAggregator<T> addAll ( final Supplier<Boolean> condition, final Collection<? extends T> collection )
    {
        if ( condition.get () )
        {
            list.addAll ( collection );
        }
        return this;
    }

    /**
     * Returns aggregated {@link ArrayList}.
     *
     * @return aggregated {@link ArrayList}
     */
    public ArrayList<T> toList ()
    {
        return list;
    }
}