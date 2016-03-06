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

import com.alee.managers.language.LanguageManager;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

/**
 * @author Mikle Garin
 */

public class TooltipConverter implements Converter
{
    @Override
    public boolean canConvert ( final Class type )
    {
        return Tooltip.class.getCanonicalName ().equals ( type.getCanonicalName () );
    }

    @Override
    public void marshal ( final Object source, final HierarchicalStreamWriter writer, final MarshallingContext context )
    {
        final Tooltip value = ( Tooltip ) source;

        // Adding delay
        if ( shouldWriteDelay ( value ) )
        {
            writer.addAttribute ( "delay", value.getDelay ().toString () );
        }

        // Adding way
        if ( shouldWriteWay ( value ) )
        {
            writer.addAttribute ( "way", value.getWay ().toString () );
        }

        // Adding type
        if ( shouldWriteType ( value ) )
        {
            writer.addAttribute ( "type", value.getType ().toString () );
        }

        // Adding value
        writer.setValue ( value.getText () );
    }

    public static boolean shouldWriteDelay ( final Tooltip value )
    {
        return value.getDelay () != null;
    }

    public static boolean shouldWriteWay ( final Tooltip value )
    {
        return value.getWay () != null;
    }

    public static boolean shouldWriteType ( final Tooltip value )
    {
        return value.getType () != null && value.getType () != LanguageManager.getDefaultTooltipType ();
    }

    @Override
    public Object unmarshal ( final HierarchicalStreamReader reader, final UnmarshallingContext context )
    {
        // Reading type
        final String typeV = reader.getAttribute ( "type" );
        final TooltipType type = parseType ( typeV );

        // Reading way
        final String wayV = reader.getAttribute ( "way" );
        final TooltipWay way = parseWay ( wayV );

        // Reading delay
        final String delayV = reader.getAttribute ( "delay" );
        final Integer delay = parseDelay ( delayV );

        // Reading value
        final String text = reader.getValue ();

        // Creating Value object
        return new Tooltip ( type, way, delay, text );
    }

    public static TooltipType parseType ( final String typeV )
    {
        return typeV != null ? TooltipType.valueOf ( typeV ) : LanguageManager.getDefaultTooltipType ();
    }

    public static TooltipWay parseWay ( final String wayV )
    {
        return wayV != null ? TooltipWay.valueOf ( wayV ) : null;
    }

    public static Integer parseDelay ( final String delayV )
    {
        return delayV != null ? Integer.parseInt ( delayV ) : null;
    }
}