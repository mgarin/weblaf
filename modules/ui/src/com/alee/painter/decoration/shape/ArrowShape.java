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

import com.alee.api.annotations.NotNull;
import com.alee.api.data.CompassDirection;
import com.alee.api.jdk.Supplier;
import com.alee.painter.decoration.WebDecoration;
import com.alee.utils.ShapeUtils;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.GeneralPath;

/**
 * Arrow shape implementation.
 *
 * @param <C> component type
 * @param <D> decoration type
 * @param <I> shape type
 * @author Mikle Garin
 */
@XStreamAlias ( "ArrowShape" )
public class ArrowShape<C extends JComponent, D extends WebDecoration<C, D>, I extends ArrowShape<C, D, I>> extends AbstractShape<C, D, I>
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
    public CompassDirection getDirection ( final C c )
    {
        return direction != null ? direction.adjust ( c.getComponentOrientation () ) : CompassDirection.north;
    }

    @NotNull
    @Override
    public Shape getShape ( final ShapeType type, final Rectangle bounds, final C c, final D d )
    {
        final CompassDirection direction = getDirection ( c );
        return ShapeUtils.getShape ( c, "ArrowShape." + type, new Supplier<Shape> ()
        {
            @Override
            public Shape get ()
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
    public Object[] getShapeSettings ( final Rectangle bounds, final C c, final D d )
    {
        return new Object[]{ getDirection ( c ) };
    }
}