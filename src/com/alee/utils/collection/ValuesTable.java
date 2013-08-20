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

import com.alee.utils.CollectionUtils;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamConverter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * This class allows you to create key-value table from which you can either request a key by value or a value by key.
 * Both key and value should be unique and adding same key or value will replace existing one in the lists.
 *
 * @author Mikle Garin
 */

@XStreamAlias ("ValuesTable")
@XStreamConverter (ValuesTableConverter.class)
public class ValuesTable<K, V> implements Serializable
{
    // Keys list
    private List<K> keys;

    // Values list
    private List<V> values;

    /**
     * Constructs an empty ValuesTable with initial capacity of ten.
     */
    public ValuesTable ()
    {
        this ( 10 );
    }

    /**
     * Constructs an empty ValuesTable with the specified initial capacity.
     */
    public ValuesTable ( int initialCapacity )
    {
        super ();
        keys = new ArrayList<K> ( initialCapacity );
        values = new ArrayList<V> ( initialCapacity );
    }

    /**
     * Returns copy of keys list.
     *
     * @return keys list copy
     */
    public List<K> getKeys ()
    {
        return CollectionUtils.copy ( keys );
    }

    /**
     * Returns copy of values list.
     *
     * @return values list copy
     */
    public List<V> getValues ()
    {
        return CollectionUtils.copy ( values );
    }

    /**
     * Adds all Map entries into this ValuesTable.
     *
     * @param data Map with entries
     */
    public void addAll ( Map<K, V> data )
    {
        putAll ( data );
    }

    /**
     * Adds all Map entries into this ValuesTable.
     *
     * @param data Map with entries
     */
    public void putAll ( Map<K, V> data )
    {
        for ( Map.Entry<K, V> entry : data.entrySet () )
        {
            put ( entry.getKey (), entry.getValue () );
        }
    }

    /**
     * Adds all ValuesTable entries into this ValuesTable.
     *
     * @param data ValuesTable with entries
     */
    public void addAll ( ValuesTable<K, V> data )
    {
        putAll ( data );
    }

    /**
     * Adds all ValuesTable entries into this ValuesTable.
     *
     * @param data ValuesTable with entries
     */
    public void putAll ( ValuesTable<K, V> data )
    {
        for ( int i = 0; i < data.size (); i++ )
        {
            put ( data.getKey ( i ), data.getValue ( i ) );
        }
    }

    /**
     * Adds a single entry (key-value pair) into this ValuesTable.
     *
     * @param key   entry key
     * @param value entry value
     */
    public void add ( K key, V value )
    {
        put ( key, value );
    }

    /**
     * Adds a single entry (key-value pair) into this ValuesTable.
     *
     * @param key   entry key
     * @param value entry value
     */
    public void put ( K key, V value )
    {
        put ( keys.size (), key, value );
    }

    /**
     * Adds a single entry (key-value pair) into this ValuesTable at the specified index.
     *
     * @param index entry index
     * @param key   entry key
     * @param value entry value
     */
    public void add ( int index, K key, V value )
    {
        put ( index, key, value );
    }

    /**
     * Adds a single entry (key-value pair) into this ValuesTable at the specified index.
     *
     * @param index entry index
     * @param key   entry key
     * @param value entry value
     */
    public void put ( int index, K key, V value )
    {
        // Searching for existing key
        int existingKeyIndex = -1;
        if ( keys.contains ( key ) )
        {
            existingKeyIndex = indexOfKey ( key );
        }

        // Searching for existing value
        int existingValueIndex = -1;
        if ( values.contains ( value ) )
        {
            existingValueIndex = indexOfValue ( value );
        }

        // Removing existing records
        if ( existingKeyIndex != -1 || existingValueIndex != -1 )
        {
            if ( existingKeyIndex > existingValueIndex )
            {
                if ( existingKeyIndex != -1 )
                {
                    keys.remove ( existingKeyIndex );
                    values.remove ( existingKeyIndex );
                }
                if ( existingValueIndex != -1 )
                {
                    keys.remove ( existingValueIndex );
                    values.remove ( existingValueIndex );
                }
            }
            else
            {
                if ( existingValueIndex != -1 )
                {
                    keys.remove ( existingValueIndex );
                    values.remove ( existingValueIndex );
                }
                if ( existingKeyIndex != -1 )
                {
                    keys.remove ( existingKeyIndex );
                    values.remove ( existingKeyIndex );
                }
            }
        }

        // Changing insert index
        if ( existingKeyIndex != -1 && existingKeyIndex < index && existingValueIndex != -1 &&
                existingValueIndex < index )
        {
            index -= 2;
        }
        else if ( existingKeyIndex != -1 && existingKeyIndex < index || existingValueIndex != -1 && existingValueIndex < index )
        {
            index--;
        }

        // Adding new key-value pair
        keys.add ( index, key );
        values.add ( index, value );
    }

    /**
     * Removes entry with the specified key.
     *
     * @param key entry key
     */
    public void remove ( K key )
    {
        removeByKey ( key );
    }

    /**
     * Removes entry with the specified key.
     *
     * @param key entry key
     */
    public void removeByKey ( K key )
    {
        remove ( indexOfKey ( key ) );
    }

    /**
     * Removes entry with the specified value.
     *
     * @param value entry value
     */
    public void removeByValue ( V value )
    {
        remove ( indexOfValue ( value ) );
    }

    /**
     * Removes entry at the specified index.
     *
     * @param index entry index
     */
    public void remove ( int index )
    {
        if ( index >= 0 && index < keys.size () )
        {
            keys.remove ( index );
            values.remove ( index );
        }
        else
        {
            throw new IndexOutOfBoundsException ( outOfBoundsMsg ( index ) );
        }
    }

    /**
     * Returns entry value at the specified index.
     *
     * @param index entry index
     * @return entry value
     */
    public V get ( int index )
    {
        return getValue ( index );
    }

    /**
     * Returns entry value at the specified index.
     *
     * @param index entry index
     * @return entry value
     */
    public V getValue ( int index )
    {
        return values.get ( index );
    }

    /**
     * Returns entry key at the specified index.
     *
     * @param index entry index
     * @return entry key
     */
    public K getKey ( int index )
    {
        return keys.get ( index );
    }


    /**
     * Returns value from entry with specified key.
     *
     * @param key entry key
     * @return entry value
     */
    public V get ( K key )
    {
        return getValue ( key );
    }

    /**
     * Returns value from entry with specified key.
     *
     * @param key entry key
     * @return entry value
     */
    public V getValue ( K key )
    {
        int index = indexOfKey ( key );
        return index != -1 ? values.get ( index ) : null;
    }

    /**
     * Returns key from entry with specified value.
     *
     * @param value entry value
     * @return entry key
     */
    public K getKey ( V value )
    {
        int index = indexOfValue ( value );
        return index != -1 ? keys.get ( index ) : null;
    }

    /**
     * Returns whether this ValuesTable contains entry with specified key or not.
     *
     * @param key entry key
     * @return true if this ValuesTable contains such entry, false otherwise
     */
    public boolean contains ( K key )
    {
        return keys.contains ( key );
    }

    /**
     * Returns whether this ValuesTable contains entry with specified key or not.
     *
     * @param key entry key
     * @return true if this ValuesTable contains such entry, false otherwise
     */
    public boolean containsKey ( K key )
    {
        return keys.contains ( key );
    }

    /**
     * Returns whether this ValuesTable contains entry with specified value or not.
     *
     * @param value entry value
     * @return true if this ValuesTable contains such entry, false otherwise
     */
    public boolean containsValue ( V value )
    {
        return values.contains ( value );
    }

    /**
     * Returns index of entry with the specified key.
     *
     * @param key entry key
     * @return entry index
     */
    public int indexOf ( K key )
    {
        return indexOfKey ( key );
    }

    /**
     * Returns index of entry with the specified key.
     *
     * @param key entry key
     * @return entry index
     */
    public int indexOfKey ( K key )
    {
        return keys.indexOf ( key );
    }

    /**
     * Returns index of entry with the specified value.
     *
     * @param value entry value
     * @return entry index
     */
    public int indexOfValue ( V value )
    {
        return values.indexOf ( value );
    }

    /**
     * Returns ValuesTable size.
     *
     * @return ValuesTable size
     */
    public int size ()
    {
        return keys.size ();
    }

    /**
     * Returns a message text for IndexOutOfBoundsException exception.
     *
     * @param index index that is out of bounds
     * @return message text
     */
    private String outOfBoundsMsg ( int index )
    {
        return "Index: " + index + ", Size: " + size ();
    }
}