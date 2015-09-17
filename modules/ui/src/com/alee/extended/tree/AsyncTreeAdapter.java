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

package com.alee.extended.tree;

import java.util.List;

/**
 * This listener class provide various asynchronous tree events.
 *
 * @param <E> custom node type
 * @author Mikle Garin
 */

public abstract class AsyncTreeAdapter<E extends AsyncUniqueNode> implements AsyncTreeListener<E>
{
    @Override
    public void loadStarted ( final E parent )
    {
        // Do nothing by default
    }

    @Override
    public void loadCompleted ( final E parent, final List<E> children )
    {
        // Do nothing by default
    }

    @Override
    public void loadFailed ( final E parent, final Throwable cause )
    {
        // Do nothing by default
    }
}