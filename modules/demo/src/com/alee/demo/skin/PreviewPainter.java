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

import com.alee.laf.panel.WebPanelUI;
import com.alee.managers.style.skin.web.WebPanelPainter;
import com.alee.utils.CompareUtils;
import com.alee.utils.ImageUtils;

import javax.swing.*;
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

public class PreviewPainter<E extends JPanel, U extends WebPanelUI> extends WebPanelPainter<E, U>
{
    private static final Map<String, WeakReference<BufferedImage>> shadeCache = new HashMap<String, WeakReference<BufferedImage>> ( 4 );

    private String shadeKey;
    private BufferedImage shadeImage;

    @Override
    protected void paintShade ( final Graphics2D g2d, final Rectangle b, final Shape borderShape )
    {
        g2d.drawImage ( getShade ( b, shadeWidth ), b.x, b.y, null );

        //        g2d.setPaint ( Color.WHITE );
        //        g2d.fillRect ( shadeWidth, shadeWidth, getWidth () - shadeWidth * 2, getHeight () - shadeWidth * 2 );


        //        final int sw = 25;
        //        final GeneralPath gp = new GeneralPath ( GeneralPath.WIND_EVEN_ODD );
        //        gp.moveTo ( sw * 1.5, sw * 2 );
        //        gp.lineTo ( getWidth () - sw * 1.5, sw * 2 );
        //        //                gp.quadTo ( getWidth ()/2,sw*2,getWidth ()-sw,sw );
        //        gp.lineTo ( getWidth () - sw, getHeight () - sw );
        //        gp.lineTo ( sw, getHeight () - sw );
        //        //                gp.quadTo ( getWidth () / 2, getHeight () - sw *2, sw, getHeight () - sw );
        //        gp.closePath ();
        //
        //        final BufferedImage shadeImage = ImageUtils.createShadeImage ( getWidth (), getHeight (), gp, sw, 0.8f, false );
        //        g2d.drawImage ( shadeImage, 0, 0, null );
        //
        //        g2d.setPaint ( Color.WHITE );
        //        g2d.fillRect ( sw, sw, getWidth () - sw * 2, getHeight () - sw * 2 );
    }

    private BufferedImage getShade ( final Rectangle b, final int shadeWidth )
    {
        final String key = getShadeKey ( b, shadeWidth );
        if ( shadeImage == null || !CompareUtils.equals ( shadeKey, key ) )
        {
            shadeKey = key;
            shadeImage = getShadeCache ( b, shadeWidth );
        }
        return shadeImage;
    }

    private static BufferedImage getShadeCache ( final Rectangle b, final int shadeWidth )
    {
        final String key = getShadeKey ( b, shadeWidth );
        final WeakReference<BufferedImage> reference = shadeCache.get ( key );
        if ( reference == null || reference.get () == null )
        {
            final GeneralPath gp = new GeneralPath ( GeneralPath.WIND_EVEN_ODD );
            gp.moveTo ( b.x + shadeWidth * 1.5, b.y + shadeWidth * 2 );
            gp.lineTo ( b.x + b.width - shadeWidth * 1.5, b.y + shadeWidth * 2 );
            gp.lineTo ( b.x + b.width - shadeWidth, b.y + b.height - shadeWidth );
            gp.lineTo ( b.x + shadeWidth, b.y + b.height - shadeWidth );
            gp.closePath ();

            final BufferedImage shadeImage = ImageUtils.createShadeImage ( b.width, b.height, gp, shadeWidth, 0.8f, false );
            shadeCache.put ( key, new WeakReference<BufferedImage> ( shadeImage ) );
        }
        return shadeCache.get ( key ).get ();
    }

    private static String getShadeKey ( final Rectangle b, final int shadeWidth )
    {
        return b.width + "," + b.height + "," + shadeWidth;
    }
}