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

import com.alee.painter.decoration.IDecoration;

import javax.swing.*;
import java.awt.*;

/**
 * Abstract text content providing some general method implementations.
 *
 * @param <E> component type
 * @param <D> decoration type
 * @param <I> content type
 * @author Mikle Garin
 */

public abstract class AbstractTextContent<E extends JComponent, D extends IDecoration<E, D>, I extends AbstractTextContent<E, D, I>>
        extends AbstractContent<E, D, I>
{
    @Override
    public void paint ( final Graphics2D g2d, final Rectangle bounds, final E c, final D d )
    {
        // Ensure that text painting is allowed
        if ( isDisplayed ( c, d ) )
        {
            // Ensure text is not empty
            final String text = getText ( c, d );
            if ( text != null )
            {
                // todo
            }
        }
    }

    /**
     * Returns whether or not text should be painted.
     *
     * @param c painted component
     * @param d painted decoration state
     * @return true if text should be painted, false otherwise
     */
    protected boolean isDisplayed ( final E c, final D d )
    {
        return true;
    }

    /**
     * Returns icon to be painted near text.
     *
     * @param c painted component
     * @param d painted decoration state
     * @return icon to be painted near text
     */
    protected abstract Icon getIcon ( E c, D d );

    /**
     * Returns text to be painted.
     *
     * @param c painted component
     * @param d painted decoration state
     * @return text to be painted
     */
    protected abstract String getText ( E c, D d );
}