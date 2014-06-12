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

package com.alee.extended.statusbar;

import com.alee.global.StyleConstants;

import java.awt.*;

/**
 * User: mgarin Date: 24.07.12 Time: 16:21
 */

public final class WebStatusBarStyle
{
    /**
     * Status bar top background color
     */
    public static Color topBgColor = StyleConstants.topBgColor;

    /**
     * Status bar bottom background color
     */
    public static Color bottomBgColor = StyleConstants.bottomBgColor;

    /**
     * Status bar margin
     */
    public static Insets margin = new Insets ( 2, 2, 2, 2 );

    /**
     * Should decorate status bar or not
     */
    public static boolean undecorated = StyleConstants.undecorated;
}