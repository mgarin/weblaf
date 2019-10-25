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
import com.alee.laf.tree.*;
import com.alee.utils.CollectionUtils;
import com.alee.utils.CoreSwingUtils;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.util.*;

/**
 * Most common transfer handler implementation that handles tree nodes transfer.
 *
 * @param <N> nodes type
 * @param <T> tree type
 * @param <M> tree model type
 * @author Mikle Garin
 */
public abstract class AbstractTreeTransferHandler<N extends UniqueNode, T extends WebTree<N>, M extends WebTreeModel<N>>
        extends TransferHandler
{
    /**
     * todo 1. Add setting "reduceDropConfirmsRate" which will cull extra "canBeDropped" method calls with the same arguments
     */

    /**
     * {@link NodesAcceptPolicy} that defines a way to filter dragged nodes.
     */
    @NotNull
    protected NodesAcceptPolicy nodesAcceptPolicy;

    /**
     * Whether or not should expand single dragged node when it is dropped onto the tree.
     */
    protected boolean expandSingleNode;

    /**
     * Whether or not should expand multiple dragged nodes when they are dropped onto the tree.
     */
    protected boolean expandMultipleNodes;

    /**
     * Transferred data handlers.
     */
    @NotNull
    protected List<? extends TreeDropHandler> dropHandlers;

    /**
     * Array of dragged nodes.
     */
    @Nullable
    protected List<N> draggedNodes;

    /**
     * Map of node indices lists under their parent node IDs.
     * This map is used to properly adjust drop index in the destination node if D&amp;D performed within one tree.
     */
    @Nullable
    protected Map<String, List<Integer>> draggedNodeIndices;

    /**
     * Constructs new tree transfer handler.
     */
    public AbstractTreeTransferHandler ()
    {
        super ();
        nodesAcceptPolicy = NodesAcceptPolicy.ancestors;
        expandSingleNode = false;
        expandMultipleNodes = false;
        dropHandlers = createDropHandlers ();
    }

    /**
     * Returns supported drop handlers.
     * These handlers are requested only once on transfer handler initialization.
     * They will be used to check and process drop operations on the tree that uses this transfer handler.
     *
     * @return supported drop handlers
     */
    @NotNull
    protected List<? extends TreeDropHandler> createDropHandlers ()
    {
        return CollectionUtils.asList ( new NodesDropHandler () );
    }

    /**
     * Return {@link NodesAcceptPolicy} that defines a way to filter dragged nodes.
     *
     * @return {@link NodesAcceptPolicy} that defines a way to filter dragged nodes
     */
    @NotNull
    public NodesAcceptPolicy getNodesAcceptPolicy ()
    {
        return nodesAcceptPolicy;
    }

    /**
     * Sets {@link NodesAcceptPolicy} that defines a way to filter dragged nodes.
     *
     * @param policy {@link NodesAcceptPolicy} that defines a way to filter dragged nodes
     */
    public void setNodesAcceptPolicy ( @NotNull final NodesAcceptPolicy policy )
    {
        this.nodesAcceptPolicy = policy;
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
     * Returns whether or not should expand multiple dragged nodes when they are dropped onto the tree.
     *
     * @return {@code true} if should expand multiple dragged nodes when they are dropped onto the tree, {@code false} otherwise
     */
    public boolean isExpandMultipleNodes ()
    {
        return expandMultipleNodes;
    }

    /**
     * Sets whether or not should expand multiple dragged nodes when they are dropped onto the tree.
     *
     * @param expand whether or not should expand multiple dragged nodes when they are dropped onto the tree
     */
    public void setExpandMultipleNodes ( final boolean expand )
    {
        this.expandMultipleNodes = expand;
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
    public abstract int getSourceActions ( @NotNull JComponent c );

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

    /**
     * Creates a {@link Transferable} to use as the source for a data transfer.
     * Returns the representation of the data to be transferred, or null if the component's property is null
     *
     * @param c the component holding the data to be transferred, provided to enable sharing of TransferHandlers
     * @return the representation of the data to be transferred, or null if the property associated with component is null
     */
    @Nullable
    @Override
    protected Transferable createTransferable ( @NotNull final JComponent c )
    {
        Transferable transferable = null;
        final T tree = ( T ) c;
        final List<N> nodes = tree.getSelectedNodes ();
        if ( !nodes.isEmpty () )
        {
            // Do not allow root node export
            if ( !nodes.contains ( tree.getRootNode () ) )
            {
                // Filtering nodes if needed
                getNodesAcceptPolicy ().filter ( tree, nodes );

                // Sorting nodes according to their position in the tree
                CollectionUtils.sort ( nodes, new NodesRowComparator<N> ( tree ) );

                // Checking whether or not can drag specified nodes
                final M model = ( M ) tree.getModel ();
                if ( model != null && canBeDragged ( tree, model, nodes ) )
                {
                    // Creating nodes copy
                    final List<N> copies = new ArrayList<N> ();
                    for ( final N node : nodes )
                    {
                        copies.add ( copy ( tree, model, node ) );
                    }

                    // Saving list of dragged nodes
                    // These nodes might be removed later if this is a MOVE operation
                    draggedNodes = nodes;

                    // Collecting node indices under their parent nodes
                    draggedNodeIndices = new HashMap<String, List<Integer>> ( 1 );
                    for ( final N node : draggedNodes )
                    {
                        final N parent = ( N ) node.getParent ();
                        if ( parent != null )
                        {
                            List<Integer> indices = draggedNodeIndices.get ( parent.getId () );
                            if ( indices == null )
                            {
                                indices = new ArrayList<Integer> ( 1 );
                                draggedNodeIndices.put ( parent.getId (), indices );
                            }
                            indices.add ( parent.getIndex ( node ) );
                        }
                    }

                    // Returning new nodes transferable
                    transferable = createTransferable ( tree, model, copies );
                }
            }
        }
        return transferable;
    }

    /**
     * Returns whether the specified nodes drag can be started or not.
     *
     * @param tree  source tree
     * @param model tree model
     * @param nodes nodes to drag
     * @return true if the specified nodes drag can be started, false otherwise
     */
    protected abstract boolean canBeDragged ( @NotNull T tree, @NotNull M model, @NotNull List<N> nodes );

    /**
     * Returns node copy used in createTransferable.
     * Used each time when node is moved within tree or into another tree.
     * Node copy should have the same ID and content but must be another instance of node type class.
     *
     * @param tree  source tree
     * @param model tree model
     * @param node  node to copy
     * @return node copy
     */
    @NotNull
    protected abstract N copy ( @NotNull T tree, @NotNull M model, @NotNull N node );

    /**
     * Returns new transferable based on dragged nodes.
     *
     * @param tree  source tree
     * @param model tree model
     * @param nodes dragged nodes
     * @return new transferable based on dragged nodes
     */
    @NotNull
    protected Transferable createTransferable ( @NotNull final T tree, @NotNull final M model, @NotNull final List<N> nodes )
    {
        return new NodesTransferable ( nodes );
    }

    @Override
    public boolean canImport ( @NotNull final TransferSupport support )
    {
        boolean canImport = false;
        if ( support.isDrop () )
        {
            // Do not allow drop onto null path
            // Also check whether actual TransferHandler accepts drop to this location
            final JTree.DropLocation dl = ( JTree.DropLocation ) support.getDropLocation ();
            final T tree = ( T ) support.getComponent ();
            final M model = ( M ) tree.getModel ();
            final N destination = tree.getNodeForPath ( dl.getPath () );
            if ( model != null && destination != null && canDropTo ( support, tree, model, destination ) )
            {
                // Do not allow drop inside one of dragged elements if this is a MOVE operation
                // Will not work when dragged to another tree, but it doesn't matter in that case
                boolean validMove = true;
                if ( isMoveAction ( support.getDropAction () ) )
                {
                    if ( draggedNodes != null )
                    {
                        for ( final N node : draggedNodes )
                        {
                            if ( node == destination || node.isNodeDescendant ( destination ) )
                            {
                                validMove = false;
                                break;
                            }
                        }
                    }
                }
                if ( validMove )
                {
                    // Perform actual drop check
                    final TreeDropHandler<N, T, M> dropHandler = getDropHandler ( support, tree, model, destination );
                    final boolean canBeDropped = dropHandler != null && dropHandler.canDrop ( support, tree, model, destination );

                    // Displaying drop location
                    support.setShowDropLocation ( canBeDropped );

                    canImport = canBeDropped;
                }
            }
        }
        return canImport;
    }

    /**
     * Returns whether or not specified destination is acceptable for drop.
     * This check is performed before another check for nodes drop possibility.
     *
     * @param support     transfer support data
     * @param tree        destination tree
     * @param model       tree model
     * @param destination node onto which drop was performed
     * @return {@code true} if the specified destination is acceptable for drop, {@code false} otherwise
     */
    protected boolean canDropTo ( @NotNull final TransferSupport support, @NotNull final T tree, @NotNull final M model,
                                  @NotNull final N destination )
    {
        return true;
    }

    /**
     * Returns drop handler supporting this drop operation.
     *
     * @param support     transfer support data
     * @param tree        destination tree
     * @param model       tree model
     * @param destination node onto which drop was performed
     * @return drop handler supporting this drop operation
     */
    @Nullable
    protected TreeDropHandler<N, T, M> getDropHandler ( @NotNull final TransferSupport support, @NotNull final T tree,
                                                        @NotNull final M model, @NotNull final N destination )
    {
        TreeDropHandler<N, T, M> dropHandler = null;
        for ( final TreeDropHandler<N, T, M> handler : dropHandlers )
        {
            final List<DataFlavor> flavors = handler.getSupportedFlavors ();
            if ( CollectionUtils.notEmpty ( flavors ) )
            {
                for ( final DataFlavor flavor : flavors )
                {
                    if ( support.isDataFlavorSupported ( flavor ) )
                    {
                        dropHandler = handler;
                        break;
                    }
                }
                if ( dropHandler != null )
                {
                    break;
                }
            }
        }
        return dropHandler;
    }

    @Override
    public boolean importData ( @NotNull final TransferHandler.TransferSupport support )
    {
        // Getting drop location info
        final JTree.DropLocation dl = ( JTree.DropLocation ) support.getDropLocation ();
        final T tree = ( T ) support.getComponent ();
        final M model = ( M ) tree.getModel ();
        final N destination = tree.getNodeForPath ( dl.getPath () );
        final int dropIndex = dl.getChildIndex ();

        // Prepare drop operation
        return model != null && destination != null && prepareDropOperation (
                support,
                tree,
                model,
                destination,
                dropIndex
        );
    }

    /**
     * Performs all preparations required to perform drop operation and calls for actual drop when ready.
     *
     * @param support     transfer support data
     * @param tree        tree to drop nodes onto
     * @param model       tree model
     * @param destination parent node to drop nodes into
     * @param dropIndex   preliminary nodes drop index
     * @return {@code true} if drop operation was successfully completed, {@code false} otherwise
     */
    protected boolean prepareDropOperation ( @NotNull final TransferSupport support, @NotNull final T tree, @NotNull final M model,
                                             @NotNull final N destination, final int dropIndex )
    {
        // Expanding parent first
        if ( !tree.isExpanded ( destination ) )
        {
            tree.expandNode ( destination );
        }

        // Adjust drop index after we ensure parent is expanded
        final int adjustedDropIndex = getAdjustedDropIndex ( support, tree, model, destination, dropIndex );

        // Now we can perform drop
        return performDropOperation ( support, tree, model, destination, adjustedDropIndex );
    }

    /**
     * Returns properly adjusted nodes drop index.
     *
     * @param support     transfer support data
     * @param tree        tree to drop nodes onto
     * @param model       tree model
     * @param destination parent node to drop nodes into
     * @param dropIndex   drop index if dropped between nodes under dropLocation node or -1 if dropped directly onto dropLocation node
     * @return properly adjusted nodes drop index
     */
    protected int getAdjustedDropIndex ( @NotNull final TransferSupport support, @NotNull final T tree, @NotNull final M model,
                                         @NotNull final N destination, final int dropIndex )
    {
        // Fixing drop index for case when dropped to non-leaf element
        int adjustedDropIndex = dropIndex == -1 ? destination.getChildCount () : dropIndex;

        // Adjusting drop index for MOVE operation
        if ( isMoveAction ( support.getDropAction () ) &&
                draggedNodeIndices != null && draggedNodeIndices.containsKey ( destination.getId () ) )
        {
            final int initialIndex = adjustedDropIndex;
            for ( final Integer index : draggedNodeIndices.get ( destination.getId () ) )
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
     * Performs actual nodes drop operation.
     *
     * @param support     transfer support data
     * @param tree        tree to drop nodes onto
     * @param model       tree model
     * @param destination parent node to drop nodes into
     * @param index       nodes drop index
     * @return {@code true} if drop operation was successfully completed, {@code false} otherwise
     */
    protected boolean performDropOperation ( @NotNull final TransferSupport support, @NotNull final T tree, @NotNull final M model,
                                             @NotNull final N destination, final int index )
    {
        boolean dropApproved = false;

        // Retrieving drop handler that will perform actual drop
        final TreeDropHandler<N, T, M> handler = getDropHandler ( support, tree, model, destination );
        if ( handler != null )
        {
            // Preparing drop operation
            // Some heavy checks might still be running here
            dropApproved = handler.prepareDrop ( support, tree, model, destination, index );

            // Proceeding to drop which might happen asynchronously in the handler
            // At this point we have done all we can to provide synchronous drop visual feedback
            if ( dropApproved )
            {
                // Retrieving callback responsible for the drop and performing the actual drop
                final NodesDropCallback<N> callback = createNodesDropCallback ( support, tree, model, destination, index );
                handler.performDrop ( support, tree, model, destination, index, callback );
            }
        }

        return dropApproved;
    }

    /**
     * Returns actual nodes drop operation callback for drop completion.
     * All node inserts should be performed later in EDT to allow drop operation get completed in source TransferHandler first.
     * Otherwise new nodes will be added into the tree before old ones are removed which would cause issues if it is the same tree.
     * This is meaningful for D&D opearation within one tree, for other situations its meaningless but doesn't cause any problems.
     *
     * @param support     transfer support data
     * @param tree        destination tree
     * @param model       tree model
     * @param destination node onto which drop was performed
     * @param index       nodes drop index
     * @return actual nodes drop operation callback for drop completion
     */
    @NotNull
    protected NodesDropCallback<N> createNodesDropCallback ( @NotNull final TransferSupport support, @NotNull final T tree,
                                                             @NotNull final M model, @NotNull final N destination, final int index )
    {
        return new NodesDropCallback<N> ()
        {
            /**
             * Dropped nodes collected while drop callback is used.
             */
            private final List<N> dropped = new ArrayList<N> ();

            @Override
            public void dropped ( @NotNull final N... nodes )
            {
                // Insert dropped nodes in EDT
                CoreSwingUtils.invokeLater ( new Runnable ()
                {
                    @Override
                    public void run ()
                    {
                        // Saving added nodes
                        Collections.addAll ( dropped, nodes );

                        // Adding dropped nodes into model
                        model.insertNodesInto ( nodes, destination, index );
                    }
                } );
            }

            @Override
            public void dropped ( @NotNull final List<N> nodes )
            {
                // Insert dropped nodes in EDT
                CoreSwingUtils.invokeLater ( new Runnable ()
                {
                    @Override
                    public void run ()
                    {
                        // Saving added nodes
                        dropped.addAll ( nodes );

                        // Adding dropped nodes into model
                        model.insertNodesInto ( nodes, destination, index );
                    }
                } );
            }

            @Override
            public void completed ()
            {
                dropCompleted ( support, tree, model, destination, index, dropped );
            }

            @Override
            public void failed ( @NotNull final Throwable cause )
            {
                dropFailed ( support, tree, model, destination, index, dropped, cause );
            }
        };
    }

    /**
     * Called upon drop completion.
     *
     * @param support     transfer support data
     * @param tree        destination tree
     * @param model       tree model
     * @param destination node onto which drop was performed
     * @param index       nodes drop index
     * @param dropped     dropped nodes collected while drop callback was used
     */
    protected void dropCompleted ( @NotNull final TransferSupport support, @NotNull final T tree, @NotNull final M model,
                                   @NotNull final N destination, final int index, @NotNull final List<N> dropped )
    {
        // Simply finish drop operation
        finishDrop ( support, tree, model, destination, index, dropped );
    }

    /**
     * Called upon drop fail.
     *
     * @param support     transfer support data
     * @param tree        destination tree
     * @param model       tree model
     * @param destination node onto which drop was performed
     * @param index       nodes drop index
     * @param dropped     dropped nodes collected while drop callback was used
     * @param cause       drop failure cause
     */
    protected void dropFailed ( @NotNull final TransferSupport support, @NotNull final T tree, @NotNull final M model,
                                @NotNull final N destination, final int index, @NotNull final List<N> dropped,
                                @NotNull final Throwable cause )
    {
        // todo Think of a good way to process drop failure
        // Logging drop operation issues that have occurred
        LoggerFactory.getLogger ( AbstractTreeTransferHandler.class ).error ( "Unable to perform drop operation", cause );

        // Finish drop operation after logging cause
        finishDrop ( support, tree, model, destination, index, dropped );
    }

    /**
     * Drop operation completion.
     * This will be called even if only partial data has been dropped.
     *
     * @param support     transfer support data
     * @param tree        destination tree
     * @param model       tree model
     * @param destination node onto which drop was performed
     * @param index       nodes drop index
     * @param dropped     dropped nodes collected while drop callback was used
     */
    protected void finishDrop ( @NotNull final TransferSupport support, @NotNull final T tree, @NotNull final M model,
                                @NotNull final N destination, final int index, @NotNull final List<N> dropped )
    {
        CoreSwingUtils.invokeLater ( new Runnable ()
        {
            @Override
            public void run ()
            {
                if ( dropped.size () > 0 )
                {
                    // Expanding nodes after drop operation
                    if ( expandSingleNode && dropped.size () == 1 )
                    {
                        // Expand single dropped node
                        tree.expandNode ( dropped.get ( 0 ) );
                    }
                    else if ( expandMultipleNodes )
                    {
                        // Expand all dropped nodes
                        for ( final N node : dropped )
                        {
                            tree.expandNode ( node );
                        }
                    }

                    // Selecting inserted nodes
                    tree.setSelectedNodes ( dropped );
                }
            }
        } );
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
    protected void exportDone ( @NotNull final JComponent source, @NotNull final Transferable data, final int action )
    {
        if ( draggedNodes != null && isMoveAction ( action ) )
        {
            // Removing nodes saved in draggedNodes in createTransferable
            final T tree = ( T ) source;
            removeTreeNodes ( tree, draggedNodes );
        }

        // Cleaning up
        draggedNodeIndices = null;
        draggedNodes = null;
    }

    /**
     * Asks tree to remove nodes after drag move operation has completed.
     *
     * @param tree          tree to remove nodes from
     * @param nodesToRemove nodes that should be removed
     */
    protected void removeTreeNodes ( @NotNull final T tree, @NotNull final List<N> nodesToRemove )
    {
        final M model = ( M ) tree.getModel ();
        if ( model != null )
        {
            model.removeNodesFromParent ( nodesToRemove );
        }
    }

    /**
     * Returns user objects extracted from specified nodes.
     *
     * @param nodes list of nodes to extract user objects from
     * @param <O>   user object type
     * @return user objects extracted from specified nodes
     */
    @NotNull
    protected <O> List<O> extract ( @NotNull final List<N> nodes )
    {
        final List<O> objects = new ArrayList<O> ( nodes.size () );
        for ( final N node : nodes )
        {
            objects.add ( ( O ) node.getUserObject () );
        }
        return objects;
    }

    @NotNull
    @Override
    public String toString ()
    {
        return getClass ().getName ();
    }
}