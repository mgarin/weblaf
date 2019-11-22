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

package com.alee.managers.icon;

import com.alee.api.Identifiable;
import com.alee.api.annotations.NotNull;
import com.alee.api.ui.DisabledCopySupplier;
import com.alee.api.ui.TransparentCopySupplier;
import com.alee.utils.ImageUtils;

import javax.swing.*;
import java.awt.*;

/**
 * Lazy {@link Icon} implementation.
 * It represents an {@link Icon} that will be loaded on demand from the {@link IconManager}.
 * Actual {@link Icon} is referenced through {@link #id} which is unique for all icons within {@link IconManager}.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-IconManager">How to use IconManager</a>
 * @see IconManager
 */
public final class LazyIcon implements Icon, Identifiable, DisabledCopySupplier<Icon>, TransparentCopySupplier<Icon>
{
    /**
     * Identifier of {@link Icon} in {@link IconManager}.
     */
    @NotNull
    private final String id;

    /**
     * Constructs new {@link LazyIcon}.
     *
     * @param id identifier of {@link Icon} in {@link IconManager}
     */
    public LazyIcon ( @NotNull final String id )
    {
        this.id = id;
    }

    /**
     * Returns identifier of {@link Icon} in {@link IconManager}.
     *
     * @return identifier of {@link Icon} in {@link IconManager}
     */
    @NotNull
    @Override
    public String getId ()
    {
        return id;
    }

    @Override
    public void paintIcon ( @NotNull final Component c, @NotNull final Graphics g, final int x, final int y )
    {
        getIcon ().paintIcon ( c, g, x, y );
    }

    @Override
    public int getIconWidth ()
    {
        return getIcon ().getIconWidth ();
    }

    @Override
    public int getIconHeight ()
    {
        return getIcon ().getIconHeight ();
    }

    /**
     * Returns actual {@link Icon} used by this {@link LazyIcon}.
     * Use this method wisely as it will load {@link Icon} into memory.
     *
     * @param <I> {@link Icon} type
     * @return actual {@link Icon} used by this {@link LazyIcon}
     */
    @NotNull
    public <I extends Icon> I getIcon ()
    {
        return IconManager.getIcon ( getId () );
    }

    /**
     * Returns disabled copy of original {@link Icon}.
     *
     * @return disabled copy of original {@link Icon}
     */
    @NotNull
    @Override
    public Icon createDisabledCopy ()
    {
        return ImageUtils.getDisabledCopy ( getIcon () );
    }

    /**
     * Returns semi-transparent copy of original {@link Icon}.
     *
     * @return semi-transparent copy of original {@link Icon}
     */
    @NotNull
    @Override
    public Icon createTransparentCopy ( final float opacity )
    {
        return ImageUtils.getTransparentCopy ( getIcon (), opacity );
    }

    @NotNull
    @Override
    public String toString ()
    {
        return getClass ().getCanonicalName () + "[id=" + getId () + "]";
    }
}