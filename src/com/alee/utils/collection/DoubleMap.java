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

package com.alee.utils.collection;

import com.alee.utils.MapUtils;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author Mikle Garin
 */

public class DoubleMap<K, V> implements Map<K, V>
{
    /**
     * Values by keys map.
     */
    protected Map<K, V> valuesByKeys;

    /**
     * Keys by values map.
     */
    protected Map<V, K> keysByValues;

    public DoubleMap ()
    {
        this ( 10 );
    }

    public DoubleMap ( final int initialCapacity )
    {
        super ();
        valuesByKeys = new HashMap<K, V> ( initialCapacity );
        keysByValues = new HashMap<V, K> ( initialCapacity );
    }

    public DoubleMap ( final Map<K, V> map )
    {
        this ( map.size () );
        putAll ( map );
    }

    public V getValue ( final K key )
    {
        return valuesByKeys.get ( key );
    }

    public K getKey ( final V value )
    {
        return keysByValues.get ( value );
    }

    public V removeByKey ( final K key )
    {
        final V removed = valuesByKeys.remove ( key );
        if ( removed != null )
        {
            keysByValues.remove ( removed );
        }
        return removed;
    }

    public K removeByValue ( final V value )
    {
        final K removed = keysByValues.remove ( value );
        if ( removed != null )
        {
            valuesByKeys.remove ( removed );
        }
        return removed;
    }

    @Override
    public V put ( final K key, final V value )
    {
        // Removing existing entries with same key or value
        final V v;
        if ( valuesByKeys.containsKey ( key ) )
        {
            v = valuesByKeys.get ( key );
            valuesByKeys.remove ( key );
            keysByValues.remove ( v );
        }
        else
        {
            v = null;
        }
        if ( keysByValues.containsKey ( value ) )
        {
            final K k = keysByValues.get ( value );
            valuesByKeys.remove ( k );
            keysByValues.remove ( value );
        }

        // Adding new entry
        valuesByKeys.put ( key, value );
        keysByValues.put ( value, key );

        return v;
    }

    @Override
    public int size ()
    {
        return valuesByKeys.size ();
    }

    @Override
    public boolean isEmpty ()
    {
        return valuesByKeys.isEmpty ();
    }

    @Override
    public boolean containsKey ( final Object key )
    {
        return valuesByKeys.containsKey ( key );
    }

    @Override
    public boolean containsValue ( final Object value )
    {
        return keysByValues.containsKey ( value );
    }

    @Override
    public V get ( final Object key )
    {
        return valuesByKeys.get ( key );
    }

    @Override
    public V remove ( final Object key )
    {
        return removeByKey ( ( K ) key );
    }

    @Override
    public void putAll ( final Map<? extends K, ? extends V> m )
    {
        for ( final Map.Entry<? extends K, ? extends V> entry : m.entrySet () )
        {
            put ( entry.getKey (), entry.getValue () );
        }
    }

    @Override
    public void clear ()
    {
        valuesByKeys.clear ();
        keysByValues.clear ();
    }

    @Override
    public Set<K> keySet ()
    {
        return valuesByKeys.keySet ();
    }

    @Override
    public Collection<V> values ()
    {
        return valuesByKeys.values ();
    }

    @Override
    public Set<Entry<K, V>> entrySet ()
    {
        return valuesByKeys.entrySet ();
    }

    public Map<K, V> getValuesByKeys ()
    {
        return MapUtils.copyMap ( valuesByKeys );
    }

    public Map<V, K> getKeysByValues ()
    {
        return MapUtils.copyMap ( keysByValues );
    }
}