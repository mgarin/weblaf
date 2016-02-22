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

import com.alee.managers.style.skin.web.data.background.IBackground;
import com.alee.managers.style.skin.web.data.border.IBorder;
import com.alee.managers.style.skin.web.data.shade.IShadow;
import com.alee.managers.style.skin.web.data.shade.ShadowType;
import com.alee.managers.style.skin.web.data.shape.IShape;
import com.alee.managers.style.skin.web.data.shape.ShapeType;
import com.alee.managers.style.skin.web.data.shape.WebShape;
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
 * Configurable decoration state used for a lot of WebLaF components.
 * Even though it seems like it doesn't have a lot of settings it provides very important basic things required to paint component parts.
 *
 * @author Mikle Garin
 */

@XStreamAlias ( "decoration" )
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
     * Decoration shape.
     * Only one shape can be provided at a time.
     * Implicit list is used only to provide convenient XML descriptor for this field.
     */
    @XStreamImplicit
    protected List<IShape> shapes = new ArrayList<IShape> ( 1 );

    /**
     * Decoration shades.
     * Right now two different shades could be provided - outer and inner.
     * Implicit list is also used to provide convenient XML descriptor for this field.
     */
    @XStreamImplicit
    protected List<IShadow> shades = new ArrayList<IShadow> ( 1 );

    /**
     * Decoration border.
     * Implicit list is used to provide convenient XML descriptor for this field.
     * Right now only single border can be used per decoration instance, but that might change in future.
     */
    @XStreamImplicit
    protected List<IBorder> borders = new ArrayList<IBorder> ( 1 );

    /**
     * Decoration background.
     * Implicit list is used to provide convenient XML descriptor for this field.
     * Right now only single background can be used per decoration instance, but that might change in future.
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
     * Returns shade of the specified type.
     *
     * @param type shade type
     * @return shade of the specified type
     */
    public IShadow getShade ( final ShadowType type )
    {
        if ( !CollectionUtils.isEmpty ( shades ) )
        {
            for ( final IShadow shade : shades )
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
    public int getShadeWidth ( final ShadowType type )
    {
        final IShadow shade = getShade ( type );
        return shade != null ? shade.getWidth () : 0;
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
    public float getBorderWidth ()
    {
        final IBorder border = getBorder ();
        return border != null ? border.getWidth () : 0f;
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
                insets = shape.getBorderInsets ( c, WebDecoration.this );
            }
        }
        return insets;
    }

    @Override
    public Shape provideShape ( final E component, final Rectangle bounds )
    {
        final IShape shape = getShape ();
        return isVisible () && shape != null ? shape.getShape ( ShapeType.background, bounds, component, this ) : bounds;
    }

    @Override
    public void paint ( final Graphics2D g2d, final Rectangle bounds, final E c )
    {
        // Painting only if decoration should be visible
        if ( isVisible () )
        {
            // Checking shape existance
            final IShape shape = getShape ();
            if ( shape != null )
            {
                // Setup settings
                final Object aa = GraphicsUtils.setupAntialias ( g2d );
                final Composite oc = GraphicsUtils.setupAlphaComposite ( g2d, getTransparency (), getTransparency () < 1f );

                // Outer shade
                final IShadow outer = getShade ( ShadowType.outer );
                if ( outer != null && shape.isVisible ( ShapeType.outerShade, c, WebDecoration.this ) )
                {
                    final Shape s = shape.getShape ( ShapeType.outerShade, bounds, c, WebDecoration.this );
                    outer.paint ( g2d, bounds, c, WebDecoration.this, s );
                }

                // Painting all available backgrounds
                final List<IBackground> backgrounds = getBackgrounds ();
                if ( !CollectionUtils.isEmpty ( backgrounds ) && shape.isVisible ( ShapeType.background, c, WebDecoration.this ) )
                {
                    final Shape s = shape.getShape ( ShapeType.background, bounds, c, WebDecoration.this );
                    for ( final IBackground background : backgrounds )
                    {
                        background.paint ( g2d, bounds, c, WebDecoration.this, s );
                    }
                }

                // Painting inner shade
                final IShadow inner = getShade ( ShadowType.inner );
                if ( inner != null && shape.isVisible ( ShapeType.innerShade, c, WebDecoration.this ) )
                {
                    final Shape s = shape.getShape ( ShapeType.innerShade, bounds, c, WebDecoration.this );
                    inner.paint ( g2d, bounds, c, WebDecoration.this, s );
                }

                // Painting border
                final IBorder border = getBorder ();
                if ( border != null && shape.isVisible ( ShapeType.border, c, WebDecoration.this ) )
                {
                    final Shape s = shape.getShape ( ShapeType.border, bounds, c, WebDecoration.this );
                    border.paint ( g2d, bounds, c, WebDecoration.this, s );

                    // Painting side lines
                    // This is a temporary solution
                    if ( shape instanceof WebShape )
                    {
                        final WebShape webShape = ( WebShape ) shape;
                        final boolean ltr = c.getComponentOrientation ().isLeftToRight ();
                        final boolean paintTop = webShape.isPaintTop ( c );
                        final boolean paintBottom = webShape.isPaintBottom ( c );
                        final boolean actualPaintLeft = ltr ? webShape.isPaintLeft ( c ) : webShape.isPaintRight ( c );
                        final boolean actualPaintRight = ltr ? webShape.isPaintRight ( c ) : webShape.isPaintLeft ( c );
                        final boolean paintTopLine = webShape.isPaintTopLine ( c );
                        final boolean paintBottomLine = webShape.isPaintBottomLine ( c );
                        final boolean actualPaintLeftLine = ltr ? webShape.isPaintLeftLine ( c ) : webShape.isPaintRightLine ( c );
                        final boolean actualPaintRightLine = ltr ? webShape.isPaintRightLine ( c ) : webShape.isPaintLeftLine ( c );
                        final int shadeWidth = getShadeWidth ( ShadowType.outer );
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
                }

                // Restoring settings
                GraphicsUtils.restoreComposite ( g2d, oc );
                GraphicsUtils.restoreAntialias ( g2d, aa );
            }
        }
    }

    @Override
    public I merge ( final I decoration )
    {
        super.merge ( decoration );
        shapes = MergeUtils.merge ( shapes, decoration.shapes );
        shades = MergeUtils.merge ( shades, decoration.shades );
        borders = MergeUtils.merge ( borders, decoration.borders );
        background = MergeUtils.merge ( background, decoration.background );
        return ( I ) this;
    }
}