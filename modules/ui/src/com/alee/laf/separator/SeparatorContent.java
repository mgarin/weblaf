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

package com.alee.laf.separator;

import com.alee.painter.decoration.IDecoration;
import com.alee.painter.decoration.content.AbstractContent;
import com.thoughtworks.xstream.annotations.XStreamAlias;

import javax.swing.*;
import java.awt.*;

/**
 * Custom separator content representing multiple parallel separator lines.
 *
 * @param <E> component type
 * @param <D> decoration type
 * @param <I> content type
 * @author Mikle Garin
 */

@XStreamAlias ( "Separator" )
public class SeparatorContent<E extends JSeparator, D extends IDecoration<E, D>, I extends SeparatorContent<E, D, I>>
        extends AbstractContent<E, D, I>
{
    /**
     * Separator lines descriptor.
     */
    protected SeparatorLines lines;

    @Override
    public boolean isEmpty ( final E c, final D d )
    {
        return lines == null || lines.isEmpty ();
    }

    @Override
    protected void paintContent ( final Graphics2D g2d, final Rectangle bounds, final E c, final D d )
    {
        lines.paint ( g2d, bounds, c.getOrientation (), isLeftToRight ( c, d ) );
    }

    @Override
    protected Dimension getContentPreferredSize ( final E c, final D d, final Dimension available )
    {
        return lines != null ? lines.getPreferredSize ( c.getOrientation () ) : new Dimension ( 0, 0 );
    }

    @Override
    public I merge ( final I content )
    {
        super.merge ( content );
        lines = content.isOverwrite () || content.lines != null ? content.lines : lines;
        return ( I ) this;
    }
}