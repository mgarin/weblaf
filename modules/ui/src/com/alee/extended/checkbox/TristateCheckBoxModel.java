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

package com.alee.extended.checkbox;

import com.alee.laf.checkbox.CheckState;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.ItemEvent;

/**
 * Custom button model for WebTristateCheckBox component.
 *
 * @author Mikle Garin
 */
public class TristateCheckBoxModel extends JToggleButton.ToggleButtonModel
{
    /**
     * Mixed state bit constant used to store into state mask.
     */
    public final static int MIXED = 1 << 7;

    /**
     * Whether partially checked tristate checkbox should be checked or unchecked on toggle.
     */
    protected boolean checkMixedOnToggle = false;

    /**
     * Constructs new model for the specified WebTristateCheckBox.
     */
    public TristateCheckBoxModel ()
    {
        super ();
    }

    /**
     * Returns whether partially checked tristate checkbox should be checked or unchecked on toggle
     *
     * @return true if partially checked tristate checkbox should be checked on toggle, false if it should be unchecked
     */
    public boolean isCheckMixedOnToggle ()
    {
        return checkMixedOnToggle;
    }

    /**
     * Sets whether partially checked tristate checkbox should be checked or unchecked on toggle.
     *
     * @param checkMixedOnToggle whether partially checked tristate checkbox should be checked or unchecked on toggle
     */
    public void setCheckMixedOnToggle ( final boolean checkMixedOnToggle )
    {
        this.checkMixedOnToggle = checkMixedOnToggle;
    }

    /**
     * Sets check state.
     *
     * @param state new check state
     */
    public void setState ( final CheckState state )
    {
        switch ( state )
        {
            case unchecked:
                setSelected ( false );
                break;
            case checked:
                setSelected ( true );
                break;
            case mixed:
                setMixed ( true );
                break;
        }
    }

    /**
     * Returns current check state.
     *
     * @return current check state
     */
    public CheckState getState ()
    {
        return isMixed () ? CheckState.mixed : isSelected () ? CheckState.checked : CheckState.unchecked;
    }

    /**
     * Sets whether checkbox is pressed or not.
     *
     * @param pressed whether checkbox is pressed or not
     */
    @Override
    public void setPressed ( final boolean pressed )
    {
        if ( !isEnabled () || isPressed () == pressed )
        {
            return;
        }

        if ( !pressed && isArmed () )
        {
            goToNextState ();
        }

        stateMask = pressed ? stateMask | PRESSED : stateMask & ~PRESSED;

        fireStateChanged ();

        if ( !isPressed () && isArmed () )
        {
            int modifiers = 0;
            final AWTEvent currentEvent = EventQueue.getCurrentEvent ();
            if ( currentEvent instanceof InputEvent )
            {
                modifiers = ( ( InputEvent ) currentEvent ).getModifiers ();
            }
            else if ( currentEvent instanceof ActionEvent )
            {
                modifiers = ( ( ActionEvent ) currentEvent ).getModifiers ();
            }

            final long time = EventQueue.getMostRecentEventTime ();
            fireActionPerformed ( new ActionEvent ( this, ActionEvent.ACTION_PERFORMED, getActionCommand (), time, modifiers ) );
        }
    }

    /**
     * Applies next check state to this tristate checkbox according to its settings.
     */
    protected void goToNextState ()
    {
        setState ( getNextState ( getState () ) );
    }

    /**
     * Sets whether checkbox is in checked state or not.
     *
     * @param selected whether should set checked state or not
     */
    @Override
    public void setSelected ( final boolean selected )
    {
        final boolean mixed = isMixed ();
        if ( mixed )
        {
            stateMask = stateMask & ~MIXED;
            stateMask = selected ? stateMask & ~SELECTED : stateMask | SELECTED;
        }
        super.setSelected ( selected );
    }

    /**
     * Returns whether checkbox is in mixed state or not.
     *
     * @return true if checkbox is in mixed state, false otherwise
     */
    public boolean isMixed ()
    {
        return ( stateMask & MIXED ) != 0;
    }

    /**
     * Sets whether checkbox is in mixed state or not.
     *
     * @param mixed whether should set mixed check state or not
     */
    public void setMixed ( final boolean mixed )
    {
        if ( isMixed () == mixed )
        {
            return;
        }

        stateMask = mixed ? stateMask | MIXED | SELECTED : stateMask & ~MIXED;

        fireStateChanged ();

        fireItemStateChanged ( new ItemEvent ( this, ItemEvent.ITEM_STATE_CHANGED, this, 3 ) );
    }

    /**
     * Returns next check state for check invertion action.
     *
     * @param checkState current check state
     * @return next check state for check invertion action
     */
    protected CheckState getNextState ( final CheckState checkState )
    {
        switch ( checkState )
        {
            case unchecked:
                return checkMixedOnToggle ? CheckState.mixed : CheckState.checked;
            case checked:
                return checkMixedOnToggle ? CheckState.unchecked : CheckState.mixed;
            case mixed:
                return checkMixedOnToggle ? CheckState.checked : CheckState.unchecked;
            default:
                return CheckState.unchecked;
        }
    }
}