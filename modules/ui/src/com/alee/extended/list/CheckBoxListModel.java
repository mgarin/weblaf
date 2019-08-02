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

package com.alee.extended.list;

import com.alee.laf.list.WebListModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Custom list model for checkbox list component.
 * This class overrides a few default model methods to avoid casting to checkbox cell data object.
 * There are also methods to add new elements and change/retrieve checkbox selection state.
 *
 * @param <T> data type
 * @author Mikle Garin
 */
public class CheckBoxListModel<T> extends WebListModel<CheckBoxCellData<T>>
{
    /**
     * Constructs new {@link CheckBoxListModel}.
     */
    public CheckBoxListModel ()
    {
        super ();
    }

    /**
     * Returns whether checkbox under the specified cell index is selected or not.
     *
     * @param index cell index
     * @return true if checkbox at the specified cell index is selected, false otherwise
     */
    public boolean isCheckBoxSelected ( final int index )
    {
        return get ( index ).isSelected ();
    }

    /**
     * Returns list of values from checked cells.
     *
     * @return list of values from checked cells
     */
    public List<T> getCheckedValues ()
    {
        final List<T> values = new ArrayList<T> ();
        for ( int i = 0; i < getSize (); i++ )
        {
            final CheckBoxCellData<T> cellData = get ( i );
            if ( cellData.isSelected () )
            {
                values.add ( cellData.getUserObject () );
            }
        }
        return values;
    }

    /**
     * Inverts checkbox selection at the specified cell index.
     *
     * @param index cell index
     */
    public void invertCheckBoxSelection ( final int index )
    {
        final CheckBoxCellData<T> cellData = get ( index );
        cellData.setSelected ( !cellData.isSelected () );
        fireContentsChanged ( this, index, index );
    }

    /**
     * Sets whether checkbox at the specified cell index is selected or not and returns whether selection has changed or not.
     *
     * @param index    cell index
     * @param selected whether checkbox is selected or not
     * @return true if selection has changed, false otherwise
     */
    public boolean setCheckBoxSelected ( final int index, final boolean selected )
    {
        final CheckBoxCellData<T> cellData = get ( index );
        if ( cellData.isSelected () != selected )
        {
            cellData.setSelected ( selected );
            fireContentsChanged ( this, index, index );
            return true;
        }
        else
        {
            return false;
        }
    }
}