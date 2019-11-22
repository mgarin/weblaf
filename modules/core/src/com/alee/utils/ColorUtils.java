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

import com.alee.api.annotations.NotNull;

import java.awt.*;
import java.util.Locale;
import java.util.Random;
import java.util.StringTokenizer;

/**
 * This class provides a set of utilities to work with colors.
 *
 * @author Mikle Garin
 */
public final class ColorUtils
{
    /**
     * Fully transparent white color.
     */
    private static final Color TRANSPARENT_COLOR = new Color ( 255, 255, 255, 0 );

    /**
     * Private constructor to avoid instantiation.
     */
    private ColorUtils ()
    {
        throw new UtilityException ( "Utility classes are not meant to be instantiated" );
    }

    /**
     * Returns fully transparent white {@link Color}.
     *
     * @return fully transparent white {@link Color}
     */
    @NotNull
    public static Color transparent ()
    {
        return TRANSPARENT_COLOR;
    }

    /**
     * Returns grayscale version of the specified {@link Color}.
     *
     * @param color {@link Color} to make grayscale version of
     * @return grayscale version of the specified {@link Color}
     */
    @NotNull
    public static Color grayscale ( @NotNull final Color color )
    {
        final int avg = ( int ) ( color.getRed () * 0.299 + color.getGreen () * 0.587 + color.getBlue () * 0.114 );
        return new Color ( avg, avg, avg, color.getAlpha () );
    }

    /**
     * Returns {@link Color} with modified alpha value.
     *
     * @param color {@link Color} to convert
     * @param alpha new alpha value
     * @return {@link Color} with modified alpha value
     */
    @NotNull
    public static Color transparent ( @NotNull final Color color, final int alpha )
    {
        return new Color ( color.getRed (), color.getGreen (), color.getBlue (), alpha );
    }

    /**
     * Returns {@link Color} with alpha value set to 255.
     *
     * @param color {@link Color} to convert
     * @return {@link Color} with alpha value set to 255
     */
    @NotNull
    public static Color opaque ( @NotNull final Color color )
    {
        return color.getAlpha () != 255 ? transparent ( color, 255 ) : color;
    }

    /**
     * Returns web-safe {@link Color}.
     *
     * @param color {@link Color} to convert
     * @return web-safe {@link Color}
     */
    @NotNull
    public static Color webSafe ( @NotNull final Color color )
    {
        return new Color (
                webSafe ( color.getRed () ),
                webSafe ( color.getGreen () ),
                webSafe ( color.getBlue () ),
                webSafe ( color.getAlpha () )
        );
    }

    /**
     * Returns web-safe color value.
     * todo This is still not completely correct
     * todo Color values should approximate relatively to other values
     *
     * @param value value to process
     * @return web-safe color value
     */
    public static int webSafe ( final int value )
    {
        final int webSafe;
        if ( 0 <= value && value <= 51 )
        {
            webSafe = value > 51 - value ? 51 : 0;
        }
        else if ( 51 < value && value <= 102 )
        {
            webSafe = value - 51 > 102 - value ? 102 : 51;
        }
        else if ( 102 < value && value <= 153 )
        {
            webSafe = value - 102 > 153 - value ? 153 : 102;
        }
        else if ( 153 < value && value <= 204 )
        {
            webSafe = value - 153 > 204 - value ? 204 : 153;
        }
        else if ( 204 < value && value <= 255 )
        {
            webSafe = value - 204 > 255 - value ? 255 : 204;
        }
        else
        {
            webSafe = value;
        }
        return webSafe;
    }

    /**
     * Returns intermediate {@link Color} between two specified {@link Color}s.
     *
     * @param color1   first {@link Color}
     * @param color2   second {@link Color}
     * @param progress progress of the intermediate {@link Color}
     * @return intermediate {@link Color} between two specified {@link Color}s
     */
    @NotNull
    public static Color intermediate ( @NotNull final Color color1, @NotNull final Color color2, final float progress )
    {
        final Color intermediate;
        if ( progress <= 0f )
        {
            intermediate = color1;
        }
        else if ( progress >= 1f )
        {
            intermediate = color2;
        }
        else
        {
            intermediate = new Color (
                    intermediate ( color1.getRed (), color2.getRed (), progress ),
                    intermediate ( color1.getGreen (), color2.getGreen (), progress ),
                    intermediate ( color1.getBlue (), color2.getBlue (), progress ),
                    intermediate ( color1.getAlpha (), color2.getAlpha (), progress )
            );
        }
        return intermediate;
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
     * Returns hex string for the specified {@link Color}.
     *
     * @param color {@link Color} to convert
     * @return hex string for the specified {@link Color}
     */
    @NotNull
    public static String toHex ( @NotNull final Color color )
    {
        final StringBuilder hex = new StringBuilder ( "#" );
        hex.append ( toHexValue ( color.getRed () ) );
        hex.append ( toHexValue ( color.getGreen () ) );
        hex.append ( toHexValue ( color.getBlue () ) );
        if ( color.getAlpha () < 255 )
        {
            hex.append ( toHexValue ( color.getAlpha () ) );
        }
        return hex.toString ().toUpperCase ( Locale.ROOT );
    }

    /**
     * Returns hex {@link String} for the specified int value.
     *
     * @param value int value
     * @return hex {@link String} for the specified int value
     */
    @NotNull
    private static String toHexValue ( final int value )
    {
        final String hex = Integer.toHexString ( value );
        return hex.length () == 1 ? "0" + hex : hex;
    }

    /**
     * Returns color decoded from a hex color string.
     *
     * @param hex hex color string
     * @return color decoded from a hex color string
     */
    @NotNull
    public static Color fromHex ( @NotNull final String hex )
    {
        final int offset = hex.startsWith ( "#" ) ? 1 : 0;
        final int red = fromHexValue ( hex.substring ( offset, offset + 2 ) );
        final int green = fromHexValue ( hex.substring ( offset + 2, offset + 4 ) );
        final int blue = fromHexValue ( hex.substring ( offset + 4, offset + 6 ) );
        final int alpha = hex.length () > 7 ? fromHexValue ( hex.substring ( offset + 6, offset + 8 ) ) : 255;
        return new Color ( red, green, blue, alpha );
    }

    /**
     * Returns int value for the specified hex {@link String}.
     *
     * @param hex hex {@link String}
     * @return int value for the specified hex {@link String}
     */
    private static int fromHexValue ( @NotNull final String hex )
    {
        return Integer.valueOf ( hex, 16 );
    }

    /**
     * Returns RGBA color string for the specified {@link Color}.
     *
     * @param color {@link Color} to convert
     * @return RGBA color string for the specified {@link Color}
     */
    @NotNull
    public static String toRGBA ( @NotNull final Color color )
    {
        return toRGBA ( color, "," );
    }

    /**
     * Returns RGBA color string for the specified {@link Color}.
     *
     * @param color     {@link Color} to convert
     * @param separator color values separator
     * @return RGBA color string for the specified {@link Color}
     */
    @NotNull
    public static String toRGBA ( @NotNull final Color color, @NotNull final String separator )
    {
        return color.getRed () +
                separator + color.getGreen () +
                separator + color.getBlue () +
                ( color.getAlpha () < 255 ? separator + color.getAlpha () : "" );
    }

    /**
     * Returns {@link Color} decoded from RGBA color string.
     *
     * @param rgba RGBA color string
     * @return {@link Color} decoded from RGBA color string
     */
    @NotNull
    public static Color fromRGBA ( @NotNull final String rgba )
    {
        return fromRGBA ( rgba, "," );
    }

    /**
     * Returns {@link Color} decoded from RGBA color string.
     *
     * @param rgba      RGBA color string
     * @param separator color values separator
     * @return {@link Color} decoded from RGBA color string
     */
    @NotNull
    public static Color fromRGBA ( @NotNull final String rgba, @NotNull final String separator )
    {
        final Color color;
        final String clean = TextUtils.removeSpacings ( rgba );
        final StringTokenizer st = new StringTokenizer ( clean, separator, false );
        final int count = st.countTokens ();
        if ( count == 3 )
        {
            color = new Color (
                    Integer.parseInt ( st.nextToken () ),
                    Integer.parseInt ( st.nextToken () ),
                    Integer.parseInt ( st.nextToken () )
            );
        }
        else if ( count == 4 )
        {
            color = new Color (
                    Integer.parseInt ( st.nextToken () ),
                    Integer.parseInt ( st.nextToken () ),
                    Integer.parseInt ( st.nextToken () ),
                    Integer.parseInt ( st.nextToken () )
            );
        }
        else
        {
            throw new RuntimeException ( "Unable to parse RGB color: " + rgba );
        }
        return color;
    }

    /**
     * Returns randomly generated soft {@link Color} based on the specified {@link Color}.
     *
     * @param base {@link Color} base
     * @return randomly generated soft {@link Color} based on the specified {@link Color}
     */
    @NotNull
    public static Color softColor ( @NotNull final Color base )
    {
        final Random random = new Random ();
        return new Color (
                ( random.nextInt ( 256 ) + base.getRed () ) / 2,
                ( random.nextInt ( 256 ) + base.getGreen () ) / 2,
                ( random.nextInt ( 256 ) + base.getBlue () ) / 2
        );
    }
}