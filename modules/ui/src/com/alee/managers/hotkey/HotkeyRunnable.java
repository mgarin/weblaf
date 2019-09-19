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
import com.alee.utils.swing.extensions.KeyEventRunnable;

import java.awt.event.KeyEvent;

/**
 * This interface allows you to create action for hotkey events which can be used together with {@link HotkeyManager} methods.
 * Unlike {@link Runnable} this interface provides {@link KeyEvent} as action source.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-HotkeyManager">How to use HotkeyManager</a>
 * @see HotkeyManager
 */
public interface HotkeyRunnable extends KeyEventRunnable
{
    /**
     * Simple runnable that doesn't perform any actions.
     */
    public static final HotkeyRunnable NONE = new HotkeyRunnable ()
    {
        @Override
        public void run ( @NotNull final KeyEvent e )
        {
            /**
             * Take no action.
             */
        }
    };

    /**
     * Performs hotkey action.
     *
     * @param e occured key event
     */
    @Override
    public void run ( @NotNull KeyEvent e );
}