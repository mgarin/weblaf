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

package com.alee.utils;

import com.alee.managers.hotkey.HotkeyData;
import com.alee.utils.swing.KeyEventRunnable;
import com.alee.utils.swing.MouseEventRunnable;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * This class provides a set of utilities to work with various Swing events.
 *
 * @author Mikle Garin
 */

public class EventUtils
{
    /**
     * Shortcut method for double-click mouse event.
     *
     * @param component component to handle events for
     * @param runnable  mouse event runnable
     */
    public static MouseAdapter onDoubleClick ( final Component component, final MouseEventRunnable runnable )
    {
        final MouseAdapter mouseAdapter = new MouseAdapter ()
        {
            @Override
            public void mouseClicked ( final MouseEvent e )
            {
                if ( SwingUtils.isLeftMouseButton ( e ) && e.getClickCount () == 2 )
                {
                    runnable.run ( e );
                }
            }
        };
        component.addMouseListener ( mouseAdapter );
        return mouseAdapter;
    }

    /**
     * Shortcut method for key press event.
     *
     * @param component component to handle events for
     * @param runnable  key event runnable
     */
    public static KeyAdapter onKeyPress ( final Component component, final KeyEventRunnable runnable )
    {
        return onKeyPress ( component, null, runnable );
    }

    /**
     * Shortcut method for key press event.
     *
     * @param component component to handle events for
     * @param hotkey    hotkey filter
     * @param runnable  key event runnable
     */
    public static KeyAdapter onKeyPress ( final Component component, final HotkeyData hotkey, final KeyEventRunnable runnable )
    {
        final KeyAdapter keyAdapter = new KeyAdapter ()
        {
            @Override
            public void keyPressed ( final KeyEvent e )
            {
                if ( hotkey == null || hotkey.isTriggered ( e ) )
                {
                    runnable.run ( e );
                }
            }
        };
        component.addKeyListener ( keyAdapter );
        return keyAdapter;
    }
}