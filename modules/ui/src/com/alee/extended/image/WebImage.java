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
import com.alee.api.resource.Resource;
import com.alee.extended.WebComponent;
import com.alee.managers.style.StyleId;
import com.alee.managers.style.StyleManager;
import com.alee.utils.ImageUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.net.URL;

/**
 * This component allows you to display images in many different ways.
 * This component uses less resources than a label and has a few optimization.
 *
 * This component should never be used with a non-Web UIs as it might cause an unexpected behavior.
 * You could still use that component even if WebLaF is not your application LaF as this component will use Web-UI in any case.
 *
 * @author Mikle Garin
 * @see ImageDescriptor
 * @see WImageUI
 * @see WebImageUI
 * @see IImagePainter
 * @see ImagePainter
 * @see WebComponent
 */
public class WebImage extends WebComponent<WebImage, WImageUI> implements SwingConstants
{
    /**
     * todo 1. Properly handle passed icons so they are not always saved to static raster image since that causes issues with SvgIcon
     * todo    Probably make a custom shell for the passed image that has all APIs necessary for displaying image?
     */

    /**
     * Component properties.
     */
    public static final String IMAGE_PROPERTY = "image";
    public static final String DISPLAY_TYPE_PROPERTY = "displayType";
    public static final String HORIZONTAL_ALIGNMENT_PROPERTY = "horizontalAlignment";
    public static final String VERTICAL_ALIGNMENT_PROPERTY = "verticalAlignment";
    public static final String OPACITY_PROPERTY = "opacity";

    /**
     * Displayed image.
     */
    @Nullable
    @SuppressWarnings ( "NonSerializableFieldInSerializableClass" )
    protected BufferedImage image;

    /**
     * Determines how exactly image should be displayed within component bounds.
     */
    @NotNull
    protected DisplayType displayType;

    /**
     * Image horizontal alignment.
     * Doesn't affect anything in case fitComponent display type is used.
     */
    protected int horizontalAlignment;

    /**
     * Image vertical alignment.
     * Doesn't affect anything in case fitComponent display type is used.
     */
    protected int verticalAlignment;

    /**
     * Image opacity.
     */
    protected float opacity;

    /**
     * Constructs an empty image component.
     */
    public WebImage ()
    {
        this ( StyleId.auto );
    }

    /**
     * Constructs component with an image loaded from the specified {@link URL}.
     *
     * @param resource image {@link Resource} to load
     */
    public WebImage ( @NotNull final Resource resource )
    {
        this ( StyleId.auto, resource );
    }

    /**
     * Constructs component with an image retrieved from the specified {@link Icon}.
     *
     * @param icon {@link Icon} to display
     */
    public WebImage ( @Nullable final Icon icon )
    {
        this ( StyleId.auto, icon );
    }

    /**
     * Constructs component with a specified {@link Image}.
     *
     * @param image {@link Image} to display
     */
    public WebImage ( @Nullable final Image image )
    {
        this ( StyleId.auto, image );
    }

    /**
     * Constructs component with a specified {@link RenderedImage}.
     *
     * @param image {@link RenderedImage} to display
     */
    public WebImage ( @Nullable final RenderedImage image )
    {
        this ( StyleId.auto, image );
    }

    /**
     * Constructs component with a specified {@link BufferedImage}.
     *
     * @param image {@link BufferedImage} to display
     */
    public WebImage ( @Nullable final BufferedImage image )
    {
        this ( StyleId.auto, image );
    }

    /**
     * Constructs an empty image component.
     *
     * @param id {@link StyleId}
     */
    public WebImage ( @NotNull final StyleId id )
    {
        this ( id, ( BufferedImage ) null );
    }

    /**
     * Constructs component with an image loaded from the specified path.
     *
     * @param id       {@link StyleId}
     * @param resource image {@link Resource} to load
     */
    public WebImage ( @NotNull final StyleId id, @NotNull final Resource resource )
    {
        this ( id, ImageUtils.loadBufferedImage ( resource ) );
    }

    /**
     * Constructs component with an image retrieved from the specified {@link Icon}.
     *
     * @param id   {@link StyleId}
     * @param icon {@link Icon} to display
     */
    public WebImage ( @NotNull final StyleId id, @Nullable final Icon icon )
    {
        this ( id, ImageUtils.toBufferedImage ( icon ) );
    }

    /**
     * Constructs component with a specified {@link Image}.
     *
     * @param id    {@link StyleId}
     * @param image {@link Image} to display
     */
    public WebImage ( @NotNull final StyleId id, @Nullable final Image image )
    {
        this ( id, ImageUtils.toBufferedImage ( image ) );
    }

    /**
     * Constructs component with a specified {@link RenderedImage}.
     *
     * @param id    {@link StyleId}
     * @param image {@link RenderedImage} to display
     */
    public WebImage ( @NotNull final StyleId id, @Nullable final RenderedImage image )
    {
        this ( id, ImageUtils.toBufferedImage ( image ) );
    }

    /**
     * Constructs component with a specified {@link BufferedImage}.
     *
     * @param id    {@link StyleId}
     * @param image {@link BufferedImage} to display
     */
    public WebImage ( @NotNull final StyleId id, @Nullable final BufferedImage image )
    {
        super ();
        this.displayType = DisplayType.preferred;
        this.horizontalAlignment = CENTER;
        this.verticalAlignment = CENTER;
        this.image = image;
        updateUI ();
        setStyleId ( id );
    }

    @NotNull
    @Override
    public StyleId getDefaultStyleId ()
    {
        return StyleId.image;
    }

    /**
     * Returns currently displayed {@link BufferedImage}.
     *
     * @return currently displayed {@link BufferedImage}
     */
    @Nullable
    public BufferedImage getImage ()
    {
        return image;
    }

    /**
     * Changes displayed image to the specified {@link Icon}.
     *
     * @param icon {@link Icon} to display
     * @return this image component
     */
    @Nullable
    public WebImage setImage ( @Nullable final Icon icon )
    {
        return setImage ( ImageUtils.toBufferedImage ( icon ) );
    }

    /**
     * Changes displayed image to the specified {@link ImageIcon}.
     *
     * @param icon {@link ImageIcon} to display
     * @return this image component
     */
    @Nullable
    public WebImage setImage ( @Nullable final ImageIcon icon )
    {
        return setImage ( ImageUtils.toBufferedImage ( icon ) );
    }

    /**
     * Changes displayed image to the specified {@link Image}.
     *
     * @param image {@link Image} to display
     * @return this image component
     */
    @Nullable
    public WebImage setImage ( @Nullable final Image image )
    {
        return setImage ( ImageUtils.toBufferedImage ( image ) );
    }

    /**
     * Changes displayed image to the specified {@link RenderedImage}.
     *
     * @param image {@link RenderedImage} to display
     * @return this image component
     */
    @Nullable
    public WebImage setImage ( @Nullable final RenderedImage image )
    {
        return setImage ( ImageUtils.toBufferedImage ( image ) );
    }

    /**
     * Changes displayed image to the specified {@link BufferedImage}.
     *
     * @param image {@link BufferedImage} to display
     * @return this image component
     */
    @Nullable
    public WebImage setImage ( @Nullable final BufferedImage image )
    {
        final BufferedImage old = this.image;
        this.image = image;
        firePropertyChange ( IMAGE_PROPERTY, old, image );
        return this;
    }

    /**
     * Returns image display type.
     *
     * @return image display type
     */
    @NotNull
    public DisplayType getDisplayType ()
    {
        return displayType;
    }

    /**
     * Changes image display type.
     *
     * @param displayType new image display type
     * @return this image component
     */
    @NotNull
    public WebImage setDisplayType ( @NotNull final DisplayType displayType )
    {
        final DisplayType old = this.displayType;
        this.displayType = displayType;
        firePropertyChange ( DISPLAY_TYPE_PROPERTY, old, displayType );
        return this;
    }

    /**
     * Returns image horizontal alignment.
     *
     * @return image horizontal alignment
     */
    public int getHorizontalAlignment ()
    {
        return horizontalAlignment;
    }

    /**
     * Changes image horizontal alignment to the specified one.
     *
     * @param horizontalAlignment new image horizontal alignment
     * @return this image component
     */
    @NotNull
    public WebImage setHorizontalAlignment ( final int horizontalAlignment )
    {
        final int old = this.horizontalAlignment;
        this.horizontalAlignment = horizontalAlignment;
        firePropertyChange ( HORIZONTAL_ALIGNMENT_PROPERTY, old, horizontalAlignment );
        return this;
    }

    /**
     * Returns image vertical alignment.
     *
     * @return image vertical alignment
     */
    public int getVerticalAlignment ()
    {
        return verticalAlignment;
    }

    /**
     * Changes image vertical alignment to the specified one.
     *
     * @param verticalAlignment new image vertical alignment
     * @return this image component
     */
    @NotNull
    public WebImage setVerticalAlignment ( final int verticalAlignment )
    {
        final int old = this.verticalAlignment;
        this.verticalAlignment = verticalAlignment;
        firePropertyChange ( VERTICAL_ALIGNMENT_PROPERTY, old, verticalAlignment );
        return this;
    }

    /**
     * Returns image opacity.
     *
     * @return image opacity
     */
    public float getOpacity ()
    {
        return opacity;
    }

    /**
     * Sets image opacity.
     *
     * @param opacity new image opacity
     * @return this image component
     */
    @NotNull
    public WebImage setOpacity ( final float opacity )
    {
        final float old = this.opacity;
        this.opacity = opacity;
        firePropertyChange ( OPACITY_PROPERTY, old, opacity );
        return this;
    }

    /**
     * Returns the look and feel (LaF) object that renders this component.
     *
     * @return the {@link WImageUI} object that renders this component
     */
    public WImageUI getUI ()
    {
        return ( WImageUI ) ui;
    }

    /**
     * Sets the LaF object that renders this component.
     *
     * @param ui {@link WImageUI}
     */
    public void setUI ( final WImageUI ui )
    {
        super.setUI ( ui );
    }

    @Override
    public void updateUI ()
    {
        StyleManager.getDescriptor ( this ).updateUI ( this );
    }

    @NotNull
    @Override
    public String getUIClassID ()
    {
        return StyleManager.getDescriptor ( this ).getUIClassId ();
    }
}