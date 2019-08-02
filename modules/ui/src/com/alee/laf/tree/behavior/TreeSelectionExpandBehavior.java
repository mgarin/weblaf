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
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;

/**
 * Tree behavior that automatically expands any selected node.
 *
 * @author Mikle Garin
 */
public class TreeSelectionExpandBehavior implements TreeSelectionListener, Behavior
{
    /**
     * todo 1. Make non-static install/uninstall methods
     */

    /**
     * Tree using this behavior.
     */
    protected final JTree tree;

    /**
     * Constructs new tree hover selection behavior.
     *
     * @param tree tree using this behavior
     */
    public TreeSelectionExpandBehavior ( final JTree tree )
    {
        super ();
        this.tree = tree;
    }

    @Override
    public void valueChanged ( final TreeSelectionEvent e )
    {
        if ( tree.getSelectionCount () > 0 )
        {
            tree.expandPath ( tree.getSelectionPath () );
        }
    }

    /**
     * Installs behavior into tree and ensures that it is the only one installed.
     *
     * @param tree tree to modify
     * @return installed behavior
     */
    public static TreeSelectionExpandBehavior install ( final JTree tree )
    {
        // Uninstalling old behavior first
        uninstall ( tree );

        // Installing new behavior
        final TreeSelectionExpandBehavior behavior = new TreeSelectionExpandBehavior ( tree );
        tree.addTreeSelectionListener ( behavior );
        return behavior;
    }

    /**
     * Uninstalls all behaviors from the specified tree.
     *
     * @param tree tree to modify
     */
    public static void uninstall ( final JTree tree )
    {
        for ( final TreeSelectionListener listener : tree.getTreeSelectionListeners () )
        {
            if ( listener instanceof TreeSelectionExpandBehavior )
            {
                tree.removeTreeSelectionListener ( listener );
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
        for ( final TreeSelectionListener listener : tree.getTreeSelectionListeners () )
        {
            if ( listener instanceof TreeSelectionExpandBehavior )
            {
                return true;
            }
        }
        return false;
    }
}