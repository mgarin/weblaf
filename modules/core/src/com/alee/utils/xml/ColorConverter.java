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
import com.alee.api.annotations.Nullable;
import com.alee.api.jdk.Objects;
import com.alee.utils.ColorUtils;
import com.alee.utils.MapUtils;
import com.alee.utils.swing.ColorUIResource;
import com.thoughtworks.xstream.converters.basic.AbstractSingleValueConverter;

import java.awt.*;
import java.util.Map;

/**
 * Custom XStream converter for {@link Color}.
 *
 * @author Mikle Garin
 */
public class ColorConverter extends AbstractSingleValueConverter
{
    /**
     * Constant for {@code null} color.
     */
    public static final String NULL_COLOR = "null";

    /**
     * Another constant for {@code null} color.
     */
    public static final String NONE_COLOR = "none";

    /**
     * Default colors map.
     */
    private static final Map<String, Color> defaultColors = MapUtils.newHashMap (
            /**
             * Null colors.
             */
            NULL_COLOR, null,
            NONE_COLOR, null,

            /**
             * Transparent color.
             */
            "transparent", ColorUtils.transparent (),

            /**
             * Standard Swing colors.
             */
            "black", Color.BLACK,
            "white", Color.WHITE,
            "red", Color.RED,
            "green", Color.GREEN,
            "blue", Color.BLUE,
            "lightGray", Color.LIGHT_GRAY,
            "gray", Color.GRAY,
            "darkGray", Color.DARK_GRAY,
            "pink", Color.PINK,
            "orange", Color.ORANGE,
            "yellow", Color.YELLOW,
            "magenta", Color.MAGENTA,
            "cyan", Color.CYAN
    );

    @Override
    public boolean canConvert ( @NotNull final Class type )
    {
        return Color.class.isAssignableFrom ( type );
    }

    @Nullable
    @Override
    public String toString ( @Nullable final Object color )
    {
        return colorToString ( ( Color ) color );
    }

    @Nullable
    @Override
    public Object fromString ( @Nullable final String string )
    {
        final Color color = colorFromString ( string );
        return color != null && ConverterContext.get ().isUIResource () ? new ColorUIResource ( color ) : color;
    }

    /**
     * Convert specified {@link Color} into string form.
     *
     * @param color {@link Color} to convert
     * @return string {@link Color} representation.
     */
    @NotNull
    public static String colorToString ( @Nullable final Color color )
    {
        String string = null;
        if ( color == null )
        {
            string = NULL_COLOR;
        }
        else if ( defaultColors.containsValue ( color ) )
        {
            for ( final Map.Entry<String, Color> entry : defaultColors.entrySet () )
            {
                if ( Objects.equals ( color, entry.getValue () ) )
                {
                    string = entry.getKey ();
                    break;
                }
            }
            if ( string == null )
            {
                throw new RuntimeException ( "Unable to find mapping for Color: " + color );
            }
        }
        else
        {
            final StringBuilder builder = new StringBuilder ( 15 );
            builder.append ( color.getRed () ).append ( "," );
            builder.append ( color.getGreen () ).append ( "," );
            builder.append ( color.getBlue () );
            if ( color.getAlpha () < 255 )
            {
                builder.append ( "," ).append ( color.getAlpha () );
            }
            string = builder.toString ();
        }
        return string;
    }

    /**
     * Parses {@link Color} from its string form.
     *
     * @param string string {@link Color} form to parse
     * @return parsed {@link Color}
     */
    @Nullable
    public static Color colorFromString ( @Nullable final String string )
    {
        try
        {
            final Color color;
            if ( string == null )
            {
                color = null;
            }
            else if ( defaultColors.containsKey ( string ) )
            {
                color = defaultColors.get ( string );
            }
            else if ( string.contains ( "#" ) )
            {
                color = ColorUtils.fromHex ( string );
            }
            else
            {
                color = ColorUtils.fromRGB ( string );
            }
            return color;
        }
        catch ( final Exception e )
        {
            throw new XmlException ( "Unable to parse Color: " + string, e );
        }
    }
}