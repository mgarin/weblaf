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
import javax.swing.tree.TreePath;
import java.util.ArrayList;
import java.util.List;

/**
 * This class provides a set of utilities to work with trees.
 *
 * @author Mikle Garin
 */

public class TreeUtils
{
    /**
     * Returns tree expansion and selection states.
     * Tree nodes must be instances of UniqueNode class.
     *
     * @param tree tree to process
     * @return tree expansion and selection states
     */
    public static TreeState getTreeState ( JTree tree )
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
    public static TreeState getTreeState ( JTree tree, boolean saveSelection )
    {
        TreeState treeState = new TreeState ();

        List<UniqueNode> elements = new ArrayList<UniqueNode> ();
        elements.add ( ( UniqueNode ) tree.getModel ().getRoot () );
        while ( elements.size () > 0 )
        {
            UniqueNode element = elements.get ( 0 );

            TreePath path = new TreePath ( element.getPath () );
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
    public static void setTreeState ( JTree tree, TreeState treeState )
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
    public static void setTreeState ( JTree tree, TreeState treeState, boolean restoreSelection )
    {
        if ( treeState == null )
        {
            return;
        }

        tree.clearSelection ();

        List<UniqueNode> elements = new ArrayList<UniqueNode> ();
        elements.add ( ( UniqueNode ) tree.getModel ().getRoot () );
        while ( elements.size () > 0 )
        {
            UniqueNode element = elements.get ( 0 );
            TreePath path = new TreePath ( element.getPath () );

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
}