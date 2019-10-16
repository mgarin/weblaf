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

package com.alee.utils.swing;

import com.alee.api.annotations.NotNull;
import com.alee.api.clone.CloneBehavior;
import com.alee.api.clone.RecursiveClone;
import com.alee.api.merge.Overwriting;

import javax.swing.plaf.UIResource;
import java.awt.*;

/**
 * Simple {@link UIResource} for {@link Color}.
 * Unlike {@link javax.swing.plaf.ColorUIResource} it clearly defines merge and clone behaviors.
 *
 * @author Mikle Garin
 */
public class ColorUIResource extends Color implements UIResource, Overwriting, CloneBehavior<ColorUIResource>
{
    /**
     * Constructs new {@link ColorUIResource}.
     *
     * @param r red color
     * @param g green color
     * @param b blue color
     */
    public ColorUIResource ( final int r, final int g, final int b )
    {
        super ( r, g, b );
    }

    /**
     * Constructs new {@link ColorUIResource}.
     *
     * @param rgb color RGB
     */
    public ColorUIResource ( final int rgb )
    {
        super ( rgb );
    }

    /**
     * Constructs new {@link ColorUIResource}.
     *
     * @param r red color
     * @param g green color
     * @param b blue color
     */
    public ColorUIResource ( final float r, final float g, final float b )
    {
        super ( r, g, b );
    }

    /**
     * Constructs new {@link ColorUIResource}.
     *
     * @param color {@link Color}
     */
    public ColorUIResource ( final Color color )
    {
        super ( color.getRGB (), ( color.getRGB () & 0xFF000000 ) != 0xFF000000 );
    }

    @Override
    public boolean isOverwrite ()
    {
        return true;
    }

    @NotNull
    @Override
    public ColorUIResource clone ( @NotNull final RecursiveClone clone, final int depth )
    {
        return new ColorUIResource ( this );
    }
}