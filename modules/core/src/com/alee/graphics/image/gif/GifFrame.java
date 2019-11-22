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

package com.alee.graphics.image.gif;

import com.alee.api.annotations.NotNull;
import com.alee.api.ui.DisabledCopySupplier;
import com.alee.api.ui.TransparentCopySupplier;
import com.alee.utils.ImageUtils;

import java.awt.image.BufferedImage;

/**
 * Single {@link GifIcon} frame.
 *
 * @author Mikle Garin
 */
public class GifFrame implements DisabledCopySupplier<GifFrame>, TransparentCopySupplier<GifFrame>
{
    /**
     * Frame {@link BufferedImage}.
     */
    @NotNull
    public final BufferedImage bufferedImage;

    /**
     * Frame display delay.
     */
    public final int delay;

    /**
     * Constructs new {@link GifFrame}.
     *
     * @param bufferedImage frame {@link BufferedImage}
     * @param delay         frame display delay
     */
    public GifFrame ( @NotNull final BufferedImage bufferedImage, final int delay )
    {
        this.bufferedImage = bufferedImage;
        this.delay = delay;
    }

    /**
     * Returns copy of this {@link GifFrame} with {@link #bufferedImage} made look disabled.
     *
     * @return copy of this {@link GifFrame} with {@link #bufferedImage} made look disabled
     */
    @NotNull
    @Override
    public GifFrame createDisabledCopy ()
    {
        return new GifFrame ( ImageUtils.createDisabledCopy ( bufferedImage ), delay );
    }

    /**
     * Returns copy of this {@link GifFrame} with {@link #bufferedImage} made semi-transparent.
     *
     * @return copy of this {@link GifFrame} with {@link #bufferedImage} made semi-transparent
     */
    @NotNull
    @Override
    public GifFrame createTransparentCopy ( final float opacity )
    {
        return new GifFrame ( ImageUtils.createTransparentCopy ( bufferedImage, opacity ), delay );
    }
}