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

import com.alee.utils.map.StrictHashMap;

import java.io.Serializable;
import java.util.Map;

/**
 * Recursive clone behavior providing some extra options for {@link GlobalCloneBehavior}s.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-Clone">How to use Clone</a>
 * @see Clone
 */
public abstract class RecursiveClone implements Serializable
{
    /**
     * Lazy cloned objects references map.
     */
    private StrictHashMap<Object, Object> references;

    /**
     * Stores reference to object clone.
     *
     * @param object object
     * @param clone  object clone
     */
    public void store ( final Object object, final Object clone )
    {
        if ( references == null )
        {
            references = new StrictHashMap<Object, Object> ();
        }
        references.put ( object, clone );
    }

    /**
     * Returns object clone if it is already available, {@code null} otherwise.
     *
     * @param object object to retrieve cached clone for
     * @return object clone if it is already available, {@code null} otherwise
     */
    public Object retrieve ( final Object object )
    {
        return references != null ? references.get ( object ) : null;
    }

    /**
     * Returns clone of the specified object.
     * {@link Map} of all cloned objects is passed for any subsequent clone operations
     *
     * @param object object to clone
     * @param depth  clone calls stack depth
     * @return clone of the specified object
     */
    public abstract <T> T clone ( T object, int depth );
}