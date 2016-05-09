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

package com.alee.painter.decoration.shape;

import com.alee.managers.log.Log;
import com.thoughtworks.xstream.converters.basic.AbstractSingleValueConverter;

import java.util.StringTokenizer;

/**
 * Custom {@link com.alee.painter.decoration.shape.Round} object converter.
 *
 * @author Mikle Garin
 */

public class RoundConverter extends AbstractSingleValueConverter
{
    /**
     * Values separator.
     */
    public static final String separator = ",";

    @Override
    public boolean canConvert ( final Class type )
    {
        return Round.class.isAssignableFrom ( type );
    }

    @Override
    public Object fromString ( final String insets )
    {
        return roundFromString ( insets );
    }

    @Override
    public String toString ( final Object object )
    {
        return roundToString ( ( Round ) object );
    }

    /**
     * Returns insets read from string.
     *
     * @param insets insets string
     * @return insets read from string
     */
    public static Round roundFromString ( final String insets )
    {
        try
        {
            final StringTokenizer tokenizer = new StringTokenizer ( insets, separator, false );
            if ( tokenizer.hasMoreTokens () )
            {
                final int topLeft = Integer.parseInt ( tokenizer.nextToken ().trim () );
                if ( tokenizer.hasMoreTokens () )
                {
                    final int topRight = Integer.parseInt ( tokenizer.nextToken ().trim () );
                    if ( tokenizer.hasMoreTokens () )
                    {
                        final int bottomRight = Integer.parseInt ( tokenizer.nextToken ().trim () );
                        if ( tokenizer.hasMoreTokens () )
                        {
                            final int bottomLeft = Integer.parseInt ( tokenizer.nextToken ().trim () );
                            return new Round ( topLeft, topRight, bottomRight, bottomLeft );
                        }
                        else
                        {
                            return new Round ( topLeft, topRight, bottomRight, topRight );
                        }
                    }
                    else
                    {
                        return new Round ( topLeft, topRight, topLeft, topRight );
                    }
                }
                else
                {
                    return new Round ( topLeft, topLeft, topLeft, topLeft );
                }
            }
            else
            {
                return new Round ();
            }
        }
        catch ( final Throwable e )
        {
            Log.get ().error ( "Unable to parse Round: " + insets, e );
            return new Round ();
        }
    }

    /**
     * Returns insets converted into string.
     *
     * @param round insets to convert
     * @return insets converted into string
     */
    public static String roundToString ( final Round round )
    {
        if ( round.topLeft == round.topRight && round.bottomLeft == round.bottomRight && round.topLeft == round.bottomLeft )
        {
            return Integer.toString ( round.topLeft );
        }
        else if ( round.topLeft == round.bottomRight && round.topRight == round.bottomLeft )
        {
            return round.topLeft + separator + round.topRight;
        }
        else if ( round.topRight == round.bottomLeft )
        {
            return round.topLeft + separator + round.topRight + separator + round.bottomRight;
        }
        else
        {
            return round.topLeft + separator + round.topRight + separator + round.bottomRight + separator + round.bottomLeft;
        }
    }
}