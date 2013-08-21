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

package com.alee.managers.focus;

import java.awt.*;

/**
 * User: mgarin Date: 25.08.11 Time: 17:40
 */

public abstract class DefaultFocusTracker implements FocusTracker
{
    private boolean trackingEnabled;
    private Component component;
    private boolean uniteWithChilds;
    private boolean listenGlobalChange;

    public DefaultFocusTracker ( Component component )
    {
        this ( component, true );
    }

    public DefaultFocusTracker ( Component component, boolean uniteWithChilds )
    {
        this ( component, uniteWithChilds, false );
    }

    public DefaultFocusTracker ( Component component, boolean uniteWithChilds, boolean listenGlobalChange )
    {
        super ();
        this.trackingEnabled = true;
        this.component = component;
        this.uniteWithChilds = uniteWithChilds;
        this.listenGlobalChange = listenGlobalChange;
    }

    public void setTrackingEnabled ( boolean trackingEnabled )
    {
        this.trackingEnabled = trackingEnabled;
    }

    @Override
    public boolean isTrackingEnabled ()
    {
        return trackingEnabled;
    }

    public void setComponent ( Component component )
    {
        this.component = component;
    }

    @Override
    public Component getTrackedComponent ()
    {
        return component;
    }

    public void setUniteWithChilds ( boolean uniteWithChilds )
    {
        this.uniteWithChilds = uniteWithChilds;
    }

    @Override
    public boolean isUniteWithChilds ()
    {
        return uniteWithChilds;
    }

    @Override
    public boolean isListenGlobalChange ()
    {
        return listenGlobalChange;
    }
}
