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

import com.alee.global.StyleConstants;
import com.alee.utils.swing.WebTimer;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

/**
 * This class provides a set of utilities to modify application windows.
 *
 * @author Mikle Garin
 */

public final class WindowUtils
{
    /**
     * Window pack timers map.
     */
    private static final Map<Window, WebTimer> windowPackTimers = new HashMap<Window, WebTimer> ();

    /**
     * Returns whether window is opaque or not.
     *
     * @param window window to process
     * @param <W>    window type
     * @return whether window background is opaque or not
     */
    public static <W extends Window> boolean isWindowOpaque ( final W window )
    {
        return ProprietaryUtils.isWindowOpaque ( window );
    }

    /**
     * Sets window opaque if that option is supported by the underlying system.
     *
     * @param window window to modify
     * @param opaque whether should make window opaque or not
     * @param <W>    window type
     * @return processed window
     */
    public static <W extends Window> W setWindowOpaque ( final W window, final boolean opaque )
    {
        ProprietaryUtils.setWindowOpaque ( window, opaque );
        return window;
    }

    /**
     * Returns window opacity.
     *
     * @param window window to process
     * @param <W>    window type
     * @return window opacity
     */
    public static <W extends Window> float getWindowOpacity ( final W window )
    {
        return ProprietaryUtils.getWindowOpacity ( window );
    }

    /**
     * Sets window opacity if that option is supported by the underlying system.
     *
     * @param window  window to modify
     * @param opacity new window opacity
     * @param <W>     window type
     * @return processed window
     */
    public static <W extends Window> W setWindowOpacity ( final W window, final float opacity )
    {
        ProprietaryUtils.setWindowOpacity ( window, opacity );
        return window;
    }

    /**
     * Returns component this window is attached to.
     *
     * @param window attached window
     * @param <W>    window type
     * @return component this window is attached to
     */
    public static <W extends Window> Component getAttachedTo ( final W window )
    {
        return null;
    }

    /**
     * Attached window to the specified component.
     *
     * @param window    window to attach
     * @param component component to attach window to
     * @param <W>       window type
     * @return attached window
     */
    public static <W extends Window> W setAttachedTo ( final W window, final Component component )
    {
        return null;
    }

    /**
     * Detaches window from the specified component.
     *
     * @param component component to detach window from
     * @param <W>       window type
     * @return detached window
     */
    public static <W extends Window> W detachFrom ( final Component component )
    {
        return null;
    }

    /**
     * Centers window relative to screen center.
     *
     * @param window window to process
     * @param <W>    window type
     * @return processed window
     */
    public static <W extends Window> W center ( final W window )
    {
        window.setLocationRelativeTo ( null );
        return window;
    }

    /**
     * Centers window relative to the specified component center.
     *
     * @param window     window to process
     * @param relativeTo component used to center window
     * @param <W>        window type
     * @return processed window
     */
    public static <W extends Window> W center ( final W window, final Component relativeTo )
    {
        window.setLocationRelativeTo ( relativeTo );
        return window;
    }

    /**
     * Changes window size and centers it relative to screen center.
     *
     * @param window window to process
     * @param width  new window width
     * @param height new window height
     * @param <W>    window type
     * @return processed window
     */
    public static <W extends Window> W center ( final W window, final int width, final int height )
    {
        window.setSize ( width, height );
        window.setLocationRelativeTo ( null );
        return window;
    }

    /**
     * Changes window size and centers it relative to screen center.
     *
     * @param window     window to process
     * @param relativeTo component used to center window
     * @param width      new window width
     * @param height     new window height
     * @param <W>        window type
     * @return processed window
     */
    public static <W extends Window> W center ( final W window, final Component relativeTo, final int width, final int height )
    {
        window.setSize ( width, height );
        window.setLocationRelativeTo ( relativeTo );
        return window;
    }

    /**
     * Packs window to its preffered height and specified width.
     *
     * @param window window to process
     * @param width  new window width
     * @param <W>    window type
     * @return processed window
     */
    public static <W extends Window> W packToWidth ( final W window, final int width )
    {
        window.setSize ( width, window.getPreferredSize ().height );
        return window;
    }

    /**
     * Packs window to its preffered width and specified height.
     *
     * @param window window to process
     * @param height new window height
     * @param <W>    window type
     * @return processed window
     */
    public static <W extends Window> W packToHeight ( final W window, final int height )
    {
        window.setSize ( window.getPreferredSize ().width, height );
        return window;
    }

    /**
     * Packs and centers specified window relative to old position.
     * Bounds transition might also be animated, depending on StyleConstants.animate variable.
     *
     * @param window window to process
     * @param <W>    window type
     * @return processed window
     * @see StyleConstants#animate
     */
    public static <W extends Window> W packAndCenter ( final W window )
    {
        return packAndCenter ( window, false );
    }

    /**
     * Packs and centers specified window relative to old position.
     * Bounds transition will be animated if requested.
     *
     * @param window  window to process
     * @param animate whether should animate window size changes or not
     * @param <W>     window type
     * @return processed window
     */
    public static <W extends Window> W packAndCenter ( final W window, final boolean animate )
    {
        if ( window == null )
        {
            return window;
        }

        final Rectangle b = window.getBounds ();
        final Dimension s = window.getPreferredSize ();
        final Rectangle newBounds = new Rectangle ( b.x + b.width / 2 - s.width / 2, b.y + b.height / 2 - s.height / 2, s.width, s.height );

        if ( windowPackTimers.containsKey ( window ) )
        {
            if ( windowPackTimers.get ( window ) != null && windowPackTimers.get ( window ).isRunning () )
            {
                windowPackTimers.get ( window ).stop ();
            }
        }

        if ( window.isShowing () && animate )
        {
            final int time = 100;
            final int steps = 10;
            final int xDiff = newBounds.width - b.width;
            final int yDiff = newBounds.height - b.height;
            final WebTimer t = new WebTimer ( time / steps, new ActionListener ()
            {
                private int step = 1;

                @Override
                public void actionPerformed ( final ActionEvent e )
                {
                    if ( step <= steps )
                    {
                        final int w = b.width + xDiff * step / steps;
                        final int h = b.height + yDiff * step / steps;
                        final Rectangle changed = new Rectangle ( b.x + b.width / 2 - w / 2, b.y + b.height / 2 - h / 2, w, h );
                        window.setBounds ( changed );
                    }
                    else
                    {
                        windowPackTimers.get ( window ).stop ();
                    }
                    step++;
                }
            } );
            windowPackTimers.put ( window, t );
            t.start ();
        }
        else
        {
            window.setBounds ( newBounds );
        }
        return window;
    }
}