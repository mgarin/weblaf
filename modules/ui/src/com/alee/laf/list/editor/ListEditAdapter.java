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
 * Adapter for {@link ListEditListener}.
 *
 * @author Mikle Garin
 */
public abstract class ListEditAdapter implements ListEditListener
{
    @Override
    public void editStarted ( final int index )
    {
        /**
         * Do nothing by default.
         */
    }

    @Override
    public void editFinished ( final int index, final Object oldValue, final Object newValue )
    {
        /**
         * Do nothing by default.
         */
    }

    @Override
    public void editCancelled ( final int index )
    {
        /**
         * Do nothing by default.
         */
    }
}