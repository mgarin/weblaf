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

package com.alee.extended.dock.drag;

import com.alee.api.Identifiable;

import java.io.Serializable;

/**
 * Dragged {@link com.alee.extended.dock.WebDockableFrame} data.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-WebDockablePane">How to use WebDockablePane</a>
 * @see com.alee.extended.dock.WebDockableFrame
 * @see com.alee.extended.dock.WebDockablePane
 * @see com.alee.managers.drag.DragManager
 */
public class FrameDragData implements Identifiable, Serializable
{
    /**
     * Dragged frame ID.
     */
    protected final String id;

    /**
     * Constructs new frame drag operation data.
     *
     * @param id dragged frame ID
     */
    public FrameDragData ( final String id )
    {
        this.id = id;
    }

    @Override
    public String getId ()
    {
        return id;
    }
}