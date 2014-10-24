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

import javax.swing.*;
import java.util.List;

/**
 * Custom TransferHandler for WebAsyncTree that provides a quick and convenient way to implement nodes DnD.
 *
 * @author Mikle Garin
 */

public abstract class AsyncTreeTransferHandler<N extends AsyncUniqueNode, T extends WebAsyncTree<N>>
        extends AbstractTreeTransferHandler<N, T, AsyncTreeModel<N>>
{
    /**
     * Whether should allow dropping nodes onto not-yet-loaded node or not.
     * Be aware that if this set to true and your tree might fail loading childs - old nodes will still get removed on drop.
     * If set to false tree will try to load child nodes first and then perform the drop operation.
     */
    protected boolean allowUncheckedDrop = false;

    /**
     * Returns whether should allow dropping nodes onto not-yet-loaded node or not.
     *
     * @return true if should allow dropping nodes onto not-yet-loaded node, false otherwise
     */
    public boolean isAllowUncheckedDrop ()
    {
        return allowUncheckedDrop;
    }

    /**
     * Sets whether should allow dropping nodes onto not-yet-loaded node or not
     *
     * @param allowUncheckedDrop whether should allow dropping nodes onto not-yet-loaded node or not
     */
    public void setAllowUncheckedDrop ( final boolean allowUncheckedDrop )
    {
        this.allowUncheckedDrop = allowUncheckedDrop;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean canDropTo ( final N dropLocation )
    {
        // Do not allow a drop on busy node as that might break tree model
        // Do not allow a drop on a failed node as it is already messed
        return super.canDropTo ( dropLocation ) && !dropLocation.isLoading () && !dropLocation.isFailed ();
    }

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
                                             final T tree, final AsyncTreeModel<N> model )
    {
        if ( allowUncheckedDrop )
        {
            // Acting differently in case parent node childs are not yet loaded
            // We don't want to modify model (insert childs) before existing childs are actually loaded
            if ( parent.isLoaded () )
            {
                // Adding data to model
                performDropOperation ( nodes, parent, tree, model, getAdjustedDropIndex ( dropIndex, support.getDropAction (), parent ) );
            }
            else
            {
                // Loading childs first
                tree.addAsyncTreeListener ( new AsyncTreeAdapter ()
                {
                    @Override
                    public void childsLoadCompleted ( final AsyncUniqueNode loadedFor, final List childs )
                    {
                        if ( loadedFor == parent )
                        {
                            // Adding data to model
                            performDropOperation ( nodes, parent, tree, model, parent.getChildCount () );

                            // Removing listener
                            tree.removeAsyncTreeListener ( this );
                        }
                    }

                    @Override
                    public void childsLoadFailed ( final AsyncUniqueNode loadedFor, final Throwable cause )
                    {
                        if ( loadedFor == parent )
                        {
                            // Removing listener
                            tree.removeAsyncTreeListener ( this );
                        }
                    }
                } );
                tree.reloadNode ( parent );
            }
            return true;
        }
        else
        {
            // We have to load childs synchronously, otherwise we cannot say for sure if drop succeed or not
            if ( !parent.isLoaded () )
            {
                tree.reloadNodeSync ( parent );
            }

            // If childs were loaded right away with our attempt - perform the drop
            return parent.isLoaded () && performDropOperation ( nodes, parent, tree, model,
                    getAdjustedDropIndex ( dropIndex, support.getDropAction (), parent ) );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean performDropOperation ( final List<N> nodes, final N parent, final T tree, final AsyncTreeModel<N> model,
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

                // Informing about drop in a separate thread
                AsyncTreeQueue.execute ( tree, new Runnable ()
                {
                    @Override
                    public void run ()
                    {
                        nodesDropped ( nodes, parent, tree, model, index );
                    }
                } );
            }
        } );
        return true;
    }
}