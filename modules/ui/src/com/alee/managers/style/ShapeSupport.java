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

package com.alee.managers.style;

import com.alee.laf.WebLookAndFeel;

import java.awt.*;

/**
 * This interface provides a single method for requesting shape.
 * This can be used by components to provide their shape for various usage cases.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-StyleManager">How to use StyleManager</a>
 * @see ShapeMethods
 * @see StyleManager
 */
public interface ShapeSupport
{
    /**
     * Returns component {@link Shape}.
     *
     * @return component {@link Shape}
     */
    public Shape getShape ();

    /**
     * Returns whether or not component's custom {@link Shape} is used for better mouse events detection.
     * If it wasn't explicitly specified - {@link WebLookAndFeel#isShapeDetectionEnabled()} is used as result.
     *
     * @return {@code true} if component's custom {@link Shape} is used for better mouse events detection, {@code false} otherwise
     */
    public boolean isShapeDetectionEnabled ();

    /**
     * Sets whether or not component's custom {@link Shape} should be used for better mouse events detection.
     * It can be enabled globally through {@link com.alee.laf.WebLookAndFeel#setShapeDetectionEnabled(boolean)}.
     *
     * @param enabled whether or not component's custom {@link Shape} should be used for better mouse events detection
     */
    public void setShapeDetectionEnabled ( boolean enabled );
}