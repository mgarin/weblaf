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

import com.alee.api.jdk.Objects;
import com.alee.utils.CollectionUtils;

import java.io.Serializable;
import java.util.*;

/**
 * {@link Set} implementation that doesn't allow any data modifications to be done.
 * Unlike {@link java.util.Collections#unmodifiableSet(Set)} this implementation keeps set data copy.
 * If you need to provide an unmodifiable reference for your set use {@link java.util.Collections} implementation.
 *
 * @param <E> data type
 * @author Mikle Garin
 */
public class ImmutableSet<E> implements Set<E>, Cloneable, Serializable
{
    /**
     * {@link Set} data.
     */
    protected final HashSet<E> data;

    /**
     * Constructs new {@link ImmutableSet} based on the specified list data.
     *
     * @param data {@link Set} data
     */
    public ImmutableSet ( final E... data )
    {
        if ( data == null )
        {
            throw new NullPointerException ( "ImmutableSet data must not be null" );
        }
        this.data = CollectionUtils.asHashSet ( data );
    }

    /**
     * Constructs new {@link ImmutableSet} based on the specified {@link Set}.
     *
     * @param data {@link Set} data
     */
    public ImmutableSet ( final Collection<? extends E> data )
    {
        if ( data == null )
        {
            throw new NullPointerException ( "ImmutableSet data must not be null" );
        }
        this.data = new HashSet<E> ( data );
    }

    @Override
    public int size ()
    {
        return data.size ();
    }

    @Override
    public boolean isEmpty ()
    {
        return data.isEmpty ();
    }

    @Override
    public boolean contains ( final Object element )
    {
        return data.contains ( element );
    }

    @Override
    public Object[] toArray ()
    {
        return data.toArray ();
    }

    @SuppressWarnings ( "SuspiciousToArrayCall" )
    @Override
    public <T> T[] toArray ( final T[] array )
    {
        return data.toArray ( array );
    }

    @Override
    public Iterator<E> iterator ()
    {
        return data.iterator ();
    }

    @Override
    public boolean add ( final E element )
    {
        throw createModificationException ();
    }

    @Override
    public boolean remove ( final Object element )
    {
        throw createModificationException ();
    }

    @Override
    public boolean containsAll ( final Collection<?> collection )
    {
        return data.containsAll ( collection );
    }

    @Override
    public boolean addAll ( final Collection<? extends E> collection )
    {
        throw createModificationException ();
    }

    @Override
    public boolean removeAll ( final Collection<?> collection )
    {
        throw createModificationException ();
    }

    @Override
    public boolean retainAll ( final Collection<?> collection )
    {
        throw createModificationException ();
    }

    @Override
    public void clear ()
    {
        throw createModificationException ();
    }

    /**
     * Returns new {@link UnsupportedOperationException} instance specific for this implementation.
     *
     * @return new {@link UnsupportedOperationException} instance specific for this implementation
     */
    protected UnsupportedOperationException createModificationException ()
    {
        return new UnsupportedOperationException ( "ImmutableSet is unmodifiable" );
    }

    @Override
    public int hashCode ()
    {
        return Objects.hash ( data.toArray () );
    }

    @Override
    public boolean equals ( final Object o )
    {
        if ( o == this )
        {
            return true;
        }
        else if ( o instanceof ImmutableSet )
        {
            final ImmutableSet other = ( ImmutableSet ) o;
            return CollectionUtils.equals ( data, other.data );
        }
        else
        {
            return false;
        }
    }

    @Override
    public String toString ()
    {
        return getClass ().getSimpleName () + Arrays.toString ( data.toArray () );
    }
}