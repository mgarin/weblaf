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

package com.alee.utils.swing;

import java.awt.*;

/**
 * Convenient cursor types for all existing predefined native cursors.
 *
 * @author Mikle Garin
 */
public enum CursorType
{
    /**
     * The default cursor type.
     */
    common ( Cursor.DEFAULT_CURSOR ),

    /**
     * The crosshair cursor type.
     */
    crosshair ( Cursor.CROSSHAIR_CURSOR ),

    /**
     * The text cursor type.
     */
    text ( Cursor.TEXT_CURSOR ),

    /**
     * The wait cursor type.
     */
    wait ( Cursor.WAIT_CURSOR ),

    /**
     * The south-west-resize cursor type.
     */
    swResize ( Cursor.SW_RESIZE_CURSOR ),

    /**
     * The south-east-resize cursor type.
     */
    seResize ( Cursor.SE_RESIZE_CURSOR ),

    /**
     * The north-west-resize cursor type.
     */
    nwResize ( Cursor.NW_RESIZE_CURSOR ),

    /**
     * The north-east-resize cursor type.
     */
    neResize ( Cursor.NE_RESIZE_CURSOR ),

    /**
     * The north-resize cursor type.
     */
    nResize ( Cursor.N_RESIZE_CURSOR ),

    /**
     * The south-resize cursor type.
     */
    sResize ( Cursor.S_RESIZE_CURSOR ),

    /**
     * The west-resize cursor type.
     */
    wResize ( Cursor.W_RESIZE_CURSOR ),

    /**
     * The east-resize cursor type.
     */
    eResize ( Cursor.E_RESIZE_CURSOR ),

    /**
     * The hand cursor type.
     */
    hand ( Cursor.HAND_CURSOR ),

    /**
     * The move cursor type.
     */
    move ( Cursor.MOVE_CURSOR );

    /**
     * Predefined cursor ID.
     */
    private final int cursror;

    /**
     * Constructs cursor type.
     *
     * @param cursror predefined cursor ID
     */
    private CursorType ( final int cursror )
    {
        this.cursror = cursror;
    }

    /**
     * Returns cursor of this type.
     *
     * @return cursor of this type
     */
    public Cursor getCursor ()
    {
        return Cursor.getPredefinedCursor ( cursror );
    }
}