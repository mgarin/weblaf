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

import java.util.List;
import java.util.Set;

/**
 * {@link Set} implementation that doesn't allow any data modifications to be done.
 * Unlike {@link java.util.Collections#unmodifiableSet(Set)} this implementation keeps set data copy.
 * If you need to provide an unmodifiable reference for your set use {@link java.util.Collections} implementations or write your own.
 *
 * @param <E> data type
 * @author Mikle Garin
 */
public class ImmutableSet<E> extends ImmutableCollection<E> implements Set<E>
{
    /**
     * Constructs new {@link ImmutableSet} based on the specified list data.
     *
     * @param data list data
     */
    public ImmutableSet ( final E... data )
    {
        super ( data );
    }

    /**
     * Constructs new {@link ImmutableSet} based on the specified {@link Set}.
     *
     * @param set {@link Set}
     */
    public ImmutableSet ( final Set<? extends E> set )
    {
        super ( set );
    }

    /**
     * Constructs new {@link ImmutableSet} based on the specified {@link List}.
     *
     * @param list {@link List}
     */
    public ImmutableSet ( final List<? extends E> list )
    {
        super ( list );
    }

    @Override
    protected UnsupportedOperationException createModificationException ()
    {
        return new UnsupportedOperationException ( "Set is unmodifiable" );
    }

    @Override
    protected ImmutableSet<E> clone ()
    {
        return new ImmutableSet<E> ( this );
    }
}