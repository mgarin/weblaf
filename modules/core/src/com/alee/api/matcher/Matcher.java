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

import java.io.Serializable;

/**
 * Base interface for any kinds of object matchers.
 *
 * @author Mikle Garin
 */
public interface Matcher extends Serializable
{
    /**
     * Returns whether or not this {@link Matcher} supports provided object.
     *
     * @param object object to check {@link Matcher} support for
     * @return {@code true} if this {@link Matcher} supports provided object, {@code false} otherwise
     */
    public boolean supports ( Object object );

    /**
     * Compares two objects and returns whether they match or not.
     *
     * @param first  first matched object
     * @param second second matched object
     * @return {@code true} if provided objects match, otherwise {@code false}
     */
    public boolean match ( Object first, Object second );
}