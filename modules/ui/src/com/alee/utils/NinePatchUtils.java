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

import com.alee.extended.painter.NinePatchIconPainter;
import com.alee.extended.painter.NinePatchStatePainter;
import com.alee.global.StyleConstants;
import com.alee.graphics.filters.ShadowFilter;
import com.alee.utils.ninepatch.NinePatchIcon;
import com.alee.utils.ninepatch.NinePatchInterval;
import com.alee.utils.ninepatch.NinePatchIntervalType;
import com.alee.utils.xml.ResourceFile;
import com.alee.utils.xml.ResourceMap;

import java.awt.*;
import java.awt.geom.Area;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.lang.ref.WeakReference;
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
     * todo 1. Allow custom shade colors
     */

    /**
     * Constants used to form icons cache keys.
     */
    private static final String OUTER_SHADE_PREFIX = "outer";
    private static final String INNER_SHADE_PREFIX = "inner";
    private static final String SEPARATOR = ";";

    /**
     * Shade nine-patch icons cache.
     */
    private static final Map<String, WeakReference<NinePatchIcon>> shadeIconCache = new HashMap<String, WeakReference<NinePatchIcon>> ();

    /**
     * Returns cached shade nine-patch icon.
     *
     * @param shadeWidth   shade width
     * @param round        corners round
     * @param shadeOpacity shade opacity
     * @return cached shade nine-patch icon
     */
    public static NinePatchIcon getShadeIcon ( final int shadeWidth, final int round, final float shadeOpacity )
    {
        final String key = OUTER_SHADE_PREFIX + SEPARATOR + shadeWidth + SEPARATOR + round + SEPARATOR + shadeOpacity;
        final NinePatchIcon icon = getNinePatchIconFromCache ( key );
        if ( icon != null )
        {
            return icon;
        }
        else
        {
            final NinePatchIcon ninePatchIcon = createShadeIcon ( shadeWidth, round, shadeOpacity );
            shadeIconCache.put ( key, new WeakReference<NinePatchIcon> ( ninePatchIcon ) );
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
    public static NinePatchIcon createShadeIcon ( final int shadeWidth, final int round, final float shadeOpacity )
    {
        // Calculating width for temprorary image
        final int inner = Math.max ( shadeWidth, round ) / 2;
        final int width = shadeWidth * 2 + inner * 2;

        // Creating template image
        final BufferedImage bi = new BufferedImage ( width, width, BufferedImage.TYPE_INT_ARGB );
        final Graphics2D ig = bi.createGraphics ();
        GraphicsUtils.setupAntialias ( ig );
        ig.setPaint ( Color.BLACK );
        ig.fillRoundRect ( shadeWidth, shadeWidth, width - shadeWidth * 2, width - shadeWidth * 2, round * 2, round * 2 );
        ig.dispose ();

        // Creating shade image
        final ShadowFilter sf = new ShadowFilter ( shadeWidth, 0, 0, shadeOpacity );
        final BufferedImage shade = sf.filter ( bi, null );

        // Clipping shade image
        final Graphics2D g2d = shade.createGraphics ();
        GraphicsUtils.setupAntialias ( g2d );
        g2d.setComposite ( AlphaComposite.getInstance ( AlphaComposite.SRC_IN ) );
        g2d.setPaint ( StyleConstants.transparent );
        g2d.fillRoundRect ( shadeWidth, shadeWidth, width - shadeWidth * 2, width - shadeWidth * 2, round * 2, round * 2 );
        g2d.dispose ();

        // Creating nine-patch icon
        final NinePatchIcon ninePatchIcon = NinePatchIcon.create ( shade );
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
    public static NinePatchIcon getInnerShadeIcon ( final int shadeWidth, final int round, final float shadeOpacity )
    {
        final String key = INNER_SHADE_PREFIX + SEPARATOR + shadeWidth + SEPARATOR + round + SEPARATOR + shadeOpacity;
        final NinePatchIcon icon = getNinePatchIconFromCache ( key );
        if ( icon != null )
        {
            return icon;
        }
        else
        {
            final NinePatchIcon ninePatchIcon = createInnerShadeIcon ( shadeWidth, round, shadeOpacity );
            shadeIconCache.put ( key, new WeakReference<NinePatchIcon> ( ninePatchIcon ) );
            return ninePatchIcon;
        }
    }

    /**
     * Fetches the nine-patch icon from the cache.
     *
     * @param key Cache key.
     * @return Nine-patch icon from the cache or null on cache miss.
     */
    private static NinePatchIcon getNinePatchIconFromCache ( final String key )
    {
        return shadeIconCache.containsKey ( key ) ? shadeIconCache.get ( key ).get () : null;
    }

    /**
     * Returns inner shade nine-patch icon.
     *
     * @param shadeWidth   shade width
     * @param round        corners round
     * @param shadeOpacity shade opacity
     * @return inner shade nine-patch icon
     */
    public static NinePatchIcon createInnerShadeIcon ( final int shadeWidth, final int round, final float shadeOpacity )
    {
        // Calculating width for temprorary image
        final int inner = Math.max ( shadeWidth, round );
        int width = shadeWidth * 2 + inner * 2;

        // Creating template image
        final BufferedImage bi = new BufferedImage ( width, width, BufferedImage.TYPE_INT_ARGB );
        final Graphics2D ig = bi.createGraphics ();
        GraphicsUtils.setupAntialias ( ig );
        final Area area = new Area ( new Rectangle ( 0, 0, width, width ) );
        area.exclusiveOr ( new Area (
                new RoundRectangle2D.Double ( shadeWidth, shadeWidth, width - shadeWidth * 2, width - shadeWidth * 2, round * 2,
                        round * 2 ) ) );
        ig.setPaint ( Color.BLACK );
        ig.fill ( area );
        ig.dispose ();

        // Creating shade image
        final ShadowFilter sf = new ShadowFilter ( shadeWidth, 0, 0, shadeOpacity );
        final BufferedImage shade = sf.filter ( bi, null );

        // Clipping shade image
        final Graphics2D g2d = shade.createGraphics ();
        GraphicsUtils.setupAntialias ( g2d );
        g2d.setComposite ( AlphaComposite.getInstance ( AlphaComposite.SRC_IN ) );
        g2d.setPaint ( StyleConstants.transparent );
        g2d.fill ( area );
        g2d.dispose ();

        final BufferedImage croppedShade = shade.getSubimage ( shadeWidth, shadeWidth, width - shadeWidth * 2, width - shadeWidth * 2 );
        width = croppedShade.getWidth ();

        // Creating nine-patch icon
        final NinePatchIcon ninePatchIcon = NinePatchIcon.create ( croppedShade );
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
    public static List<NinePatchInterval> parseIntervals ( final BufferedImage image, final NinePatchIntervalType intervalType )
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
            final int rgb;
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
    public static List<NinePatchInterval> parseStretchIntervals ( final boolean[] filled )
    {
        final List<NinePatchInterval> intervals = new ArrayList<NinePatchInterval> ();
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

    /**
     * Returns rotated by 90 degrees clockwise NinePatchIcon.
     * This method also modifies patches information properly.
     *
     * @param icon NinePatchIcon to rotate
     * @return rotated by 90 degrees clockwise NinePatchIcon
     */
    public static NinePatchIcon rotateIcon90CW ( final NinePatchIcon icon )
    {
        final BufferedImage rawImage = ImageUtils.rotateImage90CW ( icon.getRawImage () );
        final NinePatchIcon rotated = NinePatchIcon.create ( rawImage );

        // Rotating stretch information
        rotated.setHorizontalStretch ( CollectionUtils.copy ( icon.getVerticalStretch () ) );
        rotated.setVerticalStretch ( CollectionUtils.copy ( icon.getHorizontalStretch () ) );

        // Rotating margin
        final Insets om = icon.getMargin ();
        rotated.setMargin ( om.left, om.bottom, om.right, om.top );

        return rotated;
    }

    /**
     * Returns rotated by 90 degrees counter-clockwise NinePatchIcon.
     * This method also modifies patches information properly.
     *
     * @param icon NinePatchIcon to rotate
     * @return rotated by 90 degrees counter-clockwise NinePatchIcon
     */
    public static NinePatchIcon rotateIcon90CCW ( final NinePatchIcon icon )
    {
        final BufferedImage rawImage = ImageUtils.rotateImage90CCW ( icon.getRawImage () );
        final NinePatchIcon rotated = NinePatchIcon.create ( rawImage );

        // Rotating stretch information
        rotated.setHorizontalStretch ( CollectionUtils.copy ( icon.getVerticalStretch () ) );
        rotated.setVerticalStretch ( CollectionUtils.copy ( icon.getHorizontalStretch () ) );

        // Rotating margin
        final Insets om = icon.getMargin ();
        rotated.setMargin ( om.right, om.top, om.left, om.bottom );

        return rotated;
    }

    /**
     * Returns rotated by 180 degrees NinePatchIcon.
     * This method also modifies patches information properly.
     *
     * @param icon NinePatchIcon to rotate
     * @return rotated by 180 degrees NinePatchIcon
     */
    public static NinePatchIcon rotateIcon180 ( final NinePatchIcon icon )
    {
        final BufferedImage rawImage = ImageUtils.rotateImage180 ( icon.getRawImage () );
        final NinePatchIcon rotated = NinePatchIcon.create ( rawImage );

        // Rotating stretch information
        rotated.setHorizontalStretch ( CollectionUtils.copy ( icon.getHorizontalStretch () ) );
        rotated.setVerticalStretch ( CollectionUtils.copy ( icon.getVerticalStretch () ) );

        // Rotating margin
        final Insets om = icon.getMargin ();
        rotated.setMargin ( om.bottom, om.right, om.top, om.left );

        return rotated;
    }

    /**
     * Returns NinePatchIcon which is read from the source.
     *
     * @param source one of possible sources: URL, String, File, Reader, InputStream
     * @return NinePatchIcon
     */
    public static NinePatchIcon loadNinePatchIcon ( final Object source )
    {
        return loadNinePatchIcon ( XmlUtils.loadResourceFile ( source ) );
    }

    /**
     * Returns NinePatchIcon which is read from specified ResourceFile.
     *
     * @param resource file description
     * @return NinePatchIcon
     */
    public static NinePatchIcon loadNinePatchIcon ( final ResourceFile resource )
    {
        return new NinePatchIcon ( XmlUtils.loadImageIcon ( resource ) );
    }

    /**
     * Returns NinePatchStatePainter which is read from the source.
     *
     * @param source one of possible sources: URL, String, File, Reader, InputStream
     * @return NinePatchStatePainter
     */
    public static NinePatchStatePainter loadNinePatchStatePainter ( final Object source )
    {
        return loadNinePatchStatePainter ( XmlUtils.loadResourceMap ( source ) );
    }

    /**
     * Returns NinePatchStatePainter which is read from specified ResourceMap.
     *
     * @param resourceMap ResourceFile map
     * @return NinePatchStatePainter
     */
    public static NinePatchStatePainter loadNinePatchStatePainter ( final ResourceMap resourceMap )
    {
        final NinePatchStatePainter sbp = new NinePatchStatePainter ();
        for ( final String key : resourceMap.getStates ().keySet () )
        {
            sbp.addStateIcon ( key, loadNinePatchIcon ( resourceMap.getState ( key ) ) );
        }
        return sbp;
    }

    /**
     * Returns NinePatchIconPainter which is read from the source.
     *
     * @param source one of possible sources: URL, String, File, Reader, InputStream
     * @return NinePatchIconPainter
     */
    public static NinePatchIconPainter loadNinePatchIconPainter ( final Object source )
    {
        return loadNinePatchIconPainter ( XmlUtils.loadResourceFile ( source ) );
    }

    /**
     * Returns NinePatchIconPainter which is read from specified ResourceFile.
     *
     * @param resource file description
     * @return NinePatchIconPainter
     */
    public static NinePatchIconPainter loadNinePatchIconPainter ( final ResourceFile resource )
    {
        return new NinePatchIconPainter ( loadNinePatchIcon ( resource ) );
    }
}