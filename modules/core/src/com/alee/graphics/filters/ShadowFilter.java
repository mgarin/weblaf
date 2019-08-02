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
import java.awt.geom.AffineTransform;
import java.awt.image.BandCombineOp;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;

/**
 * A filter which draws a drop shadow based on the alpha channel of the image.
 *
 * @author Jerry Huxtable
 */
public class ShadowFilter extends AbstractBufferedImageOp
{
    private float radius = 5;
    private float angle = ( float ) Math.PI * 6 / 4;
    private float distance = 5;
    private float opacity = 0.5f;
    private boolean addMargins = false;
    private boolean shadowOnly = true;
    private int shadowColor = 0xff000000;

    public ShadowFilter ()
    {
        super ();
    }

    public ShadowFilter ( final float radius, final float xOffset, final float yOffset, final float opacity )
    {
        super ();
        this.radius = radius;
        this.angle = ( float ) Math.atan2 ( yOffset, xOffset );
        this.distance = ( float ) Math.sqrt ( xOffset * xOffset + yOffset * yOffset );
        this.opacity = opacity;
    }

    public void setAngle ( final float angle )
    {
        this.angle = angle;
    }

    public float getAngle ()
    {
        return angle;
    }

    public void setDistance ( final float distance )
    {
        this.distance = distance;
    }

    public float getDistance ()
    {
        return distance;
    }

    /**
     * Set the radius of the kernel, and hence the amount of blur. The bigger the radius, the longer this filter will take.
     *
     * @param radius the radius of the blur in pixels.
     */
    public void setRadius ( final float radius )
    {
        this.radius = radius;
    }

    /**
     * Get the radius of the kernel.
     *
     * @return the radius
     */
    public float getRadius ()
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
            final float xOffset = distance * ( float ) Math.cos ( angle );
            final float yOffset = -distance * ( float ) Math.sin ( angle );
            r.width += ( int ) ( Math.abs ( xOffset ) + 2 * radius );
            r.height += ( int ) ( Math.abs ( yOffset ) + 2 * radius );
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

        final float shadowR = ( ( shadowColor >> 16 ) & 0xff ) / 255f;
        final float shadowG = ( ( shadowColor >> 8 ) & 0xff ) / 255f;
        final float shadowB = ( shadowColor & 0xff ) / 255f;

        // Make a black mask from the image's alpha channel
        final float[][] extractAlpha = { { 0, 0, 0, shadowR }, { 0, 0, 0, shadowG }, { 0, 0, 0, shadowB }, { 0, 0, 0, opacity } };
        BufferedImage shadow = new BufferedImage ( width, height, BufferedImage.TYPE_INT_ARGB );
        new BandCombineOp ( extractAlpha, null ).filter ( src.getRaster (), shadow.getRaster () );
        shadow = new GaussianFilter ( radius ).filter ( shadow, null );

        final float xOffset = distance * ( float ) Math.cos ( angle );
        final float yOffset = -distance * ( float ) Math.sin ( angle );

        final Graphics2D g = dst.createGraphics ();
        g.setComposite ( AlphaComposite.getInstance ( AlphaComposite.SRC_OVER, opacity ) );
        if ( addMargins )
        {
            final float topShadow = Math.max ( 0, radius - yOffset );
            final float leftShadow = Math.max ( 0, radius - xOffset );
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