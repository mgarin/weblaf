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

package com.alee.managers.hover;

import com.alee.api.annotations.NotNull;
import com.alee.api.annotations.Nullable;

import javax.swing.*;
import java.awt.*;

/**
 * Implementations of this interface can be used to track {@link JComponent} and its children hover state.
 * There is also a {@link DefaultHoverTracker} implementation with all basic methods and which also contains a few additional features.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-HoverManager">How to use HoverManager</a>
 * @see DefaultHoverTracker
 * @see HoverManager
 */
public interface HoverTracker
{
    /**
     * Returns whether or not tracking is currently enabled.
     *
     * @return {@code true} if tracking is currently enabled, {@code false} otherwise
     */
    public boolean isEnabled ();

    /**
     * Sets whether or not tracking is currently enabled.
     *
     * @param enabled whether or not tracking is currently enabled
     */
    public void setEnabled ( boolean enabled );

    /**
     * Returns whether or not tracked component is currently hovered according to this tracker settings.
     *
     * @return {@code true} if tracked component is currently hovered according to this tracker settings, {@code false} otherwise
     */
    public boolean isHovered ();

    /**
     * Sets tracked component hovered state.
     *
     * @param hovered component hovered state
     */
    public void setHovered ( boolean hovered );

    /**
     * Returns whether specified {@link Component} is involved with this tracked {@link JComponent} or not.
     * It basically says whether or not specified {@link Component} counts towards tracked {@link JComponent} hover changes.
     *
     * @param tracked   tracked {@link JComponent}
     * @param component {@link Component} to check for involvement
     * @return {@code true} if specified {@link Component} is involved with this tracked {@link JComponent}, {@code false} otherwise
     */
    public boolean isInvolved ( @NotNull JComponent tracked, @Nullable Component component );

    /**
     * Informs about tracked {@link JComponent} hover changes depending on tracker settings.
     *
     * @param hover whether tracked {@link JComponent} is hovered or not
     */
    public void hoverChanged ( boolean hover );
}