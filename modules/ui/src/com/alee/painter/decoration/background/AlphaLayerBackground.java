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

package com.alee.painter.decoration.background;

import com.alee.painter.decoration.IDecoration;
import com.alee.utils.ImageUtils;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Alpha layer background.
 * Fills component shape with an alpha layer -like background.
 *
 * @param <E> component type
 * @param <D> decoration type
 * @param <I> background type
 * @author Mikle Garin
 */

@XStreamAlias ( "AlphaLayerBackground" )
public class AlphaLayerBackground<E extends JComponent, D extends IDecoration<E, D>, I extends AlphaLayerBackground<E, D, I>>
        extends AbstractTextureBackground<E, D, I>
{
    /**
     * Cells size.
     */
    @XStreamAsAttribute
    protected Dimension size;

    /**
     * Dark cell color.
     */
    @XStreamAsAttribute
    protected Color darkColor;

    /**
     * Light cell color.
     */
    @XStreamAsAttribute
    protected Color lightColor;

    @Override
    protected boolean isPaintable ()
    {
        return size != null && size.width > 0 && size.height > 0 && ( darkColor != null || lightColor != null );
    }

    @Override
    protected TexturePaint getTexturePaint ( final Rectangle bounds )
    {
        final BufferedImage image = ImageUtils.createCompatibleImage ( size.width * 2, size.height * 2, Transparency.TRANSLUCENT );
        final Graphics2D g2d = image.createGraphics ();
        if ( darkColor != null )
        {
            g2d.setPaint ( darkColor );
            g2d.fillRect ( 0, 0, size.width, size.height );
            g2d.fillRect ( size.width, size.height, size.width, size.height );
        }
        if ( lightColor != null )
        {
            g2d.setPaint ( lightColor );
            g2d.fillRect ( size.width, 0, size.width, size.height );
            g2d.fillRect ( 0, size.height, size.width, size.height );
        }
        g2d.dispose ();
        return new TexturePaint ( image, new Rectangle ( bounds.x, bounds.y, image.getWidth (), image.getHeight () ) );
    }

    @Override
    public I merge ( final I background )
    {
        super.merge ( background );
        //        if ( background.color != null )
        //        {
        //            color = background.color;
        //        }
        return ( I ) this;
    }
}