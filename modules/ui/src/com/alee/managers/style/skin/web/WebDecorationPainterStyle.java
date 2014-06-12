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

package com.alee.managers.style.skin.web;

import com.alee.global.StyleConstants;

import java.awt.*;

/**
 * @author Mikle Garin
 */

public final class WebDecorationPainterStyle
{
    /**
     * Whether should paint decoration or not.
     */
    public static boolean undecorated = false;

    /**
     * Whether should paint panel focus or not.
     * This variable doesn't affect anything if undecorated.
     */
    public static boolean paintFocus = false;

    /**
     * Decoration corners rounding.
     * This variable doesn't affect anything if undecorated.
     */
    public static int round = StyleConstants.smallRound;

    /**
     * Decoration shade width.
     * This variable doesn't affect anything if undecorated.
     */
    public static int shadeWidth = StyleConstants.shadeWidth;

    /**
     * Decoration shade transparency.
     */
    public static float shadeTransparency = 0.75f;

    /**
     * Decoration border stroke.
     * This variable doesn't affect anything if undecorated.
     */
    public static Stroke borderStroke = null;

    /**
     * Decoration border color.
     */
    public static Color borderColor = StyleConstants.darkBorderColor;

    /**
     * Disabled decoration border color.
     */
    public static Color disabledBorderColor = StyleConstants.disabledBorderColor;

    /**
     * Whether should paint decoration background or not.
     * This variable doesn't affect anything if undecorated.
     */
    public static boolean paintBackground = true;

    /**
     * Whether should paint web-styled background or not.
     * This variable doesn't affect anything if undecorated.
     */
    public static boolean webColoredBackground = true;

    /**
     * Decoration background color.
     */
    public static Color backgroundColor = StyleConstants.backgroundColor;
}