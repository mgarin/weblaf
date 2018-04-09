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

import com.alee.managers.settings.Configuration;
import com.alee.managers.settings.SettingsProcessor;

import javax.swing.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

/**
 * {@link SettingsProcessor} implementation that handles {@link AbstractButton} settings.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-SettingsManager">How to use SettingsManager</a>
 * @see com.alee.managers.settings.UISettingsManager
 * @see com.alee.managers.settings.SettingsManager
 * @see SettingsProcessor
 */
public class ButtonSettingsProcessor extends SettingsProcessor<AbstractButton, ButtonState, Configuration<ButtonState>>
{
    /**
     * {@link ItemListener} for tracking {@link AbstractButton} state changes.
     */
    protected transient ItemListener itemListener;

    /**
     * Constructs new {@link ButtonSettingsProcessor}.
     *
     * @param button        {@link AbstractButton} which settings are being managed
     * @param configuration {@link Configuration}
     */
    public ButtonSettingsProcessor ( final AbstractButton button, final Configuration configuration )
    {
        super ( button, configuration );
    }

    @Override
    protected void register ( final AbstractButton button )
    {
        itemListener = new ItemListener ()
        {
            @Override
            public void itemStateChanged ( final ItemEvent e )
            {
                save ();
            }
        };
        button.addItemListener ( itemListener );
    }

    @Override
    public void unregister ( final AbstractButton button )
    {
        button.removeItemListener ( itemListener );
        itemListener = null;
    }

    @Override
    protected ButtonState createDefaultValue ()
    {
        return new ButtonState ( component () );
    }

    @Override
    public void loadSettings ( final AbstractButton button )
    {
        loadSettings ().apply ( button );
    }

    @Override
    public void saveSettings ( final AbstractButton button )
    {
        saveSettings ( new ButtonState ( button ) );
    }
}