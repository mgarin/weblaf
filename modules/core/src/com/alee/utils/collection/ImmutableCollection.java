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
import com.alee.utils.ArrayUtils;
import com.alee.utils.array.ArrayIterator;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

/**
 * {@link Collection} implementation that doesn't allow any data modifications to be done.
 * Unlike {@link java.util.Collections#unmodifiableCollection(Collection)} this implementation keeps collection data copy.
 * If you need to provide an unmodifiable reference for your collection use {@link java.util.Collections} implementation.
 *
 * @param <E> data type
 * @author Mikle Garin
 */
public class ImmutableCollection<E> implements Collection<E>, Cloneable, Serializable
{
    /**
     * {@link Collection} data.
     */
    @NotNull
    @SuppressWarnings ( "NonSerializableFieldInSerializableClass" )
    protected final E[] data;

    /**
     * Constructs new {@link ImmutableCollection} based on the specified collection data.
     *
     * @param data collection data
     */
    public ImmutableCollection ( @NotNull final E... data )
    {
        this.data = data;
    }

    /**
     * Constructs new {@link ImmutableCollection} based on the specified {@link Collection}.
     *
     * @param collection {@link Collection}
     */
    public ImmutableCollection ( @NotNull final Collection<? extends E> collection )
    {
        this.data = ( E[] ) collection.toArray ();
    }

    @Override
    public int size ()
    {
        return data.length;
    }

    @Override
    public boolean isEmpty ()
    {
        return data.length == 0;
    }

    @Override
    public boolean contains ( @Nullable final Object element )
    {
        return ArrayUtils.contains ( element, data );
    }

    @NotNull
    @Override
    public Object[] toArray ()
    {
        return data;
    }

    @NotNull
    @Override
    @SuppressWarnings ( "SuspiciousSystemArraycopy" )
    public <T> T[] toArray ( final T[] array )
    {
        final int size = size ();
        if ( array.length < size () )
        {
            return ( T[] ) Arrays.copyOf ( data, size, data.getClass () );
        }
        else
        {
            System.arraycopy ( data, 0, array, 0, size );
            if ( array.length > size )
            {
                array[ size ] = null;
            }
            return array;
        }
    }

    @NotNull
    @Override
    public Iterator<E> iterator ()
    {
        return new ArrayIterator ( data )
        {
            @Override
            public void remove ()
            {
                throw createModificationException ();
            }
        };
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
        for ( final Object element : collection )
        {
            if ( !ArrayUtils.contains ( element, data ) )
            {
                return false;
            }
        }
        return true;
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
        return new UnsupportedOperationException ( "ImmutableCollection is unmodifiable" );
    }

    @Override
    public int hashCode ()
    {
        return Objects.hash ( ( Object[] ) data );
    }

    @Override
    public boolean equals ( @Nullable final Object o )
    {
        if ( o == this )
        {
            return true;
        }
        else if ( o instanceof ImmutableCollection )
        {
            final ImmutableCollection other = ( ImmutableCollection ) o;
            return Arrays.equals ( data, other.data );
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
        return getClass ().getSimpleName () + Arrays.toString ( data );
    }
}