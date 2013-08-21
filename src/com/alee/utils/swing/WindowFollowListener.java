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
 * User: mgarin Date: 20.01.12 Time: 21:33
 * <p/>
 * This listener allows you to quickly attach one window to another window. This means that this window will always follow the other one
 * when it moves on the screen. Though the other window in this link will not follow this window as it moves.
 * <p/>
 * Example usage:
 * <p/>
 * <code>new WindowFollowListener ( dialog, mainWindow );</code>
 * <p/>
 * This is enough to link the child dialog movement with the mainWindow movement.
 */

public class WindowFollowListener extends ComponentAdapter
{
    private boolean enabled = true;
    private Window followingWindow;
    private Window parentWindow;
    private Point ll;

    public static void install ( Window followingWindow, Window parentWindow )
    {
        parentWindow.addComponentListener ( new WindowFollowListener ( followingWindow, parentWindow ) );
    }

    public WindowFollowListener ( Window followingWindow, Window parentWindow )
    {
        super ();
        this.followingWindow = followingWindow;
        this.parentWindow = parentWindow;
        this.ll = parentWindow.getLocation ();
    }

    public boolean isEnabled ()
    {
        return enabled;
    }

    public void setEnabled ( boolean enabled )
    {
        this.enabled = enabled;
    }

    public Window getFollowingWindow ()
    {
        return followingWindow;
    }

    public void setFollowingWindow ( Window followingWindow )
    {
        this.followingWindow = followingWindow;
    }

    public Window getParentWindow ()
    {
        return parentWindow;
    }

    public void setParentWindow ( Window parentWindow )
    {
        this.parentWindow = parentWindow;
    }

    @Override
    public void componentResized ( ComponentEvent e )
    {
        this.ll = parentWindow.getLocation ();
    }

    @Override
    public void componentMoved ( ComponentEvent e )
    {
        if ( enabled && followingWindow != null && parentWindow != null )
        {
            Point nl = parentWindow.getLocation ();
            Point fwl = followingWindow.getLocation ();
            followingWindow.setLocation ( fwl.x + nl.x - ll.x, fwl.y + nl.y - ll.y );
            this.ll = nl;
        }
    }
}