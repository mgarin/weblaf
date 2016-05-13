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
import com.alee.managers.log.Log;
import com.alee.managers.style.*;
import com.alee.managers.tooltip.ToolTipMethods;
import com.alee.managers.tooltip.TooltipManager;
import com.alee.managers.tooltip.WebCustomTooltip;
import com.alee.painter.Paintable;
import com.alee.painter.Painter;
import com.alee.utils.ImageUtils;
import com.alee.utils.swing.MouseButton;
import com.alee.utils.swing.extensions.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.KeyAdapter;
import java.awt.event.MouseAdapter;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.List;
import java.util.Map;

/**
 * This component allows you to display images in many different ways.
 * This component uses less resources than a label and has a few optimization.
 *
 * @author Mikle Garin
 */

public class WebImage extends JComponent
        implements Styleable, Skinnable, Paintable, ShapeProvider, MarginSupport, PaddingSupport, EventMethods, ToolTipMethods,
        SizeMethods<WebImage>, SwingConstants
{
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
        this ( StyleId.image, image );
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

    @Override
    public StyleId getStyleId ()
    {
        return getWebUI ().getStyleId ();
    }

    @Override
    public StyleId setStyleId ( final StyleId id )
    {
        return getWebUI ().setStyleId ( id );
    }

    @Override
    public Skin getSkin ()
    {
        return StyleManager.getSkin ( this );
    }

    @Override
    public Skin setSkin ( final Skin skin )
    {
        return StyleManager.setSkin ( this, skin );
    }

    @Override
    public Skin setSkin ( final Skin skin, final boolean recursively )
    {
        return StyleManager.setSkin ( this, skin, recursively );
    }

    @Override
    public Skin restoreSkin ()
    {
        return StyleManager.restoreSkin ( this );
    }

    @Override
    public void addStyleListener ( final StyleListener listener )
    {
        StyleManager.addStyleListener ( this, listener );
    }

    @Override
    public void removeStyleListener ( final StyleListener listener )
    {
        StyleManager.removeStyleListener ( this, listener );
    }

    @Override
    public Map<String, Painter> getCustomPainters ()
    {
        return StyleManager.getCustomPainters ( this );
    }

    @Override
    public Painter getCustomPainter ()
    {
        return StyleManager.getCustomPainter ( this );
    }

    @Override
    public Painter getCustomPainter ( final String id )
    {
        return StyleManager.getCustomPainter ( this, id );
    }

    @Override
    public Painter setCustomPainter ( final Painter painter )
    {
        return StyleManager.setCustomPainter ( this, painter );
    }

    @Override
    public Painter setCustomPainter ( final String id, final Painter painter )
    {
        return StyleManager.setCustomPainter ( this, id, painter );
    }

    @Override
    public boolean restoreDefaultPainters ()
    {
        return StyleManager.restoreDefaultPainters ( this );
    }

    @Override
    public Shape provideShape ()
    {
        return getWebUI ().provideShape ();
    }

    @Override
    public Insets getMargin ()
    {
        return getWebUI ().getMargin ();
    }

    /**
     * Sets new margin.
     *
     * @param margin new margin
     */
    public void setMargin ( final int margin )
    {
        setMargin ( margin, margin, margin, margin );
    }

    /**
     * Sets new margin.
     *
     * @param top    new top margin
     * @param left   new left margin
     * @param bottom new bottom margin
     * @param right  new right margin
     */
    public void setMargin ( final int top, final int left, final int bottom, final int right )
    {
        setMargin ( new Insets ( top, left, bottom, right ) );
    }

    @Override
    public void setMargin ( final Insets margin )
    {
        getWebUI ().setMargin ( margin );
    }

    @Override
    public Insets getPadding ()
    {
        return getWebUI ().getPadding ();
    }

    /**
     * Sets new padding.
     *
     * @param padding new padding
     */
    public void setPadding ( final int padding )
    {
        setPadding ( padding, padding, padding, padding );
    }

    /**
     * Sets new padding.
     *
     * @param top    new top padding
     * @param left   new left padding
     * @param bottom new bottom padding
     * @param right  new right padding
     */
    public void setPadding ( final int top, final int left, final int bottom, final int right )
    {
        setPadding ( new Insets ( top, left, bottom, right ) );
    }

    @Override
    public void setPadding ( final Insets padding )
    {
        getWebUI ().setPadding ( padding );
    }

    /**
     * Returns Web-UI applied to this class.
     *
     * @return Web-UI applied to this class
     */
    public WebImageUI getWebUI ()
    {
        return ( WebImageUI ) getUI ();
    }

    /**
     * Returns the look and feel (L&amp;F) object that renders this component.
     *
     * @return the ImageUI object that renders this component
     */
    public ImageUI getUI ()
    {
        return ( ImageUI ) ui;
    }

    @Override
    public void updateUI ()
    {
        if ( getUI () == null || !( getUI () instanceof WebImageUI ) )
        {
            try
            {
                setUI ( UIManager.getUI ( this ) );
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

    @Override
    public MouseAdapter onMousePress ( final MouseEventRunnable runnable )
    {
        return EventMethodsImpl.onMousePress ( this, runnable );
    }

    @Override
    public MouseAdapter onMousePress ( final MouseButton mouseButton, final MouseEventRunnable runnable )
    {
        return EventMethodsImpl.onMousePress ( this, mouseButton, runnable );
    }

    @Override
    public MouseAdapter onMouseEnter ( final MouseEventRunnable runnable )
    {
        return EventMethodsImpl.onMouseEnter ( this, runnable );
    }

    @Override
    public MouseAdapter onMouseExit ( final MouseEventRunnable runnable )
    {
        return EventMethodsImpl.onMouseExit ( this, runnable );
    }

    @Override
    public MouseAdapter onMouseDrag ( final MouseEventRunnable runnable )
    {
        return EventMethodsImpl.onMouseDrag ( this, runnable );
    }

    @Override
    public MouseAdapter onMouseDrag ( final MouseButton mouseButton, final MouseEventRunnable runnable )
    {
        return EventMethodsImpl.onMouseDrag ( this, mouseButton, runnable );
    }

    @Override
    public MouseAdapter onMouseClick ( final MouseEventRunnable runnable )
    {
        return EventMethodsImpl.onMouseClick ( this, runnable );
    }

    @Override
    public MouseAdapter onMouseClick ( final MouseButton mouseButton, final MouseEventRunnable runnable )
    {
        return EventMethodsImpl.onMouseClick ( this, mouseButton, runnable );
    }

    @Override
    public MouseAdapter onDoubleClick ( final MouseEventRunnable runnable )
    {
        return EventMethodsImpl.onDoubleClick ( this, runnable );
    }

    @Override
    public MouseAdapter onMenuTrigger ( final MouseEventRunnable runnable )
    {
        return EventMethodsImpl.onMenuTrigger ( this, runnable );
    }

    @Override
    public KeyAdapter onKeyType ( final KeyEventRunnable runnable )
    {
        return EventMethodsImpl.onKeyType ( this, runnable );
    }

    @Override
    public KeyAdapter onKeyType ( final HotkeyData hotkey, final KeyEventRunnable runnable )
    {
        return EventMethodsImpl.onKeyType ( this, hotkey, runnable );
    }

    @Override
    public KeyAdapter onKeyPress ( final KeyEventRunnable runnable )
    {
        return EventMethodsImpl.onKeyPress ( this, runnable );
    }

    @Override
    public KeyAdapter onKeyPress ( final HotkeyData hotkey, final KeyEventRunnable runnable )
    {
        return EventMethodsImpl.onKeyPress ( this, hotkey, runnable );
    }

    @Override
    public KeyAdapter onKeyRelease ( final KeyEventRunnable runnable )
    {
        return EventMethodsImpl.onKeyRelease ( this, runnable );
    }

    @Override
    public KeyAdapter onKeyRelease ( final HotkeyData hotkey, final KeyEventRunnable runnable )
    {
        return EventMethodsImpl.onKeyRelease ( this, hotkey, runnable );
    }

    @Override
    public FocusAdapter onFocusGain ( final FocusEventRunnable runnable )
    {
        return EventMethodsImpl.onFocusGain ( this, runnable );
    }

    @Override
    public FocusAdapter onFocusLoss ( final FocusEventRunnable runnable )
    {
        return EventMethodsImpl.onFocusLoss ( this, runnable );
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

    @Override
    public int getPreferredWidth ()
    {
        return SizeMethodsImpl.getPreferredWidth ( this );
    }

    @Override
    public WebImage setPreferredWidth ( final int preferredWidth )
    {
        return SizeMethodsImpl.setPreferredWidth ( this, preferredWidth );
    }

    @Override
    public int getPreferredHeight ()
    {
        return SizeMethodsImpl.getPreferredHeight ( this );
    }

    @Override
    public WebImage setPreferredHeight ( final int preferredHeight )
    {
        return SizeMethodsImpl.setPreferredHeight ( this, preferredHeight );
    }

    @Override
    public int getMinimumWidth ()
    {
        return SizeMethodsImpl.getMinimumWidth ( this );
    }

    @Override
    public WebImage setMinimumWidth ( final int minimumWidth )
    {
        return SizeMethodsImpl.setMinimumWidth ( this, minimumWidth );
    }

    @Override
    public int getMinimumHeight ()
    {
        return SizeMethodsImpl.getMinimumHeight ( this );
    }

    @Override
    public WebImage setMinimumHeight ( final int minimumHeight )
    {
        return SizeMethodsImpl.setMinimumHeight ( this, minimumHeight );
    }

    @Override
    public int getMaximumWidth ()
    {
        return SizeMethodsImpl.getMaximumWidth ( this );
    }

    @Override
    public WebImage setMaximumWidth ( final int maximumWidth )
    {
        return SizeMethodsImpl.setMaximumWidth ( this, maximumWidth );
    }

    @Override
    public int getMaximumHeight ()
    {
        return SizeMethodsImpl.getMaximumHeight ( this );
    }

    @Override
    public WebImage setMaximumHeight ( final int maximumHeight )
    {
        return SizeMethodsImpl.setMaximumHeight ( this, maximumHeight );
    }

    @Override
    public WebImage setPreferredSize ( final int width, final int height )
    {
        return SizeMethodsImpl.setPreferredSize ( this, width, height );
    }
}