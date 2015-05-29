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

/*
Copyright 2006 Jerry Huxtable

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/

package com.alee.graphics.filters;

/**
 * A class containing static math methods useful for image processing.
 */

public class ImageMath
{
    public final static float PI = ( float ) Math.PI;
    public final static float HALF_PI = ( float ) Math.PI / 2.0f;
    public final static float QUARTER_PI = ( float ) Math.PI / 4.0f;
    public final static float TWO_PI = ( float ) Math.PI * 2.0f;

    /**
     * Apply a bias to a number in the unit interval, moving numbers towards 0 or 1 according to the bias parameter.
     *
     * @param a the number to bias
     * @param b the bias parameter. 0.5 means no change, smaller values bias towards 0, larger towards 1.
     * @return the output value
     */
    public static float bias ( final float a, final float b )
    {
        //		return (float)Math.pow(a, Math.log(b) / Math.log(0.5));
        return a / ( ( 1.0f / b - 2 ) * ( 1.0f - a ) + 1 );
    }

    /**
     * A variant of the gamma function.
     *
     * @param a the number to apply gain to
     * @param b the gain parameter. 0.5 means no change, smaller values reduce gain, larger values increase gain.
     * @return the output value
     */
    public static float gain ( final float a, final float b )
    {
        /*
                float p = (float)Math.log(1.0 - b) / (float)Math.log(0.5);

                if (a < .001)
                    return 0.0f;
                else if (a > .999)
                    return 1.0f;
                if (a < 0.5)
                    return (float)Math.pow(2 * a, p) / 2;
                else
                    return 1.0f - (float)Math.pow(2 * (1. - a), p) / 2;
        */
        final float c = ( 1.0f / b - 2.0f ) * ( 1.0f - 2.0f * a );
        if ( a < 0.5 )
        {
            return a / ( c + 1.0f );
        }
        else
        {
            return ( c - a ) / ( c - 1.0f );
        }
    }

    /**
     * The step function. Returns 0 below a threshold, 1 above.
     *
     * @param a the threshold position
     * @param x the input parameter
     * @return the output value - 0 or 1
     */
    public static float step ( final float a, final float x )
    {
        return ( x < a ) ? 0.0f : 1.0f;
    }

    /**
     * The pulse function. Returns 1 between two thresholds, 0 outside.
     *
     * @param a the lower threshold position
     * @param b the upper threshold position
     * @param x the input parameter
     * @return the output value - 0 or 1
     */
    public static float pulse ( final float a, final float b, final float x )
    {
        return ( x < a || x >= b ) ? 0.0f : 1.0f;
    }

    /**
     * A smoothed pulse function. A cubic function is used to smooth the step between two thresholds.
     *
     * @param a1 the lower threshold position for the start of the pulse
     * @param a2 the upper threshold position for the start of the pulse
     * @param b1 the lower threshold position for the end of the pulse
     * @param b2 the upper threshold position for the end of the pulse
     * @param x  the input parameter
     * @return the output value
     */
    public static float smoothPulse ( final float a1, final float a2, final float b1, final float b2, float x )
    {
        if ( x < a1 || x >= b2 )
        {
            return 0;
        }
        if ( x >= a2 )
        {
            if ( x < b1 )
            {
                return 1.0f;
            }
            x = ( x - b1 ) / ( b2 - b1 );
            return 1.0f - ( x * x * ( 3.0f - 2.0f * x ) );
        }
        x = ( x - a1 ) / ( a2 - a1 );
        return x * x * ( 3.0f - 2.0f * x );
    }

    /**
     * A smoothed step function. A cubic function is used to smooth the step between two thresholds.
     *
     * @param a the lower threshold position
     * @param b the upper threshold position
     * @param x the input parameter
     * @return the output value
     */
    public static float smoothStep ( final float a, final float b, float x )
    {
        if ( x < a )
        {
            return 0;
        }
        if ( x >= b )
        {
            return 1;
        }
        x = ( x - a ) / ( b - a );
        return x * x * ( 3 - 2 * x );
    }

    /**
     * A "circle up" function. Returns y on a unit circle given 1-x. Useful for forming bevels.
     *
     * @param x the input parameter in the range 0..1
     * @return the output value
     */
    public static float circleUp ( float x )
    {
        x = 1 - x;
        return ( float ) Math.sqrt ( 1 - x * x );
    }

    /**
     * A "circle down" function. Returns 1-y on a unit circle given x. Useful for forming bevels.
     *
     * @param x the input parameter in the range 0..1
     * @return the output value
     */
    public static float circleDown ( final float x )
    {
        return 1.0f - ( float ) Math.sqrt ( 1 - x * x );
    }

    /**
     * Clamp a value to an interval.
     *
     * @param a the lower clamp threshold
     * @param b the upper clamp threshold
     * @param x the input parameter
     * @return the clamped value
     */
    public static float clamp ( final float x, final float a, final float b )
    {
        return ( x < a ) ? a : ( x > b ) ? b : x;
    }

    /**
     * Clamp a value to an interval.
     *
     * @param a the lower clamp threshold
     * @param b the upper clamp threshold
     * @param x the input parameter
     * @return the clamped value
     */
    public static int clamp ( final int x, final int a, final int b )
    {
        return ( x < a ) ? a : ( x > b ) ? b : x;
    }

    /**
     * Return a mod b. This differs from the % operator with respect to negative numbers.
     *
     * @param a the dividend
     * @param b the divisor
     * @return a mod b
     */
    public static double mod ( double a, final double b )
    {
        final int n = ( int ) ( a / b );

        a -= n * b;
        if ( a < 0 )
        {
            return a + b;
        }
        return a;
    }

    /**
     * Return a mod b. This differs from the % operator with respect to negative numbers.
     *
     * @param a the dividend
     * @param b the divisor
     * @return a mod b
     */
    public static float mod ( float a, final float b )
    {
        final int n = ( int ) ( a / b );

        a -= n * b;
        if ( a < 0 )
        {
            return a + b;
        }
        return a;
    }

    /**
     * Return a mod b. This differs from the % operator with respect to negative numbers.
     *
     * @param a the dividend
     * @param b the divisor
     * @return a mod b
     */
    public static int mod ( int a, final int b )
    {
        final int n = a / b;

        a -= n * b;
        if ( a < 0 )
        {
            return a + b;
        }
        return a;
    }

    /**
     * The triangle function. Returns a repeating triangle shape in the range 0..1 with wavelength 1.0
     *
     * @param x the input parameter
     * @return the output value
     */
    public static float triangle ( final float x )
    {
        final float r = mod ( x, 1.0f );
        return 2.0f * ( r < 0.5 ? r : 1 - r );
    }

    /**
     * Linear interpolation.
     *
     * @param t the interpolation parameter
     * @param a the lower interpolation range
     * @param b the upper interpolation range
     * @return the interpolated value
     */
    public static float lerp ( final float t, final float a, final float b )
    {
        return a + t * ( b - a );
    }

    /**
     * Linear interpolation.
     *
     * @param t the interpolation parameter
     * @param a the lower interpolation range
     * @param b the upper interpolation range
     * @return the interpolated value
     */
    public static int lerp ( final float t, final int a, final int b )
    {
        return ( int ) ( a + t * ( b - a ) );
    }

    /**
     * Linear interpolation of ARGB values.
     *
     * @param t    the interpolation parameter
     * @param rgb1 the lower interpolation range
     * @param rgb2 the upper interpolation range
     * @return the interpolated value
     */
    public static int mixColors ( final float t, final int rgb1, final int rgb2 )
    {
        int a1 = ( rgb1 >> 24 ) & 0xff;
        int r1 = ( rgb1 >> 16 ) & 0xff;
        int g1 = ( rgb1 >> 8 ) & 0xff;
        int b1 = rgb1 & 0xff;
        final int a2 = ( rgb2 >> 24 ) & 0xff;
        final int r2 = ( rgb2 >> 16 ) & 0xff;
        final int g2 = ( rgb2 >> 8 ) & 0xff;
        final int b2 = rgb2 & 0xff;
        a1 = lerp ( t, a1, a2 );
        r1 = lerp ( t, r1, r2 );
        g1 = lerp ( t, g1, g2 );
        b1 = lerp ( t, b1, b2 );
        return ( a1 << 24 ) | ( r1 << 16 ) | ( g1 << 8 ) | b1;
    }

    /**
     * Bilinear interpolation of ARGB values.
     *
     * @param x the X interpolation parameter 0..1
     * @param y the y interpolation parameter 0..1
     * @param p array of four ARGB values in the order NW, NE, SW, SE
     * @return the interpolated value
     */
    public static int bilinearInterpolate ( final float x, final float y, final int[] p )
    {
        float m0, m1;
        final int a0 = ( p[ 0 ] >> 24 ) & 0xff;
        final int r0 = ( p[ 0 ] >> 16 ) & 0xff;
        final int g0 = ( p[ 0 ] >> 8 ) & 0xff;
        final int b0 = p[ 0 ] & 0xff;
        final int a1 = ( p[ 1 ] >> 24 ) & 0xff;
        final int r1 = ( p[ 1 ] >> 16 ) & 0xff;
        final int g1 = ( p[ 1 ] >> 8 ) & 0xff;
        final int b1 = p[ 1 ] & 0xff;
        final int a2 = ( p[ 2 ] >> 24 ) & 0xff;
        final int r2 = ( p[ 2 ] >> 16 ) & 0xff;
        final int g2 = ( p[ 2 ] >> 8 ) & 0xff;
        final int b2 = p[ 2 ] & 0xff;
        final int a3 = ( p[ 3 ] >> 24 ) & 0xff;
        final int r3 = ( p[ 3 ] >> 16 ) & 0xff;
        final int g3 = ( p[ 3 ] >> 8 ) & 0xff;
        final int b3 = p[ 3 ] & 0xff;

        final float cx = 1.0f - x;
        final float cy = 1.0f - y;

        m0 = cx * a0 + x * a1;
        m1 = cx * a2 + x * a3;
        final int a = ( int ) ( cy * m0 + y * m1 );

        m0 = cx * r0 + x * r1;
        m1 = cx * r2 + x * r3;
        final int r = ( int ) ( cy * m0 + y * m1 );

        m0 = cx * g0 + x * g1;
        m1 = cx * g2 + x * g3;
        final int g = ( int ) ( cy * m0 + y * m1 );

        m0 = cx * b0 + x * b1;
        m1 = cx * b2 + x * b3;
        final int b = ( int ) ( cy * m0 + y * m1 );

        return ( a << 24 ) | ( r << 16 ) | ( g << 8 ) | b;
    }

    /**
     * Return the NTSC gray level of an RGB value.
     *
     * @param rgb the input pixel
     * @return the gray level (0-255)
     */
    public static int brightnessNTSC ( final int rgb )
    {
        final int r = ( rgb >> 16 ) & 0xff;
        final int g = ( rgb >> 8 ) & 0xff;
        final int b = rgb & 0xff;
        return ( int ) ( r * 0.299f + g * 0.587f + b * 0.114f );
    }

    // Catmull-Rom splines
    private final static float m00 = -0.5f;
    private final static float m01 = 1.5f;
    private final static float m02 = -1.5f;
    private final static float m03 = 0.5f;
    private final static float m10 = 1.0f;
    private final static float m11 = -2.5f;
    private final static float m12 = 2.0f;
    private final static float m13 = -0.5f;
    private final static float m20 = -0.5f;
    private final static float m21 = 0.0f;
    private final static float m22 = 0.5f;
    private final static float m23 = 0.0f;
    private final static float m30 = 0.0f;
    private final static float m31 = 1.0f;
    private final static float m32 = 0.0f;
    private final static float m33 = 0.0f;

    /**
     * Compute a Catmull-Rom spline.
     *
     * @param x        the input parameter
     * @param numKnots the number of knots in the spline
     * @param knots    the array of knots
     * @return the spline value
     */
    public static float spline ( float x, final int numKnots, final float[] knots )
    {
        int span;
        final int numSpans = numKnots - 3;
        final float k0;
        final float k1;
        final float k2;
        final float k3;
        final float c0;
        final float c1;
        final float c2;
        final float c3;

        if ( numSpans < 1 )
        {
            throw new IllegalArgumentException ( "Too few knots in spline" );
        }

        x = clamp ( x, 0, 1 ) * numSpans;
        span = ( int ) x;
        if ( span > numKnots - 4 )
        {
            span = numKnots - 4;
        }
        x -= span;

        k0 = knots[ span ];
        k1 = knots[ span + 1 ];
        k2 = knots[ span + 2 ];
        k3 = knots[ span + 3 ];

        c3 = m00 * k0 + m01 * k1 + m02 * k2 + m03 * k3;
        c2 = m10 * k0 + m11 * k1 + m12 * k2 + m13 * k3;
        c1 = m20 * k0 + m21 * k1 + m22 * k2 + m23 * k3;
        c0 = m30 * k0 + m31 * k1 + m32 * k2 + m33 * k3;

        return ( ( c3 * x + c2 ) * x + c1 ) * x + c0;
    }

    /**
     * Compute a Catmull-Rom spline, but with variable knot spacing.
     *
     * @param x        the input parameter
     * @param numKnots the number of knots in the spline
     * @param xKnots   the array of knot x values
     * @param yKnots   the array of knot y values
     * @return the spline value
     */
    public static float spline ( final float x, final int numKnots, final int[] xKnots, final int[] yKnots )
    {
        int span;
        final int numSpans = numKnots - 3;
        final float k0;
        final float k1;
        final float k2;
        final float k3;
        final float c0;
        final float c1;
        final float c2;
        final float c3;

        if ( numSpans < 1 )
        {
            throw new IllegalArgumentException ( "Too few knots in spline" );
        }

        for ( span = 0; span < numSpans; span++ )
        {
            if ( xKnots[ span + 1 ] > x )
            {
                break;
            }
        }
        if ( span > numKnots - 3 )
        {
            span = numKnots - 3;
        }
        float t = ( x - xKnots[ span ] ) / ( xKnots[ span + 1 ] - xKnots[ span ] );
        span--;
        if ( span < 0 )
        {
            span = 0;
            t = 0;
        }

        k0 = yKnots[ span ];
        k1 = yKnots[ span + 1 ];
        k2 = yKnots[ span + 2 ];
        k3 = yKnots[ span + 3 ];

        c3 = m00 * k0 + m01 * k1 + m02 * k2 + m03 * k3;
        c2 = m10 * k0 + m11 * k1 + m12 * k2 + m13 * k3;
        c1 = m20 * k0 + m21 * k1 + m22 * k2 + m23 * k3;
        c0 = m30 * k0 + m31 * k1 + m32 * k2 + m33 * k3;

        return ( ( c3 * t + c2 ) * t + c1 ) * t + c0;
    }

    /**
     * Compute a Catmull-Rom spline for RGB values.
     *
     * @param x        the input parameter
     * @param numKnots the number of knots in the spline
     * @param knots    the array of knots
     * @return the spline value
     */
    public static int colorSpline ( float x, final int numKnots, final int[] knots )
    {
        int span;
        final int numSpans = numKnots - 3;
        float k0, k1, k2, k3;
        float c0, c1, c2, c3;

        if ( numSpans < 1 )
        {
            throw new IllegalArgumentException ( "Too few knots in spline" );
        }

        x = clamp ( x, 0, 1 ) * numSpans;
        span = ( int ) x;
        if ( span > numKnots - 4 )
        {
            span = numKnots - 4;
        }
        x -= span;

        int v = 0;
        for ( int i = 0; i < 4; i++ )
        {
            final int shift = i * 8;

            k0 = ( knots[ span ] >> shift ) & 0xff;
            k1 = ( knots[ span + 1 ] >> shift ) & 0xff;
            k2 = ( knots[ span + 2 ] >> shift ) & 0xff;
            k3 = ( knots[ span + 3 ] >> shift ) & 0xff;

            c3 = m00 * k0 + m01 * k1 + m02 * k2 + m03 * k3;
            c2 = m10 * k0 + m11 * k1 + m12 * k2 + m13 * k3;
            c1 = m20 * k0 + m21 * k1 + m22 * k2 + m23 * k3;
            c0 = m30 * k0 + m31 * k1 + m32 * k2 + m33 * k3;
            int n = ( int ) ( ( ( c3 * x + c2 ) * x + c1 ) * x + c0 );
            if ( n < 0 )
            {
                n = 0;
            }
            else if ( n > 255 )
            {
                n = 255;
            }
            v |= n << shift;
        }

        return v;
    }

    /**
     * Compute a Catmull-Rom spline for RGB values, but with variable knot spacing.
     *
     * @param x        the input parameter
     * @param numKnots the number of knots in the spline
     * @param xKnots   the array of knot x values
     * @param yKnots   the array of knot y values
     * @return the spline value
     */
    public static int colorSpline ( final int x, final int numKnots, final int[] xKnots, final int[] yKnots )
    {
        int span;
        final int numSpans = numKnots - 3;
        float k0, k1, k2, k3;
        float c0, c1, c2, c3;

        if ( numSpans < 1 )
        {
            throw new IllegalArgumentException ( "Too few knots in spline" );
        }

        for ( span = 0; span < numSpans; span++ )
        {
            if ( xKnots[ span + 1 ] > x )
            {
                break;
            }
        }
        if ( span > numKnots - 3 )
        {
            span = numKnots - 3;
        }
        float t = ( float ) ( x - xKnots[ span ] ) / ( xKnots[ span + 1 ] - xKnots[ span ] );
        span--;
        if ( span < 0 )
        {
            span = 0;
            t = 0;
        }

        int v = 0;
        for ( int i = 0; i < 4; i++ )
        {
            final int shift = i * 8;

            k0 = ( yKnots[ span ] >> shift ) & 0xff;
            k1 = ( yKnots[ span + 1 ] >> shift ) & 0xff;
            k2 = ( yKnots[ span + 2 ] >> shift ) & 0xff;
            k3 = ( yKnots[ span + 3 ] >> shift ) & 0xff;

            c3 = m00 * k0 + m01 * k1 + m02 * k2 + m03 * k3;
            c2 = m10 * k0 + m11 * k1 + m12 * k2 + m13 * k3;
            c1 = m20 * k0 + m21 * k1 + m22 * k2 + m23 * k3;
            c0 = m30 * k0 + m31 * k1 + m32 * k2 + m33 * k3;
            int n = ( int ) ( ( ( c3 * t + c2 ) * t + c1 ) * t + c0 );
            if ( n < 0 )
            {
                n = 0;
            }
            else if ( n > 255 )
            {
                n = 255;
            }
            v |= n << shift;
        }

        return v;
    }

    /**
     * An implementation of Fant's resampling algorithm.
     *
     * @param source the source pixels
     * @param dest   the destination pixels
     * @param length the length of the scanline to resample
     * @param offset the start offset into the arrays
     * @param stride the offset between pixels in consecutive rows
     * @param out    an array of output positions for each pixel
     */
    public static void resample ( final int[] source, final int[] dest, final int length, final int offset, final int stride,
                                  final float[] out )
    {
        int i, j;
        float sizfac;
        float inSegment;
        float outSegment;
        int a, r, g, b, nextA, nextR, nextG, nextB;
        float aSum, rSum, gSum, bSum;
        final float[] in;
        int srcIndex = offset;
        int destIndex = offset;
        final int lastIndex = source.length;
        int rgb;

        in = new float[ length + 1 ];
        i = 0;
        for ( j = 0; j < length; j++ )
        {
            while ( out[ i + 1 ] < j )
            {
                i++;
            }
            in[ j ] = i + ( j - out[ i ] ) / ( out[ i + 1 ] - out[ i ] );
        }
        in[ length ] = length;

        inSegment = 1.0f;
        outSegment = in[ 1 ];
        sizfac = outSegment;
        aSum = rSum = gSum = bSum = 0.0f;
        rgb = source[ srcIndex ];
        a = ( rgb >> 24 ) & 0xff;
        r = ( rgb >> 16 ) & 0xff;
        g = ( rgb >> 8 ) & 0xff;
        b = rgb & 0xff;
        srcIndex += stride;
        rgb = source[ srcIndex ];
        nextA = ( rgb >> 24 ) & 0xff;
        nextR = ( rgb >> 16 ) & 0xff;
        nextG = ( rgb >> 8 ) & 0xff;
        nextB = rgb & 0xff;
        srcIndex += stride;
        i = 1;

        while ( i < length )
        {
            final float aIntensity = inSegment * a + ( 1.0f - inSegment ) * nextA;
            final float rIntensity = inSegment * r + ( 1.0f - inSegment ) * nextR;
            final float gIntensity = inSegment * g + ( 1.0f - inSegment ) * nextG;
            final float bIntensity = inSegment * b + ( 1.0f - inSegment ) * nextB;
            if ( inSegment < outSegment )
            {
                aSum += ( aIntensity * inSegment );
                rSum += ( rIntensity * inSegment );
                gSum += ( gIntensity * inSegment );
                bSum += ( bIntensity * inSegment );
                outSegment -= inSegment;
                inSegment = 1.0f;
                a = nextA;
                r = nextR;
                g = nextG;
                b = nextB;
                if ( srcIndex < lastIndex )
                {
                    rgb = source[ srcIndex ];
                }
                nextA = ( rgb >> 24 ) & 0xff;
                nextR = ( rgb >> 16 ) & 0xff;
                nextG = ( rgb >> 8 ) & 0xff;
                nextB = rgb & 0xff;
                srcIndex += stride;
            }
            else
            {
                aSum += ( aIntensity * outSegment );
                rSum += ( rIntensity * outSegment );
                gSum += ( gIntensity * outSegment );
                bSum += ( bIntensity * outSegment );
                dest[ destIndex ] = ( ( int ) Math.min ( aSum / sizfac, 255 ) << 24 ) |
                        ( ( int ) Math.min ( rSum / sizfac, 255 ) << 16 ) |
                        ( ( int ) Math.min ( gSum / sizfac, 255 ) << 8 ) |
                        ( int ) Math.min ( bSum / sizfac, 255 );
                destIndex += stride;
                rSum = gSum = bSum = 0.0f;
                inSegment -= outSegment;
                outSegment = in[ i + 1 ] - in[ i ];
                sizfac = outSegment;
                i++;
            }
        }
    }

}