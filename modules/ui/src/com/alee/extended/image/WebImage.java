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
import com.alee.managers.hotkey.HotkeyData;
import com.alee.managers.language.data.TooltipWay;
import com.alee.managers.tooltip.ToolTipMethods;
import com.alee.managers.tooltip.TooltipManager;
import com.alee.managers.tooltip.WebCustomTooltip;
import com.alee.utils.*;
import com.alee.utils.laf.WebBorder;
import com.alee.utils.swing.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.KeyAdapter;
import java.awt.event.MouseAdapter;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.net.URL;
import java.util.List;

/**
 * This component allows you to display images in many different ways.
 * This component uses less resources than a label and has a few optimization.
 *
 * @author Mikle Garin
 */

public class WebImage extends JComponent implements EventMethods, ToolTipMethods, SwingConstants
{
    /**
     * todo 1. Provide separate UI and painter for this component
     */

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
     * Image opacity.
     */
    private float opacity;

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
    public WebImage ( final String src )
    {
        this ( ImageUtils.loadImage ( src ) );
    }

    /**
     * Constructs component with an image loaded from package near specified class.
     *
     * @param nearClass class near which image is located
     * @param src       image file location
     */
    public WebImage ( final Class nearClass, final String src )
    {
        this ( ImageUtils.loadImage ( nearClass, src ) );
    }

    /**
     * Constructs component with an image loaded from the specified url.
     *
     * @param url image url
     */
    public WebImage ( final URL url )
    {
        this ( ImageUtils.loadImage ( url ) );
    }

    /**
     * Constructs component with an image retrieved from the specified icon.
     *
     * @param icon icon to process
     */
    public WebImage ( final Icon icon )
    {
        this ( ImageUtils.getBufferedImage ( icon ) );
    }

    /**
     * Constructs component with an image retrieved from the specified image icon.
     *
     * @param icon image icon to process
     */
    public WebImage ( final ImageIcon icon )
    {
        this ( icon.getImage () );
    }

    /**
     * Constructs component with a specified image.
     *
     * @param image image
     */
    public WebImage ( final Image image )
    {
        this ( ImageUtils.getBufferedImage ( image ) );
    }

    /**
     * Constructs component with a specified image.
     *
     * @param image image
     */
    public WebImage ( final BufferedImage image )
    {
        super ();

        this.image = image;
        this.disabledImage = null;

        this.displayType = DisplayType.preferred;
        this.horizontalAlignment = CENTER;
        this.verticalAlignment = CENTER;
        this.opacity = 1f;

        SwingUtils.setOrientation ( this );
        setOpaque ( false );

        addPropertyChangeListener ( new PropertyChangeListener ()
        {
            @Override
            public void propertyChange ( final PropertyChangeEvent evt )
            {
                if ( CompareUtils.equals ( evt.getPropertyName (), WebLookAndFeel.ENABLED_PROPERTY ) )
                {
                    if ( !isEnabled () )
                    {
                        calculateDisabledImage ();
                        repaint ();
                    }
                    else
                    {
                        clearDisabledImage ();
                        repaint ();
                    }
                }
                else if ( CompareUtils.equals ( evt.getPropertyName (), WebLookAndFeel.COMPONENT_ORIENTATION_PROPERTY ) )
                {
                    updateBorder ();
                    revalidate ();
                    repaint ();
                }
            }
        } );
    }

    /**
     * Updates cached disabled image.
     */
    protected void calculateDisabledImage ()
    {
        disabledImage = image != null ? ImageUtils.createDisabledCopy ( image ) : null;
        lastPreviewImage = null;
    }

    /**
     * Clears cached disabled image
     */
    private void clearDisabledImage ()
    {
        if ( disabledImage != null )
        {
            disabledImage.flush ();
            disabledImage = null;
        }
        lastPreviewImage = null;
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
     * Returns icon representing current image.
     *
     * @return icon representing current image
     */
    public ImageIcon geIcon ()
    {
        return new ImageIcon ( image );
    }

    /**
     * Returns last image preview.
     * This image might be the modified version of original image set into this component.
     *
     * @return last image preview
     */
    public BufferedImage getPreviewImage ()
    {
        return lastPreviewImage;
    }

    /**
     * Returns icon representing last image preview.
     * This image icon might contain modified version of original image set into this component.
     *
     * @return icon representing last image preview
     */
    public ImageIcon getPreviewIcon ()
    {
        return lastPreviewImage != null ? new ImageIcon ( lastPreviewImage ) : null;
    }

    /**
     * Changes image to new one taken from specified icon.
     *
     * @param icon icon to process
     * @return this image component
     */
    public WebImage setIcon ( final Icon icon )
    {
        setImage ( ImageUtils.getBufferedImage ( icon ) );
        return this;
    }

    /**
     * Changes image to new one taken from specified image icon.
     *
     * @param icon image icon to process
     * @return this image component
     */
    public WebImage setIcon ( final ImageIcon icon )
    {
        setImage ( icon.getImage () );
        return this;
    }

    /**
     * Changes image to the specified one.
     *
     * @param image new image
     * @return this image component
     */
    public WebImage setImage ( final Image image )
    {
        setImage ( ImageUtils.getBufferedImage ( image ) );
        return this;
    }

    /**
     * Changes image to the specified one.
     *
     * @param image new image
     * @return this image component
     */
    public WebImage setImage ( final BufferedImage image )
    {
        this.image = image;
        if ( !isEnabled () )
        {
            calculateDisabledImage ();
        }
        revalidate ();
        repaint ();
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
        this.displayType = displayType;
        updateView ();
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
        this.horizontalAlignment = horizontalAlignment;
        updateView ();
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
        this.verticalAlignment = verticalAlignment;
        updateView ();
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
        this.opacity = opacity;
        updateView ();
        return this;
    }

    /**
     * Updates image component view.
     */
    protected void updateView ()
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
     * @return this image component
     */
    public WebImage setMargin ( final Insets margin )
    {
        this.margin = margin;
        updateBorder ();
        return this;
    }

    /**
     * Changes image margin.
     *
     * @param top    top margin
     * @param left   left margin
     * @param bottom bottom margin
     * @param right  right margin
     * @return this image component
     */
    public WebImage setMargin ( final int top, final int left, final int bottom, final int right )
    {
        return setMargin ( new Insets ( top, left, bottom, right ) );
    }

    /**
     * Changes image margin.
     *
     * @param spacing side spacing
     * @return this image component
     */
    public WebImage setMargin ( final int spacing )
    {
        return setMargin ( spacing, spacing, spacing, spacing );
    }

    /**
     * Updates image component border.
     */
    protected void updateBorder ()
    {
        if ( margin != null )
        {
            final boolean ltr = getComponentOrientation ().isLeftToRight ();
            setBorder ( new WebBorder ( margin.top, ltr ? margin.left : margin.right, margin.bottom, ltr ? margin.right : margin.left ) );
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
    protected void paintComponent ( final Graphics g )
    {
        super.paintComponent ( g );

        if ( opacity <= 0f )
        {
            return;
        }

        final Graphics2D g2d = ( Graphics2D ) g;
        final Composite oc = GraphicsUtils.setupAlphaComposite ( g2d, opacity, opacity < 1f );

        // todo Optimize for repaint (check if image is out of repainted/clipped bounds)
        final BufferedImage currentImage = getCurrentImage ();
        if ( currentImage != null )
        {
            final Insets insets = getInsets ();
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
                        final int x = horizontalAlignment == LEFT ? insets.left :
                                horizontalAlignment == RIGHT ? getWidth () - currentImage.getWidth () - insets.right :
                                        getCenterX ( insets ) - currentImage.getWidth () / 2;
                        final int y = verticalAlignment == TOP ? insets.top :
                                verticalAlignment == BOTTOM ? getHeight () - currentImage.getHeight () - insets.bottom :
                                        getCenterY ( insets ) - currentImage.getHeight () / 2;
                        g2d.drawImage ( currentImage, x, y, null );
                        break;
                    }
                    case fitComponent:
                    {
                        // Drawing sized to fit object image
                        final BufferedImage preview = getPreviewImage ( insets );
                        g2d.drawImage ( preview, getCenterX ( insets ) - preview.getWidth () / 2,
                                getCenterY ( insets ) - preview.getHeight () / 2, null );
                        break;
                    }
                    case repeat:
                    {
                        // Drawing repeated in background image
                        final int x = horizontalAlignment == LEFT ? insets.left :
                                horizontalAlignment == RIGHT ? getWidth () - currentImage.getWidth () - insets.right :
                                        getCenterX ( insets ) - currentImage.getWidth () / 2;
                        final int y = verticalAlignment == TOP ? insets.top :
                                verticalAlignment == BOTTOM ? getHeight () - currentImage.getHeight () - insets.bottom :
                                        getCenterY ( insets ) - currentImage.getHeight () / 2;
                        g2d.setPaint ( new TexturePaint ( currentImage,
                                new Rectangle2D.Double ( x, y, currentImage.getWidth (), currentImage.getHeight () ) ) );
                        g2d.fillRect ( insets.left, insets.top, getWidth () - insets.left - insets.right,
                                getHeight () - insets.top - insets.bottom );
                        break;
                    }
                }
            }
        }

        GraphicsUtils.restoreComposite ( g2d, oc, opacity < 1f );
    }

    /**
     * Returns image component center X coordinate.
     *
     * @param insets image component insets
     * @return image component center X coordinate
     */
    protected int getCenterX ( final Insets insets )
    {
        return insets.left + ( getWidth () - insets.left - insets.right ) / 2;
    }

    /**
     * Returns image component center Y coordinate.
     *
     * @param insets image component insets
     * @return image component center Y coordinate
     */
    protected int getCenterY ( final Insets insets )
    {
        return insets.top + ( getHeight () - insets.top - insets.bottom ) / 2;
    }

    /**
     * Returns preview image for specified insets.
     *
     * @param insets image component insets
     * @return preview image
     */
    protected BufferedImage getPreviewImage ( final Insets insets )
    {
        if ( image.getWidth () > getWidth () || image.getHeight () > getHeight () )
        {
            final Dimension size = getSize ();
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
    protected BufferedImage getCurrentImage ()
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
    protected Dimension getRequiredSize ()
    {
        final Insets insets = getInsets ();
        return new Dimension ( insets.left + ( image != null ? image.getWidth () : 0 ) + insets.right,
                insets.top + ( image != null ? image.getHeight () : 0 ) + insets.bottom );
    }

    @Override
    public MouseAdapter onMousePress ( final MouseEventRunnable runnable )
    {
        return EventUtils.onMousePress ( this, runnable );
    }

    @Override
    public MouseAdapter onMousePress ( final MouseButton mouseButton, final MouseEventRunnable runnable )
    {
        return EventUtils.onMousePress ( this, mouseButton, runnable );
    }

    @Override
    public MouseAdapter onMouseEnter ( final MouseEventRunnable runnable )
    {
        return EventUtils.onMouseEnter ( this, runnable );
    }

    @Override
    public MouseAdapter onMouseExit ( final MouseEventRunnable runnable )
    {
        return EventUtils.onMouseExit ( this, runnable );
    }

    @Override
    public MouseAdapter onMouseDrag ( final MouseEventRunnable runnable )
    {
        return EventUtils.onMouseDrag ( this, runnable );
    }

    @Override
    public MouseAdapter onMouseDrag ( final MouseButton mouseButton, final MouseEventRunnable runnable )
    {
        return EventUtils.onMouseDrag ( this, mouseButton, runnable );
    }

    @Override
    public MouseAdapter onMouseClick ( final MouseEventRunnable runnable )
    {
        return EventUtils.onMouseClick ( this, runnable );
    }

    @Override
    public MouseAdapter onMouseClick ( final MouseButton mouseButton, final MouseEventRunnable runnable )
    {
        return EventUtils.onMouseClick ( this, mouseButton, runnable );
    }

    @Override
    public MouseAdapter onDoubleClick ( final MouseEventRunnable runnable )
    {
        return EventUtils.onDoubleClick ( this, runnable );
    }

    @Override
    public MouseAdapter onMenuTrigger ( final MouseEventRunnable runnable )
    {
        return EventUtils.onMenuTrigger ( this, runnable );
    }

    @Override
    public KeyAdapter onKeyType ( final KeyEventRunnable runnable )
    {
        return EventUtils.onKeyType ( this, runnable );
    }

    @Override
    public KeyAdapter onKeyType ( final HotkeyData hotkey, final KeyEventRunnable runnable )
    {
        return EventUtils.onKeyType ( this, hotkey, runnable );
    }

    @Override
    public KeyAdapter onKeyPress ( final KeyEventRunnable runnable )
    {
        return EventUtils.onKeyPress ( this, runnable );
    }

    @Override
    public KeyAdapter onKeyPress ( final HotkeyData hotkey, final KeyEventRunnable runnable )
    {
        return EventUtils.onKeyPress ( this, hotkey, runnable );
    }

    @Override
    public KeyAdapter onKeyRelease ( final KeyEventRunnable runnable )
    {
        return EventUtils.onKeyRelease ( this, runnable );
    }

    @Override
    public KeyAdapter onKeyRelease ( final HotkeyData hotkey, final KeyEventRunnable runnable )
    {
        return EventUtils.onKeyRelease ( this, hotkey, runnable );
    }

    @Override
    public FocusAdapter onFocusGain ( final FocusEventRunnable runnable )
    {
        return EventUtils.onFocusGain ( this, runnable );
    }

    @Override
    public FocusAdapter onFocusLoss ( final FocusEventRunnable runnable )
    {
        return EventUtils.onFocusLoss ( this, runnable );
    }

    @Override
    public WebCustomTooltip setToolTip ( final String tooltip )
    {
        return TooltipManager.setTooltip ( this, tooltip );
    }

    @Override
    public WebCustomTooltip setToolTip ( final Icon icon, final String tooltip )
    {
        return TooltipManager.setTooltip ( this, icon, tooltip );
    }

    @Override
    public WebCustomTooltip setToolTip ( final String tooltip, final TooltipWay tooltipWay )
    {
        return TooltipManager.setTooltip ( this, tooltip, tooltipWay );
    }

    @Override
    public WebCustomTooltip setToolTip ( final Icon icon, final String tooltip, final TooltipWay tooltipWay )
    {
        return TooltipManager.setTooltip ( this, icon, tooltip, tooltipWay );
    }

    @Override
    public WebCustomTooltip setToolTip ( final String tooltip, final TooltipWay tooltipWay, final int delay )
    {
        return TooltipManager.setTooltip ( this, tooltip, tooltipWay, delay );
    }

    @Override
    public WebCustomTooltip setToolTip ( final Icon icon, final String tooltip, final TooltipWay tooltipWay, final int delay )
    {
        return TooltipManager.setTooltip ( this, icon, tooltip, tooltipWay, delay );
    }

    @Override
    public WebCustomTooltip setToolTip ( final JComponent tooltip )
    {
        return TooltipManager.setTooltip ( this, tooltip );
    }

    @Override
    public WebCustomTooltip setToolTip ( final JComponent tooltip, final int delay )
    {
        return TooltipManager.setTooltip ( this, tooltip, delay );
    }

    @Override
    public WebCustomTooltip setToolTip ( final JComponent tooltip, final TooltipWay tooltipWay )
    {
        return TooltipManager.setTooltip ( this, tooltip, tooltipWay );
    }

    @Override
    public WebCustomTooltip setToolTip ( final JComponent tooltip, final TooltipWay tooltipWay, final int delay )
    {
        return TooltipManager.setTooltip ( this, tooltip, tooltipWay, delay );
    }

    @Override
    public WebCustomTooltip addToolTip ( final String tooltip )
    {
        return TooltipManager.addTooltip ( this, tooltip );
    }

    @Override
    public WebCustomTooltip addToolTip ( final Icon icon, final String tooltip )
    {
        return TooltipManager.addTooltip ( this, icon, tooltip );
    }

    @Override
    public WebCustomTooltip addToolTip ( final String tooltip, final TooltipWay tooltipWay )
    {
        return TooltipManager.addTooltip ( this, tooltip, tooltipWay );
    }

    @Override
    public WebCustomTooltip addToolTip ( final Icon icon, final String tooltip, final TooltipWay tooltipWay )
    {
        return TooltipManager.addTooltip ( this, icon, tooltip, tooltipWay );
    }

    @Override
    public WebCustomTooltip addToolTip ( final String tooltip, final TooltipWay tooltipWay, final int delay )
    {
        return TooltipManager.addTooltip ( this, tooltip, tooltipWay, delay );
    }

    @Override
    public WebCustomTooltip addToolTip ( final Icon icon, final String tooltip, final TooltipWay tooltipWay, final int delay )
    {
        return TooltipManager.addTooltip ( this, icon, tooltip, tooltipWay, delay );
    }

    @Override
    public WebCustomTooltip addToolTip ( final JComponent tooltip )
    {
        return TooltipManager.addTooltip ( this, tooltip );
    }

    @Override
    public WebCustomTooltip addToolTip ( final JComponent tooltip, final int delay )
    {
        return TooltipManager.addTooltip ( this, tooltip, delay );
    }

    @Override
    public WebCustomTooltip addToolTip ( final JComponent tooltip, final TooltipWay tooltipWay )
    {
        return TooltipManager.addTooltip ( this, tooltip, tooltipWay );
    }

    @Override
    public WebCustomTooltip addToolTip ( final JComponent tooltip, final TooltipWay tooltipWay, final int delay )
    {
        return TooltipManager.addTooltip ( this, tooltip, tooltipWay, delay );
    }

    @Override
    public void removeToolTip ( final WebCustomTooltip tooltip )
    {
        TooltipManager.removeTooltip ( this, tooltip );
    }

    @Override
    public void removeToolTips ()
    {
        TooltipManager.removeTooltips ( this );
    }

    @Override
    public void removeToolTips ( final WebCustomTooltip... tooltips )
    {
        TooltipManager.removeTooltips ( this, tooltips );
    }

    @Override
    public void removeToolTips ( final List<WebCustomTooltip> tooltips )
    {
        TooltipManager.removeTooltips ( this, tooltips );
    }
}