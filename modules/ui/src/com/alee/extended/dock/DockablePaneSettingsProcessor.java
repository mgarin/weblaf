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

package com.alee.extended.dock;

import com.alee.api.annotations.NotNull;
import com.alee.api.data.CompassDirection;
import com.alee.managers.settings.Configuration;
import com.alee.managers.settings.SettingsProcessor;

/**
 * {@link SettingsProcessor} implementation that handles {@link WebDockablePane} settings.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-WebDockablePane">How to use WebDockablePane</a>
 * @see WebDockablePane
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-SettingsManager">How to use SettingsManager</a>
 * @see com.alee.managers.settings.UISettingsManager
 * @see com.alee.managers.settings.SettingsManager
 * @see SettingsProcessor
 */
public class DockablePaneSettingsProcessor extends SettingsProcessor<WebDockablePane, DockablePaneState, Configuration<DockablePaneState>>
{
    /**
     * {@link DockableFrameListener} for tracking {@link WebDockablePane} frames location and state changes.
     */
    protected transient DockablePaneListener dockablePaneListener;

    /**
     * Constructs new {@link DockablePaneSettingsProcessor}.
     *
     * @param dockablePane  {@link WebDockablePane} which settings are being managed
     * @param configuration {@link Configuration}
     */
    public DockablePaneSettingsProcessor ( final WebDockablePane dockablePane, final Configuration configuration )
    {
        super ( dockablePane, configuration );
    }

    @Override
    protected void register ( final WebDockablePane dockablePane )
    {
        dockablePaneListener = new DockablePaneAdapter ()
        {
            @Override
            public void frameStateChanged ( @NotNull final WebDockableFrame frame, @NotNull final DockableFrameState oldState,
                                            @NotNull final DockableFrameState newState )
            {
                save ();
            }

            @Override
            public void frameMoved ( @NotNull final WebDockableFrame frame, @NotNull final CompassDirection position )
            {
                save ();
            }
        };
        dockablePane.addDockablePaneListener ( dockablePaneListener );
    }

    @Override
    protected void unregister ( final WebDockablePane dockablePane )
    {
        dockablePane.removeDockablePaneListener ( dockablePaneListener );
        dockablePaneListener = null;
    }

    @Override
    protected void loadSettings ( final WebDockablePane dockablePane )
    {
        final DockablePaneState state = loadSettings ();
        if ( state != null )
        {
            state.apply ( dockablePane );
        }
    }

    @Override
    protected void saveSettings ( final WebDockablePane dockablePane )
    {
        saveSettings ( new DockablePaneState ( dockablePane ) );
    }
}