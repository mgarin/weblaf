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

import com.alee.extended.checkbox.CheckState;

import javax.swing.tree.DefaultMutableTreeNode;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

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
    protected Map<DefaultMutableTreeNode, CheckState> nodeCheckStates = new WeakHashMap<DefaultMutableTreeNode, CheckState> ();

    /**
     * @param checkBoxTree
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

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isChecked ( final E node )
    {
        return getCheckState ( node ) == CheckState.checked;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isPartiallyChecked ( final E node )
    {
        return getCheckState ( node ) == CheckState.mixed;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CheckState getCheckState ( final E node )
    {
        final CheckState checkState = nodeCheckStates.get ( node );
        return checkState != null ? checkState : CheckState.unchecked;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setChecked ( final E node, final boolean checked )
    {
        final CheckState oldState = getCheckState ( node );
        final CheckState newState = checked ? CheckState.checked : CheckState.unchecked;

        // Updating node if check state has actually changed
        if ( oldState != newState )
        {
            final List<E> toUpdate = new ArrayList<E> ();

            // Changing node check state
            updateNodeState ( node, newState, toUpdate );

            if ( checkBoxTree.isRecursiveCheckingEnabled () )
            {
                // Changing node childs check state
                if ( node.getChildCount () > 0 )
                {
                    updateChildNodesState ( node, newState, toUpdate );
                }

                // Changing node parents check state
                updateParentStates ( node, toUpdate );
            }

            checkBoxTree.repaint ( toUpdate );
        }
    }

    /**
     * Updates parent nodes check states.
     *
     * @param node     node to start checking parents from
     * @param toUpdate list of nodes for later update
     */
    protected void updateParentStates ( final E node, final List<E> toUpdate )
    {
        final E parent = ( E ) node.getParent ();
        if ( parent == null )
        {
            return;
        }

        boolean partially = false;
        boolean checked = true;
        for ( int i = 0; i < parent.getChildCount (); i++ )
        {
            final E child = ( E ) parent.getChildAt ( i );
            switch ( getCheckState ( child ) )
            {
                case unchecked:
                    checked = false;
                    break;
                case checked:
                    partially = true;
                    break;
                case mixed:
                    checked = false;
                    partially = true;
                    break;
            }
        }
        if ( checked )
        {
            updateNodeState ( parent, CheckState.checked, toUpdate );
        }
        else if ( partially )
        {
            updateNodeState ( parent, CheckState.mixed, toUpdate );
        }
        else
        {
            updateNodeState ( parent, CheckState.unchecked, toUpdate );
        }

        updateParentStates ( parent, toUpdate );
    }

    /**
     * Updates child nodes check state.
     *
     * @param node     parent node
     * @param newState new check state
     * @param toUpdate list of nodes for later update
     */
    protected void updateChildNodesState ( final E node, final CheckState newState, final List<E> toUpdate )
    {
        for ( int i = 0; i < node.getChildCount (); i++ )
        {
            final E childNode = ( E ) node.getChildAt ( i );
            updateNodeState ( childNode, newState, toUpdate );
            if ( childNode.getChildCount () > 0 )
            {
                updateChildNodesState ( childNode, newState, toUpdate );
            }
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

    /**
     * {@inheritDoc}
     */
    @Override
    public void invertCheck ( final E node )
    {
        setChecked ( node, getNextState ( getCheckState ( node ) ) == CheckState.checked );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void invertCheck ( final List<E> nodes )
    {
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
            setChecked ( node, check );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void uncheckAll ()
    {
        nodeCheckStates.clear ();
        checkBoxTree.repaint ();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void checkAll ()
    {
        final List<E> allNodes = checkBoxTree.getAllNodes ();
        for ( final E node : allNodes )
        {
            nodeCheckStates.put ( node, CheckState.checked );
        }
        checkBoxTree.repaint ();
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
}