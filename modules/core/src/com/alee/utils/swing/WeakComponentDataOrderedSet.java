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
import com.alee.api.jdk.BiConsumer;
import com.alee.api.jdk.BiPredicate;
import com.alee.utils.CollectionUtils;

import javax.swing.*;
import java.util.LinkedHashSet;

/**
 * Extension over {@link WeakComponentData} that provides convenience methods for {@link LinkedHashSet}-type data usage.
 * Note that {@link LinkedHashSet}s kept within the {@link JComponent} would never have {@code null} values.
 *
 * @param <C> {@link JComponent} type
 * @param <E> {@link LinkedHashSet} data type
 * @author Mikle Garin
 */
public class WeakComponentDataOrderedSet<C extends JComponent, E> extends WeakComponentData<C, LinkedHashSet<E>>
{
    /**
     * Constructs new {@link WeakComponentDataOrderedSet}.
     *
     * @param key             key used to place data list within {@link JComponent}
     * @param initialCapacity initial capacity for the {@link java.util.Set} of {@link JComponent}s
     */
    public WeakComponentDataOrderedSet ( @NotNull final String key, final int initialCapacity )
    {
        super ( key, initialCapacity );
    }

    /**
     * Returns size of data list stored within {@link JComponent}.
     *
     * @param component {@link JComponent} to check data in
     * @return size of data list stored within {@link JComponent}
     */
    public synchronized int size ( @NotNull final C component )
    {
        final LinkedHashSet<E> set = get ( component );
        return set != null ? set.size () : 0;
    }

    /**
     * Returns whether or not {@link JComponent} has any data of this kind stored within.
     *
     * @param component {@link JComponent} to check data in
     * @return {@code true} if {@link JComponent} has any data of this kind stored within, {@code false} otherwise
     */
    public synchronized boolean containsData ( @NotNull final C component )
    {
        return CollectionUtils.notEmpty ( get ( component ) );
    }

    /**
     * Returns whether or not {@link JComponent} has specified data stored within.
     *
     * @param component {@link JComponent} to check data in
     * @param data      data to find
     * @return {@code true} if {@link JComponent} has specified data stored within, {@code false} otherwise
     */
    public synchronized boolean containsData ( @NotNull final C component, @NotNull final E data )
    {
        final LinkedHashSet<E> set = get ( component );
        return set != null && set.contains ( data );
    }

    /**
     * Stores data for the specified {@link JComponent}.
     *
     * @param component {@link JComponent} to store data in
     * @param data      data to store
     */
    public synchronized void add ( @NotNull final C component, @NotNull final E data )
    {
        LinkedHashSet<E> set = get ( component );
        if ( set == null )
        {
            set = new LinkedHashSet<E> ( 1 );
            set ( component, set );
        }
        set.add ( data );
    }

    /**
     * Removes data from the specified {@link JComponent}.
     *
     * @param component {@link JComponent} to remove data from
     * @param data      data to remove
     */
    public synchronized void remove ( @NotNull final C component, @NotNull final E data )
    {
        final LinkedHashSet<E> set = get ( component );
        if ( set != null && set.remove ( data ) )
        {
            if ( set.size () == 0 )
            {
                clear ( component );
            }
        }
    }

    /**
     * Removes data from the specified {@link JComponent}.
     *
     * @param component           {@link JComponent} to remove data from
     * @param data                data to remove
     * @param removedDataConsumer {@link BiConsumer} for removed data
     */
    public synchronized void remove ( @NotNull final C component, @NotNull final E data,
                                      @NotNull final BiConsumer<C, E> removedDataConsumer )
    {
        final LinkedHashSet<E> set = get ( component );
        if ( set != null && set.remove ( data ) )
        {
            removedDataConsumer.accept ( component, data );
            if ( set.size () == 0 )
            {
                clear ( component );
            }
        }
    }

    /**
     * Provides every stored data piece into specified {@link BiConsumer}.
     *
     * @param consumer {@link BiConsumer} for {@link JComponent} and data
     */
    public synchronized void forEachData ( @NotNull final BiConsumer<C, E> consumer )
    {
        for ( final C component : components () )
        {
            forEachData ( component, consumer );
        }
    }

    /**
     * Provides data pieces stored for specified {@link JComponent} into {@link BiConsumer}.
     *
     * @param component {@link JComponent} to provide data pieces for
     * @param consumer  {@link BiConsumer} for {@link JComponent} and data
     */
    public synchronized void forEachData ( @NotNull final C component, @NotNull final BiConsumer<C, E> consumer )
    {
        final LinkedHashSet<E> set = get ( component );
        if ( set != null )
        {
            for ( final E data : set )
            {
                consumer.accept ( component, data );
            }
        }
    }

    /**
     * Returns whether or not at least one of the stored data pieces is accepted by specified {@link BiPredicate}.
     *
     * @param predicate {@link BiPredicate} for {@link JComponent} and data
     * @return {@code true} if at least one of the data pieces is accepted by specified {@link BiPredicate}, {@code false} otherwise
     */
    public synchronized boolean anyDataMatch ( @NotNull final BiPredicate<C, E> predicate )
    {
        boolean anyDataMatch = false;
        for ( final C component : components () )
        {
            if ( anyDataMatch ( component, predicate ) )
            {
                anyDataMatch = true;
                break;
            }
        }
        return anyDataMatch;
    }

    /**
     * Returns whether or not at least one of the data pieces stored for specified {@link JComponent} is accepted by {@link BiPredicate}.
     *
     * @param component {@link JComponent} to match data pieces for
     * @param predicate {@link BiPredicate} for {@link JComponent} and data
     * @return {@code true} if at least one of the data pieces is accepted by {@link BiPredicate}, {@code false} otherwise
     */
    public synchronized boolean anyDataMatch ( @NotNull final C component, @NotNull final BiPredicate<C, E> predicate )
    {
        boolean anyDataMatch = false;
        final LinkedHashSet<E> set = get ( component );
        if ( set != null )
        {
            for ( final E data : set )
            {
                if ( predicate.test ( component, data ) )
                {
                    anyDataMatch = true;
                    break;
                }
            }
        }
        return anyDataMatch;
    }

    /**
     * Returns whether or not all of the stored data pieces are accepted by specified {@link BiPredicate}.
     *
     * @param predicate {@link BiPredicate} for {@link JComponent} and data
     * @return {@code true} if all of the data pieces are accepted by specified {@link BiPredicate}, {@code false} otherwise
     */
    public synchronized boolean allDataMatch ( @NotNull final BiPredicate<C, E> predicate )
    {
        boolean allDataMatch = true;
        for ( final C component : components () )
        {
            if ( !allDataMatch ( component, predicate ) )
            {
                allDataMatch = false;
                break;
            }
        }
        return allDataMatch;
    }

    /**
     * Returns whether or not all of the data pieces stored for specified {@link JComponent} are accepted by {@link BiPredicate}.
     *
     * @param component {@link JComponent} to match data pieces for
     * @param predicate {@link BiPredicate} for {@link JComponent} and data
     * @return {@code true} if all of the data pieces are accepted by {@link BiPredicate}, {@code false} otherwise
     */
    public synchronized boolean allDataMatch ( @NotNull final C component, @NotNull final BiPredicate<C, E> predicate )
    {
        boolean allDataMatch = true;
        final LinkedHashSet<E> set = get ( component );
        if ( set != null )
        {
            for ( final E data : set )
            {
                if ( !predicate.test ( component, data ) )
                {
                    allDataMatch = false;
                    break;
                }
            }
        }
        return allDataMatch;
    }

    /**
     * Returns whether or not none of the stored data pieces are accepted by specified {@link BiPredicate}.
     *
     * @param predicate {@link BiPredicate} for {@link JComponent} and data
     * @return {@code true} if none of the data pieces are accepted by specified {@link BiPredicate}, {@code false} otherwise
     */
    public synchronized boolean noneDataMatch ( @NotNull final BiPredicate<C, E> predicate )
    {
        boolean noneDataMatch = true;
        for ( final C component : components () )
        {
            if ( !noneDataMatch ( component, predicate ) )
            {
                noneDataMatch = false;
                break;
            }
        }
        return noneDataMatch;
    }

    /**
     * Returns whether or not none of the data pieces stored for specified {@link JComponent} are accepted by {@link BiPredicate}.
     *
     * @param component {@link JComponent} to match data pieces for
     * @param predicate {@link BiPredicate} for {@link JComponent} and data
     * @return {@code true} if none of the data pieces are accepted by specified {@link BiPredicate}, {@code false} otherwise
     */
    public synchronized boolean noneDataMatch ( @NotNull final C component, @NotNull final BiPredicate<C, E> predicate )
    {
        boolean noneDataMatch = true;
        final LinkedHashSet<E> set = get ( component );
        if ( set != null )
        {
            for ( final E data : set )
            {
                if ( predicate.test ( component, data ) )
                {
                    noneDataMatch = false;
                    break;
                }
            }
        }
        return noneDataMatch;
    }
}