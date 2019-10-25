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

package com.alee.laf.tree.walker;

import com.alee.api.annotations.NotNull;

import javax.swing.*;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;

/**
 * Simple {@link TreeWalker} implementation for basic trees.
 *
 * @param <N> node type
 * @author Mikle Garin
 */
public class SimpleTreeWalker<N extends TreeNode> extends AbstractTreeWalker<N, TreeModel>
{
    /**
     * Constructs new {@link SimpleTreeWalker}.
     *
     * @param tree {@link JTree} to walk through
     */
    public SimpleTreeWalker ( @NotNull final JTree tree )
    {
        super ( tree );
    }

    @NotNull
    @Override
    protected N getRootNode ( @NotNull final TreeModel model )
    {
        return ( N ) model.getRoot ();
    }

    @Override
    protected int getChildCount ( @NotNull final TreeModel model, @NotNull final N parent )
    {
        return model.getChildCount ( parent );
    }

    @NotNull
    @Override
    protected N getChild ( @NotNull final TreeModel model, @NotNull final N parent, final int index )
    {
        return ( N ) model.getChild ( parent, index );
    }

    @Override
    protected int getIndexOfChild ( @NotNull final TreeModel model, @NotNull final N parent, @NotNull final N child )
    {
        return model.getIndexOfChild ( parent, child );
    }
}