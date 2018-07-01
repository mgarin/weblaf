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

import com.alee.api.merge.Mergeable;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

import javax.swing.*;
import java.io.Serializable;

/**
 * {@link JComboBox} state holder.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-SettingsManager">How to use SettingsManager</a>
 * @see ComboBoxSettingsProcessor
 * @see com.alee.managers.settings.UISettingsManager
 * @see com.alee.managers.settings.SettingsManager
 * @see com.alee.managers.settings.SettingsProcessor
 */
@XStreamAlias ( "ComboBoxState" )
public class ComboBoxState implements Mergeable, Cloneable, Serializable
{
    /**
     * {@link JComboBox} selected index.
     */
    @XStreamAsAttribute
    protected final Integer selectedIndex;

    /**
     * Constructs default {@link ComboBoxState}.
     */
    public ComboBoxState ()
    {
        this ( ( Integer ) null );
    }

    /**
     * Constructs new {@link ComboBoxState} with settings from {@link JComboBox}.
     *
     * @param comboBox {@link JComboBox} to retrieve settings from
     */
    public ComboBoxState ( final JComboBox comboBox )
    {
        this ( comboBox.getSelectedIndex () );
    }

    /**
     * Constructs new {@link ComboBoxState} with specified settings.
     *
     * @param selectedIndex {@link JComboBox} selected index
     */
    public ComboBoxState ( final Integer selectedIndex )
    {
        this.selectedIndex = selectedIndex;
    }

    /**
     * Returns {@link JComboBox} selected index.
     *
     * @return {@link JComboBox} selected index
     */
    public Integer selectedIndex ()
    {
        return selectedIndex;
    }

    /**
     * Applies this {@link ComboBoxState} to the specified {@link JComboBox}.
     *
     * @param comboBox {@link JComboBox} to apply this {@link ComboBoxState} to
     */
    public void apply ( final JComboBox comboBox )
    {
        if ( selectedIndex != null && selectedIndex >= 0 && selectedIndex < comboBox.getModel ().getSize () &&
                selectedIndex != comboBox.getSelectedIndex () )
        {
            comboBox.setSelectedIndex ( selectedIndex );
        }
    }
}