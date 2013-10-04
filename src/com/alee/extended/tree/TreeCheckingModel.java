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
import java.util.Collection;
import java.util.List;

/**
 * Special checking model for WebCheckBoxTree.
 *
 * @author Mikle Garin
 */

public interface TreeCheckingModel<E extends DefaultMutableTreeNode>
{
    /**
     * Returns list of checked nodes.
     *
     * @return list of checked nodes
     */
    public List<E> getCheckedNodes ();

    /**
     * Returns list of nodes in mixed state.
     *
     * @return list of nodes in mixed state
     */
    public List<E> getMixedNodes ();

    /**
     * Sets specified nodes state to checked.
     *
     * @param nodes nodes to check
     */
    public void setChecked ( Collection<E> nodes );

    /**
     * Sets specified nodes state to unchecked.
     *
     * @param nodes nodes to uncheck
     */
    public void setUnchecked ( Collection<E> nodes );

    /**
     * Returns specified tree node check state.
     *
     * @param node tree node to process
     * @return specified tree node check state
     */
    public CheckState getCheckState ( E node );

    /**
     * Sets whether the specified tree node is checked or not.
     *
     * @param node    tree node to process
     * @param checked whether the specified tree node is checked or not
     */
    public void setChecked ( E node, boolean checked );

    /**
     * Inverts tree node check.
     *
     * @param node tree node to process
     */
    public void invertCheck ( E node );

    /**
     * Inverts tree nodes check.
     *
     * @param nodes list of tree nodes to process
     */
    public void invertCheck ( List<E> nodes );

    /**
     * Unchecks all tree nodes.
     */
    public void uncheckAll ();

    /**
     * Checks all tree nodes.
     */
    public void checkAll ();
}