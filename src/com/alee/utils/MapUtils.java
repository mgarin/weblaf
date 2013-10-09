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
    public static <K, V> Map<K, V> copyMap ( Map<K, V> map )
    {
        Map<K, V> clone = new HashMap<K, V> ();
        for ( Map.Entry<K, V> entry : map.entrySet () )
        {
            clone.put ( entry.getKey (), entry.getValue () );
        }
        return clone;
    }

    /**
     * Returns copied HashMap.
     *
     * @param map HashMap to copy
     * @param <K> HashMap key type
     * @param <V> HashMap value type
     * @return copied HashMap
     */
    public static <K, V> HashMap<K, V> copyHashMap ( HashMap<K, V> map )
    {
        HashMap<K, V> clone = new HashMap<K, V> ();
        for ( Map.Entry<K, V> entry : map.entrySet () )
        {
            clone.put ( entry.getKey (), entry.getValue () );
        }
        return clone;
    }

    /**
     * Returns copied LinkedHashMap.
     *
     * @param map LinkedHashMap to copy
     * @param <K> LinkedHashMap key type
     * @param <V> LinkedHashMap value type
     * @return copied LinkedHashMap
     */
    public static <K, V> LinkedHashMap<K, V> copyLinkedHashMap ( LinkedHashMap<K, V> map )
    {
        LinkedHashMap<K, V> clone = new LinkedHashMap<K, V> ();
        for ( Map.Entry<K, V> entry : map.entrySet () )
        {
            clone.put ( entry.getKey (), entry.getValue () );
        }
        return clone;
    }

    /**
     * Returns Map with clone values.
     *
     * @param map Map to clone
     * @param <K> Map key type
     * @param <V> Map value type
     * @return cloned Map
     */
    public static <K, V extends Cloneable> Map<K, V> cloneMap ( Map<K, V> map )
    {
        Map<K, V> clone = new HashMap<K, V> ();
        for ( Map.Entry<K, V> entry : map.entrySet () )
        {
            clone.put ( entry.getKey (), ReflectUtils.cloneSafely ( entry.getValue () ) );
        }
        return clone;
    }

    /**
     * Returns HashMap with clone values.
     *
     * @param map HashMap to clone
     * @param <K> HashMap key type
     * @param <V> HashMap value type
     * @return cloned HashMap
     */
    public static <K, V extends Cloneable> HashMap<K, V> cloneHashMap ( HashMap<K, V> map )
    {
        HashMap<K, V> clone = new HashMap<K, V> ();
        for ( Map.Entry<K, V> entry : map.entrySet () )
        {
            clone.put ( entry.getKey (), ReflectUtils.cloneSafely ( entry.getValue () ) );
        }
        return clone;
    }

    /**
     * Returns LinkedHashMap with clone values.
     *
     * @param map LinkedHashMap to clone
     * @param <K> LinkedHashMap key type
     * @param <V> LinkedHashMap value type
     * @return cloned LinkedHashMap
     */
    public static <K, V extends Cloneable> LinkedHashMap<K, V> cloneLinkedHashMap ( LinkedHashMap<K, V> map )
    {
        LinkedHashMap<K, V> clone = new LinkedHashMap<K, V> ();
        for ( Map.Entry<K, V> entry : map.entrySet () )
        {
            clone.put ( entry.getKey (), ReflectUtils.cloneSafely ( entry.getValue () ) );
        }
        return clone;
    }
}