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

package com.alee.painter.decoration.background;

import com.alee.painter.decoration.IDecoration;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

import javax.swing.*;
import java.awt.*;

/**
 * Background with a moving highlight.
 *
 * @param <E> component type
 * @param <D> decoration type
 * @param <I> background type
 * @author Mikle Garin
 */

@XStreamAlias ( "MovingHighlightBackground" )
public class MovingHighlightBackground<E extends JComponent, D extends IDecoration<E, D>, I extends MovingHighlightBackground<E, D, I>>
        extends AbstractBackground<E, D, I>
{
    /**
     * Highlight color.
     */
    @XStreamAsAttribute
    protected Color color;

    /**
     * Returns highlight color.
     *
     * @return highlight color
     */
    public Color getColor ()
    {
        return color != null ? color : Color.WHITE;
    }

    @Override
    public void paint ( final Graphics2D g2d, final Rectangle bounds, final E c, final D d, final Shape shape )
    {
        final float opacity = getOpacity ();
        if ( opacity > 0 )
        {
            //            final Composite oc = GraphicsUtils.setupAlphaComposite ( g2d, opacity, opacity < 1f );
            //            final Paint op = GraphicsUtils.setupPaint ( g2d, getColor () );
            //            g2d.fill ( shape );
            //            GraphicsUtils.restorePaint ( g2d, op );
            //            GraphicsUtils.restoreComposite ( g2d, oc, opacity < 1f );
        }
    }

    @Override
    public I merge ( final I background )
    {
        super.merge ( background );
        if ( background.color != null )
        {
            color = background.color;
        }
        return ( I ) this;
    }
}