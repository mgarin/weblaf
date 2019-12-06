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

package com.alee.extended.checkbox;

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

/**
 * Mixed state icon content for any {@link AbstractButton} component.
 *
 * @param <C> component type
 * @param <D> decoration type
 * @param <I> content type
 * @author Mikle Garin
 */
@XStreamAlias ( "MixedIcon" )
public class MixedIcon<C extends AbstractButton, D extends IDecoration<C, D>, I extends MixedIcon<C, D, I>> extends AbstractContent<C, D, I>
{
    /**
     * Shape rounding.
     */
    @Nullable
    @XStreamAsAttribute
    protected Integer round;

    /**
     * Left side background {@link Color}.
     */
    @Nullable
    @XStreamAsAttribute
    protected Color leftColor;

    /**
     * Right side background {@link Color}.
     */
    @Nullable
    @XStreamAsAttribute
    protected Color rightColor;

    @NotNull
    @Override
    public String getId ()
    {
        return id != null ? id : "mixed";
    }

    @Override
    public boolean isEmpty ( @NotNull final C c, @NotNull final D d )
    {
        return false;
    }

    /**
     * Returns shape rounding.
     *
     * @param c {@link AbstractButton} that is being painted
     * @param d {@link IDecoration} state
     * @return shape rounding
     */
    public int getRound ( @NotNull final C c, @NotNull final D d )
    {
        if ( round == null )
        {
            throw new DecorationException ( "Shape round must be specified" );
        }
        return round;
    }

    /**
     * Returns left side background {@link Color}.
     *
     * @param c {@link AbstractButton} that is being painted
     * @param d {@link IDecoration} state
     * @return left side background {@link Color}
     */
    @NotNull
    public Color getLeftColor ( @NotNull final C c, @NotNull final D d )
    {
        if ( leftColor == null )
        {
            throw new DecorationException ( "Left side background color must be specified" );
        }
        return leftColor;
    }

    /**
     * Returns right side background {@link Color}.
     *
     * @param c {@link AbstractButton} that is being painted
     * @param d {@link IDecoration} state
     * @return right side background {@link Color}
     */
    @NotNull
    public Color getRightColor ( @NotNull final C c, @NotNull final D d )
    {
        if ( rightColor == null )
        {
            throw new DecorationException ( "Right side background color must be specified" );
        }
        return rightColor;
    }

    @Override
    protected void paintContent ( @NotNull final Graphics2D g2d, @NotNull final C c, @NotNull final D d, @NotNull final Rectangle bounds )
    {
        final int x = bounds.x + 2;
        final int w = bounds.width - 4;

        final Paint op = GraphicsUtils.setupPaint ( g2d, new GradientPaint (
                x, 0,
                getLeftColor ( c, d ),
                x + w, 0,
                getRightColor ( c, d )
        ) );

        final int round = getRound ( c, d );
        g2d.fillRoundRect ( x, bounds.y + 2, w, bounds.height - 4, round, round );

        GraphicsUtils.restorePaint ( g2d, op );
    }

    @NotNull
    @Override
    protected Dimension getContentPreferredSize ( @NotNull final C c, @NotNull final D d, @NotNull final Dimension available )
    {
        return new Dimension ( 0, 0 );
    }
}