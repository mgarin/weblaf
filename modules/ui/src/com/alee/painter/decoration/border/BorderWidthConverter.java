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

package com.alee.painter.decoration.border;

import com.alee.managers.log.Log;
import com.thoughtworks.xstream.converters.basic.AbstractSingleValueConverter;

import java.util.StringTokenizer;

/**
 * Custom {@link com.alee.painter.decoration.border.BorderWidth} object converter.
 *
 * @author Mikle Garin
 */

public final class BorderWidthConverter extends AbstractSingleValueConverter
{
    /**
     * Values separator.
     */
    public static final String separator = ",";

    @Override
    public boolean canConvert ( final Class type )
    {
        return BorderWidth.class.isAssignableFrom ( type );
    }

    @Override
    public Object fromString ( final String borderWidth )
    {
        return borderWidthFromString ( borderWidth );
    }

    @Override
    public String toString ( final Object object )
    {
        return borderWidthToString ( ( BorderWidth ) object );
    }

    /**
     * Returns border width read from string.
     *
     * @param borderWidth border width string
     * @return border width read from string
     */
    public static BorderWidth borderWidthFromString ( final String borderWidth )
    {
        try
        {
            final StringTokenizer tokenizer = new StringTokenizer ( borderWidth, separator, false );
            if ( tokenizer.hasMoreTokens () )
            {
                final int top = Integer.parseInt ( tokenizer.nextToken ().trim () );
                if ( tokenizer.hasMoreTokens () )
                {
                    final int right = Integer.parseInt ( tokenizer.nextToken ().trim () );
                    if ( tokenizer.hasMoreTokens () )
                    {
                        final int bottom = Integer.parseInt ( tokenizer.nextToken ().trim () );
                        if ( tokenizer.hasMoreTokens () )
                        {
                            final int left = Integer.parseInt ( tokenizer.nextToken ().trim () );
                            return new BorderWidth ( top, right, bottom, left );
                        }
                        else
                        {
                            return new BorderWidth ( top, right, bottom, right );
                        }
                    }
                    else
                    {
                        return new BorderWidth ( top, right, top, right );
                    }
                }
                else
                {
                    return new BorderWidth ( top, top, top, top );
                }
            }
            else
            {
                return new BorderWidth ();
            }
        }
        catch ( final Throwable e )
        {
            Log.get ().error ( "Unable to parse BorderWidth: " + borderWidth, e );
            return new BorderWidth ();
        }
    }

    /**
     * Returns border width converted into string.
     *
     * @param borderWidth border width to convert
     * @return border width converted into string
     */
    public static String borderWidthToString ( final BorderWidth borderWidth )
    {
        if ( borderWidth.top == borderWidth.right && borderWidth.right == borderWidth.bottom && borderWidth.bottom == borderWidth.left )
        {
            return Integer.toString ( borderWidth.top );
        }
        else if ( borderWidth.top == borderWidth.bottom && borderWidth.right == borderWidth.left )
        {
            return borderWidth.top + separator + borderWidth.right;
        }
        else if ( borderWidth.right == borderWidth.left )
        {
            return borderWidth.top + separator + borderWidth.right + separator + borderWidth.bottom;
        }
        else
        {
            return borderWidth.top + separator + borderWidth.right + separator + borderWidth.bottom + separator + borderWidth.left;
        }
    }
}