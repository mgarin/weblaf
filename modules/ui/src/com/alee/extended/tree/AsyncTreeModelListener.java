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
 * This listener interface provide various asynchronous tree model events.
 *
 * @author Mikle Garin
 */

public interface AsyncTreeModelListener<E extends AsyncUniqueNode> extends EventListener
{
    /**
     * Invoked when childs load operation starts.
     *
     * @param parent node which childs are being loaded
     */
    public void childsLoadStarted ( E parent );

    /**
     * Invoked when childs load operation finishes.
     *
     * @param parent node which childs were loaded
     * @param childs loaded child nodes
     */
    public void childsLoadCompleted ( E parent, List<E> childs );

    /**
     * Invoked when childs load operation failed.
     *
     * @param parent node which childs were loaded
     * @param cause  childs load failure cause
     */
    public void childsLoadFailed ( E parent, Throwable cause );
}