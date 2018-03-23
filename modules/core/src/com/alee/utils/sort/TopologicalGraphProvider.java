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

package com.alee.utils.sort;

import java.util.List;

/**
 * Simple graph structure provider for topological sorting.
 *
 * @param <T> graph nodes type
 * @author Mikle Garin
 */

public interface TopologicalGraphProvider<T>
{
    /**
     * Returns graph root nodes.
     *
     * @return graph root nodes
     */
    public List<T> getRoots ();

    /**
     * Returns child nodes for the specified parent.
     *
     * @param parent parent node to retrieve child nodes for
     * @return child nodes for the specified parent
     */
    public List<T> getChildren ( T parent );
}