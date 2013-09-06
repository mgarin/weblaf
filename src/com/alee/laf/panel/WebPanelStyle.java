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

import com.alee.extended.painter.Painter;
import com.alee.laf.StyleConstants;
import com.alee.utils.laf.ShapeProvider;

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
     * Decoration rounding.
     * This variable doesn't affect anything if panel is undecorated.
     */
    public static int round = StyleConstants.smallRound;

    /**
     * Decoration shade width.
     * This variable doesn't affect anything if panel is undecorated.
     */
    public static int shadeWidth = StyleConstants.shadeWidth;

    /**
     * Default panel margin.
     */
    public static Insets margin = StyleConstants.margin;

    /**
     * Default panel border stroke.
     * This variable doesn't affect anything if panel is undecorated.
     */
    public static Stroke borderStroke = null;

    /**
     * Whether should draw background or not.
     */
    public static boolean drawBackground = true;

    /**
     * Whether should draw web-colored background or not.
     */
    public static boolean webColored = true;

    /**
     * Default-styled background color.
     */
    public static Color backgroundColor = StyleConstants.backgroundColor;

    /**
     * Default panel background painter.
     */
    public static Painter painter = StyleConstants.painter;

    /**
     * Default panel clip shape provider.
     */
    public static ShapeProvider clipProvider = StyleConstants.clipProvider;

    /**
     * Whether should draw top panel side or not.
     * This variable doesn't affect anything if panel is undecorated.
     */
    public static boolean drawTop = true;

    /**
     * Whether should draw left panel side or not.
     * This variable doesn't affect anything if panel is undecorated.
     */
    public static boolean drawLeft = true;

    /**
     * Whether should draw bottom panel side or not.
     * This variable doesn't affect anything if panel is undecorated.
     */
    public static boolean drawBottom = true;

    /**
     * Whether should draw right panel side or not.
     * This variable doesn't affect anything if panel is undecorated.
     */
    public static boolean drawRight = true;
}