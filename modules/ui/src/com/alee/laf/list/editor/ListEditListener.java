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

package com.alee.laf.list.editor;

import java.util.EventListener;

/**
 * This interface provide methods to track various list editing events.
 *
 * @author Mikle Garin
 */
public interface ListEditListener extends EventListener
{
    /**
     * Notifies when editing starts.
     *
     * @param index edited cell index
     */
    public void editStarted ( int index );

    /**
     * Notifies when editing finishes.
     *
     * @param index    edited cell index
     * @param oldValue old cell value
     * @param newValue new cell value
     */
    public void editFinished ( int index, Object oldValue, Object newValue );

    /**
     * Notifies when editing is cancelled.
     *
     * @param index edited cell index
     */
    public void editCancelled ( int index );
}