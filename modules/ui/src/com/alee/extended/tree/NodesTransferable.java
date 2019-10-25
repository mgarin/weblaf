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
import com.alee.laf.tree.UniqueNode;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.Serializable;
import java.util.List;

/**
 * Custom nodes transferable used for D&amp;D operation.
 *
 * @param <N> nodes type
 * @author Mikle Garin
 */
public class NodesTransferable<N extends UniqueNode> implements Transferable, Serializable
{
    /**
     * Nodes {@link DataFlavor}.
     */
    @NotNull
    public static DataFlavor FLAVOR;

    static
    {
        try
        {
            FLAVOR = new DataFlavor ( DataFlavor.javaJVMLocalObjectMimeType + ";class=\"" + List.class.getName () + "\"" );
        }
        catch ( final Exception e )
        {
            throw new RuntimeException ( "Unable to initialize DataFlavor for NodesTransferable", e );
        }
    }

    /**
     * Transferred nodes.
     */
    @NotNull
    protected final List<N> nodes;

    /**
     * Constructs new nodes transferable with the specified nodes as data.
     *
     * @param nodes transferred nodes
     */
    public NodesTransferable ( @NotNull final List<N> nodes )
    {
        this.nodes = nodes;
    }

    @NotNull
    @Override
    public DataFlavor[] getTransferDataFlavors ()
    {
        return new DataFlavor[]{ FLAVOR };
    }

    @Override
    public boolean isDataFlavorSupported ( @NotNull final DataFlavor flavor )
    {
        return FLAVOR.equals ( flavor );
    }

    @NotNull
    @Override
    public Object getTransferData ( @NotNull final DataFlavor flavor ) throws UnsupportedFlavorException
    {
        if ( !isDataFlavorSupported ( flavor ) )
        {
            throw new UnsupportedFlavorException ( flavor );
        }
        return nodes;
    }
}