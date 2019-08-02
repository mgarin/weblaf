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

import java.awt.*;

/**
 * A filter which performs reduces noise by looking at each pixel's 8 neighbours, and if it's a minimum or maximum,
 * replacing it by the next minimum or maximum of the neighbours.
 *
 * @author Jerry Huxtable
 */
public class ReduceNoiseFilter extends WholeImageFilter
{
    public ReduceNoiseFilter ()
    {
    }

    private int smooth ( final int[] v )
    {
        int minindex = 0, maxindex = 0, min = Integer.MAX_VALUE, max = Integer.MIN_VALUE;

        for ( int i = 0; i < 9; i++ )
        {
            if ( i != 4 )
            {
                if ( v[ i ] < min )
                {
                    min = v[ i ];
                    minindex = i;
                }
                if ( v[ i ] > max )
                {
                    max = v[ i ];
                    maxindex = i;
                }
            }
        }
        if ( v[ 4 ] < min )
        {
            return v[ minindex ];
        }
        if ( v[ 4 ] > max )
        {
            return v[ maxindex ];
        }
        return v[ 4 ];
    }

    @Override
    protected int[] filterPixels ( final int width, final int height, final int[] inPixels, final Rectangle transformedSpace )
    {
        int index = 0;
        final int[] r = new int[ 9 ];
        final int[] g = new int[ 9 ];
        final int[] b = new int[ 9 ];
        final int[] outPixels = new int[ width * height ];

        for ( int y = 0; y < height; y++ )
        {
            for ( int x = 0; x < width; x++ )
            {
                int k = 0;
                final int irgb = inPixels[ index ];
                final int ir = ( irgb >> 16 ) & 0xff;
                final int ig = ( irgb >> 8 ) & 0xff;
                final int ib = irgb & 0xff;
                for ( int dy = -1; dy <= 1; dy++ )
                {
                    final int iy = y + dy;
                    if ( 0 <= iy && iy < height )
                    {
                        final int ioffset = iy * width;
                        for ( int dx = -1; dx <= 1; dx++ )
                        {
                            final int ix = x + dx;
                            if ( 0 <= ix && ix < width )
                            {
                                final int rgb = inPixels[ ioffset + ix ];
                                r[ k ] = ( rgb >> 16 ) & 0xff;
                                g[ k ] = ( rgb >> 8 ) & 0xff;
                                b[ k ] = rgb & 0xff;
                            }
                            else
                            {
                                r[ k ] = ir;
                                g[ k ] = ig;
                                b[ k ] = ib;
                            }
                            k++;
                        }
                    }
                    else
                    {
                        for ( int dx = -1; dx <= 1; dx++ )
                        {
                            r[ k ] = ir;
                            g[ k ] = ig;
                            b[ k ] = ib;
                            k++;
                        }
                    }
                }
                outPixels[ index ] = ( inPixels[ index ] & 0xff000000 ) | ( smooth ( r ) << 16 ) | ( smooth ( g ) << 8 ) | smooth ( b );
                index++;
            }
        }
        return outPixels;
    }

    @Override
    public String toString ()
    {
        return "Blur/Smooth";
    }
}