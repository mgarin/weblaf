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

import com.alee.laf.tree.UniqueNode;

import javax.swing.*;
import java.util.List;

/**
 * Custom TransferHandler for WebAsyncTree that provides a quick and convenient way to implement nodes DnD.
 *
 * @author Mikle Garin
 */

public abstract class ExTreeTransferHandler<N extends UniqueNode, T extends WebExTree<N>>
        extends AbstractTreeTransferHandler<N, T, ExTreeModel<N>>
{
    /**
     * {@inheritDoc}
     */
    @Override
    protected void removeTreeNodes ( final T tree, final List<N> nodesToRemove )
    {
        tree.removeNodes ( nodesToRemove );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean prepareDropOperation ( final TransferSupport support, final List<N> nodes, final int dropIndex, final N parent,
                                             final T tree, final ExTreeModel<N> model )
    {
        // Expanding parent first
        if ( !tree.isExpanded ( parent ) )
        {
            tree.expandNode ( parent );
        }

        // Adjust drop index after we ensure parent is expanded
        final int adjustedDropIndex = getAdjustedDropIndex ( dropIndex, support.getDropAction (), parent );

        // Now we can perform drop
        return performDropOperation ( nodes, parent, tree, model, adjustedDropIndex );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean performDropOperation ( final List<N> nodes, final N parent, final T tree, final ExTreeModel<N> model,
                                             final int index )
    {
        // This operation should be performed in EDT later to allow drop operation get completed in source TransferHandler first
        // Otherwise new nodes will be added into the tree before old ones are removed which is bad if it is the same tree
        // This is meaningful for D&D opearation within one tree, for other situations its meaningless but doesn't cause any problems
        SwingUtilities.invokeLater ( new Runnable ()
        {
            @Override
            public void run ()
            {
                // Adding data to model
                model.insertNodesInto ( nodes, parent, index );

                // Expanding nodes after drop operation
                if ( expandSingleNode && nodes.size () == 1 )
                {
                    tree.expandNode ( nodes.get ( 0 ) );
                }
                else if ( expandMultiplyNodes )
                {
                    for ( final N node : nodes )
                    {
                        tree.expandNode ( node );
                    }
                }

                // Selecting inserted nodes
                tree.setSelectedNodes ( nodes );

                // Informing about drop
                nodesDropped ( nodes, parent, tree, model, index );
            }
        } );
        return true;
    }
}