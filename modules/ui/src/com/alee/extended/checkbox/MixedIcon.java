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

import com.alee.painter.decoration.DecorationState;
import com.alee.painter.decoration.IDecoration;
import com.alee.painter.decoration.content.AbstractContent;
import com.alee.utils.GraphicsUtils;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

import java.awt.*;
import java.awt.geom.RoundRectangle2D;

/**
 * Mixed state icon content for {@link com.alee.extended.checkbox.WebTristateCheckBox} component.
 *
 * @param <E> component type
 * @param <D> decoration type
 * @param <I> content type
 * @author Mikle Garin
 */

@XStreamAlias ( "MixedIcon" )
public class MixedIcon<E extends WebTristateCheckBox, D extends IDecoration<E, D>, I extends MixedIcon<E, D, I>>
        extends AbstractContent<E, D, I>
{
    @XStreamAsAttribute
    protected Integer round;

    @XStreamAsAttribute
    protected Color leftColor;

    @XStreamAsAttribute
    protected Color rightColor;

    @Override
    public String getId ()
    {
        return DecorationState.mixed;
    }

    @Override
    public void paint ( final Graphics2D g2d, final Rectangle bounds, final E c, final D d )
    {
        final int x = bounds.x + 2;
        final int y = bounds.y + 2;
        final int w = bounds.width - 4;
        final int h = bounds.height - 4;
        final RoundRectangle2D.Double shape = new RoundRectangle2D.Double ( x, y, w, h, round, round );

        final GradientPaint paint = new GradientPaint ( x, 0, leftColor, x + w, 0, rightColor );
        final Paint op = GraphicsUtils.setupPaint ( g2d, paint );

        g2d.fill ( shape );

        GraphicsUtils.restorePaint ( g2d, op );
    }

    @Override
    public I merge ( final I icon )
    {
        if ( icon.round != null )
        {
            round = icon.round;
        }
        if ( icon.leftColor != null )
        {
            leftColor = icon.leftColor;
        }
        if ( icon.rightColor != null )
        {
            rightColor = icon.rightColor;
        }
        return ( I ) this;
    }
}