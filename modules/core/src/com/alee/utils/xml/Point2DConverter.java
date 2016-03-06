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
import com.alee.utils.TextUtils;
import com.thoughtworks.xstream.converters.basic.AbstractSingleValueConverter;

import java.awt.geom.Point2D;
import java.util.List;

/**
 * Custom {@link java.awt.geom.Point2D} object converter.
 *
 * @author Mikle Garin
 */

public class Point2DConverter extends AbstractSingleValueConverter
{
    @Override
    public boolean canConvert ( final Class type )
    {
        return Point2D.class.isAssignableFrom ( type );
    }

    @Override
    public Object fromString ( final String str )
    {
        return pointFromString ( str );
    }

    @Override
    public String toString ( final Object obj )
    {
        return pointToString ( ( Point2D ) obj );
    }

    /**
     * Returns point read from string.
     *
     * @param point point string
     * @return point read from string
     */
    public static Point2D.Float pointFromString ( final String point )
    {
        try
        {
            final List<String> points = TextUtils.stringToList ( point, "," );
            final float x = Float.parseFloat ( points.get ( 0 ) );
            final float y = Float.parseFloat ( points.get ( 1 ) );
            return new Point2D.Float ( x, y );
        }
        catch ( final Throwable e )
        {
            Log.get ().error ( "Unable to parse Point2D: " + point, e );
            return new Point2D.Float ();
        }
    }

    /**
     * Returns point converted into string.
     *
     * @param point point to convert
     * @return point converted into string
     */
    public static String pointToString ( final Point2D point )
    {
        return point.getX () + "," + point.getY ();
    }
}