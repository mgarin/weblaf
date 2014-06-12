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
 * @author Mikle Garin
 */

public class CheckBoxListModel extends WebListModel<CheckBoxCellData>
{
    /**
     * Constructs checkbox list model.
     */
    public CheckBoxListModel ()
    {
        super ();
    }


    /**
     * Adds new checkbox cell data with a specified user object into list model.
     *
     * @param userObject user object
     */
    public void addCheckBoxElement ( final Object userObject )
    {
        addElement ( new CheckBoxCellData ( userObject ) );
    }

    /**
     * Adds new checkbox cell data with a specified user object and checkbox selection state into list model.
     *
     * @param userObject user object
     * @param selected   whether checkbox selected or not
     */
    public void addCheckBoxElement ( final Object userObject, final boolean selected )
    {
        addElement ( new CheckBoxCellData ( userObject, selected ) );
    }

    /**
     * Adds new checkbox cell data with a specified user object into list model at the specified index.
     *
     * @param index      new cell index
     * @param userObject user object
     */
    public void addCheckBoxElementAt ( final int index, final Object userObject )
    {
        add ( index, new CheckBoxCellData ( userObject ) );
    }

    /**
     * Adds new checkbox cell data with a specified user object and checkbox selection state into list model at the specified index.
     *
     * @param index      new cell index
     * @param userObject user object
     * @param selected   whether checkbox selected or not
     */
    public void addCheckBoxElementAt ( final int index, final Object userObject, final boolean selected )
    {
        add ( index, new CheckBoxCellData ( userObject, selected ) );
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
    public List<Object> getCheckedValues ()
    {
        final List<Object> values = new ArrayList<Object> ();
        for ( int i = 0; i < getSize (); i++ )
        {
            final CheckBoxCellData cellData = get ( i );
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
        final CheckBoxCellData cellData = get ( index );
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
        final CheckBoxCellData cellData = get ( index );
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
