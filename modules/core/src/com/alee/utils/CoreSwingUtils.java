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
import java.applet.Applet;
import java.awt.*;
import java.lang.reflect.InvocationTargetException;

/**
 * @author Mikle Garin
 */

@SuppressWarnings ( "NonFinalUtilityClass" )
public class CoreSwingUtils
{
    /**
     * Returns window ancestor for specified component or {@code null} if it doesn't exist.
     *
     * @param component component to process
     * @return window ancestor for specified component or {@code null} if it doesn't exist
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
     * Returns root pane for the specified component or {@code null} if it doesn't exist.
     *
     * @param component component to look under
     * @return root pane for the specified component or {@code null} if it doesn't exist
     */
    public static JRootPane getRootPane ( final Component component )
    {
        if ( component == null )
        {
            return null;
        }
        else if ( component instanceof JFrame )
        {
            return ( ( JFrame ) component ).getRootPane ();
        }
        else if ( component instanceof JDialog )
        {
            return ( ( JDialog ) component ).getRootPane ();
        }
        else if ( component instanceof JWindow )
        {
            return ( ( JWindow ) component ).getRootPane ();
        }
        else if ( component instanceof JApplet )
        {
            return ( ( JApplet ) component ).getRootPane ();
        }
        else if ( component instanceof JRootPane )
        {
            return ( JRootPane ) component;
        }
        else
        {
            return getRootPane ( component.getParent () );
        }
    }

    /**
     * Returns component bounds on screen.
     *
     * @param component component to process
     * @return component bounds on screen
     */
    public static Rectangle getBoundsOnScreen ( final Component component )
    {
        final Point los = locationOnScreen ( component );
        final Dimension size = component.getSize ();
        return new Rectangle ( los, size );
    }

    /**
     * Returns component bounds inside its window.
     * This will return component bounds relative to window root pane location, not the window location.
     *
     * @param component component to process
     * @return component bounds inside its window
     */
    public static Rectangle getBoundsInWindow ( final Component component )
    {
        return component instanceof Window || component instanceof JApplet ? getRootPane ( component ).getBounds () :
                getRelativeBounds ( component, getRootPane ( component ) );
    }

    /**
     * Returns component bounds relative to another component.
     *
     * @param component  component to process
     * @param relativeTo component relative to which bounds will be returned
     * @return component bounds relative to another component
     */
    public static Rectangle getRelativeBounds ( final Component component, final Component relativeTo )
    {
        return new Rectangle ( getRelativeLocation ( component, relativeTo ), component.getSize () );
    }

    /**
     * Returns component location relative to another component.
     *
     * @param component  component to process
     * @param relativeTo component relative to which location will be returned
     * @return component location relative to another component
     */
    public static Point getRelativeLocation ( final Component component, final Component relativeTo )
    {
        final Point los = locationOnScreen ( component );
        final Point rt = locationOnScreen ( relativeTo );
        return new Point ( los.x - rt.x, los.y - rt.y );
    }

    /**
     * Returns whether or not specified {@code ancestor} is an ancestor of {@code component}.
     *
     * @param ancestor  ancestor component
     * @param component child component
     * @return {@code true} if specified {@code ancestor} is an ancestor of {@code component}, {@code false} otherwise
     */
    public static boolean isAncestorOf ( final Component ancestor, final Component component )
    {
        return ancestor instanceof Container && ( ( Container ) ancestor ).isAncestorOf ( component );
    }

    /**
     * Returns whether specified components have the same ancestor or not.
     *
     * @param component1 first component
     * @param component2 second component
     * @return true if specified components have the same ancestor, false otherwise
     */
    public static boolean isSameAncestor ( final Component component1, final Component component2 )
    {
        return getWindowAncestor ( component1 ) == getWindowAncestor ( component2 );
    }

    /**
     * Returns window decoration insets.
     *
     * @param window window to retrieve insets for
     * @return window decoration insets
     */
    public static Insets getWindowDecorationInsets ( final Window window )
    {
        final Insets insets = new Insets ( 0, 0, 0, 0 );
        if ( window instanceof Dialog || window instanceof Frame )
        {
            final JRootPane rootPane = CoreSwingUtils.getRootPane ( window );
            if ( rootPane != null )
            {
                if ( window.isShowing () )
                {
                    if ( window instanceof Dialog && !( ( Dialog ) window ).isUndecorated () ||
                            window instanceof Frame && !( ( Frame ) window ).isUndecorated () )
                    {
                        // Calculating exact decoration insets based on root pane insets
                        final Rectangle wlos = CoreSwingUtils.getBoundsOnScreen ( window );
                        final Rectangle rlos = CoreSwingUtils.getBoundsOnScreen ( rootPane );
                        insets.top = rlos.y - wlos.y;
                        insets.left = rlos.x - wlos.x;
                        insets.bottom = wlos.y + wlos.height - rlos.y - rlos.height;
                        insets.right = wlos.x + wlos.width - rlos.x - rlos.width;
                    }
                    else
                    {
                        // Fallback for custom window decorations
                        // Usually 25px should be around average decoration header width
                        insets.top = 25;
                    }
                }
                else
                {
                    // Fallback for non-displayed window
                    // Usually 25px should be around average decoration header width
                    insets.top = 25;
                }
            }
        }
        return insets;
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
     * Returns pointer information.
     * todo Add some sort of caching to enhance performance?
     * todo For example keep it for 10 ms in case there will be multiple incoming requests
     *
     * @return pointer information
     */
    public static PointerInfo getPointerInfo ()
    {
        return MouseInfo.getPointerInfo ();
    }

    /**
     * Returns mouse location on the screen.
     *
     * @return mouse location on the screen
     */
    public static Point getMouseLocation ()
    {
        return getPointerInfo ().getLocation ();
    }

    /**
     * Returns mouse location on the screen relative to specified component.
     *
     * @param component component
     * @return mouse location on the screen relative to specified component
     */
    public static Point getMouseLocation ( final Component component )
    {
        final Point mouse = getMouseLocation ();
        final Point los = locationOnScreen ( component );
        return new Point ( mouse.x - los.x, mouse.y - los.y );
    }

    /**
     * Returns whether or not specified {@link Component} is currently hovered.
     *
     * @param component {@link Component}
     * @return {@code true} if specified {@link Component} is currently hovered, {@code false} otherwise
     */
    public static boolean isHovered ( final Component component )
    {
        boolean hover = false;

        // Ensure that component is showing
        if ( component.isShowing () )
        {
            // Ensure component have non-zero visible width and height
            final Rectangle vr = computeVisibleRect ( component, new Rectangle () );
            if ( vr.width > 0 && vr.height > 0 )
            {
                // Ensure that mouse is hovering the component right now
                if ( getBoundsOnScreen ( component ).contains ( getMouseLocation () ) )
                {
                    hover = true;
                }
            }
        }

        return hover;
    }

    /**
     * Returns intersection of the visible rectangles for the component and all of its ancestors.
     * Return value is also stored in {@code visibleRect}.
     *
     * @param component   {@link Component}
     * @param visibleRect {@code Rectangle} containing intersection of the visible rectangles for the component and all of its ancestors
     * @return intersection of the visible rectangles for the component and all of its ancestors
     */
    private static Rectangle computeVisibleRect ( final Component component, final Rectangle visibleRect )
    {
        final Container p = component.getParent ();
        final Rectangle bounds = component.getBounds ();
        if ( p == null || p instanceof Window || p instanceof Applet )
        {
            visibleRect.setBounds ( 0, 0, bounds.width, bounds.height );
        }
        else
        {
            computeVisibleRect ( p, visibleRect );
            visibleRect.x -= bounds.x;
            visibleRect.y -= bounds.y;
            SwingUtilities.computeIntersection ( 0, 0, bounds.width, bounds.height, visibleRect );
        }
        return visibleRect;
    }

    /**
     * Returns {@link Component} location on screen.
     * Throws a detailed exception in case {@link Component} was not showing.
     *
     * @param component {@link Component} to determine locatin on screen for
     * @return {@link Component} location on screen
     */
    public static Point locationOnScreen ( final Component component )
    {
        try
        {
            // Trying to acquire component location on screen
            return component.getLocationOnScreen ();
        }
        catch ( final IllegalComponentStateException e )
        {
            // Re-throwing exception with more information on the component
            throw new RuntimeException ( "Component must be showing on the screen to determine its location: " + component );
        }
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
     * @param runnable {@link Runnable}
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
     * It will catch any exceptions thrown by {@link #invokeAndWait(Runnable)} method and wrap it within {@link RuntimeException}.
     *
     * @param runnable {@link Runnable}
     */
    public static void invokeAndWaitSafely ( final Runnable runnable )
    {
        try
        {
            invokeAndWait ( runnable );
        }
        catch ( final Exception e )
        {
            throw new RuntimeException ( e );
        }
    }
}