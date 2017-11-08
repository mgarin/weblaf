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

package com.alee.extended.tree.walker;

import com.alee.extended.tree.AsyncTreeModel;
import com.alee.extended.tree.AsyncUniqueNode;
import com.alee.laf.tree.walker.AbstractTreeWalker;
import com.alee.laf.tree.walker.TreeWalker;

import javax.swing.*;

/**
 * {@link TreeWalker} implementation for async trees.
 * It contains workaround allowing to avoid child nodes loading when walking the tree.
 *
 * @param <E> tree node type
 * @author Mikle Garin
 */

public class AsyncTreeWalker<E extends AsyncUniqueNode> extends AbstractTreeWalker<E, AsyncTreeModel<E>>
{
    /**
     * Constructs new {@link AsyncTreeWalker}.
     *
     * @param tree {@link JTree} to walk through
     */
    public AsyncTreeWalker ( final JTree tree )
    {
        super ( tree );
    }

    @Override
    protected E getRootNode ( final AsyncTreeModel<E> model )
    {
        return model.getRoot ();
    }

    @Override
    protected int getChildCount ( final AsyncTreeModel<E> model, final E parent )
    {
        return model.areChildrenLoaded ( parent ) ? model.getChildCount ( parent ) : 0;
    }

    @Override
    protected E getChild ( final AsyncTreeModel<E> model, final E parent, final int index )
    {
        return model.getChild ( parent, index );
    }

    @Override
    protected int getIndexOfChild ( final AsyncTreeModel<E> model, final E parent, final E child )
    {
        return model.getIndexOfChild ( parent, child );
    }
}