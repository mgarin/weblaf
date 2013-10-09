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

import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import java.util.List;

/**
 * Extended Swing DefaultTreeModel.
 * This model contains multiply elements add/remove methods and works with typed elements.
 *
 * @author Mikle Garin
 */

public class WebTreeModel<E extends MutableTreeNode> extends DefaultTreeModel
{
    /**
     * Constructs tree model with a specified node as root.
     *
     * @param root TreeNode object that is the root of the tree
     */
    public WebTreeModel ( E root )
    {
        super ( root );
    }

    /**
     * Constructs tree model with a specified node as root.
     *
     * @param root               TreeNode object that is the root of the tree
     * @param asksAllowsChildren false if any node can have children, true if each node is asked to see if it can have children
     */
    public WebTreeModel ( E root, boolean asksAllowsChildren )
    {
        super ( root, asksAllowsChildren );
    }

    /**
     * Inserts a list of child nodes into parent node.
     *
     * @param children list of new child nodes
     * @param parent   parent node
     * @param index    insert index
     */
    public void insertNodesInto ( List<E> children, E parent, int index )
    {
        for ( int i = children.size () - 1; i >= 0; i-- )
        {
            parent.insert ( children.get ( i ), index );
        }

        int[] indices = new int[ children.size () ];
        for ( int i = 0; i < children.size (); i++ )
        {
            indices[ i ] = index + i;
        }

        nodesWereInserted ( parent, indices );
    }

    /**
     * Forces node to be updated.
     *
     * @param node node to be updated
     */
    public void updateNode ( E node )
    {
        fireTreeNodesChanged ( WebTreeModel.this, getPathToRoot ( node ), null, null );
    }
}