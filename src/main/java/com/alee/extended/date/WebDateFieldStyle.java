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
import com.alee.laf.StyleConstants;

import java.awt.*;

/**
 * User: mgarin Date: 15.10.12 Time: 20:43
 */

public final class WebDateFieldStyle
{
    /**
     * Default field margin
     */
    public static Insets fieldMargin = new Insets ( 0, 0, 0, 0 );

    /**
     * Decorate date field
     */
    public static boolean drawBorder = StyleConstants.drawBorder;

    /**
     * Draw date field focus
     */
    public static boolean drawFocus = StyleConstants.drawFocus;

    /**
     * Decoration rounding
     */
    public static int round = StyleConstants.smallRound;

    /**
     * Decoration shade width
     */
    public static int shadeWidth = StyleConstants.shadeWidth;

    /**
     * Default border margins
     */
    public static Insets margin = new Insets ( -1, 0, -1, -1 );
    public static Insets undecoratedMargin = new Insets ( 0, 0, 0, 0 );

    /**
     * Fill decoration background
     */
    public static boolean drawBackground = true;

    /**
     * Web-styled background
     */
    public static boolean webColored = false;

    /**
     * Default-styled background color
     */
    public static Color backgroundColor = Color.WHITE;

    /**
     * Default panel background painter
     */
    public static Painter painter = StyleConstants.painter;
}
