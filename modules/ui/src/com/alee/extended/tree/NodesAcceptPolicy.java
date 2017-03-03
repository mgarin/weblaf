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

import javax.swing.tree.DefaultMutableTreeNode;
import java.util.List;

/**
 * Provides a way to conveniently filter out unwanted nodes from the provided list based on the specific policy.
 * This is mostly used in {@link WebCheckBoxTree} for different ways of providing nodes, but can also be used anywhere else.
 *
 * @author Mikle Garin
 */

public enum NodesAcceptPolicy
{
    /**
     * Accept all nodes.
     *
     * For example if we have this nodes structure:
     * [ ] Root node
     * |--[v] Node 1
     * |   |--[v] Node 1.1
     * |   |--[v] Node 1.2
     * |--[v] Node 2
     * |--[v] Node 3
     *
     * Legend:
     * - [ ] - node in the structure
     * - [v] - node in the provided list
     *
     * Resulting list would be:
     * - Node 1
     * - Node 1.1
     * - Node 1.2
     * - Node 2
     * - Node 3
     */
    all,

    /**
     * Accept only ancestors.
     * Any descendants will be removed.
     *
     * For example if we have this nodes structure:
     * [ ] Root node
     * |--[v] Node 1
     * |   |--[v] Node 1.1
     * |   |--[v] Node 1.2
     * |--[v] Node 2
     * |--[v] Node 3
     *
     * Legend:
     * - [ ] - node in the structure
     * - [v] - node in the provided list
     *
     * Resulting list would be:
     * - Node 1
     * - Node 2
     * - Node 3
     */
    ancestors,

    /**
     * Accept only descendants.
     * Any ancestors will be removed.
     *
     * For example if we have this nodes structure:
     * [ ] Root node
     * |--[v] Node 1
     * |   |--[v] Node 1.1
     * |   |--[v] Node 1.2
     * |--[v] Node 2
     * |--[v] Node 3
     *
     * Legend:
     * - [ ] - node in the structure
     * - [v] - node in the provided list
     *
     * Resulting list would be:
     * - Node 1.1
     * - Node 1.2
     * - Node 2
     * - Node 3
     */
    descendants;

    /**
     * Returns list of nodes filtered based on this {@link NodesAcceptPolicy}.
     *
     * @param nodes list of nodes to filter
     * @param <E>   nodes type
     * @return list of nodes filtered based on this {@link NodesAcceptPolicy}
     */
    public <E extends DefaultMutableTreeNode> List<E> filter ( final List<E> nodes )
    {
        if ( this != all )
        {
            for ( int i = nodes.size () - 1; i >= 0; i-- )
            {
                final E node = nodes.get ( i );
                for ( final E other : nodes )
                {
                    if ( other != node )
                    {
                        if ( this == ancestors && node.isNodeAncestor ( other ) )
                        {
                            nodes.remove ( i );
                            break;
                        }
                        else if ( this == descendants && node.isNodeDescendant ( other ) )
                        {
                            nodes.remove ( i );
                            break;
                        }
                    }
                }
            }
        }
        return nodes;
    }
}