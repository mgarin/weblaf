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
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

import javax.swing.*;
import java.awt.image.BufferedImage;

/**
 * Preset texure background.
 * Fills component shape with a texture based on the specified {@link #preset}.
 *
 * @param <C> component type
 * @param <D> decoration type
 * @param <I> background type
 * @author Mikle Garin
 * @see AbstractImageTextureBackground
 */
@XStreamAlias ( "PresetTextureBackground" )
public class PresetTextureBackground<C extends JComponent, D extends IDecoration<C, D>, I extends PresetTextureBackground<C, D, I>>
        extends AbstractImageTextureBackground<C, D, I>
{
    /**
     * Texture preset type.
     */
    @XStreamAsAttribute
    protected TextureType preset;

    @Override
    protected boolean isPaintable ()
    {
        return preset != null && preset != TextureType.none;
    }

    @Override
    protected BufferedImage getTextureImage ()
    {
        return preset.getTexture ();
    }
}