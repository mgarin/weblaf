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
 * Special listener used within {@link com.alee.extended.tree.AsyncTreeModel} to receive children when loaded.
 *
 * @author Mikle Garin
 */

public interface ChildrenListener<E extends AsyncUniqueNode>
{
    /**
     * Informs model that children were loaded successfully.
     *
     * @param children list of loaded children
     */
    public void loadCompleted ( List<E> children );

    /**
     * Informs model that children load failed due to the specified exception.
     *
     * @param cause exception
     */
    public void loadFailed ( Throwable cause );
}