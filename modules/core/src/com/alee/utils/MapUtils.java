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
     * Returns copied Map.
     *
     * @param map Map to copy
     * @param <K> Map key type
     * @param <V> Map value type
     * @return copied Map
     */
    public static <K, V> Map<K, V> copyMap ( final Map<K, V> map )
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
     * Returns Map with cloned values.
     *
     * @param map Map to clone
     * @param <K> Map key type
     * @param <V> Map value type
     * @return cloned Map
     */
    public static <K, V extends Cloneable> Map<K, V> cloneMap ( final Map<K, V> map )
    {
        final Map<K, V> clone = new HashMap<K, V> ( map.size () );
        for ( final Map.Entry<K, V> entry : map.entrySet () )
        {
            clone.put ( entry.getKey (), ReflectUtils.cloneSafely ( entry.getValue () ) );
        }
        return clone;
    }

    /**
     * Returns HashMap with cloned values.
     *
     * @param map HashMap to clone
     * @param <K> HashMap key type
     * @param <V> HashMap value type
     * @return cloned HashMap
     */
    public static <K, V extends Cloneable> HashMap<K, V> cloneHashMap ( final HashMap<K, V> map )
    {
        final HashMap<K, V> clone = new HashMap<K, V> ( map.size () );
        for ( final Map.Entry<K, V> entry : map.entrySet () )
        {
            clone.put ( entry.getKey (), ReflectUtils.cloneSafely ( entry.getValue () ) );
        }
        return clone;
    }

    /**
     * Returns LinkedHashMap with cloned values.
     *
     * @param map LinkedHashMap to clone
     * @param <K> LinkedHashMap key type
     * @param <V> LinkedHashMap value type
     * @return cloned LinkedHashMap
     */
    public static <K, V extends Cloneable> LinkedHashMap<K, V> cloneLinkedHashMap ( final LinkedHashMap<K, V> map )
    {
        final LinkedHashMap<K, V> clone = new LinkedHashMap<K, V> ( map.size () );
        for ( final Map.Entry<K, V> entry : map.entrySet () )
        {
            clone.put ( entry.getKey (), ReflectUtils.cloneSafely ( entry.getValue () ) );
        }
        return clone;
    }

    /**
     * Returns DoubleMap with cloned values.
     *
     * @param map DoubleMap to clone
     * @param <K> DoubleMap key type
     * @param <V> DoubleMap value type
     * @return cloned DoubleMap
     */
    public static <K, V extends Cloneable> DoubleMap<K, V> cloneLinkedHashMap ( final DoubleMap<K, V> map )
    {
        final DoubleMap<K, V> clone = new DoubleMap<K, V> ( map.size () );
        for ( final Map.Entry<K, V> entry : map.entrySet () )
        {
            clone.put ( entry.getKey (), ReflectUtils.cloneSafely ( entry.getValue () ) );
        }
        return clone;
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
     * Returns newly created LinkedHashMap with the specified key and value pair added.
     *
     * @param key   key to add
     * @param value value to add
     * @param <K>   key type
     * @param <V>   value type
     * @return newly created LinkedHashMap
     */
    public static <K, V> HashMap<K, V> newLinkedHashMap ( final K key, final V value )
    {
        final LinkedHashMap<K, V> map = new LinkedHashMap<K, V> ( 1 );
        map.put ( key, value );
        return map;
    }
}