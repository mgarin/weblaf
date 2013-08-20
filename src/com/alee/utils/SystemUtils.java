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

import com.alee.utils.system.JavaVersion;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.*;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.List;

/**
 * This class provides a set of utilities to retrieve various operating system information.
 * Futher on operating system called shortly - OS.
 *
 * @author Mikle Garin
 */

public final class SystemUtils
{
    /**
     * Windows short name.
     */
    public static final String WINDOWS = "win";

    /**
     * Mac OS short name.
     */
    public static final String MAC = "mac";

    /**
     * Unix short name.
     */
    public static final String UNIX = "unix";

    /**
     * Solaris short name.
     */
    public static final String SOLARIS = "solaris";

    /**
     * Java version application is running on.
     */
    private static JavaVersion javaVersion;

    /**
     * Copies text to system clipboard.
     *
     * @param text text to copy into clipboard
     */
    public static void copyToClipboard ( String text )
    {
        try
        {
            Clipboard clipboard = Toolkit.getDefaultToolkit ().getSystemClipboard ();
            clipboard.setContents ( new StringSelection ( text ), null );
        }
        catch ( Throwable e )
        {
            e.printStackTrace ();
        }
    }

    /**
     * Returns string clipboard content.
     *
     * @return string clipboard content
     */
    public static String getStringFromClipboard ()
    {
        Transferable t = Toolkit.getDefaultToolkit ().getSystemClipboard ().getContents ( null );
        if ( t != null && t.isDataFlavorSupported ( DataFlavor.stringFlavor ) )
        {
            try
            {
                return ( String ) t.getTransferData ( DataFlavor.stringFlavor );
            }
            catch ( UnsupportedFlavorException e )
            {
                return null;
            }
            catch ( IOException e )
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
            javaVersion = new JavaVersion ( getJavaVersionString () );
        }
        return javaVersion;
    }

    /**
     * Returns whether applicaton is running on the specified java version and above or not.
     *
     * @return true if the application is running on the specified java version and above, false otherwise
     */
    public static boolean isJavaVersion ( double version, int update )
    {
        return getJavaVersion ().compareVersion ( version, 0, update ) >= 0;
    }

    /**
     * Returns whether applicaton is running on the specified java version and above or not.
     *
     * @return true if the application is running on the specified java version and above, false otherwise
     */
    public static boolean isJavaVersion ( double major, int minor, int update )
    {
        return getJavaVersion ().compareVersion ( major, minor, update ) >= 0;
    }

    /**
     * Returns whether applicaton is running on java 6 version and above or not.
     *
     * @return true if the application is running on java 6 version and above, false otherwise
     */
    public static boolean isJava6orAbove ()
    {
        return getJavaVersion ().compareVersion ( 1.6, 0, 0 ) >= 0;
    }

    /**
     * Returns whether applicaton is running on java 7 version and above or not.
     *
     * @return true if the application is running on java 7 version and above, false otherwise
     */
    public static boolean isJava7orAbove ()
    {
        return getJavaVersion ().compareVersion ( 1.7, 0, 0 ) >= 0;
    }

    /**
     * Returns whether applicaton is running on java 8 version and above or not.
     *
     * @return true if the application is running on java 8 version and above, false otherwise
     */
    public static boolean isJava8orAbove ()
    {
        return getJavaVersion ().compareVersion ( 1.8, 0, 0 ) >= 0;
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
     * Returns short OS name.
     *
     * @return short OS name
     */
    public static String getShortOsName ()
    {
        if ( isWindows () )
        {
            return WINDOWS;
        }
        else if ( isMac () )
        {
            return MAC;
        }
        else if ( isUnix () )
        {
            return UNIX;
        }
        else if ( isSolaris () )
        {
            return SOLARIS;
        }
        else
        {
            return null;
        }
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
    public static ImageIcon getOsIcon ( boolean color )
    {
        return getOsIcon ( 16, color );
    }

    /**
     * Returns OS icon of specified size if such exists; returns 16x16 icon otherwise.
     *
     * @param size preferred icon size
     * @return OS icon
     */
    public static ImageIcon getOsIcon ( int size )
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
    public static ImageIcon getOsIcon ( int size, boolean color )
    {
        if ( size != 16 && size != 32 )
        {
            size = 16;
        }
        final String os = getShortOsName ();
        final String mark = color ? "_colored" : "";
        return os != null ? new ImageIcon ( SystemUtils.class.getResource ( "icons/os/" + size + "/" + os + mark + ".png" ) ) : null;
    }

    /**
     * Returns whether current OS is windows or not.
     *
     * @return true if current OS is windows, false otherwise
     */
    public static boolean isWindows ()
    {
        return getOsName ().toLowerCase ().contains ( "win" );
    }

    /**
     * Returns whether current OS is mac or not.
     *
     * @return true if current OS is mac, false otherwise
     */
    public static boolean isMac ()
    {
        String os = getOsName ().toLowerCase ();
        return os.contains ( "mac" ) || os.contains ( "darwin" );
    }

    /**
     * Returns whether current OS is unix or not.
     *
     * @return true if current OS is unix, false otherwise
     */
    public static boolean isUnix ()
    {
        String os = getOsName ().toLowerCase ();
        return os.contains ( "nix" ) || os.contains ( "nux" );
    }

    /**
     * Returns whether current OS is solaris or not.
     *
     * @return true if current OS is solaris, false otherwise
     */
    public static boolean isSolaris ()
    {
        return getOsName ().toLowerCase ().contains ( "sunos" );
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
        return ManagementFactory.getOperatingSystemMXBean ().getName ();
    }

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
     * @return true if Caps Lock is on, false otherwise
     */
    public static boolean isCapsLock ()
    {
        return Toolkit.getDefaultToolkit ().getLockingKeyState ( KeyEvent.VK_CAPS_LOCK );
    }

    /**
     * Returns whether Num Lock is on or not.
     *
     * @return true if Num Lock is on, false otherwise
     */
    public static boolean isNumLock ()
    {
        return Toolkit.getDefaultToolkit ().getLockingKeyState ( KeyEvent.VK_NUM_LOCK );
    }

    /**
     * Returns whether Scroll Lock is on or not.
     *
     * @return true if Scroll Lock is on, false otherwise
     */
    public static boolean isScrollLock ()
    {
        return Toolkit.getDefaultToolkit ().getLockingKeyState ( KeyEvent.VK_SCROLL_LOCK );
    }

    /**
     * Returns default GraphicsConfiguration for main screen.
     *
     * @return mail scren GraphicsConfiguration
     */
    public static GraphicsConfiguration getGraphicsConfiguration ()
    {
        return getGraphicsEnvironment ().getDefaultScreenDevice ().getDefaultConfiguration ();
    }

    /**
     * Returns default GraphicsEnvironment.
     *
     * @return default GraphicsEnvironment
     */
    private static GraphicsEnvironment getGraphicsEnvironment ()
    {
        return GraphicsEnvironment.getLocalGraphicsEnvironment ();
    }

    /**
     * Returns list of available screen devices.
     *
     * @return list of available screen devices
     */
    public static List<GraphicsDevice> getGraphicsDevices ()
    {
        // Retrieving system devices
        GraphicsEnvironment graphicsEnvironment = getGraphicsEnvironment ();
        GraphicsDevice[] screenDevices = graphicsEnvironment.getScreenDevices ();
        GraphicsDevice defaultScreenDevice = graphicsEnvironment.getDefaultScreenDevice ();

        // Collecting devices into list
        List<GraphicsDevice> devices = new ArrayList<GraphicsDevice> ();
        for ( GraphicsDevice gd : screenDevices )
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
}