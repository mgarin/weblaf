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

import com.alee.extended.dock.data.StructureElement;
import com.alee.painter.decoration.states.CompassDirection;

import java.awt.*;
import java.io.Serializable;

/**
 * @author Mikle Garin
 */

public class FrameDropData implements Serializable
{
    /**
     * Dragged frame ID.
     */
    protected String frameId;

    /**
     * Drop location highlight bounds.
     */
    protected Rectangle highlight;

    /**
     * Element currently placed at the drop location.
     */
    protected StructureElement element;

    /**
     * Dropped element placement direction relative to the element.
     */
    protected CompassDirection direction;

    /**
     *
     * @param frameId
     * @param highlight
     * @param element
     * @param direction
     */
    public FrameDropData ( final String frameId, final Rectangle highlight, final StructureElement element,
                           final CompassDirection direction )
    {
        super ();
        this.frameId = frameId;
        this.highlight = highlight;
        this.element = element;
        this.direction = direction;
    }

    public String getFrameId ()
    {
        return frameId;
    }

    public Rectangle getHighlight ()
    {
        return highlight;
    }

    public StructureElement getElement ()
    {
        return element;
    }

    public CompassDirection getDirection ()
    {
        return direction;
    }
}