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

import com.alee.laf.tree.TreeUtils;

import javax.swing.tree.MutableTreeNode;
import java.util.Comparator;

/**
 * Comparator for nodes position in the tree.
 * It doesn't rely on the visible row, but the raw position of the nodes within their parents.
 *
 * Note that this comparator will only work if provided nodes have access to their parent nodes and children.
 * This always depends on how tree model works with the nodes.
 *
 * @param <N> nodes type
 * @author Mikle Garin
 */

public class NodesPositionComparator<N extends MutableTreeNode> implements Comparator<N>
{
    @Override
    public int compare ( final N n1, final N n2 )
    {
        if ( n1 == n2 )
        {
            // Same nodes do not need to be sorted
            return 0;
        }
        else if ( TreeUtils.isNodeAncestor ( n1, n2 ) )
        {
            // If second node is ancestor of first one
            return 1;
        }
        else if ( TreeUtils.isNodeAncestor ( n2, n1 ) )
        {
            // If first node is ancestor of second one
            return -1;
        }
        else
        {
            // Now we are sure that neither of nodes is ancestor of other one
            // So now we have to find a node which is parent for both of them
            N p1 = n1;
            N p2 = n2;
            while ( p1 != null && p2 != null &&
                    ( TreeUtils.getLevel ( p1 ) != TreeUtils.getLevel ( p2 ) || p1.getParent () != p2.getParent () ) )
            {
                // We need to acquire levels of both nodes before we change them
                // This is important to avoid issues in further checks
                final int l1 = TreeUtils.getLevel ( p1 );
                final int l2 = TreeUtils.getLevel ( p2 );
                if ( l1 >= l2 )
                {
                    // If this node has higher level we need to move to its parent
                    // Also if this node is on the same level but parents for both nodes are different we also need to move to its parent
                    p1 = ( N ) p1.getParent ();
                }
                if ( l2 >= l1 )
                {
                    // If this node has higher level we need to move to its parent
                    // Also if this node is on the same level but parents for both nodes are different we also need to move to its parent
                    p2 = ( N ) p2.getParent ();
                }
            }

            // Ensure that parent was found
            if ( p1 != null && p2 != null )
            {
                // Now we have parent node for both of our nodes with the biggest level value
                // We can simply compare position of each parent of our nodes under this node
                final N parent = ( N ) p1.getParent ();
                return new Integer ( parent.getIndex ( p1 ) ).compareTo ( parent.getIndex ( p2 ) );
            }
            else
            {
                // Something went wrong
                return 0;
            }
        }
    }
}