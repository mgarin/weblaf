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

package com.alee.extended.painter;

import com.alee.utils.ImageUtils;
import com.alee.utils.LafUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Texure painter.
 * This painter fills component background with a texture based on the specified image.
 *
 * @param <E> component type
 * @author Mikle Garin
 * @see DefaultPainter
 * @see Painter
 */

public class TexturePainter<E extends JComponent> extends DefaultPainter<E>
{
    /**
     * Cached texture paint.
     */
    private TexturePaint paint = null;

    /**
     * Texture type.
     */
    private TextureType textureType = null;

    /**
     * Constructs texture paint with the specified icon as a texture.
     *
     * @param icon texture icon
     */
    public TexturePainter ( ImageIcon icon )
    {
        this ( icon.getImage () );
    }

    /**
     * Constructs texture paint with the specified image as a texture.
     *
     * @param image texture image
     */
    public TexturePainter ( Image image )
    {
        this ( ImageUtils.getBufferedImage ( image ) );
    }

    /**
     * Constructs texture paint with the specified image as a texture.
     *
     * @param image texture image
     */
    public TexturePainter ( BufferedImage image )
    {
        super ();
        updatePainter ( image );
    }

    /**
     * Constructs texture paint with the specified texture type.
     *
     * @param textureType predefined texture type
     */
    public TexturePainter ( TextureType textureType )
    {
        super ();
        setTextureType ( textureType );
    }

    /**
     * Returns texture type.
     *
     * @return texture type
     */
    public TextureType getTextureType ()
    {
        return textureType;
    }

    /**
     * Sets texture type.
     *
     * @param textureType new texture type
     */
    public void setTextureType ( TextureType textureType )
    {
        this.textureType = textureType;
        updatePainter ( textureType == null ? null :
                ImageUtils.getBufferedImage ( TexturePainter.class.getResource ( "icons/textures/" + textureType + ".png" ) ) );
    }

    /**
     * Returns texture image.
     *
     * @return texture image
     */
    public BufferedImage getImage ()
    {
        return paint != null ? paint.getImage () : null;
    }

    /**
     * Sets texture image.
     *
     * @param image new texture image
     */
    public void setImage ( BufferedImage image )
    {
        updatePainter ( image );
    }

    /**
     * Updates cached texture paint.
     *
     * @param image texture image
     */
    private void updatePainter ( BufferedImage image )
    {
        this.paint = image != null ? new TexturePaint ( image, new Rectangle ( 0, 0, image.getWidth (), image.getHeight () ) ) : null;
    }

    /**
     * Paints visual data onto the component graphics.
     * Provided graphics and component are taken directly from component UI paint method.
     * Provided bounds are usually fake (zero location, component size) but in some cases it might be specified by componentUI.
     *
     * @param g2d    component graphics
     * @param bounds bounds for painter visual data
     * @param c      component to process
     */
    @Override
    public void paint ( Graphics2D g2d, Rectangle bounds, E c )
    {
        // Do not paint anything if texture paint is not set
        if ( paint != null )
        {
            // Determining actual rect to be filled (we don't need to fill invisible area)
            Rectangle r = c.getVisibleRect ().intersection ( bounds );

            // If there is anything to fill we do it
            if ( r.width > 0 && r.height > 0 )
            {
                Object old = LafUtils.setupImageQuality ( g2d );
                g2d.setPaint ( paint );
                g2d.fillRect ( r.x, r.y, r.width, r.height );
                LafUtils.restoreImageQuality ( g2d, old );
            }
        }
    }
}