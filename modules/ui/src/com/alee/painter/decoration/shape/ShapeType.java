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

package com.alee.painter.decoration.shape;

/**
 * Enumeration representing existing shape types.
 *
 * @author Mikle Garin
 */

public enum ShapeType
{
    /**
     * Shape provided for outer shade painting.
     */
    outerShade,

    /**
     * Shape provided for border painting.
     */
    border,

    /**
     * Shape provided for inner shade painting.
     */
    innerShade,

    /**
     * Shape provide for background painting.
     */
    background;

    /**
     * Returns whether or not provided shape has shade type.
     *
     * @return true if provided shape has shade type, false otherwise
     */
    public boolean isShade ()
    {
        return this == outerShade || this == innerShade;
    }

    /**
     * Returns whether or not provided shape has outer shade type.
     *
     * @return true if provided shape has outer shade type, false otherwise
     */
    public boolean isOuterShade ()
    {
        return this == outerShade;
    }

    /**
     * Returns whether or not provided shape has border type.
     *
     * @return true if provided shape has border type, false otherwise
     */
    public boolean isBorder ()
    {
        return this == border;
    }

    /**
     * Returns whether or not provided shape has inner shade type.
     *
     * @return true if provided shape has inner shade type, false otherwise
     */
    public boolean isInnerShade ()
    {
        return this == innerShade;
    }

    /**
     * Returns whether or not provided shape has background type.
     *
     * @return true if provided shape has background type, false otherwise
     */
    public boolean isBackground ()
    {
        return this == background;
    }
}