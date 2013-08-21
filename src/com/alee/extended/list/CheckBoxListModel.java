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

import javax.swing.*;

/**
 * Custom list model for checkbox list component.
 * This class overrides a few default model methods to avoid casting to checkbox cell data object.
 * There are also methods to add new elements and change/retrieve checkbox selection state.
 *
 * @author Mikle Garin
 */

public class CheckBoxListModel extends DefaultListModel
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
    public void addCheckBoxElement ( Object userObject )
    {
        addElement ( new CheckBoxCellData ( userObject ) );
    }

    /**
     * Adds new checkbox cell data with a specified user object and checkbox selection state into list model.
     *
     * @param userObject user object
     * @param selected   whether checkbox selected or not
     */
    public void addCheckBoxElement ( Object userObject, boolean selected )
    {
        addElement ( new CheckBoxCellData ( userObject, selected ) );
    }

    /**
     * Adds new checkbox cell data with a specified user object into list model at the specified index.
     *
     * @param index      new cell index
     * @param userObject user object
     */
    public void addCheckBoxElementAt ( int index, Object userObject )
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
    public void addCheckBoxElementAt ( int index, Object userObject, boolean selected )
    {
        add ( index, new CheckBoxCellData ( userObject, selected ) );
    }

    /**
     * Returns whether checkbox under the specified cell index is selected or not.
     *
     * @param index cell index
     * @return true if checkbox at the specified cell index is selected, false otherwise
     */
    public boolean isCheckBoxSelected ( int index )
    {
        return get ( index ).isSelected ();
    }

    /**
     * Inverts checkbox selection at the specified cell index.
     *
     * @param index cell index
     */
    public void invertCheckBoxSelection ( int index )
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
    public boolean setCheckBoxSelected ( int index, boolean selected )
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

    /**
     * Replaces the element at the specified position in this list with the specified element.
     * <p/>
     * Throws an <code>ArrayIndexOutOfBoundsException</code> if the index is out of range (<code>index &lt; 0 || index &gt;=
     * size()</code>).
     *
     * @param index   index of element to replace
     * @param element element to be stored at the specified position
     * @return the element previously at the specified position
     */
    @Override
    public CheckBoxCellData set ( int index, Object element )
    {
        return ( CheckBoxCellData ) super.set ( index, element );
    }

    /**
     * Returns the element at the specified position in this list.
     * <p/>
     * Throws an <code>ArrayIndexOutOfBoundsException</code> if the index is out of range (<code>index &lt; 0 || index &gt;=
     * size()</code>).
     *
     * @param index index of element to return
     */
    @Override
    public CheckBoxCellData get ( int index )
    {
        return ( CheckBoxCellData ) super.get ( index );
    }

    /**
     * Returns the component at the specified index.
     * Throws an <code>ArrayIndexOutOfBoundsException</code> if the index is negative or not less than the size of the list.
     * <blockquote>
     * <b>Note:</b> Although this method is not deprecated, the preferred method to use is <code>get(int)</code>, which implements the
     * <code>List</code> interface defined in the 1.2 Collections framework.
     * </blockquote>
     *
     * @param index an index into this list
     * @return the component at the specified index
     * @see #get(int)
     * @see java.util.Vector#elementAt(int)
     */
    @Override
    public CheckBoxCellData elementAt ( int index )
    {
        return ( CheckBoxCellData ) super.elementAt ( index );
    }

    /**
     * Returns the first component of this list.
     * Throws a <code>NoSuchElementException</code> if this vector has no components.
     *
     * @return the first component of this list
     * @see java.util.Vector#firstElement()
     */
    @Override
    public CheckBoxCellData firstElement ()
    {
        return ( CheckBoxCellData ) super.firstElement ();
    }

    /**
     * Returns the last component of the list.
     * Throws a <code>NoSuchElementException</code> if this vector has no components.
     *
     * @return the last component of the list
     * @see java.util.Vector#lastElement()
     */
    @Override
    public CheckBoxCellData lastElement ()
    {
        return ( CheckBoxCellData ) super.lastElement ();
    }

    /**
     * Returns the component at the specified index.
     * <blockquote>
     * <b>Note:</b> Although this method is not deprecated, the preferred method to use is <code>get(int)</code>, which implements the
     * <code>List</code> interface defined in the 1.2 Collections framework.
     * </blockquote>
     *
     * @param index an index into this list
     * @return the component at the specified index
     * @throws ArrayIndexOutOfBoundsException if the <code>index</code> is negative or greater than the current size of this list
     * @see #get(int)
     */
    @Override
    public CheckBoxCellData getElementAt ( int index )
    {
        return ( CheckBoxCellData ) super.getElementAt ( index );
    }

    /**
     * Removes the element at the specified position in this list.
     * Returns the element that was removed from the list.
     * <p/>
     * Throws an <code>ArrayIndexOutOfBoundsException</code> if the index is out of range (<code>index &lt; 0 || index &gt;=
     * size()</code>).
     *
     * @param index the index of the element to removed
     */
    @Override
    public CheckBoxCellData remove ( int index )
    {
        return ( CheckBoxCellData ) super.remove ( index );
    }
}