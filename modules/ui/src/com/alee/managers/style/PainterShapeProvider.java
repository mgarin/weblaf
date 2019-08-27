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

import com.alee.api.annotations.NotNull;

import java.awt.*;

/**
 * This interface provides a single method for requesting painted component shape.
 * This can be used by painters to provide their shape for various usage cases.
 *
 * @param <C> component type
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-StyleManager">How to use StyleManager</a>
 * @see StyleManager
 */
public interface PainterShapeProvider<C extends Component>
{
    /**
     * Returns painted component shape.
     *
     * @param component component to process
     * @param bounds    bounds for painter view
     * @return painted component shape
     */
    @NotNull
    public Shape provideShape ( @NotNull C component, @NotNull Rectangle bounds );
}