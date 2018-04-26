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

/**
 * This marker interface can be implemented by classes which instances can fully overwrite each other upon {@link Merge}.
 * Whether or not it should actually happen depends on the value returned by {@link #isOverwrite()} method within merge operation.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-Merge">How to use Merge</a>
 * @see Merge
 * @see Mergeable
 */
public interface Overwriting extends Mergeable
{
    /**
     * Returns whether or not this object should overwrite another one upon merge.
     *
     * @return {@code true} if this object should overwrite another one upon merge, {@code false} otherwise
     */
    public boolean isOverwrite ();
}