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

import com.alee.api.annotations.NotNull;
import com.alee.utils.xml.XmlException;
import com.thoughtworks.xstream.converters.basic.AbstractSingleValueConverter;

import java.util.StringTokenizer;

/**
 * Custom XStream converter for {@link BorderWidth}.
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
    public boolean canConvert ( @NotNull final Class type )
    {
        return BorderWidth.class.isAssignableFrom ( type );
    }

    @NotNull
    @Override
    public String toString ( @NotNull final Object object )
    {
        return borderWidthToString ( ( BorderWidth ) object );
    }

    @NotNull
    @Override
    public Object fromString ( @NotNull final String borderWidth )
    {
        return borderWidthFromString ( borderWidth );
    }

    /**
     * Returns {@link BorderWidth} converted into string.
     *
     * @param borderWidth {@link BorderWidth} to convert
     * @return {@link BorderWidth} converted into string
     */
    @NotNull
    public static String borderWidthToString ( @NotNull final BorderWidth borderWidth )
    {
        final String string;
        if ( borderWidth.top == borderWidth.right && borderWidth.right == borderWidth.bottom && borderWidth.bottom == borderWidth.left )
        {
            string = Integer.toString ( borderWidth.top );
        }
        else if ( borderWidth.top == borderWidth.bottom && borderWidth.right == borderWidth.left )
        {
            string = borderWidth.top + separator + borderWidth.right;
        }
        else if ( borderWidth.right == borderWidth.left )
        {
            string = borderWidth.top + separator + borderWidth.right + separator + borderWidth.bottom;
        }
        else
        {
            string = borderWidth.top + separator + borderWidth.right + separator + borderWidth.bottom + separator + borderWidth.left;
        }
        return string;
    }

    /**
     * Returns {@link BorderWidth} read from string.
     *
     * @param string {@link BorderWidth} string
     * @return {@link BorderWidth} read from string
     */
    @NotNull
    public static BorderWidth borderWidthFromString ( @NotNull final String string )
    {
        final BorderWidth borderWidth;
        try
        {
            final StringTokenizer tokenizer = new StringTokenizer ( string, separator, false );
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
                            borderWidth = new BorderWidth ( top, right, bottom, left );
                        }
                        else
                        {
                            borderWidth = new BorderWidth ( top, right, bottom, right );
                        }
                    }
                    else
                    {
                        borderWidth = new BorderWidth ( top, right, top, right );
                    }
                }
                else
                {
                    borderWidth = new BorderWidth ( top, top, top, top );
                }
            }
            else
            {
                borderWidth = new BorderWidth ();
            }
        }
        catch ( final Exception e )
        {
            throw new XmlException ( "Unable to parse BorderWidth: " + string, e );
        }
        return borderWidth;
    }
}