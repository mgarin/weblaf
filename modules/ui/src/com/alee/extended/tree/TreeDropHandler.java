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
import com.alee.laf.tree.WebTree;
import com.alee.laf.tree.WebTreeModel;

import javax.swing.*;
import java.awt.datatransfer.DataFlavor;
import java.util.List;

/**
 * Base for any tree drop operation handler.
 *
 * @param <N> nodes type
 * @param <T> tree type
 * @param <M> tree model type
 * @author Mikle Garin
 * @see AbstractTreeTransferHandler#getDropHandler(javax.swing.TransferHandler.TransferSupport, com.alee.laf.tree.WebTree,
 * com.alee.laf.tree.WebTreeModel, com.alee.laf.tree.UniqueNode)
 */
public interface TreeDropHandler<N extends UniqueNode, T extends WebTree<N>, M extends WebTreeModel<N>>
{
    /**
     * Returns list of data flavors supported by this drop handler.
     * These will be used to filter out drop handlers for each specific drop operation.
     *
     * @return list of data flavors supported by this drop handler
     */
    public List<DataFlavor> getSupportedFlavors ();

    /**
     * Returns whether or not drop operation can be performed on the specified destination node.
     * Be aware that this method is called multiple times while drag operation is performed.
     * Avoid performing any heavy operations here as they will be called multiple times as well.
     *
     * @param support     transfer support data
     * @param tree        destination tree
     * @param model       tree model
     * @param destination drop destination node
     * @return true if drop operation can be performed on the specified destination node, false otherwise
     */
    public boolean canDrop ( TransferHandler.TransferSupport support, T tree, M model, N destination );

    /**
     * Returns whether or not drop operation can be performed on the specified destination node.
     * This method is called only once just before the drop operation gets completed and you can still cancel drop from here.
     * You can also perform any heavy synchronous checks here as this method is called only once.
     *
     * @param support     transfer support data
     * @param tree        destination tree
     * @param model       tree model
     * @param destination drop destination node
     * @param index       nodes drop index
     * @return true if drop operation can be performed on the specified destination node, false otherwise
     */
    public boolean prepareDrop ( TransferHandler.TransferSupport support, T tree, M model, N destination, int index );

    /**
     * Performs nodes drop operations.
     * Those might be either extracted from or created based on transferred data.
     * This method basically maps any kind of transferred data into tree nodes.
     *
     * @param support     transfer support data
     * @param tree        destination tree
     * @param model       tree model
     * @param destination drop destination node
     * @param index       nodes drop index
     * @param callback    operation callback
     */
    public void performDrop ( TransferHandler.TransferSupport support, T tree, M model, N destination, int index,
                              NodesDropCallback<N> callback );
}