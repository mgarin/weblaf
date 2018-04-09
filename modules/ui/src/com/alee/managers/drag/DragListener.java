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

import java.awt.dnd.DragSourceDragEvent;
import java.awt.dnd.DragSourceDropEvent;
import java.awt.dnd.DragSourceEvent;
import java.util.EventListener;

/**
 * Global drag and drop operations listener.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-DragManager">How to use DragManager</a>
 * @see DragManager
 */
public interface DragListener extends EventListener
{
    /**
     * Drag operation started.
     * Called once on each drag operation start.
     * When drag operation starts outside of the application it will be called when drop location comes inside the application.
     *
     * @param event drag event
     */
    public void started ( DragSourceDragEvent event );

    /**
     * Drag entered droppable area that accepts currently dragged data.
     * Might be called multiple times within single drag operation.
     *
     * @param event drag event
     */
    public void entered ( DragSourceDragEvent event );

    /**
     * Drop location changed.
     * Called every time user moves mouse while dragging.
     *
     * @param event drag event
     */
    public void moved ( DragSourceDragEvent event );

    /**
     * Drag exited droppable area that accepts currently dragged data.
     * Might be called multiple times within single drag operation.
     *
     * @param event drag event
     */
    public void exited ( DragSourceEvent event );

    /**
     * Drag operation completed.
     * This method will be called on both successful and unsuccessful completion.
     *
     * @param event drop event
     */
    public void finished ( DragSourceDropEvent event );
}