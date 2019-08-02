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

import javax.swing.tree.MutableTreeNode;
import java.util.EventListener;
import java.util.List;

/**
 * Special listener for {@link WebCheckBoxTree} check state changes.
 *
 * @param <N> {@link MutableTreeNode} type
 * @author Mikle Garin
 */
public interface CheckStateChangeListener<N extends MutableTreeNode> extends EventListener
{
    /**
     * Informs about {@link CheckStateChange}s within {@link WebCheckBoxTree}.
     *
     * @param tree         {@link WebCheckBoxTree}
     * @param stateChanges {@link List} of {@link CheckStateChange}s
     */
    public void checkStateChanged ( WebCheckBoxTree<N> tree, List<CheckStateChange<N>> stateChanges );
}