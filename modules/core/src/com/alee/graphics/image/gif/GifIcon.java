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

package com.alee.graphics.image.gif;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Mikle Garin
 */
public class GifIcon implements Icon
{
    private RepaintListener repaintListener;

    private final Map<Integer, GifDecoder.GifFrame> frames;
    private int status = GifDecoder.STATUS_OK;

    private int frameCount = 0;
    private int displayedFrame = -1;
    private Thread gifAnimator;

    private boolean stopAnimation = false;

    public GifIcon ( final Class nearClass, final String imgSrc ) throws IOException
    {
        this ( new BufferedInputStream ( nearClass.getResource ( imgSrc ).openStream () ) );
    }

    public GifIcon ( final String imgSrc ) throws FileNotFoundException
    {
        this ( new BufferedInputStream ( new FileInputStream ( imgSrc ) ) );
    }

    public GifIcon ( final BufferedInputStream stream )
    {
        super ();

        frames = new HashMap<Integer, GifDecoder.GifFrame> ();

        final GifDecoder gifDecoder = new GifDecoder ();
        status = gifDecoder.read ( stream );

        if ( status == GifDecoder.STATUS_OK )
        {
            frameCount = gifDecoder.getFrameCount ();
            displayedFrame = 0;
            for ( int i = 0; i < frameCount; i++ )
            {
                frames.put ( i, new GifDecoder.GifFrame ( gifDecoder.getFrame ( i ), gifDecoder.getDelay ( i ) ) );
            }
            startAnimation ();
        }
    }

    public void startAnimation ()
    {
        if ( frameCount > 1 )
        {
            displayedFrame = 0;
            gifAnimator = new Thread ( new Runnable ()
            {
                @Override
                public void run ()
                {
                    while ( !stopAnimation )
                    {
                        final int delay = frames.get ( displayedFrame ).delay;
                        try
                        {
                            Thread.sleep ( delay == 0 ? 100 : delay );
                        }
                        catch ( final InterruptedException e )
                        {
                            //
                        }

                        // Refresh displayed frame
                        if ( displayedFrame == frameCount - 1 )
                        {
                            displayedFrame = 0;
                        }
                        else
                        {
                            displayedFrame++;
                        }

                        if ( repaintListener != null )
                        {
                            repaintListener.imageRepaintOccurred ();
                        }
                    }
                }
            } );
            gifAnimator.setDaemon ( true );
            gifAnimator.start ();
        }
    }

    public void stopAnimation ()
    {
        if ( gifAnimator.isAlive () )
        {
            stopAnimation = true;
        }
    }

    @Override
    public void paintIcon ( final Component c, final Graphics g, final int x, final int y )
    {
        if ( frameCount > 0 )
        {
            g.drawImage ( frames.get ( displayedFrame ).bufferedImage, x, y, c );
        }
    }

    @Override
    public int getIconWidth ()
    {
        return frames.get ( displayedFrame ).bufferedImage.getWidth ();
    }

    @Override
    public int getIconHeight ()
    {
        return frames.get ( displayedFrame ).bufferedImage.getHeight ();
    }

    public BufferedImage getImage ()
    {
        return frames.get ( displayedFrame ).bufferedImage;
    }

    public int getStatus ()
    {
        return status;
    }

    public int getFrameCount ()
    {
        return frameCount;
    }

    public int getDisplayedFrame ()
    {
        return displayedFrame;
    }

    public Map<Integer, GifDecoder.GifFrame> getFrames ()
    {
        return frames;
    }

    public RepaintListener getRepaintListener ()
    {
        return repaintListener;
    }

    public void setRepaintListener ( final RepaintListener repaintListener )
    {
        this.repaintListener = repaintListener;
    }
}