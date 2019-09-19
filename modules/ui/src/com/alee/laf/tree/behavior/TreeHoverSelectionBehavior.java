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

import com.alee.api.annotations.NotNull;
import com.alee.api.annotations.Nullable;
import com.alee.extended.behavior.Behavior;
import com.alee.laf.tree.WebTree;
import com.alee.utils.swing.HoverListener;

import javax.swing.tree.MutableTreeNode;

/**
 * Tree behavior that automatically selects any hovered node.
 * It is using optimized {@link HoverListener} based on custom UI functionality.
 *
 * @param <N> node type
 * @author Mikle Garin
 */
public class TreeHoverSelectionBehavior<N extends MutableTreeNode> implements HoverListener<N>, Behavior
{
    /**
     * todo 1. Make non-static install/uninstall methods
     */

    /**
     * Tree using this behavior.
     */
    @NotNull
    protected final WebTree<N> tree;

    /**
     * Constructs new tree hover selection behavior.
     *
     * @param tree tree using this behavior
     */
    public TreeHoverSelectionBehavior ( @NotNull final WebTree<N> tree )
    {
        this.tree = tree;
    }

    @Override
    public void hoverChanged ( @Nullable final N previous, @Nullable final N current )
    {
        if ( current != null )
        {
            tree.setSelectedNode ( current );
        }
        else
        {
            tree.clearSelection ();
        }
    }

    /**
     * Installs behavior into tree and ensures that it is the only one installed.
     *
     * @param tree tree to modify
     * @return installed behavior
     */
    public static TreeHoverSelectionBehavior install ( @NotNull final WebTree tree )
    {
        // Uninstalling old behavior first
        uninstall ( tree );

        // Installing new behavior
        final TreeHoverSelectionBehavior behavior = new TreeHoverSelectionBehavior ( tree );
        tree.addHoverListener ( behavior );
        return behavior;
    }

    /**
     * Uninstalls all behaviors from the specified tree.
     *
     * @param tree tree to modify
     */
    public static void uninstall ( @NotNull final WebTree tree )
    {
        for ( final HoverListener listener : tree.getHoverListeners () )
        {
            if ( listener instanceof TreeHoverSelectionBehavior )
            {
                tree.removeHoverListener ( listener );
            }
        }
    }

    /**
     * Returns whether the specified tree has any behaviors installed or not.
     *
     * @param tree tree to process
     * @return true if the specified tree has any behaviors installed, false otherwise
     */
    public static boolean isInstalled ( @NotNull final WebTree tree )
    {
        boolean installed = false;
        for ( final HoverListener listener : tree.getHoverListeners () )
        {
            if ( listener instanceof TreeHoverSelectionBehavior )
            {
                installed = true;
                break;
            }
        }
        return installed;
    }
}