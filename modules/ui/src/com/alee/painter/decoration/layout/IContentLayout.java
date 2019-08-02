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

package com.alee.painter.decoration.layout;

import com.alee.painter.decoration.IDecoration;
import com.alee.painter.decoration.content.IContent;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * This interface is a base for any custom component content layout.
 * Content layout is optional and will handle layout of {@link com.alee.painter.decoration.content.IContent} placed in it.
 *
 * @param <C> component type
 * @param <D> decoration type
 * @param <I> layout type
 * @author Mikle Garin
 */
public interface IContentLayout<C extends JComponent, D extends IDecoration<C, D>, I extends IContentLayout<C, D, I>>
        extends IContent<C, D, I>
{
    /**
     * Returns all contents of this layout.
     *
     * @param c painted component
     * @param d painted decoration state
     * @return all contents of this layout
     */
    public List<IContent> getContents ( C c, D d );

    /**
     * Returns {@link ContentLayoutData} containing bounds for each of the layout constraints.
     * It should return bounds only for constraints that contain at least one visible content element.
     *
     * @param c      painted component
     * @param d      painted decoration state
     * @param bounds painting bounds
     * @return {@link ContentLayoutData} containing bounds for each of the layout constraints
     */
    public ContentLayoutData layoutContent ( C c, D d, Rectangle bounds );
}