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

import com.alee.api.annotations.NotNull;
import com.alee.api.annotations.Nullable;
import com.alee.laf.tree.UniqueNode;
import com.alee.laf.tree.WebTreeModel;
import com.alee.utils.CollectionUtils;
import com.alee.utils.compare.Filter;

import java.util.Comparator;
import java.util.List;

/**
 * Abstract {@link ExTreeDataProvider} that can contain single comparator and filter for child nodes.
 *
 * @param <N> node type
 * @author Mikle Garin
 */
public abstract class AbstractExTreeDataProvider<N extends UniqueNode> implements ExTreeDataProvider<N>
{
    /**
     * {@link Comparator} for all child nodes.
     * It is {@code transient} as it can only be set through code.
     * Override {@link #getChildrenComparator(UniqueNode, List)} method to provide parent-related {@link Comparator}.
     */
    @Nullable
    protected transient Comparator<N> comparator = null;

    /**
     * {@link Filter} for all child nodes.
     * It is {@code transient} as it can only be set through code.
     * Override {@link #getChildrenFilter(UniqueNode, List)} method to provide parent-related {@link Filter}.
     */
    @Nullable
    protected transient Filter<N> filter = null;

    @Override
    public Comparator<N> getChildrenComparator ( @NotNull final N parent, @NotNull final List<N> children )
    {
        return comparator;
    }

    /**
     * Sets children comparator for all nodes.
     *
     * @param comparator children comparator for all nodes
     */
    public void setChildrenComparator ( @Nullable final Comparator<N> comparator )
    {
        this.comparator = comparator;
    }

    @Override
    public Filter<N> getChildrenFilter ( @NotNull final N parent, @NotNull final List<N> children )
    {
        return filter;
    }

    /**
     * Sets children filter for all nodes.
     *
     * @param filter children filter for all nodes
     */
    public void setChildrenFilter ( @Nullable final Filter<N> filter )
    {
        this.filter = filter;
    }

    /**
     * Returns plain {@link WebTreeModel} with data from this {@link AbstractExTreeDataProvider} implementation.
     *
     * @return plain {@link WebTreeModel} with data from this {@link AbstractExTreeDataProvider} implementation
     */
    @NotNull
    public WebTreeModel<N> createPlainModel ()
    {
        final N root = getRoot ();
        loadPlainChildren ( root );
        return new WebTreeModel<N> ( root );
    }

    /**
     * Loads all child {@link UniqueNode}s for the specified parent {@link UniqueNode} recursively.
     *
     * @param parent parent {@link UniqueNode} to load all children for recursively
     */
    protected void loadPlainChildren ( @NotNull final N parent )
    {
        final List<N> children = getChildren ( parent );
        final List<N> filtered = CollectionUtils.filter ( children, getChildrenFilter ( parent, children ) );
        final List<N> sorted = CollectionUtils.sort ( children, getChildrenComparator ( parent, filtered ) );
        for ( final N child : sorted )
        {
            parent.add ( child );
            loadPlainChildren ( child );
        }
    }
}