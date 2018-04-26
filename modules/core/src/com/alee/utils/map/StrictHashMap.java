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

package com.alee.utils.map;

import java.io.Serializable;
import java.util.Map;

/**
 * {@link Map} implementation that uses direct objects comparison for keys and values instead of relying on {@link Object#equals(Object)}.
 *
 * @param <K> the type of keys maintained by this map
 * @param <V> the type of mapped values
 * @author Mikle Garin
 * @see AbstractHashMap
 */
public class StrictHashMap<K, V> extends AbstractHashMap<K, V> implements Serializable
{
    /**
     * Constructs new {@link StrictHashMap}.
     */
    public StrictHashMap ()
    {
        super ( DEFAULT_CAPACITY );
    }

    /**
     * Constructs new {@link StrictHashMap}.
     *
     * @param initialCapacity the initial capacity, must be a power of two
     */
    public StrictHashMap ( final int initialCapacity )
    {
        super ( initialCapacity );
    }

    /**
     * Constructs new {@link StrictHashMap}.
     *
     * @param initialCapacity the initial capacity, must be a power of two
     * @param loadFactor      the load factor, must be &gt; 0.0f and generally &lt; 1.0f
     */
    public StrictHashMap ( final int initialCapacity, final float loadFactor )
    {
        super ( initialCapacity, loadFactor );
    }

    /**
     * Constructs new {@link StrictHashMap}.
     *
     * @param initialCapacity the initial capacity, must be a power of two
     * @param loadFactor      the load factor, must be &gt; 0.0f and generally &lt; 1.0f
     * @param threshold       the threshold, must be sensible
     */
    public StrictHashMap ( final int initialCapacity, final float loadFactor, final int threshold )
    {
        super ( initialCapacity, loadFactor, threshold );
    }

    /**
     * Constructs new {@link StrictHashMap}.
     *
     * @param map the map to copy
     */
    public StrictHashMap ( final Map map )
    {
        super ( map );
    }

    @Override
    protected boolean isEqualKey ( final Object key1, final Object key2 )
    {
        return key1 == key2;
    }

    @Override
    protected boolean isEqualValue ( final Object value1, final Object value2 )
    {
        return value1 == value2;
    }
}