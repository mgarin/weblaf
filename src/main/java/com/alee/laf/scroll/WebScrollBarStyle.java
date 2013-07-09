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

package com.alee.laf.scroll;

import com.alee.laf.StyleConstants;

import java.awt.*;

/**
 * User: mgarin Date: 11/15/11 Time: 4:36 PM
 */

public final class WebScrollBarStyle
{
    /**
     * Scroll bar background color
     */
    public static Color scrollBg = new Color ( 245, 245, 245 );

    /**
     * Scroll bar side border color
     */
    public static Color scrollBorder = new Color ( 230, 230, 230 );

    /**
     * Scroll bar border color
     */
    public static Color scrollBarBorder = new Color ( 201, 201, 201 );

    /**
     * Scroll bar top or left gradient
     */
    public static Color scrollGradientLeft = new Color ( 239, 239, 239 );

    /**
     * Scroll bar bottom or right gradient
     */
    public static Color scrollGradientRight = new Color ( 211, 211, 211 );

    /**
     * Dragged scroll bar top or left gradient
     */
    public static Color scrollSelGradientLeft = new Color ( 203, 203, 203 );

    /**
     * Dragged scroll bar bottom or right gradient
     */
    public static Color scrollSelGradientRight = new Color ( 175, 175, 175 );

    /**
     * Decoration rounding
     */
    public static int rounding = StyleConstants.smallRound;

    /**
     * Decoration shade width
     */
    public static boolean drawBorder = StyleConstants.drawBorder;

    /**
     * Minimum horizontal scroll bar thumb width
     */
    public static int minThumbWidth = 30;

    /**
     * Minimum vertical scroll bar thumb height
     */
    public static int minThumbHeight = 30;
}