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

import java.awt.event.FocusAdapter;
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
     * Shortcut method for mouse press event.
     *
     * @param runnable mouse event runnable
     * @return used mouse adapter
     */
    public MouseAdapter onMousePress ( MouseEventRunnable runnable );

    /**
     * Shortcut method for mouse press event.
     *
     * @param mouseButton mouse button filter
     * @param runnable    mouse event runnable
     * @return used mouse adapter
     */
    public MouseAdapter onMousePress ( MouseButton mouseButton, MouseEventRunnable runnable );

    /**
     * Shortcut method for mouse enter event.
     *
     * @param runnable mouse event runnable
     * @return used mouse adapter
     */
    public MouseAdapter onMouseEnter ( MouseEventRunnable runnable );

    /**
     * Shortcut method for mouse exit event.
     *
     * @param runnable mouse event runnable
     * @return used mouse adapter
     */
    public MouseAdapter onMouseExit ( MouseEventRunnable runnable );

    /**
     * Shortcut method for mouse drag event.
     *
     * @param runnable mouse event runnable
     * @return used mouse adapter
     */
    public MouseAdapter onMouseDrag ( MouseEventRunnable runnable );

    /**
     * Shortcut method for mouse drag event.
     *
     * @param mouseButton mouse button filter
     * @param runnable    mouse event runnable
     * @return used mouse adapter
     */
    public MouseAdapter onMouseDrag ( MouseButton mouseButton, MouseEventRunnable runnable );

    /**
     * Shortcut method for mouse click event.
     *
     * @param runnable mouse event runnable
     * @return used mouse adapter
     */
    public MouseAdapter onMouseClick ( MouseEventRunnable runnable );

    /**
     * Shortcut method for mouse click event.
     *
     * @param mouseButton mouse button filter
     * @param runnable    mouse event runnable
     * @return used mouse adapter
     */
    public MouseAdapter onMouseClick ( MouseButton mouseButton, MouseEventRunnable runnable );

    /**
     * Shortcut method for double-click mouse event.
     *
     * @param runnable mouse event runnable
     * @return used mouse adapter
     */
    public MouseAdapter onDoubleClick ( MouseEventRunnable runnable );

    /**
     * Shortcut method for mouse event triggering popup menu.
     *
     * @param runnable mouse event runnable
     * @return used mouse adapter
     */
    public MouseAdapter onMenuTrigger ( MouseEventRunnable runnable );

    /**
     * Shortcut method for key type event.
     *
     * @param runnable key event runnable
     * @return used key adapter
     */
    public KeyAdapter onKeyType ( KeyEventRunnable runnable );

    /**
     * Shortcut method for key type event.
     *
     * @param hotkey   hotkey filter
     * @param runnable key event runnable
     * @return used key adapter
     */
    public KeyAdapter onKeyType ( HotkeyData hotkey, KeyEventRunnable runnable );

    /**
     * Shortcut method for key press event.
     *
     * @param runnable key event runnable
     * @return used key adapter
     */
    public KeyAdapter onKeyPress ( KeyEventRunnable runnable );

    /**
     * Shortcut method for key press event.
     *
     * @param hotkey   hotkey filter
     * @param runnable key event runnable
     * @return used key adapter
     */
    public KeyAdapter onKeyPress ( HotkeyData hotkey, KeyEventRunnable runnable );

    /**
     * Shortcut method for key release event.
     *
     * @param runnable key event runnable
     * @return used key adapter
     */
    public KeyAdapter onKeyRelease ( KeyEventRunnable runnable );

    /**
     * Shortcut method for key release event.
     *
     * @param hotkey   hotkey filter
     * @param runnable key event runnable
     * @return used key adapter
     */
    public KeyAdapter onKeyRelease ( HotkeyData hotkey, KeyEventRunnable runnable );

    /**
     * Shortcut method for focus gain event.
     *
     * @param runnable focus event runnable
     * @return used focus adapter
     */
    public FocusAdapter onFocusGain ( FocusEventRunnable runnable );

    /**
     * Shortcut method for focus loss event.
     *
     * @param runnable focus event runnable
     * @return used focus adapter
     */
    public FocusAdapter onFocusLoss ( FocusEventRunnable runnable );
}