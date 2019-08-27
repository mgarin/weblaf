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
import com.alee.painter.decoration.background.IBackground;
import com.alee.utils.CollectionUtils;
import com.alee.utils.GraphicsUtils;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.util.List;

/**
 * Simple round rectangle shape content implementation.
 * This is a temporary content implementation required due to inability to use decoration within another decoration structure.
 *
 * @param <C> component type
 * @param <D> decoration type
 * @param <I> content type
 * @author Mikle Garin
 */
@XStreamAlias ( "RoundRectangle" )
public class RoundRectangle<C extends JComponent, D extends IDecoration<C, D>, I extends RoundRectangle<C, D, I>>
        extends AbstractContent<C, D, I>
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
     * Multiple backgrounds for this shape.
     */
    @XStreamImplicit
    protected List<IBackground> backgrounds;

    @Nullable
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
    protected int getRound ( final C c, final D d )
    {
        return round != null ? round : 0;
    }

    /**
     * Returns decoration {@link IBackground}s.
     *
     * @return decoration {@link IBackground}s
     */
    public List<IBackground> getBackgrounds ()
    {
        if ( CollectionUtils.isEmpty ( backgrounds ) )
        {
            throw new DecorationException ( "At least one Background must be specified" );
        }
        return backgrounds;
    }

    @Override
    public boolean isEmpty ( final C c, final D d )
    {
        return false;
    }

    @Override
    protected void paintContent ( final Graphics2D g2d, final C c, final D d, final Rectangle bounds )
    {
        final Object aa = GraphicsUtils.setupAntialias ( g2d );

        final int round = getRound ( c, d );
        final RoundRectangle2D shape = new RoundRectangle2D.Float ( bounds.x, bounds.y, bounds.width, bounds.height, round, round );
        for ( final IBackground background : getBackgrounds () )
        {
            background.paint ( g2d, bounds, c, d, shape );
        }

        GraphicsUtils.restoreAntialias ( g2d, aa );
    }

    @Override
    protected Dimension getContentPreferredSize ( final C c, final D d, final Dimension available )
    {
        final Insets padding = getPadding ( c, d );
        final int round = getRound ( c, d );
        final int phor = padding != null ? padding.left + padding.right : 0;
        final int pver = padding != null ? padding.top + padding.bottom : 0;
        final int w = Math.max ( phor, round * 2 );
        final int h = Math.max ( pver, round * 2 );
        return new Dimension ( w, h );
    }
}