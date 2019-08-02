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

import com.thoughtworks.xstream.converters.basic.AbstractSingleValueConverter;

import java.awt.*;
import java.util.StringTokenizer;

/**
 * Custom XStream converter for {@link Dimension}.
 *
 * @author Mikle Garin
 */
public class DimensionConverter extends AbstractSingleValueConverter
{
    /**
     * Values separator.
     */
    public static final String SEPARATOR = ",";

    @Override
    public boolean canConvert ( final Class type )
    {
        return Dimension.class.isAssignableFrom ( type );
    }

    @Override
    public String toString ( final Object object )
    {
        return dimensionToString ( ( Dimension ) object );
    }

    @Override
    public Object fromString ( final String dimension )
    {
        return dimensionFromString ( dimension );
    }

    /**
     * Returns dimension converted into string.
     *
     * @param dimension dimension to convert
     * @return dimension converted into string
     */
    public static String dimensionToString ( final Dimension dimension )
    {
        return dimension.width + SEPARATOR + dimension.height;
    }

    /**
     * Returns dimension read from string.
     *
     * @param dimension dimension string
     * @return dimension read from string
     */
    public static Dimension dimensionFromString ( final String dimension )
    {
        try
        {
            if ( dimension.contains ( SEPARATOR ) )
            {
                final StringTokenizer tokenizer = new StringTokenizer ( dimension, SEPARATOR, false );
                final int width = Integer.parseInt ( tokenizer.nextToken ().trim () );
                final int height = Integer.parseInt ( tokenizer.nextToken ().trim () );
                return new Dimension ( width, height );
            }
            else
            {
                final int width = Integer.parseInt ( dimension );
                return new Dimension ( width, width );
            }
        }
        catch ( final Exception e )
        {
            throw new XmlException ( "Unable to parse Dimension: " + dimension, e );
        }
    }
}