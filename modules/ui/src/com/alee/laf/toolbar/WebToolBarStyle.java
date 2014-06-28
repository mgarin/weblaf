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

package com.alee.laf.toolbar;

import com.alee.extended.painter.Painter;
import com.alee.global.StyleConstants;

import java.awt.*;

/**
 * WebToolBar style class.
 *
 * @author Mikle Garin
 */

public final class WebToolBarStyle
{
    /**
     * Top toolbar background color.
     */
    public static Color topBgColor = Color.WHITE;

    /**
     * Bottom toolbar background color.
     */
    public static Color bottomBgColor = new Color ( 229, 233, 238 );

    /**
     * Toolbar border color.
     */
    public static Color borderColor = new Color ( 139, 144, 151 );

    /**
     * Toolbar disabled border color.
     */
    public static Color disabledBorderColor = StyleConstants.disabledBorderColor;

    /**
     * Whether should decorate toolbar with web-styled background or not.
     */
    public static boolean undecorated = false;

    /**
     * Toolbar corners rounding.
     */
    public static int round = StyleConstants.largeRound;

    /**
     * Toolbar shade width.
     */
    public static int shadeWidth = StyleConstants.shadeWidth;

    /**
     * Toolbar margin.
     */
    public static Insets margin = new Insets ( 1, 1, 1, 1 );

    /**
     * Spacing between toolbar components.
     */
    public static int spacing = StyleConstants.spacing;

    /**
     * Toolbar style.
     */
    public static ToolbarStyle toolbarStyle = ToolbarStyle.standalone;

    /**
     * Toolbar painter.
     * If set it will override WebLaF styling.
     */
    public static Painter painter = null;
}