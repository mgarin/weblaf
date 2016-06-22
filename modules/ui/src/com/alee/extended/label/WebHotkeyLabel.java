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

import com.alee.managers.hotkey.HotkeyData;
import com.alee.managers.style.StyleId;

import java.awt.event.KeyEvent;

/**
 * This class provides a quick access to hotkey-styled label with additional constructors for quick hotkey text initialization.
 * This label is only a hotkey visualization and have nothing to do with real hotkeys.
 * To modify actual hotkeys use HotkeyManager or key listeners.
 *
 * @author Mikle Garin
 */

public class WebHotkeyLabel extends WebStyledLabel
{
    /**
     * Constructs empty hotkey label.
     */
    public WebHotkeyLabel ()
    {
        super ( StyleId.hotkeylabel );
    }

    /**
     * Constructs empty hotkey label.
     *
     * @param id style ID
     */
    public WebHotkeyLabel ( final StyleId id )
    {
        super ( id );
    }

    /**
     * Constructs hotkey label for the specified single key code.
     *
     * @param keyCode single key code
     */
    public WebHotkeyLabel ( final int keyCode )
    {
        super ( StyleId.hotkeylabel, KeyEvent.getKeyText ( keyCode ) );
    }

    /**
     * Constructs hotkey label for the specified single key code.
     *
     * @param id      style ID
     * @param keyCode single key code
     */
    public WebHotkeyLabel ( final StyleId id, final int keyCode )
    {
        super ( id, KeyEvent.getKeyText ( keyCode ) );
    }

    /**
     * Constructs hotkey label for the specified key code and modifiers.
     *
     * @param keyCode   single key code
     * @param modifiers hotkey modifiers
     */
    public WebHotkeyLabel ( final int keyCode, final int modifiers )
    {
        super ( StyleId.hotkeylabel, KeyEvent.getKeyModifiersText ( modifiers ) + "+" + KeyEvent.getKeyText ( keyCode ) );
    }

    /**
     * Constructs hotkey label for the specified key code and modifiers.
     *
     * @param id        style ID
     * @param keyCode   single key code
     * @param modifiers hotkey modifiers
     */
    public WebHotkeyLabel ( final StyleId id, final int keyCode, final int modifiers )
    {
        super ( id, KeyEvent.getKeyModifiersText ( modifiers ) + "+" + KeyEvent.getKeyText ( keyCode ) );
    }

    /**
     * Constructs hotkey label for the specified hotkey data.
     *
     * @param hotkeyData hotkey data
     */
    public WebHotkeyLabel ( final HotkeyData hotkeyData )
    {
        super ( StyleId.hotkeylabel, hotkeyData.toString () );
    }

    /**
     * Constructs hotkey label for the specified hotkey data.
     *
     * @param id         style ID
     * @param hotkeyData hotkey data
     */
    public WebHotkeyLabel ( final StyleId id, final HotkeyData hotkeyData )
    {
        super ( id, hotkeyData.toString () );
    }

    /**
     * Constructs hotkey label for the directly specified hotkey text.
     *
     * @param text hotkey text
     */
    public WebHotkeyLabel ( final String text )
    {
        super ( StyleId.hotkeylabel, text );
    }

    /**
     * Constructs hotkey label for the directly specified hotkey text.
     *
     * @param id   style ID
     * @param text hotkey text
     */
    public WebHotkeyLabel ( final StyleId id, final String text )
    {
        super ( id, text );
    }
}