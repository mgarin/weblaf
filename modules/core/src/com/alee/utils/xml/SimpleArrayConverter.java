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

import com.alee.utils.TextUtils;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.converters.collections.AbstractCollectionConverter;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.mapper.Mapper;

import java.awt.*;
import java.lang.reflect.Array;
import java.util.List;

/**
 * Custom converter for arrays with simple values.
 *
 * @author Mikle Garin
 */

public class SimpleArrayConverter extends AbstractCollectionConverter
{
    /**
     * Array values separator.
     */
    private static final String separator = ";";

    /**
     * Constructs new converter.
     *
     * @param mapper XStream mapper
     */
    public SimpleArrayConverter ( final Mapper mapper )
    {
        super ( mapper );
    }

    @Override
    public boolean canConvert ( final Class type )
    {
        return type.isArray ();
    }

    @Override
    public void marshal ( final Object object, final HierarchicalStreamWriter writer, final MarshallingContext context )
    {
        final int length = Array.getLength ( object );
        final StringBuilder value = new StringBuilder ();
        for ( int i = 0; i < length; i++ )
        {
            final Object item = Array.get ( object, i );
            if ( item == null )
            {
                throw new IllegalArgumentException ( "This converter doesn't support null array elements" );
            }
            else if ( item.getClass ().isPrimitive () || item instanceof Integer || item instanceof Character || item instanceof Byte ||
                    item instanceof Short || item instanceof Long || item instanceof Float || item instanceof Double )
            {
                value.append ( item.toString () );
            }
            else if ( item instanceof Color )
            {
                value.append ( ColorConverter.colorToString ( ( Color ) item ) );
            }
            else if ( item instanceof Insets )
            {
                value.append ( InsetsConverter.insetsToString ( ( Insets ) item ) );
            }
            else
            {
                throw new IllegalArgumentException ( "Unable to marshal array of type: " + item.getClass () );
            }
            if ( i < length - 1 )
            {
                value.append ( separator );
            }
        }
        writer.setValue ( value.toString () );
    }

    @Override
    public Object unmarshal ( final HierarchicalStreamReader reader, final UnmarshallingContext context )
    {
        final String object = reader.getValue ();
        if ( object != null )
        {
            final List<String> values = TextUtils.stringToList ( object, separator );
            final Class componentType = context.getRequiredType ().getComponentType ();
            final Object array = Array.newInstance ( componentType, values.size () );
            for ( int i = 0; i < values.size (); i++ )
            {
                final String v = values.get ( i );
                if ( componentType == Integer.class || componentType == int.class )
                {
                    Array.set ( array, i, Integer.valueOf ( v ) );
                }
                else if ( componentType == Character.class || componentType == char.class )
                {
                    Array.set ( array, i, v.charAt ( 0 ) );
                }
                else if ( componentType == Byte.class || componentType == byte.class )
                {
                    Array.set ( array, i, Byte.valueOf ( v ) );
                }
                else if ( componentType == Short.class || componentType == short.class )
                {
                    Array.set ( array, i, Short.valueOf ( v ) );
                }
                else if ( componentType == Long.class || componentType == long.class )
                {
                    Array.set ( array, i, Long.valueOf ( v ) );
                }
                else if ( componentType == Float.class || componentType == float.class )
                {
                    Array.set ( array, i, Float.valueOf ( v ) );
                }
                else if ( componentType == Double.class || componentType == double.class )
                {
                    Array.set ( array, i, Double.valueOf ( v ) );
                }
                else if ( componentType == Color.class )
                {
                    Array.set ( array, i, ColorConverter.colorFromString ( v ) );
                }
                else if ( componentType == Insets.class )
                {
                    Array.set ( array, i, InsetsConverter.insetsFromString ( v ) );
                }
                else
                {
                    throw new IllegalArgumentException ( "Unable to unmarshal array of type: " + componentType );
                }
            }
            return array;
        }
        else
        {
            return null;
        }
    }
}