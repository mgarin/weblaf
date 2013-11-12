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

package com.alee.laf.menu;

import com.alee.laf.StyleConstants;

import java.awt.*;

/**
 * @author Mikle Garin
 */

public class PopupMenuPainterStyle
{
    /**
     * Popup menu style.
     */
    public static PopupMenuStyle popupMenuStyle = PopupMenuStyle.dropdown;

    /**
     * Popup menu border color.
     */
    public static Color borderColor = new Color ( 128, 128, 128, 128 );

    /**
     * Popup menu corners rounding.
     */
    public static int round = StyleConstants.bigRound;

    /**
     * Popup menu shade width.
     */
    public static int shadeWidth = 12;

    /**
     * Popup menu shade opacity.
     */
    public static float shadeOpacity = 0.75f;

    /**
     * Popup menu dropdown style corner width
     */
    public static int cornerWidth = 6;
}