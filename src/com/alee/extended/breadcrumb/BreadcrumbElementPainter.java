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

import com.alee.extended.painter.AbstractPainter;
import com.alee.laf.StyleConstants;
import com.alee.utils.ColorUtils;
import com.alee.utils.LafUtils;
import com.alee.utils.SwingUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Area;
import java.awt.geom.GeneralPath;
import java.awt.geom.RoundRectangle2D;
import java.util.HashMap;
import java.util.Map;

/**
 * @param <E> breadcrumb element type
 * @author Mikle Garin
 * @see AbstractPainter
 * @see com.alee.extended.painter.Painter
 */

public class BreadcrumbElementPainter<E extends JComponent> extends AbstractPainter<E>
{
    protected static final float[] progressFractions = new float[]{ 0f, 0.5f, 1f };
    protected static final Color progressSideColor = new Color ( 255, 255, 255, 0 );
    protected static final Color[] progressFillColors = new Color[]{ progressSideColor, new Color ( 0, 255, 0, 100 ), progressSideColor };
    protected static final Color[] selectedProgressFillColors =
            new Color[]{ progressSideColor, new Color ( 0, 255, 0, 100 ), progressSideColor };
    //    protected static final Color[] selectedProgressFillColors =
    //            new Color[]{ StyleConstants.transparent, Color.WHITE, StyleConstants.transparent };
    protected static final Color[] progressLineColors = new Color[]{ progressSideColor, Color.GRAY, progressSideColor };

    protected static final float[] shadeFractions = new float[]{ 0f, 0.25f, 0.75f, 1f };
    protected static final Color[] shadeColors =
            new Color[]{ StyleConstants.transparent, StyleConstants.shadeColor, StyleConstants.shadeColor, StyleConstants.transparent };

    protected Map<String, GeneralPath> borderShapeCache = new HashMap<String, GeneralPath> ();
    protected Map<String, Shape> fillShapeCache = new HashMap<String, Shape> ();

    protected int overlap = WebBreadcrumbStyle.elementOverlap;

    protected int shadeWidth = WebBreadcrumbStyle.shadeWidth;
    protected Color borderColor = WebBreadcrumbStyle.borderColor;
    protected Color disabledBorderColor = WebBreadcrumbStyle.disabledBorderColor;

    protected Color bgTop = WebBreadcrumbStyle.bgTop;
    protected Color bgBottom = WebBreadcrumbStyle.bgBottom;
    protected Color selectedBgColor = WebBreadcrumbStyle.selectedBgColor;

    protected BreadcrumbElementType type = BreadcrumbElementType.middle;

    protected boolean showProgress = false;
    protected float progress = 0f;

    public BreadcrumbElementPainter ()
    {
        super ();
    }

    public int getOverlap ()
    {
        return overlap;
    }

    public void setOverlap ( int overlap )
    {
        this.overlap = overlap;
        borderShapeCache.clear ();
        fireUpdate ();
    }

    public BreadcrumbElementType getType ()
    {
        return type;
    }

    public void setType ( BreadcrumbElementType type )
    {
        this.type = type;
        borderShapeCache.clear ();
        fireUpdate ();
    }

    public boolean isShowProgress ()
    {
        return showProgress;
    }

    public void setShowProgress ( boolean showProgress )
    {
        this.showProgress = showProgress;
        fireRepaint ();
    }

    public float getProgress ()
    {
        return progress;
    }

    public void setProgress ( float progress )
    {
        this.progress = Math.min ( 1f, progress );
        fireRepaint ();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Insets getMargin ( E c )
    {
        int left;
        if ( type.equals ( BreadcrumbElementType.none ) )
        {
            left = 0;
        }
        else
        {
            left = type.equals ( BreadcrumbElementType.start ) ? 0 : overlap;
        }
        int right;
        if ( type.equals ( BreadcrumbElementType.none ) )
        {
            right = 0;
        }
        else
        {
            right = type.equals ( BreadcrumbElementType.end ) ? 0 : overlap + shadeWidth;
        }
        return new Insets ( 0, left, 0, right );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void paint ( Graphics2D g2d, Rectangle bounds, E c )
    {
        final int br = BreadcrumbUtils.getRound ( c );
        final boolean ltr = c.getComponentOrientation ().isLeftToRight ();

        // Antialias
        final Object old = LafUtils.setupAntialias ( g2d );

        // Variables
        boolean selected = false;
        if ( c instanceof AbstractButton )
        {
            AbstractButton ab = ( AbstractButton ) c;
            ButtonModel bm = ab.getModel ();
            selected = bm.isPressed () || bm.isSelected ();
        }

        // Background shape
        final Shape fs = getFillShape ( c, ltr, br );

        // Painting element
        if ( !type.equals ( BreadcrumbElementType.end ) && !type.equals ( BreadcrumbElementType.none ) )
        {
            // Border shape
            final Shape bs = getBorderShape ( c, ltr );
            final Rectangle rect = bs.getBounds ();

            // Outer shade
            if ( c.isEnabled () && !selected )
            {
                g2d.setPaint ( new LinearGradientPaint ( 0, rect.y, 0, rect.y + rect.height, shadeFractions, shadeColors ) );
                LafUtils.drawShade ( g2d, bs, WebBreadcrumbStyle.shadeType, null, shadeWidth );
            }

            // Background
            g2d.setPaint ( selected ? selectedBgColor : new GradientPaint ( 0, 0, bgTop, 0, c.getHeight (), bgBottom ) );
            g2d.fill ( fs );

            // Inner shade
            if ( c.isEnabled () && selected )
            {
                g2d.setPaint ( new LinearGradientPaint ( 0, rect.y, 0, rect.y + rect.height, shadeFractions, shadeColors ) );
                LafUtils.drawShade ( g2d, bs, WebBreadcrumbStyle.shadeType, null, shadeWidth, bs );
            }

            // Border
            final Color bc = c.isEnabled () ? borderColor : disabledBorderColor;
            final Color sideColor = ColorUtils.changeAlpha ( bc, 20 );
            final Color[] borderColors = { sideColor, bc, bc, sideColor };
            g2d.setPaint ( new LinearGradientPaint ( 0, rect.y, 0, rect.y + rect.height, shadeFractions, borderColors ) );
            g2d.draw ( bs );
        }
        else
        {
            // Background
            g2d.setPaint ( selected ? selectedBgColor : new GradientPaint ( 0, 0, bgTop, 0, c.getHeight (), bgBottom ) );
            g2d.fill ( fs );
        }

        // Progress background
        if ( showProgress && progress > 0f )
        {
            Shape progressFillShape = getProgressFillShape ( c, fs );
            Rectangle pb = progressFillShape.getBounds ();

            // Background fill
            g2d.setPaint ( getProgressPaint ( c ) );
            g2d.fill ( progressFillShape );

            // Line with proper background-shaped clipping
            Shape oldClip = LafUtils.intersectClip ( g2d, fs );
            g2d.setPaint ( getProgressLinePaint ( c ) );
            g2d.drawLine ( ltr ? pb.x + pb.width : pb.x, pb.y, ltr ? pb.x + pb.width : pb.x, pb.y + pb.height );
            LafUtils.restoreClip ( g2d, oldClip );
        }

        // Restoring antialias
        LafUtils.restoreAntialias ( g2d, old );
    }

    protected LinearGradientPaint getProgressPaint ( E c )
    {
        boolean pressed = false;
        if ( c instanceof AbstractButton )
        {
            ButtonModel bm = ( ( AbstractButton ) c ).getModel ();
            pressed = bm.isPressed () || bm.isSelected ();
        }
        return new LinearGradientPaint ( 0, 0, 0, c.getHeight (), progressFractions,
                pressed ? selectedProgressFillColors : progressFillColors );
    }

    protected LinearGradientPaint getProgressLinePaint ( E c )
    {
        return new LinearGradientPaint ( 0, 0, 0, c.getHeight (), progressFractions, progressLineColors );
    }

    public GeneralPath getBorderShape ( E c, boolean ltr )
    {
        String key = ltr + ":" + c.getWidth () + "," + c.getHeight ();
        GeneralPath bs = borderShapeCache.get ( key );
        if ( bs == null )
        {
            borderShapeCache.clear ();
            bs = getBorderShapeImpl ( c, ltr );
            borderShapeCache.put ( key, bs );
        }
        return bs;
    }

    protected GeneralPath getBorderShapeImpl ( E c, boolean ltr )
    {
        GeneralPath gp = new GeneralPath ( GeneralPath.WIND_EVEN_ODD );
        if ( ltr )
        {
            gp.moveTo ( c.getWidth () - overlap - shadeWidth - 1, -1 );
            gp.lineTo ( c.getWidth () - shadeWidth - 1, c.getHeight () / 2 );
            gp.lineTo ( c.getWidth () - overlap - shadeWidth - 1, c.getHeight () );
        }
        else
        {
            gp.moveTo ( shadeWidth + overlap, -1 );
            gp.lineTo ( shadeWidth, c.getHeight () / 2 );
            gp.lineTo ( shadeWidth + overlap, c.getHeight () );
        }
        return gp;
    }

    public Shape getFillShape ( E c, boolean ltr, int round )
    {
        String key = ltr + ":" + round + ":" + c.getWidth () + "," + c.getHeight ();
        Shape fs = fillShapeCache.get ( key );
        if ( fs == null )
        {
            fillShapeCache.clear ();
            fs = getFillShapeImpl ( c, ltr, round );
            fillShapeCache.put ( key, fs );
        }
        return fs;
    }

    protected Shape getFillShapeImpl ( E c, boolean ltr, int round )
    {
        final int width = c.getWidth ();
        final int height = c.getHeight ();
        boolean encloseLast = isEncloseLastElement ( c );
        if ( c.getParent () != null && c.getParent ().getComponentCount () == 1 && !encloseLast )
        {
            if ( round > 0 )
            {
                return new RoundRectangle2D.Double ( 0, 0, width, height, round, round );
            }
            else
            {
                return SwingUtils.size ( c );
            }
        }
        else if ( !type.equals ( BreadcrumbElementType.end ) )
        {
            GeneralPath gp = new GeneralPath ( GeneralPath.WIND_EVEN_ODD );
            if ( ltr )
            {
                gp.moveTo ( width - overlap - shadeWidth - 1, 0 );
                gp.lineTo ( width - shadeWidth - 1, height / 2 );
                gp.lineTo ( width - overlap - shadeWidth - 1, height );
                if ( round > 0 && type.equals ( BreadcrumbElementType.start ) )
                {
                    gp.lineTo ( round, height );
                    gp.quadTo ( 0, height, 0, height - round );
                    gp.lineTo ( 0, round );
                    gp.quadTo ( 0, 0, round, 0 );
                }
                else
                {
                    gp.lineTo ( 0, height );
                    gp.lineTo ( 0, 0 );
                }
                gp.closePath ();
            }
            else
            {
                gp.moveTo ( shadeWidth + overlap, 0 );
                gp.lineTo ( shadeWidth, height / 2 );
                gp.lineTo ( shadeWidth + overlap, height );
                if ( round > 0 && type.equals ( BreadcrumbElementType.start ) )
                {
                    gp.lineTo ( width - round, height );
                    gp.quadTo ( width, height, width, height - round );
                    gp.lineTo ( width, round );
                    gp.quadTo ( width, 0, width - round, 0 );
                }
                else
                {
                    gp.lineTo ( width, height );
                    gp.lineTo ( width, 0 );
                }
                gp.closePath ();
            }
            return gp;
        }
        else
        {
            if ( round > 0 )
            {
                GeneralPath gp = new GeneralPath ( GeneralPath.WIND_EVEN_ODD );
                if ( ltr )
                {
                    gp.moveTo ( 0, 0 );
                    gp.lineTo ( width - round, 0 );
                    gp.quadTo ( width, 0, width, round );
                    gp.lineTo ( width, height - round );
                    gp.quadTo ( width, height, width - round, height );
                    gp.lineTo ( 0, height );
                }
                else
                {
                    gp.moveTo ( width, 0 );
                    gp.lineTo ( round, 0 );
                    gp.quadTo ( 0, 0, 0, round );
                    gp.lineTo ( 0, height - round );
                    gp.quadTo ( 0, height, round, height );
                    gp.lineTo ( width, height );
                }
                gp.closePath ();
                return gp;
            }
            else
            {
                return SwingUtils.size ( c );
            }
        }
    }

    protected boolean isEncloseLastElement ( E c )
    {
        return c.getParent () != null && c.getParent () instanceof WebBreadcrumb &&
                ( ( WebBreadcrumb ) c.getParent () ).isEncloseLastElement ();
    }

    public Shape getProgressFillShape ( E c, Shape fillShape )
    {
        boolean ltr = c.getComponentOrientation ().isLeftToRight ();
        Area fill = new Area ( fillShape );

        Rectangle bounds = fill.getBounds ();
        int oldWidth = bounds.width;
        bounds.width = Math.round ( oldWidth * progress );
        bounds.x = ltr ? bounds.x : bounds.x + oldWidth - bounds.width;
        fill.intersect ( new Area ( bounds ) );

        return fill;
    }
}