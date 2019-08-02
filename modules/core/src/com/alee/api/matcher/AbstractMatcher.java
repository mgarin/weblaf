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

package com.alee.api.matcher;

/**
 * Abstract {@link Matcher} providing.
 *
 * @param <T> first matched object type
 * @param <V> second matched object type
 * @author Mikle Garin
 */
public abstract class AbstractMatcher<T, V> implements Matcher
{
    @Override
    public final boolean match ( final Object first, final Object second )
    {
        return first == null && second == null || matchImpl ( ( T ) first, ( V ) second );
    }

    /**
     * Compares two objects and returns whether they match or not.
     *
     * @param first  first matched object
     * @param second second matched object
     * @return {@code true} if provided objects match, otherwise {@code false}
     */
    protected abstract boolean matchImpl ( T first, V second );
}