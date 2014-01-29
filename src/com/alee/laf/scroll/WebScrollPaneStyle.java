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
 * User: mgarin Date: 11/15/11 Time: 4:51 PM
 */

public final class WebScrollPaneStyle
{
    /**
     * Border color
     */
    public static Color borderColor = Color.LIGHT_GRAY;

    /**
     * Dark border color
     */
    public static Color darkBorder = new Color ( 170, 170, 170 );

    /**
     * Draw border
     */
    public static boolean drawBorder = StyleConstants.drawBorder;

    /**
     * Decoration rounding
     */
    public static int round = StyleConstants.smallRound;

    /**
     * Decoration shade width
     */
    public static int shadeWidth = StyleConstants.shadeWidth;

    /**
     * Default scroll pane margin
     */
    public static Insets margin = StyleConstants.emptyMargin;

    /**
     * Draw focus when ancestor of focused component
     */
    public static boolean drawFocus = true;

    /**
     * Draw background
     */
    public static boolean drawBackground = false;
}