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

package com.alee.demo.skin;

import com.alee.demo.api.FeatureState;
import com.alee.demo.api.PreviewPanel;
import com.alee.laf.panel.WebPanelUI;
import com.alee.managers.style.skin.web.WebPanelPainter;
import com.alee.utils.CompareUtils;
import com.alee.utils.GraphicsUtils;
import com.alee.utils.ImageUtils;
import com.alee.utils.ninepatch.NinePatchIcon;

import java.awt.*;
import java.awt.geom.GeneralPath;
import java.awt.image.BufferedImage;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

/**
 * Custom painter with a specific shade for each preview panel.
 * This painter is not designed to honor all possible panel settings since it is not required for preview.
 *
 * @author Mikle Garin
 */

public final class PreviewPainter<E extends PreviewPanel, U extends WebPanelUI> extends WebPanelPainter<E, U>
{
    /**
     * Whether preview is for a dark style.
     */
    protected boolean dark;

    /**
     * Shade images cache.
     */
    protected static final Map<String, WeakReference<NinePatchIcon>> shadeCache = new HashMap<String, WeakReference<NinePatchIcon>> ( 4 );

    /**
     * Last shade image cache key.
     */
    protected String shadeKey;

    /**
     * Currently used shade image.
     */
    protected NinePatchIcon shadeImage;

    @Override
    protected void paintShade ( final Graphics2D g2d, final Rectangle b, final Shape borderShape )
    {
        // Painting custom shade
        getShade ( b, shadeWidth ).paintIcon ( g2d, b.x, b.y, b.width, b.height );
    }

    @Override
    protected void paintBorder ( final Graphics2D g2d, final Rectangle bounds, final Shape borderShape )
    {
        // Painting default border
        super.paintBorder ( g2d, bounds, borderShape );

        // Painting custom feature state mark
        paintFeatureState ( g2d, borderShape );
    }

    /**
     * Paints feature state mark.
     *
     * @param g2d         graphics context
     * @param borderShape border shape
     */
    protected void paintFeatureState ( final Graphics2D g2d, final Shape borderShape )
    {
        final FeatureState featureState = component.getState ();
        if ( featureState != FeatureState.common )
        {
            final Object aa = GraphicsUtils.setupAntialias ( g2d );

            final int shift = round * 2;
            final Rectangle bb = borderShape.getBounds ();
            final GeneralPath gp = new GeneralPath ( GeneralPath.WIND_EVEN_ODD );
            if ( ltr )
            {
                gp.moveTo ( bb.x, bb.y + shift );
                gp.lineTo ( bb.x + shift, bb.y );
                gp.lineTo ( bb.x + shift * 2, bb.y );
                gp.lineTo ( bb.x, bb.y + shift * 2 );
            }
            else
            {
                final int cornerX = bb.x + bb.width + 1;
                gp.moveTo ( cornerX - shift * 2, bb.y );
                gp.lineTo ( cornerX - shift, bb.y );
                gp.lineTo ( cornerX, bb.y + shift );
                gp.lineTo ( cornerX, bb.y + shift * 2 );
            }
            gp.closePath ();

            g2d.setPaint ( featureState.getColor () );
            g2d.fill ( gp );

            GraphicsUtils.restoreAntialias ( g2d, aa );
        }
    }

    /**
     * Returns cached shade image.
     * Image is updated if some related settings have changed.
     *
     * @param b          shade bounds
     * @param shadeWidth shade width
     * @return cached shade image
     */
    protected NinePatchIcon getShade ( final Rectangle b, final int shadeWidth )
    {
        final String key = getShadeKey ( b, shadeWidth );
        if ( shadeImage == null || !CompareUtils.equals ( shadeKey, key ) )
        {
            shadeKey = key;
            shadeImage = getShadeCache ( b, shadeWidth );
        }
        return shadeImage;
    }

    /**
     * Returns cached shade image.
     *
     * @param b          shade bounds
     * @param shadeWidth shade width
     * @return cached shade image
     */
    protected static NinePatchIcon getShadeCache ( final Rectangle b, final int shadeWidth )
    {
        final String key = getShadeKey ( b, shadeWidth );
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
            final BufferedImage shadeImage = ImageUtils.createShadeImage ( b.width, b.height, gp, shadeWidth, 0.8f, false );

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

    /**
     * Returns shade image cache key.
     * It is optimized to only take bounds height and shade width into account.
     * Bounds width is not considered because it is always sufficient and should never really affect this shade image.
     *
     * @param b          shade bounds
     * @param shadeWidth shade width
     * @return shade image cache key
     */
    protected static String getShadeKey ( final Rectangle b, final int shadeWidth )
    {
        return b.height + "," + shadeWidth;
    }
}