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

package com.alee.api.clone.behavior;

import com.alee.api.clone.Clone;
import com.alee.api.clone.CloneException;
import com.alee.api.clone.GlobalCloneBehavior;
import com.alee.api.clone.RecursiveClone;
import com.alee.utils.ReflectUtils;
import com.alee.utils.map.ImmutableMap;
import com.alee.utils.map.SoftHashMap;

import java.util.*;

/**
 * {@link GlobalCloneBehavior} for {@link Map}s.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-Clone">How to use Clone</a>
 * @see Clone
 */
public class MapCloneBehavior implements GlobalCloneBehavior<Map>
{
    @Override
    public boolean supports ( final RecursiveClone clone, final Object object )
    {
        return object instanceof Map;
    }

    @Override
    public Map clone ( final RecursiveClone clone, final Map map, final int depth )
    {
        try
        {
            /**
             * Creating new {@link Map} instance.
             * All most commonly used {@link Map} types are processed separately.
             */
            final Map mapCopy;
            final Class<? extends Map> mapClass = map.getClass ();
            if ( mapClass == ImmutableMap.class ||
                    mapClass == HashMap.class )
            {
                // HashMap instance
                mapCopy = new HashMap ( map.size () );
            }
            else if ( mapClass == WeakHashMap.class )
            {
                // WeakHashMap instance
                mapCopy = new WeakHashMap ( map.size () );
            }
            else if ( mapClass == SoftHashMap.class )
            {
                // SoftHashMap instance
                mapCopy = new SoftHashMap ();
            }
            else if ( mapClass == LinkedHashMap.class )
            {
                // LinkedHashMap instance
                mapCopy = new LinkedHashMap ( map.size () );
            }
            else if ( mapClass == Properties.class )
            {
                // Properties instance
                mapCopy = new Properties ();
            }
            else if ( mapClass == Hashtable.class )
            {
                // Hashtable instance
                mapCopy = new Hashtable ( map.size () );
            }
            else if ( mapClass == TreeMap.class )
            {
                // TreeMap instance
                mapCopy = new TreeMap ();
            }
            else
            {
                // Attempting to create other map type instance
                mapCopy = ReflectUtils.createInstance ( mapClass );
            }

            /**
             * Cloning {@link Map} values.
             */
            if ( !map.isEmpty () )
            {
                // Storing map copy
                // todo This is a wrong reference for ImmutableMap
                clone.store ( map, mapCopy );

                // Cloning all values
                for ( final Object e : map.entrySet () )
                {
                    final Map.Entry entry = ( Map.Entry ) e;
                    final Object key = entry.getKey ();
                    final Object keyCopy = clone.clone ( key, depth + 1 );
                    final Object value = entry.getValue ();
                    final Object valueCopy = clone.clone ( value, depth + 1 );
                    mapCopy.put ( keyCopy, valueCopy );
                }
            }

            /**
             * Returning resulting {@link Map} copy.
             */
            final Map result;
            if ( map instanceof ImmutableMap )
            {
                result = new ImmutableMap ( mapCopy );
            }
            else
            {
                result = mapCopy;
            }
            return result;
        }
        catch ( final Exception e )
        {
            throw new CloneException ( "Unable to clone map: " + map, e );
        }
    }

    @Override
    public boolean isStorable ()
    {
        return true;
    }
}