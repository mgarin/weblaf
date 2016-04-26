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

package com.alee.extended.window;

import java.awt.*;

/**
 * Special layout manager which handles window positions and sizes.
 * It is similar to Swing layout managers but it handles windows only.
 *
 * @author Mikle Garin
 */

public interface ScreenLayout<W extends Window, C>
{
    /**
     * Called when window added into this layout.
     *
     * @param window      added window
     * @param constraints component constraints
     */
    public void addWindow ( W window, C constraints );

    /**
     * Called when window removed from this layout.
     *
     * @param window removed window
     */
    public void removeWindow ( W window );

    /**
     * Performs positioning and sizing of windows added into this ScreenLayout.
     */
    public abstract void layoutScreen ();
}