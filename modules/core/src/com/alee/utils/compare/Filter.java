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

package com.alee.utils.compare;

/**
 * This interface provides a base for filtering any type of objects in any situation.
 * This class is similar to FileFilter from default file chooser, but it doesn't require any specific object type like File.
 *
 * @author Mikle Garin
 */

public interface Filter<E>
{
    /**
     * Returns whether the specified object is accepted by this filter or not.
     *
     * @param object object to process
     * @return true if the specified object is accepted by this filter, false otherwise
     */
    public boolean accept ( E object );
}