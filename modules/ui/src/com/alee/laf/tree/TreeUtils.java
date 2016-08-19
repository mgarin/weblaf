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

import javax.swing.*;
import javax.swing.tree.*;
import java.util.List;

/**
 * This class provides a set of utilities for trees.
 * This is a library utility class and its not intended for use outside of the trees.
 *
 * @author Mikle Garin
 */

public final class TreeUtils
{
    /**
     * todo 1. Create proper state restoration for WebAsyncTree
     */

    /**
     * Returns tree expansion and selection states.
     * Tree nodes must be instances of UniqueNode class.
     *
     * @param tree tree to process
     * @return tree expansion and selection states
     */
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
    public static TreeState getTreeState ( final JTree tree, final boolean saveSelection )
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
    public static TreeState getTreeState ( final JTree tree, final Object root )
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
    public static TreeState getTreeState ( final JTree tree, final Object root, final boolean saveSelection )
    {
        if ( !( root instanceof UniqueNode ) )
        {
            throw new RuntimeException ( "To get tree state you must use UniqueNode or any class that extends it as tree elements" );
        }
        final TreeState state = new TreeState ();
        saveTreeStateImpl ( tree, state, ( UniqueNode ) root, saveSelection );
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
    protected static void saveTreeStateImpl ( final JTree tree, final TreeState state, final UniqueNode parent,
                                              final boolean saveSelection )
    {
        // Saving children states first
        for ( int i = 0; i < parent.getChildCount (); i++ )
        {
            saveTreeStateImpl ( tree, state, ( UniqueNode ) parent.getChildAt ( i ), saveSelection );
        }

        // Saving parent state
        final TreePath path = new TreePath ( parent.getPath () );
        state.addState ( parent.getId (), tree.isExpanded ( path ), saveSelection && tree.isPathSelected ( path ) );
    }

    /**
     * Restores tree expansion and selection states.
     * Tree nodes must be instances of UniqueNode class.
     *
     * @param tree      tree to process
     * @param treeState tree expansion and selection states
     */
    public static void setTreeState ( final JTree tree, final TreeState treeState )
    {
        setTreeState ( tree, treeState, true );
    }

    /**
     * Restores tree expansion and selection states.
     * Tree nodes must be instances of UniqueNode class.
     *
     * @param tree             tree to process
     * @param treeState        tree expansion and selection states
     * @param restoreSelection whether to restore selection states or not
     */
    public static void setTreeState ( final JTree tree, final TreeState treeState, final boolean restoreSelection )
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
    protected static void setTreeState ( final JTree tree, final TreeState treeState, final Object root )
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
    protected static void setTreeState ( final JTree tree, final TreeState treeState, final Object root, final boolean restoreSelection )
    {
        if ( !( root instanceof UniqueNode ) )
        {
            throw new RuntimeException ( "To set tree state you must use UniqueNode or any class that extends it as tree elements" );
        }
        if ( treeState == null )
        {
            return;
        }
        restoreTreeStateImpl ( tree, treeState, ( UniqueNode ) root, restoreSelection );
    }

    /**
     * Restores tree expansion and selection states from {@link TreeState}.
     *
     * @param tree             tree to process
     * @param treeState        tree expansion and selection states
     * @param parent           node to restore states for
     * @param restoreSelection whether to restore selection states or not
     */
    private static void restoreTreeStateImpl ( final JTree tree, final TreeState treeState, final UniqueNode parent,
                                               final boolean restoreSelection )
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
            tree.expandPath ( path );
        }
        else
        {
            tree.collapsePath ( path );
        }
        if ( restoreSelection )
        {
            if ( treeState.isSelected ( parent.getId () ) )
            {
                tree.addSelectionPath ( path );
            }
            else
            {
                tree.removeSelectionPath ( path );
            }
        }
    }

    /**
     * Trims list of nodes by removing those which already have their parent node in the list.
     *
     * @param nodes nodes list to trim
     */
    public static <E extends DefaultMutableTreeNode> void trimNodes ( final List<E> nodes )
    {
        for ( int i = nodes.size () - 1; i >= 0; i-- )
        {
            final E node = nodes.get ( i );
            for ( final E other : nodes )
            {
                if ( other != node && node.isNodeAncestor ( other ) )
                {
                    nodes.remove ( i );
                    break;
                }
            }
        }
    }

    /**
     * Updates visual representation of all visible tree nodes.
     * This will forces renderer to iterate through all those nodes and update the view appropriately.
     *
     * @param tree tree to update visible nodes for
     */
    public static void updateAllVisibleNodes ( final JTree tree )
    {
        final TreeModel model = tree.getModel ();
        if ( model instanceof DefaultTreeModel )
        {
            ( ( DefaultTreeModel ) model ).nodeStructureChanged ( ( TreeNode ) model.getRoot () );
        }
        else
        {
            tree.revalidate ();
            tree.repaint ();
        }
    }
}