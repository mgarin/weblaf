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
import com.alee.extended.tree.AsyncTreeModel;
import com.alee.extended.tree.ExTreeModel;
import com.alee.extended.tree.walker.AsyncTreeWalker;
import com.alee.laf.tree.walker.SimpleTreeWalker;
import com.alee.laf.tree.walker.TreeWalker;

import javax.swing.*;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

/**
 * This class provides a set of utilities for trees.
 * This is a library utility class and its not intended for use outside of the trees.
 *
 * @author Mikle Garin
 */
public final class TreeUtils
{
    /**
     * todo 1. Change this static utility class into {@link TreeState}-related class
     * todo 2. Proper state restoration for {@link com.alee.extended.tree.WebAsyncTree}
     */

    /**
     * Returns tree expansion and selection states.
     * Tree nodes must be instances of UniqueNode class.
     *
     * @param tree tree to process
     * @return tree expansion and selection states
     */
    @NotNull
    public static TreeState getTreeState ( final JTree tree )
    {
        return getTreeState ( tree, true );
    }

    /**
     * Returns tree expansion and selection states.
     * Tree nodes must be instances of UniqueNode class.
     *
     * @param tree          tree to process
     * @param saveSelection whether to save selection states or not
     * @return tree expansion and selection states
     */
    @NotNull
    public static TreeState getTreeState ( @NotNull final JTree tree, final boolean saveSelection )
    {
        return getTreeState ( tree, tree.getModel ().getRoot (), saveSelection );
    }

    /**
     * Returns tree expansion and selection states.
     * Tree nodes must be instances of UniqueNode class.
     *
     * @param tree tree to process
     * @param root node to save state for
     * @return tree expansion and selection states
     */
    @NotNull
    public static TreeState getTreeState ( @NotNull final JTree tree, @Nullable final Object root )
    {
        return getTreeState ( tree, root, true );
    }

    /**
     * Returns tree expansion and selection states.
     * Tree nodes must be instances of UniqueNode class.
     *
     * @param tree          tree to process
     * @param saveSelection whether to save selection states or not
     * @param root          node to save state for
     * @return tree expansion and selection states
     */
    @NotNull
    public static TreeState getTreeState ( @NotNull final JTree tree, @Nullable final Object root, final boolean saveSelection )
    {
        final TreeState state = new TreeState ();
        if ( root != null )
        {
            if ( !( root instanceof UniqueNode ) )
            {
                throw new RuntimeException ( "To get tree state you must use UniqueNode or any class that extends it as tree elements" );
            }
            saveTreeStateImpl ( tree, state, ( UniqueNode ) root, saveSelection );
        }
        return state;
    }

    /**
     * Saves tree expansion and selection states into {@link TreeState}.
     *
     * @param tree          tree to process
     * @param state         {@link TreeState} to save states into
     * @param parent        node to save states for
     * @param saveSelection whether to save selection states or not
     */
    private static void saveTreeStateImpl ( @NotNull final JTree tree, @NotNull final TreeState state, @NotNull final UniqueNode parent,
                                            final boolean saveSelection )
    {
        // Saving children states first
        for ( int i = 0; i < parent.getChildCount (); i++ )
        {
            saveTreeStateImpl ( tree, state, ( UniqueNode ) parent.getChildAt ( i ), saveSelection );
        }

        // Saving parent state
        // We make sure not to save collapsed state for hidden tree root
        // We also make sure not to save selection state if it not explicitly requested
        final TreePath path = new TreePath ( parent.getPath () );
        state.addState ( parent.getId (), new NodeState (
                tree.getModel ().getRoot () == parent && !tree.isRootVisible () || tree.isExpanded ( path ),
                saveSelection && tree.isPathSelected ( path )
        ) );
    }

    /**
     * Restores tree expansion and selection states.
     * Tree nodes must be instances of UniqueNode class.
     *
     * @param tree      tree to process
     * @param treeState tree expansion and selection states
     */
    public static void setTreeState ( @NotNull final JTree tree, @Nullable final TreeState treeState )
    {
        setTreeState ( tree, treeState, tree.getModel ().getRoot (), true );
    }

    /**
     * Restores tree expansion and selection states.
     * Tree nodes must be instances of UniqueNode class.
     *
     * @param tree             tree to process
     * @param treeState        tree expansion and selection states
     * @param restoreSelection whether to restore selection states or not
     */
    public static void setTreeState ( @NotNull final JTree tree, @Nullable final TreeState treeState, final boolean restoreSelection )
    {
        setTreeState ( tree, treeState, tree.getModel ().getRoot (), restoreSelection );
    }

    /**
     * Restores tree expansion and selection states.
     * Tree nodes must be instances of UniqueNode class.
     *
     * @param tree      tree to process
     * @param treeState tree expansion and selection states
     * @param root      node to restore state for
     */
    public static void setTreeState ( @NotNull final JTree tree, @Nullable final TreeState treeState, @Nullable final Object root )
    {
        setTreeState ( tree, treeState, root, true );
    }

    /**
     * Restores tree expansion and selection states.
     * Tree nodes must be instances of UniqueNode class.
     *
     * @param tree             tree to process
     * @param treeState        tree expansion and selection states
     * @param root             node to restore state for
     * @param restoreSelection whether to restore selection states or not
     */
    public static void setTreeState ( @NotNull final JTree tree, @Nullable final TreeState treeState,
                                      @Nullable final Object root, final boolean restoreSelection )
    {
        if ( root != null )
        {
            if ( !( root instanceof UniqueNode ) )
            {
                throw new RuntimeException ( "To set tree state you must use UniqueNode or any class that extends it as tree elements" );
            }
            if ( treeState != null )
            {
                restoreTreeStateImpl ( tree, treeState, ( UniqueNode ) root, restoreSelection );
            }
        }
    }

    /**
     * Restores tree expansion and selection states from {@link TreeState}.
     * todo This method's body can potentially be performed "later" recursively, but it might not always be a desired behavior
     * todo Probably might be added as an option in the tree state restore methods
     *
     * @param tree             tree to process
     * @param treeState        tree expansion and selection states
     * @param parent           node to restore states for
     * @param restoreSelection whether to restore selection states or not
     */
    private static void restoreTreeStateImpl ( @NotNull final JTree tree, @NotNull final TreeState treeState,
                                               @NotNull final UniqueNode parent, final boolean restoreSelection )
    {
        // Restoring children states first
        for ( int i = 0; i < parent.getChildCount (); i++ )
        {
            restoreTreeStateImpl ( tree, treeState, ( UniqueNode ) parent.getChildAt ( i ), restoreSelection );
        }

        // Restoring parent state
        final TreePath path = new TreePath ( parent.getPath () );
        if ( treeState.isExpanded ( parent.getId () ) )
        {
            if ( !tree.isExpanded ( path ) )
            {
                tree.expandPath ( path );
            }
        }
        else
        {
            if ( tree.isExpanded ( path ) )
            {
                tree.collapsePath ( path );
            }
        }
        if ( restoreSelection )
        {
            if ( treeState.isSelected ( parent.getId () ) )
            {
                if ( !tree.isPathSelected ( path ) )
                {
                    tree.addSelectionPath ( path );
                }
            }
            else
            {
                if ( tree.isPathSelected ( path ) )
                {
                    tree.removeSelectionPath ( path );
                }
            }
        }
    }

    /**
     * Returns appropriate {@link TreeWalker} implementation for the specified {@link JTree}.
     *
     * @param tree {@link JTree} to return appropriate {@link TreeWalker} implementation for
     * @param <N>  tree node type
     * @return appropriate {@link TreeWalker} implementation for the specified {@link JTree}
     */
    @NotNull
    public static <N extends TreeNode> TreeWalker<N> getTreeWalker ( @NotNull final JTree tree )
    {
        final TreeWalker treeWalker;
        if ( tree.getModel () instanceof AsyncTreeModel )
        {
            treeWalker = new AsyncTreeWalker ( tree );
        }
        else
        {
            treeWalker = new SimpleTreeWalker ( tree );
        }
        return ( TreeWalker<N> ) treeWalker;
    }

    /**
     * Returns {@link TreePath} from the root to the specified {@link TreeNode}.
     * The last element in the path is the specified {@link TreeNode}.
     *
     * @param node {@link TreeNode} to get {@link TreePath} for
     * @return {@link TreePath} from the root to the specified {@link TreeNode}
     */
    @Nullable
    public static TreePath getTreePath ( @Nullable final TreeNode node )
    {
        final TreePath treePath;
        if ( node != null )
        {
            final TreeNode[] path = getPath ( node );
            if ( path != null )
            {
                treePath = new TreePath ( path );
            }
            else
            {
                treePath = null;
            }
        }
        else
        {
            treePath = null;
        }
        return treePath;
    }

    /**
     * Returns the path from the root, to get to this node.
     * First element in the path is the root and the last element in the path is this node.
     *
     * @param node {@link TreeNode} to get the path for
     * @return array of {@link TreeNode}s representing the path
     */
    @Nullable
    public static TreeNode[] getPath ( @Nullable final TreeNode node )
    {
        return getPathToRoot ( node, 0 );
    }

    /**
     * Builds the parents of node up to and including the root node, where the original node is the last element in the returned array.
     * The length of the returned array gives the node's depth in the tree.
     *
     * @param node  {@link TreeNode} to get the path for
     * @param depth an int giving the number of steps already taken towards the root (on recursive calls), used to size the returned array
     * @return array of {@link TreeNode}s representing the path from the root to the specified {@link TreeNode}
     */
    @Nullable
    private static TreeNode[] getPathToRoot ( @Nullable final TreeNode node, int depth )
    {
        final TreeNode[] path;
        if ( node == null )
        {
            if ( depth != 0 )
            {
                path = new TreeNode[ depth ];
            }
            else
            {
                path = null;
            }
        }
        else
        {
            depth++;
            path = getPathToRoot ( node.getParent (), depth );
            if ( path != null )
            {
                path[ path.length - depth ] = node;
            }
        }
        return path;
    }

    /**
     * Returns whether or not {@code anotherNode} is an ancestor of {@code node}.
     * If {@code anotherNode} is {@code null}, this method returns {@code false}.
     * Note that any node is considered as an ancestor of itself.
     * This operation is at worst O(h) where h is the distance from the root to {@code node}.
     *
     * @param node        tested {@code node}
     * @param anotherNode node to test as an ancestor of {@code node}
     * @return {@code true} if {@code anotherNode} is an ancestor of {@code node}, {@code false} otherwise
     */
    public static boolean isNodeAncestor ( @NotNull final TreeNode node, @Nullable final TreeNode anotherNode )
    {
        boolean nodeAncestor = false;
        if ( anotherNode != null )
        {
            TreeNode ancestor = node;
            do
            {
                if ( ancestor == anotherNode )
                {
                    nodeAncestor = true;
                    break;
                }
            }
            while ( ( ancestor = ancestor.getParent () ) != null );
        }
        return nodeAncestor;
    }

    /**
     * Returns whether or not {@code anotherNode} is an ancestor of {@code node}.
     * If {@code anotherNode} is {@code null}, this method returns {@code false}.
     * Note that any node is considered as an ancestor of itself.
     * This operation is at worst O(h) where h is the distance from the root to {@code node}.
     *
     * Als note that unlike {@link #isNodeAncestor(TreeNode, TreeNode)} this method will make sure it takes all {@link TreeNode}s currently
     * hidden by tree filtering into account meaning result might not be consistent with what you actually see in the tree at the time.
     *
     * @param tree        {@link JTree}  containing specified {@link TreeNode}s
     * @param node        tested {@code node}
     * @param anotherNode node to test as an ancestor of {@code node}
     * @return {@code true} if {@code anotherNode} is an ancestor of {@code node}, {@code false} otherwise
     */
    public static boolean isNodeAncestor ( @NotNull final JTree tree, @NotNull final TreeNode node, @Nullable final TreeNode anotherNode )
    {
        final boolean ancestor;
        final TreeModel model = tree.getModel ();
        if ( model instanceof ExTreeModel )
        {
            boolean isParent = false;
            final ExTreeModel exTreeModel = ( ExTreeModel ) model;
            if ( anotherNode != null )
            {
                TreeNode parent = node;
                do
                {
                    if ( parent == anotherNode )
                    {
                        isParent = true;
                        break;
                    }
                }
                while ( ( parent = exTreeModel.getRawParent ( ( UniqueNode ) parent ) ) != null );
            }
            ancestor = isParent;
        }
        /* todo This should be implemented along with WebAsyncCheckBoxTree
        else if ( model instanceof AsyncTreeModel )
        {
            boolean isParent = false;
            final AsyncTreeModel asyncTreeModel = ( AsyncTreeModel ) model;
            if ( other != null )
            {
                TreeNode parent = node;
                do
                {
                    if ( parent == other )
                    {
                        isParent = true;
                        break;
                    }
                }
                while ( ( parent = asyncTreeModel.getRawParent ( ( UniqueNode ) parent ) ) != null );
            }
            ancestor = isParent;
        }*/
        else
        {
            ancestor = isNodeAncestor ( node, anotherNode );
        }
        return ancestor;
    }

    /**
     * Returns number of levels above the specified {@code node}.
     * It is basically the distance from the root to the specified {@code node}.
     * Returns {@code 0} if {@code node} is the root.
     *
     * @param node {@code node}
     * @return number of levels above the specified {@code node}
     */
    public static int getLevel ( @NotNull final TreeNode node )
    {
        int levels = 0;
        TreeNode ancestor = node;
        while ( ( ancestor = ancestor.getParent () ) != null )
        {
            levels++;
        }
        return levels;
    }

    /**
     * Expands all {@link JTree} nodes in a single call.
     *
     * @param tree {@link JTree} to expand all nodes for
     */
    public static void expandAll ( @NotNull final JTree tree )
    {
        if ( tree instanceof WebTree )
        {
            final WebTree webTree = ( WebTree ) tree;
            webTree.expandAll ();
        }
        else
        {
            int row = 0;
            while ( row < tree.getRowCount () )
            {
                tree.expandRow ( row );
                row++;
            }
        }
    }

    /**
     * Expands all {@link TreeNode}s loaded within {@link JTree} in a single call.
     *
     * @param tree {@link JTree} to expand {@link TreeNode}s for
     */
    public static void expandLoaded ( @NotNull final JTree tree )
    {
        final Object root = tree.getModel ().getRoot ();
        if ( root instanceof TreeNode )
        {
            expandLoaded ( tree, ( TreeNode ) root );
        }
        else
        {
            throw new RuntimeException ( "Specified tree doesn't use TreeNodes: " + tree );
        }
    }

    /**
     * Expands all {@link TreeNode}s loaded within {@link JTree} in a single call.
     *
     * @param tree {@link JTree} to expand {@link TreeNode}s for
     * @param node {@link TreeNode} under which all other {@link TreeNode}s should be expanded
     */
    public static void expandLoaded ( @NotNull final JTree tree, @NotNull final TreeNode node )
    {
        // Only expand parent for non-root nodes
        if ( node.getParent () != null )
        {
            // Make sure this node's parent is expanded
            // We do not need to expand the node itself, we only need to make sure it is visible in the tree
            final TreePath path = getTreePath ( node.getParent () );
            if ( !tree.isExpanded ( path ) )
            {
                tree.expandPath ( path );
            }
        }

        // Expanding all child nodes
        // We are asking node instead of tree model to avoid any additional data loading to occur
        for ( int index = 0; index < node.getChildCount (); index++ )
        {
            expandLoaded ( tree, node.getChildAt ( index ) );
        }
    }
}