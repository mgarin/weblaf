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
import java.util.ArrayList;
import java.util.List;

/**
 * Custom {@link java.awt.Stroke} object converter.
 *
 * @author Mikle Garin
 */

public class StrokeConverter extends AbstractSingleValueConverter
{
    /**
     * Stroke settings separators.
     */
    public static final String separator = ";";
    public static final String subSeparator = ",";

    /**
     * Supported stroke types.
     */
    public static final List<StrokeConverterSupport> supported = new ArrayList<StrokeConverterSupport> ();

    static
    {
        supported.add ( new BasicStrokeConverterSupport () );
    }

    @Override
    public boolean canConvert ( final Class type )
    {
        if ( Stroke.class.equals ( type ) )
        {
            return true;
        }
        if ( Stroke.class.isAssignableFrom ( type ) )
        {
            for ( final StrokeConverterSupport support : supported )
            {
                if ( support.getType ().equals ( type ) )
                {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public Object fromString ( final String stroke )
    {
        final int idEnd = stroke.indexOf ( separator );
        final String id = idEnd != -1 ? stroke.substring ( 0, idEnd ) : stroke;
        for ( final StrokeConverterSupport supportedStroke : supported )
        {
            if ( id.equals ( supportedStroke.getId () ) )
            {
                return supportedStroke.fromString ( stroke );
            }
        }
        throw new RuntimeException ( "Unsupported stroke ID provided: " + id );
    }

    @Override
    public String toString ( final Object stroke )
    {
        final Class<?> type = stroke.getClass ();
        for ( final StrokeConverterSupport supportedStroke : supported )
        {
            if ( supportedStroke.getType () == type )
            {
                return supportedStroke.toString ( ( Stroke ) stroke );
            }
        }
        throw new RuntimeException ( "Unsupported stroke provided: " + type );
    }
}