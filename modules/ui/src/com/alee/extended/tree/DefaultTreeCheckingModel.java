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
import com.alee.laf.tree.TreeUtils;
import com.alee.utils.CollectionUtils;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;
import java.util.*;

/**
 * Default checking model for WebCheckBoxTree.
 *
 * @author Mikle Garin
 */

public class DefaultTreeCheckingModel<E extends DefaultMutableTreeNode> implements TreeCheckingModel<E>
{
    /**
     * Checkbox tree which uses this checking model.
     */
    protected WebCheckBoxTree<E> checkBoxTree;

    /**
     * Node check states cache.
     */
    protected Map<E, CheckState> nodeCheckStates = new WeakHashMap<E, CheckState> ();

    /**
     * Checkbox tree check state change listeners.
     */
    protected List<CheckStateChangeListener<E>> checkStateChangeListeners = new ArrayList<CheckStateChangeListener<E>> ( 1 );

    /**
     * @param checkBoxTree checkbox tree which uses this checking model.
     */
    public DefaultTreeCheckingModel ( final WebCheckBoxTree<E> checkBoxTree )
    {
        super ();
        this.checkBoxTree = checkBoxTree;
    }

    /**
     * Returns checkbox tree which uses this checking model.
     *
     * @return checkbox tree which uses this checking model
     */
    public WebCheckBoxTree<E> getCheckBoxTree ()
    {
        return checkBoxTree;
    }

    /**
     * Sets checkbox tree which uses this checking model.
     * This method call also forces cache to clear all stored values.
     *
     * @param checkBoxTree checkbox tree which uses this checking model.
     */
    public void setCheckBoxTree ( final WebCheckBoxTree<E> checkBoxTree )
    {
        nodeCheckStates.clear ();
        this.checkBoxTree = checkBoxTree;
    }

    @Override
    public List<E> getCheckedNodes ( final boolean optimize )
    {
        return getAllNodesForState ( CheckState.checked, optimize );
    }

    @Override
    public List<E> getMixedNodes ()
    {
        return getAllNodesForState ( CheckState.mixed, false );
    }

    /**
     * Returns list of nodes for the specified state.
     * For a reasonable cause this will not work for unchecked state.
     *
     * @param state    check state
     * @param optimize whether should optimize selected nodes or not
     * @return list of nodes for the specified state
     */
    protected List<E> getAllNodesForState ( final CheckState state, final boolean optimize )
    {
        final List<E> checkedNodes = new ArrayList<E> ( nodeCheckStates.size () );
        for ( final Map.Entry<E, CheckState> entry : nodeCheckStates.entrySet () )
        {
            if ( entry.getValue () == state )
            {
                checkedNodes.add ( entry.getKey () );
            }
        }
        if ( optimize )
        {
            TreeUtils.optimizeNodes ( checkedNodes );
        }
        return checkedNodes;
    }

    @Override
    public void setChecked ( final Collection<E> nodes, final boolean checked )
    {
        // Collecting state changes
        final boolean collectChanges = checkStateChangeListeners.size () > 0;
        List<CheckStateChange<E>> changes = null;
        if ( collectChanges )
        {
            changes = new ArrayList<CheckStateChange<E>> ( nodes.size () );
        }

        // Updating states
        final List<E> toUpdate = new ArrayList<E> ();
        for ( final E node : nodes )
        {
            setCheckedImpl ( node, checked, toUpdate, changes );
        }
        repaintTreeNodes ( toUpdate );

        // Informing about state changes
        fireCheckStateChanged ( changes );
    }

    @Override
    public CheckState getCheckState ( final E node )
    {
        final CheckState checkState = nodeCheckStates.get ( node );
        return checkState != null ? checkState : CheckState.unchecked;
    }

    @Override
    public void setChecked ( final E node, final boolean checked )
    {
        // Collecting state changes
        final boolean collectChanges = checkStateChangeListeners.size () > 0;
        List<CheckStateChange<E>> changes = null;
        if ( collectChanges )
        {
            changes = new ArrayList<CheckStateChange<E>> ( 1 );
        }

        // Updating states
        final List<E> toUpdate = new ArrayList<E> ();
        setCheckedImpl ( node, checked, toUpdate, changes );
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
    protected void setCheckedImpl ( final E node, final boolean checked, final List<E> toUpdate, final List<CheckStateChange<E>> changes )
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
                changes.add ( new CheckStateChange<E> ( node, oldState, newState ) );
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
    protected void updateParentStates ( final E node, final List<E> toUpdate, final List<CheckStateChange<E>> changes )
    {
        // Updating all parent node states
        E parent = ( E ) node.getParent ();
        while ( parent != null )
        {
            // Calculating parent state
            CheckState state = CheckState.unchecked;
            boolean hasChecked = false;
            boolean hasUnchecked = false;
            for ( int i = 0; i < parent.getChildCount (); i++ )
            {
                final CheckState checkState = getCheckState ( ( E ) parent.getChildAt ( i ) );
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
                    changes.add ( new CheckStateChange<E> ( parent, oldState, state ) );
                }

                // Updating state
                updateNodeState ( parent, state, toUpdate );
            }

            // Moving upstairs
            parent = ( E ) parent.getParent ();
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
    protected void updateChildNodesState ( final E node, final CheckState newState, final List<E> toUpdate,
                                           final List<CheckStateChange<E>> changes )
    {
        for ( int i = 0; i < node.getChildCount (); i++ )
        {
            final E childNode = ( E ) node.getChildAt ( i );

            // Saving changes
            if ( changes != null )
            {
                changes.add ( new CheckStateChange<E> ( childNode, getCheckState ( childNode ), newState ) );
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
    protected void updateNodeState ( final E node, final CheckState newState, final List<E> toUpdate )
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
    public void invertCheck ( final E node )
    {
        // Collecting state changes
        final boolean collectChanges = checkStateChangeListeners.size () > 0;
        List<CheckStateChange<E>> changes = null;
        if ( collectChanges )
        {
            changes = new ArrayList<CheckStateChange<E>> ( 1 );
        }

        // Updating states
        final List<E> toUpdate = new ArrayList<E> ();
        setCheckedImpl ( node, getNextState ( getCheckState ( node ) ) == CheckState.checked, toUpdate, changes );
        repaintTreeNodes ( toUpdate );

        // Informing about state changes
        fireCheckStateChanged ( changes );
    }

    @Override
    public void invertCheck ( final Collection<E> nodes )
    {
        // Collecting state changes
        final boolean collectChanges = checkStateChangeListeners.size () > 0;
        List<CheckStateChange<E>> changes = null;
        if ( collectChanges )
        {
            changes = new ArrayList<CheckStateChange<E>> ( nodes.size () );
        }

        // Updating states
        final List<E> toUpdate = new ArrayList<E> ();
        boolean check = false;
        for ( final E node : nodes )
        {
            if ( getCheckState ( node ) != CheckState.checked )
            {
                check = true;
                break;
            }
        }
        for ( final E node : nodes )
        {
            setCheckedImpl ( node, check, toUpdate, changes );
        }
        repaintTreeNodes ( toUpdate );

        // Informing about state changes
        fireCheckStateChanged ( changes );
    }

    @Override
    public void uncheckAll ()
    {
        // Collecting state changes
        List<CheckStateChange<E>> changes = null;
        if ( checkStateChangeListeners.size () > 0 )
        {
            changes = new ArrayList<CheckStateChange<E>> ( nodeCheckStates.size () );
            for ( final Map.Entry<E, CheckState> entry : nodeCheckStates.entrySet () )
            {
                final CheckState state = entry.getValue ();
                if ( state == CheckState.mixed || state == CheckState.checked )
                {
                    changes.add ( new CheckStateChange<E> ( entry.getKey (), state, CheckState.unchecked ) );
                }
            }
        }

        // Updating states
        nodeCheckStates.clear ();
        repaintVisibleTreeRect ();

        // Informing about state changes
        fireCheckStateChanged ( changes );
    }

    @Override
    public void checkAll ()
    {
        final List<E> allNodes = checkBoxTree.getAllNodes ();

        // Collecting state changes
        List<CheckStateChange<E>> changes = null;
        if ( checkStateChangeListeners.size () > 0 )
        {
            changes = new ArrayList<CheckStateChange<E>> ( allNodes.size () );
            for ( final E node : allNodes )
            {
                final CheckState state = getCheckState ( node );
                if ( state != CheckState.checked )
                {
                    changes.add ( new CheckStateChange<E> ( node, state, CheckState.checked ) );
                }
            }
        }

        // Updating states
        for ( final E node : allNodes )
        {
            nodeCheckStates.put ( node, CheckState.checked );
        }
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
            case checked:
                return CheckState.unchecked;
            case mixed:
                return checkBoxTree.isCheckMixedOnToggle () ? CheckState.checked : CheckState.unchecked;
            default:
                return CheckState.unchecked;
        }
    }

    @Override
    public void checkingModeChanged ( final boolean recursive )
    {
        // Collecting state changes
        final boolean collectChanges = checkStateChangeListeners.size () > 0;
        List<CheckStateChange<E>> changes = null;
        if ( collectChanges )
        {
            changes = new ArrayList<CheckStateChange<E>> ();
        }

        // Updating states
        final List<E> toUpdate = new ArrayList<E> ();
        if ( recursive )
        {
            // Retrieving all checked nodes
            final List<E> checked = new ArrayList<E> ( nodeCheckStates.size () );
            for ( final Map.Entry<E, CheckState> entry : nodeCheckStates.entrySet () )
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
            for ( final E node : checked )
            {
                updateParentStates ( node, toUpdate, changes );
                updateChildNodesState ( node, CheckState.checked, toUpdate, changes );
            }
        }
        else
        {
            // Removing existing mixed states
            final Iterator<Map.Entry<E, CheckState>> iterator = nodeCheckStates.entrySet ().iterator ();
            while ( iterator.hasNext () )
            {
                final Map.Entry<E, CheckState> entry = iterator.next ();
                if ( entry.getValue () == CheckState.mixed )
                {
                    // Updating node state
                    final E node = entry.getKey ();
                    toUpdate.add ( node );
                    iterator.remove ();

                    // Saving changes
                    if ( changes != null )
                    {
                        changes.add ( new CheckStateChange<E> ( node, CheckState.mixed, CheckState.unchecked ) );
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
    protected void filterOutChildNodes ( final List<E> nodes )
    {
        final Iterator<E> checkedIterator = nodes.iterator ();
        while ( checkedIterator.hasNext () )
        {
            final E node = checkedIterator.next ();
            for ( final E otherNode : nodes )
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
     * @param node    node to process
     * @param childOf node to compare parent nodes with
     * @return true if the specified node is a child of another node or some of its child nodes, false otherwise
     */
    protected boolean isChildNode ( final E node, final E childOf )
    {
        if ( node == childOf )
        {
            return false;
        }
        else if ( childOf == null )
        {
            return true;
        }
        else
        {
            TreeNode parent = node.getParent ();
            while ( parent != null )
            {
                if ( parent == childOf )
                {
                    return true;
                }
                parent = parent.getParent ();
            }
            return false;
        }
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
    protected void repaintTreeNodes ( final List<E> nodes )
    {
        checkBoxTree.repaint ( nodes );
    }

    @Override
    public void addCheckStateChangeListener ( final CheckStateChangeListener listener )
    {
        checkStateChangeListeners.add ( listener );
    }

    @Override
    public void removeCheckStateChangeListener ( final CheckStateChangeListener listener )
    {
        checkStateChangeListeners.remove ( listener );
    }

    /**
     * Informs about single or multiply check state changes.
     *
     * @param stateChanges check state changes list
     */
    public void fireCheckStateChanged ( final List<CheckStateChange<E>> stateChanges )
    {
        if ( stateChanges != null )
        {
            for ( final CheckStateChangeListener<E> listener : CollectionUtils.copy ( checkStateChangeListeners ) )
            {
                listener.checkStateChanged ( stateChanges );
            }
        }
    }
}