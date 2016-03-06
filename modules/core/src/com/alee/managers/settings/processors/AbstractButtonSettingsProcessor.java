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

import javax.swing.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

/**
 * Custom SettingsProcessor for AbstractButton component.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-SettingsManager">How to use SettingsManager</a>
 * @see com.alee.managers.settings.SettingsManager
 * @see com.alee.managers.settings.SettingsProcessor
 */

public class AbstractButtonSettingsProcessor extends SettingsProcessor<AbstractButton, Boolean>
{
    /**
     * Button state change listener.
     */
    private ItemListener itemListener;

    /**
     * Constructs SettingsProcessor using the specified SettingsProcessorData.
     *
     * @param data SettingsProcessorData
     */
    public AbstractButtonSettingsProcessor ( final SettingsProcessorData data )
    {
        super ( data );
    }

    @Override
    public Boolean getDefaultValue ()
    {
        Boolean defaultValue = super.getDefaultValue ();
        if ( defaultValue == null )
        {
            defaultValue = false;
        }
        return defaultValue;
    }

    @Override
    protected void doInit ( final AbstractButton abstractButton )
    {
        itemListener = new ItemListener ()
        {
            @Override
            public void itemStateChanged ( final ItemEvent e )
            {
                save ();
            }
        };
        abstractButton.addItemListener ( itemListener );
    }

    @Override
    public void doDestroy ( final AbstractButton abstractButton )
    {
        abstractButton.removeItemListener ( itemListener );
        itemListener = null;
    }

    @Override
    public void doLoad ( final AbstractButton abstractButton )
    {
        final boolean newValue = loadValue ();
        if ( abstractButton.isSelected () != newValue )
        {
            abstractButton.setSelected ( newValue );
        }
    }

    @Override
    public void doSave ( final AbstractButton abstractButton )
    {
        saveValue ( abstractButton.isSelected () );
    }
}