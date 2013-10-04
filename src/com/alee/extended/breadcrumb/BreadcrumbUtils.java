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

import com.alee.extended.layout.BreadcrumbLayout;

import javax.swing.*;
import java.awt.*;

/**
 * User: mgarin Date: 25.06.12 Time: 17:09
 */

/**
 * This class provides a set of utilities for breadcrumbs.
 * This is a library utility class and its not intended for use outside of the breadcrumbs.
 *
 * @author Mikle Garin
 */

public final class BreadcrumbUtils
{
    /**
     * Returns whether breadcrumb element contains specified point or not.
     *
     * @param x       point X coordinate
     * @param y       point Y coordinate
     * @param element breadcrumb element
     * @return true if breadcrumb element contains specified point, false otherwise
     */
    public static boolean contains ( final int x, final int y, final BreadcrumbElement element )
    {
        final JComponent ec = ( JComponent ) element;
        final boolean ltr = ec.getComponentOrientation ().isLeftToRight ();
        return element.getPainter ().getFillShape ( ec, ltr, getRound ( ec ) ).contains ( x, y );
    }

    /**
     * Returns breadcrumb element corners rounding.
     *
     * @param element breadcrumb element
     * @return breadcrumb element corners rounding
     */
    public static int getRound ( final JComponent element )
    {
        final Container bc = element.getParent ();
        if ( bc != null && bc instanceof WebBreadcrumb )
        {
            return ( ( WebBreadcrumb ) bc ).getRound ();
        }
        else
        {
            return 0;
        }
    }

    /**
     * Creates and returns default breadcrumb layout.
     *
     * @return default breadcrumb layout
     */
    public static BreadcrumbLayout createDefaultLayout ()
    {
        return new BreadcrumbLayout ( WebBreadcrumbStyle.elementOverlap + WebBreadcrumbStyle.shadeWidth );
    }
}