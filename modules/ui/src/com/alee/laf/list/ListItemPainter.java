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

import com.alee.api.annotations.NotNull;
import com.alee.painter.decoration.AbstractSectionDecorationPainter;
import com.alee.painter.decoration.DecorationState;
import com.alee.painter.decoration.DecorationUtils;
import com.alee.painter.decoration.IDecoration;

import javax.swing.*;
import java.util.List;

/**
 * Simple list item painter based on {@link com.alee.painter.decoration.AbstractSectionDecorationPainter}.
 * It is used within {@link com.alee.laf.list.ListPainter} to paint items background.
 *
 * @param <C> component type
 * @param <U> component UI type
 * @param <D> decoration type
 * @author Mikle Garin
 */
public class ListItemPainter<C extends JList, U extends WebListUI, D extends IDecoration<C, D>>
        extends AbstractSectionDecorationPainter<C, U, D> implements IListItemPainter<C, U>
{
    /**
     * Painted item index.
     */
    protected transient Integer index;

    @Override
    public String getSectionId ()
    {
        return "item";
    }

    @NotNull
    @Override
    public List<String> getDecorationStates ()
    {
        final List<String> states = super.getDecorationStates ();
        addItemStates ( states );
        return states;
    }

    /**
     * Adds states provided by this painter.
     *
     * @param states list to add states to
     */
    protected void addItemStates ( final List<String> states )
    {
        // Ensure index is specified
        if ( index != null )
        {
            // Ensure item exists
            final ListModel model = component.getModel ();
            if ( model != null && 0 <= index && index < model.getSize () )
            {
                final Object item = model.getElementAt ( index );
                addItemStates ( states, item );
            }
        }
    }

    /**
     * Adds states provided by specified item.
     *
     * @param states list to add states to
     * @param item   item to provide states for
     */
    protected void addItemStates ( final List<String> states, final Object item )
    {
        // Adding row type
        addNumerationStates ( states, item );

        // Adding common item states
        states.add ( component.isSelectedIndex ( index ) ? DecorationState.selected : DecorationState.unselected );

        // Hover state
        if ( index == ui.getHoverIndex () )
        {
            states.add ( DecorationState.hover );
        }
        else
        {
            // We have to remove it as the origin might have hover state
            states.remove ( DecorationState.hover );
        }

        // Adding possible item states
        states.addAll ( DecorationUtils.getExtraStates ( item ) );
    }

    /**
     * Adds numeration states for the specified item.
     *
     * @param states list to add states to
     * @param item   item to provide states for
     */
    protected void addNumerationStates ( final List<String> states, final Object item )
    {
        states.add ( index % 2 == 0 ? DecorationState.odd : DecorationState.even );
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