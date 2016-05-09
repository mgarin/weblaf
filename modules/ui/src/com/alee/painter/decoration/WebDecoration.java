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

import com.alee.managers.style.Bounds;
import com.alee.painter.decoration.background.IBackground;
import com.alee.painter.decoration.border.IBorder;
import com.alee.painter.decoration.content.IContent;
import com.alee.painter.decoration.layout.IContentLayout;
import com.alee.painter.decoration.shadow.IShadow;
import com.alee.painter.decoration.shadow.ShadowType;
import com.alee.painter.decoration.shape.IShape;
import com.alee.painter.decoration.shape.ShapeType;
import com.alee.painter.decoration.shape.WebShape;
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
public class WebDecoration<E extends JComponent, I extends WebDecoration<E, I>> extends AbstractDecoration<E, I>
{
    /**
     * Decoration shape.
     * It defines the shape of the decoration shade, border, background and might be used for other decoration elements.
     * Implicit list is only used to provide convenient XML descriptor for this field, only one shape can be provided at a time.
     *
     * @see com.alee.painter.decoration.shape.IShape
     * @see com.alee.painter.decoration.shape.AbstractShape
     */
    @XStreamImplicit
    protected List<IShape> shapes = new ArrayList<IShape> ( 1 );

    /**
     * Optional decoration shades.
     * Maximum two different shades could be provided at the same time - outer and inner.
     *
     * @see com.alee.painter.decoration.shadow.IShadow
     * @see com.alee.painter.decoration.shadow.AbstractShadow
     */
    @XStreamImplicit
    protected List<IShadow> shades = new ArrayList<IShadow> ( 1 );

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
     * Optional decoration contents layout.
     * You can only provide single layout per decoration instance.
     * Implicit list is only used to provide convenient XML descriptor for this field.
     *
     * @see com.alee.painter.decoration.layout.IContentLayout
     */
    @XStreamImplicit
    protected List<IContentLayout> layout = new ArrayList<IContentLayout> ( 1 );

    /**
     * Optional decoration contents.
     * It can be anything contained within the decoration or placed on top of the decoration.
     * Contents are placed based on either layout, if one was provided, or their own bounds setting.
     *
     * @see com.alee.painter.decoration.content.IContent
     * @see com.alee.painter.decoration.content.AbstractContent
     */
    @XStreamImplicit
    protected List<IContent> contents = new ArrayList<IContent> ( 1 );

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

    /**
     * Returns decoration contents layout.
     *
     * @return decoration contents layout
     */
    public IContentLayout getLayout ()
    {
        return !CollectionUtils.isEmpty ( layout ) ? layout.get ( 0 ) : null;
    }

    /**
     * Returns decoration contents.
     *
     * @return decoration contents
     */
    public List<IContent> getContents ()
    {
        return !CollectionUtils.isEmpty ( contents ) ? contents : null;
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
        // todo Add ShapeType into PainterShapeProvider interface
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
                final Object oaa = GraphicsUtils.setupAntialias ( g2d );
                final Composite oc = GraphicsUtils.setupAlphaComposite ( g2d, getOpacity (), getOpacity () < 1f );
                final Rectangle cl = g2d.getClip () instanceof Rectangle ? ( Rectangle ) g2d.getClip () : c.getVisibleRect ();
                final Shape ocl = GraphicsUtils.setupClip ( g2d, Bounds.margin.of ( c, this, bounds ).intersection ( cl ) );

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
                    // todo This is a temporary solution
                    if ( shape instanceof WebShape )
                    {
                        final WebShape webShape = ( WebShape ) shape;
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

                // Painting contents
                final List<IContent> contents = getContents ();
                if ( contents != null )
                {
                    // Checking contents layout existance
                    final IContentLayout layout = getLayout ();
                    final List<Rectangle> cb = layout != null ? layout.layout ( bounds, c, this, contents ) : null;

                    // Painting contents in appropriate bounds
                    for ( int i = 0; i < contents.size (); i++ )
                    {
                        // Using either content layout or default centered placement
                        final IContent content = contents.get ( i );
                        final Rectangle b = cb != null ? cb.get ( i ) : content.getBoundsType ().of ( c, this, bounds );
                        content.paint ( g2d, b, c, WebDecoration.this );
                    }
                }

                // Restoring settings
                GraphicsUtils.restoreClip ( g2d, ocl );
                GraphicsUtils.restoreAntialias ( g2d, oaa );
                GraphicsUtils.restoreComposite ( g2d, oc );
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
        layout = MergeUtils.merge ( layout, decoration.layout );
        contents = MergeUtils.merge ( contents, decoration.contents );
        return ( I ) this;
    }
}