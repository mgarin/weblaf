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

package com.alee.utils.swing;

import com.alee.api.annotations.NotNull;
import com.alee.api.annotations.Nullable;

import javax.swing.*;
import java.io.Serializable;

/**
 * Utility class providing easy access to a single client property of {@link JComponent}.
 * It is used to decorate {@link JComponent#getClientProperty(Object)} and {@link JComponent#putClientProperty(Object, Object)} methods.
 * It also contains a {@code null}-able default value that will be returned when {@link JComponent} doesn't have property defined.
 *
 * @param <V> value type
 * @author Mikle Garin
 */
public final class NullableClientProperty<V extends Serializable> implements Serializable
{
    /**
     * todo 1. Add Optional return when moved to JDK8+
     */

    /**
     * {@link NullableClientProperty} key.
     */
    @NotNull
    private final String key;

    /**
     * {@link NullableClientProperty} default value.
     */
    @Nullable
    private final V defaultValue;

    /**
     * Constructs new {@link NullableClientProperty}.
     *
     * @param key          {@link NullableClientProperty} key
     * @param defaultValue {@link NullableClientProperty} default value
     */
    public NullableClientProperty ( @NotNull final String key, @Nullable final V defaultValue )
    {
        this.key = key;
        this.defaultValue = defaultValue;
    }

    /**
     * Returns {@link NullableClientProperty} key.
     *
     * @return {@link NullableClientProperty} key
     */
    @NotNull
    public String key ()
    {
        return key;
    }

    /**
     * Returns {@link NullableClientProperty} default value.
     *
     * @return {@link NullableClientProperty} default value
     */
    @Nullable
    public V defaultValue ()
    {
        return defaultValue;
    }

    /**
     * Returns {@link NullableClientProperty} value for the specified {@link JComponent}.
     *
     * @param component {@link JComponent} to retrieve value for
     * @return {@link NullableClientProperty} value for the specified {@link JComponent}
     */
    @Nullable
    public V get ( @NotNull final JComponent component )
    {
        final Object value = component.getClientProperty ( key );
        return value != null ? ( V ) value : defaultValue;
    }

    /**
     * Updates {@link NullableClientProperty} value in the specified {@link JComponent}.
     *
     * @param component {@link JComponent} to update value for
     * @param value     new value
     * @return old {@link NullableClientProperty} value for the specified {@link JComponent}
     */
    @Nullable
    public V set ( @NotNull final JComponent component, @Nullable final V value )
    {
        final Object old = component.getClientProperty ( key );
        component.putClientProperty ( key, value );
        return ( V ) old;
    }

    /**
     * Resets {@link NullableClientProperty} value in the specified {@link JComponent} to {@code null}.
     *
     * @param component {@link JComponent} to reset value for
     * @return old {@link NullableClientProperty} value for the specified {@link JComponent}
     */
    @Nullable
    public V reset ( @NotNull final JComponent component )
    {
        return set ( component, null );
    }
}