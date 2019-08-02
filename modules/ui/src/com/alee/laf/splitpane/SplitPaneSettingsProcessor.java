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

package com.alee.laf.splitpane;

import com.alee.managers.settings.Configuration;
import com.alee.managers.settings.SettingsProcessor;

import javax.swing.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * {@link SettingsProcessor} implementation that handles {@link JSplitPane} settings.
 *
 * @author bspkrs
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-SettingsManager">How to use SettingsManager</a>
 * @see com.alee.managers.settings.UISettingsManager
 * @see com.alee.managers.settings.SettingsManager
 * @see SettingsProcessor
 */
public class SplitPaneSettingsProcessor extends SettingsProcessor<JSplitPane, SplitPaneState, Configuration<SplitPaneState>>
{
    /**
     * {@link PropertyChangeListener} for tracking {@link JSplitPane} divider location changes.
     */
    protected transient PropertyChangeListener propertyChangeListener;

    /**
     * Constructs new {@link SplitPaneSettingsProcessor}.
     *
     * @param splitPane     {@link JSplitPane} which settings are being managed
     * @param configuration {@link Configuration}
     */
    public SplitPaneSettingsProcessor ( final JSplitPane splitPane, final Configuration configuration )
    {
        super ( splitPane, configuration );
    }

    @Override
    protected void register ( final JSplitPane splitPane )
    {
        propertyChangeListener = new PropertyChangeListener ()
        {
            @Override
            public void propertyChange ( final PropertyChangeEvent evt )
            {
                save ();
            }
        };
        splitPane.addPropertyChangeListener ( JSplitPane.DIVIDER_LOCATION_PROPERTY, propertyChangeListener );
    }

    @Override
    protected void unregister ( final JSplitPane splitPane )
    {
        splitPane.removePropertyChangeListener ( JSplitPane.DIVIDER_LOCATION_PROPERTY, propertyChangeListener );
        propertyChangeListener = null;
    }

    @Override
    protected SplitPaneState createDefaultValue ()
    {
        return new SplitPaneState ( component () );
    }

    @Override
    protected void loadSettings ( final JSplitPane splitPane )
    {
        loadSettings ().apply ( splitPane );
    }

    @Override
    protected void saveSettings ( final JSplitPane splitPane )
    {
        saveSettings ( new SplitPaneState ( splitPane ) );
    }
}