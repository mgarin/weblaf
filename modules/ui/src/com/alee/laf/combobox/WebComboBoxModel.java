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

import com.alee.laf.list.WebListModel;
import com.alee.utils.CollectionUtils;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Custom {@link JComboBox} model with generic element type.
 * Unlike {@link DefaultComboBoxModel} it will not reuse any of the provided arrays or {@link Collection}s.
 * Model should have its own data enclosed in itself in the first place, if you want to have control over it - override the model itself.
 * Also this model is based on {@link WebListModel} and overrides its methods to properly handle {@link #selected} element.
 *
 * @param <T> element type
 * @author Mikle Garin
 */

public class WebComboBoxModel<T> extends WebListModel<T> implements MutableComboBoxModel
{
    /**
     * Currently selected element.
     */
    @SuppressWarnings ( "NonSerializableFieldInSerializableClass" )
    protected T selected;

    /**
     * Constructs empty {@link WebComboBoxModel}.
     */
    public WebComboBoxModel ()
    {
        this ( new ArrayList<T> ( 0 ) );
    }

    /**
     * Constructs new {@link WebComboBoxModel}.
     *
     * @param elements combobox elements
     */
    public WebComboBoxModel ( final T... elements )
    {
        this ( CollectionUtils.asList ( elements ) );
    }

    /**
     * Constructs new {@link WebComboBoxModel}.
     *
     * @param elements combobox elements
     */
    public WebComboBoxModel ( final Collection<T> elements )
    {
        super ( elements );
        this.selected = getSize () > 0 ? getElementAt ( 0 ) : null;
    }

    @Override
    public T getElementAt ( final int index )
    {
        if ( index >= 0 && index < delegate.size () )
        {
            return delegate.get ( index );
        }
        else
        {
            return null;
        }
    }

    @Override
    public T getSelectedItem ()
    {
        return selected;
    }

    @Override
    public void setSelectedItem ( final Object element )
    {
        if ( selected != null && !selected.equals ( element ) || selected == null && element != null )
        {
            selected = ( T ) element;
            fireContentsChanged ( this, -1, -1 );
        }
    }

    @Override
    public T set ( final int index, final T element )
    {
        final T replaced = super.set ( index, element );
        if ( element != null && selected == replaced )
        {
            setSelectedItem ( element );
        }
        return replaced;
    }

    @Override
    public void setAll ( final Collection<T> elements )
    {
        super.setAll ( elements );
        if ( elements.size () > 0 )
        {
            setSelectedItem ( elements.iterator ().next () );
        }
    }

    @Override
    public void addElement ( final Object element )
    {
        add ( ( T ) element );
    }

    @Override
    public void insertElementAt ( final Object element, final int index )
    {
        add ( index, ( T ) element );
    }

    @Override
    public void add ( final int index, final T element )
    {
        super.add ( index, element );
        if ( size () == 1 && selected == null && element != null )
        {
            setSelectedItem ( element );
        }
    }

    @Override
    public void add ( final Collection<T> elements )
    {
        super.add ( elements );
        if ( size () == 1 && selected == null && CollectionUtils.notEmpty ( elements ) )
        {
            setSelectedItem ( elements.iterator ().next () );
        }
    }

    @Override
    public void removeElementAt ( final int index )
    {
        remove ( index );
    }

    @Override
    public void removeElement ( final Object item )
    {
        remove ( ( T ) item );
    }

    @Override
    public T remove ( final int index )
    {
        if ( getElementAt ( index ) == selected )
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
        return super.remove ( index );
    }

    @Override
    public void removeAll ( final Collection<T> objects )
    {
        if ( objects.contains ( selected ) )
        {
            int index = -1;
            for ( int i = 0; i < size (); i++ )
            {
                if ( !objects.contains ( get ( i ) ) )
                {
                    index = i;
                    break;
                }
            }
            setSelectedItem ( index != -1 ? get ( index ) : null );
        }
        super.removeAll ( objects );
    }
}