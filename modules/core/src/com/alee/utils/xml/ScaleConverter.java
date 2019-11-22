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
import com.alee.utils.TextUtils;
import com.alee.utils.swing.Scale;
import com.thoughtworks.xstream.converters.basic.AbstractSingleValueConverter;

import java.util.List;

/**
 * Custom XStream converter for {@link Scale}.
 *
 * @author Mikle Garin
 */
public class ScaleConverter extends AbstractSingleValueConverter
{
    @Override
    public boolean canConvert ( @NotNull final Class type )
    {
        return Scale.class.isAssignableFrom ( type );
    }

    @Override
    public String toString ( @NotNull final Object obj )
    {
        return pointToString ( ( Scale ) obj );
    }

    @Override
    public Object fromString ( @NotNull final String str )
    {
        return pointFromString ( str );
    }

    /**
     * Returns {@link Scale} converted into string.
     *
     * @param point {@link Scale} to convert
     * @return {@link Scale} converted into string
     */
    @NotNull
    public static String pointToString ( @NotNull final Scale point )
    {
        return point.getX () + "," + point.getY ();
    }

    /**
     * Returns {@link Scale} read from string.
     *
     * @param point {@link Scale} string
     * @return {@link Scale} read from string
     */
    @NotNull
    public static Scale pointFromString ( @NotNull final String point )
    {
        try
        {
            final List<String> points = TextUtils.stringToList ( point, "," );
            final double x = Double.parseDouble ( points.get ( 0 ) );
            final double y = Double.parseDouble ( points.get ( 1 ) );
            return new Scale ( x, y );
        }
        catch ( final Exception e )
        {
            throw new XmlException ( "Unable to parse Scale: " + point, e );
        }
    }
}