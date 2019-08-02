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

import java.io.Serializable;

/**
 * Global objects clone behavior.
 * It is asked to perform clone if it {@link #supports(RecursiveClone, Object)} provided objects.
 *
 * @param <O> cloned object type
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-Clone">How to use Clone</a>
 * @see Clone
 */
public interface GlobalCloneBehavior<O> extends Serializable
{
    /**
     * Returns whether or not this behavior supports cloning of the specified object.
     *
     * @param clone  {@link RecursiveClone} algorithm
     * @param object object to clone, should never be {@code null}
     * @return {@code true} if this behavior supports cloning of the specified specified object, {@code false} otherwise
     */
    public boolean supports ( RecursiveClone clone, Object object );

    /**
     * Returns clone of the specified object.
     *
     * @param clone  {@link RecursiveClone} algorithm
     * @param object object to clone, should never be {@code null}
     * @param depth  clone calls stack depth
     * @return clone of the specified object
     */
    public O clone ( RecursiveClone clone, O object, int depth );

    /**
     * Return whether or not values cloned by this behavior are storable for recursive clone operation.
     *
     * @return {@code true} if values cloned by this behavior are storable for recursive clone operation, {@code false} otherwise
     */
    public boolean isStorable ();
}