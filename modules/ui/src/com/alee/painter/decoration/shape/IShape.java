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

import com.alee.api.Identifiable;
import com.alee.api.merge.Overwriting;
import com.alee.painter.decoration.IDecoration;
import com.alee.painter.decoration.IDecoratonElement;

import javax.swing.*;
import java.awt.*;
import java.io.Serializable;

/**
 * Interface for providing custom element shape.
 *
 * @param <C> component type
 * @param <D> decoration type
 * @param <I> shape type
 * @author Mikle Garin
 */
public interface IShape<C extends JComponent, D extends IDecoration<C, D>, I extends IShape<C, D, I>>
        extends IDecoratonElement<C, D, I>, Serializable, Cloneable, Overwriting, Identifiable
{
    /**
     * Returns whether shape is visible within component bounds.
     * This method is required to optimize painting operations.
     *
     * @param type   checked shape type
     * @param bounds painting bounds
     * @param c      painted component
     * @param d      painted decoration state
     * @return true if shape is visible within component bounds, false otherwise
     */
    public boolean isVisible ( ShapeType type, Rectangle bounds, C c, D d );

    /**
     * Returns shape provided for shade painting.
     * It is usually similar to background shape but slightly larger.
     *
     * @param type   requested shape type
     * @param bounds painting bounds
     * @param c      painted component
     * @param d      painted decoration state
     * @return component shape of the specified type
     */
    public Shape getShape ( ShapeType type, Rectangle bounds, C c, D d );

    /**
     * Returns exclusive shape settings used to cache the shape itself.
     * There is no need to return bounds provided while shape is generated or parameters from other decoration parts.
     * This information might be useful outside the shape to implement proper caching of data generated based on this shape.
     *
     * @param bounds painting bounds
     * @param c      painted component
     * @param d      painted decoration state
     * @return shape settings used to cache the shape itself
     */
    public Object[] getShapeSettings ( Rectangle bounds, C c, D d );

    /**
     * Returns shape stretch information.
     * If this method returns something that is not {@code null} shape is stretchable horizontally and/or vertically.
     * That information might be extremely useful for painting optimization, but it is not necessary to implement this method.
     *
     * As an example - WebLaF uses this information to optimize (reduce amount of) shadow images generation.
     * Since generating even a small shadow requires a good chunk of memory and processing time it is necessary.
     *
     * @param bounds painting bounds
     * @param c      painted component
     * @param d      painted decoration state
     * @return shape stretch information
     */
    public StretchInfo getStretchInfo ( Rectangle bounds, C c, D d );
}