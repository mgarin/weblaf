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

package com.alee.utils.parsing;

/**
 * Exception that may be thrown upon parsing text units.
 *
 * @author Mikle Garin
 */

public class UnitsParsingException extends IllegalArgumentException
{
    /**
     * Constructs new {@link UnitsParsingException}.
     *
     * @param message exception message
     */
    public UnitsParsingException ( final String message )
    {
        super ( message );
    }

    /**
     * Constructs new {@link UnitsParsingException}.
     *
     * @param cause {@link Throwable} cause
     */
    public UnitsParsingException ( final Throwable cause )
    {
        super ( cause );
    }

    /**
     * Constructs new {@link UnitsParsingException}.
     *
     * @param message exception message
     * @param cause   {@link Throwable} cause
     */
    public UnitsParsingException ( final String message, final Throwable cause )
    {
        super ( message, cause );
    }
}