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
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * Custom SettingsProcessor for JTabbedPane component.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-SettingsManager">How to use SettingsManager</a>
 * @see com.alee.managers.settings.SettingsManager
 * @see com.alee.managers.settings.SettingsProcessor
 */

public class JTabbedPaneSettingsProcessor extends SettingsProcessor<JTabbedPane, Integer>
{
    /**
     * Tab selection change listener.
     */
    private ChangeListener listener;

    /**
     * Constructs SettingsProcessor using the specified SettingsProcessorData.
     *
     * @param data SettingsProcessorData
     */
    public JTabbedPaneSettingsProcessor ( final SettingsProcessorData data )
    {
        super ( data );
    }

    @Override
    protected void doInit ( final JTabbedPane component )
    {
        listener = new ChangeListener ()
        {
            @Override
            public void stateChanged ( final ChangeEvent e )
            {
                save ();
            }
        };
        component.addChangeListener ( listener );
    }

    @Override
    protected void doDestroy ( final JTabbedPane component )
    {
        component.removeChangeListener ( listener );
        listener = null;
    }

    @Override
    protected void doLoad ( final JTabbedPane component )
    {
        final Integer index = loadValue ();
        if ( index != null && index >= 0 && component.getTabCount () > index && index != component.getSelectedIndex () )
        {
            component.setSelectedIndex ( index );
        }
    }

    @Override
    protected void doSave ( final JTabbedPane component )
    {
        saveValue ( component.getSelectedIndex () );
    }
}