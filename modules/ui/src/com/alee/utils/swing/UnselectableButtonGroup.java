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

package com.alee.utils.swing;

import com.alee.utils.CollectionUtils;
import com.alee.utils.ReflectUtils;

import javax.swing.*;
import javax.swing.event.EventListenerList;
import java.awt.*;
import java.util.List;

/**
 * Custom button group that allows empty selection state.
 * It can also contain custom {@link com.alee.utils.swing.ButtonGroupListener} to receive
 *
 * @author Mikle Garin
 */
public class UnselectableButtonGroup extends ButtonGroup
{
    /**
     * Group selection change listeners.
     */
    protected final EventListenerList listeners = new EventListenerList ();

    /**
     * Whether or not this button group should allow empty selection state.
     */
    protected boolean unselectable;

    /**
     * Constructs new button group.
     */
    public UnselectableButtonGroup ()
    {
        super ();
    }

    /**
     * Constructs new {@link UnselectableButtonGroup} and adds specified {@link AbstractButton}s into it.
     *
     * @param buttons {@link AbstractButton}s to group
     */
    public UnselectableButtonGroup ( final AbstractButton... buttons )
    {
        this ( true );
        add ( buttons );
    }

    /**
     * Constructs new {@link UnselectableButtonGroup} and adds all {@link AbstractButton}s from the specified {@link Container} into it.
     *
     * @param buttons {@link List} of {@link AbstractButton}s to group
     */
    public UnselectableButtonGroup ( final List<AbstractButton> buttons )
    {
        this ( true );
        add ( buttons );
    }

    /**
     * Constructs new {@link UnselectableButtonGroup} and adds specified buttons.
     *
     * @param container {@link Container} to find {@link AbstractButton}s to group in
     */
    public UnselectableButtonGroup ( final Container container )
    {
        this ( true );
        add ( container );
    }

    /**
     * Constructs new button group.
     *
     * @param unselectable whether or not this button group should allow empty selection state
     */
    public UnselectableButtonGroup ( final boolean unselectable )
    {
        super ();
        setUnselectable ( unselectable );
    }

    /**
     * Returns all buttons added into this group.
     *
     * @return all buttons added into this group
     */
    public List<AbstractButton> getButtons ()
    {
        return CollectionUtils.copy ( buttons );
    }

    /**
     * Adds all specified {@link AbstractButton}s into this {@link UnselectableButtonGroup}.
     *
     * @param buttons {@link AbstractButton}s to group
     */
    public void add ( final AbstractButton... buttons )
    {
        for ( final AbstractButton button : buttons )
        {
            add ( button );
        }
    }

    /**
     * Adds all specified {@link AbstractButton}s into this {@link UnselectableButtonGroup}.
     *
     * @param buttons {@link List} of {@link AbstractButton}s to group
     */
    public void add ( final List<AbstractButton> buttons )
    {
        for ( final AbstractButton button : buttons )
        {
            add ( button );
        }
    }

    /**
     * Adds all {@link AbstractButton}s from the specified {@link Container} into this {@link UnselectableButtonGroup}.
     *
     * @param container {@link Container} to find {@link AbstractButton}s to group in
     */
    public void add ( final Container container )
    {
        for ( int i = 0; i < container.getComponentCount (); i++ )
        {
            final Component component = container.getComponent ( i );
            if ( component instanceof AbstractButton )
            {
                add ( ( AbstractButton ) component );
            }
        }
    }

    /**
     * Removes all buttons from the group.
     * Button selection is kept intact upon removal.
     */
    public void removeAll ()
    {
        for ( int i = buttons.size () - 1; i >= 0; i-- )
        {
            final AbstractButton b = buttons.get ( i );
            buttons.remove ( i );
            b.getModel ().setGroup ( null );
        }
        ReflectUtils.setFieldValueSafely ( this, "selection", null );
    }

    /**
     * Returns whether or not this button group allows empty selection state.
     *
     * @return true if this button group allows empty selection state, false otherwise
     */
    public boolean isUnselectable ()
    {
        return unselectable;
    }

    /**
     * Sets whether or not this button group should allow empty selection state.
     *
     * @param unselectable whether or not this button group should allow empty selection state
     */
    public void setUnselectable ( final boolean unselectable )
    {
        this.unselectable = unselectable;
    }

    @Override
    public void setSelected ( final ButtonModel model, final boolean selected )
    {
        if ( selected || !unselectable )
        {
            super.setSelected ( model, selected );
        }
        else
        {
            clearSelection ();
        }
        fireSelectionChanged ();
    }

    /**
     * Adds {@link com.alee.utils.swing.ButtonGroupListener}.
     *
     * @param listener {@link com.alee.utils.swing.ButtonGroupListener} to add
     */
    public void addButtonGroupListener ( final ButtonGroupListener listener )
    {
        listeners.add ( ButtonGroupListener.class, listener );
    }

    /**
     * Removes {@link com.alee.utils.swing.ButtonGroupListener}.
     *
     * @param listener {@link com.alee.utils.swing.ButtonGroupListener} to remove
     */
    public void removeButtonGroupListener ( final ButtonGroupListener listener )
    {
        listeners.remove ( ButtonGroupListener.class, listener );
    }

    /**
     * Informs all {@link com.alee.utils.swing.ButtonGroupListener}s about group selection change.
     */
    public void fireSelectionChanged ()
    {
        for ( final ButtonGroupListener listener : listeners.getListeners ( ButtonGroupListener.class ) )
        {
            listener.selectionChanged ();
        }
    }

    /**
     * Returns {@link UnselectableButtonGroup} used to group specified {@link AbstractButton}s.
     * Convenience method in case you don't want to go with {@link #UnselectableButtonGroup(AbstractButton...)} constructor.
     *
     * @param buttons {@link AbstractButton}s to group
     * @return {@link UnselectableButtonGroup} used to group specified {@link AbstractButton}s
     */
    public static UnselectableButtonGroup group ( final AbstractButton... buttons )
    {
        return new UnselectableButtonGroup ( buttons );
    }

    /**
     * Returns {@link UnselectableButtonGroup} used to group specified {@link AbstractButton}s.
     * Convenience method in case you don't want to go with {@link #UnselectableButtonGroup(List)} constructor.
     *
     * @param buttons {@link List} of {@link AbstractButton}s to group
     * @return {@link UnselectableButtonGroup} used to group specified {@link AbstractButton}s
     */
    public static UnselectableButtonGroup group ( final List<AbstractButton> buttons )
    {
        return new UnselectableButtonGroup ( buttons );
    }

    /**
     * Returns {@link UnselectableButtonGroup} used to group {@link AbstractButton}s from the specified {@link Container}.
     * Convenience method in case you don't want to go with {@link #UnselectableButtonGroup(Container)} constructor.
     *
     * @param container {@link Container} to find {@link AbstractButton}s to group in
     * @return {@link UnselectableButtonGroup} used to group {@link AbstractButton}s from the specified {@link Container}
     */
    public static UnselectableButtonGroup group ( final Container container )
    {
        return new UnselectableButtonGroup ( container );
    }
}