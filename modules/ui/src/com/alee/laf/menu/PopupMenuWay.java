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

package com.alee.laf.menu;

import javax.swing.*;

/**
 * This enumeration represents default ways of displaying popup menu.
 *
 * @author Mikle Garin
 */
public enum PopupMenuWay
{
    /**
     * Displays popup menu above the invoker component starting at its leading side.
     */
    aboveStart,

    /**
     * Displays popup menu above the invoker component at its middle.
     */
    aboveMiddle,

    /**
     * Displays popup menu above the invoker component starting at its trailing side.
     */
    aboveEnd,

    /**
     * Displays popup menu under the invoker component starting at its leading side.
     */
    belowStart,

    /**
     * Displays popup menu under the invoker component at its middle.
     */
    belowMiddle,

    /**
     * Displays popup menu under the invoker component starting at its trailing side.
     */
    belowEnd;

    /**
     * Returns corner side for this popup menu display location.
     *
     * @return corner side for this popup menu display location
     */
    public int getCornerSide ()
    {
        switch ( this )
        {
            case aboveStart:
            case aboveMiddle:
            case aboveEnd:
                return SwingConstants.BOTTOM;
            case belowStart:
            case belowMiddle:
            case belowEnd:
            default:
                return SwingConstants.TOP;

        }
    }
}