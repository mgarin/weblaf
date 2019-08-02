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

import com.alee.painter.decoration.DecorationException;
import com.alee.painter.decoration.IDecoration;
import com.alee.utils.CollectionUtils;
import com.alee.utils.GraphicsUtils;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Abstract {@link IBackground} implementation that uses multiple other {@link IBackground} to paint them within custom clip constraints.
 * It is basically useful if you want to clip original {@link Shape}
 *
 * @param <C> component type
 * @param <D> decoration type
 * @param <I> background type
 * @author Mikle Garin
 */
public abstract class AbstractClipBackground<C extends JComponent, D extends IDecoration<C, D>, I extends AbstractClipBackground<C, D, I>>
        extends AbstractBackground<C, D, I>
{
    /**
     * {@link List} of actual {@link IBackground} implementations which are used to paint background.
     */
    @XStreamImplicit
    private List<IBackground> backgrounds;

    /**
     * Returns {@link List} of actual {@link IBackground} implementations which are used to paint background.
     *
     * @return {@link List} of actual {@link IBackground} implementations which are used to paint background
     */
    protected List<IBackground> backgrounds ()
    {
        if ( CollectionUtils.isEmpty ( backgrounds ) )
        {
            throw new DecorationException ( "At least one background implementation must be specified" );
        }
        return backgrounds;
    }

    @Override
    public void activate ( final C c, final D d )
    {
        for ( final IBackground background : backgrounds () )
        {
            background.activate ( c, d );
        }
    }

    @Override
    public void deactivate ( final C c, final D d )
    {
        for ( final IBackground background : backgrounds () )
        {
            background.deactivate ( c, d );
        }
    }

    @Override
    public void paint ( final Graphics2D g2d, final Rectangle bounds, final C c, final D d, final Shape shape )
    {
        final float opacity = getOpacity ();
        if ( opacity > 0 )
        {
            final Composite oc = GraphicsUtils.setupAlphaComposite ( g2d, opacity, opacity < 1f );
            final Shape clipped = clip ( g2d, bounds, c, d, shape );
            for ( final IBackground background : backgrounds () )
            {
                background.paint ( g2d, bounds, c, d, clipped );
            }
            GraphicsUtils.restoreComposite ( g2d, oc, opacity < 1f );
        }
    }

    /**
     * Returns {@link Shape} which is a result of clipping original {@link Shape}.
     *
     * @param g2d    {@link Graphics2D}
     * @param bounds painting bounds
     * @param c      {@link JComponent} that is being painted
     * @param d      {@link IDecoration} state
     * @param shape  {@link Shape} of the painted element
     * @return {@link Shape} which is a result of clipping original {@link Shape}
     */
    protected abstract Shape clip ( Graphics2D g2d, Rectangle bounds, C c, D d, Shape shape );
}