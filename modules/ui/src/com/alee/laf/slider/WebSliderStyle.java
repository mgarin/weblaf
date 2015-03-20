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

package com.alee.laf.slider;

import com.alee.global.StyleConstants;

import java.awt.*;

/**
 * @author Mikle Garin
 * @author Michka Popoff
 */

public final class WebSliderStyle
{
    /**
     * Top track background color
     */
    public static Color trackBgTop = StyleConstants.topDarkBgColor;

    /**
     * Bottom track background color
     */
    public static Color trackBgBottom = StyleConstants.bottomBgColor;

    /**
     * Track height
     */
    public static int trackHeight = 9;

    /**
     * Track round
     */
    public static int trackRound = StyleConstants.bigRound;

    /**
     * Track shade width
     */
    public static int trackShadeWidth = StyleConstants.shadeWidth;

    /**
     * Should draw progress inside slider track
     */
    public static boolean drawProgress = true;

    /**
     * Top track progress background color
     */
    public static Color progressTrackBgTop = Color.WHITE;

    /**
     * Bottom track progress background color
     */
    public static Color progressTrackBgBottom = new Color ( 223, 223, 223 );

    /**
     * Progress track border color.
     */
    public static Color progressBorderColor = StyleConstants.darkBorderColor;

    /**
     * Progress round
     */
    public static int progressRound = StyleConstants.smallRound;

    /**
     * Progress shade width
     */
    public static int progressShadeWidth = StyleConstants.shadeWidth;

    /**
     * Should draw slider thumb
     */
    public static boolean drawThumb = true;

    /**
     * Top gripper background color
     */
    public static Color thumbBgTop = StyleConstants.topBgColor;

    /**
     * Bottom gripper background color
     */
    public static Color thumbBgBottom = StyleConstants.bottomBgColor;

    /**
     * Gripper width
     */
    public static int thumbWidth = 8;

    /**
     * Gripper height
     */
    public static int thumbHeight = 18;

    /**
     * Gripper round
     */
    public static int thumbRound = StyleConstants.smallRound;

    /**
     * Gripper shade width
     */
    public static int thumbShadeWidth = StyleConstants.shadeWidth;

    /**
     * Should use angled gripper
     */
    public static boolean angledThumb = true;

    /**
     * Should the angle be sharp
     */
    public static boolean sharpThumbAngle = true;

    /**
     * Gripper angle length
     */
    public static int thumbAngleLength = 4;

    /**
     * Should animate component
     */
    public static boolean animated = StyleConstants.animate;

    /**
     * Dark component border only on mouseover
     */
    public static boolean rolloverDarkBorderOnly = StyleConstants.rolloverDarkBorderOnly;

    /**
     * Whether or not should invert direction in which slider is being scrolled by mouse wheel.
     */
    public static boolean invertMouseWheelDirection = false;
}
