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
import java.util.Locale;

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
     * Returns whether the first Object equals to any Object from the specified array.
     * This method will compare objects even if they are null without throwing any exceptions.
     * This method should not be called from any method that overrides object default "equals" method.
     *
     * @param object      object to compare
     * @param compareWith objects to compare with
     * @return true if the first Object equals to any Object from the specified array, false otherwise
     */
    public static boolean notEquals ( final Object object, final Object... compareWith )
    {
        if ( compareWith != null && compareWith.length > 0 )
        {
            for ( final Object o : compareWith )
            {
                if ( !equalsImpl ( object, o ) )
                {
                    return true;
                }
            }
        }
        return true;
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
        if ( name1 != null && name2 != null )
        {
            final String lower1 = name1.toLowerCase ( Locale.ROOT );
            final String lower2 = name2.toLowerCase ( Locale.ROOT );
            int thisMarker = 0;
            int thatMarker = 0;
            final int s1Length = lower1.length ();
            final int s2Length = lower2.length ();
            while ( thisMarker < s1Length && thatMarker < s2Length )
            {
                final String thisChunk = getChunk ( lower1, s1Length, thisMarker );
                thisMarker += thisChunk.length ();

                final String thatChunk = getChunk ( lower2, s2Length, thatMarker );
                thatMarker += thatChunk.length ();

                // If both chunks contain numeric characters, sort them numerically
                int result;
                if ( isDigit ( thisChunk.charAt ( 0 ) ) && isDigit ( thatChunk.charAt ( 0 ) ) )
                {
                    // Simple chunk comparison by length.
                    final int thisChunkLength = thisChunk.length ();
                    result = thisChunkLength - thatChunk.length ();

                    // If equal, the first different number counts
                    if ( result == 0 )
                    {
                        for ( int i = 0; i < thisChunkLength; i++ )
                        {
                            result = thisChunk.charAt ( i ) - thatChunk.charAt ( i );
                            if ( result != 0 )
                            {
                                return result;
                            }
                        }
                    }
                }
                else
                {
                    result = thisChunk.compareTo ( thatChunk );
                }

                if ( result != 0 )
                {
                    return result;
                }
            }
            return s1Length - s2Length;
        }
        return 0;
    }

    /**
     * Returns name chunk starting from marker position.
     * Length of string is passed in for improved efficiency (only need to calculate it once)
     *
     * @param name    full name
     * @param slength full name length
     * @param marker  chunk starting position
     * @return name chunk starting from marker position
     */
    private static String getChunk ( final String name, final int slength, int marker )
    {
        final StringBuilder chunk = new StringBuilder ();
        char c = name.charAt ( marker );
        chunk.append ( c );
        marker++;
        if ( isDigit ( c ) )
        {
            while ( marker < slength )
            {
                c = name.charAt ( marker );
                if ( !isDigit ( c ) )
                {
                    break;
                }
                chunk.append ( c );
                marker++;
            }
        }
        else
        {
            while ( marker < slength )
            {
                c = name.charAt ( marker );
                if ( isDigit ( c ) )
                {
                    break;
                }
                chunk.append ( c );
                marker++;
            }
        }
        return chunk.toString ();
    }

    /**
     * Returns whether or not specified character is digit.
     *
     * @param character character to check
     * @return {@code true} if specified character is digit, {@code false} otherwise
     */
    private static boolean isDigit ( final char character )
    {
        return character >= 48 && character <= 57;
    }
}