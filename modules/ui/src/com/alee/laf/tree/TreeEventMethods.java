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

import com.alee.api.annotations.NotNull;
import com.alee.api.annotations.Nullable;
import com.alee.api.jdk.Predicate;
import com.alee.utils.swing.extensions.MethodExtension;

import javax.swing.tree.MutableTreeNode;
import java.awt.event.MouseAdapter;

/**
 * This interface provides a set of methods that should be added into components that supports custom WebLaF events.
 * Basically all these methods are already implemented in EventUtils but it is much easier to call them directly from component.
 *
 * @param <N> node type
 * @author Mikle Garin
 * @see com.alee.utils.swing.extensions.MethodExtension
 * @see com.alee.laf.tree.TreeEventMethodsImpl
 */
public interface TreeEventMethods<N extends MutableTreeNode> extends MethodExtension
{
    /**
     * Shortcut method for double-click mouse event on specific tree node.
     *
     * @param runnable mouse event runnable
     * @return used mouse adapter
     */
    public MouseAdapter onNodeDoubleClick ( @NotNull TreeNodeEventRunnable<N> runnable );

    /**
     * Shortcut method for double-click mouse event on specific tree node with condition.
     *
     * @param condition node condition
     * @param runnable  tree node event runnable
     * @return used mouse adapter
     */
    public MouseAdapter onNodeDoubleClick ( @Nullable Predicate<N> condition, @NotNull TreeNodeEventRunnable<N> runnable );
}