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

package com.alee.laf.checkbox;

import com.alee.extended.checkbox.CheckState;

import java.awt.*;

/**
 * Special class that represents checkbox icon.
 * It can be used to render checkbox component icon.
 *
 * @author Mikle Garin
 */

public abstract class CheckIcon
{
    /**
     * Whether should paint enabled check icon or not.
     */
    protected boolean enabled = true;

    /**
     * Currently active state.
     */
    protected CheckState state = CheckState.unchecked;

    /**
     * Next active state.
     * Not null only while transition is in progress.
     */
    protected CheckState nextState = null;

    /**
     * Returns whether should paint enabled check icon or not.
     *
     * @return true if should paint enabled check icon, false otherwise
     */
    public boolean isEnabled ()
    {
        return enabled;
    }

    /**
     * Sets whether should paint enabled check icon or not.
     *
     * @param enabled whether should paint enabled check icon or not
     */
    public void setEnabled ( final boolean enabled )
    {
        this.enabled = enabled;
    }

    /**
     * Returns currently active state.
     *
     * @return currently active state
     */
    public CheckState getState ()
    {
        return state;
    }

    /**
     * Sets currently active check state.
     *
     * @param state new active check state
     */
    public void setState ( final CheckState state )
    {
        this.state = state;
        this.nextState = null;
        resetStep ();
    }

    /**
     * Returns next active state.
     *
     * @return next active state
     */
    public CheckState getNextState ()
    {
        return nextState;
    }

    /**
     * Sets next active state.
     *
     * @param nextState next active state
     */
    public void setNextState ( CheckState nextState )
    {
        this.state = this.nextState != null ? this.nextState : this.state;
        this.nextState = nextState;
    }

    /**
     * Displays next step toward the next active state.
     */
    public abstract void doStep ();

    /**
     * Resets steps according to currently set state and next state.
     */
    public abstract void resetStep ();

    /**
     * Returns whether current transition has reached its end or not.
     *
     * @return true if current transition has reached its end, false otherwise
     */
    public abstract boolean isTransitionCompleted ();

    /**
     * Finishes transition.
     */
    public abstract void finishTransition ();

    /**
     * Returns check icon width.
     *
     * @return check icon width
     */
    public abstract int getIconWidth ();

    /**
     * Returns check icon height.
     *
     * @return check icon height
     */
    public abstract int getIconHeight ();

    /**
     * Paints check icon in the specified bounds.
     *
     * @param c   component to paint check icon onto
     * @param g2d graphics context
     * @param x   icon X coordinate
     * @param y   icon Y coordinate
     * @param w   icon width
     * @param h   icon height
     */
    public abstract void paintIcon ( Component c, Graphics2D g2d, int x, int y, int w, int h );
}