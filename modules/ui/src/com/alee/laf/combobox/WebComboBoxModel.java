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

package com.alee.laf.combobox;

import com.alee.utils.CollectionUtils;

import javax.swing.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * Custom {@link JComboBox} model with generic item type.
 *
 * @param <E> model object type
 * @author Mikle Garin
 */

@SuppressWarnings ( "NonSerializableFieldInSerializableClass" )
public class WebComboBoxModel<E> extends AbstractListModel implements MutableComboBoxModel, Serializable
{
    /**
     * Combobox items.
     */
    protected List<E> items;

    /**
     * Currently selected item.
     */
    protected E selectedItem;

    /**
     * Constructs an empty DefaultComboBoxModel object.
     */
    public WebComboBoxModel ()
    {
        this ( new ArrayList<E> ( 0 ) );
    }

    /**
     * Constructs a DefaultComboBoxModel object initialized with
     * an array of objects.
     *
     * @param items an array of Object objects
     */
    public WebComboBoxModel ( final E[] items )
    {
        this ( CollectionUtils.asList ( items ) );
    }

    /**
     * Constructs a DefaultComboBoxModel object initialized with
     * a vector.
     *
     * @param items a Vector object ...
     */
    public WebComboBoxModel ( final Vector<E> items )
    {
        this ( new ArrayList<E> ( items ) );
    }

    /**
     * Constructs a DefaultComboBoxModel object initialized with
     * a vector.
     *
     * @param items a Vector object ...
     */
    public WebComboBoxModel ( final List<E> items )
    {
        super ();
        this.items = items;
        this.selectedItem = getSize () > 0 ? getElementAt ( 0 ) : null;
    }

    @Override
    public int getSize ()
    {
        return items.size ();
    }

    @Override
    public E getElementAt ( final int index )
    {
        if ( index >= 0 && index < items.size () )
        {
            return items.get ( index );
        }
        else
        {
            return null;
        }
    }

    @Override
    public E getSelectedItem ()
    {
        return selectedItem;
    }

    @Override
    public void setSelectedItem ( final Object item )
    {
        if ( selectedItem != null && !selectedItem.equals ( item ) ||
                selectedItem == null && item != null )
        {
            selectedItem = ( E ) item;
            fireContentsChanged ( this, -1, -1 );
        }
    }

    @Override
    public void addElement ( final Object item )
    {
        items.add ( ( E ) item );
        fireIntervalAdded ( this, items.size () - 1, items.size () - 1 );
        if ( items.size () == 1 && selectedItem == null && item != null )
        {
            setSelectedItem ( item );
        }
    }

    @Override
    public void insertElementAt ( final Object item, final int index )
    {
        items.add ( index, ( E ) item );
        fireIntervalAdded ( this, index, index );
    }

    @Override
    public void removeElementAt ( final int index )
    {
        if ( getElementAt ( index ) == selectedItem )
        {
            if ( index == 0 )
            {
                setSelectedItem ( getSize () == 1 ? null : getElementAt ( index + 1 ) );
            }
            else
            {
                setSelectedItem ( getElementAt ( index - 1 ) );
            }
        }
        items.remove ( index );
        fireIntervalRemoved ( this, index, index );
    }

    @Override
    public void removeElement ( final Object item )
    {
        final int index = items.indexOf ( item );
        if ( index != -1 )
        {
            removeElementAt ( index );
        }
    }

    /**
     * Returns the index-position of the specified element in the list.
     *
     * @param item element to return index of in the model
     * @return an int representing the index position, where 0 is the first position
     */
    public int getIndexOf ( final Object item )
    {
        return items.indexOf ( item );
    }

    /**
     * Replaces all combobox elements.
     *
     * @param replacement new {@link List} of elements
     */
    public void replaceAllElements ( final List<E> replacement )
    {
        if ( items.size () > 0 )
        {
            items.clear ();
            if ( replacement.size () > 0 )
            {
                items.addAll ( replacement );
                if ( selectedItem != null && !replacement.contains ( selectedItem ) )
                {
                    setSelectedItem ( replacement.get ( 0 ) );
                }
                fireContentsChanged ( this, 0, items.size () - 1 );
            }
            else
            {
                setSelectedItem ( null );
                fireContentsChanged ( this, 0, 0 );
            }
        }
        else
        {
            items.addAll ( replacement );
            fireIntervalAdded ( this, 0, items.size () - 1 );
        }
    }

    /**
     * Removes all list elements.
     */
    public void removeAllElements ()
    {
        if ( items.size () > 0 )
        {
            final int firstIndex = 0;
            final int lastIndex = items.size () - 1;
            items.clear ();
            selectedItem = null;
            fireIntervalRemoved ( this, firstIndex, lastIndex );
        }
        else
        {
            selectedItem = null;
        }
    }
}