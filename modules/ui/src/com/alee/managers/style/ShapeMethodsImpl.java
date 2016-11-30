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
 * @see ShapeMethods
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