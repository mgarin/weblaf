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
import com.alee.global.StyleConstants;

import javax.swing.*;
import java.awt.*;

/**
 * WebMenu, WebMenuItem, WebCheckBoxMenuItem and WebRadioButtonMenuItem style class.
 *
 * @author Mikle Garin
 */

public final class WebMenuItemStyle
{
    /**
     * Menu item content margin.
     */
    public static Insets margin = StyleConstants.emptyMargin;

    /**
     * Spacing between menu item content and its left/right borders.
     */
    public static int sideSpacing = WebPopupMenuStyle.round;

    /**
     * Separate menu item corners rounding.
     * This will be applied to menu items that are placed outside popup menu.
     */
    public static int round = StyleConstants.smallRound;

    /**
     * Separate menu item shade width.
     * This will be applied to menu items that are placed outside popup menu.
     */
    public static int shadeWidth = StyleConstants.shadeWidth;

    /**
     * Disabled menu item foreground.
     */
    public static Color disabledFg = Color.LIGHT_GRAY;

    /**
     * Checkbox and radiobutton menu items check color.
     */
    public static Color checkColor = new Color ( 230, 230, 220 );

    /**
     * Top background color for selected item.
     */
    public static Color selectedTopBg = new Color ( 208, 208, 198 );

    /**
     * Bottom background color for selected item.
     */
    public static Color selectedBottomBg = new Color ( 196, 196, 186 );

    /**
     * Accelerator text foreground.
     */
    public static Color acceleratorFg = new Color ( 90, 90, 90 );

    /**
     * Disabld accelerator text foreground.
     */
    public static Color acceleratorDisabledFg = new Color ( 170, 170, 170 );

    /**
     * Accelerator text background.
     * Set to null to disable it.
     */
    public static Color acceleratorBg = new Color ( 255, 255, 255, 200 );

    /**
     * Gap between item icon and text.
     */
    public static int iconTextGap = 7;

    /**
     * Gap between item icon/text and accelerator text/sub-menu arrow.
     */
    public static int itemSidesGap = 15;

    /**
     * Whether should align all item texts to a single vertical line within single popup menu or not.
     */
    public static boolean alignTextToMenuIcons = true;

    /**
     * Icon alignment.
     * This property is used only when item texts are aligned to a single vertical line.
     */
    public static int iconAlignment = SwingConstants.CENTER;

    /**
     * Item painter.
     */
    public static Painter painter = null;
}