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

package com.alee.utils.swing;

import com.alee.managers.hotkey.HotkeyData;

import java.awt.event.KeyAdapter;
import java.awt.event.MouseAdapter;

/**
 * This interface provides a set of methods that should be added into components that supports custom WebLaF events.
 * Basically all these methods are already implemented in EventUtils but it is much easier to call them directly from component.
 *
 * @author Mikle Garin
 * @see com.alee.utils.EventUtils
 */

public interface EventMethods extends SwingMethods
{
    /**
     * Shortcut method for double-click mouse event.
     *
     * @param runnable mouse event runnable
     */
    public MouseAdapter onDoubleClick ( final MouseEventRunnable runnable );

    /**
     * Shortcut method for key press event.
     *
     * @param runnable key event runnable
     */
    public KeyAdapter onKeyPress ( final KeyEventRunnable runnable );

    /**
     * Shortcut method for key press event.
     *
     * @param hotkey   hotkey filter
     * @param runnable key event runnable
     */
    public KeyAdapter onKeyPress ( final HotkeyData hotkey, final KeyEventRunnable runnable );
}