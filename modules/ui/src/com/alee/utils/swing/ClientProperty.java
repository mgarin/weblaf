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
import com.alee.utils.UtilityException;

import javax.swing.*;
import java.io.Serializable;

/**
 * Utility class providing easy access to a single client property of {@link JComponent}.
 * It is used to decorate {@link JComponent#getClientProperty(Object)} and {@link JComponent#putClientProperty(Object, Object)} methods.
 * It also contains a non-{@code null} default value that will be returned when {@link JComponent} doesn't have property defined.
 *
 * @param <V> value type
 * @author Mikle Garin
 */
public final class ClientProperty<V extends Serializable> implements Serializable
{
    /**
     * todo 1. Add Optional return when moved to JDK8+
     */

    /**
     * {@link ClientProperty} key.
     */
    @NotNull
    private final String key;

    /**
     * {@link ClientProperty} default value.
     * It can be {@code null}, but that adds additional checks and possible exceptions in use.
     */
    @Nullable
    private final V defaultValue;

    /**
     * Constructs new {@link ClientProperty}.
     *
     * @param key          {@link ClientProperty} key
     * @param defaultValue {@link ClientProperty} default value
     */
    public ClientProperty ( @NotNull final String key, @Nullable final V defaultValue )
    {
        this.key = key;
        this.defaultValue = defaultValue;
    }

    /**
     * Returns {@link ClientProperty} key.
     *
     * @return {@link ClientProperty} key
     */
    @NotNull
    public String key ()
    {
        return key;
    }

    /**
     * Returns {@link ClientProperty} default value.
     *
     * @return {@link ClientProperty} default value
     */
    @Nullable
    public V defaultValue ()
    {
        return defaultValue;
    }

    /**
     * Returns {@link ClientProperty} value for the specified {@link JComponent}.
     *
     * @param component {@link JComponent} to retrieve value for
     * @return {@link ClientProperty} value for the specified {@link JComponent}
     */
    @NotNull
    public V get ( @NotNull final JComponent component )
    {
        final Object value = component.getClientProperty ( key );
        if ( value != null || defaultValue != null )
        {
            return value != null ? ( V ) value : defaultValue;
        }
        else
        {
            throw new UtilityException ( "Value and default value are both null for client property: " + key );
        }
    }

    /**
     * Updates {@link ClientProperty} value in the specified {@link JComponent}.
     *
     * @param component {@link JComponent} to update value for
     * @param value     new value
     * @return old {@link ClientProperty} value for the specified {@link JComponent}
     */
    @Nullable
    public V set ( @NotNull final JComponent component, @Nullable final V value )
    {
        final Object old = component.getClientProperty ( key );
        if ( value != null || defaultValue != null )
        {
            component.putClientProperty ( key, value );
            return old != null ? ( V ) old : defaultValue;
        }
        else
        {
            throw new UtilityException ( "Provided value and default value are both null for client property: " + key );
        }
    }

    /**
     * Resets {@link ClientProperty} value in the specified {@link JComponent} to {@code null}.
     *
     * @param component {@link JComponent} to reset value for
     * @return old {@link ClientProperty} value for the specified {@link JComponent}
     */
    @Nullable
    public V reset ( @NotNull final JComponent component )
    {
        if ( defaultValue != null )
        {
            return set ( component, null );
        }
        else
        {
            throw new UtilityException ( "Value cannot be reset because default value is null for client property: " + key );
        }
    }
}