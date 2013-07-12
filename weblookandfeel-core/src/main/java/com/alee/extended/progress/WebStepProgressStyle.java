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

package com.alee.extended.progress;

import com.alee.laf.StyleConstants;

import java.awt.*;

/**
 * User: mgarin Date: 21.01.13 Time: 14:22
 */

public final class WebStepProgressStyle
{
    /**
     * Default progress margin
     */
    public static Insets margin = new Insets ( 5, 5, 5, 5 );

    /**
     * Decoration shade width
     */
    public static int shadeWidth = StyleConstants.shadeWidth;

    /**
     * Step control size
     */
    public static int stepControlSize = 14;

    /**
     * Step control round
     */
    public static int stepControlRound = 7;

    /**
     * Inner step control size
     */
    public static int stepControlFillSize = 9;

    /**
     * Inner step control round
     */
    public static int stepControlFillRound = 4;

    /**
     * Progress path size
     */
    public static int pathSize = 8;

    /**
     * Inner progress path size
     */
    public static int fillPathSize = 3;

    /**
     * Progress bar color
     */
    public static Color progressColor = Color.GRAY;

    /**
     * Disabled progress bar color
     */
    public static Color disabledProgressColor = Color.LIGHT_GRAY;

    /**
     * Display progress labels
     */
    public static boolean showLabels = true;

    /**
     * Progress orientation
     */
    public static int orientation = WebStepProgress.HORIZONTAL;

    /**
     * Labels position relative to progress bar
     */
    public static int labelsPosition = WebStepProgress.LEADING;

    /**
     * Spacing between progress bar and labels
     */
    public static int spacing = 4;

    /**
     * Allow steps and progress selection by default
     */
    public static boolean selectionEnabled = true;

    /**
     * Default selection mode
     */
    public static int selectionMode = WebStepProgress.STEP_SELECTION;
}
