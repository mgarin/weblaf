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

package com.alee.global;

import com.alee.utils.laf.FocusType;
import com.alee.utils.laf.ShadeType;

import java.awt.*;

/**
 * Global styles class.
 * Most of the styles listed here are used within other style classes.
 *
 * @author Mikle Garin
 * @deprecated This is an artifact of older styling system and it should be removed completely
 */

@Deprecated
public class StyleConstants
{
    /**
     * Transparent color constant.
     */
    public static final Color transparent = new Color ( 255, 255, 255, 0 );

    /**
     * Components shade painting style.
     */
    public static ShadeType shadeType = ShadeType.simple;

    /**
     * Alpha-background settings.
     */
    public static int ALPHA_RECT_SIZE = 10;
    public static Color DARK_ALPHA = new Color ( 204, 204, 204 );
    public static Color LIGHT_ALPHA = new Color ( 255, 255, 255 );

    /**
     * Components animation settings.
     */
    public static boolean animate = true;
    public static long fps24 = 42L;
    public static long fps36 = 28L;
    public static long fps48 = 21L;
    public static long fps60 = 17L;

    /**
     * Components border settings.
     */
    public static Color borderColor = new Color ( 170, 170, 170 );
    public static Color darkBorderColor = Color.GRAY;
    public static Color disabledBorderColor = Color.LIGHT_GRAY;

    /**
     * Components focus settings.
     */
    public static Color focusColor = new Color ( 160, 160, 160 );
    public static Color fieldFocusColor = new Color ( 85, 142, 239 );
    public static Color transparentFieldFocusColor = new Color ( 85, 142, 239, 128 );
    public static FocusType focusType = FocusType.fieldFocus;
    public static Stroke fieldFocusStroke = new BasicStroke ( 1.5f );
    public static Stroke focusStroke = new BasicStroke ( 1, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 1, new float[]{ 1, 2 }, 0 );

    /**
     * Components background settings
     */
    public static Color topBgColor = Color.WHITE;
    public static Color bottomBgColor = new Color ( 223, 223, 223 );
    public static Color selectedBgColor = new Color ( 223, 220, 213 );
}