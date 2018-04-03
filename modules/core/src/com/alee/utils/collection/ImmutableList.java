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

import com.alee.utils.ArrayUtils;
import com.alee.utils.array.ArrayListIterator;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.ListIterator;

/**
 * {@link List} implementation that doesn't allow any data modifications to be done.
 * Unlike {@link java.util.Collections#unmodifiableList(List)} this implementation keeps list data copy.
 * If you need to provide an unmodifiable reference for your list use {@link java.util.Collections} implementations or write your own.
 *
 * @param <E> data type
 * @author Mikle Garin
 */
public class ImmutableList<E> extends ImmutableCollection<E> implements List<E>, Cloneable, Serializable
{
    /**
     * Constructs new {@link ImmutableList} based on the specified list data.
     *
     * @param data data array
     */
    public ImmutableList ( final E... data )
    {
        super ( data );
    }

    /**
     * Constructs new {@link ImmutableList} based on the specified {@link Collection}.
     *
     * @param collection data {@link Collection}
     */
    public ImmutableList ( final Collection<? extends E> collection )
    {
        super ( collection );
    }

    @Override
    public E get ( final int index )
    {
        return data[ index ];
    }

    @Override
    public E set ( final int index, final E element )
    {
        throw new UnsupportedOperationException ();
    }

    @Override
    public void add ( final int index, final E element )
    {
        throw new UnsupportedOperationException ();
    }

    @Override
    public E remove ( final int index )
    {
        throw new UnsupportedOperationException ();
    }

    @Override
    public int indexOf ( final Object o )
    {
        return ArrayUtils.indexOf ( o, data );
    }

    @Override
    public int lastIndexOf ( final Object o )
    {
        return ArrayUtils.lastIndexOf ( o, data );
    }

    @Override
    public boolean addAll ( final int index, final Collection<? extends E> c )
    {
        throw new UnsupportedOperationException ();
    }

    @Override
    public ListIterator<E> listIterator ()
    {
        return listIterator ( 0 );
    }

    @Override
    public ListIterator<E> listIterator ( final int index )
    {
        return new ArrayListIterator<E> ( data )
        {
            @Override
            public void set ( final E e )
            {
                throw createModificationException ();
            }

            @Override
            public void add ( final E e )
            {
                throw createModificationException ();
            }
        };
    }

    @Override
    public List<E> subList ( final int fromIndex, final int toIndex )
    {
        return new ImmutableList<E> ( Arrays.copyOfRange ( data, fromIndex, toIndex ) );
    }

    @Override
    protected UnsupportedOperationException createModificationException ()
    {
        return new UnsupportedOperationException ( "List is unmodifiable" );
    }

    @Override
    protected ImmutableList<E> clone ()
    {
        return new ImmutableList<E> ( this );
    }
}