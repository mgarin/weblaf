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

import javax.swing.*;
import java.awt.datatransfer.DataFlavor;
import java.util.List;

/**
 * Base for any tree drop operation handler.
 *
 * @param <N> nodes type
 * @param <T> tree type
 * @author Mikle Garin
 * @see com.alee.extended.tree.AbstractTreeTransferHandler#getSupportedDropHandlers(javax.swing.TransferHandler.TransferSupport)
 */

public interface TreeDropHandler<N extends UniqueNode, T extends WebTree<N>>
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
     *
     * @param support     transfer support data
     * @param tree        destination tree
     * @param destination drop destination node
     * @return true if drop operation can be performed on the specified destination node, false otherwise
     */
    public boolean canDrop ( TransferHandler.TransferSupport support, T tree, N destination );

    /**
     * Returns list of dropped nodes.
     * Those might be either extracted from or created based on transferred data.
     * This method basically maps any kind of transferred data into tree nodes.
     *
     * @param support     transfer support data
     * @param tree        destination tree
     * @param destination drop destination node
     * @return list of dropped nodes
     */
    public List<N> getDroppedNodes ( TransferHandler.TransferSupport support, T tree, N destination );
}