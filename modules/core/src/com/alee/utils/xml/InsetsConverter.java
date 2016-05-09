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

package com.alee.utils.xml;

import com.alee.managers.log.Log;
import com.thoughtworks.xstream.converters.basic.AbstractSingleValueConverter;

import java.awt.*;
import java.util.StringTokenizer;

/**
 * Custom {@link java.awt.Insets} object converter.
 *
 * @author Mikle Garin
 */

public class InsetsConverter extends AbstractSingleValueConverter
{
    /**
     * Values separator.
     */
    public static final String separator = ",";

    @Override
    public boolean canConvert ( final Class type )
    {
        return Insets.class.isAssignableFrom ( type );
    }

    @Override
    public Object fromString ( final String insets )
    {
        return insetsFromString ( insets );
    }

    @Override
    public String toString ( final Object object )
    {
        return insetsToString ( ( Insets ) object );
    }

    /**
     * Returns insets read from string.
     *
     * @param insets insets string
     * @return insets read from string
     */
    public static Insets insetsFromString ( final String insets )
    {
        try
        {
            final StringTokenizer tokenizer = new StringTokenizer ( insets, separator, false );
            if ( tokenizer.hasMoreTokens () )
            {
                final int top = Integer.parseInt ( tokenizer.nextToken ().trim () );
                if ( tokenizer.hasMoreTokens () )
                {
                    final int left = Integer.parseInt ( tokenizer.nextToken ().trim () );
                    if ( tokenizer.hasMoreTokens () )
                    {
                        final int bottom = Integer.parseInt ( tokenizer.nextToken ().trim () );
                        final int right = Integer.parseInt ( tokenizer.nextToken ().trim () );
                        return new Insets ( top, left, bottom, right );
                    }
                    else
                    {
                        return new Insets ( top, left, top, left );
                    }
                }
                else
                {
                    return new Insets ( top, top, top, top );
                }
            }
            else
            {
                return new Insets ( 0, 0, 0, 0 );
            }
        }
        catch ( final Throwable e )
        {
            Log.get ().error ( "Unable to parse Insets: " + insets, e );
            return new Insets ( 0, 0, 0, 0 );
        }
    }

    /**
     * Returns insets converted into string.
     *
     * @param insets insets to convert
     * @return insets converted into string
     */
    public static String insetsToString ( final Insets insets )
    {
        if ( insets.top == insets.left && insets.left == insets.bottom && insets.bottom == insets.right )
        {
            return Integer.toString ( insets.top );
        }
        else
        {
            return insets.top + separator + insets.left + separator + insets.bottom + separator + insets.right;
        }
    }
}