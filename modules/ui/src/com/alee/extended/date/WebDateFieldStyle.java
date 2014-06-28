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

package com.alee.extended.date;

import com.alee.extended.painter.Painter;
import com.alee.global.StyleConstants;

import java.awt.*;

/**
 * WebDateField style class.
 *
 * @author Mikle Garin
 */

public final class WebDateFieldStyle
{
    /**
     * Field margin.
     */
    public static Insets fieldMargin = new Insets ( 0, 0, 0, 0 );

    /**
     * Whether date field should be decorated with default border or not.
     */
    public static boolean drawBorder = StyleConstants.drawBorder;

    /**
     * Whether should draw date field focus or not.
     */
    public static boolean drawFocus = StyleConstants.drawFocus;

    /**
     * Decoration rounding.
     */
    public static int round = StyleConstants.smallRound;

    /**
     * Decoration shade width.
     */
    public static int shadeWidth = StyleConstants.shadeWidth;

    /**
     * Decorated date field margin.
     */
    public static Insets margin = new Insets ( -1, 0, -1, -1 );

    /**
     * Undecorated date field margin.
     */
    public static Insets undecoratedMargin = new Insets ( 0, 0, 0, 0 );

    /**
     * Whether should fill decoration background or not.
     */
    public static boolean drawBackground = true;

    /**
     * Whether should draw web-styled decoration background or not.
     */
    public static boolean webColored = false;

    /**
     * Background color.
     */
    public static Color backgroundColor = Color.WHITE;

    /**
     * Custom panel decoration painter.
     * If set it will override WebLaF styling.
     */
    public static Painter painter = null;
}