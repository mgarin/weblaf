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

import com.alee.api.data.Orientation;
import com.alee.painter.decoration.DecorationUtils;
import com.alee.painter.decoration.IDecoration;
import com.alee.painter.decoration.background.GradientColor;
import com.alee.painter.decoration.background.GradientType;
import com.alee.utils.GraphicsUtils;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Simple line content implementation.
 *
 * @param <E> component type
 * @param <D> decoration type
 * @param <I> content type
 * @author Mikle Garin
 */

@XStreamAlias ( "Line" )
public class LineContent<E extends JComponent, D extends IDecoration<E, D>, I extends AbstractContent<E, D, I>>
        extends AbstractContent<E, D, I>
{
    /**
     * Empty space between line and its bounds sides.
     */
    @XStreamAsAttribute
    protected Insets padding;

    /**
     * Line orientation.
     */
    @XStreamAsAttribute
    protected Orientation orientation;

    /**
     * Line colors.
     * Single or multiple colors must be provided.
     */
    @XStreamImplicit ( itemFieldName = "color" )
    private List<GradientColor> colors;

    @Override
    public boolean isEmpty ( final E c, final D d )
    {
        return false;
    }

    @Override
    public void paint ( final Graphics2D g2d, final Rectangle bounds, final E c, final D d )
    {
        final int top = padding != null ? padding.top : 0;
        final int left = padding != null ? padding.left : 0;
        final int bottom = padding != null ? padding.top + padding.bottom : 0;
        final int right = padding != null ? padding.left + padding.right : 0;
        final int x1 = bounds.x + left;
        final int y1 = bounds.y + top;
        final int x2 = orientation == Orientation.horizontal ? x1 + bounds.width - right - 1 : x1;
        final int y2 = orientation == Orientation.horizontal ? y1 : y1 + bounds.height - bottom - 1;
        final Paint paint = DecorationUtils.getPaint ( GradientType.linear, colors, x1, y1, x2, y2 );
        final Paint op = GraphicsUtils.setupPaint ( g2d, paint );
        g2d.drawLine ( x1, y1, x2, y2 );
        GraphicsUtils.restorePaint ( g2d, op );
    }

    @Override
    public Dimension getPreferredSize ( final E c, final D d )
    {
        final int w = 1 + ( padding != null ? padding.left + padding.right : 0 );
        final int h = 1 + ( padding != null ? padding.top + padding.bottom : 0 );
        return new Dimension ( w, h );
    }
}