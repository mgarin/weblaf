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
import com.alee.api.jdk.BiConsumer;
import com.alee.api.jdk.BiFunction;
import com.alee.api.jdk.BiPredicate;
import com.alee.api.jdk.Function;
import com.alee.utils.collection.ImmutableSet;
import com.alee.utils.collection.WeakHashSet;

import javax.swing.*;
import java.util.Set;

/**
 * This is a special wrapper for any {@link JComponent}-related data that have to be stored using weak references. The only hard reference
 * to that data will be stored within {@link JComponent} itself, so the data will only get removed along with the {@link JComponent} itself
 * or if removal calls are made explicitly.
 *
 * This class also keeps a {@link Set} of weak references to all components that have data of this kind stored within them. It can be used
 * to iterate through all of those components whenever it is needed.
 *
 * Note that stored data cannot be {@code null} at any point as {@code null} is considered to be no data case.
 *
 * @param <C> {@link JComponent} type
 * @param <D> data type
 * @author Mikle Garin
 */
public class WeakComponentData<C extends JComponent, D>
{
    /**
     * Key used to place data within {@link JComponent} using {@link JComponent#putClientProperty(Object, Object)} method.
     * Make sure that every manager uses its own unique keys for registering different data to avoid unwanted overwrites.
     */
    @NotNull
    protected final String key;

    /**
     * {@link Set} of {@link JComponent}s that have data of this kind stored within them.
     * {@link WeakHashSet} implementation is used to avoid hard component references that could cause memory leaks.
     */
    @NotNull
    protected final Set<C> components;

    /**
     * Constructs new {@link WeakComponentData}.
     *
     * @param key             key used to place data within {@link JComponent}
     * @param initialCapacity initial capacity for the {@link Set} of {@link JComponent}s
     */
    public WeakComponentData ( @NotNull final String key, final int initialCapacity )
    {
        super ();
        this.key = key;
        this.components = new WeakHashSet<C> ( initialCapacity );
    }

    /**
     * Returns {@link Set} of {@link JComponent}s that have data of this kind stored within them.
     *
     * @return {@link Set} of {@link JComponent}s that have data of this kind stored within them
     */
    @NotNull
    public synchronized Set<C> components ()
    {
        return new ImmutableSet<C> ( components );
    }

    /**
     * Returns size of the {@link Set} of {@link JComponent}s that have data of this kind stored within them.
     *
     * @return size of the {@link Set} of {@link JComponent}s that have data of this kind stored within them
     */
    public synchronized int size ()
    {
        return components.size ();
    }

    /**
     * Returns whether or not specified {@link JComponent} has data of this kind stored within.
     *
     * @param component {@link JComponent} to check data in
     * @return {@code true} if specified {@link JComponent} has data of this kind stored within, {@code false} otherwise
     */
    public synchronized boolean contains ( @NotNull final C component )
    {
        return components.contains ( component );
    }

    /**
     * Returns data stored in the {@link JComponent}.
     * Could return {@code null} if no data can be found.
     *
     * @param component {@link JComponent} to retrieve data from
     * @return data stored in the {@link JComponent}
     */
    @Nullable
    public synchronized D get ( @NotNull final C component )
    {
        return ( D ) component.getClientProperty ( key );
    }

    /**
     * Returns data stored in the {@link JComponent}.
     * If no data can be found it will be created using mapping {@link Function}, stored within {@link JComponent} and returned as result.
     *
     * @param component   {@link JComponent} to retrieve data from
     * @param defaultData {@link Function} that creates default data in case it is missing
     * @return data stored in the {@link JComponent}
     */
    @NotNull
    public synchronized D get ( @NotNull final C component, @NotNull final Function<C, D> defaultData )
    {
        // Trying to retrieve existing data
        D data = get ( component );
        if ( data == null )
        {
            // Requesting new data
            data = defaultData.apply ( component );
            set ( component, data );
        }
        return data;
    }

    /**
     * Modifies data stored in the {@link JComponent} using specified {@link BiFunction} and returns it.
     * If no data can be found it will be created using mapping {@link Function}, stored within {@link JComponent} and returned as result.
     *
     * @param component    {@link JComponent} to retrieve and modify data from
     * @param modifiedData {@link BiFunction} that modifies data in case it exists
     * @param defaultData  {@link Function} that creates default data in case it is missing
     * @return modified data from the {@link JComponent}
     */
    @NotNull
    public synchronized D modify ( @NotNull final C component, @NotNull final BiFunction<C, D, D> modifiedData,
                                   @NotNull final Function<C, D> defaultData )
    {
        // Trying to retrieve existing data
        D data = get ( component );
        if ( data == null )
        {
            // Requesting new data
            data = defaultData.apply ( component );
            set ( component, data );
        }
        else
        {
            // Modifying existing data
            data = modifiedData.apply ( component, data );
            set ( component, data );
        }
        return data;
    }

    /**
     * Stores data in the specified {@link JComponent}.
     * Providing {@code null} data would result in calling {@link #clear(JComponent)} method instead.
     *
     * @param component {@link JComponent} to store data in
     * @param data      data to store
     * @return old data
     */
    @Nullable
    public synchronized D set ( @NotNull final C component, @Nullable final D data )
    {
        // Saving old data
        final D oldData = get ( component );

        // Modifying data
        if ( data != null )
        {
            // Saving new data
            component.putClientProperty ( key, data );
            components.add ( component );
        }
        else
        {
            // Clearing data instead if its null
            clear ( component );
        }

        // Returning old data
        return oldData;
    }

    /**
     * Stores data in the specified {@link JComponent}.
     * Providing {@code null} data would result in calling {@link #clear(JComponent, BiConsumer)} method instead.
     *
     * @param component       {@link JComponent} to store data in
     * @param data            data to store
     * @param oldDataConsumer {@link BiConsumer} for previous data
     * @return old data
     */
    @Nullable
    public synchronized D set ( @NotNull final C component, @Nullable final D data, @NotNull final BiConsumer<C, D> oldDataConsumer )
    {
        // Processing old data
        final D oldData;
        if ( contains ( component ) )
        {
            oldData = get ( component );
            oldDataConsumer.accept ( component, get ( component ) );
        }
        else
        {
            oldData = null;
        }

        // Modifying data
        if ( data != null )
        {

            // Saving new data
            set ( component, data );
        }
        else
        {
            // Clearing data instead if its null
            clear ( component, oldDataConsumer );
        }

        // Returning old data
        return oldData;
    }

    /**
     * Clears data stored in the {@link JComponent}.
     *
     * @param component {@link JComponent} to clear stored data for
     * @return old data
     */
    @Nullable
    public synchronized D clear ( @NotNull final C component )
    {
        // Saving old data
        final D oldData = get ( component );

        // Clearing data
        component.putClientProperty ( key, null );
        components.remove ( component );

        // Returning old data
        return oldData;
    }

    /**
     * Clears data stored in the {@link JComponent}.
     *
     * @param component           {@link JComponent} to clear stored data for
     * @param removedDataConsumer {@link BiConsumer} for removed data
     * @return old data
     */
    @Nullable
    public synchronized D clear ( @NotNull final C component, @NotNull final BiConsumer<C, D> removedDataConsumer )
    {
        final D oldData;
        if ( contains ( component ) )
        {
            // Saving old data
            oldData = get ( component );

            // Processing data to be removed
            removedDataConsumer.accept ( component, oldData );

            // Clearing data
            clear ( component );
        }
        else
        {
            // No old data
            oldData = null;
        }
        return oldData;
    }

    /**
     * Provides each stored data piece into specified {@link BiConsumer}.
     *
     * @param consumer {@link BiConsumer} for {@link JComponent} and data
     */
    public synchronized void forEach ( @NotNull final BiConsumer<C, D> consumer )
    {
        for ( final C component : components () )
        {
            // Retrieving data from the component
            final D data = get ( component );

            // Consuming data
            consumer.accept ( component, data );
        }
    }

    /**
     * Returns whether or not at least one of the stored data pieces is accepted by specified {@link BiPredicate}.
     *
     * @param predicate {@link BiPredicate} for {@link JComponent} and data
     * @return {@code true} if at least one of the stored data pieces is accepted by specified {@link BiPredicate}, {@code false} otherwise
     */
    public synchronized boolean anyMatch ( @NotNull final BiPredicate<C, D> predicate )
    {
        boolean anyMatch = false;
        for ( final C component : components () )
        {
            if ( predicate.test ( component, get ( component ) ) )
            {
                anyMatch = true;
                break;
            }
        }
        return anyMatch;
    }

    /**
     * Returns whether or not all of the stored data pieces are accepted by specified {@link BiPredicate}.
     *
     * @param predicate {@link BiPredicate} for {@link JComponent} and data
     * @return {@code true} if all of the stored data pieces are accepted by specified {@link BiPredicate}, {@code false} otherwise
     */
    public synchronized boolean allMatch ( @NotNull final BiPredicate<C, D> predicate )
    {
        boolean allMatch = true;
        for ( final C component : components () )
        {
            if ( !predicate.test ( component, get ( component ) ) )
            {
                allMatch = false;
                break;
            }
        }
        return allMatch;
    }

    /**
     * Returns whether or not none of the stored data pieces are accepted by specified {@link BiPredicate}.
     *
     * @param predicate {@link BiPredicate} for {@link JComponent} and data
     * @return {@code true} if none of the stored data pieces are accepted by specified {@link BiPredicate}, {@code false} otherwise
     */
    public synchronized boolean noneMatch ( @NotNull final BiPredicate<C, D> predicate )
    {
        boolean noneMatch = true;
        for ( final C component : components () )
        {
            if ( predicate.test ( component, get ( component ) ) )
            {
                noneMatch = false;
                break;
            }
        }
        return noneMatch;
    }
}