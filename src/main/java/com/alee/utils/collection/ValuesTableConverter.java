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

package com.alee.utils.collection;

import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.converters.reflection.ReflectionConverter;
import com.thoughtworks.xstream.converters.reflection.ReflectionProvider;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.mapper.Mapper;

/**
 * Special XStream converter for ValuesTable object.
 *
 * @author Mikle Garin
 * @since 1.4
 */

public class ValuesTableConverter extends ReflectionConverter
{
    /**
     * Constructs new ValuesTableConverter.
     *
     * @param mapper
     * @param reflectionProvider
     */
    public ValuesTableConverter ( Mapper mapper, ReflectionProvider reflectionProvider )
    {
        super ( mapper, reflectionProvider );
    }

    /**
     * Determines whether the converter can marshall a particular type.
     *
     * @param type the Class representing the object type to be converted
     * @return true if this class type can be converted using this converter, false otherwise
     */
    public boolean canConvert ( Class type )
    {
        return ValuesTable.class.getCanonicalName ().equals ( type.getCanonicalName () );
    }

    /**
     * Convert an object to textual data.
     *
     * @param source  object to be marshalled
     * @param writer  stream to write to
     * @param context context that allows nested objects to be processed by XStream
     */
    public void marshal ( Object source, HierarchicalStreamWriter writer, MarshallingContext context )
    {
        ValuesTable valuesTable = ( ValuesTable ) source;
        for ( int i = 0; i < valuesTable.size (); i++ )
        {
            // Converting key
            Object key = valuesTable.getKey ( i );
            writer.startNode ( mapper.serializedClass ( key.getClass () ) );
            context.convertAnother ( key );
            writer.endNode ();

            // Converting value
            Object value = valuesTable.getValue ( i );
            writer.startNode ( mapper.serializedClass ( value.getClass () ) );
            context.convertAnother ( value );
            writer.endNode ();
        }
    }

    /**
     * Convert textual data back into an object.
     *
     * @param reader  stream to read the text from
     * @param context unmarshalling context
     * @return resulting object
     */
    public Object unmarshal ( HierarchicalStreamReader reader, UnmarshallingContext context )
    {
        ValuesTable valuesTable = new ValuesTable ();
        int row = 0;
        Object key = null;
        while ( reader.hasMoreChildren () )
        {
            reader.moveDown ();
            Class c = mapper.realClass ( reader.getNodeName () );
            if ( row % 2 == 0 )
            {
                // Reading key
                key = context.convertAnother ( valuesTable, c );
            }
            else
            {
                // Reading value and adding a new table record
                valuesTable.put ( key, context.convertAnother ( valuesTable, c ) );
            }
            reader.moveUp ();
            row++;
        }
        return valuesTable;
    }
}