package com.alee.managers.style.skin.web.data.shape;

import com.alee.managers.style.skin.web.data.decoration.WebDecoration;
import com.alee.managers.style.skin.web.data.shade.ShadeType;
import com.alee.utils.LafUtils;
import com.alee.utils.ShapeCache;
import com.alee.utils.swing.DataProvider;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.GeneralPath;

/**
 * @author nsofronov
 */
public class WebShape<E extends JComponent, D extends WebDecoration<E, D>, I extends WebShape<E, D, I>> extends AbstractShape<E, D, I>
{
    /**
     * Decoration corners rounding.
     */
    @XStreamAsAttribute
    protected Integer round;

    /**
     * Displayed decoration sides.
     */
    protected String sides;

    /**
     * Displayed decoration side lines.
     */
    protected String lines;

    /**
     * Returns decoration corners rounding.
     *
     * @return decoration corners rounding
     */
    public int getRound ()
    {
        return round != null ? round : 0;
    }

    /**
     * Returns whether or not top side should be painted.
     *
     * @return true if top side should be painted, false otherwise
     */
    protected boolean isPaintTop ()
    {
        return sides == null || sides.charAt ( 0 ) != '0';
    }

    /**
     * Returns whether or not left side should be painted.
     *
     * @return true if left side should be painted, false otherwise
     */
    protected boolean isPaintLeft ()
    {
        return sides == null || sides.charAt ( 2 ) != '0';
    }

    /**
     * Returns whether or not bottom side should be painted.
     *
     * @return true if bottom side should be painted, false otherwise
     */
    protected boolean isPaintBottom ()
    {
        return sides == null || sides.charAt ( 4 ) != '0';
    }

    /**
     * Returns whether or not right side should be painted.
     *
     * @return true if right side should be painted, false otherwise
     */
    protected boolean isPaintRight ()
    {
        return sides == null || sides.charAt ( 6 ) != '0';
    }

    /**
     * Returns whether or not top side line should be painted.
     *
     * @return true if top side line should be painted, false otherwise
     */
    protected boolean isPaintTopLine ()
    {
        return lines != null && lines.charAt ( 0 ) == '1';
    }

    /**
     * Returns whether or not left side line should be painted.
     *
     * @return true if left side line should be painted, false otherwise
     */
    protected boolean isPaintLeftLine ()
    {
        return lines != null && lines.charAt ( 2 ) == '1';
    }

    /**
     * Returns whether or not bottom side line should be painted.
     *
     * @return true if bottom side line should be painted, false otherwise
     */
    protected boolean isPaintBottomLine ()
    {
        return lines != null && lines.charAt ( 4 ) == '1';
    }

    /**
     * Returns whether or not right side line should be painted.
     *
     * @return true if right side line should be painted, false otherwise
     */
    protected boolean isPaintRightLine ()
    {
        return lines != null && lines.charAt ( 6 ) == '1';
    }

    /**
     * Returns whether or not any of the sides should be painted.
     *
     * @return true if at least one of the sides should be painted, false otherwise
     */
    protected boolean isAnySide ()
    {
        return sides == null || sides.contains ( "1" );
    }

    /**
     * Returns whether or not any of the side lines should be painted.
     *
     * @return true if at least one of the side lines should be painted, false otherwise
     */
    protected boolean isAnyLine ()
    {
        return lines != null && lines.contains ( "1" );
    }

    @Override
    public Shape getShape ( final ShapeType type, final Rectangle bounds, final E c, final D d )
    {
        return getShape ( bounds, c, d, type );
    }

    /**
     * Returns decoration border shape.
     *
     * @param bounds     painting bounds
     * @param c          painted component
     * @param background whether should return background shape or not
     * @return decoration border shape
     */
    protected Shape getShape ( final Rectangle bounds, final E c, final D d, final ShapeType type )
    {
        return ShapeCache.getShape ( c, type.toString (), new DataProvider<Shape> ()
        {
            @Override
            public Shape provide ()
            {
                return createShape ( bounds, c, d, type );
            }
        }, getCachedShapeSettings ( bounds, c, d ) );
    }

    /**
     * Returns an array of shape settings cached along with the shape.
     *
     * @param bounds painting bounds
     * @param c      painted component
     * @param d      painted decoration state
     * @return an array of shape settings cached along with the shape
     */
    protected Object[] getCachedShapeSettings ( final Rectangle bounds, final E c, final D d )
    {
        return new Object[]{ bounds, c.getComponentOrientation ().isLeftToRight (), getRound (), d.getShadeWidth ( ShadeType.outer ),
                isPaintTop (), isPaintBottom (), isPaintLeft (), isPaintRight () };
    }

    /**
     * Returns decoration shape.
     *
     * @param bounds     painting bounds
     * @param c          painted component
     * @param background whether or not should return background shape
     * @return decoration shape
     */
    protected Shape createShape ( final Rectangle bounds, final E c, final D d, final ShapeType type )
    {
        // todo Properly add side lines into shape here
        final boolean ltr = c.getComponentOrientation ().isLeftToRight ();
        final boolean paintTop = isPaintTop ();
        final boolean paintBottom = isPaintBottom ();
        final boolean actualPaintLeft = ltr ? isPaintLeft () : isPaintRight ();
        final boolean actualPaintRight = ltr ? isPaintRight () : isPaintLeft ();
        final int round = getRound ();
        final int shadeWidth = d.getShadeWidth ( ShadeType.outer );
        final int x = bounds.x;
        final int y = bounds.y;
        final int w = bounds.width;
        final int h = bounds.height;
        if ( type == ShapeType.background || type == ShapeType.shade )
        {
            final Point[] corners = new Point[ 4 ];
            final boolean[] rounded = new boolean[ 4 ];

            corners[ 0 ] = new Point ( x + ( actualPaintLeft ? shadeWidth : 0 ), y + ( paintTop ? shadeWidth : 0 ) );
            rounded[ 0 ] = actualPaintLeft && paintTop;

            corners[ 1 ] = new Point ( x + ( actualPaintRight ? w - shadeWidth : w ) - ( type == ShapeType.shade ? 1 : 0 ),
                    y + ( paintTop ? shadeWidth : 0 ) );
            rounded[ 1 ] = actualPaintRight && paintTop;

            corners[ 2 ] = new Point ( x + ( actualPaintRight ? w - shadeWidth : w ) - ( type == ShapeType.shade ? 1 : 0 ),
                    y + ( paintBottom ? h - shadeWidth : h ) - ( type == ShapeType.shade ? 1 : 0 ) );
            rounded[ 2 ] = actualPaintRight && paintBottom;

            corners[ 3 ] = new Point ( x + ( actualPaintLeft ? shadeWidth : 0 ),
                    y + ( paintBottom ? h - shadeWidth : h ) - ( type == ShapeType.shade ? 1 : 0 ) );
            rounded[ 3 ] = actualPaintLeft && paintBottom;

            return LafUtils.createRoundedShape ( round > 0 ? round + 1 : 0, corners, rounded );
        }
        else
        {
            final GeneralPath shape = new GeneralPath ( GeneralPath.WIND_EVEN_ODD );
            boolean connect;
            boolean moved = false;
            if ( paintTop )
            {
                shape.moveTo ( x + ( actualPaintLeft ? shadeWidth + round : 0 ), y + shadeWidth );
                if ( actualPaintRight )
                {
                    shape.lineTo ( x + w - shadeWidth - round - 1, y + shadeWidth );
                    shape.quadTo ( x + w - shadeWidth - 1, y + shadeWidth, x + w - shadeWidth - 1, y + shadeWidth + round );
                }
                else
                {
                    shape.lineTo ( x + w - 1, y + shadeWidth );
                }
                connect = true;
            }
            else
            {
                connect = false;
            }
            if ( actualPaintRight )
            {
                if ( !connect )
                {
                    shape.moveTo ( x + w - shadeWidth - 1, y + ( paintTop ? shadeWidth + round : 0 ) );
                    moved = true;
                }
                if ( paintBottom )
                {
                    shape.lineTo ( x + w - shadeWidth - 1, y + h - shadeWidth - round - 1 );
                    shape.quadTo ( x + w - shadeWidth - 1, y + h - shadeWidth - 1, x + w - shadeWidth - round - 1, y + h - shadeWidth - 1 );
                }
                else
                {
                    shape.lineTo ( x + w - shadeWidth - 1, y + h - 1 );
                }
                connect = true;
            }
            else
            {
                connect = false;
            }
            if ( paintBottom )
            {
                if ( !connect )
                {
                    shape.moveTo ( x + w + ( actualPaintRight ? -shadeWidth - round - 1 : -1 ), y + h - shadeWidth - 1 );
                    moved = true;
                }
                if ( actualPaintLeft )
                {
                    shape.lineTo ( x + shadeWidth + round, y + h - shadeWidth - 1 );
                    shape.quadTo ( x + shadeWidth, y + h - shadeWidth - 1, x + shadeWidth, y + h - shadeWidth - round - 1 );
                }
                else
                {
                    shape.lineTo ( x, y + h - shadeWidth - 1 );
                }
                connect = true;
            }
            else
            {
                connect = false;
            }
            if ( actualPaintLeft )
            {
                if ( !connect )
                {
                    shape.moveTo ( x + shadeWidth, y + h + ( paintBottom ? -shadeWidth - round - 1 : -1 ) );
                    moved = true;
                }
                if ( paintTop )
                {
                    shape.lineTo ( x + shadeWidth, y + shadeWidth + round );
                    shape.quadTo ( x + shadeWidth, y + shadeWidth, x + shadeWidth + round, y + shadeWidth );
                    if ( !moved )
                    {
                        shape.closePath ();
                    }
                }
                else
                {
                    shape.lineTo ( x + shadeWidth, y );
                }
            }
            return shape;
        }
    }

    @Override
    public I merge ( final I object )
    {
        return null;
    }
}
