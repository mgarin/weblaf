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

package com.alee.extended.image;

import com.alee.utils.laf.ShadeType;

import javax.swing.*;
import java.awt.*;

/**
 * @author Mikle Garin
 */

public final class WebDecoratedImageStyle implements SwingConstants
{
    /**
     * Image horizontal alignment
     */
    public static int horizontalAlignment = CENTER;

    /**
     * Image vertical alignment
     */
    public static int verticalAlignment = CENTER;

    /**
     * Draw border around image
     */
    public static boolean drawBorder = true;

    /**
     * Image border color
     */
    public static Color borderColor = new Color ( 255, 255, 255, 128 );

    /**
     * Draw glass-styled shade over image
     */
    public static boolean drawGlassLayer = true;

    /**
     * Decoration shade type
     */
    public static ShadeType shadeType = ShadeType.gradient;

    /**
     * Draw image shade, use 0 to disable shade
     */
    public static int shadeWidth = 3;

    /**
     * Image corners round, use 0 to disable rounding
     */
    public static int round = 8;

    /**
     * Blur center horizontal alignment
     */
    public static float blurAlignX = 0.5f;

    /**
     * Blur center vertical alignment
     */
    public static float blurAlignY = 0.5f;

    /**
     * Display grayscale image
     */
    public static boolean grayscale = false;

    /**
     * Display blurred image
     */
    public static boolean blur = false;

    /**
     * Image blur factor
     */
    public static float blurFactor = 3f;

    /**
     * Display zoom-blurred image
     */
    public static boolean zoomBlur = false;

    /**
     * Image zoom blur factor
     */
    public static float zoomBlurFactor = 0.3f;

    /**
     * Display rotation-blurred image
     */
    public static boolean rotationBlur = false;

    /**
     * Image rotation blur factor
     */
    public static float rotationBlurFactor = 0.2f;
}