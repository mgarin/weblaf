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

package com.alee.api.resource;

import com.alee.api.annotations.NotNull;

/**
 * Special unchecked exception used to display various resource problems.
 *
 * @author Mikle Garin
 */
public final class ResourceException extends RuntimeException
{
    /**
     * Constructs a new {@link ResourceException} with {@code null} as its detail message.
     */
    public ResourceException ()
    {
        super ();
    }

    /**
     * Constructs a new {@link ResourceException} with the specified detail message.
     *
     * @param message exception message
     */
    public ResourceException ( @NotNull final String message )
    {
        super ( message );
    }

    /**
     * Constructs a new {@link ResourceException} with the specified detail message and cause.
     *
     * @param message exception message
     * @param cause   exception cause
     */
    public ResourceException ( @NotNull final String message, @NotNull final Throwable cause )
    {
        super ( message, cause );
    }

    /**
     * Constructs a new {@link ResourceException} with the specified cause.
     *
     * @param cause exception cause
     */
    public ResourceException ( @NotNull final Throwable cause )
    {
        super ( cause );
    }
}