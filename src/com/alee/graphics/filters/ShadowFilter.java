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
import java.awt.geom.AffineTransform;
import java.awt.image.BandCombineOp;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;

/**
 * A filter which draws a drop shadow based on the alpha channel of the image.
 */

public class ShadowFilter extends AbstractBufferedImageOp
{
    private int radius = 5;
    private int xOffset = 5;
    private int yOffset = 5;
    private float opacity = 0.5f;
    private boolean addMargins = false;
    private boolean shadowOnly = true;
    private int shadowColor = 0xff000000;

    public ShadowFilter ()
    {
    }

    public ShadowFilter ( final int radius, final int xOffset, final int yOffset, final float opacity )
    {
        this.radius = radius;
        this.xOffset = xOffset;
        this.yOffset = yOffset;
        this.opacity = opacity;
    }

    public void setXOffset ( final int xOffset )
    {
        this.xOffset = xOffset;
    }

    public int getXOffset ()
    {
        return xOffset;
    }

    public void setYOffset ( final int yOffset )
    {
        this.yOffset = yOffset;
    }

    public int getYOffset ()
    {
        return yOffset;
    }

    /**
     * Set the radius of the kernel, and hence the amount of blur. The bigger the radius, the longer this filter will take.
     *
     * @param radius the radius of the blur in pixels.
     */
    public void setRadius ( final int radius )
    {
        this.radius = radius;
    }

    /**
     * Get the radius of the kernel.
     *
     * @return the radius
     */
    public int getRadius ()
    {
        return radius;
    }

    public void setOpacity ( final float opacity )
    {
        this.opacity = opacity;
    }

    public float getOpacity ()
    {
        return opacity;
    }

    public void setShadowColor ( final int shadowColor )
    {
        this.shadowColor = shadowColor;
    }

    public int getShadowColor ()
    {
        return shadowColor;
    }

    public void setAddMargins ( final boolean addMargins )
    {
        this.addMargins = addMargins;
    }

    public boolean getAddMargins ()
    {
        return addMargins;
    }

    public void setShadowOnly ( final boolean shadowOnly )
    {
        this.shadowOnly = shadowOnly;
    }

    public boolean getShadowOnly ()
    {
        return shadowOnly;
    }

    protected void transformSpace ( final Rectangle r )
    {
        if ( addMargins )
        {
            r.width += Math.abs ( xOffset ) + 2 * radius;
            r.height += Math.abs ( yOffset ) + 2 * radius;
        }
    }

    @Override
    public BufferedImage filter ( final BufferedImage src, BufferedImage dst )
    {
        final int width = src.getWidth ();
        final int height = src.getHeight ();

        if ( dst == null )
        {
            if ( addMargins )
            {
                final ColorModel cm = src.getColorModel ();
                dst = new BufferedImage ( cm, cm.createCompatibleWritableRaster ( src.getWidth (), src.getHeight () ),
                        cm.isAlphaPremultiplied (), null );
            }
            else
            {
                dst = createCompatibleDestImage ( src, null );
            }
        }

        // Make a black mask from the image's alpha channel
        final float[][] extractAlpha = { { 0, 0, 0, 0 }, { 0, 0, 0, 0 }, { 0, 0, 0, 0 }, { 0, 0, 0, opacity } };
        BufferedImage shadow = new BufferedImage ( width, height, BufferedImage.TYPE_INT_ARGB );
        new BandCombineOp ( extractAlpha, null ).filter ( src.getRaster (), shadow.getRaster () );
        shadow = new GaussianFilter ( radius ).filter ( shadow, null );

        final Graphics2D g = dst.createGraphics ();
        g.setComposite ( AlphaComposite.getInstance ( AlphaComposite.SRC_OVER, opacity ) );
        if ( addMargins )
        {
            final int topShadow = Math.max ( 0, radius - yOffset );
            final int leftShadow = Math.max ( 0, radius - xOffset );
            g.translate ( topShadow, leftShadow );
        }
        g.drawRenderedImage ( shadow, AffineTransform.getTranslateInstance ( xOffset, yOffset ) );
        if ( !shadowOnly )
        {
            g.setComposite ( AlphaComposite.SrcOver );
            g.drawRenderedImage ( src, null );
        }
        g.dispose ();

        return dst;
    }
}

