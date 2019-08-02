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
    public SimpleTreeWalker ( final JTree tree )
    {
        super ( tree );
    }

    @Override
    protected N getRootNode ( final TreeModel model )
    {
        return ( N ) model.getRoot ();
    }

    @Override
    protected int getChildCount ( final TreeModel model, final N parent )
    {
        return model.getChildCount ( parent );
    }

    @Override
    protected N getChild ( final TreeModel model, final N parent, final int index )
    {
        return ( N ) model.getChild ( parent, index );
    }

    @Override
    protected int getIndexOfChild ( final TreeModel model, final N parent, final N child )
    {
        return model.getIndexOfChild ( parent, child );
    }
}