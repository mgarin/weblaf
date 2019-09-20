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

import com.alee.api.annotations.NotNull;
import com.alee.api.annotations.Nullable;
import com.alee.managers.tooltip.AbstractComponentArea;
import com.alee.managers.tooltip.ComponentArea;

import javax.swing.table.JTableHeader;
import java.awt.*;

/**
 * {@link ComponentArea} implementation describing {@link JTableHeader} cell area.
 *
 * @param <V> cell value type
 * @param <C> component type
 * @author Mikle Garin
 */
public class TableHeaderCellArea<V, C extends JTableHeader> extends AbstractComponentArea<V, C>
{
    /**
     * Table header cell row index.
     * There is only one row possible in table header in current implementation, but more will be available in future.
     */
    protected final int row;

    /**
     * Table header cell column index.
     */
    protected final int column;

    /**
     * Constructs new {@link TableHeaderCellArea}.
     *
     * @param row    table cell row index
     * @param column table cell column index
     */
    public TableHeaderCellArea ( final int row, final int column )
    {
        this.row = row;
        this.column = column;
    }

    /**
     * Returns table cell row index.
     *
     * @return table cell row index
     */
    public int row ()
    {
        return row;
    }

    /**
     * Returns table cell column index.
     *
     * @return table cell column index
     */
    public int column ()
    {
        return column;
    }

    @Override
    public boolean isAvailable ( @NotNull final C component )
    {
        return row == 0 &&
                0 <= column && column < component.getColumnModel ().getColumnCount ();
    }

    @Nullable
    @Override
    public Rectangle getBounds ( @NotNull final C component )
    {
        return component.getHeaderRect ( column );
    }

    @Nullable
    @Override
    public V getValue ( @NotNull final C component )
    {
        return ( V ) component.getTable ().getModel ().getColumnName ( column );
    }

    @Override
    public boolean equals ( final Object other )
    {
        return other instanceof TableHeaderCellArea &&
                this.row == ( ( TableHeaderCellArea ) other ).row &&
                this.column == ( ( TableHeaderCellArea ) other ).column;
    }
}