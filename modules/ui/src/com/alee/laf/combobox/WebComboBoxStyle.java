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

package com.alee.laf.combobox;

import com.alee.global.StyleConstants;
import com.alee.utils.ImageUtils;

import javax.swing.*;
import java.awt.*;

/**
 * WebComboBox style class.
 *
 * @author Mikle Garin
 */

public final class WebComboBoxStyle
{
    /**
     * Expand icon.
     */
    public static ImageIcon expandIcon = new ImageIcon ( WebComboBoxStyle.class.getResource ( "icons/arrow.png" ) );

    /**
     * Collapse icon.
     */
    public static ImageIcon collapseIcon = ImageUtils.rotateImage180 ( expandIcon );

    /**
     * Icon side spacing.
     */
    public static int iconSpacing = 3;

    /**
     * Draw combobox border.
     */
    public static boolean drawBorder = StyleConstants.drawBorder;

    /**
     * Whether should paint web-styled background or not.
     */
    public static boolean webColoredBackground = true;

    /**
     * Expanded combobox background color.
     */
    public static Color expandedBgColor = new Color ( 223, 220, 213 );

    /**
     * Draw combobox focus.
     */
    public static boolean drawFocus = true;

    /**
     * Top background color for selected item.
     */
    public static Color selectedMenuTopBg = new Color ( 208, 208, 198 );

    /**
     * Bottom background color for selected item.
     */
    public static Color selectedMenuBottomBg = new Color ( 196, 196, 186 );

    /**
     * Decoration rounding.
     */
    public static int round = StyleConstants.smallRound;

    /**
     * Decoration shade width.
     */
    public static int shadeWidth = StyleConstants.shadeWidth;

    /**
     * Values scrolling using mouse wheel enabled.
     */
    public static boolean mouseWheelScrollingEnabled = true;

    /**
     * Scroll bar thumb corners rounding.
     * todo Not used, but should be specified under combobox style XML later
     */
    public static int scrollBarThumbRound = 4;

    /**
     * Scroll bar margin.
     */
    public static Insets scrollBarMargin = new Insets ( 0, 1, 0, 1 );

    /**
     * Whether should display scroll bar buttons or not.
     */
    public static boolean scrollBarButtonsVisible = false;

    /**
     * Whether should display scroll bar track or not.
     */
    public static boolean scrollBarTrackVisible = false;
    
    /**
     * Whether the popup is allowed to be wider than the combobox or not.
     */
    public static boolean widerPopupAllowed = false;
}