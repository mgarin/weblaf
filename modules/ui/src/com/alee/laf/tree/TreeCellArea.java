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

import com.alee.managers.tooltip.ComponentArea;

import javax.swing.*;
import javax.swing.tree.MutableTreeNode;
import java.awt.*;

/**
 * {@link ComponentArea} implementation describing {@link JTree} cell area.
 *
 * @param <V> node type
 * @param <C> component type
 * @author Mikle Garin
 */

public class TreeCellArea<V extends MutableTreeNode, C extends JTree> implements ComponentArea<V, C>
{
    /**
     * Tree row index.
     */
    protected final int row;

    /**
     * Constructs new {@link TreeCellArea}.
     *
     * @param row tree row index
     */
    public TreeCellArea ( final int row )
    {
        super ();
        this.row = row;
    }

    /**
     * Returns tree row index.
     *
     * @return tree row index
     */
    public int row ()
    {
        return row;
    }

    @Override
    public Rectangle getBounds ( final C component )
    {
        return component.getRowBounds ( row );
    }

    @Override
    public V getValue ( final C component )
    {
        return ( V ) component.getPathForRow ( row ).getLastPathComponent ();
    }

    @Override
    public boolean equals ( final Object other )
    {
        return other != null && other instanceof TreeCellArea && this.row == ( ( TreeCellArea ) other ).row;
    }
}