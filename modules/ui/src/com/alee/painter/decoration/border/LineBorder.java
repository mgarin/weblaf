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

import com.alee.api.annotations.NotNull;
import com.alee.api.annotations.Nullable;
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
    @Nullable
    @XStreamAsAttribute
    protected Stroke stroke;

    /**
     * Border color.
     */
    @Nullable
    @XStreamAsAttribute
    protected Color color;

    /**
     * Returns {@link Stroke} used for this border.
     *
     * @return {@link Stroke} used for this border
     */
    @Nullable
    public Stroke getStroke ()
    {
        return stroke;
    }

    /**
     * Returns {@link Color} used for this border.
     *
     * @return {@link Color} used for this border
     */
    @Nullable
    public Color getColor ()
    {
        return color;
    }

    @NotNull
    @Override
    public BorderWidth getWidth ()
    {
        final float opacity = getOpacity ();
        final Stroke stroke = getStroke ();
        final float lineWidth = opacity > 0 ? stroke instanceof BasicStroke ? ( ( BasicStroke ) stroke ).getLineWidth () : 1 : 0;
        final int width = Math.round ( lineWidth );
        return new BorderWidth ( width, width, width, width );
    }

    @Override
    public void paint ( @NotNull final Graphics2D g2d, @NotNull final Rectangle bounds, @NotNull final C c, @NotNull final D d,
                        @NotNull final Shape shape )
    {
        final float opacity = getOpacity ();
        if ( opacity > 0 && !getWidth ().isEmpty () )
        {
            final Color color = getColor ();
            if ( color != null )
            {
                final Stroke stroke = getStroke ();

                final Composite oc = GraphicsUtils.setupAlphaComposite ( g2d, opacity, opacity < 1f );
                final Stroke os = GraphicsUtils.setupStroke ( g2d, stroke, stroke != null );
                final Paint op = GraphicsUtils.setupPaint ( g2d, color );

                g2d.draw ( shape );

                GraphicsUtils.restorePaint ( g2d, op );
                GraphicsUtils.restoreStroke ( g2d, os, stroke != null );
                GraphicsUtils.restoreComposite ( g2d, oc, opacity < 1f );
            }
        }
    }
}