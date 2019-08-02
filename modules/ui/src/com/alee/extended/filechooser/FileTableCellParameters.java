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

package com.alee.extended.filechooser;

import com.alee.laf.table.TableCellParameters;

import java.io.File;

/**
 * {@link WebFileTable} single cell rendering parameters.
 *
 * @param <V> {@link File} type
 * @param <C> {@link WebFileTable} type
 * @author Mikle Garin
 */
public class FileTableCellParameters<V extends File, C extends WebFileTable> extends TableCellParameters<V, C>
{
    /**
     * Returns {@link WebFileTable} column identifier.
     */
    protected final String columnId;

    /**
     * Constructs new {@link FileTableCellParameters}.
     * Parameters are calculated within this constuctor when used.
     *
     * @param table  {@link WebFileTable}
     * @param row    cell row number
     * @param column cell column number
     */
    public FileTableCellParameters ( final C table, final int row, final int column )
    {
        super ( table, row, column );
        this.columnId = ( String ) table.getColumnModel ().getColumn ( column ).getIdentifier ();
    }

    /**
     * Constructs new {@link FileTableCellParameters}.
     * Parameters are predefined whenever this constuctor is used.
     *
     * @param table    {@link WebFileTable}
     * @param value    cell value
     * @param row      cell row number
     * @param column   cell column number
     * @param selected whether or not cell is selected
     * @param focused  whether or not cell has focus
     */
    public FileTableCellParameters ( final C table, final V value, final int row, final int column,
                                     final boolean selected, final boolean focused )
    {
        super ( table, value, row, column, selected, focused );
        this.columnId = ( String ) table.getColumnModel ().getColumn ( column ).getIdentifier ();
    }

    /**
     * Returns {@link WebFileTable} column identifier.
     *
     * @return {@link WebFileTable} column identifier
     */
    public String columnId ()
    {
        return columnId;
    }
}