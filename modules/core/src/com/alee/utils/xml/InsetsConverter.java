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

import com.alee.api.annotations.NotNull;
import com.alee.utils.swing.InsetsUIResource;
import com.thoughtworks.xstream.converters.basic.AbstractSingleValueConverter;

import java.awt.*;
import java.util.StringTokenizer;

/**
 * Custom XStream converter for {@link Insets}.
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
    public boolean canConvert ( @NotNull final Class type )
    {
        return Insets.class.isAssignableFrom ( type );
    }

    @Override
    public String toString ( @NotNull final Object insets )
    {
        return insetsToString ( ( Insets ) insets );
    }

    @Override
    public Object fromString ( @NotNull final String string )
    {
        final Insets insets = insetsFromString ( string );
        return ConverterContext.get ().isUIResource () ? new InsetsUIResource ( insets ) : insets;
    }

    /**
     * Returns {@link Insets} converted into string.
     *
     * @param insets {@link Insets} to convert
     * @return v converted into string
     */
    @NotNull
    public static String insetsToString ( @NotNull final Insets insets )
    {
        final String string;
        if ( insets.top == insets.left && insets.left == insets.bottom && insets.bottom == insets.right )
        {
            string = Integer.toString ( insets.top );
        }
        else
        {
            string = insets.top + separator + insets.left + separator + insets.bottom + separator + insets.right;
        }
        return string;
    }

    /**
     * Returns {@link Insets} read from string.
     *
     * @param string {@link Insets} string
     * @return {@link Insets} read from string
     */
    @NotNull
    public static Insets insetsFromString ( @NotNull final String string )
    {
        try
        {
            final Insets insets;
            final StringTokenizer tokenizer = new StringTokenizer ( string, separator, false );
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
                        insets = new Insets ( top, left, bottom, right );
                    }
                    else
                    {
                        insets = new Insets ( top, left, top, left );
                    }
                }
                else
                {
                    insets = new Insets ( top, top, top, top );
                }
            }
            else
            {
                insets = new Insets ( 0, 0, 0, 0 );
            }
            return insets;
        }
        catch ( final Exception e )
        {
            throw new XmlException ( "Unable to parse Insets: " + string, e );
        }
    }
}