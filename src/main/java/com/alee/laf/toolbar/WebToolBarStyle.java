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

package com.alee.laf.toolbar;

import com.alee.extended.painter.Painter;
import com.alee.laf.StyleConstants;

import java.awt.*;

/**
 * User: mgarin Date: 11/15/11 Time: 1:03 PM
 */

public final class WebToolBarStyle
{
    /**
     * Top or left toolbar background color
     */
    public static Color topBgColor = Color.WHITE;

    /**
     * Bottom or right toolbar background color
     */
    public static Color bottomBgColor = new Color ( 229, 233, 238 );

    /**
     * Toolbar border color
     */
    public static Color borderColor = new Color ( 139, 144, 151 );

    /**
     * Toolbar disabled border color
     */
    public static Color disabledBorderColor = StyleConstants.disabledBorderColor;

    /**
     * Decorate toolbar with Web-styled background or not
     */
    public static boolean undecorated = false;

    /**
     * Decoration rounding
     */
    public static int round = StyleConstants.largeRound;

    /**
     * Decoration shade width
     */
    public static int shadeWidth = StyleConstants.shadeWidth;

    /**
     * Default panel margin
     */
    public static Insets margin = new Insets ( 1, 1, 1, 1 );

    /**
     * Spacing between toolbar components
     */
    public static int spacing = StyleConstants.spacing;

    /**
     * Toolbar style
     */
    public static ToolbarStyle toolbarStyle = ToolbarStyle.standalone;

    /**
     * Default toolbar background painter
     */
    public static Painter painter = StyleConstants.painter;
}
