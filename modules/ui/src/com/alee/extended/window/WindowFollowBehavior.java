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

package com.alee.extended.window;

import com.alee.extended.behavior.Behavior;

import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

/**
 * Custom {@link Behavior} that allows you to quickly attach one {@link Window} to another {@link Window}.
 * Attached {@link Window} will move for the same distance as {@link Window} it is attached to is moved.
 *
 * This behavior is only intended for single-sided movement lock.
 * You shouldn't use this behavior to lock two {@link Window}s to each other as it will cause issues.
 * Another {@link Behavior} should be used to provide that kind of functionality.
 *
 * @author Mikle Garin
 */
public class WindowFollowBehavior extends ComponentAdapter implements Behavior
{
    /**
     * {@link Window} that follows another one it is attached to.
     */
    protected final Window attached;

    /**
     * {@link Window} to be followed by attached one.
     */
    protected final Window followed;

    /**
     * Whether or not this behavior is currently enabled.
     */
    protected boolean enabled;

    /**
     * Last spotted location of the followed window.
     */
    protected Point lastLocation;

    /**
     * Constructs new {@link WindowFollowBehavior}.
     *
     * @param attached {@link Window} that follows another one it is attached to
     * @param followed {@link Window} to be followed by attached one
     */
    public WindowFollowBehavior ( final Window attached, final Window followed )
    {
        super ();
        this.attached = attached;
        this.followed = followed;
        this.enabled = true;
        updateLastLocation ();
    }

    /**
     * Installs behavior into component.
     */
    public void install ()
    {
        followed.addComponentListener ( this );
    }

    /**
     * Uninstalls behavior from the component.
     */
    public void uninstall ()
    {
        followed.removeComponentListener ( this );
    }

    /**
     * Returns whether or not this behavior is currently enabled.
     *
     * @return {@code true} if this behavior is currently enabled, {@code false} otherwise
     */
    public boolean isEnabled ()
    {
        return enabled;
    }

    /**
     * Sets whether or not this behavior is currently enabled.
     *
     * @param enabled whether or not this behavior is currently enabled
     */
    public void setEnabled ( final boolean enabled )
    {
        this.enabled = enabled;
    }

    /**
     * Returns {@link Window} that follows another one it is attached to.
     *
     * @return {@link Window} that follows another one it is attached to
     */
    public Window getAttached ()
    {
        return attached;
    }

    /**
     * Returns {@link Window} to be followed by attached one.
     *
     * @return {@link Window} to be followed by attached one
     */
    public Window getFollowed ()
    {
        return followed;
    }

    @Override
    public void componentResized ( final ComponentEvent e )
    {
        updateLastLocation ();
    }

    @Override
    public void componentMoved ( final ComponentEvent e )
    {
        if ( isEnabled () && attached != null && followed != null )
        {
            final Point nl = followed.getLocation ();
            final Point fwl = attached.getLocation ();
            attached.setLocation ( fwl.x + nl.x - lastLocation.x, fwl.y + nl.y - lastLocation.y );
            this.lastLocation = nl;
        }
    }

    /**
     * Updates last spotted location of the followed window.
     */
    public void updateLastLocation ()
    {
        this.lastLocation = followed.getLocation ();
    }
}