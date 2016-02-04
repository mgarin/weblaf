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

import com.alee.utils.CollectionUtils;
import com.alee.utils.TextUtils;
import com.thoughtworks.xstream.converters.basic.AbstractSingleValueConverter;

import java.util.ArrayList;
import java.util.List;

/**
 * Custom converter for {@link java.util.List} of {@link java.lang.String}.
 *
 * @author Mikle Garin
 */

public class ListToStringConverter extends AbstractSingleValueConverter
{
    @Override
    public boolean canConvert ( final Class type )
    {
        return List.class.isAssignableFrom ( type );
    }

    @Override
    public String toString ( final Object obj )
    {
        final List list = ( List ) obj;
        if ( CollectionUtils.isEmpty ( list ) )
        {
            return null;
        }
        else
        {
            return TextUtils.listToString ( list, "," );
        }
    }

    @Override
    public Object fromString ( final String str )
    {
        if ( TextUtils.isEmpty ( str ) )
        {
            return new ArrayList<String> ( 0 );
        }
        else
        {
            return TextUtils.stringToList ( str, "," );
        }
    }
}