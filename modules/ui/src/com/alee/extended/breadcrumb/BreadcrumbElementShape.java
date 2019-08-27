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

package com.alee.extended.breadcrumb;

import com.alee.api.annotations.NotNull;
import com.alee.api.jdk.Supplier;
import com.alee.graphics.shapes.RelativeGeneralPath;
import com.alee.painter.decoration.DecorationException;
import com.alee.painter.decoration.WebDecoration;
import com.alee.painter.decoration.shadow.ShadowType;
import com.alee.painter.decoration.shape.*;
import com.alee.utils.ShapeUtils;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

import javax.swing.*;
import java.awt.*;

/**
 * Custom shape for {@link WebBreadcrumb} elements.
 *
 * @param <C> component type
 * @param <D> decoration type
 * @param <I> shape type
 * @author Mikle Garin
 */
@XStreamAlias ( "BreadcrumbElementShape" )
public class BreadcrumbElementShape<C extends JComponent, D extends WebDecoration<C, D>, I extends BreadcrumbElementShape<C, D, I>>
        extends AbstractShape<C, D, I> implements IPartialShape<C, D, I>
{
    /**
     * todo 1. Add proper stretch settings to preserve shadow between components of different width
     */

    /**
     * Displayed decoration sides.
     */
    @XStreamAsAttribute
    protected Sides sides;

    /**
     * Displayed decoration corners.
     */
    @XStreamAsAttribute
    protected Sides corners;

    /**
     * Decoration corners width.
     */
    @XStreamAsAttribute
    protected Integer cornerWidth;

    /**
     * Decoration corners rounding.
     */
    @XStreamAsAttribute
    protected Round round;

    /**
     * Returns displayed element sides.
     *
     * @return displayed element sides
     */
    public Sides sides ()
    {
        if ( sides == null )
        {
            throw new DecorationException ( "Element sides must be specified" );
        }
        return sides;
    }

    /**
     * Returns displayed element corners.
     *
     * @return displayed element corners
     */
    public Sides corners ()
    {
        if ( corners == null )
        {
            throw new DecorationException ( "Element corners must be specified" );
        }
        return corners;
    }

    /**
     * Returns corner type.
     *
     * @return corner type
     */
    public int cornerWidth ()
    {
        if ( cornerWidth == null )
        {
            throw new DecorationException ( "Corner width must be specified" );
        }
        return cornerWidth;
    }

    /**
     * Returns decoration corners rounding.
     *
     * @return decoration corners rounding
     */
    public Round round ()
    {
        return round != null ? round : new Round ();
    }

    @Override
    public boolean isPaintTop ( final C c, final D d )
    {
        return sides ().top;
    }

    @Override
    public boolean isPaintLeft ( final C c, final D d )
    {
        return sides ().left;
    }

    @Override
    public boolean isPaintBottom ( final C c, final D d )
    {
        return sides ().bottom;
    }

    @Override
    public boolean isPaintRight ( final C c, final D d )
    {
        return sides ().right;
    }

    @Override
    public boolean isPaintTopLine ( final C c, final D d )
    {
        return false;
    }

    @Override
    public boolean isPaintLeftLine ( final C c, final D d )
    {
        return false;
    }

    @Override
    public boolean isPaintBottomLine ( final C c, final D d )
    {
        return false;
    }

    @Override
    public boolean isPaintRightLine ( final C c, final D d )
    {
        return false;
    }

    @Override
    public boolean isVisible ( final ShapeType type, final Rectangle bounds, final C c, final D d )
    {
        final boolean visible;
        final Container parent = c.getParent ();
        if ( parent != null && parent instanceof WebBreadcrumb )
        {
            switch ( type )
            {
                /**
                 * We only have border and outer shadow whenever at least one side is displayed.
                 */
                case border:
                case outerShadow:
                    visible = sides ().isAny ();
                    break;

                /**
                 * Background and inner shadow are always visible.
                 */
                case background:
                case innerShadow:
                default:
                    visible = true;
                    break;
            }
        }
        else
        {
            /**
             * Whenever element is not added to {@link WebBreadcrumb} its shape is not available.
             * It is safer to return {@code false} here instead of adding multiple workarounds.
             */
            visible = false;
        }
        return visible;
    }

    @NotNull
    @Override
    public Shape getShape ( final ShapeType type, final Rectangle bounds, final C c, final D d )
    {
        // Provided settings
        final WebBreadcrumb breadcrumb = ( WebBreadcrumb ) c.getParent ();
        final int sw = d.getShadowWidth ( ShadowType.outer );
        final Sides sides = sides ();
        final Sides corners = corners ();
        final Round round = round ();
        final int cornerWidth = cornerWidth ();// overlap - sw;
        final int overlap = breadcrumb.getLayout ().overlap ();
        final boolean ltr = breadcrumb.getComponentOrientation ().isLeftToRight ();

        // Calculated settings
        final int bgShear = type.isBorder () ? -1 : 0;

        // Actual bounds for the shape
        final Rectangle b = new Rectangle (
                bounds.x + ( ( ltr ? sides.left : sides.right ) ? sw : 0 ),
                bounds.y + ( sides.top ? sw : 0 ),
                bounds.width - ( sides.left ? sw : 0 ) - ( sides.right ? sw : 0 ) + bgShear,
                bounds.height - ( sides.top ? sw : 0 ) - ( sides.bottom ? sw : 0 ) + bgShear
        );

        // Creating actual shape
        return ShapeUtils.getShape ( breadcrumb, "BreadcrumbElementShape." + type, new Supplier<Shape> ()
        {
            @Override
            public Shape get ()
            {
                final int yCenter = b.y + b.height / 2;
                final RelativeGeneralPath shape = new RelativeGeneralPath ( b, ltr );
                if ( sides.right || !type.isBorder () )
                {
                    if ( corners.right )
                    {
                        shape.moveTo ( b.width - cornerWidth, 0 );
                        shape.lineTo ( b.width, yCenter );
                        shape.lineTo ( b.width - cornerWidth, b.height );
                    }
                    else
                    {
                        if ( round.topRight > 0 )
                        {
                            shape.moveTo ( b.width - round.topRight, 0 );
                            shape.quadTo ( b.width, 0, b.width, round.topRight );
                        }
                        else
                        {
                            shape.moveTo ( b.width, 0 );
                        }
                        if ( round.bottomRight > 0 )
                        {
                            shape.lineTo ( b.width, b.height - round.bottomRight );
                            shape.quadTo ( b.width, b.height, b.width - round.bottomRight, b.height );
                        }
                        else
                        {
                            shape.lineTo ( b.width, b.height );
                        }
                    }
                }
                else
                {
                    if ( corners.right )
                    {
                        shape.moveTo ( b.width - cornerWidth, b.height );
                    }
                    else
                    {
                        shape.moveTo ( b.width, b.height );
                    }
                }
                if ( sides.bottom || !type.isBorder () )
                {
                    shape.lineTo ( 0, b.height );
                }
                else
                {
                    if ( corners.left )
                    {
                        shape.moveTo ( 0, b.height );
                    }
                    else
                    {
                        shape.moveTo ( round.bottomLeft, b.height );
                    }
                }
                if ( sides.left || !type.isBorder () )
                {
                    if ( corners.left )
                    {
                        shape.lineTo ( cornerWidth, yCenter );
                        shape.lineTo ( 0, 0 );
                    }
                    else
                    {
                        if ( round.bottomLeft > 0 )
                        {
                            shape.quadTo ( 0, b.height, 0, b.height - round.bottomLeft );
                        }
                        else
                        {
                            shape.lineTo ( 0, b.height );
                        }
                        if ( round.topLeft > 0 )
                        {
                            shape.lineTo ( 0, round.topLeft );
                            shape.quadTo ( 0, 0, round.topLeft, 0 );
                        }
                        else
                        {
                            shape.lineTo ( 0, 0 );
                        }
                    }
                }
                else
                {
                    shape.moveTo ( 0, 0 );
                }
                if ( sides.top || !type.isBorder () )
                {
                    shape.closePath ();
                }
                return shape;
            }
        }, bounds, sw, sides, corners, round, cornerWidth, overlap, ltr );
    }

    @Override
    public Object[] getShapeSettings ( final Rectangle bounds, final C c, final D d )
    {
        final WebBreadcrumb breadcrumb = ( WebBreadcrumb ) c.getParent ();
        final Sides sides = sides ();
        final Sides corners = corners ();
        final Round round = round ();
        final int cornerWidth = cornerWidth ();
        final int overlap = breadcrumb.getLayout ().overlap ();
        final boolean ltr = breadcrumb.getComponentOrientation ().isLeftToRight ();
        return new Object[]{ sides, corners, round, cornerWidth, overlap, ltr };
    }
}