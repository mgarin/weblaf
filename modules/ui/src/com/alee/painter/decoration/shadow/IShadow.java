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

package com.alee.painter.decoration.shadow;

import com.alee.api.Identifiable;
import com.alee.api.Mergeable;
import com.alee.painter.decoration.IDecoration;

import javax.swing.*;
import java.awt.*;
import java.io.Serializable;

/**
 * This interface is a base for any custom shadow.
 * It exists exclusively to provide custom-shaped elements shadow painting algorithms.
 *
 * @param <E> component type
 * @param <D> decoration type
 * @param <I> shadow type
 * @author Mikle Garin
 */

public interface IShadow<E extends JComponent, D extends IDecoration<E, D>, I extends IShadow<E, D, I>>
        extends Serializable, Cloneable, Mergeable<I>, Identifiable
{
    /**
     * Returns shadow type.
     *
     * @return shadow type
     * @see ShadowType
     */
    public ShadowType getType ();

    /**
     * Returns shadow width.
     *
     * @return shadow width
     */
    public int getWidth ();

    /**
     * Paints shadow for the specified shape.
     *
     * @param g2d    graphics context
     * @param bounds painting bounds
     * @param c      painted component
     * @param d      painted decoration state
     * @param shape  background shape
     */
    public void paint ( Graphics2D g2d, Rectangle bounds, E c, D d, Shape shape );
}