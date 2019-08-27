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

import com.alee.api.annotations.NotNull;

import javax.swing.*;

/**
 * Base interface for all WebLaF UI input listeners.
 * These listeners are used to streamline all component input settings and listeners and enclose them into single simple class.
 * Each component has it's own extension of this interface that defines any additional public methods it's implementations must have.
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
    public void install ( @NotNull C component );

    /**
     * Uninstalls input settings and listeners.
     *
     * @param component {@link JComponent}
     */
    public void uninstall ( @NotNull C component );
}