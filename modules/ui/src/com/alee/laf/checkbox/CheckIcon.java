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

package com.alee.laf.checkbox;

import com.alee.api.annotations.NotNull;
import com.alee.api.annotations.Nullable;
import com.alee.painter.decoration.DecorationException;
import com.alee.painter.decoration.IDecoration;
import com.alee.painter.decoration.content.AbstractContent;
import com.alee.utils.GraphicsUtils;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.GeneralPath;

/**
 * Checked state icon content for {@link AbstractButton} component.
 *
 * @param <C> component type
 * @param <D> decoration type
 * @param <I> content type
 * @author Mikle Garin
 */
@XStreamAlias ( "CheckIcon" )
public class CheckIcon<C extends AbstractButton, D extends IDecoration<C, D>, I extends CheckIcon<C, D, I>> extends AbstractContent<C, D, I>
{
    /**
     * Shape {@link Stroke}.
     */
    @Nullable
    @XStreamAsAttribute
    protected Stroke stroke;

    /**
     * Shape {@link Color}.
     */
    @Nullable
    @XStreamAsAttribute
    protected Color color;

    @NotNull
    @Override
    public String getId ()
    {
        return id != null ? id : "check";
    }

    @Override
    public boolean isEmpty ( @NotNull final C c, @NotNull final D d )
    {
        return false;
    }

    /**
     * Returns shape {@link Stroke}.
     *
     * @param c {@link AbstractButton} that is being painted
     * @param d {@link IDecoration} state
     * @return shape {@link Stroke}
     */
    @Nullable
    public Stroke getStroke ( @NotNull final C c, @NotNull final D d )
    {
        return stroke;
    }

    /**
     * Returns shape {@link Color}.
     *
     * @param c {@link AbstractButton} that is being painted
     * @param d {@link IDecoration} state
     * @return shape {@link Color}
     */
    @NotNull
    public Color getColor ( @NotNull final C c, @NotNull final D d )
    {
        if ( color == null )
        {
            throw new DecorationException ( "Shape color must be specified" );
        }
        return color;
    }

    @Override
    protected void paintContent ( @NotNull final Graphics2D g2d, @NotNull final C c, @NotNull final D d, @NotNull final Rectangle bounds )
    {
        final Stroke os = GraphicsUtils.setupStroke ( g2d, stroke, stroke != null );
        final Paint op = GraphicsUtils.setupPaint ( g2d, color );

        final GeneralPath gp = new GeneralPath ();
        gp.moveTo ( bounds.x + bounds.width * 0.1875, bounds.y + bounds.height * 0.375 );
        gp.lineTo ( bounds.x + bounds.width * 0.4575, bounds.y + bounds.height * 0.6875 );
        gp.lineTo ( bounds.x + bounds.width * 0.875, bounds.y + bounds.height * 0.125 );
        g2d.draw ( gp );

        GraphicsUtils.restorePaint ( g2d, op );
        GraphicsUtils.restoreStroke ( g2d, os );
    }

    @NotNull
    @Override
    protected Dimension getContentPreferredSize ( @NotNull final C c, @NotNull final D d, @NotNull final Dimension available )
    {
        return new Dimension ( 0, 0 );
    }
}