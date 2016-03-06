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

import com.alee.global.StyleConstants;

import java.awt.*;

/**
 * WebStepProgress style class.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-WebStepProgress">How to use WebStepProgress</a>
 * @see com.alee.extended.progress.WebStepProgress
 */

public final class WebStepProgressStyle
{
    /**
     * Progress sides margin.
     */
    public static Insets margin = new Insets ( 5, 5, 5, 5 );

    /**
     * Decoration shade width.
     */
    public static int shadeWidth = 2;

    /**
     * Step control size.
     */
    public static int stepControlWidth = 14;

    /**
     * Step control round.
     */
    public static int stepControlRound = 7;

    /**
     * Inner step control size.
     */
    public static int stepControlFillWidth = 9;

    /**
     * Inner step control round.
     */
    public static int stepControlFillRound = 4;

    /**
     * Progress path size.
     */
    public static int pathWidth = 8;

    /**
     * Inner progress path size.
     */
    public static int pathFillWidth = 3;

    /**
     * Progress bar color.
     */
    public static Color progressColor = Color.GRAY;

    /**
     * Disabled progress bar color.
     */
    public static Color disabledProgressColor = Color.LIGHT_GRAY;

    /**
     * Whether should display progress labels or not.
     */
    public static boolean displayLabels = true;

    /**
     * Progress orientation.
     */
    public static int orientation = WebStepProgress.HORIZONTAL;

    /**
     * Labels position relative to progress bar.
     */
    public static int labelsPosition = WebStepProgress.LEADING;

    /**
     * Spacing between progress bar and labels.
     */
    public static int spacing = 4;

    /**
     * Minimum spacing between steps.
     */
    public static int stepsSpacing = 10;

    /**
     * Whether should allow steps and progress selection or not.
     */
    public static boolean selectionEnabled = true;

    /**
     * Selection mode.
     */
    public static StepSelectionMode selectionMode = StepSelectionMode.step;
}