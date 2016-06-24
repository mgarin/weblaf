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

package com.alee.laf.list;

import com.alee.painter.decoration.AbstractSectionDecorationPainter;
import com.alee.painter.decoration.DecorationState;
import com.alee.painter.decoration.IDecoration;
import com.alee.painter.decoration.Stateful;

import javax.swing.*;
import java.util.List;

/**
 * Simple list item painter based on {@link com.alee.painter.decoration.AbstractSectionDecorationPainter}.
 * It is used within {@link com.alee.laf.list.ListPainter} to paint items background.
 *
 * @param <E> component type
 * @param <U> component UI type
 * @param <D> decoration type
 * @author Mikle Garin
 */

public class ListItemPainter<E extends JList, U extends WebListUI, D extends IDecoration<E, D>>
        extends AbstractSectionDecorationPainter<E, U, D> implements IListItemPainter<E, U>
{
    /**
     * Painted item index.
     */
    protected transient int index;

    @Override
    protected List<String> getDecorationStates ()
    {
        final List<String> states = super.getDecorationStates ();

        // Adding index type
        states.add ( index % 2 == 0 ? DecorationState.odd : DecorationState.even );

        // Adding common item states
        if ( component.isSelectedIndex ( index ) )
        {
            states.add ( DecorationState.selected );
        }

        // Adding possible item states
        final ListModel lm = component.getModel ();
        if ( lm != null && index < lm.getSize () )
        {
            final Object value = component.getModel ().getElementAt ( index );
            if ( value != null && value instanceof Stateful )
            {
                states.addAll ( ( ( Stateful ) value ).getStates () );
            }
        }

        return states;
    }

    @Override
    protected boolean isFocused ()
    {
        return false;
    }

    @Override
    public void prepareToPaint ( final int index )
    {
        this.index = index;
        updateDecorationState ();
    }
}