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

package com.alee.api.merge;

import com.alee.api.clone.Clone;

import java.io.Serializable;

/**
 * Merged objects cloning policy.
 * It is asked whether or not merged objects should be cloned before merge operation.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-Merge">How to use Merge</a>
 * @see Merge
 * @see Clone
 */
public interface ClonePolicy extends Serializable
{
    /**
     * Returns source object or cloned object, depending on implementation.
     *
     * @param clone  {@link Clone} algorithm
     * @param source source object to be cloned
     * @return source object or cloned object, depending on implementation
     */
    public Object clone ( Clone clone, Object source );
}