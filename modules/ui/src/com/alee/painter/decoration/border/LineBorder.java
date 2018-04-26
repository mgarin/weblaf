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

package com.alee.painter.decoration.border;

import com.alee.painter.decoration.IDecoration;
import com.alee.utils.GraphicsUtils;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

import javax.swing.*;
import java.awt.*;

/**
 * Simple line border implementation.
 *
 * @param <C> component type
 * @param <D> decoration type
 * @param <I> border type
 * @author Mikle Garin
 */
@XStreamAlias ( "LineBorder" )
public class LineBorder<C extends JComponent, D extends IDecoration<C, D>, I extends LineBorder<C, D, I>> extends AbstractBorder<C, D, I>
{
    /**
     * Border stroke.
     */
    @XStreamAsAttribute
    protected Stroke stroke;

    /**
     * Border color.
     */
    @XStreamAsAttribute
    protected Color color;

    /**
     * Returns {@link Stroke} used for this border.
     *
     * @return {@link Stroke} used for this border
     */
    public Stroke getStroke ()
    {
        return stroke;
    }

    /**
     * Returns {@link Color} used for this border.
     *
     * @return {@link Color} used for this border
     */
    public Color getColor ()
    {
        return color != null ? color : new Color ( 210, 210, 210 );
    }

    @Override
    public BorderWidth getWidth ()
    {
        final float t = getOpacity ();
        final Stroke s = getStroke ();
        final float w = t > 0 ? s != null && s instanceof BasicStroke ? ( ( BasicStroke ) s ).getLineWidth () : 1 : 0;
        final int bw = Math.round ( w );
        return new BorderWidth ( bw, bw, bw, bw );
    }

    @Override
    public void paint ( final Graphics2D g2d, final Rectangle bounds, final C c, final D d, final Shape shape )
    {
        final float opacity = getOpacity ();
        if ( opacity > 0 && !getWidth ().isEmpty () )
        {
            final Stroke stroke = getStroke ();
            final Color color = getColor ();

            final Composite oc = GraphicsUtils.setupAlphaComposite ( g2d, opacity, opacity < 1f );
            final Stroke os = GraphicsUtils.setupStroke ( g2d, stroke, stroke != null );
            final Paint op = GraphicsUtils.setupPaint ( g2d, color, color != null );

            g2d.draw ( shape );

            GraphicsUtils.restorePaint ( g2d, op, color != null );
            GraphicsUtils.restoreStroke ( g2d, os, stroke != null );
            GraphicsUtils.restoreComposite ( g2d, oc, opacity < 1f );
        }
    }
}