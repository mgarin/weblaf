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

package com.alee.painter.decoration.shape;

import com.alee.painter.decoration.WebDecoration;
import com.alee.painter.decoration.states.CompassDirection;
import com.alee.utils.ShapeUtils;
import com.alee.utils.swing.DataProvider;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.GeneralPath;

/**
 * Arrow shape implementation.
 *
 * @param <E> component type
 * @param <D> decoration type
 * @param <I> shape type
 * @author Mikle Garin
 */

@XStreamAlias ( "ArrowShape" )
public class ArrowShape<E extends JComponent, D extends WebDecoration<E, D>, I extends ArrowShape<E, D, I>> extends AbstractShape<E, D, I>
{
    /**
     * Arrow corner direction.
     * Only north, east, south and west directions are supported.
     */
    @XStreamAsAttribute
    protected CompassDirection direction;

    /**
     * Returns corner direction.
     *
     * @param c component
     * @return corner direction
     */
    public CompassDirection getDirection ( final E c )
    {
        return direction != null ? direction.adjust ( c.getComponentOrientation () ) : CompassDirection.north;
    }

    @Override
    public Insets getBorderInsets ( final E c, final D d )
    {
        return null;
    }

    @Override
    public Shape getShape ( final ShapeType type, final Rectangle bounds, final E c, final D d )
    {
        final CompassDirection direction = getDirection ( c );
        return ShapeUtils.getShape ( c, "ArrowShape." + type, new DataProvider<Shape> ()
        {
            @Override
            public Shape provide ()
            {
                return createArrowButtonShape ( type, bounds, direction );
            }
        }, type, bounds, direction );
    }

    /**
     * Returns arrow shape.
     *
     * @param type      requested shape type
     * @param bounds    painting bounds
     * @param direction arrow direction
     * @return arrow shape
     */
    protected Shape createArrowButtonShape ( final ShapeType type, final Rectangle bounds, final CompassDirection direction )
    {
        final int x = bounds.x;
        final int y = bounds.y;
        final int w = bounds.width + ( type == ShapeType.border ? -1 : 0 );
        final int h = bounds.height + ( type == ShapeType.border ? -1 : 0 );

        final GeneralPath shape = new GeneralPath ( GeneralPath.WIND_EVEN_ODD );
        switch ( direction )
        {
            case north:
            {
                shape.moveTo ( x, y + h );
                shape.quadTo ( x + w / 2f, y + h * 2 / 3f, x + w, y + h );
                shape.lineTo ( x + w / 2f, y );
                break;
            }
            case south:
            {
                shape.moveTo ( x, y );
                shape.quadTo ( x + w / 2f, y + h / 3f, x + w, y );
                shape.lineTo ( x + w / 2f, y + h );
                break;
            }
            case west:
            {
                shape.moveTo ( x + w, y );
                shape.quadTo ( x + w * 2 / 3f, y + h / 2f, x + w, y + h );
                shape.lineTo ( x, y + h / 2f );
                break;
            }
            case east:
            {
                shape.moveTo ( x, y );
                shape.quadTo ( x + w / 3f, y + h / 2f, x, y + h );
                shape.lineTo ( x + w, y + h / 2f );
                break;
            }
        }
        shape.closePath ();

        return shape;
    }

    @Override
    public Object[] getShapeSettings ( final Rectangle bounds, final E c, final D d )
    {
        return new Object[]{ getDirection ( c ) };
    }

    @Override
    public I merge ( final I shape )
    {
        super.merge ( shape );
        if ( shape.direction != null )
        {
            direction = shape.direction;
        }
        return ( I ) this;
    }
}