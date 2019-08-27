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

import com.alee.api.annotations.NotNull;
import com.alee.api.annotations.Nullable;
import com.alee.painter.decoration.IDecoration;

import javax.swing.*;
import java.awt.*;

/**
 * Abstract icon {@link IContent} implementation.
 * Override {@link #getIcon(JComponent, IDecoration)} method to return displayed icon.
 *
 * @param <C> component type
 * @param <D> decoration type
 * @param <I> content type
 * @author Mikle Garin
 */
public abstract class AbstractIconContent<C extends JComponent, D extends IDecoration<C, D>, I extends AbstractIconContent<C, D, I>>
        extends AbstractContent<C, D, I>
{
    @Nullable
    @Override
    public String getId ()
    {
        return id != null ? id : "icon";
    }

    @Override
    public boolean isEmpty ( final C c, final D d )
    {
        return getIcon ( c, d ) == null;
    }

    @Override
    protected void paintContent ( final Graphics2D g2d, final C c, final D d, final Rectangle bounds )
    {
        final Icon icon = getIcon ( c, d );
        if ( icon != null )
        {
            final int x = bounds.x + bounds.width / 2 - icon.getIconWidth () / 2;
            final int y = bounds.y + bounds.height / 2 - icon.getIconHeight () / 2;
            icon.paintIcon ( c, g2d, x, y );
        }
    }

    @Override
    protected Dimension getContentPreferredSize ( final C c, final D d, final Dimension available )
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
    protected abstract Icon getIcon ( C c, D d );
}