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

import com.alee.managers.style.StyleId;

/**
 * Base interface for any classes that can return {@link StyleId} for rendering operations.
 *
 * @param <P> {@link RenderingParameters} type
 * @author Mikle Garin
 */

public interface StyleIdBridge<P extends RenderingParameters> extends RenderingBridge
{
    /**
     * Returns {@link StyleId} based on provided {@link RenderingParameters}.
     * Providing {@code null} here is valid but not recommended.
     *
     * @param parameters {@link RenderingParameters}
     * @return {@link StyleId} based on provided {@link RenderingParameters}.
     */
    public StyleId getStyleId ( P parameters );
}