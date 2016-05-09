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

import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * This class provides a set of utilities to work with various tree events.
 *
 * @author Mikle Garin
 */

public class TreeEventUtils
{
    /**
     * Shortcut method for double-click mouse event on specific tree node.
     *
     * @param tree     tree to handle events for
     * @param runnable tree node event runnable
     * @return used mouse adapter
     */
    public static <E extends DefaultMutableTreeNode> MouseAdapter onNodeDoubleClick ( final WebTree<E> tree,
                                                                                      final TreeNodeEventRunnable<E> runnable )
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
    public static <E extends DefaultMutableTreeNode> MouseAdapter onNodeDoubleClick ( final WebTree<E> tree, final Predicate<E> condition,
                                                                                      final TreeNodeEventRunnable<E> runnable )
    {
        final MouseAdapter mouseAdapter = new MouseAdapter ()
        {
            @Override
            public void mouseClicked ( final MouseEvent e )
            {
                if ( SwingUtils.isLeftMouseButton ( e ) && e.getClickCount () == 2 )
                {
                    final int mouseoverRow = tree.getHoverRow ();
                    if ( mouseoverRow != -1 )
                    {
                        final E node = tree.getNodeForRow ( mouseoverRow );
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