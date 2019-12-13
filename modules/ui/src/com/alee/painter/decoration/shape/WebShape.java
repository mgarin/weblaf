package com.alee.painter.decoration.shape;

import com.alee.api.annotations.NotNull;
import com.alee.api.annotations.Nullable;
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
    @Nullable
    @XStreamAsAttribute
    protected Round round;

    /**
     * Displayed decoration sides.
     * todo Make use of {@link com.alee.painter.decoration.shape.Sides}
     */
    @Nullable
    @XStreamAsAttribute
    protected String sides;

    /**
     * Displayed decoration side lines.
     * todo Make use of {@link com.alee.painter.decoration.shape.Sides}
     */
    @Nullable
    @XStreamAsAttribute
    protected String lines;

    /**
     * Returns decoration corners rounding.
     *
     * @param c painted component
     * @param d painted decoration
     * @return decoration corners rounding
     */
    @NotNull
    public Round getRound ( @NotNull final C c, @NotNull final D d )
    {
        return round != null ? round : new Round ();
    }

    /**
     * Returns grouping layout used to place specified component if it exists, {@code null} otherwise.
     *
     * @param c painted component
     * @param d painted decoration
     * @return grouping layout used to place specified component if it exists, {@code null} otherwise
     */
    @Nullable
    protected GroupingLayout getGroupingLayout ( @NotNull final C c, @NotNull final D d )
    {
        GroupingLayout groupingLayout = null;
        final Container parent = c.getParent ();
        if ( parent != null )
        {
            final LayoutManager layout = parent.getLayout ();
            if ( layout instanceof GroupingLayout )
            {
                groupingLayout = ( GroupingLayout ) layout;
            }
        }
        return groupingLayout;
    }

    /**
     * Returns descriptor for painted component sides.
     *
     * @param c painted component
     * @param d painted decoration
     * @return descriptor for painted component sides
     */
    @Nullable
    protected String getSides ( @NotNull final C c, @NotNull final D d )
    {
        final String sides;
        if ( d.isSection () )
        {
            sides = this.sides;
        }
        else
        {
            final GroupingLayout layout = getGroupingLayout ( c, d );
            sides = layout != null ? layout.getSides ( c ) : this.sides;
        }
        return sides;
    }

    @Override
    public boolean isPaintTop ( @NotNull final C c, @NotNull final D d )
    {
        final String sides = getSides ( c, d );
        return sides == null || sides.charAt ( 0 ) != '0';
    }

    @Override
    public boolean isPaintLeft ( @NotNull final C c, @NotNull final D d )
    {
        final String sides = getSides ( c, d );
        return sides == null || sides.charAt ( 2 ) != '0';
    }

    @Override
    public boolean isPaintBottom ( @NotNull final C c, @NotNull final D d )
    {
        final String sides = getSides ( c, d );
        return sides == null || sides.charAt ( 4 ) != '0';
    }

    @Override
    public boolean isPaintRight ( @NotNull final C c, @NotNull final D d )
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
    public boolean isAnySide ( @NotNull final C c, @NotNull final D d )
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
    @Nullable
    protected String getLines ( @NotNull final C c, @NotNull final D d )
    {
        final String lines;
        if ( d.isSection () )
        {
            lines = this.lines;
        }
        else
        {
            final GroupingLayout layout = getGroupingLayout ( c, d );
            lines = layout != null ? layout.getLines ( c ) : this.lines;
        }
        return lines;
    }

    @Override
    public boolean isPaintTopLine ( @NotNull final C c, @NotNull final D d )
    {
        final String lines = getLines ( c, d );
        return !isPaintTop ( c, d ) && lines != null && lines.charAt ( 0 ) == '1';
    }

    @Override
    public boolean isPaintLeftLine ( @NotNull final C c, @NotNull final D d )
    {
        final String lines = getLines ( c, d );
        return !isPaintLeft ( c, d ) && lines != null && lines.charAt ( 2 ) == '1';
    }

    @Override
    public boolean isPaintBottomLine ( @NotNull final C c, @NotNull final D d )
    {
        final String lines = getLines ( c, d );
        return !isPaintBottom ( c, d ) && lines != null && lines.charAt ( 4 ) == '1';
    }

    @Override
    public boolean isPaintRightLine ( @NotNull final C c, @NotNull final D d )
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
    public boolean isAnyLine ( @NotNull final C c, @NotNull final D d )
    {
        return isPaintTopLine ( c, d ) || isPaintLeftLine ( c, d ) || isPaintBottomLine ( c, d ) || isPaintRightLine ( c, d );
    }

    /**
     * Returns {@link Sides}.
     *
     * @param c   painted component
     * @param d   painted decoration
     * @return {@link Sides}
     */
    protected Sides createSides ( @NotNull final C c, @NotNull final D d )
    {
        final boolean ltr = c.getComponentOrientation ().isLeftToRight ();
        return new Sides (
                isPaintTop ( c, d ),
                ltr ? isPaintLeft ( c, d ) : isPaintRight ( c, d ),
                isPaintBottom ( c, d ),
                ltr ? isPaintRight ( c, d ) : isPaintLeft ( c, d )
        );
    }

    @Override
    public boolean isVisible ( @NotNull final ShapeType type, @NotNull final Rectangle bounds, @NotNull final C c, @NotNull final D d )
    {
        boolean visible = false;
        final int ow = d.getShadowWidth ( ShadowType.outer ) * 2;
        if ( bounds.width - ow > 0 && bounds.height - ow > 0 )
        {
            switch ( type )
            {
                case outerShadow:
                    visible = isAnySide ( c, d );
                    break;

                case border:
                    visible = isAnySide ( c, d ) || isAnyLine ( c, d );
                    break;

                case background:
                case innerShadow:
                default:
                    visible = true;
                    break;
            }
        }
        return visible;
    }

    @NotNull
    @Override
    public Shape getShape ( @NotNull final ShapeType type, @NotNull final Rectangle bounds, @NotNull final C c, @NotNull final D d )
    {
        // Shape settings
        final int sw = d.getShadowWidth ( ShadowType.outer );
        final Round round = getRound ( c, d );
        final Sides sides = createSides ( c, d );

        // Retrieving shape
        return ShapeUtils.getShape ( c, "WebShape." + type, new Supplier<Shape> ()
        {
            @NotNull
            @Override
            public Shape get ()
            {
                final Shape result;
                if ( type.isBorder () )
                {
                    result = ShapeUtils.createBorderShape ( sw, bounds, round, sides );
                }
                else
                {
                    result = ShapeUtils.createFillShape ( sw, bounds, round, sides, type );
                }
                return result;
            }

        }, bounds, sw, round, sides );
    }

    @NotNull
    @Override
    public Object[] getShapeSettings ( @NotNull final Rectangle bounds, @NotNull final C c, @NotNull final D d )
    {
        return new Object[]{
                getRound ( c, d ),
                createSides ( c, d )
        };
    }

    @NotNull
    @Override
    public StretchInfo getStretchInfo ( @NotNull final Rectangle bounds, @NotNull final C c, @NotNull final D d )
    {
        // todo This section should not take shade width into consideration
        // todo That should be done within the shadow implementation code instead
        // todo Right now this works fine for common implementation but could cause issues for other implementations

        // Shape settings
        final int sw = d.getShadowWidth ( ShadowType.outer );
        final Round r = getRound ( c, d );
        final Sides sides = createSides ( c, d );
        final BorderWidth bw = d.getBorderWidth ();
        final int isw = d.getShadowWidth ( ShadowType.inner );

        // Horizontal stretch zone
        final int x0 = bounds.x + ( sides.left ? sw : 0 )
                + MathUtils.max ( bw.left, isw, r.topLeft, r.bottomLeft, sw );
        final int x1 = bounds.x + bounds.width - 1 - ( sides.right ? sw : 0 )
                - MathUtils.max ( isw, bw.right, r.topRight, r.bottomRight, sw );

        // Vertical stretch zone
        final int y0 = bounds.y + ( sides.top ? sw : 0 )
                + MathUtils.max ( bw.top, isw, r.topLeft, r.topRight, sw );
        final int y1 = bounds.y + bounds.height - 1 - ( sides.bottom ? sw : 0 )
                - MathUtils.max ( bw.bottom, isw, r.bottomLeft, r.bottomRight, sw );

        return new StretchInfo (
                x0 < x1 ? new Pair<Integer, Integer> ( x0, x1 ) : null,
                y0 < y1 ? new Pair<Integer, Integer> ( y0, y1 ) : null
        );
    }
}