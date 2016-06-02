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

/**
 * {@link com.alee.managers.drag.DragListener} adapter.
 *
 * @author Mikle Garin
 * @see com.alee.managers.drag.DragListener
 */

public abstract class DragAdapter implements DragListener
{
    @Override
    public void started ( final DragSourceDragEvent event )
    {
        // Do nothing by default
    }

    @Override
    public void entered ( final DragSourceDragEvent event )
    {
        // Do nothing by default
    }

    @Override
    public void moved ( final DragSourceDragEvent event )
    {
        // Do nothing by default
    }

    @Override
    public void exited ( final DragSourceEvent event )
    {
        // Do nothing by default
    }

    @Override
    public void finished ( final DragSourceDropEvent event )
    {
        // Do nothing by default
    }
}