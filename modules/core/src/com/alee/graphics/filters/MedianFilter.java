/*
 * Copyright 2006 Jerry Huxtable
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.alee.graphics.filters;

import java.awt.*;

/**
 * A filter which performs a 3x3 median operation. Useful for removing dust and noise.
 *
 * @author Jerry Huxtable
 */

public class MedianFilter extends WholeImageFilter
{
    public MedianFilter ()
    {
    }

    private int median ( final int[] array )
    {
        int max, maxIndex;

        for ( int i = 0; i < 4; i++ )
        {
            max = 0;
            maxIndex = 0;
            for ( int j = 0; j < 9; j++ )
            {
                if ( array[ j ] > max )
                {
                    max = array[ j ];
                    maxIndex = j;
                }
            }
            array[ maxIndex ] = 0;
        }
        max = 0;
        for ( int i = 0; i < 9; i++ )
        {
            if ( array[ i ] > max )
            {
                max = array[ i ];
            }
        }
        return max;
    }

    private int rgbMedian ( final int[] r, final int[] g, final int[] b )
    {
        int sum, index = 0, min = Integer.MAX_VALUE;

        for ( int i = 0; i < 9; i++ )
        {
            sum = 0;
            for ( int j = 0; j < 9; j++ )
            {
                sum += Math.abs ( r[ i ] - r[ j ] );
                sum += Math.abs ( g[ i ] - g[ j ] );
                sum += Math.abs ( b[ i ] - b[ j ] );
            }
            if ( sum < min )
            {
                min = sum;
                index = i;
            }
        }
        return index;
    }

    @Override
    protected int[] filterPixels ( final int width, final int height, final int[] inPixels, final Rectangle transformedSpace )
    {
        int index = 0;
        final int[] argb = new int[ 9 ];
        final int[] r = new int[ 9 ];
        final int[] g = new int[ 9 ];
        final int[] b = new int[ 9 ];
        final int[] outPixels = new int[ width * height ];

        for ( int y = 0; y < height; y++ )
        {
            for ( int x = 0; x < width; x++ )
            {
                int k = 0;
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
                                argb[ k ] = rgb;
                                r[ k ] = ( rgb >> 16 ) & 0xff;
                                g[ k ] = ( rgb >> 8 ) & 0xff;
                                b[ k ] = rgb & 0xff;
                                k++;
                            }
                        }
                    }
                }
                while ( k < 9 )
                {
                    argb[ k ] = 0xff000000;
                    r[ k ] = g[ k ] = b[ k ] = 0;
                    k++;
                }
                outPixels[ index++ ] = argb[ rgbMedian ( r, g, b ) ];
            }
        }
        return outPixels;
    }

    @Override
    public String toString ()
    {
        return "Blur/Median";
    }
}