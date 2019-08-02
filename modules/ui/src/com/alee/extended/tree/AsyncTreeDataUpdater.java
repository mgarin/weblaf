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

import java.util.List;

/**
 * This interface provides methods for asynchronous tree data update.
 * Basically these methods called when some tree node is renamed, moved or removed.
 * This interface will be informed about such changes so you can perform data update actions.
 *
 * @param <N> node type
 * @author Mikle Garin
 */
public interface AsyncTreeDataUpdater<N extends AsyncUniqueNode>
{
    /**
     * Called when nodes add operation performed.
     * At this point nodes are already added visually, but you can still cancel this action if you cannot update data properly.
     *
     * @param nodes      added nodes list
     * @param parentNode parent node where specified nodes were added
     * @param rollback   runnable you should call in case data update failed, it will cancel changes
     */
    public void nodesAdded ( List<N> nodes, N parentNode, Runnable rollback );

    /**
     * Called when node rename operation performed.
     * At this point node is already renamed visually, but you can still cancel this action if you cannot update data properly.
     *
     * @param node     renamed node
     * @param oldName  old node name
     * @param newName  new node name
     * @param rollback runnable you should call in case data update failed, it will cancel changes
     */
    public void nodeRenamed ( N node, String oldName, String newName, Runnable rollback );

    /**
     * Called when node move (D&amp;D or cut/paste) operation performed.
     * At this point nodes are already moved visually, but you can still cancel this action if you cannot update data properly.
     *
     * @param nodes     moved nodes list
     * @param oldParent old parent node
     * @param newParent new parent node
     * @param rollback  runnable you should call in case data update failed, it will cancel changes
     */
    public void nodesMoved ( List<N> nodes, N oldParent, N newParent, Runnable rollback );

    /**
     * Called when node copy (D&amp;D or copy/paste) operation performed.
     * At this point nodes are already copied visually, but you can still cancel this action if you cannot update data properly.
     *
     * @param nodes     moved nodes list
     * @param oldParent old parent node
     * @param newParent new parent node
     * @param rollback  runnable you should call in case data update failed, it will cancel changes
     */
    public void nodesCopied ( List<N> nodes, N oldParent, N newParent, Runnable rollback );

    /**
     * Called when nodes remove operation performed.
     * At this point nodes are already removed visually, but you can still cancel this action if you cannot update data properly.
     *
     * @param nodes    removed nodes list
     * @param rollback runnable you should call in case data update failed, it will cancel changes
     */
    public void nodesRemoved ( List<N> nodes, Runnable rollback );
}