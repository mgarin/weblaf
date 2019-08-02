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

import java.util.EventListener;
import java.util.List;

/**
 * This listener interface provide various asynchronous tree events.
 *
 * @param <N> node type
 * @author Mikle Garin
 */
public interface AsyncTreeListener<N extends AsyncUniqueNode> extends EventListener
{
    /**
     * Invoked when children load operation starts.
     *
     * @param parent node which children are being loaded
     */
    public void loadStarted ( N parent );

    /**
     * Invoked when children load operation finishes.
     *
     * @param parent   node which children were loaded
     * @param children loaded child nodes
     */
    public void loadCompleted ( N parent, List<N> children );

    /**
     * Invoked when children load operation failed.
     *
     * @param parent node which children were loaded
     * @param cause  children load failure cause
     */
    public void loadFailed ( N parent, Throwable cause );
}