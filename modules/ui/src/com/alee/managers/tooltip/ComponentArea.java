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

import com.alee.api.annotations.NotNull;
import com.alee.api.annotations.Nullable;

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
     * Returns whether or not this {@link ComponentArea} is still available at the specified {@link JComponent}.
     *
     * @param component {@link JComponent}
     * @return {@code true} if this {@link ComponentArea} is still available at the specified {@link JComponent}, {@code false} otherwise
     */
    public boolean isAvailable ( @NotNull C component );

    /**
     * Returns {@link ComponentArea} bounds in the specified {@link JComponent} coordinates system.
     * Could also return {@code null} at this step in case {@link ComponentArea} is not available anymore or something is wrong.
     *
     * @param component {@link JComponent}
     * @return {@link ComponentArea} bounds in the specified {@link JComponent} coordinates system
     */
    @Nullable
    public Rectangle getBounds ( @NotNull C component );

    /**
     * Returns value for this {@link ComponentArea} at the specified {@link JComponent}.
     * Could return {@code null} in case value is {@code null}, doesn't exist or couldn't be retrieved.
     *
     * @param component {@link JComponent}
     * @return value for this {@link ComponentArea} at the specified {@link JComponent}
     */
    @Nullable
    public V getValue ( @NotNull C component );
}