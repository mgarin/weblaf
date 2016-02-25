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

package com.alee.managers.style.skin.web.data.background;

import com.alee.managers.style.skin.web.data.decoration.IDecoration;
import com.alee.painter.common.TextureType;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Texure background.
 * Fills component shape with a texture based on the specified preset or image.
 *
 * @param <E> component type
 * @param <D> decoration type
 * @param <I> background type
 * @author Mikle Garin
 */

@XStreamAlias ( "TextureBackground" )
public class TextureBackground<E extends JComponent, D extends IDecoration<E, D>, I extends TextureBackground<E, D, I>>
        extends AbstractBackground<E, D, I>
{
    /**
     * Texture type.
     * todo Move presets into separate library part later
     */
    @XStreamAsAttribute
    protected TextureType preset = null;

    /**
     * Cached texture paint.
     */
    protected transient TexturePaint paint = null;

    /**
     * Updates cached texture paint.
     */
    protected void updatePaint ()
    {
        final BufferedImage image = preset.getTexture ();
        paint = image != null ? new TexturePaint ( image, new Rectangle ( 0, 0, image.getWidth (), image.getHeight () ) ) : null;
    }

    @Override
    public void paint ( final Graphics2D g2d, final Rectangle bounds, final E c, final D d, final Shape shape )
    {
        // Checking texture state
        if ( preset != null && preset != TextureType.none )
        {
            // Updating cached texture paint
            if ( paint == null )
            {
                updatePaint ();
            }

            // Do not paint anything if texture paint is not set
            if ( paint != null )
            {
                g2d.setPaint ( paint );
                g2d.fill ( shape );
            }
        }
    }

    @Override
    public I merge ( final I background )
    {
        super.merge ( background );
        if ( background.preset != null )
        {
            preset = background.preset;
        }
        return ( I ) this;
    }
}