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
 * This class allows you to create and use nine-patch icons within Swing applications.
 * <p/>
 * Basically it parses nine-patch image data (patches at the side of .9.png image) into understandable values and uses them to stretch the
 * image properly when it is painted anywhere.
 * <p/>
 * Here is a simple example how NinePatchIcon can be used as a stretchable panel background:
 * <code>
 * NinePatchIcon icon = new NinePatchIcon ( "icon.9.png" );
 * NinePatchIconPainter painter = new NinePatchIconPainter ( icon );
 * WebPanel panel = new WebPanel ( painter );
 * </code>
 *
 * @author Mikle Garin
 * @since 1.4
 */

public class NinePatchIcon implements Icon
{
    /**
     * Component onto which this nine-patch icon will be stretched.
     */
    protected Component component;

    /**
     * Raw image without patches.
     */
    protected BufferedImage rawImage;

    /**
     * Horizontal stretch intervals taken from image patches (top image patches).
     * Note that pixel parts are also included here but marked with "pixel=true" boolean value.
     */
    protected List<NinePatchInterval> horizontalStretch;

    /**
     * Vertical stretch intervals taken from image patches (left image patches).
     * Note that pixel parts are also included here but marked with "pixel=true" boolean value.
     */
    protected List<NinePatchInterval> verticalStretch;

    /**
     * Content margin taken from image patches (right and bottom patches).
     * This margin is generally valuable for components which uses this icon as a background to set their style margins properly.
     */
    protected Insets margin;

    /**
     * Cached fixed areas width of the nine-patch image with additional 1px for each stretchable area.
     */
    protected Integer cachedWidth0;

    /**
     * Cached fixed areas width of the nine-patch image.
     */
    protected Integer cachedWidth1;

    /**
     * Cached fixed areas height of the nine-patch image with additional 1px for each stretchable area.
     */
    protected Integer cachedHeight0;

    /**
     * Cached fixed areas height of the nine-patch image.
     */
    protected Integer cachedHeight1;

    /**
     * Constructs new NinePatchIcon using the nine-patch image from the specified URL.
     *
     * @param url nine-patch image URL
     */
    public NinePatchIcon ( URL url )
    {
        this ( url, null );
    }

    /**
     * Constructs new NinePatchIcon using the nine-patch image from the specified URL.
     *
     * @param url       nine-patch image URL
     * @param component component atop of which icon will be stretched
     */
    public NinePatchIcon ( URL url, Component component )
    {
        this ( ImageUtils.getBufferedImage ( url ), component );
    }

    /**
     * Constructs new NinePatchIcon using the nine-patch image from the specified path.
     *
     * @param iconSrc nine-patch image path
     */
    public NinePatchIcon ( String iconSrc )
    {
        this ( iconSrc, null );
    }

    /**
     * Constructs new NinePatchIcon using the nine-patch image from the specified path.
     *
     * @param iconSrc   nine-patch image path
     * @param component component atop of which icon will be stretched
     */
    public NinePatchIcon ( String iconSrc, Component component )
    {
        this ( ImageUtils.getBufferedImage ( iconSrc ), component );
    }

    /**
     * Constructs new NinePatchIcon using the specified nine-patch image.
     *
     * @param imageIcon nine-patch image
     */
    public NinePatchIcon ( ImageIcon imageIcon )
    {
        this ( imageIcon, null );
    }

    /**
     * Constructs new NinePatchIcon using the specified nine-patch image.
     *
     * @param imageIcon nine-patch image
     * @param component component atop of which icon will be stretched
     */
    public NinePatchIcon ( ImageIcon imageIcon, Component component )
    {
        this ( ImageUtils.getBufferedImage ( imageIcon ), component );
    }

    /**
     * Constructs new NinePatchIcon using the specified nine-patch image.
     *
     * @param image nine-patch image
     */
    public NinePatchIcon ( Image image )
    {
        this ( image, null );
    }

    /**
     * Constructs new NinePatchIcon using the specified nine-patch image.
     *
     * @param image     nine-patch image
     * @param component component atop of which icon will be stretched
     */
    public NinePatchIcon ( Image image, Component component )
    {
        this ( ImageUtils.getBufferedImage ( image ), component );
    }

    /**
     * Constructs new NinePatchIcon using the specified nine-patch image.
     *
     * @param bufferedImage nine-patch image
     */
    public NinePatchIcon ( BufferedImage bufferedImage )
    {
        this ( bufferedImage, null );
    }

    /**
     * Constructs new NinePatchIcon using the specified nine-patch image.
     *
     * @param bufferedImage nine-patch image
     * @param component     component atop of which icon will be stretched
     */
    public NinePatchIcon ( BufferedImage bufferedImage, Component component )
    {
        this ( bufferedImage, component, true );
    }

    /**
     * Constructs new NinePatchIcon using the specified nine-patch image.
     *
     * @param bufferedImage nine-patch image
     * @param component     component atop of which icon will be stretched
     * @param parsePatches  whether should parse image patches or not
     */
    protected NinePatchIcon ( BufferedImage bufferedImage, Component component, boolean parsePatches )
    {
        super ();

        if ( parsePatches )
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
            rawImage = ImageUtils.createCompatibleImage ( bufferedImage, w, h );
            Graphics2D g2d = rawImage.createGraphics ();
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
            int bottom = vc.size () == 0 ? 0 : rawImage.getHeight () - vc.get ( 0 ).getEnd () - 1;
            int left = hc.size () == 0 ? 0 : hc.get ( 0 ).getStart ();
            int right = hc.size () == 0 ? 0 : rawImage.getWidth () - hc.get ( 0 ).getEnd () - 1;
            margin = new Insets ( top, left, bottom, right );

            // Forcing cached data calculation on initialization
            calculateFixedPixelsWidth ( true );
            calculateFixedPixelsWidth ( false );
            calculateFixedPixelsHeight ( true );
            calculateFixedPixelsHeight ( false );
        }
        else
        {
            // Componet for which this icon will be streched
            this.component = component;

            // Actual image
            this.rawImage = bufferedImage;

            // Stretch variables
            horizontalStretch = new ArrayList<NinePatchInterval> ();
            verticalStretch = new ArrayList<NinePatchInterval> ();
        }
    }

    /**
     * Returns newly created NinePatchIcon with empty patches.
     *
     * @param rawImage raw image without patches
     * @return
     */
    public static NinePatchIcon create ( BufferedImage rawImage )
    {
        return new NinePatchIcon ( rawImage, null, false );
    }

    /**
     * Returns raw image without patches.
     *
     * @return raw image without patches
     */
    public BufferedImage getRawImage ()
    {
        return rawImage;
    }

    /**
     * Returns component atop of which icon will be stretched.
     *
     * @return component atop of which icon will be stretched
     */
    public Component getComponent ()
    {
        return component;
    }

    /**
     * Sets component atop of which icon will be stretched.
     *
     * @param component component atop of which icon will be stretched
     */
    public void setComponent ( Component component )
    {
        this.component = component;
    }

    /**
     * Horizontal image patches data
     */

    /**
     * Returns list of horizontal stretch intervals taken from image patches.
     *
     * @return list of horizontal stretch intervals taken from image patches
     */
    public List<NinePatchInterval> getHorizontalStretch ()
    {
        return horizontalStretch;
    }

    /**
     * Sets list of horizontal stretch intervals.
     *
     * @param horizontalStretch list of horizontal stretch intervals
     */
    public void setHorizontalStretch ( List<NinePatchInterval> horizontalStretch )
    {
        this.horizontalStretch = horizontalStretch;
        clearCachedWidthData ();
    }

    /**
     * Adds horizontal stretch interval.
     *
     * @param interval horizontal stretch interval to add
     */
    public void addHorizontalStretch ( NinePatchInterval interval )
    {
        this.horizontalStretch.add ( interval );
        clearCachedWidthData ();
    }

    /**
     * Adds horizontal stretch interval.
     *
     * @param start interval start
     * @param end   interval end
     * @param pixel whether fixed interval or not
     */
    public void addHorizontalStretch ( int start, int end, boolean pixel )
    {
        addHorizontalStretch ( new NinePatchInterval ( start, end, pixel ) );
    }

    /**
     * Returns list of vertical stretch intervals taken from image patches.
     *
     * @return list of vertical stretch intervals taken from image patches
     */
    public List<NinePatchInterval> getVerticalStretch ()
    {
        return verticalStretch;
    }

    /**
     * Sets list of vertical stretch intervals.
     *
     * @param verticalStretch list of vertical stretch intervals
     */
    public void setVerticalStretch ( List<NinePatchInterval> verticalStretch )
    {
        this.verticalStretch = verticalStretch;
        clearCachedHeightData ();
    }

    /**
     * Adds vertical stretch interval.
     *
     * @param interval vertical stretch interval to add
     */
    public void addVerticalStretch ( NinePatchInterval interval )
    {
        this.verticalStretch.add ( interval );
        clearCachedHeightData ();
    }

    /**
     * Adds vertical stretch interval.
     *
     * @param start interval start
     * @param end   interval end
     * @param pixel whether fixed interval or not
     */
    public void addVerticalStretch ( int start, int end, boolean pixel )
    {
        addVerticalStretch ( new NinePatchInterval ( start, end, pixel ) );
    }

    /**
     * Returns content margin taken from image patches.
     *
     * @return content margin taken from image patches
     */
    public Insets getMargin ()
    {
        return margin;
    }

    /**
     * Sets content margin.
     *
     * @param margin content margin
     */
    public void setMargin ( Insets margin )
    {
        this.margin = margin;
    }

    /**
     * Sets content margin.
     *
     * @param top    top margin
     * @param left   left margin
     * @param bottom bottom margin
     * @param right  right margin
     */
    public void setMargin ( int top, int left, int bottom, int right )
    {
        setMargin ( new Insets ( top, left, bottom, right ) );
    }

    /**
     * Sets content margin.
     *
     * @param spacing sides margin
     */
    public void setMargin ( int spacing )
    {
        setMargin ( spacing, spacing, spacing, spacing );
    }

    /**
     * Paints icon for the specified component.
     *
     * @param c component to process
     * @param g graphics context
     */
    public void paintIcon ( Component c, Graphics g )
    {
        paintIcon ( ( Graphics2D ) g, 0, 0, c.getWidth (), c.getHeight () );
    }

    /**
     * Paints icon for the specified component at the specified location.
     *
     * @param c component to process
     * @param g graphics context
     * @param x location X coordinate
     * @param y location Y coordinate
     */
    public void paintIcon ( Component c, Graphics g, int x, int y )
    {
        // todo Modify this behavior so that icon is properly painted in Swing components
        paintIcon ( ( Graphics2D ) g, 0, 0, c.getWidth (), c.getHeight () );
    }

    /**
     * Paints icon at the specified bounds.
     *
     * @param g2d    graphics context
     * @param bounds icon bounds
     */
    public void paintIcon ( Graphics2D g2d, Rectangle bounds )
    {
        paintIcon ( g2d, bounds.x, bounds.y, bounds.width, bounds.height );
    }

    /**
     * Paints icon at the specified bounds.
     *
     * @param g2d    graphics context
     * @param x      location X coordinate
     * @param y      location Y coordinate
     * @param width  icon width
     * @param height icon height
     */
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
                float percents = ( float ) intervalHeight / ( rawImage.getHeight () - fixedPixelsY );
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
                    float percents = ( float ) intervalWidth / ( rawImage.getWidth () - fixedPixelsX );
                    finalWidth = Math.round ( percents * unfixedX );
                }

                // Drawing image part
                g2d.drawImage ( rawImage, currentX, currentY, currentX + finalWidth, currentY + finalHeight, intervalX.getStart (),
                        intervalY.getStart (), intervalX.getStart () + intervalWidth, intervalY.getStart () + intervalHeight, null );

                // Icrementing current X
                currentX += finalWidth;
            }

            // Icrementing current Y
            currentY += finalHeight;
        }
    }

    /**
     * Returns cached fixed minimum width for this icon.
     *
     * @param addUnfixedSpaces whether to add 1px for each stretchable area or not
     * @return cached fixed minimum width for this icon
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

    /**
     * Returns fixed minimum width for this icon.
     *
     * @param addUnfixedSpaces whether to add 1px for each stretchable area or not
     * @return fixed minimum width for this icon
     */
    protected int calculateFixedPixelsWidth ( boolean addUnfixedSpaces )
    {
        int fixedPixelsX = rawImage.getWidth ();
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
     * Clears fixed pixels width caches.
     */
    protected void clearCachedWidthData ()
    {
        cachedWidth0 = null;
        cachedWidth1 = null;
    }

    /**
     * Returns cached fixed minimum height for this icon.
     *
     * @param addUnfixedSpaces whether to add 1px for each stretchable area or not
     * @return cached fixed minimum height for this icon
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

    /**
     * Returns fixed minimum height for this icon.
     *
     * @param addUnfixedSpaceswhether to add 1px for each stretchable area or not
     * @return fixed minimum height for this icon
     */
    protected int calculateFixedPixelsHeight ( boolean addUnfixedSpaces )
    {
        int fixedPixelsY = rawImage.getHeight ();
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
     * Clears fixed pixels height caches.
     */
    protected void clearCachedHeightData ()
    {
        cachedHeight0 = null;
        cachedHeight1 = null;
    }

    /**
     * Returns icon width.
     *
     * @return icon width
     */
    public int getIconWidth ()
    {
        return Math.max ( component != null ? component.getWidth () : 0, getFixedPixelsWidth ( true ) );
    }

    /**
     * Returns icon height.
     *
     * @return icon height
     */
    public int getIconHeight ()
    {
        return Math.max ( component != null ? component.getHeight () : 0, getFixedPixelsHeight ( true ) );
    }

    /**
     * Returns preferred icon size.
     *
     * @return preferred icon size
     */
    public Dimension getPreferredSize ()
    {
        return new Dimension ( getFixedPixelsWidth ( true ), getFixedPixelsHeight ( true ) );
    }

    /**
     * Returns raw image size.
     *
     * @return raw image size
     */
    public Dimension getRealImageSize ()
    {
        return new Dimension ( getRawImage ().getWidth (), getRawImage ().getHeight () );
    }
}