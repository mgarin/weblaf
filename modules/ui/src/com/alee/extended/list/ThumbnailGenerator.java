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

package com.alee.extended.list;

import com.alee.api.annotations.NotNull;
import com.alee.api.annotations.Nullable;
import com.alee.api.resource.FileResource;
import com.alee.managers.task.TaskManager;
import com.alee.utils.CoreSwingUtils;
import com.alee.utils.FileUtils;
import com.alee.utils.ImageUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * Utility class that allows asynchronous image thumbnails generation.
 *
 * @author Mikle Garin
 */
public class ThumbnailGenerator implements Runnable
{
    /**
     * todo 1. Make use of TaskManager for concurrency once it is ready
     * todo 2. Remove disabled thumbnail generation, instead it should be handled in renderer/on place
     */

    /**
     * Map containing references to running thumbnail generators.
     * It is used to abort specific generation when needed.
     */
    protected static final Map<FileElement, ThumbnailGenerator> generators = new WeakHashMap<FileElement, ThumbnailGenerator> ();

    /**
     * Generator references map operations lock.
     */
    protected static final Object generatorsLock = new Object ();

    /**
     * File list this generator is working for.
     */
    @NotNull
    protected final WebFileList list;

    /**
     * Element thumbnail is being generated for.
     */
    @NotNull
    protected final FileElement element;

    /**
     * Requested thumbnail size.
     */
    @NotNull
    protected final Dimension size;

    /**
     * Whether should generate disabled state thumbnail or not.
     */
    protected final boolean disabled;

    /**
     * Whether generation was aborted or not.
     */
    protected boolean aborted;

    /**
     * Constructs thumbnail generator for the specified file element.
     *
     * @param list     file list this generator is working for
     * @param element  element to queue thumbnail generation for
     * @param size     requested thumbnail size
     * @param disabled whether should generate disabled state thumbnail or not
     */
    public ThumbnailGenerator ( @NotNull final WebFileList list, @NotNull final FileElement element, @NotNull final Dimension size,
                                final boolean disabled )
    {
        this.list = list;
        this.element = element;
        this.size = size;
        this.disabled = disabled;
        this.aborted = false;
    }

    /**
     * Returns file list this generator is working for.
     *
     * @return file list this generator is working for
     */
    @NotNull
    public WebFileList getList ()
    {
        return list;
    }

    /**
     * Returns file element for which thumbnail is being generated.
     *
     * @return file element for which thumbnail is being generated
     */
    @NotNull
    public FileElement getElement ()
    {
        return element;
    }

    /**
     * Returns whether disabled thumbnail will also be generated or not.
     *
     * @return true if disabled thumbnail will also be generated, false otherwise
     */
    public boolean isDisabled ()
    {
        return disabled;
    }

    /**
     * Returns whether this generator was aborted or not.
     *
     * @return true if this generator was aborted, false otherwise
     */
    public boolean isAborted ()
    {
        return aborted;
    }

    /**
     * Abort thumbnail generation.
     * Note that actual abort might occur after long-running operations, but it will surely cancel thumbnail update.
     */
    public void abort ()
    {
        this.aborted = true;
    }

    /**
     * Starts thumbnail generation.
     */
    @Override
    public void run ()
    {
        // Process abort check here
        if ( !aborted )
        {
            // Creating thumbnail
            createThumbnail ( element.getFile (), list.isGenerateThumbnails () );

            // Process abort check here
            if ( !aborted )
            {
                // Updating list view
                // Repaint doesn't really require EDT but this is simply a dirty trick to invoke this call later
                CoreSwingUtils.invokeLater ( new Runnable ()
                {
                    @Override
                    public void run ()
                    {
                        list.repaint ( element );
                    }
                } );

                // Perform final cleanups
                cleanup ();
            }
            else
            {
                // Perform abort cleanups
                cleanup ();
            }
        }
        else
        {
            // Perform abort cleanups
            cleanup ();
        }
    }

    /**
     * Cleanup cached reference to this generator.
     * This is required mostly in cases when generator was not aborted.
     */
    protected void cleanup ()
    {
        synchronized ( generatorsLock )
        {
            // Perform cleanup only if current generator is still an active one for this element
            final ThumbnailGenerator generator = generators.get ( element );
            if ( generator == this )
            {
                // Updating thumbnail load state
                synchronized ( element.getLock () )
                {
                    element.setThumbnailQueued ( false );
                    element.setDisabledThumbnailQueued ( false );
                }

                // Removing element generator reference
                generators.remove ( element );
            }
        }
    }

    /**
     * Generates thumbnail for the specified file.
     *
     * @param file    file to generate thumbnail for
     * @param preview whether should create thumbnail
     */
    protected void createThumbnail ( @NotNull final File file, final boolean preview )
    {
        final FileThumbnailProvider thumbnailProvider = list.getThumbnailProvider ();
        if ( thumbnailProvider != null && thumbnailProvider.accept ( file ) )
        {
            // Using thumbnail provider to generate thumbnail icon
            final Icon thumb = element.getEnabledThumbnail () != null ? element.getEnabledThumbnail () :
                    thumbnailProvider.provide ( file, size, preview );
            if ( thumb != null )
            {
                // Applying custom thumbnail
                applyThumbnail ( thumb );
            }
            else
            {
                // Creating standard thumbnail
                createStandardThumbnail ( file, preview );
            }
        }
        else
        {
            // Creating standard thumbnail
            createStandardThumbnail ( file, preview );
        }
    }

    /**
     * Generates standard thumbnail for the specified {@link File}.
     *
     * @param file    {@link File} to generate standard thumbnail for
     * @param preview whether should create thumbnail for image from the specified {@link File}
     */
    protected void createStandardThumbnail ( @NotNull final File file, final boolean preview )
    {
        // Using either image thumbnails or default file extension icons
        if ( preview && ImageUtils.isImageSupported ( file.getName () ) )
        {
            // If thumbnail was already specified we should re-use it
            // It will save us a lot of time if we simply need to generate disabled state in addition to enabled one
            final Icon thumb = element.getEnabledThumbnail () != null ? element.getEnabledThumbnail () :
                    createThumbnailIcon ( file );
            if ( thumb != null )
            {
                // Applying standard image thumbnail
                applyThumbnail ( thumb );
            }
            else
            {
                // Applying standard extension icon
                applyStandardIcon ( file );
            }
        }
        else
        {
            // Applying standard extension icon
            applyStandardIcon ( file );
        }
    }

    /**
     * Returns thumbnail for image from the specified {@link File}.
     * Note that this method will generate {@link Icon} with a static image, so any GIF or other animated images will be static.
     *
     * @param file file to generate thumbnail for
     * @return thumbnail for image from the specified {@link File}
     */
    @Nullable
    protected Icon createThumbnailIcon ( @NotNull final File file )
    {
        Icon thumbnail;
        try
        {
            final BufferedImage bufferedImage = ImageUtils.loadBufferedImage ( new FileResource ( file ) );
            final BufferedImage previewImage = ImageUtils.createImageThumbnail ( bufferedImage, Math.min ( size.width, size.height ) );
            final ImageIcon previewIcon = new ImageIcon ( previewImage );
            previewIcon.setDescription ( previewImage.getWidth () + "x" + previewImage.getHeight () );
            thumbnail = previewIcon;
        }
        catch ( final Exception e )
        {
            thumbnail = null;
        }
        return thumbnail;
    }

    /**
     * Applies generated thumbnail to the element.
     *
     * @param thumbnail thumbnail icon
     */
    protected void applyThumbnail ( @NotNull final Icon thumbnail )
    {
        if ( !aborted )
        {
            // Applying custom generated thumbnail icon
            synchronized ( element.getLock () )
            {
                if ( element.isThumbnailQueued () )
                {
                    // We had to check that queue wasn't cancelled from outside
                    element.setEnabledThumbnail ( thumbnail );
                }
            }
            if ( !aborted && disabled )
            {
                // Re-using enabled state thumbnail to generate disabled state one
                final Icon disabledThumbnail = ImageUtils.createDisabledCopy ( thumbnail );
                if ( !aborted && element.isDisabledThumbnailQueued () )
                {
                    // We had to check that queue wasn't cancelled from outside
                    element.setDisabledThumbnail ( disabledThumbnail );
                }
            }
        }
    }

    /**
     * Applies standard icon to the element.
     *
     * @param file file to apply standard icon to
     */
    protected void applyStandardIcon ( @NotNull final File file )
    {
        if ( !aborted )
        {
            // Generating standard file extension icon
            final Icon enabledThumbnail = FileUtils.getStandardFileIcon ( file, true, true );
            if ( !aborted )
            {
                if ( element.isThumbnailQueued () )
                {
                    // We had to check that queue wasn't cancelled from outside
                    element.setEnabledThumbnail ( enabledThumbnail );
                }
                if ( !aborted && disabled )
                {
                    // Standard icons are cached so we don't need to re-use enabled icon for disabled state one generation
                    final Icon disabledThumbnail = FileUtils.getStandardFileIcon ( file, true, false );
                    if ( !aborted && element.isDisabledThumbnailQueued () )
                    {
                        // We had to check that queue wasn't cancelled from outside
                        element.setDisabledThumbnail ( disabledThumbnail );
                    }
                }
            }
        }
    }

    /**
     * Adds specified element into thumbnails generation queue.
     *
     * @param list     file list this generator is working for
     * @param element  element to queue thumbnail generation for
     * @param size     requested thumbnail size
     * @param disabled whether should generate disabled state thumbnail or not
     */
    public static void queueThumbnailLoad ( @NotNull final WebFileList list, @NotNull final FileElement element,
                                            @NotNull final Dimension size, final boolean disabled )
    {
        synchronized ( generatorsLock )
        {
            synchronized ( element.getLock () )
            {
                // Skip generation if it was already done or in queue
                if ( disabled ? !element.isDisabledThumbnailQueued () && element.getDisabledThumbnail () == null :
                        !element.isThumbnailQueued () && element.getEnabledThumbnail () == null )
                {
                    // Updating thumbnail load state
                    element.setThumbnailQueued ( true );
                    element.setDisabledThumbnailQueued ( disabled );

                    // Queueing thumbnail generation
                    final ThumbnailGenerator generator = new ThumbnailGenerator ( list, element, size, disabled );
                    generators.put ( element, generator );
                    TaskManager.execute ( TaskManager.COMPUTATION, generator );
                }
            }
        }
    }

    /**
     * Forces thumbnail generation to be aborted for the specified element.
     *
     * @param element element to abort thumbnail generation for
     */
    public static void abortThumbnailLoad ( @NotNull final FileElement element )
    {
        synchronized ( generatorsLock )
        {
            // Aborting generator
            final ThumbnailGenerator generator = generators.get ( element );
            if ( generator != null )
            {
                generator.abort ();
            }

            // Cleaning up previous thumbnails
            synchronized ( element.getLock () )
            {
                element.setEnabledThumbnail ( null );
                element.setThumbnailQueued ( false );
                element.setDisabledThumbnail ( null );
                element.setDisabledThumbnailQueued ( false );
            }
        }
    }
}