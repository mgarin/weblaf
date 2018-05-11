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

import com.alee.api.jdk.Objects;
import com.alee.api.ui.RenderingParameters;

import javax.swing.*;

/**
 * {@link JTable} single cell rendering parameters.
 *
 * @param <V> cell value type
 * @param <C> {@link JTable} type
 * @author Mikle Garin
 */
public class TableCellParameters<V, C extends JTable> implements RenderingParameters
{
    /**
     * {@link JTable}.
     */
    protected final C table;

    /**
     * Cell value.
     */
    protected final V value;

    /**
     * Cell row number.
     */
    protected final int row;

    /**
     * Cell column number.
     */
    protected final int column;

    /**
     * Whether or not cell is selected.
     */
    protected final boolean selected;

    /**
     * Whether or not cell has focus.
     */
    protected final boolean focused;

    /**
     * Constructs new {@link TableCellParameters}.
     * Parameters are calculated within this constuctor when used.
     *
     * @param table {@link JTable}
     * @param area  {@link TableCellArea}
     */
    public TableCellParameters ( final C table, final TableCellArea<V, C> area )
    {
        this ( table, area.row (), area.column () );
    }

    /**
     * Constructs new {@link TableCellParameters}.
     * Parameters are calculated within this constuctor when used.
     *
     * @param table  {@link JTable}
     * @param row    cell row number
     * @param column cell column number
     */
    public TableCellParameters ( final C table, final int row, final int column )
    {
        this.table = Objects.requireNonNull ( table, "Table must not be null" );
        this.value = ( V ) table.getModel ().getValueAt ( row, column );
        this.row = row;
        this.column = column;
        this.selected = table.isCellSelected ( row, column );
        this.focused = table.hasFocus () && table.getSelectionModel ().getLeadSelectionIndex () == row &&
                table.getColumnModel ().getSelectionModel ().getLeadSelectionIndex () == column;
    }

    /**
     * Constructs new {@link TableCellParameters}.
     * Parameters are predefined whenever this constuctor is used.
     *
     * @param table    {@link JTable}
     * @param value    cell value
     * @param row      cell row number
     * @param column   cell column number
     * @param selected whether or not cell is selected
     * @param focused  whether or not cell has focus
     */
    public TableCellParameters ( final C table, final V value, final int row, final int column,
                                 final boolean selected, final boolean focused )
    {
        this.table = Objects.requireNonNull ( table, "Table must not be null" );
        this.value = value;
        this.row = row;
        this.column = column;
        this.selected = selected;
        this.focused = focused;
    }

    /**
     * Returns {@link JTable}.
     *
     * @return {@link JTable}
     */
    public C table ()
    {
        return table;
    }

    /**
     * Returns cell value.
     *
     * @return cell value
     */
    public V value ()
    {
        return value;
    }

    /**
     * Returns cell row number.
     *
     * @return cell row number
     */
    public int row ()
    {
        return row;
    }

    /**
     * Returns cell column number.
     *
     * @return cell column number
     */
    public int column ()
    {
        return column;
    }

    /**
     * Returns whether or not cell is selected.
     *
     * @return {@code true} if cell is selected, {@code false} otherwise
     */
    public boolean isSelected ()
    {
        return selected;
    }

    /**
     * Returns whether or not cell has focus.
     *
     * @return {@code true} if cell has focus, {@code false} otherwise
     */
    public boolean isFocused ()
    {
        return focused;
    }
}