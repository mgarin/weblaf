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

import com.alee.global.StyleConstants;
import com.alee.utils.ColorUtils;
import com.alee.utils.GraphicsUtils;
import com.alee.utils.ShapeUtils;
import com.alee.utils.swing.DataProvider;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Area;
import java.awt.geom.GeneralPath;
import java.awt.geom.RoundRectangle2D;

/**
 * This class provides a set of utilities for breadcrumbs.
 * This is a library utility class and its not intended for use outside of the breadcrumbs.
 *
 * @author Mikle Garin
 */

public final class BreadcrumbUtils
{
    /**
     * Shape cache keys.
     */
    private static final String BORDER_SHAPE = "border";
    private static final String FILL_SHAPE = "fill";

    /**
     * Breadcrumb element background paint constants.
     */
    public static final float[] progressFractions = new float[]{ 0f, 0.5f, 1f };
    public static final Color progressSideColor = new Color ( 255, 255, 255, 0 );
    public static final Color[] progressFillColors = new Color[]{ progressSideColor, new Color ( 0, 255, 0, 100 ), progressSideColor };
    public static final Color[] selectedProgressFillColors =
            new Color[]{ progressSideColor, new Color ( 0, 255, 0, 100 ), progressSideColor };
    public static final Color[] progressLineColors = new Color[]{ progressSideColor, Color.GRAY, progressSideColor };
    public static final float[] shadeFractions = new float[]{ 0f, 0.25f, 0.75f, 1f };
    public static final Color[] shadeColors =
            new Color[]{ StyleConstants.transparent, new Color ( 210, 210, 210 ), new Color ( 210, 210, 210 ), StyleConstants.transparent };

    /**
     * Returns breadcrumb element margin.
     *
     * @param element element to provide margin for
     * @return breadcrumb element margin
     */
    public static Insets getElementMargin ( final JComponent element )
    {
        final int left;
        final int right;
        final Container container = element.getParent ();
        if ( container != null && container instanceof WebBreadcrumb && element instanceof BreadcrumbElement )
        {
            final WebBreadcrumb wbc = ( WebBreadcrumb ) container;
            final BreadcrumbElementType type = BreadcrumbElementType.getType ( element, wbc );
            final boolean isNone = type.equals ( BreadcrumbElementType.none );
            left = isNone ? 0 : type.equals ( BreadcrumbElementType.start ) ? 0 : wbc.getElementOverlap ();
            right = isNone ? 0 : type.equals ( BreadcrumbElementType.end ) ? 0 : wbc.getElementOverlap () + WebBreadcrumbStyle.shadeWidth;
        }
        else
        {
            left = 0;
            right = 0;
        }
        return new Insets ( 0, left, 0, right );
    }

    /**
     * Paints breadcrumb element background.
     *
     * @param g2d     graphics context
     * @param element breadcrumb element
     */
    public static void paintElementBackground ( final Graphics2D g2d, final JComponent element )
    {
        // We do not decorate anything but BreadcrumbElement ancestors
        if ( !( element instanceof BreadcrumbElement ) )
        {
            throw new IllegalArgumentException ( "This method is designed exclusively for breadcrumb elements" );
        }

        // We will paint decoration only when element is inside of the breadcrumb
        // We do it to avoid styling problems and misbehavior
        final Container container = element.getParent ();
        if ( container == null || !( container instanceof WebBreadcrumb ) )
        {
            throw new IllegalComponentStateException ( "Breadcrumb elements can only be placed in breadcrumb" );
        }

        // Antialias
        final Object old = GraphicsUtils.setupAntialias ( g2d );

        // Variables
        final WebBreadcrumb breadcrumb = ( WebBreadcrumb ) container;
        final int overlap = breadcrumb.getElementOverlap ();
        final int shadeWidth = WebBreadcrumbStyle.shadeWidth;
        final int round = 2;//WebPanelStyle.round;
        final boolean encloseLast = breadcrumb.isEncloseLastElement ();
        final BreadcrumbElement breadcrumbElement = ( BreadcrumbElement ) element;
        final int w = element.getWidth ();
        final int h = element.getHeight ();
        final BreadcrumbElementType type = BreadcrumbElementType.getType ( element, breadcrumb );
        final boolean showProgress = breadcrumbElement.isShowProgress ();
        final float progress = breadcrumbElement.getProgress ();
        final boolean ltr = element.getComponentOrientation ().isLeftToRight ();
        final boolean selected;
        if ( element instanceof AbstractButton )
        {
            final AbstractButton ab = ( AbstractButton ) element;
            final ButtonModel bm = ab.getModel ();
            selected = bm.isPressed () || bm.isSelected ();
        }
        else
        {
            selected = false;
        }

        // Background shape
        final Shape fs = getFillShape ( element, type, w, h, overlap, shadeWidth, round, encloseLast, ltr );

        // Painting element
        if ( !type.equals ( BreadcrumbElementType.end ) && !type.equals ( BreadcrumbElementType.none ) )
        {
            // Border shape
            final Shape bs = getBorderShape ( element, w, h, overlap, shadeWidth, ltr );
            final Rectangle rect = bs.getBounds ();

            // Outer shade
            if ( element.isEnabled () && !selected )
            {
                g2d.setPaint ( new LinearGradientPaint ( 0, rect.y, 0, rect.y + rect.height, shadeFractions, shadeColors ) );
                GraphicsUtils.drawShade ( g2d, bs, WebBreadcrumbStyle.shadeType, null, shadeWidth );
            }

            // Background
            g2d.setPaint ( selected ? WebBreadcrumbStyle.selectedBgColor :
                    new GradientPaint ( 0, 0, WebBreadcrumbStyle.bgTop, 0, element.getHeight (), WebBreadcrumbStyle.bgBottom ) );
            g2d.fill ( fs );

            // Inner shade
            if ( element.isEnabled () && selected )
            {
                g2d.setPaint ( new LinearGradientPaint ( 0, rect.y, 0, rect.y + rect.height, shadeFractions, shadeColors ) );
                GraphicsUtils.drawShade ( g2d, bs, WebBreadcrumbStyle.shadeType, null, shadeWidth, bs );
            }

            // Border
            final Color bc = element.isEnabled () ? WebBreadcrumbStyle.borderColor : WebBreadcrumbStyle.disabledBorderColor;
            final Color sideColor = ColorUtils.getTransparentColor ( bc, 20 );
            final Color[] borderColors = { sideColor, bc, bc, sideColor };
            g2d.setPaint ( new LinearGradientPaint ( 0, rect.y, 0, rect.y + rect.height, shadeFractions, borderColors ) );
            g2d.draw ( bs );
        }
        else
        {
            // Background
            g2d.setPaint ( selected ? WebBreadcrumbStyle.selectedBgColor :
                    new GradientPaint ( 0, 0, WebBreadcrumbStyle.bgTop, 0, element.getHeight (), WebBreadcrumbStyle.bgBottom ) );
            g2d.fill ( fs );
        }

        // Progress background
        if ( showProgress && progress > 0f )
        {
            final Shape progressFillShape = getProgressFillShape ( fs, progress, ltr );
            final Rectangle pb = progressFillShape.getBounds ();

            // Background fill
            g2d.setPaint ( getProgressPaint ( element, h ) );
            g2d.fill ( progressFillShape );

            // Line with proper background-shaped clipping
            final Shape oldClip = GraphicsUtils.intersectClip ( g2d, fs );
            g2d.setPaint ( getProgressLinePaint ( h ) );
            g2d.drawLine ( ltr ? pb.x + pb.width : pb.x, pb.y, ltr ? pb.x + pb.width : pb.x, pb.y + pb.height );
            GraphicsUtils.restoreClip ( g2d, oldClip );
        }

        // Restoring antialias
        GraphicsUtils.restoreAntialias ( g2d, old );
    }

    /**
     * Returns cached element border shape.
     *
     * @param element    breadcrumb element
     * @param w          element width
     * @param h          element height
     * @param overlap    breadcrumb element overlap
     * @param shadeWidth breadcrumb shade width
     * @param ltr        whether element has LTR orientation or not
     * @return cached element border shape
     */
    public static Shape getBorderShape ( final JComponent element, final int w, final int h, final int overlap, final int shadeWidth,
                                         final boolean ltr )
    {
        return ShapeUtils.getShape ( element, BORDER_SHAPE, new DataProvider<Shape> ()
        {
            @Override
            public Shape provide ()
            {
                return createBorderShape ( w, h, overlap, shadeWidth, ltr );
            }
        }, w, h, overlap, shadeWidth, ltr );
    }

    /**
     * Returns element border shape.
     *
     * @param w          element width
     * @param h          element height
     * @param overlap    breadcrumb element overlap
     * @param shadeWidth breadcrumb shade width
     * @param ltr        whether element has LTR orientation or not
     * @return element border shape
     */
    public static GeneralPath createBorderShape ( final int w, final int h, final int overlap, final int shadeWidth, final boolean ltr )
    {
        final GeneralPath gp = new GeneralPath ( GeneralPath.WIND_EVEN_ODD );
        if ( ltr )
        {
            gp.moveTo ( w - overlap - shadeWidth - 1, -1 );
            gp.lineTo ( w - shadeWidth - 1, h / 2 );
            gp.lineTo ( w - overlap - shadeWidth - 1, h );
        }
        else
        {
            gp.moveTo ( shadeWidth + overlap, -1 );
            gp.lineTo ( shadeWidth, h / 2 );
            gp.lineTo ( shadeWidth + overlap, h );
        }
        return gp;
    }

    /**
     * Returns cached element fill shape.
     *
     * @param element     breadcrumb element
     * @param type        element type
     * @param w           element width
     * @param h           element height
     * @param overlap     breadcrumb element overlap
     * @param shadeWidth  breadcrumb shade width
     * @param round       breadcrumb corners rounding
     * @param encloseLast whether last breadcrumb element should be enclosed or not
     * @param ltr         whether element has LTR orientation or not
     * @return cached element fill shape
     */
    public static Shape getFillShape ( final JComponent element, final BreadcrumbElementType type, final int w, final int h,
                                       final int overlap, final int shadeWidth, final int round, final boolean encloseLast,
                                       final boolean ltr )
    {
        return ShapeUtils.getShape ( element, FILL_SHAPE, new DataProvider<Shape> ()
        {
            @Override
            public Shape provide ()
            {
                return createFillShape ( element, type, w, h, overlap, shadeWidth, round, encloseLast, ltr );
            }
        }, type, w, h, overlap, shadeWidth, round, encloseLast, ltr );
    }

    /**
     * Returns element fill shape.
     *
     * @param element     breadcrumb element
     * @param type        element type
     * @param w           element width
     * @param h           element height
     * @param overlap     breadcrumb element overlap
     * @param shadeWidth  breadcrumb shade width
     * @param round       breadcrumb corners rounding
     * @param encloseLast whether last breadcrumb element should be enclosed or not
     * @param ltr         whether element has LTR orientation or not
     * @return element fill shape
     */
    public static Shape createFillShape ( final JComponent element, final BreadcrumbElementType type, final int w, final int h,
                                          final int overlap, final int shadeWidth, final int round, final boolean encloseLast,
                                          final boolean ltr )
    {
        if ( element.getParent () != null && element.getParent ().getComponentCount () == 1 && !encloseLast )
        {
            if ( round > 0 )
            {
                return new RoundRectangle2D.Double ( 0, 0, w, h, round, round );
            }
            else
            {
                return new Rectangle ( 0, 0, w, h );
            }
        }
        else if ( !type.equals ( BreadcrumbElementType.end ) )
        {
            final GeneralPath gp = new GeneralPath ( GeneralPath.WIND_EVEN_ODD );
            if ( ltr )
            {
                gp.moveTo ( w - overlap - shadeWidth - 1, 0 );
                gp.lineTo ( w - shadeWidth - 1, h / 2 );
                gp.lineTo ( w - overlap - shadeWidth - 1, h );
                if ( round > 0 && type.equals ( BreadcrumbElementType.start ) )
                {
                    gp.lineTo ( round, h );
                    gp.quadTo ( 0, h, 0, h - round );
                    gp.lineTo ( 0, round );
                    gp.quadTo ( 0, 0, round, 0 );
                }
                else
                {
                    gp.lineTo ( 0, h );
                    gp.lineTo ( 0, 0 );
                }
                gp.closePath ();
            }
            else
            {
                gp.moveTo ( shadeWidth + overlap, 0 );
                gp.lineTo ( shadeWidth, h / 2 );
                gp.lineTo ( shadeWidth + overlap, h );
                if ( round > 0 && type.equals ( BreadcrumbElementType.start ) )
                {
                    gp.lineTo ( w - round, h );
                    gp.quadTo ( w, h, w, h - round );
                    gp.lineTo ( w, round );
                    gp.quadTo ( w, 0, w - round, 0 );
                }
                else
                {
                    gp.lineTo ( w, h );
                    gp.lineTo ( w, 0 );
                }
                gp.closePath ();
            }
            return gp;
        }
        else
        {
            if ( round > 0 )
            {
                final GeneralPath gp = new GeneralPath ( GeneralPath.WIND_EVEN_ODD );
                if ( ltr )
                {
                    gp.moveTo ( 0, 0 );
                    gp.lineTo ( w - round, 0 );
                    gp.quadTo ( w, 0, w, round );
                    gp.lineTo ( w, h - round );
                    gp.quadTo ( w, h, w - round, h );
                    gp.lineTo ( 0, h );
                }
                else
                {
                    gp.moveTo ( w, 0 );
                    gp.lineTo ( round, 0 );
                    gp.quadTo ( 0, 0, 0, round );
                    gp.lineTo ( 0, h - round );
                    gp.quadTo ( 0, h, round, h );
                    gp.lineTo ( w, h );
                }
                gp.closePath ();
                return gp;
            }
            else
            {
                return new Rectangle ( 0, 0, w, h );
            }
        }
    }

    /**
     * Returns progress fill shape.
     *
     * @param fillShape element fill shape
     * @param progress  progress value
     * @param ltr       whether element has LTR orientation or not
     * @return progress fill shape
     */
    public static Shape getProgressFillShape ( final Shape fillShape, final float progress, final boolean ltr )
    {
        final Area fill = new Area ( fillShape );
        final Rectangle bounds = fill.getBounds ();
        final int oldWidth = bounds.width;
        bounds.width = Math.round ( oldWidth * progress );
        bounds.x = ltr ? bounds.x : bounds.x + oldWidth - bounds.width;
        fill.intersect ( new Area ( bounds ) );
        return fill;
    }

    /**
     * Returns progress paint.
     *
     * @param element breadcrumb element
     * @param h       element height
     * @return progress paint
     */
    public static LinearGradientPaint getProgressPaint ( final JComponent element, final int h )
    {
        boolean pressed = false;
        if ( element instanceof AbstractButton )
        {
            final ButtonModel bm = ( ( AbstractButton ) element ).getModel ();
            pressed = bm.isPressed () || bm.isSelected ();
        }
        return new LinearGradientPaint ( 0, 0, 0, h, progressFractions, pressed ? selectedProgressFillColors : progressFillColors );
    }

    /**
     * Returns progress line paint.
     *
     * @param h element height
     * @return progress line paint
     */
    public static LinearGradientPaint getProgressLinePaint ( final int h )
    {
        return new LinearGradientPaint ( 0, 0, 0, h, progressFractions, progressLineColors );
    }

    /**
     * Returns whether breadcrumb element contains specified point or not.
     *
     * @param element breadcrumb element
     * @param x       point X coordinate
     * @param y       point Y coordinate
     * @return true if breadcrumb element contains specified point, false otherwise
     */
    public static boolean contains ( final JComponent element, final int x, final int y )
    {
        final int w = element.getWidth ();
        final int h = element.getHeight ();
        final Container container = element.getParent ();
        if ( container != null && container instanceof WebBreadcrumb && element instanceof BreadcrumbElement )
        {
            final WebBreadcrumb breadcrumb = ( WebBreadcrumb ) container;
            final BreadcrumbElementType type = BreadcrumbElementType.getType ( element, breadcrumb );
            final int overlap = breadcrumb.getElementOverlap ();
            final int shadeWidth = WebBreadcrumbStyle.shadeWidth;
            final int round = 2;//WebPanelStyle.round;
            final boolean encloseLast = breadcrumb.isEncloseLastElement ();
            final boolean ltr = element.getComponentOrientation ().isLeftToRight ();
            return getFillShape ( element, type, w, h, overlap, shadeWidth, round, encloseLast, ltr ).contains ( x, y );
        }
        return 0 < x && x < w && 0 < y && y < h;
    }
}