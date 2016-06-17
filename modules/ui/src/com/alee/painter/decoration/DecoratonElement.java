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
 * Component decoration elements interface.
 *
 * @param <E> component type
 * @param <D> decoration type
 * @param <I> element type
 * @author Mikle Garin
 */

public interface DecoratonElement<E extends JComponent, D extends IDecoration<E, D>, I extends DecoratonElement<E, D, I>>
{
    /**
     * Called upon decoration activation.
     * Occurs when decoration using this element becomes the active one.
     *
     * @param c painted component
     * @param d painted decoration state
     */
    public void activate ( E c, D d );

    /**
     * Called upon decoration deactivation.
     * Occurs when decoration using this element becomes inactive.
     *
     * @param c painted component
     * @param d painted decoration state
     */
    public void deactivate ( E c, D d );
}
