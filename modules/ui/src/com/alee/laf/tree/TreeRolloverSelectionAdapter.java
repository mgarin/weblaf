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
import javax.swing.plaf.TreeUI;
import javax.swing.tree.TreePath;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

/**
 * Custom mouse motion listener that provides select-on-rollover behavior for any JTree.
 *
 * @author Mikle Garin
 */

public class TreeRolloverSelectionAdapter implements MouseMotionListener
{
    /**
     * List into which this adapter is installed.
     */
    private JTree tree;

    /**
     * Constructs rollover selection adapter for the specified list.
     *
     * @param tree list into which this adapter is installed
     */
    public TreeRolloverSelectionAdapter ( JTree tree )
    {
        super ();
        this.tree = tree;
    }

    /**
     * Performs selection change on rollover.
     *
     * @param e mouse event
     */
    @Override
    public void mouseMoved ( MouseEvent e )
    {
        // Disabled trees aren't affected
        if ( tree.isEnabled () )
        {
            final TreeUI treeUI = tree.getUI ();
            if ( treeUI instanceof WebTreeUI )
            {
                // Compute cell under point through WebTreeUI methods (to make sure that row selection type was taken into account)
                int row = ( ( WebTreeUI ) treeUI ).getRowForPoint ( e.getPoint () );
                if ( row != -1 )
                {
                    // Change selection
                    tree.setSelectionRow ( row );
                }
                else
                {
                    // Clear selection
                    tree.clearSelection ();
                }
            }
            else
            {
                // Compute cell under point through default tree methods
                TreePath treePath = tree.getPathForLocation ( e.getX (), e.getY () );
                if ( treePath != null )
                {
                    // Change selection
                    tree.setSelectionPath ( treePath );
                }
                else
                {
                    // Clear selection
                    tree.clearSelection ();
                }
            }
        }
    }

    /**
     * Ignored mouse event.
     *
     * @param e mouse event
     */
    @Override
    public void mouseDragged ( MouseEvent e )
    {
        //
    }

    /**
     * Installs rollover selection adapter into list and ensures that it is the only one installed.
     *
     * @param tree list to modify
     * @return installed rollover selection adapter
     */
    public static TreeRolloverSelectionAdapter install ( JTree tree )
    {
        // Uninstall old adapters first
        uninstall ( tree );

        // Add new adapter
        TreeRolloverSelectionAdapter adapter = new TreeRolloverSelectionAdapter ( tree );
        tree.addMouseMotionListener ( adapter );
        return adapter;
    }

    /**
     * Uninstalls all rollover selection adapters from the specified list.
     *
     * @param tree list to modify
     */
    public static void uninstall ( JTree tree )
    {
        for ( MouseMotionListener listener : tree.getMouseMotionListeners () )
        {
            if ( listener instanceof TreeRolloverSelectionAdapter )
            {
                tree.removeMouseMotionListener ( listener );
            }
        }
    }

    /**
     * Returns whether the specified list has any rollover selection adapters installed or not.
     *
     * @param tree list to process
     * @return true if the specified list has any rollover selection adapters installed, false otherwise
     */
    public static boolean isInstalled ( JTree tree )
    {
        for ( MouseMotionListener listener : tree.getMouseMotionListeners () )
        {
            if ( listener instanceof TreeRolloverSelectionAdapter )
            {
                return true;
            }
        }
        return false;
    }
}