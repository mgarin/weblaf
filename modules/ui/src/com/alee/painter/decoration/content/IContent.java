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

package com.alee.painter.decoration.content;

import com.alee.api.Identifiable;
import com.alee.api.Mergeable;
import com.alee.api.Overwritable;
import com.alee.managers.style.Bounds;
import com.alee.painter.decoration.DecoratonElement;
import com.alee.painter.decoration.IDecoration;

import javax.swing.*;
import java.awt.*;
import java.io.Serializable;

/**
 * Customizable component content interface.
 *
 * @param <E> component type
 * @param <D> decoration type
 * @param <I> content type
 * @author Mikle Garin
 */

public interface IContent<E extends JComponent, D extends IDecoration<E, D>, I extends IContent<E, D, I>>
        extends DecoratonElement<E, D, I>, Serializable, Cloneable, Mergeable<I>, Overwritable, Identifiable
{
    /**
     * Returns content bounds type.
     * Will affect bounds provided into "paint" method.
     *
     * @return content bounds type
     */
    public Bounds getBoundsType ();

    /**
     * Returns content constraints within {@link com.alee.painter.decoration.layout.IContentLayout}.
     * In case layout is not specified these constraints will never be used and requested at all.
     *
     * @return content constraints within {@link com.alee.painter.decoration.layout.IContentLayout}
     */
    public String getConstraints ();

    /**
     * Returns whether or not this content is empty.
     *
     * @param c painted component
     * @param d painted decoration state
     * @return true if this content is empty, false otherwise
     */
    public boolean isEmpty ( E c, D d );

    /**
     * Returns content baseline for the specified width and height, it is measured from the top of the component.
     * This method is primarily meant for {@code java.awt.LayoutManager}s to align components along their baseline.
     * A return value less than 0 indicates this component does not have a reasonable baseline and that {@code java.awt.LayoutManager}s
     * should not align this component on its baseline.
     *
     * @param c      aligned component
     * @param d      aligned component decoration state
     * @param width  the width to get the baseline for
     * @param height the height to get the baseline for
     * @return content baseline for the specified width and height
     */
    public int getBaseline ( E c, D d, int width, int height );

    /**
     * Paints component's content.
     *
     * @param g2d    graphics context
     * @param bounds painting bounds
     * @param c      painted component
     * @param d      painted decoration state
     */
    public void paint ( Graphics2D g2d, Rectangle bounds, E c, D d );

    /**
     * Returns content preferred size.
     *
     * @param c         painted component
     * @param d         painted decoration state
     * @param available theoretically available space for this content
     * @return content preferred size
     */
    public Dimension getPreferredSize ( E c, D d, Dimension available );
}