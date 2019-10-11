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
 * todo Remove {@link #center} direction as it not really correct?
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
        final CompassDirection opposite;
        switch ( this )
        {
            case northEast:
                opposite = southWest;
                break;

            case north:
                opposite = south;
                break;

            case northWest:
                opposite = southEast;
                break;

            case east:
                opposite = west;
                break;

            case west:
                opposite = east;
                break;

            case southEast:
                opposite = northWest;
                break;

            case south:
                opposite = north;
                break;

            case southWest:
                opposite = northEast;
                break;

            case center:
            default:
                opposite = center;
                break;
        }
        return opposite;
    }

    /**
     * Returns direction adjusted according to component orientation.
     *
     * @param orientation component orientation
     * @return direction adjusted according to component orientation
     */
    public CompassDirection adjust ( final ComponentOrientation orientation )
    {
        final CompassDirection adjusted;
        if ( !orientation.isLeftToRight () )
        {
            switch ( this )
            {
                case northEast:
                    adjusted = northWest;
                    break;
                case northWest:
                    adjusted = northEast;
                    break;

                case east:
                    adjusted = west;
                    break;
                case west:
                    adjusted = east;
                    break;

                case southEast:
                    adjusted = southWest;
                    break;
                case southWest:
                    adjusted = southEast;
                    break;

                default:
                    adjusted = this;
                    break;
            }
        }
        else
        {
            adjusted = this;
        }
        return adjusted;
    }

    /**
     * Returns enumeration literal referencing constant value.
     *
     * @param value constant value
     * @return enumeration literal referencing constant value
     */
    public static CompassDirection get ( final int value )
    {
        final CompassDirection direction;
        switch ( value )
        {
            case NORTH_EAST:
                direction = northEast;
                break;

            case NORTH:
                direction = north;
                break;

            case NORTH_WEST:
                direction = northWest;
                break;

            case EAST:
                direction = east;
                break;

            case WEST:
                direction = west;
                break;

            case SOUTH_EAST:
                direction = southEast;
                break;

            case SOUTH:
                direction = south;
                break;

            case SOUTH_WEST:
                direction = southWest;
                break;

            case CENTER:
            default:
                direction = center;
                break;
        }
        return direction;
    }
}