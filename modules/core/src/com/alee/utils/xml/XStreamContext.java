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

import com.alee.utils.MapUtils;
import com.thoughtworks.xstream.core.MapBackedDataHolder;

import java.util.Map;

/**
 * Custom {@link com.thoughtworks.xstream.converters.DataHolder} used to provide XStream marshalling context data.
 *
 * @author Mikle Garin
 * @see com.alee.utils.XmlUtils
 * @see com.thoughtworks.xstream.converters.DataHolder
 */
public class XStreamContext extends MapBackedDataHolder
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
     * @param data map with data
     */
    public XStreamContext ( final Map data )
    {
        super ( MapUtils.newHashMap ( data ) );
    }

    /**
     * Constructs new context with single key-value pair.
     *
     * @param key   context key
     * @param value context value
     */
    public XStreamContext ( final Object key, final Object value )
    {
        super ( MapUtils.newHashMap ( key, value ) );
    }
}