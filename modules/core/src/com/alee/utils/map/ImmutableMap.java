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

import com.alee.utils.MapUtils;
import com.alee.utils.collection.ImmutableCollection;
import com.alee.utils.collection.ImmutableSet;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * {@link Map} implementation that doesn't allow any data modifications to be done.
 * Unlike {@link java.util.Collections#unmodifiableMap(Map)} this implementation keeps map data copy.
 * If you need to provide an unmodifiable reference for your map use {@link java.util.Collections} implementations or write your own.
 *
 * @param <K> key type
 * @param <V> value type
 * @author Mikle Garin
 */
public class ImmutableMap<K, V> implements Map<K, V>, Cloneable, Serializable
{
    /**
     * Map data.
     */
    protected final HashMap<K, V> data;

    /**
     * Constructs new {@link ImmutableMap} based on the provided data.
     *
     * @param data mixed keys and values
     */
    public ImmutableMap ( final Object... data )
    {
        super ();
        this.data = MapUtils.newHashMap ( data );
    }

    /**
     * Constructs new {@link ImmutableMap} based on the provided map.
     *
     * @param map data {@link Map}
     */
    public ImmutableMap ( final Map<K, V> map )
    {
        super ();
        this.data = new HashMap<K, V> ( map );
    }

    @Override
    public int size ()
    {
        return data.size ();
    }

    @Override
    public boolean isEmpty ()
    {
        return data.isEmpty ();
    }

    @Override
    public boolean containsKey ( final Object key )
    {
        return data.containsKey ( key );
    }

    @Override
    public boolean containsValue ( final Object value )
    {
        return data.containsValue ( value );
    }

    @Override
    public V get ( final Object key )
    {
        return data.get ( key );
    }

    @Override
    public V put ( final K key, final V value )
    {
        throw createModificationException ();
    }

    @Override
    public V remove ( final Object key )
    {
        throw createModificationException ();
    }

    @Override
    public void putAll ( final Map<? extends K, ? extends V> m )
    {
        throw createModificationException ();
    }

    @Override
    public void clear ()
    {
        throw createModificationException ();
    }

    @Override
    public Set<K> keySet ()
    {
        return new ImmutableSet<K> ( data.keySet () );
    }

    @Override
    public Collection<V> values ()
    {
        return new ImmutableCollection<V> ( data.values () );
    }

    @Override
    public Set<Entry<K, V>> entrySet ()
    {
        return new ImmutableSet<Entry<K, V>> ( data.entrySet () );
    }

    /**
     * Returns new {@link UnsupportedOperationException} instance specific for this implementation.
     *
     * @return new {@link UnsupportedOperationException} instance specific for this implementation
     */
    protected UnsupportedOperationException createModificationException ()
    {
        return new UnsupportedOperationException ( "Map is unmodifiable" );
    }
}