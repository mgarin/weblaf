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

import java.io.Serializable;

/**
 * Recursive merge providing some extra options for {@link GlobalMergeBehavior}s.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-Merge">How to use Merge</a>
 * @see Merge
 */
public interface RecursiveMerge extends Serializable
{
    /**
     * Returns overwrite operation resulting object.
     * This is an utility method mostly for {@link GlobalMergeBehavior} implementations.
     *
     * @param base   object to overwrite
     * @param merged overwriting object
     * @return overwrite operation resulting object
     */
    public Object overwrite ( Object base, Object merged );

    /**
     * Performs merge result of the two provided objects and returns resulting object.
     *
     * @param type   expected resulting object {@link Class} type
     * @param base   base object
     * @param merged object to merge
     * @param depth  merge calls stack depth
     * @param <T>    resulting object type
     * @return merge result of the two provided objects and returns resulting object
     */
    public <T> T merge ( Class type, Object base, Object merged, int depth );

    /**
     * Performs merge result of the two provided objects with all field values merged according to {@link Merge} settings.
     *
     * @param type   expected resulting object {@link Class} type
     * @param base   base object
     * @param merged object to merge
     * @param depth  merge calls stack depth
     * @param <T>    resulting object type
     * @return merge result of the two provided objects with all field values merged according to {@link Merge} settings
     */
    public <T> T mergeFields ( Class type, Object base, Object merged, int depth );
}