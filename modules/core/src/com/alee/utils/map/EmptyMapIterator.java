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

package com.alee.utils.map;

import com.alee.utils.collection.EmptyIterator;

import java.util.NoSuchElementException;

/**
 * {@link MapIterator} implementation that has no elements.
 *
 * @param <K> the type of keys maintained by this map
 * @param <V> the type of mapped values
 * @author Mikle Garin
 */
public final class EmptyMapIterator<K, V> implements MapIterator<K, V>
{
    /**
     * {@link EmptyIterator} singleton instance.
     */
    private static EmptyMapIterator instance;

    /**
     * Returns {@link EmptyIterator} instance.
     *
     * @param <K> the type of keys maintained by this map
     * @param <V> the type of mapped values
     * @return {@link EmptyIterator} instance
     */
    public static <K, V> EmptyMapIterator<K, V> instance ()
    {
        if ( instance == null )
        {
            synchronized ( EmptyIterator.class )
            {
                if ( instance == null )
                {
                    instance = new EmptyMapIterator ();
                }
            }
        }
        return instance;
    }

    /**
     * We do not want anyone to construct {@link EmptyMapIterator} directly, use {@link #instance()} instead.
     */
    private EmptyMapIterator ()
    {
        super ();
    }

    @Override
    public boolean hasNext ()
    {
        return false;
    }

    @Override
    public K next ()
    {
        throw new NoSuchElementException ( "Iterator contains no elements" );
    }

    @Override
    public void remove ()
    {
        throw new IllegalStateException ( "Iterator contains no elements" );
    }

    @Override
    public K getKey ()
    {
        throw new IllegalStateException ( "Iterator contains no elements" );
    }

    @Override
    public V getValue ()
    {
        throw new IllegalStateException ( "Iterator contains no elements" );
    }

    @Override
    public V setValue ( final V value )
    {
        throw new IllegalStateException ( "Iterator contains no elements" );
    }
}