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

package com.alee.laf.tabbedpane;

import com.alee.extended.painter.Painter;
import com.alee.laf.StyleConstants;

import java.awt.*;

/**
 * User: mgarin Date: 11/15/11 Time: 1:17 PM
 */

public final class WebTabbedPaneStyle
{
    /**
     * Tab content margin
     */
    public static TabbedPaneStyle tabbedPaneStyle = TabbedPaneStyle.standalone;

    /**
     * Top tab color
     */
    public static Color topBg = new Color ( 227, 227, 227 );

    /**
     * Bottom tab color
     */
    public static Color bottomBg = new Color ( 208, 208, 208 );

    /**
     * Top selected tab color
     */
    public static Color selectedTopBg = Color.WHITE;

    /**
     * Bottom selected tab color
     */
    public static Color selectedBottomBg = StyleConstants.backgroundColor;

    /**
     * Decoration rounding
     */
    public static int round = StyleConstants.smallRound;

    /**
     * Decoration shade width
     */
    public static int shadeWidth = StyleConstants.shadeWidth;

    /**
     * Rotate tab insets so they will not be the same for different tab positions
     */
    public static boolean rotateTabInsets = false;

    /**
     * Tab content margin
     */
    public static Insets contentInsets = new Insets ( 0, 0, 0, 0 );

    /**
     * Tab title margin
     */
    public static Insets tabInsets = new Insets ( 3, 4, 3, 4 );

    /**
     * Empty pane Painter (when there are no available tabs)
     */
    public static Painter painter = null;

    /**
     * Left tab area spacing
     */
    public static int tabRunIndent = 0;

    /**
     * Tab runs overlay in pixels
     */
    public static int tabOverlay = 1;

    /**
     * Tabs stretch type
     */
    public static TabStretchType tabStretchType = TabStretchType.multiline;
}
