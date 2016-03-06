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

import com.alee.api.Behavior;

import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

/**
 * This behavior allows you to quickly attach one window to another window.
 * Window will always follow the other one it is attached by moving for the same distance window it is attached to was moved.
 * But window this one is attached to will not follow this window as it moves.
 * <p>
 * It is not recommended to use this behavior to completely lock two windows to each other as it will cause issues.
 * Another behavior will be available later-on to provide that kind of functionality.
 *
 * @author Mikle Garin
 */

public class WindowFollowBehavior extends ComponentAdapter implements Behavior
{
    /**
     * Window that follows another one it is attached to.
     */
    protected final Window attachedWindow;

    /**
     * Followed window.
     */
    protected final Window followedWindow;

    /**
     * Whether or not this behavior is enabled.
     */
    protected boolean enabled = true;

    /**
     * Last spotted location of the followed window.
     */
    protected Point lastLocation;

    public WindowFollowBehavior ( final Window attachedWindow, final Window followedWindow )
    {
        super ();
        this.attachedWindow = attachedWindow;
        this.followedWindow = followedWindow;
        updateLastLocation ();
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
        this.lastLocation = followedWindow.getLocation ();
    }

    public Window getAttachedWindow ()
    {
        return attachedWindow;
    }

    public Window getFollowedWindow ()
    {
        return followedWindow;
    }

    @Override
    public void componentResized ( final ComponentEvent e )
    {
        updateLastLocation ();
    }

    @Override
    public void componentMoved ( final ComponentEvent e )
    {
        if ( isEnabled () && attachedWindow != null && followedWindow != null )
        {
            final Point nl = followedWindow.getLocation ();
            final Point fwl = attachedWindow.getLocation ();
            attachedWindow.setLocation ( fwl.x + nl.x - lastLocation.x, fwl.y + nl.y - lastLocation.y );
            this.lastLocation = nl;
        }
    }

    /**
     * Installs behavior to the specified component.
     *
     * @param attachedWindow window that follows another one it is attached to
     * @param followedWindow followed window to uninstall behavior from
     * @return installed behavior
     */
    public static WindowFollowBehavior install ( final Window attachedWindow, final Window followedWindow )
    {
        final WindowFollowBehavior windowFollowBehavior = new WindowFollowBehavior ( attachedWindow, followedWindow );
        followedWindow.addComponentListener ( windowFollowBehavior );
        return windowFollowBehavior;
    }

    /**
     * Uninstalls behavior from the specified followed window.
     *
     * @param attachedWindow window that follows another one it is attached to
     * @param followedWindow followed window to uninstall behavior from
     */
    public static void uninstall ( final Window attachedWindow, final Window followedWindow )
    {
        for ( final ComponentListener listener : followedWindow.getComponentListeners () )
        {
            if ( listener instanceof WindowFollowBehavior )
            {
                if ( ( ( WindowFollowBehavior ) listener ).getAttachedWindow () == attachedWindow )
                {
                    followedWindow.removeComponentListener ( listener );
                }
            }
        }
    }
}