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

import javax.swing.*;
import javax.swing.tree.TreeNode;
import java.util.List;

/**
 * Provides a way to conveniently filter out unwanted nodes from the provided list based on the specific policy.
 *
 * @author Mikle Garin
 * @see WebTree#getSelectedNodes(NodesAcceptPolicy)
 * @see WebTree#getSelectedUserObjects(NodesAcceptPolicy)
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
     * Returns list of {@link TreeNode}s filtered based on this {@link NodesAcceptPolicy}.
     *
     * @param tree  containing specified {@link TreeNode}s
     * @param nodes list of {@link TreeNode}s to filter
     * @param <N>   {@link TreeNode} type
     * @return list of {@link TreeNode}s filtered based on this {@link NodesAcceptPolicy}
     */
    public <N extends TreeNode> List<N> filter ( final JTree tree, final List<N> nodes )
    {
        if ( this != all )
        {
            for ( int i = nodes.size () - 1; i >= 0; i-- )
            {
                final N node = nodes.get ( i );
                for ( final N other : nodes )
                {
                    if ( other != node )
                    {
                        if ( this == ancestors && TreeUtils.isNodeAncestor ( tree, node, other ) )
                        {
                            nodes.remove ( i );
                            break;
                        }
                        else if ( this == descendants && TreeUtils.isNodeAncestor ( tree, node, other ) )
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