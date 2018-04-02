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

package com.alee.utils.file;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Locale;

/**
 * File names comparator.
 * It is insensitive to case but sensitive to integer numbers within the name.
 *
 * @author Mikle Garin
 */
public class FileNameComparator implements Comparator<String>, Serializable
{
    @Override
    public int compare ( final String name1, final String name2 )
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
     * Length of string is passed in for improved efficiency (only need to calculate it once).
     *
     * @param name    full name
     * @param slength full name length
     * @param marker  chunk starting position
     * @return name chunk starting from marker position
     */
    protected String getChunk ( final String name, final int slength, int marker )
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
    protected boolean isDigit ( final char character )
    {
        return character >= 48 && character <= 57;
    }
}