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

/**
 * Base interface for any component decoration elements.
 * Element might not directly contain any visual representation, but it is always tied to specific decoration states.
 * For example {@link com.alee.painter.decoration.shape.IShape} only provides {@link java.awt.Shape} for other elements.
 * As another example {@link com.alee.painter.decoration.background.IBackground} paints an actual background for any provided shape.
 *
 * @param <C> {@link JComponent} type
 * @param <D> {@link IDecoration} type
 * @param <I> {@link IDecoratonElement} type
 * @author Mikle Garin
 */

@SuppressWarnings ( "unused" )
public interface IDecoratonElement<C extends JComponent, D extends IDecoration<C, D>, I extends IDecoratonElement<C, D, I>>
{
    /**
     * Called upon {@link IDecoration} activation.
     * Occurs when {@link IDecoration} using this {@link IDecoratonElement} becomes active.
     *
     * @param c {@link JComponent}
     * @param d {@link IDecoration}
     */
    public void activate ( C c, D d );

    /**
     * Called upon {@link IDecoration} deactivation.
     * Occurs when {@link IDecoration} using this {@link IDecoratonElement} becomes inactive.
     *
     * @param c {@link JComponent}
     * @param d {@link IDecoration}
     */
    public void deactivate ( C c, D d );
}