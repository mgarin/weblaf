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
import com.alee.utils.CollectionUtils;

import javax.swing.*;
import javax.swing.tree.TreePath;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Most common transfer handler implementation that handles tree nodes transfer.
 *
 * @param <N> nodes type
 * @param <T> tree type
 * @author Mikle Garin
 */

public abstract class AbstractTreeTransferHandler<N extends UniqueNode, T extends WebTree<N>, M extends WebTreeModel<N>>
        extends TransferHandler
{
    /**
     * todo 1. Add setting "reduceDropConfirmsRate" which will cull extra "canBeDropped" method calls with the same arguments
     */

    /**
     * Whether or not should optimize dragged nodes list to minimum.
     */
    protected boolean optimizeDraggedNodes = true;

    /**
     * Whether or not should expand single dragged node when it is dropped onto the tree.
     */
    protected boolean expandSingleNode = false;

    /**
     * Whether or not should expand multiply dragged nodes when they are dropped onto the tree.
     */
    protected boolean expandMultiplyNodes = false;

    /**
     * Transferred data handlers.
     */
    protected List<TreeDropHandler<N, T>> dropHandlers;

    /**
     * Array of dragged nodes.
     */
    protected List<N> draggedNodes;

    /**
     * Map of node indices lists under their parent node IDs.
     * This map is used to properly adjust drop index in the destination node if D&amp;D performed within one tree.
     */
    protected Map<String, List<Integer>> draggedNodeIndices;

    /**
     * Constructs new tree transfer handler.
     */
    public AbstractTreeTransferHandler ()
    {
        super ();
        dropHandlers = createDropHandlers ();
    }

    /**
     * Returns supported drop handlers.
     * These handlers are requested only once on transfer handler initialization.
     * They will be used to check and process drop operations on the tree that uses this transfer handler.
     *
     * @return supported drop handlers
     */
    protected List<TreeDropHandler<N, T>> createDropHandlers ()
    {
        return CollectionUtils.<TreeDropHandler<N, T>>asList ( new NodesDropHandler () );
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

            // Checking whether or not can drag specified nodes
            if ( !canBeDragged ( tree, nodes ) )
            {
                return null;
            }

            // Creating nodes copy
            final List<N> copies = new ArrayList<N> ();
            for ( final N node : nodes )
            {
                copies.add ( copy ( tree, node ) );
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
            return createTransferable ( tree, copies );
        }
        return null;
    }

    /**
     * Returns whether the specified nodes drag can be started or not.
     *
     * @param tree  source tree
     * @param nodes nodes to drag
     * @return true if the specified nodes drag can be started, false otherwise
     */
    protected abstract boolean canBeDragged ( T tree, List<N> nodes );

    /**
     * Returns node copy used in createTransferable.
     * Used each time when node is moved within tree or into another tree.
     * Node copy should have the same ID and content but must be another instance of node type class.
     *
     * @param tree source tree
     * @param node node to copy
     * @return node copy
     */
    protected abstract N copy ( T tree, N node );

    /**
     * Returns new transferable based on dragged nodes.
     *
     * @param tree  source tree
     * @param nodes dragged nodes
     * @return new transferable based on dragged nodes
     */
    @SuppressWarnings ( "UnusedParameters" )
    protected Transferable createTransferable ( final T tree, final List<N> nodes )
    {
        return new NodesTransferable ( nodes );
    }

    @Override
    public boolean canImport ( final TransferSupport support )
    {
        if ( !support.isDrop () )
        {
            return false;
        }

        // Do not allow drop if there are no supported drop handlers available
        final List<TreeDropHandler<N, T>> dropHandlers = getSupportedDropHandlers ( support );
        if ( CollectionUtils.isEmpty ( dropHandlers ) )
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

        // Check whether actual TransferHandler accepts drop to this location
        final T tree = ( T ) support.getComponent ();
        final N destination = ( N ) path.getLastPathComponent ();
        if ( !canDropTo ( tree, destination ) )
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
                    if ( node == destination || node.isNodeDescendant ( destination ) )
                    {
                        return false;
                    }
                }
            }
        }

        // Perform actual drop checks
        boolean canBeDropped = false;
        for ( final TreeDropHandler<N, T> dataTransferHandler : dropHandlers )
        {
            if ( dataTransferHandler.canDrop ( support, tree, destination ) )
            {
                canBeDropped = true;
                break;
            }
        }

        // Displaying drop location
        support.setShowDropLocation ( canBeDropped );

        return canBeDropped;
    }

    /**
     * Returns list of drop handlers supporting this drop operation.
     *
     * @param support transfer support data
     * @return list of drop handlers supporting this drop operation
     */
    protected List<TreeDropHandler<N, T>> getSupportedDropHandlers ( final TransferSupport support )
    {
        final List<TreeDropHandler<N, T>> handlers = new ArrayList<TreeDropHandler<N, T>> ( dropHandlers.size () );
        for ( final TreeDropHandler<N, T> dropHandler : dropHandlers )
        {
            final List<DataFlavor> flavors = dropHandler.getSupportedFlavors ();
            if ( !CollectionUtils.isEmpty ( flavors ) )
            {
                for ( final DataFlavor flavor : flavors )
                {
                    if ( support.isDataFlavorSupported ( flavor ) )
                    {
                        handlers.add ( dropHandler );
                        break;
                    }
                }
            }
        }
        return handlers;
    }

    /**
     * Returns whether or not specified destination is acceptable for drop.
     * This check is performed before another check for nodes drop possibility.
     *
     * @param tree        destination tree
     * @param destination node onto which drop was performed
     * @return true if the specified destination is acceptable for drop, false otherwise
     */
    protected boolean canDropTo ( final T tree, final N destination )
    {
        return destination != null;
    }

    @Override
    public boolean importData ( final TransferHandler.TransferSupport support )
    {
        // Getting drop location info
        final JTree.DropLocation dl = ( JTree.DropLocation ) support.getDropLocation ();
        final int dropIndex = dl.getChildIndex ();
        final TreePath dest = dl.getPath ();
        final N parent = ( N ) dest.getLastPathComponent ();
        final T tree = ( T ) support.getComponent ();
        final M model = ( M ) tree.getModel ();

        // Retrieving dropped nodes
        List<N> nodes = null;
        for ( final TreeDropHandler<N, T> dropHandler : getSupportedDropHandlers ( support ) )
        {
            nodes = dropHandler.getDroppedNodes ( support, tree, parent );
            if ( !CollectionUtils.isEmpty ( nodes ) )
            {
                break;
            }
        }
        if ( CollectionUtils.isEmpty ( nodes ) )
        {
            return false;
        }

        // Prepare drop operation
        return prepareDropOperation ( support, tree, nodes, dropIndex, parent, model );
    }

    /**
     * Performs all preparations required to perform drop operation and calls for actual drop when ready.
     *
     * @param support   transfer support data
     * @param tree      tree to drop nodes onto
     * @param nodes     list of nodes to drop
     * @param dropIndex preliminary nodes drop index
     * @param parent    parent node to drop nodes into
     * @param model     tree model
     * @return true if drop operation was successfully completed, false otherwise
     */
    protected boolean prepareDropOperation ( final TransferSupport support, final T tree, final List<N> nodes, final int dropIndex,
                                             final N parent, final M model )
    {
        // Expanding parent first
        if ( !tree.isExpanded ( parent ) )
        {
            tree.expandNode ( parent );
        }

        // Adjust drop index after we ensure parent is expanded
        final int adjustedDropIndex = getAdjustedDropIndex ( dropIndex, support.getDropAction (), parent );

        // Now we can perform drop
        return performDropOperation ( tree, nodes, parent, model, adjustedDropIndex );
    }

    /**
     * Performs actual nodes drop operation.
     *
     * @param tree   tree to drop nodes onto
     * @param nodes  list of nodes to drop
     * @param parent parent node to drop nodes into
     * @param model  tree model
     * @param index  nodes drop index
     * @return true if drop operation was successfully completed, false otherwise
     */
    protected boolean performDropOperation ( final T tree, final List<N> nodes, final N parent, final M model, final int index )
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

                // Expanding nodes after drop operation
                if ( expandSingleNode && nodes.size () == 1 )
                {
                    tree.expandNode ( nodes.get ( 0 ) );
                }
                else if ( expandMultiplyNodes )
                {
                    for ( final N node : nodes )
                    {
                        tree.expandNode ( node );
                    }
                }

                // Selecting inserted nodes
                tree.setSelectedNodes ( nodes );

                // Informing about nodes drop
                informNodesDropped ( tree, nodes, parent, model, index );
            }
        } );
        return true;
    }

    /**
     * Informing about nodes drop operation.
     * This method is separate to allowmodifying logic of this specific call.
     *
     * @param tree   tree nodes were dropped onto
     * @param nodes  list of dropped nodes
     * @param parent parent where nodes were dropped
     * @param model  tree model
     * @param index  nodes drop index
     */
    protected void informNodesDropped ( final T tree, final List<N> nodes, final N parent, final M model, final int index )
    {
        nodesDropped ( nodes, parent, tree, model, index );
    }

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
    @SuppressWarnings ( "UnusedParameters" )
    public void nodesDropped ( final List<N> nodes, final N parent, final T tree, final M model, final int index )
    {
        // No actions are required by default
        // Override this method to perform any post-drop actions
    }

    /**
     * Returns properly adjusted nodes drop index.
     *
     * @param dropIndex  drop index if dropped between nodes under dropLocation node or -1 if dropped directly onto dropLocation node
     * @param dropAction actual drop action
     * @param parent     parent node to drop nodes into  @return properly adjusted nodes drop index
     * @return properly adjusted nodes drop index
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
     * Asks tree to remove nodes after drag move operation has completed.
     *
     * @param tree          tree to remove nodes from
     * @param nodesToRemove nodes that should be removed
     */
    protected void removeTreeNodes ( final T tree, final List<N> nodesToRemove )
    {
        ( ( M ) tree.getModel () ).removeNodesFromParent ( nodesToRemove );
    }

    /**
     * Returns user objects extracted from specified nodes.
     *
     * @param nodes list of nodes to extract user objects from
     * @param <O>   user object type
     * @return user objects extracted from specified nodes
     */
    protected <O> List<O> extract ( final List<N> nodes )
    {
        final List<O> objects = new ArrayList<O> ( nodes.size () );
        for ( final N node : nodes )
        {
            objects.add ( ( O ) node.getUserObject () );
        }
        return objects;
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
     * Returns whether action is COPY or not.
     *
     * @param action drag action
     * @return true if action is COPY, false otherwise
     */
    protected boolean isCopyAction ( final int action )
    {
        return ( action & COPY ) == COPY;
    }

    @Override
    public String toString ()
    {
        return getClass ().getName ();
    }
}