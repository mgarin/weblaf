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

package com.alee.utils;


import com.alee.api.Identifiable;
import com.alee.api.Mergeable;
import com.alee.managers.log.Log;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author Mikle Garin
 */

public final class MergeUtils
{
    /**
     * todo 1. Make assignable types mergeable as well, not just objects of the exact same type
     * todo 2. Add "mergeByFields(T, Object...)" method similar to "cloneByFieldsSafely(T, Object...)"
     */

    /**
     * Merges {@code merged} object on top of {@code existing} object and returns merge result.
     *
     * @param existing base object
     * @param merged   object to merge on top of {@code existing} object
     * @return result of merging {@code merged} object on top of {@code existing} object
     */
    public static <T> T merge ( final T existing, final T merged )
    {
        // Creating a clone of merged object to avoid source object modifications
        final T mergedClone = clone ( merged );

        // Properly merging variables
        return mergeImpl ( existing, mergedClone );
    }

    /**
     * Merges {@code merged} object on top of {@code existing} object and returns merge result.
     *
     * @param existing base object
     * @param merged   clone of object to merge on top of {@code existing} object
     * @return result of merging {@code merged} object on top of {@code existing} object
     */
    private static <T> T mergeImpl ( final T existing, final T merged )
    {
        if ( existing != null && merged != null && existing.getClass () == merged.getClass () )
        {
            // Since Mergeable is our custom interface we don't need to be careful with arrays, maps and lists unlike Cloneable
            // We simply check whether object is Mergeable or not straight away and then try a few more options
            if ( merged instanceof Mergeable )
            {
                // Handling mergeable elements merge
                // We need to check that elements have same class before that though
                // That is required to ensure that objects of different types do not attempt to be merged
                return ( T ) ( ( Mergeable ) existing ).merge ( ( Mergeable ) merged );
            }
            else if ( merged.getClass ().isArray () )
            {
                // Handling array elements merge
                // We will merge elements under the same indices
                // If existing array is smaller than merged array - new array will be created for the merge result
                final int el = Array.getLength ( existing );
                final int ml = Array.getLength ( merged );
                final Object result = el >= ml ? existing : Array.newInstance ( merged.getClass ().getComponentType (), ml );
                for ( int i = 0; i < ml; i++ )
                {
                    Array.set ( result, i, merge ( Array.get ( existing, i ), Array.get ( merged, i ) ) );
                }
                return ( T ) result;
            }
            else if ( merged instanceof Map )
            {
                // Handling map elements merge
                // We will merge map elements under the same keys
                // Non-existing elements will simply be added into existing map
                final Map<String, Object> existingMap = ( Map<String, Object> ) existing;
                final Map<String, Object> mergedMap = ( Map<String, Object> ) merged;
                for ( final Map.Entry<String, Object> entry : mergedMap.entrySet () )
                {
                    final String k = entry.getKey ();
                    final Object e = existingMap.get ( k );
                    final Object v = entry.getValue ();
                    existingMap.put ( k, merge ( e, v ) );
                }
                return ( T ) existingMap;
            }
            else if ( merged instanceof List )
            {
                // todo Replace List with simple collection
                // Handling list elements merge
                // We will try to find identifiable elements in list and merge those
                // All the rest of the elements will simply be added to the end of the list in provided order
                // This is the best way we can handle list elements merge without any additional information on the elements
                final List existingList = ( List ) existing;
                final List mergedList = ( List ) merged;
                for ( final Object mergedObject : mergedList )
                {
                    // We only merge identifiable objects as there is no other way to ensure we really need to merge them
                    // We don't really want to have two different objects of the same type with the same ID in one list
                    if ( mergedObject != null && mergedObject instanceof Identifiable )
                    {
                        // Looking for object of the same type which is also identifiable in the existing list
                        // Then we compare their IDs and merge them using the same algorithm if IDs are equal
                        final String mid = ( ( Identifiable ) mergedObject ).getId ();
                        boolean found = false;
                        for ( int j = 0; j < existingList.size (); j++ )
                        {
                            final Object existingObject = existingList.get ( j );
                            if ( existingObject != null )
                            {
                                final String eid = ( ( Identifiable ) existingObject ).getId ();
                                if ( CompareUtils.equals ( eid, mid ) )
                                {
                                    if ( existingObject.getClass () == mergedObject.getClass () )
                                    {
                                        existingList.set ( j, merge ( existingObject, mergedObject ) );
                                    }
                                    else
                                    {
                                        existingList.set ( j, mergedObject );
                                    }
                                    found = true;
                                    break;
                                }
                            }
                        }
                        if ( !found )
                        {
                            // Simply adding object to the end of the list
                            existingList.add ( mergedObject );
                        }
                    }
                    else
                    {
                        // Simply adding non-identifiable object to the end of the list
                        existingList.add ( mergedObject );
                    }
                }
                return ( T ) existingList;
            }
            else
            {
                // Simply replacing value
                return merged;
            }
        }
        else if ( merged != null )
        {
            // Simply replacing value
            return merged;
        }
        else
        {
            // Returning existing value
            return existing;
        }
    }

    /**
     * Returns object clone if it is possible to clone it, otherwise returns original object.
     *
     * @param object object to clone
     * @param <T>    object type
     * @return object clone if it is possible to clone it, otherwise returns original object
     */
    public static <T> T clone ( final T object )
    {
        if ( object != null && !object.getClass ().isPrimitive () )
        {
            // Arrays, maps and collections are checked before Clonable since we use recursive clone implementation
            // Otherwise objects like ArrayList will use their own clone method implementation which would cause issues
            if ( object.getClass ().isArray () )
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
                }
                catch ( final Throwable e )
                {
                    Log.get ().error ( "Unable to instantiate array: " + object.getClass (), e );
                }
            }
            else if ( object instanceof Map )
            {
                try
                {
                    // Trying to properly clone map
                    final Map clone = ReflectUtils.createInstance ( object.getClass () );
                    for ( final Object e : ( ( Map ) object ).entrySet () )
                    {
                        // Recursive clone call
                        // We will oly clone value here as this is sufficient
                        final Map.Entry entry = ( Map.Entry ) e;
                        clone.put ( entry.getKey (), clone ( entry.getValue () ) );
                    }
                    return ( T ) clone;
                }
                catch ( final Throwable e )
                {
                    Log.get ().error ( "Unable to instantiate map: " + object.getClass (), e );
                }
            }
            else if ( object instanceof Collection )
            {
                try
                {
                    // Trying to properly clone collection
                    final Collection clone = ReflectUtils.createInstance ( object.getClass () );
                    for ( final Object element : ( Collection ) object )
                    {
                        // Recursive clone call
                        clone.add ( clone ( element ) );
                    }
                    return ( T ) clone;
                }
                catch ( final Throwable e )
                {
                    Log.get ().error ( "Unable to instantiate collection: " + object.getClass (), e );
                }
            }
            else if ( object instanceof Cloneable )
            {
                try
                {
                    // Trying to directly clone an object
                    final T clone = ( T ) ReflectUtils.clone ( ( Cloneable ) object );
                    return clone != null ? clone : object;
                }
                catch ( final Throwable e )
                {
                    Log.get ().error ( "Unable to clone object: " + object, e );
                }
            }
        }
        return object;
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
            // Making field accessible
            // Otherwise final or non-public fields won't allow any operations on them
            field.setAccessible ( true );

            // Skip transient fields
            if ( Modifier.isTransient ( field.getModifiers () ) )
            {
                continue;
            }

            // Retrieving original object field value
            final Object value = field.get ( object );

            // Creating value clone if possible
            final Object clone = clone ( value );

            // Updating field
            field.set ( copy, clone );
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
        catch ( final Throwable e )
        {
            Log.warn ( "Unable to clone object by its fields: " + object, e );
            return null;
        }
    }
}