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
 * Simple dash focus content implementation.
 * todo Remove in v1.3.0 update and replace with proper decoration
 *
 * @param <E> component type
 * @param <D> decoration type
 * @param <I> content type
 * @author Mikle Garin
 */

@XStreamAlias ( "DashFocus" )
public class DashFocusContent<E extends JComponent, D extends IDecoration<E, D>, I extends DashFocusContent<E, D, I>>
        extends AbstractContent<E, D, I>
{
    /**
     * Focus stroke.
     */
    @XStreamAsAttribute
    protected Stroke stroke;

    /**
     * Focus color.
     */
    @XStreamAsAttribute
    protected Color color;

    /**
     * Focus rounding.
     */
    @XStreamAsAttribute
    protected Integer round;

    @Override
    public String getId ()
    {
        return id != null ? id : "dash-focus";
    }

    /**
     * Returns focus stroke.
     *
     * @param c painted component
     * @param d painted decoration state
     * @return focus stroke
     */
    @SuppressWarnings ( "UnusedParameters" )
    public Stroke getStroke ( final E c, final D d )
    {
        return stroke;
    }

    /**
     * Returns focus color.
     *
     * @param c painted component
     * @param d painted decoration state
     * @return focus color
     */
    @SuppressWarnings ( "UnusedParameters" )
    protected Color getColor ( final E c, final D d )
    {
        if ( color != null )
        {
            return color;
        }
        else
        {
            throw new DecorationException ( "Focus color must be specified" );
        }
    }

    /**
     * Returns focus rounding.
     *
     * @param c painted component
     * @param d painted decoration state
     * @return focus rounding
     */
    @SuppressWarnings ( "UnusedParameters" )
    protected int getRound ( final E c, final D d )
    {
        return round != null ? round : 0;
    }

    @Override
    public boolean isEmpty ( final E c, final D d )
    {
        return false;
    }

    @Override
    protected void paintContent ( final Graphics2D g2d, final Rectangle bounds, final E c, final D d )
    {
        final Stroke stroke = getStroke ( c, d );
        final Stroke os = GraphicsUtils.setupStroke ( g2d, stroke, stroke != null );
        final Object aa = GraphicsUtils.setupAntialias ( g2d );
        final Paint op = GraphicsUtils.setupPaint ( g2d, getColor ( c, d ) );

        final int round = getRound ( c, d );
        g2d.drawRoundRect ( bounds.x, bounds.y, bounds.width - 1, bounds.height - 1, round, round );

        GraphicsUtils.restorePaint ( g2d, op );
        GraphicsUtils.restoreAntialias ( g2d, aa );
        GraphicsUtils.restoreStroke ( g2d, os, stroke != null );
    }

    @Override
    protected Dimension getContentPreferredSize ( final E c, final D d, final Dimension available )
    {
        return new Dimension ( 0, 0 );
    }

    @Override
    public I merge ( final I content )
    {
        super.merge ( content );
        stroke = content.isOverwrite () || content.stroke != null ? content.stroke : stroke;
        color = content.isOverwrite () || content.color != null ? content.color : color;
        round = content.isOverwrite () || content.round != null ? content.round : round;
        return ( I ) this;
    }
}