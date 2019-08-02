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

import com.alee.laf.tree.UniqueNode;

import java.util.EventListener;
import java.util.List;

/**
 * Custom callback that is used to inform transfer handler about drop results.
 * This listener was created to enable asynchronous drop operations in {@link com.alee.extended.tree.AbstractTreeTransferHandler}.
 *
 * @param <N> nodes type
 * @author Mikle Garin
 */
public interface NodesDropCallback<N extends UniqueNode> extends EventListener
{
    /**
     * Informs transfer handler that specified nodes drop was successfully completed.
     * This method exists to enable partial drop operation completion and UI updates.
     * Every time you will provide nodes here they will be visually added onto the tree.
     * Call this method after you have performed all data-related operations with the dropped nodes.
     *
     * @param nodes nodes that were dropped successfully
     */
    public void dropped ( N... nodes );

    /**
     * Informs transfer handler that specified nodes drop was successfully completed.
     * This method exists to enable partial drop operation completion and UI updates.
     * Every time you will provide nodes here they will be visually added onto the tree.
     * Call this method after you have performed all data-related operations with the dropped nodes.
     *
     * @param nodes nodes that were dropped successfully
     */
    public void dropped ( List<N> nodes );

    /**
     * Informs transfer handler that nodes drop was successfully completed.
     * Call this method after all {@link #dropped(java.util.List)} operations are done.
     */
    public void completed ();

    /**
     * Informs transfer handler that nodes drop has partially or fully failed.
     * Call this methid after one of {@link #dropped(java.util.List)} operations have failed.
     *
     * @param cause exception
     */
    public void failed ( Throwable cause );
}