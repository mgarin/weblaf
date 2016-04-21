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

package com.alee.utils.xml;

import java.util.HashMap;
import java.util.Map;

/**
 * Custom {@link java.util.HashMap} extension used to provide {@link com.thoughtworks.xstream.XStream} marshalling context data.
 * It can be passed instead of root object to put custom key-value pairs into marshalling context.
 *
 * @author Mikle Garin
 * @see com.alee.utils.XmlUtils
 */

public class XStreamContext<K, V> extends HashMap<K, V>
{
    /**
     * Constructs new empty context.
     */
    public XStreamContext ()
    {
        super ();
    }

    /**
     * Constructs new context based on another map.
     *
     * @param m map with data
     */
    public XStreamContext ( final Map<? extends K, ? extends V> m )
    {
        super ( m );
    }

    /**
     * Constructs new context with single key-value pair.
     *
     * @param key   context key
     * @param value context value
     */
    public XStreamContext ( final K key, final V value )
    {
        super ();
        put ( key, value );
    }
}