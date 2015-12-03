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

import com.alee.laf.label.WebLabel;
import com.alee.managers.popup.PopupAdapter;
import com.alee.managers.popup.PopupLayer;
import com.alee.managers.popup.PopupManager;
import com.alee.utils.SwingUtils;
import com.alee.utils.SystemUtils;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * This manager allows you to display custom notification popups within the application.
 * You can also add custom actions, set their duration, modify popup styling and use some other advanced features.
 *
 * @author Mikle Garin
 * @see WebInnerNotification
 * @see DisplayType
 */

public final class NotificationManager implements SwingConstants
{
    /**
     * Notifications display location.
     */
    protected static int location = -1;

    /**
     * Notifications display type.
     */
    protected static DisplayType displayType = DisplayType.stack;

    /**
     * Notifications side margin.
     */
    protected static Insets margin = new Insets ( 0, 0, 0, 0 );

    /**
     * Gap between notifications.
     */
    protected static int gap = 10;

    /**
     * Whether popups should be cascaded or not.
     */
    protected static boolean cascade = true;

    /**
     * Amount of cascaded in a row popups.
     */
    protected static int cascadeAmount = 4;

    /**
     * Whether notifications displayed in separate windows should avoid overlapping system toolbar or not.
     */
    protected static boolean avoidOverlappingSystemToolbar = true;

    /**
     * Cached notification layouts.
     */
    protected static final Map<PopupLayer, NotificationsLayout> notificationsLayouts = new WeakHashMap<PopupLayer, NotificationsLayout> ();

    /**
     * Cached notification popups.
     */
    protected static final Map<WebInnerNotification, PopupLayer> notificationPopups = new WeakHashMap<WebInnerNotification, PopupLayer> ();

    /**
     * Special layouts for notification windows.
     * Each layout is cached under according graphics device it is used with.
     */
    protected static final Map<GraphicsDevice, NotificationsScreenLayout> screenLayouts =
            new HashMap<GraphicsDevice, NotificationsScreenLayout> ( 1 );

    /**
     * Cached notification windows.
     */
    protected static final Map<WebNotification, Window> notificationWindows = new WeakHashMap<WebNotification, Window> ();

    /**
     * Returns notifications display location.
     *
     * @return notifications display location
     */
    public static int getLocation ()
    {
        return location != -1 ? location : SystemUtils.isWindows () ? SOUTH_EAST : NORTH_EAST;
    }

    /**
     * Sets notifications display location.
     *
     * @param location new notifications display location
     */
    public static void setLocation ( final int location )
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
    public static void setMargin ( final int margin )
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
    public static void setMargin ( final int top, final int left, final int bottom, final int right )
    {
        setMargin ( new Insets ( top, left, bottom, right ) );
    }

    /**
     * Sets notifications side margin.
     *
     * @param margin new notifications side margin
     */
    public static void setMargin ( final Insets margin )
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
    public static void setCascadeAmount ( final int cascadeAmount )
    {
        NotificationManager.cascadeAmount = cascadeAmount;
        if ( NotificationManager.cascade )
        {
            updateNotificationLayouts ();
        }
    }

    /**
     * Returns whether notifications displayed in separate windows should avoid overlapping system toolbar or not.
     *
     * @return true if notifications displayed in separate windows should avoid overlapping system toolbar, false otherwise
     */
    public static boolean isAvoidOverlappingSystemToolbar ()
    {
        return avoidOverlappingSystemToolbar;
    }

    /**
     * Sets whether notifications displayed in separate windows should avoid overlapping system toolbar or not.
     *
     * @param avoid whether notifications displayed in separate windows should avoid overlapping system toolbar or not
     */
    public static void setAvoidOverlappingSystemToolbar ( final boolean avoid )
    {
        NotificationManager.avoidOverlappingSystemToolbar = avoid;
    }

    /**
     * Optimized layout updates for all visible notifications.
     */
    public static void updateNotificationLayouts ()
    {
        // Updating popup notifications layouts
        final List<PopupLayer> layers = new ArrayList<PopupLayer> ();
        for ( final Map.Entry<WebInnerNotification, PopupLayer> entry : notificationPopups.entrySet () )
        {
            final PopupLayer popupLayer = entry.getValue ();
            if ( !layers.contains ( popupLayer ) )
            {
                layers.add ( popupLayer );
                popupLayer.revalidate ();
            }
        }

        // Updating window notification layouts
        for ( final Map.Entry<GraphicsDevice, NotificationsScreenLayout> entry : screenLayouts.entrySet () )
        {
            // Updating device-specific layout
            entry.getValue ().layoutScreen ();
        }
    }

    /**
     * Hides all visible notifications.
     */
    public static void hideAllNotifications ()
    {
        for ( final Map.Entry<WebInnerNotification, PopupLayer> entry : notificationPopups.entrySet () )
        {
            entry.getKey ().hidePopup ();
        }
        for ( final Map.Entry<WebNotification, Window> entry : notificationWindows.entrySet () )
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
    public static WebNotification showNotification ( final String content )
    {
        return showNotification ( null, new WebLabel ( content ), NotificationIcon.information.getIcon () );
    }

    /**
     * Returns displayed notification.
     *
     * @param content notification text content
     * @param icon    notification icon
     * @return displayed notification
     */
    public static WebNotification showNotification ( final String content, final Icon icon )
    {
        return showNotification ( null, new WebLabel ( content ), icon );
    }

    /**
     * Returns displayed notification.
     *
     * @param showFor component used to determine notification parent window
     * @param content notification text content
     * @return displayed notification
     */
    public static WebNotification showNotification ( final Component showFor, final String content )
    {
        return showNotification ( showFor, new WebLabel ( content ), NotificationIcon.information.getIcon () );
    }

    /**
     * Returns displayed notification.
     *
     * @param showFor component used to determine notification parent window
     * @param content notification text content
     * @param icon    notification icon
     * @return displayed notification
     */
    public static WebNotification showNotification ( final Component showFor, final String content, final Icon icon )
    {
        return showNotification ( showFor, new WebLabel ( content ), icon );
    }

    /**
     * Returns displayed notification.
     *
     * @param content notification content
     * @return displayed notification
     */
    public static WebNotification showNotification ( final Component content )
    {
        return showNotification ( null, content, NotificationIcon.information.getIcon () );
    }

    /**
     * Returns displayed notification.
     *
     * @param content notification content
     * @param icon    notification icon
     * @return displayed notification
     */
    public static WebNotification showNotification ( final Component content, final Icon icon )
    {
        return showNotification ( null, content, icon );
    }

    /**
     * Returns displayed notification.
     *
     * @param showFor component used to determine notification parent window
     * @param content notification content
     * @return displayed notification
     */
    public static WebNotification showNotification ( final Component showFor, final Component content )
    {
        return showNotification ( showFor, content, NotificationIcon.information.getIcon () );
    }

    /**
     * Returns displayed notification.
     *
     * @param showFor component used to determine notification parent window
     * @param content notification content
     * @param icon    notification icon
     * @return displayed notification
     */
    public static WebNotification showNotification ( final Component showFor, final Component content, final Icon icon )
    {
        final WebNotification notificationWindow = new WebNotification ();
        notificationWindow.setIcon ( icon );
        notificationWindow.setContent ( content );
        return showNotification ( showFor, notificationWindow );
    }

    /**
     * Returns displayed notification.
     *
     * @param content notification text content
     * @param options notification selectable options
     * @return displayed notification
     */
    public static WebNotification showNotification ( final String content, final NotificationOption... options )
    {
        return showNotification ( null, new WebLabel ( content ), NotificationIcon.information.getIcon (), options );
    }

    /**
     * Returns displayed notification.
     *
     * @param content notification text content
     * @param icon    notification icon
     * @param options notification selectable options
     * @return displayed notification
     */
    public static WebNotification showNotification ( final String content, final Icon icon, final NotificationOption... options )
    {
        return showNotification ( null, new WebLabel ( content ), icon, options );
    }

    /**
     * Returns displayed notification.
     *
     * @param showFor component used to determine notification parent window
     * @param content notification text content
     * @param options notification selectable options
     * @return displayed notification
     */
    public static WebNotification showNotification ( final Component showFor, final String content, final NotificationOption... options )
    {
        return showNotification ( showFor, new WebLabel ( content ), NotificationIcon.information.getIcon (), options );
    }

    /**
     * Returns displayed notification.
     *
     * @param showFor component used to determine notification parent window
     * @param content notification text content
     * @param icon    notification icon
     * @param options notification selectable options
     * @return displayed notification
     */
    public static WebNotification showNotification ( final Component showFor, final String content, final Icon icon,
                                                     final NotificationOption... options )
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
    public static WebNotification showNotification ( final Component content, final NotificationOption... options )
    {
        return showNotification ( null, content, NotificationIcon.information.getIcon (), options );
    }

    /**
     * Returns displayed notification.
     *
     * @param content notification content
     * @param icon    notification icon
     * @param options notification selectable options
     * @return displayed notification
     */
    public static WebNotification showNotification ( final Component content, final Icon icon, final NotificationOption... options )
    {
        return showNotification ( null, content, icon, options );
    }

    /**
     * Returns displayed notification.
     *
     * @param showFor component used to determine notification parent window
     * @param content notification content
     * @param options notification selectable options
     * @return displayed notification
     */
    public static WebNotification showNotification ( final Component showFor, final Component content, final NotificationOption... options )
    {
        return showNotification ( showFor, content, NotificationIcon.information.getIcon (), options );
    }

    /**
     * Returns displayed notification.
     *
     * @param showFor component used to determine notification parent window
     * @param content notification content
     * @param icon    notification icon
     * @param options notification selectable options
     * @return displayed notification
     */
    public static WebNotification showNotification ( final Component showFor, final Component content, final Icon icon,
                                                     final NotificationOption... options )
    {
        final WebNotification notificationPopup = new WebNotification ();
        notificationPopup.setIcon ( icon );
        notificationPopup.setContent ( content );
        notificationPopup.setOptions ( options );
        return showNotification ( showFor, notificationPopup );
    }

    /**
     * Displays notification in window.
     *
     * @param notification notification to display
     * @return displayed notification
     */
    public static WebNotification showNotification ( final WebNotification notification )
    {
        return showNotification ( null, notification );
    }

    /**
     * Displays notification in window.
     *
     * @param showFor      component used to determine notification parent window
     * @param notification notification to display
     * @return displayed notification
     */
    public static WebNotification showNotification ( final Component showFor, final WebNotification notification )
    {
        // Retrieving screen layout for the specified component
        final NotificationsScreenLayout layout = getLayout ( showFor );

        // Notifications caching
        notificationWindows.put ( notification, SwingUtils.getWindowAncestor ( showFor ) );
        notification.addPopupListener ( new PopupAdapter ()
        {
            @Override
            public void popupWillBeClosed ()
            {
                notificationWindows.remove ( notification );
                layout.removeWindow ( notification.getWindow () );
                notification.removePopupListener ( this );
            }
        } );

        // Displaying popup
        notification.showPopup ( showFor, 0, 0 );

        // Adding notification into screen layout
        layout.addWindow ( notification.getWindow () );

        return notification;
    }

    /**
     * Returns screen layout for the specified component.
     *
     * @param showFor component used to determine screen
     * @return screen layout for the specified component
     */
    private static NotificationsScreenLayout getLayout ( final Component showFor )
    {
        final Window window = showFor != null && showFor.isShowing () ? SwingUtils.getWindowAncestor ( showFor ) : null;
        final GraphicsDevice gd = window != null ? window.getGraphicsConfiguration ().getDevice () :
                GraphicsEnvironment.getLocalGraphicsEnvironment ().getDefaultScreenDevice ();
        if ( !screenLayouts.containsKey ( gd ) )
        {
            screenLayouts.put ( gd, new NotificationsScreenLayout ( gd ) );
        }
        return screenLayouts.get ( gd );
    }

    /**
     * Returns displayed notification.
     *
     * @param content notification text content
     * @return displayed notification
     */
    public static WebInnerNotification showInnerNotification ( final String content )
    {
        return showInnerNotification ( SwingUtils.getAvailableWindow (), new WebLabel ( content ),
                NotificationIcon.information.getIcon () );
    }

    /**
     * Returns displayed notification.
     *
     * @param content notification text content
     * @param icon    notification icon
     * @return displayed notification
     */
    public static WebInnerNotification showInnerNotification ( final String content, final Icon icon )
    {
        return showInnerNotification ( SwingUtils.getAvailableWindow (), new WebLabel ( content ), icon );
    }

    /**
     * Returns displayed notification.
     *
     * @param showFor component used to determine window where notification should be displayed
     * @param content notification text content
     * @return displayed notification
     */
    public static WebInnerNotification showInnerNotification ( final Component showFor, final String content )
    {
        return showInnerNotification ( showFor, new WebLabel ( content ), NotificationIcon.information.getIcon () );
    }

    /**
     * Returns displayed notification.
     *
     * @param showFor component used to determine window where notification should be displayed
     * @param content notification text content
     * @param icon    notification icon
     * @return displayed notification
     */
    public static WebInnerNotification showInnerNotification ( final Component showFor, final String content, final Icon icon )
    {
        return showInnerNotification ( showFor, new WebLabel ( content ), icon );
    }

    /**
     * Returns displayed notification.
     *
     * @param content notification content
     * @return displayed notification
     */
    public static WebInnerNotification showInnerNotification ( final Component content )
    {
        return showInnerNotification ( SwingUtils.getAvailableWindow (), content, NotificationIcon.information.getIcon () );
    }

    /**
     * Returns displayed notification.
     *
     * @param content notification content
     * @param icon    notification icon
     * @return displayed notification
     */
    public static WebInnerNotification showInnerNotification ( final Component content, final Icon icon )
    {
        return showInnerNotification ( SwingUtils.getAvailableWindow (), content, icon );
    }

    /**
     * Returns displayed notification.
     *
     * @param showFor component used to determine window where notification should be displayed
     * @param content notification content
     * @return displayed notification
     */
    public static WebInnerNotification showInnerNotification ( final Component showFor, final Component content )
    {
        return showInnerNotification ( showFor, content, NotificationIcon.information.getIcon () );
    }

    /**
     * Returns displayed notification.
     *
     * @param showFor component used to determine window where notification should be displayed
     * @param content notification content
     * @param icon    notification icon
     * @return displayed notification
     */
    public static WebInnerNotification showInnerNotification ( final Component showFor, final Component content, final Icon icon )
    {
        final WebInnerNotification notificationPopup = new WebInnerNotification ();
        notificationPopup.setIcon ( icon );
        notificationPopup.setContent ( content );
        return showInnerNotification ( showFor, notificationPopup );
    }

    /**
     * Returns displayed notification.
     *
     * @param content notification text content
     * @param options notification selectable options
     * @return displayed notification
     */
    public static WebInnerNotification showInnerNotification ( final String content, final NotificationOption... options )
    {
        return showInnerNotification ( SwingUtils.getAvailableWindow (), new WebLabel ( content ), NotificationIcon.information.getIcon (),
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
    public static WebInnerNotification showInnerNotification ( final String content, final Icon icon, final NotificationOption... options )
    {
        return showInnerNotification ( SwingUtils.getAvailableWindow (), new WebLabel ( content ), icon, options );
    }

    /**
     * Returns displayed notification.
     *
     * @param showFor component used to determine window where notification should be displayed
     * @param content notification text content
     * @param options notification selectable options
     * @return displayed notification
     */
    public static WebInnerNotification showInnerNotification ( final Component showFor, final String content,
                                                               final NotificationOption... options )
    {
        return showInnerNotification ( showFor, new WebLabel ( content ), NotificationIcon.information.getIcon (), options );
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
    public static WebInnerNotification showInnerNotification ( final Component showFor, final String content, final Icon icon,
                                                               final NotificationOption... options )
    {
        return showInnerNotification ( showFor, new WebLabel ( content ), icon, options );
    }

    /**
     * Returns displayed notification.
     *
     * @param content notification content
     * @param options notification selectable options
     * @return displayed notification
     */
    public static WebInnerNotification showInnerNotification ( final Component content, final NotificationOption... options )
    {
        return showInnerNotification ( SwingUtils.getAvailableWindow (), content, NotificationIcon.information.getIcon (), options );
    }

    /**
     * Returns displayed notification.
     *
     * @param content notification content
     * @param icon    notification icon
     * @param options notification selectable options
     * @return displayed notification
     */
    public static WebInnerNotification showInnerNotification ( final Component content, final Icon icon,
                                                               final NotificationOption... options )
    {
        return showInnerNotification ( SwingUtils.getAvailableWindow (), content, icon, options );
    }

    /**
     * Returns displayed notification.
     *
     * @param showFor component used to determine window where notification should be displayed
     * @param content notification content
     * @param options notification selectable options
     * @return displayed notification
     */
    public static WebInnerNotification showInnerNotification ( final Component showFor, final Component content,
                                                               final NotificationOption... options )
    {
        return showInnerNotification ( showFor, content, NotificationIcon.information.getIcon (), options );
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
    public static WebInnerNotification showInnerNotification ( final Component showFor, final Component content, final Icon icon,
                                                               final NotificationOption... options )
    {
        final WebInnerNotification notificationPopup = new WebInnerNotification ();
        notificationPopup.setIcon ( icon );
        notificationPopup.setContent ( content );
        notificationPopup.setOptions ( options );
        return showInnerNotification ( showFor, notificationPopup );
    }

    /**
     * Displays notification on popup layer.
     *
     * @param notification notification to display
     * @return displayed notification
     */
    public static WebInnerNotification showInnerNotification ( final WebInnerNotification notification )
    {
        return showInnerNotification ( SwingUtils.getAvailableWindow (), notification );
    }

    /**
     * Displays notification on popup layer.
     *
     * @param showFor      component used to determine window where notification should be displayed
     * @param notification notification to display
     * @return displayed notification
     */
    public static WebInnerNotification showInnerNotification ( final Component showFor, final WebInnerNotification notification )
    {
        // Do not allow notification display without window
        if ( showFor == null )
        {
            throw new RuntimeException ( "There is no visible windows to display inner notification!" );
        }

        // Adding custom layout into notifications
        final PopupLayer popupLayer = PopupManager.getPopupLayer ( showFor );
        if ( !notificationsLayouts.containsKey ( popupLayer ) )
        {
            final NotificationsLayout layout = new NotificationsLayout ();
            popupLayer.addLayoutManager ( layout );
            notificationsLayouts.put ( popupLayer, layout );
        }

        // Notifications caching
        notificationPopups.put ( notification, popupLayer );
        notification.addPopupListener ( new PopupAdapter ()
        {
            @Override
            public void popupWillBeClosed ()
            {
                notificationPopups.remove ( notification );
            }
        } );

        // Displaying popup
        notification.showPopup ( showFor );

        return notification;
    }
}