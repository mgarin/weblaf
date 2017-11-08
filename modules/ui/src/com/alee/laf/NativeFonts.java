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

import com.alee.api.jdk.Supplier;
import com.alee.managers.log.Log;
import com.alee.utils.DebugUtils;
import com.alee.utils.ReflectUtils;
import com.alee.utils.SystemUtils;

import javax.swing.plaf.FontUIResource;
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
     * todo 1. Should only be working as a fallback for a more convenient style-related implementation instead of default implementation
     */

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
    public static FontUIResource get ( final ControlType control )
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
     */
    private static FontUIResource getWindowsFont ( final String fontKey, final Supplier<FontUIResource> fallback )
    {
        FontUIResource font;
        if ( isUseNativeFonts () )
        {
            try
            {
                /**
                 * We won't retrieve native font size separately as it is commonly already included in the native {@link Font} itself.
                 * This is how it could be done though: {@code Toolkit.getDefaultToolkit ().getDesktopProperty ( fontKey + ".height" )}
                 */
                final Object nativeFont = Toolkit.getDefaultToolkit ().getDesktopProperty ( fontKey );
                if ( nativeFont instanceof Font )
                {
                    font = new FontUIResource ( ( Font ) nativeFont );
                }
                else
                {
                    font = ( FontUIResource ) nativeFont;
                }
            }
            catch ( final Exception e )
            {
                if ( DebugUtils.isGlobalDebugEnabled () )
                {
                    Log.get ().error ( "Unable to retrieve native Windows font: " + fontKey, e );
                }
                font = fallback.get ();
            }
        }
        else
        {
            font = fallback.get ();
        }
        return font;
    }

    /**
     * Returns native Mac OS X {@link FontUIResource} using the specified retrieval method name.
     *
     * @param fontMethodName native {@link Font} retrieval method name
     * @param fallback       {@link Supplier} for the fallback {@link FontUIResource}
     * @return native Mac OS X {@link FontUIResource} using the specified retrieval method name
     */
    private static FontUIResource getMacOSFont ( final String fontMethodName, final Supplier<FontUIResource> fallback )
    {
        FontUIResource font;
        if ( isUseNativeFonts () )
        {
            try
            {
                final Object object = ReflectUtils.callStaticMethod ( "com.apple.laf.AquaFonts", fontMethodName );
                if ( object instanceof Font )
                {
                    font = new FontUIResource ( ( Font ) object );
                }
                else
                {
                    font = ( FontUIResource ) object;
                }

            }
            catch ( final Exception e )
            {
                if ( DebugUtils.isGlobalDebugEnabled () )
                {
                    final String msg = "Unable to retrieve native Mac OS X font using method: com.apple.laf.AquaFonts.%s()";
                    Log.get ().error ( String.format ( msg, fontMethodName ), e );
                }
                font = fallback.get ();
            }
        }
        else
        {
            font = fallback.get ();
        }
        return font;
    }
}