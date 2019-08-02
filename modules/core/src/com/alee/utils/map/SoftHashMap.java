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
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.util.*;

/**
 * Map implementation that holds soft references its values.
 * This map might be useful to hold memory-sensitive cache.
 *
 * @param <K> key type
 * @param <V> value type
 * @author Dr. Heinz M. Kabutz
 * @see <a href="http://www.javaspecialists.co.za/archive/Issue098.html">The Java Specialists' Newsletter [Issue 098]</a>
 */
public class SoftHashMap<K, V> extends AbstractMap<K, V> implements Serializable
{
    /**
     * The internal HashMap that holds the SoftReference.
     */
    private final Map<K, SoftReference<V>> hash = new HashMap<K, SoftReference<V>> ();

    /**
     * Reversed map.
     */
    private final Map<SoftReference<V>, K> reverseLookup = new HashMap<SoftReference<V>, K> ();

    /**
     * Reference queue for cleared SoftReference objects.
     */
    private final ReferenceQueue<V> queue = new ReferenceQueue<V> ();

    @Override
    public V get ( final Object key )
    {
        expungeStaleEntries ();
        V result = null;
        // We get the SoftReference represented by that key
        final SoftReference<V> soft_ref = hash.get ( key );
        if ( soft_ref != null )
        {
            // From the SoftReference we get the value, which can be
            // null if it has been garbage collected
            result = soft_ref.get ();
            if ( result == null )
            {
                // If the value has been garbage collected, remove the
                // entry from the HashMap.
                hash.remove ( key );
                reverseLookup.remove ( soft_ref );
            }
        }
        return result;
    }

    /**
     * Removes stale entries.
     */
    private void expungeStaleEntries ()
    {
        Reference<? extends V> sv;
        while ( ( sv = queue.poll () ) != null )
        {
            hash.remove ( reverseLookup.remove ( sv ) );
        }
    }

    @Override
    public V put ( final K key, final V value )
    {
        expungeStaleEntries ();
        final SoftReference<V> soft_ref = new SoftReference<V> ( value, queue );
        reverseLookup.put ( soft_ref, key );
        final SoftReference<V> result = hash.put ( key, soft_ref );
        if ( result == null )
        {
            return null;
        }
        return result.get ();
    }

    @Override
    public V remove ( final Object key )
    {
        expungeStaleEntries ();
        final SoftReference<V> result = hash.remove ( key );
        if ( result == null )
        {
            return null;
        }
        return result.get ();
    }

    @Override
    public void clear ()
    {
        hash.clear ();
        reverseLookup.clear ();
    }

    @Override
    public int size ()
    {
        expungeStaleEntries ();
        return hash.size ();
    }

    @Override
    public Set<Entry<K, V>> entrySet ()
    {
        expungeStaleEntries ();
        final Set<Entry<K, V>> result = new LinkedHashSet<Entry<K, V>> ();
        for ( final Entry<K, SoftReference<V>> entry : hash.entrySet () )
        {
            final V value = entry.getValue ().get ();
            if ( value != null )
            {
                result.add ( new Entry<K, V> ()
                {
                    @Override
                    public K getKey ()
                    {
                        return entry.getKey ();
                    }

                    @Override
                    public V getValue ()
                    {
                        return value;
                    }

                    @Override
                    public V setValue ( final V v )
                    {
                        entry.setValue ( new SoftReference<V> ( v, queue ) );
                        return value;
                    }
                } );
            }
        }
        return result;
    }
}