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

import com.alee.graphics.filters.ImageFilterUtils;
import com.alee.graphics.filters.ShadowFilter;
import com.alee.laf.GlobalConstants;
import com.alee.laf.StyleConstants;
import com.mortennobel.imagescaling.ResampleOp;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.GeneralPath;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: mgarin Date: 05.07.11 Time: 13:22
 */

public class ImageUtils
{
    /**
     * Default cached image data parts separator
     */

    public static final String IMAGE_CACHE_SEPARATOR = StyleConstants.SEPARATOR;

    /**
     * Checks if the specified image pixel is fully transparent
     */

    public static boolean isImageContains ( BufferedImage image, int x, int y )
    {
        return ( image.getRGB ( x, y ) >> 24 & 0xFF ) > 0;
    }

    /**
     * Creates a compatible image using given data
     */

    public static BufferedImage createCompatibleImage ( int width, int height )
    {
        return SystemUtils.getGraphicsConfiguration ().createCompatibleImage ( width, height );
    }

    public static BufferedImage createCompatibleImage ( int width, int height, int transparency )
    {
        return SystemUtils.getGraphicsConfiguration ().createCompatibleImage ( width, height, transparency );
    }

    public static BufferedImage createCompatibleImage ( BufferedImage image )
    {
        return createCompatibleImage ( image, image.getWidth (), image.getHeight () );
    }

    public static BufferedImage createCompatibleImage ( BufferedImage image, int transparency )
    {
        return createCompatibleImage ( image.getWidth (), image.getHeight (), transparency );
    }

    public static BufferedImage createCompatibleImage ( BufferedImage image, int width, int height )
    {
        return createCompatibleImage ( width, height, image.getTransparency () );
    }

    /**
     * Creates a compatible image from the content specified by the resource
     */

    public static BufferedImage loadCompatibleImage ( URL resource ) throws IOException
    {
        BufferedImage image = ImageIO.read ( resource );
        return toCompatibleImage ( image );
    }

    /**
     * If the source image is already compatible, then the source image is returned. This version takes a BufferedImage, but it could be
     * extended to take an Image instead
     */

    public static BufferedImage toCompatibleImage ( BufferedImage image )
    {
        // Image is already compatible
        if ( isCompatibleImage ( image ) )
        {
            return image;
        }

        // Create new compatible image
        BufferedImage compatibleImage = SystemUtils.getGraphicsConfiguration ()
                .createCompatibleImage ( image.getWidth (), image.getHeight (), image.getTransparency () );
        Graphics2D g2d = compatibleImage.createGraphics ();
        g2d.drawImage ( image, 0, 0, null );
        g2d.dispose ();

        return compatibleImage;
    }

    /**
     * Returns true if image is compatible
     */

    public static boolean isCompatibleImage ( BufferedImage image )
    {
        return image.getColorModel ().equals ( SystemUtils.getGraphicsConfiguration ().getColorModel () );
    }

    /**
     * Cuts image by the specified shape
     */

    public static ImageIcon cutImage ( Shape shape, ImageIcon image )
    {
        return new ImageIcon ( cutImage ( shape, image.getImage () ) );
    }

    public static BufferedImage cutImage ( Shape shape, Image image )
    {
        int w = image.getWidth ( null );
        int h = image.getHeight ( null );

        BufferedImage cutImage = createCompatibleImage ( w, h, Transparency.TRANSLUCENT );
        Graphics2D g2d = cutImage.createGraphics ();
        LafUtils.setupAntialias ( g2d );
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

    public static List<Image> toImagesList ( List<ImageIcon> icons )
    {
        List<Image> images = new ArrayList<Image> ();
        for ( ImageIcon icon : icons )
        {
            images.add ( icon.getImage () );
        }
        return images;
    }

    /**
     * Combines few images into single one
     */

    public static ImageIcon combineIcons ( List<ImageIcon> icons )
    {
        return combineIcons ( 0, icons );
    }

    public static ImageIcon combineIcons ( int spacing, List<ImageIcon> icons )
    {
        // No icons given
        if ( icons == null || icons.size () == 0 )
        {
            return null;
        }

        Image[] images = new Image[ icons.size () ];
        int i = 0;
        for ( ImageIcon icon : icons )
        {
            images[ i ] = icon != null ? icon.getImage () : null;
            i++;
        }
        return new ImageIcon ( combineIcons ( images ) );
    }

    public static ImageIcon combineIcons ( ImageIcon... icons )
    {
        return combineIcons ( 0, icons );
    }

    public static ImageIcon combineIcons ( int spacing, ImageIcon... icons )
    {
        // No icons given
        if ( icons == null || icons.length == 0 )
        {
            return null;
        }

        Image[] images = new Image[ icons.length ];
        int i = 0;
        for ( ImageIcon icon : icons )
        {
            images[ i ] = icon != null ? icon.getImage () : null;
            i++;
        }
        return new ImageIcon ( combineIcons ( images ) );
    }

    public static BufferedImage combineIcons ( Image... images )
    {
        return combineIcons ( 0, images );
    }

    public static BufferedImage combineIcons ( int spacing, Image... images )
    {
        // No images given
        if ( images == null || images.length == 0 )
        {
            return null;
        }

        // Finding the maximum image size first
        Dimension maxSize = new Dimension ( 0, 0 );
        for ( Image image : images )
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
        BufferedImage bi = createCompatibleImage ( maxSize.width, maxSize.height, Transparency.TRANSLUCENT );
        Graphics2D g2d = bi.createGraphics ();
        int x = 0;
        for ( Image image : images )
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

    private static Map<String, ImageIcon> mergedIconsCache = new HashMap<String, ImageIcon> ();

    public static void clearMergedIconsCache ()
    {
        mergedIconsCache.clear ();
    }

    public static ImageIcon mergeIcons ( List<ImageIcon> icons )
    {
        return mergeIcons ( null, icons );
    }

    public static ImageIcon mergeIcons ( String key, List<ImageIcon> icons )
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
            return icons.get ( 0 );
        }

        Image[] images = new Image[ icons.size () ];
        int i = 0;
        for ( ImageIcon icon : icons )
        {
            images[ i ] = icon != null ? icon.getImage () : null;
            i++;
        }
        ImageIcon icon = new ImageIcon ( mergeImages ( images ) );
        if ( key != null )
        {
            mergedIconsCache.put ( key, icon );
        }
        return icon;
    }

    public static ImageIcon mergeIcons ( ImageIcon... icons )
    {
        return mergeIcons ( null, icons );
    }

    public static ImageIcon mergeIcons ( String key, ImageIcon... icons )
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
            return icons[ 0 ];
        }

        Image[] images = new Image[ icons.length ];
        int i = 0;
        for ( ImageIcon icon : icons )
        {
            images[ i ] = icon != null ? icon.getImage () : null;
            i++;
        }
        ImageIcon icon = new ImageIcon ( mergeImages ( images ) );
        if ( key != null )
        {
            mergedIconsCache.put ( key, icon );
        }
        return icon;
    }

    private static Map<String, BufferedImage> mergedImagesCache = new HashMap<String, BufferedImage> ();

    public static void clearMergedImagesCache ()
    {
        mergedImagesCache.clear ();
    }

    public static BufferedImage mergeImages ( Image... images )
    {
        return mergeImages ( null, images );
    }

    public static BufferedImage mergeImages ( String key, Image... images )
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
        Dimension maxSize = new Dimension ( 0, 0 );
        for ( Image image : images )
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
        BufferedImage bi = createCompatibleImage ( maxSize.width, maxSize.height, Transparency.TRANSLUCENT );
        Graphics2D g2d = bi.createGraphics ();
        for ( Image image : images )
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

    public static BufferedImage loadImage ( String src )
    {
        return loadImage ( new File ( src ) );
    }

    public static BufferedImage loadImage ( File file )
    {
        try
        {
            return ImageIO.read ( file );
        }
        catch ( Throwable e )
        {
            return null;
        }
    }

    /**
     * Loads image from URL
     */

    public static ImageIcon loadImage ( URL url )
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
        catch ( Throwable e )
        {
            return null;
        }
    }

    /**
     * Loads image from specified resource near class
     */

    public static ImageIcon loadImage ( Class nearClass, String src )
    {
        try
        {
            return new ImageIcon ( nearClass.getResource ( src ) );
        }
        catch ( Throwable e )
        {
            return null;
        }
    }

    /**
     * Loads image from InputStream
     */

    public static ImageIcon loadImage ( InputStream inputStream )
    {
        try
        {
            return new ImageIcon ( ImageIO.read ( inputStream ) );
        }
        catch ( Throwable e )
        {
            return null;
        }
    }

    /**
     * Adds background to icon
     */

    public static ImageIcon addBackground ( ImageIcon imageIcon, Color background )
    {
        return new ImageIcon ( addBackground ( getBufferedImage ( imageIcon ), background ) );
    }

    public static BufferedImage addBackground ( BufferedImage image, Color background )
    {
        BufferedImage bi = createCompatibleImage ( image );
        Graphics2D g2d = bi.createGraphics ();
        g2d.setPaint ( background );
        g2d.fillRect ( 0, 0, image.getWidth (), image.getHeight () );
        g2d.drawImage ( image, 0, 0, null );
        g2d.dispose ();
        return bi;
    }

    /**
     * Resizes image canvas
     */

    public static ImageIcon resizeCanvas ( ImageIcon imageIcon, int width, int height )
    {
        return new ImageIcon ( resizeCanvas ( getBufferedImage ( imageIcon ), width, height ) );
    }

    public static BufferedImage resizeCanvas ( BufferedImage image, int width, int height )
    {
        BufferedImage bi = createCompatibleImage ( image, width, height );
        Graphics2D g2d = bi.createGraphics ();
        g2d.drawImage ( image, width / 2 - image.getWidth () / 2, height / 2 - image.getHeight () / 2, null );
        g2d.dispose ();
        return bi;
    }

    /**
     * Rotate image
     */

    public static ImageIcon rotateImage90CW ( ImageIcon imageIcon )
    {
        return new ImageIcon ( rotateImage90CW ( getBufferedImage ( imageIcon ) ) );
    }

    public static BufferedImage rotateImage90CW ( BufferedImage image )
    {
        BufferedImage bufferedImage = createCompatibleImage ( image.getHeight (), image.getWidth (), Transparency.TRANSLUCENT );
        Graphics2D g2d = bufferedImage.createGraphics ();
        g2d.translate ( image.getHeight (), 0 );
        g2d.rotate ( Math.PI / 2 );
        g2d.drawImage ( image, 0, 0, null );
        g2d.dispose ();
        return bufferedImage;
    }

    public static ImageIcon rotateImage90CCW ( ImageIcon imageIcon )
    {
        return new ImageIcon ( rotateImage90CCW ( getBufferedImage ( imageIcon ) ) );
    }

    public static BufferedImage rotateImage90CCW ( BufferedImage image )
    {
        BufferedImage bufferedImage = createCompatibleImage ( image.getHeight (), image.getWidth (), Transparency.TRANSLUCENT );
        Graphics2D g2d = bufferedImage.createGraphics ();
        g2d.translate ( 0, image.getWidth () );
        g2d.rotate ( -Math.PI / 2 );
        g2d.drawImage ( image, 0, 0, null );
        g2d.dispose ();
        return bufferedImage;
    }

    public static ImageIcon rotateImage180 ( ImageIcon imageIcon )
    {
        return new ImageIcon ( rotateImage180 ( getBufferedImage ( imageIcon ) ) );
    }

    public static BufferedImage rotateImage180 ( BufferedImage image )
    {
        BufferedImage bufferedImage = createCompatibleImage ( image.getWidth (), image.getHeight (), Transparency.TRANSLUCENT );
        Graphics2D g2d = bufferedImage.createGraphics ();
        g2d.translate ( image.getWidth (), image.getHeight () );
        g2d.rotate ( Math.PI );
        g2d.drawImage ( image, 0, 0, null );
        g2d.dispose ();
        return bufferedImage;
    }

    /**
     * Creates empty icon
     */

    public static ImageIcon createEmptyIcon ( int width, int height )
    {
        return new ImageIcon ( createEmptyImage ( width, height ) );
    }

    public static BufferedImage createEmptyImage ( int width, int height )
    {
        return createCompatibleImage ( width, height, Transparency.TRANSLUCENT );
    }

    /**
     * Creates color icon
     */

    public static ImageIcon createColorIcon ( Color color )
    {
        return createColorIcon ( color, 16, 16 );
    }

    public static ImageIcon createColorIcon ( Color color, int width, int height )
    {
        return new ImageIcon ( createColorImage ( color, width, height ) );
    }

    public static BufferedImage createColorImage ( Color color )
    {
        return createColorImage ( color, 16, 16 );
    }

    public static BufferedImage createColorImage ( Color color, int width, int height )
    {
        BufferedImage image = createCompatibleImage ( width, height, Transparency.TRANSLUCENT );
        Graphics2D g2d = image.createGraphics ();
        LafUtils.setupAntialias ( g2d );
        g2d.setPaint ( Color.GRAY );
        g2d.drawRoundRect ( 0, 0, width - 1, height - 1, StyleConstants.largeRound, StyleConstants.bigRound );
        g2d.setPaint ( Color.WHITE );
        g2d.drawRoundRect ( 1, 1, width - 3, height - 3, StyleConstants.bigRound, StyleConstants.bigRound );
        g2d.setPaint ( color );
        g2d.fillRoundRect ( 2, 2, width - 4, height - 4, StyleConstants.bigRound, StyleConstants.bigRound );
        g2d.dispose ();
        return image;
    }

    /**
     * Creates color chooser icon
     */

    public static final ImageIcon coloredChooserIcon = new ImageIcon ( ImageUtils.class.getResource ( "icons/color/color.png" ) );
    public static final ImageIcon transarentChooserIcon = new ImageIcon ( ImageUtils.class.getResource ( "icons/color/transparent.png" ) );

    public static ImageIcon createColorChooserIcon ( Color color )
    {
        return new ImageIcon ( createColorChooserImage ( color ) );
    }

    public static BufferedImage createColorChooserImage ( Color color )
    {
        BufferedImage image = createCompatibleImage ( 16, 16, Transparency.TRANSLUCENT );
        Graphics2D g2d = image.createGraphics ();
        if ( color == null || color.getAlpha () < 255 )
        {
            g2d.drawImage ( transarentChooserIcon.getImage (), 0, 0, null );
        }
        if ( color != null )
        {
            g2d.setPaint ( color );
            g2d.fillRect ( 2, 2, 13, 12 );
        }
        g2d.drawImage ( coloredChooserIcon.getImage (), 0, 0, null );
        g2d.dispose ();
        return image;
    }

    /**
     * Darkens specified BufferedImage
     */

    public static void darkenImage ( BufferedImage image, float darken )
    {
        Graphics2D g2d = image.createGraphics ();
        g2d.setComposite ( AlphaComposite.getInstance ( AlphaComposite.SRC_OVER, darken ) );
        g2d.setPaint ( Color.BLACK );
        g2d.fillRect ( 0, 0, image.getWidth (), image.getHeight () );
        g2d.dispose ();
    }

    /**
     * Average image color
     */

    public static Color getImageAverageColor ( ImageIcon icon )
    {
        int red = 0;
        int green = 0;
        int blue = 0;
        BufferedImage bi = getBufferedImage ( icon.getImage () );
        for ( int i = 0; i < icon.getIconWidth (); i++ )
        {
            for ( int j = 0; j < icon.getIconHeight (); j++ )
            {
                int rgb = bi.getRGB ( i, j );
                red += ( rgb >> 16 ) & 0xFF;
                green += ( rgb >> 8 ) & 0xFF;
                blue += ( rgb ) & 0xFF;
            }
        }
        int count = icon.getIconWidth () * icon.getIconHeight ();
        return new Color ( red / count, green / count, blue / count );
    }

    /**
     * Is this image format can be loaded
     */

    public static boolean isImageLoadable ( String name )
    {
        return GlobalConstants.IMAGE_FORMATS.contains ( FileUtils.getFileExtPart ( name, false ).toLowerCase () );
    }

    /**
     * Image preview generation
     */

    public static ImageIcon createThumbnailIcon ( String src )
    {
        return createThumbnailIcon ( src, 50 );
    }

    public static ImageIcon createThumbnailIcon ( String src, int size )
    {
        // Retrieving image to create thumbnail from
        ImageIcon icon = getImageIcon ( src );
        if ( icon != null )
        {
            // Creating and caching thumbnail
            ImageIcon imageIcon = createPreviewIcon ( icon.getImage (), size );

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

    public static ImageIcon createPreviewIcon ( ImageIcon image, int size )
    {
        return createPreviewIcon ( image.getImage (), size );
    }

    public static ImageIcon createPreviewIcon ( Image image, int size )
    {
        return createPreviewIcon ( getBufferedImage ( image ), size );
    }

    public static ImageIcon createPreviewIcon ( BufferedImage image, int size )
    {
        BufferedImage previewImage = createPreviewImage ( image, size );
        if ( previewImage != null )
        {
            return new ImageIcon ( previewImage );
        }
        else
        {
            return new ImageIcon ();
        }
    }

    public static BufferedImage createPreviewImage ( BufferedImage image, Dimension fitTo )
    {
        return createPreviewImage ( image, fitTo.width, fitTo.height );
    }

    public static BufferedImage createPreviewImage ( BufferedImage image, int width, int height )
    {
        if ( image.getWidth () > width || image.getHeight () > height )
        {
            // Calcuating maximum preview length
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

    public static BufferedImage createPreviewImage ( BufferedImage image, int length )
    {
        if ( image == null )
        {
            return null;
        }

        int width;
        int height;
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
            int w = Math.max ( 1, width );
            int h = Math.max ( 1, height );

            BufferedImage rescaledImage = createCompatibleImage ( w, h, Transparency.TRANSLUCENT );
            Graphics2D g2d = rescaledImage.createGraphics ();
            LafUtils.setupImageQuality ( g2d );
            g2d.drawImage ( image, 0, 0, width, height, null );
            g2d.dispose ();

            return rescaledImage;
        }
    }

    /**
     * Image read methods
     */

    private static Map<String, ImageIcon> iconsCache = new HashMap<String, ImageIcon> ();

    public static boolean isImageCached ( String src )
    {
        return iconsCache.containsKey ( src ) && iconsCache.get ( src ) != null;
    }

    public static void setImageCache ( String src, ImageIcon imageIcon )
    {
        iconsCache.put ( src, imageIcon );
    }

    public static void clearImagesCache ()
    {
        iconsCache.clear ();
    }

    public static void clearImageCache ( String src )
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

    public static ImageIcon getImageIcon ( File file )
    {
        return getImageIcon ( file, true );
    }

    public static ImageIcon getImageIcon ( File file, boolean useCache )
    {
        return getImageIcon ( file.getAbsolutePath (), useCache );
    }

    public static ImageIcon getImageIcon ( String src )
    {
        return getImageIcon ( src, true );
    }

    public static ImageIcon getImageIcon ( String src, boolean useCache )
    {
        if ( src != null && !src.trim ().equals ( "" ) )
        {
            ImageIcon imageIcon;
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

    private static ImageIcon createImageIcon ( String src )
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
            catch ( Throwable e )
            {
                return new ImageIcon ();
            }
        }
    }

    public static ImageIcon getImageIcon ( URL resource )
    {
        return getImageIcon ( resource, true );
    }

    public static ImageIcon getImageIcon ( URL resource, boolean useCache )
    {
        if ( resource != null )
        {
            String key = resource.toString ();
            ImageIcon imageIcon;
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
     * Makes a copy of BufferedImage
     */

    public static BufferedImage copy ( Image image )
    {
        return copy ( getBufferedImage ( image ) );
    }

    public static BufferedImage copy ( BufferedImage bufferedImage )
    {
        BufferedImage newImage = createCompatibleImage ( bufferedImage );
        Graphics2D g2d = newImage.createGraphics ();
        g2d.drawImage ( bufferedImage, 0, 0, null );
        g2d.dispose ();
        return newImage;
    }

    /**
     * Retrieves BufferedImage from Image
     */

    public static BufferedImage getBufferedImage ( URL url )
    {
        return getBufferedImage ( new ImageIcon ( url ) );
    }

    public static BufferedImage getBufferedImage ( String iconSrc )
    {
        return getBufferedImage ( new ImageIcon ( iconSrc ) );
    }

    public static BufferedImage getBufferedImage ( ImageIcon imageIcon )
    {
        return getBufferedImage ( imageIcon.getImage () );
    }

    public static BufferedImage getBufferedImage ( Image image )
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
            BufferedImage bi = createCompatibleImage ( image.getWidth ( null ), image.getHeight ( null ), Transparency.TRANSLUCENT );
            Graphics2D g2d = bi.createGraphics ();
            g2d.drawImage ( image, 0, 0, null );
            g2d.dispose ();
            return bi;
        }
    }

    /**
     * Retrieves BufferedImage from Icon
     */

    public static BufferedImage getBufferedImage ( Icon icon )
    {
        if ( icon == null )
        {
            return null;
        }
        else if ( icon instanceof ImageIcon )
        {
            Image image = ( ( ImageIcon ) icon ).getImage ();
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

    public static BufferedImage createBufferedImage ( Icon icon )
    {
        BufferedImage bi = createCompatibleImage ( icon.getIconWidth (), icon.getIconHeight (), Transparency.TRANSLUCENT );
        Graphics2D g2d = bi.createGraphics ();
        icon.paintIcon ( null, g2d, 0, 0 );
        g2d.dispose ();
        return bi;
    }


    public static ImageIcon getImageIcon ( Icon icon )
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

    public static ImageIcon getSizedImagePreview ( String src, int length, boolean drawBorder )
    {
        if ( sizedPreviewCache.containsKey ( length + IMAGE_CACHE_SEPARATOR + src ) )
        {
            return sizedPreviewCache.get ( length + IMAGE_CACHE_SEPARATOR + src );
        }
        else
        {
            ImageIcon icon = createThumbnailIcon ( src, length );
            ImageIcon sized = createSizedImagePreview ( icon, length, drawBorder );
            sizedPreviewCache.put ( length + IMAGE_CACHE_SEPARATOR + src, sized );
            return sized;
        }
    }

    public static ImageIcon getSizedImagePreview ( String id, ImageIcon icon, int length, boolean drawBorder )
    {
        if ( sizedPreviewCache.containsKey ( id ) )
        {
            return sizedPreviewCache.get ( id );
        }
        else
        {
            ImageIcon sized = createSizedImagePreview ( icon, length, drawBorder );
            sizedPreviewCache.put ( id, sized );
            return sized;
        }
    }

    public static ImageIcon createSizedImagePreview ( ImageIcon icon, int length, boolean drawBorder )
    {
        // Addition border spacing
        //        if ( drawBorder )
        //        {
        length += 4;
        //        }

        // Creating standard size image
        BufferedImage bi = createCompatibleImage ( length, length, Transparency.TRANSLUCENT );
        Graphics2D g2d = bi.createGraphics ();
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
        ImageIcon imageIcon = new ImageIcon ( bi );
        imageIcon.setDescription ( icon != null ? icon.getDescription () : null );
        return imageIcon;
    }

    /**
     * Creates disabled image copy
     */

    private static Map<String, ImageIcon> grayscaleCache = new HashMap<String, ImageIcon> ();

    public static void clearDisabledCopyCache ( String id )
    {
        grayscaleCache.remove ( id );
    }

    public static ImageIcon getDisabledCopy ( String key, ImageIcon imageIcon )
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

    public static ImageIcon createDisabledCopy ( ImageIcon imageIcon )
    {
        return new ImageIcon ( createDisabledCopy ( imageIcon.getImage () ) );
    }

    public static BufferedImage createDisabledCopy ( Image img )
    {
        BufferedImage bi = createGrayscaleCopy ( img );

        BufferedImage bi2 = createCompatibleImage ( bi );
        Graphics2D g2d = bi2.createGraphics ();
        LafUtils.setupAlphaComposite ( g2d, StyleConstants.disabledIconsTransparency );
        g2d.drawImage ( bi, 0, 0, null );
        g2d.dispose ();

        return bi2;
    }

    /**
     * Creates grayscale image copy
     */

    public static ImageIcon createGrayscaleCopy ( ImageIcon imageIcon )
    {
        return new ImageIcon ( createGrayscaleCopy ( imageIcon.getImage () ) );
    }

    public static BufferedImage createGrayscaleCopy ( Image img )
    {
        return createGrayscaleCopy ( getBufferedImage ( img ) );
    }

    public static BufferedImage createGrayscaleCopy ( BufferedImage img )
    {
        return ImageFilterUtils.applyGrayscaleFilter ( img, null );
    }

    /**
     * Creating partially transparent ImageIcon
     */

    private static Map<String, ImageIcon> trasparentCache = new HashMap<String, ImageIcon> ();

    public static ImageIcon getTransparentCopy ( String id, ImageIcon imageIcon, float trasparency )
    {
        if ( trasparentCache.containsKey ( id ) )
        {
            return trasparentCache.get ( id );
        }
        else
        {
            trasparentCache.put ( id, createTransparentCopy ( imageIcon, trasparency ) );
            return trasparentCache.get ( id );
        }
    }

    public static ImageIcon createTransparentCopy ( ImageIcon imageIcon, float trasparency )
    {
        BufferedImage bi = createCompatibleImage ( imageIcon.getIconWidth (), imageIcon.getIconHeight (), Transparency.TRANSLUCENT );

        Graphics2D g2d = bi.createGraphics ();
        LafUtils.setupAlphaComposite ( g2d, trasparency );
        g2d.drawImage ( imageIcon.getImage (), 0, 0, null );
        g2d.dispose ();

        return new ImageIcon ( bi );
    }

    /**
     * Creating bordered pretty image
     */

    public static BufferedImage createPrettyImage ( Image image, int shadeWidth, int round )
    {
        return createPrettyImage ( getBufferedImage ( image ), shadeWidth, round );
    }

    public static BufferedImage createPrettyImage ( BufferedImage bufferedImage, int shadeWidth, int round )
    {
        int width = bufferedImage.getWidth ();
        int height = bufferedImage.getHeight ();

        BufferedImage bi = createCompatibleImage ( width + shadeWidth * 2 + 1, height + shadeWidth * 2 + 1, Transparency.TRANSLUCENT );
        Graphics2D g2d = bi.createGraphics ();
        LafUtils.setupAntialias ( g2d );
        LafUtils.setupImageQuality ( g2d );

        RoundRectangle2D.Double border = new RoundRectangle2D.Double ( shadeWidth, shadeWidth, width, height, round, round );

        Shape old = g2d.getClip ();
        g2d.setClip ( border );
        g2d.drawImage ( bufferedImage, shadeWidth, shadeWidth, null );
        g2d.setClip ( old );

        LafUtils.drawShade ( g2d, border, StyleConstants.shadeColor, shadeWidth );

        g2d.setPaint ( new LinearGradientPaint ( 0, shadeWidth, 0, height - shadeWidth, new float[]{ 0f, 0.5f, 1f },
                new Color[]{ new Color ( 125, 125, 125, 48 ), new Color ( 125, 125, 125, 0 ), new Color ( 125, 125, 125, 48 ) } ) );
        g2d.fill ( border );

        g2d.setColor ( Color.GRAY );
        g2d.draw ( border );

        g2d.dispose ();

        return bi;
    }

    /**
     * Creating shade for specified shape
     */

    public static BufferedImage createImageShade ( int w, int h, Shape shape, int shadeWidth, float shadeOpacity )
    {
        return createImageShade ( w, h, shape, shadeWidth, shadeOpacity, StyleConstants.transparent );
    }

    public static BufferedImage createImageShade ( int w, int h, Shape shape, int shadeWidth, float shadeOpacity, Color clearColor )
    {
        int width = shadeWidth * 2 + w;
        int height = shadeWidth * 2 + h;

        // Creating template image
        BufferedImage bi = createCompatibleImage ( width, width, Transparency.TRANSLUCENT );
        Graphics2D ig = bi.createGraphics ();
        LafUtils.setupAntialias ( ig );
        ig.translate ( shadeWidth, shadeWidth );
        ig.setPaint ( Color.BLACK );
        ig.fill ( shape );
        ig.dispose ();

        // Creating shade image
        ShadowFilter sf = new ShadowFilter ( shadeWidth, 0, 0, shadeOpacity );
        BufferedImage shade = sf.filter ( bi, null );

        // Clipping shade image
        if ( clearColor != null )
        {
            Graphics2D g2d = shade.createGraphics ();
            LafUtils.setupAntialias ( g2d );
            g2d.translate ( shadeWidth, shadeWidth );
            g2d.setComposite ( AlphaComposite.getInstance ( AlphaComposite.SRC_IN ) );
            g2d.setPaint ( clearColor );
            g2d.fill ( shape );
            g2d.dispose ();
        }

        return shade;
    }

    /**
     * Arrow icons and images creation methods
     */

    public static ImageIcon createSimpleUpArrowIcon ( int shadeWidth )
    {
        return new ImageIcon ( createSimpleUpArrowImage ( shadeWidth ) );
    }

    public static BufferedImage createSimpleUpArrowImage ( int shadeWidth )
    {
        return createImageShade ( 10, 10, createUpArrowFill (), shadeWidth, 1f, Color.WHITE );
    }

    public static ImageIcon createUpArrowIcon ( int shadeWidth )
    {
        return new ImageIcon ( createUpArrowImage ( shadeWidth ) );
    }

    public static BufferedImage createUpArrowImage ( int shadeWidth )
    {
        return createArrowImage ( createUpArrowFill (), createUpArrowBorder (), shadeWidth );
    }

    private static GeneralPath createUpArrowFill ()
    {
        GeneralPath shape = new GeneralPath ( GeneralPath.WIND_EVEN_ODD );
        shape.moveTo ( 0, 7 );
        shape.lineTo ( 5, 1 );
        shape.lineTo ( 10, 7 );
        shape.closePath ();
        return shape;
    }

    private static GeneralPath createUpArrowBorder ()
    {
        GeneralPath border = new GeneralPath ( GeneralPath.WIND_EVEN_ODD );
        border.moveTo ( 0, 6 );
        border.lineTo ( 4, 2 );
        border.lineTo ( 8, 6 );
        border.closePath ();
        return border;
    }

    public static ImageIcon createSimpleLeftArrowIcon ( int shadeWidth )
    {
        return new ImageIcon ( createSimpleLeftArrowImage ( shadeWidth ) );
    }

    public static BufferedImage createSimpleLeftArrowImage ( int shadeWidth )
    {
        return createImageShade ( 10, 10, createLeftArrowFill (), shadeWidth, 1f, Color.WHITE );
    }

    public static ImageIcon createLeftArrowIcon ( int shadeWidth )
    {
        return new ImageIcon ( createLeftArrowImage ( shadeWidth ) );
    }

    public static BufferedImage createLeftArrowImage ( int shadeWidth )
    {
        return createArrowImage ( createLeftArrowFill (), createLeftArrowBorder (), shadeWidth );
    }

    private static GeneralPath createLeftArrowFill ()
    {
        GeneralPath shape = new GeneralPath ( GeneralPath.WIND_EVEN_ODD );
        shape.moveTo ( 7, 0 );
        shape.lineTo ( 1, 5 );
        shape.lineTo ( 7, 10 );
        shape.closePath ();
        return shape;
    }

    private static GeneralPath createLeftArrowBorder ()
    {
        GeneralPath border = new GeneralPath ( GeneralPath.WIND_EVEN_ODD );
        border.moveTo ( 6, 0 );
        border.lineTo ( 2, 4 );
        border.lineTo ( 6, 8 );
        border.closePath ();
        return border;
    }

    public static ImageIcon createSimpleDownArrowIcon ( int shadeWidth )
    {
        return new ImageIcon ( createSimpleDownArrowImage ( shadeWidth ) );
    }

    public static BufferedImage createSimpleDownArrowImage ( int shadeWidth )
    {
        return createImageShade ( 10, 10, createDownArrowFill (), shadeWidth, 1f, Color.WHITE );
    }

    public static ImageIcon createDownArrowIcon ( int shadeWidth )
    {
        return new ImageIcon ( createDownArrowImage ( shadeWidth ) );
    }

    public static BufferedImage createDownArrowImage ( int shadeWidth )
    {
        return createArrowImage ( createDownArrowFill (), createDownArrowBorder (), shadeWidth );
    }

    private static GeneralPath createDownArrowFill ()
    {
        GeneralPath shape = new GeneralPath ( GeneralPath.WIND_EVEN_ODD );
        shape.moveTo ( 0, 3 );
        shape.lineTo ( 5, 9 );
        shape.lineTo ( 10, 3 );
        shape.closePath ();
        return shape;
    }

    private static GeneralPath createDownArrowBorder ()
    {
        GeneralPath border = new GeneralPath ( GeneralPath.WIND_EVEN_ODD );
        border.moveTo ( 0, 3 );
        border.lineTo ( 4, 7 );
        border.lineTo ( 8, 3 );
        border.closePath ();
        return border;
    }

    public static ImageIcon createSimpleRightArrowIcon ( int shadeWidth )
    {
        return new ImageIcon ( createSimpleRightArrowImage ( shadeWidth ) );
    }

    public static BufferedImage createSimpleRightArrowImage ( int shadeWidth )
    {
        return createImageShade ( 10, 10, createRightArrowFill (), shadeWidth, 1f, Color.WHITE );
    }

    public static ImageIcon createRightArrowIcon ( int shadeWidth )
    {
        return new ImageIcon ( createRightArrowImage ( shadeWidth ) );
    }

    public static BufferedImage createRightArrowImage ( int shadeWidth )
    {
        return createArrowImage ( createRightArrowFill (), createRightArrowBorder (), shadeWidth );
    }

    private static GeneralPath createRightArrowFill ()
    {
        GeneralPath shape = new GeneralPath ( GeneralPath.WIND_EVEN_ODD );
        shape.moveTo ( 3, 0 );
        shape.lineTo ( 9, 5 );
        shape.lineTo ( 3, 10 );
        shape.closePath ();
        return shape;
    }

    private static GeneralPath createRightArrowBorder ()
    {
        GeneralPath border = new GeneralPath ( GeneralPath.WIND_EVEN_ODD );
        border.moveTo ( 3, 0 );
        border.lineTo ( 7, 4 );
        border.lineTo ( 3, 8 );
        border.closePath ();
        return border;
    }

    private static BufferedImage createArrowImage ( GeneralPath shape, GeneralPath border, int shadeWidth )
    {
        BufferedImage image = createImageShade ( 10, 10, shape, shadeWidth, 1f, Color.BLACK );
        Graphics2D g2d = image.createGraphics ();
        LafUtils.setupAntialias ( g2d );
        g2d.setPaint ( Color.WHITE );
        g2d.translate ( shadeWidth, shadeWidth );
        g2d.draw ( border );
        g2d.dispose ();
        return image;
    }
}