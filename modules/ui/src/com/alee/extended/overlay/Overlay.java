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
import com.alee.api.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.io.Serializable;

/**
 * Base interface for any {@link Overlay} implementation for {@link WebOverlay}.
 * {@link Overlay} can be added into {@link WebOverlay} to be displayed on top of another {@link JComponent}.
 *
 * @author Mikle Garin
 */
public interface Overlay extends Serializable
{
    /**
     * Returns overlay {@link JComponent}.
     *
     * @return overlay {@link JComponent}
     */
    @NotNull
    public JComponent component ();

    /**
     * Returns additional margin for the {@link WebOverlay} content {@link JComponent}.
     *
     * @return additional margin for the {@link WebOverlay} content {@link JComponent}
     */
    @NotNull
    public Insets margin ();

    /**
     * Returns bounds for the specified overlay {@link JComponent}.
     * Bounds should be relative to the provided component bounds and can "step out".
     * {@link OverlayLayout} will use these bounds to adjust preferred size and properly place component and overlays.
     *
     * @param container {@link WebOverlay} container
     * @param component {@link JComponent} to be overlayed
     * @param bounds    overlayed {@link JComponent} bounds
     * @return bounds for the specified overlay {@link JComponent}
     */
    @NotNull
    public Rectangle bounds ( @NotNull WebOverlay container, @Nullable JComponent component, @NotNull Rectangle bounds );
}