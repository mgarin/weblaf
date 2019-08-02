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

import com.alee.utils.SwingUtils;

import java.awt.event.MouseEvent;

/**
 * Enumeration representing mouse button types.
 *
 * @author Mikle Garin
 */
public enum MouseButton
{
    left ( MouseEvent.BUTTON1 ),
    middle ( MouseEvent.BUTTON2 ),
    right ( MouseEvent.BUTTON3 );

    /**
     * Mouse button code.
     */
    private final int code;

    /**
     * Constructs new mouse button reference enum.
     *
     * @param code mouse button code
     */
    private MouseButton ( final int code )
    {
        this.code = code;
    }

    /**
     * Returns mouse button code.
     *
     * @return mouse button code
     */
    public int getCode ()
    {
        return code;
    }

    /**
     * Returns mouse button type used within specified mouse event.
     *
     * @param event mouse event
     * @return mouse button type used within specified mouse event
     */
    public static MouseButton get ( final MouseEvent event )
    {
        if ( SwingUtils.isRightMouseButton ( event ) )
        {
            return right;
        }
        else if ( SwingUtils.isMiddleMouseButton ( event ) )
        {
            return middle;
        }
        else
        {
            return left;
        }
    }

    /**
     * Returns mouse button type for the specified mouse button code.
     *
     * @param code mouse button code
     * @return mouse button type for the specified mouse button code
     */
    public static MouseButton get ( final int code )
    {
        switch ( code )
        {
            case MouseEvent.BUTTON1:
                return left;

            case MouseEvent.BUTTON2:
                return middle;

            case MouseEvent.BUTTON3:
                return right;

            default:
                return null;
        }
    }
}