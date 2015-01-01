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

import com.alee.utils.swing.SwingMethods;

/**
 * This interface provides a set of methods that should be added into popover that supports custom WebLaF events.
 * Basically all these methods are already implemented in EventUtils but it is much easier to call them directly from popover.
 *
 * @author Mikle Garin
 */

public interface PopOverEventMethods extends SwingMethods
{
    /**
     * Shortcut method for popover open event.
     *
     * @param runnable popover event runnable
     * @return used popover adapter
     */
    public PopOverAdapter onOpen ( final PopOverEventRunnable runnable );

    /**
     * Shortcut method for popover reopen event.
     *
     * @param runnable popover event runnable
     * @return used popover adapter
     */
    public PopOverAdapter onReopen ( final PopOverEventRunnable runnable );

    /**
     * Shortcut method for popover detach event.
     *
     * @param runnable popover event runnable
     * @return used popover adapter
     */
    public PopOverAdapter onDetach ( final PopOverEventRunnable runnable );

    /**
     * Shortcut method for popover close event.
     *
     * @param runnable popover event runnable
     * @return used popover adapter
     */
    public PopOverAdapter onClose ( final PopOverEventRunnable runnable );
}