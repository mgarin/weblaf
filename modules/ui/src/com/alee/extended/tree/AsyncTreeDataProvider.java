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

import com.alee.utils.compare.Filter;

import java.util.Comparator;

/**
 * This interface provides methods for asynchronous tree data retrieval.
 *
 * @param <E> custom node type
 * @author Mikle Garin
 * @see com.alee.extended.tree.WebAsyncTree
 * @see com.alee.extended.tree.AsyncTreeModel
 */

public interface AsyncTreeDataProvider<E extends AsyncUniqueNode>
{
    /**
     * Returns asynchronous tree root node.
     * This request uses the EDT and should be processed quickly.
     *
     * @return root node
     */
    public E getRoot ();

    /**
     * Starts loading child nodes for the specified asynchronous tree node.
     * When you finish loading children for the specified node or you failed to load them, simply inform the listener about that.
     * This request uses a separate thread and might take a lot of time to process without having any UI issues.
     *
     * @param node     parent node
     * @param listener children loading progress listener
     */
    public void loadChildren ( E node, ChildrenListener<E> listener );

    /**
     * Returns child nodes comparator for the specified asynchronous tree node.
     * No sorting applied to children in case null is returned.
     *
     * @param node parent node
     * @return child nodes comparator
     */
    public Comparator<E> getChildrenComparator ( E node );

    /**
     * Returns child nodes filter for the specified asynchronous tree node.
     * No filtering applied to children in case null is returned.
     *
     * @param node parent node
     * @return child nodes filter
     */
    public Filter<E> getChildrenFilter ( E node );

    /**
     * Returns whether the specified node is leaf (doesn't have any children) or not.
     * This request uses the EDT and should be processed quickly.
     * If you are not sure if the node is leaf or not - simply return false, that will allow the tree to expand this node on request.
     *
     * @param node node
     * @return true if the specified node is leaf, false otherwise
     */
    public boolean isLeaf ( E node );
}