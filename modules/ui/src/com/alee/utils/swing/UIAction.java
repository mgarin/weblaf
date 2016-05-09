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

import javax.swing.*;
import java.beans.PropertyChangeListener;

/**
 * @author Mikle Garin
 */

public abstract class UIAction implements Action
{
    /**
     * Action name.
     */
    private final String name;

    /**
     * Constructs new UI action.
     *
     * @param name action name
     */
    public UIAction ( final String name )
    {
        this.name = name;
    }

    /**
     * Returns action name.
     *
     * @return action name
     */
    public final String getName ()
    {
        return name;
    }

    @Override
    public Object getValue ( final String key )
    {
        return key.equals ( NAME ) ? name : null;
    }

    // UIAction is not mutable, this does nothing.
    @Override
    public void putValue ( final String key, final Object value )
    {
    }

    // UIAction is not mutable, this does nothing.
    @Override
    public void setEnabled ( final boolean b )
    {
    }

    /**
     * Cover method for {@code isEnabled(null)}.
     */
    @Override
    public final boolean isEnabled ()
    {
        return isEnabled ( null );
    }

    /**
     * Subclasses that need to conditionalize the enabled state should
     * override this. Be aware that {@code sender} may be null.
     *
     * @param sender Widget enabled state is being asked for, may be null.
     * @return whether or not action is enabled
     */
    public boolean isEnabled ( final Object sender )
    {
        return true;
    }

    // UIAction is not mutable, this does nothing.
    @Override
    public void addPropertyChangeListener ( final PropertyChangeListener listener )
    {
    }

    // UIAction is not mutable, this does nothing.
    @Override
    public void removePropertyChangeListener ( final PropertyChangeListener listener )
    {
    }
}