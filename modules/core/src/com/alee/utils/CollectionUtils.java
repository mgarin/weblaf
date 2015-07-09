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

import com.alee.utils.collection.IndexedSupplier;
import com.alee.utils.compare.Filter;
import com.alee.utils.text.TextProvider;

import java.util.*;

/**
 * This class provides a set of utilities to work with collections.
 *
 * @author Mikle Garin
 */

public final class CollectionUtils
{
    /**
     * Returns whether specified collection is empty or not.
     *
     * @param collection collection to process
     * @return true if specified collection is empty, false otherwise
     */
    public static boolean isEmpty ( final Collection collection )
    {
        return collection == null || collection.isEmpty ();
    }

    /**
     * Returns list with limited amount of objects from the initial list.
     * Only specified amount of first objects in initial list will be transferred to new list.
     *
     * @param list  initial list
     * @param limit objects amount limitation
     * @param <T>   object type
     * @return list with limited amount of objects from the initial list
     */
    public static <T> List<T> limit ( final List<T> list, final int limit )
    {
        return list.size () <= limit ? list : copySubList ( list, 0, limit );
    }

    /**
     * Returns sub list with copied values.
     *
     * @param list      source list
     * @param fromIndex start index
     * @param toIndex   end index
     * @param <T>       data type
     * @return sub list with copied values
     */
    public static <T> ArrayList<T> copySubList ( final List<T> list, final int fromIndex, final int toIndex )
    {
        return new ArrayList<T> ( list.subList ( fromIndex, toIndex ) );
    }

    /**
     * Returns sub list with cloned values.
     *
     * @param list      source list
     * @param fromIndex start index
     * @param toIndex   end index
     * @param <T>       data type
     * @return sub list with cloned values
     */
    public static <T extends Cloneable> ArrayList<T> cloneSubList ( final List<T> list, final int fromIndex, final int toIndex )
    {
        final ArrayList<T> clone = new ArrayList<T> ( toIndex - fromIndex );
        for ( int i = fromIndex; i < toIndex; i++ )
        {
            clone.add ( ReflectUtils.cloneSafely ( list.get ( i ) ) );
        }
        return clone;
    }

    /**
     * Returns data converted into list.
     *
     * @param data data
     * @param <T>  data type
     * @return data list
     */
    public static <T> ArrayList<T> asList ( final T... data )
    {
        final ArrayList<T> list = new ArrayList<T> ( data.length );
        Collections.addAll ( list, data );
        return list;
    }

    /**
     * Returns data converted into list.
     *
     * @param data data
     * @param <T>  data type
     * @return data list
     */
    public static <T> ArrayList<T> asList ( final Iterator<T> data )
    {
        final ArrayList<T> list = new ArrayList<T> ();
        while ( data.hasNext () )
        {
            list.add ( data.next () );
        }
        return list;
    }

    /**
     * Adds all objects into the specified list.
     *
     * @param collection list to fill
     * @param objects    objects
     * @param <T>        objects type
     * @return true if list changed as the result of this operation, false otherwise
     */
    public static <T> boolean addAll ( final Collection<T> collection, final T... objects )
    {
        boolean result = false;
        for ( final T object : objects )
        {
            if ( !collection.contains ( object ) )
            {
                result |= collection.add ( object );
            }
        }
        return result;
    }

    /**
     * Adds all non-null objects into the specified list.
     *
     * @param collection list to fill
     * @param objects    objects
     * @param <T>        objects type
     * @return true if list changed as the result of this operation, false otherwise
     */
    public static <T> boolean addAllNonNull ( final Collection<T> collection, final T... objects )
    {
        boolean result = false;
        for ( final T object : objects )
        {
            if ( !collection.contains ( object ) && object != null )
            {
                result |= collection.add ( object );
            }
        }
        return result;
    }

    /**
     * Adds all objects into the specified list.
     *
     * @param collection list to fill
     * @param objects    objects
     * @param <T>        objects type
     * @return true if list changed as the result of this operation, false otherwise
     */
    public static <T> boolean addAll ( final Collection<T> collection, final Collection<T> objects )
    {
        boolean result = false;
        for ( final T object : objects )
        {
            if ( !collection.contains ( object ) )
            {
                result |= collection.add ( object );
            }
        }
        return result;
    }

    /**
     * Adds all non-null objects into the specified list.
     *
     * @param collection list to fill
     * @param objects    objects
     * @param <T>        objects type
     * @return true if list changed as the result of this operation, false otherwise
     */
    public static <T> boolean addAllNonNull ( final Collection<T> collection, final Collection<T> objects )
    {
        boolean result = false;
        for ( final T object : objects )
        {
            if ( !collection.contains ( object ) && object != null )
            {
                result |= collection.add ( object );
            }
        }
        return result;
    }

    /**
     * Removes all objects from the specified list.
     *
     * @param collection list to fill
     * @param objects    objects
     * @param <T>        objects type
     * @return true if list changed as the result of this operation, false otherwise
     */
    public static <T> boolean removeAll ( final Collection<T> collection, final T... objects )
    {
        boolean result = false;
        for ( final T object : objects )
        {
            result |= collection.remove ( object );
        }
        return result;
    }

    /**
     * Returns copy of the specified list.
     * Note that this method will copy same list values into the new list.
     *
     * @param collection list to copy
     * @param <T>        list type
     * @return copy of the specified list
     */
    public static <T> ArrayList<T> copy ( final Collection<T> collection )
    {
        if ( collection == null )
        {
            return null;
        }
        return new ArrayList<T> ( collection );
    }

    /**
     * Returns clone of the specified collection.
     * Note that this method will clone all values into new list.
     *
     * @param collection collection to clone
     * @param <T>        collection objects type
     * @return clone of the specified list
     */
    public static <T extends Cloneable> ArrayList<T> clone ( final Collection<T> collection )
    {
        if ( collection == null )
        {
            return null;
        }
        final ArrayList<T> cloned = new ArrayList<T> ( collection.size () );
        for ( final T value : collection )
        {
            cloned.add ( ReflectUtils.cloneSafely ( value ) );
        }
        return cloned;
    }

    /**
     * Returns data converted into list.
     *
     * @param data data
     * @param <T>  data type
     * @return data list
     */
    public static <T> ArrayList<T> copy ( final T... data )
    {
        final ArrayList<T> list = new ArrayList<T> ( data.length );
        Collections.addAll ( list, data );
        return list;
    }

    /**
     * Returns collection with clonable values being cloned and non-clonable values simply copied from source collection.
     *
     * @param collection collection to perform action for
     * @param <T>        collection objects type
     * @return collection with clonable values being cloned and non-clonable values simply copied from source collection
     */
    public static <T> ArrayList<T> cloneOrCopy ( final Collection<T> collection )
    {
        if ( collection == null )
        {
            return null;
        }
        final ArrayList<T> cloned = new ArrayList<T> ( collection.size () );
        for ( final T value : collection )
        {
            if ( value instanceof Collection )
            {
                cloned.add ( ( T ) cloneOrCopy ( ( Collection ) value ) );
            }
            else if ( value instanceof Cloneable )
            {
                cloned.add ( ( T ) ReflectUtils.cloneSafely ( ( Cloneable ) value ) );
            }
            else
            {
                cloned.add ( value );
            }
        }
        return cloned;
    }

    /**
     * Removes all null elements from list.
     *
     * @param list list to refactor
     * @param <T>  list type
     * @return refactored list
     */
    public static <T> List<T> removeNulls ( final List<T> list )
    {
        if ( list == null )
        {
            return null;
        }
        for ( int i = list.size () - 1; i >= 0; i-- )
        {
            if ( list.get ( i ) == null )
            {
                list.remove ( i );
            }
        }
        return list;
    }

    /**
     * Returns whether lists are equal or not.
     *
     * @param list1 first list
     * @param list2 second list
     * @return true if lists are equal, false otherwise
     */
    public static boolean areEqual ( final List list1, final List list2 )
    {
        if ( list1 == null && list2 == null )
        {
            return true;
        }
        else if ( ( list1 == null || list2 == null ) && list1 != list2 )
        {
            return false;
        }
        else
        {
            if ( list1.size () != list2.size () )
            {
                return false;
            }
            else
            {
                for ( final Object object : list1 )
                {
                    if ( !list2.contains ( object ) )
                    {
                        return false;
                    }
                }
                return true;
            }
        }
    }

    /**
     * Returns list of strings extracted from the specified elements list.
     *
     * @param list         elements list
     * @param textProvider text provider
     * @param <T>          elements type
     * @return list of strings extracted from the specified elements list
     */
    public static <T> ArrayList<String> toStringList ( final List<T> list, final TextProvider<T> textProvider )
    {
        final ArrayList<String> stringList = new ArrayList<String> ( list.size () );
        for ( final T element : list )
        {
            stringList.add ( textProvider.provide ( element ) );
        }
        return stringList;
    }

    /**
     * Returns an int array created using Integer list.
     *
     * @param list Integer list
     * @return int array
     */
    public static int[] toArray ( final List<Integer> list )
    {
        final int[] array = new int[ list.size () ];
        for ( int i = 0; i < list.size (); i++ )
        {
            final Integer integer = list.get ( i );
            array[ i ] = integer != null ? integer : 0;
        }
        return array;
    }

    /**
     * Returns a list of objects converted from array.
     *
     * @param array data array
     * @param <T>   data type
     * @return data list
     */
    public static <T> ArrayList<T> toList ( final T[] array )
    {
        final ArrayList<T> list = new ArrayList<T> ( array.length );
        Collections.addAll ( list, array );
        return list;
    }

    /**
     * Returns a list of objects converted from deque.
     *
     * @param deque data deque
     * @param <T>   data type
     * @return data list
     */
    public static <T> ArrayList<T> toList ( final Deque<T> deque )
    {
        return new ArrayList<T> ( deque );
    }

    /**
     * Returns list of elements filtered from collection.
     *
     * @param collection collecton to filter
     * @param filter     filter to process
     * @param <T>        elements type
     * @return list of elements filtered from collection
     */
    public static <T> ArrayList<T> filter ( final Collection<T> collection, final Filter<T> filter )
    {
        final ArrayList<T> filtered = new ArrayList<T> ( collection.size () );
        for ( final T element : collection )
        {
            if ( filter.accept ( element ) )
            {
                filtered.add ( element );
            }
        }
        return filtered;
    }

    /**
     * Returns map keys list.
     *
     * @param map map to process
     * @param <K> key object type
     * @param <V> value object type
     * @return map keys list
     */
    public static <K, V> ArrayList<K> keysList ( final Map<K, V> map )
    {
        return new ArrayList<K> ( map.keySet () );
    }

    /**
     * Returns map values list.
     *
     * @param map map to process
     * @param <K> key object type
     * @param <V> value object type
     * @return map values list
     */
    public static <K, V> ArrayList<V> valuesList ( final Map<K, V> map )
    {
        return new ArrayList<V> ( map.values () );
    }

    /**
     * Returns map values summary list with unique elements only.
     *
     * @param map map to process
     * @param <K> key object type
     * @param <V> value object type
     * @return map values summary list with unique elements only
     */
    public static <K, V> ArrayList<V> valuesSummaryList ( final Map<K, List<V>> map )
    {
        final ArrayList<V> summary = new ArrayList<V> ( 0 );
        for ( final Map.Entry<K, List<V>> entry : map.entrySet () )
        {
            final List<V> list = entry.getValue ();
            summary.ensureCapacity ( summary.size () + list.size () );
            for ( final V value : list )
            {
                if ( !summary.contains ( value ) )
                {
                    summary.add ( value );
                }
            }
        }
        return summary;
    }

    /**
     * Fills and returns list with data provided by supplier interface implementation.
     *
     * @param amount   amount of list elements
     * @param supplier data provider
     * @param <T>      data type
     * @return list filled with data provided by supplier interface implementation
     */
    public static <T> List<T> fillList ( final int amount, final IndexedSupplier<T> supplier )
    {
        final List<T> list = new ArrayList<T> ( amount );
        for ( int i = 0; i < amount; i++ )
        {
            list.add ( supplier.get ( i ) );
        }
        return list;
    }
}