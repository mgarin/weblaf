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

import javax.swing.*;

/**
 * This enumeration represents possible breadcrumb element types.
 * Each type has its own appropriate styling.
 *
 * @author Mikle Garin
 */

public enum BreadcrumbElementType
{
    /**
     * First element in the breadcrumb.
     */
    start,

    /**
     * Middle element in the breadcrumb.
     */
    middle,

    /**
     * Last element in the breadcrumb.
     */
    end,

    /**
     * Undecorated breadcrumb element.
     */
    none;

    /**
     * Returns breadcrumb element type.
     *
     * @param element    breadcrumb element
     * @param breadcrumb breadcrumb
     * @return breadcrumb element type
     */
    public static BreadcrumbElementType getType ( final JComponent element, final WebBreadcrumb breadcrumb )
    {
        final int index = breadcrumb.getComponentZOrder ( element );
        final int last = breadcrumb.getComponentCount () - 1;
        final boolean encloseLastElement = breadcrumb.isEncloseLastElement ();
        if ( last == 0 && !encloseLastElement )
        {
            return BreadcrumbElementType.none;
        }
        else if ( index == 0 )
        {
            return BreadcrumbElementType.start;
        }
        else if ( index == last && !encloseLastElement )
        {
            return BreadcrumbElementType.end;
        }
        else
        {
            return BreadcrumbElementType.middle;
        }
    }
}