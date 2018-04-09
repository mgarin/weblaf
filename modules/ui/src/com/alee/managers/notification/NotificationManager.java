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

import com.alee.api.jdk.BiConsumer;
import com.alee.api.jdk.Function;
import com.alee.extended.label.WebStyledLabel;
import com.alee.extended.window.PopupAdapter;
import com.alee.managers.popup.PopupLayer;
import com.alee.managers.popup.PopupManager;
import com.alee.utils.CoreSwingUtils;
import com.alee.utils.SwingUtils;
import com.alee.utils.SystemUtils;
import com.alee.utils.swing.WeakComponentData;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This manager allows you to display custom notification popups within the application.
 * You can also add custom actions, set their duration, modify popup styling and use some other advanced features.
 *
 * @author Mikle Garin
 * @see PopupManager
 * @see WebNotification
 * @see WebInnerNotification
 * @see DisplayType
 */
public final class NotificationManager implements SwingConstants
{
    /**
     * Notifications display location.
     */
    private static int location = -1;

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
     * Whether notifications displayed in separate windows should avoid overlapping system toolbar or not.
     */
    private static boolean avoidOverlappingSystemToolbar = true;

    /**
     * Cached notification layouts.
     */
    private static final WeakComponentData<PopupLayer, NotificationsLayout> notificationsLayouts =
            new WeakComponentData<PopupLayer, NotificationsLayout> ( "NotificationManager.NotificationsLayout", 2 );

    /**
     * Cached notification popups.
     */
    private static final WeakComponentData<WebInnerNotification, PopupLayer> notificationPopups =
            new WeakComponentData<WebInnerNotification, PopupLayer> ( "NotificationManager.PopupLayer", 5 );

    /**
     * Special layouts for notification windows.
     * Each layout is cached under according graphics device it is used with.
     */
    private static final Map<GraphicsDevice, NotificationsScreenLayout> screenLayouts =
            new HashMap<GraphicsDevice, NotificationsScreenLayout> ( 1 );

    /**
     * Cached notification windows.
     */
    private static final WeakComponentData<WebNotification, Window> notificationWindows =
            new WeakComponentData<WebNotification, Window> ( "NotificationManager.Window", 5 );

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
        notificationPopups.forEach ( new BiConsumer<WebInnerNotification, PopupLayer> ()
        {
            @Override
            public void accept ( final WebInnerNotification webInnerNotification, final PopupLayer popupLayer )
            {
                if ( !layers.contains ( popupLayer ) )
                {
                    layers.add ( popupLayer );
                    popupLayer.revalidate ();
                }
            }
        } );

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
        notificationPopups.forEach ( new BiConsumer<WebInnerNotification, PopupLayer> ()
        {
            @Override
            public void accept ( final WebInnerNotification webInnerNotification, final PopupLayer popupLayer )
            {
                webInnerNotification.hidePopup ();
            }
        } );
        notificationWindows.forEach ( new BiConsumer<WebNotification, Window> ()
        {
            @Override
            public void accept ( final WebNotification webNotification, final Window window )
            {
                webNotification.hidePopup ();
            }
        } );
    }

    /**
     * Returns displayed notification.
     *
     * @param content notification text content
     * @return displayed notification
     */
    public static WebNotification showNotification ( final String content )
    {
        return showNotification ( null, new WebStyledLabel ( content ), NotificationIcon.information.getIcon () );
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
        return showNotification ( null, new WebStyledLabel ( content ), icon );
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
        return showNotification ( showFor, new WebStyledLabel ( content ), NotificationIcon.information.getIcon () );
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
        return showNotification ( showFor, new WebStyledLabel ( content ), icon );
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
        return showNotification ( null, new WebStyledLabel ( content ), NotificationIcon.information.getIcon (), options );
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
        return showNotification ( null, new WebStyledLabel ( content ), icon, options );
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
        return showNotification ( showFor, new WebStyledLabel ( content ), NotificationIcon.information.getIcon (), options );
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
        return showNotification ( showFor, new WebStyledLabel ( content ), icon, options );
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
        notificationWindows.set ( notification, CoreSwingUtils.getWindowAncestor ( showFor ) );
        notification.addPopupListener ( new PopupAdapter ()
        {
            @Override
            public void popupWillBeClosed ()
            {
                notificationWindows.clear ( notification );
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
        final Window window = showFor != null && showFor.isShowing () ? CoreSwingUtils.getWindowAncestor ( showFor ) : null;
        final GraphicsDevice gd = SystemUtils.getGraphicsDevice ( window );
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
        return showInnerNotification ( SwingUtils.getAvailableWindow (), new WebStyledLabel ( content ),
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
        return showInnerNotification ( SwingUtils.getAvailableWindow (), new WebStyledLabel ( content ), icon );
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
        return showInnerNotification ( showFor, new WebStyledLabel ( content ), NotificationIcon.information.getIcon () );
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
        return showInnerNotification ( showFor, new WebStyledLabel ( content ), icon );
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
        return showInnerNotification ( SwingUtils.getAvailableWindow (), new WebStyledLabel ( content ),
                NotificationIcon.information.getIcon (), options );
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
        return showInnerNotification ( SwingUtils.getAvailableWindow (), new WebStyledLabel ( content ), icon, options );
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
        return showInnerNotification ( showFor, new WebStyledLabel ( content ), NotificationIcon.information.getIcon (), options );
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
        return showInnerNotification ( showFor, new WebStyledLabel ( content ), icon, options );
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
        notificationsLayouts.get ( popupLayer, new Function<PopupLayer, NotificationsLayout> ()
        {
            @Override
            public NotificationsLayout apply ( final PopupLayer popupLayer )
            {
                final NotificationsLayout layout = new NotificationsLayout ();
                popupLayer.addLayoutManager ( layout );
                return layout;
            }
        } );

        // Notifications caching
        notificationPopups.set ( notification, popupLayer );
        notification.addPopupListener ( new PopupAdapter ()
        {
            @Override
            public void popupWillBeClosed ()
            {
                notificationPopups.clear ( notification );
                notification.removePopupListener ( this );
            }
        } );

        // Displaying popup
        notification.showPopup ( showFor );

        return notification;
    }
}