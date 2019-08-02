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

import com.alee.api.jdk.Objects;
import com.alee.utils.system.JavaVersion;
import com.alee.utils.system.SystemType;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * This class provides a set of utilities to retrieve various operating system (shortly OS) information.
 *
 * @author Mikle Garin
 */
public final class SystemUtils
{
    /**
     * Java version application is running on.
     */
    private static JavaVersion javaVersion;

    /**
     * Cached OS name.
     */
    private static final String osName;

    /**
     * Cached OS type.
     */
    private static final SystemType osType;

    /**
     * Cached OS variables initialization.
     */
    static
    {
        // Retrieving OS name
        osName = AccessController.doPrivileged ( new PrivilegedAction<String> ()
        {
            @Override
            public String run ()
            {
                return System.getProperty ( "os.name" );
            }
        } );

        // Resolving OS type based on name
        final String lc = osName.toLowerCase ( Locale.ROOT );
        if ( lc.contains ( "win" ) )
        {
            osType = SystemType.WINDOWS;
        }
        else if ( lc.contains ( "mac" ) || lc.contains ( "darwin" ) )
        {
            osType = SystemType.MAC;
        }
        else if ( lc.contains ( "nix" ) || lc.contains ( "nux" ) )
        {
            osType = SystemType.UNIX;
        }
        else if ( lc.contains ( "sunos" ) )
        {
            osType = SystemType.SOLARIS;
        }
        else
        {
            osType = SystemType.UNKNOWN;
        }
    }

    /**
     * Transparent cursor.
     */
    private static Cursor transparentCursor;

    /**
     * Private constructor to avoid instantiation.
     */
    private SystemUtils ()
    {
        throw new UtilityException ( "Utility classes are not meant to be instantiated" );
    }

    /**
     * Copies text to system clipboard.
     *
     * @param text text to copy into clipboard
     */
    public static void copyToClipboard ( final String text )
    {
        try
        {
            final Clipboard clipboard = Toolkit.getDefaultToolkit ().getSystemClipboard ();
            clipboard.setContents ( new StringSelection ( text ), null );
        }
        catch ( final Exception e )
        {
            LoggerFactory.getLogger ( SystemUtils.class ).error ( e.toString (), e );
        }
    }

    /**
     * Returns string clipboard content.
     *
     * @return string clipboard content
     */
    public static String getStringFromClipboard ()
    {
        final Transferable t = Toolkit.getDefaultToolkit ().getSystemClipboard ().getContents ( null );
        if ( t != null && t.isDataFlavorSupported ( DataFlavor.stringFlavor ) )
        {
            try
            {
                return ( String ) t.getTransferData ( DataFlavor.stringFlavor );
            }
            catch ( final UnsupportedFlavorException e )
            {
                return null;
            }
            catch ( final IOException e )
            {
                return null;
            }
        }
        return null;
    }

    /**
     * Returns java version.
     *
     * @return java version
     */
    public static String getJavaVersionString ()
    {
        return System.getProperty ( "java.version" );
    }

    /**
     * Returns java version application is running on.
     *
     * @return java version application is running on
     */
    public static JavaVersion getJavaVersion ()
    {
        if ( javaVersion == null )
        {
            javaVersion = new JavaVersion ();
        }
        return javaVersion;
    }

    /**
     * Returns whether application is running on the specified java version and above or not.
     *
     * @param version version number
     * @return {@code true} if the application is running on the specified java version and above, {@code false} otherwise
     */
    public static boolean isJavaVersionOrAbove ( final double version )
    {
        return isJavaVersionOrAbove ( version, 0, 0 );
    }

    /**
     * Returns whether application is running on the specified java version and above or not.
     *
     * @param version version number
     * @param update  update number
     * @return {@code true} if the application is running on the specified java version and above, {@code false} otherwise
     */
    public static boolean isJavaVersionOrAbove ( final double version, final int update )
    {
        return isJavaVersionOrAbove ( version, 0, update );
    }

    /**
     * Returns whether application is running on the specified java version and above or not.
     *
     * @param major  major version
     * @param minor  minor version
     * @param update update number
     * @return {@code true} if the application is running on the specified java version and above, {@code false} otherwise
     */
    public static boolean isJavaVersionOrAbove ( final double major, final int minor, final int update )
    {
        return getJavaVersion ().compareTo ( major, minor, update ) >= 0;
    }

    /**
     * Returns whether application is running on java 6 version and above or not.
     *
     * @return {@code true} if the application is running on java 6 version and above, {@code false} otherwise
     */
    public static boolean isJava6orAbove ()
    {
        return isJavaVersionOrAbove ( 1.6 );
    }

    /**
     * Returns whether application is running on java 7 version and above or not.
     *
     * @return {@code true} if the application is running on java 7 version and above, {@code false} otherwise
     */
    public static boolean isJava7orAbove ()
    {
        return isJavaVersionOrAbove ( 1.7 );
    }

    /**
     * Returns whether application is running on java 8 version and above or not.
     *
     * @return {@code true} if the application is running on java 8 version and above, {@code false} otherwise
     */
    public static boolean isJava8orAbove ()
    {
        return isJavaVersionOrAbove ( 1.8 );
    }

    /**
     * Returns whether application is running on java 9 version and above or not.
     *
     * @return {@code true} if the application is running on java 9 version and above, {@code false} otherwise
     */
    public static boolean isJava9orAbove ()
    {
        return isJavaVersionOrAbove ( 9.0 );
    }

    /**
     * Returns whether application is running on java 10 version and above or not.
     *
     * @return {@code true} if the application is running on java 10 version and above, {@code false} otherwise
     */
    public static boolean isJava10orAbove ()
    {
        return isJavaVersionOrAbove ( 10.0 );
    }

    /**
     * Returns java vm name.
     *
     * @return java vm name
     */
    public static String getJavaName ()
    {
        return System.getProperty ( "java.vm.name" );
    }

    /**
     * Returns java vm vendor.
     *
     * @return java vm vendor
     */
    public static String getJavaVendor ()
    {
        return System.getProperty ( "java.vm.vendor" );
    }

    /**
     * Returns OS icon.
     *
     * @return OS icon
     */
    public static ImageIcon getOsIcon ()
    {
        return getOsIcon ( true );
    }

    /**
     * Returns colored or grayscale OS icon.
     *
     * @param color whether return colored icon or not
     * @return OS icon
     */
    public static ImageIcon getOsIcon ( final boolean color )
    {
        return getOsIcon ( 16, color );
    }

    /**
     * Returns OS icon of specified size if such exists; returns 16x16 icon otherwise.
     *
     * @param size preferred icon size
     * @return OS icon
     */
    public static ImageIcon getOsIcon ( final int size )
    {
        return getOsIcon ( size, true );
    }

    /**
     * Returns colored or grayscale OS icon of specified size if such exists; returns 16x16 icon otherwise.
     *
     * @param size  preferred icon size
     * @param color whether return colored icon or not
     * @return OS icon
     */
    public static ImageIcon getOsIcon ( final int size, final boolean color )
    {
        final ImageIcon icon;
        final String os = getShortOsName ();
        if ( os != null )
        {
            final int iconSize = Objects.equals ( size, 16, 32 ) ? size : 16;
            final String mark = color ? "_colored" : "";
            final String path = "icons/os/" + iconSize + "/" + os + mark + ".png";
            icon = new ImageIcon ( SystemUtils.class.getResource ( path ) );
        }
        else
        {
            icon = null;
        }
        return icon;
    }

    /**
     * Returns OS type.
     *
     * @return OS type
     */
    public static SystemType getOsType ()
    {
        return osType;
    }

    /**
     * Returns whether current OS is windows or not.
     *
     * @return {@code true} if current OS is windows, {@code false} otherwise
     */
    public static boolean isWindows ()
    {
        return osType == SystemType.WINDOWS;
    }

    /**
     * Returns whether current OS is mac or not.
     *
     * @return {@code true} if current OS is mac, {@code false} otherwise
     */
    public static boolean isMac ()
    {
        return osType == SystemType.MAC;
    }

    /**
     * Returns whether current OS is unix or not.
     *
     * @return {@code true} if current OS is unix, {@code false} otherwise
     */
    public static boolean isUnix ()
    {
        return osType == SystemType.UNIX;
    }

    /**
     * Returns whether current OS is solaris or not.
     *
     * @return {@code true} if current OS is solaris, {@code false} otherwise
     */
    public static boolean isSolaris ()
    {
        return osType == SystemType.SOLARIS;
    }

    /**
     * Returns OS architecture.
     *
     * @return OS architecture
     */
    public static String getOsArch ()
    {
        return ManagementFactory.getOperatingSystemMXBean ().getArch ();
    }

    /**
     * Returns OS name.
     *
     * @return OS name
     */
    public static String getOsName ()
    {
        return osName;
    }

    /**
     * Returns short OS name.
     *
     * @return short OS name
     */
    public static String getShortOsName ()
    {
        return osType.shortName ();
    }

    /**
     * Returns OS vendor site address.
     *
     * @return OS vendor site address
     */
    public static String getOsSite ()
    {
        if ( isWindows () )
        {
            return "http://www.microsoft.com/";
        }
        else if ( isMac () )
        {
            return "http://www.apple.com/";
        }
        else if ( isUnix () )
        {
            return "http://www.unix.org/";
        }
        else if ( isSolaris () )
        {
            return "http://www.oracle.com/";
        }
        else
        {
            return null;
        }
    }

    /**
     * Returns OS version.
     *
     * @return OS version
     */
    public static String getOsVersion ()
    {
        return ManagementFactory.getOperatingSystemMXBean ().getVersion ();
    }

    /**
     * Returns the number of processors available to the Java virtual machine.
     *
     * @return number of processors
     */
    public static int getOsProcessors ()
    {
        return ManagementFactory.getOperatingSystemMXBean ().getAvailableProcessors ();
    }

    /**
     * Returns JRE architecture.
     *
     * @return JRE architecture
     */
    public static String getJreArch ()
    {
        return getJreArchName ().contains ( "64" ) ? "64" : "32";
    }

    /**
     * Returns JRE architecture name.
     *
     * @return JRE architecture name
     */
    public static String getJreArchName ()
    {
        return System.getProperty ( "sun.arch.data.model" );
    }

    /**
     * Returns whether Caps Lock is on or not.
     *
     * @return {@code true} if Caps Lock is on, {@code false} otherwise
     */
    public static boolean isCapsLock ()
    {
        return Toolkit.getDefaultToolkit ().getLockingKeyState ( KeyEvent.VK_CAPS_LOCK );
    }

    /**
     * Returns whether Num Lock is on or not.
     *
     * @return {@code true} if Num Lock is on, {@code false} otherwise
     */
    public static boolean isNumLock ()
    {
        return Toolkit.getDefaultToolkit ().getLockingKeyState ( KeyEvent.VK_NUM_LOCK );
    }

    /**
     * Returns whether Scroll Lock is on or not.
     *
     * @return {@code true} if Scroll Lock is on, {@code false} otherwise
     */
    public static boolean isScrollLock ()
    {
        return Toolkit.getDefaultToolkit ().getLockingKeyState ( KeyEvent.VK_SCROLL_LOCK );
    }

    /**
     * Returns default GraphicsEnvironment.
     *
     * @return default GraphicsEnvironment
     */
    public static GraphicsEnvironment getGraphicsEnvironment ()
    {
        return GraphicsEnvironment.getLocalGraphicsEnvironment ();
    }

    /**
     * Returns whether or not a display, keyboard, and mouse can be supported in this environment.
     *
     * @return {@code true} if display, keyboard, and mouse can be supported in this environment, {@code false} otherwise
     */
    public static boolean isHeadlessEnvironment ()
    {
        return GraphicsEnvironment.isHeadless ();
    }

    /**
     * Returns default screen device.
     *
     * @return default screen device
     */
    public static GraphicsDevice getDefaultScreenDevice ()
    {
        return getGraphicsEnvironment ().getDefaultScreenDevice ();
    }

    /**
     * Returns default screen GraphicsConfiguration.
     *
     * @return default screen GraphicsConfiguration
     */
    public static GraphicsConfiguration getGraphicsConfiguration ()
    {
        return getDefaultScreenDevice ().getDefaultConfiguration ();
    }

    /**
     * Returns list of available screen devices.
     *
     * @return list of available screen devices
     */
    public static List<GraphicsDevice> getGraphicsDevices ()
    {
        // Retrieving system devices
        final GraphicsDevice[] screenDevices = getGraphicsEnvironment ().getScreenDevices ();
        final GraphicsDevice defaultScreenDevice = getDefaultScreenDevice ();

        // Collecting devices into list
        final List<GraphicsDevice> devices = new ArrayList<GraphicsDevice> ();
        for ( final GraphicsDevice gd : screenDevices )
        {
            if ( gd.getType () == GraphicsDevice.TYPE_RASTER_SCREEN )
            {
                if ( gd == defaultScreenDevice )
                {
                    // Add default device to list start
                    devices.add ( 0, gd );
                }
                else
                {
                    devices.add ( gd );
                }
            }
        }
        return devices;
    }

    /**
     * Returns screen device for the specified window.
     *
     * @param window window to find screen device for
     * @return screen device for the specified window
     */
    public static GraphicsDevice getGraphicsDevice ( final Window window )
    {
        return window != null ? window.getGraphicsConfiguration ().getDevice () : getDefaultScreenDevice ();
    }

    /**
     * Returns screen device for the specified location.
     *
     * @param location location to find screen device for
     * @return screen device for the specified location
     */
    public static GraphicsDevice getGraphicsDevice ( final Point location )
    {
        for ( final GraphicsDevice device : getGraphicsDevices () )
        {
            if ( device.getDefaultConfiguration ().getBounds ().contains ( location ) )
            {
                return device;
            }
        }
        return getDefaultScreenDevice ();
    }

    /**
     * Returns screen device where most part of specified bounds is placed.
     *
     * @param bounds bounds to find screen device for
     * @return screen device where most part of specified bounds is placed
     */
    public static GraphicsDevice getGraphicsDevice ( final Rectangle bounds )
    {
        // Determining screen on which most part of our bounds is placed
        int maxArea = 0;
        GraphicsDevice device = null;
        for ( final GraphicsDevice gd : getGraphicsDevices () )
        {
            final GraphicsConfiguration gc = gd.getDefaultConfiguration ();
            final Rectangle sb = gc.getBounds ();
            if ( bounds.intersects ( sb ) )
            {
                // This part of code will check intersection between our bounds and screen bounds
                final Rectangle intersection = bounds.intersection ( sb );
                final int s = intersection.width * intersection.height;
                if ( maxArea < s )
                {
                    maxArea = s;
                    device = gd;
                }
            }
        }
        return device != null ? device : getDefaultScreenDevice ();
    }

    /**
     * Returns screen device bounds.
     *
     * @param device            screen device to return bounds for
     * @param applyScreenInsets whether or not should extract screen insets from graphics device bounds
     * @return screen device bounds
     */
    public static Rectangle getDeviceBounds ( final GraphicsDevice device, final boolean applyScreenInsets )
    {
        return getDeviceBounds ( device != null ? device.getDefaultConfiguration () : getGraphicsConfiguration (), applyScreenInsets );
    }

    /**
     * Returns screen device bounds.
     *
     * @param gc                screen device graphics configuration
     * @param applyScreenInsets whether or not should extract screen insets from screen device bounds
     * @return screen device bounds
     */
    public static Rectangle getDeviceBounds ( final GraphicsConfiguration gc, final boolean applyScreenInsets )
    {
        // Ensure we have some configuration
        final GraphicsConfiguration conf = gc != null ? gc : getGraphicsConfiguration ();

        // Graphics bounds
        final Rectangle bounds = conf.getBounds ();

        // Taking screen insets into account
        if ( applyScreenInsets )
        {
            final Insets insets = Toolkit.getDefaultToolkit ().getScreenInsets ( conf );
            bounds.x += insets.left;
            bounds.y += insets.top;
            bounds.width -= insets.left + insets.right;
            bounds.height -= insets.top + insets.bottom;
        }

        return bounds;
    }

    /**
     * Returns screen device bounds for all screen devices available.
     *
     * @param applyScreenInsets whether or not should extract screen insets from screen device bounds
     * @return screen device bounds
     */
    public static List<Rectangle> getDevicesBounds ( final boolean applyScreenInsets )
    {
        final List<GraphicsDevice> devices = getGraphicsDevices ();
        final List<Rectangle> bounds = new ArrayList<Rectangle> ( devices.size () );
        for ( final GraphicsDevice device : devices )
        {
            bounds.add ( getDeviceBounds ( device, applyScreenInsets ) );
        }
        return bounds;
    }

    /**
     * Returns screen bounds for the specified location.
     *
     * @param location          location to find screen bounds for
     * @param applyScreenInsets whether or not should extract screen insets from graphics device bounds
     * @return screen bounds for the specified location
     */
    public static Rectangle getDeviceBounds ( final Point location, final boolean applyScreenInsets )
    {
        final GraphicsDevice device = getGraphicsDevice ( location );
        return getDeviceBounds ( device, applyScreenInsets );
    }

    /**
     * Returns screen bounds within which most part of the specified bounds is placed.
     *
     * @param bounds            bounds to find screen bounds for
     * @param applyScreenInsets whether or not should extract screen insets from graphics device bounds
     * @return screen bounds within which most part of the specified bounds is placed
     */
    public static Rectangle getDeviceBounds ( final Rectangle bounds, final boolean applyScreenInsets )
    {
        final GraphicsDevice device = getGraphicsDevice ( bounds );
        return getDeviceBounds ( device, applyScreenInsets );
    }

    /**
     * Returns screen bounds within which most part of the specified component is placed.
     *
     * @param component         component to find screen bounds for
     * @param applyScreenInsets whether or not should extract screen insets from graphics device bounds
     * @return screen bounds within which most part of the specified component is placed
     */
    public static Rectangle getDeviceBounds ( final Component component, final boolean applyScreenInsets )
    {
        final Rectangle bounds = CoreSwingUtils.getBoundsOnScreen ( component, true );
        return getDeviceBounds ( bounds, applyScreenInsets );
    }

    /**
     * Returns maximized bounds for the screen where specified frame is displayed.
     * Note that we don't need to provide x/y offset of the screen here.
     * It seems that maximized bounds require only bounds inside of the screen bounds, not between the screens overall.
     *
     * @param frame frame to provide maximized bounds for
     * @return maximized bounds for the screen where specified frame is displayed
     */
    public static Rectangle getMaximizedBounds ( final Frame frame )
    {
        final GraphicsConfiguration gc = frame.getGraphicsConfiguration ();
        final Rectangle max = getDeviceBounds ( gc, true );
        final Rectangle b = getDeviceBounds ( gc, false );
        return new Rectangle ( max.x - b.x, max.y - b.y, max.width, max.height );
    }

    /**
     * Returns maximized bounds for the west half of the screen where specified frame is displayed.
     * Note that we don't need to provide x/y offset of the screen here.
     * It seems that maximized bounds require only bounds inside of the screen bounds, not between the screens overall.
     *
     * @param frame frame to provide maximized bounds for
     * @return maximized bounds for the west half of the screen where specified frame is displayed
     */
    public static Rectangle getMaximizedWestBounds ( final Frame frame )
    {
        final Rectangle b = getMaximizedBounds ( frame );
        return new Rectangle ( b.x, b.y, b.width / 2, b.height );
    }

    /**
     * Returns maximized bounds for the east half of the screen where specified frame is displayed.
     * Note that we don't need to provide x/y offset of the screen here.
     * It seems that maximized bounds require only bounds inside of the screen bounds, not between the screens overall.
     *
     * @param frame frame to provide maximized bounds for
     * @return maximized bounds for the east half of the screen where specified frame is displayed
     */
    public static Rectangle getMaximizedEastBounds ( final Frame frame )
    {
        final Rectangle b = getMaximizedBounds ( frame );
        return new Rectangle ( b.x + b.width - b.width / 2, b.y, b.width / 2, b.height );
    }

    /**
     * Returns whether or not specified frame state is supported by the OS.
     *
     * @param state frame state
     * @return {@code true} if the specified frame state is supported by the OS, {@code false} otherwise
     */
    public static boolean isFrameStateSupported ( final int state )
    {
        return Toolkit.getDefaultToolkit ().isFrameStateSupported ( state );
    }

    /**
     * Returns fully transparent cursor.
     *
     * @return fully transparent cursor
     */
    public static Cursor getTransparentCursor ()
    {
        if ( transparentCursor == null )
        {
            final Dimension d = Toolkit.getDefaultToolkit ().getBestCursorSize ( 16, 16 );
            final BufferedImage image = ImageUtils.createCompatibleImage ( d.width, d.height, Transparency.TRANSLUCENT );
            transparentCursor = Toolkit.getDefaultToolkit ().createCustomCursor ( image, new Point ( 0, 0 ), "transparent" );
        }
        return transparentCursor;
    }
}