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

package com.alee.laf.panel;

import com.alee.global.StyleConstants;

import java.awt.*;

/**
 * WebPanel style class.
 *
 * @author Mikle Garin
 */

public final class WebPanelStyle
{
    /**
     * Whether should decorate panel or not.
     */
    public static boolean undecorated = true;

    /**
     * Whether should draw panel focus or not.
     * This variable doesn't affect anything if panel is undecorated.
     */
    public static boolean drawFocus = false;

    /**
     * Panel corners rounding.
     * This variable doesn't affect anything if panel is undecorated.
     */
    public static int round = StyleConstants.smallRound;

    /**
     * Panel shade width.
     * This variable doesn't affect anything if panel is undecorated.
     */
    public static int shadeWidth = StyleConstants.shadeWidth;

    /**
     * Panel shade transparency.
     */
    public static float shadeTransparency = 0.75f;

    /**
     * Panel margin.
     */
    public static Insets margin = StyleConstants.emptyMargin;

    /**
     * Panel border stroke.
     * This variable doesn't affect anything if panel is undecorated.
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
     * Whether should paint background or not.
     */
    public static boolean paintBackground = true;

    /**
     * Whether should paint web-styled background or not.
     */
    public static boolean webColoredBackground = true;

    /**
     * Panel background color.
     */
    public static Color backgroundColor = StyleConstants.backgroundColor;
}