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

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * {@link Iterator} implementation for arrays.
 *
 * @param <E> data type
 * @author Mikle Garin
 */

public class ArrayIterator<E> implements Iterator<E>
{
    /**
     * Array data.
     */
    protected final E[] array;

    /**
     * Iteration index.
     */
    protected int index;

    /**
     * Constructs new {@link ArrayIterator} for the specified {@code array} data.
     *
     * @param array array data
     */
    public ArrayIterator ( final E... array )
    {
        super ();
        this.array = array;
        this.index = 0;
    }

    @Override
    public boolean hasNext ()
    {
        return index < array.length;
    }

    @Override
    public E next ()
    {
        if ( index < array.length )
        {
            return array[ index++ ];
        }
        else
        {
            throw new NoSuchElementException ();
        }
    }

    @Override
    public void remove ()
    {
        throw new UnsupportedOperationException ( "Cannot remove array elements" );
    }
}