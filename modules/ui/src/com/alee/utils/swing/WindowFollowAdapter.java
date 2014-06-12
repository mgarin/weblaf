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

package com.alee.utils.swing;

import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

/**
 * This listener allows you to quickly attach one window to another window. This means that this window will always follow the other one
 * when it moves on the screen. Though the other window in this link will not follow this window as it moves.
 *
 * @author Mikle Garin
 */

public class WindowFollowAdapter extends ComponentAdapter
{
    protected final Window followingWindow;
    protected final Window parentWindow;

    protected boolean enabled = true;
    protected Point lastLocation;

    public WindowFollowAdapter ( final Window followingWindow, final Window parentWindow )
    {
        super ();
        this.followingWindow = followingWindow;
        this.parentWindow = parentWindow;
        this.lastLocation = parentWindow.getLocation ();
    }

    public boolean isEnabled ()
    {
        return enabled;
    }

    public void setEnabled ( final boolean enabled )
    {
        this.enabled = enabled;
    }

    public void updateLastLocation ()
    {
        this.lastLocation = parentWindow.getLocation ();
    }

    public Window getFollowingWindow ()
    {
        return followingWindow;
    }

    public Window getParentWindow ()
    {
        return parentWindow;
    }

    @Override
    public void componentResized ( final ComponentEvent e )
    {
        updateLastLocation ();
    }

    @Override
    public void componentMoved ( final ComponentEvent e )
    {
        if ( isEnabled () && followingWindow != null && parentWindow != null )
        {
            final Point nl = parentWindow.getLocation ();
            final Point fwl = followingWindow.getLocation ();
            followingWindow.setLocation ( fwl.x + nl.x - lastLocation.x, fwl.y + nl.y - lastLocation.y );
            this.lastLocation = nl;
        }
    }

    public static WindowFollowAdapter install ( final Window followingWindow, final Window parentWindow )
    {
        final WindowFollowAdapter windowFollowAdapter = new WindowFollowAdapter ( followingWindow, parentWindow );
        parentWindow.addComponentListener ( windowFollowAdapter );
        return windowFollowAdapter;
    }

    public static void uninstall ( final Window parentWindow, final WindowFollowAdapter windowFollowAdapter )
    {
        parentWindow.removeComponentListener ( windowFollowAdapter );
    }
}