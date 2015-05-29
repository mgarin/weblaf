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

import java.awt.image.BufferedImage;

/**
 * A filter which performs a box blur on an image. The horizontal and vertical blurs can be specified separately and a number of iterations
 * can be given which allows an approximation to Gaussian blur.
 */

public class BoxBlurFilter extends AbstractBufferedImageOp
{
    private int hRadius;
    private int vRadius;
    private int iterations = 1;

    public BoxBlurFilter ()
    {
        super ();
    }

    public BoxBlurFilter ( final int hRadius, final int vRadius, final int iterations )
    {
        super ();
        setHRadius ( hRadius );
        setVRadius ( vRadius );
        setIterations ( iterations );
    }

    public void setHRadius ( final int hRadius )
    {
        this.hRadius = hRadius;
    }

    public int getHRadius ()
    {
        return hRadius;
    }

    public void setVRadius ( final int vRadius )
    {
        this.vRadius = vRadius;
    }

    public int getVRadius ()
    {
        return vRadius;
    }

    public void setRadius ( final int radius )
    {
        this.hRadius = this.vRadius = radius;
    }

    public int getRadius ()
    {
        return hRadius;
    }

    public void setIterations ( final int iterations )
    {
        this.iterations = iterations;
    }

    public int getIterations ()
    {
        return iterations;
    }

    @Override
    public BufferedImage filter ( final BufferedImage src, BufferedImage dst )
    {
        final int width = src.getWidth ();
        final int height = src.getHeight ();

        if ( dst == null )
        {
            dst = createCompatibleDestImage ( src, null );
        }

        final int[] inPixels = new int[ width * height ];
        final int[] outPixels = new int[ width * height ];
        getRGB ( src, 0, 0, width, height, inPixels );

        for ( int i = 0; i < iterations; i++ )
        {
            blur ( inPixels, outPixels, width, height, hRadius );
            blur ( outPixels, inPixels, height, width, vRadius );
        }

        setRGB ( dst, 0, 0, width, height, inPixels );
        return dst;
    }

    public static void blur ( final int[] in, final int[] out, final int width, final int height, final int radius )
    {
        final int widthMinus1 = width - 1;
        final int tableSize = 2 * radius + 1;
        final int[] divide = new int[ 256 * tableSize ];

        for ( int i = 0; i < 256 * tableSize; i++ )
        {
            divide[ i ] = i / tableSize;
        }

        int inIndex = 0;

        for ( int y = 0; y < height; y++ )
        {
            int outIndex = y;
            int ta = 0, tr = 0, tg = 0, tb = 0;

            for ( int i = -radius; i <= radius; i++ )
            {
                final int rgb = in[ inIndex + ImageMath.clamp ( i, 0, width - 1 ) ];
                ta += ( rgb >> 24 ) & 0xff;
                tr += ( rgb >> 16 ) & 0xff;
                tg += ( rgb >> 8 ) & 0xff;
                tb += rgb & 0xff;
            }

            for ( int x = 0; x < width; x++ )
            {
                out[ outIndex ] = ( divide[ ta ] << 24 ) | ( divide[ tr ] << 16 ) | ( divide[ tg ] << 8 ) |
                        divide[ tb ];

                int i1 = x + radius + 1;
                if ( i1 > widthMinus1 )
                {
                    i1 = widthMinus1;
                }
                int i2 = x - radius;
                if ( i2 < 0 )
                {
                    i2 = 0;
                }
                final int rgb1 = in[ inIndex + i1 ];
                final int rgb2 = in[ inIndex + i2 ];

                ta += ( ( rgb1 >> 24 ) & 0xff ) - ( ( rgb2 >> 24 ) & 0xff );
                tr += ( ( rgb1 & 0xff0000 ) - ( rgb2 & 0xff0000 ) ) >> 16;
                tg += ( ( rgb1 & 0xff00 ) - ( rgb2 & 0xff00 ) ) >> 8;
                tb += ( rgb1 & 0xff ) - ( rgb2 & 0xff );
                outIndex += height;
            }
            inIndex += width;
        }
    }
}