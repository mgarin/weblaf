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

import com.alee.laf.StyleConstants;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.InvocationTargetException;

/**
 * This utility class operates only with proprietary API calls.
 * Their usage is inevitable, otherwise i would have replaced them with something else.
 *
 * @author Mikle Garin
 */

public final class ProprietaryUtils
{
    /**
     * Installs some proprietary L&amp;F defaults for proper text rendering.
     * Basically this method is a workaround for this simple call:
     * {@code
     * table.put ( sun.swing.SwingUtilities2.AA_TEXT_PROPERTY_KEY, sun.swing.SwingUtilities2.AATextInfo.getAATextInfo ( true ) );
     * }
     * but it doesn't directly use any proprietary API.
     *
     * @param table defaults table
     */
    public static void setupUIDefaults ( final UIDefaults table )
    {
        try
        {
            final Class su2 = ReflectUtils.getClass ( "sun.swing.SwingUtilities2" );
            final Object aaProperty = ReflectUtils.getStaticFieldValue ( su2, "AA_TEXT_PROPERTY_KEY" );
            final Class aaTextInfo = ReflectUtils.getInnerClass ( su2, "AATextInfo" );
            final Object aaValue = ReflectUtils.callStaticMethod ( aaTextInfo, "getAATextInfo", true );
            table.put ( aaProperty, aaValue );
        }
        catch ( final ClassNotFoundException e )
        {
            e.printStackTrace ();
        }
        catch ( final NoSuchFieldException e )
        {
            e.printStackTrace ();
        }
        catch ( final IllegalAccessException e )
        {
            e.printStackTrace ();
        }
        catch ( final NoSuchMethodException e )
        {
            e.printStackTrace ();
        }
        catch ( final InvocationTargetException e )
        {
            e.printStackTrace ();
        }
    }

    /**
     * Returns whether window transparency is supported on current OS or not.
     *
     * @return true if window transparency is supported on current OS; false otherwise
     */
    public static boolean isWindowTransparencyAllowed ()
    {
        try
        {
            // Replace when Unix-systems will have proper support for transparency
            // com.sun.awt.AWTUtilities.isTranslucencySupported ( com.sun.awt.AWTUtilities.Translucency.PERPIXEL_TRANSPARENT )
            return SystemUtils.isWindows () || SystemUtils.isMac () || SystemUtils.isSolaris ();
        }
        catch ( final Throwable e )
        {
            return false;
        }
    }

    /**
     * Sets window opaque if that option is supported by the underlying system.
     *
     * @param window window to process
     * @param opaque whether should make window opaque or not
     */
    public static void setWindowOpaque ( final Window window, final boolean opaque )
    {
        if ( window != null && isWindowTransparencyAllowed () )
        {
            try
            {
                if ( SystemUtils.isJava7orAbove () )
                {
                    // For Java 7 and later this will work just fine
                    final Color bg = opaque ? StyleConstants.backgroundColor : StyleConstants.transparent;
                    ReflectUtils.callMethod ( window, "setBackground", bg );
                }
                else
                {
                    // Workaround to allow this method usage on all possible Java versions
                    ReflectUtils.callStaticMethod ( "com.sun.awt.AWTUtilities", "setWindowOpaque", window, opaque );
                }
            }
            catch ( final Throwable e )
            {
                // Ignore any exceptions this native feature might cause
                // Still, should inform that such actions cause an exception on the underlying system
                e.printStackTrace ();
            }

            // Fix for later Java 7 versions
            // Plus this makes sense even for earlier JDK versions so its outside of version-relative part
            if ( window instanceof JWindow )
            {
                final JWindow jWindow = ( JWindow ) window;
                boolean modified = false;

                // Setting root pane to proper opacity state
                final JRootPane rootPane = jWindow.getRootPane ();
                if ( rootPane != null && rootPane.isOpaque () != opaque )
                {
                    rootPane.setOpaque ( opaque );
                    modified = true;
                }

                // Setting content pane to proper opacity state
                final Container container = jWindow.getContentPane ();
                if ( container != null && container instanceof JComponent && container.isOpaque () != opaque )
                {
                    ( ( JComponent ) container ).setOpaque ( opaque );
                    modified = true;
                }

                // Repaint root pane in case opacity changed
                // Without this update will not be properly displayed
                if ( modified )
                {
                    rootPane.repaint ();
                }
            }
        }
    }

    /**
     * Returns whether window is opaque or not.
     *
     * @param window window to process
     * @return whether window background is opaque or not
     */
    public static boolean isWindowOpaque ( final Window window )
    {
        if ( window != null && isWindowTransparencyAllowed () )
        {
            try
            {
                final Boolean isOpaque;
                if ( SystemUtils.isJava7orAbove () )
                {
                    // For Java 7 and later this will work just fine
                    final Color bg = ReflectUtils.callMethod ( window, "getBackground" );
                    isOpaque = bg.getAlpha () == 255;
                }
                else
                {
                    // Workaround to allow this method usage on all possible Java versions
                    isOpaque = ReflectUtils.callStaticMethod ( "com.sun.awt.AWTUtilities", "isWindowOpaque", window );
                }
                return isOpaque != null ? isOpaque : true;
            }
            catch ( final Throwable e )
            {
                // Ignore any exceptions this native feature might cause
                // Still, should inform that such actions cause an exception on the underlying system
                e.printStackTrace ();
            }
        }
        return true;
    }

    /**
     * Sets window opacity if that option is supported by the underlying system.
     *
     * @param window  window to process
     * @param opacity new window opacity
     */
    public static void setWindowOpacity ( final Window window, final float opacity )
    {
        if ( window != null && isWindowTransparencyAllowed () )
        {
            try
            {
                if ( SystemUtils.isJava7orAbove () )
                {
                    // For Java 7 and later this will work just fine
                    ReflectUtils.callMethod ( window, "setOpacity", opacity );
                }
                else
                {
                    // Workaround to allow this method usage on all possible Java versions
                    ReflectUtils.callStaticMethod ( "com.sun.awt.AWTUtilities", "setWindowOpacity", window, opacity );
                }
            }
            catch ( final Throwable e )
            {
                // Ignore any exceptions this native feature might cause
                // Still, should inform that such actions cause an exception on the underlying system
                e.printStackTrace ();
            }
        }
    }

    /**
     * Returns window opacity.
     *
     * @param window window to process
     * @return window opacity
     */
    public static float getWindowOpacity ( final Window window )
    {
        if ( window != null && isWindowTransparencyAllowed () )
        {
            try
            {
                final Float opacity;
                if ( SystemUtils.isJava7orAbove () )
                {
                    // For Java 7 and later this will work just fine
                    opacity = ReflectUtils.callMethod ( window, "getOpacity" );
                }
                else
                {
                    // Workaround to allow this method usage on all possible Java versions
                    opacity = ReflectUtils.callStaticMethod ( "com.sun.awt.AWTUtilities", "getWindowOpacity", window );
                }
                return opacity != null ? opacity : 1f;
            }
            catch ( final Throwable e )
            {
                // Ignore any exceptions this native feature might cause
                // Still, should inform that such actions cause an exception on the underlying system
                e.printStackTrace ();
            }
        }
        return 1f;
    }
}