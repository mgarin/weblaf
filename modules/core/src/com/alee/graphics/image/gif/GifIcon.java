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

import com.alee.api.annotations.NotNull;
import com.alee.api.annotations.Nullable;
import com.alee.api.resource.Resource;
import com.alee.api.ui.DisabledCopySupplier;
import com.alee.api.ui.TransparentCopySupplier;
import com.alee.utils.CollectionUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Simple GIF {@link Icon} implementation.
 *
 * @author Mikle Garin
 * @see GifDecoder
 * @see GifEncoder
 */
public class GifIcon implements Icon, DisabledCopySupplier<GifIcon>, TransparentCopySupplier<GifIcon>
{
    /**
     * {@link List} of all {@link GifFrame}s in this {@link GifIcon}.
     */
    @NotNull
    protected final List<GifFrame> frames;

    /**
     * {@link GifIcon} loading status.
     *
     * @see GifDecoder#STATUS_OK
     * @see GifDecoder#STATUS_FORMAT_ERROR
     * @see GifDecoder#STATUS_OPEN_ERROR
     */
    protected final int status;

    /**
     * {@link List} of {@link FrameChangeListener}s.
     */
    @NotNull
    protected List<FrameChangeListener> listeners;

    /**
     * Currently displayed {@link GifFrame} index.
     */
    protected transient int displayedFrame;

    /**
     * Daemon {@link Thread} that handles {@link GifIcon} animation.
     */
    @Nullable
    protected transient Thread gifAnimator;

    /**
     * Constructs new {@link GifIcon}.
     *
     * @param resource GIF {@link Resource}
     */
    public GifIcon ( @NotNull final Resource resource )
    {
        this ( resource.getInputStream () );
    }

    /**
     * Constructs new {@link GifIcon}.
     *
     * @param inputStream GIF {@link InputStream}
     */
    public GifIcon ( @NotNull final InputStream inputStream )
    {
        this.frames = new ArrayList<GifFrame> ();

        final GifDecoder gifDecoder = new GifDecoder ();
        this.status = gifDecoder.read ( inputStream instanceof BufferedInputStream
                ? ( BufferedInputStream ) inputStream
                : new BufferedInputStream ( inputStream ) );

        this.listeners = new ArrayList<FrameChangeListener> ();

        if ( this.status == GifDecoder.STATUS_OK )
        {
            if ( gifDecoder.getFrameCount () > 0 )
            {
                this.displayedFrame = 0;
                for ( int i = 0; i < gifDecoder.getFrameCount (); i++ )
                {
                    frames.add ( new GifFrame ( gifDecoder.getFrame ( i ), gifDecoder.getDelay ( i ) ) );
                }
                startAnimation ();
            }
            else
            {
                throw new RuntimeException ( "At least one GIF frame should be provided" );
            }
        }
        else
        {
            throw new RuntimeException ( "Unable to load GIF image, status: " + this.status );
        }
    }

    /**
     * Constructs new {@link GifIcon}.
     *
     * @param frames {@link GifFrame}s
     */
    public GifIcon ( @NotNull final GifFrame... frames )
    {
        this ( CollectionUtils.asList ( frames ) );
    }

    /**
     * Constructs new {@link GifIcon}.
     *
     * @param frames {@link List} of {@link GifFrame}s
     */
    public GifIcon ( @NotNull final List<GifFrame> frames )
    {
        if ( frames.size () > 0 )
        {
            this.frames = frames;
            this.status = GifDecoder.STATUS_OK;
            this.listeners = new ArrayList<FrameChangeListener> ();
            this.displayedFrame = 0;
            startAnimation ();
        }
        else
        {
            throw new RuntimeException ( "At least one GIF frame should be provided" );
        }
    }

    /**
     * Returns {@link GifIcon} loading status.
     *
     * @return {@link GifIcon} loading status
     * @see GifDecoder#STATUS_OK
     * @see GifDecoder#STATUS_FORMAT_ERROR
     * @see GifDecoder#STATUS_OPEN_ERROR
     */
    public int getStatus ()
    {
        return status;
    }

    /**
     * Returns {@link GifIcon} frame count.
     *
     * @return {@link GifIcon} frame count
     */
    public int getFrameCount ()
    {
        return frames.size ();
    }

    /**
     * Returns copy of the {@link List} of all {@link GifFrame}s in this {@link GifIcon}.
     *
     * @return copy of the {@link List} of all {@link GifFrame}s in this {@link GifIcon}
     */
    @NotNull
    public List<GifFrame> getFrames ()
    {
        return CollectionUtils.copy ( frames );
    }

    /**
     * Returns {@link GifFrame} at the specified index.
     *
     * @param index {@link GifFrame} index
     * @return {@link GifFrame} at the specified index
     */
    @NotNull
    public GifFrame getFrame ( final int index )
    {
        return frames.get ( index );
    }

    /**
     * Returns currently displayed {@link GifFrame} index.
     *
     * @return currently displayed {@link GifFrame} index
     */
    public int getDisplayedFrame ()
    {
        return displayedFrame;
    }

    /**
     * Returns currently displayed {@link BufferedImage}.
     *
     * @return currently displayed {@link BufferedImage}
     */
    @NotNull
    public BufferedImage getDisplayedImage ()
    {
        return frames.get ( displayedFrame ).bufferedImage;
    }

    /**
     * Starts animation {@link Thread}.
     */
    public void startAnimation ()
    {
        if ( getFrameCount () > 1 && gifAnimator == null )
        {
            displayedFrame = 0;
            gifAnimator = new Thread ( new Runnable ()
            {
                @Override
                public void run ()
                {
                    try
                    {
                        while ( true )
                        {
                            final int delay = frames.get ( displayedFrame ).delay;
                            Thread.sleep ( delay == 0 ? 100 : delay );

                            // Refresh displayed frame
                            if ( displayedFrame == getFrameCount () - 1 )
                            {
                                displayedFrame = 0;
                            }
                            else
                            {
                                displayedFrame++;
                            }

                            fireFrameChanged ( displayedFrame );
                        }
                    }
                    catch ( final InterruptedException ignored )
                    {
                        // Animation interrupted
                    }
                }
            } );
            gifAnimator.setDaemon ( true );
            gifAnimator.start ();
        }
    }

    /**
     * Stops animation {@link Thread}.
     */
    public void stopAnimation ()
    {
        if ( gifAnimator != null && gifAnimator.isAlive () )
        {
            gifAnimator.interrupt ();
            try
            {
                gifAnimator.join ();
                gifAnimator = null;
            }
            catch ( final InterruptedException ignored )
            {
                //
            }
        }
    }

    @Override
    public void paintIcon ( final Component c, final Graphics g, final int x, final int y )
    {
        if ( getFrameCount () > 0 )
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

    /**
     * Adds specified {@link FrameChangeListener}.
     *
     * @param listener {@link FrameChangeListener} to add
     */
    public void addFrameChangeListener ( @NotNull final FrameChangeListener listener )
    {
        listeners.add ( listener );
    }

    /**
     * Removes specified {@link FrameChangeListener}.
     *
     * @param listener {@link FrameChangeListener} to remove
     */
    public void removeFrameChangeListener ( @NotNull final FrameChangeListener listener )
    {
        listeners.remove ( listener );
    }

    /**
     * Fires displayed {@link GifFrame} change.
     *
     * @param index new displayed {@link GifFrame} index
     */
    public void fireFrameChanged ( final int index )
    {
        if ( CollectionUtils.notEmpty ( listeners ) )
        {
            final GifFrame frame = getFrame ( index );
            for ( final FrameChangeListener listener : listeners )
            {
                listener.frameChanged ( this, index, frame );
            }
        }
    }

    /**
     * Returns copy of this {@link GifIcon} with only first frame made look disabled.
     *
     * @return copy of this {@link GifIcon} with only first frame made look disabled
     */
    @NotNull
    @Override
    public GifIcon createDisabledCopy ()
    {
        return new GifIcon ( CollectionUtils.asList ( frames.get ( 0 ).createDisabledCopy () ) );
    }

    /**
     * Returns copy of this {@link GifIcon} with only first frame made semi-transparent.
     *
     * @param opacity opacity value, must be between 0 and 1
     * @return copy of this {@link GifIcon} with only first frame made semi-transparent
     */
    @NotNull
    @Override
    public GifIcon createTransparentCopy ( final float opacity )
    {
        return new GifIcon ( CollectionUtils.asList ( frames.get ( 0 ).createTransparentCopy ( opacity ) ) );
    }
}