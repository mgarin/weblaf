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

import com.alee.laf.tree.WebTree;
import com.alee.managers.log.Log;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Mikle Garin
 */

public class AbstractTreeTransferHandler<N extends DefaultMutableTreeNode, T extends WebTree<N>> extends TransferHandler
{
    /**
     * Nodes flavor.
     */
    protected DataFlavor nodesFlavor;

    /**
     * Nodes flavor array.
     */
    protected DataFlavor[] flavors = new DataFlavor[ 1 ];

    /**
     * Constructs new tree transfer handler.
     */
    public AbstractTreeTransferHandler ()
    {
        super ();
        nodesFlavor = createNodesFlavor ();
        flavors[ 0 ] = nodesFlavor;
    }

    /**
     * Creates nodes transferable flavor.
     *
     * @return nodes transferable flavor
     */
    protected DataFlavor createNodesFlavor ()
    {
        try
        {
            return new DataFlavor ( DataFlavor.javaJVMLocalObjectMimeType + ";class=\"" + List.class.getName () + "\"" );
        }
        catch ( final ClassNotFoundException e )
        {
            Log.error ( this, e );
            return null;
        }
    }

    /**
     * Returns user objects collected from specified nodes.
     *
     * @param nodes list of nodes to collect user objects from
     * @param <O>   user object type
     * @return user objects collected from specified nodes
     */
    public <O> List<O> collect ( final List<N> nodes )
    {
        final List<O> objects = new ArrayList<O> ( nodes.size () );
        for ( final N node : nodes )
        {
            objects.add ( ( O ) node.getUserObject () );
        }
        return objects;
    }

    /**
     * Custom nodes transferable used for D&D operation.
     */
    public class NodesTransferable implements Transferable, Serializable
    {
        /**
         * Transferred nodes.
         */
        protected final List<N> nodes;

        /**
         * Constructs new nodes transferable with the specified nodes as data.
         *
         * @param nodes transferred nodes
         */
        public NodesTransferable ( final List<N> nodes )
        {
            super ();
            this.nodes = nodes;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public Object getTransferData ( final DataFlavor flavor ) throws UnsupportedFlavorException
        {
            if ( !isDataFlavorSupported ( flavor ) )
            {
                throw new UnsupportedFlavorException ( flavor );
            }
            return nodes;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public DataFlavor[] getTransferDataFlavors ()
        {
            return flavors;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public boolean isDataFlavorSupported ( final DataFlavor flavor )
        {
            return nodesFlavor.equals ( flavor );
        }
    }
}