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

import java.awt.geom.Point2D;
import java.util.List;

/**
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
    public String toString ( final Object obj )
    {
        final Point2D point = ( Point2D ) obj;
        return point.getX () + "," + point.getY ();
    }

    @Override
    public Object fromString ( final String str )
    {
        final List<String> points = TextUtils.stringToList ( str, "," );
        final float x = Float.parseFloat ( points.get ( 0 ) );
        final float y = Float.parseFloat ( points.get ( 1 ) );
        return new Point2D.Float ( x, y );
    }
}