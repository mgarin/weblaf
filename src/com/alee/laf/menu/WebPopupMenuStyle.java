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

import com.alee.extended.painter.Painter;
import com.alee.laf.StyleConstants;
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
    public static PopupMenuStyle popupMenuStyle = PopupMenuStyle.dropdown;

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
    public static Color borderColor = new Color ( 128, 128, 128, 128 );

    /**
     * Popup menu corners rounding.
     * This value might vary depending on available underlying system features.
     */
    public static int round = ProprietaryUtils.isWindowTransparencyAllowed () ? StyleConstants.bigRound : StyleConstants.smallRound;

    /**
     * Popup menu shade width.
     * Be aware that this value should always be greater or equal to cornerWidth due to Swing menu processing limitations.
     * This value might vary depending on available underlying system features.
     */
    public static int shadeWidth = ProprietaryUtils.isWindowTransparencyAllowed () ? 12 : 0;

    /**
     * Popup menu shade opacity.
     */
    public static float shadeOpacity = 0.75f;

    /**
     * Popup menu dropdown style corner width
     */
    public static int cornerWidth = 6;

    /**
     * Popup menu margin.
     */
    public static Insets margin = StyleConstants.emptyMargin;

    /**
     * Spacing between different popup menues.
     */
    public static int menuSpacing = 1;

    /**
     * Popup menu painter.
     * If set it will override WebLaF styling.
     */
    public static Painter painter = null;

    /**
     * Whether should fix initial popup menu location or not.
     * If set to true popup menu will try to use best possible location to show up.
     * <p/>
     * This is set to true by default to place menubar and menu popups correctly.
     * You might want to set this to false for some specific popup menu, not all of them at once.
     */
    public static boolean fixLocation = true;
}