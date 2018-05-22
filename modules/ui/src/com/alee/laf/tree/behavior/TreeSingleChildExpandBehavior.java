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

package com.alee.laf.tree.behavior;

import com.alee.extended.behavior.Behavior;

import javax.swing.*;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.tree.TreePath;
import java.util.Arrays;

/**
 * Tree behavior that automatically expands child nodes futher if it parent node has only one child.
 * Actual expand operation occurs right after node expand event and works only on its children.
 * Be aware that this behavior is not suited for async trees.
 *
 * @author Mikle Garin
 */
public class TreeSingleChildExpandBehavior implements TreeExpansionListener, Behavior
{
    /**
     * todo 1. Make non-static install/uninstall methods
     */

    @Override
    public void treeExpanded ( final TreeExpansionEvent event )
    {
        final JTree tree = ( JTree ) event.getSource ();
        final TreePath expandedPath = event.getPath ();
        final Object expandedObject = expandedPath.getLastPathComponent ();
        if ( tree.getModel ().getChildCount ( expandedObject ) == 1 )
        {
            final Object[] parentPath = expandedPath.getPath ();
            final Object[] path = Arrays.copyOf ( parentPath, parentPath.length + 1 );
            path[ parentPath.length ] = tree.getModel ().getChild ( expandedObject, 0 );
            tree.expandPath ( new TreePath ( path ) );
        }
    }

    @Override
    public void treeCollapsed ( final TreeExpansionEvent event )
    {
        // Do nothing
    }

    /**
     * Installs behavior into tree and ensures that it is the only one installed.
     *
     * @param tree tree to modify
     * @return installed behavior
     */
    public static TreeSingleChildExpandBehavior install ( final JTree tree )
    {
        // Uninstalling old behavior first
        uninstall ( tree );

        // Installing new behavior
        final TreeSingleChildExpandBehavior behavior = new TreeSingleChildExpandBehavior ();
        tree.addTreeExpansionListener ( behavior );
        return behavior;
    }

    /**
     * Uninstalls all behaviors from the specified tree.
     *
     * @param tree tree to modify
     */
    public static void uninstall ( final JTree tree )
    {
        for ( final TreeExpansionListener listener : tree.getTreeExpansionListeners () )
        {
            if ( listener instanceof TreeSingleChildExpandBehavior )
            {
                tree.removeTreeExpansionListener ( listener );
            }
        }
    }

    /**
     * Returns whether the specified tree has any behaviors installed or not.
     *
     * @param tree tree to process
     * @return true if the specified tree has any behaviors installed, false otherwise
     */
    public static boolean isInstalled ( final JTree tree )
    {
        for ( final TreeExpansionListener listener : tree.getTreeExpansionListeners () )
        {
            if ( listener instanceof TreeSingleChildExpandBehavior )
            {
                return true;
            }
        }
        return false;
    }
}