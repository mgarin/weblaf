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

package com.alee.laf.tree;

import com.alee.utils.compare.IntegerComparator;

import javax.swing.tree.MutableTreeNode;
import java.util.Comparator;

/**
 * Simple comparator that can be used to sort {@link MutableTreeNode}s by their row numbers in {@link WebTree}.
 *
 * @param <N> {@link MutableTreeNode} type
 * @author Mikle Garin
 */
public final class NodesRowComparator<N extends MutableTreeNode> implements Comparator<N>
{
    /**
     * {@link WebTree} used to retrieve nodes row number.
     */
    private final WebTree<N> tree;

    /**
     * Constructs new {@link NodesRowComparator} for the specified {@link WebTree}.
     *
     * @param tree {@link WebTree} used to retrieve nodes row number
     */
    public NodesRowComparator ( final WebTree<N> tree )
    {
        this.tree = tree;
    }

    @Override
    public int compare ( final N node1, final N node2 )
    {
        final int row1 = tree.getRowForNode ( node1 );
        final int row2 = tree.getRowForNode ( node2 );
        return IntegerComparator.instance ().compare ( row1, row2 );
    }
}