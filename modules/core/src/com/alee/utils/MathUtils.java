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

import java.util.Random;

/**
 * This class provides a set of utilities to perform various math operations.
 *
 * @author Mikle Garin
 */

public final class MathUtils
{
    /**
     * Random numbers generator.
     */
    private static final Random random = new Random ();

    /**
     * Returns random integer number between 0 (inclusive) and 100 (inclusive).
     *
     * @return random integer number between 0 (inclusive) and 100 (inclusive)
     */
    public static int random ()
    {
        return random.nextInt ( 101 );
    }

    /**
     * Returns random integer number between 0 (inclusive) and cap (exclusive).
     *
     * @param cap random numbers cap
     * @return random integer number between 0 (inclusive) and cap (exclusive)
     */
    public static int random ( final int cap )
    {
        return random.nextInt ( cap );
    }

    /**
     * Returns random integer number between min (inclusive) and max (inclusive).
     *
     * @param min random numbers minimum
     * @param max random numbers maximum
     * @return random integer number between min (inclusive) and max (inclusive)
     */
    public static int random ( final int min, final int max )
    {
        return min + random.nextInt ( Math.abs ( max + 1 - min ) );
    }

    /**
     * Returns random integer number from one the specified integers.
     *
     * @param numbers number to choose random one from
     * @return random integer number from one the specified integers
     */
    public static int random ( final int... numbers )
    {
        return numbers[ random ( 0, numbers.length - 1 ) ];
    }

    /**
     * Returns square for specified integer number.
     *
     * @param i integer number to process
     * @return square for specified integer number
     */
    public static int sqr ( final int i )
    {
        return i * i;
    }

    /**
     * Returns square for specified float number.
     *
     * @param f float number to process
     * @return square for specified float number
     */
    public static float sqr ( final float f )
    {
        return f * f;
    }

    /**
     * Returns rounded square root for the specified integer number.
     *
     * @param i integer number to process
     * @return rounded square root for the specified integer number
     */
    public static int sqrt ( final double i )
    {
        return ( int ) Math.round ( Math.sqrt ( i ) );
    }

    /**
     * Returns value limited by the specified range.
     *
     * @param min   range minimum
     * @param value value to limit
     * @param max   range maximum
     * @return value limited by the specified range
     */
    public static int limit ( final int min, final int value, final int max )
    {
        if ( min > max )
        {
            throw new RuntimeException ( "Minimum value cannot be greated than maximum value" );
        }
        return Math.max ( min, Math.min ( value, max ) );
    }

    /**
     * Returns value limited by the specified range.
     *
     * @param min   range minimum
     * @param value value to limit
     * @param max   range maximum
     * @return value limited by the specified range
     */
    public static float limit ( final float min, final float value, final float max )
    {
        if ( min > max )
        {
            throw new RuntimeException ( "Minimum value cannot be greated than maximum value" );
        }
        return Math.max ( min, Math.min ( value, max ) );
    }

    /**
     * Returns maximum of the specified integer numbers.
     *
     * @param integers integer numbers to process
     * @return maximum of the specified integer numbers
     */
    public static int max ( final int... integers )
    {
        int max = integers[ 0 ];
        for ( int i = 1; i < integers.length; i++ )
        {
            max = Math.max ( max, integers[ i ] );
        }
        return max;
    }

    /**
     * Returns minimum of the specified integer numbers.
     *
     * @param integers integer numbers to process
     * @return minimum of the specified integer numbers
     */
    public static int min ( final int... integers )
    {
        int min = integers[ 0 ];
        for ( int i = 1; i < integers.length; i++ )
        {
            min = Math.min ( min, integers[ i ] );
        }
        return min;
    }

    //    /**
    //     * Returns maximum of the specified float numbers.
    //     *
    //     * @param floats float numbers to process
    //     * @return maximum of the specified float numbers
    //     */
    //    public static float max ( float... floats )
    //    {
    //        float max = floats[ 0 ];
    //        for ( int i = 1; i < floats.length; i++ )
    //        {
    //            max = Math.max ( max, floats[ i ] );
    //        }
    //        return max;
    //    }
    //
    //
    //    /**
    //     * Returns maximum of the specified double numbers.
    //     *
    //     * @param doubles double numbers to process
    //     * @return maximum of the specified double numbers
    //     */
    //    public static double max ( double... doubles )
    //    {
    //        double max = doubles[ 0 ];
    //        for ( int i = 1; i < doubles.length; i++ )
    //        {
    //            max = Math.max ( max, doubles[ i ] );
    //        }
    //        return max;
    //    }
}