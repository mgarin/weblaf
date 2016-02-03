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

    /**
     * Compares file names insensitive to case but sensitive to integer numbers.
     *
     * @param name1 first file name to be compared
     * @param name2 second file name to be compared
     * @return a negative integer, zero, or a positive integer as the first argument is less than, equal to, or greater than the second
     */
    public static int compareNames ( final String name1, final String name2 )
    {
        final int n1 = name1.length ();
        final int n2 = name2.length ();
        final int min = Math.min ( n1, n2 );

        for ( int i = 0; i < min; i++ )
        {
            char c1 = name1.charAt ( i );
            char c2 = name2.charAt ( i );
            boolean d1 = Character.isDigit ( c1 );
            boolean d2 = Character.isDigit ( c2 );

            if ( d1 && d2 )
            {
                // Enter numerical comparison
                char c3, c4;
                do
                {
                    i++;
                    c3 = i < n1 ? name1.charAt ( i ) : 'x';
                    c4 = i < n2 ? name2.charAt ( i ) : 'x';
                    d1 = Character.isDigit ( c3 );
                    d2 = Character.isDigit ( c4 );
                }
                while ( d1 && d2 && c3 == c4 );

                if ( d1 != d2 )
                {
                    return d1 ? 1 : -1;
                }
                if ( c1 != c2 )
                {
                    return c1 - c2;
                }
                if ( c3 != c4 )
                {
                    return c3 - c4;
                }
                i--;

            }
            else if ( c1 != c2 )
            {
                c1 = Character.toUpperCase ( c1 );
                c2 = Character.toUpperCase ( c2 );

                if ( c1 != c2 )
                {
                    c1 = Character.toLowerCase ( c1 );
                    c2 = Character.toLowerCase ( c2 );

                    if ( c1 != c2 )
                    {
                        // No overflow because of numeric promotion
                        return c1 - c2;
                    }
                }
            }
        }
        return n1 - n2;
    }
}