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

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Special general utilities for notification layout classes.
 *
 * @author Mikle Garin
 */

public final class NotificationsLayoutUtils implements SwingConstants
{
    /**
     * Lays out provided notifications by changing their bounds.
     * This is a general method that used by different notification layouts.
     *
     * @param notifications notifications to lay out
     * @param bounds        available bounds
     */
    public static void layout ( final List<? extends Component> notifications, final Rectangle bounds )
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

        // Runtime values
        int maxWidth = 0;
        int maxCascade = 0;
        int x = east ? bounds.x + bounds.width - margin.right : bounds.x + margin.left;
        final int startY = south ? bounds.y + bounds.height - margin.bottom : bounds.y + margin.top;
        int y = startY;
        int count = 0;
        for ( final Component notification : notifications )
        {
            // Calculating settings
            final Dimension ps = notification.getPreferredSize ();
            if ( south ? ( y - ps.height < bounds.y ) : ( y + ps.height > bounds.height ) )
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
            notification.setBounds ( x1, y1, ps.width, ps.height );
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