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

import com.alee.api.data.BoxOrientation;
import com.alee.api.data.Orientation;
import com.alee.graphics.data.Line;
import com.alee.painter.decoration.DecorationException;
import com.alee.painter.decoration.DecorationUtils;
import com.alee.painter.decoration.IDecoration;
import com.alee.painter.decoration.background.GradientColor;
import com.alee.painter.decoration.background.GradientType;
import com.alee.utils.CompareUtils;
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
 * todo Remove in v1.3.0 update and replace with proper decoration
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
     * Line orientation.
     */
    @XStreamAsAttribute
    protected Orientation orientation;

    /**
     * Line alignment.
     * It can contain different values depending on {@link #orientation}.
     */
    @XStreamAsAttribute
    protected BoxOrientation align;

    /**
     * Line colors.
     * Single or multiple colors must be provided.
     */
    @XStreamImplicit ( itemFieldName = "color" )
    protected List<GradientColor> colors;

    @Override
    public String getId ()
    {
        return id != null ? id : "line";
    }

    /**
     * Returns line orientation.
     *
     * @param c painted component
     * @param d painted decoration state
     * @return line orientation
     */
    @SuppressWarnings ( "UnusedParameters" )
    protected Orientation getOrientation ( final E c, final D d )
    {
        if ( orientation != null )
        {
            return orientation;
        }
        else
        {
            throw new DecorationException ( "Line orientation must be specified" );
        }
    }

    /**
     * Returns line colors.
     *
     * @param c painted component
     * @param d painted decoration state
     * @return line colors
     */
    @SuppressWarnings ( "UnusedParameters" )
    protected List<GradientColor> getColors ( final E c, final D d )
    {
        if ( orientation != null )
        {
            return colors;
        }
        else
        {
            throw new DecorationException ( "At least one line color must be specified" );
        }
    }

    /**
     * Returns line alignment.
     *
     * @param c painted component
     * @param d painted decoration state
     * @return line alignment
     */
    @SuppressWarnings ( "UnusedParameters" )
    protected BoxOrientation getAlignment ( final E c, final D d )
    {
        if ( align != null )
        {
            if ( getOrientation ( c, d ).isVertical () )
            {
                if ( CompareUtils.equals ( align, BoxOrientation.left, BoxOrientation.center, BoxOrientation.right ) )
                {
                    return align;
                }
                else
                {
                    throw new DecorationException ( "Vertical line cannot use " + align + " alignment" );
                }
            }
            else
            {
                if ( CompareUtils.equals ( align, BoxOrientation.top, BoxOrientation.center, BoxOrientation.bottom ) )
                {
                    return align;
                }
                else
                {
                    throw new DecorationException ( "Vertical line cannot use " + align + " alignment" );
                }
            }
        }
        else
        {
            return BoxOrientation.center;
        }
    }

    @Override
    public boolean isEmpty ( final E c, final D d )
    {
        return false;
    }

    @Override
    protected void paintContent ( final Graphics2D g2d, final Rectangle bounds, final E c, final D d )
    {
        final Line line;
        final BoxOrientation align = getAlignment ( c, d );
        if ( getOrientation ( c, d ).isVertical () )
        {
            if ( align == BoxOrientation.left || bounds.width <= 2 )
            {
                line = new Line ( bounds.x, bounds.y, bounds.x, bounds.y + bounds.height - 1 );
            }
            else if ( align == BoxOrientation.right )
            {
                line = new Line ( bounds.x + bounds.width - 1, bounds.y, bounds.x + bounds.width - 1, bounds.y + bounds.height - 1 );
            }
            else
            {
                line = new Line ( bounds.x + bounds.width / 2, bounds.y, bounds.x + bounds.width / 2, bounds.y + bounds.height - 1 );
            }
        }
        else
        {
            if ( align == BoxOrientation.top || bounds.height <= 2 )
            {
                line = new Line ( bounds.x, bounds.y, bounds.x + bounds.width - 1, bounds.y );
            }
            else if ( align == BoxOrientation.bottom )
            {
                line = new Line ( bounds.x, bounds.y + bounds.height - 1, bounds.x + bounds.width - 1, bounds.y + bounds.height - 1 );
            }
            else
            {
                line = new Line ( bounds.x, bounds.y + bounds.height / 2, bounds.x + bounds.width - 1, bounds.y + bounds.height / 2 );
            }
        }
        final Paint paint = DecorationUtils.getPaint ( GradientType.linear, getColors ( c, d ), line.x1, line.y1, line.x2, line.y2 );
        final Paint op = GraphicsUtils.setupPaint ( g2d, paint );
        g2d.drawLine ( line.x1, line.y1, line.x2, line.y2 );
        GraphicsUtils.restorePaint ( g2d, op );
    }

    @Override
    protected Dimension getContentPreferredSize ( final E c, final D d, final Dimension available )
    {
        final Orientation orientation = getOrientation ( c, d );
        return new Dimension ( orientation.isVertical () ? 1 : 0, orientation.isHorizontal () ? 1 : 0 );
    }

    @Override
    public I merge ( final I content )
    {
        super.merge ( content );
        orientation = content.isOverwrite () || content.orientation != null ? content.orientation : orientation;
        align = content.isOverwrite () || content.align != null ? content.align : align;
        colors = content.isOverwrite () ? content.colors : MergeUtils.merge ( colors, content.colors );
        return ( I ) this;
    }
}