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

import com.alee.utils.collection.ValuesTable;
import com.thoughtworks.xstream.converters.basic.AbstractSingleValueConverter;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * User: mgarin Date: 22.03.12 Time: 14:44
 */

public class ColorConverter extends AbstractSingleValueConverter
{
    private static ValuesTable<String, Color> defaultColors = new ValuesTable<String, Color> ();

    static
    {
        defaultColors.put ( "black", Color.BLACK );
        defaultColors.put ( "white", Color.WHITE );
        defaultColors.put ( "red", Color.RED );
        defaultColors.put ( "green", Color.GREEN );
        defaultColors.put ( "blue", Color.BLUE );
        defaultColors.put ( "lightGray", Color.LIGHT_GRAY );
        defaultColors.put ( "gray", Color.GRAY );
        defaultColors.put ( "darkGray", Color.DARK_GRAY );
        defaultColors.put ( "pink", Color.PINK );
        defaultColors.put ( "orange", Color.ORANGE );
        defaultColors.put ( "yellow", Color.YELLOW );
        defaultColors.put ( "magenta", Color.MAGENTA );
        defaultColors.put ( "cyan", Color.CYAN );
    }

    @Override
    public boolean canConvert ( Class type )
    {
        return type.equals ( Color.class );
    }

    @Override
    public Object fromString ( String color )
    {
        if ( defaultColors.containsKey ( color ) )
        {
            return defaultColors.get ( color );
        }
        else
        {
            StringTokenizer tokenizer = new StringTokenizer ( color.replaceAll ( " ", "" ), "," );
            List<Integer> values = new ArrayList<Integer> ();
            while ( tokenizer.hasMoreTokens () )
            {
                try
                {
                    values.add ( Integer.parseInt ( tokenizer.nextToken () ) );
                }
                catch ( NumberFormatException e )
                {
                    return null;
                }
            }
            if ( values.size () == 3 )
            {
                return new Color ( values.get ( 0 ), values.get ( 1 ), values.get ( 2 ) );
            }
            else if ( values.size () == 4 )
            {
                return new Color ( values.get ( 0 ), values.get ( 1 ), values.get ( 2 ), values.get ( 3 ) );
            }
            else
            {
                return null;
            }
        }
    }

    @Override
    public String toString ( Object obj )
    {
        Color color = ( Color ) obj;
        if ( defaultColors.containsValue ( color ) )
        {
            return defaultColors.getKey ( color );
        }
        else
        {
            return color.getRed () + "," + color.getGreen () + "," + color.getBlue () +
                    ( color.getAlpha () < 255 ? "," + color.getAlpha () : "" );
        }
    }
}