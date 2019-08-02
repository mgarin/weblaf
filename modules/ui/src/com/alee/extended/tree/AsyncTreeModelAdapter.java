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
 * Adapter for {@link AsyncTreeModelListener}.
 *
 * @param <N> {@link AsyncUniqueNode} type
 * @author Mikle Garin
 */
public abstract class AsyncTreeModelAdapter<N extends AsyncUniqueNode> implements AsyncTreeModelListener<N>
{
    @Override
    public void loadStarted ( final N parent )
    {
        /**
         * Do nothing by default.
         */
    }

    @Override
    public void loadCompleted ( final N parent, final List<N> children )
    {
        /**
         * Do nothing by default.
         */
    }

    @Override
    public void loadFailed ( final N parent, final Throwable cause )
    {
        /**
         * Do nothing by default.
         */
    }
}