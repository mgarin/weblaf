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

/**
 * @author Jerry Huxtable
 */

public class RaysFilter extends MotionBlurFilter
{
    private float opacity = 1.0f;
    private float threshold = 0.0f;
    private float strength = 0.5f;
    private boolean raysOnly = false;
    private Colormap colormap;

    public RaysFilter ()
    {
    }

    public void setOpacity ( final float opacity )
    {
        this.opacity = opacity;
    }

    public float getOpacity ()
    {
        return opacity;
    }

    public void setThreshold ( final float threshold )
    {
        this.threshold = threshold;
    }

    public float getThreshold ()
    {
        return threshold;
    }

    public void setStrength ( final float strength )
    {
        this.strength = strength;
    }

    public float getStrength ()
    {
        return strength;
    }

    public void setRaysOnly ( final boolean raysOnly )
    {
        this.raysOnly = raysOnly;
    }

    public boolean getRaysOnly ()
    {
        return raysOnly;
    }

    public void setColormap ( final Colormap colormap )
    {
        this.colormap = colormap;
    }

    public Colormap getColormap ()
    {
        return colormap;
    }

    @Override
    public BufferedImage filter ( final BufferedImage src, BufferedImage dst )
    {
        final int width = src.getWidth ();
        final int height = src.getHeight ();
        final int[] pixels = new int[ width ];
        final int[] srcPixels = new int[ width ];

        BufferedImage rays = new BufferedImage ( width, height, BufferedImage.TYPE_INT_ARGB );

        final int threshold3 = ( int ) ( threshold * 3 * 255 );
        for ( int y = 0; y < height; y++ )
        {
            getRGB ( src, 0, y, width, 1, pixels );
            for ( int x = 0; x < width; x++ )
            {
                final int rgb = pixels[ x ];
                final int a = rgb & 0xff000000;
                final int r = ( rgb >> 16 ) & 0xff;
                final int g = ( rgb >> 8 ) & 0xff;
                final int b = rgb & 0xff;
                int l = r + g + b;
                if ( l < threshold3 )
                {
                    pixels[ x ] = 0xff000000;
                }
                else
                {
                    l /= 3;
                    pixels[ x ] = a | ( l << 16 ) | ( l << 8 ) | l;
                }
            }
            setRGB ( rays, 0, y, width, 1, pixels );
        }

        rays = super.filter ( rays, null );

        for ( int y = 0; y < height; y++ )
        {
            getRGB ( rays, 0, y, width, 1, pixels );
            getRGB ( src, 0, y, width, 1, srcPixels );
            for ( int x = 0; x < width; x++ )
            {
                int rgb = pixels[ x ];
                final int a = rgb & 0xff000000;
                int r = ( rgb >> 16 ) & 0xff;
                int g = ( rgb >> 8 ) & 0xff;
                int b = rgb & 0xff;

                if ( colormap != null )
                {
                    final int l = r + g + b;
                    rgb = colormap.getColor ( l * strength * ( 1 / 3f ) );
                }
                else
                {
                    r = PixelUtils.clamp ( ( int ) ( r * strength ) );
                    g = PixelUtils.clamp ( ( int ) ( g * strength ) );
                    b = PixelUtils.clamp ( ( int ) ( b * strength ) );
                    rgb = a | ( r << 16 ) | ( g << 8 ) | b;
                }

                pixels[ x ] = rgb;
            }
            setRGB ( rays, 0, y, width, 1, pixels );
        }

        if ( dst == null )
        {
            dst = createCompatibleDestImage ( src, null );
        }

        final Graphics2D g = dst.createGraphics ();
        if ( !raysOnly )
        {
            g.setComposite ( AlphaComposite.SrcOver );
            g.drawRenderedImage ( src, null );
        }
        g.setComposite ( MiscComposite.getInstance ( MiscComposite.ADD, opacity ) );
        g.drawRenderedImage ( rays, null );
        g.dispose ();

        return dst;
    }
}