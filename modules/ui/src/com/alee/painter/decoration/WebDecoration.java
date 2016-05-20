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

package com.alee.painter.decoration;

import com.alee.api.ColorSupport;
import com.alee.api.StrokeSupport;
import com.alee.managers.style.Bounds;
import com.alee.painter.decoration.background.IBackground;
import com.alee.painter.decoration.border.BorderWidth;
import com.alee.painter.decoration.border.IBorder;
import com.alee.painter.decoration.shadow.IShadow;
import com.alee.painter.decoration.shadow.ShadowType;
import com.alee.painter.decoration.shape.IPartialShape;
import com.alee.painter.decoration.shape.IShape;
import com.alee.painter.decoration.shape.ShapeType;
import com.alee.utils.CollectionUtils;
import com.alee.utils.GraphicsUtils;
import com.alee.utils.MergeUtils;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Configurable decoration state used for most WebLaF components.
 * It provides basic elements required to paint component parts.
 *
 * @param <E> component type
 * @param <I> decoration type
 * @author Mikle Garin
 */

@XStreamAlias ( "decoration" )
public class WebDecoration<E extends JComponent, I extends WebDecoration<E, I>> extends ContentDecoration<E, I>
{
    /**
     * Decoration shape.
     * It defines the shape of the decoration shadow, border, background and might be used for other decoration elements.
     * Implicit list is only used to provide convenient XML descriptor for this field, only one shape can be provided at a time.
     *
     * @see com.alee.painter.decoration.shape.IShape
     * @see com.alee.painter.decoration.shape.AbstractShape
     */
    @XStreamImplicit
    protected List<IShape> shapes = new ArrayList<IShape> ( 1 );

    /**
     * Optional decoration shadows.
     * Maximum two different shadows could be provided at the same time - outer and inner.
     *
     * @see com.alee.painter.decoration.shadow.IShadow
     * @see com.alee.painter.decoration.shadow.AbstractShadow
     */
    @XStreamImplicit
    protected List<IShadow> shadows = new ArrayList<IShadow> ( 1 );

    /**
     * Optional decoration border.
     * Implicit list is used to provide convenient XML descriptor for this field.
     * Right now only single border can be used per decoration instance, but that might change in future.
     *
     * @see com.alee.painter.decoration.border.IBorder
     * @see com.alee.painter.decoration.border.AbstractBorder
     */
    @XStreamImplicit
    protected List<IBorder> borders = new ArrayList<IBorder> ( 1 );

    /**
     * Optional decoration backgrounds.
     * Multiple backgrounds can be used per decoration instance.
     * Though it is not reasonable to use backgrounds which will fully overlap eachother.
     *
     * @see com.alee.painter.decoration.background.IBackground
     * @see com.alee.painter.decoration.background.AbstractBackground
     */
    @XStreamImplicit
    protected List<IBackground> background = new ArrayList<IBackground> ( 1 );

    /**
     * Returns decoration shape.
     *
     * @return decoration shape
     */
    public IShape getShape ()
    {
        return !CollectionUtils.isEmpty ( shapes ) ? shapes.get ( 0 ) : null;
    }

    /**
     * Returns shadow of the specified type.
     *
     * @param type shadow type
     * @return shadow of the specified type
     */
    public IShadow getShadow ( final ShadowType type )
    {
        if ( !CollectionUtils.isEmpty ( shadows ) )
        {
            for ( final IShadow shadow : shadows )
            {
                if ( shadow.getType () == type )
                {
                    return shadow;
                }
            }
        }
        return null;
    }

    /**
     * Returns width of the shadow with the specified type.
     *
     * @param type shadow type
     * @return width of the shadow with the specified type
     */
    public int getShadowWidth ( final ShadowType type )
    {
        final IShadow shadow = getShadow ( type );
        return shadow != null ? shadow.getWidth () : 0;
    }

    /**
     * Returns decoration border.
     *
     * @return decoration border
     */
    public IBorder getBorder ()
    {
        return !CollectionUtils.isEmpty ( borders ) ? borders.get ( 0 ) : null;
    }

    /**
     * Returns border width.
     *
     * @return border width
     */
    public BorderWidth getBorderWidth ()
    {
        final IBorder border = getBorder ();
        return border != null ? border.getWidth () : BorderWidth.EMPTY;
    }

    /**
     * Returns decoration backgrounds.
     *
     * @return decoration backgrounds
     */
    public List<IBackground> getBackgrounds ()
    {
        return !CollectionUtils.isEmpty ( background ) ? background : null;
    }

    @Override
    public Insets getBorderInsets ( final E c )
    {
        Insets insets = null;
        if ( isVisible () )
        {
            final IShape shape = getShape ();
            if ( shape != null )
            {
                final BorderWidth bw = getBorderWidth ();
                final int sw = getShadowWidth ( ShadowType.outer );
                if ( shape instanceof IPartialShape )
                {
                    final IPartialShape ps = ( IPartialShape ) shape;
                    final int top = ps.isPaintTop ( c, this ) ? bw.top + sw : ps.isPaintTopLine ( c, this ) ? bw.top : 0;
                    final int left = ps.isPaintLeft ( c, this ) ? bw.left + sw : ps.isPaintLeftLine ( c, this ) ? bw.left : 0;
                    final int bottom = ps.isPaintBottom ( c, this ) ? bw.bottom + sw : ps.isPaintBottomLine ( c, this ) ? bw.bottom : 0;
                    final int right = ps.isPaintRight ( c, this ) ? bw.right + sw : ps.isPaintRightLine ( c, this ) ? bw.right : 0;
                    insets = new Insets ( top, left, bottom, right );
                }
                else
                {
                    insets = new Insets ( bw.top + sw, bw.left + sw, bw.bottom + sw, bw.right + sw );
                }
            }
        }
        return insets;
    }

    @Override
    public Shape provideShape ( final E component, final Rectangle bounds )
    {
        // todo Add ShapeType into PainterShapeProvider interface
        final IShape shape = getShape ();
        return isVisible () && shape != null ? shape.getShape ( ShapeType.background, bounds, component, this ) : bounds;
    }

    @Override
    public void paint ( final Graphics2D g2d, final Rectangle bounds, final E c )
    {
        // Painting only if bounds are enough and decoration is visible
        if ( bounds.width > 0 && bounds.height > 0 && isVisible () )
        {
            // Painting only if margin bounds ar enough and intersect visible area
            final Rectangle cl = g2d.getClip () instanceof Rectangle ? ( Rectangle ) g2d.getClip () : c.getVisibleRect ();
            final Rectangle mb = Bounds.margin.of ( c, this, bounds );
            if ( mb.width > 0 && mb.height > 0 && mb.intersects ( cl ) )
            {
                // Checking shape existance
                final IShape shape = getShape ();
                if ( shape != null )
                {
                    // Setup settings
                    final Object oaa = GraphicsUtils.setupAntialias ( g2d );
                    final Composite oc = GraphicsUtils.setupAlphaComposite ( g2d, getOpacity (), getOpacity () < 1f );
                    final Shape ocl = GraphicsUtils.setupClip ( g2d, mb.intersection ( cl ) );

                    // Outer shadow
                    final IShadow outer = getShadow ( ShadowType.outer );
                    if ( outer != null && shape.isVisible ( ShapeType.outerShadow, bounds, c, WebDecoration.this ) )
                    {
                        final Shape s = shape.getShape ( ShapeType.outerShadow, bounds, c, WebDecoration.this );
                        outer.paint ( g2d, bounds, c, WebDecoration.this, s );
                    }

                    // Painting all available backgrounds
                    final List<IBackground> backgrounds = getBackgrounds ();
                    if ( !CollectionUtils.isEmpty ( backgrounds ) &&
                            shape.isVisible ( ShapeType.background, bounds, c, WebDecoration.this ) )
                    {
                        final Shape s = shape.getShape ( ShapeType.background, bounds, c, WebDecoration.this );
                        for ( final IBackground background : backgrounds )
                        {
                            background.paint ( g2d, bounds, c, WebDecoration.this, s );
                        }
                    }

                    // Painting inner shadow
                    final IShadow inner = getShadow ( ShadowType.inner );
                    if ( inner != null && shape.isVisible ( ShapeType.innerShadow, bounds, c, WebDecoration.this ) )
                    {
                        final Shape s = shape.getShape ( ShapeType.innerShadow, bounds, c, WebDecoration.this );
                        inner.paint ( g2d, bounds, c, WebDecoration.this, s );
                    }

                    // Painting border
                    final IBorder border = getBorder ();
                    if ( border != null && shape.isVisible ( ShapeType.border, bounds, c, WebDecoration.this ) )
                    {
                        final Shape s = shape.getShape ( ShapeType.border, bounds, c, WebDecoration.this );
                        border.paint ( g2d, bounds, c, WebDecoration.this, s );

                        // Painting side lines
                        // todo This is a temporary solution
                        if ( shape instanceof IPartialShape && border instanceof ColorSupport && border instanceof StrokeSupport )
                        {
                            final IPartialShape webShape = ( IPartialShape ) shape;
                            final boolean ltr = c.getComponentOrientation ().isLeftToRight ();
                            final boolean paintTop = webShape.isPaintTop ( c, this );
                            final boolean paintBottom = webShape.isPaintBottom ( c, this );
                            final boolean actualPaintLeft = ltr ? webShape.isPaintLeft ( c, this ) : webShape.isPaintRight ( c, this );
                            final boolean actualPaintRight = ltr ? webShape.isPaintRight ( c, this ) : webShape.isPaintLeft ( c, this );
                            final boolean paintTopLine = webShape.isPaintTopLine ( c, this );
                            final boolean paintBottomLine = webShape.isPaintBottomLine ( c, this );
                            final boolean actualPaintLeftLine =
                                    ltr ? webShape.isPaintLeftLine ( c, this ) : webShape.isPaintRightLine ( c, this );
                            final boolean actualPaintRightLine =
                                    ltr ? webShape.isPaintRightLine ( c, this ) : webShape.isPaintLeftLine ( c, this );
                            final int shadowWidth = getShadowWidth ( ShadowType.outer );

                            final Stroke stroke = ( ( StrokeSupport ) border ).getStroke ();
                            final Stroke os = GraphicsUtils.setupStroke ( g2d, stroke, stroke != null );
                            final Color bc = ( ( ColorSupport ) border ).getColor ();
                            final Paint op = GraphicsUtils.setupPaint ( g2d, bc, bc != null );

                            if ( !paintTop && paintTopLine )
                            {
                                final int x1 = bounds.x + ( actualPaintLeft ? shadowWidth : 0 );
                                final int x2 = bounds.x + bounds.width - ( actualPaintRight ? shadowWidth : 0 ) - 1;
                                g2d.drawLine ( x1, bounds.y, x2, bounds.y );
                            }
                            if ( !paintBottom && paintBottomLine )
                            {
                                final int y = bounds.y + bounds.height - 1;
                                final int x1 = bounds.x + ( actualPaintLeft ? shadowWidth : 0 );
                                final int x2 = bounds.x + bounds.width - ( actualPaintRight ? shadowWidth : 0 ) - 1;
                                g2d.drawLine ( x1, y, x2, y );
                            }
                            if ( !actualPaintLeft && actualPaintLeftLine )
                            {
                                final int y1 = bounds.y + ( paintTop ? shadowWidth : 0 );
                                final int y2 = bounds.y + bounds.height - ( paintBottom ? shadowWidth : 0 ) - 1;
                                g2d.drawLine ( bounds.x, y1, bounds.x, y2 );
                            }
                            if ( !actualPaintRight && actualPaintRightLine )
                            {
                                final int x = bounds.x + bounds.width - 1;
                                final int y1 = bounds.y + ( paintTop ? shadowWidth : 0 );
                                final int y2 = bounds.y + bounds.height - ( paintBottom ? shadowWidth : 0 ) - 1;
                                g2d.drawLine ( x, y1, x, y2 );
                            }

                            GraphicsUtils.restorePaint ( g2d, op, bc != null );
                            GraphicsUtils.restoreStroke ( g2d, os, stroke != null );
                        }
                    }

                    // Restoring settings
                    GraphicsUtils.restoreClip ( g2d, ocl );
                    GraphicsUtils.restoreAntialias ( g2d, oaa );
                    GraphicsUtils.restoreComposite ( g2d, oc );
                }

                // Painting contents
                paintContent ( g2d, bounds, c );
            }
        }
    }

    @Override
    public I merge ( final I decoration )
    {
        super.merge ( decoration );
        shapes = isOverwrite () ? decoration.shapes : MergeUtils.merge ( shapes, decoration.shapes );
        shadows = isOverwrite () ? decoration.shadows : MergeUtils.merge ( shadows, decoration.shadows );
        borders = isOverwrite () ? decoration.borders : MergeUtils.merge ( borders, decoration.borders );
        background = isOverwrite () ? decoration.background : MergeUtils.merge ( background, decoration.background );
        return ( I ) this;
    }
}