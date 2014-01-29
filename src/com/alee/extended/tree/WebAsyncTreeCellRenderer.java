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

package com.alee.extended.tree;

import com.alee.laf.tree.WebTreeCellRenderer;
import com.alee.laf.tree.WebTreeElement;
import com.alee.utils.ImageUtils;

import javax.swing.*;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * Custom default tree cell renderer for WebAsyncTree.
 *
 * @author Mikle Garin
 */

public class WebAsyncTreeCellRenderer extends WebTreeCellRenderer
{
    /**
     * todo 1. Create getIcon & getText methods to simplify rendererers usage
     */

    /**
     * Special failed state icon.
     */
    public static final ImageIcon failedStateIcon = new ImageIcon ( AsyncUniqueNode.class.getResource ( "icons/failed.png" ) );

    /**
     * User failed icons cache.
     */
    public static final Map<ImageIcon, ImageIcon> failedStateIcons = new WeakHashMap<ImageIcon, ImageIcon> ( 5 );

    /**
     * Returns user failed state icon.
     *
     * @param icon base icon
     * @return user failed state icon
     */
    public static ImageIcon getFailedStateIcon ( final ImageIcon icon )
    {
        ImageIcon failedIcon = failedStateIcons.get ( icon );
        if ( failedIcon == null )
        {
            failedIcon = ImageUtils.mergeIcons ( icon, failedStateIcon );
            failedStateIcons.put ( icon, failedIcon );
        }
        return failedIcon;
    }

    /**
     * Returns tree cell renderer component.
     *
     * @param tree       tree
     * @param value      cell value
     * @param isSelected whether cell is selected or not
     * @param expanded   whether cell is expanded or not
     * @param leaf       whether cell is leaf or not
     * @param row        cell row number
     * @param hasFocus   whether cell has focus or not
     * @return cell renderer component
     */
    @Override
    public WebTreeElement getTreeCellRendererComponent ( final JTree tree, final Object value, final boolean isSelected,
                                                         final boolean expanded, final boolean leaf, final int row, final boolean hasFocus )
    {
        super.getTreeCellRendererComponent ( tree, value, isSelected, expanded, leaf, row, hasFocus );

        // Custom loader icon for busy state
        if ( value instanceof AsyncUniqueNode )
        {
            final AsyncUniqueNode node = ( AsyncUniqueNode ) value;
            if ( node.isLoading () )
            {
                setIcon ( node.getLoaderIcon () );
            }
        }

        return this;
    }
}