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
 * @param <E> component type
 * @param <D> decoration type
 * @param <I> layout type
 * @author Mikle Garin
 */

public interface IContentLayout<E extends JComponent, D extends IDecoration<E, D>, I extends IContentLayout<E, D, I>>
        extends IContent<E, D, I>
{
    /**
     * Returns amount of available content elements.
     *
     * @param c painted component
     * @param d painted decoration state
     * @return amount of available content elements
     */
    public int getContentCount ( E c, D d );

    /**
     * Returns whether or not specified content is empty.
     *
     * @param c           painted component
     * @param d           painted decoration state
     * @param constraints content constraints
     * @return true if specified content is empty, false otherwise
     */
    public boolean isEmpty ( E c, D d, String constraints );

    /**
     * Returns contents placed under the specified constraints.
     *
     * @param c           painted component
     * @param d           painted decoration state
     * @param constraints content constraints
     * @return contents placed under the specified constraints
     */
    public List<IContent> getContents ( E c, D d, String constraints );

    /**
     * Paints layout contents at the specified constraints.
     *
     * @param g2d         graphics context
     * @param bounds      painting bounds
     * @param c           painted component
     * @param d           painted decoration state
     * @param constraints content constraints
     */
    public void paint ( Graphics2D g2d, Rectangle bounds, E c, D d, String constraints );

    /**
     * Returns preferred size of contents placed under the specified constraints.
     *
     * @param c           painted component
     * @param d           painted decoration state
     * @param available   theoretically available space for this content
     * @param constraints content constraints
     * @return preferred size of contents placed under the specified constraints
     */
    public Dimension getPreferredSize ( E c, D d, Dimension available, String constraints );
}