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

package com.alee.managers.tooltip;

import com.alee.global.StyleConstants;
import com.alee.managers.language.data.TooltipWay;
import com.alee.utils.laf.ShadeType;

import javax.swing.*;
import java.awt.*;

/**
 * WebCustomTooltip style class.
 *
 * @author Mikle Garin
 */

public final class WebCustomTooltipStyle
{
    /**
     * Frames per second in tooltip fade animation.
     */
    public static int fadeFps = 24;

    /**
     * Time in milliseconds that tooltip takes to fade in or fade out.
     */
    public static long fadeTime = 200;

    /**
     * Tooltip "corner" element length.
     */
    public static int cornerLength = 8;

    /**
     * Tooltip "corner" element side width.
     */
    public static int cornerSideX = 7;

    /**
     * Default tooltip display way.
     * If set to null - best display way will be calculated each time tooltip shown.
     */
    public static TooltipWay displayWay = null;

    /**
     * Whether to show component hotkeys information at the right side of the tooltip or not.
     * Only non-hidden hotkeys information will be displayed.
     */
    public static boolean showHotkey = true;

    /**
     * Hotkey location inside the tooltip.
     * It might have either SwingConstants.LEFT, RIGHT, LEADING or TRAILING value.
     */
    public static int hotkeyLocation = SwingConstants.TRAILING;

    /**
     * Whether tooltip should use default close behavior () or allow user to define his own close behavior.
     * Default behavior is when tooltip closes when cursor leave component area, or when any mouse/key press occurs.
     */
    public static boolean defaultCloseBehavior = true;

    /**
     * Spacing between tooltip border and tooltip content.
     */
    public static int contentSpacing = StyleConstants.mediumContentSpacing;

    /**
     * Additional left and right sides content spacing.
     * This is basically used to improve text readability in tooltips.
     */
    public static int leftRightSpacing = 0;

    /**
     * Minimal spacing between window edge and tooltip.
     * Used to avoid tooltips falling behind the window edge when shown close to it.
     */
    public static int windowSideSpacing = 5;

    /**
     * Tooltip corners rounding.
     */
    public static int round = StyleConstants.bigRound;

    /**
     * Decoration shade type.
     */
    public static ShadeType shadeType = ShadeType.gradient;

    /**
     * Decoration shade width.
     */
    public static int shadeWidth = 0;

    /**
     * Decoration shade color.
     */
    public static Color shadeColor = Color.GRAY;

    /**
     * Tooltip border color.
     * When set to null border won't be drawn at all.
     */
    public static Color borderColor = null;

    /**
     * Tooltip top background color.
     * When set to null background won't be drawn at all.
     */
    public static Color topBgColor = Color.BLACK;

    /**
     * Tooltip bottom background color.
     * When set to null background will be filled with topBgColor.
     */
    public static Color bottomBgColor = Color.BLACK;

    /**
     * Tooltip text color.
     */
    public static Color textColor = StyleConstants.tooltipTextColor;

    /**
     * Hotkey text color.
     */
    public static Color hotkeyColor = Color.BLACK;

    /**
     * Tooltip background trasparency.
     */
    public static float trasparency = StyleConstants.mediumTransparent;
}