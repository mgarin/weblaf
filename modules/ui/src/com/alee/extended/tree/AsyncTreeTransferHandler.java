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

import com.alee.api.annotations.NotNull;
import com.alee.managers.task.TaskManager;

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
    protected boolean canDropTo ( @NotNull final TransferSupport support, @NotNull final T tree, @NotNull final M model,
                                  @NotNull final N destination )
    {
        return !destination.isLoading () && !destination.isFailed ();
    }

    @Override
    protected boolean prepareDropOperation ( @NotNull final TransferSupport support, @NotNull final T tree, @NotNull final M model,
                                             @NotNull final N destination, final int dropIndex )
    {
        final boolean prepared;
        if ( allowUncheckedDrop )
        {
            // Acting differently in case parent node children are not yet loaded
            // We don't want to modify model (insert children) before existing children are actually loaded
            final AsyncTreeDataProvider<N> dataProvider = tree.getDataProvider ();
            if ( dataProvider != null )
            {
                if ( destination.isLoaded () )
                {
                    // Expanding parent first
                    if ( !tree.isExpanded ( destination ) )
                    {
                        tree.expandNode ( destination );
                    }

                    // Adjust drop index after we ensure parent is loaded and expanded
                    final int adjustedDropIndex = getAdjustedDropIndex ( support, tree, model, destination, dropIndex );

                    // Adding data to model
                    // Performing it in a separate non-EDT thread
                    TaskManager.execute ( dataProvider.getThreadGroupId (), new Runnable ()
                    {
                        @Override
                        public void run ()
                        {
                            performDropOperation ( support, tree, model, destination, adjustedDropIndex );
                        }
                    } );
                }
                else
                {
                    // Loading children first
                    tree.addAsyncTreeListener ( new AsyncTreeAdapter<N> ()
                    {
                        @Override
                        public void loadCompleted ( @NotNull final N parent, @NotNull final List<N> children )
                        {
                            if ( parent == destination )
                            {
                                // Adding data to model
                                // Performing it in a separate non-EDT thread
                                TaskManager.execute ( dataProvider.getThreadGroupId (), new Runnable ()
                                {
                                    @Override
                                    public void run ()
                                    {
                                        performDropOperation ( support, tree, model, destination, destination.getChildCount () );
                                    }
                                } );

                                // Removing listener
                                tree.removeAsyncTreeListener ( this );
                            }
                        }

                        @Override
                        public void loadFailed ( @NotNull final N parent, @NotNull final Throwable cause )
                        {
                            if ( parent == destination )
                            {
                                // Removing listener
                                tree.removeAsyncTreeListener ( this );
                            }
                        }
                    } );
                    tree.reloadNode ( destination );
                }
                prepared = true;
            }
            else
            {
                // Cannot proceed when target tree doesn't have data provider installed
                prepared = false;
            }
        }
        else
        {
            // We have to load children synchronously, otherwise we cannot say for sure if drop succeed or not
            if ( !destination.isLoaded () )
            {
                tree.reloadNodeSync ( destination );
            }

            // If children were loaded right away with our attempt - perform the drop
            prepared = destination.isLoaded () && performDropOperation (
                    support,
                    tree,
                    model,
                    destination,
                    getAdjustedDropIndex ( support, tree, model, destination, dropIndex )
            );
        }
        return prepared;
    }
}