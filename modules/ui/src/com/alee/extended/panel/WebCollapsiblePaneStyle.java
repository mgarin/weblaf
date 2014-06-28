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

package com.alee.extended.panel;

import com.alee.global.StyleConstants;
import com.alee.utils.ImageUtils;

import javax.swing.*;
import java.awt.*;

/**
 * User: mgarin Date: 29.01.13 Time: 15:44
 */

public final class WebCollapsiblePaneStyle
{
    /**
     * Whether animate transition between states or not.
     */
    public static boolean animate = StyleConstants.animate;

    /**
     * Collapsed state icon.
     */
    public static ImageIcon expandIcon = new ImageIcon ( WebCollapsiblePane.class.getResource ( "icons/arrow.png" ) );

    /**
     * Expanded state icon.
     */
    public static ImageIcon collapseIcon = ImageUtils.rotateImage180 ( expandIcon );

    /**
     * State icon margin.
     */
    public static Insets stateIconMargin = new Insets ( 5, 5, 5, 5 );

    /**
     * Whether rotate state icon according to title pane position or not.
     */
    public static boolean rotateStateIcon = true;

    /**
     * Whether display state icon in title pane or not.
     */
    public static boolean showStateIcon = true;

    /**
     * State icon position in title pane.
     */
    public static int stateIconPostion = SwingConstants.RIGHT;

    /**
     * Title pane position in collapsible pane.
     */
    public static int titlePanePostion = SwingConstants.TOP;

    /**
     * Content margin.
     */
    public static Insets contentMargin = new Insets ( 0, 0, 0, 0 );
}