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

import com.alee.managers.settings.SettingsProcessor;
import com.alee.managers.settings.SettingsProcessorData;

import javax.swing.text.JTextComponent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

/**
 * Custom SettingsProcessor for {@link javax.swing.text.JTextComponent} component.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-SettingsManager">How to use SettingsManager</a>
 * @see com.alee.managers.settings.SettingsManager
 * @see com.alee.managers.settings.SettingsProcessor
 */

public class TextComponentSettingsProcessor<C extends JTextComponent> extends SettingsProcessor<C, String> implements FocusListener
{
    /**
     * Constructs SettingsProcessor using the specified SettingsProcessorData.
     *
     * @param data SettingsProcessorData
     */
    public TextComponentSettingsProcessor ( final SettingsProcessorData data )
    {
        super ( data );
    }

    @Override
    public String getDefaultValue ()
    {
        String defaultValue = super.getDefaultValue ();
        if ( defaultValue == null )
        {
            defaultValue = "";
        }
        return defaultValue;
    }

    @Override
    protected void doInit ( final C component )
    {
        component.addFocusListener ( this );
    }

    @Override
    protected void doDestroy ( final C component )
    {
        component.removeFocusListener ( this );
    }

    @Override
    public void focusGained ( final FocusEvent e )
    {
        // This event is irrelevant
    }

    @Override
    public void focusLost ( final FocusEvent e )
    {
        save ();
    }

    @Override
    protected void doLoad ( final C textComponent )
    {
        textComponent.setText ( loadValue () );
    }

    @Override
    protected void doSave ( final C textComponent )
    {
        saveValue ( textComponent.getText () );
    }
}