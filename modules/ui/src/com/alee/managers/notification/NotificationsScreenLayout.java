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

package com.alee.managers.notification;

import com.alee.extended.window.AbstractScreenLayout;

import javax.swing.*;
import java.awt.*;

/**
 * Custom screen layout for {@link NotificationManager}.
 * It properly places notification windows on the screen.
 *
 * @author Mikle Garin
 * @see com.alee.managers.notification.NotificationManager
 * @see com.alee.managers.notification.NotificationsLayoutUtils
 */
public class NotificationsScreenLayout extends AbstractScreenLayout<Window, Object> implements SwingConstants
{
    /**
     * Screen device this layout is attached to.
     */
    protected final GraphicsDevice device;

    /**
     * Constructing layout for default screen device.
     */
    public NotificationsScreenLayout ()
    {
        this ( GraphicsEnvironment.getLocalGraphicsEnvironment ().getDefaultScreenDevice () );
    }

    /**
     * Constructing layout for specified screen device.
     *
     * @param device screen device to construct layout for
     */
    public NotificationsScreenLayout ( final GraphicsDevice device )
    {
        super ();
        this.device = device;
    }

    @Override
    public void layoutScreen ()
    {
        synchronized ( lock )
        {
            if ( windows.size () > 0 )
            {
                // Using screen bounds
                final Rectangle bounds;
                final Rectangle b = device.getDefaultConfiguration ().getBounds ();
                if ( NotificationManager.isAvoidOverlappingSystemToolbar () )
                {
                    // Correcting bounds by taking system toolbar into account
                    final Insets si = Toolkit.getDefaultToolkit ().getScreenInsets ( device.getDefaultConfiguration () );
                    bounds = new Rectangle ( b.x + si.left, b.y + si.top, b.width - si.left - si.right, b.height - si.top - si.bottom );
                }
                else
                {
                    // Using raw screen bounds
                    bounds = b;
                }

                // Layout notifications
                NotificationsLayoutUtils.layout ( windows, bounds );
            }
        }
    }
}