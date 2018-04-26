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

import com.alee.managers.icon.IconManager;
import com.alee.painter.decoration.IDecoration;
import com.alee.utils.ImageUtils;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

import javax.swing.*;
import java.awt.image.BufferedImage;

/**
 * Image texure background.
 * Fills component shape with a texture based on the specified {@link #iconId} of an {@link Icon} from {@link IconManager}.
 *
 * @param <C> component type
 * @param <D> decoration type
 * @param <I> background type
 * @author Mikle Garin
 * @see AbstractImageTextureBackground
 */
@XStreamAlias ( "ImageTextureBackground" )
public class ImageTextureBackground<C extends JComponent, D extends IDecoration<C, D>, I extends ImageTextureBackground<C, D, I>>
        extends AbstractImageTextureBackground<C, D, I>
{
    /**
     * Identifier of an {@link Icon} from {@link IconManager}.
     */
    @XStreamAsAttribute
    protected String iconId;

    @Override
    protected boolean isPaintable ()
    {
        return iconId != null && IconManager.hasIcon ( iconId );
    }

    @Override
    protected BufferedImage getTextureImage ()
    {
        final Icon icon = IconManager.getIcon ( iconId );
        return ImageUtils.getBufferedImage ( icon );
    }
}