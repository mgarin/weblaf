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

import javax.swing.*;
import javax.swing.plaf.basic.BasicTreeUI;
import javax.swing.tree.AbstractLayoutCache;
import javax.swing.tree.TreePath;
import java.awt.*;

/**
 * Pluggable look and feel interface for {@link com.alee.laf.tree.WebTree} component.
 *
 * @author Mikle Garin
 */
public abstract class WTreeUI extends BasicTreeUI
{
    /**
     * Returns tree selection style.
     *
     * @return tree selection style
     */
    @NotNull
    public abstract TreeSelectionStyle getSelectionStyle ();

    /**
     * Sets tree selection style.
     *
     * @param style tree selection style
     */
    public abstract void setSelectionStyle ( @NotNull TreeSelectionStyle style );

    /**
     * Returns current hover row.
     *
     * @return current hover row
     */
    public abstract int getHoverRow ();

    /**
     * Returns row index for specified location on the tree or {@code -1}.
     * This method takes selection style into account.
     *
     * @param location location on the tree
     * @return row index for specified location on the tree or {@code -1}
     */
    public abstract int getExactRowForLocation ( @NotNull Point location );

    /**
     * Returns row index for specified location on the tree or {@code -1}.
     *
     * @param location location on the tree
     * @param fullRow  whether take the whole row into account or just node renderer bounds
     * @return row index for specified location on the tree or {@code -1}
     */
    public abstract int getExactRowForLocation ( @NotNull Point location, boolean fullRow );

    /**
     * Returns row bounds by its index.
     * This method takes selection style into account.
     *
     * @param row row index
     * @return row bounds by its index
     */
    @NotNull
    public abstract Rectangle getRowBounds ( int row );

    /**
     * Returns row bounds by its index.
     *
     * @param row     row index
     * @param fullRow whether take the whole row or just node renderer bounds
     * @return row bounds by its index
     */
    @NotNull
    public abstract Rectangle getRowBounds ( int row, boolean fullRow );

    /**
     * Returns whether location is in the checkbox tree checkbox control or not.
     * This method is only used when this UI is applied to {@link com.alee.extended.tree.WebCheckBoxTree}.
     * todo This should be separated into custom UI for WebCheckBoxTree
     *
     * @param path tree path
     * @param x    location X coordinate
     * @param y    location Y coordinate
     * @return true if location is in the checkbox tree checkbox control, false otherwise
     */
    public abstract boolean isLocationInCheckBoxControl ( @NotNull TreePath path, int x, int y );

    /**
     * Returns tree cell renderer pane.
     *
     * @return tree cell renderer pane
     */
    @NotNull
    public abstract CellRendererPane getCellRendererPane ();

    /**
     * Returns tree layout cache.
     *
     * @return tree layout cache
     */
    @Nullable
    public abstract AbstractLayoutCache getTreeLayoutCache ();

    /**
     * Forces all nodes to update their sizes.
     * This will reset sizes cache and perform tree visual update.
     */
    public void updateNodeSizes ()
    {
        treeState.invalidateSizes ();
        updateSize ();
    }
}