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

import javax.swing.tree.TreeNode;
import java.util.Comparator;

/**
 * Interface for any element containing sortable structure of {@link TreeNode}s.
 *
 * @param <N> {@link TreeNode} type
 * @author Mikle Garin
 */
public interface SortableNodes<N extends TreeNode>
{
    /**
     * Returns {@link Comparator} used for {@link TreeNode}s.
     *
     * @return {@link Comparator} used for {@link TreeNode}s
     */
    public Comparator<N> getComparator ();

    /**
     * Sets {@link Comparator} for {@link TreeNode}s.
     * Changing this {@link Comparator} will automatically update sorting for all {@link TreeNode}s that have children.
     *
     * @param comparator {@link Comparator} for {@link TreeNode}s
     */
    public void setComparator ( Comparator<N> comparator );

    /**
     * Uninstalls {@link Comparator} used for {@link TreeNode}s.
     */
    public void clearComparator ();

    /**
     * Updates {@link TreeNode}s sorting for all existing nodes.
     */
    public void sort ();

    /**
     * Updates {@link TreeNode}s sorting for the specified {@link TreeNode} children.
     *
     * @param node {@link TreeNode} to update children sorting for
     */
    public void sort ( N node );

    /**
     * Updates {@link TreeNode}s sorting for the specified {@link TreeNode} children.
     *
     * @param node        {@link TreeNode} to update children sorting for
     * @param recursively whether should update the whole children structure recursively or not
     */
    public void sort ( N node, boolean recursively );
}