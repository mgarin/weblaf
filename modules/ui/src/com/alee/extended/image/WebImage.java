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

import com.alee.extended.WebComponent;
import com.alee.laf.WebLookAndFeel;
import com.alee.managers.log.Log;
import com.alee.managers.style.StyleId;
import com.alee.managers.style.StyleableComponent;
import com.alee.utils.ImageUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.net.URL;

/**
 * This component allows you to display images in many different ways.
 * This component uses less resources than a label and has a few optimization.
 * <p/>
 * This component should never be used with a non-Web UIs as it might cause an unexpected behavior.
 * You could still use that component even if WebLaF is not your application L&amp;F as this component will use Web-UI in any case.
 *
 * @author Mikle Garin
 * @see WebComponent
 * @see WebImageUI
 * @see ImagePainter
 */

public class WebImage extends WebComponent<WebImage,WImageUI> implements SwingConstants
{
    /**
     * todo 1. Properly handle passed icons so they are not always saved to static raster image (issue with SvgIcon)
     */

    /**
     * Component properties.
     */
    public static final String IMAGE_PROPERTY = "image";
    public static final String DISPLAY_TYPE_PROPERTY = "displayType";
    public static final String HORIZONTAL_ALIGNMENT_PROPERTY = WebLookAndFeel.HORIZONTAL_ALIGNMENT_PROPERTY;
    public static final String VERTICAL_ALIGNMENT_PROPERTY = WebLookAndFeel.VERTICAL_ALIGNMENT_PROPERTY;
    public static final String OPACITY_PROPERTY = WebLookAndFeel.OPACITY_PROPERTY;

    /**
     * Displayed image.
     */
    private BufferedImage image;

    /**
     * Determines how exactly image should be displayed within component bounds.
     */
    private DisplayType displayType;

    /**
     * Image horizontal alignment.
     * Doesn't affect anything in case fitComponent display type is used.
     */
    private int horizontalAlignment;

    /**
     * Image vertical alignment.
     * Doesn't affect anything in case fitComponent display type is used.
     */
    private int verticalAlignment;

    /**
     * Image opacity.
     */
    private float opacity;

    /**
     * Constructs an empty image component.
     */
    public WebImage ()
    {
        this ( StyleId.auto );
    }

    /**
     * Constructs component with an image loaded from the specified path.
     *
     * @param src path to image
     */
    public WebImage ( final String src )
    {
        this ( StyleId.auto, src );
    }

    /**
     * Constructs component with an image loaded from package near specified class.
     *
     * @param nearClass class near which image is located
     * @param src       image file location
     */
    public WebImage ( final Class nearClass, final String src )
    {
        this ( StyleId.auto, nearClass, src );
    }

    /**
     * Constructs component with an image loaded from the specified url.
     *
     * @param url image url
     */
    public WebImage ( final URL url )
    {
        this ( StyleId.auto, url );
    }

    /**
     * Constructs component with an image retrieved from the specified icon.
     *
     * @param icon icon to process
     */
    public WebImage ( final Icon icon )
    {
        this ( StyleId.auto, icon );
    }

    /**
     * Constructs component with an image retrieved from the specified image icon.
     *
     * @param icon image icon to process
     */
    public WebImage ( final ImageIcon icon )
    {
        this ( StyleId.auto, icon );
    }

    /**
     * Constructs component with a specified image.
     *
     * @param image image
     */
    public WebImage ( final Image image )
    {
        this ( StyleId.auto, image );
    }

    /**
     * Constructs component with a specified image.
     *
     * @param image image
     */
    public WebImage ( final BufferedImage image )
    {
        this ( StyleId.auto, image );
    }

    /**
     * Constructs an empty image component.
     *
     * @param id style ID
     */
    public WebImage ( final StyleId id )
    {
        this ( id, ( Image ) null );
    }

    /**
     * Constructs component with an image loaded from the specified path.
     *
     * @param id  style ID
     * @param src path to image
     */
    public WebImage ( final StyleId id, final String src )
    {
        this ( id, ImageUtils.loadImage ( src ) );
    }

    /**
     * Constructs component with an image loaded from package near specified class.
     *
     * @param id        style ID
     * @param nearClass class near which image is located
     * @param src       image file location
     */
    public WebImage ( final StyleId id, final Class nearClass, final String src )
    {
        this ( id, ImageUtils.loadImage ( nearClass, src ) );
    }

    /**
     * Constructs component with an image loaded from the specified url.
     *
     * @param id  style ID
     * @param url image url
     */
    public WebImage ( final StyleId id, final URL url )
    {
        this ( id, ImageUtils.loadImage ( url ) );
    }

    /**
     * Constructs component with an image retrieved from the specified icon.
     *
     * @param id   style ID
     * @param icon icon to process
     */
    public WebImage ( final StyleId id, final Icon icon )
    {
        this ( id, ImageUtils.getBufferedImage ( icon ) );
    }

    /**
     * Constructs component with an image retrieved from the specified image icon.
     *
     * @param id   style ID
     * @param icon image icon to process
     */
    public WebImage ( final StyleId id, final ImageIcon icon )
    {
        this ( id, icon.getImage () );
    }

    /**
     * Constructs component with a specified image.
     *
     * @param id    style ID
     * @param image image
     */
    public WebImage ( final StyleId id, final Image image )
    {
        this ( id, ImageUtils.getBufferedImage ( image ) );
    }

    /**
     * Constructs component with a specified image.
     *
     * @param id    style ID
     * @param image image
     */
    public WebImage ( final StyleId id, final BufferedImage image )
    {
        super ();
        initialize ( id, image );
    }

    /**
     * Initializes image component.
     *
     * @param id    style ID
     * @param image initially displayed image
     */
    protected void initialize ( final StyleId id, final BufferedImage image )
    {
        setImage ( image );
        setOpacity ( 1f );
        setDisplayType ( DisplayType.preferred );
        setHorizontalAlignment ( CENTER );
        setVerticalAlignment ( CENTER );
        updateUI ();
        setStyleId ( id );
    }

    @Override
    public StyleId getDefaultStyleId ()
    {
        return StyleId.image;
    }

    /**
     * Returns current image.
     *
     * @return image
     */
    public BufferedImage getImage ()
    {
        return image;
    }

    /**
     * Changes image to new one taken from specified icon.
     *
     * @param icon icon to process
     * @return this image component
     */
    public WebImage setImage ( final Icon icon )
    {
        return setImage ( ImageUtils.getBufferedImage ( icon ) );
    }

    /**
     * Changes image to new one taken from specified image icon.
     *
     * @param icon image icon to process
     * @return this image component
     */
    public WebImage setImage ( final ImageIcon icon )
    {
        return setImage ( icon.getImage () );
    }

    /**
     * Changes image to the specified one.
     *
     * @param image new image
     * @return this image component
     */
    public WebImage setImage ( final Image image )
    {
        return setImage ( ImageUtils.getBufferedImage ( image ) );
    }

    /**
     * Changes image to the specified one.
     *
     * @param image new image
     * @return this image component
     */
    public WebImage setImage ( final BufferedImage image )
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
    public WebImage setDisplayType ( final DisplayType displayType )
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
    public WebImage setOpacity ( final float opacity )
    {
        final float old = this.opacity;
        this.opacity = opacity;
        firePropertyChange ( OPACITY_PROPERTY, old, opacity );
        return this;
    }

    /**
     * Returns the look and feel (L&amp;F) object that renders this component.
     *
     * @return the {@link WImageUI} object that renders this component
     */
    public WImageUI getUI ()
    {
        return ( WImageUI ) ui;
    }

    /**
     * Sets the L&amp;F object that renders this component.
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
        if ( getUI () == null || !( getUI () instanceof WImageUI ) )
        {
            try
            {
                setUI ( ( WImageUI ) UIManager.getUI ( this ) );
            }
            catch ( final Throwable e )
            {
                Log.error ( this, e );
                setUI ( new WebImageUI () );
            }
        }
        else
        {
            setUI ( getUI () );
        }
    }

    @Override
    public String getUIClassID ()
    {
        return StyleableComponent.image.getUIClassID ();
    }
}