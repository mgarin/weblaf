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

package com.alee.managers.drag;

import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.DragSourceDragEvent;
import java.awt.dnd.DragSourceDropEvent;
import java.awt.image.BufferedImage;

/**
 * Special interface that describes single object type drag representation.
 *
 * @author Mikle Garin
 * @see com.alee.managers.drag.DragManager
 */

public interface DragViewHandler<T>
{
    /**
     * Returns object flavor that can be used to retrieve dragged object.
     *
     * @return object flavor that can be used to retrieve dragged object
     */
    public DataFlavor getObjectFlavor ();

    /**
     * Returns image object representation.
     * This method is called once per drag operation to initialize dragged object view.
     *
     * @param object object to create image representation for
     * @param event  drag source drag event
     * @return image object representation
     */
    public BufferedImage getView ( T object, DragSourceDragEvent event );

    /**
     * Returns image object representation location relative to mouse location.
     * This method is called each time image location should be updated.
     *
     * @param object object return image representation location for
     * @param event  drag source drag event
     * @return image object representation location relative to mouse location
     */
    public Point getViewRelativeLocation ( T object, DragSourceDragEvent event );

    /**
     * Notifies about drag operation end.
     * This method is called once per drag operation.
     *
     * @param object dragged object
     * @param event  drag source drop event
     */
    public void dragEnded ( T object, DragSourceDropEvent event );
}