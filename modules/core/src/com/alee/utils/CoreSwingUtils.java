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

import com.alee.api.annotations.NotNull;
import com.alee.api.annotations.Nullable;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.applet.Applet;
import java.awt.*;
import java.awt.event.InputEvent;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

/**
 * This class provides a set of utilities to work with core Swing components, their settings and events.
 *
 * @author Mikle Garin
 */
public final class CoreSwingUtils
{
    /**
     * Private constructor to avoid instantiation.
     */
    private CoreSwingUtils ()
    {
        throw new UtilityException ( "Utility classes are not meant to be instantiated" );
    }

    /**
     * Enables logging of all uncaught exceptions occured within EDT.
     */
    public static void enableEventQueueLogging ()
    {
        Toolkit.getDefaultToolkit ().getSystemEventQueue ().push ( new EventQueue ()
        {
            @Override
            protected void dispatchEvent ( final AWTEvent event )
            {
                try
                {
                    super.dispatchEvent ( event );
                }
                catch ( final Throwable e )
                {
                    final String msg = "Uncaught EventQueue exception: %s";
                    LoggerFactory.getLogger ( CoreSwingUtils.class ).error ( String.format ( msg, e.toString () ), e );
                }
            }
        } );
    }

    /**
     * Returns window ancestor for specified component or {@code null} if it doesn't exist.
     *
     * @param component component to process
     * @return window ancestor for specified component or {@code null} if it doesn't exist
     */
    @Nullable
    public static Window getWindowAncestor ( @Nullable final Component component )
    {
        final Window window;
        if ( component != null )
        {
            if ( !( component instanceof Window ) )
            {
                Container parent;
                for ( parent = component.getParent (); parent != null; parent = parent.getParent () )
                {
                    if ( parent instanceof Window )
                    {
                        break;
                    }
                }
                window = ( Window ) parent;
            }
            else
            {
                window = ( Window ) component;
            }
        }
        else
        {
            window = null;
        }
        return window;
    }

    /**
     * Returns root pane for the specified component or {@code null} if it doesn't exist.
     *
     * @param component component to look under
     * @return root pane for the specified component or {@code null} if it doesn't exist
     */
    @Nullable
    public static JRootPane getRootPane ( @Nullable final Component component )
    {
        final JRootPane rootPane;
        if ( component == null )
        {
            rootPane = null;
        }
        else if ( component instanceof JFrame )
        {
            rootPane = ( ( JFrame ) component ).getRootPane ();
        }
        else if ( component instanceof JDialog )
        {
            rootPane = ( ( JDialog ) component ).getRootPane ();
        }
        else if ( component instanceof JWindow )
        {
            rootPane = ( ( JWindow ) component ).getRootPane ();
        }
        else if ( component instanceof JApplet )
        {
            rootPane = ( ( JApplet ) component ).getRootPane ();
        }
        else if ( component instanceof JRootPane )
        {
            rootPane = ( JRootPane ) component;
        }
        else
        {
            rootPane = getRootPane ( component.getParent () );
        }
        return rootPane;
    }

    /**
     * Returns whether or not specified {@link Component} is actually visible on screen.
     * Note that this method does not account for any other windows that may overlay current one, that needs to be checked separately.
     *
     * @param component {@link Component} to check visibility on screen for
     * @return {@code true} if specified {@link Component} is actually visible on screen, {@code false} otherwise
     */
    public static boolean isVisibleOnScreen ( @NotNull final Component component )
    {
        final boolean visible;
        if ( component.isShowing () )
        {
            final Rectangle bounds = getBoundsOnScreen ( component, true );
            visible = bounds.width > 0 && bounds.height > 0;
        }
        else
        {
            visible = false;
        }
        return visible;
    }

    /**
     * Returns {@link Component} bounds on screen, either only visible or complete ones.
     * Visible only bounds will only include part of the {@link Component} that is actually visible on the screen.
     * Complete {@link Component} bounds might include parts that are not actually visible on the screen due to layout or scroll pane.
     *
     * @param component   {@link Component} to process
     * @param visibleOnly whether or not only visible {@link Component} part bounds should be returned
     * @return {@link Component} bounds on screen, either only visible or complete ones
     */
    @NotNull
    public static Rectangle getBoundsOnScreen ( @NotNull final Component component, final boolean visibleOnly )
    {
        final Rectangle bounds;
        final Point los = locationOnScreen ( component );
        final Dimension size = component.getSize ();
        if ( visibleOnly )
        {
            final Rectangle visible = getVisibleRect ( component );
            bounds = new Rectangle ( los.x + visible.x, los.y + visible.y, visible.width, visible.height );
        }
        else
        {
            bounds = new Rectangle ( los, size );
        }
        return bounds;
    }

    /**
     * Returns component bounds inside its window.
     * This will return component bounds relative to window root pane location, not the window location.
     *
     * @param component component to process
     * @return component bounds inside its window
     */
    @Nullable
    public static Rectangle getBoundsInWindow ( @NotNull final Component component )
    {
        final Rectangle boundsInWindow;
        final JRootPane rootPane = getRootPane ( component );
        if ( rootPane != null )
        {
            if ( component instanceof Window || component instanceof JApplet )
            {
                boundsInWindow = rootPane.getBounds ();
            }
            else
            {
                boundsInWindow = getRelativeBounds ( component, rootPane );
            }
        }
        else
        {
            boundsInWindow = null;
        }
        return boundsInWindow;

    }

    /**
     * Returns component bounds relative to another component.
     *
     * @param component  component to process
     * @param relativeTo component relative to which bounds will be returned
     * @return component bounds relative to another component
     */
    @NotNull
    public static Rectangle getRelativeBounds ( @NotNull final Component component, @NotNull final Component relativeTo )
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
    @NotNull
    public static Point getRelativeLocation ( @NotNull final Component component, @NotNull final Component relativeTo )
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
    public static boolean isAncestorOf ( @Nullable final Component ancestor, @Nullable final Component component )
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
    public static boolean isSameAncestor ( @Nullable final Component component1, @Nullable final Component component2 )
    {
        return getWindowAncestor ( component1 ) == getWindowAncestor ( component2 );
    }

    /**
     * Returns window decoration insets.
     *
     * @param window window to retrieve insets for
     * @return window decoration insets
     */
    @NotNull
    public static Insets getWindowDecorationInsets ( @Nullable final Window window )
    {
        final Insets insets = new Insets ( 0, 0, 0, 0 );
        if ( window instanceof Dialog || window instanceof Frame )
        {
            final JRootPane rootPane = getRootPane ( window );
            if ( rootPane != null )
            {
                if ( window.isShowing () )
                {
                    if ( window instanceof Dialog && !( ( Dialog ) window ).isUndecorated () ||
                            window instanceof Frame && !( ( Frame ) window ).isUndecorated () )
                    {
                        // Calculating exact decoration insets based on root pane insets
                        final Rectangle wlos = getBoundsOnScreen ( window, false );
                        final Rectangle rlos = getBoundsOnScreen ( rootPane, false );
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
    public static boolean isFullScreen ( @Nullable final Component component )
    {
        final boolean fullScreen;
        final Window window = getWindowAncestor ( component );
        if ( window != null )
        {
            final GraphicsConfiguration gc = window.getGraphicsConfiguration ();
            if ( gc != null )
            {
                final GraphicsDevice device = gc.getDevice ();
                fullScreen = device != null && device.getFullScreenWindow () == window;
            }
            else
            {
                fullScreen = false;
            }
        }
        else
        {
            fullScreen = false;
        }
        return fullScreen;
    }

    /**
     * Returns pointer information.
     * todo Add some sort of caching to enhance performance?
     * todo For example keep it for 10 ms in case there will be multiple incoming requests
     *
     * @return pointer information
     */
    @NotNull
    public static PointerInfo getPointerInfo ()
    {
        PointerInfo pointerInfo = MouseInfo.getPointerInfo ();

        /**
         * Workaround for some cases when {@link MouseInfo} returns a {@code null} {@link PointerInfo}.
         * One of the known cases is when it is requested under Windows OS while user is on the lock screen.
         * There are also some other cases, possibly when OS display device configuration is being modified.
         */
        if ( pointerInfo == null )
        {
            try
            {
                /**
                 * Unfortunately {@link PointerInfo} constructor is not accessible so we have to resort to Reflection.
                 * In the worst case if {@link PointerInfo} constructor changes - this will result in {@code null}.
                 */
                pointerInfo = ReflectUtils.createInstance (
                        PointerInfo.class,
                        SystemUtils.getDefaultScreenDevice (),
                        new Point ( 0, 0 )
                );
            }
            catch ( final Exception e )
            {
                throw new RuntimeException ( "Unable to create empty PointerInfo", e );
            }
        }

        return pointerInfo;
    }

    /**
     * Returns mouse location on the screen.
     *
     * @return mouse location on the screen
     */
    @NotNull
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
    @NotNull
    public static Point getMouseLocation ( @NotNull final Component component )
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
    public static boolean isHovered ( @NotNull final Component component )
    {
        boolean hover = false;

        // Ensure that component is showing
        if ( component.isShowing () )
        {
            // Ensure component have non-zero visible width and height
            if ( !getVisibleRect ( component ).isEmpty () )
            {
                // Ensure that mouse is hovering the component right now
                if ( getBoundsOnScreen ( component, true ).contains ( getMouseLocation () ) )
                {
                    hover = true;
                }
            }
        }

        return hover;
    }

    /**
     * Returns top component inside the specified container component at the specified point.
     *
     * @param component container component to process
     * @param point     point on the component
     * @return top component inside the specified container component at the specified point
     */
    @Nullable
    public static Component getTopComponentAt ( @Nullable final Component component, @NotNull final Point point )
    {
        return getTopComponentAt ( component, point.x, point.y );
    }

    /**
     * Returns top component inside the specified container component at the specified point.
     *
     * @param component container component to process
     * @param x         X coordinate on the component
     * @param y         Y coordinate on the component
     * @return top component inside the specified container component at the specified point
     */
    @Nullable
    public static Component getTopComponentAt ( @Nullable final Component component, final int x, final int y )
    {
        final Component top;
        if ( component != null && component.isVisible () && component.contains ( x, y ) )
        {
            if ( component instanceof Container )
            {
                Component topInChild = null;
                final Container container = ( Container ) component;
                for ( int i = 0; i < container.getComponentCount (); i++ )
                {
                    final Component child = container.getComponent ( i );
                    topInChild = getTopComponentAt ( child, x - child.getX (), y - child.getY () );
                    if ( topInChild != null )
                    {
                        break;
                    }
                }
                if ( topInChild != null )
                {
                    top = topInChild;
                }
                else
                {
                    top = component;
                }
            }
            else
            {
                top = component;
            }
        }
        else
        {
            top = null;
        }
        return top;
    }

    /**
     * Returns {@link Component} location on screen.
     * Throws a detailed exception in case {@link Component} was not showing.
     *
     * @param component {@link Component} to determine locatin on screen for
     * @return {@link Component} location on screen
     */
    @NotNull
    public static Point locationOnScreen ( @NotNull final Component component )
    {
        try
        {
            // Trying to acquire component location on screen
            return component.getLocationOnScreen ();
        }
        catch ( final IllegalComponentStateException e )
        {
            // Re-throwing exception with more information on the component
            throw new UtilityException ( "Component must be showing on the screen to determine its location: " + component );
        }
    }

    /**
     * Returns {@link List} of displayed {@link JPopupMenu}s.
     * Multiple {@link JPopupMenu}s can be displayed in case when submenus are visible.
     *
     * @return {@link List} of displayed {@link JPopupMenu}s
     */
    @NotNull
    public static List<JPopupMenu> getPopupMenus ()
    {
        final MenuElement[] selected = MenuSelectionManager.defaultManager ().getSelectedPath ();
        final List<JPopupMenu> menus = new ArrayList<JPopupMenu> ( selected.length );
        for ( final MenuElement element : selected )
        {
            if ( element instanceof JPopupMenu )
            {
                menus.add ( ( JPopupMenu ) element );
            }
        }
        return menus;
    }

    /**
     * Returns whether or not specified {@link InputEvent} contains a menu shortcut key.
     *
     * @param event {@link InputEvent}
     * @return {@code true} if specified {@link InputEvent} contains a menu shortcut key, {@code false} otherwise
     */
    public static boolean isMenuShortcutKeyDown ( @NotNull final InputEvent event )
    {
        return ( event.getModifiers () & Toolkit.getDefaultToolkit ().getMenuShortcutKeyMask () ) != 0;
    }

    /**
     * Returns whether or not current thread is an AWT event dispatching thread.
     *
     * @return {@code true} if the current thread is an AWT event dispatching thread, {@code false} otherwise
     */
    public static boolean isEventDispatchThread ()
    {
        return SwingUtilities.isEventDispatchThread ();
    }

    /**
     * Will invoke specified {@link Runnable} later in EDT.
     * It is highly recommended to perform all UI-related operations within EDT by invoking them through this method.
     *
     * @param runnable {@link Runnable} to execute
     */
    public static void invokeLater ( @NotNull final Runnable runnable )
    {
        SwingUtilities.invokeLater ( runnable );
    }

    /**
     * Will invoke the specified {@link Runnable} in EDT in case it is called from non-EDT thread.
     * It will catch any exceptions thrown by {@link SwingUtilities#invokeAndWait(Runnable)} and wrap them in {@link UtilityException}.
     * It is generally recommended to use {@link #invokeLater(Runnable)} instead whenever it is possible to avoid stalling EDT.
     *
     * @param runnable {@link Runnable} to execute
     */
    public static void invokeAndWait ( @NotNull final Runnable runnable )
    {
        invokeAndWait ( runnable, false );
    }

    /**
     * Will invoke the specified {@link Runnable} in EDT in case it is called from non-EDT thread.
     * It will catch any exceptions thrown by {@link SwingUtilities#invokeAndWait(Runnable)} and wrap them in {@link UtilityException}.
     * It is generally recommended to use {@link #invokeLater(Runnable)} instead whenever it is possible to avoid stalling EDT.
     *
     * @param runnable         {@link Runnable} to execute
     * @param ignoreInterrupts whether or not {@link InterruptedException}s should be ignored
     */
    public static void invokeAndWait ( @NotNull final Runnable runnable, final boolean ignoreInterrupts )
    {
        try
        {
            if ( SwingUtilities.isEventDispatchThread () )
            {
                // This is reasonable since we can't invoke and wait if we are already on EDT
                runnable.run ();
            }
            else
            {
                // If we aren't on EDT we should queue runnable execution
                SwingUtilities.invokeAndWait ( runnable );
            }
        }
        catch ( final InvocationTargetException e )
        {
            // Re-throw wrapped exception
            throw new UtilityException ( e );
        }
        catch ( final InterruptedException e )
        {
            if ( !ignoreInterrupts )
            {
                // Re-throw wrapped exception
                throw new UtilityException ( e );
            }
        }
    }

    /**
     * Returns intersection of the visible rectangles for the component and all of its ancestors.
     *
     * @param component {@link Component}
     * @return intersection of the visible rectangles for the component and all of its ancestors
     */
    @NotNull
    public static Rectangle getVisibleRect ( @NotNull final Component component )
    {
        return component instanceof JComponent ?
                ( ( JComponent ) component ).getVisibleRect () :
                computeVisibleRect ( component, new Rectangle () );
    }

    /**
     * Returns intersection of the visible rectangles for the component and all of its ancestors.
     * Return value is also stored in {@code visibleRect}.
     * This is a copy of private {@code JComponent#computeVisibleRect(Component, Rectangle)} method.
     *
     * @param component   {@link Component}
     * @param visibleRect {@code Rectangle} containing intersection of the visible rectangles for the component and all of its ancestors
     * @return intersection of the visible rectangles for the component and all of its ancestors
     */
    @NotNull
    private static Rectangle computeVisibleRect ( @NotNull final Component component, @NotNull final Rectangle visibleRect )
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
}