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

package com.alee.managers.language.data;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

/**
 * User: mgarin Date: 27.04.12 Time: 16:27
 */

public class TextConverter implements Converter
{
    @Override
    public boolean canConvert ( Class type )
    {
        return Text.class.getCanonicalName ().equals ( type.getCanonicalName () );
    }

    @Override
    public void marshal ( Object source, HierarchicalStreamWriter writer, MarshallingContext context )
    {
        Text value = ( Text ) source;

        // Adding state
        if ( value.getState () != null )
        {
            writer.addAttribute ( "state", "" + value.getState () );
        }

        // Adding value
        writer.setValue ( value.getText () );
    }

    @Override
    public Object unmarshal ( HierarchicalStreamReader reader, UnmarshallingContext context )
    {
        // Reading state
        String state = reader.getAttribute ( "state" );

        // Reading value
        String value = reader.getValue ();

        // Creating Text object
        return new Text ( value, state );
    }
}