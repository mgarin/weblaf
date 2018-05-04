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

package com.alee.painter.decoration.shadow;

import com.alee.api.clone.behavior.OmitOnClone;
import com.alee.api.merge.behavior.OmitOnMerge;
import com.alee.graphics.filters.ShadowFilter;
import com.alee.painter.decoration.WebDecoration;
import com.alee.painter.decoration.shape.IShape;
import com.alee.painter.decoration.shape.StretchInfo;
import com.alee.utils.ColorUtils;
import com.alee.utils.GraphicsUtils;
import com.alee.utils.ImageUtils;
import com.alee.utils.TextUtils;
import com.alee.utils.general.Pair;
import com.alee.utils.ninepatch.NinePatchIcon;
import com.thoughtworks.xstream.annotations.XStreamAlias;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Area;
import java.awt.image.BufferedImage;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

/**
 * Basic shadow that can be painted on any shape.
 * Shadow is generated through the {@link com.alee.graphics.filters.ShadowFilter} and saved into {@link java.awt.image.BufferedImage}.
 * Generated image with the shadow is usually kept in cache to optimize UI performance.
 *
 * One generated shadow sometimes might be reused by multiple components of different size.
 * That happens only when {@link com.alee.painter.decoration.shape.IShape} implementation this shadow is based on provides stretch areas.
 * In that case {@link com.alee.utils.ninepatch.NinePatchIcon} is created based on the shadow image to stretch the shadow.
 *
 * @param <C> component type
 * @param <D> decoration type
 * @param <I> shadow type
 * @author Mikle Garin
 */
@XStreamAlias ( "WebShadow" )
public class WebShadow<C extends JComponent, D extends WebDecoration<C, D>, I extends WebShadow<C, D, I>> extends AbstractShadow<C, D, I>
{
    /**
     * Shadow icons cache.
     */
    protected static transient final Map<String, WeakReference<NinePatchIcon>> shadowIconsCache =
            new HashMap<String, WeakReference<NinePatchIcon>> ( 100 );

    /**
     * Shadow images cache.
     */
    protected static transient final Map<String, WeakReference<BufferedImage>> shadowImagesCache =
            new HashMap<String, WeakReference<BufferedImage>> ( 20 );

    /**
     * Cache key data separator.
     */
    protected static final String separator = ",";

    /**
     * Reference keeping shadow icon in memory.
     */
    @OmitOnClone
    @OmitOnMerge
    protected transient NinePatchIcon shadowIcon;

    /**
     * Reference keeping shadow image in memory.
     */
    @OmitOnClone
    @OmitOnMerge
    protected transient BufferedImage shadowImage;

    @Override
    public void paint ( final Graphics2D g2d, final Rectangle bounds, final C c, final D d, final Shape shape )
    {
        final int width = getWidth ();
        final float opacity = getOpacity ();
        if ( width > 0 && opacity > 0f )
        {
            // Shadow image bounds
            final ShadowType type = getType ();
            final Rectangle b = getShadowBounds ( type, shape, width );

            // Deciding how shadow should be painted
            final IShape shapeType = d.getShape ();
            final StretchInfo stretch = shapeType.getStretchInfo ( bounds, c, d );
            final Object[] settings = shapeType.getShapeSettings ( bounds, c, d );
            if ( stretch != null && stretch.isStretchable () )
            {
                // Painting stretchable shadow based on 9-patch icon
                // It is cached using shadow settings, shape settings and bounds if needed
                if ( type == ShadowType.outer )
                {
                    // Outer 9-patch shadow icon
                    shadowIcon = getShadowIcon ( stretch, b, width, opacity, getColor (), shape, settings );
                    shadowIcon.paintIcon ( g2d, b.x, b.y, b.width, b.height );
                    shadowImage = null;
                }
                else
                {
                    // Inner 9-patch shadow icon
                    shadowIcon = getInnerShadowIcon ( stretch, b, width, opacity, getColor (), shape, settings );
                    shadowIcon.paintIcon ( g2d, b.x, b.y, b.width, b.height );
                    shadowImage = null;
                }
            }
            else
            {
                // Painting static shadow image
                // It is cached using shadow settings, shape settings and bounds
                if ( type == ShadowType.outer )
                {
                    // Outer shadow image
                    shadowImage = getShadowImage ( b, width, opacity, getColor (), shape, settings );
                    g2d.drawImage ( shadowImage, b.x, b.y, b.width, b.height, null );
                    shadowIcon = null;
                }
                else
                {
                    // Inner shadow image
                    shadowImage = getInnerShadowImage ( b, width, opacity, getColor (), shape, settings );
                    g2d.drawImage ( shadowImage, b.x, b.y, b.width, b.height, null );
                    shadowIcon = null;
                }
            }

            //            // Shadow info
            //            final StretchInfo stretch = d.getShape ().getStretchInfo ( bounds, c, d );
            //            if ( stretch != null )
            //            {
            //                g2d.setPaint ( Color.RED );
            //                final Pair<Integer, Integer> hor = stretch.getHorizontalStretch ();
            //                if ( hor != null )
            //                {
            //                    g2d.drawLine ( hor.getKey (), bounds.y, hor.getKey (), bounds.y + bounds.height );
            //                    g2d.drawLine ( hor.getValue (), bounds.y, hor.getValue (), bounds.y + bounds.height );
            //                }
            //                final Pair<Integer, Integer> ver = stretch.getVerticalStretch ();
            //                if ( ver != null )
            //                {
            //                    g2d.drawLine ( bounds.x, ver.getKey (), bounds.x + bounds.width, ver.getKey () );
            //                    g2d.drawLine ( bounds.x, ver.getValue (), bounds.x + bounds.width, ver.getValue () );
            //                }
            //            }
        }
        else
        {
            shadowIcon = null;
            shadowImage = null;
        }
    }

    /**
     * Returns bounds used for shadow image generation.
     *
     * @param type  shadow type
     * @param shape shadow shape
     * @param width shadow width
     * @return bounds used for shadow image generation
     */
    protected Rectangle getShadowBounds ( final ShadowType type, final Shape shape, final int width )
    {
        final Rectangle sb = shape.getBounds ();
        if ( type == ShadowType.outer )
        {
            return new Rectangle ( sb.x - width, sb.y - width, sb.width + width * 2, sb.height + width * 2 );
        }
        else
        {
            return new Rectangle ( sb.x, sb.y, sb.width, sb.height );
        }
    }

    /**
     * Returns cached shadow icon based on provided shape.
     *
     * @param stretchInfo shape stretch information
     * @param bounds      shadow bounds
     * @param width       shadow width
     * @param opacity     shadow opacity
     * @param color       shadow color
     * @param shape       shadow shape
     * @param settings    shape settings
     * @return cached shadow icon based on provided shape
     */
    public static NinePatchIcon getShadowIcon ( final StretchInfo stretchInfo, final Rectangle bounds, final int width, final float opacity,
                                                final Color color, final Shape shape, final Object... settings )
    {
        // Width and height is added as key in case there are no horizontal and/or vertical stretchable areas
        final int hor = stretchInfo.getHorizontalStretch () == null ? bounds.width : 0;
        final int ver = stretchInfo.getVerticalStretch () == null ? bounds.height : 0;
        final String key = TextUtils.getSettingsKey ( ShadowType.outer, hor, ver, width, opacity, color, settings );
        NinePatchIcon shadow = shadowIconsCache.containsKey ( key ) ? shadowIconsCache.get ( key ).get () : null;
        if ( shadow == null )
        {
            shadow = createShadowIcon ( stretchInfo, bounds, width, opacity, color, shape );
            shadowIconsCache.put ( key, new WeakReference<NinePatchIcon> ( shadow ) );
        }
        return shadow;
    }

    /**
     * Returns shadow nine-patch icon.
     *
     * @param stretchInfo shape stretch information
     * @param bounds      shadow bounds
     * @param width       shadow width
     * @param opacity     shadow opacity
     * @param color       shadow color
     * @param shape       shadow shape
     * @return shadow nine-patch icon
     */
    public static NinePatchIcon createShadowIcon ( final StretchInfo stretchInfo, final Rectangle bounds, final int width,
                                                   final float opacity, final Color color, final Shape shape )
    {
        // Creating shadow image
        final BufferedImage image = createShadowImage ( bounds, width, opacity, color, shape );

        // Creating nine-patch icon based on shadow image
        final NinePatchIcon icon = NinePatchIcon.create ( image );
        final Pair<Integer, Integer> hor = stretchInfo.getHorizontalStretch ();
        if ( hor != null )
        {
            final int x0 = hor.getKey () - bounds.x;
            final int x1 = hor.getValue () - bounds.x;
            icon.addHorizontalStretch ( 0, x0 - 1, true );
            icon.addHorizontalStretch ( x0, x1, false );
            icon.addHorizontalStretch ( x1 + 1, image.getWidth (), true );
        }
        else
        {
            icon.addHorizontalStretch ( 0, image.getWidth (), true );
        }
        final Pair<Integer, Integer> ver = stretchInfo.getVerticalStretch ();
        if ( ver != null )
        {
            final int y0 = ver.getKey () - bounds.y;
            final int y1 = ver.getValue () - bounds.y;
            icon.addVerticalStretch ( 0, y0 - 1, true );
            icon.addVerticalStretch ( y0, y1, false );
            icon.addVerticalStretch ( y1 + 1, image.getHeight (), true );
        }
        else
        {
            icon.addVerticalStretch ( 0, image.getHeight (), true );
        }
        icon.setMargin ( width );
        return icon;
    }

    /**
     * Returns cached shadow image based on provided shape.
     *
     * @param bounds   shadow bounds
     * @param width    shadow width
     * @param opacity  shadow opacity
     * @param color    shadow color
     * @param shape    shadow shape
     * @param settings shape settings
     * @return cached shadow image based on provided shape
     */
    public static BufferedImage getShadowImage ( final Rectangle bounds, final int width, final float opacity, final Color color,
                                                 final Shape shape, final Object... settings )
    {
        final String key = TextUtils.getSettingsKey ( ShadowType.outer, bounds.width, bounds.height, width, opacity, color, settings );
        BufferedImage shadow = shadowImagesCache.containsKey ( key ) ? shadowImagesCache.get ( key ).get () : null;
        if ( shadow == null )
        {
            shadow = createShadowImage ( bounds, width, opacity, color, shape );
            shadowImagesCache.put ( key, new WeakReference<BufferedImage> ( shadow ) );
        }
        return shadow;
    }

    /**
     * Returns shadow image based on provided shape.
     *
     * @param bounds  shadow bounds
     * @param width   shadow width
     * @param opacity shadow opacity
     * @param color   shadow color
     * @param shape   shadow shape
     * @return shadow image based on provided shape
     */
    public static BufferedImage createShadowImage ( final Rectangle bounds, final int width, final float opacity, final Color color,
                                                    final Shape shape )
    {
        // Creating template image
        final BufferedImage bi = ImageUtils.createCompatibleImage ( bounds.width, bounds.height, Transparency.TRANSLUCENT );
        final Graphics2D ig = bi.createGraphics ();
        GraphicsUtils.setupAntialias ( ig );
        ig.translate ( -bounds.x, -bounds.y );
        ig.setPaint ( Color.BLACK );
        ig.fill ( shape );
        ig.dispose ();

        // Creating shadow image
        final ShadowFilter sf = new ShadowFilter ( width, 0, 0, opacity );
        sf.setShadowColor ( Color.BLACK.getRGB () );
        final BufferedImage shadow = sf.filter ( bi, null );

        // Clipping shadow image
        final Graphics2D g2d = shadow.createGraphics ();
        GraphicsUtils.setupAntialias ( g2d );
        g2d.translate ( -bounds.x, -bounds.y );
        g2d.setComposite ( AlphaComposite.getInstance ( AlphaComposite.SRC_IN ) );
        g2d.setPaint ( ColorUtils.transparent () );
        g2d.fill ( shape );
        g2d.setPaint ( color );
        g2d.setComposite ( AlphaComposite.getInstance ( AlphaComposite.SRC_IN ) );
        g2d.fillRect ( 0, 0, bounds.width, bounds.height );
        g2d.dispose ();

        return shadow;
    }

    /**
     * Returns cached inner shadow icon based on provided shape.
     *
     * @param stretchInfo shape stretch information
     * @param bounds      shadow bounds
     * @param width       shadow width
     * @param opacity     shadow opacity
     * @param color       shadow color
     * @param shape       shadow shape
     * @param settings    shape settings
     * @return cached inner shadow icon based on provided shape
     */
    public static NinePatchIcon getInnerShadowIcon ( final StretchInfo stretchInfo, final Rectangle bounds, final int width,
                                                     final float opacity, final Color color, final Shape shape, final Object... settings )
    {
        // Width and height is added as key in case there are no horizontal and/or vertical stretchable areas
        final int hor = stretchInfo.getHorizontalStretch () == null ? bounds.width : 0;
        final int ver = stretchInfo.getVerticalStretch () == null ? bounds.height : 0;
        final String key = TextUtils.getSettingsKey ( ShadowType.inner, hor, ver, width, opacity, color, settings );
        NinePatchIcon shadow = shadowIconsCache.containsKey ( key ) ? shadowIconsCache.get ( key ).get () : null;
        if ( shadow == null )
        {
            shadow = createInnerShadowIcon ( stretchInfo, bounds, width, opacity, color, shape );
            shadowIconsCache.put ( key, new WeakReference<NinePatchIcon> ( shadow ) );
        }
        return shadow;
    }

    /**
     * Returns inner shadow nine-patch icon.
     *
     * @param stretchInfo shape stretch information
     * @param bounds      shadow bounds
     * @param width       shadow width
     * @param opacity     shadow opacity
     * @param color       shadow color
     * @param shape       shadow shape
     * @return inner shadow nine-patch icon
     */
    public static NinePatchIcon createInnerShadowIcon ( final StretchInfo stretchInfo, final Rectangle bounds, final int width,
                                                        final float opacity, final Color color, final Shape shape )
    {
        // Creating inner shadow image
        final BufferedImage image = createInnerShadowImage ( bounds, width, opacity, color, shape );

        // Creating nine-patch icon based on inner shadow image
        final NinePatchIcon icon = NinePatchIcon.create ( image );
        final Pair<Integer, Integer> hor = stretchInfo.getHorizontalStretch ();
        if ( hor != null )
        {
            final int x0 = hor.getKey () - bounds.x;
            final int x1 = hor.getValue () - bounds.x;
            icon.addHorizontalStretch ( 0, x0 - 1, true );
            icon.addHorizontalStretch ( x0, x1, false );
            icon.addHorizontalStretch ( x1 + 1, image.getWidth (), true );
        }
        else
        {
            icon.addHorizontalStretch ( 0, image.getWidth (), true );
        }
        final Pair<Integer, Integer> ver = stretchInfo.getVerticalStretch ();
        if ( ver != null )
        {
            final int y0 = ver.getKey () - bounds.y;
            final int y1 = ver.getValue () - bounds.y;
            icon.addVerticalStretch ( 0, y0 - 1, true );
            icon.addVerticalStretch ( y0, y1, false );
            icon.addVerticalStretch ( y1 + 1, image.getHeight (), true );
        }
        else
        {
            icon.addVerticalStretch ( 0, image.getHeight (), true );
        }
        icon.setMargin ( width );
        return icon;
    }

    /**
     * Returns cached inner shadow image based on provided shape.
     *
     * @param bounds   shadow bounds
     * @param width    shadow width
     * @param opacity  shadow opacity
     * @param color    shadow color
     * @param shape    shadow shape
     * @param settings shape settings
     * @return cached inner shadow image based on provided shape
     */
    public static BufferedImage getInnerShadowImage ( final Rectangle bounds, final int width, final float opacity, final Color color,
                                                      final Shape shape, final Object... settings )
    {
        final String key = TextUtils.getSettingsKey ( ShadowType.inner, bounds.width, bounds.height, width, opacity, color, settings );
        BufferedImage shadow = shadowImagesCache.containsKey ( key ) ? shadowImagesCache.get ( key ).get () : null;
        if ( shadow == null )
        {
            shadow = createInnerShadowImage ( bounds, width, opacity, color, shape );
            shadowImagesCache.put ( key, new WeakReference<BufferedImage> ( shadow ) );
        }
        return shadow;
    }

    /**
     * Returns inner shadow image based on provided shape.
     *
     * @param bounds  shadow bounds
     * @param width   shadow width
     * @param opacity shadow opacity
     * @param color   shadow color
     * @param shape   shadow shape
     * @return inner shadow image based on provided shape
     */
    public static BufferedImage createInnerShadowImage ( final Rectangle bounds, final int width, final float opacity, final Color color,
                                                         final Shape shape )
    {
        final Rectangle b = new Rectangle ( bounds.x - width * 2, bounds.y - width * 2,
                bounds.width + width * 4, bounds.height + width * 4 );

        // Creating template image
        final BufferedImage bi = ImageUtils.createCompatibleImage ( b.width, b.height, Transparency.TRANSLUCENT );
        final Graphics2D ig = bi.createGraphics ();
        GraphicsUtils.setupAntialias ( ig );
        ig.translate ( -b.x, -b.y );
        final Area area = new Area ( new Rectangle ( b.x, b.y, b.width, b.height ) );
        area.exclusiveOr ( new Area ( shape ) );
        ig.setPaint ( Color.BLACK );
        ig.fill ( area );
        ig.dispose ();

        // Creating inner shadow image
        final ShadowFilter sf = new ShadowFilter ( width, 0, 0, opacity );
        sf.setShadowColor ( Color.BLACK.getRGB () );
        final BufferedImage shadow = sf.filter ( bi, null );

        // Clipping inner shadow image
        final Graphics2D g2d = shadow.createGraphics ();
        GraphicsUtils.setupAntialias ( g2d );
        g2d.translate ( -b.x, -b.y );
        g2d.setComposite ( AlphaComposite.getInstance ( AlphaComposite.SRC_IN ) );
        g2d.setPaint ( ColorUtils.transparent () );
        g2d.fill ( area );
        g2d.setPaint ( color );
        g2d.setComposite ( AlphaComposite.getInstance ( AlphaComposite.SRC_IN ) );
        g2d.fillRect ( 0, 0, b.width, b.height );
        g2d.dispose ();

        return shadow.getSubimage ( width * 2, width * 2, b.width - width * 4, b.height - width * 4 );
    }
}