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

import com.alee.laf.StyleConstants;
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
     * Draw combobox focus.
     */
    public static boolean drawFocus = true;

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
}