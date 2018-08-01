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
 * Special {@link Filter} extension for {@link TreeNode}s filtering.
 * Provides additional methods to clear filter caches.
 *
 * @param <N> {@link TreeNode} type
 * @author Mikle Garin
 */
public interface NodesFilter<N extends TreeNode> extends Filter<N>
{
    /**
     * Clears filter cache for all {@link TreeNode}s.
     */
    public void clearCache ();

    /**
     * Clears filter cache for the specified {@link TreeNode}.
     *
     * @param node {@link TreeNode} to clear filter cache for
     */
    public void clearCache ( N node );
}