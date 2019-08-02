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

package com.alee.laf.tabbedpane;

import com.alee.managers.settings.Configuration;
import com.alee.managers.settings.SettingsProcessor;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * {@link SettingsProcessor} implementation that handles {@link JTabbedPane} settings.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-SettingsManager">How to use SettingsManager</a>
 * @see com.alee.managers.settings.UISettingsManager
 * @see com.alee.managers.settings.SettingsManager
 * @see SettingsProcessor
 */
public class TabbedPaneSettingsProcessor extends SettingsProcessor<JTabbedPane, TabbedPaneState, Configuration<TabbedPaneState>>
{
    /**
     * {@link ChangeListener} for tracking tab selection changes.
     */
    protected transient ChangeListener changeListener;

    /**
     * Constructs new {@link TabbedPaneSettingsProcessor}.
     *
     * @param tabbedPane    {@link JTabbedPane} which settings are being managed
     * @param configuration {@link Configuration}
     */
    public TabbedPaneSettingsProcessor ( final JTabbedPane tabbedPane, final Configuration configuration )
    {
        super ( tabbedPane, configuration );
    }

    @Override
    protected void register ( final JTabbedPane tabbedPane )
    {
        changeListener = new ChangeListener ()
        {
            @Override
            public void stateChanged ( final ChangeEvent e )
            {
                save ();
            }
        };
        tabbedPane.addChangeListener ( changeListener );
    }

    @Override
    protected void unregister ( final JTabbedPane tabbedPane )
    {
        tabbedPane.removeChangeListener ( changeListener );
        changeListener = null;
    }

    @Override
    protected TabbedPaneState createDefaultValue ()
    {
        return new TabbedPaneState ( component () );
    }

    @Override
    protected void loadSettings ( final JTabbedPane tabbedPane )
    {
        loadSettings ().apply ( tabbedPane );
    }

    @Override
    protected void saveSettings ( final JTabbedPane tabbedPane )
    {
        saveSettings ( new TabbedPaneState ( tabbedPane ) );
    }
}