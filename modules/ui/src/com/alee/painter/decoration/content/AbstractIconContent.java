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
 * Abstract implementation of simple icon content.
 *
 * @param <E> component type
 * @param <D> decoration type
 * @param <I> content type
 * @author Mikle Garin
 */

public abstract class AbstractIconContent<E extends JComponent, D extends IDecoration<E, D>, I extends AbstractIconContent<E, D, I>>
        extends AbstractContent<E, D, I>
{
    @Override
    public String getId ()
    {
        return id != null ? id : "icon";
    }

    @Override
    public boolean isEmpty ( final E c, final D d )
    {
        return getIcon ( c, d ) == null;
    }

    @Override
    public void paint ( final Graphics2D g2d, final Rectangle bounds, final E c, final D d )
    {
        final Icon icon = getIcon ( c, d );
        if ( icon != null )
        {
            final int w = icon.getIconWidth ();
            final int h = icon.getIconHeight ();
            final int x = bounds.x + ( bounds.width > w ? bounds.width / 2 - w / 2 : 0 );
            final int y = bounds.y + ( bounds.height > h ? bounds.height / 2 - h / 2 : 0 );
            icon.paintIcon ( c, g2d, x, y );
        }
    }

    @Override
    public Dimension getPreferredSize ( final E c, final D d, Dimension available )
    {
        final Icon icon = getIcon ( c, d );
        return icon != null ? new Dimension ( icon.getIconWidth (), icon.getIconHeight () ) : new Dimension ( 0, 0 );
    }

    /**
     * Returns icon to paint.
     *
     * @param c painted component
     * @param d painted decoration state
     * @return icon to paint
     */
    protected abstract Icon getIcon ( E c, D d );
}