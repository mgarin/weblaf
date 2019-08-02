/*
 *  Licensed to the Apache Software Foundation (ASF) under one or more
 *  contributor license agreements.  See the NOTICE file distributed with
 *  this work for additional information regarding copyright ownership.
 *  The ASF licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
 *  the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.alee.utils.map;

import java.util.Iterator;

/**
 * Defines an iterator that operates over a {@code Map}.
 * <p>
 * This iterator is a special version designed for maps. It can be more
 * efficient to use this rather than an entry set iterator where the option
 * is available, and it is certainly more convenient.
 * <p>
 * A map that provides this interface may not hold the data internally using
 * Map Entry objects, thus this interface can avoid lots of object creation.
 * <p>
 * In use, this iterator iterates through the keys in the map. After each call
 * to {@code next()}, the {@code getValue()} method provides direct
 * access to the value. The value can also be set using {@code setValue()}.
 * <pre>
 * MapIterator it = map.mapIterator();
 * while (it.hasNext()) {
 *   Object key = it.next();
 *   Object value = it.getValue();
 *   it.setValue(newValue);
 * }
 * </pre>
 *
 * @param <K> the type of keys maintained by this map
 * @param <V> the type of mapped values
 * @author Stephen Colebourne
 * @version $Revision: 646777 $ $Date: 2008-04-10 14:33:15 +0200 (Thu, 10 Apr 2008) $
 * @since Commons Collections 3.0
 */
public interface MapIterator<K, V> extends Iterator<K>
{
    /**
     * Checks to see if there are more entries still to be iterated.
     *
     * @return {@code true} if the iterator has more elements
     */
    @Override
    public boolean hasNext ();

    /**
     * Gets the next <em>key</em> from the {@code Map}.
     *
     * @return the next key in the iteration
     * @throws java.util.NoSuchElementException if the iteration is finished
     */
    @Override
    public K next ();

    /**
     * Gets the current key, which is the key returned by the last call
     * to {@code next()}.
     *
     * @return the current key
     * @throws IllegalStateException if {@code next()} has not yet been called
     */
    public K getKey ();

    /**
     * Gets the current value, which is the value associated with the last key
     * returned by {@code next()}.
     *
     * @return the current value
     * @throws IllegalStateException if {@code next()} has not yet been called
     */
    public V getValue ();

    /**
     * Removes the last returned key from the underlying {@code Map} (optional operation).
     * <p>
     * This method can be called once per call to {@code next()}.
     *
     * @throws UnsupportedOperationException if remove is not supported by the map
     * @throws IllegalStateException         if {@code next()} has not yet been called
     * @throws IllegalStateException         if {@code remove()} has already been called
     *                                       since the last call to {@code next()}
     */
    @Override
    public void remove ();

    /**
     * Sets the value associated with the current key (optional operation).
     *
     * @param value the new value
     * @return the previous value
     * @throws UnsupportedOperationException if setValue is not supported by the map
     * @throws IllegalStateException         if {@code next()} has not yet been called
     * @throws IllegalStateException         if {@code remove()} has been called since the
     *                                       last call to {@code next()}
     */
    public V setValue ( V value );
}