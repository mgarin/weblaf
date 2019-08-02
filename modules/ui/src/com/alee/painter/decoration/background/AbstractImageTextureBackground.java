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

import com.alee.api.clone.behavior.OmitOnClone;
import com.alee.api.merge.behavior.OmitOnMerge;
import com.alee.managers.style.StyleException;
import com.alee.painter.decoration.IDecoration;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Abstract image texure background.
 * Fills component shape with a texture based on the provided {@link BufferedImage}.
 * Note that in the current implementation this background can only handle static images.
 * Any animated images (like gif or apng) will be displayed as a signle static frame, usually first one.
 *
 * @param <C> component type
 * @param <D> decoration type
 * @param <I> background type
 * @author Mikle Garin
 */
public abstract class AbstractImageTextureBackground<C extends JComponent, D extends IDecoration<C, D>, I extends AbstractImageTextureBackground<C, D, I>>
        extends AbstractTextureBackground<C, D, I>
{
    /**
     * Cached texture image.
     * It is only cleared upon object destruction to optimize its usage performance.
     * If we would clean it up every time this background is deactivated it would not work well in some cases.
     */
    @OmitOnClone
    @OmitOnMerge
    protected transient BufferedImage image;

    @Override
    protected TexturePaint getTexturePaint ( final Rectangle bounds )
    {
        // Updating image cache if needed
        if ( image == null )
        {
            image = getTextureImage ();
        }

        // Ensure we retrieved image
        if ( image == null )
        {
            throw new StyleException ( "Unable to retrieve texture image" );
        }

        // Returning texture paint
        return new TexturePaint ( image, new Rectangle ( bounds.x, bounds.y, image.getWidth (), image.getHeight () ) );
    }

    /**
     * Returns {@link BufferedImage} used for {@link TexturePaint}.
     * Note that returned {@link BufferedImage} will be cached in {@link #image} field to optimize performance.
     *
     * @return {@link BufferedImage} used for {@link TexturePaint}
     */
    protected abstract BufferedImage getTextureImage ();
}