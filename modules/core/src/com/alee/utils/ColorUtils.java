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

package com.alee.utils;

import java.awt.*;
import java.util.*;

/**
 * This class provides a set of utilities to work with colors.
 *
 * @author Mikle Garin
 */
public final class ColorUtils
{
    /**
     * Colors cache.
     */
    private static final Map<Integer, Color> colors = new HashMap<Integer, Color> ( 50 );

    /**
     * Private constructor to avoid instantiation.
     */
    private ColorUtils ()
    {
        throw new UtilityException ( "Utility classes are not meant to be instantiated" );
    }

    /**
     * Returns cached {@link Color}.
     *
     * @param red   red value
     * @param green green value
     * @param blue  blue value
     * @return cached {@link Color}
     */
    public static Color color ( final int red, final int green, final int blue )
    {
        return color ( red, green, blue, 255 );
    }

    /**
     * Returns cached {@link Color}.
     *
     * @param red   red value
     * @param green green value
     * @param blue  blue value
     * @param alpha alpha value
     * @return cached {@link Color}
     */
    public static Color color ( final int red, final int green, final int blue, final int alpha )
    {
        final int rgb = ( alpha & 0xFF ) << 24 | ( red & 0xFF ) << 16 | ( green & 0xFF ) << 8 | blue & 0xFF;
        Color color = colors.get ( red );
        if ( color == null )
        {
            color = new Color ( red, green, blue, alpha );
            colors.put ( rgb, color );
        }
        return color;
    }

    /**
     * Returns black color with specified alpha
     *
     * @param alpha color alpha
     * @return black color with specified alpha
     */
    public static Color black ( final int alpha )
    {
        return color ( 0, 0, 0, alpha );
    }

    /**
     * Returns white color with specified alpha
     *
     * @param alpha color alpha
     * @return white color with specified alpha
     */
    public static Color white ( final int alpha )
    {
        return color ( 255, 255, 255, alpha );
    }

    /**
     * Returns grayscale version of the specified {@link Color}.
     *
     * @param color {@link Color} to make grayscale version of
     * @return grayscale version of the specified {@link Color}
     */
    public static Color grayscale ( final Color color )
    {
        final int avg = ( int ) ( color.getRed () * 0.299 + color.getGreen () * 0.587 + color.getBlue () * 0.114 );
        return color ( avg, avg, avg, color.getAlpha () );
    }

    /**
     * Returns cached transparent {@link Color}.
     *
     * @return cached transparent {@link Color}
     */
    public static Color transparent ()
    {
        return color ( 255, 255, 255, 0 );
    }

    /**
     * Returns color with modified alpha value.
     *
     * @param color color to process
     * @param alpha new alpha value
     * @return color with modified alpha value
     */
    public static Color transparent ( final Color color, final int alpha )
    {
        return color ( color.getRed (), color.getGreen (), color.getBlue (), alpha );
    }

    /**
     * Returns color with alpha value set to 255.
     *
     * @param color color to modify
     * @return color with alpha value set to 255
     */
    public static Color opaque ( final Color color )
    {
        return color.getAlpha () != 255 ? color ( color.getRed (), color.getGreen (), color.getBlue (), 255 ) : color;
    }

    /**
     * Returns web-safe color.
     *
     * @param color color to process
     * @return web-safe color
     */
    public static Color webSafe ( final Color color )
    {
        return color ( webSafe ( color.getRed () ), webSafe ( color.getGreen () ), webSafe ( color.getBlue () ) );
    }

    /**
     * Returns web-safe color value.
     *
     * @param value value to process
     * @return web-safe color value
     */
    public static int webSafe ( final int value )
    {
        if ( 0 <= value && value <= 51 )
        {
            return value > 51 - value ? 51 : 0;
        }
        else if ( 51 <= value && value <= 102 )
        {
            return 51 + value > 102 - value ? 102 : 51;
        }
        else if ( 102 <= value && value <= 153 )
        {
            return 102 + value > 153 - value ? 153 : 102;
        }
        else if ( 153 <= value && value <= 204 )
        {
            return 153 + value > 204 - value ? 204 : 153;
        }
        else if ( 204 <= value && value <= 255 )
        {
            return 204 + value > 255 - value ? 255 : 204;
        }
        return value;
    }

    /**
     * Returns intermediate color between two specified colors.
     *
     * @param color1   first color
     * @param color2   second color
     * @param progress progress of the intermediate color
     * @return intermediate color between two specified colors
     */
    public static Color intermediate ( final Color color1, final Color color2, final float progress )
    {
        if ( progress <= 0f )
        {
            return color1;
        }
        else if ( progress >= 1f )
        {
            return color2;
        }
        else
        {
            return new Color ( intermediate ( color1.getRed (), color2.getRed (), progress ),
                    intermediate ( color1.getGreen (), color2.getGreen (), progress ),
                    intermediate ( color1.getBlue (), color2.getBlue (), progress ),
                    intermediate ( color1.getAlpha (), color2.getAlpha (), progress ) );
        }
    }

    /**
     * Returns intermediate value between two specified values.
     *
     * @param value1   first value
     * @param value2   second value
     * @param progress progress of the intermediate value
     * @return intermediate value between two specified values
     */
    private static int intermediate ( final int value1, final int value2, final float progress )
    {
        return value1 + Math.round ( ( ( float ) value2 - value1 ) * progress );
    }

    /**
     * Returns hex color string for the specified color.
     *
     * @param color color to process
     * @return hex color string for the specified color
     */
    public static String toHex ( final Color color )
    {
        final int rgb = color.getRGB ();
        if ( rgb == 0 )
        {
            return "#000000";
        }
        else
        {
            final String hex = Integer.toHexString ( rgb ).toUpperCase ( Locale.ROOT );
            return "#" + hex.substring ( 2, hex.length () );
        }
    }

    /**
     * Returns color decoded from a hex color string.
     *
     * @param hex hex color string
     * @return color decoded from a hex color string
     */
    public static Color fromHex ( final String hex )
    {
        final String clean = TextUtils.removeSpacings ( hex );
        return Color.decode ( clean.startsWith ( "#" ) ? clean : "#" + clean );
    }

    /**
     * Returns RGB color string for the specified color.
     *
     * @param color color to process
     * @return RGB color string for the specified color
     */
    public static String toRGB ( final Color color )
    {
        return toRGB ( color, "," );
    }

    /**
     * Returns RGB color string for the specified color.
     *
     * @param color     color to process
     * @param separator color parts separator
     * @return RGB color string for the specified color
     */
    public static String toRGB ( final Color color, final String separator )
    {
        return color.getRed () + separator + color.getGreen () + separator + color.getBlue () +
                ( color.getAlpha () < 255 ? separator + color.getAlpha () : "" );
    }

    /**
     * Returns color decoded from an rgb color string.
     *
     * @param rgb rgb color string
     * @return color decoded from an rgb color string
     */
    public static Color fromRGB ( final String rgb )
    {
        return fromRGB ( rgb, "," );
    }

    /**
     * Returns color decoded from an rgb color string.
     *
     * @param rgb       rgb color string
     * @param separator color parts separator
     * @return color decoded from an rgb color string
     */
    public static Color fromRGB ( final String rgb, final String separator )
    {
        final String clean = TextUtils.removeSpacings ( rgb );
        final StringTokenizer st = new StringTokenizer ( clean, separator, false );
        final int count = st.countTokens ();
        if ( count == 3 )
        {
            return new Color ( Integer.parseInt ( st.nextToken () ), Integer.parseInt ( st.nextToken () ),
                    Integer.parseInt ( st.nextToken () ) );
        }
        else if ( count == 4 )
        {
            return new Color ( Integer.parseInt ( st.nextToken () ), Integer.parseInt ( st.nextToken () ),
                    Integer.parseInt ( st.nextToken () ), Integer.parseInt ( st.nextToken () ) );
        }
        else
        {
            throw new RuntimeException ( "Unable to parse RGB color: " + rgb );
        }
    }

    /**
     * Returns randomly generated soft color based on the specified color.
     *
     * @param base color base
     * @return randomly generated soft color based on the specified color
     */
    public static Color softColor ( final Color base )
    {
        final Random random = new Random ();
        int red = random.nextInt ( 256 );
        int green = random.nextInt ( 256 );
        int blue = random.nextInt ( 256 );
        if ( base != null )
        {
            red = ( red + base.getRed () ) / 2;
            green = ( green + base.getGreen () ) / 2;
            blue = ( blue + base.getBlue () ) / 2;
        }
        return new Color ( red, green, blue );
    }
}