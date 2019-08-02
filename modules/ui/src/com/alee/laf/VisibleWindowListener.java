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

package com.alee.laf;

import java.awt.*;
import java.util.EventListener;

/**
 * Global Swing listener for tracking visible {@link Window} instances.
 * It fires {@link #windowDisplayed(Window)} whenever any {@link Window} becomes visible.
 * It fires {@link #windowHidden(Window)} whenever any {@link Window} becomes hidden or disposed.
 *
 * Be careful using this listener not to create hard references to any {@link Window} passed to avoid major memory leaks.
 *
 * @author Mikle Garin
 */
public interface VisibleWindowListener extends EventListener
{
    /**
     * Informs about {@link Window} becoming visible.
     *
     * @param window {@link Window}
     */
    public void windowDisplayed ( Window window );

    /**
     * Informs about {@link Window} becoming hidden or disposed.
     *
     * @param window {@link Window}
     */
    public void windowHidden ( Window window );
}