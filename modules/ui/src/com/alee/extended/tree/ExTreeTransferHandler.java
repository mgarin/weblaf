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
import com.alee.laf.tree.UniqueNode;
import com.alee.managers.log.Log;

import javax.swing.*;
import javax.swing.tree.TreePath;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Custom TransferHandler for WebAsyncTree that provides a quick and convenient way to implement nodes DnD.
 *
 * @author Mikle Garin
 */

public abstract class ExTreeTransferHandler<N extends UniqueNode, T extends WebExTree<N>> extends AbstractTreeTransferHandler<N, T>
{
    /**
     * Whether or not should optimize dragged nodes list to minimum.
     */
    protected boolean optimizeDraggedNodes = true;

    /**
     * Array of dragged nodes that should be removed at the end of DnD operation.
     */
    protected List<N> nodesToRemove;

    /**
     * Map of removed node indices lists under their parent node IDs.
     * This map is used to properly adjust drop index in the destination node if D&D performed within one tree.
     */
    protected Map<String, List<Integer>> removedUnder;

    /**
     * Return whether or not should optimize dragged nodes list to minimum.
     *
     * @return true if should optimize dragged nodes list to minimum, false otherwise
     */
    public boolean isOptimizeDraggedNodes ()
    {
        return optimizeDraggedNodes;
    }

    /**
     * Sets whether or not should optimize dragged nodes list to minimum.
     *
     * @param optimize whether or not should optimize dragged nodes list to minimum
     */
    public void setOptimizeDraggedNodes ( final boolean optimize )
    {
        this.optimizeDraggedNodes = optimize;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean canImport ( final TransferSupport support )
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
        final TreePath path = dl.getPath ();
        if ( path == null )
        {
            return false;
        }

        try
        {
            final N target = ( N ) path.getLastPathComponent ();

            // Do not allow drop inside one of dragged elements
            // Will not work when dragged to another tree, but it doesn't matter in that case
            if ( nodesToRemove != null )
            {
                for ( final N node : nodesToRemove )
                {
                    if ( target == node || target.isNodeAncestor ( node ) )
                    {
                        return false;
                    }
                }
            }

            // Perform the actual drop check
            final List<N> nodes = ( List<N> ) support.getTransferable ().getTransferData ( nodesFlavor );
            final boolean canBeDropped = canBeDropped ( nodes, target, dl.getChildIndex () );

            // Displaying drop location
            support.setShowDropLocation ( canBeDropped );

            return canBeDropped;
        }
        catch ( final UnsupportedFlavorException ufe )
        {
            Log.warn ( this, "UnsupportedFlavor: " + ufe.getMessage () );
            return false;
        }
        catch ( final IOException ioe )
        {
            Log.error ( this, "I/O error: " + ioe.getMessage () );
            return false;
        }
    }

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
        final T tree = ( T ) c;
        final List<N> nodes = tree.getSelectedNodes ();
        if ( !nodes.isEmpty () )
        {
            // Do not allow root node export
            if ( nodes.contains ( tree.getRootNode () ) )
            {
                return null;
            }

            // Optimizing dragged nodes
            if ( optimizeDraggedNodes )
            {
                TreeUtils.optimizeNodes ( nodes );
            }

            if ( !canBeDragged ( nodes ) )
            {
                return null;
            }

            // Creating copies
            final List<N> copies = new ArrayList<N> ();
            for ( final N node : nodes )
            {
                copies.add ( copy ( node ) );
            }

            // Saving list of nodes to be deleted if operation succeed
            nodesToRemove = nodes;

            // todo Do not correct for COPY
            // Collecting removed node indices under their parent nodes
            removedUnder = new HashMap<String, List<Integer>> ( 1 );
            for ( final N node : nodesToRemove )
            {
                final UniqueNode parent = node.getParent ();
                List<Integer> indices = removedUnder.get ( parent.getId () );
                if ( indices == null )
                {
                    indices = new ArrayList<Integer> ( 1 );
                    removedUnder.put ( parent.getId (), indices );
                }
                indices.add ( parent.getIndex ( node ) );
            }

            // Returning new nodes transferable
            return new NodesTransferable ( copies );
        }
        return null;
    }

    /**
     * Invoked after data has been exported.
     * This method should remove the data that was transferred if the action was MOVE.
     * This method is invoked from EDT so it is safe to perform tree operations.
     *
     * @param source the component that was the source of the data
     * @param data   the data that was transferred or possibly null if the action is NONE
     * @param action the actual action that was performed
     */
    @Override
    protected void exportDone ( final JComponent source, final Transferable data, final int action )
    {
        if ( isMoveAction ( action ) )
        {
            // Removing nodes saved in nodesToRemove in createTransferable
            final T tree = ( T ) source;
            tree.removeNodes ( nodesToRemove );
            nodesToRemove = null;
        }
    }

    /**
     * Returns whether action is MOVE or not.
     *
     * @param action drag action
     * @return true if action is MOVE, false otherwise
     */
    protected boolean isMoveAction ( final int action )
    {
        return ( action & MOVE ) == MOVE;
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
    public boolean importData ( final TransferSupport support )
    {
        // Checking whether we can perform the import or not
        if ( !canImport ( support ) )
        {
            return false;
        }

        // Extracting transfer data.
        final List<N> nodes;
        try
        {
            nodes = ( List<N> ) support.getTransferable ().getTransferData ( nodesFlavor );
        }
        catch ( final UnsupportedFlavorException ufe )
        {
            Log.warn ( this, "UnsupportedFlavor: " + ufe.getMessage () );
            return false;
        }
        catch ( final IOException ioe )
        {
            Log.error ( this, "I/O error: " + ioe.getMessage () );
            return false;
        }

        // Getting drop location info
        final JTree.DropLocation dl = ( JTree.DropLocation ) support.getDropLocation ();
        final int dropIndex = dl.getChildIndex ();
        final TreePath dest = dl.getPath ();
        final N parent = ( N ) dest.getLastPathComponent ();
        final T tree = ( T ) support.getComponent ();
        final ExTreeModel<N> model = ( ExTreeModel<N> ) tree.getModel ();

        // Adding data to model
        performDropOperation ( nodes, parent, tree, model, getAdjustedDropIndex ( dropIndex, parent ) );
        return true;
    }

    /**
     * Returns properly adjusted nodes drop index.
     *
     * @param dropIndex drop index if dropped between nodes under dropLocation node or -1 if dropped directly onto dropLocation node
     * @param parent    parent node to drop nodes into
     * @return properly adjusted nodes drop index
     */
    protected int getAdjustedDropIndex ( final int dropIndex, final N parent )
    {
        // todo Do not correct for COPY
        // Adjusting drop index
        int adjustedDropIndex = dropIndex == -1 ? parent.getChildCount () : dropIndex;
        if ( removedUnder.containsKey ( parent.getId () ) )
        {
            for ( final Integer index : removedUnder.get ( parent.getId () ) )
            {
                if ( index < adjustedDropIndex )
                {
                    // We simply decrement inserted index in case some node which was higher than this one was deleted
                    // That allows us to have index that is correct when dragged nodes are already removed from the tree
                    adjustedDropIndex--;
                }
            }
        }
        return adjustedDropIndex;
    }

    /**
     * Performs actual nodes drop operation.
     *
     * @param nodes  list of nodes to drop
     * @param parent parent node to drop nodes into
     * @param tree   tree to drop nodes onto
     * @param model  tree model
     * @param index  nodes drop index
     */
    protected void performDropOperation ( final List<N> nodes, final N parent, final T tree, final ExTreeModel<N> model, final int index )
    {
        // This operation should be performed in EDT later to allow drop operation get completed in source TransferHandler first
        // Otherwise new nodes will be added into the tree before old ones are removed which is bad if it is the same tree
        // This is meaningful for D&D opearation within one tree, for other situations its meaningless but doesn't cause any problems
        SwingUtilities.invokeLater ( new Runnable ()
        {
            @Override
            public void run ()
            {
                // Adding data to model
                model.insertNodesInto ( nodes, parent, index );

                // Selecting inserted nodes
                tree.setSelectedNodes ( nodes );

                // Informing about drop
                nodesDropped ( nodes, parent, tree, model, index );
            }
        } );
    }

    /**
     * Returns whether the specified nodes drag can be started or not.
     *
     * @param nodes nodes to drag
     * @return true if the specified nodes drag can be started, false otherwise
     */
    protected abstract boolean canBeDragged ( List<N> nodes );

    /**
     * Returns whether nodes can be dropped to the specified location and index or not.
     *
     * @param nodes        list of nodes to drop
     * @param dropLocation node onto which drop was performed
     * @param dropIndex    drop index if dropped between nodes under dropLocation node or -1 if dropped directly onto dropLocation node
     * @return true if nodes can be dropped to the specified location and index, false otherwise
     */
    protected abstract boolean canBeDropped ( List<N> nodes, N dropLocation, int dropIndex );

    /**
     * Returns node copy used in createTransferable.
     * Used each time when node is moved within tree or into another tree.
     * Node copy should have the same ID and content but must be another instance of node type class.
     *
     * @param node node to copy
     * @return node copy
     */
    protected abstract N copy ( final N node );

    /**
     * Informs about nodes drop operation completition in a separate tree thread.
     * This method should be used to perform actual data move operation.
     * You are allowed to perform slow operations here as it is executed in a special async tree thread.
     *
     * @param nodes  list of nodes to drop
     * @param parent parent node to drop nodes into
     * @param tree   tree to drop nodes onto
     * @param model  tree model
     * @param index  nodes drop index
     */
    public abstract void nodesDropped ( final List<N> nodes, final N parent, final T tree, final ExTreeModel<N> model, final int index );

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString ()
    {
        return getClass ().getName ();
    }
}