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
import com.alee.api.jdk.Objects;
import com.alee.api.merge.behavior.OmitOnMerge;
import com.alee.painter.decoration.IDecoration;
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
 * @param <C> component type
 * @param <D> decoration type
 * @param <I> shadow type
 * @author Mikle Garin
 */
@XStreamAlias ( "ExpandingShadow" )
public class ExpandingShadow<C extends JComponent, D extends IDecoration<C, D>, I extends ExpandingShadow<C, D, I>>
        extends AbstractShadow<C, D, I>
{
    /**
     * Shadow images cache.
     */
    protected static transient final Map<String, WeakReference<NinePatchIcon>> shadowCache =
            new HashMap<String, WeakReference<NinePatchIcon>> ( 4 );

    /**
     * Last shadow image cache key.
     */
    @OmitOnClone
    @OmitOnMerge
    protected transient String shadowKey;

    /**
     * Currently used shadow image.
     */
    @OmitOnClone
    @OmitOnMerge
    protected transient NinePatchIcon shadowImage;

    @Override
    public void paint ( final Graphics2D g2d, final Rectangle bounds, final C c, final D d, final Shape shape )
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
                getShadow ( width, opacity ).paintIcon ( g2d, sb.x, sb.y, sb.width, sb.height );
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
     * @param width   shadow width
     * @param opacity shadow opacity
     * @return cached shadow image
     */
    protected NinePatchIcon getShadow ( final int width, final float opacity )
    {
        final String key = getShadowKey ( width, opacity );
        if ( shadowImage == null || Objects.notEquals ( shadowKey, key ) )
        {
            final WeakReference<NinePatchIcon> reference = shadowCache.get ( key );
            if ( reference == null || reference.get () == null )
            {
                // Creating new shadow icon
                final Rectangle sb = new Rectangle ( width * 6, width * 6 );
                final NinePatchIcon ninePatchIcon = createShadowIcon ( sb, width, opacity );

                // Caching shadow icon
                shadowCache.put ( key, new WeakReference<NinePatchIcon> ( ninePatchIcon ) );
            }

            // Updating
            shadowKey = key;
            shadowImage = shadowCache.get ( key ).get ();
        }
        return shadowImage;
    }

    /**
     * Returns newly created {@link NinePatchIcon} containing stretchable shadow.
     *
     * @param bounds  icon bounds
     * @param width   shadow width
     * @param opacity shadow opacity
     * @return newly created {@link NinePatchIcon} containing stretchable shadow
     */
    protected NinePatchIcon createShadowIcon ( final Rectangle bounds, final int width, final float opacity )
    {
        // Creating shadow pattern
        final GeneralPath gp = new GeneralPath ( GeneralPath.WIND_EVEN_ODD );
        gp.moveTo ( bounds.x + width * 1.2, bounds.y + width * 1.2 );
        gp.lineTo ( bounds.x + bounds.width - width * 1.2, bounds.y + width * 1.2 );
        gp.lineTo ( bounds.x + bounds.width - width, bounds.y + bounds.height - width * 0.5 );
        gp.quadTo ( bounds.x + bounds.width / 2, bounds.y + bounds.height - width * 1.9,
                bounds.x + width, bounds.y + bounds.height - width * 0.5 );
        gp.closePath ();

        // Creating shadow image
        final BufferedImage shadowImage = ImageUtils.createShadowImage ( bounds.width, bounds.height, gp, width, opacity, false );

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
        return ninePatchIcon;
    }

    /**
     * Returns shadow image cache key.
     *
     * @param width   shadow width
     * @param opacity shadow opacity
     * @return shadow image cache key
     */
    protected String getShadowKey ( final int width, final float opacity )
    {
        return width + "," + opacity;
    }
}