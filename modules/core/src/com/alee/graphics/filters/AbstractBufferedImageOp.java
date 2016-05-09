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
import java.awt.image.BufferedImageOp;
import java.awt.image.ColorModel;

/**
 * A convenience class which implements those methods of BufferedImageOp which are rarely changed.
 */

public abstract class AbstractBufferedImageOp implements BufferedImageOp
{
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

    /**
     * A convenience method for getting ARGB pixels from an image.
     * This tries to avoid the performance penalty of BufferedImage.getRGB managing the image.
     */
    public int[] getRGB ( final BufferedImage image, final int x, final int y, final int width, final int height, final int[] pixels )
    {
        final int type = image.getType ();
        if ( type == BufferedImage.TYPE_INT_ARGB || type == BufferedImage.TYPE_INT_RGB )
        {
            return ( int[] ) image.getRaster ().getDataElements ( x, y, width, height, pixels );
        }
        return image.getRGB ( x, y, width, height, pixels, 0, width );
    }

    /**
     * A convenience method for setting ARGB pixels in an image.
     * This tries to avoid the performance penalty of BufferedImage.setRGB managing the image.
     */
    public void setRGB ( final BufferedImage image, final int x, final int y, final int width, final int height, final int[] pixels )
    {
        final int type = image.getType ();
        if ( type == BufferedImage.TYPE_INT_ARGB || type == BufferedImage.TYPE_INT_RGB )
        {
            image.getRaster ().setDataElements ( x, y, width, height, pixels );
        }
        else
        {
            image.setRGB ( x, y, width, height, pixels, 0, width );
        }
    }
}