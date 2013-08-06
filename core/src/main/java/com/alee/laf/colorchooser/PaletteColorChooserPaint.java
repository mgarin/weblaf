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

package com.alee.laf.colorchooser;

import com.alee.utils.ColorUtils;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.ColorModel;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.util.HashMap;
import java.util.Map;

/**
 * User: mgarin Date: 07.07.2010 Time: 17:30:58
 */

public class PaletteColorChooserPaint implements Paint
{
    private Color cornerColor = Color.RED;
    private boolean webSafe = false;

    private ColorModel model = ColorModel.getRGBdefault ();

    private int x;
    private int y;
    private int width;
    private int height;

    public PaletteColorChooserPaint ( int x, int y, int width, int height, Color cornerColor )
    {
        super ();
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.cornerColor = cornerColor;
    }

    public PaintContext createContext ( ColorModel cm, Rectangle deviceBounds, Rectangle2D userBounds, final AffineTransform xform,
                                        RenderingHints hints )
    {
        return new PaintContext ()
        {
            private Map<Rectangle, WritableRaster> rastersCache = new HashMap<Rectangle, WritableRaster> ();

            public void dispose ()
            {
                rastersCache.clear ();
            }

            public ColorModel getColorModel ()
            {
                return model;
            }

            public Raster getRaster ( int x, int y, int w, int h )
            {
                Rectangle r = new Rectangle ( x, y, w, h );
                if ( rastersCache.containsKey ( r ) )
                {
                    return rastersCache.get ( r );
                }
                else
                {
                    WritableRaster raster = model.createCompatibleWritableRaster ( w, h );

                    x -= Math.round ( xform.getTranslateX () );
                    y -= Math.round ( xform.getTranslateY () );

                    int[] data = new int[ w * h * 4 ];
                    for ( int j = 0; j < h; j++ )
                    {
                        for ( int i = 0; i < w; i++ )
                        {
                            int base = ( j * w + i ) * 4;
                            data[ base ] = getRed ( x + i, y + j );
                            data[ base + 1 ] = getGreen ( x + i, y + j );
                            data[ base + 2 ] = getBlue ( x + i, y + j );
                            data[ base + 3 ] = 255;
                        }
                    }
                    raster.setPixels ( 0, 0, w, h, data );

                    rastersCache.put ( r, raster );
                    return raster;
                }
            }
        };
    }

    public Color getColor ( int xCoord, int yCoord )
    {
        if ( xCoord < x )
        {
            xCoord = x;
        }
        else if ( xCoord > x + 256 )
        {
            xCoord = x + 256;
        }
        if ( yCoord < y )
        {
            yCoord = y;
        }
        else if ( yCoord > y + 256 )
        {
            yCoord = y + 256;
        }
        return new Color ( getRed ( xCoord, yCoord ), getGreen ( xCoord, yCoord ), getBlue ( xCoord, yCoord ) );
    }

    private int getRed ( int xCoord, int yCoord )
    {
        int red = 255 - ( 255 - cornerColor.getRed () ) * ( xCoord - x ) / width;
        red = red - ( red ) * ( yCoord - y ) / height;
        return getWebSafe ( red );
    }

    private int getGreen ( int xCoord, int yCoord )
    {
        int green = 255 - ( 255 - cornerColor.getGreen () ) * ( xCoord - x ) / width;
        green = green - ( green ) * ( yCoord - y ) / height;
        return getWebSafe ( green );
    }

    private int getBlue ( int xCoord, int yCoord )
    {
        int blue = 255 - ( 255 - cornerColor.getBlue () ) * ( xCoord - x ) / width;
        blue = blue - ( blue ) * ( yCoord - y ) / height;
        return getWebSafe ( blue );
    }

    private int getWebSafe ( int color )
    {
        if ( webSafe )
        {
            color = ColorUtils.getWebSafeValue ( color );
        }
        if ( color < 0 )
        {
            color = 0;
        }
        else if ( color > 255 )
        {
            color = 255;
        }
        return color;
    }

    public int getTransparency ()
    {
        return OPAQUE;
    }

    public Color getCornerColor ()
    {
        return cornerColor;
    }

    public boolean isWebSafe ()
    {
        return webSafe;
    }

    public void setWebSafe ( boolean webSafe )
    {
        this.webSafe = webSafe;
    }
}