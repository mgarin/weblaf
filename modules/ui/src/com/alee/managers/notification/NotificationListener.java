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

/**
 * Special listener for WebNotificationPopup component.
 *
 * @author Mikle Garin
 * @see WebInnerNotification
 */

public interface NotificationListener
{
    /**
     * Notifies that user has selected the specified option.
     * This a separate from accepted/closed method that will be called only if some options list was set into notification popup.
     * Popup may still be closed without selecting any options if its settings allows that.
     *
     * @param option selected option
     */
    public void optionSelected ( NotificationOption option );

    /**
     * Notifies that user accepted notification.
     * Any action that waited for this may now be performed.
     */
    public void accepted ();

    /**
     * Notifies that popup was simply closed by user or delayed close action.
     * That means that any action that aways user's decision should be delayed.
     */
    public void closed ();
}