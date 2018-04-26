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
import com.alee.api.merge.Overwriting;
import com.alee.managers.style.BoundsType;
import com.alee.painter.decoration.IDecoration;
import com.alee.painter.decoration.IDecoratonElement;

import javax.swing.*;
import java.awt.*;
import java.io.Serializable;

/**
 * Customizable component content interface.
 *
 * @param <C> component type
 * @param <D> decoration type
 * @param <I> content type
 * @author Mikle Garin
 */
public interface IContent<C extends JComponent, D extends IDecoration<C, D>, I extends IContent<C, D, I>>
        extends Identifiable, IDecoratonElement<C, D, I>, Overwriting, Cloneable, Serializable
{
    /**
     * todo 1. Remove {@link IDecoration} usage from all methods?
     */

    /**
     * Returns content bounds type.
     * Will affect bounds provided into "paint" method.
     *
     * @return content bounds type
     */
    public BoundsType getBoundsType ();

    /**
     * Returns content constraints within {@link com.alee.painter.decoration.layout.IContentLayout}.
     * In case this {@link IContent} is not placed within any layout these constraints will never be requested.
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
    public boolean isEmpty ( C c, D d );

    /**
     * Returns whether or not this content has a reasonable baseline.
     *
     * @param c aligned component
     * @param d aligned component decoration state
     * @return {@code true} if this content has a reasonable baseline, {@code false} otherwise
     */
    public boolean hasBaseline ( C c, D d );

    /**
     * Returns content baseline within the specified bounds, measured from the top of the bounds.
     * A return value less than {@code 0} indicates this content does not have a reasonable baseline.
     *
     * @param c      aligned component
     * @param d      aligned component decoration state
     * @param bounds bounds to get the baseline for
     * @return content baseline within the specified bounds, measured from the top of the bounds
     */
    public int getBaseline ( C c, D d, Rectangle bounds );

    /**
     * Returns enum indicating how the baseline of the content changes as the size changes.
     *
     * @param c aligned component
     * @param d aligned component decoration state
     * @return enum indicating how the baseline of the content changes as the size changes
     */
    public Component.BaselineResizeBehavior getBaselineResizeBehavior ( C c, D d );

    /**
     * Paints component's content.
     *
     * @param g2d    graphics context
     * @param c      painted component
     * @param d      painted decoration state
     * @param bounds painting bounds
     */
    public void paint ( Graphics2D g2d, C c, D d, Rectangle bounds );

    /**
     * Returns content preferred size.
     *
     * @param c         painted component
     * @param d         painted decoration state
     * @param available theoretically available space for this content
     * @return content preferred size
     */
    public Dimension getPreferredSize ( C c, D d, Dimension available );
}