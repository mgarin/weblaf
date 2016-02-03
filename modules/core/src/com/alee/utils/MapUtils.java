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

import com.alee.utils.collection.DoubleMap;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * This class provides a set of utilities to work with various maps.
 *
 * @author Mikle Garin
 */

public final class MapUtils
{
    /**
     * Returns whether specified map is empty or not.
     *
     * @param map map to process
     * @return true if specified map is empty, false otherwise
     */
    public static boolean isEmpty ( final Map<?, ?> map )
    {
        return map == null || map.isEmpty ();
    }

    /**
     * Returns copied Map.
     *
     * @param map Map to copy
     * @param <K> Map key type
     * @param <V> Map value type
     * @return copied Map
     */
    public static <K, V> HashMap<K, V> copyMap ( final Map<K, V> map )
    {
        return new HashMap<K, V> ( map );
    }

    /**
     * Returns copied HashMap.
     *
     * @param map HashMap to copy
     * @param <K> HashMap key type
     * @param <V> HashMap value type
     * @return copied HashMap
     */
    public static <K, V> HashMap<K, V> copyHashMap ( final HashMap<K, V> map )
    {
        return new HashMap<K, V> ( map );
    }

    /**
     * Returns copied LinkedHashMap.
     *
     * @param map LinkedHashMap to copy
     * @param <K> LinkedHashMap key type
     * @param <V> LinkedHashMap value type
     * @return copied LinkedHashMap
     */
    public static <K, V> LinkedHashMap<K, V> copyLinkedHashMap ( final LinkedHashMap<K, V> map )
    {
        return new LinkedHashMap<K, V> ( map );
    }

    /**
     * Returns copied DoubleMap.
     *
     * @param map DoubleMap to copy
     * @param <K> DoubleMap key type
     * @param <V> DoubleMap value type
     * @return copied DoubleMap
     */
    public static <K, V> DoubleMap<K, V> copyDoubleMap ( final DoubleMap<K, V> map )
    {
        return new DoubleMap<K, V> ( map );
    }

    /**
     * Returns newly created HashMap with the specified key and value pair added.
     *
     * @param key   key to add
     * @param value value to add
     * @param <K>   key type
     * @param <V>   value type
     * @return newly created HashMap
     */
    public static <K, V> HashMap<K, V> newHashMap ( final K key, final V value )
    {
        final HashMap<K, V> map = new HashMap<K, V> ( 1 );
        map.put ( key, value );
        return map;
    }

    /**
     * Returns newly created HashMap with the specified key and value pairs added.
     *
     * @param objects key-value pairs
     * @param <K>     key type
     * @param <V>     value type
     * @return newly created HashMap
     */
    public static <K, V> HashMap<K, V> newHashMap ( final Object... objects )
    {
        if ( objects != null && objects.length > 0 )
        {
            if ( objects.length % 2 == 0 )
            {
                final HashMap<K, V> map = new HashMap<K, V> ( 1 );
                for ( int i = 0; i < objects.length; i += 2 )
                {
                    map.put ( ( K ) objects[ i ], ( V ) objects[ i + 1 ] );
                }
                return map;
            }
            else
            {
                throw new RuntimeException ( "Amount of key-value objects must be even" );
            }
        }
        else
        {
            return new HashMap<K, V> ( 0 );
        }
    }

    /**
     * Returns newly created LinkedHashMap with the specified key and value pair added.
     *
     * @param key   key to add
     * @param value value to add
     * @param <K>   key type
     * @param <V>   value type
     * @return newly created LinkedHashMap
     */
    public static <K, V> LinkedHashMap<K, V> newLinkedHashMap ( final K key, final V value )
    {
        final LinkedHashMap<K, V> map = new LinkedHashMap<K, V> ( 1 );
        map.put ( key, value );
        return map;
    }

    /**
     * Returns newly created LinkedHashMap with the specified key and value pairs added.
     *
     * @param objects key-value pairs
     * @param <K>     key type
     * @param <V>     value type
     * @return newly created LinkedHashMap
     */
    public static <K, V> LinkedHashMap<K, V> newLinkedHashMap ( final Object... objects )
    {
        if ( objects != null && objects.length > 0 )
        {
            if ( objects.length % 2 == 0 )
            {
                final LinkedHashMap<K, V> map = new LinkedHashMap<K, V> ( 1 );
                for ( int i = 0; i < objects.length; i += 2 )
                {
                    map.put ( ( K ) objects[ i ], ( V ) objects[ i + 1 ] );
                }
                return map;
            }
            else
            {
                throw new RuntimeException ( "Amount of key-value objects must be even" );
            }
        }
        else
        {
            return new LinkedHashMap<K, V> ( 0 );
        }
    }

    /**
     * Removes all map entries where value is the same as the specified one.
     *
     * @param map   map to remove entries from
     * @param value value for which entries should be removed
     * @param <K>   key type
     * @param <V>   value type
     */
    public static <K, V> void removeAllValues ( final Map<K, V> map, final V value )
    {
        final Iterator<Map.Entry<K, V>> iterator = map.entrySet ().iterator ();
        while ( iterator.hasNext () )
        {
            final Map.Entry<K, V> entry = iterator.next ();
            if ( CompareUtils.equals ( entry.getValue (), value ) )
            {
                iterator.remove ();
            }
        }
    }

    /**
     * Merges specified maps into one new map and returns it.
     *
     * @param maps maps to merge into new one
     * @param <K>  key type
     * @param <V>  value type
     * @return new map containing all provided maps merged into it
     */
    public static <K, V> HashMap<K, V> merge ( final Map<K, V>... maps )
    {
        // Preparing new map size
        int size = 0;
        for ( final Map<K, V> map : maps )
        {
            if ( map != null )
            {
                size += map.size ();
            }
        }

        // Creating and filling new map
        final HashMap<K, V> merged = new HashMap<K, V> ( size );
        for ( final Map<K, V> map : maps )
        {
            if ( map != null )
            {
                merged.putAll ( map );
            }
        }
        return merged;
    }
}