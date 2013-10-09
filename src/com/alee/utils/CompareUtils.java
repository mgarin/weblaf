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

package com.alee.utils;

import java.util.Date;

/**
 * This class provides a set of utilities to compare various objects.
 *
 * @author Mikle Garin
 */

public final class CompareUtils
{
    /**
     * Returns whether the first String equals to the second String or not.
     * This method will compare two String objects even if they are null without throwing any exceptions.
     * This method should not be called from any method that overrides object default "equals" method.
     *
     * @param s1 first String
     * @param s2 second String
     * @return true if the first String equals to the second String, false otherwise
     */
    public static boolean equals ( String s1, String s2 )
    {
        return s1 == null && s2 == null || !( s1 == null || s2 == null ) && s1.equals ( s2 );
    }

    /**
     * Returns whether the first Date equals to the second Date or not.
     * This method will compare two Date objects even if they are null without throwing any exceptions.
     * This method should not be called from any method that overrides object default "equals" method.
     *
     * @param d1 first Date
     * @param d2 second Date
     * @return true if the first Date equals to the second Date, false otherwise
     */
    public static boolean equals ( Date d1, Date d2 )
    {
        return d1 == null && d2 == null || !( d1 == null || d2 == null ) && d1.equals ( d2 );
    }

    /**
     * Returns whether the first Object equals to the second Object or not.
     * This method will compare two objects even if they are null without throwing any exceptions.
     * This method should not be called from any method that overrides object default "equals" method.
     *
     * @param o1 first Object
     * @param o2 second Object
     * @return true if the first Object equals to the second Object, false otherwise
     */
    public static boolean equals ( Object o1, Object o2 )
    {
        return o1 == null && o2 == null || !( o1 == null || o2 == null ) && o1.equals ( o2 );
    }
}