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

import java.util.List;

/**
 * This class provides a set of utilities to compare various objects.
 *
 * @author Mikle Garin
 */

public final class CompareUtils
{
    /**
     * Returns whether the first Object equals to any Object from the specified array.
     * This method will compare objects even if they are null without throwing any exceptions.
     * This method should not be called from any method that overrides object default "equals" method.
     *
     * @param object      object to compare
     * @param compareWith objects to compare with
     * @return true if the first Object equals to any Object from the specified array, false otherwise
     */
    public static boolean equals ( final Object object, final Object... compareWith )
    {
        if ( compareWith != null && compareWith.length > 0 )
        {
            for ( final Object o : compareWith )
            {
                if ( equalsImpl ( object, o ) )
                {
                    return true;
                }
            }
        }
        return false;
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
    private static boolean equalsImpl ( final Object o1, final Object o2 )
    {
        return o1 == null && o2 == null || o1 != null && o2 != null && o1.equals ( o2 );
    }

    /**
     * Returns whether text contains any of the tokens from the specified list or not.
     *
     * @param text   text to look for tokens
     * @param tokens tokens list
     * @return true if text contains any of the tokens from the specified list, false otherwise
     */
    public static boolean contains ( final String text, final List<String> tokens )
    {
        for ( final String token : tokens )
        {
            if ( text.contains ( token ) )
            {
                return true;
            }
        }
        return false;
    }
}