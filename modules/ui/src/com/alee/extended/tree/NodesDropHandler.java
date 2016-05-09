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
import com.alee.managers.log.Log;
import com.alee.utils.CollectionUtils;

import javax.swing.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Tree nodes drop handler implementation.
 *
 * @param <N> nodes type
 * @param <T> tree type
 * @author Mikle Garin
 */

public class NodesDropHandler<N extends UniqueNode, T extends WebTree<N>> implements TreeDropHandler<N, T>
{
    /**
     * Supported flavors.
     */
    protected ArrayList<DataFlavor> flavors;

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
    public boolean canDrop ( final TransferHandler.TransferSupport support, final T tree, final N destination )
    {
        try
        {
            final List<N> nodes = ( List<N> ) support.getTransferable ().getTransferData ( NodesTransferable.FLAVOR );
            final JTree.DropLocation dl = ( JTree.DropLocation ) support.getDropLocation ();
            return canBeDropped ( tree, nodes, destination, dl.getChildIndex () );
        }
        catch ( final UnsupportedFlavorException ufe )
        {
            Log.warn ( this, "UnsupportedFlavor: " + ufe.getMessage () );
            return false;
        }
        catch ( final IOException ioe )
        {
            Log.error ( this, "I/O exception: " + ioe.getMessage () );
            return false;
        }
    }

    /**
     * Returns whether nodes can be dropped to the specified location and index or not.
     *
     * @param tree         destination tree
     * @param nodes        list of nodes to drop
     * @param dropLocation node onto which drop was performed
     * @param dropIndex    drop index if dropped between nodes under dropLocation node or -1 if dropped directly onto dropLocation node
     * @return true if nodes can be dropped to the specified location and index, false otherwise
     */
    protected boolean canBeDropped ( final T tree, final List<N> nodes, final N dropLocation, final int dropIndex )
    {
        return true;
    }

    @Override
    public List<N> getDroppedNodes ( final TransferHandler.TransferSupport support, final T tree, final N destination )
    {
        try
        {
            return ( List<N> ) support.getTransferable ().getTransferData ( NodesTransferable.FLAVOR );
        }
        catch ( final UnsupportedFlavorException ufe )
        {
            Log.warn ( this, "UnsupportedFlavor: " + ufe.getMessage () );
            return null;
        }
        catch ( final IOException ioe )
        {
            Log.error ( this, "I/O exception: " + ioe.getMessage () );
            return null;
        }
    }
}