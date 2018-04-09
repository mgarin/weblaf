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

import com.alee.extended.layout.AbstractLayoutManager;
import com.alee.managers.style.BoundsType;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Custom layout for proper notifications placement on layered pane.
 *
 * @author Mikle Garin
 * @see com.alee.managers.notification.NotificationManager
 * @see com.alee.managers.notification.NotificationsLayoutUtils
 */
public class NotificationsLayout extends AbstractLayoutManager implements SwingConstants
{
    /**
     * Cached notifications.
     */
    protected final List<WebInnerNotification> notifications = new ArrayList<WebInnerNotification> ( 2 );

    /**
     * Notifications lock.
     */
    protected final Object lock = new Object ();

    @Override
    public void addComponent ( final Component component, final Object constraints )
    {
        synchronized ( lock )
        {
            if ( component instanceof WebInnerNotification )
            {
                notifications.add ( ( WebInnerNotification ) component );
            }
        }
    }

    @Override
    public void removeComponent ( final Component component )
    {
        synchronized ( lock )
        {
            if ( component instanceof WebInnerNotification )
            {
                notifications.remove ( component );
            }
        }
    }

    @Override
    public Dimension preferredLayoutSize ( final Container container )
    {
        return null;
    }

    @Override
    public void layoutContainer ( final Container container )
    {
        synchronized ( lock )
        {
            if ( notifications.size () > 0 )
            {
                // Container bounds
                final Rectangle bounds = BoundsType.margin.bounds ( container );

                // Layout notifications
                NotificationsLayoutUtils.layout ( notifications, bounds );
            }
        }
    }
}