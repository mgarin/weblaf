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

import com.alee.utils.LafUtils;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import java.awt.*;

/**
 * Common implementations for {@link ShapeMethods} interface methods.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-StyleManager">How to use StyleManager</a>
 * @see ShapeMethods
 * @see StyleManager
 */
public final class ShapeMethodsImpl
{
    /**
     * Returns component shape.
     *
     * @param component component to provide shape for
     * @return component shape
     */
    public static Shape getShape ( final JComponent component )
    {
        final ShapeSupport ui = getShapeSupportUI ( component );
        return ui != null ? ui.getShape () : BoundsType.margin.bounds ( component );
    }

    /**
     * Returns whether or not component's custom {@link Shape} is used for better mouse events detection.
     * If it wasn't explicitly specified - {@link com.alee.laf.WebLookAndFeel#isShapeDetectionEnabled()} is used as result.
     *
     * @param component {@link JComponent}
     * @return {@code true} if component's custom {@link Shape} is used for better mouse events detection, {@code false} otherwise
     */
    public static boolean isShapeDetectionEnabled ( final JComponent component )
    {
        final ShapeSupport ui = getShapeSupportUI ( component );
        return ui != null && ui.isShapeDetectionEnabled ();
    }

    /**
     * Sets whether or not component's custom {@link Shape} should be used for better mouse events detection.
     * It can be enabled globally through {@link com.alee.laf.WebLookAndFeel#setShapeDetectionEnabled(boolean)}.
     *
     * @param component {@link JComponent}
     * @param enabled   whether or not component's custom {@link Shape} should be used for better mouse events detection
     */
    public static void setShapeDetectionEnabled ( final JComponent component, final boolean enabled )
    {
        final ShapeSupport ui = getShapeSupportUI ( component );
        if ( ui != null )
        {
            ui.setShapeDetectionEnabled ( enabled );
        }
    }

    /**
     * Returns UI with shape support.
     *
     * @param component component to retrieve UI from
     * @return UI with shape support
     */
    private static ShapeSupport getShapeSupportUI ( final JComponent component )
    {
        final ComponentUI ui = LafUtils.getUI ( component );
        return ui != null && ui instanceof ShapeSupport ? ( ShapeSupport ) ui : null;
    }
}