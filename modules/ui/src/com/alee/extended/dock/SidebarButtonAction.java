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

package com.alee.extended.dock;

/**
 * Enumeration representing possible frame sidebar button actions.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-WebDockablePane">How to use WebDockablePane</a>
 */
public enum SidebarButtonAction
{
    /**
     * Restore previous frame state on sidebar button action, it might be either
     * {@link com.alee.extended.dock.DockableFrameState#docked} or {@link com.alee.extended.dock.DockableFrameState#floating} state.
     *
     * @see com.alee.extended.dock.DockableFrameState#docked
     * @see com.alee.extended.dock.DockableFrameState#floating
     */
    restore,

    /**
     * Display frame preview on sidebar button action.
     *
     * @see com.alee.extended.dock.DockableFrameState#preview
     */
    preview,

    /**
     * Dock frame on sidebar button action.
     *
     * @see com.alee.extended.dock.DockableFrameState#docked
     */
    dock,

    /**
     * Detach frame on sidebar button action.
     *
     * @see com.alee.extended.dock.DockableFrameState#floating
     */
    detach
}