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

import com.alee.managers.tooltip.AbstractComponentArea;
import com.alee.managers.tooltip.ComponentArea;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

/**
 * {@link ComponentArea} implementation describing {@link JTable} cell area.
 *
 * @param <V> cell value type
 * @param <C> component type
 * @author Mikle Garin
 */
public class TableCellArea<V, C extends JTable> extends AbstractComponentArea<V, C>
{
    /**
     * Table cell row index.
     */
    protected final int row;

    /**
     * Table cell column index.
     */
    protected final int column;

    /**
     * Constructs new {@link TableCellArea}.
     *
     * @param row    table cell row index
     * @param column table cell column index
     */
    public TableCellArea ( final int row, final int column )
    {
        super ();
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
    public Rectangle getBounds ( final C component )
    {
        // Calculating cell bounds
        final Rectangle bounds = component.getCellRect ( row, column, false );

        // Adjusting tooltip location
        final TableCellRenderer cellRenderer = component.getCellRenderer ( row, column );
        final Component renderer = component.prepareRenderer ( cellRenderer, row, column );
        adjustBounds ( component, renderer, bounds );

        return bounds;
    }

    @Override
    public V getValue ( final C component )
    {
        return ( V ) component.getValueAt ( row, column );
    }

    @Override
    public boolean equals ( final Object other )
    {
        return other instanceof TableCellArea &&
                this.row == ( ( TableCellArea ) other ).row &&
                this.column == ( ( TableCellArea ) other ).column;
    }
}