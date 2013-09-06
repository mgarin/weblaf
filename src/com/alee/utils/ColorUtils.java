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
     * Returns color with alpha value set to 255.
     *
     * @param color color to modify
     * @return color with alpha value set to 255
     */
    public static Color removeAlpha ( Color color )
    {
        if ( color.getAlpha () != 255 )
        {
            return new Color ( color.getRGB (), false );
        }
        else
        {
            return color;
        }
    }

    /**
     * Returns color with modified alpha.
     *
     * @param color color to process
     * @param alpha new alpha
     * @return color with modified alpha
     */
    public static Color changeAlpha ( Color color, int alpha )
    {
        return new Color ( color.getRed (), color.getGreen (), color.getBlue (), alpha );
    }

    /**
     * Returns intermediate color between two specified colors.
     *
     * @param color1   first color
     * @param color2   second color
     * @param progress progress of the intermediate color
     * @return intermediate color between two specified colors
     */
    public static Color getIntermediateColor ( Color color1, Color color2, float progress )
    {
        return new Color ( getIntermediateValue ( color1.getRed (), color2.getRed (), progress ),
                getIntermediateValue ( color1.getGreen (), color2.getGreen (), progress ),
                getIntermediateValue ( color1.getBlue (), color2.getBlue (), progress ),
                getIntermediateValue ( color1.getAlpha (), color2.getAlpha (), progress ) );
    }

    /**
     * Returns intermediate value between two specified values.
     *
     * @param value1   first value
     * @param value2   second value
     * @param progress progress of the intermediate value
     * @return intermediate value between two specified values
     */
    public static int getIntermediateValue ( int value1, int value2, float progress )
    {
        return value1 + Math.round ( ( ( float ) value2 - value1 ) * progress );
    }

    /**
     * Returns web-safe color.
     *
     * @param color color to process
     * @return web-safe color
     */
    public static Color getWebSafeColor ( Color color )
    {
        return new Color ( getWebSafeValue ( color.getRed () ), getWebSafeValue ( color.getGreen () ),
                getWebSafeValue ( color.getBlue () ) );
    }

    /**
     * Returns web-safe color value.
     *
     * @param value value to process
     * @return web-safe color value
     */
    public static int getWebSafeValue ( int value )
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
     * Returns hex color string for the specified color.
     *
     * @param color color to process
     * @return hex color string for the specified color
     */
    public static String getHexColor ( Color color )
    {
        return getHexColor ( color.getRGB () );
    }

    /**
     * Returns hex color string for the specified rgb value.
     *
     * @param rgb rgb value
     * @return hex color string for the specified rgb value
     */
    public static String getHexColor ( int rgb )
    {
        String hex = Integer.toHexString ( rgb ).toUpperCase ();
        return hex.substring ( 2, hex.length () );
    }

    /**
     * Returns color decoded from a hex color string.
     *
     * @param hex hex color string
     * @return color decoded from a hex color string
     */
    public static Color parseHexColor ( String hex )
    {
        hex = hex.replaceAll ( " ", "" );
        return Color.decode ( hex.startsWith ( "#" ) ? hex : ( "#" + hex ) );
    }

    /**
     * Returns color decoded from an rgb color string.
     *
     * @param rgb rgb color string
     * @return color decoded from an rgb color string
     */
    public static Color parseRgbColor ( String rgb )
    {
        return parseRgbColor ( rgb, "," );
    }

    /**
     * Returns color decoded from an rgb color string.
     *
     * @param rgb       rgb color string
     * @param separator color parts separator
     * @return color decoded from an rgb color string
     */
    public static Color parseRgbColor ( String rgb, String separator )
    {
        rgb = rgb.replaceAll ( " ", "" );
        StringTokenizer st = new StringTokenizer ( rgb, separator, false );
        int count = st.countTokens ();
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
            return null;
        }
    }

    /**
     * Returns black color with specified alpha
     *
     * @param alpha color alpha
     * @return black color with specified alpha
     */
    public static Color black ( int alpha )
    {
        return new Color ( 0, 0, 0, alpha );
    }

    /**
     * Returns white color with specified alpha
     *
     * @param alpha color alpha
     * @return white color with specified alpha
     */
    public static Color white ( int alpha )
    {
        return new Color ( 255, 255, 255, alpha );
    }

    /**
     * Returns randomly generated soft color based on the specified color.
     *
     * @param base color base
     * @return randomly generated soft color based on the specified color
     */
    public static Color getRandomSoftColor ( Color base )
    {
        Random random = new Random ();
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