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

package com.alee.laf.table;

import com.alee.painter.SectionPainter;

import javax.swing.*;

/**
 * Base interface for {@link JTable} component cell painters.
 *
 * @param <C> component type
 * @param <U> component UI type
 * @author Mikle Garin
 */

public interface ITableCellPainter<C extends JTable, U extends WebTableUI> extends SectionPainter<C, U>
{
    /**
     * Prepares painter to pain table cell.
     *
     * @param row    painted row index
     * @param column painted column index
     */
    public void prepareToPaint ( int row, int column );
}