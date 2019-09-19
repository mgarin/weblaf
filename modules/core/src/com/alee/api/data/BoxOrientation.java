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

import com.alee.api.annotations.NotNull;

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
     * Returns whether or not this is a {@link #center} literal.
     *
     * @return {@code true} if this is a {@link #center} literal, {@code false} otherwise
     */
    public boolean isCenter ()
    {
        return this == center;
    }

    /**
     * Returns whether or not this is a {@link #top} literal.
     *
     * @return {@code true} if this is a {@link #top} literal, {@code false} otherwise
     */
    public boolean isTop ()
    {
        return this == top;
    }

    /**
     * Returns whether or not this is a {@link #left} literal.
     *
     * @return {@code true} if this is a {@link #left} literal, {@code false} otherwise
     */
    public boolean isLeft ()
    {
        return this == left;
    }

    /**
     * Returns whether or not this is a {@link #bottom} literal.
     *
     * @return {@code true} if this is a {@link #bottom} literal, {@code false} otherwise
     */
    public boolean isBottom ()
    {
        return this == bottom;
    }

    /**
     * Returns whether or not this is a {@link #right} literal.
     *
     * @return {@code true} if this is a {@link #right} literal, {@code false} otherwise
     */
    public boolean isRight ()
    {
        return this == right;
    }

    /**
     * Returns enumeration literal referencing constant value.
     *
     * @param value constant value
     * @return enumeration literal referencing constant value
     */
    @NotNull
    public static BoxOrientation get ( final int value )
    {
        final BoxOrientation orientation;
        switch ( value )
        {
            case TOP:
                orientation = top;
                break;

            case LEADING:
            case LEFT:
                orientation = left;
                break;

            case BOTTOM:
                orientation = bottom;
                break;

            case TRAILING:
            case RIGHT:
                orientation = right;
                break;

            case CENTER:
            default:
                orientation = center;
                break;
        }
        return orientation;
    }
}