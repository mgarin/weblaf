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

package com.alee.utils;

import com.alee.graphics.filters.ShadowFilter;
import com.alee.laf.StyleConstants;
import com.alee.utils.ninepatch.NinePatchIcon;
import com.alee.utils.ninepatch.NinePatchInterval;
import com.alee.utils.ninepatch.NinePatchIntervalType;

import java.awt.*;
import java.awt.geom.Area;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class provides a set of utilities to work with various nine-patch images.
 *
 * @author Mikle Garin
 */

public final class NinePatchUtils
{
    /**
     * Shade nine-patch icons cache.
     */
    private static Map<String, NinePatchIcon> shadeIconCache = new HashMap<String, NinePatchIcon> ();

    /**
     * Returns cached shade nine-patch icon.
     *
     * @param shadeWidth   shade width
     * @param round        corners round
     * @param shadeOpacity shade opacity
     * @return cached shade nine-patch icon
     */
    public static NinePatchIcon getShadeIcon ( int shadeWidth, int round, float shadeOpacity )
    {
        String key = "outer;" + shadeWidth + ";" + round + ";" + shadeOpacity;
        if ( shadeIconCache.containsKey ( key ) )
        {
            return shadeIconCache.get ( key );
        }
        else
        {
            NinePatchIcon ninePatchIcon = createShadeIcon ( shadeWidth, round, shadeOpacity );
            shadeIconCache.put ( key, ninePatchIcon );
            return ninePatchIcon;
        }
    }

    /**
     * Returns shade nine-patch icon.
     *
     * @param shadeWidth   shade width
     * @param round        corners round
     * @param shadeOpacity shade opacity
     * @return shade nine-patch icon
     */
    public static NinePatchIcon createShadeIcon ( int shadeWidth, int round, float shadeOpacity )
    {
        // Calculating width for temprorary image
        int inner = Math.max ( shadeWidth, round ) / 2;
        int width = shadeWidth * 2 + inner * 2;

        // Creating template image
        BufferedImage bi = new BufferedImage ( width, width, BufferedImage.TYPE_INT_ARGB );
        Graphics2D ig = bi.createGraphics ();
        LafUtils.setupAntialias ( ig );
        ig.setPaint ( Color.BLACK );
        ig.fillRoundRect ( shadeWidth, shadeWidth, width - shadeWidth * 2, width - shadeWidth * 2, round * 2, round * 2 );
        ig.dispose ();

        // Creating shade image
        ShadowFilter sf = new ShadowFilter ( shadeWidth, 0, 0, shadeOpacity );
        BufferedImage shade = sf.filter ( bi, null );

        // Clipping shade image
        Graphics2D g2d = shade.createGraphics ();
        LafUtils.setupAntialias ( g2d );
        g2d.setComposite ( AlphaComposite.getInstance ( AlphaComposite.SRC_IN ) );
        g2d.setPaint ( StyleConstants.transparent );
        g2d.fillRoundRect ( shadeWidth, shadeWidth, width - shadeWidth * 2, width - shadeWidth * 2, round * 2, round * 2 );
        g2d.dispose ();

        // Creating nine-patch icon
        NinePatchIcon ninePatchIcon = NinePatchIcon.create ( shade );
        ninePatchIcon.addHorizontalStretch ( 0, shadeWidth + inner, true );
        ninePatchIcon.addHorizontalStretch ( shadeWidth + inner + 1, width - shadeWidth - inner - 1, false );
        ninePatchIcon.addHorizontalStretch ( width - shadeWidth - inner, width, true );
        ninePatchIcon.addVerticalStretch ( 0, shadeWidth + inner, true );
        ninePatchIcon.addVerticalStretch ( shadeWidth + inner + 1, width - shadeWidth - inner - 1, false );
        ninePatchIcon.addVerticalStretch ( width - shadeWidth - inner, width, true );
        ninePatchIcon.setMargin ( shadeWidth );
        return ninePatchIcon;
    }

    /**
     * Returns cached inner shade nine-patch icon.
     *
     * @param shadeWidth   shade width
     * @param round        corners round
     * @param shadeOpacity shade opacity
     * @return cached inner shade nine-patch icon
     */
    public static NinePatchIcon getInnerShadeIcon ( int shadeWidth, int round, float shadeOpacity )
    {
        String key = "inner;" + shadeWidth + ";" + round + ";" + shadeOpacity;
        if ( shadeIconCache.containsKey ( key ) )
        {
            return shadeIconCache.get ( key );
        }
        else
        {
            NinePatchIcon ninePatchIcon = createInnerShadeIcon ( shadeWidth, round, shadeOpacity );
            shadeIconCache.put ( key, ninePatchIcon );
            return ninePatchIcon;
        }
    }

    /**
     * Returns inner shade nine-patch icon.
     *
     * @param shadeWidth   shade width
     * @param round        corners round
     * @param shadeOpacity shade opacity
     * @return inner shade nine-patch icon
     */
    public static NinePatchIcon createInnerShadeIcon ( int shadeWidth, int round, float shadeOpacity )
    {
        // Calculating width for temprorary image
        int inner = Math.max ( shadeWidth, round );
        int width = shadeWidth * 2 + inner * 2;

        // Creating template image
        BufferedImage bi = new BufferedImage ( width, width, BufferedImage.TYPE_INT_ARGB );
        Graphics2D ig = bi.createGraphics ();
        LafUtils.setupAntialias ( ig );
        Area area = new Area ( new Rectangle ( 0, 0, width, width ) );
        area.exclusiveOr ( new Area (
                new RoundRectangle2D.Double ( shadeWidth, shadeWidth, width - shadeWidth * 2, width - shadeWidth * 2, round * 2,
                        round * 2 ) ) );
        ig.setPaint ( Color.BLACK );
        ig.fill ( area );
        ig.dispose ();

        // Creating shade image
        ShadowFilter sf = new ShadowFilter ( shadeWidth, 0, 0, shadeOpacity );
        BufferedImage shade = sf.filter ( bi, null );

        // Clipping shade image
        Graphics2D g2d = shade.createGraphics ();
        LafUtils.setupAntialias ( g2d );
        g2d.setComposite ( AlphaComposite.getInstance ( AlphaComposite.SRC_IN ) );
        g2d.setPaint ( StyleConstants.transparent );
        g2d.fill ( area );
        g2d.dispose ();

        BufferedImage croppedShade = shade.getSubimage ( shadeWidth, shadeWidth, width - shadeWidth * 2, width - shadeWidth * 2 );
        width = croppedShade.getWidth ();

        // Creating nine-patch icon
        NinePatchIcon ninePatchIcon = NinePatchIcon.create ( croppedShade );
        ninePatchIcon.addHorizontalStretch ( 0, inner, true );
        ninePatchIcon.addHorizontalStretch ( inner + 1, width - inner - 1, false );
        ninePatchIcon.addHorizontalStretch ( width - inner, width, true );
        ninePatchIcon.addVerticalStretch ( 0, inner, true );
        ninePatchIcon.addVerticalStretch ( inner + 1, width - inner - 1, false );
        ninePatchIcon.addVerticalStretch ( width - inner, width, true );
        ninePatchIcon.setMargin ( shadeWidth );
        return ninePatchIcon;
    }

    /**
     * Returns a list of nine-patch data intervals from the specified image.
     *
     * @param image        nin-patch image to process
     * @param intervalType intervals type
     * @return list of nine-patch data intervals from the specified image
     */
    public static List<NinePatchInterval> parseIntervals ( BufferedImage image, NinePatchIntervalType intervalType )
    {
        final boolean hv = intervalType.equals ( NinePatchIntervalType.horizontalStretch ) ||
                intervalType.equals ( NinePatchIntervalType.verticalStretch );
        final int l = ( intervalType.equals ( NinePatchIntervalType.horizontalStretch ) ||
                intervalType.equals ( NinePatchIntervalType.horizontalContent ) ? image.getWidth () : image.getHeight () ) - 1;

        final List<NinePatchInterval> intervals = new ArrayList<NinePatchInterval> ();
        NinePatchInterval interval = null;
        boolean pixelPart;
        for ( int i = 1; i < l; i++ )
        {
            int rgb;
            switch ( intervalType )
            {
                case horizontalStretch:
                    rgb = image.getRGB ( i, 0 );
                    break;
                case verticalStretch:
                    rgb = image.getRGB ( 0, i );
                    break;
                case horizontalContent:
                    rgb = image.getRGB ( i, image.getHeight () - 1 );
                    break;
                case verticalContent:
                    rgb = image.getRGB ( image.getWidth () - 1, i );
                    break;
                default:
                    rgb = 0;
                    break;
            }

            pixelPart = rgb != Color.BLACK.getRGB ();
            if ( interval == null )
            {
                // Initial interval
                interval = new NinePatchInterval ( i - 1, i - 1, pixelPart );
            }
            else if ( pixelPart == interval.isPixel () )
            {
                // Enlarge interval
                interval.setEnd ( i - 1 );
            }
            else if ( pixelPart != interval.isPixel () )
            {
                // Add pixel interval only for stretch types and nonpixel for any type
                if ( hv || !interval.isPixel () )
                {
                    intervals.add ( interval );
                }
                // New interval starts
                interval = new NinePatchInterval ( i - 1, i - 1, pixelPart );
            }
        }
        if ( interval != null )
        {
            // Add pixel interval only for stretch types and nonpixel for any type
            if ( hv || !interval.isPixel () )
            {
                intervals.add ( interval );
            }
        }
        return intervals;
    }

    /**
     * Returns nine-patch stretch intervals.
     *
     * @param filled pixels fill data
     * @return nine-patch stretch intervals
     */
    public static List<NinePatchInterval> parseStretchIntervals ( boolean[] filled )
    {
        List<NinePatchInterval> intervals = new ArrayList<NinePatchInterval> ();
        NinePatchInterval interval = null;
        boolean pixelPart;
        for ( int i = 0; i < filled.length; i++ )
        {
            pixelPart = !filled[ i ];
            if ( interval == null )
            {
                // Initial interval
                interval = new NinePatchInterval ( i, i, pixelPart );
            }
            else if ( pixelPart == interval.isPixel () )
            {
                // Enlarge interval
                interval.setEnd ( i );
            }
            else if ( pixelPart != interval.isPixel () )
            {
                intervals.add ( interval );

                // New interval starts
                interval = new NinePatchInterval ( i, i, pixelPart );
            }
        }
        if ( interval != null )
        {
            intervals.add ( interval );
        }
        return intervals;
    }
}