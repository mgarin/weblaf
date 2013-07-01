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
 * @since 1.4
 */

public final class WebPanelStyle
{
    /**
     * Decorate panel with Web-styled background or not.
     */
    public static boolean undecorated = true;

    /**
     * Draw panel focus.
     */
    public static boolean drawFocus = false;

    /**
     * Decoration rounding.
     */
    public static int round = StyleConstants.smallRound;

    /**
     * Decoration shade width.
     */
    public static int shadeWidth = StyleConstants.shadeWidth;

    /**
     * Default panel margin.
     */
    public static Insets margin = StyleConstants.margin;

    /**
     * Default panel border stroke.
     */
    public static Stroke borderStroke = null;

    /**
     * Fill decoration background.
     */
    public static boolean drawBackground = true;

    /**
     * Web-styled background.
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
     * Sides which will drawed when panel is decorated with Web-styled border.
     */
    public static boolean drawTop = true;
    public static boolean drawLeft = true;
    public static boolean drawBottom = true;
    public static boolean drawRight = true;
}