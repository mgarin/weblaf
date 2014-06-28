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

package com.alee.managers.style.skin.web;

import com.alee.global.StyleConstants;

import java.awt.*;

/**
 * @author Mikle Garin
 */

public final class WebPopupPainterStyle
{
    /**
     * Popup style.
     */
    public static PopupStyle popupStyle = PopupStyle.dropdown;

    /**
     * Popup border color.
     */
    public static Color borderColor = new Color ( 128, 128, 128, 128 );

    /**
     * Popup corners rounding.
     */
    public static int round = StyleConstants.bigRound;

    /**
     * Popup shade width.
     */
    public static int shadeWidth = 12;

    /**
     * Popup shade transparency.
     */
    public static float shadeTransparency = 0.75f;

    /**
     * Popup dropdown style corner width.
     * Should not be larger than shade width in current implementation.
     */
    public static int cornerWidth = 6;

    /**
     * Popup background transparency.
     */
    public static float transparency = 0.95f;
}