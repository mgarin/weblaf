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

import com.alee.global.StyleConstants;

import java.awt.*;

/**
 * WebPopOver style class.
 *
 * @author Mikle Garin
 */

public final class WebPopOverStyle
{
    /**
     * Whether WebPopOver should be movable or not.
     */
    public static boolean movable = true;

    /**
     * Default WebPopOver display source point.
     */
    public static PopOverSourcePoint popOverSourcePoint = PopOverSourcePoint.componentSide;

    /**
     * WebPopOver border color.
     */
    public static Color borderColor = new Color ( 128, 128, 128, 128 );

    /**
     * WebPopOver background color.
     */
    public static Color contentBackgroundColor = Color.WHITE;

    /**
     * WebPopOver corners rounding.
     */
    public static int round = StyleConstants.bigRound;

    /**
     * WebPopOver shade width.
     */
    public static int shadeWidth = 20;

    /**
     * WebPopOver shade transparency.
     */
    public static float shadeTransparency = 0.75f;

    /**
     * WebPopOver dropdown style corner width.
     * Should not be larger than shade width in current implementation.
     */
    public static int cornerWidth = 10;

    /**
     * WebPopOver background transparency.
     */
    public static float transparency = 0.95f;
}