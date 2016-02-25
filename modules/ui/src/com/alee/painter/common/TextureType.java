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

package com.alee.painter.common;

import com.alee.utils.ImageUtils;
import com.thoughtworks.xstream.annotations.XStreamAlias;

import java.awt.image.BufferedImage;

/**
 * This enumeration contains list of exising predefined textures for TexturePainter class.
 *
 * @author Mikle Garin
 * @see TexturePainter
 */

@XStreamAlias ( "TextureType" )
public enum TextureType
{
    /**
     * Empty texture.
     */
    none,

    /**
     * Linen cloth texture.
     */
    linen,

    /**
     * Dark linen cloth texture.
     * Pattern source: http://subtlepatterns.com/
     * Made by Jordan Pittman.
     */
    darkLinen,

    /**
     * Graphy texture.
     * Pattern source: http://subtlepatterns.com/
     * Made by We Are Pixel8: http://www.wearepixel8.com/
     */
    graphy,

    /**
     * Illusion texture.
     * Pattern source: http://subtlepatterns.com/
     * Made by Mohawk Studios: http://mohawkstudios.com/
     */
    illusion,

    /**
     * Escheresque texture.
     * Pattern source: http://subtlepatterns.com/
     * Made by Ste Patten.
     */
    escheresque,

    /**
     * Wild oliva texture.
     * Pattern source: http://subtlepatterns.com/
     * Made by Badhon Ebrahim: http://dribbble.com/graphcoder
     */
    wildOliva,

    /**
     * Dark maze texture.
     * Pattern source: http://subtlepatterns.com/
     * Made by Peax: http://www.peax-webdesign.com/
     */
    darkMaze,

    /**
     * Light maze texture.
     * Pattern source: http://subtlepatterns.com/
     * Made by Peax: http://www.peax-webdesign.com/
     */
    lightMaze,

    /**
     * Washed wall texture.
     * Pattern source: http://subtlepatterns.com/
     * Made by Sagive SEO: http://www.sagive.co.il/
     */
    washedWall,

    /**
     * Alpha layer texture.
     * Pattern source: http://subtlepatterns.com/
     * Made by Gluszczenko: http://www.gluszczenko.com/
     */
    alpha;

    /**
     * Returns texture image.
     *
     * @return texture image
     */
    public BufferedImage getTexture ()
    {
        return ImageUtils.getBufferedImage ( TextureType.class.getResource ( "icons/textures/" + this + ".png" ) );
    }
}