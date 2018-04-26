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

import com.alee.painter.decoration.IDecoration;

import javax.swing.*;

/**
 * Partially decoratable component shape interface.
 * It must be implemented by any shape that provides partial decoration support.
 *
 * @param <C> component type
 * @param <D> decoration type
 * @param <I> shape type
 * @author Mikle Garin
 */
public interface IPartialShape<C extends JComponent, D extends IDecoration<C, D>, I extends IPartialShape<C, D, I>> extends IShape<C, D, I>
{
    /**
     * Returns whether or not top side should be painted.
     *
     * @param c painted component
     * @param d painted decoration
     * @return true if top side should be painted, false otherwise
     */
    public boolean isPaintTop ( C c, D d );

    /**
     * Returns whether or not left side should be painted.
     *
     * @param c painted component
     * @param d painted decoration
     * @return true if left side should be painted, false otherwise
     */
    public boolean isPaintLeft ( C c, D d );

    /**
     * Returns whether or not bottom side should be painted.
     *
     * @param c painted component
     * @param d painted decoration
     * @return true if bottom side should be painted, false otherwise
     */
    public boolean isPaintBottom ( C c, D d );

    /**
     * Returns whether or not right side should be painted.
     *
     * @param c painted component
     * @param d painted decoration
     * @return true if right side should be painted, false otherwise
     */
    public boolean isPaintRight ( C c, D d );

    /**
     * Returns whether or not top side line should be painted.
     *
     * @param c painted component
     * @param d painted decoration
     * @return true if top side line should be painted, false otherwise
     */
    public boolean isPaintTopLine ( C c, D d );

    /**
     * Returns whether or not left side line should be painted.
     *
     * @param c painted component
     * @param d painted decoration
     * @return true if left side line should be painted, false otherwise
     */
    public boolean isPaintLeftLine ( C c, D d );

    /**
     * Returns whether or not bottom side line should be painted.
     *
     * @param c painted component
     * @param d painted decoration
     * @return true if bottom side line should be painted, false otherwise
     */
    public boolean isPaintBottomLine ( C c, D d );

    /**
     * Returns whether or not right side line should be painted.
     *
     * @param c painted component
     * @param d painted decoration
     * @return true if right side line should be painted, false otherwise
     */
    public boolean isPaintRightLine ( C c, D d );
}