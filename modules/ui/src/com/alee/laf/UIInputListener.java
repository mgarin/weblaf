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

import javax.swing.*;

/**
 * Base interface for all custom WebLaF input listeners.
 * These listeners are used to streamline all input settings and listeners and enclose them into one simple class.
 *
 * @param <C> {@link JComponent} type
 * @author Mikle Garin
 */

public interface UIInputListener<C extends JComponent>
{
    /**
     * Installs input settings and listeners.
     *
     * @param component {@link JComponent}
     */
    public void install ( C component );

    /**
     * Uninstalls input settings and listeners.
     *
     * @param component {@link JComponent}
     */
    public void uninstall ( C component );
}