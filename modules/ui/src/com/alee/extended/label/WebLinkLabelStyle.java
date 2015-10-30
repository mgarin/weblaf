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

package com.alee.extended.label;

import java.awt.*;

/**
 * @author Mikle Garin
 */

public final class WebLinkLabelStyle
{
    /**
     * Highlight link on rollover
     */
    public static boolean highlight = true;

    /**
     * Perform link action on mouse press, if false - action will be performed on mouse release
     */
    public static boolean onPressAction = false;

    /**
     * Colorize link that was visited once already
     */
    public static boolean highlightVisited = true;

    /**
     * Link foreground
     */
    public static Color foreground = new Color ( 0, 0, 200 );

    /**
     * Visited link foreground
     */
    public static Color visitedForeground = new Color ( 100, 0, 0 );
}