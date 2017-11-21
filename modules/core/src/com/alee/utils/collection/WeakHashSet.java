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
    protected WeakHashMap<E, Boolean> map;

    /**
     * {@link Set} of keys.
     */
    protected transient Set<E> keySet;

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
        super ();
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
    public boolean contains ( final Object o )
    {
        return map.containsKey ( o );
    }

    @Override
    public boolean remove ( final Object o )
    {
        return map.remove ( o ) != null;
    }

    @Override
    public boolean add ( final E e )
    {
        return map.put ( e, Boolean.TRUE ) == null;
    }

    @Override
    public Iterator<E> iterator ()
    {
        return keySet.iterator ();
    }

    @Override
    public Object[] toArray ()
    {
        return keySet.toArray ();
    }

    @Override
    public <T> T[] toArray ( final T[] a )
    {
        //noinspection SuspiciousToArrayCall
        return keySet.toArray ( a );
    }

    @Override
    public String toString ()
    {
        return keySet.toString ();
    }

    @Override
    public int hashCode ()
    {
        return keySet.hashCode ();
    }

    @Override
    public boolean equals ( final Object o )
    {
        return o == this || o instanceof Set && keySet.equals ( o );
    }

    @Override
    public boolean containsAll ( final Collection<?> c )
    {
        return keySet.containsAll ( c );
    }

    @Override
    public boolean removeAll ( final Collection<?> c )
    {
        return keySet.removeAll ( c );
    }

    @Override
    public boolean retainAll ( final Collection<?> c )
    {
        return keySet.retainAll ( c );
    }
}