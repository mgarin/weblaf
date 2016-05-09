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

package com.alee.painter;

import java.util.Map;

/**
 * This interface is implemented by components which support custom painters.
 * It provides methods to modify component painter in runtime.
 *
 * @author Mikle Garin
 * @see com.alee.managers.style.StyleManager
 * @see com.alee.painter.Painter
 */

public interface Paintable
{
    /**
     * Returns all custom painters for this component.
     *
     * @return all custom painters for this component
     */
    public Map<String, Painter> getCustomPainters ();

    /**
     * Returns custom base painter for this component.
     *
     * @return custom base painter for this component
     */
    public Painter getCustomPainter ();

    /**
     * Returns custom painter for this component.
     *
     * @param id painter ID
     * @return custom painter for this component
     */
    public Painter getCustomPainter ( final String id );

    /**
     * Sets custom base painter for this component.
     *
     * @param painter custom base painter
     * @return previously set custom base painter
     */
    public Painter setCustomPainter ( Painter painter );

    /**
     * Sets custom painter for this component under the specified painter ID.
     *
     * @param id      painter ID
     * @param painter painter
     * @return old custom painter
     */
    public Painter setCustomPainter ( final String id, final Painter painter );

    /**
     * Restores default painters for this component.
     *
     * @return true if default painters were restored, false otherwise
     */
    public boolean restoreDefaultPainters ();
}