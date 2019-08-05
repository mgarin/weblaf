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

package com.alee.utils.swing;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Small extension for ActionListener to simplify timer usage.
 *
 * @author Mikle Garin
 * @see com.alee.utils.swing.WebTimer
 */
public abstract class TimerActionListener implements ActionListener
{
    @Override
    public void actionPerformed ( final ActionEvent e )
    {
        timerActionPerformed ( e, ( WebTimer ) e.getSource () );
    }

    /**
     * Simply an extended version of {@code actionPerformed()} method.
     *
     * @param e     action event
     * @param timer timer which forced this action
     */
    public abstract void timerActionPerformed ( ActionEvent e, WebTimer timer );
}