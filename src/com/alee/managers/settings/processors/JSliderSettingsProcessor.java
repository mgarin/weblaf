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
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * Custom SettingsProcessor for JSlider component.
 *
 * @author Mikle Garin
 */

public class JSliderSettingsProcessor extends SettingsProcessor<JSlider, Integer>
{
    /**
     * Slider value change listener.
     */
    private ChangeListener changeListener;

    /**
     * Constructs SettingsProcessor using the specified SettingsProcessorData.
     *
     * @param data SettingsProcessorData
     */
    public JSliderSettingsProcessor ( SettingsProcessorData data )
    {
        super ( data );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer getDefaultValue ()
    {
        Integer defaultValue = super.getDefaultValue ();
        if ( defaultValue == null )
        {
            defaultValue = getComponent ().getMinimum ();
        }
        return defaultValue;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void doInit ( JSlider slider )
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
        slider.addChangeListener ( changeListener );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void doDestroy ( JSlider slider )
    {
        slider.removeChangeListener ( changeListener );
        changeListener = null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void doLoad ( JSlider slider )
    {
        slider.setValue ( loadValue () );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void doSave ( JSlider slider )
    {
        SettingsManager.set ( getGroup (), getKey (), slider.getValue () );
    }
}