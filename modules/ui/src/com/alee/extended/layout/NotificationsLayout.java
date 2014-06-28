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

package com.alee.extended.layout;

import com.alee.managers.notification.DisplayType;
import com.alee.managers.notification.NotificationManager;
import com.alee.managers.notification.WebNotificationPopup;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Custom layout for proper notifications placement on glass pane.
 *
 * @author Mikle Garin
 */

public class NotificationsLayout extends AbstractLayoutManager implements SwingConstants
{
    /**
     * Cached notifications.
     */
    private List<WebNotificationPopup> notifications = new ArrayList<WebNotificationPopup> ( 2 );

    /**
     * {@inheritDoc}
     */
    @Override
    public void addComponent ( Component component, Object constraints )
    {
        if ( component instanceof WebNotificationPopup )
        {
            notifications.add ( ( WebNotificationPopup ) component );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeComponent ( Component component )
    {
        if ( component instanceof WebNotificationPopup )
        {
            notifications.remove ( component );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Dimension preferredLayoutSize ( Container parent )
    {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void layoutContainer ( Container parent )
    {
        if ( notifications.size () > 0 )
        {
            // Notifications settings
            final int location = NotificationManager.getLocation ();
            final boolean east = location == SOUTH_EAST || location == NORTH_EAST;
            final boolean south = location == SOUTH_EAST || location == SOUTH_WEST;
            final boolean flowDisplayType = NotificationManager.getDisplayType () == DisplayType.flow;
            final Insets margin = NotificationManager.getMargin ();
            final int gap = NotificationManager.getGap ();
            final boolean cascade = NotificationManager.isCascade ();
            final int cascadeAmount = NotificationManager.getCascadeAmount ();

            // Container settings
            final int width = parent.getWidth ();
            final int height = parent.getHeight ();

            // Runtime values
            int maxWidth = 0;
            int maxCascade = 0;
            int x = east ? width - margin.right : margin.left;
            final int startY = south ? height - margin.bottom : margin.top;
            int y = startY;
            int count = 0;
            for ( WebNotificationPopup popup : notifications )
            {
                // Calculating settings
                final Dimension ps = popup.getPreferredSize ();
                if ( south ? ( y - ps.height < 0 ) : ( y + ps.height > height ) )
                {
                    final int gapsAmount = cascade ? Math.max ( 1, maxCascade ) : 1;
                    y = startY;
                    x = east ? ( x - maxWidth - gap * gapsAmount ) : ( x + maxWidth + gap );
                    maxWidth = 0;
                    maxCascade = 0;
                    count = 0;
                }
                final int cascadeShear = cascade ? gap * count : 0;

                // Placing notification
                final int x1 = east ? ( x - ps.width - cascadeShear ) : ( x + cascadeShear );
                final int y1 = south ? ( y - ps.height ) : y;
                popup.setBounds ( x1, y1, ps.width, ps.height );
                maxWidth = Math.max ( maxWidth, ps.width );

                // Incrementing notification position
                y = y + ( south ? -1 : 1 ) * ( flowDisplayType ? ps.height + gap : gap );
                if ( cascade )
                {
                    count++;
                    maxCascade = Math.max ( maxCascade, count );
                    if ( count > cascadeAmount - 1 )
                    {
                        count = 0;
                    }
                }
            }
        }
    }
}