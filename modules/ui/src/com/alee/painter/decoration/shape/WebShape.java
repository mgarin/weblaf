package com.alee.painter.decoration.shape;

import com.alee.api.annotations.NotNull;
import com.alee.api.jdk.Supplier;
import com.alee.laf.grouping.GroupingLayout;
import com.alee.painter.decoration.WebDecoration;
import com.alee.painter.decoration.border.BorderWidth;
import com.alee.painter.decoration.shadow.ShadowType;
import com.alee.utils.MathUtils;
import com.alee.utils.ShapeUtils;
import com.alee.utils.general.Pair;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.GeneralPath;

/**
 * Common WebLaF component shape implementation.
 * It provides rounded rectangular component shape.
 * Different sides of the shape can also be clipped to visually group components.
 *
 * @param <C> component type
 * @param <D> decoration type
 * @param <I> shape type
 * @author nsofronov
 * @author Mikle Garin
 */
@XStreamAlias ( "WebShape" )
public class WebShape<C extends JComponent, D extends WebDecoration<C, D>, I extends WebShape<C, D, I>> extends AbstractShape<C, D, I>
        implements IPartialShape<C, D, I>
{
    /**
     * Decoration corners rounding.
     */
    @XStreamAsAttribute
    protected Round round;

    /**
     * Displayed decoration sides.
     * todo Make use of {@link com.alee.painter.decoration.shape.Sides}
     */
    @XStreamAsAttribute
    protected String sides;

    /**
     * Displayed decoration side lines.
     * todo Make use of {@link com.alee.painter.decoration.shape.Sides}
     */
    @XStreamAsAttribute
    protected String lines;

    /**
     * Returns decoration corners rounding.
     *
     * @return decoration corners rounding
     */
    public Round getRound ()
    {
        return round != null ? round : new Round ();
    }

    /**
     * Returns grouping layout used to place specified component if it exists, {@code null} otherwise.
     *
     * @param c painted component
     * @return grouping layout used to place specified component if it exists, {@code null} otherwise
     */
    protected GroupingLayout getGroupingLayout ( final C c )
    {
        final Container parent = c.getParent ();
        if ( parent != null )
        {
            final LayoutManager layout = parent.getLayout ();
            if ( layout instanceof GroupingLayout )
            {
                return ( GroupingLayout ) layout;
            }
        }
        return null;
    }

    /**
     * Returns descriptor for painted component sides.
     *
     * @param c painted component
     * @param d painted decoration
     * @return descriptor for painted component sides
     */
    protected String getSides ( final C c, final D d )
    {
        if ( d.isSection () )
        {
            return this.sides;
        }
        else
        {
            final GroupingLayout layout = getGroupingLayout ( c );
            return layout != null ? layout.getSides ( c ) : this.sides;
        }
    }

    @Override
    public boolean isPaintTop ( final C c, final D d )
    {
        final String sides = getSides ( c, d );
        return sides == null || sides.charAt ( 0 ) != '0';
    }

    @Override
    public boolean isPaintLeft ( final C c, final D d )
    {
        final String sides = getSides ( c, d );
        return sides == null || sides.charAt ( 2 ) != '0';
    }

    @Override
    public boolean isPaintBottom ( final C c, final D d )
    {
        final String sides = getSides ( c, d );
        return sides == null || sides.charAt ( 4 ) != '0';
    }

    @Override
    public boolean isPaintRight ( final C c, final D d )
    {
        final String sides = getSides ( c, d );
        return sides == null || sides.charAt ( 6 ) != '0';
    }

    /**
     * Returns whether or not any of the sides should be painted.
     *
     * @param c painted component
     * @param d painted decoration
     * @return true if at least one of the sides should be painted, false otherwise
     */
    public boolean isAnySide ( final C c, final D d )
    {
        final String sides = getSides ( c, d );
        return sides == null || sides.contains ( "1" );
    }

    /**
     * Returns descriptor for painted component lines.
     *
     * @param c painted component
     * @param d painted decoration
     * @return descriptor for painted component lines
     */
    protected String getLines ( final C c, final D d )
    {
        if ( d.isSection () )
        {
            return this.lines;
        }
        else
        {
            final GroupingLayout layout = getGroupingLayout ( c );
            return layout != null ? layout.getLines ( c ) : this.lines;
        }
    }

    @Override
    public boolean isPaintTopLine ( final C c, final D d )
    {
        final String lines = getLines ( c, d );
        return !isPaintTop ( c, d ) && lines != null && lines.charAt ( 0 ) == '1';
    }

    @Override
    public boolean isPaintLeftLine ( final C c, final D d )
    {
        final String lines = getLines ( c, d );
        return !isPaintLeft ( c, d ) && lines != null && lines.charAt ( 2 ) == '1';
    }

    @Override
    public boolean isPaintBottomLine ( final C c, final D d )
    {
        final String lines = getLines ( c, d );
        return !isPaintBottom ( c, d ) && lines != null && lines.charAt ( 4 ) == '1';
    }

    @Override
    public boolean isPaintRightLine ( final C c, final D d )
    {
        final String lines = getLines ( c, d );
        return !isPaintRight ( c, d ) && lines != null && lines.charAt ( 6 ) == '1';
    }

    /**
     * Returns whether or not any of the side lines should be painted.
     *
     * @param c painted component
     * @param d painted decoration
     * @return true if at least one of the side lines should be painted, false otherwise
     */
    public boolean isAnyLine ( final C c, final D d )
    {
        return isPaintTopLine ( c, d ) || isPaintLeftLine ( c, d ) || isPaintBottomLine ( c, d ) || isPaintRightLine ( c, d );
    }

    @Override
    public boolean isVisible ( final ShapeType type, final Rectangle bounds, final C c, final D d )
    {
        // Ensure that shape bounds are enough
        final int ow = d.getShadowWidth ( ShadowType.outer ) * 2;
        if ( bounds.width - ow > 0 && bounds.height - ow > 0 )
        {
            // Ensure that some sides are painted
            // It is important for outer shadow and border
            switch ( type )
            {
                case outerShadow:
                    return isAnySide ( c, d );

                case border:
                    return isAnySide ( c, d ) || isAnyLine ( c, d );

                case background:
                case innerShadow:
                default:
                    return true;
            }
        }
        return false;
    }

    @NotNull
    @Override
    public Shape getShape ( final ShapeType type, final Rectangle bounds, final C c, final D d )
    {
        // Shape settings
        final Round r = getRound ();
        final boolean ltr = c.getComponentOrientation ().isLeftToRight ();
        final boolean top = isPaintTop ( c, d );
        final boolean bottom = isPaintBottom ( c, d );
        final boolean left = ltr ? isPaintLeft ( c, d ) : isPaintRight ( c, d );
        final boolean right = ltr ? isPaintRight ( c, d ) : isPaintLeft ( c, d );
        final int sw = d.getShadowWidth ( ShadowType.outer );

        // Retrieving shape
        return ShapeUtils.getShape ( c, "WebShape." + type, new Supplier<Shape> ()
        {
            @Override
            public Shape get ()
            {
                final int x = bounds.x;
                final int y = bounds.y;
                final int w = bounds.width;
                final int h = bounds.height;
                if ( type.isBorder () )
                {
                    final GeneralPath shape = new GeneralPath ( GeneralPath.WIND_EVEN_ODD );
                    boolean connect;
                    boolean moved = false;
                    if ( top )
                    {
                        shape.moveTo ( x + ( left ? sw + r.topLeft : 0 ), y + sw );
                        if ( right )
                        {
                            shape.lineTo ( x + w - sw - r.topRight - 1, y + sw );
                            shape.quadTo ( x + w - sw - 1, y + sw, x + w - sw - 1, y + sw + r.topRight );
                        }
                        else
                        {
                            shape.lineTo ( x + w - 1, y + sw );
                        }
                        connect = true;
                    }
                    else
                    {
                        connect = false;
                    }
                    if ( right )
                    {
                        if ( !connect )
                        {
                            shape.moveTo ( x + w - sw - 1, y + ( top ? sw + r.topRight : 0 ) );
                            moved = true;
                        }
                        if ( bottom )
                        {
                            shape.lineTo ( x + w - sw - 1, y + h - sw - r.bottomRight - 1 );
                            shape.quadTo ( x + w - sw - 1, y + h - sw - 1, x + w - sw - r.bottomRight - 1, y + h - sw - 1 );
                        }
                        else
                        {
                            shape.lineTo ( x + w - sw - 1, y + h - 1 );
                        }
                        connect = true;
                    }
                    else
                    {
                        connect = false;
                    }
                    if ( bottom )
                    {
                        if ( !connect )
                        {
                            shape.moveTo ( x + w + ( right ? -sw - r.bottomRight - 1 : -1 ), y + h - sw - 1 );
                            moved = true;
                        }
                        if ( left )
                        {
                            shape.lineTo ( x + sw + r.bottomLeft, y + h - sw - 1 );
                            shape.quadTo ( x + sw, y + h - sw - 1, x + sw, y + h - sw - r.bottomLeft - 1 );
                        }
                        else
                        {
                            shape.lineTo ( x, y + h - sw - 1 );
                        }
                        connect = true;
                    }
                    else
                    {
                        connect = false;
                    }
                    if ( left )
                    {
                        if ( !connect )
                        {
                            shape.moveTo ( x + sw, y + h + ( bottom ? -sw - r.bottomLeft - 1 : -1 ) );
                            moved = true;
                        }
                        if ( top )
                        {
                            shape.lineTo ( x + sw, y + sw + r.topLeft );
                            shape.quadTo ( x + sw, y + sw, x + sw + r.topLeft, y + sw );
                            if ( !moved )
                            {
                                shape.closePath ();
                            }
                        }
                        else
                        {
                            shape.lineTo ( x + sw, y );
                        }
                    }
                    return shape;
                }
                else
                {
                    final int shShear = type.isOuterShadow () ? sw : 0;
                    final int bgShear = type.isBorder () ? -1 : 0;

                    final Point[] corners = new Point[ 4 ];
                    final int[] rounded = new int[ 4 ];

                    corners[ 0 ] = p ( x + ( left ? sw : -shShear ), y + ( top ? sw : -shShear ) );
                    rounded[ 0 ] = left && top && r.topLeft > 0 ? r.topLeft + 1 : 0;

                    corners[ 1 ] = p ( x + ( right ? w - sw : w + shShear ) + bgShear, y + ( top ? sw : -shShear ) );
                    rounded[ 1 ] = right && top && r.topRight > 0 ? r.topRight + 1 : 0;

                    corners[ 2 ] = p ( x + ( right ? w - sw : w + shShear ) + bgShear, y + ( bottom ? h - sw : h + shShear ) + bgShear );
                    rounded[ 2 ] = right && bottom && r.bottomRight > 0 ? r.bottomRight + 1 : 0;

                    corners[ 3 ] = p ( x + ( left ? sw : -shShear ), y + ( bottom ? h - sw : h + shShear ) + bgShear );
                    rounded[ 3 ] = left && bottom && r.bottomLeft > 0 ? r.bottomLeft + 1 : 0;

                    return ShapeUtils.createRoundedShape ( corners, rounded );
                }
            }
        }, bounds, sw, r, top, bottom, left, right );
    }

    @Override
    public Object[] getShapeSettings ( final Rectangle bounds, final C c, final D d )
    {
        final Round r = getRound ();
        final boolean ltr = c.getComponentOrientation ().isLeftToRight ();
        final boolean top = isPaintTop ( c, d );
        final boolean bottom = isPaintBottom ( c, d );
        final boolean left = ltr ? isPaintLeft ( c, d ) : isPaintRight ( c, d );
        final boolean right = ltr ? isPaintRight ( c, d ) : isPaintLeft ( c, d );
        return new Object[]{ r, top, bottom, left, right };
    }

    @Override
    public StretchInfo getStretchInfo ( final Rectangle bounds, final C c, final D d )
    {
        // todo This section should not take shade width into consideration
        // todo That should be done within the shadow implementation code instead
        // todo Right now this works fine for common implementation but could cause issues for other implementations

        // Shape settings
        final Round r = getRound ();
        final boolean ltr = c.getComponentOrientation ().isLeftToRight ();
        final boolean top = isPaintTop ( c, d );
        final boolean bottom = isPaintBottom ( c, d );
        final boolean left = ltr ? isPaintLeft ( c, d ) : isPaintRight ( c, d );
        final boolean right = ltr ? isPaintRight ( c, d ) : isPaintLeft ( c, d );
        final int sw = d.getShadowWidth ( ShadowType.outer );
        final BorderWidth bw = d.getBorderWidth ();
        final int isw = d.getShadowWidth ( ShadowType.inner );

        // Horizontal stretch zone
        final int x0 = bounds.x + ( left ? sw : 0 ) + MathUtils.max ( bw.left, isw, r.topLeft, r.bottomLeft, sw );
        final int x1 = bounds.x + bounds.width - 1 - ( right ? sw : 0 ) - MathUtils.max ( isw, bw.right, r.topRight, r.bottomRight, sw );
        final Pair<Integer, Integer> hor = x0 < x1 ? new Pair<Integer, Integer> ( x0, x1 ) : null;

        // Vertical stretch zone
        final int y0 = bounds.y + ( top ? sw : 0 ) + MathUtils.max ( bw.top, isw, r.topLeft, r.topRight, sw );
        final int y1 =
                bounds.y + bounds.height - 1 - ( bottom ? sw : 0 ) - MathUtils.max ( bw.bottom, isw, r.bottomLeft, r.bottomRight, sw );
        final Pair<Integer, Integer> ver = y0 < y1 ? new Pair<Integer, Integer> ( y0, y1 ) : null;

        return new StretchInfo ( hor, ver );
    }
}