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

package com.alee.laf.tree.walker;

import com.alee.api.jdk.Consumer;
import com.alee.api.jdk.Predicate;

import javax.swing.tree.TreeNode;

/**
 * Interface for classes providing tree walking functionality.
 * Using a {@link TreeWalker} you can quickly iterate through {@link javax.swing.JTree} nodes to perform actions or check conditions.
 * Common {@link TreeWalker} implementations will only iterate through nodes that currently exist within {@link javax.swing.tree.TreeModel}.
 *
 * @param <N> node type
 * @author Mikle Garin
 */

public interface TreeWalker<N extends TreeNode>
{
    /**
     * Performs specified action for each node this walker passes.
     *
     * @param action {@link Consumer} containing action to perform on each node
     */
    public void forEach ( Consumer<N> action );

    /**
     * Returns whether or not any of nodes walker passes match specified predicate.
     *
     * @param predicate {@link Predicate} containin condition for nodes
     * @return {@code true} if any of nodes walker passes match specified predicate, {@code false} otherwise
     */
    public boolean anyMatch ( Predicate<N> predicate );

    /**
     * Returns whether or not all of nodes walker passes match specified predicate.
     *
     * @param predicate {@link Predicate} containin condition for nodes
     * @return {@code true} if all of nodes walker passes match specified predicate, {@code false} otherwise
     */
    public boolean allMatch ( Predicate<N> predicate );

    /**
     * Returns whether or not none of nodes walker passes match specified predicate.
     *
     * @param predicate {@link Predicate} containin condition for nodes
     * @return {@code true} if none of nodes walker passes match specified predicate, {@code false} otherwise
     */
    public boolean noneMatch ( Predicate<N> predicate );
}