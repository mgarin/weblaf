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

import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;

/**
 * An abstract superclass for point filters.
 * The interface is the same as the old RGBImageFilter.
 *
 * @author Jerry Huxtable
 */

public abstract class PointFilter extends AbstractBufferedImageOp
{
    protected boolean canFilterIndexColorModel = false;

    @Override
    public BufferedImage filter ( final BufferedImage src, BufferedImage dst )
    {
        final int width = src.getWidth ();
        final int height = src.getHeight ();
        final int type = src.getType ();
        final WritableRaster srcRaster = src.getRaster ();

        if ( dst == null )
        {
            dst = createCompatibleDestImage ( src, null );
        }
        final WritableRaster dstRaster = dst.getRaster ();

        setDimensions ( width, height );

        final int[] inPixels = new int[ width ];
        for ( int y = 0; y < height; y++ )
        {
            // We try to avoid calling getRGB on images as it causes them to become un-managed, causing horrible performance problems.
            if ( type == BufferedImage.TYPE_INT_ARGB )
            {
                srcRaster.getDataElements ( 0, y, width, 1, inPixels );
                for ( int x = 0; x < width; x++ )
                {
                    inPixels[ x ] = filterRGB ( x, y, inPixels[ x ] );
                }
                dstRaster.setDataElements ( 0, y, width, 1, inPixels );
            }
            else
            {
                src.getRGB ( 0, y, width, 1, inPixels, 0, width );
                for ( int x = 0; x < width; x++ )
                {
                    inPixels[ x ] = filterRGB ( x, y, inPixels[ x ] );
                }
                dst.setRGB ( 0, y, width, 1, inPixels, 0, width );
            }
        }

        return dst;
    }

    public void setDimensions ( final int width, final int height )
    {
    }

    public abstract int filterRGB ( int x, int y, int rgb );
}