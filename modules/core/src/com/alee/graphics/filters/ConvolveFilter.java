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
    public ConvolveFilter ( final float[] matrix )
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
    public ConvolveFilter ( final int rows, final int cols, final float[] matrix )
    {
        this ( new Kernel ( cols, rows, matrix ) );
    }

    /**
     * Construct a filter with the given 3x3 kernel.
     */
    public ConvolveFilter ( final Kernel kernel )
    {
        this.kernel = kernel;
    }

    public void setKernel ( final Kernel kernel )
    {
        this.kernel = kernel;
    }

    public Kernel getKernel ()
    {
        return kernel;
    }

    public void setEdgeAction ( final int edgeAction )
    {
        this.edgeAction = edgeAction;
    }

    public int getEdgeAction ()
    {
        return edgeAction;
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

        convolve ( kernel, inPixels, outPixels, width, height, alpha, edgeAction );

        setRGB ( dst, 0, 0, width, height, outPixels );
        return dst;
    }

    @Override
    public BufferedImage createCompatibleDestImage ( final BufferedImage src, ColorModel dstCM )
    {
        if ( dstCM == null )
        {
            dstCM = src.getColorModel ();
        }
        return new BufferedImage ( dstCM, dstCM.createCompatibleWritableRaster ( src.getWidth (), src.getHeight () ),
                dstCM.isAlphaPremultiplied (), null );
    }

    @Override
    public Rectangle2D getBounds2D ( final BufferedImage src )
    {
        return new Rectangle ( 0, 0, src.getWidth (), src.getHeight () );
    }

    @Override
    public Point2D getPoint2D ( final Point2D srcPt, Point2D dstPt )
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

    public static void convolve ( final Kernel kernel, final int[] inPixels, final int[] outPixels, final int width, final int height, final int edgeAction )
    {
        convolve ( kernel, inPixels, outPixels, width, height, true, edgeAction );
    }

    public static void convolve ( final Kernel kernel, final int[] inPixels, final int[] outPixels, final int width, final int height, final boolean alpha, final int edgeAction )
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
    public static void convolveHV ( final Kernel kernel, final int[] inPixels, final int[] outPixels, final int width, final int height, final boolean alpha, final int edgeAction )
    {
        int index = 0;
        final float[] matrix = kernel.getKernelData ( null );
        final int rows = kernel.getHeight ();
        final int cols = kernel.getWidth ();
        final int rows2 = rows / 2;
        final int cols2 = cols / 2;

        for ( int y = 0; y < height; y++ )
        {
            for ( int x = 0; x < width; x++ )
            {
                float r = 0, g = 0, b = 0, a = 0;

                for ( int row = -rows2; row <= rows2; row++ )
                {
                    final int iy = y + row;
                    final int iOffset;
                    if ( 0 <= iy && iy < height )
                    {
                        iOffset = iy * width;
                    }
                    else if ( edgeAction == CLAMP_EDGES )
                    {
                        iOffset = y * width;
                    }
                    else if ( edgeAction == WRAP_EDGES )
                    {
                        iOffset = ( ( iy + height ) % height ) * width;
                    }
                    else
                    {
                        continue;
                    }
                    final int mOffset = cols * ( row + rows2 ) + cols2;
                    for ( int col = -cols2; col <= cols2; col++ )
                    {
                        final float f = matrix[ mOffset + col ];

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
                            final int rgb = inPixels[ iOffset + ix ];
                            a += f * ( ( rgb >> 24 ) & 0xff );
                            r += f * ( ( rgb >> 16 ) & 0xff );
                            g += f * ( ( rgb >> 8 ) & 0xff );
                            b += f * ( rgb & 0xff );
                        }
                    }
                }
                final int ia = alpha ? PixelUtils.clamp ( ( int ) ( a + 0.5 ) ) : 0xff;
                final int ir = PixelUtils.clamp ( ( int ) ( r + 0.5 ) );
                final int ig = PixelUtils.clamp ( ( int ) ( g + 0.5 ) );
                final int ib = PixelUtils.clamp ( ( int ) ( b + 0.5 ) );
                outPixels[ index++ ] = ( ia << 24 ) | ( ir << 16 ) | ( ig << 8 ) | ib;
            }
        }
    }

    /**
     * Convolve with a kernel consisting of one row
     */
    public static void convolveH ( final Kernel kernel, final int[] inPixels, final int[] outPixels, final int width, final int height, final boolean alpha, final int edgeAction )
    {
        int index = 0;
        final float[] matrix = kernel.getKernelData ( null );
        final int cols = kernel.getWidth ();
        final int cols2 = cols / 2;

        for ( int y = 0; y < height; y++ )
        {
            final int iOffset = y * width;
            for ( int x = 0; x < width; x++ )
            {
                float r = 0, g = 0, b = 0, a = 0;
                for ( int col = -cols2; col <= cols2; col++ )
                {
                    final float f = matrix[ cols2 + col ];

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
                        final int rgb = inPixels[ iOffset + ix ];
                        a += f * ( ( rgb >> 24 ) & 0xff );
                        r += f * ( ( rgb >> 16 ) & 0xff );
                        g += f * ( ( rgb >> 8 ) & 0xff );
                        b += f * ( rgb & 0xff );
                    }
                }
                final int ia = alpha ? PixelUtils.clamp ( ( int ) ( a + 0.5 ) ) : 0xff;
                final int ir = PixelUtils.clamp ( ( int ) ( r + 0.5 ) );
                final int ig = PixelUtils.clamp ( ( int ) ( g + 0.5 ) );
                final int ib = PixelUtils.clamp ( ( int ) ( b + 0.5 ) );
                outPixels[ index++ ] = ( ia << 24 ) | ( ir << 16 ) | ( ig << 8 ) | ib;
            }
        }
    }

    /**
     * Convolve with a kernel consisting of one column
     */
    public static void convolveV ( final Kernel kernel, final int[] inPixels, final int[] outPixels, final int width, final int height, final boolean alpha, final int edgeAction )
    {
        int index = 0;
        final float[] matrix = kernel.getKernelData ( null );
        final int rows = kernel.getHeight ();
        final int rows2 = rows / 2;

        for ( int y = 0; y < height; y++ )
        {
            for ( int x = 0; x < width; x++ )
            {
                float r = 0, g = 0, b = 0, a = 0;

                for ( int row = -rows2; row <= rows2; row++ )
                {
                    final int iy = y + row;
                    final int iOffset;
                    if ( iy < 0 )
                    {
                        if ( edgeAction == CLAMP_EDGES )
                        {
                            iOffset = 0;
                        }
                        else if ( edgeAction == WRAP_EDGES )
                        {
                            iOffset = ( ( y + height ) % height ) * width;
                        }
                        else
                        {
                            iOffset = iy * width;
                        }
                    }
                    else if ( iy >= height )
                    {
                        if ( edgeAction == CLAMP_EDGES )
                        {
                            iOffset = ( height - 1 ) * width;
                        }
                        else if ( edgeAction == WRAP_EDGES )
                        {
                            iOffset = ( ( y + height ) % height ) * width;
                        }
                        else
                        {
                            iOffset = iy * width;
                        }
                    }
                    else
                    {
                        iOffset = iy * width;
                    }

                    final float f = matrix[ row + rows2 ];

                    if ( f != 0 )
                    {
                        final int rgb = inPixels[ iOffset + x ];
                        a += f * ( ( rgb >> 24 ) & 0xff );
                        r += f * ( ( rgb >> 16 ) & 0xff );
                        g += f * ( ( rgb >> 8 ) & 0xff );
                        b += f * ( rgb & 0xff );
                    }
                }
                final int ia = alpha ? PixelUtils.clamp ( ( int ) ( a + 0.5 ) ) : 0xff;
                final int ir = PixelUtils.clamp ( ( int ) ( r + 0.5 ) );
                final int ig = PixelUtils.clamp ( ( int ) ( g + 0.5 ) );
                final int ib = PixelUtils.clamp ( ( int ) ( b + 0.5 ) );
                outPixels[ index++ ] = ( ia << 24 ) | ( ir << 16 ) | ( ig << 8 ) | ib;
            }
        }
    }
}