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

package com.alee.laf.tree.behavior;

import com.alee.api.annotations.NotNull;
import com.alee.api.annotations.Nullable;
import com.alee.extended.behavior.AbstractObjectHoverBehavior;
import com.alee.laf.tree.WebTreeUI;

import javax.swing.*;
import javax.swing.plaf.TreeUI;
import javax.swing.tree.TreePath;
import java.awt.*;

/**
 * Abstract behavior that provides hover events for {@link JTree} paths.
 * For a simple installation and uninstallation you can call {@link #install()} and {@link #uninstall()} methods.
 *
 * @param <C> component type
 * @author Mikle Garin
 */
public abstract class TreePathHoverBehavior<C extends JTree> extends AbstractObjectHoverBehavior<C, TreePath>
{
    /**
     * Constructs behavior for the specified {@link JTree}.
     *
     * @param tree tree into which this behavior is installed
     */
    public TreePathHoverBehavior ( @NotNull final C tree )
    {
        this ( tree, true );
    }

    /**
     * Constructs behavior for the specified {@link JTree}.
     *
     * @param tree        {@link JTree} into which this behavior is installed
     * @param enabledOnly whether or not behavior should only track hover events when tree is enabled
     */
    public TreePathHoverBehavior ( @NotNull final C tree, final boolean enabledOnly )
    {
        super ( tree, enabledOnly );
    }

    @Nullable
    @Override
    protected TreePath getObjectAt ( @NotNull final Point location )
    {
        final TreeUI treeUI = component.getUI ();
        final int index;
        if ( treeUI instanceof WebTreeUI )
        {
            // Compute index under point through WebTreeUI methods (to make sure that row selection type was taken into account)
            index = ( ( WebTreeUI ) treeUI ).getExactRowForLocation ( location );
        }
        else
        {
            // Compute index under point through default tree methods
            index = component.getRowForLocation ( location.x, location.y );
        }
        return component.getPathForRow ( index );
    }

    @Nullable
    @Override
    protected TreePath getEmptyObject ()
    {
        return null;
    }

    /**
     * Informs when hover object changes.
     *
     * @param previous previous hover {@link TreePath}
     * @param current  current hover {@link TreePath}
     */
    @Override
    public abstract void hoverChanged ( @Nullable TreePath previous, @Nullable TreePath current );
}