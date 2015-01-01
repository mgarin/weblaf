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

import com.alee.utils.swing.KeyEventRunnable;

import java.awt.event.KeyEvent;

/**
 * This interface allows you to create action for hotkey events which can be used together with HotkeyManager methods.
 * Unlike Runnable this interface provides KeyEvent as action source.
 *
 * @author Mikle Garin
 */

public interface HotkeyRunnable extends KeyEventRunnable
{
    /**
     * Simple runnable that doesn't perform any actions.
     */
    public static final HotkeyRunnable NONE = new HotkeyRunnable ()
    {
        @Override
        public void run ( final KeyEvent e )
        {
            // Do nothing
        }
    };

    /**
     * Performs hotkey action.
     *
     * @param e occured key event
     */
    @Override
    public void run ( KeyEvent e );
}