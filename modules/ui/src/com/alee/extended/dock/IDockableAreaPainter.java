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

package com.alee.extended.dock;

import com.alee.api.annotations.NotNull;
import com.alee.api.data.CompassDirection;
import com.alee.painter.SectionPainter;

/**
 * Base interface for {@link WebDockablePane} area painters.
 *
 * @param <C> component type
 * @param <U> component UI type
 * @author Mikle Garin
 */
public interface IDockableAreaPainter<C extends WebDockablePane, U extends WDockablePaneUI<C>> extends SectionPainter<C, U>
{
    /**
     * Returns whether or not this painter has decorations for the specified {@link WebDockablePane} area.
     *
     * @param area {@link WebDockablePane} area
     * @return {@code true} if this painter has decorations for the specified {@link WebDockablePane} area, {@code false} otherwise
     */
    public boolean hasDecorationFor ( @NotNull CompassDirection area );

    /**
     * Prepares painter to paint specified {@link WebDockablePane} area.
     *
     * @param area {@link WebDockablePane} area
     */
    public void prepareToPaint ( @NotNull CompassDirection area );
}