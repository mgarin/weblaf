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

import com.alee.utils.compare.Filter;
import com.alee.utils.text.TextProvider;

import java.util.*;

/**
 * This class provides a set of utilities to work with various collections and arrays.
 *
 * @author Mikle Garin
 */

public final class CollectionUtils
{
    /**
     * Adds all objects into specified list.
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
            result |= collection.add ( object );
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
    public static <T> List<T> copy ( final Collection<T> collection )
    {
        return new ArrayList<T> ( collection );
    }

    /**
     * Returns clone of the specified list.
     * Note that this method will clone all values into new list.
     *
     * @param collection list to clone
     * @param <T>        list type
     * @return clone of the specified list
     */
    public static <T extends Cloneable> List<T> clone ( final Collection<T> collection )
    {
        final List<T> cloned = new ArrayList<T> ( collection.size () );
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
    public static <T> List<T> copy ( final T... data )
    {
        final List<T> list = new ArrayList<T> ( data.length );
        Collections.addAll ( list, data );
        return list;
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
        if ( list != null )
        {
            for ( int i = list.size () - 1; i >= 0; i-- )
            {
                if ( list.get ( i ) == null )
                {
                    list.remove ( i );
                }
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
    public static <T> List<String> toStringList ( final List<T> list, final TextProvider<T> textProvider )
    {
        final List<String> stringList = new ArrayList<String> ( list.size () );
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
    public static <T> List<T> toList ( final T[] array )
    {
        final List<T> list = new ArrayList<T> ( array.length );
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
    public static <T> List<T> toList ( final Deque<T> deque )
    {
        return new ArrayList<T> ( deque );
    }

    /**
     * Returns index of specified object in array.
     *
     * @param object object to find
     * @param array  array to process
     * @return index of specified object in array
     */
    public static int indexOf ( final Object object, final Object[] array )
    {
        for ( int i = 0; i < array.length; i++ )
        {
            final Object obj = array[ i ];
            if ( obj == null && object == null || obj != null && object != null && obj.equals ( object ) )
            {
                return i;
            }
        }
        return -1;
    }

    /**
     * Returns index of specified text in array.
     *
     * @param text  text to find
     * @param array array to process
     * @return index of specified text in array
     */
    public static int indexOf ( final String text, final String[] array )
    {
        return indexOf ( text, array, false );
    }

    /**
     * Returns index of specified text in array.
     *
     * @param text       text to find
     * @param array      array to process
     * @param ignoreCase whether ignore text case or not
     * @return index of specified text in array
     */
    public static int indexOf ( final String text, final String[] array, final boolean ignoreCase )
    {
        for ( int i = 0; i < array.length; i++ )
        {
            final String txt = array[ i ];
            if ( txt == null && text == null ||
                    txt != null && text != null && ( ignoreCase ? txt.equalsIgnoreCase ( text ) : txt.equals ( text ) ) )
            {
                return i;
            }
        }
        return -1;
    }

    /**
     * Returns index of specified integer in array.
     *
     * @param number integer to find
     * @param array  array to process
     * @return index of specified integer in array
     */
    public static int indexOf ( final int number, final int[] array )
    {
        for ( int i = 0; i < array.length; i++ )
        {
            final int num = array[ i ];
            if ( num == number )
            {
                return i;
            }
        }
        return -1;
    }

    /**
     * Returns index of specified character in array.
     *
     * @param character character to find
     * @param array     array to process
     * @return index of specified character in array
     */
    public static int indexOf ( final char character, final char[] array )
    {
        for ( int i = 0; i < array.length; i++ )
        {
            final char ch = array[ i ];
            if ( ch == character )
            {
                return i;
            }
        }
        return -1;
    }

    /**
     * Returns list of elements filtered from collection.
     *
     * @param collection collecton to filter
     * @param filter     filter to process
     * @param <T>        elements type
     * @return list of elements filtered from collection
     */
    public static <T> List<T> filter ( final Collection<T> collection, final Filter<T> filter )
    {
        final List<T> filtered = new ArrayList<T> ( collection.size () );
        for ( final T element : collection )
        {
            if ( filter.accept ( element ) )
            {
                filtered.add ( element );
            }
        }
        return filtered;
    }
}