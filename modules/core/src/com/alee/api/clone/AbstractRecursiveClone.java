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

/**
 * Abstract {@link RecursiveClone} implementation providing cloned object store.
 * It can be used to avoid clone operation from entering an infinite loop and also to preserve cloned object links.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-Clone">How to use Clone</a>
 * @see Clone
 */
public abstract class AbstractRecursiveClone implements RecursiveClone
{
    /**
     * Lazy cloned objects references map.
     * It is not always necessary for clone operation so we want to avoid always creating it.
     * It will be created on demand, mostly for objects that are deeper than one level.
     */
    private StrictHashMap<Object, Object> references;

    @Override
    public void store ( final Object object, final Object clone )
    {
        if ( references == null )
        {
            references = new StrictHashMap<Object, Object> ();
        }
        references.put ( object, clone );
    }

    @Override
    public Object retrieve ( final Object object )
    {
        return references != null ? references.get ( object ) : null;
    }
}