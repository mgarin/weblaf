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

import javax.swing.tree.MutableTreeNode;

/**
 * {@link WebCheckBoxTree} node state change information object.
 *
 * @param <N> node type
 * @author Mikle Garin
 */
public final class CheckStateChange<N extends MutableTreeNode>
{
    /**
     * Node which state was changed.
     */
    private final N node;

    /**
     * Old node check state.
     */
    private final CheckState oldState;

    /**
     * New node check state.
     */
    private final CheckState newState;

    /**
     * Constructs new check state change information object.
     *
     * @param node     node which state was changed
     * @param oldState old node check state
     * @param newState new node check state
     */
    public CheckStateChange ( final N node, final CheckState oldState, final CheckState newState )
    {
        super ();
        this.node = node;
        this.oldState = oldState;
        this.newState = newState;
    }

    /**
     * Returns node which state was changed.
     *
     * @return node which state was changed
     */
    public N getNode ()
    {
        return node;
    }

    /**
     * Returns old node check state.
     *
     * @return old node check state
     */
    public CheckState getOldState ()
    {
        return oldState;
    }

    /**
     * Returns new node check state.
     *
     * @return new node check state
     */
    public CheckState getNewState ()
    {
        return newState;
    }
}