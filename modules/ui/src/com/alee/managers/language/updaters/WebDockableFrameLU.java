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

package com.alee.managers.language.updaters;

import com.alee.extended.dock.WebDockableFrame;
import com.alee.managers.language.data.Value;

/**
 * This class provides language default updates for WebDockableFrame component.
 *
 * @author Mikle Garin
 */

public class WebDockableFrameLU extends DefaultLanguageUpdater<WebDockableFrame>
{
    @Override
    public void update ( final WebDockableFrame c, final String key, final Value value, final Object... data )
    {
        c.setTitle ( getDefaultText ( value, data ) );
    }
}