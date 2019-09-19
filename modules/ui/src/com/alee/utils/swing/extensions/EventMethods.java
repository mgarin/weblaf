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

package com.alee.utils.swing.extensions;

import com.alee.api.annotations.NotNull;
import com.alee.api.annotations.Nullable;
import com.alee.managers.hotkey.HotkeyData;
import com.alee.utils.swing.MouseButton;

import java.awt.event.FocusAdapter;
import java.awt.event.KeyAdapter;
import java.awt.event.MouseAdapter;

/**
 * This interface provides a set of methods that should be added into components that supports custom WebLaF events.
 *
 * @author Mikle Garin
 * @see com.alee.utils.swing.extensions.MethodExtension
 * @see com.alee.utils.swing.extensions.EventMethodsImpl
 */
public interface EventMethods extends MethodExtension
{
    /**
     * Shortcut method for mouse press event.
     *
     * @param runnable mouse event runnable
     * @return created {@link MouseAdapter}
     */
    @NotNull
    public MouseAdapter onMousePress ( @NotNull MouseEventRunnable runnable );

    /**
     * Shortcut method for mouse press event.
     *
     * @param mouseButton mouse button filter
     * @param runnable    mouse event runnable
     * @return created {@link MouseAdapter}
     */
    @NotNull
    public MouseAdapter onMousePress ( @Nullable MouseButton mouseButton, @NotNull MouseEventRunnable runnable );

    /**
     * Shortcut method for mouse enter event.
     *
     * @param runnable mouse event runnable
     * @return created {@link MouseAdapter}
     */
    @NotNull
    public MouseAdapter onMouseEnter ( @NotNull MouseEventRunnable runnable );

    /**
     * Shortcut method for mouse exit event.
     *
     * @param runnable mouse event runnable
     * @return created {@link MouseAdapter}
     */
    @NotNull
    public MouseAdapter onMouseExit ( @NotNull MouseEventRunnable runnable );

    /**
     * Shortcut method for mouse drag event.
     *
     * @param runnable mouse event runnable
     * @return created {@link MouseAdapter}
     */
    @NotNull
    public MouseAdapter onMouseDrag ( @NotNull MouseEventRunnable runnable );

    /**
     * Shortcut method for mouse drag event.
     *
     * @param mouseButton mouse button filter
     * @param runnable    mouse event runnable
     * @return created {@link MouseAdapter}
     */
    @NotNull
    public MouseAdapter onMouseDrag ( @Nullable MouseButton mouseButton, @NotNull MouseEventRunnable runnable );

    /**
     * Shortcut method for mouse click event.
     *
     * @param runnable mouse event runnable
     * @return created {@link MouseAdapter}
     */
    @NotNull
    public MouseAdapter onMouseClick ( @NotNull MouseEventRunnable runnable );

    /**
     * Shortcut method for mouse click event.
     *
     * @param mouseButton mouse button filter
     * @param runnable    mouse event runnable
     * @return created {@link MouseAdapter}
     */
    @NotNull
    public MouseAdapter onMouseClick ( @Nullable MouseButton mouseButton, @NotNull MouseEventRunnable runnable );

    /**
     * Shortcut method for double-click mouse event.
     *
     * @param runnable mouse event runnable
     * @return created {@link MouseAdapter}
     */
    @NotNull
    public MouseAdapter onDoubleClick ( @NotNull MouseEventRunnable runnable );

    /**
     * Shortcut method for mouse event triggering popup menu.
     *
     * @param runnable mouse event runnable
     * @return created {@link MouseAdapter}
     */
    @NotNull
    public MouseAdapter onMenuTrigger ( @NotNull MouseEventRunnable runnable );

    /**
     * Shortcut method for key type event.
     *
     * @param runnable key event runnable
     * @return created {@link KeyAdapter}
     */
    @NotNull
    public KeyAdapter onKeyType ( @NotNull KeyEventRunnable runnable );

    /**
     * Shortcut method for key type event.
     *
     * @param hotkey   hotkey filter
     * @param runnable key event runnable
     * @return created {@link KeyAdapter}
     */
    @NotNull
    public KeyAdapter onKeyType ( @Nullable HotkeyData hotkey, @NotNull KeyEventRunnable runnable );

    /**
     * Shortcut method for key press event.
     *
     * @param runnable key event runnable
     * @return created {@link KeyAdapter}
     */
    @NotNull
    public KeyAdapter onKeyPress ( @NotNull KeyEventRunnable runnable );

    /**
     * Shortcut method for key press event.
     *
     * @param hotkey   hotkey filter
     * @param runnable key event runnable
     * @return created {@link KeyAdapter}
     */
    @NotNull
    public KeyAdapter onKeyPress ( @Nullable HotkeyData hotkey, @NotNull KeyEventRunnable runnable );

    /**
     * Shortcut method for key release event.
     *
     * @param runnable key event runnable
     * @return created {@link KeyAdapter}
     */
    @NotNull
    public KeyAdapter onKeyRelease ( @NotNull KeyEventRunnable runnable );

    /**
     * Shortcut method for key release event.
     *
     * @param hotkey   hotkey filter
     * @param runnable key event runnable
     * @return created {@link KeyAdapter}
     */
    @NotNull
    public KeyAdapter onKeyRelease ( @Nullable HotkeyData hotkey, @NotNull KeyEventRunnable runnable );

    /**
     * Shortcut method for focus gain event.
     *
     * @param runnable focus event runnable
     * @return created {@link FocusAdapter}
     */
    @NotNull
    public FocusAdapter onFocusGain ( @NotNull FocusEventRunnable runnable );

    /**
     * Shortcut method for focus loss event.
     *
     * @param runnable focus event runnable
     * @return created {@link FocusAdapter}
     */
    @NotNull
    public FocusAdapter onFocusLoss ( @NotNull FocusEventRunnable runnable );

    /**
     * Shortcut method for drag start.
     * This is a special event that requires a sequence of conditions to be triggered.
     * This event will also be only triggered once per drag operation.
     *
     * @param shift    coordinate shift required to start drag
     * @param runnable mouse event runnable
     * @return created {@link MouseAdapter}
     */
    @NotNull
    public MouseAdapter onDragStart ( int shift, @NotNull MouseEventRunnable runnable );

    /**
     * Shortcut method for drag start.
     * This is a special event that requires a sequence of conditions to be triggered.
     * This event will also be only triggered once per drag operation.
     *
     * @param shift       coordinate shift required to start drag
     * @param mouseButton mouse button filter
     * @param runnable    mouse event runnable
     * @return created {@link MouseAdapter}
     */
    @NotNull
    public MouseAdapter onDragStart ( int shift, @Nullable MouseButton mouseButton, @NotNull MouseEventRunnable runnable );
}