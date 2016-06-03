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

import com.alee.extended.dock.WebDockablePane;
import com.alee.extended.dock.data.StructureContainer;
import com.alee.managers.settings.SettingsProcessor;
import com.alee.managers.settings.SettingsProcessorData;

/**
 * Custom SettingsProcessor for {@link com.alee.extended.dock.WebDockablePane} component.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-SettingsManager">How to use SettingsManager</a>
 * @see com.alee.managers.settings.SettingsManager
 * @see com.alee.managers.settings.SettingsProcessor
 */

public class DockablePaneSettingsProcessor extends SettingsProcessor<WebDockablePane, StructureContainer>
{
    /**
     * Constructs SettingsProcessor using the specified SettingsProcessorData.
     *
     * @param data SettingsProcessorData
     */
    public DockablePaneSettingsProcessor ( final SettingsProcessorData data )
    {
        super ( data );
    }

    @Override
    protected void doInit ( final WebDockablePane component )
    {
        // todo Call save (); from listeners
    }

    @Override
    protected void doDestroy ( final WebDockablePane component )
    {
        // todo Destroy listeners
    }

    @Override
    protected void doLoad ( final WebDockablePane component )
    {
        final StructureContainer state = loadValue ();
        if ( state != null )
        {
            component.setState ( state );
        }
    }

    @Override
    protected void doSave ( final WebDockablePane component )
    {
        saveValue ( component.getState () );
    }
}