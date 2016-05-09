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

package com.alee.extended.breadcrumb;

import com.alee.global.StyleConstants;
import com.alee.utils.laf.ShadeType;

import java.awt.*;

/**
 * @author Mikle Garin
 */

public final class WebBreadcrumbStyle
{
    /**
     * Breadcrumb element overlap width
     */
    public static int elementOverlap = 8;

    /**
     * Decoration shade type
     */
    public static ShadeType shadeType = ShadeType.gradient;

    /**
     * Breadcrumb element overlap width
     */
    public static int shadeWidth = 4;

    /**
     * Breadcrumb element border color
     */
    public static Color borderColor = StyleConstants.darkBorderColor;

    /**
     * Breadcrumb element border color
     */
    public static Color disabledBorderColor = StyleConstants.disabledBorderColor;

    /**
     * Breadcrumb top color
     */
    public static Color bgTop = StyleConstants.topBgColor;

    /**
     * Breadcrumb bottom color
     */
    public static Color bgBottom = StyleConstants.bottomBgColor;

    /**
     * Breadcrumb bottom selected color
     */
    public static Color selectedBgColor = StyleConstants.selectedBgColor;

    /**
     * Default breadcrumb element margin
     */
    public static Insets elementMargin = new Insets ( 2, 5, 2, 5 );

    /**
     * Enclose last breadcrumb element by default
     */
    public static boolean encloseLastElement = false;
}