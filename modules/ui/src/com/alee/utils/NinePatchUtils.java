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

import com.alee.api.annotations.NotNull;
import com.alee.utils.ninepatch.NinePatchIcon;
import com.alee.utils.ninepatch.NinePatchInterval;
import com.alee.utils.ninepatch.NinePatchIntervalType;
import com.alee.utils.xml.Resource;

import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

/**
 * This class provides a set of utilities to work with various nine-patch images.
 *
 * @author Mikle Garin
 */
public final class NinePatchUtils
{
    /**
     * Private constructor to avoid instantiation.
     */
    private NinePatchUtils ()
    {
        throw new UtilityException ( "Utility classes are not meant to be instantiated" );
    }

    /**
     * Returns NinePatchIcon which is read from specified ResourceFile.
     *
     * @param resource file description
     * @return NinePatchIcon
     */
    @NotNull
    public static NinePatchIcon getNinePatchIcon ( @NotNull final Resource resource )
    {
        return new NinePatchIcon ( ImageUtils.getImageIcon ( resource ) );
    }

    /**
     * Returns a list of nine-patch data intervals from the specified image.
     *
     * @param image        nin-patch image to process
     * @param intervalType intervals type
     * @return list of nine-patch data intervals from the specified image
     */
    @NotNull
    public static List<NinePatchInterval> parseIntervals ( @NotNull final BufferedImage image,
                                                           @NotNull final NinePatchIntervalType intervalType )
    {
        final boolean hv = intervalType == NinePatchIntervalType.horizontalStretch ||
                intervalType == NinePatchIntervalType.verticalStretch;
        final int l = ( intervalType == NinePatchIntervalType.horizontalStretch ||
                intervalType == NinePatchIntervalType.horizontalContent ? image.getWidth () : image.getHeight () ) - 1;

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
            else
            {
                // Add pixel interval only for stretch types and non-pixel for any type
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
            // Add pixel interval only for stretch types and non-pixel for any type
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
    @NotNull
    public static List<NinePatchInterval> parseStretchIntervals ( @NotNull final boolean[] filled )
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
            else
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
    @NotNull
    public static NinePatchIcon rotateIcon90CW ( @NotNull final NinePatchIcon icon )
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
    @NotNull
    public static NinePatchIcon rotateIcon90CCW ( @NotNull final NinePatchIcon icon )
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
    @NotNull
    public static NinePatchIcon rotateIcon180 ( @NotNull final NinePatchIcon icon )
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
     * Returns shade nine-patch icon.
     *
     * @param shadeWidth   shade width
     * @param round        corners round
     * @param shadeOpacity shade opacity
     * @return shade nine-patch icon
     */
    @NotNull
    @Deprecated
    public static NinePatchIcon createShadeIcon ( final int shadeWidth, final int round, final float shadeOpacity )
    {
        // Making round value into real rounding radius
        final int r = round * 2;

        // Calculating width for temporary image
        final int inner = Math.max ( shadeWidth, round );
        final int w = shadeWidth * 2 + inner * 2;

        // Creating shade image
        final Shape shape = new RoundRectangle2D.Double ( shadeWidth, shadeWidth, w - shadeWidth * 2, w - shadeWidth * 2, r, r );
        final BufferedImage shade = ImageUtils.createShadowImage ( w, w, shape, shadeWidth, shadeOpacity, true );

        // Creating nine-patch icon based on shade image
        final NinePatchIcon ninePatchIcon = NinePatchIcon.create ( shade );
        ninePatchIcon.addHorizontalStretch ( 0, shadeWidth + inner, true );
        ninePatchIcon.addHorizontalStretch ( shadeWidth + inner + 1, w - shadeWidth - inner - 1, false );
        ninePatchIcon.addHorizontalStretch ( w - shadeWidth - inner, w, true );
        ninePatchIcon.addVerticalStretch ( 0, shadeWidth + inner, true );
        ninePatchIcon.addVerticalStretch ( shadeWidth + inner + 1, w - shadeWidth - inner - 1, false );
        ninePatchIcon.addVerticalStretch ( w - shadeWidth - inner, w, true );
        ninePatchIcon.setMargin ( shadeWidth );
        return ninePatchIcon;
    }
}