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
 * @author Mikle Garin
 */

public interface AsyncTreeDataUpdater<E extends AsyncUniqueNode>
{
    /**
     * Called when node add operation performed.
     * At this point node is already added visually, but you can still cancel this action if you cannot update data properly.
     *
     * @param nodes      added nodes list
     * @param parentNode parent node where specified nodes were added
     * @param addFailed  runnable you should call in case data update failed, it will cancel changes
     */
    public void nodesAdded ( List<E> nodes, E parentNode, Runnable addFailed );

    /**
     * Called when node rename operation performed.
     * At this point node is already renamed visually, but you can still cancel this action if you cannot update data properly.
     *
     * @param node         renamed node
     * @param oldName      old node name
     * @param newName      new node name
     * @param renameFailed runnable you should call in case data update failed, it will cancel changes
     */
    public void nodeRenamed ( E node, String oldName, String newName, Runnable renameFailed );

    /**
     * Called when node move (D&D or cut/paste) operation performed.
     * At this point node is already moved visually, but you can still cancel this action if you cannot update data properly.
     *
     * @param node       moved node
     * @param oldParent  old parent node
     * @param newParent  new parent node
     * @param moveFailed runnable you should call in case data update failed, it will cancel changes
     */
    public void nodeMoved ( E node, E oldParent, E newParent, Runnable moveFailed );

    /**
     * Called when node remove operation performed.
     * At this point node is already removed visually, but you can still cancel this action if you cannot update data properly.
     *
     * @param node         removed node
     * @param removeFailed runnable you should call in case data update failed, it will cancel changes
     */
    public void nodeRemoved ( E node, Runnable removeFailed );
}