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

package com.alee.laf.button;

import com.alee.extended.painter.Painter;
import com.alee.laf.StyleConstants;

import java.awt.*;

/**
 * User: mgarin Date: 11/21/11 Time: 4:53 PM
 */

public final class WebButtonStyle
{
    /**
     * Top background color
     */
    public static Color topBgColor = StyleConstants.topBgColor;

    /**
     * Bottom background color
     */
    public static Color bottomBgColor = StyleConstants.bottomBgColor;

    /**
     * Top selected background color
     */
    public static Color topSelectedBgColor = StyleConstants.selectedBgColor;

    /**
     * Bottom selected background color
     */
    public static Color bottomSelectedBgColor = new Color ( 210, 210, 210 );

    /**
     * Selected button foreground
     */
    public static Color selectedForeground = StyleConstants.selectedTextColor;

    /**
     * Draw rollover shine
     */
    public static boolean rolloverShine = false;

    /**
     * Rollover shine color
     */
    public static Color shineColor = StyleConstants.shineColor;

    /**
     * Draw dark border only on rollover
     */
    public static boolean rolloverDarkBorderOnly = StyleConstants.rolloverDarkBorderOnly;

    /**
     * Should draw shade
     */
    public static boolean drawShade = StyleConstants.drawShade;

    /**
     * Draw shade only on rollover
     */
    public static boolean rolloverShadeOnly = StyleConstants.rolloverShadeOnly;

    /**
     * Draw shade when button is disabled
     */
    public static boolean showDisabledShade = StyleConstants.showDisabledShade;

    /**
     * Decoration rounding
     */
    public static int round = StyleConstants.mediumRound;

    /**
     * Icon button decoration rounding
     */
    public static int iconRound = StyleConstants.smallRound;

    /**
     * Decoration shade width
     */
    public static int shadeWidth = StyleConstants.shadeWidth;

    /**
     * Default button margin
     */
    public static Insets margin = StyleConstants.margin;

    /**
     * Shade color
     */
    public static Color shadeColor = StyleConstants.shadeColor;

    /**
     * Inner shade width
     */
    public static int innerShadeWidth = StyleConstants.innerShadeWidth;

    /**
     * Inner shade color
     */
    public static Color innerShadeColor = StyleConstants.innerShadeColor;

    /**
     * Left and right spacing
     */
    public static int leftRightSpacing = StyleConstants.leftRightSpacing;

    /**
     * Left and right spacing
     */
    public static int iconLeftRightSpacing = 0;

    /**
     * Make unselected toggle button icon transparent
     */
    public static boolean shadeToggleIcon = StyleConstants.shadeToggleIcon;

    /**
     * Unselected toggle button icon transparency
     */
    public static float shadeToggleIconTransparency = StyleConstants.shadeToggleIconTransparency;

    /**
     * Draw button focus
     */
    public static boolean drawFocus = StyleConstants.drawFocus;

    /**
     * Draw button decoration on rollover only
     */
    public static boolean rolloverDecoratedOnly = StyleConstants.rolloverDecoratedOnly;

    /**
     * Animate component
     */
    public static boolean animate = StyleConstants.animate;

    /**
     * Undecorated button
     */
    public static boolean undecorated = StyleConstants.undecorated;

    /**
     * Default button background painter
     */
    public static Painter painter = StyleConstants.painter;

    /**
     * Animate component
     */
    public static boolean moveIconOnPress = true;
}