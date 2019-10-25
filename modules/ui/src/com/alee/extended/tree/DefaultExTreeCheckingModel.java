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

/**
 * Default checking model for {@link WebExCheckBoxTree}.
 *
 * @param <N> {@link UniqueNode} type
 * @param <T> {@link WebExCheckBoxTree} type
 * @author Mikle Garin
 */
public class DefaultExTreeCheckingModel<N extends UniqueNode, T extends WebExCheckBoxTree<N>> extends DefaultTreeCheckingModel<N, T>
{
    /**
     * Constructs new {@link DefaultExTreeCheckingModel} for the specified {@link WebExCheckBoxTree}.
     *
     * @param checkBoxTree {@link WebExCheckBoxTree} which uses this {@link DefaultExTreeCheckingModel}
     */
    public DefaultExTreeCheckingModel ( @NotNull final T checkBoxTree )
    {
        super ( checkBoxTree );
    }

    @Nullable
    @Override
    protected N getParent ( @NotNull final N node )
    {
        final ExTreeModel<N> model = checkBoxTree.getModel ();
        return model != null ? model.getRawParent ( node ) : super.getParent ( node );
    }

    @NotNull
    @Override
    protected N getChildAt ( @NotNull final N parent, final int index )
    {
        final ExTreeModel<N> model = checkBoxTree.getModel ();
        return model != null ? model.getRawChildAt ( parent, index ) : super.getChildAt ( parent, index );
    }

    @Override
    protected int getChildCount ( @NotNull final N parent )
    {
        final ExTreeModel<N> model = checkBoxTree.getModel ();
        return model != null ? model.getRawChildrenCount ( parent ) : super.getChildCount ( parent );
    }
}