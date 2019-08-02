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

import com.alee.utils.compare.Filter;

import javax.swing.tree.TreeNode;

/**
 * Interface for any element containing filterable structure of {@link TreeNode}s.
 *
 * @param <N> {@link TreeNode} type
 * @author Mikle Garin
 */
public interface FilterableNodes<N extends TreeNode>
{
    /**
     * Returns {@link Filter} used for {@link TreeNode}s.
     *
     * @return {@link Filter} used for {@link TreeNode}s.
     */
    public Filter<N> getFilter ();

    /**
     * Sets {@link Filter} for {@link TreeNode}s.
     * Changing this {@link Filter} will automatically update filtering for all {@link TreeNode}s that have children.
     *
     * @param filter {@link Filter} for {@link TreeNode}s.
     */
    public void setFilter ( Filter<N> filter );

    /**
     * Uninstalls {@link Filter} used for {@link TreeNode}s.
     */
    public void clearFilter ();

    /**
     * Updates {@link TreeNode}s filtering for all existing nodes.
     */
    public void filter ();

    /**
     * Updates {@link TreeNode}s filtering for the specified {@link TreeNode} children.
     *
     * @param node {@link TreeNode} to update filtering for
     */
    public void filter ( N node );

    /**
     * Updates {@link TreeNode}s filtering for the specified {@link TreeNode} children.
     *
     * @param node        {@link TreeNode} to update filtering for
     * @param recursively whether should update the whole children structure recursively or not
     */
    public void filter ( N node, boolean recursively );
}