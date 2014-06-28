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

/**
 * ListEditListener interface adapter.
 *
 * @author Mikle Garin
 */

public abstract class ListEditAdapter implements ListEditListener
{
    /**
     * Informs that editing was started.
     *
     * @param index edited cell index
     */
    @Override
    public void editStarted ( int index )
    {
        //
    }

    /**
     * Informs that editing was finished.
     *
     * @param index    edited cell index
     * @param oldValue old cell value
     * @param newValue new cell value
     */
    @Override
    public void editFinished ( int index, Object oldValue, Object newValue )
    {
        //
    }

    /**
     * Informs that editing was cancelled.
     *
     * @param index edited cell index
     */
    @Override
    public void editCancelled ( int index )
    {
        //
    }
}