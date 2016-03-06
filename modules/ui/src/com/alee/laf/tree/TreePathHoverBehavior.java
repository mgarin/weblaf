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

import com.alee.utils.swing.AbstractObjectHoverBehavior;

import javax.swing.*;
import javax.swing.plaf.TreeUI;
import javax.swing.tree.TreePath;
import java.awt.*;

/**
 * Abstract behavior that provides hover events for {@link javax.swing.JTree} paths.
 * To install this behavior you will need to add it as {@link java.awt.event.MouseListener}, {@link java.awt.event.MouseMotionListener} and
 * {@link java.awt.event.ComponentListener} into the tree, otherwise it will not function properly.
 * <p>
 * It uses mouse enter/exit/move events and component resized/moved/shown/hidden events to track hover index.
 * It might seem excessive, but simple move listener does not cover whole variety of possible cases when hover index can be changed.
 *
 * @author Mikle Garin
 */

public abstract class TreePathHoverBehavior extends AbstractObjectHoverBehavior<JTree, TreePath>
{
    /**
     * Constructs behavior for the specified tree.
     *
     * @param tree tree into which this behavior is installed
     */
    public TreePathHoverBehavior ( final JTree tree )
    {
        super ( tree );
    }

    /**
     * Constructs behavior for the specified tree.
     *
     * @param tree        tree into which this behavior is installed
     * @param enabledOnly whether or not behavior should only track hover events when tree is enabled
     */
    public TreePathHoverBehavior ( final JTree tree, final boolean enabledOnly )
    {
        super ( tree, enabledOnly );
    }

    @Override
    protected TreePath getObjectAt ( final Point point )
    {
        final TreeUI treeUI = component.getUI ();
        final int index;
        if ( treeUI instanceof WebTreeUI )
        {
            // Compute index under point through WebTreeUI methods (to make sure that row selection type was taken into account)
            index = ( ( WebTreeUI ) treeUI ).getRowForPoint ( point );
        }
        else
        {
            // Compute index under point through default tree methods
            index = component.getRowForLocation ( point.x, point.y );
        }
        return component.getPathForRow ( index );
    }
}