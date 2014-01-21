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

package com.alee.extended.button;

import com.alee.laf.menu.PopupMenuWay;

import javax.swing.*;

/**
 * WebSplitButton style class.
 *
 * @author Mikle Garin
 */

public class WebSplitButtonStyle
{
    /**
     * Split button icon.
     */
    public static ImageIcon splitIcon = new ImageIcon ( WebSplitButtonStyle.class.getResource ( "icons/splitIcon.png" ) );

    /**
     * Gap between split icon and split part sides.
     */
    public static int splitIconGap = 1;

    /**
     * Gap between split part (split icon) and button content (button icon and text).
     */
    public static int contentGap = 6;

    /**
     * Whether should always display popup menu when button is clicked or not.
     * If set to false popup menu will only be displayed when split button part is clicked.
     */
    public static boolean alwaysShowMenu = false;

    /**
     * Popup menu display way.
     */
    public static PopupMenuWay popupMenuWay = PopupMenuWay.belowStart;
}