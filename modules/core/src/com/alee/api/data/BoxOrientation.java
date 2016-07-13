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

package com.alee.api.data;

import javax.swing.*;

/**
 * Enumeration representing box orientation constants.
 * It is designed to conveniently provide state titles for various components.
 *
 * @author Mikle Garin
 */

public enum BoxOrientation implements SwingConstants
{
    center ( CENTER ),
    top ( TOP ),
    left ( LEADING ),
    bottom ( BOTTOM ),
    right ( TRAILING );

    /**
     * Constant value.
     */
    private final int value;

    /**
     * Constructs new enumeration literal based on constant value.
     *
     * @param value constant value
     */
    private BoxOrientation ( final int value )
    {
        this.value = value;
    }

    /**
     * Returns constant value.
     *
     * @return constant value
     */
    public int getValue ()
    {
        return value;
    }

    /**
     * Returns enumeration literal referencing constant value.
     *
     * @param value constant value
     * @return enumeration literal referencing constant value
     */
    public static BoxOrientation get ( final int value )
    {
        switch ( value )
        {
            case TOP:
                return top;

            case LEADING:
            case LEFT:
                return left;

            case BOTTOM:
                return bottom;

            case TRAILING:
            case RIGHT:
                return right;

            case CENTER:
            default:
                return center;
        }
    }
}