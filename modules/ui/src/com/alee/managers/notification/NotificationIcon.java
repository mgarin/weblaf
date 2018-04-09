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

import com.alee.utils.swing.EnumLazyIconProvider;

import javax.swing.*;

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
     * Returns cached icon for this notification type.
     *
     * @return cached icon for this notification type
     */
    public ImageIcon getIcon ()
    {
        return EnumLazyIconProvider.getIcon ( this, "icons/types/" );
    }
}