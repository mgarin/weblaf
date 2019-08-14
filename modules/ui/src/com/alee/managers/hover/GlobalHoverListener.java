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

import com.alee.api.annotations.Nullable;

import java.awt.*;
import java.util.EventListener;

/**
 * Global hover tracking listener.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-HoverManager">How to use HoverManager</a>
 * @see HoverManager
 */
public interface GlobalHoverListener extends EventListener
{
    /**
     * Informs about global hover changes within the application.
     * In case hover goes outside the application or comes from other application one of components might be {@code null}.
     *
     * @param oldHover previously hovered {@link Component}
     * @param newHover currently hovered {@link Component}
     */
    public void hoverChanged ( @Nullable Component oldHover, @Nullable Component newHover );
}