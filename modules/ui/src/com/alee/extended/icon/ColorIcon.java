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

package com.alee.extended.icon;

import com.alee.api.annotations.NotNull;
import com.alee.api.annotations.Nullable;
import com.alee.api.clone.behavior.OmitOnClone;
import com.alee.api.merge.behavior.OmitOnMerge;
import com.alee.painter.decoration.background.AlphaLayerBackground;
import com.alee.utils.GraphicsUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * @author Mikle Garin
 */
public class ColorIcon implements Icon
{
    /**
     * Displayed {@link Color}.
     */
    @Nullable
    protected final Color color;

    /**
     * Icon width.
     */
    protected final int width;

    /**
     * Icon height.
     */
    protected final int height;

    /**
     * Cached alpha layer texture {@link BufferedImage}.
     */
    @Nullable
    @OmitOnMerge
    @OmitOnClone
    protected transient BufferedImage alphaTexture;

    /**
     * Constructs new {@link ColorIcon}.
     *
     * @param color displayed {@link Color}
     */
    public ColorIcon ( @Nullable final Color color )
    {
        this ( color, 14, 14 );
    }

    /**
     * Constructs new {@link ColorIcon}.
     *
     * @param color  displayed {@link Color}
     * @param width  icon width
     * @param height icon height
     */
    public ColorIcon ( @Nullable final Color color, final int width, final int height )
    {
        this.color = color;
        this.width = width;
        this.height = height;
        this.alphaTexture = null;
    }

    @Override
    public void paintIcon ( @NotNull final Component c, @NotNull final Graphics g, final int x, final int y )
    {
        final Graphics2D g2d = ( Graphics2D ) g;
        GraphicsUtils.setupAntialias ( g2d );

        if ( color == null || color.getTransparency () < 255 )
        {
            if ( alphaTexture == null )
            {
                alphaTexture = AlphaLayerBackground.createAlphaBackgroundTexture (
                        new Dimension ( new Dimension ( width / 2, width / 2 ) )
                );
            }
            g2d.setPaint ( new TexturePaint (
                    alphaTexture,
                    new Rectangle ( x, y, alphaTexture.getWidth (), alphaTexture.getHeight () )
            ) );
            g2d.fillRect ( x, y, width, height );
        }
        else if ( alphaTexture != null )
        {
            alphaTexture.flush ();
            alphaTexture = null;
        }

        if ( color != null )
        {
            g2d.setPaint ( color );
            g2d.fillRect ( x, y, width, height );
        }

        // todo Use color from skin once variables are implemented
        g2d.setPaint ( new Color ( 87, 89, 91 ) );
        g2d.drawRect ( x, y, width - 1, height - 1 );
    }

    @Override
    public int getIconWidth ()
    {
        return width;
    }

    @Override
    public int getIconHeight ()
    {
        return height;
    }
}