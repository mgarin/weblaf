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
 * @since 1.4
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
    public JTabbedPaneSettingsProcessor ( SettingsProcessorData data )
    {
        super ( data );
    }

    /**
     * {@inheritDoc}
     */
    protected void doInit ( JTabbedPane component )
    {
        listener = new ChangeListener ()
        {
            public void stateChanged ( ChangeEvent e )
            {
                save ();
            }
        };
        component.addChangeListener ( listener );
    }

    /**
     * {@inheritDoc}
     */
    protected void doDestroy ( JTabbedPane component )
    {
        component.removeChangeListener ( listener );
        listener = null;
    }

    /**
     * {@inheritDoc}
     */
    protected void doLoad ( JTabbedPane component )
    {
        final Integer index = loadValue ();
        if ( index != null && index >= 0 && component.getTabCount () > index && index != component.getSelectedIndex () )
        {
            component.setSelectedIndex ( index );
        }
    }

    /**
     * {@inheritDoc}
     */
    protected void doSave ( JTabbedPane component )
    {
        saveValue ( component.getSelectedIndex () );
    }
}