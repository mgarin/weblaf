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

import com.alee.extended.colorchooser.GradientData;
import com.alee.extended.colorchooser.WebGradientColorChooser;
import com.alee.managers.settings.SettingsManager;
import com.alee.managers.settings.SettingsProcessor;
import com.alee.managers.settings.SettingsProcessorData;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * Custom SettingsProcessor for WebGradientColorChooser component.
 *
 * @author Mikle Garin
 */

public class WebGradientColorChooserSettingsProcessor extends SettingsProcessor<WebGradientColorChooser, GradientData>
{
    /**
     * Gradient change listener.
     */
    private ChangeListener changeListener;

    /**
     * Constructs SettingsProcessor using the specified SettingsProcessorData.
     *
     * @param data SettingsProcessorData
     */
    public WebGradientColorChooserSettingsProcessor ( SettingsProcessorData data )
    {
        super ( data );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void doInit ( WebGradientColorChooser gradientColorChooser )
    {
        changeListener = new ChangeListener ()
        {
            @Override
            public void stateChanged ( ChangeEvent e )
            {
                if ( SettingsManager.isSaveOnChange () )
                {
                    save ();
                }
            }
        };
        gradientColorChooser.addChangeListener ( changeListener );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void doDestroy ( WebGradientColorChooser gradientColorChooser )
    {
        gradientColorChooser.removeChangeListener ( changeListener );
        changeListener = null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void doLoad ( WebGradientColorChooser gradientColorChooser )
    {
        gradientColorChooser.setGradientData ( loadValue () );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void doSave ( WebGradientColorChooser gradientColorChooser )
    {
        saveValue ( gradientColorChooser.getGradientData () );
    }
}