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

import com.alee.global.StyleConstants;
import com.alee.managers.style.skin.web.PopupStyle;
import com.alee.managers.style.skin.web.WebPopupPainterStyle;
import com.alee.utils.ProprietaryUtils;

import java.awt.*;

/**
 * WebPopupMenu style class.
 *
 * @author Mikle Garin
 */

public final class WebPopupMenuStyle
{
    /**
     * Popup menu style.
     */
    public static PopupStyle popupStyle = WebPopupPainterStyle.popupStyle;

    /**
     * Whether should apply dropdown popup menu style to popup menues within menubar.
     */
    public static boolean dropdownStyleForMenuBar = true;

    /**
     * Popup menu background color.
     */
    public static Color backgroundColor = Color.WHITE;

    /**
     * Popup menu border color.
     */
    public static Color borderColor = WebPopupPainterStyle.borderColor;

    /**
     * Popup menu corners rounding.
     * This value might vary depending on available underlying system features.
     */
    public static int round = ProprietaryUtils.isWindowTransparencyAllowed () ? WebPopupPainterStyle.round : StyleConstants.smallRound;

    /**
     * Popup menu shade width.
     * Be aware that this value should always be greater or equal to cornerWidth due to Swing menu processing limitations.
     * This value might vary depending on available underlying system features.
     */
    public static int shadeWidth = ProprietaryUtils.isWindowTransparencyAllowed () ? WebPopupPainterStyle.shadeWidth : 0;

    /**
     * Popup menu shade transparency.
     */
    public static float shadeTransparency = WebPopupPainterStyle.shadeTransparency;

    /**
     * Popup menu dropdown style corner width.
     * This property is used only when corner location is not being fixed.
     */
    public static int cornerWidth = WebPopupPainterStyle.cornerWidth;

    /**
     * Popup menu dropdown corner alignment.
     */
    public static int cornerAlignment = -1;

    /**
     * Popup menu background transparency.
     */
    public static float transparency = WebPopupPainterStyle.transparency;

    /**
     * Popup menu margin.
     */
    public static Insets margin = StyleConstants.emptyMargin;

    /**
     * Spacing between different popup menus.
     */
    public static int menuSpacing = 1;

    /**
     * Whether should fix initial popup menu location or not.
     * If set to true popup menu will try to use best possible location to show up.
     * <p/>
     * This is set to true by default to place menubar and menu popups correctly.
     * You might want to set this to false for some specific popup menu, but not all of them at once.
     */
    public static boolean fixLocation = true;
}