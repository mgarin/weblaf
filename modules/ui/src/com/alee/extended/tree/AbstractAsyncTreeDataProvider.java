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

import java.util.Comparator;
import java.util.List;

/**
 * Abstract {@link AsyncTreeDataProvider} that can contain single comparator and filter for child nodes.
 *
 * @param <N> node type
 * @author Mikle Garin
 */
public abstract class AbstractAsyncTreeDataProvider<N extends AsyncUniqueNode> implements AsyncTreeDataProvider<N>
{
    /**
     * {@link Comparator} for all child nodes.
     * It is {@code transient} as it can only be set through code.
     * Override {@link #getChildrenComparator(AsyncUniqueNode, List)} method to provide parent-related {@link Comparator}.
     */
    protected transient Comparator<N> comparator = null;

    /**
     * {@link Filter} for all child nodes.
     * It is {@code transient} as it can only be set through code.
     * Override {@link #getChildrenFilter(AsyncUniqueNode, List)} method to provide parent-related {@link Filter}.
     */
    protected transient Filter<N> filter = null;

    @Override
    public Comparator<N> getChildrenComparator ( final N parent, final List<N> children )
    {
        return comparator;
    }

    /**
     * Sets {@link Comparator} for all child nodes.
     *
     * @param comparator {@link Comparator} for all child nodes
     */
    public void setChildrenComparator ( final Comparator<N> comparator )
    {
        this.comparator = comparator;
    }

    @Override
    public Filter<N> getChildrenFilter ( final N parent, final List<N> children )
    {
        return filter;
    }

    /**
     * Sets {@link Filter} for all child nodes.
     *
     * @param filter {@link Filter} for all child nodes
     */
    public void setChildrenFilter ( final Filter<N> filter )
    {
        this.filter = filter;
    }

    /**
     * Returns {@code false} by default to allow children load requests to pass through for any node.
     * It is recommended to override this behavior if you can easily determine whether node is leaf or not.
     *
     * @param node {@link AsyncUniqueNode} to check
     * @return {@code false}
     */
    @Override
    public boolean isLeaf ( final N node )
    {
        return false;
    }
}