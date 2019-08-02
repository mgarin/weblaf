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

import java.awt.*;
import java.awt.event.MouseAdapter;

/**
 * This class provides a no-op {@link java.awt.event.MouseListener}.
 * It can be used in case you need to block all mouse events from passing to underlying components.
 *
 * @author Mikle Garin
 */
public class NoOpMouseListener extends MouseAdapter
{
    /**
     * Installs {@link NoOpMouseListener} into the specified {@link Component}.
     *
     * @param component {@link Component} to install {@link NoOpMouseListener} into
     */
    public static void install ( final Component component )
    {
        final NoOpMouseListener adapter = new NoOpMouseListener ();
        component.addMouseListener ( adapter );
        component.addMouseMotionListener ( adapter );
        component.addMouseWheelListener ( adapter );
    }
}