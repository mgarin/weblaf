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

package com.alee.utils;

import sun.swing.SwingUtilities2;

import javax.swing.*;
import javax.swing.plaf.basic.BasicHTML;
import javax.swing.text.View;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.awt.image.RGBImageFilter;
import java.lang.reflect.Field;

/**
 * Solution for subpixel text antialias on translucent windows.
 *
 * @author Jannis Weis
 */
public class TextPaintUtils
{
    public static <T extends JComponent> void drawStringImpl ( final Graphics g, final T c,
                                                               final View view,
                                                               final String text, final Rectangle textRect,
                                                               final Font font, final FontMetrics fm,
                                                               final int mnemIndex,
                                                               final Color background )
    {
        if ( text != null && !text.equals ( "" ) )
        {
            final int asc = fm.getAscent ();
            final int x = textRect.x;
            final int y = textRect.y;
            final Point textPos = new Point ( x, y );

            Graphics2D drawingGraphics = ( Graphics2D ) g;
            BufferedImage img = null;
            Color bg = background;

            /*
             * If there is a non-opaque parent on Windows no sub-pixel AA is supported.
             * In this case we paint the text to an offscreen image with opaque background and paste
             * it draw it back to the original graphics object.
             *
             * See https://bugs.openjdk.java.net/browse/JDK-8215980?attachmentOrder=desc
             */
            boolean imgGraphics = false;
            Component window = c;
            if ( SystemUtils.isWindows () )
            {
                Component comp = c;
                while ( comp != null )
                {
                    imgGraphics = comp.getBackground ().getAlpha () < 255 && !comp.isOpaque ();
                    if ( imgGraphics )
                    {
                        break;
                    }
                    comp = comp.getParent ();
                }
                if ( imgGraphics )
                {
                    window = comp instanceof Window ? comp : SwingUtilities.getWindowAncestor ( comp );
                }
            }

            if ( imgGraphics )
            {
                textPos.setLocation ( SwingUtilities.convertPoint ( c, textPos, window ) );

                double scaleX = ( ( Graphics2D ) g ).getTransform ().getScaleX ();
                double scaleY = ( ( Graphics2D ) g ).getTransform ().getScaleY ();
                textPos.setLocation ( ( int ) Math.round ( scaleX * textPos.x ),
                        ( int ) Math.round ( scaleX * textPos.y ) );
                img = createCompatibleImage ( ( int ) Math.round ( scaleX * textRect.width ),
                        ( int ) Math.round ( scaleY * textRect.height ) );
                /*
                 * Ensure the background color has sufficient contrast to the foreground.
                 */
                Color fg = g.getColor ();
                double brightness = Math.sqrt ( 0.241 * fg.getRed () * fg.getRed ()
                        + 0.691 * fg.getGreen () * fg.getGreen ()
                        + 0.068 * fg.getBlue () * fg.getBlue () );
                bg = brightness > 127 ? Color.BLACK : Color.WHITE;

                drawingGraphics = ( Graphics2D ) img.getGraphics ();
                drawingGraphics.setColor ( bg );
                drawingGraphics.fillRect ( 0, 0, img.getWidth (), img.getHeight () );
                drawingGraphics.setColor ( g.getColor () );
                textRect.setLocation ( 0, 0 );
                drawingGraphics.setClip ( 0, 0, img.getWidth (), img.getHeight () );
                drawingGraphics.scale ( scaleX, scaleY );
            }
            else
            {
                drawingGraphics.clipRect ( textRect.x, textRect.y, textRect.width, textRect.height );
            }

            drawingGraphics.setFont ( font );

            View v = view != null ? view : getView ( c );
            if ( v != null )
            {
                v.paint ( drawingGraphics, textRect );
            }
            else
            {
                textRect.y += asc;
                if ( mnemIndex >= 0 )
                {
                    SwingUtilities2.drawStringUnderlineCharAt ( c, drawingGraphics, text,
                            mnemIndex, textRect.x, textRect.y );
                }
                else
                {
                    SwingUtilities2.drawString ( c, drawingGraphics, text, textRect.x, textRect.y );
                }
            }
            if ( img != null )
            {
                final int bgRgb = bg.getRGB ();
                final int bgRed = bg.getRed ();
                final int bgBlue = bg.getBlue ();
                final int bgGreen = bg.getGreen ();
                final int fgRed = g.getColor ().getRed ();
                final int fgBlue = g.getColor ().getBlue ();
                final int fgGreen = g.getColor ().getGreen ();
                drawingGraphics.dispose ();
                final BufferedImage destImg = getImage ( ( Graphics2D ) g );
                ImageFilter filter = new RGBImageFilter ()
                {
                    @Override
                    public int filterRGB ( final int xx, final int yy, final int rgb )
                    {
                        if ( rgb == bgRgb )
                        {
                            return 0;
                        }
                        int red = ( rgb & 0xff0000 ) >> 16;
                        int green = ( rgb & 0x00ff00 ) >> 8;
                        int blue = ( rgb & 0x0000ff );
                        /*
                         * Calcualte the alpha for each color channel based on the assumption that the resulting color
                         * was calculated by the formula
                         *     out = alpha * src + (1 - alpha) * dest
                         * Where 'out' is the output value, 'src' the value being painted and 'dest' the value of the at the
                         * current drawing surface.
                         */
                        float aa = ( ( float ) ( red - bgRed ) ) / ( fgRed - bgRed );
                        float ba = ( ( float ) ( blue - bgBlue ) ) / ( fgBlue - bgBlue );
                        float ga = ( ( float ) ( green - bgGreen ) ) / ( fgGreen - bgGreen );

                        if ( destImg != null )
                        {
                            int destRgb = destImg.getRGB ( x + xx, y + yy );
                            int destRed = ( destRgb & 0xff0000 ) >> 16;
                            int destGreen = ( destRgb & 0x00ff00 ) >> 8;
                            int destBlue = ( destRgb & 0x0000ff );

                            int outRed = Math.max ( 0, Math.min ( 255, ( int ) ( ( fgRed * aa ) + ( 1 - aa ) * destRed ) ) );
                            int outBlue = Math.max ( 0, Math.min ( 255, ( int ) ( ( fgBlue * ba ) + ( 1 - ba ) * destBlue ) ) );
                            int outGreen = Math.max ( 0, Math.min ( 255, ( int ) ( ( fgGreen * ga ) + ( 1 - ga ) * destGreen ) ) );

                            return ( 255 << 24 ) | ( outRed << 16 ) | ( outGreen << 8 ) | outBlue;
                        }
                        else
                        {
                            /*
                             * If the current offscreen image isn't a 'BufferedImage' then we can't access the indidual color
                             * channels. We average the individual alpha values to achieve grayscale antialiasing.
                             *
                             * If the offscreen image isn't 'null' (but still not a 'BufferedImage') we could first convert it to a
                             * `BufferedImage`. This isn't done here for performance reasons and the fact that the grayscale antialiasing
                             * still looks a lot better than no antialiasing.
                             */
                            return ( ( int ) ( 255 * ( ( aa + ga + ba ) / 3 ) ) << 24 ) | ( fgRed << 16 ) | ( fgGreen << 8 ) | fgBlue;
                        }
                    }
                };
                Image out = Toolkit.getDefaultToolkit ().createImage ( new FilteredImageSource ( img.getSource (), filter ) );
                g.drawImage ( out, x, y, textRect.width, textRect.height, null );
            }
        }
    }

    private static BufferedImage getImage ( final Graphics2D graphics2D )
    {
        try
        {
            Field surfaceField = graphics2D.getClass ().getField ( "surfaceData" );
            Object surfaceDataValue = surfaceField.get ( graphics2D );
            if ( surfaceDataValue == null )
            {
                return null;
            }

            Field imgField;
            try
            {
                imgField = surfaceDataValue.getClass ().getDeclaredField ( "bufImg" ); // BufImgSurfaceData
            }
            catch ( Exception ignored )
            {
                imgField = surfaceDataValue.getClass ().getField ( "offscreenImage" ); // CGLSurfaceData
            }
            imgField.setAccessible ( true );
            Object img = imgField.get ( surfaceDataValue );
            if ( img instanceof BufferedImage )
            {
                return ( BufferedImage ) img;
            }
        }
        catch ( Exception e )
        {
            e.printStackTrace ();
        }
        return null;
    }

    private static BufferedImage createCompatibleImage ( final int width, final int height )
    {
        return isHeadless () ? new BufferedImage ( width, height, BufferedImage.TYPE_INT_RGB )
                : getGraphicsConfiguration ().createCompatibleImage ( width, height, Transparency.OPAQUE );
    }

    private static boolean isHeadless ()
    {
        return GraphicsEnvironment.isHeadless ();
    }

    private static GraphicsConfiguration getGraphicsConfiguration ()
    {
        return GraphicsEnvironment.getLocalGraphicsEnvironment ().getDefaultScreenDevice ().getDefaultConfiguration ();
    }

    private static View getView ( final JComponent c )
    {
        Object obj = c.getClientProperty ( BasicHTML.propertyKey );
        if ( obj instanceof View )
        {
            return ( View ) obj;
        }
        return null;
    }
}