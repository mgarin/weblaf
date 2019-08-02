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
import com.alee.api.merge.Overwriting;
import com.alee.painter.decoration.IDecoration;
import com.alee.painter.decoration.IShapedElement;

import javax.swing.*;
import java.io.Serializable;

/**
 * Interface for any custom shadow effects.
 *
 * @param <C> component type
 * @param <D> decoration type
 * @param <I> shadow type
 * @author Mikle Garin
 */
public interface IShadow<C extends JComponent, D extends IDecoration<C, D>, I extends IShadow<C, D, I>>
        extends IShapedElement<C, D, I>, Identifiable, Overwriting, Cloneable, Serializable
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
}