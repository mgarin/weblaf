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

import com.alee.utils.CollectionUtils;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreePath;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Extended Swing DefaultTreeModel.
 * This model contains multiply elements add/remove methods and works with typed elements.
 *
 * @author Mikle Garin
 */

public class WebTreeModel<E extends DefaultMutableTreeNode> extends DefaultTreeModel
{
    /**
     * Constructs tree model with a specified node as root.
     *
     * @param root TreeNode object that is the root of the tree
     */
    public WebTreeModel ( final E root )
    {
        super ( root );
    }

    /**
     * Constructs tree model with a specified node as root.
     *
     * @param root               TreeNode object that is the root of the tree
     * @param asksAllowsChildren false if any node can have children, true if each node is asked to see if it can have children
     */
    public WebTreeModel ( final E root, final boolean asksAllowsChildren )
    {
        super ( root, asksAllowsChildren );
    }

    /**
     * Returns root node.
     *
     * @return root node
     */
    public E getRootNode ()
    {
        return ( E ) getRoot ();
    }

    @Override
    public void insertNodeInto ( final MutableTreeNode child, final MutableTreeNode parent, final int index )
    {
        // Inserting node
        parent.insert ( child, index );

        // Firing node addition
        nodesWereInserted ( parent, new int[]{ index } );
    }

    /**
     * Inserts a list of child nodes into parent node.
     *
     * @param children list of new child nodes
     * @param parent   parent node
     * @param index    insert index
     */
    public void insertNodesInto ( final List<E> children, final E parent, final int index )
    {
        final int count = children.size ();
        if ( count > 0 )
        {
            // Inserting nodes
            final int[] indices = new int[ count ];
            for ( int i = index; i < index + count; i++ )
            {
                final int sourceIndex = i - index;
                parent.insert ( children.get ( sourceIndex ), i );
                indices[ sourceIndex ] = i;
            }

            // Firing nodes addition
            nodesWereInserted ( parent, indices );
        }
    }

    /**
     * Inserts a list of child nodes into parent node.
     *
     * @param children array of new child nodes
     * @param parent   parent node
     * @param index    insert index
     */
    public void insertNodesInto ( final E[] children, final E parent, final int index )
    {
        final int count = children.length;
        if ( count > 0 )
        {
            // Inserting nodes
            final int[] indices = new int[ count ];
            for ( int i = index; i < index + count; i++ )
            {
                final int sourceIndex = i - index;
                parent.insert ( children[ sourceIndex ], i );
                indices[ sourceIndex ] = i;
            }

            // Firing nodes addition
            nodesWereInserted ( parent, indices );
        }
    }

    @Override
    public void removeNodeFromParent ( final MutableTreeNode node )
    {
        // Removing nodes and collecting information on the operation
        final E parent = ( E ) node.getParent ();
        if ( parent == null )
        {
            throw new IllegalArgumentException ( "Removed node does not have a parent" );
        }
        final int index = parent.getIndex ( node );
        parent.remove ( index );

        // Firing nodes removal
        nodesWereRemoved ( parent, new int[]{ index }, new Object[]{ node } );
    }

    /**
     * Removes all child nodes under the specified node from tree structure.
     *
     * @param parent node to remove children from
     */
    public void removeNodesFromParent ( final E parent )
    {
        final int count = parent.getChildCount ();
        if ( count > 0 )
        {
            // Removing nodes and collecting information on the operation
            final int[] indices = new int[ count ];
            final Object[] removed = new Object[ count ];
            for ( int index = 0; index < count; index++ )
            {
                indices[ index ] = index;
                removed[ index ] = parent.getChildAt ( index );
                parent.remove ( index );
            }

            // Firing nodes removal
            nodesWereRemoved ( parent, indices, removed );
        }
    }

    /**
     * Removes specified nodes from tree structure.
     *
     * @param nodes nodes to remove
     */
    public void removeNodesFromParent ( final E[] nodes )
    {
        removeNodesFromParent ( CollectionUtils.toList ( nodes ) );
    }

    /**
     * Removes specified nodes from tree structure.
     *
     * @param nodes nodes to remove
     */
    public void removeNodesFromParent ( final List<E> nodes )
    {
        if ( nodes.size () > 0 )
        {
            // Removing nodes and collecting information on the operation
            final Map<E, Map<E, Integer>> removedNodes = new HashMap<E, Map<E, Integer>> ();
            for ( final E node : nodes )
            {
                // Empty parents are ignored as they might have been removed just now
                final E parent = ( E ) node.getParent ();
                if ( parent != null )
                {
                    final int index = parent.getIndex ( node );
                    Map<E, Integer> indices = removedNodes.get ( parent );
                    if ( indices == null )
                    {
                        indices = new HashMap<E, Integer> ( nodes.size () );
                        removedNodes.put ( parent, indices );
                    }
                    parent.remove ( index );
                    indices.put ( node, index );
                }
            }

            // Firing nodes removal
            for ( final Map.Entry<E, Map<E, Integer>> perParent : removedNodes.entrySet () )
            {
                final E parent = perParent.getKey ();
                final int[] indices = CollectionUtils.toIntArray ( perParent.getValue ().values () );
                final Object[] removed = CollectionUtils.toObjectArray ( perParent.getValue ().keySet () );
                nodesWereRemoved ( parent, indices, removed );
            }
        }
    }

    @Override
    public void valueForPathChanged ( final TreePath path, final Object newValue )
    {
        final MutableTreeNode aNode = ( MutableTreeNode ) path.getLastPathComponent ();
        // aNode.setUserObject ( newValue ); // Hidden to avoid problems
        nodeChanged ( aNode );
    }

    /**
     * Forces tree node to be updated.
     *
     * @param node tree node to be updated
     */
    public void updateNode ( final E node )
    {
        if ( node != null )
        {
            fireTreeNodesChanged ( WebTreeModel.this, getPathToRoot ( node ), null, null );
        }
    }

    /**
     * Forces tree nodes to be updated.
     *
     * @param nodes tree nodes to be updated
     */
    public void updateNodes ( final E... nodes )
    {
        if ( nodes != null && nodes.length > 0 )
        {
            for ( final E node : nodes )
            {
                fireTreeNodesChanged ( WebTreeModel.this, getPathToRoot ( node ), null, null );
            }
        }
    }

    /**
     * Forces tree nodes to be updated.
     *
     * @param nodes tree nodes to be updated
     */
    public void updateNodes ( final List<E> nodes )
    {
        if ( CollectionUtils.notEmpty ( nodes ) )
        {
            for ( final E node : nodes )
            {
                fireTreeNodesChanged ( WebTreeModel.this, getPathToRoot ( node ), null, null );
            }
        }
    }

    /**
     * Forces tree node to be updated.
     *
     * @param node tree node to be updated
     */
    public void updateNodeStructure ( final E node )
    {
        if ( node != null )
        {
            fireTreeStructureChanged ( WebTreeModel.this, getPathToRoot ( node ), null, null );
        }
    }

    /**
     * Forces update of all visible node sizes and view.
     * This call might be useful if renderer changes dramatically and you have to update the whole tree.
     */
    public void updateTree ()
    {
        fireTreeStructureChanged ( WebTreeModel.this, getRootNode ().getPath (), null, null );
    }
}