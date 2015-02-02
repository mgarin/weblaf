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
import com.alee.laf.tree.WebTree;
import com.alee.laf.tree.WebTreeModel;
import com.alee.managers.log.Log;

import javax.swing.*;
import javax.swing.tree.TreePath;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Mikle Garin
 */

public abstract class AbstractTreeTransferHandler<N extends UniqueNode, T extends WebTree<N>, M extends WebTreeModel<N>>
        extends TransferHandler
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
     * Whether or not should optimize dragged nodes list to minimum.
     */
    protected boolean optimizeDraggedNodes = true;

    /**
     * Whether or not should expand single dragged node when it is dropped onto the tree.
     */
    protected boolean expandSingleNode = true;

    /**
     * Whether or not should expand multiply dragged nodes when they are dropped onto the tree.
     */
    protected boolean expandMultiplyNodes = false;

    /**
     * Array of dragged nodes.
     */
    protected List<N> draggedNodes;

    /**
     * Map of node indices lists under their parent node IDs.
     * This map is used to properly adjust drop index in the destination node if D&D performed within one tree.
     */
    protected Map<String, List<Integer>> draggedNodeIndices;

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
     * Returns whether should expand single dragged node when it is dropped onto the tree or not.
     *
     * @return true if should expand single dragged node when it is dropped onto the tree, false otherwise
     */
    public boolean isExpandSingleNode ()
    {
        return expandSingleNode;
    }

    /**
     * Sets whether should expand single dragged node when it is dropped onto the tree or not.
     *
     * @param expand whether should expand single dragged node when it is dropped onto the tree or not
     */
    public void setExpandSingleNode ( final boolean expand )
    {
        this.expandSingleNode = expand;
    }

    /**
     * Returns whether or not should expand multiply dragged nodes when they are dropped onto the tree.
     *
     * @return true if should expand multiply dragged nodes when they are dropped onto the tree, false otherwise
     */
    public boolean isExpandMultiplyNodes ()
    {
        return expandMultiplyNodes;
    }

    /**
     * Sets whether or not should expand multiply dragged nodes when they are dropped onto the tree.
     *
     * @param expand whether or not should expand multiply dragged nodes when they are dropped onto the tree
     */
    public void setExpandMultiplyNodes ( final boolean expand )
    {
        this.expandMultiplyNodes = expand;
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

            // Saving list of dragged nodes
            // These nodes might be removed later if this is a MOVE operation
            draggedNodes = nodes;

            // Collecting node indices under their parent nodes
            draggedNodeIndices = new HashMap<String, List<Integer>> ( 1 );
            for ( final N node : draggedNodes )
            {
                final N parent = ( N ) node.getParent ();
                List<Integer> indices = draggedNodeIndices.get ( parent.getId () );
                if ( indices == null )
                {
                    indices = new ArrayList<Integer> ( 1 );
                    draggedNodeIndices.put ( parent.getId (), indices );
                }
                indices.add ( parent.getIndex ( node ) );
            }

            // Returning new nodes transferable
            return new NodesTransferable ( copies );
        }
        return null;
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

        // Do not allow drop onto null path
        final JTree.DropLocation dl = ( JTree.DropLocation ) support.getDropLocation ();
        final TreePath path = dl.getPath ();
        if ( path == null )
        {
            return false;
        }

        try
        {
            // Check whether actual TransferHandler accepts drop to this location
            final N target = ( N ) path.getLastPathComponent ();
            if ( !canDropTo ( target ) )
            {
                return false;
            }

            // Do not allow drop inside one of dragged elements if this is a MOVE operation
            // Will not work when dragged to another tree, but it doesn't matter in that case
            if ( isMoveAction ( support.getDropAction () ) )
            {
                if ( draggedNodes != null )
                {
                    for ( final N node : draggedNodes )
                    {
                        if ( node == target || node.isNodeDescendant ( target ) )
                        {
                            return false;
                        }
                    }
                }
            }

            // This is not recommended due to crossing with other cases
            // This should be checked inside specific TransferHandler with its own checks
            //            // Do not allow drop into old location of one of the dragged nodes
            //            if ( isMoveAction ( support.getDropAction () ) )
            //            {
            //                final int index = dl.getChildIndex ();
            //                if ( index == -1 )
            //                {
            //                    for ( final N node : draggedNodes )
            //                    {
            //                        if ( node.getParent ().equals ( target ) )
            //                        {
            //                            return false;
            //                        }
            //                    }
            //                }
            //            }

            // Perform the actual drop check
            final List<N> nodes = ( List<N> ) support.getTransferable ().getTransferData ( nodesFlavor );
            final int index = dl.getChildIndex ();
            final boolean canBeDropped = canBeDropped ( nodes, target, index );

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
            Log.error ( this, "I/O exception: " + ioe.getMessage () );
            return false;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean importData ( final TransferHandler.TransferSupport support )
    {
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
            Log.error ( this, "I/O exception: " + ioe.getMessage () );
            return false;
        }

        // Getting drop location info
        final JTree.DropLocation dl = ( JTree.DropLocation ) support.getDropLocation ();
        final int dropIndex = dl.getChildIndex ();
        final TreePath dest = dl.getPath ();
        final N parent = ( N ) dest.getLastPathComponent ();
        final T tree = ( T ) support.getComponent ();
        final M model = ( M ) tree.getModel ();
        return prepareDropOperation ( support, nodes, dropIndex, parent, tree, model );
    }

    /**
     * Performs all preparations required to perform drop operation and calls for actual drop when ready.
     *
     * @param support   transfer support data
     * @param nodes     list of nodes to drop
     * @param dropIndex preliminary nodes drop index
     * @param parent    parent node to drop nodes into
     * @param tree      tree to drop nodes onto
     * @param model     tree model
     * @return true if drop operation was successfully completed, false otherwise
     */
    protected abstract boolean prepareDropOperation ( final TransferSupport support, final List<N> nodes, final int dropIndex,
                                                      final N parent, final T tree, final M model );

    /**
     * Performs actual nodes drop operation.
     *
     * @param nodes  list of nodes to drop
     * @param parent parent node to drop nodes into
     * @param tree   tree to drop nodes onto
     * @param model  tree model
     * @param index  nodes drop index
     * @return true if drop operation was successfully completed, false otherwise
     */
    protected abstract boolean performDropOperation ( final List<N> nodes, final N parent, final T tree, final M model, final int index );

    /**
     * Returns properly adjusted nodes drop index.
     *
     * @param dropIndex  drop index if dropped between nodes under dropLocation node or -1 if dropped directly onto dropLocation node
     * @param dropAction actual drop action
     * @param parent     parent node to drop nodes into  @return properly adjusted nodes drop index
     */
    protected int getAdjustedDropIndex ( final int dropIndex, final int dropAction, final N parent )
    {
        // Fixing drop index for case when dropped to non-leaf element
        int adjustedDropIndex = dropIndex == -1 ? parent.getChildCount () : dropIndex;

        // Adjusting drop index for MOVE operation
        if ( isMoveAction ( dropAction ) && draggedNodeIndices != null && draggedNodeIndices.containsKey ( parent.getId () ) )
        {
            final int initialIndex = adjustedDropIndex;
            for ( final Integer index : draggedNodeIndices.get ( parent.getId () ) )
            {
                if ( index < initialIndex )
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
            // Removing nodes saved in draggedNodes in createTransferable
            final T tree = ( T ) source;
            removeTreeNodes ( tree, draggedNodes );
            draggedNodes = null;
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
     * Returns whether the specified nodes drag can be started or not.
     *
     * @param nodes nodes to drag
     * @return true if the specified nodes drag can be started, false otherwise
     */
    protected abstract boolean canBeDragged ( List<N> nodes );

    /**
     * Checks whether specified target is acceptable for drop or not.
     * This check is performed before another check for nodes drop possibility.
     *
     * @param dropLocation node onto which drop was performed
     * @return true if the specified target is acceptable for drop, false otherwise
     */
    protected boolean canDropTo ( final N dropLocation )
    {
        return dropLocation != null;
    }

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
     * Asks tree to remove nodes after drag move operation has completed.
     *
     * @param tree          tree to remove nodes from
     * @param nodesToRemove nodes that should be removed
     */
    protected abstract void removeTreeNodes ( T tree, List<N> nodesToRemove );

    /**
     * Informs about nodes drop operation completition in a separate tree thread.
     * This method should be used to perform actual data move operation.
     *
     * @param nodes  list of nodes to drop
     * @param parent parent node to drop nodes into
     * @param tree   tree to drop nodes onto
     * @param model  tree model
     * @param index  nodes drop index
     */
    public abstract void nodesDropped ( final List<N> nodes, final N parent, final T tree, final M model, final int index );

    /**
     * Returns user objects collected from specified nodes.
     *
     * @param nodes list of nodes to collect user objects from
     * @param <O>   user object type
     * @return user objects collected from specified nodes
     */
    protected <O> List<O> collect ( final List<N> nodes )
    {
        final List<O> objects = new ArrayList<O> ( nodes.size () );
        for ( final N node : nodes )
        {
            objects.add ( ( O ) node.getUserObject () );
        }
        return objects;
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