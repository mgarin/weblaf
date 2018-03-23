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
 * Enumeration representing orientation constants.
 * It is designed to conveniently provide state titles for various components.
 *
 * @author Mikle Garin
 */

public enum Orientation implements SwingConstants
{
    /**
     * Horizontal orientation.
     *
     * @see #HORIZONTAL
     */
    horizontal ( HORIZONTAL ),

    /**
     * Vertical orientation.
     *
     * @see #VERTICAL
     */
    vertical ( VERTICAL );

    /**
     * Constant value.
     */
    private final int value;

    /**
     * Constructs new enumeration literal based on constant value.
     *
     * @param value constant value
     */
    private Orientation ( final int value )
    {
        this.value = value;
    }

    /**
     * Returns {@link Orientation} opposite to this one.
     *
     * @return {@link Orientation} opposite to this one
     */
    public Orientation opposite ()
    {
        return this == horizontal ? vertical : horizontal;
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
     * Returns whether or not orientation is horizontal.
     *
     * @return true if orientation is horizontal, false otherwise
     */
    public boolean isHorizontal ()
    {
        return this == horizontal;
    }

    /**
     * Returns whether or not orientation is vertical.
     *
     * @return true if orientation is vertical, false otherwise
     */
    public boolean isVertical ()
    {
        return this == vertical;
    }

    /**
     * Returns enumeration literal referencing constant value.
     *
     * @param value constant value
     * @return enumeration literal referencing constant value
     */
    public static Orientation get ( final int value )
    {
        switch ( value )
        {
            case VERTICAL:
                return vertical;

            case HORIZONTAL:
                return horizontal;

            default:
                throw new RuntimeException ( "Unknown orientation value: " + value );
        }
    }
}