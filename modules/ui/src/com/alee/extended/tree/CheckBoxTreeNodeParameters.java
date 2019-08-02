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
import com.alee.laf.tree.TreeNodeParameters;

import javax.swing.tree.MutableTreeNode;

/**
 * {@link WebCheckBoxTree} single {@link MutableTreeNode} rendering parameters.
 *
 * @param <N> {@link MutableTreeNode} type
 * @param <C> {@link WebCheckBoxTree} type
 * @author Mikle Garin
 */
public class CheckBoxTreeNodeParameters<N extends MutableTreeNode, C extends WebCheckBoxTree<N>> extends TreeNodeParameters<N, C>
{
    /**
     * Whether or not checkbox for this {@link MutableTreeNode} is currently visible.
     */
    protected final boolean checkBoxVisible;

    /**
     * Whether or not checkbox for this {@link MutableTreeNode} is currently enabled.
     */
    protected final boolean checkBoxEnabled;

    /**
     * {@link CheckState} for this {@link MutableTreeNode}.
     */
    protected final CheckState checkState;

    /**
     * Constructs new {@link CheckBoxTreeNodeParameters}.
     * Parameters are calculated within this constuctor when used.
     *
     * @param tree {@link WebCheckBoxTree}
     * @param node {@link MutableTreeNode}
     */
    public CheckBoxTreeNodeParameters ( final C tree, final N node )
    {
        super ( tree, node );
        this.checkBoxVisible = tree.isCheckBoxVisible () && tree.isCheckBoxVisible ( node );
        this.checkBoxEnabled = tree.isEnabled () && tree.isCheckingByUserEnabled () && tree.isCheckBoxEnabled ( node );
        this.checkState = tree.getCheckState ( node );
    }

    /**
     * Constructs new {@link TreeNodeParameters}.
     * Parameters are predefined whenever this constuctor is used.
     *
     * @param tree     {@link WebCheckBoxTree}
     * @param node     {@link MutableTreeNode}
     * @param row      {@link MutableTreeNode} row number
     * @param leaf     whether or not {@link MutableTreeNode} is leaf
     * @param selected whether or not {@link MutableTreeNode} is selected
     * @param expanded whether or not {@link MutableTreeNode} is expanded
     * @param focused  whether or not {@link MutableTreeNode} has focus
     */
    public CheckBoxTreeNodeParameters ( final C tree, final N node, final int row, final boolean leaf,
                                        final boolean selected, final boolean expanded, final boolean focused )
    {
        super ( tree, node, row, leaf, selected, expanded, focused );
        this.checkBoxVisible = tree.isCheckBoxVisible () && tree.isCheckBoxVisible ( node );
        this.checkBoxEnabled = tree.isEnabled () && tree.isCheckingByUserEnabled () && tree.isCheckBoxEnabled ( node );
        this.checkState = tree.getCheckState ( node );
    }

    /**
     * Returns whether or not checkbox for this {@link MutableTreeNode} is currently visible.
     *
     * @return {@code true} if checkbox for this {@link MutableTreeNode} is currently visible, {@code false} otherwise
     */
    public boolean isCheckBoxVisible ()
    {
        return checkBoxVisible;
    }

    /**
     * Returns whether or not checkbox for this {@link MutableTreeNode} is currently enabled.
     *
     * @return {@code true} if checkbox for this {@link MutableTreeNode} is currently enabled, {@code false} otherwise
     */
    public boolean isCheckBoxEnabled ()
    {
        return checkBoxEnabled;
    }

    /**
     * Returns {@link CheckState} for this {@link MutableTreeNode}.
     *
     * @return {@link CheckState} for this {@link MutableTreeNode}
     */
    public CheckState checkState ()
    {
        return checkState;
    }
}