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
 * Single object type merge behavior.
 * If you want to simply indicate that your object instances can be merged implement {@link Mergeable} interface instead.
 *
 * @param <T> merged objects type
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-Merge">How to use Merge</a>
 * @see Merge
 * @see Mergeable
 */
public interface MergeBehavior<T extends MergeBehavior<T>> extends Mergeable
{
    /**
     * Returns object as a result of merging another {@code object} on top of this one.
     *
     * @param merge  {@link RecursiveMerge} algorithm
     * @param type   expected resulting object {@link Class} type
     * @param object object to merge into this one, it might also be {@code null}
     * @param depth  merge calls stack depth
     * @return object as a result of merging another {@code object} on top of this one
     */
    public T merge ( RecursiveMerge merge, Class type, T object, int depth );
}