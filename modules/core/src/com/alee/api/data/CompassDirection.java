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
import java.awt.*;

/**
 * Enumeration representing compass direction constants.
 * It is designed to conveniently provide state titles for various components.
 *
 * @author Mikle Garin
 */

public enum CompassDirection implements SwingConstants
{
    center ( CENTER ),
    northEast ( NORTH_EAST ),
    north ( NORTH ),
    northWest ( NORTH_WEST ),
    east ( EAST ),
    west ( WEST ),
    southEast ( SOUTH_EAST ),
    south ( SOUTH ),
    southWest ( SOUTH_WEST );

    /**
     * Constant value.
     */
    private final int value;

    /**
     * Constructs new enumeration literal based on constant value.
     *
     * @param value constant value
     */
    private CompassDirection ( final int value )
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
     * Returns direction opposite to this one.
     *
     * @return direction opposite to this one
     */
    public CompassDirection opposite ()
    {
        switch ( this )
        {
            case northEast:
                return southWest;

            case north:
                return south;

            case northWest:
                return southEast;

            case east:
                return west;

            case west:
                return east;

            case southEast:
                return northWest;

            case south:
                return north;

            case southWest:
                return northEast;

            case center:
            default:
                return center;
        }
    }

    /**
     * Returns direction adjusted according to component orientation.
     *
     * @param orientation component orientation
     * @return direction adjusted according to component orientation
     */
    public CompassDirection adjust ( final ComponentOrientation orientation )
    {
        if ( !orientation.isLeftToRight () )
        {
            switch ( this )
            {
                case northEast:
                    return northWest;
                case northWest:
                    return northEast;

                case east:
                    return west;
                case west:
                    return east;

                case southEast:
                    return southWest;
                case southWest:
                    return southEast;

                default:
                    return this;
            }
        }
        return this;
    }

    /**
     * Returns enumeration literal referencing constant value.
     *
     * @param value constant value
     * @return enumeration literal referencing constant value
     */
    public static CompassDirection get ( final int value )
    {
        switch ( value )
        {
            case NORTH_EAST:
                return northEast;

            case NORTH:
                return north;

            case NORTH_WEST:
                return northWest;

            case EAST:
                return east;

            case WEST:
                return west;

            case SOUTH_EAST:
                return southEast;

            case SOUTH:
                return south;

            case SOUTH_WEST:
                return southWest;

            case CENTER:
            default:
                return center;
        }
    }
}