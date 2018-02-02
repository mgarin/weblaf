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

import com.alee.painter.decoration.AbstractSectionDecorationPainter;
import com.alee.painter.decoration.DecorationState;
import com.alee.painter.decoration.IDecoration;

import javax.swing.*;
import java.util.List;

/**
 * Simple table row painter based on {@link AbstractSectionDecorationPainter}.
 *
 * @param <C> component type
 * @param <U> component UI type
 * @param <D> decoration type
 * @author Mikle Garin
 */

public class TableRowPainter<C extends JTable, U extends WebTableUI, D extends IDecoration<C, D>>
        extends AbstractSectionDecorationPainter<C, U, D> implements ITableRowPainter<C, U>
{
    /**
     * Painted row index.
     */
    protected transient Integer row;

    @Override
    public String getSectionId ()
    {
        return "row";
    }

    @Override
    protected boolean isFocused ()
    {
        return false;
    }

    @Override
    public List<String> getDecorationStates ()
    {
        final List<String> states = super.getDecorationStates ();

        // Checking setings initialization
        if ( row != null && component.getModel () != null )
        {
            // Column type
            states.add ( row % 2 == 0 ? DecorationState.odd : DecorationState.even );

            // Selected state
            states.add ( !component.getColumnSelectionAllowed () && component.getRowSelectionAllowed () &&
                    component.isRowSelected ( row ) ? DecorationState.selected : DecorationState.unselected );
        }

        return states;
    }

    @Override
    public void prepareToPaint ( final int row )
    {
        this.row = row;
        updateDecorationState ();
    }
}