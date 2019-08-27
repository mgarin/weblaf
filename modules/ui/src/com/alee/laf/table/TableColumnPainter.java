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
import com.alee.painter.decoration.AbstractSectionDecorationPainter;
import com.alee.painter.decoration.DecorationState;
import com.alee.painter.decoration.IDecoration;

import javax.swing.*;
import java.util.List;

/**
 * Simple table column painter based on {@link AbstractSectionDecorationPainter}.
 *
 * @param <C> component type
 * @param <U> component UI type
 * @param <D> decoration type
 * @author Mikle Garin
 */
public class TableColumnPainter<C extends JTable, U extends WebTableUI, D extends IDecoration<C, D>>
        extends AbstractSectionDecorationPainter<C, U, D> implements ITableColumnPainter<C, U>
{
    /**
     * Painted column index.
     */
    protected transient Integer column;

    @Override
    public String getSectionId ()
    {
        return "column";
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
        if ( column != null && component.getModel () != null )
        {
            // Column type
            states.add ( column % 2 == 0 ? DecorationState.odd : DecorationState.even );

            // Selected state
            states.add ( !component.getRowSelectionAllowed () && component.getColumnSelectionAllowed () &&
                    component.isColumnSelected ( column ) ? DecorationState.selected : DecorationState.unselected );
        }

        return states;
    }

    @Override
    public void prepareToPaint ( final int column )
    {
        this.column = column;
        updateDecorationState ();
    }
}