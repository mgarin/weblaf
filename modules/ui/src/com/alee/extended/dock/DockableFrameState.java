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
 * Enumeration representing possible {@link com.alee.extended.dock.WebDockableFrame} component states.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-WebDockablePane">How to use WebDockablePane</a>
 */
public enum DockableFrameState
{
    /**
     * Frame is not added into dockable pane.
     * It can be switched to either {@link #minimized}, {@link #docked} or {@link #floating} modes using UI.
     */
    closed,

    /**
     * Frame is added into dockable pane but minimized, only its sidebar button is visible.
     * It can be switched to either {@link #preview} or {@link #docked} modes using UI.
     */
    minimized,

    /**
     * Frame is visible on the screen in preview mode.
     * This state is applied when frame sidebar button was hovered for a while.
     * It can be switched to either {@link #minimized} or {@link #docked} modes using UI.
     */
    preview,

    /**
     * Frame is visible inside the dockable frame.
     * It can be switched to either {@link #minimized}, {@link #floating} or {@link #closed} modes using UI.
     */
    docked,

    /**
     * Frame is detached from dockable pane into separate dialog following window containing that dockable pane.
     * It can be switched to either {@link #minimized}, {@link #docked} or {@link #closed} modes using UI.
     */
    floating
}