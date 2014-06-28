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

package com.alee.laf.checkbox;

import com.alee.global.StyleConstants;

import java.awt.*;

/**
 * WebCheckBox style class.
 *
 * @author Mikle Garin
 */

public final class WebCheckBoxStyle
{
    /**
     * Border color.
     */
    public static Color borderColor = StyleConstants.borderColor;

    /**
     * Dark border color.
     */
    public static Color darkBorderColor = StyleConstants.darkBorderColor;

    /**
     * Disabled border color.
     */
    public static Color disabledBorderColor = StyleConstants.disabledBorderColor;

    /**
     * Top background gradient color.
     */
    public static Color topBgColor = StyleConstants.topBgColor;

    /**
     * Bottom background gradient color.
     */
    public static Color bottomBgColor = StyleConstants.bottomBgColor;

    /**
     * Top background gradient color on selection.
     */
    public static Color topSelectedBgColor = StyleConstants.topSelectedBgColor;

    /**
     * Bottom background gradient color on selection.
     */
    public static Color bottomSelectedBgColor = StyleConstants.bottomSelectedBgColor;

    /**
     * Border rounding.
     */
    public static int round = StyleConstants.smallRound;

    /**
     * Decoration shade width.
     */
    public static int shadeWidth = StyleConstants.shadeWidth;

    /**
     * Default checkbox margin.
     */
    public static Insets margin = StyleConstants.emptyMargin;

    /**
     * Whether should animate selection changes or not.
     */
    public static boolean animated = StyleConstants.animate;

    /**
     * Whether should display dark border only on rollover or not.
     */
    public static boolean rolloverDarkBorderOnly = StyleConstants.rolloverDarkBorderOnly;

    /**
     * Icon background border stroke.
     */
    public static Stroke borderStroke = new BasicStroke ( 1.5f );

    /**
     * Icon width.
     */
    public static int iconWidth = 16;

    /**
     * Icon height.
     */
    public static int iconHeight = 16;
}