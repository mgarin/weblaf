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

package com.alee.api.clone;

/**
 * Object clone behavior.
 *
 * @param <T> cloned object type
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-Clone">How to use Clone</a>
 * @see Clone
 */
public interface CloneBehavior<T> extends Cloneable
{
    /**
     * Returns cloned instance of the object implementing {@link CloneBehavior}.
     *
     * @param clone {@link RecursiveClone} algorithm
     * @param depth clone calls stack depth
     * @return cloned instance of the object implementing {@link CloneBehavior}
     */
    public T clone ( RecursiveClone clone, int depth );
}