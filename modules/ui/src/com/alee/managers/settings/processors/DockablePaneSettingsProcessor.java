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

import com.alee.extended.dock.*;
import com.alee.extended.dock.data.StructureContainer;
import com.alee.managers.settings.SettingsProcessor;
import com.alee.managers.settings.SettingsProcessorData;
import com.alee.painter.decoration.states.CompassDirection;

/**
 * Custom SettingsProcessor for {@link com.alee.extended.dock.WebDockablePane} component.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-SettingsManager">How to use SettingsManager</a>
 * @see com.alee.managers.settings.SettingsManager
 * @see com.alee.managers.settings.SettingsProcessor
 */

public class DockablePaneSettingsProcessor extends SettingsProcessor<WebDockablePane, StructureContainer> implements DockableFrameListener
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
        component.addFrameListener ( this );
    }

    @Override
    protected void doDestroy ( final WebDockablePane component )
    {
        component.removeFrameListener ( this );
    }

    @Override
    public void frameOpened ( final WebDockableFrame frame )
    {
        // This event is tracked within state change
    }

    @Override
    public void frameStateChanged ( final WebDockableFrame frame, final DockableFrameState oldState, final DockableFrameState newState )
    {
        save ();
    }

    @Override
    public void frameMoved ( final WebDockableFrame frame, final CompassDirection position )
    {
        save ();
    }

    @Override
    public void frameClosed ( final WebDockableFrame frame )
    {
        // This event is tracked within state change
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