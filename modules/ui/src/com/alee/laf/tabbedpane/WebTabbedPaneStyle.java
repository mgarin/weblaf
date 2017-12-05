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

import com.alee.painter.Painter;

import java.awt.*;

/**
 * WebTabbedPane style class.
 *
 * @author Mikle Garin
 */

public final class WebTabbedPaneStyle
{
    /**
     * Tab content margin.
     */
    public static TabbedPaneStyle tabbedPaneStyle = TabbedPaneStyle.standalone;

    /**
     * Top tab color.
     */
    public static Color topBg = new Color ( 227, 227, 227 );

    /**
     * Bottom tab color.
     */
    public static Color bottomBg = new Color ( 208, 208, 208 );

    /**
     * Top selected tab color.
     */
    public static Color selectedTopBg = Color.WHITE;

    /**
     * Bottom selected tab color.
     */
    public static Color selectedBottomBg = new Color ( 237, 237, 237 );

    /**
     * Decoration rounding.
     */
    public static int round = 2;

    /**
     * Decoration shade width.
     */
    public static int shadeWidth = 2;

    /**
     * Whether to rotate tab insets for different tab positions or not.
     */
    public static boolean rotateTabInsets = false;

    /**
     * Tab content margin.
     */
    public static Insets contentInsets = new Insets ( 0, 0, 0, 0 );

    /**
     * Tab title margin.
     */
    public static Insets tabInsets = new Insets ( 3, 4, 3, 4 );

    /**
     * Empty pane Painter.
     * Used when there are no available tabs.
     */
    public static Painter painter = null;

    /**
     * Left tab area spacing.
     */
    public static int tabRunIndent = 0;

    /**
     * Tab runs overlay in pixels.
     */
    public static int tabOverlay = 1;

    /**
     * Tabs stretch type.
     */
    public static TabStretchType tabStretchType = TabStretchType.multiline;

    /**
     * Color of the tab border.
     */
    public static Color tabBorderColor = Color.GRAY;

    /**
     * Color of the content border.
     */
    public static Color contentBorderColor = Color.GRAY;

    /**
     * If true, only the selected tab gets a border.
     */
    public static boolean paintBorderOnlyOnSelectedTab = false;

    /**
     * If true, the selected tab background colors will be used in every case.
     */
    public static boolean forceUseSelectedTabBgColors = false;

    /**
     * If true, only the top border is painted.
     */
    public static boolean paintOnlyTopBorder = false;
}