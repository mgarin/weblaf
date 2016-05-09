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
import java.util.EnumMap;
import java.util.Map;

/**
 * This enumeration represents available predefined notification icons.
 *
 * @author Mikle Garin
 * @see NotificationManager
 */

public enum NotificationIcon
{
    /**
     * Information icon.
     */
    information,

    /**
     * Warning icon.
     */
    warning,

    /**
     * Error icon.
     */
    error,

    /**
     * Question icon.
     */
    question,

    /**
     * Plus icon.
     */
    plus,

    /**
     * Cross icon.
     */
    cross,

    /**
     * Minus icon.
     */
    minus,

    /**
     * Tip icon.
     */
    tip,

    /**
     * Image icon.
     */
    image,

    /**
     * Application icon.
     */
    application,

    /**
     * File icon.
     */
    file,

    /**
     * Horizontal file icon.
     */
    fileHor,

    /**
     * Folder icon.
     */
    folder,

    /**
     * Horizontal folder icon.
     */
    folderHor,

    /**
     * Calendar icon.
     */
    calendar,

    /**
     * Text icon.
     */
    text,

    /**
     * Mail icon.
     */
    mail,

    /**
     * Color icon.
     */
    color,

    /**
     * Database icon.
     */
    database,

    /**
     * Clock icon.
     */
    clock,

    /**
     * Film icon.
     */
    film,

    /**
     * Keyboard button icon.
     */
    keyboardButton,

    /**
     * Table icon.
     */
    table,

    /**
     * Map icon.
     */
    map;

    /**
     * Map of cached icons.
     * Icon is created only when used first time, there is no point to load it before that moment - that will be memory waste.
     */
    private static final Map<NotificationIcon, ImageIcon> iconsCache = new EnumMap<NotificationIcon, ImageIcon> ( NotificationIcon.class );

    /**
     * Returns cached icon for this notification type.
     *
     * @return cached icon for this notification type
     */
    public ImageIcon getIcon ()
    {
        if ( iconsCache.containsKey ( this ) )
        {
            return iconsCache.get ( this );
        }
        else
        {
            final ImageIcon icon = new ImageIcon ( NotificationIcon.class.getResource ( "icons/types/" + this + ".png" ) );
            iconsCache.put ( this, icon );
            return icon;
        }
    }
}