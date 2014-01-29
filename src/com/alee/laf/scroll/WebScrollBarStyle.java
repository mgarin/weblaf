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

package com.alee.laf.scroll;

import java.awt.*;

/**
 * WebScrollBar style class.
 *
 * @author Mikle Garin
 */

public final class WebScrollBarStyle
{
    /**
     * Whether scroll bar arrow buttons should be displayed or not.
     */
    public static boolean buttonsVisible = true;

    /**
     * Whether scroll bar track should be displayed or not.
     */
    public static boolean drawTrack = false;

    /**
     * Scroll bar track border color.
     */
    public static Color trackBorderColor = new Color ( 230, 230, 230 );

    /**
     * Scroll bar track background color.
     */
    public static Color trackBackgroundColor = new Color ( 245, 245, 245 );

    /**
     * Scroll bar thumb border color.
     */
    public static Color thumbBorderColor = new Color ( 189, 189, 189 );

    /**
     * Scroll bar thumb background color.
     */
    public static Color thumbBackgroundColor = new Color ( 217, 217, 217 );

    /**
     * Scroll bar thumb disabled border color.
     */
    public static Color thumbDisabledBorderColor = new Color ( 210, 210, 210 );

    /**
     * Scroll bar thumb disabled background color.
     */
    public static Color thumbDisabledBackgroundColor = new Color ( 230, 230, 230 );

    /**
     * Scroll bar thumb rollover border color.
     */
    public static Color thumbRolloverBorderColor = new Color ( 166, 166, 166 );

    /**
     * Scroll bar thumb rollover background color.
     */
    public static Color thumbRolloverBackgroundColor = new Color ( 194, 194, 194 );

    /**
     * Scroll bar thumb pressed/dragged border color.
     */
    public static Color thumbPressedBorderColor = new Color ( 126, 126, 126 );

    /**
     * Scroll bar thumb pressed/dragged background color.
     */
    public static Color thumbPressedBackgroundColor = new Color ( 145, 145, 145 );

    /**
     * Scroll bar thumb corners rounding.
     */
    public static int thumbRound = 8;

    /**
     * Scroll bar margin.
     */
    public static Insets margin = new Insets ( 0, 0, 0, 0 );

    /**
     * Scroll bar thumb margin.
     */
    public static Insets thumbMargin = new Insets ( 0, 2, 0, 2 );

    /**
     * Scroll bar arrow buttons preferred size.
     * Only odd values are recommended for default button representation.
     * For custom implementations you may specify anything you like.
     */
    public static Dimension buttonsSize = new Dimension ( 13, 13 );

    /**
     * Minimum horizontal scroll bar thumb width.
     */
    public static int minThumbWidth = 25;

    /**
     * Minimum vertical scroll bar thumb height.
     */
    public static int minThumbHeight = 25;
}