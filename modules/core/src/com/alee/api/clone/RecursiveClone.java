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
 * Recursive clone providing some extra options for {@link GlobalCloneBehavior}s.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-Clone">How to use Clone</a>
 * @see Clone
 */
public interface RecursiveClone extends Serializable
{
    /**
     * Stores reference to object clone.
     *
     * @param object object
     * @param clone  object clone
     */
    public void store ( Object object, Object clone );

    /**
     * Returns object clone if it is already available, {@code null} otherwise.
     *
     * @param object object to retrieve cached clone for
     * @return object clone if it is already available, {@code null} otherwise
     */
    public Object retrieve ( Object object );

    /**
     * Returns clone of the specified object.
     *
     * @param object object to clone
     * @param depth  clone calls stack depth
     * @param <T>    cloned object type
     * @return clone of the specified object
     */
    public <T> T clone ( T object, int depth );

    /**
     * Returns clone of the specified object with all field values cloned according to {@link Clone} settings.
     *
     * @param object object to clone
     * @param depth  clone calls stack depth
     * @param <T>    cloned object type
     * @return clone of the specified object with all field values cloned according to {@link Clone} settings
     */
    public <T> T cloneFields ( T object, int depth );
}