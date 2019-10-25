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
import com.alee.api.annotations.Nullable;
import com.alee.laf.tree.UniqueNode;
import com.alee.laf.tree.WebTree;
import com.alee.laf.tree.WebTreeModel;
import com.alee.utils.CollectionUtils;

import javax.swing.*;
import java.awt.datatransfer.DataFlavor;
import java.util.ArrayList;
import java.util.List;

/**
 * Tree nodes drop handler implementation.
 *
 * @param <N> nodes type
 * @param <T> tree type
 * @param <M> tree model type
 * @author Mikle Garin
 */
public class NodesDropHandler<N extends UniqueNode, T extends WebTree<N>, M extends WebTreeModel<N>> implements TreeDropHandler<N, T, M>
{
    /**
     * Supported flavors.
     */
    @Nullable
    protected ArrayList<DataFlavor> flavors;

    @NotNull
    @Override
    public List<DataFlavor> getSupportedFlavors ()
    {
        if ( flavors == null )
        {
            flavors = CollectionUtils.asList ( NodesTransferable.FLAVOR );
        }
        return flavors;
    }

    @Override
    public boolean canDrop ( @NotNull final TransferHandler.TransferSupport support, @NotNull final T tree, @NotNull final M model,
                             @NotNull final N destination )
    {
        boolean canDrop;
        try
        {
            // Checking possibility to drop nodes
            final List<N> nodes = ( List<N> ) support.getTransferable ().getTransferData ( NodesTransferable.FLAVOR );
            final JTree.DropLocation dl = ( JTree.DropLocation ) support.getDropLocation ();
            canDrop = canDrop ( support, tree, model, destination, dl.getChildIndex (), nodes );
        }
        catch ( final Exception ufe )
        {
            // Simply ignore any issues here
            // We are only checking possibility to drop, anything could go wrong
            canDrop = false;
        }
        return canDrop;
    }

    /**
     * Returns whether nodes can be dropped to the specified location and index or not.
     * Be aware that this method is called multiple times while drag operation is performed.
     * Avoid performing any heavy operations here as they will be called multiple times as well.
     *
     * @param support     transfer support data
     * @param tree        destination tree
     * @param model       tree model
     * @param destination node onto which drop was performed
     * @param dropIndex   drop index if dropped between nodes under dropLocation node or -1 if dropped directly onto dropLocation node
     * @param nodes       list of nodes to drop
     * @return true if nodes can be dropped to the specified location and index, false otherwise
     */
    protected boolean canDrop ( @NotNull final TransferHandler.TransferSupport support, @NotNull final T tree, @NotNull final M model,
                                @NotNull final N destination, final int dropIndex, @NotNull final List<N> nodes )
    {
        return true;
    }

    @Override
    public boolean prepareDrop ( @NotNull final TransferHandler.TransferSupport support, @NotNull final T tree, @NotNull final M model,
                                 @NotNull final N destination, final int index )
    {
        boolean prepared;
        try
        {
            // Checking possibility to drop nodes
            final List<N> nodes = ( List<N> ) support.getTransferable ().getTransferData ( NodesTransferable.FLAVOR );
            final JTree.DropLocation dl = ( JTree.DropLocation ) support.getDropLocation ();
            prepared = prepareDrop ( support, tree, model, destination, dl.getChildIndex (), nodes );
        }
        catch ( final Exception ufe )
        {
            // Simply ignore any issues here
            // We are only checking possibility to drop, anything could go wrong
            prepared = false;
        }
        return prepared;
    }

    /**
     * Returns whether nodes can be dropped to the specified location and index or not.
     * This method is called only once just before the drop operation gets completed and you can still cancel drop from here.
     * You can also perform any heavy synchronous checks here as this method is called only once.
     *
     * @param support     transfer support data
     * @param tree        destination tree
     * @param model       tree model
     * @param destination node onto which drop was performed
     * @param dropIndex   drop index if dropped between nodes under dropLocation node or -1 if dropped directly onto dropLocation node
     * @param nodes       list of nodes to drop
     * @return true if nodes can be dropped to the specified location and index, false otherwise
     */
    protected boolean prepareDrop ( @NotNull final TransferHandler.TransferSupport support, @NotNull final T tree, @NotNull final M model,
                                    @NotNull final N destination, final int dropIndex, @NotNull final List<N> nodes )
    {
        return canDrop ( support, tree, model, destination, dropIndex, nodes );
    }

    @Override
    public void performDrop ( @NotNull final TransferHandler.TransferSupport support, @NotNull final T tree, @NotNull final M model,
                              @NotNull final N destination, final int index, @NotNull final NodesDropCallback<N> callback )
    {
        try
        {
            // Perform nodes drop
            final List<N> nodes = ( List<N> ) support.getTransferable ().getTransferData ( NodesTransferable.FLAVOR );
            callback.dropped ( nodes );
            callback.completed ();
        }
        catch ( final Exception e )
        {
            // Inform about drop issues
            callback.failed ( e );
        }
    }
}