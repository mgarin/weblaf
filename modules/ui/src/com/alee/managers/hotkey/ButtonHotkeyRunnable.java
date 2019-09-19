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

import com.alee.api.annotations.NotNull;

import javax.swing.*;
import java.awt.event.KeyEvent;

/**
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-HotkeyManager">How to use HotkeyManager</a>
 * @see HotkeyManager
 */
public class ButtonHotkeyRunnable implements HotkeyRunnable
{
    private final AbstractButton button;
    private final int pressTime;

    public ButtonHotkeyRunnable ( final AbstractButton button )
    {
        this ( button, 0 );
    }

    public ButtonHotkeyRunnable ( final AbstractButton button, final int pressTime )
    {
        super ();
        this.button = button;
        this.pressTime = pressTime;
    }

    @Override
    public void run ( @NotNull final KeyEvent e )
    {
        button.doClick ( pressTime );
    }
}