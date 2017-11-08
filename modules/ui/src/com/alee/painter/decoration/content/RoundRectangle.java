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

import com.alee.painter.decoration.DecorationException;
import com.alee.painter.decoration.IDecoration;
import com.alee.utils.GraphicsUtils;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

import javax.swing.*;
import java.awt.*;

/**
 * Simple round rectangle shape content implementation.
 * This is a temporary content implementation required due to inability to use decoration within another decoration structure.
 *
 * @param <E> component type
 * @param <D> decoration type
 * @param <I> content type
 * @author Mikle Garin
 */

@XStreamAlias ( "RoundRectangle" )
public class RoundRectangle<E extends JComponent, D extends IDecoration<E, D>, I extends RoundRectangle<E, D, I>>
        extends AbstractContent<E, D, I>
{
    /**
     * todo 1. Remove this class in v1.3.0 update and replace with proper inner decoration
     */

    /**
     * Corners rounding.
     */
    @XStreamAsAttribute
    protected Integer round;

    /**
     * Background color.
     */
    @XStreamAsAttribute
    protected Color color;

    @Override
    public String getId ()
    {
        return id != null ? id : "background";
    }

    /**
     * Returns background rounding.
     *
     * @param c painted component
     * @param d painted decoration state
     * @return background rounding
     */
    @SuppressWarnings ( "UnusedParameters" )
    protected int getRound ( final E c, final D d )
    {
        return round != null ? round : 0;
    }

    /**
     * Returns background color.
     *
     * @param c painted component
     * @param d painted decoration state
     * @return background color
     */
    @SuppressWarnings ( "UnusedParameters" )
    protected Color getColor ( final E c, final D d )
    {
        if ( color != null )
        {
            return color;
        }
        throw new DecorationException ( "Background color must be specified" );
    }

    @Override
    public boolean isEmpty ( final E c, final D d )
    {
        return false;
    }

    @Override
    protected void paintContent ( final Graphics2D g2d, final E c, final D d, final Rectangle bounds )
    {
        final Object aa = GraphicsUtils.setupAntialias ( g2d );
        final Paint op = GraphicsUtils.setupPaint ( g2d, getColor ( c, d ) );

        final int round = getRound ( c, d );
        g2d.fillRoundRect ( bounds.x, bounds.y, bounds.width, bounds.height, round, round );

        GraphicsUtils.restorePaint ( g2d, op );
        GraphicsUtils.restoreAntialias ( g2d, aa );
    }

    @Override
    protected Dimension getContentPreferredSize ( final E c, final D d, final Dimension available )
    {
        final Insets padding = getPadding ( c, d );
        final int round = getRound ( c, d );
        final int phor = padding != null ? padding.left + padding.right : 0;
        final int pver = padding != null ? padding.top + padding.bottom : 0;
        final int w = Math.max ( phor, round * 2 );
        final int h = Math.max ( pver, round * 2 );
        return new Dimension ( w, h );
    }

    @Override
    public I merge ( final I content )
    {
        super.merge ( content );
        round = content.isOverwrite () || content.round != null ? content.round : round;
        color = content.isOverwrite () || content.color != null ? content.color : color;
        return ( I ) this;
    }
}