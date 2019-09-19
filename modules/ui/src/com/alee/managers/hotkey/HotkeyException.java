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

package com.alee.managers.hotkey;

/**
 * Special exception class used to display various hotkey problems.
 *
 * @author Mikle Garin
 */
public final class HotkeyException extends RuntimeException
{
    /**
     * Constructs a new {@link HotkeyException} with {@code null} as its detail message.
     */
    public HotkeyException ()
    {
        super ();
    }

    /**
     * Constructs a new {@link HotkeyException} with the specified detail message.
     *
     * @param message exception message
     */
    public HotkeyException ( final String message )
    {
        super ( message );
    }

    /**
     * Constructs a new {@link HotkeyException} with the specified detail message and cause.
     *
     * @param message exception message
     * @param cause   exception cause
     */
    public HotkeyException ( final String message, final Throwable cause )
    {
        super ( message, cause );
    }

    /**
     * Constructs a new {@link HotkeyException} with the specified cause.
     *
     * @param cause exception cause
     */
    public HotkeyException ( final Throwable cause )
    {
        super ( cause );
    }
}