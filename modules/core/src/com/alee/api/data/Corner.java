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

package com.alee.api.data;

import com.alee.api.jdk.Objects;

import static javax.swing.ScrollPaneConstants.*;

/**
 * Enumeration of different corner types.
 *
 * @author Mikle Garin
 */

public enum Corner
{
    upperLeft ( UPPER_LEFT_CORNER ),
    upperLeading ( UPPER_LEADING_CORNER ),
    upperRight ( UPPER_RIGHT_CORNER ),
    upperTrailing ( UPPER_TRAILING_CORNER ),
    lowerLeft ( LOWER_LEFT_CORNER ),
    lowerLeading ( LOWER_LEADING_CORNER ),
    lowerRight ( LOWER_RIGHT_CORNER ),
    lowerTrailing ( LOWER_TRAILING_CORNER );

    /**
     * Scroll pane constant of this corner type.
     */
    private final String scrollPaneConstant;

    /**
     * Constructs new corner type literal.
     *
     * @param scrollPaneConstant scroll pane constant of this corner type
     * @see javax.swing.ScrollPaneConstants
     */
    private Corner ( final String scrollPaneConstant )
    {
        this.scrollPaneConstant = scrollPaneConstant;
    }

    /**
     * Returns scroll pane constant of this corner type.
     *
     * @return scroll pane constant of this corner type
     */
    public String getScrollPaneConstant ()
    {
        return scrollPaneConstant;
    }

    /**
     * Returns whether or not specified value is a scroll pane constant.
     *
     * @param value value to process
     * @return true if specified value is a scroll pane constant, false otherwise
     */
    public static boolean is ( final String value )
    {
        for ( final Corner corner : values () )
        {
            if ( Objects.equals ( value, corner.scrollPaneConstant ) )
            {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns corner type for the specified scroll pane constant.
     *
     * @param scrollPaneConstant scroll pane constant
     * @return corner type for the specified scroll pane constant
     */
    public static Corner at ( final String scrollPaneConstant )
    {
        for ( final Corner corner : values () )
        {
            if ( Objects.equals ( scrollPaneConstant, corner.scrollPaneConstant ) )
            {
                return corner;
            }
        }
        throw new IllegalArgumentException ( "Wrong corner type specified: " + scrollPaneConstant );
    }
}