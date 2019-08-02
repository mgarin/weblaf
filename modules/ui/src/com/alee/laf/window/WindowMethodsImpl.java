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

package com.alee.laf.window;

import com.alee.utils.ProprietaryUtils;

import java.awt.*;

/**
 * Common implementations for {@link com.alee.laf.window.WindowMethods} interface methods.
 *
 * @author Mikle Garin
 * @see com.alee.laf.window.WindowMethods
 */
public final class WindowMethodsImpl
{
    /**
     * Returns whether window is opaque or not.
     *
     * @param window window to process
     * @param <W>    provided window type
     * @return whether window background is opaque or not
     */
    public static <W extends Window> boolean isWindowOpaque ( final W window )
    {
        return ProprietaryUtils.isWindowOpaque ( window );
    }

    /**
     * Sets window opaque if that option is supported by the underlying system.
     *
     * @param window window to modify
     * @param opaque whether should make window opaque or not
     * @param <W>    provided window type
     * @param <T>    actual window type
     * @return processed window
     */
    public static <W extends Window, T extends W> T setWindowOpaque ( final W window, final boolean opaque )
    {
        ProprietaryUtils.setWindowOpaque ( window, opaque );
        return ( T ) window;
    }

    /**
     * Returns window opacity.
     *
     * @param window window to process
     * @param <W>    provided window type
     * @return window opacity
     */
    public static <W extends Window> float getWindowOpacity ( final W window )
    {
        return ProprietaryUtils.getWindowOpacity ( window );
    }

    /**
     * Sets window opacity if that option is supported by the underlying system.
     *
     * @param window  window to modify
     * @param opacity new window opacity
     * @param <W>     provided window type
     * @param <T>     actual window type
     * @return processed window
     */
    public static <W extends Window, T extends W> T setWindowOpacity ( final W window, final float opacity )
    {
        ProprietaryUtils.setWindowOpacity ( window, opacity );
        return ( T ) window;
    }

    /**
     * Centers window relative to screen center.
     *
     * @param window window to process
     * @param <W>    provided window type
     * @param <T>    actual window type
     * @return processed window
     */
    public static <W extends Window, T extends W> T center ( final W window )
    {
        window.setLocationRelativeTo ( null );
        return ( T ) window;
    }

    /**
     * Centers window relative to the specified component center.
     *
     * @param window     window to process
     * @param relativeTo component used to center window
     * @param <W>        provided window type
     * @param <T>        actual window type
     * @return processed window
     */
    public static <W extends Window, T extends W> T center ( final W window, final Component relativeTo )
    {
        window.setLocationRelativeTo ( relativeTo );
        return ( T ) window;
    }

    /**
     * Changes window size and centers it relative to screen center.
     *
     * @param window window to process
     * @param width  new window width
     * @param height new window height
     * @param <W>    provided window type
     * @param <T>    actual window type
     * @return processed window
     */
    public static <W extends Window, T extends W> T center ( final W window, final int width, final int height )
    {
        window.setSize ( width, height );
        window.setLocationRelativeTo ( null );
        return ( T ) window;
    }

    /**
     * Changes window size and centers it relative to screen center.
     *
     * @param window     window to process
     * @param relativeTo component used to center window
     * @param width      new window width
     * @param height     new window height
     * @param <W>        provided window type
     * @param <T>        actual window type
     * @return processed window
     */
    public static <W extends Window, T extends W> T center ( final W window, final Component relativeTo, final int width, final int height )
    {
        window.setSize ( width, height );
        window.setLocationRelativeTo ( relativeTo );
        return ( T ) window;
    }

    /**
     * Packs window to its preffered height and specified width.
     *
     * @param window window to process
     * @param width  new window width
     * @param <W>    provided window type
     * @param <T>    actual window type
     * @return processed window
     */
    public static <W extends Window, T extends W> T packToWidth ( final W window, final int width )
    {
        window.setSize ( width, window.getPreferredSize ().height );
        return ( T ) window;
    }

    /**
     * Packs window to its preffered width and specified height.
     *
     * @param window window to process
     * @param height new window height
     * @param <W>    provided window type
     * @param <T>    actual window type
     * @return processed window
     */
    public static <W extends Window, T extends W> T packToHeight ( final W window, final int height )
    {
        window.setSize ( window.getPreferredSize ().width, height );
        return ( T ) window;
    }
}