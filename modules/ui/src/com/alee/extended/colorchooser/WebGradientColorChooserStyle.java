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

package com.alee.extended.colorchooser;

import com.alee.global.StyleConstants;
import com.alee.laf.WebLookAndFeel;

import java.awt.*;

/**
 * User: mgarin Date: 23.11.12 Time: 19:10
 */

public final class WebGradientColorChooserStyle
{
    /**
     * Decoration shade width
     */
    public static int shadeWidth = StyleConstants.shadeWidth;

    /**
     * Default chooser line width
     */
    public static int lineWidth = 20;

    /**
     * Default gripper size
     */
    public static Dimension gripperSize = new Dimension ( 11, 19 );

    /**
     * Default chooser margin
     */
    public static Insets margin = StyleConstants.emptyMargin;

    /**
     * Should draw labels by default
     */
    public static boolean paintLabels = true;

    /**
     * Default labels font
     */
    public static Font labelsFont = WebLookAndFeel.globalControlFont.deriveFont ( 10f );

    /**
     * Default labels foreground
     */
    public static Color foreground = Color.BLACK;
}
