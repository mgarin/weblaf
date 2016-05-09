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
 * Custom {@link java.awt.Rectangle} object converter.
 *
 * @author Mikle Garin
 */

public class RectangleConverter extends AbstractSingleValueConverter
{
    /**
     * Values separator.
     */
    public static final String separator = ",";

    @Override
    public boolean canConvert ( final Class type )
    {
        return Rectangle.class.isAssignableFrom ( type );
    }

    @Override
    public Object fromString ( final String insets )
    {
        return rectangleFromString ( insets );
    }

    @Override
    public String toString ( final Object object )
    {
        return rectangleToString ( ( Rectangle ) object );
    }

    /**
     * Returns insets read from string.
     *
     * @param insets insets string
     * @return insets read from string
     */
    public static Rectangle rectangleFromString ( final String insets )
    {
        try
        {
            final StringTokenizer tokenizer = new StringTokenizer ( insets, separator, false );
            final int x = Integer.parseInt ( tokenizer.nextToken ().trim () );
            final int y = Integer.parseInt ( tokenizer.nextToken ().trim () );
            final int width = Integer.parseInt ( tokenizer.nextToken ().trim () );
            final int height = Integer.parseInt ( tokenizer.nextToken ().trim () );
            return new Rectangle ( x, y, width, height );
        }
        catch ( final Throwable e )
        {
            Log.get ().error ( "Unable to parse Rectangle: " + insets, e );
            return new Rectangle ();
        }
    }

    /**
     * Returns insets converted into string.
     *
     * @param r insets to convert
     * @return insets converted into string
     */
    public static String rectangleToString ( final Rectangle r )
    {
        return r.x + separator + r.y + separator + r.width + separator + r.height;
    }
}