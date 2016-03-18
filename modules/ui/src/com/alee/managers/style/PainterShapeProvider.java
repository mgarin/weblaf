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

import java.awt.*;

/**
 * This interface provides a single method for requesting painted component shape.
 * This can be used by painters to provide their shape for various usage cases.
 *
 * @param <E> component type
 * @author Mikle Garin
 */

public interface PainterShapeProvider<E extends Component>
{
    /**
     * Returns painted component shape.
     *
     * @param component component to process
     * @param bounds    bounds for painter visual data
     * @return painted component shape
     */
    public Shape provideShape ( final E component, final Rectangle bounds );
}