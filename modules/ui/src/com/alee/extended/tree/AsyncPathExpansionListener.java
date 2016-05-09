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

/**
 * This custom listener is used to track async tree path expansion action.
 *
 * @param <E> node type
 * @author Mikle Garin
 */

public interface AsyncPathExpansionListener<E extends AsyncUniqueNode>
{
    /**
     * Notifies that path expansion has failed and did not find even a single node.
     * This might happen in case there is no loaded nodes with an ID from the path in the moment when expansion is invoked.
     */
    public void pathFailedToExpand ();

    /**
     * Notifies that one of path nodes was just expanded.
     * This might be called a few times per single path expansion depending on the length of the path and some other factors.
     *
     * @param expandedNode recently expanded node from the specified path
     */
    public void pathNodeExpanded ( final E expandedNode );

    /**
     * Notifies that path expansion was not fully finished and cannot be continued.
     * Last node that could be expanded from the specified path is returned.
     * There might be a lot of reasons why this happened - failed node children loading, unexisting node ID etc.
     *
     * @param lastFoundNode last reached path node
     */
    public void pathPartiallyExpanded ( final E lastFoundNode );

    /**
     * Notifies that path expansion has finished succesfully.
     * That means that the last path node was actually reached in the tree.
     *
     * @param lastNode last path node
     */
    public void pathExpanded ( final E lastNode );
}