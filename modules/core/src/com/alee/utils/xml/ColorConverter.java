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
 * Custom {@link java.awt.Color} object converter.
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
     * Transparent color constant.
     */
    public static final Color TRANSPARENT = new Color ( 255, 255, 255, 0 );

    /**
     * Default colors map.
     */
    private static final ValuesTable<String, Color> defaultColors = new ValuesTable<String, Color> ();

    static
    {
        // Special value for null color
        defaultColors.put ( NULL_COLOR, null );

        // Standard Swing color set
        defaultColors.put ( "transparent", TRANSPARENT );
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
    public boolean canConvert ( final Class type )
    {
        return Color.class.isAssignableFrom ( type );
    }

    @Override
    public String toString ( final Object object )
    {
        final Color color = ( Color ) object;
        return colorToString ( color );
    }

    @Override
    public Object fromString ( final String color )
    {
        return colorFromString ( color );
    }

    /**
     * Convert specified color into string form.
     *
     * @param color color to convert
     * @return string color representation.
     */
    public static String colorToString ( final Color color )
    {
        return defaultColors.containsValue ( color ) ? defaultColors.getKey ( color ) :
                color.getRed () + "," + color.getGreen () + "," + color.getBlue () +
                        ( color.getAlpha () < 255 ? "," + color.getAlpha () : "" );
    }

    /**
     * Parses Color from its string form.
     *
     * @param color string form to parse
     * @return parsed color
     */
    public static Color colorFromString ( final String color )
    {
        return defaultColors.containsKey ( color ) ? defaultColors.get ( color ) :
                color.contains ( "#" ) ? ColorUtils.parseHexColor ( color ) : ColorUtils.parseRgbColor ( color );
    }
}