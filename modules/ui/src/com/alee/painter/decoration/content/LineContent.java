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
import com.alee.utils.MergeUtils;
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
public class LineContent<E extends JComponent, D extends IDecoration<E, D>, I extends LineContent<E, D, I>> extends AbstractContent<E, D, I>
{
    /**
     * Line sides padding.
     * It exists for convenient lines usage.
     * Normally content implementations do not have their own padding.
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

    /**
     * Returns line sides padding.
     *
     * @return line sides padding
     */
    protected Insets getPadding ()
    {
        return padding != null ? padding : new Insets ( 0, 0, 0, 0 );
    }

    /**
     * Returns line orientation.
     *
     * @return line orientation
     */
    protected Orientation getOrientation ()
    {
        return orientation != null ? orientation : Orientation.vertical;
    }

    @Override
    public boolean isEmpty ( final E c, final D d )
    {
        return false;
    }

    @Override
    public void paint ( final Graphics2D g2d, final Rectangle bounds, final E c, final D d )
    {
        final Insets p = getPadding ();
        final Orientation orientation = getOrientation ();
        final int x1 = bounds.x + p.left;
        final int y1 = bounds.y + p.top;
        final int x2 = orientation.isHorizontal () ? x1 + bounds.width - p.right - 1 : x1;
        final int y2 = orientation.isVertical () ? y1 + bounds.height - p.bottom - 1 : y1;
        final Paint paint = DecorationUtils.getPaint ( GradientType.linear, colors, x1, y1, x2, y2 );
        final Paint op = GraphicsUtils.setupPaint ( g2d, paint );
        g2d.drawLine ( x1, y1, x2, y2 );
        GraphicsUtils.restorePaint ( g2d, op );
    }

    @Override
    public Dimension getPreferredSize ( final E c, final D d, final Dimension available )
    {
        final Insets p = getPadding ();
        final Orientation orientation = getOrientation ();
        final int w = p.left + ( orientation.isVertical () ? 1 : 0 ) + p.right;
        final int h = p.top + ( orientation.isHorizontal () ? 1 : 0 ) + p.bottom;
        return new Dimension ( w, h );
    }

    @Override
    public I merge ( final I content )
    {
        super.merge ( content );
        if ( content.padding != null )
        {
            padding = content.padding;
        }
        if ( content.orientation != null )
        {
            orientation = content.orientation;
        }
        colors = MergeUtils.merge ( colors, content.colors );
        return ( I ) this;
    }
}