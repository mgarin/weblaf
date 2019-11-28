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

package com.alee.extended.overlay;

import com.alee.api.annotations.NotNull;

import javax.swing.*;
import java.awt.*;

/**
 * Abstract {@link Overlay} containing basic settings acquired through constructor.
 *
 * @author Mikle Garin
 */
public abstract class AbstractOverlay implements Overlay
{
    /**
     * Overlay {@link JComponent}.
     */
    @NotNull
    protected final JComponent component;

    /**
     * Additional margin for the {@link WebOverlay} content {@link JComponent}.
     */
    @NotNull
    private final Insets margin;

    /**
     * Constructs new {@link AbstractOverlay}.
     *
     * @param component overlay {@link JComponent}
     * @param margin    additional margin for the {@link WebOverlay} content {@link JComponent}
     */
    protected AbstractOverlay ( @NotNull final JComponent component, @NotNull final Insets margin )
    {
        this.component = component;
        this.margin = margin;
    }

    @NotNull
    @Override
    public JComponent component ()
    {
        return component;
    }

    @NotNull
    @Override
    public Insets margin ()
    {
        return margin;
    }
}