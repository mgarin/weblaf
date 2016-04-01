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

import java.util.List;

/**
 * Custom TransferHandler for WebAsyncTree that provides a quick and convenient way to implement nodes DnD.
 *
 * @param <N> nodes type
 * @param <T> tree type
 * @author Mikle Garin
 */

public abstract class AsyncTreeTransferHandler<N extends AsyncUniqueNode, T extends WebAsyncTree<N>>
        extends AbstractTreeTransferHandler<N, T, AsyncTreeModel<N>>
{
    /**
     * Whether should allow dropping nodes onto not-yet-loaded node or not.
     * Be aware that if this set to true and your tree might fail loading children - old nodes will still get removed on drop.
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

    @Override
    protected boolean canDropTo ( final T tree, final N destination )
    {
        // Do not allow a drop on busy node as that might break tree model
        // Do not allow a drop on a failed node as it is already messed
        return super.canDropTo ( tree, destination ) && !destination.isLoading () && !destination.isFailed ();
    }

    @Override
    protected boolean prepareDropOperation ( final TransferSupport support, final T tree, final List<N> nodes, final int dropIndex,
                                             final N parent, final AsyncTreeModel<N> model )
    {
        if ( allowUncheckedDrop )
        {
            // Acting differently in case parent node children are not yet loaded
            // We don't want to modify model (insert children) before existing children are actually loaded
            if ( parent.isLoaded () )
            {
                // Adding data to model
                performDropOperation ( tree, nodes, parent, model, getAdjustedDropIndex ( dropIndex, support.getDropAction (), parent ) );
            }
            else
            {
                // Loading children first
                tree.addAsyncTreeListener ( new AsyncTreeAdapter ()
                {
                    @Override
                    public void loadCompleted ( final AsyncUniqueNode loadedFor, final List children )
                    {
                        if ( loadedFor == parent )
                        {
                            // Adding data to model
                            performDropOperation ( tree, nodes, parent, model, parent.getChildCount () );

                            // Removing listener
                            tree.removeAsyncTreeListener ( this );
                        }
                    }

                    @Override
                    public void loadFailed ( final AsyncUniqueNode loadedFor, final Throwable cause )
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
            // We have to load children synchronously, otherwise we cannot say for sure if drop succeed or not
            if ( !parent.isLoaded () )
            {
                tree.reloadNodeSync ( parent );
            }

            // If children were loaded right away with our attempt - perform the drop
            return parent.isLoaded () && performDropOperation ( tree, nodes, parent, model,
                    getAdjustedDropIndex ( dropIndex, support.getDropAction (), parent ) );
        }
    }

    @Override
    protected void informNodesDropped ( final T tree, final List<N> nodes, final N parent, final AsyncTreeModel<N> model, final int index )
    {
        // Informing about drop in a async tree queue to perform it in a separate non-EDT thread
        AsyncTreeQueue.execute ( tree, new Runnable ()
        {
            @Override
            public void run ()
            {
                AsyncTreeTransferHandler.super.informNodesDropped ( tree, nodes, parent, model, index );
            }
        } );
    }
}