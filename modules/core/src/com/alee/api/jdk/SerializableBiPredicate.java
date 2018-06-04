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

package com.alee.api.jdk;

import java.io.Serializable;

/**
 * {@link BiPredicate} marked as {@link Serializable}.
 *
 * @param <T> first input argument type
 * @param <U> second input argument type
 * @author Mikle Garin
 * @see Predicate
 */
public interface SerializableBiPredicate<T, U> extends BiPredicate<T, U>, Serializable
{
    /**
     * This interface doesn't offer any additional methods to implement.
     */
}