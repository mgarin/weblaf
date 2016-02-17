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

import javax.swing.*;
import java.util.ArrayList;
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
    protected final List<ButtonGroupListener> listeners = new ArrayList<ButtonGroupListener> ( 1 );

    /**
     * Whether or not this button group should allow empty selection state.
     */
    protected boolean unselectable = true;

    /**
     * Constructs new button group.
     */
    public UnselectableButtonGroup ()
    {
        super ();
    }

    /**
     * Constructs new button group and adds specified buttons.
     *
     * @param buttons buttons to add into this group
     */
    public UnselectableButtonGroup ( final AbstractButton... buttons )
    {
        super ();
        add ( buttons );
    }

    /**
     * Constructs new button group and adds specified buttons.
     *
     * @param buttons buttons to add into this group
     */
    public UnselectableButtonGroup ( final List<AbstractButton> buttons )
    {
        super ();
        add ( buttons );
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
     * Adds specified buttons into this group.
     *
     * @param buttons buttons to add into this group
     */
    public void add ( final AbstractButton... buttons )
    {
        for ( final AbstractButton button : buttons )
        {
            add ( button );
        }
    }

    /**
     * Adds specified buttons into this group.
     *
     * @param buttons buttons to add into this group
     */
    public void add ( final List<AbstractButton> buttons )
    {
        for ( final AbstractButton button : buttons )
        {
            add ( button );
        }
    }

    /**
     * Removes all buttons from the group.
     */
    public void removeAll ()
    {
        clearSelection ();
        for ( int i = buttons.size () - 1; i >= 0; i-- )
        {
            final AbstractButton b = buttons.get ( i );
            buttons.remove ( i );
            b.getModel ().setGroup ( null );
        }
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
        listeners.add ( listener );
    }

    /**
     * Removes {@link com.alee.utils.swing.ButtonGroupListener}.
     *
     * @param listener {@link com.alee.utils.swing.ButtonGroupListener} to remove
     */
    public void removeButtonGroupListener ( final ButtonGroupListener listener )
    {
        listeners.remove ( listener );
    }

    /**
     * Informs all {@link com.alee.utils.swing.ButtonGroupListener}s about group selection change.
     */
    public void fireSelectionChanged ()
    {
        for ( final ButtonGroupListener listener : CollectionUtils.copy ( listeners ) )
        {
            listener.selectionChanged ();
        }
    }

    /**
     * Returns buttons group used to group specified buttons.
     *
     * @param buttons buttons to group
     * @return buttons group used to group specified buttons
     */
    public static UnselectableButtonGroup group ( final AbstractButton... buttons )
    {
        return new UnselectableButtonGroup ( buttons );
    }

    /**
     * Returns buttons group used to group specified buttons.
     *
     * @param buttons buttons to group
     * @return buttons group used to group specified buttons
     */
    public static UnselectableButtonGroup group ( final List<AbstractButton> buttons )
    {
        return new UnselectableButtonGroup ( buttons );
    }
}