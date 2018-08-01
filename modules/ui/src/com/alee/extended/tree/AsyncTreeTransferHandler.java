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
 * Custom {@link javax.swing.TransferHandler} implementation for {@link WebAsyncTree} that provides a quick and convenient way to implement
 * drag and drop for {@link AsyncUniqueNode}s within the tree or between different trees using the same type of {@link AsyncUniqueNode}s.
 *
 * @param <N> {@link AsyncUniqueNode} type
 * @param <T> {@link WebAsyncTree} type
 * @param <M> {@link AsyncTreeModel} type
 * @author Mikle Garin
 */
public abstract class AsyncTreeTransferHandler<N extends AsyncUniqueNode, T extends WebAsyncTree<N>, M extends AsyncTreeModel<N>>
        extends AbstractTreeTransferHandler<N, T, M>
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

    /**
     * In addition to {@link AbstractTreeTransferHandler} this method:
     * - Doesn't allow drop on busy nodes as that might break tree model
     * - Doesn't allow drop on a failed nodes as they are considered to have no chidren
     */
    @Override
    protected boolean canDropTo ( final TransferSupport support, final T tree, final M model, final N destination )
    {
        return super.canDropTo ( support, tree, model, destination ) && !destination.isLoading () && !destination.isFailed ();
    }

    @Override
    protected boolean prepareDropOperation ( final TransferSupport support, final T tree, final M model, final N parent,
                                             final int dropIndex )
    {
        if ( allowUncheckedDrop )
        {
            // Acting differently in case parent node children are not yet loaded
            // We don't want to modify model (insert children) before existing children are actually loaded
            if ( parent.isLoaded () )
            {
                // Expanding parent first
                if ( !tree.isExpanded ( parent ) )
                {
                    tree.expandNode ( parent );
                }

                // Adjust drop index after we ensure parent is loaded and expanded
                final int adjustedDropIndex = getAdjustedDropIndex ( dropIndex, support.getDropAction (), parent );

                // Adding data to model
                // Performing it in a separate non-EDT thread
                AsyncTreeQueue.getInstance ( tree ).execute ( new Runnable ()
                {
                    @Override
                    public void run ()
                    {
                        performDropOperation ( support, tree, model, parent, adjustedDropIndex );
                    }
                } );
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
                            // Performing it in a separate non-EDT thread
                            AsyncTreeQueue.getInstance ( tree ).execute ( new Runnable ()
                            {
                                @Override
                                public void run ()
                                {
                                    performDropOperation ( support, tree, model, parent, parent.getChildCount () );
                                }
                            } );

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
            return parent.isLoaded () && performDropOperation ( support, tree, model, parent,
                    getAdjustedDropIndex ( dropIndex, support.getDropAction (), parent ) );
        }
    }
}