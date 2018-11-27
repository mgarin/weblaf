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

import com.alee.api.jdk.Function;
import com.alee.api.jdk.Objects;
import com.alee.api.jdk.Supplier;
import com.alee.utils.compare.Filter;

import java.util.*;

/**
 * This class provides a set of utilities to work with {@link Collection} implementations.
 *
 * @author Mikle Garin
 */
public final class CollectionUtils
{
    /**
     * Private constructor to avoid instantiation.
     */
    private CollectionUtils ()
    {
        throw new UtilityException ( "Utility classes are not meant to be instantiated" );
    }

    /**
     * Returns whether specified {@link Collection} is empty or not.
     *
     * @param collection {@link Collection} to process
     * @return {@code true} if specified {@link Collection} is empty, {@code false} otherwise
     */
    public static boolean isEmpty ( final Collection collection )
    {
        return collection == null || collection.isEmpty ();
    }

    /**
     * Returns whether specified {@link Collection} is empty or not.
     *
     * @param collection {@link Collection} to process
     * @return {@code true} if specified {@link Collection} is not empty, {@code false} otherwise
     */
    public static boolean notEmpty ( final Collection collection )
    {
        return !isEmpty ( collection );
    }

    /**
     * Returns non-{@code null} {@link List} that is either specified {@code list} or new empty {@link ArrayList}.
     *
     * @param list {@link List}
     * @param <T>  elements type
     * @return non-{@code null} {@link List} that is either specified {@code list} or new empty {@link ArrayList}
     */
    public static <T> List<T> nonNull ( final List<T> list )
    {
        return list != null ? list : new ArrayList<T> ( 0 );
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
     * Returns maximum element of the {@link Collection} according to {@link Comparator}.
     * This methid is slightly more optimal and safe unlike {@link Collections#max(Collection, Comparator)}.
     *
     * @param collection {@link Collection}
     * @param comparator {@link Comparator}
     * @param <T>        collection element type
     * @return maximum element of the {@link Collection} according to {@link Comparator}
     */
    public static <T> T max ( final Collection<T> collection, final Comparator<T> comparator )
    {
        final T result;
        if ( CollectionUtils.isEmpty ( collection ) )
        {
            result = null;
        }
        else if ( collection.size () == 1 )
        {
            result = collection.iterator ().next ();
        }
        else
        {
            result = Collections.max ( collection, comparator );
        }
        return result;
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
     * Returns booleans converted into list.
     *
     * @param data booleans array
     * @return booleans list
     */
    public static ArrayList<Boolean> asList ( final boolean[] data )
    {
        final ArrayList<Boolean> list = new ArrayList<Boolean> ( data.length );
        for ( final boolean i : data )
        {
            list.add ( i );
        }
        return list;
    }

    /**
     * Returns integers converted into list.
     *
     * @param data integers array
     * @return integers list
     */
    public static ArrayList<Integer> asList ( final int[] data )
    {
        final ArrayList<Integer> list = new ArrayList<Integer> ( data.length );
        for ( final int i : data )
        {
            list.add ( i );
        }
        return list;
    }

    /**
     * Returns characters converted into list.
     *
     * @param data characters array
     * @return characters list
     */
    public static ArrayList<Character> asList ( final char[] data )
    {
        final ArrayList<Character> list = new ArrayList<Character> ( data.length );
        for ( final char i : data )
        {
            list.add ( i );
        }
        return list;
    }

    /**
     * Returns bytes converted into list.
     *
     * @param data bytes array
     * @return bytes list
     */
    public static ArrayList<Byte> asList ( final byte[] data )
    {
        final ArrayList<Byte> list = new ArrayList<Byte> ( data.length );
        for ( final byte i : data )
        {
            list.add ( i );
        }
        return list;
    }

    /**
     * Returns shorts converted into list.
     *
     * @param data shorts array
     * @return shorts list
     */
    public static ArrayList<Short> asList ( final short[] data )
    {
        final ArrayList<Short> list = new ArrayList<Short> ( data.length );
        for ( final short i : data )
        {
            list.add ( i );
        }
        return list;
    }

    /**
     * Returns longs converted into list.
     *
     * @param data longs array
     * @return longs list
     */
    public static ArrayList<Long> asList ( final long[] data )
    {
        final ArrayList<Long> list = new ArrayList<Long> ( data.length );
        for ( final long i : data )
        {
            list.add ( i );
        }
        return list;
    }

    /**
     * Returns floats converted into list.
     *
     * @param data floats array
     * @return floats list
     */
    public static ArrayList<Float> asList ( final float[] data )
    {
        final ArrayList<Float> list = new ArrayList<Float> ( data.length );
        for ( final float i : data )
        {
            list.add ( i );
        }
        return list;
    }

    /**
     * Returns doubles converted into list.
     *
     * @param data doubles array
     * @return doubles list
     */
    public static ArrayList<Double> asList ( final double[] data )
    {
        final ArrayList<Double> list = new ArrayList<Double> ( data.length );
        for ( final double i : data )
        {
            list.add ( i );
        }
        return list;
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
     * Returns non-null data converted into list.
     *
     * @param data data
     * @param <T>  data type
     * @return non-null data list
     */
    public static <T> ArrayList<T> asNonNullList ( final T... data )
    {
        final ArrayList<T> list = new ArrayList<T> ( data.length );
        if ( data != null )
        {
            for ( final T object : data )
            {
                if ( object != null )
                {
                    list.add ( object );
                }
            }
        }
        return list;
    }

    /**
     * Returns whether or not all {@link Collection} elements are unique.
     *
     * @param collection {@link Collection}
     * @param <T>        {@link Collection} element type
     * @return {@code true} if all {@link Collection} elements are unique, {@code false} otherwise
     */
    public static <T> boolean areAllUnique ( final Collection<T> collection )
    {
        return new HashSet<T> ( collection ).size () == collection.size ();
    }

    /**
     * Adds all objects into the specified list.
     *
     * @param collection list to fill
     * @param objects    objects
     * @return {@code true} if list changed as the result of this operation, {@code false} otherwise
     */
    public static boolean addUnique ( final Collection<Boolean> collection, final boolean[] objects )
    {
        boolean result = false;
        for ( final boolean object : objects )
        {
            if ( !collection.contains ( object ) )
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
     * @return {@code true} if list changed as the result of this operation, {@code false} otherwise
     */
    public static boolean addUnique ( final Collection<Integer> collection, final int[] objects )
    {
        boolean result = false;
        for ( final int object : objects )
        {
            if ( !collection.contains ( object ) )
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
     * @return {@code true} if list changed as the result of this operation, {@code false} otherwise
     */
    public static boolean addUnique ( final Collection<Character> collection, final char[] objects )
    {
        boolean result = false;
        for ( final char object : objects )
        {
            if ( !collection.contains ( object ) )
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
     * @return {@code true} if list changed as the result of this operation, {@code false} otherwise
     */
    public static boolean addUnique ( final Collection<Byte> collection, final byte[] objects )
    {
        boolean result = false;
        for ( final byte object : objects )
        {
            if ( !collection.contains ( object ) )
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
     * @return {@code true} if list changed as the result of this operation, {@code false} otherwise
     */
    public static boolean addUnique ( final Collection<Short> collection, final short[] objects )
    {
        boolean result = false;
        for ( final short object : objects )
        {
            if ( !collection.contains ( object ) )
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
     * @return {@code true} if list changed as the result of this operation, {@code false} otherwise
     */
    public static boolean addUnique ( final Collection<Long> collection, final long[] objects )
    {
        boolean result = false;
        for ( final long object : objects )
        {
            if ( !collection.contains ( object ) )
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
     * @return {@code true} if list changed as the result of this operation, {@code false} otherwise
     */
    public static boolean addUnique ( final Collection<Float> collection, final float[] objects )
    {
        boolean result = false;
        for ( final float object : objects )
        {
            if ( !collection.contains ( object ) )
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
     * @return {@code true} if list changed as the result of this operation, {@code false} otherwise
     */
    public static boolean addUnique ( final Collection<Double> collection, final double[] objects )
    {
        boolean result = false;
        for ( final double object : objects )
        {
            if ( !collection.contains ( object ) )
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
     * @return {@code true} if list changed as the result of this operation, {@code false} otherwise
     */
    public static <T> boolean addUnique ( final Collection<T> collection, final T... objects )
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
     * Adds all objects into the specified list.
     *
     * @param collection list to fill
     * @param objects    objects
     * @param <T>        objects type
     * @return {@code true} if list changed as the result of this operation, {@code false} otherwise
     */
    public static <T> boolean addUnique ( final Collection<T> collection, final Collection<T> objects )
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
     * @return {@code true} if list changed as the result of this operation, {@code false} otherwise
     */
    public static <T> boolean addUniqueNonNull ( final Collection<T> collection, final T... objects )
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
     * Adds all non-null objects into the specified list.
     *
     * @param collection list to fill
     * @param objects    objects
     * @param <T>        objects type
     * @return {@code true} if list changed as the result of this operation, {@code false} otherwise
     */
    public static <T> boolean addUniqueNonNull ( final Collection<T> collection, final Collection<T> objects )
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
     * @return {@code true} if list changed as the result of this operation, {@code false} otherwise
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
     * Removes all objects from the specified list.
     *
     * @param collection list to fill
     * @param objects    objects
     * @param <T>        objects type
     * @return {@code true} if list changed as the result of this operation, {@code false} otherwise
     */
    public static <T> boolean removeAll ( final Collection<T> collection, final Collection<T> objects )
    {
        boolean result = false;
        for ( final T object : objects )
        {
            result |= collection.remove ( object );
        }
        return result;
    }

    /**
     * Returns collection that contains elements from all specified collections.
     * Order in which collection are provided will be preserved.
     *
     * @param collections collections to join
     * @param <T>         collection type
     * @return collection that contains elements from all specified collections
     */
    public static <T> ArrayList<T> join ( final Collection<T>... collections )
    {
        // Calculating final collection size
        int size = 0;
        if ( collections != null )
        {
            for ( final Collection<T> collection : collections )
            {
                size += collection != null ? collection.size () : 0;
            }
        }

        // Creating joined collection
        final ArrayList<T> list = new ArrayList<T> ( size );
        if ( collections != null )
        {
            for ( final Collection<T> collection : collections )
            {
                if ( notEmpty ( collection ) )
                {
                    list.addAll ( collection );
                }
            }
        }
        return list;
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
     * Returns whether {@link Collection}s are equal or not.
     *
     * @param collection1 first {@link Collection}
     * @param collection2 second {@link Collection}
     * @return {@code true} if {@link Collection}s are equal, {@code false} otherwise
     */
    public static boolean equals ( final Collection collection1, final Collection collection2 )
    {
        boolean equal = collection1.size () == collection2.size ();
        if ( equal )
        {
            for ( final Object element : collection1 )
            {
                if ( !collection2.contains ( element ) )
                {
                    equal = false;
                    break;
                }
            }
        }
        return equal;
    }

    /**
     * Returns whether {@link List}s are equal or not.
     *
     * @param list1         first {@link List}
     * @param list2         second {@link List}
     * @param strictIndices whether or not {@link List}s should have same elements at the same indices
     * @return {@code true} if {@link List}s are equal, {@code false} otherwise
     */
    public static boolean equals ( final List list1, final List list2, final boolean strictIndices )
    {
        if ( list1 == null && list2 == null )
        {
            return true;
        }
        else if ( list1 == null || list2 == null || list1.size () != list2.size () )
        {
            return false;
        }
        else
        {
            if ( strictIndices )
            {
                for ( int i = 0; i < list1.size (); i++ )
                {
                    if ( Objects.notEquals ( list1.get ( i ), list2.get ( i ) ) )
                    {
                        return false;
                    }
                }
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
                for ( final Object object : list2 )
                {
                    if ( !list1.contains ( object ) )
                    {
                        return false;
                    }
                }
            }
            return true;
        }
    }

    /**
     * Returns an {@code int[]} array created using {@link Collection} of {@link Integer}s.
     *
     * @param collection {@link Collection} of {@link Integer}s
     * @return {@code int[]} array created using {@link Collection} of {@link Integer}s
     */
    public static int[] toIntArray ( final Collection<Integer> collection )
    {
        final int[] array = new int[ collection.size () ];
        int index = 0;
        for ( final Integer integer : collection )
        {
            array[ index ] = integer != null ? integer : 0;
            index++;
        }
        return array;
    }

    /**
     * Returns an {@code Object[]} array created using {@link Collection} of {@link Object}s.
     *
     * @param collection {@link Collection} of {@link Object}s
     * @return {@code Object[]} array created using {@link Collection} of {@link Object}s
     */
    public static Object[] toObjectArray ( final Collection collection )
    {
        final Object[] array = new Object[ collection.size () ];
        int index = 0;
        for ( final Object object : collection )
        {
            array[ index ] = object;
            index++;
        }
        return array;
    }

    /**
     * Returns {@link List} of {@link String}s extracted from the specified elements {@link List}.
     *
     * @param list         {@link List}
     * @param textProvider {@link String} provider
     * @param <T>          {@link List} elements type
     * @return {@link List} of {@link String}s extracted from the specified elements {@link List}
     */
    public static <T> ArrayList<String> toStringList ( final List<T> list, final Function<T, String> textProvider )
    {
        final ArrayList<String> stringList = new ArrayList<String> ( list.size () );
        for ( final T element : list )
        {
            stringList.add ( textProvider.apply ( element ) );
        }
        return stringList;
    }

    /**
     * Returns {@link List} of {@link Object}s converted from array.
     *
     * @param array array
     * @param <T>   array elements type
     * @return {@link List} of {@link Object}s converted from array
     */
    public static <T> ArrayList<T> toList ( final T[] array )
    {
        final ArrayList<T> list = new ArrayList<T> ( array.length );
        Collections.addAll ( list, array );
        return list;
    }

    /**
     * Returns {@link List} of objects converted from {@link Collection}.
     *
     * @param collection {@link Collection}
     * @param <T>        {@link Collection} elements type
     * @return {@link List} of objects converted from {@link Collection}
     */
    public static <T> ArrayList<T> toList ( final Collection<T> collection )
    {
        return new ArrayList<T> ( collection );
    }

    /**
     * Returns a vector of objects converted from collection.
     *
     * @param collection data collection
     * @param <T>        data type
     * @return a vector of objects converted from collection
     */
    public static <T> Vector<T> toVector ( final Collection<T> collection )
    {
        return new Vector<T> ( collection );
    }

    /**
     * Returns a vector of objects converted from data.
     *
     * @param data data
     * @param <T>  data type
     * @return a vector of objects converted from data
     */
    public static <T> Vector<T> asVector ( final T... data )
    {
        final Vector<T> vector = new Vector<T> ( data.length );
        Collections.addAll ( vector, data );
        return vector;
    }

    /**
     * Returns list of elements filtered from collection.
     *
     * @param collection collection to filter
     * @param filter     filter to process
     * @param <T>        elements type
     * @return list of elements filtered from collection
     */
    public static <T> ArrayList<T> filter ( final Collection<T> collection, final Filter<T> filter )
    {
        final ArrayList<T> filtered;
        if ( filter != null )
        {
            filtered = new ArrayList<T> ( collection.size () );
            for ( final T element : collection )
            {
                if ( filter.accept ( element ) )
                {
                    filtered.add ( element );
                }
            }
        }
        else
        {
            filtered = new ArrayList<T> ( collection );
        }
        return filtered;
    }

    /**
     * Returns list of elements filtered from collection.
     *
     * @param collection collection to filter
     * @param filters    filters to process
     * @param <T>        elements type
     * @return list of elements filtered from collection
     */
    public static <T> ArrayList<T> filter ( final Collection<T> collection, final Filter<T>... filters )
    {
        final ArrayList<T> filtered = new ArrayList<T> ( collection.size () );
        for ( final T element : collection )
        {
            for ( int i = 0; i < filters.length; i++ )
            {
                final Filter<T> filter = filters[ i ];
                if ( filter != null && !filter.accept ( element ) )
                {
                    break;
                }
                else if ( i == filters.length - 1 )
                {
                    filtered.add ( element );
                }
            }
        }
        return filtered;
    }

    /**
     * Removes non-distinct {@link List} elements.
     *
     * @param list {@link List} to distinct
     * @param <T>  elements type
     * @return same {@link List} with non-distinct elements removed
     */
    public static <T> List<T> distinct ( final List<T> list )
    {
        final Set<T> seen = new HashSet<T> ( list.size () );
        final Iterator<T> iterator = list.iterator ();
        while ( iterator.hasNext () )
        {
            final T element = iterator.next ();
            if ( !seen.contains ( element ) )
            {
                seen.add ( element );
            }
            else
            {
                iterator.remove ();
            }
        }
        return list;
    }

    /**
     * Sorts {@link List} using the specified {@link Comparator}.
     *
     * @param list       {@link List} to sort
     * @param comparator {@link Comparator}
     * @param <T>        elements type
     * @return same {@link List} but sorted according to the specified {@link Comparator}
     */
    public static <T> List<T> sort ( final List<T> list, final Comparator<T> comparator )
    {
        if ( comparator != null )
        {
            final Object[] array = list.toArray ();
            Arrays.sort ( array, ( Comparator ) comparator );
            final ListIterator<T> iterator = list.listIterator ();
            for ( final Object element : array )
            {
                iterator.next ();
                iterator.set ( ( T ) element );
            }
        }
        return list;
    }

    /**
     * Sorts {@link List} using the specified {@link Comparator}s.
     *
     * @param list        {@link List} to sort
     * @param comparators {@link List} of {@link Comparator}s
     * @param <T>         elements type
     * @return same {@link List} but sorted according to the specified {@link Comparator}
     */
    public static <T> List<T> sort ( final List<T> list, final Comparator<T>... comparators )
    {
        for ( final Comparator<T> comparator : comparators )
        {
            if ( comparator != null )
            {
                sort ( list, comparator );
            }
        }
        return list;
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
     * Returns {@link List} filled with data provided by index mapping {@link Function}.
     *
     * @param size        {@link List} size
     * @param indexMapper index mapping {@link Function}
     * @param <T>         elements type
     * @return {@link List} filled with data provided by index mapping {@link Function}
     */
    public static <T> List<T> fillList ( final int size, final Function<Integer, T> indexMapper )
    {
        final List<T> list = new ArrayList<T> ( size );
        for ( int i = 0; i < size; i++ )
        {
            list.add ( indexMapper.apply ( i ) );
        }
        return list;
    }

    /**
     * Returns data converted into {@link HashSet}.
     *
     * @param data data
     * @param <T>  data type
     * @return data {@link HashSet}
     */
    public static <T> HashSet<T> asHashSet ( final Collection<T> data )
    {
        return new HashSet<T> ( data );
    }

    /**
     * Returns data converted into {@link HashSet}.
     *
     * @param data data
     * @param <T>  data type
     * @return data {@link HashSet}
     */
    public static <T> HashSet<T> asHashSet ( final T... data )
    {
        final HashSet<T> set = new HashSet<T> ( data.length );
        Collections.addAll ( set, data );
        return set;
    }

    /**
     * Returns new {@link List} filled with {@link Integer}s in the specified range.
     *
     * @param from first range integer, inclusive
     * @param to   last range integer, inclusive
     * @return new {@link List} filled with {@link Integer}s in the specified range
     */
    public static List<Integer> intRange ( final int from, final int to )
    {
        final List<Integer> range = new ArrayList<Integer> ( Math.max ( from, to ) - Math.min ( from, to ) + 1 );
        for ( int i = from; i != to; i += from < to ? 1 : -1 )
        {
            range.add ( i );
        }
        range.add ( to );
        return range;
    }

    /**
     * Checks that the specified {@link Collection} is not empty and throws a customized {@link RuntimeException} if it is.
     *
     * @param collection        {@link Collection} to check for emptiness
     * @param exceptionSupplier {@link Supplier} for a customized exception
     * @param <T>               {@link Collection} type
     * @return {@link Collection} if not empty
     * @throws RuntimeException if {@link Collection} is empty
     */
    public static <T extends Collection<?>> T requireNotEmpty ( final T collection, final Supplier<RuntimeException> exceptionSupplier )
    {
        if ( isEmpty ( collection ) )
        {
            throw exceptionSupplier.get ();
        }
        return collection;
    }
}