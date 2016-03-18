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

package com.alee.managers.style.data;

import com.alee.managers.style.StyleException;
import com.alee.painter.DefaultPainter;
import com.alee.painter.Painter;
import com.alee.utils.ReflectUtils;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * @author Mikle Garin
 */

public final class StyleConverterUtils
{
    /**
     * Converter constants.
     */
    public static final String IGNORED_ATTRIBUTE = "ignored";

    /**
     * Read properties for the specified class into the provided properties map.
     *
     * @param reader     {@link com.thoughtworks.xstream.io.HierarchicalStreamReader}
     * @param context    {@link com.thoughtworks.xstream.converters.UnmarshallingContext}
     * @param properties map to read properties into
     * @param clazz      class to read properties for, it will be used to retrieve properties field types
     * @param styleId    component style ID, might be used to report problems
     */
    public static void readProperties ( final HierarchicalStreamReader reader, final UnmarshallingContext context,
                                        final Map<String, Object> properties, final Class clazz, final String styleId )
    {
        while ( reader.hasMoreChildren () )
        {
            reader.moveDown ();
            readProperty ( reader, context, styleId, properties, clazz, reader.getNodeName () );
            reader.moveUp ();
        }
    }

    /**
     * Parses single style property into properties map.
     *
     * @param reader        {@link com.thoughtworks.xstream.io.HierarchicalStreamReader}
     * @param context       {@link com.thoughtworks.xstream.converters.UnmarshallingContext}
     * @param styleId       component style ID, might be used to report problems
     * @param properties    map to read property into
     * @param propertyClass class to read property for, it will be used to retrieve property field type
     * @param propertyName  property name
     */
    public static void readProperty ( final HierarchicalStreamReader reader, final UnmarshallingContext context, final String styleId,
                                      final Map<String, Object> properties, final Class propertyClass, final String propertyName )
    {
        final String ignored = reader.getAttribute ( IGNORED_ATTRIBUTE );
        if ( ignored != null && Boolean.parseBoolean ( ignored ) )
        {
            // Adding special value that will be ignored when values are applied
            // This is generally a tricky way to provide
            properties.put ( propertyName, IgnoredValue.VALUE );
        }
        else
        {
            // Trying to retrieve field type by its name
            final Class fieldClass = ReflectUtils.getFieldTypeSafely ( propertyClass, propertyName );
            if ( fieldClass != null )
            {
                try
                {
                    // Reading property using field type
                    properties.put ( propertyName, context.convertAnother ( properties, fieldClass ) );
                }
                catch ( final Throwable e )
                {
                    throw new StyleException ( "Component property \"" + propertyName + "\" value from style \"" + styleId +
                            "\" cannot be read", e );
                }
            }
            else
            {
                // Trying to retrieve field getter method by its name
                final Method getter = ReflectUtils.getFieldGetter ( propertyClass, propertyName );
                if ( getter != null )
                {
                    try
                    {
                        // Reading property using getter return type
                        final Class<?> rClass = getter.getReturnType ();
                        properties.put ( propertyName, context.convertAnother ( properties, rClass ) );
                    }
                    catch ( final Throwable e )
                    {
                        throw new StyleException ( "Component property \"" + propertyName + "\" value from style \"" + styleId +
                                "\" cannot be read", e );
                    }
                }
                else
                {
                    throw new StyleException ( "Component property \"" + propertyName + "\" type from style \"" + styleId +
                            "\" cannot be determined! Make sure it points to existing field or getter method" );
                }
            }
        }
    }

    /**
     * Retuns default painter class for the painter field in specified class.
     *
     * @param inClass class containing painter referencing field
     * @param field   painter referencing field name
     * @return default painter class for the painter field in specified class
     */
    public static Class<? extends Painter> getDefaultPainter ( final Class inClass, final String field )
    {
        // Checking class existance
        if ( inClass != null )
        {
            // Checking field existance
            final Field painterField = ReflectUtils.getFieldSafely ( inClass, field );
            painterField.setAccessible ( true );
            if ( painterField != null )
            {
                // Trying to acquire default painter annotation
                final DefaultPainter defaultPainter = painterField.getAnnotation ( DefaultPainter.class );
                if ( defaultPainter != null )
                {
                    // Return defalt painter
                    return defaultPainter.value ();
                }
            }
            else
            {
                // Since this is a major issue that we try a wrong field we will throw exception
                throw new StyleException (
                        "Unable to find painter field \"" + field + "\" in class \"" + inClass + "\" for default painter class retrieval" );
            }
        }
        return null;
    }
}