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
import java.util.ArrayList;
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
        final Object root = tree.getModel ().getRoot ();
        if ( !( root instanceof UniqueNode ) )
        {
            throw new RuntimeException ( "To get tree state you must use UniqueNode or any class that extends it as tree elements!" );
        }

        final TreeState treeState = new TreeState ();
        final List<UniqueNode> elements = new ArrayList<UniqueNode> ();
        elements.add ( ( UniqueNode ) root );
        while ( elements.size () > 0 )
        {
            final UniqueNode element = elements.get ( 0 );
            final TreePath path = new TreePath ( element.getPath () );
            treeState.addState ( element.getId (), tree.isExpanded ( path ), saveSelection && tree.isPathSelected ( path ) );

            for ( int i = 0; i < element.getChildCount (); i++ )
            {
                elements.add ( ( UniqueNode ) element.getChildAt ( i ) );
            }

            elements.remove ( element );
        }
        return treeState;
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
        final Object root = tree.getModel ().getRoot ();
        if ( !( root instanceof UniqueNode ) )
        {
            throw new RuntimeException ( "To set tree state you must use UniqueNode or any class that extends it as tree elements!" );
        }

        if ( treeState == null )
        {
            return;
        }

        tree.clearSelection ();

        final List<UniqueNode> elements = new ArrayList<UniqueNode> ();
        elements.add ( ( UniqueNode ) root );
        while ( elements.size () > 0 )
        {
            final UniqueNode element = elements.get ( 0 );
            final TreePath path = new TreePath ( element.getPath () );

            // todo Create workaround for async trees
            // Restoring expansion states
            if ( treeState.isExpanded ( element.getId () ) )
            {
                tree.expandPath ( path );

                // We are going futher only into expanded nodes, otherwise this will expand even collapsed ones
                for ( int i = 0; i < element.getChildCount (); i++ )
                {
                    elements.add ( ( UniqueNode ) tree.getModel ().getChild ( element, i ) );
                }
            }
            else
            {
                tree.collapsePath ( path );
            }

            // Restoring selection states
            if ( restoreSelection )
            {
                if ( treeState.isSelected ( element.getId () ) )
                {
                    tree.addSelectionPath ( path );
                }
                else
                {
                    tree.removeSelectionPath ( path );
                }
            }

            elements.remove ( element );
        }
    }

    /**
     * Optimizes list of nodes by removing those which already have their parent node in the list.
     *
     * @param nodes nodes list to optimize
     */
    public static <E extends DefaultMutableTreeNode> void optimizeNodes ( final List<E> nodes )
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