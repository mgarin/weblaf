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

import com.alee.api.annotations.NotNull;
import com.alee.api.annotations.Nullable;

import java.util.*;

/**
 * Custom {@link Set} implementation that keeps only {@link java.lang.ref.WeakReference}s to all of its objects.
 * {@link WeakHashSet} is a simple decorator that is backed by {@link WeakHashMap} implementation.
 *
 * @param <E> elements type
 * @author Mikle Garin
 */
public class WeakHashSet<E> extends AbstractSet<E>
{
    /**
     * {@link WeakHashMap} that backs this {@link WeakHashSet}.
     */
    @NotNull
    protected final WeakHashMap<E, Boolean> map;

    /**
     * {@link Set} of keys.
     */
    @NotNull
    protected final transient Set<E> keySet;

    /**
     * Constructs new {@link WeakHashSet} with default initial capacity.
     */
    public WeakHashSet ()
    {
        this ( 10 );
    }

    /**
     * Constructs new {@link WeakHashSet} with specified initial capacity.
     *
     * @param initialCapacity initial capacity
     */
    public WeakHashSet ( final int initialCapacity )
    {
        map = new WeakHashMap<E, Boolean> ( initialCapacity );
        keySet = map.keySet ();
    }

    @Override
    public void clear ()
    {
        map.clear ();
    }

    @Override
    public int size ()
    {
        return map.size ();
    }

    @Override
    public boolean isEmpty ()
    {
        return map.isEmpty ();
    }

    @Override
    public boolean contains ( @Nullable final Object o )
    {
        return map.containsKey ( o );
    }

    @Override
    public boolean remove ( @Nullable final Object o )
    {
        return map.remove ( o ) != null;
    }

    @Override
    public boolean add ( @Nullable final E e )
    {
        return map.put ( e, Boolean.TRUE ) == null;
    }

    @NotNull
    @Override
    public Iterator<E> iterator ()
    {
        return keySet.iterator ();
    }

    @NotNull
    @Override
    public Object[] toArray ()
    {
        return keySet.toArray ();
    }

    @NotNull
    @Override
    @SuppressWarnings ( "SuspiciousToArrayCall" )
    public <T> T[] toArray ( @NotNull final T[] a )
    {
        return keySet.toArray ( a );
    }

    @Override
    public boolean containsAll ( @NotNull final Collection<?> c )
    {
        return keySet.containsAll ( c );
    }

    @Override
    public boolean removeAll ( @NotNull final Collection<?> c )
    {
        return keySet.removeAll ( c );
    }

    @Override
    public boolean retainAll ( @NotNull final Collection<?> c )
    {
        return keySet.retainAll ( c );
    }

    @Override
    public int hashCode ()
    {
        return keySet.hashCode ();
    }

    @Override
    public boolean equals ( @Nullable final Object o )
    {
        return o == this || o instanceof Set && keySet.equals ( o );
    }

    @NotNull
    @Override
    public String toString ()
    {
        return keySet.toString ();
    }
}