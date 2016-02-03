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

package com.alee.managers.style.skin.web.data.shade;

import com.alee.managers.style.skin.web.data.decoration.IDecoration;
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
 * @author Mikle Garin
 */

@XStreamAlias ( "ExpandingShade" )
public class ExpandingShade<E extends JComponent, D extends IDecoration<E, D>, I extends ExpandingShade<E, D, I>>
        extends AbstractShade<E, D, I>
{
    /**
     * Shade images cache.
     */
    protected static final Map<String, WeakReference<NinePatchIcon>> shadeCache = new HashMap<String, WeakReference<NinePatchIcon>> ( 4 );

    /**
     * Last shade image cache key.
     */
    protected transient String shadeKey;

    /**
     * Currently used shade image.
     */
    protected transient NinePatchIcon shadeImage;

    @Override
    public void paint ( final Graphics2D g2d, final Rectangle bounds, final E c, final D d, final Shape shape )
    {
        final int width = getWidth ();
        final float transparency = getTransparency ();
        if ( width > 0 && transparency > 0f )
        {
            final ShadeType type = getType ();
            if ( type == ShadeType.outer )
            {
                final Rectangle b = shape.getBounds ();
                final Rectangle sb = new Rectangle ( b.x - width, b.y - width, b.width + width * 2, b.height + width * 2 );
                getShade ( sb, width, transparency ).paintIcon ( g2d, sb.x, sb.y, sb.width, sb.height );
            }
            else
            {
                throw new RuntimeException ( "Inner shade type is not supported by this shade" );
            }
        }
    }

    /**
     * Returns cached shade image.
     * Image is updated if some related settings have changed.
     *
     * @param bounds       shade bounds
     * @param shadeWidth   shade width
     * @param transparency shade transparency
     * @return cached shade image
     */
    protected NinePatchIcon getShade ( final Rectangle bounds, final int shadeWidth, final float transparency )
    {
        final String key = getShadeKey ( bounds, shadeWidth, transparency );
        if ( shadeImage == null || !CompareUtils.equals ( shadeKey, key ) )
        {
            shadeKey = key;
            shadeImage = getShadeCache ( bounds, shadeWidth, transparency );
        }
        return shadeImage;
    }

    /**
     * Returns shade image cache key.
     * It is optimized to only take bounds height and shade width into account.
     * Bounds width is not considered because it is always sufficient and should never really affect this shade image.
     *
     * @param b            shade bounds
     * @param shadeWidth   shade width
     * @param transparency shade transparency
     * @return shade image cache key
     */
    protected static String getShadeKey ( final Rectangle b, final int shadeWidth, final float transparency )
    {
        return b.height + "," + shadeWidth + "," + transparency;
    }

    /**
     * Returns cached shade image.
     *
     * @param b            shade bounds
     * @param shadeWidth   shade width
     * @param transparency shade transparency
     * @return cached shade image
     */
    protected static NinePatchIcon getShadeCache ( final Rectangle b, final int shadeWidth, final float transparency )
    {
        final String key = getShadeKey ( b, shadeWidth, transparency );
        final WeakReference<NinePatchIcon> reference = shadeCache.get ( key );
        if ( reference == null || reference.get () == null )
        {
            // Creating shade pattern
            final GeneralPath gp = new GeneralPath ( GeneralPath.WIND_EVEN_ODD );
            gp.moveTo ( b.x + shadeWidth * 1.45, b.y + shadeWidth * 1.45 );
            gp.lineTo ( b.x + b.width - shadeWidth * 1.45, b.y + shadeWidth * 1.45 );
            gp.lineTo ( b.x + b.width - shadeWidth, b.y + b.height - shadeWidth );
            gp.lineTo ( b.x + shadeWidth, b.y + b.height - shadeWidth );
            gp.closePath ();

            // Creating shade image
            final BufferedImage shadeImage = ImageUtils.createShadeImage ( b.width, b.height, gp, shadeWidth, transparency, false );

            // Creating nine-patch icon based on shade image
            final int w = shadeImage.getWidth ();
            final int inner = shadeWidth / 2;
            final NinePatchIcon ninePatchIcon = NinePatchIcon.create ( shadeImage );
            ninePatchIcon.addHorizontalStretch ( 0, shadeWidth + inner, true );
            ninePatchIcon.addHorizontalStretch ( shadeWidth + inner + 1, w - shadeWidth - inner - 1, false );
            ninePatchIcon.addHorizontalStretch ( w - shadeWidth - inner, w, true );
            ninePatchIcon.addVerticalStretch ( 0, shadeWidth + inner, true );
            ninePatchIcon.addVerticalStretch ( shadeWidth + inner + 1, w - shadeWidth - inner - 1, false );
            ninePatchIcon.addVerticalStretch ( w - shadeWidth - inner, w, true );
            ninePatchIcon.setMargin ( shadeWidth );

            // Caching shade icon
            shadeCache.put ( key, new WeakReference<NinePatchIcon> ( ninePatchIcon ) );
        }
        return shadeCache.get ( key ).get ();
    }
}