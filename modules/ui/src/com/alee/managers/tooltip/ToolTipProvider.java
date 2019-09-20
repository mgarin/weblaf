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

/**
 * Base interface for custom tooltip providers for complex components.
 * It only provides tooltips for component areas described by {@link ComponentArea} implementation.
 *
 * @param <V> value type
 * @param <C> component type
 * @param <A> component area type
 * @author Mikle Garin
 */
public interface ToolTipProvider<V, C extends JComponent, A extends ComponentArea<V, C>>
{
    /**
     * Returns tooltip display delay.
     * Any value below 1 will force tooltips to be displayed instantly.
     *
     * @return tooltip display delay
     */
    public long getDelay ();

    /**
     * Returns tooltip source bounds, {@code null} if {@link ComponentArea} is not available anymore.
     * Tooltip will be displayed relative to these bounds using provided {@link TooltipWay}.
     *
     * @param component {@link JComponent} to provide tooltip for
     * @param area      {@link ComponentArea}
     * @return tooltip source bounds, {@code null} if {@link ComponentArea} is not available anymore
     */
    @Nullable
    public Rectangle getSourceBounds ( @NotNull C component, @NotNull A area );

    /**
     * Return {@link WebCustomTooltip} for the specified value and {@link ComponentArea}.
     *
     * @param component {@link JComponent} to provide tooltip for
     * @param area      {@link ComponentArea}
     * @return {@link WebCustomTooltip} for the specified value and {@link ComponentArea}
     */
    @Nullable
    public WebCustomTooltip getToolTip ( @NotNull C component, @NotNull A area );

    /**
     * Informs about hover area changes.
     * This method implementations should display tooltips
     *
     * @param component {@link JComponent} to provide tooltip for
     * @param oldArea   {@link ComponentArea}
     * @param newArea   {@link ComponentArea}
     */
    public void hoverAreaChanged ( @NotNull C component, @Nullable A oldArea, @Nullable A newArea );
}