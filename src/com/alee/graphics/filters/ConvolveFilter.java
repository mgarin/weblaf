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

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.Kernel;

/**
 * A filter which applies a convolution kernel to an image.
 *
 * @author Jerry Huxtable
 */

public class ConvolveFilter extends AbstractBufferedImageOp
{
    public static int ZERO_EDGES = 0;
    public static int CLAMP_EDGES = 1;
    public static int WRAP_EDGES = 2;

    protected Kernel kernel = null;
    public boolean alpha = true;
    private int edgeAction = CLAMP_EDGES;

    /**
     * Construct a filter with a null kernel. This is only useful if you're going to change the kernel later on.
     */
    public ConvolveFilter ()
    {
        this ( new float[ 9 ] );
    }

    /**
     * Construct a filter with the given 3x3 kernel.
     *
     * @param matrix an array of 9 floats containing the kernel
     */
    public ConvolveFilter ( float[] matrix )
    {
        this ( new Kernel ( 3, 3, matrix ) );
    }

    /**
     * Construct a filter with the given kernel.
     *
     * @param rows   the number of rows in the kernel
     * @param cols   the number of columns in the kernel
     * @param matrix an array of rows*cols floats containing the kernel
     */
    public ConvolveFilter ( int rows, int cols, float[] matrix )
    {
        this ( new Kernel ( cols, rows, matrix ) );
    }

    /**
     * Construct a filter with the given 3x3 kernel.
     */
    public ConvolveFilter ( Kernel kernel )
    {
        this.kernel = kernel;
    }

    public void setKernel ( Kernel kernel )
    {
        this.kernel = kernel;
    }

    public Kernel getKernel ()
    {
        return kernel;
    }

    public void setEdgeAction ( int edgeAction )
    {
        this.edgeAction = edgeAction;
    }

    public int getEdgeAction ()
    {
        return edgeAction;
    }

    @Override
    public BufferedImage filter ( BufferedImage src, BufferedImage dst )
    {
        int width = src.getWidth ();
        int height = src.getHeight ();

        if ( dst == null )
        {
            dst = createCompatibleDestImage ( src, null );
        }

        int[] inPixels = new int[ width * height ];
        int[] outPixels = new int[ width * height ];
        getRGB ( src, 0, 0, width, height, inPixels );

        convolve ( kernel, inPixels, outPixels, width, height, alpha, edgeAction );

        setRGB ( dst, 0, 0, width, height, outPixels );
        return dst;
    }

    @Override
    public BufferedImage createCompatibleDestImage ( BufferedImage src, ColorModel dstCM )
    {
        if ( dstCM == null )
        {
            dstCM = src.getColorModel ();
        }
        return new BufferedImage ( dstCM, dstCM.createCompatibleWritableRaster ( src.getWidth (), src.getHeight () ),
                dstCM.isAlphaPremultiplied (), null );
    }

    @Override
    public Rectangle2D getBounds2D ( BufferedImage src )
    {
        return new Rectangle ( 0, 0, src.getWidth (), src.getHeight () );
    }

    @Override
    public Point2D getPoint2D ( Point2D srcPt, Point2D dstPt )
    {
        if ( dstPt == null )
        {
            dstPt = new Point2D.Double ();
        }
        dstPt.setLocation ( srcPt.getX (), srcPt.getY () );
        return dstPt;
    }

    @Override
    public RenderingHints getRenderingHints ()
    {
        return null;
    }

    public static void convolve ( Kernel kernel, int[] inPixels, int[] outPixels, int width, int height, int edgeAction )
    {
        convolve ( kernel, inPixels, outPixels, width, height, true, edgeAction );
    }

    public static void convolve ( Kernel kernel, int[] inPixels, int[] outPixels, int width, int height, boolean alpha, int edgeAction )
    {
        if ( kernel.getHeight () == 1 )
        {
            convolveH ( kernel, inPixels, outPixels, width, height, alpha, edgeAction );
        }
        else if ( kernel.getWidth () == 1 )
        {
            convolveV ( kernel, inPixels, outPixels, width, height, alpha, edgeAction );
        }
        else
        {
            convolveHV ( kernel, inPixels, outPixels, width, height, alpha, edgeAction );
        }
    }

    /**
     * Convolve with a 2D kernel
     */
    public static void convolveHV ( Kernel kernel, int[] inPixels, int[] outPixels, int width, int height, boolean alpha, int edgeAction )
    {
        int index = 0;
        float[] matrix = kernel.getKernelData ( null );
        int rows = kernel.getHeight ();
        int cols = kernel.getWidth ();
        int rows2 = rows / 2;
        int cols2 = cols / 2;

        for ( int y = 0; y < height; y++ )
        {
            for ( int x = 0; x < width; x++ )
            {
                float r = 0, g = 0, b = 0, a = 0;

                for ( int row = -rows2; row <= rows2; row++ )
                {
                    int iy = y + row;
                    int ioffset;
                    if ( 0 <= iy && iy < height )
                    {
                        ioffset = iy * width;
                    }
                    else if ( edgeAction == CLAMP_EDGES )
                    {
                        ioffset = y * width;
                    }
                    else if ( edgeAction == WRAP_EDGES )
                    {
                        ioffset = ( ( iy + height ) % height ) * width;
                    }
                    else
                    {
                        continue;
                    }
                    int moffset = cols * ( row + rows2 ) + cols2;
                    for ( int col = -cols2; col <= cols2; col++ )
                    {
                        float f = matrix[ moffset + col ];

                        if ( f != 0 )
                        {
                            int ix = x + col;
                            if ( !( 0 <= ix && ix < width ) )
                            {
                                if ( edgeAction == CLAMP_EDGES )
                                {
                                    ix = x;
                                }
                                else if ( edgeAction == WRAP_EDGES )
                                {
                                    ix = ( x + width ) % width;
                                }
                                else
                                {
                                    continue;
                                }
                            }
                            int rgb = inPixels[ ioffset + ix ];
                            a += f * ( ( rgb >> 24 ) & 0xff );
                            r += f * ( ( rgb >> 16 ) & 0xff );
                            g += f * ( ( rgb >> 8 ) & 0xff );
                            b += f * ( rgb & 0xff );
                        }
                    }
                }
                int ia = alpha ? PixelUtils.clamp ( ( int ) ( a + 0.5 ) ) : 0xff;
                int ir = PixelUtils.clamp ( ( int ) ( r + 0.5 ) );
                int ig = PixelUtils.clamp ( ( int ) ( g + 0.5 ) );
                int ib = PixelUtils.clamp ( ( int ) ( b + 0.5 ) );
                outPixels[ index++ ] = ( ia << 24 ) | ( ir << 16 ) | ( ig << 8 ) | ib;
            }
        }
    }

    /**
     * Convolve with a kernel consisting of one row
     */
    public static void convolveH ( Kernel kernel, int[] inPixels, int[] outPixels, int width, int height, boolean alpha, int edgeAction )
    {
        int index = 0;
        float[] matrix = kernel.getKernelData ( null );
        int cols = kernel.getWidth ();
        int cols2 = cols / 2;

        for ( int y = 0; y < height; y++ )
        {
            int ioffset = y * width;
            for ( int x = 0; x < width; x++ )
            {
                float r = 0, g = 0, b = 0, a = 0;
                for ( int col = -cols2; col <= cols2; col++ )
                {
                    float f = matrix[ cols2 + col ];

                    if ( f != 0 )
                    {
                        int ix = x + col;
                        if ( ix < 0 )
                        {
                            if ( edgeAction == CLAMP_EDGES )
                            {
                                ix = 0;
                            }
                            else if ( edgeAction == WRAP_EDGES )
                            {
                                ix = ( x + width ) % width;
                            }
                        }
                        else if ( ix >= width )
                        {
                            if ( edgeAction == CLAMP_EDGES )
                            {
                                ix = width - 1;
                            }
                            else if ( edgeAction == WRAP_EDGES )
                            {
                                ix = ( x + width ) % width;
                            }
                        }
                        int rgb = inPixels[ ioffset + ix ];
                        a += f * ( ( rgb >> 24 ) & 0xff );
                        r += f * ( ( rgb >> 16 ) & 0xff );
                        g += f * ( ( rgb >> 8 ) & 0xff );
                        b += f * ( rgb & 0xff );
                    }
                }
                int ia = alpha ? PixelUtils.clamp ( ( int ) ( a + 0.5 ) ) : 0xff;
                int ir = PixelUtils.clamp ( ( int ) ( r + 0.5 ) );
                int ig = PixelUtils.clamp ( ( int ) ( g + 0.5 ) );
                int ib = PixelUtils.clamp ( ( int ) ( b + 0.5 ) );
                outPixels[ index++ ] = ( ia << 24 ) | ( ir << 16 ) | ( ig << 8 ) | ib;
            }
        }
    }

    /**
     * Convolve with a kernel consisting of one column
     */
    public static void convolveV ( Kernel kernel, int[] inPixels, int[] outPixels, int width, int height, boolean alpha, int edgeAction )
    {
        int index = 0;
        float[] matrix = kernel.getKernelData ( null );
        int rows = kernel.getHeight ();
        int rows2 = rows / 2;

        for ( int y = 0; y < height; y++ )
        {
            for ( int x = 0; x < width; x++ )
            {
                float r = 0, g = 0, b = 0, a = 0;

                for ( int row = -rows2; row <= rows2; row++ )
                {
                    int iy = y + row;
                    int ioffset;
                    if ( iy < 0 )
                    {
                        if ( edgeAction == CLAMP_EDGES )
                        {
                            ioffset = 0;
                        }
                        else if ( edgeAction == WRAP_EDGES )
                        {
                            ioffset = ( ( y + height ) % height ) * width;
                        }
                        else
                        {
                            ioffset = iy * width;
                        }
                    }
                    else if ( iy >= height )
                    {
                        if ( edgeAction == CLAMP_EDGES )
                        {
                            ioffset = ( height - 1 ) * width;
                        }
                        else if ( edgeAction == WRAP_EDGES )
                        {
                            ioffset = ( ( y + height ) % height ) * width;
                        }
                        else
                        {
                            ioffset = iy * width;
                        }
                    }
                    else
                    {
                        ioffset = iy * width;
                    }

                    float f = matrix[ row + rows2 ];

                    if ( f != 0 )
                    {
                        int rgb = inPixels[ ioffset + x ];
                        a += f * ( ( rgb >> 24 ) & 0xff );
                        r += f * ( ( rgb >> 16 ) & 0xff );
                        g += f * ( ( rgb >> 8 ) & 0xff );
                        b += f * ( rgb & 0xff );
                    }
                }
                int ia = alpha ? PixelUtils.clamp ( ( int ) ( a + 0.5 ) ) : 0xff;
                int ir = PixelUtils.clamp ( ( int ) ( r + 0.5 ) );
                int ig = PixelUtils.clamp ( ( int ) ( g + 0.5 ) );
                int ib = PixelUtils.clamp ( ( int ) ( b + 0.5 ) );
                outPixels[ index++ ] = ( ia << 24 ) | ( ir << 16 ) | ( ig << 8 ) | ib;
            }
        }
    }
}

