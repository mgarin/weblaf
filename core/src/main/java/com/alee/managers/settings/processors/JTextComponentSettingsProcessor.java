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

package com.alee.managers.settings.processors;

import com.alee.managers.settings.SettingsManager;
import com.alee.managers.settings.SettingsProcessor;
import com.alee.managers.settings.SettingsProcessorData;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

/**
 * Custom SettingsProcessor for JTextComponent component.
 *
 * @author Mikle Garin
 * @since 1.4
 */

public class JTextComponentSettingsProcessor extends SettingsProcessor<JTextComponent, String>
{
    /**
     * Component action listener.
     */
    private ActionListener actionListener;

    /**
     * Component focus loss listener.
     */
    private FocusAdapter focusAdapter;

    /**
     * Constructs SettingsProcessor using the specified SettingsProcessorData.
     *
     * @param data SettingsProcessorData
     */
    public JTextComponentSettingsProcessor ( SettingsProcessorData data )
    {
        super ( data );
    }

    /**
     * {@inheritDoc}
     */
    public String getDefaultValue ()
    {
        String defaultValue = super.getDefaultValue ();
        if ( defaultValue == null )
        {
            defaultValue = "";
        }
        return defaultValue;
    }

    /**
     * {@inheritDoc}
     */
    protected void doInit ( JTextComponent textComponent )
    {
        focusAdapter = new FocusAdapter ()
        {
            public void focusLost ( FocusEvent e )
            {
                if ( SettingsManager.isSaveOnChange () )
                {
                    save ();
                }
            }
        };
        textComponent.addFocusListener ( focusAdapter );

        if ( textComponent instanceof JTextField )
        {
            JTextField textField = ( JTextField ) textComponent;
            actionListener = new ActionListener ()
            {
                public void actionPerformed ( ActionEvent e )
                {
                    if ( SettingsManager.isSaveOnChange () )
                    {
                        save ();
                    }
                }
            };
            textField.addActionListener ( actionListener );
        }
    }

    /**
     * {@inheritDoc}
     */
    protected void doLoad ( JTextComponent textComponent )
    {
        textComponent.setText ( loadValue () );
    }

    /**
     * {@inheritDoc}
     */
    protected void doSave ( JTextComponent textComponent )
    {
        saveValue ( textComponent.getText () );
    }

    /**
     * {@inheritDoc}
     */
    protected void doDestroy ( JTextComponent textComponent )
    {
        textComponent.removeFocusListener ( focusAdapter );
        focusAdapter = null;

        if ( textComponent instanceof JTextField )
        {
            JTextField textField = ( JTextField ) textComponent;
            textField.removeActionListener ( actionListener );
            actionListener = null;
        }
    }
}