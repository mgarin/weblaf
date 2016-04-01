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
import com.alee.managers.log.Log;

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
     * Key used to indicate a light weight popup should be used.
     */
    public static final int LIGHT_WEIGHT_POPUP = 0;

    /**
     * Key used to indicate a medium weight Popup should be used.
     */
    public static final int MEDIUM_WEIGHT_POPUP = 1;

    /*
     * Key used to indicate a heavy weight Popup should be used.
     */
    public static final int HEAVY_WEIGHT_POPUP = 2;

    /**
     * Whether or not window transparency is allowed globally or not.
     */
    private static boolean windowTransparencyAllowed = true;

    /**
     * Whether or not window shape is allowed globally or not.
     */
    private static boolean windowShapeAllowed = true;

    /**
     * Allow per-pixel transparent windows usage on Linux systems.
     * This might be an unstable feature so it is disabled by default.
     */
    private static boolean allowLinuxTransparency = false;

    /**
     * Returns whether per-pixel transparent windows usage is allowed on Linux systems or not.
     *
     * @return true if per-pixel transparent windows usage is allowed on Linux systems, false otherwise
     */
    public static boolean isAllowLinuxTransparency ()
    {
        return allowLinuxTransparency;
    }

    /**
     * Sets whether per-pixel transparent windows usage is allowed on Linux systems or not.
     * This might be an unstable feature so it is disabled by default. Use it at your own risk.
     *
     * @param allow whether per-pixel transparent windows usage is allowed on Linux systems or not
     */
    public static void setAllowLinuxTransparency ( final boolean allow )
    {
        ProprietaryUtils.allowLinuxTransparency = allow;
    }

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
            Log.error ( ProprietaryUtils.class, e );
        }
        catch ( final NoSuchFieldException e )
        {
            Log.error ( ProprietaryUtils.class, e );
        }
        catch ( final IllegalAccessException e )
        {
            Log.error ( ProprietaryUtils.class, e );
        }
        catch ( final NoSuchMethodException e )
        {
            Log.error ( ProprietaryUtils.class, e );
        }
        catch ( final InvocationTargetException e )
        {
            Log.error ( ProprietaryUtils.class, e );
        }
    }

    /**
     * Returns whether window transparency is allowed globally or not.
     * Whether or not it is allowed depends on the settings and current OS type.
     *
     * @return true if window transparency is allowed globally, false otherwise
     */
    public static boolean isWindowTransparencyAllowed ()
    {
        try
        {
            if ( windowTransparencyAllowed )
            {
                // Replace when Unix-systems will have proper support for transparency
                // Also on Windows systems fonts of all components on transparent windows is not the same, which becomes a real issue sometimes
                final Class au = ReflectUtils.getClass ( "com.sun.awt.AWTUtilities" );
                final Class t = ReflectUtils.getInnerClass ( au, "Translucency" );
                final Object ppt = ReflectUtils.getStaticFieldValue ( t, "PERPIXEL_TRANSPARENT" );
                final Boolean wts = ReflectUtils.callStaticMethod ( au, "isWindowTranslucencySupported" );
                final Boolean tc = ReflectUtils.callStaticMethod ( au, "isTranslucencyCapable", SystemUtils.getGraphicsConfiguration () );
                final Boolean ppts = ReflectUtils.callStaticMethod ( au, "isTranslucencySupported", ppt );
                return wts && tc && ppts && ( SystemUtils.isWindows () || SystemUtils.isMac () || SystemUtils.isSolaris () ||
                        SystemUtils.isUnix () && allowLinuxTransparency );
            }
            else
            {
                return false;
            }
        }
        catch ( final Throwable e )
        {
            return SystemUtils.isWindows () || SystemUtils.isMac () || SystemUtils.isSolaris () ||
                    SystemUtils.isUnix () && allowLinuxTransparency;
        }
    }

    /**
     * Sets whether window transparency is allowed globally or not.
     *
     * @param allowed whether window transparency is allowed globally or not
     */
    public static void setWindowTransparencyAllowed ( final boolean allowed )
    {
        windowTransparencyAllowed = allowed;
    }

    /**
     * Returns whether window shape is allowed globally or not.
     * Whether or not it is allowed depends on the settings and current OS type.
     *
     * @return true if window shape is allowed globally, false otherwise
     */
    public static boolean isWindowShapeAllowed ()
    {
        try
        {
            if ( windowShapeAllowed )
            {
                // TODO: move to Java 7 API
                final Class au = ReflectUtils.getClass ( "com.sun.awt.AWTUtilities" );
                final Class t = ReflectUtils.getInnerClass ( au, "Translucency" );
                final Object ppt = ReflectUtils.getStaticFieldValue ( t, "PERPIXEL_TRANSPARENT" );
                final Boolean wts = ReflectUtils.callStaticMethod ( au, "isWindowShapingSupported" );
                final Boolean ppts = ReflectUtils.callStaticMethod ( au, "isTranslucencySupported", ppt );
                return wts && ppts;
            }
            else
            {
                return false;
            }
        }
        catch ( final Throwable e )
        {
            return windowShapeAllowed;
        }
    }

    /**
     * Sets whether window shape is allowed globally or not.
     *
     * @param allowed whether window shape is allowed globally or not
     */
    public static void setWindowShapeAllowed ( final boolean allowed )
    {
        windowShapeAllowed = allowed;
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
                // Change system window opacity
                if ( SystemUtils.isJava7orAbove () )
                {
                    // In Java 7 and later we change window background color to make it opaque or non-opaque
                    setupOpacityBackgroundColor ( opaque, window );
                }
                else
                {
                    // In Java 6 we use AWTUtilities method to change window opacity mode
                    ReflectUtils.callStaticMethod ( "com.sun.awt.AWTUtilities", "setWindowOpaque", window, opaque );
                }

                // todo Possible intersection with styling from skin
                // Changing opacity of root and content panes
                final JRootPane rootPane = SwingUtils.getRootPane ( window );
                if ( rootPane != null )
                {
                    // Changing root pane background color and opacity
                    setupOpacityBackgroundColor ( opaque, rootPane );
                    rootPane.setOpaque ( opaque );

                    // Changing content pane color and opacity
                    final Container container = rootPane.getContentPane ();
                    if ( container != null )
                    {
                        setupOpacityBackgroundColor ( opaque, container );
                        if ( container instanceof JComponent )
                        {
                            ( ( JComponent ) container ).setOpaque ( opaque );
                        }
                    }

                    // Repaint root pane in case opacity changed
                    // Without this repaint it will not be properly displayed
                    rootPane.repaint ();
                }
            }
            catch ( final Throwable e )
            {
                // Ignore any exceptions this native feature might cause
                // Still, should inform that such actions cause an exception on the underlying system
                Log.error ( ProprietaryUtils.class, e );
            }
        }
    }

    /**
     * Changes component background color to match opacity.
     *
     * @param opaque    whether component should should be opaque or not
     * @param component component to process
     * @throws java.lang.NoSuchMethodException             if method was not found
     * @throws java.lang.reflect.InvocationTargetException if method throws an exception
     * @throws java.lang.IllegalAccessException            if method is inaccessible
     */
    private static void setupOpacityBackgroundColor ( final boolean opaque, final Component component )
            throws NoSuchMethodException, InvocationTargetException, IllegalAccessException
    {
        ReflectUtils.callMethod ( component, "setBackground", opaque ? Color.WHITE : StyleConstants.transparent );
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
                Log.error ( ProprietaryUtils.class, e );
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
                Log.error ( ProprietaryUtils.class, e );
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
                Log.error ( ProprietaryUtils.class, e );
            }
        }
        return 1f;
    }

    /**
     * Sets window shape if that option is supported by the underlying system.
     *
     * @param window window to process
     * @param shape  new window shape
     */
    public static void setWindowShape ( final Window window, final Shape shape )
    {
        if ( window != null && isWindowShapeAllowed () )
        {
            try
            {
                if ( SystemUtils.isJava7orAbove () )
                {
                    // For Java 7 and later this will work just fine
                    ReflectUtils.callMethod ( window, "setShape", shape );
                }
                else
                {
                    // Workaround to allow this method usage on all possible Java versions
                    ReflectUtils.callStaticMethod ( "com.sun.awt.AWTUtilities", "setWindowShape", window, shape );
                }
            }
            catch ( final Throwable e )
            {
                // Ignore any exceptions this native feature might cause
                // Still, should inform that such actions cause an exception on the underlying system
                Log.error ( ProprietaryUtils.class, e );
            }
        }
    }

    /**
     * Returns window shape.
     *
     * @param window window to process
     * @return window shape
     */
    public static Shape getWindowShape ( final Window window )
    {
        if ( window != null && isWindowShapeAllowed () )
        {
            try
            {
                final Shape shape;
                if ( SystemUtils.isJava7orAbove () )
                {
                    // For Java 7 and later this will work just fine
                    shape = ReflectUtils.callMethod ( window, "getShape" );
                }
                else
                {
                    // Workaround to allow this method usage on all possible Java versions
                    shape = ReflectUtils.callStaticMethod ( "com.sun.awt.AWTUtilities", "getWindowShape", window );
                }
                return shape;
            }
            catch ( final Throwable e )
            {
                // Ignore any exceptions this native feature might cause
                // Still, should inform that such actions cause an exception on the underlying system
                Log.error ( ProprietaryUtils.class, e );
            }
        }
        return null;
    }
}