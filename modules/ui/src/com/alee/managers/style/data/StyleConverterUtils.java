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
import com.thoughtworks.xstream.mapper.CannotResolveClassException;
import com.thoughtworks.xstream.mapper.Mapper;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * Set of utilities for serializing and deserializing style files.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-StyleManager">How to use StyleManager</a>
 * @see com.alee.managers.style.StyleManager
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
     * @param reader     {@link HierarchicalStreamReader}
     * @param context    {@link UnmarshallingContext}
     * @param mapper     {@link Mapper}
     * @param properties map to read properties into
     * @param clazz      class to read properties for, it will be used to retrieve properties field types
     * @param styleId    component style ID, might be used to report problems
     */
    public static void readProperties ( final HierarchicalStreamReader reader, final UnmarshallingContext context, final Mapper mapper,
                                        final Map<String, Object> properties, final Class clazz, final String styleId )
    {
        while ( reader.hasMoreChildren () )
        {
            reader.moveDown ();
            readProperty ( reader, context, mapper, styleId, properties, clazz, reader.getNodeName () );
            reader.moveUp ();
        }
    }

    /**
     * Parses single style property into properties map.
     *
     * @param reader        {@link HierarchicalStreamReader}
     * @param context       {@link UnmarshallingContext}
     * @param mapper        {@link Mapper}
     * @param styleId       component style ID, might be used to report problems
     * @param properties    map to read property into
     * @param propertyClass class to read property for, it will be used to retrieve property field type
     * @param propertyName  property name
     */
    private static void readProperty ( final HierarchicalStreamReader reader, final UnmarshallingContext context, final Mapper mapper,
                                       final String styleId, final Map<String, Object> properties, final Class propertyClass,
                                       final String propertyName )
    {
        final String ignored = reader.getAttribute ( IGNORED_ATTRIBUTE );
        if ( Boolean.parseBoolean ( ignored ) )
        {
            // Adding special value that will be ignored when values are applied
            // This is generally a tricky way to provide
            properties.put ( propertyName, IgnoredValue.VALUE );
        }
        else
        {
            // Retrieving field class
            final Class fieldClass;
            final String classAttribute = reader.getAttribute ( "class" );
            if ( classAttribute != null )
            {
                try
                {
                    // This would be the case for interface implementations as they cannot be instantiated directly
                    // We also rethrow appropriate exception here in case unknown type have been specified
                    fieldClass = mapper.realClass ( classAttribute );
                }
                catch ( final CannotResolveClassException e )
                {
                    final String msg = "Component property '%s' value from style '%s' has unknown type: %s";
                    throw new StyleException ( String.format ( msg, propertyName, styleId, classAttribute ), e );
                }
            }
            else
            {
                // Trying to retrieve field type by its name
                // This would be the case for common types like String or int
                fieldClass = ReflectUtils.getFieldTypeSafely ( propertyClass, propertyName );
            }

            // Reading property value
            if ( fieldClass != null )
            {
                try
                {
                    // Reading property using field type
                    properties.put ( propertyName, context.convertAnother ( properties, fieldClass ) );
                }
                catch ( final Exception e )
                {
                    final String msg = "Component property '%s' value from style '%s' cannot be read";
                    throw new StyleException ( String.format ( msg, propertyName, styleId ), e );
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
                    catch ( final Exception e )
                    {
                        final String msg = "Component property '%s' value from style '%s' cannot be read";
                        throw new StyleException ( String.format ( msg, propertyName, styleId ), e );
                    }
                }
                else
                {
                    final String msg = "Component property '%s' type from style '%s' cannot be determined. " +
                            "Make sure it points to existing field or getter method";
                    throw new StyleException ( String.format ( msg, propertyName, styleId ) );
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
        // Checking class existence
        if ( inClass != null )
        {
            // Checking field existence
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
                final String msg = "Unable to find painter field '%s' in class '%s' for default painter class retrieval";
                throw new StyleException ( String.format ( msg, field, inClass ) );
            }
        }
        return null;
    }
}