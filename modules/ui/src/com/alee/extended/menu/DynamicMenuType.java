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

package com.alee.extended.menu;

/**
 * @author Mikle Garin
 */
public enum DynamicMenuType
{
    /**
     * Round menus.
     */

    /**
     * Roll components to their positions.
     */
    roll,

    /**
     * Move components from middle to their positions
     */
    star,

    /**
     * Camera shutter-like animation.
     */
    shutter,

    /**
     * Components fade in and fade out.
     */
    fade,

    /**
     * Linear menus.
     */

    /**
     * Simple list-like menu
     */
    list;

    public boolean isRoundMenu ()
    {
        return this == roll || this == star || this == shutter || this == fade;
    }

    public boolean isPlainMenu ()
    {
        return this == list;
    }
}