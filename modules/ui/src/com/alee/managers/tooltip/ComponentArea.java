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

package com.alee.managers.tooltip;

import javax.swing.*;
import java.awt.*;
import java.io.Serializable;

/**
 * Interface for any class that points at specific area on the {@link JComponent}.
 *
 * @param <V> area value type
 * @param <C> component type
 * @author Mikle Garin
 */
public interface ComponentArea<V, C extends JComponent> extends Serializable
{
    /**
     * Returns component area bounds in component coordinates system.
     *
     * @param component {@link JComponent}
     * @return component area bounds in component coordinates system
     */
    public Rectangle getBounds ( C component );

    /**
     * Returns value for the specified component area.
     *
     * @param component {@link JComponent}
     * @return value for the specified component area
     */
    public V getValue ( C component );
}