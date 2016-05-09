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

package com.alee.api;

/**
 * This interface is an indicator of the fact that this class instances can be merged with each other.
 * Result of this merge will be one of those instances with values merged with values from another instance.
 *
 * @param <T> object type
 * @author Mikle Garin
 */

public interface Mergeable<T extends Mergeable>
{
    /**
     * Performs merge of this object with another object of the same type.
     * Returns this object as a result of the performed merge.
     *
     * @param object object to merge into this one
     * @return this object as a result of the performed merge
     */
    public T merge ( T object );
}