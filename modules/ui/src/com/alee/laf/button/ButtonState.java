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

package com.alee.laf.button;

import com.alee.api.merge.Mergeable;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

import javax.swing.*;
import java.io.Serializable;

/**
 * {@link AbstractButton} state holder.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-SettingsManager">How to use SettingsManager</a>
 * @see ButtonSettingsProcessor
 * @see com.alee.managers.settings.UISettingsManager
 * @see com.alee.managers.settings.SettingsManager
 * @see com.alee.managers.settings.SettingsProcessor
 */
@XStreamAlias ( "ButtonState" )
public class ButtonState implements Mergeable, Cloneable, Serializable
{
    /**
     * Whether or not {@link AbstractButton} is selected.
     */
    @XStreamAsAttribute
    protected final Boolean selected;

    /**
     * Constructs default {@link ButtonState}.
     */
    public ButtonState ()
    {
        this ( ( Boolean ) null );
    }

    /**
     * Constructs new {@link ButtonState} with settings from {@link AbstractButton}.
     *
     * @param button {@link AbstractButton} to retrieve settings from
     */
    public ButtonState ( final AbstractButton button )
    {
        this ( button.isSelected () );
    }

    /**
     * Constructs new {@link ButtonState} with specified settings.
     *
     * @param selected whether or not {@link AbstractButton} is selected
     */
    public ButtonState ( final Boolean selected )
    {
        this.selected = selected;
    }

    /**
     * Returns whether or not {@link AbstractButton} is selected.
     *
     * @return {@code true} if {@link AbstractButton} is selected, {@code false} otherwise
     */
    public boolean isSelected ()
    {
        return selected != null && selected;
    }

    /**
     * Applies this {@link ButtonState} to the specified {@link AbstractButton}.
     *
     * @param button {@link AbstractButton} to apply this {@link ButtonState} to
     */
    public void apply ( final AbstractButton button )
    {
        if ( selected != null && button.isSelected () != selected )
        {
            button.setSelected ( selected );
        }
    }
}