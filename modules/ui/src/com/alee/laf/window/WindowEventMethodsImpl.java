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

import com.alee.utils.swing.extensions.ComponentEventRunnable;
import com.alee.utils.swing.extensions.MethodExtension;
import com.alee.utils.swing.extensions.WindowCloseAdapter;

import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Common implementations for {@link com.alee.laf.window.WindowEventMethods} interface methods.
 *
 * @author Mikle Garin
 * @see com.alee.laf.window.WindowEventMethods
 */
public final class WindowEventMethodsImpl implements MethodExtension
{
    /**
     * Shortcut method for window closing event.
     *
     * @param window   window to handle events for
     * @param runnable window event runnable
     * @return used window adapter
     */
    public static WindowAdapter onClosing ( final Window window, final WindowEventRunnable runnable )
    {
        final WindowAdapter windowAdapter = new WindowAdapter ()
        {
            @Override
            public void windowClosing ( final WindowEvent e )
            {
                runnable.run ( e );
            }
        };
        window.addWindowListener ( windowAdapter );
        return windowAdapter;
    }

    /**
     * Shortcut method for window close event.
     *
     * @param window   window to handle events for
     * @param runnable component event runnable
     * @return used window close adapter
     */
    public static WindowCloseAdapter onClose ( final Window window, final ComponentEventRunnable runnable )
    {
        final WindowCloseAdapter windowAdapter = new WindowCloseAdapter ()
        {
            @Override
            public void closed ( final ComponentEvent e )
            {
                runnable.run ( e );
            }
        };
        window.addWindowListener ( windowAdapter );
        window.addComponentListener ( windowAdapter );
        return windowAdapter;
    }
}