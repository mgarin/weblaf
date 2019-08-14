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
import com.alee.graphics.filters.ShadowFilter;
import com.alee.utils.collection.ImmutableList;
import com.alee.utils.xml.Resource;
import com.alee.utils.xml.ResourceLocation;
import com.mortennobel.imagescaling.ResampleOp;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.color.ColorSpace;
import java.awt.geom.Area;
import java.awt.image.*;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.*;

/**
 * @author Mikle Garin
 */
public final class ImageUtils
{
    /**
     * todo 1. Rework the way image instances and data are handled within WebLaF
     * todo 2. Provide an appropriate way to cache images (based on component/
     */

    /**
     * Image cache keys separator.
     */
    private static final String IMAGE_CACHE_KEYS_SEPARATOR = "|";

    /**
     * Viewable image formats.
     */
    public static final List<String> VIEWABLE_IMAGES = new ImmutableList<String> (
            "png", "apng", "gif", "agif", "jpg", "jpeg", "jpeg2000", "bmp"
    );

    /**
     * Private constructor to avoid instantiation.
     */
    private ImageUtils ()
    {
        throw new UtilityException ( "Utility classes are not meant to be instantiated" );
    }

    /**
     * Returns whether or not image pixel at the specified X and Y coordinates is fully transparent.
     *
     * @param image image
     * @param x     X coordinate
     * @param y     Y coordinate
     * @return true if image pixel at the specified x and y coordinates is fully transparent, false otherwise
     */
    public static boolean isTransparent ( final BufferedImage image, final int x, final int y )
    {
        return ( image.getRGB ( x, y ) >> 24 & 0xFF ) > 0;
    }

    /**
     * Creates a compatible image using given data
     */

    public static BufferedImage createCompatibleImage ( final int width, final int height )
    {
        return SystemUtils.getGraphicsConfiguration ().createCompatibleImage ( width, height );
    }

    public static BufferedImage createCompatibleImage ( final int width, final int height, final int transparency )
    {
        return SystemUtils.getGraphicsConfiguration ().createCompatibleImage ( width, height, transparency );
    }

    public static BufferedImage createCompatibleImage ( final BufferedImage image )
    {
        return createCompatibleImage ( image, image.getWidth (), image.getHeight () );
    }

    public static BufferedImage createCompatibleImage ( final BufferedImage image, final int transparency )
    {
        return createCompatibleImage ( image.getWidth (), image.getHeight (), transparency );
    }

    public static BufferedImage createCompatibleImage ( final BufferedImage image, final int width, final int height )
    {
        return createCompatibleImage ( width, height, image.getTransparency () );
    }

    /**
     * Creates a compatible image from the content specified by the resource
     */

    public static BufferedImage loadCompatibleImage ( final URL resource ) throws IOException
    {
        final BufferedImage image = ImageIO.read ( resource );
        return toCompatibleImage ( image );
    }

    /**
     * If the source image is already compatible, then the source image is returned. This version takes a BufferedImage, but it could be
     * extended to take an Image instead
     */

    public static BufferedImage toCompatibleImage ( final BufferedImage image )
    {
        // Image is already compatible
        if ( isCompatibleImage ( image ) )
        {
            return image;
        }

        // Create new compatible image
        final BufferedImage compatibleImage = SystemUtils.getGraphicsConfiguration ()
                .createCompatibleImage ( image.getWidth (), image.getHeight (), image.getTransparency () );
        final Graphics2D g2d = compatibleImage.createGraphics ();
        g2d.drawImage ( image, 0, 0, null );
        g2d.dispose ();

        return compatibleImage;
    }

    /**
     * Returns true if image is compatible
     */

    public static boolean isCompatibleImage ( final BufferedImage image )
    {
        return image.getColorModel ().equals ( SystemUtils.getGraphicsConfiguration ().getColorModel () );
    }

    /**
     * Cuts image by the specified shape
     */

    public static ImageIcon cutImage ( final Shape shape, final ImageIcon image )
    {
        return new ImageIcon ( cutImage ( shape, image.getImage () ) );
    }

    public static BufferedImage cutImage ( final Shape shape, final Image image )
    {
        final int w = image.getWidth ( null );
        final int h = image.getHeight ( null );

        final BufferedImage cutImage = createCompatibleImage ( w, h, Transparency.TRANSLUCENT );
        final Graphics2D g2d = cutImage.createGraphics ();
        GraphicsUtils.setupAntialias ( g2d );
        g2d.setPaint ( Color.WHITE );
        g2d.fill ( shape );
        g2d.setComposite ( AlphaComposite.getInstance ( AlphaComposite.SRC_IN ) );
        g2d.drawImage ( image, 0, 0, null );
        g2d.dispose ();

        return cutImage;
    }

    /**
     * Returns Images list instead of ImageIcons list
     */

    @NotNull
    public static List<Image> toImagesList ( final List<? extends ImageIcon> icons )
    {
        final List<Image> images = new ArrayList<Image> ( icons.size () );
        for ( final ImageIcon icon : icons )
        {
            images.add ( icon.getImage () );
        }
        return images;
    }

    /**
     * Combines few images into single one
     */

    public static ImageIcon combineIcons ( final List<ImageIcon> icons )
    {
        return combineIcons ( 0, icons );
    }

    public static ImageIcon combineIcons ( final int spacing, final List<ImageIcon> icons )
    {
        // No icons given
        if ( icons == null || icons.size () == 0 )
        {
            return null;
        }

        final Image[] images = new Image[ icons.size () ];
        int i = 0;
        for ( final ImageIcon icon : icons )
        {
            images[ i ] = icon != null ? icon.getImage () : null;
            i++;
        }
        return new ImageIcon ( combineIcons ( spacing, images ) );
    }

    public static ImageIcon combineIcons ( final ImageIcon... icons )
    {
        return combineIcons ( 0, icons );
    }

    public static ImageIcon combineIcons ( final int spacing, final ImageIcon... icons )
    {
        // No icons given
        if ( icons == null || icons.length == 0 )
        {
            return null;
        }

        final Image[] images = new Image[ icons.length ];
        int i = 0;
        for ( final ImageIcon icon : icons )
        {
            images[ i ] = icon != null ? icon.getImage () : null;
            i++;
        }
        return new ImageIcon ( combineIcons ( spacing, images ) );
    }

    public static BufferedImage combineIcons ( final Image... images )
    {
        return combineIcons ( 0, images );
    }

    public static BufferedImage combineIcons ( final int spacing, final Image... images )
    {
        // No images given
        if ( images == null || images.length == 0 )
        {
            return null;
        }

        // Finding the maximum image size first
        final Dimension maxSize = new Dimension ( 0, 0 );
        for ( final Image image : images )
        {
            if ( image != null )
            {
                maxSize.width = maxSize.width + image.getWidth ( null ) + spacing;
                maxSize.height = Math.max ( maxSize.height, image.getHeight ( null ) );
            }
        }
        maxSize.width -= spacing;

        // Return null image if sizes are invalid
        if ( maxSize.width <= 0 || maxSize.height <= 0 )
        {
            return null;
        }

        // Creating new merged image
        final BufferedImage bi = createCompatibleImage ( maxSize.width, maxSize.height, Transparency.TRANSLUCENT );
        final Graphics2D g2d = bi.createGraphics ();
        int x = 0;
        for ( final Image image : images )
        {
            if ( image != null )
            {
                g2d.drawImage ( image, x, 0, null );
                x += image.getWidth ( null ) + spacing;
            }
        }
        g2d.dispose ();

        return bi;
    }

    /**
     * Merges few images into single one
     */

    private static final Map<String, ImageIcon> mergedIconsCache = new HashMap<String, ImageIcon> ();

    public static void clearMergedIconsCache ()
    {
        mergedIconsCache.clear ();
    }

    public static ImageIcon mergeIcons ( final List<? extends Icon> icons )
    {
        return mergeIcons ( null, icons );
    }

    public static ImageIcon mergeIcons ( final String key, final List<? extends Icon> icons )
    {
        // Icon is cached already
        if ( key != null && mergedIconsCache.containsKey ( key ) )
        {
            return mergedIconsCache.get ( key );
        }

        // No icons given
        if ( icons == null || icons.size () == 0 )
        {
            return null;
        }

        // Single icon given
        if ( icons.size () == 1 )
        {
            final ImageIcon icon = getImageIcon ( icons.get ( 0 ) );
            if ( key != null )
            {
                mergedIconsCache.put ( key, icon );
            }
            return icon;
        }

        final Image[] images = new Image[ icons.size () ];
        int i = 0;
        for ( final Icon icon : icons )
        {
            images[ i ] = icon != null ? getBufferedImage ( icon ) : null;
            i++;
        }
        final ImageIcon icon = new ImageIcon ( mergeImages ( images ) );
        if ( key != null )
        {
            mergedIconsCache.put ( key, icon );
        }
        return icon;
    }

    public static ImageIcon mergeIcons ( final Icon... icons )
    {
        return mergeIcons ( null, icons );
    }

    public static ImageIcon mergeIcons ( final String key, final Icon... icons )
    {
        // Icon is cached already
        if ( key != null && mergedIconsCache.containsKey ( key ) )
        {
            return mergedIconsCache.get ( key );
        }

        // No icons given
        if ( icons == null || icons.length == 0 )
        {
            return null;
        }

        // Single icon given
        if ( icons.length == 1 )
        {
            final ImageIcon icon = getImageIcon ( icons[ 0 ] );
            if ( key != null )
            {
                mergedIconsCache.put ( key, icon );
            }
            return icon;
        }

        final Image[] images = new Image[ icons.length ];
        int i = 0;
        for ( final Icon icon : icons )
        {
            images[ i ] = icon != null ? getBufferedImage ( icon ) : null;
            i++;
        }
        final ImageIcon icon = new ImageIcon ( mergeImages ( images ) );
        if ( key != null )
        {
            mergedIconsCache.put ( key, icon );
        }
        return icon;
    }

    private static final Map<String, BufferedImage> mergedImagesCache = new HashMap<String, BufferedImage> ();

    public static void clearMergedImagesCache ()
    {
        mergedImagesCache.clear ();
    }

    public static BufferedImage mergeImages ( final Image... images )
    {
        return mergeImages ( null, images );
    }

    public static BufferedImage mergeImages ( final String key, final Image... images )
    {
        // Image is cached already
        if ( key != null && mergedImagesCache.containsKey ( key ) )
        {
            return mergedImagesCache.get ( key );
        }

        // No images given
        if ( images == null || images.length == 0 )
        {
            return null;
        }

        // Single image given
        if ( images.length == 1 )
        {
            return ImageUtils.getBufferedImage ( images[ 0 ] );
        }

        // Finding the maximum image size first
        final Dimension maxSize = new Dimension ( 0, 0 );
        for ( final Image image : images )
        {
            if ( image != null )
            {
                maxSize.width = Math.max ( maxSize.width, image.getWidth ( null ) );
                maxSize.height = Math.max ( maxSize.height, image.getHeight ( null ) );
            }
        }

        // Return null image if sizes are invalid
        if ( maxSize.width <= 0 || maxSize.height <= 0 )
        {
            return null;
        }

        // Creating new merged image
        final BufferedImage bi = createCompatibleImage ( maxSize.width, maxSize.height, Transparency.TRANSLUCENT );
        final Graphics2D g2d = bi.createGraphics ();
        for ( final Image image : images )
        {
            if ( image != null )
            {
                g2d.drawImage ( image, 0, 0, null );
            }
        }
        g2d.dispose ();

        if ( key != null )
        {
            mergedImagesCache.put ( key, bi );
        }
        return bi;
    }

    /**
     * Loads image from specified source file
     */

    public static BufferedImage loadImage ( final String src )
    {
        return loadImage ( new File ( src ) );
    }

    public static BufferedImage loadImage ( final File file )
    {
        try
        {
            return ImageIO.read ( file );
        }
        catch ( final Exception e )
        {
            return null;
        }
    }

    /**
     * Loads image from URL
     */

    public static ImageIcon loadImage ( final URL url )
    {
        try
        {
            //            URLConnection uc = url.openConnection ();
            //            ProxyManager.setProxySettings ( uc );
            //            InputStream inputStream = uc.getInputStream ();
            //            ImageIcon imageIcon = loadImage ( inputStream );
            //            inputStream.close ();
            //            return imageIcon;
            return new ImageIcon ( url );
        }
        catch ( final Exception e )
        {
            return null;
        }
    }

    /**
     * Loads image from specified resource near class
     */

    public static ImageIcon loadImage ( final Class nearClass, final String src )
    {
        try
        {
            return new ImageIcon ( nearClass.getResource ( src ) );
        }
        catch ( final Exception e )
        {
            return null;
        }
    }

    /**
     * Loads image from InputStream
     */

    public static ImageIcon loadImage ( final InputStream inputStream )
    {
        try
        {
            return new ImageIcon ( ImageIO.read ( inputStream ) );
        }
        catch ( final Exception e )
        {
            return null;
        }
    }

    /**
     * Adds background to icon
     */

    public static ImageIcon addBackground ( final ImageIcon imageIcon, final Color background )
    {
        return new ImageIcon ( addBackground ( getBufferedImage ( imageIcon ), background ) );
    }

    public static BufferedImage addBackground ( final BufferedImage image, final Color background )
    {
        final BufferedImage bi = createCompatibleImage ( image );
        final Graphics2D g2d = bi.createGraphics ();
        g2d.setPaint ( background );
        g2d.fillRect ( 0, 0, image.getWidth (), image.getHeight () );
        g2d.drawImage ( image, 0, 0, null );
        g2d.dispose ();
        return bi;
    }

    /**
     * Resizes image canvas
     */

    public static ImageIcon resizeCanvas ( final ImageIcon imageIcon, final int width, final int height )
    {
        return new ImageIcon ( resizeCanvas ( getBufferedImage ( imageIcon ), width, height ) );
    }

    public static BufferedImage resizeCanvas ( final BufferedImage image, final int width, final int height )
    {
        final BufferedImage bi = createCompatibleImage ( image, width, height );
        final Graphics2D g2d = bi.createGraphics ();
        g2d.drawImage ( image, width / 2 - image.getWidth () / 2, height / 2 - image.getHeight () / 2, null );
        g2d.dispose ();
        return bi;
    }

    /**
     * Rotate image
     */

    public static ImageIcon rotateImage90CW ( final ImageIcon imageIcon )
    {
        return new ImageIcon ( rotateImage90CW ( getBufferedImage ( imageIcon ) ) );
    }

    public static BufferedImage rotateImage90CW ( final BufferedImage image )
    {
        final BufferedImage bufferedImage = createCompatibleImage ( image.getHeight (), image.getWidth (), Transparency.TRANSLUCENT );
        final Graphics2D g2d = bufferedImage.createGraphics ();
        g2d.translate ( image.getHeight (), 0 );
        g2d.rotate ( Math.PI / 2 );
        g2d.drawImage ( image, 0, 0, null );
        g2d.dispose ();
        return bufferedImage;
    }

    public static ImageIcon rotateImage90CCW ( final ImageIcon imageIcon )
    {
        return new ImageIcon ( rotateImage90CCW ( getBufferedImage ( imageIcon ) ) );
    }

    public static BufferedImage rotateImage90CCW ( final BufferedImage image )
    {
        final BufferedImage bufferedImage = createCompatibleImage ( image.getHeight (), image.getWidth (), Transparency.TRANSLUCENT );
        final Graphics2D g2d = bufferedImage.createGraphics ();
        g2d.translate ( 0, image.getWidth () );
        g2d.rotate ( -Math.PI / 2 );
        g2d.drawImage ( image, 0, 0, null );
        g2d.dispose ();
        return bufferedImage;
    }

    public static ImageIcon rotateImage180 ( final ImageIcon imageIcon )
    {
        return new ImageIcon ( rotateImage180 ( getBufferedImage ( imageIcon ) ) );
    }

    public static BufferedImage rotateImage180 ( final BufferedImage image )
    {
        final BufferedImage bufferedImage = createCompatibleImage ( image.getWidth (), image.getHeight (), Transparency.TRANSLUCENT );
        final Graphics2D g2d = bufferedImage.createGraphics ();
        g2d.translate ( image.getWidth (), image.getHeight () );
        g2d.rotate ( Math.PI );
        g2d.drawImage ( image, 0, 0, null );
        g2d.dispose ();
        return bufferedImage;
    }

    /**
     * Creates empty icon
     */

    public static ImageIcon createEmptyIcon ( final int width, final int height )
    {
        return new ImageIcon ( createEmptyImage ( width, height ) );
    }

    public static BufferedImage createEmptyImage ( final int width, final int height )
    {
        return createCompatibleImage ( width, height, Transparency.TRANSLUCENT );
    }

    /**
     * Creates color icon
     */

    public static ImageIcon createColorIcon ( final Color color )
    {
        return createColorIcon ( color, 16, 16 );
    }

    public static ImageIcon createColorIcon ( final Color color, final int width, final int height )
    {
        return new ImageIcon ( createColorImage ( color, width, height ) );
    }

    public static BufferedImage createColorImage ( final Color color )
    {
        return createColorImage ( color, 16, 16 );
    }

    public static BufferedImage createColorImage ( final Color color, final int width, final int height )
    {
        final int largeRound = 6;
        final int bigRound = 4;
        final BufferedImage image = createCompatibleImage ( width, height, Transparency.TRANSLUCENT );
        final Graphics2D g2d = image.createGraphics ();
        GraphicsUtils.setupAntialias ( g2d );
        g2d.setPaint ( Color.GRAY );
        g2d.drawRoundRect ( 0, 0, width - 1, height - 1, largeRound, bigRound );
        g2d.setPaint ( Color.WHITE );
        g2d.drawRoundRect ( 1, 1, width - 3, height - 3, bigRound, bigRound );
        g2d.setPaint ( color );
        g2d.fillRoundRect ( 2, 2, width - 4, height - 4, bigRound, bigRound );
        g2d.dispose ();
        return image;
    }

    /**
     * Creates color chooser icon
     */

    public static ImageIcon createColorChooserIcon ( final Color color )
    {
        return new ImageIcon ( createColorChooserImage ( color ) );
    }

    public static BufferedImage createColorChooserImage ( final Color color )
    {
        final BufferedImage image = createCompatibleImage ( 16, 16, Transparency.TRANSLUCENT );
        final Graphics2D g2d = image.createGraphics ();

        if ( color == null || color.getAlpha () < 255 )
        {
            final ImageIcon transparentIcon = getImageIcon ( ImageUtils.class.getResource ( "icons/color/transparent.png" ) );
            g2d.drawImage ( transparentIcon.getImage (), 0, 0, null );
        }
        if ( color != null )
        {
            g2d.setPaint ( color );
            g2d.fillRect ( 2, 2, 13, 12 );
        }

        final ImageIcon colorIcon = getImageIcon ( ImageUtils.class.getResource ( "icons/color/color.png" ) );
        g2d.drawImage ( colorIcon.getImage (), 0, 0, null );

        g2d.dispose ();
        return image;
    }

    /**
     * Darkens specified BufferedImage
     */

    public static void darkenImage ( final BufferedImage image, final float darken )
    {
        final Graphics2D g2d = image.createGraphics ();
        g2d.setComposite ( AlphaComposite.getInstance ( AlphaComposite.SRC_OVER, darken ) );
        g2d.setPaint ( Color.BLACK );
        g2d.fillRect ( 0, 0, image.getWidth (), image.getHeight () );
        g2d.dispose ();
    }

    /**
     * Average image color
     */

    public static Color getImageAverageColor ( final ImageIcon icon )
    {
        int red = 0;
        int green = 0;
        int blue = 0;
        final BufferedImage bi = getBufferedImage ( icon.getImage () );
        for ( int i = 0; i < icon.getIconWidth (); i++ )
        {
            for ( int j = 0; j < icon.getIconHeight (); j++ )
            {
                final int rgb = bi.getRGB ( i, j );
                red += ( rgb >> 16 ) & 0xFF;
                green += ( rgb >> 8 ) & 0xFF;
                blue += rgb & 0xFF;
            }
        }
        final int count = icon.getIconWidth () * icon.getIconHeight ();
        return new Color ( red / count, green / count, blue / count );
    }

    /**
     * Is this image format can be loaded
     */

    public static boolean isImageLoadable ( final String name )
    {
        return VIEWABLE_IMAGES.contains ( FileUtils.getFileExtPart ( name, false ).toLowerCase ( Locale.ROOT ) );
    }

    /**
     * Image preview generation
     */

    public static ImageIcon createThumbnailIcon ( final String src )
    {
        return createThumbnailIcon ( src, 50 );
    }

    public static ImageIcon createThumbnailIcon ( final String src, final int size )
    {
        // Retrieving image to create thumbnail from
        final ImageIcon icon = getImageIcon ( src, false );
        if ( icon != null )
        {
            // Creating and caching thumbnail
            final ImageIcon imageIcon = createPreviewIcon ( icon.getImage (), size );

            // Saving image size
            if ( imageIcon != null )
            {
                imageIcon.setDescription ( icon.getIconWidth () + "x" + icon.getIconHeight () );
            }

            return imageIcon;
        }
        else
        {
            return null;
        }
    }

    public static ImageIcon createPreviewIcon ( final ImageIcon image, final int size )
    {
        return createPreviewIcon ( image.getImage (), size );
    }

    public static ImageIcon createPreviewIcon ( final Image image, final int size )
    {
        return createPreviewIcon ( getBufferedImage ( image ), size );
    }

    public static ImageIcon createPreviewIcon ( final BufferedImage image, final int size )
    {
        final BufferedImage previewImage = createPreviewImage ( image, size );
        if ( previewImage != null )
        {
            return new ImageIcon ( previewImage );
        }
        else
        {
            return new ImageIcon ();
        }
    }

    public static BufferedImage createPreviewImage ( final BufferedImage image, final Dimension fitTo )
    {
        return createPreviewImage ( image, fitTo.width, fitTo.height );
    }

    public static BufferedImage createPreviewImage ( final BufferedImage image, final int width, final int height )
    {
        if ( image.getWidth () > width || image.getHeight () > height )
        {
            // Calculating maximum preview length
            if ( height * ( ( float ) image.getWidth () / image.getHeight () ) <= width )
            {
                return createPreviewImage ( image,
                        Math.max ( height, Math.round ( height * ( ( float ) image.getWidth () / image.getHeight () ) ) ) );
            }
            else
            {
                return createPreviewImage ( image,
                        Math.max ( width, Math.round ( width * ( ( float ) image.getHeight () / image.getWidth () ) ) ) );
            }
        }
        else
        {
            // Image is smaller than allowed size
            return image;
        }
    }

    public static BufferedImage createPreviewImage ( final BufferedImage image, final int length )
    {
        if ( image == null )
        {
            return null;
        }

        final int width;
        final int height;
        if ( image.getWidth () <= length && image.getHeight () <= length )
        {
            return image;
        }
        else if ( image.getWidth () > image.getHeight () )
        {
            width = length;
            height = Math.round ( ( float ) length * image.getHeight () / image.getWidth () );
        }
        else if ( image.getWidth () < image.getHeight () )
        {
            height = length;
            width = Math.round ( ( float ) length * image.getWidth () / image.getHeight () );
        }
        else
        {
            width = height = length;
        }

        // Creating scaled image (can only scale down)
        // http://code.google.com/p/java-image-scaling/
        if ( width >= 3 && height >= 3 )
        {
            return new ResampleOp ( width, height ).filter ( image, createCompatibleImage ( image ) );
        }
        else
        {
            final int w = Math.max ( 1, width );
            final int h = Math.max ( 1, height );

            final BufferedImage rescaledImage = createCompatibleImage ( w, h, Transparency.TRANSLUCENT );
            final Graphics2D g2d = rescaledImage.createGraphics ();
            GraphicsUtils.setupImageQuality ( g2d );
            g2d.drawImage ( image, 0, 0, width, height, null );
            g2d.dispose ();

            return rescaledImage;
        }
    }

    /**
     * Image read methods
     */

    private static final Map<String, ImageIcon> iconsCache = new HashMap<String, ImageIcon> ();

    public static boolean isImageCached ( final String src )
    {
        return iconsCache.containsKey ( src ) && iconsCache.get ( src ) != null;
    }

    public static void setImageCache ( final String src, final ImageIcon imageIcon )
    {
        iconsCache.put ( src, imageIcon );
    }

    public static void clearImagesCache ()
    {
        iconsCache.clear ();
    }

    public static void clearImageCache ( final String src )
    {
        if ( iconsCache.size () > 0 && iconsCache.containsKey ( src ) )
        {
            if ( iconsCache.get ( src ) != null && iconsCache.get ( src ).getImage () != null )
            {
                iconsCache.get ( src ).getImage ().flush ();
            }
            iconsCache.remove ( src );
        }
    }

    public static ImageIcon getImageIcon ( final File file )
    {
        return getImageIcon ( file, true );
    }

    public static ImageIcon getImageIcon ( final File file, final boolean useCache )
    {
        return getImageIcon ( file.getAbsolutePath (), useCache );
    }

    public static ImageIcon getImageIcon ( final String src )
    {
        return getImageIcon ( src, true );
    }

    public static ImageIcon getImageIcon ( final String src, final boolean useCache )
    {
        if ( src != null && !src.trim ().equals ( "" ) )
        {
            final ImageIcon imageIcon;
            if ( useCache && iconsCache.containsKey ( src ) )
            {
                imageIcon = iconsCache.get ( src );
                if ( imageIcon != null )
                {
                    return imageIcon;
                }
                else
                {
                    // todo This might cause performance issues
                    iconsCache.remove ( src );
                    return getImageIcon ( src, useCache );
                }
            }
            else
            {
                imageIcon = createImageIcon ( src );
                if ( useCache )
                {
                    iconsCache.put ( src, imageIcon );
                }
                return imageIcon;
            }
        }
        else
        {
            return null;
        }
    }

    private static ImageIcon createImageIcon ( final String src )
    {
        if ( !new File ( src ).exists () )
        {
            return new ImageIcon ();
        }
        else
        {
            try
            {
                return new ImageIcon ( ImageIO.read ( new File ( src ) ) );
            }
            catch ( final Exception e )
            {
                return new ImageIcon ();
            }
        }
    }

    public static ImageIcon getImageIcon ( final URL resource )
    {
        return getImageIcon ( resource, true );
    }

    public static ImageIcon getImageIcon ( final URL resource, final boolean useCache )
    {
        if ( resource != null )
        {
            final String key = resource.toString ();
            final ImageIcon imageIcon;
            if ( useCache && iconsCache.containsKey ( key ) )
            {
                imageIcon = iconsCache.get ( key );
                if ( imageIcon != null )
                {
                    return imageIcon;
                }
                else
                {
                    // todo This might cause performance issues
                    iconsCache.remove ( key );
                    return getImageIcon ( key, useCache );
                }
            }
            else
            {
                imageIcon = new ImageIcon ( resource );
                if ( useCache )
                {
                    iconsCache.put ( key, imageIcon );
                }
                return imageIcon;
            }
        }
        else
        {
            return null;
        }
    }

    /**
     * Returns {@link javax.swing.ImageIcon} read from specified {@link com.alee.utils.xml.Resource}.
     *
     * @param location image file location
     * @return {@link javax.swing.ImageIcon} read from specified {@link com.alee.utils.xml.Resource}
     */
    public static ImageIcon getImageIcon ( final Resource location )
    {
        if ( location.getLocation ().equals ( ResourceLocation.url ) )
        {
            try
            {
                return new ImageIcon ( new URL ( location.getPath () ) );
            }
            catch ( final MalformedURLException e )
            {
                final String msg = "Unable to load image from URL: %s";
                LoggerFactory.getLogger ( ImageUtils.class ).error ( String.format ( msg, location.getPath () ), e );
                return null;
            }
        }
        if ( location.getLocation ().equals ( ResourceLocation.filePath ) )
        {
            try
            {
                return new ImageIcon ( new File ( location.getPath () ).getCanonicalPath () );
            }
            catch ( final IOException e )
            {
                final String msg = "Unable to load image from file: %s";
                LoggerFactory.getLogger ( ImageUtils.class ).error ( String.format ( msg, location.getPath () ), e );
                return null;
            }
        }
        else if ( location.getLocation ().equals ( ResourceLocation.nearClass ) )
        {
            try
            {
                return new ImageIcon ( Class.forName ( location.getClassName () ).getResource ( location.getPath () ) );
            }
            catch ( final ClassNotFoundException e )
            {
                final String msg = "Unable to load image from file '%s' near class: %s";
                final String fmsg = TextUtils.format ( msg, location.getPath (), location.getClassName () );
                LoggerFactory.getLogger ( ImageUtils.class ).error ( fmsg, e );
                return null;
            }
        }
        else
        {
            return null;
        }
    }

    /**
     * Makes a copy of BufferedImage
     */

    public static BufferedImage copy ( final Image image )
    {
        return copy ( getBufferedImage ( image ) );
    }

    public static BufferedImage copy ( final BufferedImage bufferedImage )
    {
        final BufferedImage newImage = createCompatibleImage ( bufferedImage );
        final Graphics2D g2d = newImage.createGraphics ();
        g2d.drawImage ( bufferedImage, 0, 0, null );
        g2d.dispose ();
        return newImage;
    }

    /**
     * Returns {@link BufferedImage} converted from the specified {@link RenderedImage}.
     *
     * @param image {@link RenderedImage} to convert
     * @return {@link BufferedImage} converted from the specified {@link RenderedImage}
     */
    public static BufferedImage getBufferedImage ( final RenderedImage image )
    {
        if ( image instanceof BufferedImage )
        {
            return ( BufferedImage ) image;
        }

        final ColorModel cm = image.getColorModel ();
        final int width = image.getWidth ();
        final int height = image.getHeight ();
        final WritableRaster raster = cm.createCompatibleWritableRaster ( width, height );
        final boolean isAlphaPremultiplied = cm.isAlphaPremultiplied ();
        final Hashtable properties = new Hashtable ();
        final String[] keys = image.getPropertyNames ();
        if ( keys != null )
        {
            for ( final String key : keys )
            {
                properties.put ( key, image.getProperty ( key ) );
            }
        }

        final BufferedImage result = new BufferedImage ( cm, raster, isAlphaPremultiplied, properties );
        image.copyData ( raster );

        return result;
    }

    /**
     * Retrieves BufferedImage from Image
     */

    public static BufferedImage getBufferedImage ( final URL url )
    {
        return getBufferedImage ( new ImageIcon ( url ) );
    }

    public static BufferedImage getBufferedImage ( final String iconSrc )
    {
        return getBufferedImage ( new ImageIcon ( iconSrc ) );
    }

    public static BufferedImage getBufferedImage ( final ImageIcon imageIcon )
    {
        return getBufferedImage ( imageIcon.getImage () );
    }

    public static BufferedImage getBufferedImage ( final Image image )
    {
        if ( image == null || image.getWidth ( null ) <= 0 || image.getHeight ( null ) <= 0 )
        {
            return null;
        }
        else if ( image instanceof BufferedImage )
        {
            return ( BufferedImage ) image;
        }
        //        else if ( image instanceof ToolkitImage && ( ( ToolkitImage ) image ).getBufferedImage () != null )
        //        {
        //            return ( ( ToolkitImage ) image ).getBufferedImage ();
        //        }
        else
        {
            final BufferedImage bi = createCompatibleImage ( image.getWidth ( null ), image.getHeight ( null ), Transparency.TRANSLUCENT );
            final Graphics2D g2d = bi.createGraphics ();
            g2d.drawImage ( image, 0, 0, null );
            g2d.dispose ();
            return bi;
        }
    }

    /**
     * Retrieves BufferedImage from Icon
     */

    public static BufferedImage getBufferedImage ( final Icon icon )
    {
        if ( icon == null )
        {
            return null;
        }
        else if ( icon instanceof ImageIcon )
        {
            final Image image = ( ( ImageIcon ) icon ).getImage ();
            if ( image != null )
            {
                return getBufferedImage ( image );
            }
            else
            {
                return createBufferedImage ( icon );
            }
        }
        else
        {
            return createBufferedImage ( icon );
        }
    }

    public static BufferedImage createBufferedImage ( final Icon icon )
    {
        final BufferedImage bi = createCompatibleImage ( icon.getIconWidth (), icon.getIconHeight (), Transparency.TRANSLUCENT );
        final Graphics2D g2d = bi.createGraphics ();
        icon.paintIcon ( null, g2d, 0, 0 );
        g2d.dispose ();
        return bi;
    }

    public static ImageIcon getImageIcon ( final Icon icon )
    {
        if ( icon instanceof ImageIcon )
        {
            return ( ImageIcon ) icon;
        }
        else
        {
            return new ImageIcon ( getBufferedImage ( icon ) );
        }
    }

    /**
     * Scaled preview creation
     */

    private static final Map<String, ImageIcon> sizedPreviewCache = new HashMap<String, ImageIcon> ();

    public static ImageIcon getSizedImagePreview ( final String src, final int length, final boolean drawBorder )
    {
        if ( sizedPreviewCache.containsKey ( length + IMAGE_CACHE_KEYS_SEPARATOR + src ) )
        {
            return sizedPreviewCache.get ( length + IMAGE_CACHE_KEYS_SEPARATOR + src );
        }
        else
        {
            final ImageIcon icon = createThumbnailIcon ( src, length );
            final ImageIcon sized = createSizedImagePreview ( icon, length, drawBorder );
            sizedPreviewCache.put ( length + IMAGE_CACHE_KEYS_SEPARATOR + src, sized );
            return sized;
        }
    }

    public static ImageIcon getSizedImagePreview ( final String id, final ImageIcon icon, final int length, final boolean drawBorder )
    {
        if ( sizedPreviewCache.containsKey ( id ) )
        {
            return sizedPreviewCache.get ( id );
        }
        else
        {
            final ImageIcon sized = createSizedImagePreview ( icon, length, drawBorder );
            sizedPreviewCache.put ( id, sized );
            return sized;
        }
    }

    public static ImageIcon createSizedImagePreview ( final ImageIcon icon, int length, final boolean drawBorder )
    {
        // Addition border spacing
        //        if ( drawBorder )
        //        {
        length += 4;
        //        }

        // Creating standard size image
        final BufferedImage bi = createCompatibleImage ( length, length, Transparency.TRANSLUCENT );
        final Graphics2D g2d = bi.createGraphics ();
        if ( icon != null )
        {
            g2d.drawImage ( icon.getImage (), length / 2 - icon.getIconWidth () / 2, length / 2 - icon.getIconHeight () / 2, null );
        }
        if ( drawBorder )
        {
            g2d.setPaint ( Color.LIGHT_GRAY );
            g2d.drawRect ( 0, 0, length - 1, length - 1 );
        }
        g2d.dispose ();

        // Creating ImageIcon
        final ImageIcon imageIcon = new ImageIcon ( bi );
        imageIcon.setDescription ( icon != null ? icon.getDescription () : null );
        return imageIcon;
    }

    /**
     * Creates disabled image copy
     */

    private static final Map<String, ImageIcon> grayscaleCache = new HashMap<String, ImageIcon> ();

    public static void clearDisabledCopyCache ()
    {
        grayscaleCache.clear ();
    }

    public static void clearDisabledCopyCache ( final String id )
    {
        grayscaleCache.remove ( id );
    }

    public static ImageIcon getDisabledCopy ( final String key, final Icon icon )
    {
        return getDisabledCopy ( key, getImageIcon ( icon ) );
    }

    public static ImageIcon getDisabledCopy ( final String key, final ImageIcon imageIcon )
    {
        if ( grayscaleCache.containsKey ( key ) )
        {
            return grayscaleCache.get ( key );
        }
        else
        {
            grayscaleCache.put ( key, createDisabledCopy ( imageIcon ) );
            return grayscaleCache.get ( key );
        }
    }

    public static ImageIcon createDisabledCopy ( final ImageIcon imageIcon )
    {
        return new ImageIcon ( createDisabledCopy ( imageIcon.getImage () ) );
    }

    public static BufferedImage createDisabledCopy ( final Image img )
    {
        final BufferedImage bi = createGrayscaleCopy ( img );

        final BufferedImage bi2 = createCompatibleImage ( bi );
        final Graphics2D g2d = bi2.createGraphics ();
        GraphicsUtils.setupAlphaComposite ( g2d, 0.7f );
        g2d.drawImage ( bi, 0, 0, null );
        g2d.dispose ();

        return bi2;
    }

    /**
     * Creates grayscale image copy
     */

    private static final ColorConvertOp grayscaleColorConvert = new ColorConvertOp ( ColorSpace.getInstance ( ColorSpace.CS_GRAY ), null );

    public static ImageIcon createGrayscaleCopy ( final ImageIcon imageIcon )
    {
        return new ImageIcon ( createGrayscaleCopy ( imageIcon.getImage () ) );
    }

    public static BufferedImage createGrayscaleCopy ( final Image img )
    {
        return createGrayscaleCopy ( getBufferedImage ( img ) );
    }

    public static BufferedImage createGrayscaleCopy ( final BufferedImage img )
    {
        return grayscaleColorConvert.filter ( img, null );
    }

    /**
     * Creating partially transparent ImageIcon
     */

    private static final Map<String, ImageIcon> transparentCache = new HashMap<String, ImageIcon> ();

    public static void clearTransparentCache ()
    {
        transparentCache.clear ();
    }

    public static void clearTransparentCache ( final String id )
    {
        transparentCache.remove ( id );
    }

    public static ImageIcon getTransparentCopy ( final String id, final ImageIcon imageIcon, final float opacity )
    {
        if ( transparentCache.containsKey ( id ) )
        {
            return transparentCache.get ( id );
        }
        else
        {
            transparentCache.put ( id, createTransparentCopy ( imageIcon, opacity ) );
            return transparentCache.get ( id );
        }
    }

    public static ImageIcon createTransparentCopy ( final ImageIcon imageIcon, final float opacity )
    {
        final BufferedImage bi = createCompatibleImage ( imageIcon.getIconWidth (), imageIcon.getIconHeight (), Transparency.TRANSLUCENT );

        final Graphics2D g2d = bi.createGraphics ();
        GraphicsUtils.setupAlphaComposite ( g2d, opacity );
        g2d.drawImage ( imageIcon.getImage (), 0, 0, null );
        g2d.dispose ();

        return new ImageIcon ( bi );
    }

    /**
     * Returns shadow image based on provided shape.
     *
     * @param width       shadow image width
     * @param height      shadow image height
     * @param shape       shadow shape
     * @param shadowWidth shadow width
     * @param opacity     shadow opacity
     * @param clip        whether or not should clip shadow form
     * @return shadow image based on provided shape
     */
    public static BufferedImage createShadowImage ( final int width, final int height, final Shape shape, final int shadowWidth,
                                                    final float opacity, final boolean clip )
    {
        // Creating template image
        final BufferedImage bi = createCompatibleImage ( width, height, Transparency.TRANSLUCENT );
        final Graphics2D ig = bi.createGraphics ();
        GraphicsUtils.setupAntialias ( ig );
        ig.setPaint ( Color.BLACK );
        ig.fill ( shape );
        ig.dispose ();

        // Creating shadow image
        final ShadowFilter sf = new ShadowFilter ( shadowWidth, 0, 0, opacity );
        final BufferedImage shadow = sf.filter ( bi, null );

        // Clipping shadow image
        if ( clip )
        {
            final Graphics2D g2d = shadow.createGraphics ();
            GraphicsUtils.setupAntialias ( g2d );
            g2d.setComposite ( AlphaComposite.getInstance ( AlphaComposite.SRC_IN ) );
            g2d.setPaint ( ColorUtils.transparent () );
            g2d.fill ( shape );
            g2d.dispose ();
        }

        return shadow;
    }

    /**
     * Returns shadow image based on provided shape.
     *
     * @param width       shadow image width
     * @param shape       shadow shape
     * @param shadowWidth shadow width
     * @param opacity     shadow opacity
     * @return shadow image based on provided shape
     */
    public static BufferedImage createInnerShadowImage ( final int width, final Shape shape, final int shadowWidth, final float opacity )
    {
        // Creating template image
        final BufferedImage bi = ImageUtils.createCompatibleImage ( width, width, Transparency.TRANSLUCENT );
        final Graphics2D ig = bi.createGraphics ();
        GraphicsUtils.setupAntialias ( ig );
        final Area area = new Area ( new Rectangle ( 0, 0, width, width ) );
        area.exclusiveOr ( new Area ( shape ) );
        ig.setPaint ( Color.BLACK );
        ig.fill ( area );
        ig.dispose ();

        // Creating shadow image
        final ShadowFilter sf = new ShadowFilter ( shadowWidth, 0, 0, opacity );
        final BufferedImage shadow = sf.filter ( bi, null );

        // Clipping shadow image
        final Graphics2D g2d = shadow.createGraphics ();
        GraphicsUtils.setupAntialias ( g2d );
        g2d.setComposite ( AlphaComposite.getInstance ( AlphaComposite.SRC_IN ) );
        g2d.setPaint ( ColorUtils.transparent () );
        g2d.fill ( area );
        g2d.dispose ();

        return shadow.getSubimage ( shadowWidth, shadowWidth, width - shadowWidth * 2, width - shadowWidth * 2 );
    }

    /**
     * Returns {@link java.awt.image.BufferedImage} decoded from Base64 string.
     *
     * @param imageString image encoded in Base64 string to decode
     * @return {@link java.awt.image.BufferedImage} decoded from Base64 string
     */
    public static BufferedImage decodeImage ( final String imageString )
    {
        BufferedImage image = null;
        if ( imageString == null || imageString.equals ( "" ) )
        {
            return image;
        }
        final byte[] bytes = EncryptionUtils.base64decode ( imageString ).getBytes ();
        final ByteArrayInputStream bis = new ByteArrayInputStream ( bytes );
        try
        {
            image = ImageIO.read ( bis );
            bis.close ();
        }
        catch ( final Exception ex )
        {
            final String msg = "Unable to decode image icon";
            LoggerFactory.getLogger ( ImageUtils.class ).error ( msg, ex );
            try
            {
                bis.close ();
            }
            catch ( final IOException ignored )
            {
                //
            }
        }
        return image;
    }

    /**
     * Returns image encoded in Base64 string.
     *
     * @param image image to encode into Base64 string
     * @return image encoded in Base64 string
     */
    public static String encodeImage ( final BufferedImage image )
    {
        String imageString = null;
        if ( image == null )
        {
            return imageString;
        }
        final ByteArrayOutputStream bos = new ByteArrayOutputStream ();
        try
        {
            ImageIO.write ( image, "png", bos );
            imageString = EncryptionUtils.base64encode ( new String ( bos.toByteArray () ) );
            bos.close ();
        }
        catch ( final IOException ex )
        {
            final String msg = "Unable to encode image icon";
            LoggerFactory.getLogger ( ImageUtils.class ).error ( msg, ex );
            try
            {
                bos.close ();
            }
            catch ( final IOException ignored )
            {
                //
            }
        }
        return imageString;
    }
}