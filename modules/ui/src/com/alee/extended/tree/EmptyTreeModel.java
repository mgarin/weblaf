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

import com.alee.laf.tree.WebTreeNode;

import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

/**
 * Simple {@link TreeModel} implementation with no data.
 * It is used in custom WebLaF trees to avoid any default bulky sample models.
 *
 * @author Mikle Garin
 */
public final class EmptyTreeModel implements TreeModel
{
    /**
     * Root {@link WebTreeNode}.
     */
    private final WebTreeNode root;

    /**
     * Constructs new {@link EmptyTreeModel}.
     */
    public EmptyTreeModel ()
    {
        this.root = new WebTreeNode ( "Empty model" );
    }

    @Override
    public Object getRoot ()
    {
        return root;
    }

    @Override
    public Object getChild ( final Object parent, final int index )
    {
        return null;
    }

    @Override
    public int getChildCount ( final Object parent )
    {
        return 0;
    }

    @Override
    public boolean isLeaf ( final Object node )
    {
        return true;
    }

    @Override
    public void valueForPathChanged ( final TreePath path, final Object newValue )
    {

    }

    @Override
    public int getIndexOfChild ( final Object parent, final Object child )
    {
        return -1;
    }

    @Override
    public void addTreeModelListener ( final TreeModelListener l )
    {

    }

    @Override
    public void removeTreeModelListener ( final TreeModelListener l )
    {

    }
}