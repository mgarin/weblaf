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
import com.alee.extended.dock.data.DockableElement;
import com.alee.api.data.CompassDirection;

import java.awt.*;
import java.io.Serializable;

/**
 * Drop location data for {@link com.alee.extended.dock.WebDockableFrame}.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-WebDockablePane">How to use WebDockablePane</a>
 * @see com.alee.extended.dock.WebDockableFrame
 * @see com.alee.extended.dock.WebDockablePane
 * @see com.alee.managers.drag.DragManager
 */
public class FrameDropData implements Identifiable, Serializable
{
    /**
     * Dragged frame ID.
     */
    protected final String id;

    /**
     * Drop location highlight bounds.
     */
    protected final Rectangle highlight;

    /**
     * Element currently placed at the drop location.
     */
    protected final DockableElement element;

    /**
     * Dropped element placement direction relative to the element.
     */
    protected final CompassDirection direction;

    /**
     * @param id        dragged frame ID
     * @param highlight drop location highlight bounds
     * @param element   element currently placed at the drop location
     * @param direction dropped element placement direction relative to the element
     */
    public FrameDropData ( final String id, final Rectangle highlight, final DockableElement element, final CompassDirection direction )
    {
        this.id = id;
        this.highlight = highlight;
        this.element = element;
        this.direction = direction;
    }

    @Override
    public String getId ()
    {
        return id;
    }

    /**
     * Returns drop location highlight bounds.
     *
     * @return drop location highlight bounds
     */
    public Rectangle getHighlight ()
    {
        return highlight;
    }

    /**
     * Returns element currently placed at the drop location.
     *
     * @return element currently placed at the drop location
     */
    public DockableElement getElement ()
    {
        return element;
    }

    /**
     * Returns dropped element placement direction relative to the element.
     *
     * @return dropped element placement direction relative to the element
     */
    public CompassDirection getDirection ()
    {
        return direction;
    }
}