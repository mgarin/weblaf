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

import com.alee.api.annotations.NotNull;
import com.alee.utils.xml.XmlException;
import com.thoughtworks.xstream.converters.basic.AbstractSingleValueConverter;

import java.util.StringTokenizer;

/**
 * Custom XStream converter for {@link Round}.
 *
 * @author Mikle Garin
 * @see Round
 */
public final class RoundConverter extends AbstractSingleValueConverter
{
    /**
     * Values separator.
     */
    public static final String separator = ",";

    @Override
    public boolean canConvert ( @NotNull final Class type )
    {
        return Round.class.isAssignableFrom ( type );
    }

    @NotNull
    @Override
    public String toString ( @NotNull final Object object )
    {
        return roundToString ( ( Round ) object );
    }

    @NotNull
    @Override
    public Object fromString ( @NotNull final String round )
    {
        return roundFromString ( round );
    }

    /**
     * Returns {@link Round} converted into string.
     *
     * @param round {@link Round} to convert
     * @return {@link Round} converted into string
     */
    @NotNull
    public static String roundToString ( @NotNull final Round round )
    {
        final String string;
        if ( round.topLeft == round.topRight && round.bottomLeft == round.bottomRight && round.topLeft == round.bottomLeft )
        {
            string = Integer.toString ( round.topLeft );
        }
        else if ( round.topLeft == round.bottomRight && round.topRight == round.bottomLeft )
        {
            string = round.topLeft + separator + round.topRight;
        }
        else if ( round.topRight == round.bottomLeft )
        {
            string = round.topLeft + separator + round.topRight + separator + round.bottomRight;
        }
        else
        {
            string = round.topLeft + separator + round.topRight + separator + round.bottomRight + separator + round.bottomLeft;
        }
        return string;
    }

    /**
     * Returns {@link Round} read from string.
     *
     * @param string {@link Round} string
     * @return {@link Round} read from string
     */
    @NotNull
    public static Round roundFromString ( @NotNull final String string )
    {
        try
        {
            final Round round;
            final StringTokenizer tokenizer = new StringTokenizer ( string, separator, false );
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
                            round = new Round ( topLeft, topRight, bottomRight, bottomLeft );
                        }
                        else
                        {
                            round = new Round ( topLeft, topRight, bottomRight, topRight );
                        }
                    }
                    else
                    {
                        round = new Round ( topLeft, topRight, topLeft, topRight );
                    }
                }
                else
                {
                    round = new Round ( topLeft, topLeft, topLeft, topLeft );
                }
            }
            else
            {
                round = new Round ();
            }
            return round;
        }
        catch ( final Exception e )
        {
            throw new XmlException ( "Unable to parse Round: " + string, e );
        }
    }
}