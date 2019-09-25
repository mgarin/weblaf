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

package com.alee.api.ui;

import com.alee.api.annotations.NotNull;
import com.alee.api.annotations.Nullable;

import java.awt.*;

/**
 * Base interface for any classes that can return foreground {@link Color} for rendering operations.
 *
 * @param <P> {@link RenderingParameters} type
 * @author Mikle Garin
 */
public interface ForegroundBridge<P extends RenderingParameters> extends RenderingBridge
{
    /**
     * Returns foreground {@link Color} based on provided {@link RenderingParameters}.
     * Providing {@code null} here is completely valid.
     *
     * @param parameters {@link RenderingParameters}
     * @return foreground {@link Color} based on provided {@link RenderingParameters}.
     */
    @Nullable
    public Color getForeground ( @NotNull P parameters );
}