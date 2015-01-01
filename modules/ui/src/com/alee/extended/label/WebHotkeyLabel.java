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

package com.alee.extended.label;

import com.alee.laf.label.WebLabel;
import com.alee.managers.hotkey.HotkeyData;

import java.awt.event.KeyEvent;

/**
 * This class provides a quick access to hotkey-styled label with additional costructors for quick hotkey text initialization.
 * This label is only a hotkey visualization and have nothing to do with real hotkeys.
 * To modify actual hotkeys use HotkeyManager or key listeners.
 *
 * @author Mikle Garin
 */

public class WebHotkeyLabel extends WebLabel
{
    public WebHotkeyLabel ()
    {
        super ();
        initializeView ();
    }

    public WebHotkeyLabel ( final int keyCode )
    {
        super ( KeyEvent.getKeyText ( keyCode ) );
        initializeView ();
    }

    public WebHotkeyLabel ( final int keyCode, final int modifiers )
    {
        super ( KeyEvent.getKeyModifiersText ( modifiers ) + "+" + KeyEvent.getKeyText ( keyCode ) );
        initializeView ();
    }

    public WebHotkeyLabel ( final HotkeyData hotkeyData )
    {
        super ( hotkeyData.toString () );
        initializeView ();
    }

    public WebHotkeyLabel ( final String text )
    {
        super ( text );
        initializeView ();
    }

    private void initializeView ()
    {
        setStyleId ( "hotkey-label" );
    }
}