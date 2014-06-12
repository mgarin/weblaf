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

package com.alee.managers.hotkey;

import javax.swing.*;
import java.awt.event.KeyEvent;

/**
 * User: mgarin Date: 30.11.12 Time: 17:15
 */

public class ButtonHotkeyRunnable implements HotkeyRunnable
{
    private AbstractButton button;
    private int pressTime;

    public ButtonHotkeyRunnable ( AbstractButton button )
    {
        this ( button, 0 );
    }

    public ButtonHotkeyRunnable ( AbstractButton button, int pressTime )
    {
        super ();
        this.button = button;
        this.pressTime = pressTime;
    }

    @Override
    public void run ( KeyEvent e )
    {
        button.doClick ( pressTime );
    }
}
