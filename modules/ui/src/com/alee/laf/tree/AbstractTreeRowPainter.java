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

import com.alee.painter.AbstractPainter;

import javax.swing.*;

/**
 * Abstract tree row painter.
 *
 * @author Mikle Garin
 */

public abstract class AbstractTreeRowPainter<E extends JTree, U extends WebTreeUI> extends AbstractPainter<E, U>
        implements TreeRowPainter<E, U>
{
    /**
     * Painted row index.
     * Updated through {@link #prepareToPaint(int)} method call right before paint is called.
     */
    protected int row;

    @Override
    public void prepareToPaint ( final int row )
    {
        this.row = row;
    }
}