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

package com.alee.extended.window;

import com.alee.utils.UtilityException;

import javax.swing.*;

/**
 * This enumeration represents possible WebPopOver dialog display ways relative to invoker component.
 * Be aware that left/right direction is affected by WebPopOver component orientation.
 *
 * @author Mikle Garin
 * @see WebPopOver
 */
public enum PopOverDirection
{
    /**
     * Display pop-over above invoker component.
     */
    up,

    /**
     * Display pop-over below invoker component.
     */
    down,

    /**
     * Display pop-over leading to the invoker component.
     * This will also take pop-over component orientation into account.
     */
    left,

    /**
     * Display pop-over trailing to the invoker component.
     * This will also take pop-over component orientation into account.
     */
    right;

    /**
     * Returns directions check priority according to preferred direction.
     *
     * @return directions check priority
     */
    public PopOverDirection[] getPriority ()
    {
        switch ( this )
        {
            case up:
                return new PopOverDirection[]{ up, down, right, left };

            case down:
                return new PopOverDirection[]{ down, up, right, left };

            case left:
                return new PopOverDirection[]{ left, right, down, up };

            case right:
                return new PopOverDirection[]{ right, left, down, up };

            default:
                throw new UtilityException ( "Unsupported PopOverDirection: " + this );
        }
    }

    /**
     * Returns WebPopOver corner side.
     *
     * @param ltr whether pop-over has LTR orientation or not
     * @return WebPopOver corner side
     */
    public int getCornerSide ( final boolean ltr )
    {
        switch ( this )
        {
            case up:
                return SwingConstants.BOTTOM;

            case down:
                return SwingConstants.TOP;

            case left:
                return ltr ? SwingConstants.RIGHT : SwingConstants.LEFT;

            case right:
                return ltr ? SwingConstants.LEFT : SwingConstants.RIGHT;

            default:
                throw new UtilityException ( "Unsupported PopOverDirection: " + this );
        }
    }
}