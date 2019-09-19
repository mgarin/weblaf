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

import com.alee.api.annotations.NotNull;
import com.alee.api.annotations.Nullable;
import com.alee.utils.CollectionUtils;
import com.alee.utils.compare.IntegerComparator;

import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Swing {@link DefaultTreeModel} extension.
 * This model works with specific known node type and contains additional methods to affect multiple model elements at once.
 *
 * @param <N> node type
 * @author Mikle Garin
 */
public class WebTreeModel<N extends MutableTreeNode> extends DefaultTreeModel
{
    /**
     * Constructs tree model with a specified node as root.
     *
     * @param root TreeNode object that is the root of the tree
     */
    public WebTreeModel ( @Nullable final N root )
    {
        super ( root );
    }

    /**
     * Constructs tree model with a specified node as root.
     *
     * @param root               TreeNode object that is the root of the tree
     * @param asksAllowsChildren false if any node can have children, true if each node is asked to see if it can have children
     */
    public WebTreeModel ( @Nullable final N root, final boolean asksAllowsChildren )
    {
        super ( root, asksAllowsChildren );
    }

    /**
     * Returns root node.
     *
     * @return root node
     */
    @Nullable
    public N getRootNode ()
    {
        return ( N ) getRoot ();
    }

    @Override
    public void insertNodeInto ( @NotNull final MutableTreeNode child, @NotNull final MutableTreeNode parent, final int index )
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
    public void insertNodesInto ( @NotNull final List<N> children, @NotNull final N parent, final int index )
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
    public void insertNodesInto ( @NotNull final N[] children, @NotNull final N parent, final int index )
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
    public void removeNodeFromParent ( @NotNull final MutableTreeNode node )
    {
        // Removing nodes and collecting information on the operation
        final N parent = ( N ) node.getParent ();
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
    public void removeNodesFromParent ( @NotNull final N parent )
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
    public void removeNodesFromParent ( @NotNull final N[] nodes )
    {
        removeNodesFromParent ( CollectionUtils.toList ( nodes ) );
    }

    /**
     * Removes specified nodes from tree structure.
     *
     * @param nodes nodes to remove
     */
    public void removeNodesFromParent ( @NotNull final List<N> nodes )
    {
        if ( nodes.size () > 0 )
        {
            // Collecting nodes for each parent
            final Map<N, List<Integer>> mappedNodes = new HashMap<N, List<Integer>> ();
            for ( final N node : nodes )
            {
                // Empty parents are ignored as they might have been removed just now
                final N parent = ( N ) node.getParent ();
                if ( parent != null )
                {
                    List<Integer> parentNodes = mappedNodes.get ( parent );
                    if ( parentNodes == null )
                    {
                        parentNodes = new ArrayList<Integer> ( nodes.size () );
                        mappedNodes.put ( parent, parentNodes );
                    }
                    parentNodes.add ( parent.getIndex ( node ) );
                }
            }

            // Removing mapped nodes
            for ( final Map.Entry<N, List<Integer>> parentEntry : mappedNodes.entrySet () )
            {
                // Retrieving parent and sorted indices of nodes to remove
                final N parent = parentEntry.getKey ();
                final List<Integer> removedIndices = CollectionUtils.sort ( parentEntry.getValue (), IntegerComparator.instance () );

                // Removing nodes from parent
                final List<N> removedNodes = new ArrayList<N> ( removedIndices.size () );
                for ( int i = removedIndices.size () - 1; i >= 0; i-- )
                {
                    final int index = removedIndices.get ( i );
                    removedNodes.add ( ( N ) parent.getChildAt ( index ) );
                    parent.remove ( index );
                }

                // Firing removal event
                final int[] indices = CollectionUtils.toIntArray ( removedIndices );
                final Object[] removed = CollectionUtils.toObjectArray ( removedNodes );
                nodesWereRemoved ( parent, indices, removed );
            }
        }
    }

    /**
     * Forces tree node to be updated.
     *
     * @param node tree node to be updated
     */
    public void updateNode ( @Nullable final N node )
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
    public void updateNodes ( @Nullable final N... nodes )
    {
        if ( nodes != null && nodes.length > 0 )
        {
            for ( final N node : nodes )
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
    public void updateNodes ( @Nullable final List<N> nodes )
    {
        if ( CollectionUtils.notEmpty ( nodes ) )
        {
            for ( final N node : nodes )
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
    public void updateNodeStructure ( @Nullable final N node )
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
        final N rootNode = getRootNode ();
        if ( rootNode != null )
        {
            final TreeNode[] path = TreeUtils.getPath ( rootNode );
            fireTreeStructureChanged ( WebTreeModel.this, path, null, null );
        }
    }
}