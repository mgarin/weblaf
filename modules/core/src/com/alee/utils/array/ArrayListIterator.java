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

package com.alee.utils.array;

import java.util.ListIterator;
import java.util.NoSuchElementException;

/**
 * {@link ListIterator} implementation for arrays.
 *
 * @author Mikle Garin
 */
public class ArrayListIterator<E> extends ArrayIterator<E> implements ListIterator<E>
{
    /**
     * Constructs new {@link ArrayListIterator} for the specified {@code array} data.
     *
     * @param array array data
     */
    public ArrayListIterator ( final E... array )
    {
        super ( array );
    }

    @Override
    public boolean hasPrevious ()
    {
        return index > 0;
    }

    @Override
    public E previous ()
    {
        if ( index > 0 )
        {
            return array[ --index ];
        }
        else
        {
            throw new NoSuchElementException ();
        }
    }

    @Override
    public int nextIndex ()
    {
        return index < array.length ? index + 1 : array.length;
    }

    @Override
    public int previousIndex ()
    {
        return index > 0 ? index - 1 : -1;
    }

    @Override
    public void set ( final E e )
    {
        array[ index ] = e;
    }

    @Override
    public void add ( final E e )
    {
        throw new UnsupportedOperationException ( "Cannot add array elements" );
    }
}