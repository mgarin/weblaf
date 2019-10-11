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
 * {@link SidebarButton}s visibility condition for {@link WebDockablePane}.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-WebDockablePane">How to use WebDockablePane</a>
 */
public enum SidebarButtonVisibility
{
    /**
     * Do not display sidebar buttons.
     * This will simply hide all sidebar buttons at all times.
     */
    never,

    /**
     * Display sidebar buttons for frames in {@link DockableFrameState#minimized} and {@link DockableFrameState#preview} states.
     * Frames in {@link DockableFrameState#docked} or {@link DockableFrameState#floating} states will not have sidebar buttons available.
     * This is also default setting used by {@link WebDockablePane}.
     */
    minimized,

    /**
     * Display buttons for all frames on the sidebar if at least one of the frames is in {@link DockableFrameState#minimized} or
     * {@link DockableFrameState#preview} states. Sidebar will be hidden when there are no frames in those two states present.
     * This is a handy option which uses more versatile sidebar display approach unlike {@link #minimized} visibility.
     */
    anyMinimized,

    /**
     * Display sidebar buttons for frames in any state added into dockable pane.
     * This is the way some applications like IntelliJ IDEA handle sidebar buttons - they are always visible for all opened frames.
     * It might be useful to keep the whole layout more static if hiding/docking frames is a common operation.
     */
    always
}