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

import com.alee.managers.settings.Configuration;
import com.alee.managers.settings.SettingsProcessor;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * {@link SettingsProcessor} implementation that handles {@link JComboBox} settings.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-SettingsManager">How to use SettingsManager</a>
 * @see com.alee.managers.settings.UISettingsManager
 * @see com.alee.managers.settings.SettingsManager
 * @see SettingsProcessor
 */
public class ComboBoxSettingsProcessor extends SettingsProcessor<JComboBox, ComboBoxState, Configuration<ComboBoxState>>
{
    /**
     * {@link ActionListener} for tracking {@link JComboBox} selected index.
     */
    protected transient ActionListener actionListener;

    /**
     * Constructs new {@link ComboBoxSettingsProcessor}.
     *
     * @param comboBox      {@link JComboBox} which settings are being managed
     * @param configuration {@link Configuration}
     */
    public ComboBoxSettingsProcessor ( final JComboBox comboBox, final Configuration configuration )
    {
        super ( comboBox, configuration );
    }

    @Override
    protected void register ( final JComboBox comboBox )
    {
        actionListener = new ActionListener ()
        {
            @Override
            public void actionPerformed ( final ActionEvent e )
            {
                save ();
            }
        };
        comboBox.addActionListener ( actionListener );
    }

    @Override
    protected void unregister ( final JComboBox comboBox )
    {
        comboBox.removeActionListener ( actionListener );
        actionListener = null;
    }


    @Override
    protected ComboBoxState createDefaultValue ()
    {
        return new ComboBoxState ( component () );
    }

    @Override
    protected void loadSettings ( final JComboBox comboBox )
    {
        loadSettings ().apply ( comboBox );
    }

    @Override
    protected void saveSettings ( final JComboBox comboBox )
    {
        saveSettings ( new ComboBoxState ( comboBox ) );
    }
}