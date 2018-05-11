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

package com.alee.laf.tree;

import com.alee.api.jdk.Objects;
import com.alee.api.ui.RenderingParameters;

import javax.swing.*;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

/**
 * {@link JTree} single {@link TreeNode} rendering parameters.
 *
 * @param <N> {@link TreeNode} type
 * @param <C> {@link JTree} type
 * @author Mikle Garin
 */
public class TreeNodeParameters<N extends TreeNode, C extends JTree> implements RenderingParameters
{
    /**
     * {@link JTree}.
     */
    protected final C tree;

    /**
     * {@link TreeNode}.
     */
    protected final N node;

    /**
     * {@link TreeNode} row number.
     */
    protected final int row;

    /**
     * Whether or not {@link TreeNode} is leaf.
     */
    protected final boolean leaf;

    /**
     * Whether or not {@link TreeNode} is selected.
     */
    protected final boolean selected;

    /**
     * Whether or not {@link TreeNode} is expanded.
     */
    protected final boolean expanded;

    /**
     * Whether or not {@link TreeNode} has focus.
     */
    protected final boolean focused;

    /**
     * Constructs new {@link TreeNodeParameters}.
     * Parameters are calculated within this constuctor when used.
     *
     * @param tree {@link JTree}
     * @param area {@link TreeCellArea}
     */
    public TreeNodeParameters ( final C tree, final TreeCellArea<? extends N, C> area )
    {
        this ( tree, ( N ) tree.getPathForRow ( area.row () ).getLastPathComponent () );
    }

    /**
     * Constructs new {@link TreeNodeParameters}.
     * Parameters are calculated within this constuctor when used.
     *
     * @param tree {@link JTree}
     * @param node {@link TreeNode}
     */
    public TreeNodeParameters ( final C tree, final N node )
    {
        final TreePath treePath = TreeUtils.getTreePath ( node );
        this.tree = Objects.requireNonNull ( tree, "Tree must not be null" );
        this.node = node;
        this.row = tree.getRowForPath ( treePath );
        this.leaf = tree.getModel ().isLeaf ( node );
        this.selected = tree.isPathSelected ( treePath );
        this.expanded = tree.isExpanded ( treePath );
        this.focused = tree.hasFocus () && tree.getLeadSelectionRow () == row;
    }

    /**
     * Constructs new {@link TreeNodeParameters}.
     * Parameters are predefined whenever this constuctor is used.
     *
     * @param tree     {@link JTree}
     * @param node     {@link TreeNode}
     * @param row      {@link TreeNode} row number
     * @param leaf     whether or not {@link TreeNode} is leaf
     * @param selected whether or not {@link TreeNode} is selected
     * @param expanded whether or not {@link TreeNode} is expanded
     * @param focused  whether or not {@link TreeNode} has focus
     */
    public TreeNodeParameters ( final C tree, final N node, final int row, final boolean leaf,
                                final boolean selected, final boolean expanded, final boolean focused )
    {
        this.tree = Objects.requireNonNull ( tree, "Tree must not be null" );
        this.node = node;
        this.row = row;
        this.leaf = leaf;
        this.selected = selected;
        this.expanded = expanded;
        this.focused = focused;
    }

    /**
     * Returns {@link JTree}.
     *
     * @return {@link JTree}
     */
    public C tree ()
    {
        return tree;
    }

    /**
     * Returns {@link TreeNode}.
     *
     * @return {@link TreeNode}
     */
    public N node ()
    {
        return node;
    }

    /**
     * Returns {@link TreeNode} row number.
     *
     * @return {@link TreeNode} row number
     */
    public int row ()
    {
        return row;
    }

    /**
     * Returns whether or not {@link TreeNode} is leaf.
     *
     * @return {@code true} if {@link TreeNode} is leaf, {@code false} otherwise
     */
    public boolean isLeaf ()
    {
        return leaf;
    }

    /**
     * Returns whether or not {@link TreeNode} is selected.
     *
     * @return {@code true} if {@link TreeNode} is selected, {@code false} otherwise
     */
    public boolean isSelected ()
    {
        return selected;
    }

    /**
     * Returns whether or not {@link TreeNode} is expanded.
     *
     * @return {@code true} if {@link TreeNode} is expanded, {@code false} otherwise
     */
    public boolean isExpanded ()
    {
        return expanded;
    }

    /**
     * Returns whether or not {@link TreeNode} has focus.
     *
     * @return {@code true} if {@link TreeNode} has focus, {@code false} otherwise
     */
    public boolean isFocused ()
    {
        return focused;
    }
}