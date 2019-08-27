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
import com.alee.painter.decoration.*;

import javax.swing.*;
import java.util.List;

/**
 * Simple table cell painter based on {@link AbstractSectionDecorationPainter}.
 *
 * @param <C> component type
 * @param <U> component UI type
 * @param <D> decoration type
 * @author Mikle Garin
 */
public class TableCellPainter<C extends JTable, U extends WebTableUI, D extends IDecoration<C, D>>
        extends AbstractSectionDecorationPainter<C, U, D> implements ITableCellPainter<C, U>
{
    /**
     * Painted row index.
     */
    protected transient Integer row;

    /**
     * Painted column index.
     */
    protected transient Integer column;

    @Override
    public String getSectionId ()
    {
        return "cell";
    }

    @Override
    protected boolean isFocused ()
    {
        return false;
    }

    @NotNull
    @Override
    public List<String> getDecorationStates ()
    {
        final List<String> states = super.getDecorationStates ();

        // Checking setings initialization
        if ( row != null && column != null && component.getModel () != null )
        {
            // Selected state
            states.add ( component.getColumnSelectionAllowed () && component.getRowSelectionAllowed () &&
                    component.isCellSelected ( row, column ) ? DecorationState.selected : DecorationState.unselected );

            // Adding possible cell value states
            final Object value = component.getValueAt ( row, column );
            states.addAll ( DecorationUtils.getExtraStates ( value ) );
        }

        return states;
    }

    @Override
    public void prepareToPaint ( final int row, final int column )
    {
        this.row = row;
        this.column = column;
        updateDecorationState ();
    }
}