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

package com.alee.extended.window;

import com.alee.utils.swing.extensions.MethodExtension;

/**
 * This interface provides a set of methods that should be added into popover that supports custom WebLaF events.
 *
 * @author Mikle Garin
 * @see com.alee.utils.swing.extensions.MethodExtension
 * @see com.alee.extended.window.PopOverEventMethodsImpl
 */
public interface PopOverEventMethods extends MethodExtension
{
    /**
     * Shortcut method for popover open event.
     *
     * @param runnable popover event runnable
     * @return used popover adapter
     */
    public PopOverAdapter onOpen ( PopOverEventRunnable runnable );

    /**
     * Shortcut method for popover reopen event.
     *
     * @param runnable popover event runnable
     * @return used popover adapter
     */
    public PopOverAdapter onReopen ( PopOverEventRunnable runnable );

    /**
     * Shortcut method for popover detach event.
     *
     * @param runnable popover event runnable
     * @return used popover adapter
     */
    public PopOverAdapter onDetach ( PopOverEventRunnable runnable );

    /**
     * Shortcut method for popover close event.
     *
     * @param runnable popover event runnable
     * @return used popover adapter
     */
    public PopOverAdapter onClose ( PopOverEventRunnable runnable );
}