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
 * Global objects merge behavior.
 * It is asked to perform merge if it {@link #supports(RecursiveMerge, Class, Object, Object)} provided objects.
 *
 * @param <O> base object type
 * @param <M> merged object type
 * @param <R> resulting object type
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-Merge">How to use Merge</a>
 * @see Merge
 */
public interface GlobalMergeBehavior<O, M, R> extends Serializable
{
    /**
     * Returns whether or not this behavior supports specified objects merge.
     *
     * @param merge  {@link RecursiveMerge} algorithm
     * @param type   expected resulting object {@link Class} type
     * @param base   base object, should never be {@code null}
     * @param merged object to merge, should never be {@code null}
     * @return {@code true} if this behavior supports specified objects merge, {@code false} otherwise
     */
    public boolean supports ( RecursiveMerge merge, Class<R> type, Object base, Object merged );

    /**
     * Performs merge of the two provided objects and returns resulting object.
     * Depending on the case it might be one of the two provided objects or their merge result.
     *
     * @param merge  {@link RecursiveMerge} algorithm
     * @param type   expected resulting object {@link Class} type
     * @param base   base object, should never be {@code null}
     * @param merged object to merge, should never be {@code null}
     * @param depth  merge calls stack depth
     * @return merge result
     */
    public R merge ( RecursiveMerge merge, Class type, O base, M merged, int depth );
}