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

package com.alee.extended.image;

import com.alee.api.annotations.NotNull;
import com.alee.api.annotations.Nullable;
import com.alee.api.jdk.Objects;
import com.alee.laf.WebLookAndFeel;
import com.alee.painter.decoration.AbstractDecorationPainter;
import com.alee.painter.decoration.IDecoration;
import com.alee.utils.GraphicsUtils;
import com.alee.utils.ImageUtils;
import com.alee.utils.SwingUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

/**
 * Basic painter for {@link WebImage} component.
 * It is used as {@link WImageUI} default painter.
 *
 * @param <C> component type
 * @param <U> component UI type
 * @param <D> decoration type
 * @author Mikle Garin
 */
public class ImagePainter<C extends WebImage, U extends WImageUI, D extends IDecoration<C, D>> extends AbstractDecorationPainter<C, U, D>
        implements IImagePainter<C, U>
{
    /**
     * todo 1. Move image painting into IContent or IBackground implementation
     */

    /**
     * Last cached image size.
     * This is used to determine when image component was resized since last paint call.
     */
    protected transient Dimension lastDimension = null;

    /**
     * Disabled image version cached for performance reasons.
     * This is a disabled version of full image, not its preview.
     */
    protected transient BufferedImage disabledImage;

    /**
     * Last cached image preview.
     * This variable is used when actual painted image is smaller than source image.
     * In that case source image is getting scaled and saved into this variable.
     */
    protected transient BufferedImage lastPreviewImage = null;

    @Override
    protected void uninstallPropertiesAndListeners ()
    {
        uninstallRuntimeVariables ();
        super.uninstallPropertiesAndListeners ();
    }

    @Override
    protected void propertyChanged ( @NotNull final String property, @Nullable final Object oldValue, @Nullable final Object newValue )
    {
        // Perform basic actions on property changes
        super.propertyChanged ( property, oldValue, newValue );

        // Updating visual settings
        if ( Objects.equals ( property, WebLookAndFeel.ENABLED_PROPERTY ) )
        {
            // Updating disabled state image
            if ( !isEnabled () )
            {
                calculateDisabledImage ();
            }
            else
            {
                clearDisabledImage ();
            }
            revalidate ();
            repaint ();
        }
        else if ( Objects.equals ( property, WebImage.IMAGE_PROPERTY ) )
        {
            this.disabledImage = null;
            this.lastPreviewImage = null;
            if ( !isEnabled () )
            {
                calculateDisabledImage ();
            }
            revalidate ();
            repaint ();
        }
        else if ( Objects.equals ( property, WebImage.DISPLAY_TYPE_PROPERTY, WebImage.HORIZONTAL_ALIGNMENT_PROPERTY,
                WebImage.VERTICAL_ALIGNMENT_PROPERTY, WebImage.OPACITY_PROPERTY ) )
        {
            repaint ();
        }
    }

    /**
     * Performs runtime variables cleanup.
     */
    protected void uninstallRuntimeVariables ()
    {
        lastPreviewImage = null;
        disabledImage = null;
        lastDimension = null;
    }

    /**
     * Updates cached disabled image.
     */
    protected void calculateDisabledImage ()
    {
        final BufferedImage image = component.getImage ();
        disabledImage = image != null ? ImageUtils.createDisabledCopy ( image ) : null;
        lastPreviewImage = null;
    }

    /**
     * Clears cached disabled image
     */
    protected void clearDisabledImage ()
    {
        if ( disabledImage != null )
        {
            disabledImage.flush ();
            disabledImage = null;
        }
        lastPreviewImage = null;
    }

    @Override
    protected void paintContent ( @NotNull final Graphics2D g2d, @NotNull final C c, @NotNull final U ui, @NotNull final Rectangle bounds )
    {
        final float opacity = component.getOpacity ();
        if ( opacity > 0f )
        {
            // todo Optimize for repaint (check if image is out of repainted/clipped bounds)
            final BufferedImage currentImage = getCurrentImage ();
            if ( currentImage != null )
            {
                final Composite oc = GraphicsUtils.setupAlphaComposite ( g2d, opacity, opacity < 1f );

                final Insets i = component.getInsets ();
                final Dimension size = component.getSize ();
                final Rectangle clip = new Rectangle ( i.left, i.top, size.width - i.left - i.right, size.height - i.top - i.bottom );
                final Shape ocl = GraphicsUtils.intersectClip ( g2d, clip );

                if ( c.getSize ().equals ( getPreferredSize () ) )
                {
                    // Drawing image when it is currently at preferred size
                    g2d.drawImage ( currentImage, i.left, i.top, null );
                }
                else
                {
                    final int horizontalAlignment = component.getHorizontalAlignment ();
                    final int verticalAlignment = component.getVerticalAlignment ();
                    switch ( component.getDisplayType () )
                    {
                        case preferred:
                        {
                            // Drawing preferred sized image at specified side
                            final int x = horizontalAlignment == SwingConstants.LEFT ? i.left :
                                    horizontalAlignment == SwingConstants.RIGHT ? size.width - currentImage.getWidth () - i.right :
                                            getCenterX ( i ) - currentImage.getWidth () / 2;
                            final int y = verticalAlignment == SwingConstants.TOP ? i.top :
                                    verticalAlignment == SwingConstants.BOTTOM ? size.height - currentImage.getHeight () - i.bottom :
                                            getCenterY ( i ) - currentImage.getHeight () / 2;
                            g2d.drawImage ( currentImage, x, y, null );
                            break;
                        }
                        case fitComponent:
                        {
                            // Drawing sized to fit object image
                            final BufferedImage preview = getPreviewImage ( i );
                            g2d.drawImage ( preview, getCenterX ( i ) - preview.getWidth () / 2,
                                    getCenterY ( i ) - preview.getHeight () / 2, null );
                            break;
                        }
                        case repeat:
                        {
                            // Drawing repeated in background image
                            final int x = horizontalAlignment == SwingConstants.LEFT ? i.left :
                                    horizontalAlignment == SwingConstants.RIGHT ? size.width - currentImage.getWidth () - i.right :
                                            getCenterX ( i ) - currentImage.getWidth () / 2;
                            final int y = verticalAlignment == SwingConstants.TOP ? i.top :
                                    verticalAlignment == SwingConstants.BOTTOM ? size.height - currentImage.getHeight () - i.bottom :
                                            getCenterY ( i ) - currentImage.getHeight () / 2;
                            g2d.setPaint ( new TexturePaint ( currentImage,
                                    new Rectangle2D.Double ( x, y, currentImage.getWidth (), currentImage.getHeight () ) ) );
                            g2d.fillRect ( i.left, i.top, size.width - i.left - i.right, size.height - i.top - i.bottom );
                            break;
                        }
                    }
                }

                GraphicsUtils.restoreClip ( g2d, ocl );
                GraphicsUtils.restoreComposite ( g2d, oc, opacity < 1f );
            }
        }
    }

    /**
     * Returns image component center X coordinate.
     *
     * @param insets image component insets
     * @return image component center X coordinate
     */
    protected int getCenterX ( @NotNull final Insets insets )
    {
        return insets.left + ( component.getWidth () - insets.left - insets.right ) / 2;
    }

    /**
     * Returns image component center Y coordinate.
     *
     * @param insets image component insets
     * @return image component center Y coordinate
     */
    protected int getCenterY ( @NotNull final Insets insets )
    {
        return insets.top + ( component.getHeight () - insets.top - insets.bottom ) / 2;
    }

    /**
     * Returns preview image for specified insets.
     *
     * @param insets image component insets
     * @return preview image
     */
    protected BufferedImage getPreviewImage ( @NotNull final Insets insets )
    {
        final BufferedImage preview;
        final BufferedImage image = component.getImage ();
        final Dimension size = SwingUtils.shrink ( component.getSize (), insets );
        if ( image.getWidth () > size.width || image.getHeight () > size.height )
        {
            if ( lastPreviewImage == null || lastDimension != null && !lastDimension.equals ( size ) )
            {
                if ( lastPreviewImage != null )
                {
                    lastPreviewImage.flush ();
                    lastPreviewImage = null;
                }
                lastPreviewImage = ImageUtils.createPreviewImage ( getCurrentImage (), size );
                lastDimension = size;
            }
            preview = lastPreviewImage;
        }
        else
        {
            preview = image;
        }
        return preview;
    }

    /**
     * Returns currently displayed image.
     *
     * @return currently displayed image
     */
    protected BufferedImage getCurrentImage ()
    {
        return !isEnabled () && disabledImage != null ? disabledImage : component.getImage ();
    }

    @NotNull
    @Override
    public Dimension getPreferredSize ()
    {
        final Insets insets = component.getInsets ();
        final BufferedImage image = component.getImage ();
        return new Dimension ( insets.left + ( image != null ? image.getWidth () : 0 ) + insets.right,
                insets.top + ( image != null ? image.getHeight () : 0 ) + insets.bottom );
    }
}