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

import com.alee.extended.layout.NotificationsLayout;
import com.alee.laf.label.WebLabel;
import com.alee.managers.popup.PopupAdapter;
import com.alee.managers.popup.PopupLayer;
import com.alee.managers.popup.PopupManager;
import com.alee.utils.SwingUtils;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * This manager allows you to display custom notification popups within the application.
 * You can also add custom actions, set their duration, modify popup styling and use some other advanced features.
 *
 * @author Mikle Garin
 * @see WebNotificationPopup
 * @see DisplayType
 */

public final class NotificationManager implements SwingConstants
{
    /**
     * Notifications display location.
     */
    private static int location = SOUTH_EAST;

    /**
     * Notifications display type.
     */
    private static DisplayType displayType = DisplayType.stack;

    /**
     * Notifications side margin.
     */
    private static Insets margin = new Insets ( 0, 0, 0, 0 );

    /**
     * Gap between notifications.
     */
    private static int gap = 10;

    /**
     * Whether popups should be cascaded or not.
     */
    private static boolean cascade = true;

    /**
     * Amount of cascaded in a row popups.
     */
    private static int cascadeAmount = 4;

    /**
     * Cached notification layouts.
     */
    private static Map<PopupLayer, NotificationsLayout> notificationsLayouts = new WeakHashMap<PopupLayer, NotificationsLayout> ();

    /**
     * Cached notifications.
     */
    private static Map<WebNotificationPopup, PopupLayer> notifications = new WeakHashMap<WebNotificationPopup, PopupLayer> ();

    /**
     * Returns notifications display location.
     *
     * @return notifications display location
     */
    public static int getLocation ()
    {
        return location;
    }

    /**
     * Sets notifications display location.
     *
     * @param location new notifications display location
     */
    public static void setLocation ( int location )
    {
        NotificationManager.location = location;
        updateNotificationLayouts ();
    }

    /**
     * Retyrns notification display type.
     *
     * @return notification display type
     */
    public static DisplayType getDisplayType ()
    {
        return displayType;
    }

    /**
     * Sets notification display type.
     *
     * @param displayType new notification display type
     */
    public static void setDisplayType ( final DisplayType displayType )
    {
        NotificationManager.displayType = displayType;
        updateNotificationLayouts ();
    }

    /**
     * Returns notifications side margin.
     *
     * @return notifications side margin
     */
    public static Insets getMargin ()
    {
        return margin;
    }

    /**
     * Sets notifications side margin.
     *
     * @param margin margin
     */
    public static void setMargin ( int margin )
    {
        setMargin ( margin, margin, margin, margin );
    }

    /**
     * Sets notifications side margin.
     *
     * @param top    top margin
     * @param left   left margin
     * @param bottom bottom margin
     * @param right  right margin
     */
    public static void setMargin ( int top, int left, int bottom, int right )
    {
        setMargin ( new Insets ( top, left, bottom, right ) );
    }

    /**
     * Sets notifications side margin.
     *
     * @param margin new notifications side margin
     */
    public static void setMargin ( Insets margin )
    {
        NotificationManager.margin = margin;
        updateNotificationLayouts ();
    }

    /**
     * Returns gap between notifications.
     *
     * @return gap between notifications
     */
    public static int getGap ()
    {
        return gap;
    }

    /**
     * Sets gap between notifications.
     *
     * @param gap new gap between notifications
     */
    public static void setGap ( final int gap )
    {
        NotificationManager.gap = gap;
        updateNotificationLayouts ();
    }

    /**
     * Returns whether popups should be cascaded or not.
     *
     * @return whether popups should be cascaded or not
     */
    public static boolean isCascade ()
    {
        return cascade;
    }

    /**
     * Sets whether popups should be cascaded or not.
     *
     * @param cascade whether popups should be cascaded or not
     */
    public static void setCascade ( final boolean cascade )
    {
        NotificationManager.cascade = cascade;
        updateNotificationLayouts ();
    }

    /**
     * Returns amount of cascaded in a row popups.
     *
     * @return amount of cascaded in a row popups
     */
    public static int getCascadeAmount ()
    {
        return cascadeAmount;
    }

    /**
     * Sets amount of cascaded in a row popups.
     *
     * @param cascadeAmount new amount of cascaded in a row popups
     */
    public static void setCascadeAmount ( int cascadeAmount )
    {
        NotificationManager.cascadeAmount = cascadeAmount;
        if ( NotificationManager.cascade )
        {
            updateNotificationLayouts ();
        }
    }

    /**
     * Optimized layout updates for all visible notifications.
     */
    public static void updateNotificationLayouts ()
    {
        final List<PopupLayer> layers = new ArrayList<PopupLayer> ();
        for ( Map.Entry<WebNotificationPopup, PopupLayer> entry : notifications.entrySet () )
        {
            final PopupLayer popupLayer = entry.getValue ();
            if ( !layers.contains ( popupLayer ) )
            {
                layers.add ( popupLayer );
                popupLayer.revalidate ();
            }
        }
    }

    /**
     * Hides all visible notifications.
     */
    public static void hideAllNotifications ()
    {
        for ( Map.Entry<WebNotificationPopup, PopupLayer> entry : notifications.entrySet () )
        {
            entry.getKey ().hidePopup ();
        }
    }

    /**
     * Returns displayed notification.
     *
     * @param content notification text content
     * @return displayed notification
     */
    public static WebNotificationPopup showNotification ( final String content )
    {
        return showNotification ( SwingUtils.getActiveWindow (), new WebLabel ( content ), NotificationIcon.information.getIcon () );
    }

    /**
     * Returns displayed notification.
     *
     * @param content notification text content
     * @param icon    notification icon
     * @return displayed notification
     */
    public static WebNotificationPopup showNotification ( final String content, final Icon icon )
    {
        return showNotification ( SwingUtils.getActiveWindow (), new WebLabel ( content ), icon );
    }

    /**
     * Returns displayed notification.
     *
     * @param showFor component used to determine window where notification should be displayed
     * @param content notification text content
     * @return displayed notification
     */
    public static WebNotificationPopup showNotification ( final Component showFor, final String content )
    {
        return showNotification ( showFor, new WebLabel ( content ), NotificationIcon.information.getIcon () );
    }

    /**
     * Returns displayed notification.
     *
     * @param showFor component used to determine window where notification should be displayed
     * @param content notification text content
     * @param icon    notification icon
     * @return displayed notification
     */
    public static WebNotificationPopup showNotification ( final Component showFor, final String content, final Icon icon )
    {
        return showNotification ( showFor, new WebLabel ( content ), icon );
    }

    /**
     * Returns displayed notification.
     *
     * @param content notification content
     * @return displayed notification
     */
    public static WebNotificationPopup showNotification ( final Component content )
    {
        return showNotification ( SwingUtils.getActiveWindow (), content, NotificationIcon.information.getIcon () );
    }

    /**
     * Returns displayed notification.
     *
     * @param content notification content
     * @param icon    notification icon
     * @return displayed notification
     */
    public static WebNotificationPopup showNotification ( final Component content, final Icon icon )
    {
        return showNotification ( SwingUtils.getActiveWindow (), content, icon );
    }

    /**
     * Returns displayed notification.
     *
     * @param showFor component used to determine window where notification should be displayed
     * @param content notification content
     * @return displayed notification
     */
    public static WebNotificationPopup showNotification ( final Component showFor, final Component content )
    {
        return showNotification ( showFor, content, NotificationIcon.information.getIcon () );
    }

    /**
     * Returns displayed notification.
     *
     * @param showFor component used to determine window where notification should be displayed
     * @param content notification content
     * @param icon    notification icon
     * @return displayed notification
     */
    public static WebNotificationPopup showNotification ( final Component showFor, final Component content, final Icon icon )
    {
        final WebNotificationPopup notificationPopup = new WebNotificationPopup ();
        notificationPopup.setIcon ( icon );
        notificationPopup.setContent ( content );
        return showNotification ( showFor, notificationPopup );
    }

    /**
     * Returns displayed notification.
     *
     * @param content notification text content
     * @param options notification selectable options
     * @return displayed notification
     */
    public static WebNotificationPopup showNotification ( final String content, NotificationOption... options )
    {
        return showNotification ( SwingUtils.getActiveWindow (), new WebLabel ( content ), NotificationIcon.information.getIcon (),
                options );
    }

    /**
     * Returns displayed notification.
     *
     * @param content notification text content
     * @param icon    notification icon
     * @param options notification selectable options
     * @return displayed notification
     */
    public static WebNotificationPopup showNotification ( final String content, final Icon icon, NotificationOption... options )
    {
        return showNotification ( SwingUtils.getActiveWindow (), new WebLabel ( content ), icon, options );
    }

    /**
     * Returns displayed notification.
     *
     * @param showFor component used to determine window where notification should be displayed
     * @param content notification text content
     * @param options notification selectable options
     * @return displayed notification
     */
    public static WebNotificationPopup showNotification ( final Component showFor, final String content, NotificationOption... options )
    {
        return showNotification ( showFor, new WebLabel ( content ), NotificationIcon.information.getIcon (), options );
    }

    /**
     * Returns displayed notification.
     *
     * @param showFor component used to determine window where notification should be displayed
     * @param content notification text content
     * @param icon    notification icon
     * @param options notification selectable options
     * @return displayed notification
     */
    public static WebNotificationPopup showNotification ( final Component showFor, final String content, final Icon icon,
                                                          NotificationOption... options )
    {
        return showNotification ( showFor, new WebLabel ( content ), icon, options );
    }

    /**
     * Returns displayed notification.
     *
     * @param content notification content
     * @param options notification selectable options
     * @return displayed notification
     */
    public static WebNotificationPopup showNotification ( final Component content, NotificationOption... options )
    {
        return showNotification ( SwingUtils.getActiveWindow (), content, NotificationIcon.information.getIcon (), options );
    }

    /**
     * Returns displayed notification.
     *
     * @param content notification content
     * @param icon    notification icon
     * @param options notification selectable options
     * @return displayed notification
     */
    public static WebNotificationPopup showNotification ( final Component content, final Icon icon, NotificationOption... options )
    {
        return showNotification ( SwingUtils.getActiveWindow (), content, icon, options );
    }

    /**
     * Returns displayed notification.
     *
     * @param showFor component used to determine window where notification should be displayed
     * @param content notification content
     * @param options notification selectable options
     * @return displayed notification
     */
    public static WebNotificationPopup showNotification ( final Component showFor, final Component content, NotificationOption... options )
    {
        return showNotification ( showFor, content, NotificationIcon.information.getIcon (), options );
    }

    /**
     * Returns displayed notification.
     *
     * @param showFor component used to determine window where notification should be displayed
     * @param content notification content
     * @param icon    notification icon
     * @param options notification selectable options
     * @return displayed notification
     */
    public static WebNotificationPopup showNotification ( final Component showFor, final Component content, final Icon icon,
                                                          NotificationOption... options )
    {
        final WebNotificationPopup notificationPopup = new WebNotificationPopup ();
        notificationPopup.setIcon ( icon );
        notificationPopup.setContent ( content );
        notificationPopup.setOptions ( options );
        return showNotification ( showFor, notificationPopup );
    }

    /**
     * Displays notification on popup layer.
     *
     * @param notification notification to display
     */
    public static WebNotificationPopup showNotification ( final WebNotificationPopup notification )
    {
        return showNotification ( SwingUtils.getActiveWindow (), notification );
    }

    /**
     * Displays notification on popup layer.
     *
     * @param showFor      component used to determine window where notification should be displayed
     * @param notification notification to display
     */
    public static WebNotificationPopup showNotification ( final Component showFor, final WebNotificationPopup notification )
    {
        // Adding custom layout into notifications
        final PopupLayer popupLayer = PopupManager.getPopupLayer ( showFor );
        if ( !notificationsLayouts.containsKey ( popupLayer ) )
        {
            final NotificationsLayout layout = new NotificationsLayout ();
            popupLayer.addLayoutManager ( layout );
            notificationsLayouts.put ( popupLayer, layout );
        }

        // Notifications caching
        notifications.put ( notification, popupLayer );
        notification.addPopupListener ( new PopupAdapter ()
        {
            @Override
            public void popupWillBeClosed ()
            {
                notifications.remove ( notification );
            }
        } );

        // Displaying popup
        notification.showPopup ( showFor );

        return notification;
    }
}