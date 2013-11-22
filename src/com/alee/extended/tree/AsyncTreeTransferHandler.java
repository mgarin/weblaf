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

import com.alee.laf.tree.TreeUtils;

import javax.swing.*;
import javax.swing.tree.TreePath;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Custom TransferHandler for WebAsyncTree that provides a quick and convenient way to implement nodes DnD.
 *
 * @author Mikle Garin
 */

public abstract class AsyncTreeTransferHandler<T extends AsyncUniqueNode> extends TransferHandler
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
     * Array of dragged nodes that should be removed at the end of DnD operation.
     */
    protected T[] nodesToRemove;

    /**
     * Constructs new async tree transfer handler.
     *
     * @param nodesArrayClass nodes array class type
     */
    public AsyncTreeTransferHandler ( final Class<T[]> nodesArrayClass )
    {
        super ();
        try
        {
            nodesFlavor = new DataFlavor ( DataFlavor.javaJVMLocalObjectMimeType + ";class=\"" + nodesArrayClass.getName () + "\"" );
            flavors[ 0 ] = nodesFlavor;
        }
        catch ( ClassNotFoundException e )
        {
            System.out.println ( "ClassNotFound: " + e.getMessage () );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean canImport ( final TransferHandler.TransferSupport support )
    {
        if ( !support.isDrop () )
        {
            return false;
        }

        // Do not allow drop if flavor is not supported
        if ( !support.isDataFlavorSupported ( nodesFlavor ) )
        {
            return false;
        }

        // Do not allow a drop on busy node as that might break tree model
        // Do not allow a drop on a failed node as it is already messed
        final JTree.DropLocation dl = ( JTree.DropLocation ) support.getDropLocation ();
        final T target = ( T ) dl.getPath ().getLastPathComponent ();
        if ( target.isLoading () || target.isFailed () )
        {
            return false;
        }

        try
        {
            // Do not allow drop inside one of dragged elements
            // Will not work when dragged to another tree, but it doesn't matter in that case
            if ( nodesToRemove != null )
            {
                for ( final T node : nodesToRemove )
                {
                    if ( target == node || target.isNodeAncestor ( node ) )
                    {
                        return false;
                    }
                }
            }

            // Perform the actual drop check
            final T[] nodes = ( T[] ) support.getTransferable ().getTransferData ( nodesFlavor );
            final boolean canBeDropped = canBeDropped ( nodes, target, dl.getChildIndex () );

            // Displaying drop location
            support.setShowDropLocation ( canBeDropped );

            return canBeDropped;
        }
        catch ( UnsupportedFlavorException ufe )
        {
            System.out.println ( "UnsupportedFlavor: " + ufe.getMessage () );
            return false;
        }
        catch ( IOException ioe )
        {
            System.out.println ( "I/O error: " + ioe.getMessage () );
            return false;
        }
    }

    /**
     * Returns whether nodes can be dropped to the specified location and index or not.
     *
     * @param nodes        nodes to drop
     * @param dropLocation node onto which drop was performed
     * @param dropIndex    drop index if dropped between nodes unde dropLocation node or -1 if dropped directly onto dropLocation node
     * @return true if nodes can be dropped to the specified location and index, false otherwise
     */
    protected abstract boolean canBeDropped ( T[] nodes, T dropLocation, int dropIndex );

    /**
     * Creates a Transferable to use as the source for a data transfer.
     * Returns the representation of the data to be transferred, or null if the component's property is null
     *
     * @param c the component holding the data to be transferred, provided to enable sharing of TransferHandlers
     * @return the representation of the data to be transferred, or null if the property associated with component is null
     */
    @Override
    protected Transferable createTransferable ( final JComponent c )
    {
        final WebAsyncTree<T> tree = ( WebAsyncTree<T> ) c;
        final List<T> nodes = tree.getSelectedNodes ();
        if ( !nodes.isEmpty () )
        {
            // Do not allow root node export
            if ( nodes.contains ( tree.getRootNode () ) )
            {
                return null;
            }

            // Optimizing dragged nodes
            TreeUtils.optimizeNodes ( nodes );

            if ( !canBeDragged ( nodes ) )
            {
                return null;
            }

            // Creating copies
            final List<T> copies = new ArrayList<T> ();
            for ( final T node : nodes )
            {
                copies.add ( copy ( node ) );
            }

            // Saving list of nodes to be deleted if operation succeed
            nodesToRemove = toArray ( nodes );

            // Returning new nodes transferable
            return new NodesTransferable ( toArray ( copies ) );
        }
        return null;
    }

    protected abstract boolean canBeDragged ( List<T> nodes );

    /**
     * Translates nodes list into array and returns it.
     * This one have to be defined outside of this class since we are not able to create typed array here.
     *
     * @param nodes nodes list to translate
     * @return nodes array
     */
    protected abstract T[] toArray ( List<T> nodes );

    /**
     * Returns defensive node copy used in createTransferable.
     * Used each time when node is moved within tree or into another tree.
     *
     * @param node node to copy
     * @return node copy
     */
    protected abstract T copy ( final T node );

    /**
     * Invoked after data has been exported.
     * This method should remove the data that was transferred if the action was MOVE.
     *
     * @param source the component that was the source of the data
     * @param data   the data that was transferred or possibly null if the action is NONE
     * @param action the actual action that was performed
     */
    @Override
    protected void exportDone ( final JComponent source, final Transferable data, final int action )
    {
        if ( ( action & MOVE ) == MOVE )
        {
            // Removing nodes saved in nodesToRemove in createTransferable
            final WebAsyncTree<T> tree = ( WebAsyncTree<T> ) source;
            final AsyncTreeModel<T> model = ( AsyncTreeModel<T> ) tree.getModel ();
            model.removeNodesFromParent ( nodesToRemove );
        }
    }

    /**
     * Returns the type of transfer actions supported by the source.
     * Any bitwise-OR combination of {@code COPY}, {@code MOVE} and {@code LINK}.
     * Returning {@code NONE} disables transfers from the component.
     *
     * @param c the component holding the data to be transferred
     * @return type of transfer actions supported by the source
     */
    @Override
    public int getSourceActions ( final JComponent c )
    {
        return MOVE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean importData ( final TransferHandler.TransferSupport support )
    {
        // Checking whether we can perform the import or not
        if ( !canImport ( support ) )
        {
            return false;
        }

        // Extracting transfer data.
        final T[] nodes;
        try
        {
            nodes = ( T[] ) support.getTransferable ().getTransferData ( nodesFlavor );
        }
        catch ( UnsupportedFlavorException ufe )
        {
            System.out.println ( "UnsupportedFlavor: " + ufe.getMessage () );
            return false;
        }
        catch ( java.io.IOException ioe )
        {
            System.out.println ( "I/O error: " + ioe.getMessage () );
            return false;
        }

        // Getting drop location info
        final JTree.DropLocation dl = ( JTree.DropLocation ) support.getDropLocation ();
        final int childIndex = dl.getChildIndex ();
        final TreePath dest = dl.getPath ();
        final T parent = ( T ) dest.getLastPathComponent ();
        final WebAsyncTree<T> tree = ( WebAsyncTree<T> ) support.getComponent ();
        final AsyncTreeModel<T> model = ( AsyncTreeModel<T> ) tree.getModel ();

        // Acting differently in case parent node childs are not yet loaded
        // We don't want to modify model (insert childs) before existing childs are actually loaded
        if ( parent.isLoaded () )
        {
            // Adding data to model
            model.insertNodesInto ( nodes, parent, childIndex == -1 ? parent.getChildCount () : childIndex );

            // Selecting inserted nodes
            tree.setSelectedNodes ( nodes );
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
                        model.insertNodesInto ( nodes, parent, parent.getChildCount () );

                        // Selecting inserted nodes
                        tree.setSelectedNodes ( nodes );

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

        // Expanding node into which drop was performed
        //        tree.expandNode ( parent );

        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString ()
    {
        return getClass ().getName ();
    }

    /**
     * Custom nodes transferable used for D&D operation.
     */
    public class NodesTransferable implements Transferable, Serializable
    {
        /**
         * Transferred nodes.
         */
        protected final T[] nodes;

        /**
         * Constructs new nodes transferable with the specified nodes as data.
         *
         * @param nodes transferred nodes
         */
        public NodesTransferable ( final T[] nodes )
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