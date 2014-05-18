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

package com.alee.utils.swing.menu;

import com.alee.laf.menu.WebCheckBoxMenuItem;
import com.alee.laf.menu.WebMenuItem;
import com.alee.laf.menu.WebPopupMenu;
import com.alee.laf.menu.WebRadioButtonMenuItem;
import com.alee.managers.hotkey.HotkeyData;

import java.awt.event.ActionListener;

/**
 * Special generator that simplifies and shortens popup menu creation code.
 *
 * @author Mikle Garin
 */

public class PopupMenuGenerator extends AbstractMenuGenerator
{
    protected final WebPopupMenu popupMenu;

    public PopupMenuGenerator ()
    {
        this ( ( String ) null );
    }

    public PopupMenuGenerator ( final String styleId )
    {
        super ();
        this.popupMenu = new WebPopupMenu ( styleId );
    }

    public PopupMenuGenerator ( final WebPopupMenu popupMenu )
    {
        super ();
        this.popupMenu = popupMenu;
    }

    public void addSeparator ()
    {
        popupMenu.addSeparator ();
    }

    public WebMenuItem addItem ( final String text, final ActionListener actionListener )
    {
        return addItem ( text, ( HotkeyData ) null, actionListener );
    }

    public WebMenuItem addItem ( final String text, final HotkeyData hotkey, final ActionListener actionListener )
    {
        return addItem ( null, text, hotkey, actionListener );
    }

    public WebMenuItem addItem ( final String icon, final String text, final ActionListener actionListener )
    {
        return addItem ( icon, text, null, actionListener );
    }

    public WebMenuItem addItem ( final String icon, final String text, final HotkeyData hotkey, final ActionListener actionListener )
    {
        return addItem ( icon, text, hotkey, true, actionListener );
    }

    public WebMenuItem addItem ( final String icon, final String text, final boolean enabled, final ActionListener actionListener )
    {
        return addItem ( icon, text, null, enabled, actionListener );
    }

    public WebMenuItem addItem ( final String text, final boolean enabled, final ActionListener actionListener )
    {
        return addItem ( text, ( HotkeyData ) null, enabled, actionListener );
    }

    public WebMenuItem addItem ( final String text, final HotkeyData hotkey, final boolean enabled, final ActionListener actionListener )
    {
        return addItem ( null, text, hotkey, enabled, actionListener );
    }

    public WebMenuItem addItem ( final String icon, final String text, final HotkeyData hotkey, final boolean enabled,
                                 final ActionListener actionListener )
    {
        final WebMenuItem item = createItem ( icon, text, hotkey, enabled, actionListener );
        popupMenu.add ( item );
        return item;
    }

    public WebCheckBoxMenuItem addCheckItem ( final String icon, final String text, final HotkeyData hotkey, final boolean enabled,
                                              final boolean selected, final ActionListener actionListener )
    {
        final WebCheckBoxMenuItem item = createCheckBoxItem ( icon, text, hotkey, enabled, selected, actionListener );
        popupMenu.add ( item );
        return item;
    }

    public WebRadioButtonMenuItem addRadioItem ( final String icon, final String text, final HotkeyData hotkey, final boolean enabled,
                                                 final boolean selected, final ActionListener actionListener )
    {
        final WebRadioButtonMenuItem item = createRadioButtonItem ( icon, text, hotkey, enabled, selected, actionListener );
        popupMenu.add ( item );
        return item;
    }

    public WebPopupMenu getPopupMenu ()
    {
        return popupMenu;
    }
}