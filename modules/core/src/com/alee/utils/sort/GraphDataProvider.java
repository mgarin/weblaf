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
 * @author Mikle Garin
 */

public interface GraphDataProvider<T>
{
    /**
     * Returns data for graph root nodes.
     *
     * @return data for graph root nodes
     */
    public List<T> getRoots ();

    /**
     * Returns child data for the specified data.
     *
     * @param data data to retrieve child data for
     * @return child data for the specified data
     */
    public List<T> getChildren ( T data );
}