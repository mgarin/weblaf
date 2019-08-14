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

import java.util.Enumeration;
import java.util.NoSuchElementException;

/**
 * {@link Enumeration} implementation that has no elements.
 *
 * @param <E> element type
 * @author Mikle Garin
 */
public final class EmptyEnumeration<E> implements Enumeration<E>
{
    /**
     * {@link EmptyEnumeration} singleton instance.
     */
    @Nullable
    private static Enumeration instance;

    /**
     * Returns {@link EmptyEnumeration} instance.
     *
     * @param <E> elements type
     * @return {@link EmptyEnumeration} instance
     */
    @NotNull
    public static synchronized <E> Enumeration<E> instance ()
    {
        if ( instance == null )
        {
            instance = new EmptyEnumeration ();
        }
        return instance;
    }

    /**
     * We do not want anyone to construct {@link EmptyEnumeration} directly, use {@link #instance()} instead.
     */
    private EmptyEnumeration ()
    {
        super ();
    }

    @Override
    public boolean hasMoreElements ()
    {
        return false;
    }

    @Nullable
    @Override
    public E nextElement ()
    {
        throw new NoSuchElementException ( "No more elements" );
    }
}