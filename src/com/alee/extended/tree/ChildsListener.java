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
 * Special listener used within AsyncTreeModel to receive childs when loaded.
 *
 * @author Mikle Garin
 */

public interface ChildsListener<E extends AsyncUniqueNode>
{
    /**
     * Informs model that childs were loaded successfully.
     *
     * @param childs list of loaded childs
     */
    public void childsLoadCompleted ( List<E> childs );

    /**
     * Informs model that childs load failed due to the specified exception.
     *
     * @param cause exception
     */
    public void childsLoadFailed ( Throwable cause );
}