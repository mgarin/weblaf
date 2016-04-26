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

import java.awt.event.WindowAdapter;

/**
 * This interface provides a set of methods that should be added into windows that supports custom WebLaF events.
 * Basically all these methods are already implemented in EventUtils but it is much easier to call them directly from window.
 *
 * @author Mikle Garin
 * @see com.alee.utils.swing.extensions.MethodExtension
 * @see WindowEventMethodsImpl
 */

public interface WindowEventMethods extends MethodExtension
{
    /**
     * Shortcut method for window closing event.
     *
     * @param runnable window event runnable
     * @return used window adapter
     */
    public WindowAdapter onClosing ( WindowEventRunnable runnable );

    /**
     * Shortcut method for window close event.
     *
     * @param runnable component event runnable
     * @return used window close adapter
     */
    public WindowCloseAdapter onClose ( ComponentEventRunnable runnable );
}