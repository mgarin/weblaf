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

package com.alee.managers.style.skin.web.data.decoration;

import com.alee.managers.style.skin.web.data.background.ColorBackground;
import com.alee.managers.style.skin.web.data.background.IBackground;
import com.alee.managers.style.skin.web.data.border.IBorder;
import com.alee.managers.style.skin.web.data.border.LineBorder;
import com.alee.managers.style.skin.web.data.shade.BasicShade;
import com.alee.managers.style.skin.web.data.shade.IShade;
import com.alee.managers.style.skin.web.data.shade.ShadeType;
import com.alee.utils.*;
import com.alee.utils.swing.DataProvider;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.GeneralPath;
import java.util.List;

/**
 * Configurable decoration state used for a lot of WebLaF components.
 * Even though it seems like it doesn't have a lot of settings it provides very important basic things required to paint component parts.
 *
 * @author Mikle Garin
 */

@XStreamAlias ("decoration")
public class WebDecoration<E extends JComponent, I extends WebDecoration<E, I>> extends AbstractDecoration<E, I>
{
    /**
     * Shape cache keys.
     * Decoration shapes are cached under specific keys which are shared between all components.
     * That allows more optimized shapes generation and usage across all of existing components with similar base shapes.
     */
    protected static final String BORDER_SHAPE = "border";
    protected static final String BACKGROUND_SHAPE = "background";

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
     * Decoration shades.
     * Right now two different shades could be provided - outer and inner.
     * Implicit list is also used to provide convenient XML descriptor for this field.
     */
    @XStreamImplicit
    protected List<IShade> shades = CollectionUtils.<IShade>asList ( new BasicShade () );

    /**
     * Decoration border.
     * Implicit list is used to provide convenient XML descriptor for this field.
     * Right now only single border can be used per decoration instance, but that might change in future.
     */
    @XStreamImplicit
    protected List<IBorder> borders = CollectionUtils.<IBorder>asList ( new LineBorder () );

    /**
     * Decoration background.
     * Implicit list is used to provide convenient XML descriptor for this field.
     * Right now only single background can be used per decoration instance, but that might change in future.
     */
    @XStreamImplicit
    protected List<IBackground> background = CollectionUtils.<IBackground>asList ( new ColorBackground () );

    /**
     * Returns decoration transparency.
     *
     * @return decoration transparency
     */
    public float getTransparency ()
    {
        return transparency != null ? transparency : 1f;
    }

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

    /**
     * Returns shade of the specified type.
     *
     * @param type shade type
     * @return shade of the specified type
     */
    protected IShade getShade ( final ShadeType type )
    {
        if ( !CollectionUtils.isEmpty ( shades ) )
        {
            for ( final IShade shade : shades )
            {
                if ( shade.getType () == type )
                {
                    return shade;
                }
            }
        }
        return null;
    }

    /**
     * Returns width of the shade with the specified type.
     *
     * @param type shade type
     * @return width of the shade with the specified type
     */
    protected int getShadeWidth ( final ShadeType type )
    {
        final IShade shade = getShade ( type );
        return shade != null ? shade.getWidth () : 0;
    }

    /**
     * Returns decoration border.
     *
     * @return decoration border
     */
    protected IBorder getBorder ()
    {
        return !CollectionUtils.isEmpty ( borders ) ? borders.get ( 0 ) : null;
    }

    /**
     * Returns decoration backgrounds.
     *
     * @return decoration backgrounds
     */
    protected List<IBackground> getBackgrounds ()
    {
        return !CollectionUtils.isEmpty ( background ) ? background : null;
    }

    @Override
    public Insets getBorderInsets ()
    {
        Insets insets = null;
        if ( isVisible () )
        {
            // todo Count in border(s) width(s) instead of 1px
            final int spacing = getShadeWidth ( ShadeType.outer ) + 1;
            final int top = isPaintTop () ? spacing : isPaintTopLine () ? 1 : 0;
            final int left = isPaintLeft () ? spacing : isPaintLeftLine () ? 1 : 0;
            final int bottom = isPaintBottom () ? spacing : isPaintBottomLine () ? 1 : 0;
            final int right = isPaintRight () ? spacing : isPaintRightLine () ? 1 : 0;
            insets = new Insets ( top, left, bottom, right );
        }
        return insets;
    }

    @Override
    public Shape provideShape ( final E component, final Rectangle bounds )
    {
        return isVisible () ? getShape ( bounds, component, true ) : bounds;
    }

    @Override
    public void paint ( final Graphics2D g2d, final Rectangle bounds, final E c )
    {
        if ( isVisible () )
        {
            // Retrieving decoration parts
            final IShade outer = getShade ( ShadeType.outer );
            final IBorder border = getBorder ();
            final List<IBackground> backgrounds = getBackgrounds ();
            final IShade inner = getShade ( ShadeType.inner );

            // Initializing paint sequence only if there is something to paint in the first place
            final boolean anySide = isAnySide ();
            final boolean anyLine = isAnyLine ();
            if ( anySide || anyLine || backgrounds != null || inner != null )
            {
                // Antialias
                final Object aa = GraphicsUtils.setupAntialias ( g2d );

                // Transparency
                final float transparency = getTransparency ();
                final Composite oc = GraphicsUtils.setupAlphaComposite ( g2d, transparency, transparency < 1f );

                // Border and background shape
                final Shape borderShape = getShape ( bounds, c, false );

                // Painting outer shade
                if ( anySide && outer != null )
                {
                    outer.paint ( g2d, bounds, c, WebDecoration.this, borderShape );
                }

                // Painting all available backgrounds
                if ( !CollectionUtils.isEmpty ( backgrounds ) )
                {
                    final Shape backgroundShape = getShape ( bounds, c, true );
                    for ( final IBackground background : backgrounds )
                    {
                        background.paint ( g2d, bounds, c, WebDecoration.this, backgroundShape );
                    }
                }

                // Painting inner shade
                if ( inner != null )
                {
                    inner.paint ( g2d, bounds, c, WebDecoration.this, borderShape );
                }

                // Painting border
                if ( ( anySide || anyLine ) && border != null )
                {
                    border.paint ( g2d, bounds, c, WebDecoration.this, borderShape );

                    // Painting side lines
                    // todo Move this into the border implementation
                    final boolean ltr = c.getComponentOrientation ().isLeftToRight ();
                    final boolean paintTop = isPaintTop ();
                    final boolean paintBottom = isPaintBottom ();
                    final boolean actualPaintLeft = ltr ? isPaintLeft () : isPaintRight ();
                    final boolean actualPaintRight = ltr ? isPaintRight () : isPaintLeft ();
                    final boolean paintTopLine = isPaintTopLine ();
                    final boolean paintBottomLine = isPaintBottomLine ();
                    final boolean actualPaintLeftLine = ltr ? isPaintLeftLine () : isPaintRightLine ();
                    final boolean actualPaintRightLine = ltr ? isPaintRightLine () : isPaintLeftLine ();
                    final int shadeWidth = getShadeWidth ( ShadeType.outer );
                    final Stroke os = GraphicsUtils.setupStroke ( g2d, border.getStroke (), border.getStroke () != null );
                    final Paint op = GraphicsUtils.setupPaint ( g2d, border.getColor (), border.getColor () != null );
                    if ( !paintTop && paintTopLine )
                    {
                        final int x1 = bounds.x + ( actualPaintLeft ? shadeWidth : 0 );
                        final int x2 = bounds.x + bounds.width - ( actualPaintRight ? shadeWidth : 0 ) - 1;
                        g2d.drawLine ( x1, bounds.y, x2, bounds.y );
                    }
                    if ( !paintBottom && paintBottomLine )
                    {
                        final int y = bounds.y + bounds.height - 1;
                        final int x1 = bounds.x + ( actualPaintLeft ? shadeWidth : 0 );
                        final int x2 = bounds.x + bounds.width - ( actualPaintRight ? shadeWidth : 0 ) - 1;
                        g2d.drawLine ( x1, y, x2, y );
                    }
                    if ( !actualPaintLeft && actualPaintLeftLine )
                    {
                        final int y1 = bounds.y + ( paintTop ? shadeWidth : 0 );
                        final int y2 = bounds.y + bounds.height - ( paintBottom ? shadeWidth : 0 ) - 1;
                        g2d.drawLine ( bounds.x, y1, bounds.x, y2 );
                    }
                    if ( !actualPaintRight && actualPaintRightLine )
                    {
                        final int x = bounds.x + bounds.width - 1;
                        final int y1 = bounds.y + ( paintTop ? shadeWidth : 0 );
                        final int y2 = bounds.y + bounds.height - ( paintBottom ? shadeWidth : 0 ) - 1;
                        g2d.drawLine ( x, y1, x, y2 );
                    }
                    GraphicsUtils.restorePaint ( g2d, op, border.getColor () != null );
                    GraphicsUtils.restoreStroke ( g2d, os, border.getStroke () != null );
                }

                GraphicsUtils.restoreComposite ( g2d, oc );
                GraphicsUtils.restoreAntialias ( g2d, aa );
            }
        }
    }

    /**
     * Returns decoration border shape.
     *
     * @param bounds     painting bounds
     * @param c          painted component
     * @param background whether should return background shape or not
     * @return decoration border shape
     */
    protected Shape getShape ( final Rectangle bounds, final E c, final boolean background )
    {
        return ShapeCache.getShape ( c, background ? BACKGROUND_SHAPE : BORDER_SHAPE, new DataProvider<Shape> ()
        {
            @Override
            public Shape provide ()
            {
                return createShape ( bounds, c, background );
            }
        }, getCachedShapeSettings ( bounds, c ) );
    }

    /**
     * Returns an array of shape settings cached along with the shape.
     *
     * @param bounds painting bounds
     * @param c      painted component
     * @return an array of shape settings cached along with the shape
     */
    protected Object[] getCachedShapeSettings ( final Rectangle bounds, final E c )
    {
        return new Object[]{ bounds, c.getComponentOrientation ().isLeftToRight (), getRound (), getShadeWidth ( ShadeType.outer ),
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
    protected Shape createShape ( final Rectangle bounds, final E c, final boolean background )
    {
        // todo Properly add side lines into shape here
        final boolean ltr = c.getComponentOrientation ().isLeftToRight ();
        final boolean paintTop = isPaintTop ();
        final boolean paintBottom = isPaintBottom ();
        final boolean actualPaintLeft = ltr ? isPaintLeft () : isPaintRight ();
        final boolean actualPaintRight = ltr ? isPaintRight () : isPaintLeft ();
        final int round = getRound ();
        final int shadeWidth = getShadeWidth ( ShadeType.outer );
        final int x = bounds.x;
        final int y = bounds.y;
        final int w = bounds.width;
        final int h = bounds.height;
        if ( background )
        {
            final Point[] corners = new Point[ 4 ];
            final boolean[] rounded = new boolean[ 4 ];

            corners[ 0 ] = new Point ( x + ( actualPaintLeft ? shadeWidth : 0 ), y + ( paintTop ? shadeWidth : 0 ) );
            rounded[ 0 ] = actualPaintLeft && paintTop;

            corners[ 1 ] = new Point ( x + ( actualPaintRight ? w - shadeWidth : w ), y + ( paintTop ? shadeWidth : 0 ) );
            rounded[ 1 ] = actualPaintRight && paintTop;

            corners[ 2 ] = new Point ( x + ( actualPaintRight ? w - shadeWidth : w ), y + ( paintBottom ? h - shadeWidth : h ) );
            rounded[ 2 ] = actualPaintRight && paintBottom;

            corners[ 3 ] = new Point ( x + ( actualPaintLeft ? shadeWidth : 0 ), y + ( paintBottom ? h - shadeWidth : h ) );
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
    public I merge ( final I decoration )
    {
        super.merge ( decoration );
        if ( decoration.round != null )
        {
            round = decoration.round;
        }
        if ( decoration.sides != null )
        {
            sides = decoration.sides;
        }
        if ( decoration.lines != null )
        {
            lines = decoration.lines;
        }
        shades = MergeUtils.merge ( shades, decoration.shades );
        borders = MergeUtils.merge ( borders, decoration.borders );
        background = MergeUtils.merge ( background, decoration.background );
        return ( I ) this;
    }
}