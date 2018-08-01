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

import com.alee.laf.tree.UniqueNode;
import com.alee.utils.compare.Filter;

import java.io.Serializable;
import java.util.Comparator;
import java.util.List;

/**
 * This interface provides methods for synchronous tree nodes retrieval within {@link ExTreeModel}.
 * It also extends {@link Serializable} as it is used within {@link ExTreeModel} which must also be {@link Serializable}.
 *
 * @param <N> node type
 * @author Mikle Garin
 * @see WebExTree
 * @see ExTreeModel
 * @see UniqueNode
 */
public interface ExTreeDataProvider<N extends UniqueNode> extends Serializable
{
    /**
     * Returns root {@link UniqueNode}.
     * This operation is always performed on EDT and should not take excessive amounts of time.
     *
     * @return root {@link UniqueNode}
     * @see <a href="https://github.com/mgarin/weblaf/wiki/Event-Dispatch-Thread">Event Dispatch Thread</a>
     */
    public N getRoot ();

    /**
     * Returns child {@link UniqueNode}s for the specified parent {@link UniqueNode}.
     * This operation is always performed on EDT and should not take excessive amounts of time.
     *
     * @param parent {@link UniqueNode} to load children for
     * @return child {@link UniqueNode}s for the specified parent {@link UniqueNode}
     * @see <a href="https://github.com/mgarin/weblaf/wiki/Event-Dispatch-Thread">Event Dispatch Thread</a>
     */
    public List<N> getChildren ( N parent );

    /**
     * Returns {@link Filter} that will be used for the specified {@link UniqueNode} children.
     * Specific {@link List} of child {@link UniqueNode}s is given for every separate filter operation.
     * No filtering applied to children in case {@code null} is returned.
     * This operation is always performed on EDT and should not take excessive amounts of time.
     *
     * @param parent   {@link UniqueNode} which children will be filtered using returned {@link Filter}
     * @param children {@link UniqueNode}s to be filtered
     * @return {@link Filter} that will be used for the specified {@link UniqueNode} children
     * @see <a href="https://github.com/mgarin/weblaf/wiki/Event-Dispatch-Thread">Event Dispatch Thread</a>
     */
    public Filter<N> getChildrenFilter ( N parent, List<N> children );

    /**
     * Returns {@link Comparator} that will be used for the specified {@link UniqueNode} children.
     * Specific {@link List} of child {@link UniqueNode}s is given for every separate comparison operation.
     * No sorting applied to children in case {@code null} is returned.
     * This operation is always performed on EDT and should not take excessive amounts of time.
     *
     * @param parent   {@link UniqueNode} which children will be sorted using returned {@link Comparator}
     * @param children {@link UniqueNode}s to be sorted
     * @return {@link Comparator} that will be used for the specified {@link UniqueNode} children
     * @see <a href="https://github.com/mgarin/weblaf/wiki/Event-Dispatch-Thread">Event Dispatch Thread</a>
     */
    public Comparator<N> getChildrenComparator ( N parent, List<N> children );
}