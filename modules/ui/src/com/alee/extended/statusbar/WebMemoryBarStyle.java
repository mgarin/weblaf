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

package com.alee.extended.statusbar;

import com.alee.global.StyleConstants;

import javax.swing.*;
import java.awt.*;

/**
 * User: mgarin Date: 22.06.12 Time: 12:44
 */

public final class WebMemoryBarStyle
{
    /**
     * Allocated mark border color
     */
    public static ImageIcon memoryIcon = new ImageIcon ( WebMemoryBarStyle.class.getResource ( "icons/memory.png" ) );

    /**
     * Allocated mark border color
     */
    public static Color allocatedBorderColor = Color.GRAY;

    /**
     * Allocated mark disabled border color
     */
    public static Color allocatedDisabledBorderColor = Color.LIGHT_GRAY;

    /**
     * Used memory bar border color
     */
    public static Color usedBorderColor = new Color ( 130, 130, 183 );

    /**
     * Used memory bar background color
     */
    public static Color usedFillColor = new Color ( 0, 0, 255, 50 );

    /**
     * Should draw memory bar border or not
     */
    public static boolean drawBorder = StyleConstants.drawBorder;

    /**
     * Should fill memory bar background or not
     */
    public static boolean fillBackground = true;

    /**
     * Memory bar text left and right spacing
     */
    public static int leftRightSpacing = StyleConstants.largeLeftRightSpacing;

    /**
     * Decoration shade width
     */
    public static int shadeWidth = StyleConstants.shadeWidth;

    /**
     * Decoration rounding
     */
    public static int round = StyleConstants.smallRound;

    /**
     * Allow user to call GC by pressing on the memory bar
     */
    public static boolean allowGcAction = true;

    /**
     * Should display info tooltip
     */
    public static boolean showTooltip = true;

    /**
     * Info tooltip display delay
     */
    public static int tooltipDelay = 1000;

    /**
     * Display maximum available memory
     */
    public static boolean showMaximum = true;
}