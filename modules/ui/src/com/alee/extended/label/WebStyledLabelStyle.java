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

package com.alee.extended.label;

import com.alee.laf.label.WebLabelStyle;

import java.awt.*;

/**
 * @author Mikle Garin
 */

public final class WebStyledLabelStyle
{
    /**
     * Label margin.
     */
    public static Insets margin = WebLabelStyle.margin;

    /**
     * Preferred row count.
     */
    public static int preferredRowCount = 1;

    /**
     * Whether should ignore StyleRange color settings or not.
     */
    public static boolean ignoreColorSettings = false;

    /**
     * Subscript and superscript font size ratio.
     */
    public static float scriptFontRatio = 1.5f;

    /**
     * Truncated text suffix.
     */
    public static String truncatedTextSuffix = "...";
}