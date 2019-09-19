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

package com.alee.laf;

import com.alee.api.annotations.NotNull;
import com.alee.api.annotations.Nullable;
import com.alee.api.jdk.Supplier;
import com.alee.utils.ReflectUtils;
import com.alee.utils.SystemUtils;
import org.slf4j.LoggerFactory;

import javax.swing.plaf.FontUIResource;
import javax.swing.plaf.synth.Region;
import javax.swing.plaf.synth.SynthConstants;
import java.awt.*;

/**
 * This class returns native operation system fonts for different {@link ControlType}s.
 * Fallback fonts could be used as a workaround for some systems if native fonts cannot be retrieved.
 *
 * @author Mikle Garin
 */
public final class NativeFonts
{
    /**
     * todo 1. Separate native font retrieval into NativeFonts interface implementations
     * todo 2. Should only be working as a fallback for a more convenient style-related implementation instead of default implementation
     * todo 3. Don't use generalized fonts for {@link ControlType}s, they do not fit all possible use cases on different OS
     */

    /**
     * Whether or not various {@link Font} loading related warnings should be displayed.
     */
    private static boolean warningsEnabled = true;

    /**
     * Fallback Windows font names.
     */
    private static final String TAHOMA = "Tahoma";
    private static final String SEGOE_UI = "Segoe UI";

    /**
     * Fallback Mac OS font names.
     */
    private static final String LUCIDA_GRANDE = "Lucida Grande";

    /**
     * Fallback Unix font names.
     */
    private static final String SANS_SERIF = Font.SANS_SERIF;

    /**
     * Whether or not native OS fonts should be used when possible.
     */
    private static boolean useNativeFonts = true;

    /**
     * Whether or not GTK is available.
     * It is {@code null} until availability is checked.
     */
    private static volatile Boolean gtkAvailable = null;

    /**
     * Whether or not {@code com.sun.java.swing.plaf.gtk.GTKStyleFactory} is available.
     * It is {@code null} until factory instantiation is attempted.
     */
    private static volatile Boolean gtkStyleFactoryAvailable = null;

    /**
     * {@code com.sun.java.swing.plaf.gtk.GTKStyleFactory} instance.
     * Can be {@code null} if it was not requested yet or not available.
     */
    private static Object gtkStyleFactory = null;

    /**
     * Whether or not default GTK {@code Font} is available.
     * It is {@code null} until default GTK {@link Font} is requested for the first time.
     */
    private static volatile Boolean defaultGTKFontAvailable = null;

    /**
     * Default GTK {@code Font}.
     * Can be {@code null} if it was not requested yet or not available.
     */
    private static Font defaultGTKFont = null;

    /**
     * Sets whether or not various {@link Font} loading related warnings should be displayed.
     *
     * @param enabled whether or not various {@link Font} loading related warnings should be displayed
     */
    public static void setWarningsEnabled ( final boolean enabled )
    {
        NativeFonts.warningsEnabled = enabled;
    }

    /**
     * Returns whether or not native OS fonts should be used when possible.
     *
     * @return {@code true} if native OS fonts should be used when possible, {@code false} otherwise
     */
    public static boolean isUseNativeFonts ()
    {
        return useNativeFonts;
    }

    /**
     * Sets whether or not native OS fonts should be used when possible.
     *
     * @param useNativeFonts whether or not native OS fonts should be used when possible
     */
    public static void setUseNativeFonts ( final boolean useNativeFonts )
    {
        NativeFonts.useNativeFonts = useNativeFonts;
    }

    /**
     * Returns native {@link FontUIResource} for the specified {@link ControlType}.
     *
     * @param control {@link ControlType}
     * @return native {@link FontUIResource} for the specified {@link ControlType}
     */
    @NotNull
    public static FontUIResource get ( @NotNull final ControlType control )
    {
        final FontUIResource font;
        switch ( SystemUtils.getOsType () )
        {
            case WINDOWS:
            {
                /**
                 * Available font keys:
                 * {@code "win.defaultGUI.font"} - common control font
                 * {@code "win.ansiFixed.font"} - text area font
                 * {@code "win.tooltip.font"} - tooltip font
                 * {@code "win.menu.font"} - menu, menu item and toolbar font
                 * {@code "win.frame.captionFont"} - window title font
                 * {@code "win.messagebox.font"} - alert and message component font
                 * {@code "win.icon.font"} - file chooser icon files list font
                 *
                 * Check out "http://docs.oracle.com/javase/7/docs/technotes/guides/swing/1.4/w2k_props.html" page for more informaion on
                 * the various available desktop properties including font-related ones.
                 */
                switch ( control )
                {
                    default:
                    case CONTROL:
                    {
                        font = getWindowsFont ( "win.defaultGUI.font", new Supplier<FontUIResource> ()
                        {
                            @Override
                            public FontUIResource get ()
                            {
                                return new FontUIResource ( TAHOMA, Font.PLAIN, 12 );
                            }
                        } );
                        break;
                    }
                    case TEXT:
                    {
                        /**
                         * Originally it uses {@code "win.ansiFixed.font"} key, but it provides an ugly unreadable font.
                         * Therefore I have replaced it with default native control font as it is the most common and close.
                         */
                        font = getWindowsFont ( "win.defaultGUI.font", new Supplier<FontUIResource> ()
                        {
                            @Override
                            public FontUIResource get ()
                            {
                                return new FontUIResource ( TAHOMA, Font.PLAIN, 12 );
                            }
                        } );
                        break;
                    }
                    case TOOLTIP:
                    {
                        font = getWindowsFont ( "win.tooltip.font", new Supplier<FontUIResource> ()
                        {
                            @Override
                            public FontUIResource get ()
                            {
                                return new FontUIResource ( SEGOE_UI, Font.PLAIN, 12 );
                            }
                        } );
                        break;
                    }
                    case MESSAGE:
                    {
                        font = getWindowsFont ( "win.messagebox.font", new Supplier<FontUIResource> ()
                        {
                            @Override
                            public FontUIResource get ()
                            {
                                return new FontUIResource ( SEGOE_UI, Font.PLAIN, 13 );
                            }
                        } );
                        break;
                    }
                    case MENU:
                    {
                        font = getWindowsFont ( "win.menu.font", new Supplier<FontUIResource> ()
                        {
                            @Override
                            public FontUIResource get ()
                            {
                                return new FontUIResource ( SEGOE_UI, Font.PLAIN, 12 );
                            }
                        } );
                        break;
                    }
                    case MENU_SMALL:
                    {
                        font = getWindowsFont ( "win.menu.font", new Supplier<FontUIResource> ()
                        {
                            @Override
                            public FontUIResource get ()
                            {
                                return new FontUIResource ( SEGOE_UI, Font.PLAIN, 12 );
                            }
                        } );
                        break;
                    }
                    case WINDOW:
                    {
                        font = getWindowsFont ( "win.frame.captionFont", new Supplier<FontUIResource> ()
                        {
                            @Override
                            public FontUIResource get ()
                            {
                                return new FontUIResource ( SEGOE_UI, Font.PLAIN, 14 );
                            }
                        } );
                        break;
                    }
                }
                break;
            }
            case MAC:
            {
                /**
                 * Available font methods:
                 * {@code "getControlTextFont"} - common control font
                 * {@code "getControlTextSmallFont"} - small control variation and tooltip font
                 * {@code "getViewFont"} - list, tree and table font
                 * {@code "getMenuFont"} - menu, menu item and window title font
                 * {@code "getAlertHeaderFont"} - alert and message component font
                 *
                 * todo 1. View font is currently not used as there is no separate CONTROL type for list, tree and table
                 * todo 2. Toolbar is considered as control and not menu on Mac OS X
                 */
                switch ( control )
                {
                    default:
                    case CONTROL:
                    {
                        font = getMacOSFont ( "getControlTextFont", new Supplier<FontUIResource> ()
                        {
                            @Override
                            public FontUIResource get ()
                            {
                                return new FontUIResource ( LUCIDA_GRANDE, Font.PLAIN, 13 );
                            }
                        } );
                        break;
                    }
                    case TEXT:
                    {
                        font = getMacOSFont ( "getControlTextFont", new Supplier<FontUIResource> ()
                        {
                            @Override
                            public FontUIResource get ()
                            {
                                return new FontUIResource ( LUCIDA_GRANDE, Font.PLAIN, 13 );
                            }
                        } );
                        break;
                    }
                    case TOOLTIP:
                    {
                        font = getMacOSFont ( "getControlTextSmallFont", new Supplier<FontUIResource> ()
                        {
                            @Override
                            public FontUIResource get ()
                            {
                                return new FontUIResource ( LUCIDA_GRANDE, Font.PLAIN, 11 );
                            }
                        } );
                        break;
                    }
                    case MESSAGE:
                    {
                        font = getMacOSFont ( "getAlertHeaderFont", new Supplier<FontUIResource> ()
                        {
                            @Override
                            public FontUIResource get ()
                            {
                                return new FontUIResource ( LUCIDA_GRANDE, Font.PLAIN, 11 );
                            }
                        } );
                        break;
                    }
                    case MENU:
                    {
                        font = getMacOSFont ( "getMenuFont", new Supplier<FontUIResource> ()
                        {
                            @Override
                            public FontUIResource get ()
                            {
                                return new FontUIResource ( LUCIDA_GRANDE, Font.PLAIN, 14 );
                            }
                        } );
                        break;
                    }
                    case MENU_SMALL:
                    {
                        font = getMacOSFont ( "getMenuFont", new Supplier<FontUIResource> ()
                        {
                            @Override
                            public FontUIResource get ()
                            {
                                return new FontUIResource ( LUCIDA_GRANDE, Font.PLAIN, 13 );
                            }
                        } );
                        break;
                    }
                    case WINDOW:
                    {
                        font = getMacOSFont ( "getMenuFont", new Supplier<FontUIResource> ()
                        {
                            @Override
                            public FontUIResource get ()
                            {
                                return new FontUIResource ( LUCIDA_GRANDE, Font.BOLD, 14 );
                            }
                        } );
                        break;
                    }
                }
                break;
            }
            case UNIX:
            case SOLARIS:
            {
                switch ( control )
                {
                    default:
                    case CONTROL:
                    {
                        font = getUnixFont ( Region.BUTTON, new Supplier<FontUIResource> ()
                        {
                            @Override
                            public FontUIResource get ()
                            {
                                return new FontUIResource ( SANS_SERIF, Font.PLAIN, 12 );
                            }
                        } );
                        break;
                    }
                    case TEXT:
                    {
                        font = getUnixFont ( Region.TEXT_FIELD, new Supplier<FontUIResource> ()
                        {
                            @Override
                            public FontUIResource get ()
                            {
                                return new FontUIResource ( SANS_SERIF, Font.PLAIN, 12 );
                            }
                        } );
                        break;
                    }
                    case TOOLTIP:
                    {
                        font = getUnixFont ( Region.TOOL_TIP, new Supplier<FontUIResource> ()
                        {
                            @Override
                            public FontUIResource get ()
                            {
                                return new FontUIResource ( SANS_SERIF, Font.PLAIN, 12 );
                            }
                        } );
                        break;
                    }
                    case MESSAGE:
                    {
                        font = getUnixFont ( Region.OPTION_PANE, new Supplier<FontUIResource> ()
                        {
                            @Override
                            public FontUIResource get ()
                            {
                                return new FontUIResource ( SANS_SERIF, Font.PLAIN, 12 );
                            }
                        } );
                        break;
                    }
                    case MENU:
                    {
                        font = getUnixFont ( Region.MENU_ITEM, new Supplier<FontUIResource> ()
                        {
                            @Override
                            public FontUIResource get ()
                            {
                                return new FontUIResource ( SANS_SERIF, Font.PLAIN, 12 );
                            }
                        } );
                        break;
                    }
                    case MENU_SMALL:
                    {
                        font = getUnixFont ( Region.MENU_ITEM_ACCELERATOR, new Supplier<FontUIResource> ()
                        {
                            @Override
                            public FontUIResource get ()
                            {
                                return new FontUIResource ( SANS_SERIF, Font.PLAIN, 11 );
                            }
                        } );
                        break;
                    }
                    case WINDOW:
                    {
                        font = getUnixFont ( Region.INTERNAL_FRAME_TITLE_PANE, new Supplier<FontUIResource> ()
                        {
                            @Override
                            public FontUIResource get ()
                            {
                                return new FontUIResource ( SANS_SERIF, Font.BOLD, 12 );
                            }
                        } );
                        break;
                    }
                }
                break;
            }
            case UNKNOWN:
            default:
            {
                switch ( control )
                {
                    default:
                    case CONTROL:
                    {
                        font = new FontUIResource ( SANS_SERIF, Font.PLAIN, 12 );
                        break;
                    }
                    case TEXT:
                    {
                        font = new FontUIResource ( SANS_SERIF, Font.PLAIN, 12 );
                        break;
                    }
                    case TOOLTIP:
                    {
                        font = new FontUIResource ( SANS_SERIF, Font.PLAIN, 12 );
                        break;
                    }
                    case MESSAGE:
                    {
                        font = new FontUIResource ( SANS_SERIF, Font.PLAIN, 12 );
                        break;
                    }
                    case MENU:
                    {
                        font = new FontUIResource ( SANS_SERIF, Font.PLAIN, 12 );
                        break;
                    }
                    case MENU_SMALL:
                    {
                        font = new FontUIResource ( SANS_SERIF, Font.PLAIN, 11 );
                        break;
                    }
                    case WINDOW:
                    {
                        font = new FontUIResource ( SANS_SERIF, Font.BOLD, 12 );
                        break;
                    }
                }
                break;
            }
        }
        return font;
    }

    /**
     * Returns native Windows {@link FontUIResource} for the {@link Font} key and {@link Font} size key.
     *
     * @param fontKey  native {@link Font} key
     * @param fallback {@link Supplier} for the fallback {@link FontUIResource}
     * @return native Windows {@link FontUIResource} for the {@link Font} key and {@link Font} size key
     * @see com.sun.java.swing.plaf.windows.WindowsLookAndFeel.WindowsFontProperty#configureValue(Object)
     */
    @NotNull
    @SuppressWarnings ( "JavadocReference" )
    private static FontUIResource getWindowsFont ( @NotNull final String fontKey, @NotNull final Supplier<FontUIResource> fallback )
    {
        Font font;

        // Determining font base
        if ( isUseNativeFonts () )
        {
            try
            {
                /**
                 * We won't retrieve native font size separately as it is commonly already included in the native {@link Font} itself.
                 * This is how it could be done though: {@code Toolkit.getDefaultToolkit ().getDesktopProperty ( fontKey + ".height" )}
                 */
                final Object nativeFont = Toolkit.getDefaultToolkit ().getDesktopProperty ( fontKey );
                if ( nativeFont != null )
                {
                    font = ( Font ) nativeFont;
                }
                else
                {
                    if ( warningsEnabled )
                    {
                        final String msg = "Unable to retrieve native Windows font: %s";
                        LoggerFactory.getLogger ( NativeFonts.class ).error ( String.format ( msg, fontKey ) );
                    }
                    font = fallback.get ();
                }
            }
            catch ( final Exception e )
            {
                if ( warningsEnabled )
                {
                    final String msg = "Unable to retrieve native Windows font: %s";
                    LoggerFactory.getLogger ( NativeFonts.class ).error ( String.format ( msg, fontKey ), e );
                }
                font = fallback.get ();
            }
        }
        else
        {
            font = fallback.get ();
        }

        // Switching to composite FontUIResource if needed
        if ( SystemUtils.isJava7orAbove () )
        {
            // Newer approach using sun.font.FontUtilities
            try
            {
                final Class fontUtilities = ReflectUtils.getClass ( "sun.font.FontUtilities" );
                try
                {
                    if ( !( Boolean ) ReflectUtils.callStaticMethod ( fontUtilities, "fontSupportsDefaultEncoding", font ) )
                    {
                        try
                        {
                            font = ReflectUtils.callStaticMethod ( fontUtilities, "getCompositeFontUIResource", font );
                        }
                        catch ( final Exception e )
                        {
                            if ( warningsEnabled )
                            {
                                final String msg = "Unable to retrieve composite Font";
                                LoggerFactory.getLogger ( NativeFonts.class ).error ( msg, e );
                            }
                        }
                    }
                }
                catch ( final Exception e )
                {
                    if ( warningsEnabled )
                    {
                        final String msg = "Unable to check Font default encoding support";
                        LoggerFactory.getLogger ( NativeFonts.class ).error ( msg, e );
                    }
                }
            }
            catch ( final ClassNotFoundException e )
            {
                if ( warningsEnabled )
                {
                    final String msg = "Unable to access sun.font.FontUtilities";
                    LoggerFactory.getLogger ( NativeFonts.class ).error ( msg, e );
                }
            }
        }
        else
        {
            // Older approach using sun.font.FontManager
            try
            {
                final Class fontManager = ReflectUtils.getClass ( "sun.font.FontManager" );
                try
                {
                    if ( !( Boolean ) ReflectUtils.callStaticMethod ( fontManager, "fontSupportsDefaultEncoding", font ) )
                    {
                        try
                        {
                            font = ReflectUtils.callStaticMethod ( fontManager, "getCompositeFontUIResource", font );
                        }
                        catch ( final Exception e )
                        {
                            if ( warningsEnabled )
                            {
                                final String msg = "Unable to retrieve composite Font";
                                LoggerFactory.getLogger ( NativeFonts.class ).error ( msg, e );
                            }
                        }
                    }
                }
                catch ( final Exception e )
                {
                    if ( warningsEnabled )
                    {
                        final String msg = "Unable to check Font default encoding support";
                        LoggerFactory.getLogger ( NativeFonts.class ).error ( msg, e );
                    }
                }
            }
            catch ( final ClassNotFoundException e )
            {
                if ( warningsEnabled )
                {
                    final String msg = "Unable to access sun.font.FontManager";
                    LoggerFactory.getLogger ( NativeFonts.class ).error ( msg, e );
                }
            }
        }

        // Enclosing font in FontUIResource if needed
        final FontUIResource fontUIResource;
        if ( font instanceof FontUIResource )
        {
            fontUIResource = ( FontUIResource ) font;
        }
        else
        {
            fontUIResource = new FontUIResource ( font );
        }
        return fontUIResource;
    }

    /**
     * Returns native Mac OS X {@link FontUIResource} using the specified retrieval method name.
     *
     * @param fontMethodName native {@link Font} retrieval method name
     * @param fallback       {@link Supplier} for the fallback {@link FontUIResource}
     * @return native Mac OS X {@link FontUIResource} using the specified retrieval method name
     */
    @NotNull
    private static FontUIResource getMacOSFont ( @NotNull final String fontMethodName, @NotNull final Supplier<FontUIResource> fallback )
    {
        Font font;
        if ( isUseNativeFonts () )
        {
            try
            {
                final Object nativeFont = ReflectUtils.callStaticMethod ( "com.apple.laf.AquaFonts", fontMethodName );
                if ( nativeFont != null )
                {
                    font = ( Font ) nativeFont;
                }
                else
                {
                    if ( warningsEnabled )
                    {
                        final String msg = "Unable to retrieve native Mac OS X font using method: com.apple.laf.AquaFonts.%s()";
                        LoggerFactory.getLogger ( NativeFonts.class ).error ( String.format ( msg, fontMethodName ) );
                    }
                    font = fallback.get ();
                }
            }
            catch ( final Exception e )
            {
                if ( warningsEnabled )
                {
                    final String msg = "Unable to retrieve native Mac OS X font using method: com.apple.laf.AquaFonts.%s()";
                    LoggerFactory.getLogger ( NativeFonts.class ).error ( String.format ( msg, fontMethodName ), e );
                }
                font = fallback.get ();
            }
        }
        else
        {
            font = fallback.get ();
        }

        // Enclosing font in FontUIResource if needed
        final FontUIResource fontUIResource;
        if ( font instanceof FontUIResource )
        {
            fontUIResource = ( FontUIResource ) font;
        }
        else
        {
            fontUIResource = new FontUIResource ( font );
        }
        return fontUIResource;
    }

    /**
     * Returns native Unix {@link FontUIResource} using the specified retrieval method name.
     *
     * @param region   {@link Region} to retrieve font for
     * @param fallback {@link Supplier} for the fallback {@link FontUIResource}
     * @return native Unix {@link FontUIResource} using the specified retrieval method name
     */
    @NotNull
    private static FontUIResource getUnixFont ( @NotNull final Region region, @NotNull final Supplier<FontUIResource> fallback )
    {
        Font font = null;
        if ( isUseNativeFonts () && isGTKAvailable () )
        {
            final Object factory = getGTKStyleFactory ();
            if ( factory != null )
            {
                try
                {
                    final Object style = ReflectUtils.callMethod ( factory, "getStyle", null, region );
                    if ( style != null )
                    {
                        try
                        {
                            if ( SystemUtils.isJava9orAbove () || SystemUtils.isJavaVersionOrAbove ( 1.8, 211 ) )
                            {
                                font = ReflectUtils.callMethod ( style, "getDefaultFont" );
                            }
                            else if ( SystemUtils.isJava7orAbove () )
                            {
                                final Object[] args = { null };
                                font = ReflectUtils.callMethod ( style, "getFontForState", args );
                            }
                            else
                            {
                                final Object[] args = { null, region, SynthConstants.ENABLED };
                                font = ReflectUtils.callMethod ( style, "getFontForState", args );
                            }
                        }
                        catch ( final Exception e )
                        {
                            final String msg = "Unable to retrieve region Font: %s";
                            LoggerFactory.getLogger ( NativeFonts.class ).error ( String.format ( msg, region ) );
                        }
                    }
                }
                catch ( final Exception e )
                {
                    final String msg = "Unable to retrieve region style: %s";
                    LoggerFactory.getLogger ( NativeFonts.class ).error ( String.format ( msg, region ) );
                }
            }
        }
        if ( font == null )
        {
            font = fallback.get ();
        }

        // Enclosing font in FontUIResource if needed
        final FontUIResource fontUIResource;
        if ( font instanceof FontUIResource )
        {
            fontUIResource = ( FontUIResource ) font;
        }
        else
        {
            fontUIResource = new FontUIResource ( font );
        }
        return fontUIResource;
    }

    /**
     * Returns whether or not GTK is available.
     *
     * @return {@code true} if GTK is available, {@code false} otherwise
     */
    private static boolean isGTKAvailable ()
    {
        if ( gtkAvailable == null )
        {
            synchronized ( NativeFonts.class )
            {
                if ( gtkAvailable == null )
                {
                    if ( SystemUtils.isJava7orAbove () )
                    {
                        try
                        {
                            gtkAvailable = ReflectUtils.callMethod ( Toolkit.getDefaultToolkit (), "isNativeGTKAvailable" );
                            if ( gtkAvailable )
                            {
                                try
                                {
                                    gtkAvailable = ReflectUtils.callMethod ( Toolkit.getDefaultToolkit (), "loadGTK" );
                                }
                                catch ( final Exception e )
                                {
                                    final String msg = "Unable to load GTK libraries";
                                    LoggerFactory.getLogger ( NativeFonts.class ).error ( msg );
                                    gtkAvailable = false;
                                }
                            }
                        }
                        catch ( final Exception e )
                        {
                            final String msg = "Unable to check GTK availability";
                            LoggerFactory.getLogger ( NativeFonts.class ).error ( msg );
                            gtkAvailable = false;
                        }
                    }
                    else
                    {
                        try
                        {
                            gtkAvailable = ReflectUtils.callMethod ( Toolkit.getDefaultToolkit (), "checkGTK" );
                        }
                        catch ( final Exception e )
                        {
                            final String msg = "Unable to check GTK availability";
                            LoggerFactory.getLogger ( NativeFonts.class ).error ( msg );
                            gtkAvailable = false;
                        }
                    }
                }
            }
        }
        return gtkAvailable;
    }

    /**
     * Returns {@code com.sun.java.swing.plaf.gtk.GTKStyleFactory} instance or {@code null} if it cannot be instantiated.
     * Note that this method will only attempt {@code com.sun.java.swing.plaf.gtk.GTKStyleFactory} instantiation once and will cache result.
     *
     * @return {@code com.sun.java.swing.plaf.gtk.GTKStyleFactory} instance or {@code null} if it cannot be instantiated
     */
    @Nullable
    private static Object getGTKStyleFactory ()
    {
        if ( gtkStyleFactoryAvailable == null )
        {
            synchronized ( NativeFonts.class )
            {
                if ( gtkStyleFactoryAvailable == null )
                {
                    try
                    {
                        gtkStyleFactory = ReflectUtils.createInstance ( "com.sun.java.swing.plaf.gtk.GTKStyleFactory" );
                        final Font defaultGTKFont = getDefaultGTKFont ();
                        if ( defaultGTKFont != null )
                        {
                            try
                            {
                                ReflectUtils.callMethod ( gtkStyleFactory, "initStyles", defaultGTKFont );
                                gtkStyleFactoryAvailable = true;
                            }
                            catch ( final Exception e )
                            {
                                final String msg = "Unable to initialize GTKStyleFactory styles";
                                LoggerFactory.getLogger ( NativeFonts.class ).error ( msg );
                                gtkStyleFactory = null;
                                gtkStyleFactoryAvailable = false;
                            }
                        }
                        else
                        {
                            gtkStyleFactory = null;
                            gtkStyleFactoryAvailable = false;
                        }
                    }
                    catch ( final Exception e )
                    {
                        final String msg = "Unable to instantiate GTK style factory";
                        LoggerFactory.getLogger ( NativeFonts.class ).error ( msg );
                        gtkStyleFactory = null;
                        gtkStyleFactoryAvailable = false;
                    }
                }
            }
        }
        return gtkStyleFactory;
    }

    /**
     * Returns default GTK {@link Font} or {@code null} if it cannot be instantiated..
     *
     * @return default GTK {@link Font} or {@code null} if it cannot be instantiated.
     */
    @Nullable
    private static Font getDefaultGTKFont ()
    {
        if ( defaultGTKFontAvailable == null )
        {
            synchronized ( NativeFonts.class )
            {
                if ( defaultGTKFontAvailable == null )
                {
                    try
                    {
                        // Desktop property appears to have preference over rc font
                        Object fontName = Toolkit.getDefaultToolkit ().getDesktopProperty ( "gnome.Gtk/FontName" );
                        if ( !( fontName instanceof String ) )
                        {
                            final Class ec = ReflectUtils.getClass ( "com.sun.java.swing.plaf.gtk.GTKEngine" );
                            final Object engine = ReflectUtils.getStaticFieldValue ( ec, "INSTANCE" );
                            final Class settings = ReflectUtils.getInnerClass ( ec, "Settings" );
                            final Object fontKey = ReflectUtils.getStaticFieldValue ( settings, "GTK_FONT_NAME" );
                            fontName = ReflectUtils.callMethod ( engine, "getSetting", fontKey );
                            if ( !( fontName instanceof String ) )
                            {
                                fontName = "SansSerif 12";
                            }
                        }
                        defaultGTKFont = ReflectUtils.callStaticMethod ( "com.sun.java.swing.plaf.gtk.PangoFonts",
                                "lookupFont", fontName );
                        defaultGTKFontAvailable = true;
                    }
                    catch ( final Exception e )
                    {
                        final String msg = "Unable retrieve default GTK font";
                        LoggerFactory.getLogger ( NativeFonts.class ).error ( msg );
                        defaultGTKFont = null;
                        defaultGTKFontAvailable = false;
                    }
                }
            }
        }
        return defaultGTKFont;
    }
}