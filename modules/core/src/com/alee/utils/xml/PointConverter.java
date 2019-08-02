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

import com.alee.utils.TextUtils;
import com.thoughtworks.xstream.converters.basic.AbstractSingleValueConverter;

import java.awt.*;
import java.util.List;

/**
 * Custom XStream converter for {@link Point}.
 *
 * @author Mikle Garin
 */
public class PointConverter extends AbstractSingleValueConverter
{
    @Override
    public boolean canConvert ( final Class type )
    {
        return Point.class.isAssignableFrom ( type );
    }

    @Override
    public String toString ( final Object obj )
    {
        return pointToString ( ( Point ) obj );
    }

    @Override
    public Object fromString ( final String str )
    {
        return pointFromString ( str );
    }

    /**
     * Returns {@link Point} converted into string.
     *
     * @param point {@link Point} to convert
     * @return {@link Point} converted into string
     */
    public static String pointToString ( final Point point )
    {
        return point.x + "," + point.y;
    }

    /**
     * Returns {@link Point} read from string.
     *
     * @param point {@link Point} string
     * @return {@link Point} read from string
     */
    public static Point pointFromString ( final String point )
    {
        try
        {
            final List<String> points = TextUtils.stringToList ( point, "," );
            final int x = Integer.parseInt ( points.get ( 0 ) );
            final int y = Integer.parseInt ( points.get ( 1 ) );
            return new Point ( x, y );
        }
        catch ( final Exception e )
        {
            throw new XmlException ( "Unable to parse Point: " + point, e );
        }
    }
}