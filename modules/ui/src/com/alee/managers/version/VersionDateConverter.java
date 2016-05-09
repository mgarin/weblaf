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

package com.alee.managers.version;

import com.thoughtworks.xstream.converters.SingleValueConverter;

import java.sql.Date;
import java.text.SimpleDateFormat;

/**
 * @author Mikle Garin
 */

public class VersionDateConverter implements SingleValueConverter
{
    private static final SimpleDateFormat sdf = new SimpleDateFormat ( "dd.MM.yyyy" );

    @Override
    public boolean canConvert ( final Class aClass )
    {
        return Long.class.getCanonicalName ().equals ( aClass.getCanonicalName () );
    }

    @Override
    public String toString ( final Object o )
    {
        if ( o == null )
        {
            return "";
        }
        else
        {
            return sdf.format ( new Date ( ( Long ) o ) );
        }
    }

    @Override
    public Object fromString ( final String s )
    {
        try
        {
            return sdf.parse ( s ).getTime ();
        }
        catch ( final Throwable e )
        {
            try
            {
                return Long.parseLong ( s );
            }
            catch ( final Throwable ex )
            {
                return 0L;
            }
        }
    }
}