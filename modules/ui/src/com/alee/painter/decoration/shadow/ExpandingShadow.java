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

import com.alee.painter.decoration.IDecoration;
import com.alee.utils.CompareUtils;
import com.alee.utils.ImageUtils;
import com.alee.utils.ninepatch.NinePatchIcon;
import com.thoughtworks.xstream.annotations.XStreamAlias;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.GeneralPath;
import java.awt.image.BufferedImage;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

/**
 * Shadow that grows larger to the south part of the component.
 *
 * @param <E> component type
 * @param <D> decoration type
 * @param <I> shadow type
 * @author Mikle Garin
 */

@XStreamAlias ( "ExpandingShadow" )
public class ExpandingShadow<E extends JComponent, D extends IDecoration<E, D>, I extends ExpandingShadow<E, D, I>>
        extends AbstractShadow<E, D, I>
{
    /**
     * Shadow images cache.
     */
    protected static final Map<String, WeakReference<NinePatchIcon>> shadowCache = new HashMap<String, WeakReference<NinePatchIcon>> ( 4 );

    /**
     * Last shadow image cache key.
     */
    protected transient String shadowKey;

    /**
     * Currently used shadow image.
     */
    protected transient NinePatchIcon shadowImage;

    @Override
    public void paint ( final Graphics2D g2d, final Rectangle bounds, final E c, final D d, final Shape shape )
    {
        final int width = getWidth ();
        final float opacity = getOpacity ();
        if ( width > 0 && opacity > 0f )
        {
            final ShadowType type = getType ();
            if ( type == ShadowType.outer )
            {
                final Rectangle b = shape.getBounds ();
                final Rectangle sb = new Rectangle ( b.x - width, b.y - width, b.width + width * 2, b.height + width * 2 );
                getShadow ( sb, width, opacity ).paintIcon ( g2d, sb.x, sb.y, sb.width, sb.height );
            }
            else
            {
                throw new RuntimeException ( "Inner shadow type is not supported by this shadow" );
            }
        }
    }

    /**
     * Returns cached shadow image.
     * Image is updated if some related settings have changed.
     *
     * @param bounds  shadow bounds
     * @param width   shadow width
     * @param opacity shadow opacity
     * @return cached shadow image
     */
    protected NinePatchIcon getShadow ( final Rectangle bounds, final int width, final float opacity )
    {
        final String key = getShadowKey ( bounds, width, opacity );
        if ( shadowImage == null || !CompareUtils.equals ( shadowKey, key ) )
        {
            shadowKey = key;
            shadowImage = getShadeCache ( bounds, width, opacity );
        }
        return shadowImage;
    }

    /**
     * Returns shadow image cache key.
     * It is optimized to only take bounds height and shadow width into account.
     * Bounds width is not considered because it is always sufficient and should never really affect this shadow image.
     *
     * @param b       shadow bounds
     * @param width   shadow width
     * @param opacity shadow opacity
     * @return shadow image cache key
     */
    protected static String getShadowKey ( final Rectangle b, final int width, final float opacity )
    {
        return b.height + "," + width + "," + opacity;
    }

    /**
     * Returns cached shadow image.
     *
     * @param b       shadow bounds
     * @param width   shadow width
     * @param opacity shadow opacity
     * @return cached shadow image
     */
    protected static NinePatchIcon getShadeCache ( final Rectangle b, final int width, final float opacity )
    {
        final String key = getShadowKey ( b, width, opacity );
        final WeakReference<NinePatchIcon> reference = shadowCache.get ( key );
        if ( reference == null || reference.get () == null )
        {
            // Creating shadow pattern
            final GeneralPath gp = new GeneralPath ( GeneralPath.WIND_EVEN_ODD );
            gp.moveTo ( b.x + width * 1.45, b.y + width * 1.45 );
            gp.lineTo ( b.x + b.width - width * 1.45, b.y + width * 1.45 );
            gp.lineTo ( b.x + b.width - width, b.y + b.height - width );
            gp.quadTo ( b.x + b.width / 2, b.y + b.height - width * 1.9, b.x + width, b.y + b.height - width );
            gp.closePath ();

            // Creating shadow image
            final BufferedImage shadowImage = ImageUtils.createShadeImage ( b.width, b.height, gp, width, opacity, false );

            // Creating nine-patch icon based on shadow image
            final int w = shadowImage.getWidth ();
            final int inner = width / 2;
            final NinePatchIcon ninePatchIcon = NinePatchIcon.create ( shadowImage );
            ninePatchIcon.addHorizontalStretch ( 0, width + inner, true );
            ninePatchIcon.addHorizontalStretch ( width + inner + 1, w - width - inner - 1, false );
            ninePatchIcon.addHorizontalStretch ( w - width - inner, w, true );
            ninePatchIcon.addVerticalStretch ( 0, width + inner, true );
            ninePatchIcon.addVerticalStretch ( width + inner + 1, w - width - inner - 1, false );
            ninePatchIcon.addVerticalStretch ( w - width - inner, w, true );
            ninePatchIcon.setMargin ( width );

            // Caching shadow icon
            shadowCache.put ( key, new WeakReference<NinePatchIcon> ( ninePatchIcon ) );
        }
        return shadowCache.get ( key ).get ();
    }
}