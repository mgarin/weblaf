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

package com.alee.examples.groups.label;

import com.alee.examples.WebLookAndFeelDemo;
import com.alee.examples.content.DefaultExample;
import com.alee.extended.label.WebHotkeyLabel;
import com.alee.extended.panel.GroupPanel;
import com.alee.managers.hotkey.Hotkey;

import java.awt.*;
import java.awt.event.KeyEvent;

/**
 * User: mgarin Date: 16.05.12 Time: 12:20
 */

public class HotkeyLabelsExample extends DefaultExample
{
    @Override
    public String getTitle ()
    {
        return "Hotkey labels";
    }

    @Override
    public String getDescription ()
    {
        return "Hotkey labels";
    }

    @Override
    public Component getPreview ( final WebLookAndFeelDemo owner )
    {
        // Text-based hotkeys
        final GroupPanel panel1 = new GroupPanel ( 4, new WebHotkeyLabel ( "Shift" ), new WebHotkeyLabel ( "Escape" ),
                new WebHotkeyLabel ( "Shift+Escape" ) );

        // Disabled KeyEvent-based hotkeys
        final GroupPanel panel2 = new GroupPanel ( 4, new WebHotkeyLabel ( KeyEvent.VK_SPACE ),
                new WebHotkeyLabel ( KeyEvent.VK_SPACE, KeyEvent.CTRL_MASK ) );

        // HotkeyData-based hotkeys
        final GroupPanel panel3 = new GroupPanel ( 4, new WebHotkeyLabel ( Hotkey.ALT ), new WebHotkeyLabel ( Hotkey.F4 ),
                new WebHotkeyLabel ( Hotkey.ALT_F4 ) );

        return new GroupPanel ( 4, false, panel1, panel2, panel3 );
    }
}