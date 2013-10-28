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
     * Icon key constants.
     */
    private static final String ROOT_KEY = "root";
    private static final String OPEN_KEY = "open";
    private static final String CLOSED_KEY = "closed";
    private static final String LEAF_KEY = "leaf";

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
                final boolean failed = node.isFailed ();
                switch ( node.getType () )
                {
                    case root:
                    {
                        setIcon ( failed ? getFailedStateIcon ( ROOT_KEY, WebTreeUI.ROOT_ICON ) : WebTreeUI.ROOT_ICON );
                        break;
                    }
                    case folder:
                    {
                        setIcon ( expanded ? ( failed ? getFailedStateIcon ( OPEN_KEY, WebTreeUI.OPEN_ICON ) : WebTreeUI.OPEN_ICON ) :
                                ( failed ? getFailedStateIcon ( CLOSED_KEY, WebTreeUI.CLOSED_ICON ) : WebTreeUI.CLOSED_ICON ) );
                        break;
                    }
                    case leaf:
                    {
                        setIcon ( failed ? getFailedStateIcon ( LEAF_KEY, WebTreeUI.LEAF_ICON ) : WebTreeUI.LEAF_ICON );
                        break;
                    }
                }
            }

            // Node text
            setText ( node.getName () );
        }

        return this;
    }
}