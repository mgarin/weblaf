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
     * Shape provided for outer shadow painting.
     */
    outerShadow,

    /**
     * Shape provided for border painting.
     */
    border,

    /**
     * Shape provided for inner shadow painting.
     */
    innerShadow,

    /**
     * Shape provide for background painting.
     */
    background;

    /**
     * Returns whether or not provided shape has shadow type.
     *
     * @return true if provided shape has shadow type, false otherwise
     */
    public boolean isShadow ()
    {
        return this == outerShadow || this == innerShadow;
    }

    /**
     * Returns whether or not provided shape has outer shadow type.
     *
     * @return true if provided shape has outer shadow type, false otherwise
     */
    public boolean isOuterShadow ()
    {
        return this == outerShadow;
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
     * Returns whether or not provided shape has inner shadow type.
     *
     * @return true if provided shape has inner shadow type, false otherwise
     */
    public boolean isInnerShadow ()
    {
        return this == innerShadow;
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