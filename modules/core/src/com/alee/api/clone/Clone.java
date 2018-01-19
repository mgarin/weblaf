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

import com.alee.utils.ReflectUtils;
import com.alee.utils.reflection.ModifierType;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Configurable algorithm for cloning object instances.
 * It can be customized through the settings provided in its constructor once on creation.
 * To clone any object using this class instance simply call {@link #clone(Object)} method.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-Clone">How to use Clone</a>
 * @see Clone
 */

public class Clone implements Serializable
{
    /**
     * todo 1. Change this class to a {@link com.alee.api.merge.Merge}-like object
     * todo 2. Add support for all types which were previously supported
     */

    /**
     * Returns object clone if it is possible to clone it, otherwise returns original object.
     * Arrays, maps and collections are checked before {@link Cloneable} since we use recursive clone implementation.
     * Otherwise objects like {@link java.util.ArrayList} will use their own clone method implementation which would cause issues.
     *
     * @param object object to clone
     * @param <T>    object type
     * @return object clone if it is possible to clone it, otherwise returns original object
     */
    public static <T> T clone ( final T object )
    {
        final T clone;
        if ( object == null )
        {
            // Return null if object is null
            clone = null;
        }
        else if ( object.getClass ().isEnum () || ReflectUtils.isPrimitive ( object ) )
        {
            // Return object itself if it is enumeration or primitive
            clone = object;
        }
        else if ( object.getClass ().isArray () )
        {
            try
            {
                // Creating new array instance
                final Class<?> type = object.getClass ().getComponentType ();
                final int length = Array.getLength ( object );
                final Object newArray = Array.newInstance ( type, length );
                for ( int i = 0; i < length; i++ )
                {
                    // Recursive clone call
                    Array.set ( newArray, i, clone ( Array.get ( object, i ) ) );
                }
                clone = object;
            }
            catch ( final Exception e )
            {
                throw new CloneException ( "Unable to instantiate array: " + object.getClass (), e );
            }
        }
        else if ( object instanceof Map )
        {
            try
            {
                // Trying to properly clone map
                final Map map = ReflectUtils.createInstance ( object.getClass () );
                for ( final Object e : ( ( Map ) object ).entrySet () )
                {
                    // Recursive clone call
                    // We will oly clone value here as this is sufficient
                    final Map.Entry entry = ( Map.Entry ) e;
                    map.put ( entry.getKey (), clone ( entry.getValue () ) );
                }
                clone = ( T ) map;
            }
            catch ( final Exception e )
            {
                throw new CloneException ( "Unable to instantiate map: " + object.getClass (), e );
            }
        }
        // todo else if ( object instanceof ImmutableList )
        // todo else if ( object instanceof ImmutableCollection )
        else if ( object instanceof Collection )
        {
            try
            {
                // Trying to properly clone collection
                final Collection collection = ReflectUtils.createInstance ( object.getClass () );
                for ( final Object element : ( Collection ) object )
                {
                    // Recursive clone call
                    collection.add ( clone ( element ) );
                }
                clone = ( T ) collection;
            }
            catch ( final Exception e )
            {
                throw new CloneException ( "Unable to instantiate collection: " + object.getClass (), e );
            }
        }
        else if ( object instanceof Cloneable )
        {
            try
            {
                // Trying to directly clone an object
                clone = ReflectUtils.callMethod ( object, "clone" );
            }
            catch ( final Exception e )
            {
                throw new CloneException ( "Unable to clone object: " + object, e );
            }
        }
        else
        {
            clone = object;
        }
        return clone;
    }

    /**
     * Returns cloned object instance.
     * This method will clone fields directly instead of calling clone method on the object.
     * Object fields will be cloned normally through clone method if they implement Cloneable interface.
     *
     * @param object    object to clone
     * @param arguments class constructor arguments
     * @param <T>       cloned object type
     * @return cloned object instance
     * @throws java.lang.InstantiationException            if the class is abstract
     * @throws java.lang.NoSuchMethodException             if method was not found
     * @throws java.lang.reflect.InvocationTargetException if method throws an exception
     * @throws java.lang.IllegalAccessException            if method is inaccessible
     */
    public static <T> T cloneByFields ( final T object, final Object... arguments )
            throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException
    {
        final T copy = ReflectUtils.createInstance ( object.getClass (), arguments );
        final List<Field> fields = ReflectUtils.getFields ( object );
        for ( final Field field : fields )
        {
            // Only clone non-transient fields
            if ( ReflectUtils.hasNoneOfModifiers ( field, ModifierType.TRANSIENT ) )
            {
                // Retrieving original object field value
                final Object value = field.get ( object );

                // Creating value clone if possible
                final Object clone = Clone.clone ( value );

                // Updating field
                ReflectUtils.setFieldValue ( copy, field, clone );
            }
        }
        return copy;
    }

    /**
     * Returns cloned object instance.
     * This method will clone fields directly instead of calling clone method on the object.
     * Object fields will be cloned normally through clone method if they implement Cloneable interface.
     *
     * @param object    object to clone
     * @param arguments class constructor arguments
     * @param <T>       cloned object type
     * @return cloned object instance
     */
    public static <T> T cloneByFieldsSafely ( final T object, final Object... arguments )
    {
        try
        {
            return cloneByFields ( object, arguments );
        }
        catch ( final Exception e )
        {
            final String msg = "Unable to clone object by its fields: %s";
            LoggerFactory.getLogger ( Clone.class ).warn ( String.format ( msg, object ), e );
            return null;
        }
    }
}