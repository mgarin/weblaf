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
import java.awt.image.BufferedImage;

/**
 * A filter which produces motion blur the faster, but lower-quality way.
 */

public class MotionBlurFilter extends AbstractBufferedImageOp
{
    private float alignX;
    private float alignY;
    private float distance;
    private float angle;
    private float rotation;
    private float zoom;

    public MotionBlurFilter ()
    {
        this ( 0f, 0f, 0f, 0f );
    }

    public MotionBlurFilter ( final float distance, final float angle, final float rotation, final float zoom )
    {
        this ( distance, angle, rotation, zoom, 0.5f, 0.5f );
    }

    public MotionBlurFilter ( final float distance, final float angle, final float rotation, final float zoom, final float alignX,
                              final float alignY )
    {
        this.distance = distance;
        this.angle = angle;
        this.rotation = rotation;
        this.zoom = zoom;
        this.alignX = alignX;
        this.alignY = alignY;
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

    public void setRotation ( final float rotation )
    {
        this.rotation = rotation;
    }

    public float getRotation ()
    {
        return rotation;
    }

    public void setZoom ( final float zoom )
    {
        this.zoom = zoom;
    }

    public float getZoom ()
    {
        return zoom;
    }

    public void setAlignX ( final float alignX )
    {
        this.alignX = alignX;
    }

    public float getAlignX ()
    {
        return alignX;
    }

    public void setAlignY ( final float alignY )
    {
        this.alignY = alignY;
    }

    public float getAlignY ()
    {
        return alignY;
    }

    public void setAlign ( final Point2D align )
    {
        this.alignX = ( float ) align.getX ();
        this.alignY = ( float ) align.getY ();
    }

    public Point2D getAlign ()
    {
        return new Point2D.Float ( alignX, alignY );
    }

    private int log2 ( final int n )
    {
        int m = 1;
        int log2n = 0;

        while ( m < n )
        {
            m *= 2;
            log2n++;
        }
        return log2n;
    }

    @Override
    public BufferedImage filter ( final BufferedImage src, BufferedImage dst )
    {
        if ( dst == null )
        {
            dst = createCompatibleDestImage ( src, null );
        }
        BufferedImage tSrc = src;
        final float cx = ( float ) src.getWidth () * alignX;
        final float cy = ( float ) src.getHeight () * alignY;
        final float imageRadius = ( float ) Math.sqrt ( cx * cx + cy * cy );
        float translateX = ( float ) ( distance * Math.cos ( angle ) );
        float translateY = ( float ) ( distance * -Math.sin ( angle ) );
        float scale = zoom;
        float rotate = rotation;
        final float maxDistance = distance + Math.abs ( rotation * imageRadius ) + zoom * imageRadius;
        final int steps = log2 ( ( int ) maxDistance );

        translateX /= maxDistance;
        translateY /= maxDistance;
        scale /= maxDistance;
        rotate /= maxDistance;

        if ( steps == 0 )
        {
            final Graphics2D g = dst.createGraphics ();
            g.drawRenderedImage ( src, null );
            g.dispose ();
            return dst;
        }

        BufferedImage tmp = createCompatibleDestImage ( src, null );
        for ( int i = 0; i < steps; i++ )
        {
            final Graphics2D g = tmp.createGraphics ();
            g.drawImage ( tSrc, null, null );
            g.setRenderingHint ( RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON );
            g.setRenderingHint ( RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR );
            g.setComposite ( AlphaComposite.getInstance ( AlphaComposite.SRC_OVER, 0.5f ) );

            g.translate ( cx + translateX, cy + translateY );
            g.scale ( 1.0001 + scale,
                    1.0001 + scale );  // The .0001 works round a bug on Windows where drawImage throws an ArrayIndexOutOfBoundException
            if ( rotation != 0 )
            {
                g.rotate ( rotate );
            }
            g.translate ( -cx, -cy );

            g.drawImage ( dst, null, null );
            g.dispose ();
            final BufferedImage ti = dst;
            dst = tmp;
            tmp = ti;
            tSrc = dst;

            translateX *= 2;
            translateY *= 2;
            scale *= 2;
            rotate *= 2;
        }
        return dst;
    }
}
