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

package com.alee.painter.decoration.shape;

import com.alee.api.merge.Mergeable;
import com.thoughtworks.xstream.annotations.XStreamConverter;

import java.io.Serializable;

/**
 * Sides display states.
 *
 * In XML it can be represented in three multiple ways:
 *
 * 1. Integer values
 * Displayed side: "1"
 * Hidden side: "0"
 * Examples: "1,0,1,0" or "1,0,1" or "1,0" or "0"
 *
 * 2. Boolean values
 * Displayed side: "true"
 * Hidden side: "false"
 * Examples: "true,false,true,false" or "true,false,true" or "true,false" or "false"
 *
 * 3. Text values
 * Displayed side: "top", "left", "bottom", "right"
 * Hidden side: side not specified
 * Examples: "top,left,bottom,right" or "top,left,bottom" or "top,right" or "bottom"
 *
 * @author Mikle Garin
 * @see SidesConverter
 */
@XStreamConverter ( SidesConverter.class )
public final class Sides implements Mergeable, Cloneable, Serializable
{
    /**
     * Sides constants.
     */
    public static final String TOP = "top";
    public static final String LEFT = "left";
    public static final String BOTTOM = "bottom";
    public static final String RIGHT = "right";
    public static final String NONE = "none";

    /**
     * Whether or not top (north) side should be displayed.
     */
    public final boolean top;

    /**
     * Whether or not left (west) side should be displayed.
     */
    public final boolean left;

    /**
     * Whether or not botton (south) side should be displayed.
     */
    public final boolean bottom;

    /**
     * Whether or not right (east) side should be displayed.
     */
    public final boolean right;

    /**
     * Constructs sides display settings.
     */
    public Sides ()
    {
        this ( true );
    }

    /**
     * Constructs sides display settings.
     *
     * @param display whether or not all sides should be displayed
     */
    public Sides ( final boolean display )
    {
        this ( display, display, display, display );
    }

    /**
     * Constructs sides display settings.
     *
     * @param topBottom whether or not top and bottom sides should be displayed
     * @param leftRight whether or not left and right sides should be displayed
     */
    public Sides ( final boolean topBottom, final boolean leftRight )
    {
        this ( topBottom, leftRight, topBottom, leftRight );
    }

    /**
     * Constructs sides display settings.
     *
     * @param top       whether or not top side should be displayed
     * @param leftRight whether or not left and right sides should be displayed
     * @param bottom    whether or not bottom side should be displayed
     */
    public Sides ( final boolean top, final boolean leftRight, final boolean bottom )
    {
        this ( top, leftRight, bottom, leftRight );
    }

    /**
     * Constructs sides display settings.
     *
     * @param top    whether or not top side should be displayed
     * @param left   whether or not left side should be displayed
     * @param bottom whether or not bottom side should be displayed
     * @param right  whether or not right side should be displayed
     */
    public Sides ( final boolean top, final boolean left, final boolean bottom, final boolean right )
    {
        this.top = top;
        this.left = left;
        this.bottom = bottom;
        this.right = right;
    }

    /**
     * Returns whether or not at least one side should be displayed.
     *
     * @return {@code true} if at least one side should be displayed, {@code false} otherwise
     */
    public boolean isAny ()
    {
        return top || left || bottom || right;
    }

    @Override
    public String toString ()
    {
        return SidesConverter.sidesToString ( this );
    }
}