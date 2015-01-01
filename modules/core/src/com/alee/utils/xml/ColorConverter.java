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

import com.alee.utils.ColorUtils;
import com.alee.utils.collection.ValuesTable;
import com.thoughtworks.xstream.converters.basic.AbstractSingleValueConverter;

import java.awt.*;

/**
 * Custom Color class converter.
 *
 * @author Mikle Garin
 */

public class ColorConverter extends AbstractSingleValueConverter
{
    /**
     * Null color constant.
     */
    public static final String NULL_COLOR = "null";

    /**
     * Default colors map.
     */
    private static final ValuesTable<String, Color> defaultColors = new ValuesTable<String, Color> ();

    static
    {
        // Special value for null color
        defaultColors.put ( NULL_COLOR, null );

        // Standard Swing color set
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

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean canConvert ( final Class type )
    {
        return type.equals ( Color.class );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object fromString ( final String color )
    {
        return parseColor ( color );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString ( final Object object )
    {
        final Color color = ( Color ) object;
        return convertColor ( color );
    }

    /**
     * Parses Color from its string form.
     *
     * @param color string form to parse
     * @return parsed color
     */
    public static Color parseColor ( final String color )
    {
        return defaultColors.containsKey ( color ) ? defaultColors.get ( color ) :
                color.contains ( "#" ) ? ColorUtils.parseHexColor ( color ) : ColorUtils.parseRgbColor ( color );
    }

    /**
     * Convert specified color into string form.
     *
     * @param color color to convert
     * @return string color representation.
     */
    public static String convertColor ( final Color color )
    {
        return defaultColors.containsValue ( color ) ? defaultColors.getKey ( color ) :
                color.getRed () + "," + color.getGreen () + "," + color.getBlue () +
                        ( color.getAlpha () < 255 ? "," + color.getAlpha () : "" );
    }
}