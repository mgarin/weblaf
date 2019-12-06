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

package com.alee.laf.radiobutton;

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
import java.awt.geom.Ellipse2D;

/**
 * Checked state icon content for {@link AbstractButton} component.
 *
 * @param <C> component type
 * @param <D> decoration type
 * @param <I> content type
 * @author Mikle Garin
 */
@XStreamAlias ( "RadioIcon" )
public class RadioIcon<C extends AbstractButton, D extends IDecoration<C, D>, I extends RadioIcon<C, D, I>> extends AbstractContent<C, D, I>
{
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
        return id != null ? id : "radio";
    }

    @Override
    public boolean isEmpty ( @NotNull final C c, @NotNull final D d )
    {
        return false;
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
        // Configuring graphics
        final Paint op = GraphicsUtils.setupPaint ( g2d, new GradientPaint (
                bounds.x, 0,
                getLeftColor ( c, d ),
                bounds.x + bounds.width, 0,
                getRightColor ( c, d )
        ) );

        // Filling content shape
        g2d.fill ( new Ellipse2D.Double ( bounds.x, bounds.y, bounds.width, bounds.height ) );

        // Restoring graphics settings
        GraphicsUtils.restorePaint ( g2d, op );
    }

    @NotNull
    @Override
    protected Dimension getContentPreferredSize ( @NotNull final C c, @NotNull final D d, @NotNull final Dimension available )
    {
        return new Dimension ( 0, 0 );
    }
}