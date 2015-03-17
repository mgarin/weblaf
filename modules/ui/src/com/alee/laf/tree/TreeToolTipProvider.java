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

import com.alee.managers.language.data.TooltipWay;
import com.alee.managers.tooltip.AbstractToolTipProvider;
import com.alee.managers.tooltip.WebCustomTooltip;

import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.*;

/**
 * Abstract WebLaF tooltip provider for WebTree component.
 *
 * @author Mikle Garin
 */

public abstract class TreeToolTipProvider<E extends DefaultMutableTreeNode> extends AbstractToolTipProvider<WebTree<E>>
{
    @Override
    public Rectangle getSourceBounds ( final WebTree tree, final Object value, final int index, final int column, final boolean isSelected )
    {
        final Rectangle bounds = tree.getRowBounds ( index );
        return bounds.intersection ( tree.getVisibleRect () );
    }

    @Override
    public final WebCustomTooltip getToolTip ( final WebTree<E> tree, final Object value, final int row, final int depth,
                                               final boolean isSelected )
    {
        return getToolTip ( tree, ( E ) value, row, isSelected );
    }

    @Override
    public final String getToolTipText ( final WebTree<E> tree, final Object value, final int row, final int depth,
                                         final boolean isSelected )
    {
        return getToolTipText ( tree, ( E ) value, row, isSelected );
    }

    /**
     * Return custom WebLaF tooltip for the specified tree node.
     *
     * @param tree       tree to provide tooltip for
     * @param node       node
     * @param row        node row
     * @param isSelected whether the cell is selected or not
     * @return node tooltip
     */
    public WebCustomTooltip getToolTip ( final WebTree<E> tree, final E node, final int row, final boolean isSelected )
    {
        return new WebCustomTooltip ( tree, getToolTipText ( tree, node, row, isSelected ), TooltipWay.trailing );
    }

    /**
     * Returns custom node tooltip text based on the node.
     *
     * @param tree       tree to provide tooltip for
     * @param node       node
     * @param row        node row
     * @param isSelected whether node is selected or not
     * @return custom node tooltip text based on the node
     */
    public abstract String getToolTipText ( final WebTree<E> tree, final E node, final int row, final boolean isSelected );

    @Override
    protected Object getValue ( final WebTree tree, final int index, final int column )
    {
        return tree.getNodeForRow ( index );
    }

    @Override
    protected boolean isSelected ( final WebTree tree, final int index, final int column )
    {
        return tree.isRowSelected ( index );
    }
}