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

import com.alee.laf.checkbox.CheckState;
import com.alee.laf.tree.NodesAcceptPolicy;

import javax.swing.event.EventListenerList;
import javax.swing.tree.MutableTreeNode;
import java.util.*;

/**
 * Default checking model for {@link WebCheckBoxTree}.
 *
 * @param <N> {@link MutableTreeNode} type
 * @author Mikle Garin
 */
public class DefaultTreeCheckingModel<N extends MutableTreeNode, T extends WebCheckBoxTree<N>> implements TreeCheckingModel<N>
{
    /**
     * {@link WebCheckBoxTree} that uses this checking model.
     */
    protected final T checkBoxTree;

    /**
     * {@link MutableTreeNode} check states.
     */
    protected Map<N, CheckState> nodeCheckStates;

    /**
     * Model listeners.
     *
     * @see CheckStateChangeListener
     */
    protected EventListenerList listeners;

    /**
     * Comparator for {@link MutableTreeNode}s returned for specified {@link CheckState}.
     */
    protected Comparator<N> nodesComparator;

    /**
     * Constructs new {@link DefaultTreeCheckingModel} for the specified {@link WebCheckBoxTree}.
     *
     * @param checkBoxTree {@link WebCheckBoxTree} that uses this {@link DefaultTreeCheckingModel}
     */
    public DefaultTreeCheckingModel ( final T checkBoxTree )
    {
        super ();
        this.checkBoxTree = checkBoxTree;
        this.nodeCheckStates = new WeakHashMap<N, CheckState> ();
        this.listeners = new EventListenerList ();
        this.nodesComparator = createNodesComparator ();
    }

    /**
     * Returns new {@link Comparator} for nodes.
     * Can also return {@code null} to disable nodes sorting.
     *
     * @return new {@link Comparator} for nodes
     */
    protected Comparator<N> createNodesComparator ()
    {
        return new NodesPositionComparator<N> ();
    }

    @Override
    public List<N> getNodes ( final CheckState state, final NodesAcceptPolicy policy )
    {
        // Collecting nodes for state
        final List<N> collected = new ArrayList<N> ();
        if ( state == CheckState.checked || state == CheckState.mixed )
        {
            // Collecting checked or mixed nodes
            for ( final Map.Entry<N, CheckState> entry : nodeCheckStates.entrySet () )
            {
                if ( entry.getValue () == state )
                {
                    collected.add ( entry.getKey () );
                }
            }
        }
        else
        {
            // Collecting unchecked nodes
            final List<N> runthrough = new ArrayList<N> ();
            runthrough.add ( checkBoxTree.getRootNode () );
            while ( !runthrough.isEmpty () )
            {
                // Removing first element to shrink runthrough list
                final N node = runthrough.remove ( 0 );
                final CheckState nodeState = nodeCheckStates.get ( node );
                final boolean unchecked = nodeState == null || nodeState == CheckState.unchecked;

                // Simply adding unchecked node
                if ( unchecked )
                {
                    collected.add ( node );
                }

                // Make sure to check all child nodes
                for ( int i = 0; i < getChildCount ( node ); i++ )
                {
                    runthrough.add ( getChildAt ( node, i ) );
                }
            }
        }

        // Removing chilren of collected nodes
        if ( policy != null )
        {
            policy.filter ( checkBoxTree, collected );
        }

        // Sorting nodes by their position in the tree
        if ( nodesComparator != null )
        {
            Collections.sort ( collected, nodesComparator );
        }

        return collected;
    }

    @Override
    public CheckState getCheckState ( final N node )
    {
        final CheckState checkState = nodeCheckStates.get ( node );
        return checkState != null ? checkState : CheckState.unchecked;
    }

    @Override
    public void setChecked ( final N node, final boolean checked )
    {
        // Collecting state changes
        final boolean collectChanges = listeners.getListenerCount ( CheckStateChangeListener.class ) > 0;
        List<CheckStateChange<N>> changes = null;
        if ( collectChanges )
        {
            changes = new ArrayList<CheckStateChange<N>> ( 1 );
        }

        // Updating states
        final List<N> toUpdate = new ArrayList<N> ();
        setCheckedImpl ( node, checked, toUpdate, changes );
        repaintTreeNodes ( toUpdate );

        // Informing about state changes
        fireCheckStateChanged ( changes );
    }

    @Override
    public void setChecked ( final Collection<N> nodes, final boolean checked )
    {
        // Collecting state changes
        final boolean collectChanges = listeners.getListenerCount ( CheckStateChangeListener.class ) > 0;
        List<CheckStateChange<N>> changes = null;
        if ( collectChanges )
        {
            changes = new ArrayList<CheckStateChange<N>> ( nodes.size () );
        }

        // Updating states
        final List<N> toUpdate = new ArrayList<N> ();
        for ( final N node : nodes )
        {
            setCheckedImpl ( node, checked, toUpdate, changes );
        }
        repaintTreeNodes ( toUpdate );

        // Informing about state changes
        fireCheckStateChanged ( changes );
    }

    /**
     * Sets whether the specified tree node is checked or not.
     *
     * @param node     tree node to process
     * @param checked  whether the specified tree node is checked or not
     * @param toUpdate list of nodes for later update
     * @param changes  list to collect state changes into
     */
    protected void setCheckedImpl ( final N node, final boolean checked, final List<N> toUpdate, final List<CheckStateChange<N>> changes )
    {
        // Remembering old and new states
        final CheckState oldState = getCheckState ( node );
        final CheckState newState = checked ? CheckState.checked : CheckState.unchecked;

        // Updating node if check state has actually changed
        if ( oldState != newState )
        {
            // Changing node check state
            updateNodeState ( node, newState, toUpdate );

            // Saving changes
            if ( changes != null )
            {
                changes.add ( new CheckStateChange<N> ( node, oldState, newState ) );
            }

            // Updating parent and child node states
            if ( checkBoxTree.isRecursiveCheckingEnabled () )
            {
                updateChildNodesState ( node, newState, toUpdate, changes );
                updateParentStates ( node, toUpdate, changes );
            }
        }
    }

    /**
     * Updates parent nodes check states.
     *
     * @param node     node to start checking parents from
     * @param toUpdate list of nodes for later update
     * @param changes  list to collect state changes into
     */
    protected void updateParentStates ( final N node, final List<N> toUpdate, final List<CheckStateChange<N>> changes )
    {
        // Updating all parent node states
        N parent = getParent ( node );
        while ( parent != null )
        {
            // Calculating parent state
            CheckState state = CheckState.unchecked;
            boolean hasChecked = false;
            boolean hasUnchecked = false;
            for ( int i = 0; i < getChildCount ( parent ); i++ )
            {
                final CheckState checkState = getCheckState ( getChildAt ( parent, i ) );
                if ( checkState == CheckState.mixed )
                {
                    state = CheckState.mixed;
                    break;
                }
                else if ( checkState == CheckState.checked )
                {
                    hasChecked = true;
                    if ( hasUnchecked )
                    {
                        state = CheckState.mixed;
                        break;
                    }
                    else
                    {
                        state = CheckState.checked;
                    }
                }
                else if ( checkState == CheckState.unchecked )
                {
                    hasUnchecked = true;
                    if ( hasChecked )
                    {
                        state = CheckState.mixed;
                        break;
                    }
                    else
                    {
                        state = CheckState.unchecked;
                    }
                }
            }

            final CheckState oldState = getCheckState ( parent );
            if ( oldState != state )
            {
                // Saving changes
                if ( changes != null )
                {
                    changes.add ( new CheckStateChange<N> ( parent, oldState, state ) );
                }

                // Updating state
                updateNodeState ( parent, state, toUpdate );
            }

            // Moving upstairs
            parent = getParent ( parent );
        }
    }

    /**
     * Updates child nodes check state.
     *
     * @param node     parent node
     * @param newState new check state
     * @param toUpdate list of nodes for later update
     * @param changes  list to collect state changes into
     */
    protected void updateChildNodesState ( final N node, final CheckState newState, final List<N> toUpdate,
                                           final List<CheckStateChange<N>> changes )
    {
        for ( int i = 0; i < getChildCount ( node ); i++ )
        {
            final N childNode = getChildAt ( node, i );

            // Saving changes
            if ( changes != null )
            {
                changes.add ( new CheckStateChange<N> ( childNode, getCheckState ( childNode ), newState ) );
            }

            // Updating state
            updateNodeState ( childNode, newState, toUpdate );

            // Updating child nodes state
            updateChildNodesState ( childNode, newState, toUpdate, changes );
        }
    }

    /**
     * Updates single node check state.
     *
     * @param node     node to update
     * @param newState new check state
     * @param toUpdate list of nodes for later update
     */
    protected void updateNodeState ( final N node, final CheckState newState, final List<N> toUpdate )
    {
        if ( newState != CheckState.unchecked )
        {
            nodeCheckStates.put ( node, newState );
        }
        else
        {
            nodeCheckStates.remove ( node );
        }
        toUpdate.add ( node );
    }

    @Override
    public void invertCheck ( final N node )
    {
        // Collecting state changes
        final boolean collectChanges = listeners.getListenerCount ( CheckStateChangeListener.class ) > 0;
        List<CheckStateChange<N>> changes = null;
        if ( collectChanges )
        {
            changes = new ArrayList<CheckStateChange<N>> ( 1 );
        }

        // Updating states
        final List<N> toUpdate = new ArrayList<N> ();
        setCheckedImpl ( node, getNextState ( getCheckState ( node ) ) == CheckState.checked, toUpdate, changes );
        repaintTreeNodes ( toUpdate );

        // Informing about state changes
        fireCheckStateChanged ( changes );
    }

    @Override
    public void invertCheck ( final Collection<N> nodes )
    {
        // Collecting state changes
        final boolean collectChanges = listeners.getListenerCount ( CheckStateChangeListener.class ) > 0;
        List<CheckStateChange<N>> changes = null;
        if ( collectChanges )
        {
            changes = new ArrayList<CheckStateChange<N>> ( nodes.size () );
        }

        // Updating states
        final List<N> toUpdate = new ArrayList<N> ();
        boolean check = false;
        for ( final N node : nodes )
        {
            if ( getCheckState ( node ) != CheckState.checked )
            {
                check = true;
                break;
            }
        }
        for ( final N node : nodes )
        {
            setCheckedImpl ( node, check, toUpdate, changes );
        }
        repaintTreeNodes ( toUpdate );

        // Informing about state changes
        fireCheckStateChanged ( changes );
    }

    @Override
    public void checkAll ()
    {
        final List<N> allNodes = checkBoxTree.getAvailableNodes ();

        // Collecting state changes
        List<CheckStateChange<N>> changes = null;
        if ( listeners.getListenerCount ( CheckStateChangeListener.class ) > 0 )
        {
            changes = new ArrayList<CheckStateChange<N>> ( allNodes.size () );
            for ( final N node : allNodes )
            {
                final CheckState state = getCheckState ( node );
                if ( state != CheckState.checked )
                {
                    changes.add ( new CheckStateChange<N> ( node, state, CheckState.checked ) );
                }
            }
        }

        // Updating states
        for ( final N node : allNodes )
        {
            nodeCheckStates.put ( node, CheckState.checked );
        }
        repaintVisibleTreeRect ();

        // Informing about state changes
        fireCheckStateChanged ( changes );
    }

    @Override
    public void uncheckAll ()
    {
        // Collecting state changes
        List<CheckStateChange<N>> changes = null;
        if ( listeners.getListenerCount ( CheckStateChangeListener.class ) > 0 )
        {
            changes = new ArrayList<CheckStateChange<N>> ( nodeCheckStates.size () );
            for ( final Map.Entry<N, CheckState> entry : nodeCheckStates.entrySet () )
            {
                final CheckState state = entry.getValue ();
                if ( state == CheckState.mixed || state == CheckState.checked )
                {
                    changes.add ( new CheckStateChange<N> ( entry.getKey (), state, CheckState.unchecked ) );
                }
            }
        }

        // Updating states
        nodeCheckStates.clear ();
        repaintVisibleTreeRect ();

        // Informing about state changes
        fireCheckStateChanged ( changes );
    }

    /**
     * Returns next check state for check invertion action.
     *
     * @param checkState current check state
     * @return next check state for check invertion action
     */
    protected CheckState getNextState ( final CheckState checkState )
    {
        switch ( checkState )
        {
            case unchecked:
                return CheckState.checked;

            case mixed:
                return checkBoxTree.isCheckMixedOnToggle () ? CheckState.checked : CheckState.unchecked;

            case checked:
            default:
                return CheckState.unchecked;
        }
    }

    @Override
    public void checkingModeChanged ( final boolean recursive )
    {
        // Collecting state changes
        final boolean collectChanges = listeners.getListenerCount ( CheckStateChangeListener.class ) > 0;
        List<CheckStateChange<N>> changes = null;
        if ( collectChanges )
        {
            changes = new ArrayList<CheckStateChange<N>> ();
        }

        // Updating states
        final List<N> toUpdate = new ArrayList<N> ();
        if ( recursive )
        {
            // Retrieving all checked nodes
            final List<N> checked = new ArrayList<N> ( nodeCheckStates.size () );
            for ( final Map.Entry<N, CheckState> entry : nodeCheckStates.entrySet () )
            {
                if ( entry.getValue () == CheckState.checked )
                {
                    checked.add ( entry.getKey () );
                }
            }

            // Filtering out child nodes under other checked nodes
            // We don't need to update them because they will get checked in the process
            filterOutChildNodes ( checked );

            // Updating node states
            for ( final N node : checked )
            {
                updateParentStates ( node, toUpdate, changes );
                updateChildNodesState ( node, CheckState.checked, toUpdate, changes );
            }
        }
        else
        {
            // Removing existing mixed states
            final Iterator<Map.Entry<N, CheckState>> iterator = nodeCheckStates.entrySet ().iterator ();
            while ( iterator.hasNext () )
            {
                final Map.Entry<N, CheckState> entry = iterator.next ();
                if ( entry.getValue () == CheckState.mixed )
                {
                    // Updating node state
                    final N node = entry.getKey ();
                    toUpdate.add ( node );
                    iterator.remove ();

                    // Saving changes
                    if ( changes != null )
                    {
                        changes.add ( new CheckStateChange<N> ( node, CheckState.mixed, CheckState.unchecked ) );
                    }
                }
            }
        }
        repaintTreeNodes ( toUpdate );

        // Informing about state changes
        fireCheckStateChanged ( changes );
    }

    /**
     * Filters out all nodes which are children of other nodes presented in the list.
     *
     * @param nodes list of nodes to filter
     */
    protected void filterOutChildNodes ( final List<N> nodes )
    {
        final Iterator<N> checkedIterator = nodes.iterator ();
        while ( checkedIterator.hasNext () )
        {
            final N node = checkedIterator.next ();
            for ( final N otherNode : nodes )
            {
                if ( isChildNode ( node, otherNode ) )
                {
                    checkedIterator.remove ();
                    break;
                }
            }
        }
    }

    /**
     * Returns whether the specified node is a child of another node or some of its child nodes or not.
     *
     * @param node   node to process
     * @param parent node to compare parent nodes with
     * @return {@code true} if the specified node is a child of another node or some of its child nodes, {@code false} otherwise
     */
    protected boolean isChildNode ( final N node, final N parent )
    {
        if ( node == parent )
        {
            return false;
        }
        else if ( parent == null )
        {
            return true;
        }
        else
        {
            N nodeParent = getParent ( node );
            while ( nodeParent != null )
            {
                if ( nodeParent == parent )
                {
                    return true;
                }
                nodeParent = getParent ( nodeParent );
            }
            return false;
        }
    }

    /**
     * Returns parent {@link MutableTreeNode} for the specified {@link MutableTreeNode}.
     *
     * @param node {@link MutableTreeNode} to find parent for
     * @return parent {@link MutableTreeNode} for the specified {@link MutableTreeNode}
     */
    protected N getParent ( final N node )
    {
        return ( N ) node.getParent ();
    }

    /**
     * Returns child {@link MutableTreeNode} for the specified parent {@link MutableTreeNode} at the specified index.
     *
     * @param parent parent {@link MutableTreeNode}
     * @param index  child {@link MutableTreeNode} index
     * @return child {@link MutableTreeNode} for the specified parent {@link MutableTreeNode} at the specified index
     */
    protected N getChildAt ( final N parent, final int index )
    {
        return ( N ) parent.getChildAt ( index );
    }

    /**
     * Returns amount of children for the specified parent {@link MutableTreeNode}.
     *
     * @param parent {@link MutableTreeNode} to count children for
     * @return amount of children for the specified parent {@link MutableTreeNode}
     */
    protected int getChildCount ( final N parent )
    {
        return parent.getChildCount ();
    }

    /**
     * Repaints visible tree rect.
     */
    protected void repaintVisibleTreeRect ()
    {
        checkBoxTree.repaint ( checkBoxTree.getVisibleRect () );
    }

    /**
     * Repaints specified tree nodes.
     *
     * @param nodes tree nodes to repaint
     */
    protected void repaintTreeNodes ( final List<N> nodes )
    {
        checkBoxTree.repaint ( nodes );
    }

    @Override
    public void addCheckStateChangeListener ( final CheckStateChangeListener listener )
    {
        listeners.add ( CheckStateChangeListener.class, listener );
    }

    @Override
    public void removeCheckStateChangeListener ( final CheckStateChangeListener listener )
    {
        listeners.remove ( CheckStateChangeListener.class, listener );
    }

    /**
     * Informs about single or multiple check state changes.
     *
     * @param stateChanges check state changes list
     */
    public void fireCheckStateChanged ( final List<CheckStateChange<N>> stateChanges )
    {
        if ( stateChanges != null )
        {
            for ( final CheckStateChangeListener<N> listener : listeners.getListeners ( CheckStateChangeListener.class ) )
            {
                listener.checkStateChanged ( checkBoxTree, stateChanges );
            }
        }
    }
}