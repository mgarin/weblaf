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
 * @param <E> tree node type
 * @author Mikle Garin
 */

public class SimpleTreeWalker<E extends TreeNode> extends AbstractTreeWalker<E, TreeModel>
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
    protected E getRootNode ( final TreeModel model )
    {
        return ( E ) model.getRoot ();
    }

    @Override
    protected int getChildCount ( final TreeModel model, final E parent )
    {
        return model.getChildCount ( parent );
    }

    @Override
    protected E getChild ( final TreeModel model, final E parent, final int index )
    {
        return ( E ) model.getChild ( parent, index );
    }

    @Override
    protected int getIndexOfChild ( final TreeModel model, final E parent, final E child )
    {
        return model.getIndexOfChild ( parent, child );
    }
}