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
import com.alee.managers.tooltip.TooltipWay;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

/**
 * User: mgarin Date: 27.04.12 Time: 16:27
 */

public class TooltipConverter implements Converter
{
    @Override
    public boolean canConvert ( Class type )
    {
        return Tooltip.class.getCanonicalName ().equals ( type.getCanonicalName () );
    }

    @Override
    public void marshal ( Object source, HierarchicalStreamWriter writer, MarshallingContext context )
    {
        Tooltip value = ( Tooltip ) source;

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

    public static boolean shouldWriteDelay ( Tooltip value )
    {
        return value.getDelay () != null;
    }

    public static boolean shouldWriteWay ( Tooltip value )
    {
        return value.getWay () != null;
    }

    public static boolean shouldWriteType ( Tooltip value )
    {
        return value.getType () != null && value.getType () != LanguageManager.getDefaultTooltipType ();
    }

    @Override
    public Object unmarshal ( HierarchicalStreamReader reader, UnmarshallingContext context )
    {
        // Reading type
        String typeV = reader.getAttribute ( "type" );
        TooltipType type = parseType ( typeV );

        // Reading way
        String wayV = reader.getAttribute ( "way" );
        TooltipWay way = parseWay ( wayV );

        // Reading delay
        String delayV = reader.getAttribute ( "delay" );
        Integer delay = parseDelay ( delayV );

        // Reading value
        String text = reader.getValue ();

        // Creating Value object
        return new Tooltip ( type, way, delay, text );
    }

    public static TooltipType parseType ( String typeV )
    {
        return typeV != null ? TooltipType.valueOf ( typeV ) : LanguageManager.getDefaultTooltipType ();
    }

    public static TooltipWay parseWay ( String wayV )
    {
        return wayV != null ? TooltipWay.valueOf ( wayV ) : null;
    }

    public static Integer parseDelay ( String delayV )
    {
        return delayV != null ? Integer.parseInt ( delayV ) : null;
    }
}