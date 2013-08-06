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

package com.alee.utils.ninepatch;

import com.alee.utils.ImageUtils;
import com.alee.utils.NinePatchUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * User: mgarin Date: 13.12.11 Time: 15:37
 */

public class NinePatchIcon implements Icon
{
    // Component onto which this nine-patch icon will be stretched
    private Component component;

    // Basic image without patches data
    private BufferedImage image;

    // Image patches data
    private List<NinePatchInterval> horizontalStretch;
    private List<NinePatchInterval> verticalStretch;

    // Content margin
    private Insets margin;

    // Cached data for increased performance
    private Integer cachedWidth0;
    private Integer cachedWidth1;
    private Integer cachedHeight0;
    private Integer cachedHeight1;

    /**
     * Advanced icon constructors
     */

    public NinePatchIcon ( URL url )
    {
        this ( url, null );
    }

    public NinePatchIcon ( URL url, Component component )
    {
        this ( ImageUtils.getBufferedImage ( url ), component );
    }

    public NinePatchIcon ( String iconSrc )
    {
        this ( iconSrc, null );
    }

    public NinePatchIcon ( String iconSrc, Component component )
    {
        this ( ImageUtils.getBufferedImage ( iconSrc ), component );
    }

    public NinePatchIcon ( ImageIcon imageIcon )
    {
        this ( imageIcon, null );
    }

    public NinePatchIcon ( ImageIcon imageIcon, Component component )
    {
        this ( ImageUtils.getBufferedImage ( imageIcon ), component );
    }

    public NinePatchIcon ( Image image )
    {
        this ( image, null );
    }

    public NinePatchIcon ( Image image, Component component )
    {
        this ( ImageUtils.getBufferedImage ( image ), component );
    }

    public NinePatchIcon ( BufferedImage bufferedImage )
    {
        this ( bufferedImage, null );
    }

    public NinePatchIcon ( BufferedImage bufferedImage, Component component )
    {
        this ( bufferedImage, component, false );
    }

    private NinePatchIcon ( BufferedImage bufferedImage, Component component, boolean creationFlag )
    {
        super ();

        if ( creationFlag )
        {
            // Componet for which this icon will be streched
            this.component = component;

            // Actual image
            this.image = bufferedImage;

            // Stretch variables
            horizontalStretch = new ArrayList<NinePatchInterval> ();
            verticalStretch = new ArrayList<NinePatchInterval> ();
        }
        else
        {
            // Incorrect image
            if ( bufferedImage.getWidth () < 3 || bufferedImage.getHeight () < 3 )
            {
                throw new IllegalArgumentException ( "Buffered image must be atleast 3x3 pixels size" );
            }

            // Componet for which this icon will be streched
            this.component = component;

            // Creating actual image in a compatible format
            int w = bufferedImage.getWidth () - 2;
            int h = bufferedImage.getHeight () - 2;
            image = ImageUtils.createCompatibleImage ( bufferedImage, w, h );
            Graphics2D g2d = image.createGraphics ();
            g2d.drawImage ( bufferedImage, 0, 0, w, h, 1, 1, bufferedImage.getWidth () - 1, bufferedImage.getHeight () - 1, null );
            g2d.dispose ();

            // Parsing stretch variables
            horizontalStretch = NinePatchUtils.parseIntervals ( bufferedImage, NinePatchIntervalType.horizontalStretch );
            verticalStretch = NinePatchUtils.parseIntervals ( bufferedImage, NinePatchIntervalType.verticalStretch );

            // Incorrect image
            if ( !( ( horizontalStretch.size () > 1 || horizontalStretch.size () == 1 && !horizontalStretch.get ( 0 ).isPixel () ) &&
                    ( verticalStretch.size () > 1 || verticalStretch.size () == 1 && !verticalStretch.get ( 0 ).isPixel () ) ) )
            {
                throw new IllegalArgumentException ( "There must be stretch constraints specified on image" );
            }

            // Parsing content margins
            List<NinePatchInterval> vc = NinePatchUtils.parseIntervals ( bufferedImage, NinePatchIntervalType.verticalContent );
            List<NinePatchInterval> hc = NinePatchUtils.parseIntervals ( bufferedImage, NinePatchIntervalType.horizontalContent );
            int top = vc.size () == 0 ? 0 : vc.get ( 0 ).getStart ();
            int bottom = vc.size () == 0 ? 0 : image.getHeight () - vc.get ( 0 ).getEnd () - 1;
            int left = hc.size () == 0 ? 0 : hc.get ( 0 ).getStart ();
            int right = hc.size () == 0 ? 0 : image.getWidth () - hc.get ( 0 ).getEnd () - 1;
            margin = new Insets ( top, left, bottom, right );

            // Forcing cached data calculation on initialization
            calculateFixedPixelsWidth ( true );
            calculateFixedPixelsWidth ( false );
            calculateFixedPixelsHeight ( true );
            calculateFixedPixelsHeight ( false );
        }
    }

    /**
     * New icon creation method
     */

    public static NinePatchIcon create ( BufferedImage bufferedImage )
    {
        return new NinePatchIcon ( bufferedImage, null, true );
    }

    /**
     * Basic image without patches data
     */

    public BufferedImage getImage ()
    {
        return image;
    }

    /**
     * Component onto which this nine-patch icon will be stretched
     */

    public Component getComponent ()
    {
        return component;
    }

    public void setComponent ( Component component )
    {
        this.component = component;
    }

    /**
     * Horizontal image patches data
     */

    public List<NinePatchInterval> getHorizontalStretch ()
    {
        return horizontalStretch;
    }

    public void setHorizontalStretch ( List<NinePatchInterval> horizontalStretch )
    {
        this.horizontalStretch = horizontalStretch;
        clearCachedWidthData ();
    }

    public void addHorizontalStretch ( NinePatchInterval interval )
    {
        this.horizontalStretch.add ( interval );
        clearCachedWidthData ();
    }

    public void addHorizontalStretch ( int start, int end, boolean pixel )
    {
        addHorizontalStretch ( new NinePatchInterval ( start, end, pixel ) );
    }

    /**
     * Vertical image patches data
     */

    public List<NinePatchInterval> getVerticalStretch ()
    {
        return verticalStretch;
    }

    public void setVerticalStretch ( List<NinePatchInterval> verticalStretch )
    {
        this.verticalStretch = verticalStretch;
        clearCachedHeightData ();
    }

    public void addVerticalStretch ( NinePatchInterval interval )
    {
        this.verticalStretch.add ( interval );
        clearCachedHeightData ();
    }

    public void addVerticalStretch ( int start, int end, boolean pixel )
    {
        addVerticalStretch ( new NinePatchInterval ( start, end, pixel ) );
    }

    /**
     * Content margin
     */

    public Insets getMargin ()
    {
        return margin;
    }

    public void setMargin ( Insets margin )
    {
        this.margin = margin;
    }

    /**
     * Patches painting methods
     */

    public void paintIcon ( Component c, Graphics g )
    {
        paintIcon ( ( Graphics2D ) g, 0, 0, c.getWidth (), c.getHeight () );
    }

    public void paintIcon ( Component c, Graphics g, int x, int y )
    {
        // todo Check whether this is normal or not (x and y are == 0)
        paintIcon ( ( Graphics2D ) g, 0, 0, c.getWidth (), c.getHeight () );
    }

    public void paintIcon ( Graphics2D g2d, Rectangle bounds )
    {
        paintIcon ( g2d, bounds.x, bounds.y, bounds.width, bounds.height );
    }

    public void paintIcon ( Graphics2D g2d, int x, int y, int width, int height )
    {
        final int componentWidth = Math.max ( width, getIconWidth () );
        final int componentHeight = Math.max ( height, getIconHeight () );
        final int fixedPixelsX = getFixedPixelsWidth ( false );
        final int unfixedX = componentWidth - fixedPixelsX;
        final int fixedPixelsY = getFixedPixelsHeight ( false );
        final int unfixedY = componentHeight - fixedPixelsY;

        int currentY = y;
        for ( int j = 0; j < verticalStretch.size (); j++ )
        {
            // Current Y interval
            NinePatchInterval intervalY = verticalStretch.get ( j );
            NinePatchInterval beforeY = j > 0 ? verticalStretch.get ( j - 1 ) : null;

            // Percent part height
            int intervalHeight = intervalY.getEnd () - intervalY.getStart () + 1;
            int finalHeight;
            if ( intervalY.isPixel () )
            {
                finalHeight = intervalHeight;
            }
            else
            {
                float percents = ( float ) intervalHeight / ( image.getHeight () - fixedPixelsY );
                finalHeight = Math.round ( percents * unfixedY );
            }

            int currentX = x;
            for ( int i = 0; i < horizontalStretch.size (); i++ )
            {
                // Current X interval
                NinePatchInterval intervalX = horizontalStretch.get ( i );
                NinePatchInterval beforeX = i > 0 ? horizontalStretch.get ( i - 1 ) : null;

                // Percent part width
                int intervalWidth = intervalX.getEnd () - intervalX.getStart () + 1;
                int finalWidth;
                if ( intervalX.isPixel () )
                {
                    finalWidth = intervalWidth;
                }
                else
                {
                    float percents = ( float ) intervalWidth / ( image.getWidth () - fixedPixelsX );
                    finalWidth = Math.round ( percents * unfixedX );
                }

                // Drawing image part
                g2d.drawImage ( image, currentX, currentY, currentX + finalWidth, currentY + finalHeight, intervalX.getStart (),
                        intervalY.getStart (), intervalX.getStart () + intervalWidth, intervalY.getStart () + intervalHeight, null );

                // Icrementing current X
                currentX += finalWidth;
            }

            // Icrementing current Y
            currentY += finalHeight;
        }
    }

    /**
     * Fixed minimum width for icon
     */

    public int getFixedPixelsWidth ( boolean addUnfixedSpaces )
    {
        if ( addUnfixedSpaces )
        {
            if ( cachedWidth0 == null )
            {
                cachedWidth0 = calculateFixedPixelsWidth ( addUnfixedSpaces );
            }
            return cachedWidth0;
        }
        else
        {
            if ( cachedWidth1 == null )
            {
                cachedWidth1 = calculateFixedPixelsWidth ( addUnfixedSpaces );
            }
            return cachedWidth1;
        }
    }

    private int calculateFixedPixelsWidth ( boolean addUnfixedSpaces )
    {
        int fixedPixelsX = image.getWidth ();
        for ( NinePatchInterval interval : horizontalStretch )
        {
            if ( !interval.isPixel () )
            {
                fixedPixelsX -= interval.getEnd () - interval.getStart () + 1;
                if ( addUnfixedSpaces )
                {
                    fixedPixelsX += 1;
                }
            }
        }
        return fixedPixelsX;
    }

    /**
     * Fixed minimum height for icon
     */

    public int getFixedPixelsHeight ( boolean addUnfixedSpaces )
    {
        if ( addUnfixedSpaces )
        {
            if ( cachedHeight0 == null )
            {
                cachedHeight0 = calculateFixedPixelsHeight ( addUnfixedSpaces );
            }
            return cachedHeight0;
        }
        else
        {
            if ( cachedHeight1 == null )
            {
                cachedHeight1 = calculateFixedPixelsHeight ( addUnfixedSpaces );
            }
            return cachedHeight1;
        }
    }

    private int calculateFixedPixelsHeight ( boolean addUnfixedSpaces )
    {
        int fixedPixelsY = image.getHeight ();
        for ( NinePatchInterval interval : verticalStretch )
        {
            if ( !interval.isPixel () )
            {
                fixedPixelsY -= interval.getEnd () - interval.getStart () + 1;
                if ( addUnfixedSpaces )
                {
                    fixedPixelsY += 1;
                }
            }
        }
        return fixedPixelsY;
    }

    /**
     * Data cache operations
     */

    private void clearCachedWidthData ()
    {
        cachedWidth0 = null;
        cachedWidth1 = null;
    }

    private void clearCachedHeightData ()
    {
        cachedHeight0 = null;
        cachedHeight1 = null;
    }

    /**
     * Icon sizes
     */

    public int getIconWidth ()
    {
        return Math.max ( component != null ? component.getWidth () : 0, getFixedPixelsWidth ( true ) );
    }

    public int getIconHeight ()
    {
        return Math.max ( component != null ? component.getHeight () : 0, getFixedPixelsHeight ( true ) );
    }

    public Dimension getPreferredSize ()
    {
        return new Dimension ( getFixedPixelsWidth ( true ), getFixedPixelsHeight ( true ) );
    }

    public Dimension getRealImageSize ()
    {
        return new Dimension ( getImage ().getWidth (), getImage ().getHeight () );
    }
}
