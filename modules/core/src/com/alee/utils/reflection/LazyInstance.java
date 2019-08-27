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

package com.alee.utils.reflection;

import com.alee.api.annotations.NotNull;
import com.alee.api.annotations.Nullable;
import com.alee.utils.ReflectUtils;

/**
 * Contains information required to create an instance of the {@link #clazz} using constructor with the specified {@link #arguments}.
 * This object might be useful as an instance factory or as lazy instance instantiator.
 *
 * @param <T> object type
 * @author Mikle Garin
 */
public final class LazyInstance<T>
{
    /**
     * Object {@link Class}.
     */
    @NotNull
    private final Class<? extends T> clazz;

    /**
     * Arguments for object constructor.
     */
    @NotNull
    private final Object[] arguments;

    /**
     * Singleton instance.
     */
    @Nullable
    private T instance;

    /**
     * Constructs new {@link LazyInstance}.
     *
     * @param clazz     object {@link Class}
     * @param arguments arguments for object constructor
     */
    public LazyInstance ( @NotNull final Class<? extends T> clazz, @NotNull final Object... arguments )
    {
        this.clazz = clazz;
        this.arguments = arguments;
    }

    /**
     * Returns new object instance.
     * Can be called multiple times on the same instance of {@link LazyInstance}.
     *
     * @return new object instance
     */
    @NotNull
    public T create ()
    {
        try
        {
            return ReflectUtils.createInstance ( clazz, arguments );
        }
        catch ( final Exception e )
        {
            final String msg = "Unable to instantiate skin for class: %s";
            throw new ReflectionException ( String.format ( msg, clazz ), e );
        }
    }

    /**
     * Returns singleton object instance.
     * It will always return the same instance for the same {@link LazyInstance}.
     *
     * @return singleton object instance
     */
    @NotNull
    public synchronized T get ()
    {
        if ( instance == null )
        {
            instance = create ();
        }
        return instance;
    }
}