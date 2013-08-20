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

import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.converters.reflection.ReflectionConverter;
import com.thoughtworks.xstream.converters.reflection.ReflectionProvider;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.mapper.Mapper;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Custom converter for Map classes with String-type keys.
 *
 * @author Mikle Garin
 */

public class StringMapConverter extends ReflectionConverter
{
    /**
     * Constructs StringMapConverter with the specified mapper and reflection provider.
     *
     * @param mapper             mapper
     * @param reflectionProvider reflection provider
     */
    public StringMapConverter ( Mapper mapper, ReflectionProvider reflectionProvider )
    {
        super ( mapper, reflectionProvider );
    }

    /**
     * {@inheritDoc}
     */
    public boolean canConvert ( Class type )
    {
        // return type.isAssignableFrom ( Map.class );
        return type.equals ( HashMap.class ) || type.equals ( Hashtable.class ) || type.equals ( LinkedHashMap.class ) ||
                type.equals ( ConcurrentHashMap.class );
    }

    /**
     * {@inheritDoc}
     */
    public void marshal ( Object source, HierarchicalStreamWriter writer, MarshallingContext context )
    {
        Map<String, Object> map = ( Map<String, Object> ) source;
        for ( Map.Entry<String, Object> entry : map.entrySet () )
        {
            // Starting entry node
            String key = entry.getKey ();
            Object value = entry.getValue ();
            if ( XMLChar.isValidName ( key ) )
            {
                writer.startNode ( key );
            }
            else
            {
                writer.startNode ( "entry" );
                writer.addAttribute ( "key", key );
            }

            // Adding type reference
            writer.addAttribute ( "type", mapper.serializedClass ( value.getClass () ) );

            // Converting value
            context.convertAnother ( value );

            // Closing entry node
            writer.endNode ();
        }
    }

    /**
     * {@inheritDoc}
     */
    public Object unmarshal ( HierarchicalStreamReader reader, UnmarshallingContext context )
    {
        Map<String, Object> map = new HashMap<String, Object> ();
        while ( reader.hasMoreChildren () )
        {
            reader.moveDown ();
            String keyAttribue = reader.getAttribute ( "key" );
            String key = keyAttribue != null ? keyAttribue : reader.getNodeName ();
            Class type = mapper.realClass ( reader.getAttribute ( "type" ) );
            map.put ( key, context.convertAnother ( map, type ) );
            reader.moveUp ();
        }
        return map;
    }
}