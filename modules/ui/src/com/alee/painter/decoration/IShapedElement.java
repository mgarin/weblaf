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

package com.alee.painter.decoration;

import javax.swing.*;
import java.awt.*;

/**
 * Interface for component decoration elements based on custom shapes.
 *
 * @param <C> component type
 * @param <D> decoration type
 * @param <I> element type
 * @author Mikle Garin
 */
public interface IShapedElement<C extends JComponent, D extends IDecoration<C, D>, I extends IShapedElement<C, D, I>>
        extends IDecoratonElement<C, D, I>
{
    /**
     * Paints this element using the specified {@link Shape}.
     *
     * @param g2d    {@link Graphics2D}
     * @param bounds painting bounds
     * @param c      {@link JComponent} that is being painted
     * @param d      {@link IDecoration} state
     * @param shape  {@link Shape} of the painted element
     */
    public void paint ( Graphics2D g2d, Rectangle bounds, C c, D d, Shape shape );
}