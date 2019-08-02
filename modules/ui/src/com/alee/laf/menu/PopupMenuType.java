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

package com.alee.laf.menu;

/**
 * This enumeration represents various popup menu types.
 *
 * @author Mikle Garin
 */
public enum PopupMenuType
{
    /**
     * Popup menu displayed directly from the menubar.
     * In default styling such menus have a dropdown corner.
     */
    menuBarMenu,

    /**
     * Popup menu displayed as a sub menu from another menu in menubar.
     * In default styling such menus doesn't have dropdown corner.
     */
    menuBarSubMenu,

    /**
     * Popup menu displayed under a combobox.
     * In default styling such menus have a dropdown corner.
     */
    comboBoxMenu,

    /**
     * Custom user popup menu that can be opened anywhere.
     * In default styling such menus have a dropdown corner.
     */
    customPopupMenu
}