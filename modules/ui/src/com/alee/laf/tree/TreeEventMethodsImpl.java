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

import com.alee.api.jdk.Predicate;
import com.alee.utils.SwingUtils;

import javax.swing.tree.MutableTreeNode;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Common implementations for {@link com.alee.laf.tree.TreeEventMethods} interface methods.
 *
 * @author Mikle Garin
 * @see com.alee.laf.tree.TreeEventMethods
 */

public final class TreeEventMethodsImpl
{
    /**
     * Shortcut method for double-click mouse event on specific tree node.
     *
     * @param tree     tree to handle events for
     * @param runnable tree node event runnable
     * @return used mouse adapter
     */
    public static <N extends MutableTreeNode> MouseAdapter onNodeDoubleClick ( final WebTree<N> tree,
                                                                               final TreeNodeEventRunnable<N> runnable )
    {
        return onNodeDoubleClick ( tree, null, runnable );
    }

    /**
     * Shortcut method for double-click mouse event on specific tree node with condition.
     *
     * @param tree      tree to handle events for
     * @param condition node condition
     * @param runnable  tree node event runnable
     * @return used mouse adapter
     */
    public static <N extends MutableTreeNode> MouseAdapter onNodeDoubleClick ( final WebTree<N> tree, final Predicate<N> condition,
                                                                               final TreeNodeEventRunnable<N> runnable )
    {
        final MouseAdapter mouseAdapter = new MouseAdapter ()
        {
            @Override
            public void mouseClicked ( final MouseEvent e )
            {
                if ( SwingUtils.isDoubleClick ( e ) )
                {
                    final int row = tree.getUI ().getExactRowForLocation ( e.getPoint () );
                    if ( row != -1 )
                    {
                        final N node = tree.getNodeForRow ( row );
                        if ( condition == null || condition.test ( node ) )
                        {
                            runnable.run ( node );
                        }
                    }
                }
            }
        };
        tree.addMouseListener ( mouseAdapter );
        return mouseAdapter;
    }
}