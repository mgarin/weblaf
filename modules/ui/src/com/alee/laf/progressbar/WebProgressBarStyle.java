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

package com.alee.laf.progressbar;

import com.alee.global.StyleConstants;

import java.awt.*;

/**
 * WebProgressBar style class.
 *
 * @author Mikle Garin
 */

public final class WebProgressBarStyle
{
    /**
     * Top background gradient color.
     */
    public static Color bgTop = new Color ( 242, 242, 242 );

    /**
     * Bottom background gradient color.
     */
    public static Color bgBottom = new Color ( 223, 223, 223 );

    /**
     * Inner progress background top gradient color.
     */
    public static Color progressTopColor = Color.WHITE;

    /**
     * Inner progress background bottom gradient color.
     */
    public static Color progressBottomColor = new Color ( 223, 223, 223 );

    /**
     * Enabled progress border color.
     */
    public static Color progressEnabledBorderColor = Color.GRAY;

    /**
     * Disabled progress border color.
     */
    public static Color progressDisabledBorderColor = Color.LIGHT_GRAY;

    /**
     * Highlight color.
     */
    public static Color highlightWhite = new Color ( 255, 255, 255, 180 );

    /**
     * Highlight dark color.
     */
    public static Color highlightDarkWhite = new Color ( 255, 255, 255, 210 );

    /**
     * Indeterminate border color.
     */
    public static Color indeterminateBorder = new Color ( 210, 210, 210 );

    /**
     * Decoration rounding.
     */
    public static int round = StyleConstants.bigRound;

    /**
     * Inner progress rounding.
     */
    public static int innerRound = StyleConstants.smallRound;

    /**
     * Decoration shade width.
     */
    public static int shadeWidth = StyleConstants.shadeWidth;

    /**
     * Whether should paint border for indeterminate state or not.
     */
    public static boolean paintIndeterminateBorder = true;

    /**
     * Default preferred progress width.
     */
    public static int preferredProgressWidth = 240;
}