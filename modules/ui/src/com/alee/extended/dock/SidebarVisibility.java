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
 * Various visibility settings for {@link com.alee.extended.dock.WebDockablePane} sidebar buttons.
 *
 * @author Mikle Garin
 */

public enum SidebarVisibility
{
    /**
     * Do not ever display sidebar buttons.
     * This will simply hide all sidebar buttons.
     */
    none,

    /**
     * Display sidebar buttons for frames in minimized and preview states.
     * Docked or floating frames will not have any sidebar button available.
     * This is also default setting used by {@link com.alee.extended.dock.WebDockablePane}.
     */
    minimized,

    /**
     * Display sidebar buttons for frames in any state added into dockable pane.
     * This is the way some applications like IntelliJ IDEA handle sidebar buttons.
     * It might be useful to keep the whole layout more static if hiding/docking frames is a common operation.
     */
    all
}