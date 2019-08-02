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
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;

/**
 * A filter which acts as a superclass for filters which need to have the whole image in memory to do their stuff.
 *
 * @author Jerry Huxtable
 */
public abstract class WholeImageFilter extends AbstractBufferedImageOp
{
    /**
     * The output image bounds.
     */
    protected Rectangle transformedSpace;

    /**
     * The input image bounds.
     */
    protected Rectangle originalSpace;

    /**
     * Construct a WholeImageFilter.
     */
    public WholeImageFilter ()
    {
    }

    @Override
    public BufferedImage filter ( final BufferedImage src, BufferedImage dst )
    {
        final int width = src.getWidth ();
        final int height = src.getHeight ();
        final int type = src.getType ();
        final WritableRaster srcRaster = src.getRaster ();

        originalSpace = new Rectangle ( 0, 0, width, height );
        transformedSpace = new Rectangle ( 0, 0, width, height );
        transformSpace ( transformedSpace );

        if ( dst == null )
        {
            final ColorModel dstCM = src.getColorModel ();
            dst = new BufferedImage ( dstCM, dstCM.createCompatibleWritableRaster ( transformedSpace.width, transformedSpace.height ),
                    dstCM.isAlphaPremultiplied (), null );
        }
        final WritableRaster dstRaster = dst.getRaster ();

        int[] inPixels = getRGB ( src, 0, 0, width, height, null );
        inPixels = filterPixels ( width, height, inPixels, transformedSpace );
        setRGB ( dst, 0, 0, transformedSpace.width, transformedSpace.height, inPixels );

        return dst;
    }

    /**
     * Calculate output bounds for given input bounds.
     *
     * @param rect input and output rectangle
     */
    protected void transformSpace ( final Rectangle rect )
    {
    }

    /**
     * Actually filter the pixels.
     *
     * @param width            the image width
     * @param height           the image height
     * @param inPixels         the image pixels
     * @param transformedSpace the output bounds
     * @return the output pixels
     */
    protected abstract int[] filterPixels ( int width, int height, int[] inPixels, Rectangle transformedSpace );
}