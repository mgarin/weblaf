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
import com.alee.painter.decoration.DecorationException;
import com.alee.painter.decoration.IDecoration;
import com.alee.utils.GraphicsUtils;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

import javax.swing.*;
import java.awt.*;

/**
 * Simple dash focus content implementation.
 * This is a temporary content implementation required due to inability to use decoration within another decoration structure.
 *
 * @param <C> component type
 * @param <D> decoration type
 * @param <I> content type
 * @author Mikle Garin
 */
@XStreamAlias ( "DashFocus" )
public class DashFocus<C extends JComponent, D extends IDecoration<C, D>, I extends DashFocus<C, D, I>> extends AbstractContent<C, D, I>
{
    /**
     * todo 1. Remove this class in v1.3.0 update and replace with proper inner decoration
     */

    /**
     * Focus rounding.
     */
    @XStreamAsAttribute
    protected Integer round;

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

    @Nullable
    @Override
    public String getId ()
    {
        return id != null ? id : "dash-focus";
    }

    /**
     * Returns focus rounding.
     *
     * @param c painted component
     * @param d painted decoration state
     * @return focus rounding
     */
    protected int getRound ( final C c, final D d )
    {
        return round != null ? round : 0;
    }

    /**
     * Returns focus stroke.
     *
     * @param c painted component
     * @param d painted decoration state
     * @return focus stroke
     */
    public Stroke getStroke ( final C c, final D d )
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
    protected Color getColor ( final C c, final D d )
    {
        if ( color != null )
        {
            return color;
        }
        throw new DecorationException ( "Focus color must be specified" );
    }

    @Override
    public boolean isEmpty ( final C c, final D d )
    {
        return false;
    }

    @Override
    protected void paintContent ( final Graphics2D g2d, final C c, final D d, final Rectangle bounds )
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
    protected Dimension getContentPreferredSize ( final C c, final D d, final Dimension available )
    {
        return new Dimension ( 0, 0 );
    }
}