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
import com.alee.api.annotations.Nullable;
import com.alee.api.resource.Resource;
import com.alee.api.ui.DisabledCopySupplier;
import com.alee.api.ui.TransparentCopySupplier;
import com.alee.graphics.filters.ShadowFilter;
import com.alee.utils.collection.ImmutableList;
import com.mortennobel.imagescaling.ResampleOp;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.color.ColorSpace;
import java.awt.geom.Area;
import java.awt.image.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.*;

/**
 * @author Mikle Garin
 */
public final class ImageUtils
{
    /**
     * Viewable image formats.
     */
    public static final List<String> SUPPORTED_IMAGES = new ImmutableList<String> (
            "png", "apng", "gif", "agif", "jpg", "jpeg", "jpeg2000", "bmp"
    );

    /**
     * Grayscale image filter.
     */
    private static final ColorConvertOp GRAYSCALE_FILTER = new ColorConvertOp ( ColorSpace.getInstance ( ColorSpace.CS_GRAY ), null );

    /**
     * Disabled icons cache.
     */
    protected static final Map<Icon, Icon> DISABLED_ICONS_CACHE = new WeakHashMap<Icon, Icon> ( 50 );

    /**
     * Transparent icons cache.
     */
    protected static final Map<Icon, Map<Float, Icon>> TRANSPARENT_ICONS_CACHE = new WeakHashMap<Icon, Map<Float, Icon>> ( 10 );

    /**
     * Private constructor to avoid instantiation.
     */
    private ImageUtils ()
    {
        throw new UtilityException ( "Utility classes are not meant to be instantiated" );
    }

    /**
     * Returns whether or not loading of the image with extension mentioned in the specified name is supported.
     *
     * @param name image name with extension in it
     * @return {@code true} if loading of the image with extension mentioned in the specified name is supported, {@code false} otherwise
     */
    public static boolean isImageSupported ( @Nullable final String name )
    {
        return SUPPORTED_IMAGES.contains ( FileUtils.getFileExtPart ( name, false ).toLowerCase ( Locale.ROOT ) );
    }

    /**
     * Returns whether or not specified {@link BufferedImage} pixel is fully transparent.
     *
     * @param bufferedImage {@link BufferedImage}
     * @param x             {@link BufferedImage} pixel X index
     * @param y             {@link BufferedImage} pixel Y index
     * @return {@code true} if specified {@link BufferedImage} pixel is fully transparent, {@code false} otherwise
     */
    public static boolean isTransparent ( @NotNull final BufferedImage bufferedImage, final int x, final int y )
    {
        return ( bufferedImage.getRGB ( x, y ) >> 24 & 0xFF ) > 0;
    }

    /**
     * Returns new empty {@link BufferedImage} compatible with default screen {@link GraphicsConfiguration}.
     *
     * @param bufferedImage {@link BufferedImage} to use size and transparency settings from
     * @return new empty {@link BufferedImage} compatible with default screen {@link GraphicsConfiguration}
     */
    @NotNull
    public static BufferedImage createCompatibleImage ( @NotNull final BufferedImage bufferedImage )
    {
        return createCompatibleImage ( bufferedImage.getWidth (), bufferedImage.getHeight (), bufferedImage.getTransparency () );
    }

    /**
     * Returns new {@link BufferedImage} compatible with default screen {@link GraphicsConfiguration}.
     *
     * @param width  {@link BufferedImage} width
     * @param height {@link BufferedImage} height
     * @return new {@link BufferedImage} compatible with default screen {@link GraphicsConfiguration}
     */
    @NotNull
    public static BufferedImage createCompatibleImage ( final int width, final int height )
    {
        return SystemUtils.getGraphicsConfiguration ().createCompatibleImage ( width, height );
    }

    /**
     * Returns new {@link BufferedImage} compatible with default screen {@link GraphicsConfiguration}.
     *
     * @param width        new {@link BufferedImage} width
     * @param height       new {@link BufferedImage} height
     * @param transparency new {@link BufferedImage} {@link Transparency} type
     * @return new {@link BufferedImage} compatible with default screen {@link GraphicsConfiguration}
     */
    @NotNull
    public static BufferedImage createCompatibleImage ( final int width, final int height, final int transparency )
    {
        return SystemUtils.getGraphicsConfiguration ().createCompatibleImage ( width, height, transparency );
    }

    /**
     * Returns whether or not specified {@link BufferedImage} is compatible with default screen {@link GraphicsConfiguration}.
     *
     * @param bufferedImage {@link BufferedImage} to check
     * @return {@code true} if specified {@link BufferedImage} is compatible with default screen {@link GraphicsConfiguration},
     * {@code false} otherwise
     */
    public static boolean isCompatibleImage ( @NotNull final BufferedImage bufferedImage )
    {
        return bufferedImage.getColorModel ().equals ( SystemUtils.getGraphicsConfiguration ().getColorModel () );
    }

    /**
     * Returns {@link BufferedImage} loaded from the specified {@link Resource}.
     * Resulting {@link BufferedImage} is also made compatible with default screen {@link GraphicsConfiguration}.
     *
     * @param resource {@link Resource} to load {@link BufferedImage} from
     * @return {@link BufferedImage} loaded from the specified {@link Resource}
     */
    @NotNull
    public static BufferedImage loadCompatibleImage ( @NotNull final Resource resource )
    {
        try
        {
            return toCompatibleImage ( loadBufferedImage ( resource ) );
        }
        catch ( final Exception e )
        {
            throw new UtilityException ( "Unable to load compatible BufferedImage: " + resource, e );
        }
    }

    /**
     * Returns {@link BufferedImage} compatible with default screen {@link GraphicsConfiguration}.
     * Specified {@link BufferedImage} might be copied or simply returned "as is" as a result of calling this method.
     *
     * @param bufferedImage {@link BufferedImage} to create copy of that will be compatible with default screen {@link
     *                      GraphicsConfiguration}
     * @return {@link BufferedImage} compatible with default screen {@link GraphicsConfiguration}.
     */
    @NotNull
    public static BufferedImage toCompatibleImage ( @NotNull final BufferedImage bufferedImage )
    {
        final BufferedImage compatibleImage;
        if ( isCompatibleImage ( bufferedImage ) )
        {
            compatibleImage = bufferedImage;
        }
        else
        {
            compatibleImage = createCompatibleImage ( bufferedImage );
            final Graphics2D g2d = compatibleImage.createGraphics ();
            g2d.drawImage ( bufferedImage, 0, 0, null );
            g2d.dispose ();
        }
        return compatibleImage;
    }

    /**
     * Returns {@link BufferedImage} loaded from the specified {@link Resource}.
     *
     * @param resource {@link Resource} to load {@link BufferedImage} from
     * @return {@link BufferedImage} loaded from the specified {@link Resource}
     */
    @NotNull
    public static BufferedImage loadBufferedImage ( @NotNull final Resource resource )
    {
        return loadBufferedImage ( resource.getInputStream () );
    }

    /**
     * Returns {@link BufferedImage} loaded from the specified {@link InputStream}.
     *
     * @param inputStream {@link InputStream} to load {@link BufferedImage} from
     * @return {@link BufferedImage} loaded from the specified {@link InputStream}
     */
    @NotNull
    public static BufferedImage loadBufferedImage ( @NotNull final InputStream inputStream )
    {
        try
        {
            return ImageIO.read ( inputStream );
        }
        catch ( final Exception e )
        {
            throw new UtilityException ( "Unable to load BufferedImage: " + inputStream, e );
        }
    }

    /**
     * Returns {@link BufferedImage} converted from the specified {@link Image}.
     *
     * @param image {@link Image} to convert
     * @return {@link BufferedImage} converted from the specified {@link Image}
     */
    @Nullable
    public static BufferedImage toBufferedImage ( @Nullable final Image image )
    {
        final BufferedImage bufferedImage;
        if ( image instanceof BufferedImage )
        {
            bufferedImage = ( BufferedImage ) image;
        }
        else if ( image != null && image.getWidth ( null ) > 0 && image.getHeight ( null ) > 0 )
        {
            final BufferedImage bi = createCompatibleImage ( image.getWidth ( null ), image.getHeight ( null ), Transparency.TRANSLUCENT );
            final Graphics2D g2d = bi.createGraphics ();
            g2d.drawImage ( image, 0, 0, null );
            g2d.dispose ();
            bufferedImage = bi;
        }
        else
        {
            bufferedImage = null;
        }
        return bufferedImage;
    }

    /**
     * Returns {@link BufferedImage} converted from the specified {@link Image}.
     *
     * @param image {@link Image} to convert
     * @return {@link BufferedImage} converted from the specified {@link Image}
     */
    @NotNull
    public static BufferedImage toNonNullBufferedImage ( @NotNull final Image image )
    {
        final BufferedImage bufferedImage;
        if ( image instanceof BufferedImage )
        {
            bufferedImage = ( BufferedImage ) image;
        }
        else if ( image.getWidth ( null ) > 0 && image.getHeight ( null ) > 0 )
        {
            final BufferedImage bi = createCompatibleImage ( image.getWidth ( null ), image.getHeight ( null ), Transparency.TRANSLUCENT );
            final Graphics2D g2d = bi.createGraphics ();
            g2d.drawImage ( image, 0, 0, null );
            g2d.dispose ();
            bufferedImage = bi;
        }
        else
        {
            throw new UtilityException ( "Unable to convert Image to BufferedImage: " + image );
        }
        return bufferedImage;
    }

    /**
     * Returns {@link BufferedImage} converted from the specified {@link Icon}.
     *
     * @param icon {@link Icon} to convert
     * @return {@link BufferedImage} converted from the specified {@link Icon}
     */
    @Nullable
    public static BufferedImage toBufferedImage ( @Nullable final Icon icon )
    {
        return icon != null ? toNonNullBufferedImage ( icon ) : null;
    }

    /**
     * Returns {@link BufferedImage} converted from the specified {@link Icon}.
     *
     * @param icon {@link Icon} to convert
     * @return {@link BufferedImage} converted from the specified {@link Icon}
     */
    @NotNull
    public static BufferedImage toNonNullBufferedImage ( @NotNull final Icon icon )
    {
        final BufferedImage bufferedImage;
        if ( icon instanceof ImageIcon && ( ( ImageIcon ) icon ).getImage () != null )
        {
            bufferedImage = toNonNullBufferedImage ( ( ( ImageIcon ) icon ).getImage () );
        }
        else
        {
            bufferedImage = paintToBufferedImage ( icon );
        }
        return bufferedImage;
    }

    /**
     * Returns {@link BufferedImage} with the specified {@link Icon} painted on it.
     *
     * @param icon {@link Icon} to paint
     * @return {@link BufferedImage} with the specified {@link Icon} painted on it
     */
    @NotNull
    private static BufferedImage paintToBufferedImage ( @NotNull final Icon icon )
    {
        final BufferedImage bufferedImage = createCompatibleImage ( icon.getIconWidth (), icon.getIconHeight (), Transparency.TRANSLUCENT );
        final Graphics2D g2d = bufferedImage.createGraphics ();
        icon.paintIcon ( null, g2d, 0, 0 );
        g2d.dispose ();
        return bufferedImage;
    }

    /**
     * Returns {@link BufferedImage} converted from the specified {@link RenderedImage}.
     *
     * @param renderedImage {@link RenderedImage} to convert
     * @return {@link BufferedImage} converted from the specified {@link RenderedImage}
     */
    @Nullable
    public static BufferedImage toBufferedImage ( @Nullable final RenderedImage renderedImage )
    {
        return renderedImage != null ? toNonNullBufferedImage ( renderedImage ) : null;
    }

    /**
     * Returns {@link BufferedImage} converted from the specified {@link RenderedImage}.
     *
     * @param renderedImage {@link RenderedImage} to convert
     * @return {@link BufferedImage} converted from the specified {@link RenderedImage}
     */
    @NotNull
    public static BufferedImage toNonNullBufferedImage ( @NotNull final RenderedImage renderedImage )
    {
        final BufferedImage bufferedImage;
        if ( renderedImage instanceof BufferedImage )
        {
            bufferedImage = ( BufferedImage ) renderedImage;
        }
        else
        {
            final ColorModel cm = renderedImage.getColorModel ();
            final int width = renderedImage.getWidth ();
            final int height = renderedImage.getHeight ();
            final WritableRaster raster = cm.createCompatibleWritableRaster ( width, height );
            final boolean isAlphaPremultiplied = cm.isAlphaPremultiplied ();
            final Hashtable properties = new Hashtable ();
            final String[] keys = renderedImage.getPropertyNames ();
            if ( keys != null )
            {
                for ( final String key : keys )
                {
                    properties.put ( key, renderedImage.getProperty ( key ) );
                }
            }

            bufferedImage = new BufferedImage ( cm, raster, isAlphaPremultiplied, properties );
            renderedImage.copyData ( raster );
        }
        return bufferedImage;
    }

    /**
     * Returns {@link BufferedImage} containing a copy of currently displayed {@link Icon} frame.
     *
     * @param icon {@link Icon} to create {@link BufferedImage} for
     * @return {@link BufferedImage} containing a copy of currently displayed {@link Icon} frame
     */
    @NotNull
    public static BufferedImage copyToBufferedImage ( @NotNull final Icon icon )
    {
        return copyToBufferedImage ( toNonNullBufferedImage ( icon ) );
    }

    /**
     * Returns {@link BufferedImage} containing a copy of {@link Image}.
     *
     * @param image {@link Image} to create {@link BufferedImage} for
     * @return {@link BufferedImage} containing a copy of {@link Image}
     */
    @NotNull
    public static BufferedImage copyToBufferedImage ( @NotNull final Image image )
    {
        return copyToBufferedImage ( toNonNullBufferedImage ( image ) );
    }

    /**
     * Returns {@link BufferedImage} containing a copy of {@link BufferedImage}.
     *
     * @param bufferedImage {@link BufferedImage} to create {@link BufferedImage} for
     * @return {@link BufferedImage} containing a copy of {@link BufferedImage}
     */
    @NotNull
    public static BufferedImage copyToBufferedImage ( @NotNull final BufferedImage bufferedImage )
    {
        final BufferedImage newImage = createCompatibleImage ( bufferedImage );
        final Graphics2D g2d = newImage.createGraphics ();
        g2d.drawImage ( bufferedImage, 0, 0, null );
        g2d.dispose ();
        return newImage;
    }

    /**
     * Returns {@link ImageIcon} loaded from the specified {@link Resource}.
     * This method doesn't use {@link ImageIO} because loaded image might not be represented by a single {@link BufferedImage}.
     * For example GIF icons consist of many frames that are handled internally by {@link ImageIcon}.
     *
     * @param resource {@link Resource} to load {@link ImageIcon} from
     * @return {@link ImageIcon} loaded from the specified {@link Resource}
     */
    @NotNull
    public static ImageIcon loadImageIcon ( @NotNull final Resource resource )
    {
        return loadImageIcon ( resource.getInputStream () );
    }

    /**
     * Returns {@link ImageIcon} loaded from the specified {@link Resource}.
     * This method doesn't use {@link ImageIO} because loaded image might not be represented by a single {@link BufferedImage}.
     * For example GIF icons consist of many frames that are handled internally by {@link ImageIcon}.
     *
     * @param inputStream {@link InputStream} to load {@link ImageIcon} from
     * @return {@link ImageIcon} loaded from the specified {@link Resource}
     */
    @NotNull
    public static ImageIcon loadImageIcon ( @NotNull final InputStream inputStream )
    {
        return new ImageIcon ( IOUtils.toByteArray ( inputStream ) );
    }

    /**
     * Returns {@link ImageIcon} converted from the specified {@link Image}.
     *
     * @param image {@link Image} to convert
     * @return {@link ImageIcon} converted from the specified {@link Image}
     */
    @Nullable
    public static ImageIcon toImageIcon ( @Nullable final Image image )
    {
        return image != null ? toNonNullImageIcon ( image ) : null;
    }

    /**
     * Returns {@link ImageIcon} converted from the specified {@link Image}.
     *
     * @param image {@link Image} to convert
     * @return {@link ImageIcon} converted from the specified {@link Image}
     */
    @NotNull
    public static ImageIcon toNonNullImageIcon ( @NotNull final Image image )
    {
        return new ImageIcon ( image );
    }

    /**
     * Returns {@link ImageIcon} converted from the specified {@link Icon}.
     *
     * @param icon {@link Icon} to convert
     * @return {@link ImageIcon} converted from the specified {@link Icon}
     */
    @Nullable
    public static ImageIcon toImageIcon ( @Nullable final Icon icon )
    {
        return icon != null ? toNonNullImageIcon ( icon ) : null;
    }

    /**
     * Returns {@link ImageIcon} converted from the specified {@link Icon}.
     *
     * @param icon {@link Icon} to convert
     * @return {@link ImageIcon} converted from the specified {@link Icon}
     */
    @NotNull
    public static ImageIcon toNonNullImageIcon ( @NotNull final Icon icon )
    {
        final ImageIcon imageIcon;
        if ( icon instanceof ImageIcon )
        {
            imageIcon = ( ImageIcon ) icon;
        }
        else
        {
            imageIcon = new ImageIcon ( toNonNullBufferedImage ( icon ) );
        }
        return imageIcon;
    }

    /**
     * Returns new or cached copy of specified {@link Icon} made look disabled.
     *
     * @param icon {@link Icon} to retrieve disabled copy for
     * @return new or cached copy of specified {@link Icon} made look disabled
     */
    @NotNull
    public static Icon getDisabledCopy ( @NotNull final Icon icon )
    {
        final Icon disabledIcon;
        if ( DISABLED_ICONS_CACHE.containsKey ( icon ) )
        {
            disabledIcon = DISABLED_ICONS_CACHE.get ( icon );
        }
        else
        {
            synchronized ( DISABLED_ICONS_CACHE )
            {
                if ( DISABLED_ICONS_CACHE.containsKey ( icon ) )
                {
                    disabledIcon = DISABLED_ICONS_CACHE.get ( icon );
                }
                else
                {
                    disabledIcon = createDisabledCopy ( icon );
                    DISABLED_ICONS_CACHE.put ( icon, disabledIcon );
                }
            }
        }
        return disabledIcon;
    }

    /**
     * Returns {@link Icon} that is copy of the specified {@link Icon} made look disabled.
     *
     * @param icon {@link Icon} to create disabled copy for
     * @return {@link Icon} that is copy of the specified {@link Icon} made look disabled
     */
    @NotNull
    public static Icon createDisabledCopy ( @NotNull final Icon icon )
    {
        final Icon disabledCopy;
        if ( icon instanceof DisabledCopySupplier )
        {
            disabledCopy = ( ( DisabledCopySupplier<Icon> ) icon ).createDisabledCopy ();
        }
        else
        {
            disabledCopy = new ImageIcon ( createDisabledCopy ( toNonNullBufferedImage ( icon ) ) );
        }
        return disabledCopy;
    }

    /**
     * Returns {@link BufferedImage} that is copy of the specified {@link Image} made look disabled.
     *
     * @param image {@link Image} to create disabled copy for
     * @return {@link BufferedImage} that is copy of the specified {@link Image} made look disabled
     */
    @NotNull
    public static BufferedImage createDisabledCopy ( @NotNull final Image image )
    {
        return createDisabledCopy ( toNonNullBufferedImage ( image ) );
    }

    /**
     * Returns {@link BufferedImage} that is copy of the specified {@link BufferedImage} made look disabled.
     *
     * @param bufferedImage {@link BufferedImage} to create disabled copy for
     * @return {@link BufferedImage} that is copy of the specified {@link BufferedImage} made look disabled
     */
    @NotNull
    public static BufferedImage createDisabledCopy ( @NotNull final BufferedImage bufferedImage )
    {
        // Creating image copy
        final BufferedImage disabledCopy = createCompatibleImage ( bufferedImage );

        // Painting original image as semi-transparent
        final Graphics2D g2d = disabledCopy.createGraphics ();
        GraphicsUtils.setupAlphaComposite ( g2d, 0.7f );
        g2d.drawImage ( bufferedImage, 0, 0, null );
        g2d.dispose ();

        // Applying grayscale filter
        GRAYSCALE_FILTER.filter ( disabledCopy, disabledCopy );

        return disabledCopy;
    }

    /**
     * Returns new or cached copy of specified {@link Icon} made look disabled.
     *
     * @param icon    {@link Icon} to retrieve disabled copy for
     * @param opacity opacity value, must be between 0 and 1
     * @return new or cached copy of specified {@link Icon} made look disabled
     */
    @NotNull
    public static Icon getTransparentCopy ( @NotNull final Icon icon, final float opacity )
    {
        final Map<Float, Icon> transparentCopies;
        if ( TRANSPARENT_ICONS_CACHE.containsKey ( icon ) )
        {
            transparentCopies = TRANSPARENT_ICONS_CACHE.get ( icon );
        }
        else
        {
            synchronized ( TRANSPARENT_ICONS_CACHE )
            {
                if ( TRANSPARENT_ICONS_CACHE.containsKey ( icon ) )
                {
                    transparentCopies = TRANSPARENT_ICONS_CACHE.get ( icon );
                }
                else
                {
                    transparentCopies = new HashMap<Float, Icon> ( 1 );
                    TRANSPARENT_ICONS_CACHE.put ( icon, transparentCopies );
                }
            }
        }
        final Icon transparentIcon;
        if ( transparentCopies.containsKey ( opacity ) )
        {
            transparentIcon = transparentCopies.get ( opacity );
        }
        else
        {
            synchronized ( TRANSPARENT_ICONS_CACHE )
            {
                if ( transparentCopies.containsKey ( opacity ) )
                {
                    transparentIcon = transparentCopies.get ( opacity );
                }
                else
                {
                    transparentIcon = createTransparentCopy ( icon, opacity );
                    transparentCopies.put ( opacity, transparentIcon );
                }
            }
        }
        return transparentIcon;
    }

    /**
     * Returns {@link Icon} that is copy of the specified {@link Icon} made look disabled.
     *
     * @param icon    {@link Icon} to create disabled copy for
     * @param opacity opacity value, must be between 0 and 1
     * @return {@link Icon} that is copy of the specified {@link Icon} made look disabled
     */
    @NotNull
    public static Icon createTransparentCopy ( @NotNull final Icon icon, final float opacity )
    {
        final Icon transparentCopy;
        if ( icon instanceof TransparentCopySupplier )
        {
            transparentCopy = ( ( TransparentCopySupplier<Icon> ) icon ).createTransparentCopy ( opacity );
        }
        else
        {
            transparentCopy = new ImageIcon ( createTransparentCopy ( toNonNullBufferedImage ( icon ), opacity ) );
        }
        return transparentCopy;
    }

    /**
     * Returns {@link BufferedImage} that is copy of the specified {@link Image} made semi-transparent.
     *
     * @param image   {@link Image} to create semi-transparent copy for
     * @param opacity opacity value, must be between 0 and 1
     * @return {@link BufferedImage} that is copy of the specified {@link Image} made semi-transparent
     */
    @NotNull
    public static BufferedImage createTransparentCopy ( @NotNull final Image image, final float opacity )
    {
        return createTransparentCopy ( toNonNullBufferedImage ( image ), opacity );
    }

    /**
     * Returns {@link BufferedImage} that is copy of the specified {@link BufferedImage} made semi-transparent.
     *
     * @param bufferedImage {@link BufferedImage} to create semi-transparent copy for
     * @param opacity       opacity value, must be between 0 and 1
     * @return {@link BufferedImage} that is copy of the specified {@link BufferedImage} made semi-transparent
     */
    @NotNull
    public static BufferedImage createTransparentCopy ( @NotNull final BufferedImage bufferedImage, final float opacity )
    {
        final BufferedImage transparentCopy = createCompatibleImage (
                bufferedImage.getWidth (),
                bufferedImage.getHeight (),
                Transparency.TRANSLUCENT
        );

        final Graphics2D g2d = transparentCopy.createGraphics ();
        GraphicsUtils.setupAlphaComposite ( g2d, opacity );
        g2d.drawImage ( bufferedImage, 0, 0, null );
        g2d.dispose ();

        return transparentCopy;
    }

    /**
     * Returns new {@link BufferedImage} with the specified {@link Icon} painted on it within the specified {@link Shape}.
     *
     * @param icon  {@link Icon} to cut
     * @param shape {@link Shape} to cut
     * @return new {@link BufferedImage} with the specified {@link Icon} painted on it within the specified {@link Shape}
     */
    public static BufferedImage cutImage ( @NotNull final Icon icon, @NotNull final Shape shape )
    {
        return cutImage ( toNonNullBufferedImage ( icon ), shape );
    }

    /**
     * Returns new {@link BufferedImage} with the specified {@link Image} painted on it within the specified {@link Shape}.
     *
     * @param image {@link Image} to cut
     * @param shape {@link Shape} to cut
     * @return new {@link BufferedImage} with the specified {@link Image} painted on it within the specified {@link Shape}
     */
    public static BufferedImage cutImage ( @NotNull final Image image, @NotNull final Shape shape )
    {
        return cutImage ( toNonNullBufferedImage ( image ), shape );
    }

    /**
     * Returns new {@link BufferedImage} with the specified {@link BufferedImage} painted on it within the specified {@link Shape}.
     *
     * @param bufferedImage {@link Image} to cut
     * @param shape         {@link Shape} to cut
     * @return new {@link BufferedImage} with the specified {@link BufferedImage} painted on it within the specified {@link Shape}
     */
    public static BufferedImage cutImage ( @NotNull final BufferedImage bufferedImage, @NotNull final Shape shape )
    {
        final BufferedImage cutImage = createCompatibleImage ( bufferedImage );
        final Graphics2D g2d = cutImage.createGraphics ();
        GraphicsUtils.setupAntialias ( g2d );
        g2d.setPaint ( Color.WHITE );
        g2d.fill ( shape );
        g2d.setComposite ( AlphaComposite.getInstance ( AlphaComposite.SRC_IN ) );
        g2d.drawImage ( bufferedImage, 0, 0, null );
        g2d.dispose ();
        return cutImage;
    }

    /**
     * Returns thumbnail {@link BufferedImage} of the specified {@link BufferedImage}.
     * Note that this method can only scale specified {@link BufferedImage} down, it will not scale it up.
     * Also n that resulting {@link BufferedImage} will not have width and height larger than specified maximum ones, but might be less.
     *
     * @param bufferedImage {@link BufferedImage} to create thumbnail for
     * @param maxSize       maximum thumbnail {@link BufferedImage} size
     * @return thumbnail {@link BufferedImage} of the specified {@link BufferedImage}
     */
    @NotNull
    public static BufferedImage createImageThumbnail ( @NotNull final BufferedImage bufferedImage, @NotNull final Dimension maxSize )
    {
        return createImageThumbnail ( bufferedImage, maxSize.width, maxSize.height );
    }

    /**
     * Returns thumbnail {@link BufferedImage} of the specified {@link BufferedImage}.
     * Note that this method can only scale specified {@link BufferedImage} down, it will not scale it up.
     * Also n that resulting {@link BufferedImage} will not have width and height larger than specified maximum ones, but might be less.
     *
     * @param bufferedImage {@link BufferedImage} to create thumbnail for
     * @param maxWidth      maximum thumbnail {@link BufferedImage} width
     * @param maxHeight     maximum thumbnail {@link BufferedImage} height
     * @return thumbnail {@link BufferedImage} of the specified {@link BufferedImage}
     */
    @NotNull
    public static BufferedImage createImageThumbnail ( @NotNull final BufferedImage bufferedImage, final int maxWidth, final int maxHeight )
    {
        final BufferedImage preview;
        if ( bufferedImage.getWidth () > maxWidth || bufferedImage.getHeight () > maxHeight )
        {
            if ( maxHeight * ( ( float ) bufferedImage.getWidth () / bufferedImage.getHeight () ) <= maxWidth )
            {
                preview = createImageThumbnail ( bufferedImage, Math.max ( maxHeight,
                        Math.round ( maxHeight * ( ( float ) bufferedImage.getWidth () / bufferedImage.getHeight () ) ) ) );
            }
            else
            {
                preview = createImageThumbnail ( bufferedImage, Math.max ( maxWidth,
                        Math.round ( maxWidth * ( ( float ) bufferedImage.getHeight () / bufferedImage.getWidth () ) ) ) );
            }
        }
        else
        {
            preview = bufferedImage;
        }
        return preview;
    }

    /**
     * Returns thumbnail {@link BufferedImage} of the specified {@link BufferedImage}.
     * Note that this method can only scale specified {@link BufferedImage} down, it will not scale it up.
     * Also note that resulting {@link BufferedImage} will not have width and height larger than specified maximum, but might be less.
     *
     * @param bufferedImage {@link BufferedImage} to create thumbnail for
     * @param maxSize       maximum thumbnail {@link BufferedImage} width and height
     * @return thumbnail {@link BufferedImage} of the specified {@link BufferedImage}
     */
    @NotNull
    public static BufferedImage createImageThumbnail ( @NotNull final BufferedImage bufferedImage, final int maxSize )
    {
        final BufferedImage preview;
        if ( bufferedImage.getWidth () <= maxSize && bufferedImage.getHeight () <= maxSize )
        {
            // Simply return source image, but make sure it has compatible type
            preview = toCompatibleImage ( bufferedImage );
        }
        else
        {
            // Calculate resulting width and height
            final int width;
            final int height;
            if ( bufferedImage.getWidth () > bufferedImage.getHeight () )
            {
                width = maxSize;
                height = Math.round ( ( float ) maxSize * bufferedImage.getHeight () / bufferedImage.getWidth () );
            }
            else if ( bufferedImage.getWidth () < bufferedImage.getHeight () )
            {
                height = maxSize;
                width = Math.round ( ( float ) maxSize * bufferedImage.getWidth () / bufferedImage.getHeight () );
            }
            else
            {
                width = height = maxSize;
            }

            // Creating scaled down image
            if ( width >= 3 && height >= 3 )
            {
                // Using Java Image Scaling library approach
                final ResampleOp scaleOp = new ResampleOp ( width, height );
                preview = scaleOp.filter ( bufferedImage, createCompatibleImage ( bufferedImage ) );
            }
            else
            {
                // Scaling down a very small image
                preview = createCompatibleImage ( Math.max ( 1, width ), Math.max ( 1, height ), Transparency.TRANSLUCENT );
                final Graphics2D g2d = preview.createGraphics ();
                GraphicsUtils.setupImageQuality ( g2d );
                g2d.drawImage ( bufferedImage, 0, 0, width, height, null );
                g2d.dispose ();
            }
        }
        return preview;
    }

    /**
     * Returns dominant {@link Color} for the specified {@link BufferedImage}.
     *
     * @param image {@link BufferedImage} to determine dominant {@link Color} for
     * @return dominant {@link Color} for the specified {@link BufferedImage}
     */
    @NotNull
    public static Color getDominantColor ( @NotNull final BufferedImage image )
    {
        int red = 0;
        int green = 0;
        int blue = 0;
        for ( int i = 0; i < image.getWidth (); i++ )
        {
            for ( int j = 0; j < image.getHeight (); j++ )
            {
                final int rgb = image.getRGB ( i, j );
                red += rgb >> 16 & 0xFF;
                green += rgb >> 8 & 0xFF;
                blue += rgb & 0xFF;
            }
        }
        final int count = image.getWidth () * image.getHeight ();
        return new Color ( red / count, green / count, blue / count );
    }

    /**
     * Returns new {@link ImageIcon} with specified {@link Icon} painted on it and rotated 90 degrees clockwise.
     *
     * @param icon {@link Icon} to rotate
     * @return new {@link ImageIcon} with specified {@link Icon} painted on it and rotated 90 degrees clockwise.
     */
    @NotNull
    public static ImageIcon rotateIcon90CW ( @NotNull final Icon icon )
    {
        return new ImageIcon ( rotateImage90CW ( toNonNullBufferedImage ( icon ) ) );
    }

    /**
     * Returns new {@link BufferedImage} with specified {@link Image} painted on it and rotated 90 degrees clockwise.
     *
     * @param image {@link Image} to rotate
     * @return new {@link BufferedImage} with specified {@link Image} painted on it and rotated 90 degrees clockwise.
     */
    @NotNull
    public static BufferedImage rotateImage90CW ( @NotNull final Image image )
    {
        return rotateImage90CW ( toNonNullBufferedImage ( image ) );
    }

    /**
     * Returns new {@link BufferedImage} with specified {@link BufferedImage} painted on it and rotated 90 degrees clockwise.
     *
     * @param bufferedImage {@link BufferedImage} to rotate
     * @return new {@link BufferedImage} with specified {@link BufferedImage} painted on it and rotated 90 degrees clockwise.
     */
    @NotNull
    public static BufferedImage rotateImage90CW ( @NotNull final BufferedImage bufferedImage )
    {
        final BufferedImage rotatedImage = createCompatibleImage ( bufferedImage );
        final Graphics2D g2d = rotatedImage.createGraphics ();
        g2d.translate ( bufferedImage.getHeight (), 0 );
        g2d.rotate ( Math.PI / 2 );
        g2d.drawImage ( bufferedImage, 0, 0, null );
        g2d.dispose ();
        return rotatedImage;
    }

    /**
     * Returns new {@link ImageIcon} with specified {@link Icon} painted on it and rotated 90 degrees counter-clockwise.
     *
     * @param icon {@link Icon} to rotate
     * @return new {@link ImageIcon} with specified {@link Icon} painted on it and rotated 90 degrees counter-clockwise.
     */
    @NotNull
    public static ImageIcon rotateIcon90CCW ( @NotNull final Icon icon )
    {
        return new ImageIcon ( rotateImage90CCW ( toBufferedImage ( icon ) ) );
    }

    /**
     * Returns new {@link BufferedImage} with specified {@link Image} painted on it and rotated 90 degrees counter-clockwise.
     *
     * @param image {@link Image} to rotate
     * @return new {@link BufferedImage} with specified {@link Image} painted on it and rotated 90 degrees counter-clockwise.
     */
    @NotNull
    public static BufferedImage rotateImage90CCW ( @NotNull final Image image )
    {
        return rotateImage90CCW ( toNonNullBufferedImage ( image ) );
    }

    /**
     * Returns new {@link BufferedImage} with specified {@link BufferedImage} painted on it and rotated 90 degrees counter-clockwise.
     *
     * @param bufferedImage {@link BufferedImage} to rotate
     * @return new {@link BufferedImage} with specified {@link BufferedImage} painted on it and rotated 90 degrees counter-clockwise.
     */
    @NotNull
    public static BufferedImage rotateImage90CCW ( @NotNull final BufferedImage bufferedImage )
    {
        final BufferedImage rotatedImage = createCompatibleImage ( bufferedImage );
        final Graphics2D g2d = rotatedImage.createGraphics ();
        g2d.translate ( 0, bufferedImage.getWidth () );
        g2d.rotate ( -Math.PI / 2 );
        g2d.drawImage ( bufferedImage, 0, 0, null );
        g2d.dispose ();
        return rotatedImage;
    }

    /**
     * Returns new {@link ImageIcon} with specified {@link Icon} painted on it and rotated 180 degrees.
     *
     * @param icon {@link Icon} to rotate
     * @return new {@link ImageIcon} with specified {@link Icon} painted on it and rotated 180 degrees.
     */
    @NotNull
    public static ImageIcon rotateIcon180 ( @NotNull final Icon icon )
    {
        return new ImageIcon ( rotateImage180 ( toBufferedImage ( icon ) ) );
    }

    /**
     * Returns new {@link BufferedImage} with specified {@link Image} painted on it and rotated 180 degrees.
     *
     * @param image {@link Image} to rotate
     * @return new {@link BufferedImage} with specified {@link Image} painted on it and rotated 180 degrees.
     */
    @NotNull
    public static BufferedImage rotateImage180 ( @NotNull final Image image )
    {
        return rotateImage180 ( toNonNullBufferedImage ( image ) );
    }

    /**
     * Returns new {@link BufferedImage} with specified {@link BufferedImage} painted on it and rotated 180 degrees.
     *
     * @param bufferedImage {@link BufferedImage} to rotate
     * @return new {@link BufferedImage} with specified {@link BufferedImage} painted on it and rotated 180 degrees.
     */
    @NotNull
    public static BufferedImage rotateImage180 ( @NotNull final BufferedImage bufferedImage )
    {
        final BufferedImage rotatedImage = createCompatibleImage ( bufferedImage );
        final Graphics2D g2d = rotatedImage.createGraphics ();
        g2d.translate ( bufferedImage.getWidth (), bufferedImage.getHeight () );
        g2d.rotate ( Math.PI );
        g2d.drawImage ( bufferedImage, 0, 0, null );
        g2d.dispose ();
        return rotatedImage;
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
    @NotNull
    public static BufferedImage createShadowImage ( final int width, final int height, @NotNull final Shape shape, final int shadowWidth,
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
    @NotNull
    public static BufferedImage createInnerShadowImage ( final int width, @NotNull final Shape shape, final int shadowWidth,
                                                         final float opacity )
    {
        // Creating template image
        final BufferedImage bi = createCompatibleImage ( width, width, Transparency.TRANSLUCENT );
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
     * Returns {@link BufferedImage} decoded from Base64 string.
     *
     * @param imageData image data decode
     * @return {@link BufferedImage} decoded from Base64 string
     */
    @Nullable
    public static BufferedImage decodeImage ( @Nullable final String imageData )
    {
        BufferedImage image = null;
        if ( TextUtils.notEmpty ( imageData ) )
        {
            final String rawImageData = EncryptionUtils.base64decode ( imageData );
            if ( rawImageData != null )
            {
                final byte[] bytes = rawImageData.getBytes ();
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
            }
        }
        return image;
    }

    /**
     * Returns image data encoded in Base64 string.
     *
     * @param image image to encode
     * @return image data encoded in Base64 string
     */
    @Nullable
    public static String encodeImage ( @Nullable final BufferedImage image )
    {
        String imageString = null;
        if ( image != null )
        {
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
        }
        return imageString;
    }
}