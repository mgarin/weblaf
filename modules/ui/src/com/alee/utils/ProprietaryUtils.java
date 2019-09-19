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
import com.alee.utils.reflection.ReflectionException;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.plaf.basic.BasicLookAndFeel;
import java.awt.*;
import java.awt.event.WindowListener;
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
     * Private constructor to avoid instantiation.
     */
    private ProprietaryUtils ()
    {
        throw new UtilityException ( "Utility classes are not meant to be instantiated" );
    }

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
     * Initializes proprietary {@code javax.swing.plaf.basic.BasicLookAndFeel.AWTEventHelper}.
     */
    public static void installAWTEventListener ()
    {
        try
        {
            final LookAndFeel laf = UIManager.getLookAndFeel ();
            if ( laf instanceof BasicLookAndFeel )
            {
                ReflectUtils.callMethod ( laf, "installAWTEventListener" );
            }
        }
        catch ( final NoSuchMethodException e )
        {
            LoggerFactory.getLogger ( ProprietaryUtils.class ).error ( e.toString (), e );
        }
        catch ( final IllegalAccessException e )
        {
            LoggerFactory.getLogger ( ProprietaryUtils.class ).error ( e.toString (), e );
        }
        catch ( final InvocationTargetException e )
        {
            LoggerFactory.getLogger ( ProprietaryUtils.class ).error ( e.toString (), e );
        }
    }

    /**
     * Installs some proprietary LaF defaults for proper text rendering.
     * Basically this method is a workaround for this simple call:
     * {@code table.put ( sun.swing.SwingUtilities2.AA_TEXT_PROPERTY_KEY, sun.swing.SwingUtilities2.AATextInfo.getAATextInfo ( true ) );}
     * But this implementation doesn't directly use any proprietary API.
     *
     * @param table defaults table
     */
    public static void setupAATextInfo ( @NotNull final UIDefaults table )
    {
        if ( SystemUtils.isJava9orAbove () )
        {
            try
            {
                final Class su2 = ReflectUtils.getClass ( "sun.swing.SwingUtilities2" );
                ReflectUtils.callStaticMethod ( su2, "putAATextInfo", true, table );
            }
            catch ( final NoSuchMethodException e )
            {
                LoggerFactory.getLogger ( ProprietaryUtils.class ).error ( e.toString (), e );
            }
            catch ( final IllegalAccessException e )
            {
                LoggerFactory.getLogger ( ProprietaryUtils.class ).error ( e.toString (), e );
            }
            catch ( final InvocationTargetException e )
            {
                LoggerFactory.getLogger ( ProprietaryUtils.class ).error ( e.toString (), e );
            }
            catch ( final ClassNotFoundException e )
            {
                LoggerFactory.getLogger ( ProprietaryUtils.class ).error ( e.toString (), e );
            }
        }
        else
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
                LoggerFactory.getLogger ( ProprietaryUtils.class ).error ( e.toString (), e );
            }
            catch ( final NoSuchFieldException e )
            {
                LoggerFactory.getLogger ( ProprietaryUtils.class ).error ( e.toString (), e );
            }
            catch ( final IllegalAccessException e )
            {
                LoggerFactory.getLogger ( ProprietaryUtils.class ).error ( e.toString (), e );
            }
            catch ( final NoSuchMethodException e )
            {
                LoggerFactory.getLogger ( ProprietaryUtils.class ).error ( e.toString (), e );
            }
            catch ( final InvocationTargetException e )
            {
                LoggerFactory.getLogger ( ProprietaryUtils.class ).error ( e.toString (), e );
            }
        }
    }

    /**
     * Setups parameters that can improve Swing performance on some JDK versions.
     */
    public static void setupPerformanceParameters ()
    {
        if ( SystemUtils.isJava8orAbove () )
        {
            /**
             * Uses faster color management module instead of default one.
             * This is only needed for Java 8 and later versions, in older versions KcmsServiceProvider was used by default.
             */
            System.setProperty ( "sun.java2d.cmm", "sun.java2d.cmm.kcms.KcmsServiceProvider" );
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
        boolean allowed;
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
                allowed = wts && tc && ppts && ( SystemUtils.isWindows () || SystemUtils.isMac () || SystemUtils.isSolaris () ||
                        SystemUtils.isUnix () && allowLinuxTransparency );
            }
            else
            {
                allowed = false;
            }
        }
        catch ( final Exception e )
        {
            allowed = SystemUtils.isWindows () || SystemUtils.isMac () || SystemUtils.isSolaris () ||
                    SystemUtils.isUnix () && allowLinuxTransparency;
        }
        return allowed;
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
        boolean allowed;
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
                allowed = wts && ppts;
            }
            else
            {
                allowed = false;
            }
        }
        catch ( final Exception e )
        {
            allowed = windowShapeAllowed;
        }
        return allowed;
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
    public static void setWindowOpaque ( @Nullable final Window window, final boolean opaque )
    {
        if ( window != null && isWindowTransparencyAllowed () )
        {
            try
            {
                // Change system window opacity
                if ( SystemUtils.isJava7orAbove () )
                {
                    // In Java 7 and later we change window background color to make it opaque or non-opaque
                    setupOpacityBackgroundColor ( window, opaque );
                }
                else
                {
                    // In Java 6 we use AWTUtilities method to change window opacity mode
                    ReflectUtils.callStaticMethod ( "com.sun.awt.AWTUtilities", "setWindowOpaque", window, opaque );
                }

                // todo Possible intersection with styling from skin
                // Changing opacity of root and content panes
                final JRootPane rootPane = CoreSwingUtils.getRootPane ( window );
                if ( rootPane != null )
                {
                    // Changing root pane background color and opacity
                    setupOpacityBackgroundColor ( rootPane, opaque );
                    rootPane.setOpaque ( opaque );

                    // Changing content pane color and opacity
                    final Container container = rootPane.getContentPane ();
                    if ( container != null )
                    {
                        setupOpacityBackgroundColor ( container, opaque );
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
            catch ( final Exception e )
            {
                // Ignore any exceptions this native feature might cause
                // Still, should inform that such actions cause an exception on the underlying system
                LoggerFactory.getLogger ( ProprietaryUtils.class ).error ( e.toString (), e );
            }
        }
    }

    /**
     * Changes {@link Component} background color to match opacity.
     *
     * @param component {@link Component} to process
     * @param opaque    whether component should should be opaque or not
     * @throws java.lang.NoSuchMethodException             if method was not found
     * @throws java.lang.reflect.InvocationTargetException if method throws an exception
     * @throws java.lang.IllegalAccessException            if method is inaccessible
     */
    private static void setupOpacityBackgroundColor ( @NotNull final Component component, final boolean opaque )
            throws NoSuchMethodException, InvocationTargetException, IllegalAccessException
    {
        ReflectUtils.callMethod ( component, "setBackground", opaque ? Color.WHITE : ColorUtils.transparent () );
    }

    /**
     * Enables mixing cutout shape to avoid issues with AWT and non-opaque components when they are layered within same container.
     *
     * @param component {@link Component} to enable mixing cutout shape for
     */
    public static void enableMixingCutoutShape ( final Component component )
    {
        try
        {
            if ( SystemUtils.isJava9orAbove () )
            {
                ReflectUtils.callMethod ( component, "setMixingCutoutShape", new Rectangle () );
            }
            else
            {
                final Object componentAccessor = ReflectUtils.callStaticMethod ( "sun.awt.AWTAccessor", "getComponentAccessor" );
                ReflectUtils.callMethod ( componentAccessor, "setMixingCutoutShape", component, new Rectangle () );
            }
        }
        catch ( final Exception e )
        {
            // Ignore any exceptions this native feature might cause
            // Still, should inform that such actions cause an exception on the underlying system
            LoggerFactory.getLogger ( ProprietaryUtils.class ).error ( e.toString (), e );
        }
    }

    /**
     * Returns whether window is opaque or not.
     *
     * @param window window to process
     * @return whether window background is opaque or not
     */
    public static boolean isWindowOpaque ( @Nullable final Window window )
    {
        boolean opaque = true;
        if ( window != null && isWindowTransparencyAllowed () )
        {
            try
            {
                final Boolean isOpaque;
                if ( SystemUtils.isJava7orAbove () )
                {
                    // For Java 7 and later this will work just fine
                    final Color bg = ReflectUtils.callMethod ( window, "getBackground" );
                    isOpaque = bg == null || bg.getAlpha () == 255;
                }
                else
                {
                    // Workaround to allow this method usage on all possible Java versions
                    isOpaque = ReflectUtils.callStaticMethod ( "com.sun.awt.AWTUtilities", "isWindowOpaque", window );
                }
                opaque = isOpaque != null ? isOpaque : true;
            }
            catch ( final Exception e )
            {
                // Ignore any exceptions this native feature might cause
                // Still, should inform that such actions cause an exception on the underlying system
                LoggerFactory.getLogger ( ProprietaryUtils.class ).error ( e.toString (), e );
            }
        }
        return opaque;
    }

    /**
     * Sets window opacity if that option is supported by the underlying system.
     *
     * @param window  window to process
     * @param opacity new window opacity
     */
    public static void setWindowOpacity ( @Nullable final Window window, final float opacity )
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
            catch ( final Exception e )
            {
                // Ignore any exceptions this native feature might cause
                // Still, should inform that such actions cause an exception on the underlying system
                LoggerFactory.getLogger ( ProprietaryUtils.class ).error ( e.toString (), e );
            }
        }
    }

    /**
     * Returns window opacity.
     *
     * @param window window to process
     * @return window opacity
     */
    public static float getWindowOpacity ( @Nullable final Window window )
    {
        float opacity = 1f;
        if ( window != null && isWindowTransparencyAllowed () )
        {
            try
            {
                final Float retrieved;
                if ( SystemUtils.isJava7orAbove () )
                {
                    // For Java 7 and later this will work just fine
                    retrieved = ReflectUtils.callMethod ( window, "getOpacity" );
                }
                else
                {
                    // Workaround to allow this method usage on all possible Java versions
                    retrieved = ReflectUtils.callStaticMethod ( "com.sun.awt.AWTUtilities", "getWindowOpacity", window );
                }
                opacity = retrieved != null ? retrieved : 1f;
            }
            catch ( final Exception e )
            {
                // Ignore any exceptions this native feature might cause
                // Still, should inform that such actions cause an exception on the underlying system
                LoggerFactory.getLogger ( ProprietaryUtils.class ).error ( e.toString (), e );
            }
        }
        return opacity;
    }

    /**
     * Sets new {@link Shape} for the specified {@link Window} if that option is supported by the underlying system.
     * {@code null} can be passed to reset {@link Window} shape.
     *
     * @param window {@link Window} to update
     * @param shape  new {@link Shape} for the {@link Window}
     */
    public static void setWindowShape ( @Nullable final Window window, @Nullable final Shape shape )
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
            catch ( final Exception e )
            {
                // Ignore any exceptions this native feature might cause
                // Still, should inform that such actions cause an exception on the underlying system
                LoggerFactory.getLogger ( ProprietaryUtils.class ).error ( e.toString (), e );
            }
        }
    }

    /**
     * Returns window shape.
     *
     * @param window window to process
     * @return window shape
     */
    @Nullable
    public static Shape getWindowShape ( @Nullable final Window window )
    {
        Shape shape = null;
        if ( window != null && isWindowShapeAllowed () )
        {
            try
            {
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
            }
            catch ( final Exception e )
            {
                // Ignore any exceptions this native feature might cause
                // Still, should inform that such actions cause an exception on the underlying system
                LoggerFactory.getLogger ( ProprietaryUtils.class ).error ( e.toString (), e );
            }
        }
        return shape;
    }

    /**
     * Performs window policy initalization procedure.
     * This method was changed between JDK versions so this workaround is used to support it properly.
     *
     * @param window window to process
     */
    public static void checkAndSetPolicy ( @NotNull final Window window )
    {
        final String toolkitClass = "sun.awt.SunToolkit";
        try
        {
            final Class<Object> toolkit = ReflectUtils.getClass ( toolkitClass );
            try
            {
                if ( SystemUtils.isJava7orAbove () )
                {
                    ReflectUtils.callStaticMethod ( toolkit, "checkAndSetPolicy", window );
                }
                else
                {
                    ReflectUtils.callStaticMethod ( toolkit, "checkAndSetPolicy", window, true );
                }
            }
            catch ( final Exception e )
            {
                final String msg = "Unable to check and set window policy";
                LoggerFactory.getLogger ( ProprietaryUtils.class ).error ( msg, e );
            }
        }
        catch ( final Exception e )
        {
            final String msg = "Unable to find toolkit: %s";
            LoggerFactory.getLogger ( ProprietaryUtils.class ).error ( String.format ( msg, toolkitClass ), e );
        }
    }

    /**
     * Returns {@code javax.swing.SwingUtilities.SharedOwnerFrame} instance that is used as default parent for all parentless dialogs.
     * todo Returning {@code null} from this method can potentially cause issues for windows
     *
     * @return {@code javax.swing.SwingUtilities.SharedOwnerFrame} instance that is used as default parent for all parentless dialogs
     */
    @Nullable
    public static Frame getSharedOwnerFrame ()
    {
        Frame sharedOwner = null;
        try
        {
            sharedOwner = ReflectUtils.callStaticMethod ( SwingUtilities.class, "getSharedOwnerFrame" );
        }
        catch ( final NoSuchMethodException e )
        {
            final String msg = "Unable to retrieve SharedOwnerFrame, it seems your JDK version doesn't support it";
            LoggerFactory.getLogger ( ProprietaryUtils.class ).error ( msg, e );
        }
        catch ( final InvocationTargetException e )
        {
            final String msg = "Exception occured while retrieving SharedOwnerFrame";
            LoggerFactory.getLogger ( ProprietaryUtils.class ).error ( msg, e );
        }
        catch ( final IllegalAccessException e )
        {
            final String msg = "Unable to access SharedOwnerFrame";
            LoggerFactory.getLogger ( ProprietaryUtils.class ).error ( msg, e );
        }
        return sharedOwner;
    }

    /**
     * Returns {@code javax.swing.SwingUtilities.SharedOwnerFrame} shutdown listener.
     *
     * @return {@code javax.swing.SwingUtilities.SharedOwnerFrame} shutdown listener
     */
    @Nullable
    public static WindowListener getSharedOwnerFrameShutdownListener ()
    {
        WindowListener listener = null;
        try
        {
            listener = ReflectUtils.callStaticMethod ( SwingUtilities.class, "getSharedOwnerFrameShutdownListener" );
        }
        catch ( final NoSuchMethodException e )
        {
            final String msg = "Unable to retrieve SharedOwnerFrame shutdown listener, it seems your JDK version doesn't support it";
            LoggerFactory.getLogger ( ProprietaryUtils.class ).error ( msg, e );
        }
        catch ( final InvocationTargetException e )
        {
            final String msg = "Exception occured while retrieving SharedOwnerFrame shutdown listener";
            LoggerFactory.getLogger ( ProprietaryUtils.class ).error ( msg, e );
        }
        catch ( final IllegalAccessException e )
        {
            final String msg = "Unable to access SharedOwnerFrame shutdown listener";
            LoggerFactory.getLogger ( ProprietaryUtils.class ).error ( msg, e );
        }
        return listener;
    }

    /**
     * Returns window ancestor for specified component or {@code javax.swing.SwingUtilities.SharedOwnerFrame} if it doesn't exist.
     * Any kinds of {@link Window}s except for {@link Dialog} and {@link Frame} are not accepted by {@link JDialog}, that is why they are
     * filtered and {@code javax.swing.SwingUtilities.SharedOwnerFrame} is used instead in such cases.
     *
     * @param component component to process
     * @return window ancestor for specified component or {@code javax.swing.SwingUtilities.SharedOwnerFrame} if it doesn't exist
     */
    @Nullable
    public static Window getWindowAncestorForDialog ( final Component component )
    {
        final Window window = CoreSwingUtils.getWindowAncestor ( component );
        return window instanceof Dialog || window instanceof Frame ? window : getSharedOwnerFrame ();
    }

    /**
     * Sets specified {@link Window} type to {@code Window.Type.POPUP}.
     * Temporary substitute for JDK7+ {@code Window#setType(Window.Type)} method.
     * Also {@code Window.Type.POPUP} is not set for Unix systems due to window prioritization issues.
     * todo Simplify upon migration to JDK7+
     *
     * @param window {@link Window} to change type for
     */
    public static void setPopupWindowType ( @NotNull final Window window )
    {
        setWindowType ( window, "POPUP" );
    }

    /**
     * Sets specified {@link Window} type to {@code Window.Type.UTILITY}.
     * Temporary substitute for JDK7+ {@code Window#setType(Window.Type)} method.
     * todo Simplify upon migration to JDK7+
     *
     * @param window {@link Window} to change type for
     */
    public static void setUtilityWindowType ( @NotNull final Window window )
    {
        setWindowType ( window, "UTILITY" );
    }

    /**
     * Sets specified {@link Window} type to {@code Window.Type.<typeName>}.
     * Temporary substitute for JDK7+ {@code Window#setType(Window.Type)} method.
     * todo Simplify upon migration to JDK7+
     *
     * @param window   {@link Window} to change type for
     * @param typeName {@code Window.Type} enumeration literal name
     */
    private static void setWindowType ( @NotNull final Window window, @NotNull final String typeName )
    {
        if ( SystemUtils.isJava7orAbove () )
        {
            try
            {
                //
                final Class type = ReflectUtils.getInnerClass ( Window.class, "Type" );
                final Object popup = ReflectUtils.getStaticFieldValue ( type, typeName );
                ReflectUtils.callMethod ( window, "setType", popup );
            }
            catch ( final Exception e )
            {
                throw new ReflectionException ( "Unable to setup Window type: " + window );
            }
        }
    }
}