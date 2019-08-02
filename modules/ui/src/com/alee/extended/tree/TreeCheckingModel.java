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

import javax.swing.tree.MutableTreeNode;
import java.util.Collection;
import java.util.List;

/**
 * Special checking model for {@link WebCheckBoxTree}.
 *
 * @param <N> node type
 * @author Mikle Garin
 */
public interface TreeCheckingModel<N extends MutableTreeNode>
{
    /**
     * Returns list of nodes for the specified state.
     *
     * @param state  {@link CheckState} to return nodes for
     * @param policy {@link NodesAcceptPolicy} that defines a way to filter nodes
     * @return list of nodes for the specified state
     */
    public List<N> getNodes ( CheckState state, NodesAcceptPolicy policy );

    /**
     * Returns specified tree node check state.
     *
     * @param node tree node to process
     * @return specified tree node check state
     */
    public CheckState getCheckState ( N node );

    /**
     * Sets whether the specified tree node is checked or not.
     *
     * @param node    tree node to process
     * @param checked whether the specified tree node should be checked or not
     */
    public void setChecked ( N node, boolean checked );

    /**
     * Sets specified nodes state to checked.
     *
     * @param nodes   nodes to check
     * @param checked whether the specified tree nodes should be checked or not
     */
    public void setChecked ( Collection<N> nodes, boolean checked );

    /**
     * Inverts tree node check.
     *
     * @param node tree node to process
     */
    public void invertCheck ( N node );

    /**
     * Inverts tree nodes check.
     *
     * @param nodes list of tree nodes to process
     */
    public void invertCheck ( Collection<N> nodes );

    /**
     * Checks all tree nodes.
     */
    public void checkAll ();

    /**
     * Unchecks all tree nodes.
     */
    public void uncheckAll ();

    /**
     * Notifies model about checking mode change.
     *
     * @param recursive whether checked or unchecked node children should be checked or unchecked recursively or not
     */
    public void checkingModeChanged ( boolean recursive );

    /**
     * Adds check state change listener.
     *
     * @param listener check state change listener to add
     */
    public void addCheckStateChangeListener ( CheckStateChangeListener listener );

    /**
     * Removes check state change listener.
     *
     * @param listener check state change listener to remove
     */
    public void removeCheckStateChangeListener ( CheckStateChangeListener listener );
}