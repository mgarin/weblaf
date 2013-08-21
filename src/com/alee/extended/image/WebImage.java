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

import com.alee.laf.WebLookAndFeel;
import com.alee.utils.ImageUtils;
import com.alee.utils.LafUtils;
import com.alee.utils.SwingUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.net.URL;

/**
 * This component allows you to display images in many different ways.
 * This component uses less resources than a label and has a few optimization.
 *
 * @author Mikle Garin
 */

public class WebImage extends JComponent implements SwingConstants
{
    /**
     * Image source.
     */
    private BufferedImage image;

    /**
     * Cached disabled image version.
     */
    private BufferedImage disabledImage;

    /**
     * How image should be displayed.
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
     * Image transparency.
     */
    private float transparency;

    /**
     * Image margins.
     */
    private Insets margin;

    /**
     * Last cached image size.
     * This is used to determine when image component was resized since last paint call.
     */
    private Dimension lastDimention = null;

    /**
     * Last cached image preview.
     * This variable is used when actual painted image is smaller than source image.
     * In that case source image is getting scaled and saved into this variable.
     */
    private BufferedImage lastPreviewImage = null;

    /**
     * Constructs an empty image component.
     */
    public WebImage ()
    {
        this ( ( Image ) null );
    }

    /**
     * Constructs component with an image loaded from the specified path.
     *
     * @param src path to image
     */
    public WebImage ( String src )
    {
        this ( ImageUtils.loadImage ( src ) );
    }

    /**
     * Constructs component with an image loaded from package near specified class.
     *
     * @param nearClass class near which image is located
     * @param src       image file location
     */
    public WebImage ( Class nearClass, String src )
    {
        this ( ImageUtils.loadImage ( nearClass, src ) );
    }

    /**
     * Constructs component with an image loaded from the specified url.
     *
     * @param url image url
     */
    public WebImage ( URL url )
    {
        this ( ImageUtils.loadImage ( url ) );
    }

    /**
     * Constructs component with an image retrieved from the specified icon.
     *
     * @param icon icon to process
     */
    public WebImage ( Icon icon )
    {
        this ( ImageUtils.getBufferedImage ( icon ) );
    }

    /**
     * Constructs component with an image retrieved from the specified image icon.
     *
     * @param icon image icon to process
     */
    public WebImage ( ImageIcon icon )
    {
        this ( icon.getImage () );
    }

    /**
     * Constructs component with a specified image.
     *
     * @param image image
     */
    public WebImage ( Image image )
    {
        this ( ImageUtils.getBufferedImage ( image ) );
    }

    /**
     * Constructs component with a specified image.
     *
     * @param image image
     */
    public WebImage ( BufferedImage image )
    {
        super ();

        this.image = image;
        this.disabledImage = null;

        this.displayType = DisplayType.preferred;
        this.horizontalAlignment = CENTER;
        this.verticalAlignment = CENTER;
        this.transparency = 1f;

        SwingUtils.setOrientation ( this );
        setOpaque ( false );

        addPropertyChangeListener ( WebLookAndFeel.COMPONENT_ENABLED_PROPERTY, new PropertyChangeListener ()
        {
            @Override
            public void propertyChange ( PropertyChangeEvent evt )
            {
                if ( !isEnabled () )
                {
                    disabledImage = ImageUtils.createDisabledCopy ( WebImage.this.image );
                    lastPreviewImage = null;
                    repaint ();
                }
                else if ( disabledImage != null )
                {
                    disabledImage.flush ();
                    disabledImage = null;
                    lastPreviewImage = null;
                    repaint ();
                }
            }
        } );
    }

    /**
     * Returns image width or -1 if image was not set.
     *
     * @return image width or -1 if image was not set
     */
    public int getImageWidth ()
    {
        return image != null ? image.getWidth () : -1;
    }

    /**
     * Returns image height or -1 if image was not set.
     *
     * @return image height or -1 if image was not set
     */
    public int getImageHeight ()
    {
        return image != null ? image.getHeight () : -1;
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
     */
    public void setIcon ( Icon icon )
    {
        setImage ( ImageUtils.getBufferedImage ( icon ) );
    }

    /**
     * Changes image to new one taken from specified image icon.
     *
     * @param icon image icon to process
     */
    public void setIcon ( ImageIcon icon )
    {
        setImage ( icon.getImage () );
    }

    /**
     * Changes image to the specified one.
     *
     * @param image new image
     */
    public void setImage ( Image image )
    {
        setImage ( ImageUtils.getBufferedImage ( image ) );
    }

    /**
     * Changes image to the specified one.
     *
     * @param image new image
     */
    public void setImage ( BufferedImage image )
    {
        this.image = image;
        revalidate ();
        repaint ();
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
     */
    public void setDisplayType ( DisplayType displayType )
    {
        this.displayType = displayType;
        updateView ();
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
     */
    public void setHorizontalAlignment ( int horizontalAlignment )
    {
        this.horizontalAlignment = horizontalAlignment;
        updateView ();
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
     */
    public void setVerticalAlignment ( int verticalAlignment )
    {
        this.verticalAlignment = verticalAlignment;
        updateView ();
    }

    /**
     * Returns image transparency.
     *
     * @return image transparency
     */
    public float getTransparency ()
    {
        return transparency;
    }

    /**
     * Changes image transparency.
     *
     * @param transparency new image transparency
     */
    public void setTransparency ( float transparency )
    {
        this.transparency = transparency;
        updateView ();
    }

    /**
     * Updates image component view.
     */
    private void updateView ()
    {
        if ( isShowing () )
        {
            repaint ();
        }
    }

    /**
     * Returns image margin.
     *
     * @return image margin
     */
    public Insets getMargin ()
    {
        return margin;
    }

    /**
     * Changes image margin.
     *
     * @param margin new image margin
     */
    public void setMargin ( Insets margin )
    {
        this.margin = margin;
        updateBorder ();
    }

    /**
     * Changes image margin.
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
     * Changes image margin.
     *
     * @param spacing side spacing
     */
    public void setMargin ( int spacing )
    {
        setMargin ( spacing, spacing, spacing, spacing );
    }

    /**
     * Updates image component border.
     */
    private void updateBorder ()
    {
        if ( margin != null )
        {
            setBorder ( BorderFactory.createEmptyBorder ( margin.top, margin.left, margin.bottom, margin.right ) );
        }
        else
        {
            setBorder ( null );
        }
    }

    /**
     * Paints image component.
     *
     * @param g graphics
     */
    @Override
    protected void paintComponent ( Graphics g )
    {
        super.paintComponent ( g );

        if ( transparency <= 0f )
        {
            return;
        }

        Graphics2D g2d = ( Graphics2D ) g;
        Composite oc = LafUtils.setupAlphaComposite ( g2d, transparency, transparency < 1f );

        // todo Optimize for repaint (check if image is out of repainted/clipped bounds)
        BufferedImage currentImage = getCurrentImage ();
        if ( currentImage != null )
        {
            Insets insets = getInsets ();
            if ( getSize ().equals ( getRequiredSize () ) )
            {
                // Drawing image when it is currently at preferred size
                g2d.drawImage ( currentImage, insets.left, insets.top, null );
            }
            else
            {
                switch ( displayType )
                {
                    case preferred:
                    {
                        // Drawing preferred sized image at specified side
                        int x = horizontalAlignment == LEFT ? insets.left :
                                ( horizontalAlignment == RIGHT ? getWidth () - currentImage.getWidth () - insets.right :
                                        getCenterX ( insets ) - currentImage.getWidth () / 2 );
                        int y = verticalAlignment == TOP ? insets.top :
                                ( verticalAlignment == BOTTOM ? getHeight () - currentImage.getHeight () - insets.bottom :
                                        getCenterY ( insets ) - currentImage.getHeight () / 2 );
                        g2d.drawImage ( currentImage, x, y, null );
                        break;
                    }
                    case fitComponent:
                    {
                        // Drawing sized to fit object image
                        BufferedImage preview = getPreviewImage ( insets );
                        g2d.drawImage ( preview, getCenterX ( insets ) - preview.getWidth () / 2,
                                getCenterY ( insets ) - preview.getHeight () / 2, null );
                        break;
                    }
                    case repeat:
                    {
                        // Drawing repeated in background image
                        int x = horizontalAlignment == LEFT ? insets.left :
                                ( horizontalAlignment == RIGHT ? getWidth () - currentImage.getWidth () - insets.right :
                                        getCenterX ( insets ) - currentImage.getWidth () / 2 );
                        int y = verticalAlignment == TOP ? insets.top :
                                ( verticalAlignment == BOTTOM ? getHeight () - currentImage.getHeight () - insets.bottom :
                                        getCenterY ( insets ) - currentImage.getHeight () / 2 );
                        g2d.setPaint ( new TexturePaint ( currentImage,
                                new Rectangle2D.Double ( x, y, currentImage.getWidth (), currentImage.getHeight () ) ) );
                        g2d.fillRect ( insets.left, insets.top, getWidth () - insets.left - insets.right,
                                getHeight () - insets.top - insets.bottom );
                        break;
                    }
                }
            }
        }

        LafUtils.restoreComposite ( g2d, oc, transparency < 1f );
    }

    /**
     * Returns image component center X coordinate.
     *
     * @param insets image component insets
     * @return image component center X coordinate
     */
    private int getCenterX ( Insets insets )
    {
        return insets.left + ( getWidth () - insets.left - insets.right ) / 2;
    }

    /**
     * Returns image component center Y coordinate.
     *
     * @param insets image component insets
     * @return image component center Y coordinate
     */
    private int getCenterY ( Insets insets )
    {
        return insets.top + ( getHeight () - insets.top - insets.bottom ) / 2;
    }

    /**
     * Returns preview image for specified insets.
     *
     * @param insets image component insets
     * @return preview image
     */
    private BufferedImage getPreviewImage ( Insets insets )
    {
        if ( image.getWidth () > getWidth () || image.getHeight () > getHeight () )
        {
            Dimension size = getSize ();
            size.setSize ( size.width - insets.left - insets.right, size.height - insets.top - insets.bottom );
            if ( lastPreviewImage == null || lastDimention != null && !lastDimention.equals ( size ) )
            {
                if ( lastPreviewImage != null )
                {
                    lastPreviewImage.flush ();
                    lastPreviewImage = null;
                }
                lastPreviewImage = ImageUtils.createPreviewImage ( getCurrentImage (), size );
                lastDimention = getSize ();
            }
            return lastPreviewImage;
        }
        else
        {
            return image;
        }
    }

    /**
     * Returns currently displayed image.
     *
     * @return currently displayed image
     */
    private BufferedImage getCurrentImage ()
    {
        return !isEnabled () && disabledImage != null ? disabledImage : image;
    }

    /**
     * Returns preferred size of image component.
     *
     * @return preferred size of image component
     */
    @Override
    public Dimension getPreferredSize ()
    {
        if ( isPreferredSizeSet () )
        {
            return super.getPreferredSize ();
        }
        else
        {
            return getRequiredSize ();
        }
    }

    /**
     * Returns component size required to fully show the image.
     *
     * @return component size required to fully show the image
     */
    private Dimension getRequiredSize ()
    {
        Insets insets = getInsets ();
        return new Dimension ( insets.left + ( image != null ? image.getWidth () : 0 ) + insets.right,
                insets.top + ( image != null ? image.getHeight () : 0 ) + insets.bottom );
    }
}