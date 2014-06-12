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

package com.alee.extended.tree.sample;

import com.alee.extended.tree.WebAsyncTreeCellRenderer;
import com.alee.laf.tree.WebTreeElement;
import com.alee.laf.tree.WebTreeUI;

import javax.swing.*;

/**
 * Sample asynchronous tree cell renderer.
 *
 * @author Mikle Garin
 */

public class SampleTreeCellRenderer extends WebAsyncTreeCellRenderer
{
    /**
     * Returns custom tree cell renderer component
     *
     * @param tree       tree
     * @param value      cell value
     * @param isSelected whether cell is selected or not
     * @param expanded   whether cell is expanded or not
     * @param leaf       whether cell is leaf or not
     * @param row        cell row number
     * @param hasFocus   whether cell has focusor not
     * @return renderer component
     */
    @Override
    public WebTreeElement getTreeCellRendererComponent ( final JTree tree, final Object value, final boolean isSelected,
                                                         final boolean expanded, final boolean leaf, final int row, final boolean hasFocus )
    {
        super.getTreeCellRendererComponent ( tree, value, isSelected, expanded, leaf, row, hasFocus );

        if ( value instanceof SampleNode )
        {
            final SampleNode node = ( SampleNode ) value;

            // Node icon
            if ( !node.isLoading () )
            {
                // Type icon
                final ImageIcon icon;
                switch ( node.getType () )
                {
                    case root:
                    {
                        icon = WebTreeUI.ROOT_ICON;
                        break;
                    }
                    case folder:
                    {
                        icon = expanded ? WebTreeUI.OPEN_ICON : WebTreeUI.CLOSED_ICON;
                        break;
                    }
                    case leaf:
                    {
                        icon = WebTreeUI.LEAF_ICON;
                        break;
                    }
                    default:
                    {
                        icon = null;
                        break;
                    }
                }
                setIcon ( node.isFailed () ? getFailedStateIcon ( icon ) : icon );
            }

            // Node text
            setText ( node.getName () );
        }

        return this;
    }
}