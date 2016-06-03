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
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * Custom SettingsProcessor for {@link javax.swing.JSplitPane} component.
 *
 * @author bspkrs
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-SettingsManager">How to use SettingsManager</a>
 * @see com.alee.managers.settings.SettingsManager
 * @see com.alee.managers.settings.SettingsProcessor
 */

public class SplitPaneSettingsProcessor extends SettingsProcessor<JSplitPane, Integer>
{
    /**
     * Split pane location change listener.
     */
    private PropertyChangeListener listener;

    /**
     * Constructs SettingsProcessor using the specified SettingsProcessorData.
     *
     * @param data SettingsProcessorData
     */
    public SplitPaneSettingsProcessor ( final SettingsProcessorData data )
    {
        super ( data );
    }

    @Override
    protected void doInit ( final JSplitPane splitPane )
    {
        listener = new PropertyChangeListener ()
        {
            @Override
            public void propertyChange ( final PropertyChangeEvent pce )
            {
                save ();
            }
        };
        splitPane.addPropertyChangeListener ( JSplitPane.DIVIDER_LOCATION_PROPERTY, listener );
    }

    @Override
    protected void doDestroy ( final JSplitPane splitPane )
    {
        splitPane.removePropertyChangeListener ( listener );
        listener = null;
    }

    @Override
    protected void doLoad ( final JSplitPane splitPane )
    {
        final Integer location = loadValue ();
        splitPane.setDividerLocation ( location != null ? location : -1 );
    }

    @Override
    protected void doSave ( final JSplitPane splitPane )
    {
        saveValue ( splitPane.getDividerLocation () );
    }
}