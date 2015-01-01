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

import com.alee.utils.swing.AbstractScreenLayout;

import javax.swing.*;
import java.awt.*;

/**
 * Custom screen layout for NotificationManager.
 * It properly places notification windows on the screen.
 *
 * @author Mikle Garin
 * @see com.alee.managers.notification.NotificationManager
 * @see com.alee.managers.notification.NotificationsLayoutUtils
 */

public class NotificationsScreenLayout extends AbstractScreenLayout<Window, Object> implements SwingConstants
{
    @Override
    public void layoutScreen ()
    {
        synchronized ( lock )
        {
            if ( windows.size () > 0 )
            {
                // Screen settings
                final Rectangle bounds;
                final GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment ();
                if ( NotificationManager.isAvoidOverlappingSystemToolbar () )
                {
                    // Correcting bounds by taking system toolbar into account
                    bounds = ge.getMaximumWindowBounds ();
                }
                else
                {
                    // Using default screen bounds
                    bounds = ge.getDefaultScreenDevice ().getDefaultConfiguration ().getBounds ();
                }

                // Layout notifications
                NotificationsLayoutUtils.layout ( windows, bounds );
            }
        }
    }
}