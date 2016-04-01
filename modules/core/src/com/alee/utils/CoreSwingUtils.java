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

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.InvocationTargetException;

/**
 * @author Mikle Garin
 */

public class CoreSwingUtils
{
    /**
     * Returns window ancestor for specified component or null if it doesn't exist.
     *
     * @param component component to process
     * @return window ancestor for specified component or null if it doesn't exist
     */
    public static Window getWindowAncestor ( final Component component )
    {
        if ( component == null )
        {
            return null;
        }
        if ( component instanceof Window )
        {
            return ( Window ) component;
        }
        for ( Container p = component.getParent (); p != null; p = p.getParent () )
        {
            if ( p instanceof Window )
            {
                return ( Window ) p;
            }
        }
        return null;
    }

    /**
     * Returns whether or not specified component is placed on a fullscreen window.
     *
     * @param component component to process
     * @return true if specified component is placed on a fullscreen window, false otherwise
     */
    public static boolean isFullScreen ( final Component component )
    {
        final Window window = getWindowAncestor ( component );
        if ( window != null )
        {
            final GraphicsConfiguration gc = window.getGraphicsConfiguration ();
            if ( gc != null )
            {
                final GraphicsDevice device = gc.getDevice ();
                return device != null && device.getFullScreenWindow () == window;
            }
        }
        return false;
    }

    /**
     * Returns mouse point relative to specified component.
     *
     * @param component component to process
     * @return mouse point relative to specified component
     */
    public static Point getMousePoint ( final Component component )
    {
        final Point p = MouseInfo.getPointerInfo ().getLocation ();
        final Point los = component.getLocationOnScreen ();
        return new Point ( p.x - los.x, p.y - los.y );
    }

    /**
     * Will invoke the specified action later in EDT in case it is called from non-EDT thread.
     * Otherwise action will be performed immediately.
     *
     * @param runnable runnable
     */
    public static void invokeLater ( final Runnable runnable )
    {
        SwingUtilities.invokeLater ( runnable );
    }

    /**
     * Will invoke the specified action in EDT in case it is called from non-EDT thread.
     *
     * @param runnable runnable
     * @throws InterruptedException      if we're interrupted while waiting for the EDT to finish excecuting {@code doRun.run()}
     * @throws InvocationTargetException if an exception is thrown while running {@code doRun}
     */
    public static void invokeAndWait ( final Runnable runnable ) throws InterruptedException, InvocationTargetException
    {
        if ( SwingUtilities.isEventDispatchThread () )
        {
            runnable.run ();
        }
        else
        {
            SwingUtilities.invokeAndWait ( runnable );
        }
    }

    /**
     * Will invoke the specified action in EDT in case it is called from non-EDT thread.
     * It will also block any exceptions thrown by "invokeAndWait" method.
     *
     * @param runnable runnable
     */
    public static void invokeAndWaitSafely ( final Runnable runnable )
    {
        try
        {
            invokeAndWait ( runnable );
        }
        catch ( final Throwable e )
        {
            //
        }
    }
}