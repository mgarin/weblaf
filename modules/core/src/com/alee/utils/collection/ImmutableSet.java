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
    @NotNull
    protected final HashSet<E> data;

    /**
     * Constructs new {@link ImmutableSet} based on the specified list data.
     *
     * @param data {@link Set} data
     */
    public ImmutableSet ( @NotNull final E... data )
    {
        this.data = CollectionUtils.asHashSet ( data );
    }

    /**
     * Constructs new {@link ImmutableSet} based on the specified {@link Set}.
     *
     * @param data {@link Set} data
     */
    public ImmutableSet ( @NotNull final Collection<? extends E> data )
    {
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
    public boolean contains ( @Nullable final Object element )
    {
        return data.contains ( element );
    }

    @NotNull
    @Override
    public Object[] toArray ()
    {
        return data.toArray ();
    }

    @NotNull
    @Override
    @SuppressWarnings ( "SuspiciousToArrayCall" )
    public <T> T[] toArray ( @NotNull final T[] array )
    {
        return data.toArray ( array );
    }

    @NotNull
    @Override
    public Iterator<E> iterator ()
    {
        return data.iterator ();
    }

    @Override
    public boolean add ( @Nullable final E element )
    {
        throw createModificationException ();
    }

    @Override
    public boolean remove ( @Nullable final Object element )
    {
        throw createModificationException ();
    }

    @Override
    public boolean containsAll ( @NotNull final Collection<?> collection )
    {
        return data.containsAll ( collection );
    }

    @Override
    public boolean addAll ( @NotNull final Collection<? extends E> collection )
    {
        throw createModificationException ();
    }

    @Override
    public boolean removeAll ( @NotNull final Collection<?> collection )
    {
        throw createModificationException ();
    }

    @Override
    public boolean retainAll ( @NotNull final Collection<?> collection )
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
    @NotNull
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
    public boolean equals ( @Nullable final Object o )
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

    @NotNull
    @Override
    public String toString ()
    {
        return getClass ().getSimpleName () + Arrays.toString ( data.toArray () );
    }
}