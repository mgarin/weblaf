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

package com.alee.utils.system;

/**
 * Operation system (shortly OS) type enumeration.
 *
 * @author Mikle Garin
 */
public enum SystemType
{
    /**
     * Windows OS.
     */
    WINDOWS ( "win" ),

    /**
     * Mac OS.
     */
    MAC ( "mac" ),

    /**
     * Unix OS.
     */
    UNIX ( "unix" ),

    /**
     * Solaris OS.
     */
    SOLARIS ( "solaris" ),

    /**
     * Unknown OS type.
     */
    UNKNOWN ( "unknown" );

    /**
     * OS short name.
     */
    private final String shortName;

    /**
     * Constructs new {@link SystemType}.
     *
     * @param shortName OS short name
     */
    private SystemType ( final String shortName )
    {
        this.shortName = shortName;
    }

    /**
     * Returns OS short name.
     *
     * @return OS short name
     */
    public String shortName ()
    {
        return shortName;
    }
}